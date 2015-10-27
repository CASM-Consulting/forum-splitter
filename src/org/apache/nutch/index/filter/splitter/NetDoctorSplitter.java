package org.apache.nutch.index.filter.splitter;

// java imports
import java.util.LinkedList;

// nutch imports
import org.apache.nutch.index.filter.Post;

//jsoup imports
import org.jsoup.nodes.Document;

public class NetDoctorSplitter implements ForumSplitter {

	@Override
	public LinkedList<Post> split(Document doc) {
		return new LinkedList<Post>();
	}

}
