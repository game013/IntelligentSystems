/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jdt.core.dom.CatchClause;
import org.eclipse.jdt.core.dom.ConditionalExpression;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.WhileStatement;

import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public class CyclomaticComplexity extends AbstractMetric {

	/**
	 * 
	 */
	private int cyclomaticComplexity = 0;

	private int numberOfMethods = 0;

	@Override
	public boolean visit(MethodDeclaration node) {

		increaseCc();
		this.numberOfMethods++;
		return super.visit(node);
	}

	@Override
	public boolean visit(ForStatement node) {
		increaseCc();

		return super.visit(node);
	}

	@Override
	public boolean visit(EnhancedForStatement node) {
		increaseCc();
		return super.visit(node);
	}

	@Override
	public boolean visit(ConditionalExpression node) {
		increaseCc();
		return super.visit(node);
	}

	@Override
	public boolean visit(DoStatement node) {
		increaseCc();
		return super.visit(node);
	}

	@Override
	public boolean visit(WhileStatement node) {
		increaseCc();
		return super.visit(node);
	}

	@Override
	public boolean visit(SwitchCase node) {
		if (!node.isDefault())
			increaseCc();
		return super.visit(node);
	}

	@Override
	public boolean visit(Initializer node) {
		increaseCc();
		return super.visit(node);
	}

	@Override
	public boolean visit(CatchClause node) {
		increaseCc();
		return super.visit(node);
	}

	@Override
	public boolean visit(IfStatement node) {

		String expr = node.getExpression().toString().replace("&&", "&").replace("||", "|");
		int ands = StringUtils.countMatches(expr, "&");
		int ors = StringUtils.countMatches(expr, "|");

		increaseCc(ands + ors);
		increaseCc();

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
				(double) this.cyclomaticComplexity / this.numberOfMethods);
	}

	/**
	 * 
	 */
	private void increaseCc() {

		increaseCc(1);
	}

	/**
	 * @param qty
	 */
	protected void increaseCc(int qty) {

		cyclomaticComplexity += qty;
	}

}
