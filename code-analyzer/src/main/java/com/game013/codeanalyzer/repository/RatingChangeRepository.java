/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.game013.codeanalyzer.domain.RatingChange;

/**
 * @author Oscar Garavito
 *
 */
public interface RatingChangeRepository extends CrudRepository<RatingChange, Integer> {

	List<RatingChange> findByNewRatingLessThanEqual(long newRating);

	List<RatingChange> findByNewRatingGreaterThanEqual(long newRating);

}
