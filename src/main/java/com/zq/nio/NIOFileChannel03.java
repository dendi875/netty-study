package com.zq.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 使用一个 Buffer 完成文件读取、写入
 * 1. 使用 FileChannel（通道）和方法 read、write，完成文件的拷贝
 * 2. 拷贝一个文本文件 1.txt，放在项目下即可
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-23 14:41:01
 */
public class NIOFileChannel03 {

	public static void main(String[] args) throws IOException {
		File inputFile = new File("1.txt");
		FileInputStream inputStream = new FileInputStream(inputFile);
		FileChannel inputFileChannel = inputStream.getChannel();

		File outFile = new File("2.txt");
		FileOutputStream outputStream = new FileOutputStream(outFile);
		FileChannel outFileChannel = outputStream.getChannel();

		ByteBuffer byteBuffer = ByteBuffer.allocate(2);

		while (true) {

			// 要进行 buffer 的清空，为了多次循环读取
			byteBuffer.clear();

			// 将数据读到buffer
			int len = inputFileChannel.read(byteBuffer);
			if (len == -1) {
				break;
			}

			// 要进行 buffer 的反转
			byteBuffer.flip();

			// 将buffer的数据写到通道
			outFileChannel.write(byteBuffer);
		}

		inputStream.close();
		outputStream.close();
	}
}
