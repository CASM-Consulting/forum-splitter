package org.apache.nutch.parse.forum.splitter;

// Jsoup imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

// Logging imports
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
// java imports
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

/**
 * Forum splitter designed to scrape message posts from the Childline message forum
 * @author jp242
 *
 */
public class ChildlineForumSplitterFactory implements IForumSplitterFactory {
	
	private static final Logger LOG = LoggerFactory.getLogger(ChildlineForumSplitterFactory.class);
	
	private static final String DOMAIN = "childline.org.uk";
	
	private static final String TOPIC_CONTAINER = "js-topic-container";
	private static final String ID = "data-reply-id";
	private static final String POST_BODY_NAME = "js-reply-container";
	private static final String POST_CONTENT = "forum__post__content";
	private static final String POST_CONTENT_INNER = "forum__post__content__inner";
	private static final String QUOTE = "forum__quoted-message";
	private static final String CD_DATA = "p";
	private static final String TOPIC_ID = "data-topic-id";
	private static final String PAGINATION = "pagination__info";
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("MMM dd yyyy H.m");
	private final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Override
	public ChildlineForumSplitter create() {
		return new ChildlineForumSplitter(POST_BODY_NAME,POST_CONTENT_INNER);
	}

	@Override
	public boolean correctDomain(String url) {
		return url.contains(DOMAIN);
	}
	
	public class ChildlineForumSplitter implements IForumSplitter {
		
		private final String BODY_NAME;
		private final String CONTENT;

		public ChildlineForumSplitter(String bodyName, String contentName) {
			BODY_NAME = bodyName;
			CONTENT = contentName;
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {
			
			LOG.info("Childline message board posts found = "  + posts.size());

			for(Post post : posts){
				
				Document doc = Jsoup.parse(post.postHTML());
				
				// Get the username and date info. 
				String[] nameDate = doc.getElementsByClass(CONTENT).get(0).getElementsByTag("span").get(0).text().split("/");
				post.put(GlobalFieldValues.MEMBER, nameDate[0].trim());
				
				// Parse the post date info.
				try {
					Date date = sdf.parse(nameDate[1].trim());
					String formattedDate = sdf2.format(date).trim().replace(" ", "T") + "Z";
					post.put(GlobalFieldValues.POST_DATE, formattedDate);
				} catch (ParseException e) {
					LOG.error("Failed to parse the date in current format");
				}
				
//				// Retain whether the post is quoting and replying to another persons response to the topic
//				Elements quoted = doc.getElementsByClass(QUOTE);
////				LOG.error(quoted.text());
//				if(quoted.size() > 0 && Integer.valueOf(post.get(GlobalFieldValues.POSITION).get(0)) > 0) {
//					final String[] quotedInfo = getQuotedInfo(quoted.get(0)); // complete this info
//					post.put(GlobalFieldValues.QUOTE_MEM, quotedInfo[0].trim());
//					try {
//						Date date = sdf.parse(quotedInfo[1].trim());
//						post.put(GlobalFieldValues.QUOTE_DATE, sdf2.format(date));	
//					} catch (ParseException e) {
//						LOG.error("Failed to parse quoted date in current format: " + quotedInfo[1]);
//					}
//				}
			}
		}

		@Override
		public LinkedList<Post> split(Document doc) {
			
			LinkedList<Post> fThread = new LinkedList<Post>();
			Elements topic_container = doc.getElementsByClass(TOPIC_CONTAINER);
			
			if(topic_container.size() <= 0) {
				return fThread;
			}
			
			// Gets the thread id to attribute to each post
			Element topic = topic_container.first();
			final String thread_ID = topic.attr(TOPIC_ID).trim();
			
			// Get the subject
			Element subjectElem = doc.getElementsByTag("title").first();
			final String subject = subjectElem.text().split("\\|")[0].trim();
			
			// Get breadcrumb
			boolean topFound = false;
			Elements bread = doc.select("div.breadcrumb__inner");
			System.out.println(bread.size());
			Elements links = bread.get(0).select("a");
			System.out.println(links.size());
			List<String> cats = new ArrayList<>();
			for(Element elem : links) {
				cats.add(elem.text());
			}
			if(bread.get(0).select("span.breadcrumb__inner__current").size() > 0) {
				cats.add(bread.get(0).select("span.breadcrumb__inner__current").get(0).text());
			}
			List<String> subCats = cats.subList(cats.indexOf("Message boards"), cats.size());
			
			System.out.println(subCats.size());
			
			// Retrieve the full replies body
			Elements replies = doc.getElementsByClass(BODY_NAME);
			
			// Retrieve the starting thread position (i.e. is this a pagination from the original thread)
			int page = Integer.valueOf(doc.getElementsByClass(PAGINATION).get(0).text().split("\\s")[1]).intValue();
			if(page <= 1) {
				final Element text = topic.getElementsByClass(POST_CONTENT).first();
				final StringBuilder post_text = new StringBuilder();
//				System.out.println(text.children().get(0).children());
				for(Element element : text.children().get(0).children()) {
					if(!(element.tagName().equals("span")) && !(element.tagName().equals("blockquote"))) {
						post_text.append(element.text()).append(" ");
					}
				}
				final Post newPost = new Post(text.html(),post_text.toString());
				newPost.put(GlobalFieldValues.ID, thread_ID);
				newPost.put(GlobalFieldValues.POST_ID, thread_ID);
				newPost.put(GlobalFieldValues.THREAD_ID, thread_ID);
				newPost.put(GlobalFieldValues.POSITION, String.valueOf(0));
				newPost.put(GlobalFieldValues.SUBJECT, subject);
//				newPost.put("url")
				
				// Put the category hierarchy
				for(int i = 0; i < subCats.size(); i++) {
					newPost.put("category-" + i, subCats.get(i));
				}
				
				fThread.add(newPost);
			}
			
			LOG.error("DEBUG: Number of replies = " + replies.size());
			
//			// check it is not an empty post (there seem to be instances...) and add meta-data
			for(Element post : replies) {
				Elements reply_content = post.getElementsByClass(POST_CONTENT).first().getElementsByClass(POST_CONTENT_INNER);
//						.stream()
//						.filter(el -> !(el.select("blockquote").size()>0))
//						.collect(Collectors.toList());
//				List<Element> removeQuotes = reply_content.stream()
//					.filter(el -> !(el.getElementsByTag("blockquote").size()>0))
//					.collect(Collectors.toList());
//				System.out.println(reply_content.size());
//				System.out.println(reply_content.first().getAllElements());

				if(reply_content.size() > 0) {
//					final Elements text = reply_content.first().getElementsByTag(CD_DATA);
					final StringBuilder post_text = new StringBuilder();
					for(Element element : reply_content.first().children()) {
						if(!(element.tagName().equals("span")) && !(element.tagName().equals("blockquote")) && !(element.tagName().equals(POST_CONTENT_INNER))) {
//							System.out.println(element.tagName());
//							System.out.println(element.text());
							post_text.append(element.text()).append(" ");
						}
					}
//					System.out.println(post_text.toString());
					// Get the post identifying meta-data
					final Post newPost = new Post(post.html(),post_text.toString());
					newPost.put(GlobalFieldValues.ID, post.attr(ID));
					newPost.put(GlobalFieldValues.POST_ID, post.attr(ID));
					newPost.put(GlobalFieldValues.THREAD_ID, thread_ID);
					newPost.put(GlobalFieldValues.POSITION, String.valueOf(page));
					newPost.put(GlobalFieldValues.SUBJECT, subject);
					
					// Put the category hierarchy
					for(int i = 0; i < subCats.size(); i++) {
						newPost.put("category-" + i, subCats.get(i));
					}
					
					fThread.add(newPost);
					page++;
				}
			}
			
			mapFields(fThread);
			
			return fThread;
		}
		
		/**
		 * @param html The html body of the quoted post
		 * @return The user and post date of the quoted/respondent post for cross referencing
		 */
		public String[] getQuotedInfo(Element html) {
			return html.getElementsByTag("span").get(0).text().split("/");
		}
	}
	
	public static void main(String[] args) {
		String testpage = "/Users/jp242/Documents/Projects/NSPCC/test-page.html";
		ChildlineForumSplitterFactory cfsf = new ChildlineForumSplitterFactory();
		ChildlineForumSplitter csf = cfsf.create();
		try {
			Document doc = Jsoup.parse(new File(testpage),"UTF-8");
			LinkedList<Post> posts = csf.split(doc);
			csf.mapFields(posts);
			for(Post post: posts) {
				for (String meta : post.keySet()) {
					if(!meta.equals("posthtml")){
						System.out.println(meta + " " + post.get(meta).get(0));
					}
				}
				System.out.println();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
