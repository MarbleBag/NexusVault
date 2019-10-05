package nexusvault.format.tex.unc;

import nexusvault.format.tex.ImageMetaCalculator;
import nexusvault.format.tex.ImageMetaInformation;
import nexusvault.format.tex.struct.StructTextureFileHeader;

final class UncompressedImageMetaCalculator implements ImageMetaCalculator {

	private final int bytesPerPixel;

	public UncompressedImageMetaCalculator(int bytesPerPixel) {
		this.bytesPerPixel = bytesPerPixel;
	}

	@Override
	public ImageMetaInformation getImageInformation(StructTextureFileHeader header, int idx) {
		if (header == null) {
			throw new IllegalArgumentException("'header' must not be null");
		}
		if ((idx < 0) || (header.mipMaps <= idx)) {
			throw new IndexOutOfBoundsException(String.format("'idx' out of bounds. Range [0;%d). Got %d", header.mipMaps, idx));
		}

		int width = 0, height = 0, length = 0, offset = 0;
		for (int i = 0; i <= idx; ++i) {
			width = (int) (header.width / Math.pow(2, header.mipMaps - 1 - i));
			height = (int) (header.height / Math.pow(2, header.mipMaps - 1 - i));
			width = width <= 0 ? 1 : width;
			height = height <= 0 ? 1 : height;

			offset += length;
			length = width * height * bytesPerPixel;
		}

		return new ImageMetaInformation(offset, length, width, height);
	}

}
