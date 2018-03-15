package org.apache.nutch.parse.filter;

// java imports
import java.util.Set;
import java.util.LinkedList;

// nutch/hadoop imports
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.HtmlParseFilter;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.parse.filter.filters.IPageFilter;
import org.apache.nutch.parse.filter.filters.IPostFilter;
import org.apache.nutch.parse.forum.splitter.IForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitterFactory;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.Registry;
// html imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.DocumentFragment;

// logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// Gson imports
import com.google.gson.Gson;

/**
 * Used as a first point of contact to parse html for forum posts. Retrieves all
 * forum splitter objects and parses the html adding meta-data where a post is found.
 * @author jp242
 */
public class ForumHTMLParseFilter implements HtmlParseFilter {
	
    private static final Logger LOG = LoggerFactory.getLogger(ForumHTMLParseFilter.class);
	
	// Boilerplate configuration field.
	private Configuration conf;

	/**
	 * Adds forum meta-data to the parsed document structure.
	 */
	@Override
	public ParseResult filter(Content content, ParseResult parseResult, HTMLMetaTags metaTags, DocumentFragment doc) {
		
		LOG.info("INFO: Begin parsing " + content.getUrl() + " for forum posts.");
		
		// Get the parse object which stores the document meta-data.
	    final Parse parse = parseResult.get(content.getUrl());
	    
	    // Get the documents meta-data object in order to add to it.
	    final Metadata md = parse.getData().getParseMeta();	

	    // Parse to jsoup Document for further parsing -  ignores DocumentFragment (doc) as standard java DOM navigation sucks.
	    Document jDoc = Jsoup.parse(new String(content.getContent())); 
	    	    
	    // Done outside of filter phase to ensure content is available to subsequent filters.
    	LinkedList<Post> posts = new LinkedList<Post>();
    	splitloop:
	    for(IForumSplitterFactory fact : Registry.factories()) {

			LOG.info("Parsing with factory: " + fact.getClass());
			
			if(!fact.correctDomain(content.getUrl())) {
				continue;
			}

	    	final IForumSplitter fs = fact.create();
		    posts = fs.split(jDoc);
	    	
	    	if(posts != null && posts.size() > 0) {
	    		break splitloop;
	    	}

		}
	    
	    // Return if no posts were found on this page.
	    if(posts == null || posts.size() <= 0) {
			LOG.info("INFO: No posts found in page " + content.getUrl());
	    	return parseResult;
	    }
	    
	    LOG.info("INFO: Posts found - parsing for additional meta-data.");
	    	    
	    // Retrieve the configured set of forum filters
	    final Set<String> requestedFilters = Registry.configuredFilters(conf);
	    
	    // Pass the post through each of the configured post forum filters. Cannot be parallel processed.
	    posts.stream()
	    	.forEach(post -> Registry.filters().stream()			    
	    				.filter(filter -> filter instanceof IPostFilter)
	    				.filter(filter -> requestedFilters.contains(filter.name()))
	    				.forEach(filter -> filter.parseContent(post, md)));
	    
	    LOG.info("INFO: Forum post filtering stage complete!");
	    
	    // Serialize the posts to json string and pass to the meta-data. Cannot be parallel processed
	    final Gson gson = new Gson();
	    posts.stream()
	    	.map(Post::toJson)
	    	.forEach(string -> md.add(GlobalFieldValues.POST_FIELD , string));
	    
	    LOG.info("INFO: Serialised posts to json.");
	    
	    // Pass the content through each of the configured page filters. Cannot be parallel processed
	    Registry.filters().stream()
	    	.filter(filter -> filter instanceof IPageFilter)
	    	.filter(filter -> requestedFilters.contains(filter.name()))
	    	.forEach(filter -> filter.parseContent(content,md));
	    
	    LOG.info("INFO: Filtering complete on page: " + content.getUrl());
	    
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
