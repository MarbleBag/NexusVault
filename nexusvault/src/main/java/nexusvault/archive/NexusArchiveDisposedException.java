package nexusvault.archive;

public final class NexusArchiveDisposedException extends ArchiveException {

	private static final long serialVersionUID = -1L;

	public NexusArchiveDisposedException() {
		super();
	}

	public NexusArchiveDisposedException(String s) {
		super(s);
	}

	public NexusArchiveDisposedException(String message, Throwable cause) {
		super(message, cause);
	}

	public NexusArchiveDisposedException(Throwable cause) {
		super(cause);
	}

}
