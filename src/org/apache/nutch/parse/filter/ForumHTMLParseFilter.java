package org.apache.nutch.parse.filter;

// java imports
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// nutch/hadoop imports
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.index.filter.Post;
import org.apache.nutch.index.filter.splitters.ForumSplitterFactory;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.HtmlParseFilter;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

// html imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.DocumentFragment;

// reflections imports
import org.reflections.Reflections;

/**
 * Used as a first point of contact to parse html for forum posts. Retrieves all
 * forum splitter objects and parses the html adding meta data where a post is found.
 * appropriate.
 * 
 * @author jp242
 *
 */
public class ForumHTMLParseFilter implements HtmlParseFilter {
	
	{
		// Instantiate the forum splitter factories.
		registerFactories();
	}

	private static final List<ForumSplitterFactory> factories = new ArrayList<ForumSplitterFactory>(); 	// List of splitter factories to use when parsing web-pages.
	private Configuration conf; 					 													// Boilerplate configuration field.

	/**
	 * Adds forum meta-data to the parsed document structure.
	 */
	@Override
	public ParseResult filter(Content content, ParseResult parseResult, HTMLMetaTags metaTags, DocumentFragment doc) {
		
		// Get the parse object which stores the document meta-data.
	    Parse parse = parseResult.get(content.getUrl());
	    
	    // Get the documents meta-data object in order to add to it.
	    Metadata md = parse.getData().getParseMeta();

	    // Add any forum posts to the meta-data object of the page.
	    Document jDoc = Jsoup.parse(new String(content.getContent())); // ignore DocumentFragment as DOM navigation sucks.
	    for(ForumSplitterFactory fs : factories) {
			for(Post post : fs.create().split(jDoc)) {
				md.add(GlobalFieldValues.POST_FIELD, post.content());
			}
	    }
		
		return parseResult;
	}
	
	/**
	 * Called on instantiation to build a list of all implemented forum splitter factories.
	 */
	private static void registerFactories() {

		Reflections reflections = new Reflections("org.apache.nutch.index.filter.splitters");
		Set<Class<? extends ForumSplitterFactory>> splitters = reflections.getSubTypesOf(ForumSplitterFactory.class);
		for(Class<? extends ForumSplitterFactory> splitter : splitters) {
			ForumSplitterFactory anf = null;
			try {
				anf = splitter.newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			try {
				factories.add(anf);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Boilerplate
	 */
	@Override
	public Configuration getConf() {
		return conf;
	}

	/**
	 * Boilerplate
	 */
	@Override
	public void setConf(Configuration conf) {
		this.conf = conf;
	}

}
