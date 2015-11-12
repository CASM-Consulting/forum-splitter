package org.apache.nutch.splitter.utils;

/**
 * Global values shared between communicating classes.
 * @author jp242
 *
 */
public class GlobalFieldValues {
	
	public static final String POST_FIELD = "posts";	// The field name used to index the forum-post meta-data.
	public static final String NUM_POSTS = "numposts";	// The field name used to index the number of forum posts stored in a page.
	public static final String PAGE_START = "pagestart";// The field name used to index the pagination start point.
	public static final String PAGE_END = "pageend";	// The field name used to index the pagination end point.
	public static final String BASE_URL = "baseurl"; 	// The field name used to index the base url of a page.
	public static final String SUBJECT = "subject";		// The field name used to index the subject of the forum thread.
	public static final String QUESTION = "question";	// The field name used to index the question being asked in the forum.
	
}
