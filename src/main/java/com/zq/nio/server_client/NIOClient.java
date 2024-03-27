package com.zq.nio.server_client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * 非阻塞IO，客户端示例
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-24 17:34:29
 */
public class NIOClient {

	public static void main(String[] args) throws IOException {
		// 创建一个SocketChannel
		SocketChannel socketChannel = SocketChannel.open();

		// 设置成非阻塞模式
		socketChannel.configureBlocking(false);

		// 绑定服务端的ip/port
		InetSocketAddress socketAddress = new InetSocketAddress("127.0.0.1", 6666);

		// 因为是非阻塞的，而且连接需要一段时间，所以要while来判断是否连接成功
		if (!socketChannel.connect(socketAddress)) {
			while (!socketChannel.finishConnect()) {
				System.out.println("正在连接到服务端...");
			}
		}

		// 到这里说明连接成功，发送一条消息给服务端
		String str = "hello,nio";
		// 把字符串转为字节数组，然后再包装成buffer
		ByteBuffer byteBuffer = ByteBuffer.wrap(str.getBytes());
		// 向buffer中写数据
		socketChannel.write(byteBuffer);

		// 模拟客户端阻塞在这里
		System.in.read();
	}
}
