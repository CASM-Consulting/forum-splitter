package org.apache.nutch.parse.filter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

/**
 * If the web page is the beginning of a thread as the first post as the assumed question being asked of the thread.
 * TODO: Messy class - is it needed?
 * @author jp242
 *
 */
public class QuestionFilter implements IFilter {

    private static final Log LOG = LogFactory.getLog(ForumHTMLParseFilter.class);
	
	@Override
	public void parseContent(Content content, Metadata metaData) {
		
		// Try to get the page value if it already exists
		try {
		    final Integer start = Integer.parseInt(metaData.get(GlobalFieldValues.PAGE_START));
		    if(start == 0 && metaData.get(GlobalFieldValues.POST_FIELD).getBytes().length < 32766) {
		    	metaData.add(name(), metaData.get(GlobalFieldValues.POST_FIELD));
		    }
		} catch (NumberFormatException e) {
			// Otherwise find out the page and if it is 0, add the first post as the question.
			final String[] urlS = content.getUrl().split("\\?", 2);
		    if(urlS.length > 1 && urlS[1].contains("start=")) {
	    		final String page = urlS[1].split("start=")[1];
	    		try {
	    		    final Integer start = Integer.parseInt(page.split("[^0-9]",2)[0]);
	    		    if(start == 0 && metaData.get(GlobalFieldValues.POST_FIELD).getBytes().length < 32766) {
	    		    	metaData.add(name(), metaData.get(GlobalFieldValues.POST_FIELD));
	    		    }
	    		} catch (NumberFormatException e1) {
	    			LOG.warn("WARN: Unable to parse the pagination value");
	    		}	    		
		    }
		}
	}

	@Override
	public String name() {
		return GlobalFieldValues.QUESTION;
	}

}
