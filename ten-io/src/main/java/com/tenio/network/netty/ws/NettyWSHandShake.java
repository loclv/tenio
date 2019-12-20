/*
The MIT License

Copyright (c) 2016-2019 kong <congcoi123@gmail.com>

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
package com.tenio.network.netty.ws;

import java.net.URISyntaxException;

import com.tenio.configuration.BaseConfiguration;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;

/**
 * <a href="https://en.wikipedia.org/wiki/WebSocket">WebSocket</a> is distinct
 * from HTTP. Both protocols are located at layer 7 in the OSI model and depend
 * on TCP at layer 4. Although they are different, RFC 6455 states that
 * WebSocket "is designed to work over HTTP ports 80 and 443 as well as to
 * support HTTP proxies and intermediaries," thus making it compatible with the
 * HTTP protocol. To achieve compatibility, the WebSocket handshake uses the
 * HTTP Upgrade header[1] to change from the HTTP protocol to the WebSocket
 * protocol.
 * 
 * @author kong
 * 
 */
public class NettyWSHandShake extends ChannelInboundHandlerAdapter {

	/**
	 * The handshake starts with an HTTP request/response, allowing servers to
	 * handle HTTP connections as well as WebSocket connections on the same port.
	 * Once the connection is established, communication switches to a bidirectional
	 * binary protocol which does not conform to the HTTP protocol.
	 */
	private WebSocketServerHandshaker __handshaker;
	/**
	 * @see {@link BaseConfiguration}
	 */
	private BaseConfiguration __configuration;

	public NettyWSHandShake(BaseConfiguration configuration) {
		__configuration = configuration;
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {

		// check the request for handshake
		if (msg instanceof HttpRequest) {

			HttpRequest httpRequest = (HttpRequest) msg;
			HttpHeaders headers = httpRequest.headers();

			if (headers.get("Connection").equalsIgnoreCase("Upgrade")
					|| headers.get("Upgrade").equalsIgnoreCase("WebSocket")) {

				// add new handler to the existing pipeline to handle HandShake-WebSocket
				// Messages
				ctx.pipeline().replace(this, "handler", new NettyWSHandler(__configuration));

				// do the Handshake to upgrade connection from HTTP to WebSocket protocol
				__handleHandshake(ctx, httpRequest);
			}
		} else {
			// do nothing or logging
		}
	}

	/**
	 * Do the handshaking for WebSocket request.
	 * 
	 * @param ctx the channel @see {@link ChannelHandlerContext}
	 * @param req the request @see {@link HttpRequest}
	 * @throws URISyntaxException
	 */
	private void __handleHandshake(ChannelHandlerContext ctx, HttpRequest req) throws URISyntaxException {
		WebSocketServerHandshakerFactory wsFactory = new WebSocketServerHandshakerFactory(__getWebSocketURL(req), null,
				true);
		__handshaker = wsFactory.newHandshaker(req);
		if (__handshaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			__handshaker.handshake(ctx.channel(), req);
		}
	}

	private String __getWebSocketURL(HttpRequest req) {
		String url = "ws://" + req.headers().get("Host") + req.uri();
		return url;
	}

}