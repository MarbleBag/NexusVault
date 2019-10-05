package nexusvault.format.tex.jpg;

import java.util.Objects;

import nexusvault.format.tex.jpg.tool.Constants;
import nexusvault.format.tex.jpg.tool.StackSet;

final class BaseStackWriter implements ImageRegionWriter {

	private int imageWidth;
	private int imageHeight;
	private int stacksPerRow;
	private int stacksPerColumn;
	private int numberOfSteps;

	private final PixelCompositionStrategy pixelComposition;

	public BaseStackWriter(PixelCompositionStrategy pixelComposition) {
		this.pixelComposition = Objects.requireNonNull(pixelComposition);
	}

	@Override
	public void setImageSize(int imageWidth, int imageHeight) {
		this.imageWidth = imageWidth;
		this.imageHeight = imageHeight;
		stacksPerRow = ((imageWidth + Constants.BLOCK_WIDTH) - 1) / Constants.BLOCK_WIDTH;
		stacksPerColumn = ((imageHeight + Constants.BLOCK_HEIGHT) - 1) / Constants.BLOCK_HEIGHT;
		numberOfSteps = stacksPerRow * stacksPerColumn;
	}

	@Override
	public int getNumberOfSteps() {
		return numberOfSteps;
	}

	@Override
	public void writeImageRegion(StackSet stack, byte[] output) {
		final int stackX = stack.getId() % stacksPerRow;
		final int stackY = stack.getId() / stacksPerRow;
		final int offsetX = stackX * Constants.BLOCK_WIDTH;
		final int offsetY = stackY * Constants.BLOCK_HEIGHT * imageWidth;
		final int startPixelXY = offsetX + offsetY;
		final int lastImagePixelXY = imageHeight * imageWidth;

		int pixelYOffset = startPixelXY;
		int blockXY = 0;
		for (int y = 0; y < Constants.BLOCK_HEIGHT; ++y) {
			if (pixelYOffset >= lastImagePixelXY) {
				break; // do not compute pixels outside of the image.
			}
			blockXY = y * Constants.BLOCK_WIDTH;

			for (int x = 0; x < Constants.BLOCK_WIDTH; ++x) {
				final int pixelXY = x + pixelYOffset;
				if ((pixelXY >= lastImagePixelXY) || ((offsetX + x) >= imageWidth)) {
					break; // do not compute pixels outside of the image.
				}

				final int pixelLayerA = stack.data[blockXY + (0 * Constants.BLOCK_SIZE)];
				final int pixelLayerB = stack.data[blockXY + (1 * Constants.BLOCK_SIZE)];
				final int pixelLayerC = stack.data[blockXY + (2 * Constants.BLOCK_SIZE)];
				final int pixelLayerD = stack.data[blockXY + (3 * Constants.BLOCK_SIZE)];

				pixelComposition.composite(output, pixelXY * 4, pixelLayerA, pixelLayerB, pixelLayerC, pixelLayerD);
				blockXY += 1;
			}
			pixelYOffset += imageWidth;
		}
	}

}
