/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.NullLiteral;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.TypeLiteral;

import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public class LnLiteralAmountAndLengthOfFile extends AbstractMetric {

	private int numLiterals;

	private int lengthOfFile;

	@Override
	public boolean visit(CompilationUnit node) {

		this.lengthOfFile = node.getLength();
		return super.visit(node);
	}

	@Override
	public boolean visit(BooleanLiteral node) {

		countLiteral();
		return super.visit(node);
	}

	@Override
	public boolean visit(CharacterLiteral node) {

		this.countLiteral();
		return super.visit(node);
	}

	@Override
	public boolean visit(NullLiteral node) {

		this.countLiteral();
		return super.visit(node);
	}

	@Override
	public boolean visit(NumberLiteral node) {

		this.countLiteral();
		return super.visit(node);
	}

	@Override
	public boolean visit(StringLiteral node) {

		this.countLiteral();
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeLiteral node) {

		this.countLiteral();
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

		extractionResult.setData(this.getClass().getSimpleName(),
				Math.log((double) this.numLiterals / this.lengthOfFile));
	}

	/**
	 * @param identifier
	 */
	private void countLiteral() {

		this.numLiterals++;
	}

}
