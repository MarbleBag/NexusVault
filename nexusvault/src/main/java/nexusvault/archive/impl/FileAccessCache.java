package nexusvault.archive.impl;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

final class FileAccessCache {

	static class DefaultThreadFactory implements ThreadFactory {
		private static final AtomicInteger poolNumber = new AtomicInteger(1);
		private final ThreadGroup group;
		private final AtomicInteger threadNumber = new AtomicInteger(1);
		private final String namePrefix;

		DefaultThreadFactory() {
			final SecurityManager s = System.getSecurityManager();
			group = (s != null) ? s.getThreadGroup() : Thread.currentThread().getThreadGroup();
			namePrefix = "pool-" + poolNumber.getAndIncrement() + "-thread-";
		}

		@Override
		public Thread newThread(Runnable r) {
			final Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
			t.setDaemon(true);
			if (t.getPriority() != Thread.NORM_PRIORITY) {
				t.setPriority(Thread.NORM_PRIORITY);
			}
			return t;
		}
	}

	private final long cacheTime;
	private final Path filePath;

	private final ExecutorService executor;

	private volatile long lastUsed;
	private volatile boolean expiring;
	private volatile boolean taskShutdown;

	private final Object lock = new Object();

	private SeekableByteChannel stream;
	private final EnumSet<StandardOpenOption> fileAccessOption;

	public FileAccessCache(long cacheTime, Path filePath, EnumSet<StandardOpenOption> fileAccessOption) {
		this.cacheTime = cacheTime;
		this.filePath = filePath;
		this.fileAccessOption = fileAccessOption;
		this.executor = new ThreadPoolExecutor(0, 1, 15L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>(), new DefaultThreadFactory());
		this.taskShutdown = true;
	}

	public void shutDown() {
		executor.shutdownNow();
	}

	public boolean isShutDown() {
		return executor.isShutdown();
	}

	public SeekableByteChannel getFileAccess() throws IOException {
		synchronized (lock) {
			this.lastUsed = System.currentTimeMillis();
			this.expiring = false;
			if ((stream == null) || !stream.isOpen()) {
				stream = Files.newByteChannel(filePath, fileAccessOption);
			}
		}
		return stream;
	}

	public Path getSource() {
		return filePath;
	}

	private boolean tryToCloseChannel() {
		synchronized (lock) {
			if (!expiring) {
				return false;
			}
			if (System.currentTimeMillis() < (cacheTime + lastUsed)) {
				return false;
			}
			try {
				stream.close();
			} catch (final RuntimeException e) {
				e.printStackTrace();
			} catch (final Exception e) {
				e.printStackTrace();
			}
			return true;
		}
	}

	private void checkInspector() {
		synchronized (lock) {
			if (taskShutdown) {
				final Runnable timer = () -> {
					while (true) {
						if (expiring) {
							final long time = System.currentTimeMillis();
							if (time > (lastUsed + cacheTime)) {
								synchronized (lock) {
									if (tryToCloseChannel()) {
										taskShutdown = true;
										break;
									}
								}
							}
						}

						try {
							if (executor.isShutdown()) {
								return;
							}
							Thread.sleep(1000);
						} catch (final InterruptedException e) {
							if (executor.isShutdown()) {
								return;
							}
						}
					}
				};

				taskShutdown = false;
				executor.execute(timer);
			}
		}
	}

	public void startExpiring() {
		if (this.expiring) {
			return;
		}

		this.lastUsed = System.currentTimeMillis();
		this.expiring = true;
		checkInspector();
	}

}