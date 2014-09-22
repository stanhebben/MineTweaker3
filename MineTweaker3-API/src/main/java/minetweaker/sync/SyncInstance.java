/*
 * This file is part of MineTweaker API, licensed under the MIT License (MIT).
 * 
 * Copyright (c) 2014 MineTweaker <http://minetweaker3.powerofbytes.com>
 */
package minetweaker.sync;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Stan
 */
public class SyncInstance {
	private final Map<SyncKey, SyncEntry> entries;
	private final RandomAccessFile dataFile;
	
	public SyncInstance(File indexFile, File dataFile) throws IOException {
		entries = new HashMap<SyncKey, SyncEntry>();
		this.dataFile = new RandomAccessFile(dataFile, "rwd");
		
		if (!indexFile.exists()) {
			indexFile.createNewFile();
		}

		if (!dataFile.exists()) {
			dataFile.createNewFile();
		}

		DataInputStream readIndex = new DataInputStream(new BufferedInputStream(new FileInputStream(indexFile)));
		String name = readIndex.readUTF();
		while (!name.isEmpty()) {
			long dataOffset = readIndex.readLong();
			long size = readIndex.readLong();

			byte[] filehash = new byte[readIndex.readInt()];
			readIndex.readFully(filehash);

			SyncKey key = new SyncKey(filehash);
			SyncEntry entry = new SyncEntry(key, name, dataOffset, size);
			entries.put(key, entry);

			name = readIndex.readUTF();
		}
	}
	
	public boolean contains(SyncKey key) {
		return entries.containsKey(key);
	}
	
	public InputStream open(SyncKey key) throws IOException {
		SyncEntry entry = entries.get(key);
		if (entry == null) {
			throw new FileNotFoundException();
		} else {
			synchronized (dataFile) {
				dataFile.seek(entry.getDataOffset());
				byte[] data = new byte[(int) entry.getSize()];
				dataFile.readFully(data);
				return new ByteArrayInputStream(data);
			}
		}
	}
}
