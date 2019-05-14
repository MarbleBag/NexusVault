package nexusvault.format.m3;

public interface ModelBone {

	int getBoneIndex();

	/**
	 * @return x - absolute location
	 */
	float getLocationX();

	/**
	 * @return y - absolute location
	 */
	float getLocationY();

	/**
	 * @return z - absolute location
	 */
	float getLocationZ();

	boolean hasParentBone();

	/**
	 * This method may return <code>-1</code> in case {@link #hasParentBone()} returned <code>false</code>.
	 *
	 * @return index to find its parent bone in {@link Model#getBones()}
	 */
	int getParentBoneReference();

	float[] getTransformationMatrix(int idx);

}
