package org.apache.nutch.splitter.utils;

// java imports
import java.io.IOException;

// logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// opennlp imports
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;

public class Tokeniser {
	
    private static final Logger LOG = LoggerFactory.getLogger(Tokeniser.class);
	
	private static final String modelName = "entoken.bin";
	private static final TokenizerME tokeniser = createTokeniser();
	
	private static TokenizerME createTokeniser() {
		try {
			TokenizerModel model = new TokenizerModel(Tokeniser.class.getClassLoader().getResourceAsStream(modelName));
			return new TokenizerME(model);
		} catch (IOException e) {
			LOG.error("Failed when attempting to instantiate tokeniser.");
			LOG.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * @param text
	 * @return A list of all tokens contained in the given text.
	 */
	public static synchronized String[] tokenise(String text) {
		return tokeniser.tokenize(text);
	}
	
}
