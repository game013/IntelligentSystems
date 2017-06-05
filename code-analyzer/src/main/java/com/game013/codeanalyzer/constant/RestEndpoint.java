/**
 * COPYRIGHT (C) 2015. All Rights Reserved.
 */
package com.game013.codeanalyzer.constant;

/**
 * @author Oscar Garavito
 *
 */
public class RestEndpoint {

	public static final String URL = "http://codeforces.com/api/";

	public static final String RATED_LIST_RESOURCE = "user.ratedList";

	public static final String SUBMISSION_RESOURCE = "user.status";

	public static final String CONTEST_STATUS_RESOURCE = "contest.status";

	public static final String RATING_RESOURCE = "user.rating";
	
	public static final String CODE_URL_LAYOUT = "http://codeforces.com/contest/%d/submission/%d";

	private RestEndpoint() {

	}

}
