package com.sang.rocketdog.bootstrap;

import java.security.cert.CertificateException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.SSLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.sang.rocketdog.container.Container;
import com.sang.rocketdog.container.SpringContainer;

public class Bootstrap {
	private static Logger logger = LoggerFactory.getLogger(Bootstrap.class);
	private static volatile boolean running = true;

	public static void main(String[] args) throws SSLException, CertificateException {
		try {
			final Container container = new SpringContainer();

			Runtime.getRuntime().addShutdownHook(new Thread() {
				public void run() {
					try {
						container.stop();
						logger.info("Dubbo "
								+ container.getClass().getSimpleName()
								+ " stopped!");
					} catch (Throwable t) {
						logger.error(t.getMessage(), t);
					}
					synchronized (Bootstrap.class) {
						running = false;
						Bootstrap.class.notify();
					}
				}
			});

			container.start();
			logger.info("RocketDog " + container.getClass().getSimpleName()
					+ " started!");
			System.out.println(new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]")
					.format(new Date()) + " RocketDog service server started!");
		} catch (RuntimeException e) {
			e.printStackTrace();
			logger.error(e.getMessage(), e);
			System.exit(1);
		}
		synchronized (Bootstrap.class) {
			while (running) {
				try {
					Bootstrap.class.wait();
				} catch (Throwable e) {
				}
			}
		}
	}
}
