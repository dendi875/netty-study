package com.zq.netty.codec;

import com.google.protobuf.InvalidProtocolBufferException;

public class ProtobufTest {

	public static void main(String[] args) throws InvalidProtocolBufferException {
		// 发送一个 Student 对象到服务器
		// 构建一个 Student 对象
		StudentPOJO.Student student = StudentPOJO.Student.newBuilder()
				.setId(5).setName("张三")
				.build();

		// serialization: student to bytes
		byte[] bytes = student.toByteArray();
		// deserialization: bytes to student

		StudentPOJO.Student studentFromBytes = StudentPOJO.Student.parseFrom(bytes);
        System.out.println(student.equals(studentFromBytes));
	}
}
