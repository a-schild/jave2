package ws.schild.jave.filtergraphs;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import ws.schild.jave.filters.ConcatFilter;
import ws.schild.jave.filters.FilterChain;
import ws.schild.jave.filters.FilterGraph;
import ws.schild.jave.filters.MovieFilter;
import ws.schild.jave.filters.OverlayFilter;
import ws.schild.jave.filters.SetPtsFilter;
import ws.schild.jave.filters.TrimFilter;
import ws.schild.jave.filters.helpers.OverlayLocation;

/**
 * Trim and watermark any number of input videos.
 * @author mressler
 *
 */
public class TrimAndWatermark extends FilterGraph {
	
	public TrimAndWatermark(
		File watermark,
		Double trimStart, 
		Double trimDuration) 
	{
		super(
			new FilterChain(
				new TrimFilter("0", trimStart, trimDuration),
				new SetPtsFilter().addOutputLabel("main"),
				new MovieFilter(watermark, "watermark"),
				new OverlayFilter("main", "watermark", OverlayLocation.BOTTOM_RIGHT, -10, -10)
			)
		);
	}
	
	public TrimAndWatermark(
		File watermark,
		List<TrimInfo> trimInfo) 
	{
		super();
		
		IntStream.range(0, trimInfo.size())
			.mapToObj(i -> filterchainForInputIndex(i, trimInfo.get(i), watermark))
			.forEach(this::addChain);
		
		addChain(new FilterChain(new ConcatFilter(
			IntStream.range(0, trimInfo.size())
				.mapToObj(this::labelForOutput)
				.collect(Collectors.toList())
		)));
	}
	
	public static class TrimInfo {
		public Double trimStart;
		public Double trimDuration;

		public TrimInfo(Double trimStart, Double trimDuration) {
			this.trimStart = trimStart;
			this.trimDuration = trimDuration;
		}
	}
	
	private FilterChain filterchainForInputIndex(Integer i, TrimInfo info, File watermark) {
		return new FilterChain(
			new TrimFilter(i.toString(), info.trimStart, info.trimDuration),
			new SetPtsFilter().addOutputLabel("trimmed" + i),
			new MovieFilter(watermark), // Movie output is the second input to the overlay filter
			new OverlayFilter("trimmed" + i, OverlayLocation.BOTTOM_RIGHT, -10, -10)
				.addOutputLabel(labelForOutput(i))
		);
	}
	
	private String labelForOutput(Integer i) {
		return "overlayed" + i;
	}
	
}
