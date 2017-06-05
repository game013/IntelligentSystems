/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.game013.codeanalyzer.domain.RatingChange;

import lombok.Data;

/**
 * @author Oscar Garavito
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RatingChangeResponse {

	private String status;

	private List<RatingChange> result;

}
