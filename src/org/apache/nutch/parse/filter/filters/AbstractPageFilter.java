package org.apache.nutch.parse.filter.filters;

// logging imports
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

//nutch imports
import org.apache.nutch.metadata.Metadata;
import org.apache.nutch.protocol.Content;

/**
 * Retrieves a pagination value. Defaults to 0 if none found.
 * @author jp242
 */
public abstract class AbstractPageFilter implements IPageFilter {
	
    private static final Log LOG = LogFactory.getLog(AbstractPageFilter.class);
    
	@Override
	public void parseContent(Content content, Metadata metaData) {
	    final String[] urlS = content.getUrl().split("\\?", 2);
	    if(urlS.length > 1 && urlS[1].contains(splitRegex())) {
    		final Integer page = parsePageNumber(urlS[1].split(splitRegex())[1]);
    		if(page != null) {
        		metaData.add(name(), page.toString());
        		return;
    		}
	    }
	    metaData.add(name(), String.valueOf(0));
	}
	
	/**
	 * Returns the regex to use when splitting the url string.
	 * @return
	 */
	protected abstract String splitRegex();
	
	/**
	 * Splits any trailing text from what is assumed to be a @String beginning with integers.
	 * @param url String which starts with integers and may contain trailing alpha-numeric characters
	 * @return The integer split from the String
	 */
	private Integer parsePageNumber(String url) {
		try {
		    final Integer start = Integer.parseInt(url.split("[^0-9]",2)[0]);
    		return start;
		} catch (NumberFormatException e) {
			LOG.warn("WARN: Unable to parse the pagination value");
		}
		return null;
	}

}
