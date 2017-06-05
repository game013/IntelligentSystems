/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import org.eclipse.jdt.core.dom.CompilationUnit;

import com.game013.codeanalyzer.constant.GeneralConstant;
import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public class DistributionOfKeyWords extends AbstractMetric {

	private int keywords = 0;

	@Override
	public boolean visit(CompilationUnit node) {

		this.keywords = 0;
		String code = node.toString();
		for (String javaKeyword : GeneralConstant.JAVA_KEYWORDS) {
			this.keywords += (code.length() - code.replaceAll(javaKeyword, "").length()) / javaKeyword.length();
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

		extractionResult.setData(this.getClass().getSimpleName(), (double) this.keywords);
	}

}
