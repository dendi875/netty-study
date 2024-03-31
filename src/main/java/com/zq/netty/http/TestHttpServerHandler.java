package com.zq.netty.http;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.HttpObject;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.CharsetUtil;

import java.net.URI;

/**
 * HttpObject：客户端和服务器端相互通讯的数据被封装成 HttpObject
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-03-29 18:43:51
 */
public class TestHttpServerHandler extends SimpleChannelInboundHandler<HttpObject> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, HttpObject msg) throws Exception {

		System.out.println("channel: " + ctx.channel() + ", handler: " + ctx.handler());

		if (msg instanceof HttpRequest) {

			System.out.println("msg 类型: " + msg.getClass() + ", 客户端地址: " + ctx.channel().remoteAddress());

			// 过滤掉 http://localhost:36668/favicon.ico 这次请求
			HttpRequest httpRequest = (HttpRequest) msg;
			URI uri = new URI(httpRequest.uri());
			if ("/favicon.ico".equals(uri.getPath())) {
				System.out.println("过滤掉 favicon.ico 这次请求");
				return;
			}


			ByteBuf byteBuf = Unpooled.copiedBuffer("I am a server ", CharsetUtil.UTF_8);

			DefaultFullHttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK, byteBuf);
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain");
			response.headers().set(HttpHeaderNames.CONTENT_LENGTH, byteBuf.readableBytes());

			ctx.writeAndFlush(response);
		}
	}
}
