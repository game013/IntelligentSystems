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
public class Submission {

	public static final String OK_VEREDICT = "OK";

	public static final String JAVA_LANG = "Java";

	public static final String JAVA_8 = "Java 8";

	public static final String JAVA_7 = "Java 7";

	public static final String CPP_LANG = "C++";

	@Id
	private long id;

	private int contestId;

	private String handle;

	private String programmingLanguage;

	private String verdict;

	public boolean isValid() {

		return OK_VEREDICT.equals(this.verdict)
				&& (this.programmingLanguage.contains(CPP_LANG) || this.programmingLanguage.contains(JAVA_LANG));
	}

}
