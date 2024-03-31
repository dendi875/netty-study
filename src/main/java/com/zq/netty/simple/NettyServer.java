package com.zq.netty.simple;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * 1. Netty 服务器在 6668 端口监听，客户端能发送消息给服务器"hello,服务器~"
 * 2. 服务器可以回复消息给客户端"hello,客户端~"
 * 3. 目的：对 Netty 线程模型有一个初步认识，便于理解 Netty 模型理论
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-27 12:03:18
 */
public class NettyServer {


	public static void main(String[] args) throws InterruptedException {
		// 创建两个线程组 BossGroup 和 WorkerGroup， workerGroup 默认为 cpu核数*2
		NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
		NioEventLoopGroup workerGroup = new NioEventLoopGroup();

		try {
			// 创建服务器的启动对象，并配置参数
			ServerBootstrap bootstrap = new ServerBootstrap();
			bootstrap.group(bossGroup, workerGroup) // 设置两个线程组
					.channel(NioServerSocketChannel.class) // 使用 NioSocketChannel 作为服务器的通道实现
					.option(ChannelOption.SO_BACKLOG, 128) // 设置线程队列连接个数
					.childOption(ChannelOption.SO_KEEPALIVE, true) // 设置保持活动连接状态
					.childHandler(new ChannelInitializer<SocketChannel>() {
						//给pipeline 设置处理器
						@Override
						protected void initChannel(SocketChannel ch) throws Exception {
							// 可以使用一个集合管理 SocketChannel， 再推送消息时，
							// 可以将业务加入到各个channel 对应的 NIOEventLoop 的 taskQueue 或者 scheduleTaskQueue
							System.out.println("客户 socketChannel hashCode=" + ch.hashCode());
//							ch.pipeline().addLast(new NettyServerHandler());
							ch.pipeline().addLast(new NettyTaskQueueServerHandler());
						}
					}); // 给我们的workerGroup 的 EventLoop 对应的管道设置处理器

			System.out.println("服务器 is ready...");

			// 绑定一个端口并启动服务器，绑定端口是异步操作，当绑定操作处理完，将会调用相应的监听器处理逻辑
			ChannelFuture cf = bootstrap.bind(6668).sync();

			// 给 cf 注册监听器，监控我们关心的事件
			cf.addListener(new ChannelFutureListener() {
				@Override
				public void operationComplete(ChannelFuture future) throws Exception {
					if (cf.isSuccess()) {
						System.out.println("监听端口 6668 成功");
					} else {
						System.out.println("监听端口 6668 失败");
					}
				}
			});

			// 对关闭通道进行监听
			cf.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
		}

	}
}
