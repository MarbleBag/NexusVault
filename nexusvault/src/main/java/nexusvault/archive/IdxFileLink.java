package nexusvault.archive;

import java.io.IOException;
import java.nio.ByteBuffer;

public interface IdxFileLink extends IdxEntry {

	byte[] getHash();

	int getFlags();

	long getUncompressedSize();

	long getCompressedSize();

	String getFileEnding();

	String getNameWithoutFileExtension();

	ByteBuffer getData() throws IOException;

}