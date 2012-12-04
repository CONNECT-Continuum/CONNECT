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
package gov.hhs.fha.nhinc.patientdiscovery.outbound;

import gov.hhs.fha.nhinc.aspect.OutboundProcessingEvent;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.gateway.executorservice.ExecutorServiceHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.MessageGeneratorUtils;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.PRPAIN201305UV02ArgTransformer;
import gov.hhs.fha.nhinc.patientdiscovery.aspect.RespondingGatewayPRPAIN201306UV02Builder;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryOrchestratable;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201306Transforms;

import java.util.concurrent.ExecutorService;

import org.hl7.v3.CommunityPRPAIN201306UV02ResponseType;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;

public class PassthroughOutboundPatientDiscovery implements OutboundPatientDiscovery {

    private final MessageGeneratorUtils msgUtils = MessageGeneratorUtils.getInstance();
    private OutboundPatientDiscoveryDelegate delegate;
    private PatientDiscoveryAuditLogger auditLogger;

    /**
     * Constructor.
     */
    public PassthroughOutboundPatientDiscovery() {
        this.delegate = new OutboundPatientDiscoveryDelegate();
        this.auditLogger = new PatientDiscoveryAuditLogger();
    }

    /**
     * Constructor.
     * 
     * @param delegate
     * @param auditLogger
     */
    public PassthroughOutboundPatientDiscovery(OutboundPatientDiscoveryDelegate delegate,
            PatientDiscoveryAuditLogger auditLogger) {
        this.delegate = delegate;
        this.auditLogger = auditLogger;
    }

    @Override
    @OutboundProcessingEvent(beforeBuilder = PRPAIN201305UV02ArgTransformer.class,
            afterReturningBuilder = RespondingGatewayPRPAIN201306UV02Builder.class, serviceType = "Patient Discovery",
            version = "1.0")
    public RespondingGatewayPRPAIN201306UV02ResponseType respondingGatewayPRPAIN201305UV02(
            RespondingGatewayPRPAIN201305UV02RequestType request, AssertionType assertion) {

        auditRequestFromAdapter(request.getPRPAIN201305UV02(), assertion);

        RespondingGatewayPRPAIN201306UV02ResponseType response = sendToNhin(request.getPRPAIN201305UV02(), assertion,
                msgUtils.convertFirstToNhinTargetSystemType(request.getNhinTargetCommunities()));

        auditResponseToAdapter(response, assertion);

        return response;
    }

    @Override
    public void setExecutorService(ExecutorService regularExecutor, ExecutorService largeJobExecutor) {
        // Do nothing. Passthrough does not do fan out.
    }

    private void auditRequestFromAdapter(PRPAIN201305UV02 request, AssertionType assertion) {
        auditLogger.auditNhin201305(request, assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION);
    }

    private void auditResponseToAdapter(RespondingGatewayPRPAIN201306UV02ResponseType response,
            AssertionType assertion) {
        auditLogger.auditEntity201306(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION);
    }

    private RespondingGatewayPRPAIN201306UV02ResponseType sendToNhin(PRPAIN201305UV02 request,
            AssertionType assertion, NhinTargetSystemType target) {
        PRPAIN201306UV02 response;

        try {
            OutboundPatientDiscoveryOrchestratable inMessage = new OutboundPatientDiscoveryOrchestratable(delegate,
                    null, null, null, assertion, NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME, target, request);
            OutboundPatientDiscoveryOrchestratable outMessage = delegate.process(inMessage);
            response = outMessage.getResponse();
        } catch (Exception ex) {
            String err = ExecutorServiceHelper.getFormattedExceptionInfo(ex, target,
                    NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME);
            response = generateErrorResponse(target, request, err);
        }

        return convert(response, target);
    }

    private RespondingGatewayPRPAIN201306UV02ResponseType convert(PRPAIN201306UV02 response, NhinTargetSystemType target) {
        String hcid = getHCID(target);
        CommunityPRPAIN201306UV02ResponseType communityResponse = msgUtils
                .createCommunityPRPAIN201306UV02ResponseType(hcid);
        communityResponse.setPRPAIN201306UV02(response);

        RespondingGatewayPRPAIN201306UV02ResponseType gatewayResponse = new RespondingGatewayPRPAIN201306UV02ResponseType();
        gatewayResponse.getCommunityResponse().add(communityResponse);

        return gatewayResponse;
    }
    
    private String getHCID(NhinTargetSystemType target) {
        String hcid = null;
        if (target != null && target.getHomeCommunity() != null) {
            hcid = target.getHomeCommunity().getHomeCommunityId(); 
        }
        
        return hcid;
        
    }

    private PRPAIN201306UV02 generateErrorResponse(NhinTargetSystemType target, PRPAIN201305UV02 request, String error) {
        String errStr = "Error from target homeId=" + target.getHomeCommunity().getHomeCommunityId();
        errStr += "  The error received was " + error;
        return (new HL7PRPA201306Transforms()).createPRPA201306ForErrors(request, errStr);
    }

}
