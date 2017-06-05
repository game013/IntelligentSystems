/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.game013.codeanalyzer.domain.Submission;

/**
 * @author Oscar Garavito
 *
 */
public interface SubmissionRepository extends CrudRepository<Submission, Long> {

	List<Submission> findByContestIdAndHandleAndProgrammingLanguage(int contestId, String handle,
			String programmingLanguage);

}
