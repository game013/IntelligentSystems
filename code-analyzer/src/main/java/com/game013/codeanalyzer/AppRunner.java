/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.game013.codeanalyzer.service.api.IExtractorServiceDelegate;
import com.game013.codeanalyzer.service.api.IMetricExtractorService;

/**
 * @author Oscar Garavito
 *
 */
@Component
public class AppRunner implements CommandLineRunner {

	private final IExtractorServiceDelegate extractorServiceDelagate;

	private final IMetricExtractorService metricExtractorService;

	public AppRunner(IExtractorServiceDelegate extractorServiceDelagate,
			IMetricExtractorService metricExtractorService) {

		this.extractorServiceDelagate = extractorServiceDelagate;
		this.metricExtractorService = metricExtractorService;
	}

	/*
	 * (non-Javadoc)
	 * @see org.springframework.boot.CommandLineRunner#run(java.lang.String[])
	 */
	@Override
	public void run(String... arg0) throws Exception {

//		 this.extractorServiceDelagate.printStatistics();
		this.metricExtractorService.extractMetrics();
	}

}
