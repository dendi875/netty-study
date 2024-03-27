package com.zq.nio;

import java.nio.ByteBuffer;

/**
 *
 * 关于 Buffer 和 Channel 的注意事项和细节
 * 1. ByteBuffer 支持类型化的 put 和 get，put 放入的是什么数据类型，
 * get 就应该使用相应的数据类型来取出，否则可能有 BufferUnderflowException 异常。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-23 15:01:06
 */
public class NIOByteBufferPutGet {

	public static void main(String[] args) {
		ByteBuffer byteBuffer = ByteBuffer.allocate(128);

		// 写入
		byteBuffer.putInt(1);
		byteBuffer.putLong(1000000L);
		byteBuffer.putChar('日');
		byteBuffer.putShort((short) 3);

		// 在读取之前要反转
		byteBuffer.flip();

		System.out.println(byteBuffer.getInt());
		System.out.println(byteBuffer.getLong());
		System.out.println(byteBuffer.getChar());
		System.out.println(byteBuffer.getShort());
	}
}

