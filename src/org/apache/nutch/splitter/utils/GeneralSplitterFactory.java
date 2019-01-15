package org.apache.nutch.splitter.utils;

// java imports
import java.util.LinkedHashMap;
import java.util.LinkedList;

// logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.parse.forum.splitter.IForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitterFactory;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class GeneralSplitterFactory implements IForumSplitterFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(GeneralSplitterFactory.class);
	
	public static final String CUSTOM = "custom";
	public static final String TAG = "tag";
	public static final String CLASS = "class";
	public static final String ATT = "attribute";
	
	public static final String BODY = "root";	// A single tag typically used to represent the top element of the web-page (i.e. <body>)
	private final LinkedHashMap<String,LinkedHashMap<String,String>> fields;

	public GeneralSplitterFactory(LinkedHashMap<String,LinkedHashMap<String,String>> fields) {
		this.fields = fields;
	}

	@Override
	public IForumSplitter create() {
		return new GeneralSplitter();
	}

	/**
	 * Overridden to return true for this implementation as it is a general implementation
	 * Domain restrictions should therefore be handled by the crawler
	 * @param url
	 * @return
	 */
	@Override
	public boolean correctDomain(String url) {
		return true;
	}
	
	public class GeneralSplitter implements IForumSplitter {
		
		public GeneralSplitter() {}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			
		}

		/**
		 * Simply used to split the document into its root element(s) 
		 * Typically should return just one 'Post' representing the main body of the pages html content i.e. <body>
		 */
		@Override
		public LinkedList<Post> split(Document doc) {
			
			LinkedList<Post> posts = new LinkedList<>();	
			try {
				Elements elems = doc.select(parseQuery(fields.remove(BODY)));
				for(Element elem : elems) {
					posts.add(new Post(elem.html(),elem.text()));
				}
			} catch (InvalidCSSQueryException e) {
				LOG.error(e.getMessage());
			}
			
			return posts;
			
		}
		
	}
	
	/**
	 * Tag -->> Class -->> Attribute = hierarchy
	 * @param queryMap
	 * @return A string constructed to adhere to @JSoup select method
	 * @throws InvalidCSSQueryException
	 */
	public static String parseQuery(LinkedHashMap<String,String> queryMap) throws InvalidCSSQueryException {
		StringBuilder builder = new StringBuilder();
		if(queryMap.containsKey("custom")) {
			return queryMap.get("customs");
		}
		String tag = queryMap.get("tag");
		if(tag == null) {
			throw new InvalidCSSQueryException("tag",queryMap.get("tag"));
		}
		else {
			builder.append(tag);
			if(queryMap.containsKey("class")) {
				builder.append(".").append(queryMap.get("class"));
			}
			if(queryMap.containsKey("attribute")) {
				builder.append("[").append(queryMap.get("attribute")).append("]");
			}
		}

		return builder.toString();
	}

}
