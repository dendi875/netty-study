package com.zq.netty.http;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-29 18:18:47
 */
public class TestServerInitializer extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		// 得到管道
		ChannelPipeline pipeline = ch.pipeline();

		// 向管道中添加处理器
		// 增加一个自定义的 handler
		pipeline.addLast("MyHttpServerCodec", new HttpServerCodec());
		// Netty 提供的处理 http 请求的编解码器
		pipeline.addLast("MyTestHttpServerHandler", new TestHttpServerHandler());
	}
}
