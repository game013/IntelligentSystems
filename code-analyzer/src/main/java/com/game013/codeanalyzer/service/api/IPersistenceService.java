/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.service.api;

import com.game013.codeanalyzer.domain.Code;
import com.game013.codeanalyzer.domain.Coder;
import com.game013.codeanalyzer.domain.RatingChange;
import com.game013.codeanalyzer.domain.Submission;

/**
 * @author Oscar Garavito
 *
 */
public interface IPersistenceService {

	Code save(Code code);

	Coder save(Coder coder);

	RatingChange save(RatingChange ratingChange);

	Submission save(Submission submission);

}
