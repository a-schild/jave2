package ws.schild.jave.filters;

import java.util.List;

public class ConcatFilter extends Filter {

	public ConcatFilter(List<String> inputLabels) {
		super("concat");
		inputLabels.stream()
			.forEach(this::addInputLabel);
		addNamedArgument("n", Integer.toString(inputLabels.size()));
	}

}
