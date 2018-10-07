package org.apache.nutch.parse.forum.splitter;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.LinkedList;

/**
 * Created by jack on 15/04/16.
 */
public class DepressionForumsSplitterFactory implements IForumSplitterFactory {

    private static final String BODY = "cPost";
    private static final String CONTENT = "cPost_contentWrap";
    private static final String AUTHOR = "cAuthorPane";
    
    private static final String TIME = "time";
    private static final String DTIME = "datetime";
    
    private static final String DOMAIN = "depressionforums.org";

    @Override
    public IForumSplitter create() {
        return new DepressionForumSplitter(BODY,CONTENT);
    }
    
	@Override
	public boolean correctDomain(String url) {
		return url.contains(DOMAIN);
	}

    public class DepressionForumSplitter extends AbstractForumSplitter {

        public DepressionForumSplitter(String bodyName, String contentName) {
            super(bodyName, contentName);
        }

        @Override
        public void mapFields(LinkedList<Post> posts) {

            for(Post post : posts) {

                Document doc = Jsoup.parse(post.postHTML());

                // add member details
                Elements membs = doc.getElementsByClass(AUTHOR);
                if(membs.first() != null) {
                    final String member = membs.first().text().split("\\s")[0];
                    post.put(GlobalFieldValues.MEMBER, String.valueOf(member.hashCode()));
                }

                // Add post-date details
                Elements time = doc.getElementsByTag(TIME);
                if(time.first() != null) {
                    final String date = time.first().attr(DTIME);
                    post.put(GlobalFieldValues.POST_DATE,date);
                }

            }

        }
    }
}
