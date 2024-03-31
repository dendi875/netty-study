package com.zq.netty.simple;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-27 20:08:54
 */
public class NettyClient {

	public static void main(String[] args) throws InterruptedException {
		// 客户端需要一个事件循环组
		NioEventLoopGroup group = new NioEventLoopGroup();

		try {
			// 创建客户端启动对象，客户端使用的是 Bootstrap
			Bootstrap bootstrap = new Bootstrap();
			// 设置相关参数
			bootstrap.group(group)
					.channel(NioSocketChannel.class) // 设置客户端通道
					.handler(new ChannelInitializer<SocketChannel>() {
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast(new NettyClientHandler()); // 加入自定义处理器
						}
					});

			System.out.println("客户端 ok...");

			// 启动客户端并连接到服务器
			ChannelFuture cf = bootstrap.connect("127.0.0.1", 6668).sync();

			// 给关闭通道进行监听
			cf.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully();
		}
	}
}
