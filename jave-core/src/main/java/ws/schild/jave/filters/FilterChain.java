package ws.schild.jave.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>A filterchain as described by <a href="https://ffmpeg.org/ffmpeg-filters.html#Filtergraph-syntax-1">
 * FFMPEG Documentation</a>.</p>
 * 
 * <p>A filterchain is a comma separated series of filters.</p>
 * 
 * @author mressler
 *
 */
public class FilterChain implements VideoFilter {

	private List<Filter> filters;
	
	/**
	 * Create an empty filterchain.
	 */
	public FilterChain() {
		filters = new ArrayList<>();
	}
	/**
	 * Create a filterchain with the specified filters
	 * @param filters The ordered list of filters in this chain
	 */
	public FilterChain(Filter... filters) {
		this.filters = Arrays.asList(filters);
	}
	
	/**
	 * Add one Filter to this filterchain
	 * @param filter The Filter to add to this chain.
	 * @return this FilterChain for builder pattern magic
	 */
	public FilterChain addFilter(Filter filter) {
		filters.add(filter);
		return this;
	}
	
	@Override
	public String getExpression() {
		return filters.stream()
			.map(VideoFilter::getExpression)
			.collect(Collectors.joining(","));
	}

}
