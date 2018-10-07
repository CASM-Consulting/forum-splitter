package org.apache.nutch.parse.forum.splitter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.SplitterFactory;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

public class PatientInfoSplitterFactory implements IForumSplitterFactory {
	
	public static final String POST = "post";
	public static final String CONTENT = "post-content";

	public static final String USER = "post-username";
	public static final String AVATAR = "avatar-hover";
	
	private static final String FUZ = "fuzzy";
	private static final String DTIME = "datetime";
	
	private static final String DOMAIN = "patient.info";
	

	@Override
	public IForumSplitter create() {
		return new PatientInfoSplitter(POST,CONTENT);
	}
	
	@Override
	public boolean correctDomain(String url) {
		return url.contains(DOMAIN);
	}
	
	public class PatientInfoSplitter extends AbstractForumSplitter {

		public PatientInfoSplitter(String bodyName, String contentName) {
			super(bodyName, contentName);
		}

		@Override
		public void mapFields(LinkedList<Post> posts) {

			for(Post post : posts) {

				Document doc = Jsoup.parse(post.postHTML());

				// Add member information
				Elements membs = doc.getElementsByClass(USER);
				if(membs.first() != null) {
					String member = membs.first().getElementsByClass(AVATAR).first().text();
					post.put(GlobalFieldValues.MEMBER,String.valueOf(member.hashCode()));
				}

				// Add post-date information
				Elements time = doc.getElementsByClass(FUZ);
				if(time.first() != null) {
					String toks = time.first().attr(DTIME).split("T")[0];
					DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
					LocalDate date = LocalDate.parse(toks,dtf);
					post.put(GlobalFieldValues.POST_DATE,date.atStartOfDay().toString() + ":00Z");
				}

			}
		}
		
	}
	
}
