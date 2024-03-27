package com.zq.nio.zero_copy;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

/**
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
public class ZeroCopyClient {

	public static void main(String[] args) throws IOException {
		SocketChannel socketChannel = SocketChannel.open();
		socketChannel.connect(new InetSocketAddress("127.0.0.1", 7002));
		String fileName = "/Users/zhangquan/Downloads/尚硅谷Maven教程笔记.pdf";

		// 得到一个文件Channel
		FileChannel fileChannel = new FileInputStream(fileName).getChannel();

		System.out.println("开始发送...");
		long st = System.currentTimeMillis();
		//在 linux 下一个 transferTo 方法就可以完成传输
		//在 windows 下一次调用 transferTo 只能发送 8m,在win下就需要分段传输
		// transferTo 底层就使用了 zero copy
		long count = fileChannel.transferTo(0, fileChannel.size(), socketChannel);
		long et = System.currentTimeMillis();

		System.out.println("发送总字节数："+ count +"，耗时：" + (et - st));

		fileChannel.close();
	}
}
