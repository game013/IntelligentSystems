/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.game013.codeanalyzer.domain.Code;
import com.game013.codeanalyzer.domain.Coder;
import com.game013.codeanalyzer.domain.RatingChange;
import com.game013.codeanalyzer.domain.Submission;
import com.game013.codeanalyzer.repository.CodeRepository;
import com.game013.codeanalyzer.repository.CoderRepository;
import com.game013.codeanalyzer.repository.RatingChangeRepository;
import com.game013.codeanalyzer.repository.SubmissionRepository;
import com.game013.codeanalyzer.service.api.IPersistenceService;

/**
 * @author Oscar Garavito
 *
 */
@Service
public class PersistenceService implements IPersistenceService {

	private final CodeRepository codeRepository;

	private final CoderRepository coderRepository;

	private final RatingChangeRepository ratingChangeRepository;

	private final SubmissionRepository submissionRepository;

	@Autowired
	public PersistenceService(CodeRepository codeRepository, CoderRepository coderRepository,
			RatingChangeRepository ratingChangeRepository, SubmissionRepository submissionRepository) {

		this.codeRepository = codeRepository;
		this.coderRepository = coderRepository;
		this.ratingChangeRepository = ratingChangeRepository;
		this.submissionRepository = submissionRepository;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.service.api.IPersistenceService#save(com.game013
	 * .codeanalyzer.domain.Code)
	 */
	@Override
	public Code save(Code code) {

		return this.codeRepository.save(code);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.service.api.IPersistenceService#save(com.game013
	 * .codeanalyzer.domain.Coder)
	 */
	@Override
	public Coder save(Coder coder) {

		return this.coderRepository.save(coder);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.service.api.IPersistenceService#save(com.game013
	 * .codeanalyzer.domain.RatingChange)
	 */
	@Override
	public RatingChange save(RatingChange ratingChange) {

		return this.ratingChangeRepository.save(ratingChange);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.service.api.IPersistenceService#save(com.game013
	 * .codeanalyzer.domain.Submission)
	 */
	@Override
	public Submission save(Submission submission) {

		return this.submissionRepository.save(submission);
	}

}
