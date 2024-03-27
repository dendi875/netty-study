package com.zq.nio;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * 应用实例4 - 拷贝文件 transferFrom 方法
 * 实例要求：
 * 1. 使用 FileChannel（通道）和方法 transferFrom，完成文件的拷贝
 * 2. 拷贝一张图片
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-23 14:52:42
 */
public class NIOFileChannel04 {
	public static void main(String[] args) throws IOException {
		// 创建输入和输出流
		FileInputStream inputStream = new FileInputStream("/Users/zhangquan/Pictures/nio-1.jpg");
		FileOutputStream outputStream = new FileOutputStream("/Users/zhangquan/Pictures/nio-2.jpg");

		// 获取输入和输出流对应的 Channel
		FileChannel inputStreamChannel = inputStream.getChannel();
		FileChannel outputStreamChannel = outputStream.getChannel();

		// 进行拷贝
		outputStreamChannel.transferFrom(inputStreamChannel, 0, inputStreamChannel.size());

		// 关闭流和通道
		inputStreamChannel.close();
		outputStreamChannel.close();

		inputStream.close();
		outputStream.close();
	}
}
