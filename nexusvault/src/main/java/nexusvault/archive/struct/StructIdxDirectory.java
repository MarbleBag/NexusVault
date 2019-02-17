package nexusvault.archive.struct;

import kreed.reflection.struct.DataType;
import kreed.reflection.struct.Order;
import kreed.reflection.struct.StructField;
import kreed.reflection.struct.StructUtil;

public final class StructIdxDirectory extends StructIdxEntry {

	public static final int SIZE_IN_BYTES = StructUtil.sizeOf(StructIdxDirectory.class);

	@Order(1)
	@StructField(DataType.UBIT_32)
	public long nameOffset; // 0x000

	@Order(2)
	@StructField(DataType.UBIT_32)
	public int directoryHeaderIdx; // 0x004

	public String name;

}
