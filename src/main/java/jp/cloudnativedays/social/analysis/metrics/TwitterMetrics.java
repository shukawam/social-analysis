package jp.cloudnativedays.social.analysis.metrics;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.search.MeterNotFoundException;
import jp.cloudnativedays.social.analysis.model.TweetData;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class TwitterMetrics {

	private final MeterRegistry meterRegistry;

	private static final String METRICS_PREFIX = "social.twitter.";

	private static final String QUERY_STRING = "queryString";

	private static final String USER_NAME = "screenName";

	private static final String TWEET_ID = "tweetId";

	private final Map<String, Integer> sentimentGaugeCache = new HashMap<>();

	private final Map<String, Integer> retweetGaugeCache = new HashMap<>();

	private final Map<String, Integer> favoriteGaugeCache = new HashMap<>();

	private final Map<String, Integer> wordGaugeCache = new HashMap<>();

	public TwitterMetrics(MeterRegistry meterRegistry) {
		this.meterRegistry = meterRegistry;
	}

	public void setMetrics(TweetData tweetData) {
		setSentimentMetrics(tweetData);
		setRetweetCounts(tweetData);
		setFavoriteCounts(tweetData);
	}

	public void setSentimentMetrics(TweetData tweetData) {
		if (!isSentimentSet(tweetData)) {
			sentimentGaugeCache.put(tweetData.getTweetId(), tweetData.getSentimentScore());
			meterRegistry.gauge(METRICS_PREFIX + "sentiment",
					Tags.of(Tag.of(QUERY_STRING, tweetData.getQueryString()), Tag.of(TWEET_ID, tweetData.getTweetId()),
							Tag.of(USER_NAME, tweetData.getUsername())),
					sentimentGaugeCache, g -> g.get(tweetData.getTweetId()));
		}
	}

	public boolean isSentimentSet(TweetData tweetData) {
		try {
			meterRegistry.get(METRICS_PREFIX + "sentiment").tag(TWEET_ID, tweetData.getTweetId()).meters();
		}
		catch (MeterNotFoundException e) {
			return false;
		}
		return true;
	}

	public void setRetweetCounts(TweetData tweetData) {
		retweetGaugeCache.put(tweetData.getTweetId(), tweetData.getRetweetCount());
		meterRegistry.gauge(METRICS_PREFIX + "retweets",
				Tags.of(Tag.of(QUERY_STRING, tweetData.getQueryString()), Tag.of(TWEET_ID, tweetData.getTweetId()),
						Tag.of(USER_NAME, tweetData.getUsername())),
				retweetGaugeCache, g -> g.get(tweetData.getTweetId()));
	}

	public void setFavoriteCounts(TweetData tweetData) {
		favoriteGaugeCache.put(tweetData.getTweetId(), tweetData.getFavoriteCount());
		meterRegistry.gauge(METRICS_PREFIX + "favorites",
				Tags.of(Tag.of(QUERY_STRING, tweetData.getQueryString()), Tag.of(TWEET_ID, tweetData.getTweetId()),
						Tag.of(USER_NAME, tweetData.getUsername())),
				favoriteGaugeCache, g -> g.get(tweetData.getTweetId()));
	}

	public void setWordCounts(Map<String, Integer> words) {
		wordGaugeCache.clear();
		words.forEach((k, v) -> {
			wordGaugeCache.put(k, v);
			meterRegistry.gauge(METRICS_PREFIX + "words", Tags.of(Tag.of("words", k)), wordGaugeCache, g -> g.get(k));
		});
	}

	public void setExecutionTime(long i) {
		meterRegistry.gauge(METRICS_PREFIX + "query.time.msec", i);
	}

}
