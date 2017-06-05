/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.game013.codeanalyzer.constant.RestEndpoint;
import com.game013.codeanalyzer.domain.Code;
import com.game013.codeanalyzer.domain.Coder;
import com.game013.codeanalyzer.domain.RatingChange;
import com.game013.codeanalyzer.domain.Submission;
import com.game013.codeanalyzer.dto.RatingChangeResponse;
import com.game013.codeanalyzer.dto.RatingResponse;
import com.game013.codeanalyzer.dto.SubmissionResponse;
import com.game013.codeanalyzer.service.api.ICodeRetrievalService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author Oscar Garavito
 *
 */
@Service
@Slf4j
public class CodeRetrievalService implements ICodeRetrievalService {

	private final RestTemplate restTemplate;

	@Autowired
	public CodeRetrievalService(RestTemplate restTemplate) {

		this.restTemplate = restTemplate;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * com.game013.codeanalyzer.service.api.ICodeRetrievalService#getBestCoders(
	 * )
	 */
	@Override
	public List<Coder> getBestCoders(boolean activeOnly) {

		List<Coder> response = readFile();
		if (response.isEmpty()) {
			String params = String.format("?activeOnly=%s", activeOnly);
			String url = RestEndpoint.URL + RestEndpoint.RATED_LIST_RESOURCE + params;
			log.info("Starting get data from URL: {}", url);

			ResponseEntity<RatingResponse> result = restTemplate.getForEntity(url, RatingResponse.class);
			response = result.getBody().getResult();
		}
		return response;
	}

	@Override
	public List<Submission> getCoderSubmissions(String handle) {

		return getCoderSubmission(String.format("?handle=%s", handle));
	}

	@Override
	public List<Submission> getCoderSubmissions(String handle, int from, int count) {

		return getCoderSubmission(String.format("?handle=%s&from=%d&count=%d", handle, from, count));
	}

	@Override
	public List<Submission> getValidCoderSubmissions(int contestId, String handle) {

		String params = String.format("?contestId=%d&handle=%s", contestId, handle);
		String url = RestEndpoint.URL + RestEndpoint.CONTEST_STATUS_RESOURCE + params;
		log.info("Starting get data from URL: {}", url);

		ResponseEntity<SubmissionResponse> result = restTemplate.getForEntity(url, SubmissionResponse.class);
		return result.getBody().getResult().parallelStream().filter(Submission::isValid).collect(Collectors.toList());
	}

	@Override
	public List<RatingChange> getRatingChange(String handle) {

		String params = String.format("?handle=%s", handle);
		String url = RestEndpoint.URL + RestEndpoint.RATING_RESOURCE + params;
		log.info("Starting get data from URL: {}", url);

		ResponseEntity<RatingChangeResponse> result = restTemplate.getForEntity(url, RatingChangeResponse.class);
		return result.getBody().getResult();
	}

	@Override
	public Code getCode(int contestId, long submissionId) {

		String url = String.format(RestEndpoint.CODE_URL_LAYOUT, contestId, submissionId);
		try {
			Document document = Jsoup.connect(url).userAgent("Mozilla").get();
			Elements preCodeElems = document.select("pre.program-source");
			if (preCodeElems.size() != 1) {
				throw new IllegalStateException(
						String.format("Code not found or multiple elements found %d", preCodeElems.size()));
			}
			Code code = new Code(submissionId, preCodeElems.get(0).text());
			return code;
		} catch (IOException e) {
			throw new IllegalStateException("Error getting data from codeforces", e);
		}
	}

	private List<Coder> readFile() {

		ObjectMapper jsonMapper = new ObjectMapper();
		File file = new File("/Users/mavegame/Documents/Temporal/CodeAnalysis/user-ratedList.json");
		List<Coder> bestCoders = Collections.emptyList();
		try {
			RatingResponse result = jsonMapper.readValue(file, RatingResponse.class);
			bestCoders = result.getResult();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bestCoders;
	}

	private List<Submission> getCoderSubmission(String params) {

		String url = RestEndpoint.URL + RestEndpoint.SUBMISSION_RESOURCE + params;
		log.info("Starting get data from URL: {}", url);

		ResponseEntity<SubmissionResponse> result = restTemplate.getForEntity(url, SubmissionResponse.class);
		return result.getBody().getResult();
	}

}
