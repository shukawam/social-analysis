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
		List<String> tokens = tokenizer.tokenize(in).stream().map(token -> token.getPartOfSpeechLevel1())
				.collect(Collectors.toList());
		tokens.forEach(token -> System.out.println(token));
		return tokenizer.tokenize(in);
	}

}
