package org.apache.nutch.parse.filter;

// java import
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

// nutch import
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.filter.filters.IPageFilter;

// gson imports
import com.google.gson.Gson;

import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.Registry;
import org.apache.solr.common.SolrInputDocument;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Default class for adding forum fields to the index of a NutchDocument. The
 * intention is to subsequently communicate these to the corresponding field(s)
 * in the Solr index.
 * @author jp242
 */
public class ForumIndexer implements IndexingFilter {
	
    private static final Logger LOG = LoggerFactory.getLogger(ForumIndexer.class);

	private Configuration conf; // Boilerplate config object
	
	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text text, CrawlDatum crawlDatum, Inlinks inlinks) throws IndexingException {
		
		// Get the previously collated meta-data
		final Metadata data = parse.getData().getParseMeta();
				
		// Retrieve the forum post meta-data stored in the parse.
		String[] json = data.getValues(GlobalFieldValues.POST_FIELD);
				
		if(json == null || json.length <= 0) {
		    // Return null if no posts were found and the configuration (set in nutch-site.xml) specifies to skip the document.
			return (Registry.skipPosts(conf)) ? null : doc;
		}
		
		// Deserialize from json string to Post objects
		final Gson gson = new Gson();
		List<Post> posts = Arrays.stream(json)
				.parallel()
				.map(string -> gson.fromJson(string, Post.class))
				.collect(Collectors.toList());
		
		LOG.info("INFO: Deserialised posts");
		
		// Add the posts as sub-documents to the parent (eventual SolrInputDocument) document.
		int i = 0;
		for(Post post : posts) {
			
			LOG.info("INFO: Adding post as Solr child document within the parent webpage Solr document.");
			
			final SolrInputDocument subDoc = new SolrInputDocument();
			// Add the id and post position of the new sub-doc i.e. forum post.
			subDoc.addField(GlobalFieldValues.ID, doc.getFieldValue(GlobalFieldValues.ID).toString());
			subDoc.addField(GlobalFieldValues.POSITION, i);
			
			// Add all other parse fields to the sub-document.
		    for(String filter : Registry.configuredFilters(conf)) {
		    	if(post.get(filter) != null) {
		    		subDoc.addField(filter, post.get(filter));
		    	}
		    }
		    // Add the sub-document as a child of the parent document (webpage containing the post).
			doc.add(GlobalFieldValues.POST_FIELD, subDoc);
			i++;
		}
				
		LOG.info("INFO: Forum-post child documents added.");
		
		// Add the number of posts that were found to the meta-data.
		doc.add(GlobalFieldValues.NUM_POSTS, posts.size());
		
	    // Pass all page meta-data to the index document.
	    final Set<String> requestedFilters = Registry.configuredFilters(conf);

	    Registry.filters().stream()											// Cannot be done in parallel.
	    	.filter(filter -> requestedFilters.contains(filter.name()))
	    	.filter(filter -> filter instanceof IPageFilter)
	    	.filter(filter -> data.get(filter.name()) != null)
	    	.forEach(filter -> doc.add(filter.name(), 
	    			(data.isMultiValued(filter.name())) ? data.getValues(filter.name()) : data.get(filter.name())));
	    		
		return doc;
	}

	// Boilerplate
	public Configuration getConf() {
		return conf;
	}

	// Boilerplate
	public void setConf(Configuration conf) {
		this.conf = conf;
	}

}
