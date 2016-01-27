package org.apache.nutch.parse.filter;

// java imports
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

// nutch/hadoop imports
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.HtmlParseFilter;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.IPageFilter;
import org.apache.nutch.splitter.utils.IPostFilter;
import org.apache.nutch.splitter.utils.Registry;

// html imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.DocumentFragment;

// Gson imports
import com.google.gson.Gson;

import org.apache.nutch.index.filter.Post;
import org.apache.nutch.index.filter.splitter.IForumSplitter;
import org.apache.nutch.index.filter.splitter.IForumSplitterFactory;

/**
 * Used as a first point of contact to parse html for forum posts. Retrieves all
 * forum splitter objects and parses the html adding meta-data where a post is found.
 * @author jp242
 */
public class ForumHTMLParseFilter implements HtmlParseFilter {
	
	{
		// Instantiate the filters
		Registry.registerFilters();
		// Instantiate the forum splitter factories.
		Registry.registerFactories();
	}
		
	private Configuration conf; 					 			// Boilerplate configuration field.
	
	private static  List<IForumSplitterFactory> factories;	// Splitters designed to parse specific forums
	private static  List<IFilter> filters;					// Filters to parse the page content
	

	public ForumHTMLParseFilter() {
		// Instantiate the filters
		filters = Registry.registerFilters();
		// Instantiate the forum splitter factories.
		factories = Registry.registerFactories();
	}

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

	    // Parse to jsoup Document for further parsing -  ignores DocumentFragment (doc) as standard java DOM navigation sucks.
	    Document jDoc = Jsoup.parse(new String(content.getContent())); 
	    	    
	    // Done outside of filter phase to ensure content is available to subsequent filters.
    	List<Post> posts = new ArrayList<Post>();
	    factoryloop:
	    for(IForumSplitterFactory fact : Registry.factories()) {
	    	final IForumSplitter fs = fact.create();
	    	posts = fs.split(jDoc);
	    	
			// Add the meta-data to the specified field name.
	    	postFound = (posts != null && posts.size() > 0) ? true : false;
	    	if(postFound) {		
		    	// Breakout as it is assumed only one type of forum tech. exists in a single page.
				break factoryloop;	
	    	}
		}
	    
	    // Return if no posts were found on this page.
	    if(!postFound) {
	    	return parseResult;
	    }
	    	    
	    // Retrieve the configured set of forum filters
	    final Set<String> requestedFilters = Registry.configuredFilters(conf);
	    
	    // Pass the post through each of the configured post forum filters.
	    posts.parallelStream()
	    	.forEach(post -> Registry.filters().parallelStream()
	    				.filter(filter -> filter instanceof IPostFilter)
	    				.filter(filter -> requestedFilters.contains(filter.name()))
	    				.forEach(filter -> filter.parseContent(post, md)));
	    
	    // "serialize" the posts to json string and pass to the meta-data
	    final Gson gson = new Gson();
	    posts.parallelStream()
	    	.map(post -> gson.toJson(post))
	    	.forEach(string -> md.add(GlobalFieldValues.POST_FIELD , string));
	    
	    // Pass the content through each of the configured page filters.
	    Registry.filters().parallelStream()
	    	.filter(filter -> filter instanceof IPageFilter)
	    	.filter(filter -> requestedFilters.contains(filter.name()))
	    	.forEach(filter -> filter.parseContent(content,md));
	    
		return parseResult;
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
