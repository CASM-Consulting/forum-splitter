package org.apache.nutch.parse.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.splitter.utils.GlobalFieldValues;
import org.apache.nutch.splitter.utils.IPageFilter;

/**
 * Retrieves the pagination value. Defaults to 0 if none found.
 * @author jp242
 *
 */
public class PageStartFilter implements IPageFilter {
	
    private static final Log LOG = LogFactory.getLog(ForumHTMLParseFilter.class);
    
	@Override
	public void parseContent(Content content, Metadata metaData) {
	    final String[] urlS = content.getUrl().split("\\?", 2);
	    if(urlS.length > 1 && urlS[1].contains("start=")) {
    		final String page = urlS[1].split("start=")[1];
    		try {
    		    final Integer start = Integer.parseInt(page.split("[^0-9]",2)[0]);
	    		metaData.add(name(), start.toString());
    		} catch (NumberFormatException e) {
    			LOG.warn("WARN: Unable to parse the pagination value");
    		}	    		
	    }
	    else {
	    	metaData.add(name(), String.valueOf(0));
	    }
	}

	@Override
	public String name() {
		return GlobalFieldValues.PAGE_START;
	}

}
