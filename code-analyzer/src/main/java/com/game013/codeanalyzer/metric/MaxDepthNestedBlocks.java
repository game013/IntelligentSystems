/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.eclipse.jdt.core.dom.ASTNode;
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
public class MaxDepthNestedBlocks extends AbstractMetric {

	private Map<ASTNode, Integer> nodeDepth = new HashMap<>();

	private Set<ASTNode> nodesToBeUpdated = new HashSet<>();

	private static Set<Class<? extends ASTNode>> blockNodeTypes = new HashSet<>();

	static {
		blockNodeTypes.add(MethodDeclaration.class);
		blockNodeTypes.add(ForStatement.class);
		blockNodeTypes.add(EnhancedForStatement.class);
		blockNodeTypes.add(ConditionalExpression.class);
		blockNodeTypes.add(DoStatement.class);
		blockNodeTypes.add(WhileStatement.class);
		blockNodeTypes.add(SwitchCase.class);
		blockNodeTypes.add(CatchClause.class);
		blockNodeTypes.add(IfStatement.class);
		blockNodeTypes.add(Initializer.class);
	}

	@Override
	public void preVisit(ASTNode node) {

		if (isBlockNode(node)) {
			this.nodeDepth.put(node, this.nodesToBeUpdated.size());
			this.nodesToBeUpdated.add(node);
		}
		super.preVisit(node);
	}

	@Override
	public void postVisit(ASTNode node) {

		this.nodesToBeUpdated.remove(node);
		super.postVisit(node);
	}

	private boolean isBlockNode(ASTNode node) {

		return blockNodeTypes.contains(node.getClass());
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.metric.Metric#setResult(com.game013.codeanalyzer
	 * .domain.ExtractionResult)
	 */
	@Override
	public void setResult(ExtractionResult extractionResult) {

		Comparator<? super Entry<ASTNode, Integer>> maxValueComparator = (entry1, entry2) -> entry1.getValue()
				.compareTo(entry2.getValue());
		Optional<Entry<ASTNode, Integer>> maxValue = nodeDepth.entrySet().stream().max(maxValueComparator);
		extractionResult.setData(this.getClass().getSimpleName(), maxValue.map(e -> (double) e.getValue()).orElse(0.0));
	}

}
