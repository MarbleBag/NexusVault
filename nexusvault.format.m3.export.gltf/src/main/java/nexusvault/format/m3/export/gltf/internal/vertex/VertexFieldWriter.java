package nexusvault.format.m3.export.gltf.internal.vertex;

import kreed.io.util.BinaryWriter;
import nexusvault.format.m3.ModelVertex;
import nexusvault.format.m3.export.gltf.internal.GlTFComponentType;
import nexusvault.format.m3.export.gltf.internal.GlTFMeshAttribute;
import nexusvault.format.m3.export.gltf.internal.GlTFType;

/**
 * Abstraction to access various fields of a vertex.
 */
public abstract class VertexFieldWriter {
	private final GlTFComponentType componentType;
	private final GlTFType type;
	private final GlTFMeshAttribute attribute;
	private final String nameShort;
	private final int offsetWithinVertex;

	public VertexFieldWriter(String nameShort, GlTFComponentType componentType, GlTFType type, GlTFMeshAttribute attribute, int offsetWithinVertex) {
		super();
		this.nameShort = nameShort;
		this.componentType = componentType;
		this.type = type;
		this.attribute = attribute;
		this.offsetWithinVertex = offsetWithinVertex;
	}

	public GlTFComponentType getComponentType() {
		return this.componentType;
	}

	public GlTFType getType() {
		return this.type;
	}

	public String getAttributeKey() {
		return this.attribute.getAttributeKey();
	}

	public String getNameShort() {
		return this.nameShort;
	}

	public final int getSizeInBytes() {
		return this.componentType.getByteCount() * this.type.getComponentCount();
	}

	public final int getFieldOffset() {
		return this.offsetWithinVertex;
	}

	abstract protected void writeTo(BinaryWriter writer, ModelVertex vertex);

	public void resetField() {

	}

	public boolean hasMinimum() {
		return false;
	}

	public boolean hasMaximum() {
		return false;
	}

	public Number[] getMinimum() {
		return null;
	}

	public Number[] getMaximum() {
		return null;
	}

}