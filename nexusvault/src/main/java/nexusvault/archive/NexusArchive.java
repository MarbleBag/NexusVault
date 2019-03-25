package nexusvault.archive;

import java.io.IOException;
import java.nio.file.Path;

import nexusvault.archive.impl.BaseNexusArchiveReader;
import nexusvault.archive.util.IdxFileCollector;

public interface NexusArchive {

	/**
	 * Creates a default {@link NexusArchive} for the given file. If an archive file is given, the archive will look for an index file with the same name and
	 * vice versa.
	 *
	 * @param archiveOrIndex
	 *            the location of an archive or index file
	 * @return a archive which is build from the given source
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public static NexusArchive loadArchive(Path archiveOrIndex) throws IOException {
		return BaseNexusArchiveReader.loadArchive(archiveOrIndex);
	}

	/**
	 * Contains informations about the source of a {@link NexusArchive}
	 */
	public static interface NexusArchiveSource {
		Path getIndexFile();

		Path getArchiveFile();
	}

	void load(Path archiveOrIndex) throws IOException;

	void reload() throws IOException;

	/**
	 * The root directory of an archive has no name and serves as an anchor for the highest elements an archive contains.
	 *
	 * @return root directory of this archive
	 * @see IdxPath
	 * @see IdxFileCollector
	 * @throws NexusArchiveDisposedException
	 *             if the archive was disposed
	 */
	IdxDirectory getRootDirectory();

	/**
	 * Returns the current source of this NexusArchive. <br>
	 * This information will still be retrievable after this archive was {@link #dispose() disposed} and will be equal to the value of this method before the
	 * archive was disposed.
	 *
	 * @return the source of this archive
	 */
	NexusArchiveSource getSource();

	/**
	 * Instructs the archive to release all resources. Subsequent calls to this method should be safe. <br>
	 * <p>
	 * After a archive was disposed, it should not be used again.
	 */
	void dispose();

	/**
	 * @return <code>true</code> - if this archive was disposed. Calls to a disposed archive may cause undefined behavior
	 */
	boolean isDisposed();

}
