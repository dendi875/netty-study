package com.zq.nio;

import java.nio.ByteBuffer;

/**
 * 1. 可以将一个普通 Buffer 转成只读 Buffer
 * 2. 不能向只读的 Buffer 中写数据了
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-23 15:10:55
 */
public class ReadOnlyBuffer {

	private final static int CAPACITY = 64;

	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(CAPACITY);

		for (int i = 0; i < CAPACITY; i++) {
			byteBuffer.put((byte) i);
		}

		// 进行反转后读取
		byteBuffer.flip();

		ByteBuffer readOnlyBuffer = byteBuffer.asReadOnlyBuffer();
		while (readOnlyBuffer.hasRemaining()) {
			System.out.println(readOnlyBuffer.get());
		}

//		readOnlyBuffer.put((byte) 64);
	}
}
