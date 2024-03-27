package com.zq.nio.server_client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channel;
import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 *
 * 网络编程应用实例 - 群聊系统
 * 实例要求：
 *
 * 编写一个 NIO 群聊系统，实现服务器端和客户端之间的数据简单通讯（非阻塞）
 * 实现多人群聊
 * 服务器端：可以监测用户上线，离线，并实现消息转发功能
 * 客户端：通过 Channel 可以无阻塞发送消息给其它所有用户，同时可以接受其它用户发送的消息（有服务器转发得到）
 * 目的：进一步理解 NIO 非阻塞网络编程机制
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-25 20:05:29
 */
public class GroupChatServer {

	private Selector selector;

	private ServerSocketChannel listenChannel;

	private static final int PORT = 6667;

	public GroupChatServer() {
		try {
			// 获取到选择器
			selector = Selector.open();
			listenChannel = ServerSocketChannel.open();
			// 绑定端口
			listenChannel.bind(new InetSocketAddress("127.0.0.1", PORT));
			// 设置成非阻塞模式
			listenChannel.configureBlocking(false);
			// 将该 listenChannel 注册到 selector
			listenChannel.register(selector, SelectionKey.OP_ACCEPT);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 循环监听事件
	 */
	private void listen() {
		while (true) {
			// 阻塞看有没有准备就绪的事件
			try {
				int count = selector.select();
				if (count > 0) {
					// 获取到就绪事件对应的SelectionKey
					Set<SelectionKey> selectionKeys = selector.selectedKeys();
					Iterator<SelectionKey> iterator = selectionKeys.iterator();
					// 遍历 selectionKeys 的集合
					while (iterator.hasNext()) {
						SelectionKey key = iterator.next();
						if (key.isAcceptable()) {
							// 接受连接请求
							SocketChannel sc = listenChannel.accept();
							sc.configureBlocking(false);
							// 注册到 selector 上
							sc.register(selector, SelectionKey.OP_READ);
							// 提示
							System.out.println(sc.getRemoteAddress() + " 上线");
						}

						if (key.isReadable()) {
							readData(key);
						}

						// 从集合中删除当前 key，防止多线程下重复处理
						iterator.remove();
					}
				} else {
					System.out.println("等待事件就绪...");
				}
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
		}
	}

	/**
	 * 读取客户端数据，并转给其它客户端
	 *
	 * 通过 SelectionKey 得到 Chanel，然后再把 Channel 的数据读到 buffer 中
	 *
	 * @param key SelectionKey
	 */
	private void readData(SelectionKey key) {
		SocketChannel channel = null;

		ByteBuffer buffer = ByteBuffer.allocate(1024);
		try {
			channel = (SocketChannel) key.channel();
			int len = channel.read(buffer);
			if (len > 0) {
				// 把缓冲区转为字节再转为字符串
				String msg = new String(buffer.array());
				System.out.println("客户端发送的信息为：" + msg);
				// 向其它的客户端转发该条消息
				sendMsgToOtherClients(msg, channel);
			}
		} catch (IOException e) {
			try {
				System.out.println(channel.getRemoteAddress() + " 离线了");
				// 从注册上摘下来
				key.cancel();
				// 关闭通道资源
				channel.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * 转发消息给其它客户端
	 *
	 * @param msg String
	 * @param channel SocketChannel
	 */
	private void sendMsgToOtherClients(String msg, SocketChannel channel) throws IOException {
		System.out.println("服务器转发消息中....");
		// 遍历注册到该 selector 中的所有 SocketChannel，排除自己
		for (SelectionKey key : selector.keys()) {
			// 取出该 SelectionKey 对应的通道
			Channel targetChannel = key.channel();
			// 排除自己
			if (targetChannel instanceof SocketChannel && targetChannel != channel) {
				SocketChannel destChannel = (SocketChannel) targetChannel;
				// 把消息写到channel的buffer中
				destChannel.write(ByteBuffer.wrap(msg.getBytes()));
			}
		}
	}

	public static void main(String[] args) {
		GroupChatServer groupChatServer = new GroupChatServer();
		groupChatServer.listen();
	}
}
