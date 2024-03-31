package com.zq.netty.simple;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelPipeline;
import io.netty.util.CharsetUtil;

import java.util.concurrent.TimeUnit;

/**
 * 在整个事件循环中由  Pipeline 对我们的业务进行处理，如果在 Pipeline 的某个 Handler 里有一个非常耗时的操作，
 * 那么会导致 Pipeline 阻塞，那么我们就可以把耗时的操作提交到 NioEventLoop 的 taskQueue 里异步处理。
 *
 * taskQueue 三种典型的使用场景：
 * 第一种：用户程序自定义的普通任务
 * 第二种：用户自定义定时任务
 * 第三种：非当前 Reactor 线程调用 Channel 的各种方法 例如在推送系统的业务线程里面，
 * 根据用户的标识，找到对应的 Channel 引用，然后调用 Write 类方法向该用户推送消息，
 * 就会进入到这种场景。最终的 Write 会提交到任务队列中后被异步消费
 *
 * @author <a href="mailto:zhangquan@hupu.com">zhangquan</a>
 * @since  2024-03-29 11:19:30
 */
public class NettyTaskQueueServerHandler extends ChannelInboundHandlerAdapter {

	/**
	 * @param ctx ChannelHandlerContext ctx:上下文对象, 含有 管道pipeline , 通道channel, 地址
	 * @param msg 就是客户端发送的数据 默认Object
	 * @throws Exception
	 */
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		// 比如这里我们有一个非常耗时的业务，我们就可以提交给该 channel 对应的 NioEventLoop 的 taskQueue 中

		// taskQueue 第一种使用场景，把用户自定义的普通任务提交给 taskQueue
		ctx.channel().eventLoop().execute(new Runnable() {
			@Override
			public void run() {
				// 模拟耗时操作
				try {
					Thread.sleep(5 * 1000);
					ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client-1", CharsetUtil.UTF_8));
					System.out.println("线程 " + Thread.currentThread().getName() + " channel =" + ctx.channel());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		// 再把用户自定义的任务任务提交给 taskQueue
		ctx.channel().eventLoop().execute(new Runnable() {
			@Override
			public void run() {
				// 模拟耗时操作
				try {
					Thread.sleep(5 * 1000);
					ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client-2", CharsetUtil.UTF_8));
					System.out.println("线程 " + Thread.currentThread().getName() + " channel =" + ctx.channel());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

		// 第二种场景：把用户自定义的定时任务提交到 scheduleTaskQueue 中
		ctx.channel().eventLoop().schedule(new Runnable() {

			@Override
			public void run() {
				// 模拟耗时操作
				try {
					Thread.sleep(5 * 1000);
					ctx.writeAndFlush(Unpooled.copiedBuffer("hello, client-3", CharsetUtil.UTF_8));
					System.out.println("线程 " + Thread.currentThread().getName() + " channel =" + ctx.channel());
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}, 5, TimeUnit.SECONDS);

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
