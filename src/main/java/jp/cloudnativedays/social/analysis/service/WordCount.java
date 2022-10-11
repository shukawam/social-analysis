package jp.cloudnativedays.social.analysis.service;

import com.atilika.kuromoji.ipadic.Token;
import jp.cloudnativedays.social.analysis.configuration.SentimentLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.*;
import static java.util.function.Function.*;

/**
 * @author shukawam
 */
@Service
public class WordCount {

	private static final Logger logger = LoggerFactory.getLogger(SentimentLoader.class);

	private final MorphologicalAnalysis morphologicalAnalysis;

	public WordCount(MorphologicalAnalysis morphologicalAnalysis) {
		this.morphologicalAnalysis = morphologicalAnalysis;
	}

	public Map<String, Long> getWordCountResult(String in) {
		List<Token> tokenList = morphologicalAnalysis.getToken(in);
		List<String> surfaceList = tokenList.stream().map(token -> token.getSurface()).collect(toList());
		Map<String, Long> wordCountResult = surfaceList.stream().collect(groupingBy(identity(), counting()));
		wordCountResult.forEach((k, v) -> logger.info(String.format("%s: %s", k, v)));
		return surfaceList.stream().collect(groupingBy(identity(), counting()));
	}

}
