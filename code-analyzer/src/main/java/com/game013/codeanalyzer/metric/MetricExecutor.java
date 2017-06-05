/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.metric;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
import java.util.List;
import java.util.StringJoiner;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.FileASTRequestor;

import com.game013.codeanalyzer.domain.ExtractionResult;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Oscar Garavito
 *
 */
@Slf4j
public class MetricExecutor extends FileASTRequestor {

	/**
	 * 
	 */
	List<Class<? extends Metric>> metricClasses;

	private final int type;

	/**
	 * 
	 */
	public MetricExecutor(int type) {

		metricClasses = Arrays.asList(AvgBranchingFactor.class, AvgLineLength.class, AvgMethodsByClass.class,
				CyclomaticComplexity.class, DistributionOfKeyWords.class, IdentifierAvgLength.class,
				LnKeywordsAmountAndLengthOfFile.class, LnLiteralAmountAndLengthOfFile.class, MaxDepthNestedBlocks.class,
				NumberOfAstNodes.class, NumberOfLinesOfCode.class, UseJavaApi.class);
		this.type = type;
	}

	@Override
	public void acceptAST(String sourceFilePath, CompilationUnit ast) {

		log.info("Processing file [{}]", sourceFilePath);
		ExtractionResult extractionResult = new ExtractionResult();

		for (Class<? extends Metric> metricClass : this.metricClasses) {
			executeMetric(metricClass, ast, extractionResult);
		}

		log.info("Calculated metrics: {}", extractionResult);
		writeFile(extractionResult);
	}

	/**
	 * @param metricClass
	 * @param ast
	 * @param extractionResult
	 */
	private void executeMetric(Class<? extends Metric> metricClass, CompilationUnit ast,
			ExtractionResult extractionResult) {

		try {
			Metric metric = metricClass.newInstance();
			metric.execute(ast);
			metric.setResult(extractionResult);
		} catch (InstantiationException | IllegalAccessException e) {
			log.error("Error instantiating metric class", e);
		}
	}

	protected void writeFile(ExtractionResult extractionResult) {

		try {
			StringJoiner joiner = new StringJoiner(";");
			joiner.add(Integer.toString(this.type));
			for (Class<? extends Metric> clazz : this.metricClasses) {
				joiner.add(Double.toString(extractionResult.getData(clazz.getSimpleName())));
			}
			Files.write(Paths.get("/tmp/code-analyzer/metrics/code-metrics-codeforces.csv"), Arrays.asList(joiner.toString()),
					StandardOpenOption.APPEND);
			log.info("File data appended");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

}
