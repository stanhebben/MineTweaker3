/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.sync;

import java.util.Arrays;

/**
 *
 * @author Stan
 */
public class SyncKey {
	private final byte[] key;
	
	public SyncKey(byte[] key) {
		this.key = key;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = 79 * hash + Arrays.hashCode(this.key);
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
		final SyncKey other = (SyncKey) obj;
		if (!Arrays.equals(this.key, other.key)) {
			return false;
		}
		return true;
	}
}
