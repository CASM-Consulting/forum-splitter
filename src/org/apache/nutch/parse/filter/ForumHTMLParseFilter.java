package org.apache.nutch.parse.filter;

// java imports
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
import org.apache.nutch.splitter.utils.Registry;

// html imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.DocumentFragment;

import org.apache.nutch.index.filter.IPost;
import org.apache.nutch.index.filter.splitter.IForumSplitterFactory;

/**
 * Used as a first point of contact to parse html for forum posts. Retrieves all
 * forum splitter objects and parses the html adding meta-data where a post is found.
 * @author jp242
 */
public class ForumHTMLParseFilter implements HtmlParseFilter {
		
	private Configuration conf; 					 			// Boilerplate configuration field.

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
	    // ignores DocumentFragment (doc) as standard java DOM navigation sucks.
	    Document jDoc = Jsoup.parse(new String(content.getContent())); 
	    
	    // Done outside of filter phase to ensure content is available to subsequent filters.
	    factoryloop:
	    for(IForumSplitterFactory fs : Registry.factories()) {
	    	final List<IPost> posts = fs.create().split(jDoc);
			// Add the meta-data to the specified field name.
	    	postFound = (posts != null && posts.size() > 0) ? true : false;
	    	if(postFound) {
		    	posts.parallelStream()
	    		.forEach(post -> md.add(GlobalFieldValues.POST_FIELD, post.content()));	// Add posts
		    	md.add(GlobalFieldValues.NUM_POSTS, String.valueOf(posts.size()));		// Add the number of posts found
		    	// Breakout as it is assumed only one type of forum tech. exists in a single page.
				break factoryloop;	
	    	}
		}
	    
	    // Return if no posts were found on this page.
	    if(!postFound) {
	    	return parseResult;
	    }
	    
	    // Pass the content through each of the configured forum filters.
	    final Set<String> requestedFilters = Registry.configuredFilters(conf);
	    Registry.filters().parallelStream()
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
