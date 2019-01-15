package org.apache.nutch.parse.forum.splitter;

// java imports
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

// nutch imports
import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.InvalidCSSQueryException;
import org.apache.nutch.splitter.utils.Utils;

// jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

// logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GeneralSplitterFactory implements IForumSplitterFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(GeneralSplitterFactory.class);
	
	private static String domain;
	
	public final String ROOT = "root";

	private Map<String,List<Map<String,String>>> fields;
	
	public static final String CUSTOM = "custom";
	public static final String TAG = "tag";
	public static final String CLASS = "class";
	public static final String ATT = "att";

	public GeneralSplitterFactory(Map<String,List<Map<String,String>>> fields) {
		this.fields = fields;
	}
	
	public GeneralSplitterFactory(Map<String,List<Map<String,String>>> fields,String domain) {
		this(fields);
		this.domain = domain;
	}

	@Override
	public IForumSplitter create() {
		return new GeneralSplitter();
	}

	@Override
	public boolean correctDomain(String url) {
		try {
			return (domain != null) ? Utils.getDomain(url).equals(domain) : true;
		} catch (URISyntaxException e) {
			LOG.debug(e.getMessage());
		}
		return true;
	}
	
	public class GeneralSplitter implements IForumSplitter {

		public GeneralSplitter() {}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			
			if(fields.size() > 1) {
				for(String field : fields.keySet()) {
					List<String> queries = new ArrayList<>();
					for(Map<String,String> query : fields.get(field)) {
						try {
							queries.add(createQuery(query));
						} catch (InvalidCSSQueryException e) {
							LOG.error(e.getMessage());
						}
					}
					for(Post post : posts) {
						List<String> meta = getContent(Jsoup.parse(post.postHTML()),new ArrayList<>(),queries).stream()
								.map(elem -> elem.text())
								.collect(Collectors.toList());
						
						post.put(field, meta);
					}

				}
			}
			
		}

		@Override
		public LinkedList<Post> split(Document doc) {
			
			LinkedList<Post> posts = new LinkedList<>();
			List<String> queries = new ArrayList<>();
			for(Map<String,String> query : fields.get(ROOT)) {
				try {
					queries.add(createQuery(query));
				} catch (InvalidCSSQueryException e) {
					LOG.error(e.getMessage());
				}
			}
			for(Element element : getContent(doc, new ArrayList<Element>(), queries)) {
				posts.add(new Post(element.html(),element.text()));
			}
			
			return posts;
		}
				
	}

	/**
	 * @return List of elements which contain the content for a specific requested part of a web-page.
	 */
	public static List<Element> getContent(Element currElem, List<Element> output, List<String> queries) {
		
		if(queries.size() == 1) {
			output.addAll(currElem.select(queries.get(0)));
		}
		else {
			for(Element newElem : currElem.select(queries.get(0))) {
				return getContent(newElem,output,queries.subList(1, queries.size()));
			}
		}
		
		return output;
	}
	
	/**
	 * @param labels css labels for query (tag -> class -> attribute) 
	 * Currently only supports single instances of each.
	 * @return A css query according to the jsoup select syntax
	 * @throws InvalidCSSQueryException 
	 */
	public static String createQuery(Map<String,String> labels) throws InvalidCSSQueryException {
		StringBuilder sb = new StringBuilder();
		if(labels == null || labels.size() == 0) {
			throw new InvalidCSSQueryException(null,null);
		}
		
		// If it's a custom query then simply return said query
		if(labels.containsKey(CUSTOM)) {
			return labels.get(CUSTOM);
		}
		
		// must atleast contain an html tag to be valid
		if(labels.containsKey(TAG)) {
			sb.append(labels.get(TAG));
		}
		else {
			throw new InvalidCSSQueryException(TAG, labels.get(TAG));
		}
		
		// add the class label to the css query.
		if(labels.containsKey(CLASS)) {
			sb.append(".").append(labels.get(CLASS));
		}
		if(labels.containsKey(ATT)) {
			sb.append("[").append(labels.get(ATT)).append("]");
		}
		
		return sb.toString();
	}

}
