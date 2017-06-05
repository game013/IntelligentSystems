/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.CompilationUnit;

import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public class AvgLineLength extends AbstractMetric {

	private int numberOfLines = 0;

	private int lengthOfFile = 0;

	@Override
	public boolean visit(CompilationUnit node) {

		this.lengthOfFile = node.getLength();
		String code = node.toString();
		this.numberOfLines = code.split(StringUtils.LF).length;
		System.out.println("Number of lines: " + this.numberOfLines);

		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.metric.Metric#setResult(com.game013.codeanalyzer
	 * .domain.ExtractionResult)
	 */
	@Override
	public void setResult(ExtractionResult extractionResult) {

		extractionResult.setData(this.getClass().getSimpleName(), (double) this.lengthOfFile / this.numberOfLines);
	}

}
