package com.sang.rocketdog.transport;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelUpstreamHandler;

public class RocketDogDecoder extends SimpleChannelUpstreamHandler {

	private ChannelBuffer cumulation=ChannelBuffers.EMPTY_BUFFER;;

	private InternalCodec codec= DefaultInternalCodec.getInstance();
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
		
		ChannelBuffer message= null;
		
		if(cumulation.readable()){
			cumulation.writeBytes(input);
		}else{
			message = ChannelBuffers.wrappedBuffer(input);
		}
		
        Object msg;
        int saveReaderIndex;
        try {
            // decode object.
            do {
                saveReaderIndex = message.readerIndex();
                try {
                    msg = codec.decode(ctx.getChannel(), message);
                } catch (IOException e) {
                	cumulation = ChannelBuffers.EMPTY_BUFFER;
                    throw e;
                }
                if (msg == InternalCodec.DecodeResult.NEED_MORE_INPUT) {
                    message.readerIndex(saveReaderIndex);
                    break;
                } else {
                    if (saveReaderIndex == message.readerIndex()) {
                    	cumulation = ChannelBuffers.EMPTY_BUFFER;
                        throw new IOException("Decode without read data.");
                    }
                    if (msg != null) {
                        Channels.fireMessageReceived(ctx, msg, event.getRemoteAddress());
                    }
                }
            } while (message.readable());
        } finally {
            if (message.readable()) {
                message.discardReadBytes();
                cumulation = message;
            } else {
            	cumulation = ChannelBuffers.EMPTY_BUFFER;
            }
        }
        
        
	}



	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e)
			throws Exception {
		ctx.sendUpstream(e);
	}
}
