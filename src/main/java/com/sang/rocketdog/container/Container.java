package com.sang.rocketdog.container;

import java.security.cert.CertificateException;

import javax.net.ssl.SSLException;

public interface Container {
    /**
     * start.
     * @throws SSLException 
     * @throws CertificateException 
     */
    void start() throws SSLException, CertificateException;
    
    /**
     * stop.
     */
    void stop();
}
