/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IExtendedModifier;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public class IdentifierAvgLength extends AbstractMetric {

	private int length;

	private int numIdentifiers;

	@Override
	public boolean visit(MethodDeclaration node) {

		if (!node.isConstructor() && !"main".equals(node.getName().getIdentifier())) {
			this.countIdentifier(node.getName().getIdentifier());
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(FieldDeclaration node) {

		for (Object f : node.fragments()) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) f;
			this.countIdentifier(fragment.getName().getIdentifier());
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(SingleVariableDeclaration node) {

		this.countIdentifier(node.getName().getIdentifier());
		return super.visit(node);
	}

	@Override
	public boolean visit(TypeDeclaration node) {

		if (!isPublic(node)) {
			this.countIdentifier(node.getName().getIdentifier());
		}
		return super.visit(node);
	}

	@Override
	public boolean visit(VariableDeclarationStatement node) {

		for (Object f : node.fragments()) {
			VariableDeclarationFragment fragment = (VariableDeclarationFragment) f;
			this.countIdentifier(fragment.getName().getIdentifier());
		}
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

		extractionResult.setData(this.getClass().getSimpleName(), (double) length / numIdentifiers);
	}

	/**
	 * @param identifier
	 */
	private void countIdentifier(String identifier) {

		this.numIdentifiers++;
		this.length += identifier.length();
	}

	/**
	 * @param node
	 * @return
	 */
	private boolean isPublic(TypeDeclaration node) {

		boolean isPublic = false;
		for (Object m : node.modifiers()) {
			IExtendedModifier em = (IExtendedModifier) m;
			if (em.isModifier()) {
				Modifier modifier = (Modifier) em;
				if (modifier.isPublic()) {
					isPublic = true;
					break;
				}
			}
		}
		return isPublic;
	}

}
