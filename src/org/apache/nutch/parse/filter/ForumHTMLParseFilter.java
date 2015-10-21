package org.apache.nutch.parse.filter;

// nutch imports
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.index.filter.ForumSplitter;
import org.apache.nutch.index.filter.PhpBBForumSplitter;
import org.apache.nutch.index.filter.Post;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.HTMLMetaTags;
import org.apache.nutch.parse.HtmlParseFilter;
import org.apache.nutch.parse.Parse;
import org.apache.nutch.parse.ParseResult;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
// html imports
import org.jsoup.Jsoup;
import org.w3c.dom.DocumentFragment;

/**
 * Used as a first point of contact to parse html for forum posts. Retrieves all
 * forum splitter objects and parses the html adding meta data where a post is found.
 * appropriate.
 * 
 * @author jp242
 *
 */
public class ForumHTMLParseFilter implements HtmlParseFilter {

	private Configuration conf; 					 // Boilerplate configuration field.

	/**
	 * Adds forum meta-data to the parsed document structure.
	 */
	@Override
	public ParseResult filter(Content content, ParseResult parseResult, HTMLMetaTags metaTags, DocumentFragment doc) {
		
		ForumSplitter fs = new PhpBBForumSplitter();
		
	    Parse parse = parseResult.get(content.getUrl());
	    
	    Metadata md = parse.getData().getParseMeta();

		for(Post post : fs.split(Jsoup.parse(new String(content.getContent())))) {
			md.add(GlobalFieldValues.POST_FIELD,post.content());
		}
		
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
