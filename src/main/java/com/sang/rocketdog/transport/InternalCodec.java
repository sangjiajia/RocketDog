package com.sang.rocketdog.transport;

import java.io.IOException;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public interface InternalCodec {
    void encode(Channel channel, ChannelBuffer buffer, Object message) throws IOException;

    Object decode(Channel channel, ChannelBuffer buffer) throws IOException;


    enum DecodeResult {
        NEED_MORE_INPUT, SKIP_SOME_INPUT
    }
}
