package jp.cloudnativedays.social.analysis.service;

import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author shukawam
 */
@Service
public class WordCount {

	private final MorphologicalAnalysis morphologicalAnalysis;

	public WordCount(MorphologicalAnalysis morphologicalAnalysis) {
		this.morphologicalAnalysis = morphologicalAnalysis;
	}

	@NewSpan
	public Map<String, Long> getWordCountResultMap(String in) {
		return morphologicalAnalysis.getToken(in).stream().map(t -> t.getSurface()).collect(Collectors.toList())
				.stream().collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
	}

}
