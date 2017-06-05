/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game013.codeanalyzer.domain.Code;
import com.game013.codeanalyzer.domain.Coder;
import com.game013.codeanalyzer.domain.RatingChange;
import com.game013.codeanalyzer.domain.Submission;
import com.game013.codeanalyzer.service.api.ICodeRetrievalService;
import com.game013.codeanalyzer.service.api.IExtractorServiceDelegate;
import com.game013.codeanalyzer.service.api.IPersistenceService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Oscar Garavito
 *
 */
@Service
@Slf4j
public class ExtractorServiceDelegate implements IExtractorServiceDelegate {

	private final ICodeRetrievalService codeRetrievalService;

	private final IPersistenceService persistenceService;

	@Autowired
	public ExtractorServiceDelegate(ICodeRetrievalService codeRetrievalService,
			IPersistenceService persistenceService) {

		this.codeRetrievalService = codeRetrievalService;
		this.persistenceService = persistenceService;
	}
	
	/*
	 * (non-Javadoc)
	 * @see com.game013.codeanalyzer.service.api.IExtractorServiceDelegate#
	 * printStatistics()
	 */
	/*
	 * Executed from 0 to 399 best coders. Total 10587 coders.
	 * Also executed from 5_000 to 5_999 best coders.
	 * Also executed from 9_000 to 9_599 best coders.
	 */
	@Override
	public void printStatistics() {

		log.info("Getting list of best coders.");
		List<Coder> bestCoders = codeRetrievalService.getBestCoders(true);
		log.info("Returned first {} best coders that are active.", bestCoders.size());

		int starter = 9_350;
		int length = 250;

		for (int i = starter; i < starter + length; i++) {

			Coder coder = bestCoders.get(i);
			log.info("Code of: {}", coder.getHandle());
			persistenceService.save(coder);

			List<RatingChange> response = codeRetrievalService.getRatingChange(coder.getHandle());
			List<RatingChange> ratingChanges = response.parallelStream().filter(RatingChange::isUseful)
					.collect(Collectors.toList());
			log.info("Found {} rating changes that are useful ratings", ratingChanges.size());

			for (RatingChange rc : ratingChanges) {
				List<Submission> validSubmissions = codeRetrievalService.getValidCoderSubmissions(rc.getContestId(),
						rc.getHandle());
				log.info("Found {} valid submissions for contest {}", validSubmissions.size(), rc.getContestId());

				if (!validSubmissions.isEmpty()) {
					persistenceService.save(rc);
				}

				for (Submission submission : validSubmissions) {
					Code code = codeRetrievalService.getCode(submission.getContestId(), submission.getId());

					submission.setHandle(rc.getHandle());
					persistenceService.save(submission);
					persistenceService.save(code);
				}
			}
		}
		log.info("Process finished");
	}

	public void printStatisticsOld() {

		log.info("Getting list of best coders.");
		List<Coder> bestCoders = codeRetrievalService.getBestCoders(true);
		log.info("Returned first {} best coders that are active.", bestCoders.size());

		long validCoders = 0;
		int starter = 0;
		int length = 100;

		for (int i = starter; i < starter + length; i++) {
			Coder coder = bestCoders.get(i);
			if (getIsValid(coder.getHandle(), i)) {
				validCoders++;
			}
		}
		log.info("Found {} valid coders to study.", validCoders);
	}

	private boolean getIsValid(String handle, int index) {

		log.info("Processing coder {} from best coders", index + 1);
		List<Submission> submissions = codeRetrievalService.getCoderSubmissions(handle, 1, 100);
		log.info("Obtained {} submissions", submissions.size());
		return submissions.stream().filter(Submission::isValid).count() > 0;
	}

}
