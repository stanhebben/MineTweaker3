package zenscript.runtime;

import zenscript.annotations.ZenGetter;

/**
 * 
 * @author Stan Hebben
 */
public class Range {
	private final int from;
	private final int to;
	
	public Range(int from, int to) {
		this.from = from;
		this.to = to;
	}
	
	@ZenGetter
	public int getFrom() {
		return from;
	}
	
	@ZenGetter
	public int getTo() {
		return to;
	}
}
