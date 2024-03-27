package com.zq.nio;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Arrays;

/**
 * NIO 支持通过多个 Buffer（即 Buffer数组）完成读写操作，即 Scattering 和 Gathering
 *
 * 1. Scattering：将数据写入到 buffer 时，可以采用 buffer 数组，依次写入 [分散]
 * 2. Gathering：从 buffer 读取数据时，可以采用 buffer 数组，依次读「聚合」
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-23 15:38:36
 */
public class ScatteringAndGatheringTest {

	public static void main(String[] args) throws IOException {
		// 我们使用网络的形式来举例，所以我们使用 ServerSocketChannel 和 SocketChannel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
		SocketAddress socketAddress = new InetSocketAddress(7000);
		// 绑定端口并启动TCP服务器
		serverSocketChannel.socket().bind(socketAddress);

		// 创建 Buffer 数组
		ByteBuffer[] byteBuffers = new ByteBuffer[2];
		byteBuffers[0] = ByteBuffer.allocate(3);
		byteBuffers[1] = ByteBuffer.allocate(5);

		// 接受客户端的连接请求
		SocketChannel socketChannel = serverSocketChannel.accept();

		// 假如每次接受从客户端发送过来的 8 个字节
		int messageLength = 8;

		// 循环读取客户端发送过来的数据
		while (true) {

			int byteRead = 0;
			while (byteRead < messageLength) {
				// 把客户端发送过来的数据读到buffer数组中
				long rl = socketChannel.read(byteBuffers);
				byteRead += rl;
				System.out.println("byteRead = " + byteRead);
				// 查看当前 buffer 的 position 和 limit
				Arrays.stream(byteBuffers)
						.map((buffer) -> "position: " + buffer.position() + " limit: " + buffer.limit())
						.forEach(System.out::println);
			}

			// 反转 buffer，然后要将 buffer 的数据回显给客户端
			Arrays.stream(byteBuffers).forEach(Buffer::flip);

			int byteWrite = 0;
			while (byteWrite < messageLength) {
				long wl = socketChannel.write(byteBuffers);
				byteWrite += wl;
			}

			// 清空buffer，为了是下一次循环时能正常读取
			Arrays.stream(byteBuffers).forEach(Buffer::clear);

			System.out.println("byteRead: " + byteRead + " byteWrite: " + byteWrite);
		}
	}
}
