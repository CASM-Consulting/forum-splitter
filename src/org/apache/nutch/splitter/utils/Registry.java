package org.apache.nutch.splitter.utils;

// java imports
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

// logging imports
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.nutch.index.filter.split.IForumSplitterFactory;
import org.apache.nutch.parse.filter.IFilter;

// Reflections imports
import org.reflections.Reflections;

/**
 * Methods called at runtime to instantiate various classes according to runtime parameters/configuration
 * @author jp242
 */
public final class Registry {
	
	static {
		// Instantiate the forum splitter factories.
		factories = Registry.registerFactories();
		// Instantiate the filters
		filters = Registry.registerFilters();
	}

	private static final List<IForumSplitterFactory> factories;	// Splitters designed to parse specific forums
	private static final List<IFilter> filters;					// Filters to parse the page content
	
    private static final Log LOG = LogFactory.getLog(Registry.class);
    
    // Package and class variables for reflection based instantiation.
    private static final String FACTORY_PACKAGE = "org.apache.nutch.index.filter.splitter";
    private static final Class<IForumSplitterFactory> FACTORY_CLASS = IForumSplitterFactory.class;
    
    private static final String FILTER_PACKAGE = "org.apache.nutch.parse.filter";
    private static final Class<IFilter> FILTER_CLASS = IFilter.class;
    
    // Defines the delimiter and configuration parameter name expected when listing filters in the Nutch configuration file.
    private static final String VARIABLE = "forum.post.filter";
    private static final String DELIM = "|";
    
    //Defines the configuration parameter used to specify whether to skip indexing documents which do not contain forum posts
    private static final String NO_POSTS = "skip.no.posts";
    
    public static List<IFilter> filters() {
    	return filters;
    }
    
    public static List<IForumSplitterFactory> factories() {
    	return factories;
    }
    
    /**
     * @return A List of filters to use when extracting meta-data from web pages.
     */
	private static List<IFilter> registerFilters() {
		return instantiate(FILTER_PACKAGE,FILTER_CLASS);
	}
	
	/**
	 * @return A list of splitters used to identify and split forum posts on a web-page
	 */
	private static List<IForumSplitterFactory> registerFactories() {
		return instantiate(FACTORY_PACKAGE,FACTORY_CLASS);
	}
	
	/**
	 * @param pack
	 * @param insClass
	 * @return An instantiated list of all class sub-types contained in the given package
	 */
	private static <A> List<A> instantiate(final String pack, Class<A> insClass) {
		List<A> instances = new ArrayList<A>();
		Reflections reflections = new Reflections(pack);
		Set<Class<? extends A>> classes = reflections.getSubTypesOf(insClass);
		for(Class<? extends A> cls : classes) {
			try {
				instances.add(cls.newInstance());
			} catch (InstantiationException e) {
				LOG.fatal("FATAL: Could not instantiate the class: " + cls.toString());
			} catch (IllegalAccessException e) {
				LOG.fatal("FATAL: Illegal Access Exception when instantiating: " + cls.toString());
			}
		}
		return instances;
	}
	
	/**
	 * @param conf Configuration containing runtime parameters
	 * @return Set of all requested filters in the Nutch configuration
	 */
	public static Set<String> configuredFilters(Configuration conf) {
		return new HashSet<String>(Arrays.asList(conf.get(VARIABLE).split(DELIM)));
	}
	
	/**
	 * @param conf Configuration containing runtime parameters
	 * @return Returns true if the the Nutch configuration specifies to skip indexing documents which do not contain forum posts. Defaults to false if not configured.
	 */
	public static boolean skipPosts(Configuration conf) {
		return Boolean.parseBoolean(conf.get(NO_POSTS, "false"));
	}
	
}
