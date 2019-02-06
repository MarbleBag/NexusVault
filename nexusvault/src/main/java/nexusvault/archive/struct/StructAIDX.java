package nexusvault.archive.struct;

import kreed.reflection.struct.DataType;
import kreed.reflection.struct.StructField;
import kreed.reflection.struct.StructUtil;

public final class StructAIDX {
	public static final int SIZE_IN_BYTES = StructUtil.sizeOf(StructAIDX.class);

	@StructField(DataType.BIT_32)
	public  int signature;
	
	@StructField(DataType.BIT_32)
	public  int version;
	
	@StructField(DataType.BIT_32)
	public  int unknown1;
	
	@StructField(DataType.BIT_32)
	public  int rootPackHeaderIdx;



	@Override
	public String toString() {
		return "StructAIDX [signature=" + signature + ", version=" + version + ", unknown1=" + unknown1 + ", rootPackHeaderIdx=" + rootPackHeaderIdx + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + rootPackHeaderIdx;
		result = (prime * result) + signature;
		result = (prime * result) + unknown1;
		result = (prime * result) + version;
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
		final StructAIDX other = (StructAIDX) obj;
		if (rootPackHeaderIdx != other.rootPackHeaderIdx) {
			return false;
		}
		if (signature != other.signature) {
			return false;
		}
		if (unknown1 != other.unknown1) {
			return false;
		}
		if (version != other.version) {
			return false;
		}
		return true;
	}

}