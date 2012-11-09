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
package gov.hhs.fha.nhinc.docsubmission.aspect;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.event.AssertionEventDescriptionBuilder;
import gov.hhs.fha.nhinc.event.builder.AssertionDescriptionExtractor;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

/**
 * @author akong
 * 
 */
public class InboundProcessingEventDescriptionBuilder extends AssertionEventDescriptionBuilder {

    private final ProvideAndRegisterDocumentSetDescriptionExtractor REQUEST_EXTRACTOR;
    private final RegistryResponseDescriptionExtractor RESPONSE_EXTRACTOR;

    private ProvideAndRegisterDocumentSetRequestType request;
    private RegistryResponseType response;

    public InboundProcessingEventDescriptionBuilder() {
        REQUEST_EXTRACTOR = new ProvideAndRegisterDocumentSetDescriptionExtractor();
        RESPONSE_EXTRACTOR = new RegistryResponseDescriptionExtractor();
    }

    public InboundProcessingEventDescriptionBuilder(
            final ProvideAndRegisterDocumentSetDescriptionExtractor requestExtractor,
            final AssertionDescriptionExtractor assertionExtractor,
            final RegistryResponseDescriptionExtractor responseExtractor) {
        REQUEST_EXTRACTOR = requestExtractor;
        RESPONSE_EXTRACTOR = responseExtractor;
        super.setAssertionExtractor(assertionExtractor);
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildTimeStamp()
     */
    @Override
    public void buildTimeStamp() {
        // TODO Auto-generated method stub
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildStatuses()
     */
    @Override
    public void buildStatuses() {
        setStatuses(RESPONSE_EXTRACTOR.getStatuses(response));
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildRespondingHCIDs()
     */
    @Override
    public void buildRespondingHCIDs() {
        // Leave blank since we are the responding hcid.
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildPayloadTypes()
     */
    @Override
    public void buildPayloadTypes() {
        setPayLoadTypes(REQUEST_EXTRACTOR.getPayloadTypes(request));
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildPayloadSize()
     */
    @Override
    public void buildPayloadSizes() {
        setPayloadSizes(REQUEST_EXTRACTOR.getPayloadSize(request));
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.EventDescriptionBuilder#buildErrorCodes()
     */
    @Override
    public void buildErrorCodes() {
        setErrorCodes(RESPONSE_EXTRACTOR.getErrorCodes(response));
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder#setArguments(java.lang.Object[])
     */
    @Override
    public void setArguments(Object... arguments) {
        if (arguments != null && arguments.length == 2 && areArgumentTypesExpected(arguments)) {
            this.request = (ProvideAndRegisterDocumentSetRequestType) arguments[0];
            extractAssertion(arguments);
        }
    }

    private boolean areArgumentTypesExpected(Object... arguments) {
        return arguments[0] instanceof ProvideAndRegisterDocumentSetRequestType
                && arguments[1] instanceof AssertionType;
    }

    /*
     * (non-Javadoc)
     * 
     * @see gov.hhs.fha.nhinc.event.BaseEventDescriptionBuilder#setReturnValue(java.lang.Object)
     */
    @Override
    public void setReturnValue(Object returnValue) {
        if (returnValue != null && returnValue instanceof RegistryResponseType) {
            this.response = (RegistryResponseType) returnValue;
        }
    }
}
