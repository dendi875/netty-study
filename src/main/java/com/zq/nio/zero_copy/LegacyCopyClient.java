package com.zq.nio.zero_copy;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 使用传统的 IO 方法传递一个大文件
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-26 17:08:41
 */
public class LegacyCopyClient {

	public static void main(String[] args) throws IOException {
		Socket socket = new Socket("127.0.0.1", 7001);

		String fileName = "/Users/zhangquan/Downloads/尚硅谷Maven教程笔记.pdf";
		FileInputStream inputStream = new FileInputStream(fileName);

		DataOutputStream dataOutputStream = new DataOutputStream(socket.getOutputStream());

		byte[] buffer = new byte[4096];
		long readCount;
		long total = 0;

		long start = System.currentTimeMillis();
		// 把文件转为输入流，然后把输入流的数据读取后存到 buffer中，然后再把 buffer 中的数据写到网络输出流中
		while ((readCount = inputStream.read(buffer)) >= 0) {
			total += readCount;
			dataOutputStream.write(buffer);
		}
		long end = System.currentTimeMillis();

		System.out.println("发送总字节数："+ total +"，耗时：" + (end - start));


		dataOutputStream.close();
		inputStream.close();
		socket.close();
	}
}
