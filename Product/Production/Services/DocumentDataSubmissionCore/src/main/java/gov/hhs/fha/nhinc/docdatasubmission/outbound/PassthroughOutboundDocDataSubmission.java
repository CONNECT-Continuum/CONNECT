/*
 * Copyright (c) 2009-2018, United States Government, as represented by the Secretary of Health and Human Services.
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
package gov.hhs.fha.nhinc.docdatasubmission.outbound;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.common.nhinccommon.UrlInfoType;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayRegisterDocumentSetSecuredRequestType;
import gov.hhs.fha.nhinc.docdatasubmission.MessageGeneratorUtilsDocData;
import gov.hhs.fha.nhinc.docdatasubmission.audit.DocDataSubmissionAuditLogger;
import gov.hhs.fha.nhinc.docdatasubmission.entity.OutboundDocDataSubmissionDelegate;
import gov.hhs.fha.nhinc.docdatasubmission.entity.OutboundDocDataSubmissionOrchestratable;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.RegisterDocumentSetRequestType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;

public class PassthroughOutboundDocDataSubmission implements OutboundDocDataSubmission {

    private DocDataSubmissionAuditLogger auditLogger = new DocDataSubmissionAuditLogger();
    private OutboundDocDataSubmissionDelegate dsDelegate = new OutboundDocDataSubmissionDelegate();

    public PassthroughOutboundDocDataSubmission() {
        super();
    }

    public PassthroughOutboundDocDataSubmission(DocDataSubmissionAuditLogger auditLogger,
        OutboundDocDataSubmissionDelegate dsDelegate) {
        this.auditLogger = auditLogger;
        this.dsDelegate = dsDelegate;
    }

    @Override
    public RegistryResponseType registerDocumentSetB(RegisterDocumentSetRequestType body, AssertionType assertion,
        NhinTargetCommunitiesType targets, UrlInfoType urlInfo) {

        assertion = MessageGeneratorUtilsDocData.getInstance().generateMessageId(assertion);
        NhinTargetSystemType target = MessageGeneratorUtilsDocData.getInstance()
            .convertFirstToNhinTargetSystemType(targets);
        RespondingGatewayRegisterDocumentSetSecuredRequestType request = createAuditRequest(body, assertion, targets);

        auditRequest(request.getRegisterDocumentSetRequest(), assertion, target);

        OutboundDocDataSubmissionOrchestratable dsOrchestratable = createOrchestratable(dsDelegate, body, target,
            assertion);
        RegistryResponseType response = ((OutboundDocDataSubmissionOrchestratable) dsDelegate.process(dsOrchestratable))
            .getResponse();

        return response;
    }

    private void auditRequest(RegisterDocumentSetRequestType request, AssertionType assertion,
        NhinTargetSystemType target) {
        auditLogger.auditRequestMessage(request, assertion, target, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION,
            NhincConstants.AUDIT_LOG_NHIN_INTERFACE, Boolean.TRUE, null, NhincConstants.NHINC_XDS_SERVICE_NAME);
    }

    private OutboundDocDataSubmissionOrchestratable createOrchestratable(OutboundDocDataSubmissionDelegate delegate,
        RegisterDocumentSetRequestType request, NhinTargetSystemType targetSystem, AssertionType assertion) {

        OutboundDocDataSubmissionOrchestratable dsOrchestratable = new OutboundDocDataSubmissionOrchestratable(
            delegate);
        dsOrchestratable.setAssertion(assertion);
        dsOrchestratable.setRequest(request);

        return dsOrchestratable;
    }

    private RespondingGatewayRegisterDocumentSetSecuredRequestType createAuditRequest(
        RegisterDocumentSetRequestType msg, AssertionType assertion, NhinTargetCommunitiesType targets) {

        RespondingGatewayRegisterDocumentSetSecuredRequestType request = new RespondingGatewayRegisterDocumentSetSecuredRequestType();
        request.setNhinTargetCommunities(targets);
        request.setRegisterDocumentSetRequest(msg);

        return request;
    }
}
