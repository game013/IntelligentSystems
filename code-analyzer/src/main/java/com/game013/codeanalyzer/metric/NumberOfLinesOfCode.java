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
public class NumberOfLinesOfCode extends AbstractMetric {

	private int numberOfLines = 0;

	@Override
	public boolean visit(CompilationUnit node) {

		String code = node.toString();
		this.numberOfLines = 0;
		boolean isCurrentComment = false;
		for (String line : code.split(StringUtils.LF)) {
			String l = line.trim();

			if (l.startsWith("/*") || l.startsWith("//")) {
				isCurrentComment = true;
			}
			if (l.contains("*/") && !l.endsWith("*/")) {
				isCurrentComment = false;
			}
			if (!isCurrentComment && l.length() > 0) {
				this.numberOfLines++;
			}
			if (l.contains("*/") || l.contains("//")) {
				isCurrentComment = false;
			}
		}
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

		extractionResult.setData(this.getClass().getSimpleName(), (double) this.numberOfLines);
	}

}
