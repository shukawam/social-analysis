package jp.cloudnativedays.social.analysis.model;

import java.util.Map;

public class TweetData {

	private String tweetId;

	private String queryString;

	private String username;

	private Integer sentimentScore;

	private Integer retweetCount;

	private Integer favoriteCount;

	private Map<String, Long> wordCount;

	public TweetData(String tweetId, String queryString, String username) {
		this.tweetId = tweetId;
		this.queryString = queryString;
		this.username = username;
	}

	public String getTweetId() {
		return tweetId;
	}

	public void setTweetId(String tweetId) {
		this.tweetId = tweetId;
	}

	public String getQueryString() {
		return queryString;
	}

	public void setQueryString(String queryString) {
		this.queryString = queryString;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Integer getSentimentScore() {
		return sentimentScore;
	}

	public void setSentimentScore(Integer sentimentScore) {
		this.sentimentScore = sentimentScore;
	}

	public Integer getRetweetCount() {
		return retweetCount;
	}

	public void setRetweetCount(Integer retweetCount) {
		this.retweetCount = retweetCount;
	}

	public Integer getFavoriteCount() {
		return favoriteCount;
	}

	public void setFavoriteCount(Integer favoriteCount) {
		this.favoriteCount = favoriteCount;
	}

	public Map<String, Long> getWordCount() {
		return wordCount;
	}

	public void setWordCount(Map<String, Long> wordCount) {
		this.wordCount = wordCount;
	}

}
