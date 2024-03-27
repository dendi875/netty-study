package com.zq.nio.server_client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 案例要求：
 *
 * 编写一个 NIO 入门案例，实现服务器端和客户端之间的数据简单通讯（非阻塞）
 * 目的：理解 NIO 非阻塞网络编程机制
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-24 16:34:12
 */
public class NIOServer {

	public static void main(String[] args) throws IOException {
		// 创建 ServerSocketChannel
		ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();

		// 得到一个 Selector 对象
		Selector selector = Selector.open();

		// 启动一个tcp服务器，并绑定端口监听连接
		serverSocketChannel.socket().bind(new InetSocketAddress(6666));

		// 设置为非阻塞
		serverSocketChannel.configureBlocking(false);

		// 把 serverSocketChannel 注册到 selector 上，
		// 并且对于服务端来说，它只关心 ACCEPT 事件
		serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);

		// 循环等待客户端的请求
		while (true) {
			// 调用 select 看有没有监听的事件发生
			if (selector.select(1000) == 0) {
				System.out.println("等待了1秒，没有连接事件发生...");
				continue;
			}

			// 获取到事件集合，遍历事件，并对不同的事件做相应的处理
			Set<SelectionKey> selectionKeys = selector.selectedKeys();
			Iterator<SelectionKey> keyIterator = selectionKeys.iterator();
			while (keyIterator.hasNext()) {
				SelectionKey key = keyIterator.next();
				// 根据不同的事件类型做不同的处理
				if (key.isAcceptable()) {
					// 连接请求事件，需要来接收客户端的请求
					SocketChannel socketChannel = serverSocketChannel.accept();
					// 设置为非阻塞的，不然会报IllegalBlockingModeException
					socketChannel.configureBlocking(false);
					System.out.println("有客户端连接过来了 socketChannel.hashCode():" + socketChannel.hashCode());
					// 注册读事件，同时把缓冲区和Channel关联，方便下次读事件发生时获取数据
					socketChannel.register(selector,
							SelectionKey.OP_READ, ByteBuffer.allocate(1024));
				}

				if (key.isReadable()) {
					// 通过 SelectionKey 获取到 SocketChannel
					SocketChannel channel = (SocketChannel) key.channel();
					// 获取到通道关联的buffer
					ByteBuffer buffer = (ByteBuffer) key.attachment();
					// 把channel中的数据读到buffer中
					channel.read(buffer);
					// 把 buffer 中的字节数转为字符串，并回显给客户端
					System.out.println("客户端发送的消息:" + new String(buffer.array()));
				}

				// 手动从集合中移除selectionKey，防止多线程下的重复操作
				keyIterator.remove();
			}
		}
	}
}
