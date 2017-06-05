/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.dom.ASTNode;

import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public class AvgBranchingFactor extends AbstractMetric {

	private Map<ASTNode, Double> numberOfChildren;

	public AvgBranchingFactor() {

		this.numberOfChildren = new HashMap<>();
	}

	@Override
	public void preVisit(ASTNode node) {

		ASTNode parent = node.getParent();
		if (parent != null) {
			numberOfChildren.merge(parent, 1.0, (o, n) -> o + n);
		}
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

		double branchs = this.numberOfChildren.values().stream().mapToDouble(Double::valueOf).sum();
		extractionResult.setData(this.getClass().getSimpleName(), branchs / this.numberOfChildren.size());
	}

}
