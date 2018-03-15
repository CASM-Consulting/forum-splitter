package org.apache.nutch.parse.local;

import com.beust.jcommander.JCommander;
// Jcommander input parser imports
import com.beust.jcommander.Parameter;

// jsoup doc parsing imports
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

//opencsv - csv writing imports
import au.com.bytecode.opencsv.CSVWriter;

//java imports
import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.io.FileWriter;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.nutch.parse.filter.Post;
import org.apache.nutch.parse.forum.splitter.IForumSplitter;
import org.apache.nutch.parse.forum.splitter.IForumSplitterFactory;
import org.apache.nutch.splitter.utils.Registry;

/**
 * A local implementation of the forum scraper to be used outside of Nutch
 * Currently very limited functionality that does not make use of the filter system. 
 * @author jack
 */
public class LocalForumScraper {
	
	private final File inputDir;
	private final String domain;
	private final String output;
	private String[] headers;
	
	public LocalForumScraper(String[] args){
		InputParser ip = new InputParser();
		JCommander.newBuilder()
			.addObject(ip)
			.build()
			.parse(args);
		
		this.inputDir = new File(ip.localDir());
		this.domain = ip.domain();
		this.output = ip.output();
		this.headers = ip.headers().toArray(new String[ip.headers().size()]);
	}
	
	/**
	 * Scrapes a directory of local stored html files and writes out to csv
	 * @throws IOException
	 */
	public void scrapeDirectory() throws IOException {
		CSVWriter writer = new CSVWriter(new FileWriter(output));
		Iterator<File> files = FileUtils.iterateFiles(inputDir, new IOFileFilter(){

			@Override
			public boolean accept(File dir, String name) {
				// TODO Auto-generated method stub
				return name.contains("html");
			}

			@Override
			public boolean accept(File file) {
				// TODO Auto-generated method stub
				return file.getName().contains("html");
			}},new IOFileFilter(){

				@Override
				public boolean accept(File pathname) {
					// TODO Auto-generated method stub
					return true;
				}

				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return true;
				}});
		
		while(files.hasNext()) {
			File next = files.next();
			LinkedList<Post> posts = scrapeDocument(next);
			
			// Setup csv header if not already 
			if(this.headers == null || this.headers.length <= 0) {
				headers = posts.get(0).keySet().toArray(new String[posts.get(0).keySet().size()]);
				writer.writeNext(headers);
			}
			
			// correctly order and write the content to the csv
			for(Post post : posts) {
				String[] row = new String[headers.length];
				for(int i = 0; i < row.length; i++) {
					row[i] = post.get(headers[i]).get(0);
				}
				writer.writeNext(row);
			}
		}
		writer.close();
	}
	
	/**
	 * Passes a locally stored web-page through the relevant  
	 * @param htmlFile
	 * @return
	 * @throws IOException
	 */
	public LinkedList<Post> scrapeDocument(File htmlFile) throws IOException {
		
	    // Parse to jsoup Document for further parsing -  ignores DocumentFragment (doc) as standard java DOM navigation sucks.
		Document doc = Jsoup.parse(htmlFile, "UTF-8");
		
	    // Done outside of filter phase to ensure content is available to subsequent filters.
    	LinkedList<Post> posts = new LinkedList<Post>();
		
	    for(IForumSplitterFactory fact : Registry.factories()) {
			// scrape the content using the correct parser - specified by domain
			if(!fact.correctDomain(domain)) {
				continue;
			}
	    	final IForumSplitter fs = fact.create();
		    posts = fs.split(doc);
		}
	    
		return posts;
	}
	
	
	
	public class InputParser {
		
		@Parameter(names = {"-i","--input-dir"},description="Local directory containing html files for scraping.",
		required=true)
		private String localDir;
		
		@Parameter(names = {"-o","--output-csv"},description="The abs path for the output csv.",
		required=true)
		private String output;
		
		@Parameter(names = {"-d","--domain"},description = "The domain from which the pages originated to ensure the correct scraper is used",
		required=true)
		private String domain;
		
		@Parameter(names = {"-h","--headers"},description = "Optional param allowing header/content specification")
		private List<String> headers;
		
		public String localDir() {
			return localDir;
		}
		
		public String domain() {
			return domain;
		}
		
		public String output() {
			return output;
		}
		
		public List<String> headers() {
			return headers;
		}
		
	}
	
	
	public static void main(String[] args) {
		LocalForumScraper lfs = new LocalForumScraper(args);
		try {
			lfs.scrapeDirectory();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
