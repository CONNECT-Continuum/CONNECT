/*
 * Copyright (c) 2009-2015, United States Government, as represented by the Secretary of Health and Human Services.
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

import gov.hhs.fha.nhinc.mail.MessageHandler;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Handles inbound messages from an external mail client. Inbound messages are
 * un-directified and either - sent to a recipient on an internal mail client -
 * SMTP+Mime - SMTP+XDM - process XDM as XDR - SOAP+XDR
 */
public class DirectInboundMsgHandler implements MessageHandler {

    private static final Logger LOG = LoggerFactory.getLogger(DirectInboundMsgHandler.class);
    /**
     * Property for the external direct client used to send the outbound
     * message.
     */
    private final DirectReceiver directReceiver;

    /**
     * Constructor.
     *
     * @param directReceiver direct receiver used to process messages.
     */
    public DirectInboundMsgHandler(DirectReceiver directReceiver) {
        this.directReceiver = directReceiver;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean handleMessage(MimeMessage message) {
        boolean handled = false;
        try {
            directReceiver.receiveInbound(message);
            handled = true;
        } catch (Exception e) {
            LOG.error("Exception while processing and sending outbound direct message", e);
        }
        return handled;
    }
}
