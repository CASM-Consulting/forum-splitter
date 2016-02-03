package org.apache.nutch.parse.filter.filters;

// Nutch imports
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.protocol.Content;

import org.apache.nutch.splitter.utils.GlobalFieldValues;

/**
 * Retrieves the base URI - removing any queries.
 * @author jp242
 */
public class URLFilter implements IPageFilter {
	
	@Override
	public void parseContent(Content content, Metadata metaData) {
	    final String baseUrl = content.getBaseUrl().split("\\?")[0];
	    metaData.add(name(), baseUrl);
	}

	@Override
	public String name() {
		return GlobalFieldValues.BASE_URL;
	}

}
