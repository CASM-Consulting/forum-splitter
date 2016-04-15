package org.apache.nutch.parse.forum.splitter;

import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.LinkedList;

/**
 * Created by jack on 15/04/16.
 */
public class DepressionForumsSplitterFactory implements IForumSplitterFactory {

    private static final String BODY = "cPost";
    private static final String CONTENT = "cPost_contentWrap";

    @Override
    public IForumSplitter create() {
        return new DepressionForumSplitter(BODY,CONTENT);
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
                final String member = doc.getElementsByClass("cAuthorPane").text().split("\\s")[0];
                post.put(GlobalFieldValues.MEMBER,member);

                // Add post-date details
                final String date = doc.getElementsByTag("time").attr("datetime").split("T")[0];
                post.put(GlobalFieldValues.POST_DATE,date);

            }

        }
    }
}
