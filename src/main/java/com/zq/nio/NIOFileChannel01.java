package com.zq.nio;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 *
 * 实例要求：本地文件写数据
 *
 * 使用 ByteBuffer（缓冲）和 FileChannel（通道），将 "hello,张三" 写入到 file01.txt 中
 * 文件不存在就创建
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-23 12:08:08
 */
public class NIOFileChannel01 {

	public static void main(String[] args) throws IOException {
		String str = "hello,张三";

		// 创建文件输出流
		FileOutputStream fileOutputStream = new FileOutputStream("/tmp/file01.txt");
		FileChannel fileChannel = fileOutputStream.getChannel();

		// 创建一个缓冲区，分配容量大小
		ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
		// 将字符串转为字节，然后写入到 buffer 中，这步是对 Buffer 进行「写操作」
		byteBuffer.put(str.getBytes());

		// 因为下一步，要站在Channel的角度来操作，对缓冲区进行反转
		byteBuffer.flip();

		// 这里站在 Channel 的角度来看待，需要将 Buffer 的数据「写」到 Channel 中
		fileChannel.write(byteBuffer);

		// 闭关流
		fileOutputStream.close();
	}
}
