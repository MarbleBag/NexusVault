package nexusvault.format.tex.jpg;

import nexusvault.format.tex.ImageMetaInformation;
import nexusvault.format.tex.struct.StructTextureFileHeader;

/**
 * Simple, clean code to decode ws jpgs without chroma subsampling.
 *
 */
public final class JPGDecoderNoSampling extends JPGDecoderBase {

	private int stacksPerRow;
	private int stacksPerColumn;
	private int numberOfDecodes;

	public JPGDecoderNoSampling(PixelCompositionStrategy pixelCompositor) {
		super(pixelCompositor);
	}

	@Override
	protected final void loadFormatSpecificConfig(StructTextureFileHeader header, ImageMetaInformation meta) {
		stacksPerRow = ((meta.width + Constants.BLOCK_WIDTH) - 1) / Constants.BLOCK_WIDTH;
		stacksPerColumn = ((meta.height + Constants.BLOCK_HEIGHT) - 1) / Constants.BLOCK_HEIGHT;
		numberOfDecodes = stacksPerRow * stacksPerColumn;
	}

	@Override
	final long getNumberOfDecodableStacks() {
		return numberOfDecodes;
	}

	@Override
	final StackSet getFreeStack() { // TODO
		final StackSet stack = new StackSet(4);
		return stack;
	}

	@Override
	final void returnStack(StackSet stack) {
		// TODO Auto-generated method stub
	}

	@Override
	final void decodeStack(StackSet stack) {
		final int[] data = stack.data;
		for (int layerId = 0; layerId < Constants.NUMBER_OF_LAYERS; ++layerId) {
			if (!hasLayerDefaultValue(layerId)) {
				decodeNextBlock(getLayerType(layerId));
				transferDecodeToData(layerId, data, layerId * Constants.BLOCK_SIZE);
			} else {
				final int layerStart = layerId * Constants.BLOCK_SIZE;
				final int layerEnd = layerStart + Constants.BLOCK_SIZE;
				final int defaultValue = getLayerDefaultValue(layerId);
				data[layerStart] = defaultValue;
				for (int i = layerStart + 1; i < layerEnd; i += i) {
					System.arraycopy(data, layerStart, data, i, ((layerEnd - i) < i) ? (layerEnd - i) : i);
				}
			}
		}
	}

	@Override
	final void processStack(StackSet stack) {
		// threadsafe version, a bit heavy on the continues ressource allocation?
		processBlockStack(stack, null);
	}

	private void processBlockStack(StackSet stack, int[] tmpIDCTBuffer) {
		dequantizate(stack);
		inverseDCT(stack, tmpIDCTBuffer);
		shiftAndClamp(stack);
	}

	private void dequantizate(StackSet stack) {
		final int[] data = stack.data;
		for (int layerId = 0; layerId < Constants.NUMBER_OF_LAYERS; ++layerId) {
			if (hasLayerDefaultValue(layerId)) {
				continue;
			}
			final int blockoffset = layerId * Constants.BLOCK_SIZE;
			dequantizate(layerId, data, blockoffset);
		}
	}

	private void inverseDCT(StackSet stack, int[] inverseDCTBuffer) {
		final int[] data = stack.data;
		for (int layerId = 0; layerId < Constants.NUMBER_OF_LAYERS; ++layerId) {
			if (hasLayerDefaultValue(layerId)) {
				continue;
			}
			final int blockOffset = layerId * Constants.BLOCK_SIZE;
			inverseDCT(data, blockOffset, inverseDCTBuffer);
		}
	}

	private void shiftAndClamp(StackSet stack) {
		final int[] data = stack.data;
		for (int layerId = 0; layerId < Constants.NUMBER_OF_LAYERS; ++layerId) {
			if (hasLayerDefaultValue(layerId)) {
				continue;
			}
			final int blockoffset = layerId * Constants.BLOCK_SIZE;
			shiftAndClamp(layerId, data, blockoffset);
		}
	}

	@Override
	final void writeStack(StackSet stack) {
		final int stackX = stack.getId() % stacksPerRow;
		final int stackY = stack.getId() / stacksPerRow;
		final int offsetX = stackX * Constants.BLOCK_WIDTH;
		final int offsetY = stackY * Constants.BLOCK_HEIGHT * image.getImageWidth();
		final int startPixelXY = offsetX + offsetY;
		final int lastImagePixelXY = image.getImageHeight() * image.getImageWidth();

		int pixelYOffset = startPixelXY;
		int blockXY = 0;
		for (int y = 0; y < Constants.BLOCK_HEIGHT; ++y) {
			if (pixelYOffset >= lastImagePixelXY) {
				break; // do not compute pixels outside of the image.
			}
			blockXY = y * Constants.BLOCK_WIDTH;

			for (int x = 0; x < Constants.BLOCK_WIDTH; ++x) {
				final int pixelXY = x + pixelYOffset;
				if ((pixelXY >= lastImagePixelXY) || ((offsetX + x) >= image.getImageWidth())) {
					break; // do not compute pixels outside of the image.
				}

				final int pixelLayerA = stack.data[blockXY + (0 * Constants.BLOCK_SIZE)];
				final int pixelLayerB = stack.data[blockXY + (1 * Constants.BLOCK_SIZE)];
				final int pixelLayerC = stack.data[blockXY + (2 * Constants.BLOCK_SIZE)];
				final int pixelLayerD = stack.data[blockXY + (3 * Constants.BLOCK_SIZE)];

				getPixelCompositor().composite(image.getImageData(), pixelXY * 4, pixelLayerA, pixelLayerB, pixelLayerC, pixelLayerD);
				blockXY += 1;
			}
			pixelYOffset += image.getImageWidth();
		}
	}

}
