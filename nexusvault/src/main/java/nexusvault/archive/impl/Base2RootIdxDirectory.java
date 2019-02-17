package nexusvault.archive.impl;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import kreed.io.util.BinaryReader;
import kreed.io.util.Seek;
import nexusvault.archive.IdxDirectory;
import nexusvault.archive.struct.StructIdxDirectory;
import nexusvault.archive.struct.StructIdxFile;

@Deprecated
final class Base2RootIdxDirectory extends Base2IdxDirectory {

	public Base2RootIdxDirectory(long subDirectoryHeaderIdx) {
		super(null, "", subDirectoryHeaderIdx);
	}

	protected void buildFileTree(Index2File indexFile, BinaryReader reader) {
		final Queue<Base2IdxDirectory> fringe = new LinkedList<>();
		fringe.add(this);

		while (!fringe.isEmpty()) {
			final Base2IdxDirectory directory = fringe.poll();
			loadDirectory(indexFile, reader, directory);
			for (final IdxDirectory subDirs : directory.getSubDirectories()) {
				fringe.add((Base2IdxDirectory) subDirs);
			}
		}
	}

	private void loadDirectory(Index2File indexFile, BinaryReader reader, Base2IdxDirectory parent) {
		final PackHeader packheader = indexFile.getPackHeader(parent.packHeaderIdx);
		reader.seek(Seek.BEGIN, packheader.getOffset());

		final long numSubDirectories = reader.readUInt32();
		final long numFiles = reader.readUInt32();
		final String nameTwine = extractNames(reader, packheader.getSize(), numSubDirectories, numFiles);

		final List<Base2IdxEntry> entries = new ArrayList<>();

		for (int i = 0; i < numSubDirectories; ++i) {
			final long subDirNameOffset = reader.readUInt32();
			final long subDirectoryHeaderIdx = reader.readUInt32();

			final int nullTerminator = nameTwine.indexOf(0, (int) subDirNameOffset);
			final String subDirName = nameTwine.substring((int) subDirNameOffset, nullTerminator);

			final Base2IdxDirectory directory = new Base2IdxDirectory(parent, subDirName, subDirectoryHeaderIdx);
			entries.add(directory);
		}

		for (long i = 0; i < numFiles; ++i) {
			final long fileNameOffset = reader.readUInt32(); // o: 4
			final int flags = reader.readInt32(); // o: 8
			final long writeTime = reader.readInt64(); // o: 16
			final long uncompressedSize = reader.readInt64(); // o: 24
			final long compressedSize = reader.readInt64(); // o: 32
			final byte[] hash = new byte[20];
			reader.readInt8(hash, 0, hash.length);
			final int unk1 = reader.readInt32(); // o: 56 // Prob. filled with trash data

			final int nullTerminator = nameTwine.indexOf(0, (int) fileNameOffset);
			final String fileName = nameTwine.substring((int) fileNameOffset, nullTerminator);

			final Base2IdxFileLink file = new Base2IdxFileLink(parent, fileName, flags, writeTime, uncompressedSize, compressedSize, hash, unk1);
			entries.add(file);
		}

		parent.setChilds(entries);
	}

	private String extractNames(BinaryReader reader, long blockSize, long numSubDirectories, long numFiles) {
		final long dataPosition = reader.getPosition();
		final long nameOffset = (numSubDirectories * StructIdxDirectory.SIZE_IN_BYTES) + (numFiles * StructIdxFile.SIZE_IN_BYTES);
		final long nameLengthInByte = blockSize - (2 * Integer.BYTES) - nameOffset;
		reader.seek(Seek.CURRENT, nameOffset);
		final byte[] entryNameAsBytes = new byte[(int) nameLengthInByte];
		reader.readInt8(entryNameAsBytes, 0, entryNameAsBytes.length);
		final String nameTwine = new String(entryNameAsBytes, Charset.forName("UTF8"));
		reader.seek(Seek.BEGIN, dataPosition);
		return nameTwine;
	}

}
