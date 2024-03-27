package com.zq.nio.server_client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 *  网络编程应用实例 - 群聊系统-客户端
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-26 15:24:49
 */
public class GroupChatClient {

	private static final String HOST = "127.0.0.1";

	private static final int PORT = 6667;

	private Selector selector;

	private SocketChannel socketChannel;

	private String localAddress;

	public GroupChatClient() throws IOException {
		selector = Selector.open();
		socketChannel = SocketChannel.open(new InetSocketAddress(HOST, PORT));
		// 设置非阻塞
		socketChannel.configureBlocking(false);
		// 将 channel 注册到 selector 上
		socketChannel.register(selector, SelectionKey.OP_READ);
		// 获取 localAddress
		localAddress = socketChannel.getLocalAddress().toString().substring(1);

		System.out.println(localAddress + " is ok....");
	}

	/**
	 * 向服务器发送消息
	 *
	 * @param msg String
	 */
	private void sendMsg(String msg) {
		msg = localAddress + " 说: " + msg;
		try {
			socketChannel.write(ByteBuffer.wrap(msg.getBytes()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从服务器读取消息
	 */
	public void readMsg() {
		try {
			int count = selector.select();
			if (count > 0) {
				Set<SelectionKey> selectionKeys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = selectionKeys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					// 获取通道
					if (key.isReadable()) {
						SocketChannel channel = (SocketChannel) key.channel();
						// 从通道中获取数据并存到buffer中
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						channel.read(buffer);
						// 把buffer中的字节数据转为字符串，并输出
						System.out.println(new String(buffer.array()).trim());
					}

					iterator.remove();
				}
			} else {
				System.out.println("没有可以用的通道...");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		GroupChatClient client = new GroupChatClient();

		// 启动一个线程，每3s从服务器读取数据
		new Thread(() -> {
			while (true) {
				client.readMsg();
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, "read-msg-from-server").start();

		// 发送数据给服务端
		Scanner scanner = new Scanner(System.in);
		while (scanner.hasNextLine()) {
			String s = scanner.nextLine();
			client.sendMsg(s);
		}
	}
}
