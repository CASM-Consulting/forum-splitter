package org.apache.nutch.splitter.utils;

// java imports
import java.io.FileNotFoundException;
import java.io.IOException;

// logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// opennlp imports
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.util.InvalidFormatException;

public class SentenceDetector {
	
    private static final Logger LOG = LoggerFactory.getLogger(SentenceDetector.class);
	
	private static final String modelName = "ensent.bin";
	private static final SentenceDetectorME sentDetector = createModel();
	
	private static SentenceDetectorME createModel() {
		try {
			SentenceModel model = new SentenceModel(SentenceDetector.class.getClassLoader().getResourceAsStream(modelName));
			return new SentenceDetectorME(model);
		} catch (InvalidFormatException e) {
			LOG.error("Failed when attempting to instantiate sentence detector.");
			LOG.error(e.getMessage());
		} catch (FileNotFoundException e) {
			LOG.error("No sentence detector model at given location.");
		} catch (IOException e) {
			LOG.error("Failed when attempting to instantiate sentence detector.");
			LOG.error(e.getMessage());
		}
		return null;
	}
	
	/**
	 * @param text
	 * @return A list of all sentences discovered in the given text.
	 */
	public static synchronized String[] sentences(String text) {
		return sentDetector.sentDetect(text);
	}
	
	
//	public static void main(String[] args) {
//		String text = "hello how are you? I want to have a party tonight.";
//		for(String sentence : SentenceDetector.sentences(text)) {
//			System.out.println(sentence);
//		}
//	}

}
