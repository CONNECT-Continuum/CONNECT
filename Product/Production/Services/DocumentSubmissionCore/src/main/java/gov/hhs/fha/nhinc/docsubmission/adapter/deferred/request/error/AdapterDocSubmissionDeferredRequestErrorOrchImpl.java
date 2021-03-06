/*
 * Copyright (c) 2009-2019, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docsubmission.adapter.deferred.request.error;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.largefile.LargeFileUtils;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.healthit.nhin.XDRAcknowledgementType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType;
import ihe.iti.xds_b._2007.ProvideAndRegisterDocumentSetRequestType.Document;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Neil Webb
 */
public class AdapterDocSubmissionDeferredRequestErrorOrchImpl {
    private static final Logger LOG = LoggerFactory.getLogger(AdapterDocSubmissionDeferredRequestErrorOrchImpl.class);

    public XDRAcknowledgementType provideAndRegisterDocumentSetBRequestError(
        ProvideAndRegisterDocumentSetRequestType request, String errorMessage, AssertionType assertion) {
        LOG.trace("Begin AdapterDocSubmissionDeferredRequestErrorOrchImpl.provideAndRegisterDocumentSetBRequestError");

        processRequest(request);

        // Stub until adapter component is available
        XDRAcknowledgementType ack = new XDRAcknowledgementType();
        RegistryResponseType regResp = new RegistryResponseType();
        regResp.setStatus(NhincConstants.XDR_ACK_STATUS_MSG);
        ack.setMessage(regResp);

        LOG.trace("End AdapterDocSubmissionDeferredRequestErrorOrchImpl.provideAndRegisterDocumentSetBRequestError");
        return ack;
    }

    private static void processRequest(ProvideAndRegisterDocumentSetRequestType request) {
        LargeFileUtils fileUtils = LargeFileUtils.getInstance();
        List<Document> docList = request.getDocument();
        for (Document doc : docList) {
            try {
                LOG.debug("Closing request input streams.");
                fileUtils.closeStreamWithoutException(doc.getValue().getDataSource().getInputStream());
            } catch (Exception ioe) {
                LOG.error("Failed to close input stream", ioe);
            }
        }
    }
}
