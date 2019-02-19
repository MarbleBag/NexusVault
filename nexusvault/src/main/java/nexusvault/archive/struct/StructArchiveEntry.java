package nexusvault.archive.struct;

import java.util.Arrays;

import kreed.reflection.struct.DataType;
import kreed.reflection.struct.Order;
import kreed.reflection.struct.StructField;

public final class StructArchiveEntry {

	@Order(1)
	@StructField(DataType.BIT_32)
	public long headerIdx;

	@Order(1)
	@StructField(value = DataType.BIT_8, length = 20)
	public byte[] shaHash;

	@Order(1)
	@StructField(DataType.BIT_32)
	public long size;

	public StructArchiveEntry() {

	}

	public StructArchiveEntry(long blockIndex, byte[] shaHash, long byteOfContent) {
		super();
		this.headerIdx = blockIndex;
		this.size = byteOfContent;
		this.shaHash = new byte[shaHash.length];
		System.arraycopy(shaHash, 0, this.shaHash, 0, shaHash.length);
	}

	@Override
	public String toString() {
		return "StructArchiveEntry [blockIndex=" + headerIdx + ", shaHash=" + Arrays.toString(shaHash) + ", size=" + size + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + (int) (headerIdx ^ (headerIdx >>> 32));
		result = (prime * result) + Arrays.hashCode(shaHash);
		result = (prime * result) + (int) (size ^ (size >>> 32));
		return result;
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
		final StructArchiveEntry other = (StructArchiveEntry) obj;
		if (headerIdx != other.headerIdx) {
			return false;
		}
		if (!Arrays.equals(shaHash, other.shaHash)) {
			return false;
		}
		if (size != other.size) {
			return false;
		}
		return true;
	}

}
