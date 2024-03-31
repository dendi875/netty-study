package com.zq.netty.http;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 *
 * Netty 实现Http服务器
 *
 * 1. Netty 服务器在 36668 端口监听，浏览器发出请求 http://localhost:6668/
 * 2. 服务器可以回复消息给客户端"Hello!我是服务器5",并对特定请求资源进行过滤。
 * 3. 目的：Netty 可以做 Http 服务开发，并且理解 Handler 实例和客户端及其请求的关系。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-29 17:45:09
 */
public class TestServer {

	public static void main(String[] args) throws InterruptedException {

		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup, workerGroup)
					.channel(NioServerSocketChannel.class)
					.childHandler(new TestServerInitializer());

			ChannelFuture channelFuture = serverBootstrap.bind(36668).sync();

			channelFuture.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}
}
