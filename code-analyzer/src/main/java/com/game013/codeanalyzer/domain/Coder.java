/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

/**
 * @author Oscar Garavito
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@Entity
public class Coder {

	@Id
	private String handle;

	private Long rating;

	private String rank;

	private Long maxRating;

	private String maxRank;

	private String country;

}
