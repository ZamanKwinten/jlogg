package jlogg.os.windows;

import java.io.IOException;
import java.net.ConnectException;
import java.net.StandardProtocolFamily;
import java.net.UnixDomainSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import jlogg.ConstantMgr;

public class WindowsDomainSocketServer {
	private static final Path SOCKET_PATH = ConstantMgr.JLoggConfigDir.toPath().resolve("jlogg.socket");
	private static final UnixDomainSocketAddress ADDRESS = UnixDomainSocketAddress.of(SOCKET_PATH);

	static WindowsFileOpenHandler openHandler;

	static void startup() {
		try {
			clearSocketFile();
			var server = ServerSocketChannel.open(StandardProtocolFamily.UNIX);
			server.bind(ADDRESS);
			server.configureBlocking(true);

			new ChannelReaderThread(server).start();

		} catch (IOException e) {
			throw new RuntimeException(e);
		}

	}

	static void clearSocketFile() {
		try {
			Files.deleteIfExists(SOCKET_PATH);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	static void writeMessage(String message) throws ConnectException {
		try {
			SocketChannel channel = SocketChannel.open(StandardProtocolFamily.UNIX);
			channel.connect(ADDRESS);

			var bytes = message.getBytes();
			var buffer = ByteBuffer.allocate(bytes.length);
			buffer.clear();
			buffer.put(bytes);
			buffer.flip();
			while (buffer.hasRemaining()) {
				channel.write(buffer);
			}

		} catch (ConnectException e) {
			throw e;
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static class ChannelReaderThread extends Thread {

		private final ServerSocketChannel server;

		public ChannelReaderThread(ServerSocketChannel server) {
			this.server = server;

			setDaemon(true);
			setName("WindowsDomainSocketServer");
		}

		@Override
		public void run() {

			while (true) {
				try {
					read().ifPresent(msg -> {
						if (openHandler != null) {
							openHandler.open(msg);
						}
					});
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		private Optional<String> read() throws IOException {
			var channel = server.accept();
			StringBuilder sb = new StringBuilder();

			var buffer = ByteBuffer.allocate(1024);

			for (int read = channel.read(buffer); read > 0; read = channel.read(buffer)) {
				buffer.flip();
				byte[] bytes = new byte[read];
				buffer.get(bytes);
				sb.append(new String(bytes));
			}

			return sb.isEmpty() ? Optional.empty() : Optional.of(sb.toString());
		}
	}

	static class ExistsException extends Exception {
		private static final long serialVersionUID = 1L;
	}
}
