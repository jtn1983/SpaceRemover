package Server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class Main {
    public static void main(String[] args) throws IOException {
        final ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 25555));

        try (SocketChannel socketChannel = serverSocketChannel.accept()) {
            final ByteBuffer inputBuffer = ByteBuffer.allocate(2 << 10);

            while (socketChannel.isConnected()) {
                int bytesCount = socketChannel.read(inputBuffer);
                if (bytesCount == -1) break;
                final String msg = new String(inputBuffer.array(), 0, bytesCount, StandardCharsets.UTF_8);
                inputBuffer.clear();
                socketChannel.write(ByteBuffer.wrap((msg.replaceAll("\\s+", "")).getBytes(StandardCharsets.UTF_8)));
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

}
