package org.apache.nutch.parse.filter.filters;

// nutch imports
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.protocol.Content;
import org.apache.nutch.splitter.utils.GlobalFieldValues;

/**
 * If the web page is the beginning of a thread as the first post as the assumed question being asked of the thread.
 * TODO: Messy class - is it needed?
 * @author jp242
 *
 */
public class QuestionFilter implements IPageFilter {
	
	@Override
	public void parseContent(Content content, Metadata metaData) {
		
		// Try to get the page value if it already exists
		try {
		    final Integer start = Integer.parseInt(metaData.get(GlobalFieldValues.PAGE_START));
		    if(start == 0 && metaData.get(GlobalFieldValues.CONTENT).getBytes().length < 32766) {
		    	metaData.add(name(), metaData.get(GlobalFieldValues.CONTENT));
		    }
		} catch (NumberFormatException e) {
			// Otherwise find out the page and if it is 0, add the first post as the question.
			final String[] urlS = content.getUrl().split("\\?", 2);
			if(urlS.length > 1) {
			    if(!urlS[1].contains("start=") && metaData.get(GlobalFieldValues.CONTENT).getBytes().length < 32766) {
			    	metaData.add(name(), metaData.get(GlobalFieldValues.CONTENT));
			    }
			}
			else{
			    if(metaData.get(GlobalFieldValues.CONTENT).getBytes().length < 32766) {
			    	metaData.add(name(), metaData.get(GlobalFieldValues.CONTENT));

			    }
			}
		}
	}

	@Override
	public String name() {
		return GlobalFieldValues.QUESTION;
	}

}
