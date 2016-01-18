package org.apache.nutch.index.filter;

// logging import
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

/**
 * Default class for adding forum fields to the index of a NutchDocument. The
 * intention is to subsequently communicate these to the corresponding field(s)
 * in the Solr index.
 * jp242 on 30/09/2015.
 */
public class ForumIndexer implements IndexingFilter {
	
	// TODO: // Modularise each meta-data addition.
	
    private static final Log LOG = LogFactory.getLog(ForumIndexer.class);

	private Configuration conf;

	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text text, CrawlDatum crawlDatum, Inlinks inlinks) throws IndexingException {
		
		final Metadata data = parse.getData().getParseMeta();
		
		// Retrieve the forum post meta-data stored in the parse.
		String[] posts = data.getValues(GlobalFieldValues.POST_FIELD);
		
		if(posts == null || posts.length <= 0) {
		    // Return null if no posts were found and the config (set in nutch-site.xml) specifies to skip the document.
		    if(Boolean.parseBoolean(conf.get("skip.no.posts", "false"))) {
		    		return null;
		    }
		    else {
		    	return doc;
		    }
		}
		
		// Add the base url (for search purposes).
		doc.add(GlobalFieldValues.BASE_URL, data.get(GlobalFieldValues.BASE_URL));
		
		// Add the subject 
		doc.add(GlobalFieldValues.SUBJECT, data.get(GlobalFieldValues.SUBJECT));
		
		// Add the number of posts found on this page.
		doc.add(GlobalFieldValues.NUM_POSTS, posts.length);
		
		// Add the pagination value if one exists
		final String page = ( data.get(GlobalFieldValues.PAGE_START) == null || Integer.parseInt(data.get(GlobalFieldValues.PAGE_START)) <= 0) ? 
				"0" : data.get(GlobalFieldValues.PAGE_START);
		doc.add(GlobalFieldValues.PAGE_START,page);
		doc.add(GlobalFieldValues.PAGE_END, (Integer.parseInt(page)+posts.length)-1);
		
		// Add post content to the index fields.
		for (String post : posts) {
			doc.add(GlobalFieldValues.POST_FIELD, post);
		}
		
		// Add the first post as the potential question being asked
		// Must have be a field of less than 32766 bytes in length as this is the ( hard-coded ) maximum length for a ( un- )analyzed Lucene index field.
	    if(Integer.parseInt(page) == 0 && posts[0].getBytes().length < 32766) {
	    	doc.add(GlobalFieldValues.QUESTION, posts[0]);
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
