/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.game013.codeanalyzer.constant.GeneralConstant;

import lombok.Data;

/**
 * @author Oscar Garavito
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class RatingChange {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	private int contestId;

	private String handle;

	private long oldRating;

	private long newRating;

	private int rank;

	public boolean isBeginner() {

		return this.newRating > 0 && this.newRating <= GeneralConstant.UPPER_BEGINNER_RATING;
	}

	public boolean isExpert() {

		return this.newRating > GeneralConstant.LOWER_EXPERT_RATING;
	}

	public boolean isUseful() {

		return this.isBeginner() || this.isExpert();
	}

}
