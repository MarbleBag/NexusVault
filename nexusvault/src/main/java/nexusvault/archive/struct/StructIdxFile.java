package nexusvault.archive.struct;

import kreed.reflection.struct.DataType;
import kreed.reflection.struct.Order;
import kreed.reflection.struct.StructField;
import kreed.reflection.struct.StructUtil;

public final class StructIdxFile extends StructIdxEntry {

	public static final int SIZE_IN_BYTES = StructUtil.sizeOf(StructIdxFile.class);

	@Order(1)
	@StructField(DataType.UBIT_32)
	public long nameOffset; // 0x000

	@Order(2)
	@StructField(DataType.BIT_32)
	public int flags; // 0x004

	@Order(3)
	@StructField(DataType.UBIT_64)
	public long writeTime; // 0x008

	@Order(4)
	@StructField(DataType.UBIT_64)
	public long uncompressedSize; // 0x010

	@Order(5)
	@StructField(DataType.UBIT_64)
	public long compressedSize; // 0x018

	@Order(6)
	@StructField(value = DataType.BIT_8, length = 20)
	public byte[] hash; // 0x020

	@Order(7)
	@StructField(DataType.BIT_32)
	public int unk_000; // 0x034

	public String name;
}