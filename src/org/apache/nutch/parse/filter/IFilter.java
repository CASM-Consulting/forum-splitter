package org.apache.nutch.parse.filter;

import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.protocol.Content;

/**
 * Represents a single content filter used to extract/add meta-data to the document.
 * Must have an empty constructor to allow instantiation through reflection.
 * @author jp242
 *
 */
public interface IFilter {
	
	/**
	 * Parses the content, adding meta-data to the document
	 * @param content The input @Content for easy chaining.
	 * @return
	 */
	public void parseContent(Content content, Metadata metaData);
	
	
	/**
	 * @return A name assigned to the filter to allow filter options to be selected at runtime.
	 */
	public String name();

}
