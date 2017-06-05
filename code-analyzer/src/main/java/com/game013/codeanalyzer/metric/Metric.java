/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public interface Metric {

	/**
	 * @param compilationUnit
	 */
	void execute(CompilationUnit compilationUnit);

	/**
	 * @param extractionResult
	 */
	void setResult(ExtractionResult extractionResult);

}
