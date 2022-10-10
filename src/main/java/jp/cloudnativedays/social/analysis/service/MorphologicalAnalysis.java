package jp.cloudnativedays.social.analysis.service;

import com.atilika.kuromoji.ipadic.Token;
import com.atilika.kuromoji.ipadic.Tokenizer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MorphologicalAnalysis {

	public List<Token> getToken(String in) {
		Tokenizer tokenizer = new Tokenizer();
		var tokens = tokenizer.tokenize(in).stream().filter(
				token -> "名詞".equals(token.getPartOfSpeechLevel1()) || "動詞".equals(token.getPartOfSpeechLevel1()))
				.collect(Collectors.toList());
		tokens.forEach(token -> System.out.println(token.toString()));
		return tokens;
	}

}
