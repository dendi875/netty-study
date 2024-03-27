package com.zq.nio.zero_copy;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *  使用传统的 IO 方法传递一个大文件
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-26 17:08:02
 */
public class LegacyCopyServer {

	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(7001);

		while (true) {
			Socket socket = serverSocket.accept();
			DataInputStream dataInputStream = new DataInputStream(socket.getInputStream());
			byte[] bytes = new byte[4096];

			while (true) {
				try {
					int len = dataInputStream.read(bytes, 0, bytes.length);
					if (len == -1) {
						break;
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
