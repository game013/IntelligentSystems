/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Oscar Garavito
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Code {

	@Id
	private long submissionId;

	private String code;

}
