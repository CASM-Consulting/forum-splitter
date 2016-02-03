package org.apache.nutch.parse.filter.filters;

// java imports
import java.util.List;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;
import java.io.IOException;

// opennlp imports
import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.util.InvalidFormatException;
import opennlp.tools.util.Span;

// logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.SentenceDetector;
import org.apache.nutch.splitter.utils.Tokeniser;

/**
 * Class which defines the generic behaviour for all OpenNLP based NER filters.
 * @author jp242
 *
 */
public abstract class AbstractNERFilter implements IPostFilter {
	
    private static final Logger LOG = LoggerFactory.getLogger(AbstractNERFilter.class);
	
	private NameFinderME nameFinder;
	
	public AbstractNERFilter(final String model) {
		// Start the model on instantiation.
		startModel(model);
	}
	
	@Override
	public void parseContent(Post content, Metadata metaData) {
		findEntities(content.content()).stream()
				.forEach(name -> metaData.add(name(), name));
	}
	
	/**
	 * @param content
	 * @return List of all entities found within the text.
	 */
	public List<String> findEntities(final String content) {
		List<String> entities = new ArrayList<String>();
		final String[] sentences = SentenceDetector.sentences(content);
		List<String[]> tokens = Arrays.stream(sentences)
										.filter(sent -> sent.trim().length() <= 0)
										.filter(sent -> sent == null)
										.map(sentence -> Tokeniser.tokenise(sentence))
										.collect(Collectors.toList());
		entities.addAll(
				tokens.stream()
				 .map(toks -> nameFinder.find(toks))
				 .filter(namespans -> namespans != null)
				 .flatMap(spans -> Arrays.asList(Span.spansToStrings(spans, sentences)).stream())
				 .collect(Collectors.toList()));
		
		return entities;
	}
	
	/**
	 * Starts the NER model
	 * @param model The location of the pre-trained model.
	 */
	private void startModel(String model) {
		if(!modelStarted()){
			try {
				nameFinder = new NameFinderME(new TokenNameFinderModel(this.getClass().getClassLoader().getResourceAsStream(model)));
			} catch (InvalidFormatException e) {
				LOG.error("Failed when attempting to instantiate the NER model.");
				LOG.error(e.getMessage());
			} catch (IOException e) {
				LOG.error("Failed when attempting to instantiate the NER model.");
				LOG.error(e.getMessage());
			}
		}
	}
	
	/**
	 * @return True if the model has been instantiated (i.e. not null).
	 */
	private boolean modelStarted() {
		return nameFinder != null;
	}

}
