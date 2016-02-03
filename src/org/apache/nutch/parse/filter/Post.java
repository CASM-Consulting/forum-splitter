package org.apache.nutch.parse.filter;

import java.util.ArrayList;
// java imports
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.nutch.splitter.utils.GlobalFieldValues;

// google imports
import com.google.gson.Gson;
import com.google.gson.JsonElement;

/**
 * Represents a single post in a forum thread.
 */
public final class Post {
	
	private HashMap<String,List<String>> fields;
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
		fields = new HashMap<String,List<String>>();
		ArrayList<String> con = new ArrayList<String>();
		con.add(content);
		this.fields.put(GlobalFieldValues.CONTENT,con);
	}
	
	public void put(String key, String value) {
		if(fields.get(key) == null) {
			fields.put(key, new ArrayList<String>());
			fields.get(key).add(value);
		}
		else {
			fields.get(key).add(value);
		}
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
		return this.fields.get(GlobalFieldValues.CONTENT).get(0);
	}
	
	/**
	 * @return @Post as a json string
	 */
	public String toJson() {
		Gson gson = new Gson();
		JsonElement tree = gson.toJsonTree(this);
		return gson.toJson(tree);
	}
	
	public Map<String,List<String>> fields() {
		return fields;
	}
	
	public String toString() {
		return this.toJson();
	}
	
	public static void main(String[] args) {
		
		Post p = new Post("FISH","WEASEL");
		p.put("WIGGLE", "CHICKEN");
		p.put("WIGGLE", "RABBIT");
		p.put("MOUSE", "CAT");
		
		String json = p.toJson();
		
		Gson gson = new Gson();
		Post po = gson.fromJson(json, Post.class);
		
		System.out.println(po.toString());
	}
	
}
