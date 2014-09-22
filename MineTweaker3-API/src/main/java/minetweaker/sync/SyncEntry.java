/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.sync;

/**
 *
 * @author Stan
 */
public class SyncEntry {
	private final SyncKey key;
	private final String filename;
	private final long dataOffset;
	private final long size;
	
	public SyncEntry(SyncKey key, String filename, long dataOffset, long size) {
		this.key = key;
		this.filename = filename;
		this.dataOffset = dataOffset;
		this.size = size;
	}
	
	public SyncKey getKey() {
		return key;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public long getDataOffset() {
		return dataOffset;
	}
	
	public long getSize() {
		return size;
	}
}
