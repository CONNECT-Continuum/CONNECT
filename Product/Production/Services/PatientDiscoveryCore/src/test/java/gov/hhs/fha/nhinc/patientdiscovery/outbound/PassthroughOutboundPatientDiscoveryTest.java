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
package gov.hhs.fha.nhinc.patientdiscovery.outbound;

import gov.hhs.fha.nhinc.audit.ejb.AuditEJBLogger;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.patientdiscovery.audit.PatientDiscoveryAuditLogger;
import gov.hhs.fha.nhinc.patientdiscovery.audit.transform.PatientDiscoveryAuditTransforms;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryDelegate;
import gov.hhs.fha.nhinc.patientdiscovery.entity.OutboundPatientDiscoveryOrchestratable;
import java.util.Properties;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;
import org.hl7.v3.RespondingGatewayPRPAIN201306UV02ResponseType;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import org.junit.Test;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author akong
 *
 */
public class PassthroughOutboundPatientDiscoveryTest {

    private final AuditEJBLogger mockEJBLogger = mock(AuditEJBLogger.class);
    private final OutboundPatientDiscoveryDelegate delegate = mock(OutboundPatientDiscoveryDelegate.class);
    private final String TARGET_HCID = "1.1";

    @Test
    public void invoke() {
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        request.setPRPAIN201305UV02(new PRPAIN201305UV02());
        request.setNhinTargetCommunities(createNhinTargetCommunitiesType(TARGET_HCID));
        AssertionType assertion = new AssertionType();
        OutboundPatientDiscoveryOrchestratable outOrchestratable = new OutboundPatientDiscoveryOrchestratable();
        outOrchestratable.setResponse(new PRPAIN201306UV02());
        PatientDiscoveryAuditLogger auditLogger = getAuditLogger(true);

        when(delegate.process(any(OutboundPatientDiscoveryOrchestratable.class))).thenReturn(outOrchestratable);
        PassthroughOutboundPatientDiscovery passthroughPatientDiscovery = new PassthroughOutboundPatientDiscovery(
            delegate, auditLogger);
        RespondingGatewayPRPAIN201306UV02ResponseType actualMessage = passthroughPatientDiscovery
            .respondingGatewayPRPAIN201305UV02(request, assertion);

        assertSame(outOrchestratable.getResponse(), actualMessage.getCommunityResponse().get(0).getPRPAIN201306UV02());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger).auditRequestMessage(eq(request.getPRPAIN201305UV02()), any(AssertionType.class), any(
            NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.TRUE), isNull(Properties.class),
            eq(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME), any(PatientDiscoveryAuditTransforms.class));
    }

    @Test
    public void auditLoggingTurnedOffForPD() {
        RespondingGatewayPRPAIN201305UV02RequestType request = new RespondingGatewayPRPAIN201305UV02RequestType();
        request.setPRPAIN201305UV02(new PRPAIN201305UV02());
        request.setNhinTargetCommunities(createNhinTargetCommunitiesType(TARGET_HCID));
        AssertionType assertion = new AssertionType();

        OutboundPatientDiscoveryOrchestratable outOrchestratable = new OutboundPatientDiscoveryOrchestratable();
        outOrchestratable.setResponse(new PRPAIN201306UV02());
        PatientDiscoveryAuditLogger auditLogger = getAuditLogger(false);

        when(delegate.process(any(OutboundPatientDiscoveryOrchestratable.class))).thenReturn(outOrchestratable);
        PassthroughOutboundPatientDiscovery passthroughPatientDiscovery = new PassthroughOutboundPatientDiscovery(
            delegate, auditLogger);
        RespondingGatewayPRPAIN201306UV02ResponseType actualMessage = passthroughPatientDiscovery
            .respondingGatewayPRPAIN201305UV02(request, assertion);

        assertSame(outOrchestratable.getResponse(), actualMessage.getCommunityResponse().get(0).getPRPAIN201306UV02());
        assertNotNull("Assertion MessageId is null", assertion.getMessageId());
        verify(mockEJBLogger, never()).auditRequestMessage(eq(request.getPRPAIN201305UV02()), any(AssertionType.class),
            any(NhinTargetSystemType.class), eq(NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION),
            eq(NhincConstants.AUDIT_LOG_NHIN_INTERFACE), eq(Boolean.TRUE), isNull(Properties.class),
            eq(NhincConstants.PATIENT_DISCOVERY_SERVICE_NAME), any(PatientDiscoveryAuditTransforms.class));
    }

    private NhinTargetCommunitiesType createNhinTargetCommunitiesType(String hcid) {
        NhinTargetCommunitiesType targetCommunities = new NhinTargetCommunitiesType();
        NhinTargetCommunityType targetCommunity = new NhinTargetCommunityType();
        targetCommunity.setHomeCommunity(createHomeCommunity(hcid));
        targetCommunities.getNhinTargetCommunity().add(targetCommunity);
        return targetCommunities;
    }

    private HomeCommunityType createHomeCommunity(String hcid) {
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(hcid);
        return homeCommunity;
    }

    private PatientDiscoveryAuditLogger getAuditLogger(final boolean isLoggingOn) {
        return new PatientDiscoveryAuditLogger() {
            @Override
            protected AuditEJBLogger getAuditLogger() {
                return mockEJBLogger;
            }

            @Override
            protected boolean isAuditLoggingOn(String serviceName) {
                return isLoggingOn;
            }
        };
    }
}
