package org.openzen.zencode.runtime;

import org.openzen.zencode.annotations.ZenGetter;

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

	@Override
	public int hashCode() {
		int hash = 3;
		hash = 61 * hash + this.from;
		hash = 61 * hash + this.to;
		return hash;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Range other = (Range) obj;
		if (this.from != other.from) {
			return false;
		}
		if (this.to != other.to) {
			return false;
		}
		return true;
	}
}
