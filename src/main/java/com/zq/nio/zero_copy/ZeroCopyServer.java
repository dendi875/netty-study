package com.zq.nio.zero_copy;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;

/**
 * 案例要求：
 *
 * 1. 使用传统的 IO 方法传递一个大文件
 * 2. 使用 NIO 零拷贝方式传递（transferTo）一个大文件
 * 3. 看看两种传递方式耗时时间分别是多少
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-26 16:59:03
 */
public class ZeroCopyServer {

	public static void main(String[] args) throws IOException {
		InetSocketAddress address = new InetSocketAddress("127.0.0.1", 7002);

		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		ServerSocket serverSocket = serverSocketChannel.socket();
		serverSocket.bind(address);

		ByteBuffer buffer = ByteBuffer.allocate(4096);
		while (true) {
			SocketChannel socketChannel = serverSocketChannel.accept();
			int readCount = 0;
			while (-1 != readCount) {
				try {
					readCount = socketChannel.read(buffer);
				} catch (IOException e) {
					break;
				}
			}

			// 重置 position
			buffer.rewind();
		}

	}
}
