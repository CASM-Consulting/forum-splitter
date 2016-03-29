package org.apache.nutch.index.query;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.apache.nutch.parse.filter.ForumHTMLParseFilter;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.CursorMarkParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SolrStatsQueries {
	
    private static final Logger logger = LoggerFactory.getLogger(ForumHTMLParseFilter.class);
	private HttpSolrClient solr;
	private final int ROWS = 400;
	
	public SolrStatsQueries() {
		solr = new HttpSolrClient("http://localhost:8983/solr/nutch_example");
	}
	
	/**
	 * Performs preset/default query based on simple query string.
	 * @param query
	 * @throws PollingServiceException
	 */
	public void query(final String query) {
		query(query,null);
	}
	
	public Set<String> buildStopWordList() {
		StringWriter writer = new StringWriter();
		try {
			InputStream is = SolrStatsQueries.class.getClassLoader().getResourceAsStream("stopwords.txt");
			if(is == null) {
				System.out.println("is null");
			}
			IOUtils.copy(is, writer, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
		}
		String out = writer.toString().replace("\n\n", "\n");
		String[] strs = out.split("\n");
		return new HashSet<String>(Arrays.asList(strs));
		
	}
	
//	public boolean respond(QueryResponse rsp) {
//		Set<String> stopWords = buildStopWordList();
//		int lim = 100;
//		int i = 0;
//		int j = 0;
//		while(i < lim) {
//			Count count = rsp.getFacetField("postcontent").getValues().get(j);
//			if(!stopWords.contains(count.getName().toLowerCase().trim())) {
//				System.out.println(count.getName() + " " + count.getCount());
//				j++;
//				i++;
//			}
//			else{
//				j++;
//			}
//		}
//		return false;
//	}
	
	public boolean respond(QueryResponse rsp) {

		System.out.println("Unique members: " + rsp.getFacetField("member").getValueCount());
		
		double totalPosts = 0.0;
		double above50Quant = 0.0;
		
		double noPosts = 0;
		double lotPosts = 0;
		for(Count count : rsp.getFacetField("member").getValues()) {
			if(count.getCount() <= 1) {
				noPosts++;
			}
			if(count.getCount() >= 50) {
				lotPosts ++;
				above50Quant += count.getCount();
			}
			totalPosts += count.getCount();
			
		}
		System.out.println("Total posts: " + totalPosts);

		System.out.println("Single posters: " + noPosts);
		System.out.println("Percentage single posters: " + ((Double.valueOf(noPosts)/rsp.getFacetField("member").getValueCount()) * 100.0));
		
		System.out.println("People posting more than 50 times: " + lotPosts);
		System.out.println("Percentage members post more than 50 times: " + ((Double.valueOf(lotPosts)/rsp.getFacetField("member").getValueCount()) * 100.0));
		System.out.println("Total posts by those, posting more than 50 times: " + above50Quant);
		System.out.println("Percentage of posts submitted by those posting more than 50 times: " + (above50Quant/totalPosts) * 100);
		System.out.println("Ave. number of posts by those posting over 50 times: " + above50Quant/lotPosts);

		
		return false;
	}
	
//	public boolean respond(QueryResponse rsp) {
//		
//		int noResponse = 0;
//		System.out.println("Total threads: " + rsp.getFacetField("id").getValueCount());
//		for(Count count : rsp.getFacetField("id").getValues()) {
//			if(count.getCount() == 1) {
//				noResponse ++;
//			}
//		}
//		System.out.println("Total unresponded threads: " + noResponse);
//		return false;
//	}
	
	
	
	/**
	 * Performs a preset/default Solr query based on a query string and filter queries.
	 * @param query
	 * @param filterQueries
	 * @throws PollingServiceException
	 */
	public void query(final String query, final List<String> filterQueries) {
		// Create Solr query
		final SolrQuery q = new SolrQuery(query);
		q.setRows(ROWS)
			.setIncludeScore(true)
		    .addSort(SolrQuery.SortClause.asc("id")) // Ensures cursor string is unique for each state.
			.setStart(0);
		// Execute query
		query(q,filterQueries);
	}
	
	/**
	 * Performs a preset/default Solr query based on a query string and filter queries.
	 * @param query
	 * @param filterQueries
	 * @throws PollingServiceException
	 */
	public void queryUniqueMemberFacet(final String query, String facet,String... filt) {
		// Create Solr query
		final SolrQuery q = new SolrQuery(query);
		q.setFacet(true);
		q.setFacetLimit(-1);
		q.setFacetMinCount(1);
		q.setFacetSort("index");
		q.setFacetLimit(2000);
//		q.setFacetSort("count");
		q.addFacetField(facet);
		if(filt != null) {
			q.addFilterQuery(filt);
		}
		q.setRows(ROWS)
			.setIncludeScore(true)
		    .addSort(SolrQuery.SortClause.asc("id")) // Ensures cursor string is unique for each state.
			.setStart(0);
		// Execute query
		query(q,null);
	}
	
	/**
	 * Adds filter queries to the @SolrQuery and executes the query.
	 * @param query
	 * @param filterQueries
	 * @throws PollingServiceException
	 */
	public void query(final SolrQuery query, final List<String> filterQueries) {
		// Check fqs are valid and add them to the query
		if(filterQueries != null) {
			filterQueries.stream()
				.filter(s -> s.trim().length() > 0)
				.filter(s -> s != null)
				.map(s -> query.addFilterQuery(s));
		}
		// Execute query
		query(query);
	}
	
	/**
	 * Executes a @SolrQuery and delegates the resulting @QueryResponse objects to respond.
	 * Uses a cursor to paginate through query results. See https://cwiki.apache.org/confluence/display/solr/Pagination+of+Results for details on query construction.
	 * @param query
	 * @param filterQueries
	 * @throws PollingServiceException
	 */
	public void query(final SolrQuery query) {
		
		// Indicates start of cursor
		String cursorMark = CursorMarkParams.CURSOR_MARK_START;
		
		boolean pagedAll = false;
		// Cursor through the results
		try {
			// Setup cursor
			while(!pagedAll){
				
				//Execute query
				query.set(CursorMarkParams.CURSOR_MARK_PARAM, cursorMark);				
				QueryResponse rsp = solr.query(query);
				
				// Update the cursor state
				String nextCursor = rsp.getNextCursorMark();
				
				// Respond to the search results by implementing the respond method.
				final boolean more = respond(rsp);

				// Stop if the user is satisified and needs no more result processing 
				// or the cursor has reached the end of the document set.		
				if(!more || nextCursor.equals(cursorMark)) {
					pagedAll = true;
				}				
				else {
					cursorMark = nextCursor;
				}
			}
		} catch (SolrServerException e1) {
			logger.debug(e1.getMessage());
		} catch (IOException e) {
			logger.debug(e.getMessage());
		}
		
	}
	
	/**
	 * Shutdown the connection to the Solr server.
	 * @throws PollingServiceException 
	 */
	public void shutdown() {
		try {
			solr.close();
		} catch (IOException e) {
			logger.debug(e.getMessage());
		}
	}
	
	public static void main(String[] args) {
		SolrStatsQueries ssq = new SolrStatsQueries();
//		ssq.queryUniqueMemberFacet("id:*carersuk*", "baseurl","numposts:1");
		ssq.queryUniqueMemberFacet("id:*mentalhealthforum*", "member","member:*");
		//carers "-id:*start=*"
		//mental  "*id:s=*","-id:*-*.html*"
		// patient "id:*page=0*"

	}

}
