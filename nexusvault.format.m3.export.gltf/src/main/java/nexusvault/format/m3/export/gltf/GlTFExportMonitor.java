package nexusvault.format.m3.export.gltf;

import java.nio.file.Path;

public interface GlTFExportMonitor {
	/**
	 * TODO<br>
	 * <b>WIP</b><br>
	 * Up on learning more about m3 materials, this method may change to encompass this
	 *
	 * @param textureId
	 *            path of the requested id
	 * @param resourceBundle
	 *            the bundle in which the texture should be stored
	 */
	void requestTextures(String textureId, ResourceBundle resourceBundle);

	void newFile(Path path);
}