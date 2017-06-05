/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import java.util.HashSet;
import java.util.Set;

import org.eclipse.jdt.core.dom.IMethodBinding;
import org.eclipse.jdt.core.dom.ITypeBinding;
import org.eclipse.jdt.core.dom.MethodInvocation;

import com.game013.codeanalyzer.domain.ExtractionResult;

/**
 * @author Oscar Garavito
 *
 */
public class UseJavaApi extends AbstractMetric {

	private Set<String> methodInvocations = new HashSet<String>();

	@Override
	public boolean visit(MethodInvocation node) {
		IMethodBinding binding = node.resolveMethodBinding();
		count(binding);

		return super.visit(node);
	}

	private void count(IMethodBinding binding) {
		if (binding != null && isJavaApiMethod(binding)) {
			String method = getMethodName(binding);
			methodInvocations.add(method);
		}
	}

	private String getMethodName(IMethodBinding binding) {

		String argumentList = "";
		ITypeBinding[] args = binding.getParameterTypes();
		for (ITypeBinding arg : args) {
			argumentList += arg.getName();
		}
		String method = binding.getDeclaringClass().getQualifiedName() + "." + binding.getName() + "/"
				+ binding.getTypeArguments().length + "[" + argumentList + "]";
		return method;
	}

	private boolean isJavaApiMethod(IMethodBinding binding) {

		return binding.getDeclaringClass().getQualifiedName().startsWith("java");
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.metric.Metric#setResult(com.game013.codeanalyzer
	 * .domain.ExtractionResult)
	 */
	@Override
	public void setResult(ExtractionResult extractionResult) {

		extractionResult.setData(this.getClass().getSimpleName(), (double) this.methodInvocations.size());
	}

}
