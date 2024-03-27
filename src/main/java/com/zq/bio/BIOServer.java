package com.zq.bio;

import java.io.IOException;
import java.io.InputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Java BIO 应用实例
 *
 * 实例说明：
 * 1. 使用 BIO 模型编写一个服务器端，监听 6666 端口，当有客户端连接时，就启动一个线程与之通讯。
 * 2. 要求使用线程池机制改善，可以连接多个客户端。
 * 3. 服务器端可以接收客户端发送的数据（telnet 方式即可）。
 *
 * 客户端： control + ]
 * nc 127.0.0.1 6666
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-21 22:34:39
 */
public class BIOServer {

	public static void main(String[] args) throws IOException {
		// 创建一个线程池
		ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
		// 创建ServerSocket
		ServerSocket serverSocket = new ServerSocket(6666);
		System.out.println("服务器启动了");
		while (true) {
			System.out.println("线程信息id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName());
			// 监听，等待客户端连接
			System.out.println("等待连接...");
			final Socket socket = serverSocket.accept();
			System.out.println("连接到了一个客户端");
			newCachedThreadPool.execute(new Runnable() {
				@Override
				public void run() {
					handler(socket);
				}
			});
		}
	}

	public static void handler(Socket socket) {
		System.out.println("线程信息id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName());
		try {
			// 接收客户端发送的数据
			byte[] bytes = new byte[1024];
			InputStream inputStream = socket.getInputStream();
			// 循环读取客户端
			while (true) {
				System.out.println("线程信息id = " + Thread.currentThread().getId() + "名字 = " + Thread.currentThread().getName());
				System.out.println("read...");
				// 从通道中读取数据，把数据读到 bytes 数组中
				int len = inputStream.read(bytes);
				if (len != -1) {
					System.out.println("客户端发送的数据为：" + new String(bytes, 0, len));
				} else {
					break;
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			System.out.println("关闭和client的连接");
			try {
				socket.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
