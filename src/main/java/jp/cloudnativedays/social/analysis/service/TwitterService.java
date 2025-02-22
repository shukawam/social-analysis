package jp.cloudnativedays.social.analysis.service;

import jp.cloudnativedays.social.analysis.client.TwitterClient;
import jp.cloudnativedays.social.analysis.metrics.TwitterMetrics;
import jp.cloudnativedays.social.analysis.model.TweetData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.TwitterException;

@Service
public class TwitterService {

	private static final Logger logger = LoggerFactory.getLogger(TwitterService.class);

	private final Sentiment sentiment;

	private final TwitterMetrics twitterMetrics;

	private final String[] queryStrings;

	private final TwitterClient twitterClient;

	private final WordCount wordCount;

	public TwitterService(Sentiment sentiment, TwitterMetrics twitterMetrics, TwitterClient twitterClient,
			@Value("${twitter.query}") String[] queryStrings, WordCount wordCount) {
		this.sentiment = sentiment;
		this.twitterMetrics = twitterMetrics;
		this.twitterClient = twitterClient;
		this.queryStrings = queryStrings;
		this.wordCount = wordCount;
	}

	@NewSpan
	public void searchTwitterAndSetMetrics() throws TwitterException {

		long start = System.currentTimeMillis();
		StringBuilder tweetsAsStr = new StringBuilder();
		long fromTime = System.currentTimeMillis() / 1000L - 3600;

		for (String queryString : queryStrings) {
			Query query = new Query(queryString + " -filter:retweets");
			boolean hasNext = true;

			while (hasNext) {

				QueryResult queryResult = twitterClient.search(query);
				hasNext = queryResult.hasNext();
				query = queryResult.nextQuery();

				logger.info("Performed Twitter Query : QueryString '" + queryString + "': "
						+ queryResult.getTweets().size() + " items found " + ": hasNextPage is " + hasNext);

				for (Status status : queryResult.getTweets()) {
					if (status.isRetweet()) {
						continue;
					}
					TweetData tweetData = new TweetData(Long.toString(status.getId()), queryString,
							status.getUser().getScreenName());
					tweetData.setSentimentScore(0);
					tweetData.setRetweetCount(status.getRetweetCount());
					tweetData.setFavoriteCount(status.getFavoriteCount());

					if (!status.isRetweet() && !twitterMetrics.isSentimentSet(tweetData)) {
						String tweetTxt = status.getText();
						if (status.getCreatedAt().getTime() / 1000L > fromTime) {
							tweetsAsStr.append(tweetTxt).append(" ");
						}
						if (status.getLang().equals("ja")) {
							logger.debug("Sentiment Check on tweet : " + tweetTxt);
							tweetData.setSentimentScore(sentiment.getSentimentScoreFromSentence(tweetTxt));
						}
					}
					twitterMetrics.setMetrics(tweetData);
				}
			}
		}
		twitterMetrics.setWordCounts(wordCount.countWord(tweetsAsStr.toString()));
		twitterMetrics.setExecutionTime(System.currentTimeMillis() - start);
	}

}
