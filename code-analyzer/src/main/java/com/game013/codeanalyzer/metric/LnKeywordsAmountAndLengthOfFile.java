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
public class LnKeywordsAmountAndLengthOfFile extends AbstractMetric {

	private int distinctKeywords = 0;

	private int lengthOfFile = 0;

	@Override
	public boolean visit(CompilationUnit node) {

		this.distinctKeywords = 0;
		this.lengthOfFile = node.getLength();

		String code = node.toString();
		for (String javaKeyword : GeneralConstant.JAVA_KEYWORDS) {
			this.distinctKeywords += code.contains(javaKeyword) ? 1 : 0;
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

		extractionResult.setData(this.getClass().getSimpleName(),
				Math.log((double) this.distinctKeywords / this.lengthOfFile));
	}

}
