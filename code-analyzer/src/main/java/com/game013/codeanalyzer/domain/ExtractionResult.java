/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.domain;

import java.util.HashMap;
import java.util.Map;

import lombok.ToString;

/**
 * @author Oscar Garavito
 *
 */
@ToString
public class ExtractionResult {

	private Map<String, Double> dataSummary;

	public ExtractionResult() {

		this.dataSummary = new HashMap<>();
	}

	public void setData(String metric, Double value) {

		this.dataSummary.put(metric, value);
	}

	public Double getData(String metric) {

		return this.dataSummary.get(metric);
	}

}
