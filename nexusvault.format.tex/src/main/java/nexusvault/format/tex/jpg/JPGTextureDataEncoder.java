package nexusvault.format.tex.jpg;

import nexusvault.format.tex.AbstTextureDataDecoder;
import nexusvault.format.tex.ImageMetaInformation;
import nexusvault.format.tex.TextureDataDecoder;
import nexusvault.format.tex.TextureImage;
import nexusvault.format.tex.TextureImageFormat;
import nexusvault.format.tex.TextureRawData;
import nexusvault.format.tex.struct.StructTextureFileHeader;

abstract class JPGTextureDataEncoder extends AbstTextureDataDecoder implements TextureDataDecoder {

	private final JPGDecoderBase decoder;

	public JPGTextureDataEncoder(JPGDecoderBase decoder) {
		if (decoder == null) {
			throw new IllegalArgumentException("'decoder' must not be null");
		}
		this.decoder = decoder;
	}

	@Override
	protected final TextureImage getImage(StructTextureFileHeader header, TextureRawData data, ImageMetaInformation meta, int idx) {
		return decoder.decodeImage(header, data, meta);
	}

	@Override
	public final TextureImageFormat getImageFormat() {
		return decoder.getImageFormat();
	}

	@Override
	protected ImageMetaInformation getImageInformation(StructTextureFileHeader header, int idx) {
		if (header == null) {
			throw new IllegalArgumentException("'header' must not be null");
		}
		if ((idx < 0) || (header.mipMaps <= idx)) {
			throw new IndexOutOfBoundsException(String.format("'idx' out of bounds. Range [0;%d). Got %d", header.mipMaps, idx));
		}
		if (header.mipMaps != header.imageSizesCount) {
			throw new IndexOutOfBoundsException(
					String.format("'imageSizesCount' and 'mipMaps' need to be qual. MipMaps: %d, ImageSizeCount:", header.mipMaps, header.imageSizesCount));
		}

		final int length = header.imageSizes[idx];
		int offset = 0;
		for (int i = 0; i < idx; ++i) {
			offset += header.imageSizes[i];
		}

		int width = header.width >> (header.mipMaps - idx - 1);
		int height = header.height >> (header.mipMaps - idx - 1);
		width = width <= 0 ? 1 : width;
		height = height <= 0 ? 1 : height;

		return new ImageMetaInformation(offset, length, width, height);
	}

}
