package nexusvault.archive.impl;

import java.util.Arrays;

public final class IdxFileAttribute {

	private String name;
	private int flags;
	private long writeTime;
	private long uncompressedSize;
	private long compressedSize;
	private byte[] hash;
	private int unk_034;

	public IdxFileAttribute(String name, int flags, long writeTime, long uncompressedSize, long compressedSize, byte[] hash, int unk_034) {
		super();

		if (name == null) {
			throw new IllegalArgumentException("'name' must not be null");
		}
		if (hash == null) {
			throw new IllegalArgumentException("'hash' must not be null");
		}

		this.name = name;
		this.flags = flags;
		this.writeTime = writeTime;
		this.uncompressedSize = uncompressedSize;
		this.compressedSize = compressedSize;
		this.hash = hash;
		this.unk_034 = unk_034;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final IdxFileAttribute other = (IdxFileAttribute) obj;
		if (compressedSize != other.compressedSize) {
			return false;
		}
		if (flags != other.flags) {
			return false;
		}
		if (!Arrays.equals(hash, other.hash)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (uncompressedSize != other.uncompressedSize) {
			return false;
		}
		if (unk_034 != other.unk_034) {
			return false;
		}
		if (writeTime != other.writeTime) {
			return false;
		}
		return true;
	}

	public long getCompressedSize() {
		return compressedSize;
	}

	public int getFlags() {
		return flags;
	}

	public byte[] getHash() {
		return hash;
	}

	public String getName() {
		return name;
	}

	public long getUncompressedSize() {
		return uncompressedSize;
	}

	public int getUnk_034() {
		return unk_034;
	}

	public long getWriteTime() {
		return writeTime;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) (compressedSize ^ (compressedSize >>> 32));
		result = (prime * result) + flags;
		result = (prime * result) + Arrays.hashCode(hash);
		result = (prime * result) + ((name == null) ? 0 : name.hashCode());
		result = (prime * result) + (int) (uncompressedSize ^ (uncompressedSize >>> 32));
		result = (prime * result) + unk_034;
		result = (prime * result) + (int) (writeTime ^ (writeTime >>> 32));
		return result;
	}

	public void setCompressedSize(long compressedSize) {
		this.compressedSize = compressedSize;
	}

	public void setFlags(int flags) {
		this.flags = flags;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if <code>hash</code> is null
	 */
	public void setHash(byte[] hash) {
		if (hash == null) {
			throw new IllegalArgumentException("'hash' must not be null");
		}
		this.hash = hash;
	}

	/**
	 * @throws IllegalArgumentException
	 *             if <code>name</code> is null
	 */
	public void setName(String name) {
		if (name == null) {
			throw new IllegalArgumentException("'name' must not be null");
		}
		this.name = name;
	}

	public void setUncompressedSize(long uncompressedSize) {
		this.uncompressedSize = uncompressedSize;
	}

	public void setUnk_034(int unk_034) {
		this.unk_034 = unk_034;
	}

	public void setWriteTime(long writeTime) {
		this.writeTime = writeTime;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder();
		builder.append("IdxFileAttribute [name=");
		builder.append(name);
		builder.append(", flags=");
		builder.append(flags);
		builder.append(", writeTime=");
		builder.append(writeTime);
		builder.append(", uncompressedSize=");
		builder.append(uncompressedSize);
		builder.append(", compressedSize=");
		builder.append(compressedSize);
		builder.append(", hash=");
		builder.append(Arrays.toString(hash));
		builder.append(", unk_034=");
		builder.append(unk_034);
		builder.append("]");
		return builder.toString();
	}

}
