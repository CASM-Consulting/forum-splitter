package org.apache.nutch.parse.filter;

// java imports
import java.util.HashMap;
import java.util.Map;

import org.apache.nutch.splitter.utils.GlobalFieldValues;

// google imports
import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * Represents a single post in a forum thread.
 */
public final class Post {
	
	private HashMap<String,String> fields;
	private String POSTHTML;
	
	/**
	 * Private, empty constructor for @Gson "(de)serialization"
	 */
	private Post(){ }
	
	/**
	 * For standard instantiation purposes.
	 * @param post full post, html expected
	 * @param content post content, html expected
	 */
	public Post(String postHTML, String content) {
		this.POSTHTML = postHTML;
		fields = new HashMap<String,String>();
		this.fields.put(GlobalFieldValues.CONTENT,content);
	}
	
	/**
	 * @return The post in its entirety.
	 */
	public String postHTML() {
		return POSTHTML;
	}
	
	/**
	 * @return The html content of the post body.
	 */
	public String content() {
		return this.fields.get(GlobalFieldValues.CONTENT);
	}
	
	/**
	 * @return @Post as a json string
	 */
	public String toJson() {
		Gson gson = new Gson();
		JsonElement tree = gson.toJsonTree(this);
		return gson.toJson(tree);
	}
	
	public Map<String,String> fields() {
		return fields;
	}
	
	public String toString() {
		return this.toJson();
	}
	
}
