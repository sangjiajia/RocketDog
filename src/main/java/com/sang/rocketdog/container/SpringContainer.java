package com.sang.rocketdog.container;

import java.net.InetSocketAddress;
import java.security.cert.CertificateException;
import java.util.concurrent.Executors;

import javax.net.ssl.SSLException;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;
import org.jboss.netty.example.http.helloworld.HttpHelloWorldServerPipelineFactory;
import org.jboss.netty.handler.ssl.SslContext;
import org.jboss.netty.handler.ssl.util.SelfSignedCertificate;

import com.sang.rocketdog.transport.RocketDogDecoder;
import com.sang.rocketdog.transport.RocketDogEncoder;
import com.sang.rocketdog.transport.RocketDogNettyHandler;

public class SpringContainer implements Container {

	static final boolean SSL = System.getProperty("ssl") != null;
	static final int PORT = Integer.parseInt(System.getProperty("port",
			SSL ? "8443" : "8080"));

	public void start() throws SSLException, CertificateException {
		// Configure SSL.
		final SslContext sslCtx;
		if (SSL) {
			SelfSignedCertificate ssc = new SelfSignedCertificate();
			sslCtx = SslContext.newServerContext(ssc.certificate(),
					ssc.privateKey());
		} else {
			sslCtx = null;
		}

		// Configure the server.
		ServerBootstrap bootstrap = new ServerBootstrap(
				new NioServerSocketChannelFactory(
						Executors.newCachedThreadPool(),
						Executors.newCachedThreadPool()));

		// Enable TCP_NODELAY to handle pipelined requests without latency.
		bootstrap.setOption("child.tcpNoDelay", true);

		// Set up the event pipeline factory.
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            public ChannelPipeline getPipeline() {
                ChannelPipeline pipeline = Channels.pipeline();
                if(sslCtx!=null){
                	pipeline.addLast("ssl", sslCtx.newHandler());
                }
                pipeline.addLast("decoder", new RocketDogDecoder());
                pipeline.addLast("encoder", new RocketDogEncoder());
                pipeline.addLast("handler", new RocketDogNettyHandler());
                return pipeline;
            }
        });
		// Bind and start to accept incoming connections.
		bootstrap.bind(new InetSocketAddress(PORT));
	}

	public void stop() {
		// TODO Auto-generated method stub

	}

}
