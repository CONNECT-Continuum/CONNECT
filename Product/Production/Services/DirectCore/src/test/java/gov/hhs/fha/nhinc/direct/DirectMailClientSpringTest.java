/*
 * Copyright (c) 2012, United States Government, as represented by the Secretary of Health and Human Services.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above
 *       copyright notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 *       notice, this list of conditions and the following disclaimer in the documentation
 *       and/or other materials provided with the distribution.
 *     * Neither the name of the United States Government nor the
 *       names of its contributors may be used to endorse or promote products
 *       derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE UNITED STATES GOVERNMENT BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package gov.hhs.fha.nhinc.direct;

import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.removeSmtpAgentConfig;
import static gov.hhs.fha.nhinc.direct.DirectUnitTestUtil.writeSmtpAgentConfig;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Test {@link DirectMailClient} with Spring Framework Configuration.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/test.direct.appcontext.xml")
public class DirectMailClientSpringTest {

    @Autowired
    private DirectMailClient intDirectMailClient;

    @Autowired
    private DirectMailClient extDirectMailClient;
    
    @Autowired
    private MessageHandler outboundMessageHandler;

    @Autowired
    private MessageHandler inboundMessageHandlerSmtp;

    @Autowired
    private MessageHandler inboundMessageHandlerSoap;

    /**
     * Set up keystore for test.
     */
    @BeforeClass
    public static void setUpClass() {
        writeSmtpAgentConfig();
    }

    /**
     * Test that we can get an external mail client with spring.
     */
    @Test
    public void canGetExternalMailClient() {
        assertNotNull(extDirectMailClient);
    }
    
    /**
     * Test that we can get an internal mail client with spring.
     */
    @Test
    public void canGetInternalMailClient() {
        assertNotNull(intDirectMailClient);
    }
    
    /**
     * Test that the two mail clients are distinct instances.
     */
    @Test
    public void canDistinguishInternalExternal() {
        assertNotSame(intDirectMailClient, extDirectMailClient);        
    }
    
    @Test
    public void canGetOutboundMessageHandler() {
        assertNotNull(outboundMessageHandler);
    }
    
    @Test
    public void canGetInboundMessageHandlerForSmtp() {
        assertNotNull(inboundMessageHandlerSmtp);
}

    @Test
    public void canGetInboundMessageHandlerForSoap() {
        assertNotNull(inboundMessageHandlerSoap);
    }
    
    @Test
    public void canDistinguishMessageHandlers() {
        assertNotSame(outboundMessageHandler, inboundMessageHandlerSmtp);
        assertNotSame(outboundMessageHandler, inboundMessageHandlerSoap);
        assertNotSame(inboundMessageHandlerSoap, inboundMessageHandlerSmtp);
    }    

    /**
     * Tear down keystore created in setup.
     */
    @AfterClass
    public static void tearDownClass() {
        removeSmtpAgentConfig();
    }    
}
