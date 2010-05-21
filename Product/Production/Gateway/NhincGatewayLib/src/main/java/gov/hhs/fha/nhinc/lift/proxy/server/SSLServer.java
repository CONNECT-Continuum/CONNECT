//
// Non-Export Controlled Information
//
//####################################################################
//## The MIT License
//## 
//## Copyright (c) 2010 Harris Corporation
//## 
//## Permission is hereby granted, free of charge, to any person
//## obtaining a copy of this software and associated documentation
//## files (the "Software"), to deal in the Software without
//## restriction, including without limitation the rights to use,
//## copy, modify, merge, publish, distribute, sublicense, and/or sell
//## copies of the Software, and to permit persons to whom the
//## Software is furnished to do so, subject to the following conditions:
//## 
//## The above copyright notice and this permission notice shall be
//## included in all copies or substantial portions of the Software.
//## 
//## THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
//## EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
//## OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
//## NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
//## HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
//## WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
//## FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
//## OTHER DEALINGS IN THE SOFTWARE.
//## 
//###################################################################
//********************************************************************
// FILE: SSLServer.java
//
// Copyright (C) 2010 Harris Corporation. All rights reserved.
//
// CLASSIFICATION: Unclassified
//
// DESCRIPTION: SSLServer.java 
//              
//
// LIMITATIONS: None
//
// SOFTWARE HISTORY:
//
//> Feb 24, 2010 PTR#  - R. Robinson
// Initial Coding.
//<
//********************************************************************
package gov.hhs.fha.nhinc.lift.proxy.server;

import java.io.IOException;
import java.net.SocketAddress;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;

import gov.hhs.fha.nhinc.lift.proxy.util.DemoProtocol;
import java.net.ServerSocket;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SSLServer extends Server {

    private Log log = null;
    private final SSLServerSocket server;

    /**
     * Set up a SSL server socket on the provided address.  Makes and runs
     * clones of the provided HandlerPrototype to handle client connections
     * to the Server.
     * @param address
     * @param prototype
     * @throws IOException
     */
    public SSLServer(SocketAddress address, HandlerPrototype prototype) throws IOException {
        super(prototype);

        log = createLogger();
        SSLServerSocketFactory factory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        ServerSocket createdSocket = factory.createServerSocket();
        server = (SSLServerSocket) createdSocket;

        // Doing this causes problems, I don't understand this much but I think it's necessary.
//		server.setNeedClientAuth(true);

        server.bind(address);
        log.info("SERVER: Bound to address: " + server.getInetAddress() + ":" + server.getLocalPort());
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    public SSLServerSocket getServer() {
        return server;
    }

    @Override
    protected void acceptConnection() throws IOException {
        log.debug("SERVER: Waiting for incomming connection.");

        SSLSocket socket = (SSLSocket) server.accept();

        log.debug("SERVER: Accepted a new connection.");

        log.debug("SERVER: Handshaking with connection.");

        /*
         * Spawn off and start a new Handler thread to process
         * the new connection.
         */
        HandlerPrototype hand = this.getPrototype().clone(new DemoProtocol(socket));

        if (hand != null) {
            log.debug("SERVER: Passing connection to new HANDLER.");
            (new Thread(hand)).start();
        } else {
            log.debug("SERVER: Connection failed to handshake properly.");

            socket.close();
        }
    }
}
