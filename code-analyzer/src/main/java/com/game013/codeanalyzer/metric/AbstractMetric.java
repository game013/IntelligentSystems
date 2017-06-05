/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;

/**
 * @author Oscar Garavito
 *
 */
public abstract class AbstractMetric extends ASTVisitor implements Metric {

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.metric.Metric#execute(org.eclipse.jdt.core.dom.
	 * CompilationUnit)
	 */
	@Override
	public void execute(CompilationUnit compilationUnit) {

		compilationUnit.accept(this);
	}

}
