package nexusvault.format.m3.v100.struct;

import kreed.reflection.struct.DataType;
import kreed.reflection.struct.Order;
import kreed.reflection.struct.StructField;
import nexusvault.format.m3.v100.BytePositionTracker;
import nexusvault.format.m3.v100.StructVisitor;
import nexusvault.format.m3.v100.VisitableStruct;

public class StructUInt32 implements VisitableStruct {

	@Order(2)
	@StructField(value = DataType.UBIT_32)
	public long value; //

	@Override
	public void visit(StructVisitor process, BytePositionTracker fileReader, int dataPosition) {
	}

}
