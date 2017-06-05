/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.game013.codeanalyzer.domain.Submission;

import lombok.Data;

/**
 * @author Oscar Garavito
 *
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class SubmissionResponse {

	private String status;

	private List<Submission> result;

}
