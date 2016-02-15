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
		if(metaData.get(GlobalFieldValues.PAGE_START) == null) {
			final String[] urlS = content.getUrl().split("\\?", 2);
			if(urlS.length > 1) {
				// hard-coded byte array check as Lucene has a size limit - (do not want text-general field type as limited field size helps prevent
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
		else{
		    final Integer start = Integer.parseInt(metaData.get(GlobalFieldValues.PAGE_START));
		    if(start == 0 && metaData.get(GlobalFieldValues.CONTENT).getBytes().length < 32766) {
		    	metaData.add(name(), metaData.get(GlobalFieldValues.CONTENT));
		    }
		}
	}

	@Override
	public String name() {
		return GlobalFieldValues.QUESTION;
	}

}
