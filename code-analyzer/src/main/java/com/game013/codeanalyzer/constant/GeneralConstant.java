/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.constant;

import java.util.Arrays;
import java.util.List;

/**
 * @author Oscar Garavito
 *
 */
public class GeneralConstant {

	public static final int LOWER_EXPERT_RATING = 2300;

	public static final int UPPER_BEGINNER_RATING = 1900;

	public static final String SRC_FOLDER_PATH = "/tmp/code-analyzer/src/";

	public static final List<String> JAVA_KEYWORDS = Arrays.asList("abstract", "continue", "for", "new", "switch",
			"assert", "default", "goto", "package", "synchronized", "boolean", "do", "if", "private", "this", "break",
			"double", "implements", "protected", "throw", "byte", "else", "import", "public", "throws", "case", "enum",
			"instanceof", "return", "transient", "catch", "extends", "int", "short", "try", "char", "final",
			"interface", "static", "void", "class", "finally", "long", "strictfp", "volatile", "const", "float",
			"native", "super", "while");

	private GeneralConstant() {

	}

}
