package com.zq.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * MappedByteBuffer 可让文件直接在内存（堆外内存）修改,操作系统不需要拷贝一次
 * ，而如何同步到文件由 NIO 来完成。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-23 15:20:15
 */
public class MappedByteBufferTest {

	public static void main(String[] args) throws IOException {
		RandomAccessFile file = new RandomAccessFile("1.txt", "rw");

		// 获取文件对应通道
		FileChannel fileChannel = file.getChannel();

		// 把文件映射到内存中
		/**
		 * 参数 1:FileChannel.MapMode.READ_WRITE 使用的读写模式
		 * 参数 2：0：可以直接修改的起始位置
		 * 参数 3:5: 是映射到内存的大小（不是索引位置），即将 1.txt 的多少个字节映射到内存
		 * 可以直接修改的范围就是索引0-4
		 * 实际类型 DirectByteBuffer
		 */
		MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, 5);
		// 进行修改
		mappedByteBuffer.put(0, (byte) 'H');
		mappedByteBuffer.put(4, (byte) 'O');

//		mappedByteBuffer.put(5, (byte) 'E'); //IndexOutOfBoundsException

		// 关闭打开的文件资源
		file.close();
	}
}
