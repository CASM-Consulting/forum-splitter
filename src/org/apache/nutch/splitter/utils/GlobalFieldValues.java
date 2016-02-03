package org.apache.nutch.splitter.utils;

/**
 * Global values shared between communicating classes, Nutch and Solr.
 * @author jp242
 *
 */
public class GlobalFieldValues {
	
	// Document id fields
	public static final String ID = "id";
	
	//Post splitting fields
	public static final String POST_FIELD = "post";	// The field name used to index the forum-post meta-data.
	public static final String NUM_POSTS = "numposts";	// The field name used to index the number of forum posts stored in a page.
	public static final String POSITION = "position";
	public static final String CONTENT = "postcontent"; // The textual post content
	
	// Post tag fields
	public static final String MEMBER = "member";
	public static final String MEMBER_POSTS = "memberposts";	
	public static final String MEM_SINCE = "membsince";
	public static final String TITLE = "title";
	public static final String LOCATION = "location";
	public static final String THANKS = "thanks";
	
	// Filter fields
	public static final String PAGE_START = "pagestart";// The field name used to index the pagination start point.
	public static final String PAGE_END = "pageend";	// The field name used to index the pagination end point.
	public static final String BASE_URL = "baseurl"; 	// The field name used to index the base url of a page.
	public static final String SUBJECT = "subject";		// The field name used to index the subject of the forum thread.
	public static final String QUESTION = "question";	// The field name used to index the question being asked in the forum.
	public static final String LINKS = "links";
		
}
