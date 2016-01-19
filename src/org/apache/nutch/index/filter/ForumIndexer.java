package org.apache.nutch.index.filter;

// java import
import java.util.Arrays;
import java.util.Set;

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

import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.Registry;
import org.jsoup.Jsoup;

/**
 * Default class for adding forum fields to the index of a NutchDocument. The
 * intention is to subsequently communicate these to the corresponding field(s)
 * in the Solr index.
 * @author jp242
 */
public class ForumIndexer implements IndexingFilter {

	private Configuration conf; // Boilerplate config object

	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text text, CrawlDatum crawlDatum, Inlinks inlinks) throws IndexingException {
		
		// Get the previously collated meta-data
		final Metadata data = parse.getData().getParseMeta();
		
		// Retrieve the forum post meta-data stored in the parse.
		String[] posts = data.getValues(GlobalFieldValues.POST_FIELD);
		
		if(posts == null || posts.length <= 0) {
		    // Return null if no posts were found and the configuration (set in nutch-site.xml) specifies to skip the document.
		    if(Registry.skipPosts(conf)) {
		    		return null;
		    }
		    else {
		    	return doc;
		    }
		}
		
		// Add basic post information to the index fields.
		// Done outside of @IFilter pass to ensure content is available.
		Arrays.stream(posts)
			.parallel()
			.forEach(post -> doc.add(GlobalFieldValues.POST_FIELD, Jsoup.parse(post).text()));
		
		doc.add(GlobalFieldValues.NUM_POSTS, posts.length);
		
		
	    // Pass all filter meta-data to the index document.
	    final Set<String> requestedFilters = Registry.configuredFilters(conf);
	    Registry.filters().parallelStream()
	    	.filter(filter -> requestedFilters.contains(filter.name()))
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
