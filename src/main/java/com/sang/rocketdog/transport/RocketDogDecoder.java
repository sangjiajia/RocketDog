package com.sang.rocketdog.transport;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class RocketDogDecoder extends SimpleChannelUpstreamHandler {


	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent event)
			throws Exception {
		Object m = event.getMessage();
		if(!(m instanceof ChannelBuffer)){
			ctx.sendUpstream(event);
			return;
		}
		ChannelBuffer input = (ChannelBuffer)m;
		if(!input.readable()){
			return;
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		ctx.sendUpstream(e);
	}
}
