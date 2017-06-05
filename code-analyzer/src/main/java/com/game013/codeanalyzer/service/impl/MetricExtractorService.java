/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.springframework.stereotype.Service;

import com.game013.codeanalyzer.constant.GeneralConstant;
import com.game013.codeanalyzer.domain.Code;
import com.game013.codeanalyzer.domain.RatingChange;
import com.game013.codeanalyzer.domain.Submission;
import com.game013.codeanalyzer.metric.MetricExecutor;
import com.game013.codeanalyzer.repository.CodeRepository;
import com.game013.codeanalyzer.repository.RatingChangeRepository;
import com.game013.codeanalyzer.repository.SubmissionRepository;
import com.game013.codeanalyzer.service.api.IMetricExtractorService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Oscar Garavito
 *
 */
@Service
@Slf4j
public class MetricExtractorService implements IMetricExtractorService {

	private final RatingChangeRepository ratingChangeRepository;

	private final SubmissionRepository submissionRepository;

	private final CodeRepository codeRepository;

	public MetricExtractorService(RatingChangeRepository ratingChangeRepository,
			SubmissionRepository submissionRepository, CodeRepository codeRepository) {

		this.ratingChangeRepository = ratingChangeRepository;
		this.submissionRepository = submissionRepository;
		this.codeRepository = codeRepository;
	}

	/*
	 * (non-Javadoc)
	 * @see com.game013.codeanalyzer.service.api.IMetricExtractorService#
	 * extractMetrics()
	 */
	@Override
	public void extractMetrics() {

		log.trace("Extracting metrics from files");
		// this.extractDataFromToyExample();
		this.extractDataFromReal();
	}

	protected void extractDataFromToyExample() {

		List<RatingChange> ratingChanges = this.ratingChangeRepository
				.findByNewRatingGreaterThanEqual(GeneralConstant.LOWER_EXPERT_RATING);
		log.info("Found {} expert rating, selecting first", ratingChanges.size());
		if (!ratingChanges.isEmpty()) {
			RatingChange ratingChange = ratingChanges.get(224);
			List<Submission> submissions = this.submissionRepository.findByContestIdAndHandleAndProgrammingLanguage(
					ratingChange.getContestId(), ratingChange.getHandle(), Submission.JAVA_8);
			log.info("Found {} submissions for contest {} and handle {}, selecting first", submissions.size(),
					ratingChange.getContestId(), ratingChange.getHandle());

			if (!submissions.isEmpty()) {
				Submission submission = submissions.get(0);
				Code code = this.codeRepository.findOne(submission.getId());

				String fileName = writeFileCode(code);
				parseFile(fileName, 1);
			}
		}
	}

	protected void extractDataFromReal() {

		List<Code> expertCodes = codeRepository.getExpertCode(GeneralConstant.LOWER_EXPERT_RATING, 500);
		for (Code code : expertCodes) {
			String fileName = writeFileCode(code);
			parseFile(fileName, 1);
		}
		List<Code> beginnerCodes = codeRepository.getBeginnerCode(GeneralConstant.UPPER_BEGINNER_RATING, 500);
		for (Code code : beginnerCodes) {
			String fileName = writeFileCode(code);
			parseFile(fileName, 0);
		}
	}

	private String writeFileCode(Code code) {

		String fileName = String.format("/tmp/code-analyzer/src/java_7_%s.java",
				RandomStringUtils.randomAlphanumeric(10));

		try {
			log.info("Writing Java file named [{}]", fileName);
			log.info("Code:**************\n{}", code.getCode());
			File file = new File(fileName);
			FileUtils.write(file, code.getCode(), Charset.defaultCharset());
		} catch (IOException e) {
			throw new IllegalStateException("Error writing Java file", e);
		}

		return fileName;
	}

	private void parseFile(String fileName, int type) {

		log.info("Parsing file [{}]", fileName);
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setBindingsRecovery(true);
		MetricExecutor metricExecutor = new MetricExecutor(type);

		Map<String, String> options = JavaCore.getOptions();
		JavaCore.setComplianceOptions(JavaCore.VERSION_1_8, options);
		parser.setCompilerOptions(options);
		parser.setEnvironment(null, new String[] { GeneralConstant.SRC_FOLDER_PATH }, null, true);
		parser.createASTs(new String[] { fileName }, null, new String[0], metricExecutor, null);
	}

}
