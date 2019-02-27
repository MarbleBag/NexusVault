package nexusvault.archive.impl;

import java.io.IOException;

import kreed.io.util.BinaryReader;

final class CachedIndexFile extends AbstIndexFile {

	private final BufferedFileAccessCache cache;

	public CachedIndexFile(BufferedFileAccessCache cache) {
		super();
		if (cache == null) {
			throw new IllegalArgumentException("'cache' must not be null");
		}
		this.cache = cache;
	}

	@Override
	public IndexDirectoryData getDirectoryData(int packIdx) throws IOException {
		final IndexDirectoryData data = super.getDirectoryData(packIdx);
		cache.startExpiring();
		return data;
	}

	@Override
	protected BinaryReader getBinaryReader() throws IOException {
		return cache.getFileReader();
	}

	@Override
	public boolean isDisposed() {
		return this.cache.isShutDown();
	}

	@Override
	public void dispose() {
		this.cache.shutDown();
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("CachedIndexFile [Source=");
		builder.append(cache.getSource());
		builder.append("]");
		return builder.toString();
	}

}