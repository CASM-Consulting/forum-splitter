package org.apache.nutch.parse.filter;

import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

/**
 * Retrieves the subject of the forum post - taken as the n-1 fragment of the base url
 * @author jp242
 */
public class SubjectFilter implements IPageFilter {
	
	@Override
	public void parseContent(Content content, Metadata metaData) {
	    final String[] splitUrl = content.getBaseUrl().split("/");
	    metaData.add(name(), splitUrl[splitUrl.length-1]);
	}

	@Override
	public String name() {
		return GlobalFieldValues.SUBJECT;
	}

}
