/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public class AvgMethodsByClass extends AbstractMetric {

	private int numberOfMethods;

	private int numberOfClasses;

	@Override
	public boolean visit(MethodDeclaration node) {

		this.numberOfMethods++;
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {

		this.numberOfClasses++;
		return super.visit(node);
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.metric.Metric#setResult(com.game013.codeanalyzer
	 * .domain.ExtractionResult)
	 */
	@Override
	public void setResult(ExtractionResult extractionResult) {

		extractionResult.setData(this.getClass().getSimpleName(), (double) this.numberOfMethods / this.numberOfClasses);
	}

}
