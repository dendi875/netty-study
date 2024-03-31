package com.zq.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

/**
 * 我们自定义一个Handler 需要继续 netty 规定好的某个HandlerAdapter(规范)
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-27 19:21:45
 */
public class NettyServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * @param ctx ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
	 * @param msg 就是客户端发送的数据 默认Object
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("服务器读取线程 " + Thread.currentThread().getName() + " channel =" + ctx.channel());
		System.out.println("server ctx: " + ctx);
		System.out.println("看看channel 和 pipeline的关系");
		Channel channel = ctx.channel();
		ChannelPipeline pipeline = ctx.pipeline(); // 本质是一个双向链接, 出站入站

		//将 msg 转成一个 ByteBuf
		//ByteBuf 是 Netty 提供的，不是 NIO 的 ByteBuffer.
		ByteBuf buf = (ByteBuf) msg;
		System.out.println("客户端发送消息是:" + buf.toString(CharsetUtil.UTF_8));
		System.out.println("客户端地址:" + channel.remoteAddress());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		//writeAndFlush 是 write + flush，将数据写入到缓存并刷新
		//一般，我们对这个发送的数据进行编码
		ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client", CharsetUtil.UTF_8));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
}
