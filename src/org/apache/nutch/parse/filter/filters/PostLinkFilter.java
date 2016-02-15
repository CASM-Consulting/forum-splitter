package org.apache.nutch.parse.filter.filters;

// java imports
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.stream.Collectors;

// Nutch imports
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

// jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Adds as meta-data, any web links found in forums posts.
 * @author jp242
 */
public class PostLinkFilter implements IPostFilter {
	
	public static final String NAME = GlobalFieldValues.LINKS;
	public static final String ATT_HTML = "href";
	public static final Set<String> prefixes = new HashSet<String>(Arrays.asList(new String[]{"www","http","https"}));

	@Override
	public void parseContent(Post content, Metadata metaData) {
		content.put(NAME, findLinks(content.postHTML()));
	}
	
	/**
	 * @param post
	 * @return List of all (if any) web links within a single post
	 */
	private List<String> findLinks(String post) {
		
		// Look for link tags in post 
		List<String> links = new ArrayList<String>();
		Document doc = Jsoup.parse(post);
		links.addAll(doc.getElementsByAttribute(ATT_HTML).stream()
				.map(el -> el.attr(ATT_HTML))
				.filter(str -> prefixes.stream().anyMatch(prefix -> str.startsWith(prefix)))
				.collect(Collectors.toList()));
		
		return links;
	}

	@Override
	public String name() {
		return NAME;
	}

}
