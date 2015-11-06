package org.apache.nutch.parse.filter;

// java imports
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

// nutch/hadoop imports
import org.apache.hadoop.conf.Configuration;
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

//commons imports
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.nutch.index.filter.Post;
import org.apache.nutch.index.filter.splitter.ForumSplitterFactory;

/**
 * Used as a first point of contact to parse html for forum posts. Retrieves all
 * forum splitter objects and parses the html adding meta data where a post is found.
 * appropriate.
 * 
 * @author jp242
 *
 */
public class ForumHTMLParseFilter implements HtmlParseFilter {
	
    private static final Log LOG = LogFactory.getLog(ForumHTMLParseFilter.class);
	
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
	    final Parse parse = parseResult.get(content.getUrl());
	    
	    // Get the documents meta-data object in order to add to it.
	    final Metadata md = parse.getData().getParseMeta();
	    
	    // Used to determine if further parsing is required.
	    boolean postFound = false;	

	    // Add any forum posts to the meta-data object of the page.
	    Document jDoc = Jsoup.parse(new String(content.getContent())); // ignore DocumentFragment as DOM navigation sucks.
	    
	    factoryloop:
	    for(ForumSplitterFactory fs : factories) {
			for(Post post : fs.create().split(jDoc)) {
				// Add the meta-data to the specified field name.
				md.add(GlobalFieldValues.POST_FIELD, post.content());
				postFound = true;
			}
			if(postFound) {
				break factoryloop;	// Breakout as it is assumed only one type of forum tech. exists in a single page.
			}
		}
	    

	    // Pass the uri to the meta data
	    md.add(GlobalFieldValues.BASE_URL, content.getUrl().split("\\?")[0]);
	    
	    // If the posts in this page are paginated get the start value.
	    if(postFound) {
		    final String[] urlS = content.getUrl().split("\\?", 2);
		    if(urlS.length > 1) {
		    	if(urlS[1].contains("start=")) {
		    		final String page = urlS[1].split("start=")[1];
		    		try {
		    		    final Integer start = Integer.parseInt(page.split("[^0-9]",2)[0]);
			    		md.add(GlobalFieldValues.PAGE_START, start.toString());
		    		} catch (NumberFormatException e) {
		    			LOG.warn("WARN: Unable to parse the pagination value");
		    		}
		    		
		    	}
		    }
	    }

		return parseResult;
	}
	
	/**
	 * Called on instantiation to build a list of all implemented @ForumSplitterFactory
	 */
	private static void registerFactories() {
		
		// Use reflection to search the package containing the @ForumSplitterFactory classes.
		Reflections reflections = new Reflections("org.apache.nutch.index.filter.splitter");
		Set<Class<? extends ForumSplitterFactory>> splitters = reflections.getSubTypesOf(ForumSplitterFactory.class);
		for(Class<? extends ForumSplitterFactory> splitter : splitters) {
			ForumSplitterFactory anf = null;
			try {
				anf = splitter.newInstance();
			} catch (InstantiationException e) {
				LOG.fatal("FATAL: Could not instantiate the class: " + splitter.toString());
			} catch (IllegalAccessException e) {
				LOG.fatal("FATAL: Illegal Access Exception when instatiating: " + splitter.toString());
			}
			
			factories.add(anf);
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
