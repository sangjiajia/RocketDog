package com.sang.rocketdog.transport;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;

public interface InternalCodec {
    void encode(Channel channel, ChannelBuffer buffer, Object message) throws Exception;

    Object decode(Channel channel, ChannelBuffer buffer) throws Exception;


    enum DecodeResult {
        NEED_MORE_INPUT, SKIP_SOME_INPUT
    }
}
