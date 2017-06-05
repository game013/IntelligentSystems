/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.game013.codeanalyzer.domain.Code;

/**
 * @author Oscar Garavito
 *
 */
public interface CodeRepository extends CrudRepository<Code, Long> {

	/**
	 * Obtains a list of code wrote by expert coders, filtering by given
	 * parameters.
	 * 
	 * @param advancedLowBound
	 *            Low bound from that advanced will be categorized as.
	 * @param limit
	 *            Maximum number of wanted registers.
	 * @return List of extracted code.
	 */
	@Query(value = "select distinct c.* " + "from rating_change rc "
			+ "inner join submission s ON rc.contest_id = s.contest_id and rc.handle = s.handle "
			+ "inner join code c ON s.id = c.submission_id " + "where rc.new_rating >= ?1 "
			+ "and s.programming_language = 'Java 7'" + "limit ?2", nativeQuery = true)
	List<Code> getExpertCode(int advancedLowBound, int limit);

	/**
	 * Obtains a list of code wrote by beginner coders, filtering by given
	 * parameters.
	 * 
	 * @param beginnerUpperBound
	 *            Upper bound from that beginner will be categorized as.
	 * @param limit
	 *            Maximum number of wanted registers.
	 * @return List of extracted code.
	 */
	@Query(value = "select distinct c.* " + "from rating_change rc "
			+ "inner join submission s ON rc.contest_id = s.contest_id and rc.handle = s.handle "
			+ "inner join code c ON s.id = c.submission_id " + "where rc.new_rating <= ?1 "
			+ "and s.programming_language = 'Java 7'" + "limit ?2", nativeQuery = true)
	List<Code> getBeginnerCode(int beginnerUpperBound, int limit);

}
