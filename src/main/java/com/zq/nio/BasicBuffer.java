package com.zq.nio;

import java.nio.IntBuffer;

/**
 * Java 中 nio Buffer 的使用示例
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-22 22:27:52
 */
public class BasicBuffer {

	public static void main(String[] args) {
		// 创建一个 Buffer，大小为 5，表示可以存放 5 个init
		IntBuffer intBuffer = IntBuffer.allocate(5);

		// 向 Buffer 中存放数据
		for (int i = 0; i < intBuffer.capacity(); i++) {
			intBuffer.put(i);
		}

		// 从 Buffer 中读取数据，需要将 buffer 转换，这个很关键
		intBuffer.flip();
		while (intBuffer.hasRemaining()) {
			System.out.println(intBuffer.get());
		}
	}
}
