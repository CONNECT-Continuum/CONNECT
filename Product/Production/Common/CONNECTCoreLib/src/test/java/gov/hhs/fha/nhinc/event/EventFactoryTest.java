/**
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
package gov.hhs.fha.nhinc.event;

import static org.junit.Assert.assertTrue;
import gov.hhs.fha.nhinc.event.initiator.BeginOutboundMessageEvent;
import gov.hhs.fha.nhinc.event.initiator.BeginOutboundProcessingEvent;
import gov.hhs.fha.nhinc.event.responder.BeginInboundMessageEvent;
import gov.hhs.fha.nhinc.event.responder.EndInboundMessageEvent;

import org.apache.commons.logging.Log;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author zmelnick
 * 
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "/EventFactoryConfig.xml" })
public class EventFactoryTest {

    protected Mockery context = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    final Log mockLog = context.mock(Log.class);

    @Autowired
    private EventFactory eventFactory;
    
    @Test
    public void createBeginInboundMessageEvent() {
        Event event = eventFactory.createBeginInboundMessage(); 
        assertTrue(event instanceof BeginInboundMessageEvent);
    }
    
    @Test
    public void createEndInboundMessageEvent() {
        Event event = eventFactory.createEndInboundMessage(); 
        assertTrue(event instanceof EndInboundMessageEvent);
    }
    
    @Test
    public void createBeginOutboundMessage() {
        Event event = eventFactory.createBeginOutboundMessage(); 
        assertTrue(event instanceof BeginOutboundMessageEvent);
    }

    @Test
    public void createBeginOutboundProcessing() {
        Event event = eventFactory.createBeginOutboundProcessing(); 
        assertTrue(event instanceof BeginOutboundProcessingEvent);
    }

//    public Event createBeginNwhinInvocation();
//
//    public Event createEndNwhinInvocation();
//
//    public Event createEndOutboundProcessing();
//
//    public Event createEndOutboundMessage();
//
//    public Event createBeginInboundProcessing();
//
//    public Event createBeginAdapterDelegation();
//
//    public Event createEndAdapterDelegation();
//
//    public Event createEndInboundProcessing();
//
//    public Event createMessageProcessingFailed();

  

    
}
