/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.service.api;

import java.util.List;

import com.game013.codeanalyzer.domain.Code;
import com.game013.codeanalyzer.domain.Coder;
import com.game013.codeanalyzer.domain.RatingChange;
import com.game013.codeanalyzer.domain.Submission;

/**
 * @author Oscar Garavito
 *
 */
public interface ICodeRetrievalService {

	List<Coder> getBestCoders(boolean activeOnly);

	List<Submission> getCoderSubmissions(String handle);

	List<Submission> getCoderSubmissions(String handle, int from, int count);

	List<Submission> getValidCoderSubmissions(int contestId, String handle);

	List<RatingChange> getRatingChange(String handle);

	Code getCode(int contestId, long submissionId);

}
