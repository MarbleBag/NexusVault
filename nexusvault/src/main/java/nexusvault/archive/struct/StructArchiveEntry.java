package nexusvault.archive.struct;

import java.util.Arrays;
import java.util.Objects;

import kreed.reflection.struct.DataType;
import kreed.reflection.struct.Order;
import kreed.reflection.struct.StructField;
import kreed.reflection.struct.StructUtil;

public final class StructArchiveEntry {

	public static final int SIZE_IN_BYTES = StructUtil.sizeOf(StructArchiveEntry.class);

	@Order(1)
	@StructField(DataType.UBIT_32)
	public long packIdx;

	@Order(2)
	@StructField(value = DataType.BIT_8, length = 20)
	public byte[] hash;

	@Order(3)
	@StructField(DataType.UBIT_64)
	public long size;

	public StructArchiveEntry() {

	}

	/**
	 * @param packIdx
	 *            reference to the pack which belongs to this entry
	 * @param hash
	 *            is used to identify a entry
	 * @param size
	 *            of the data in bytes, which is stored under <code>packIdx</code>
	 *
	 * @throws IllegalArgumentException
	 *             if <code>hash</code> is null
	 */
	public StructArchiveEntry(long packIdx, byte[] hash, long size) {
		super();
		if (hash == null) {
			throw new IllegalArgumentException("'hash' must not be null");
		}

		this.packIdx = packIdx;
		this.size = size;
		this.hash = new byte[hash.length];
		System.arraycopy(hash, 0, this.hash, 0, hash.length);
	}

	@Override
	public String toString() {
		return "StructArchiveEntry [blockIndex=" + packIdx + ", shaHash=" + Arrays.toString(hash) + ", size=" + size + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + Arrays.hashCode(hash);
		result = (prime * result) + Objects.hash(packIdx, size);
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
		return Arrays.equals(hash, other.hash) && (packIdx == other.packIdx) && (size == other.size);
	}

}
