package org.apache.nutch.splitter.utils;

import java.util.ArrayList;
// java imports
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.nutch.index.filter.Post;
// Nutch imports
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.protocol.Content;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Adds as meta-data, any web links found in forums posts.
 * @author jp242
 */
public class PostLinkFilter implements IPostFilter {
	
	public static final String NAME = "post-link";
	public static final String[] LINK_PREFIXES = {"www","http","https"};
	public static final String LINK_HTML = "a";

	@Override
	public void parseContent(Post content, Metadata metaData) {
		
		content.put(GlobalFieldValues.LINKS, findLinks(content.content()).get(0));
//		String[] posts = metaData.getValues(GlobalFieldValues.POST_FIELD);
//		Arrays.stream(posts)
//			.parallel()
//			.map(this :: findLinks)
//			.flatMap(links -> links.stream())
//			.forEach(link -> metaData.add(name(), link));
	}
	
	/**
	 * @param post
	 * @return List of all (if any) web links within a single post
	 */
	private List<String> findLinks(String post) {
		// Look for link tags in post first
		List<String> links = new ArrayList<String>();
		Document doc = Jsoup.parse(post);
		links.addAll(doc.getElementsByTag(LINK_HTML).stream()
				.map(Element :: text)
				.collect(Collectors.toList()));
		// Assume any links have not been tagged in the post and try common link prefixes
		if(links.isEmpty()) {
			for(String prefix : LINK_PREFIXES) {
				final String[] strs = post.split(prefix);
				for(String link : strs) {
					links.add(link.split("\\s*")[0]);
				}
			}
		}
		return links;
	}

	@Override
	public String name() {
		return NAME;
	}

}
