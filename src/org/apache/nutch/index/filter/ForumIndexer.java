package org.apache.nutch.index.filter;

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
 * in the Solr index, which are dedicated to storing these posts. Created by
 * jp242 on 30/09/2015.
 */
public class ForumIndexer implements IndexingFilter {

	private Configuration conf;

	@Override
	public NutchDocument filter(NutchDocument doc, Parse parse, Text text, CrawlDatum crawlDatum, Inlinks inlinks)
			throws IndexingException {
		
		// Retrieve the forum post meta-data stored in the parse.
		String[] posts = parse.getData().getParseMeta().getValues(GlobalFieldValues.POST_FIELD);
		System.err.println("posts length: " + posts.length);
		
		// Add it to the index fields in the nutch document.
		for (String post : posts) {
			doc.add(GlobalFieldValues.POST_FIELD, post);
			System.err.println("POST IS THIS: " + post);
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
