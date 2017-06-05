/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import org.eclipse.jdt.core.dom.ASTNode;

import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public class NumberOfAstNodes extends AbstractMetric {

	private int numberOfNodes;

	@Override
	public void preVisit(ASTNode node) {

		this.numberOfNodes++;
		super.preVisit(node);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.metric.Metric#setResult(com.game013.codeanalyzer
	 * .domain.ExtractionResult)
	 */
	@Override
	public void setResult(ExtractionResult extractionResult) {

		extractionResult.setData(this.getClass().getSimpleName(), (double) this.numberOfNodes);
	}

}
