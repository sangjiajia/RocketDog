package com.sang.rocketdog.transport;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public class DefaultInternalCodec implements InternalCodec{

	private DefaultInternalCodec(){
		
	}
	 static final  DefaultInternalCodec instance = new DefaultInternalCodec();
	
	public static DefaultInternalCodec getInstance(){
		return instance;
	}
	public void encode(Channel channel, ChannelBuffer buffer, Object message)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	public Object decode(Channel channel, ChannelBuffer buffer)
			throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
