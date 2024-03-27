package com.zq.nio;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 本地文件读数据
 * 使用 ByteBuffer（缓冲）和 FileChannel（通道），将 file01.txt 中的数据读入到程序，并显示在控制台屏幕
 * 假定文件已经存在
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-23 14:22:45
 */
public class NIOFileChannel02 {

	public static void main(String[] args) throws IOException {
		// 创建文件输入流
		File file = new File("/tmp/file01.txt");
		FileInputStream fileInputStream = new FileInputStream(file);

		// 获取对应的 Channel
		FileChannel fileChannel = fileInputStream.getChannel();

		// 创建缓冲区
		ByteBuffer byteBuffer = ByteBuffer.allocate((int) file.length());

		// 将通道的数据读入到缓冲区
		fileChannel.read(byteBuffer);

		System.out.println(new String(byteBuffer.array()));

		fileInputStream.close();
	}
}
