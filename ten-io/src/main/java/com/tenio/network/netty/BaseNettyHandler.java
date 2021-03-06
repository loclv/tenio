/*
The MIT License

Copyright (c) 2016-2020 kong <congcoi123@gmail.com>

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
*/
package com.tenio.network.netty;

import com.tenio.configuration.BaseConfiguration;
import com.tenio.configuration.constant.LogicEvent;
import com.tenio.event.EventManager;
import com.tenio.network.Connection;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * Use <a href="https://netty.io/">Netty</a> to handle message. Base on the
 * messages' content. You can handle your own logic here.
 * 
 * @author kong
 * 
 */
public abstract class BaseNettyHandler extends ChannelInboundHandlerAdapter {

	/**
	 * Retrieve a connection by its channel
	 * 
	 * @param channel, see {@link Channel}
	 * @return a connection
	 */
	protected Connection _getConnection(Channel channel) {
		return channel.attr(NettyConnection.KEY_THIS).get();
	}

	/**
	 * When a client is disconnected from your server for any reason, you can handle
	 * it in this event
	 * 
	 * @param ctx                    the channel, see {@link ChannelHandlerContext}
	 * @param keepPlayerOnDisconnect this value can be configured in your
	 *                               configurations, see {@link BaseConfiguration}.
	 *                               If the value is set to true, when the client is
	 *                               disconnected, its player can be held for an
	 *                               interval time (you can configure this interval
	 *                               time in your configurations)
	 */
	protected void _channelInactive(ChannelHandlerContext ctx, boolean keepPlayerOnDisconnect) {
		// get the connection first
		var connection = _getConnection(ctx.channel());
		EventManager.getLogic().emit(LogicEvent.CONNECTION_CLOSE, connection, keepPlayerOnDisconnect);
		connection = null;
	}

	/**
	 * Record the exceptions
	 * 
	 * @param ctx   the channel, see {@link ChannelHandlerContext}
	 * @param cause the exception will occur
	 */
	protected void _exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		// get the connection first
		var connection = _getConnection(ctx.channel());
		EventManager.getLogic().emit(LogicEvent.CONNECTION_EXCEPTION, ctx.channel().id().asLongText(), connection,
				cause);
	}

}
