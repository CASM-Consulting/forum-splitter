package org.apache.nutch.index.filter;

// nutch import
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.Text;
import org.apache.nutch.crawl.CrawlDatum;
import org.apache.nutch.crawl.Inlinks;
import org.apache.nutch.indexer.IndexingException;
import org.apache.nutch.indexer.IndexingFilter;
import org.apache.nutch.indexer.NutchDocument;
import org.apache.nutch.parse.Parse;

import org.apache.nutch.splitter.utils.GlobalFieldValues;

/**
 * Default class for adding forum fields to the index of a NutchDocument. The
 * intention is to subsequently communicate these to the corresponding field(s)
 * in the Solr index.
 * jp242 on 30/09/2015.
 */
public class ForumIndexer implements IndexingFilter {

	private Configuration conf;

	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text text, CrawlDatum crawlDatum, Inlinks inlinks) throws IndexingException {
		
		// Retrieve the forum post meta-data stored in the parse.
		String[] posts = parse.getData().getParseMeta().getValues(GlobalFieldValues.POST_FIELD);
		
		if(posts == null || posts.length <= 0) {
		    // Return null if no posts were found and the config (set in nutch-site.xml) specifies to skip the document.
		    if(Boolean.parseBoolean(conf.get("skip.no.posts", "false"))) {
		    		return null;
		    }
		    else {
		    	return doc;
		    }
		}
		
		// Add the number of posts found on this page.
		doc.add(GlobalFieldValues.NUM_POSTS, posts.length);
		
		// Add the pagination value if one exists
		final String page = ( parse.getData().getParseMeta().get(GlobalFieldValues.PAGE) == null ) ? 
				"0" : parse.getData().getParseMeta().get(GlobalFieldValues.PAGE);
		doc.add(GlobalFieldValues.PAGE,page);
		
		// Add post content to the index fields.
		for (String post : posts) {
			doc.add(GlobalFieldValues.POST_FIELD, post);
		}
		
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
