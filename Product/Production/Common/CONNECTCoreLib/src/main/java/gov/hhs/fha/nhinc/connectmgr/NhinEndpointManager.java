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
package gov.hhs.fha.nhinc.connectmgr;

import gov.hhs.fha.nhinc.exchangemgr.ExchangeManager;
import gov.hhs.fha.nhinc.exchangemgr.ExchangeManagerHelper;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.UDDI_SPEC_VERSION;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NhinEndpointManager {

    private static final Logger LOG = LoggerFactory.getLogger(NhinEndpointManager.class);

    protected ExchangeManager getExchangeManager() {
        return ExchangeManager.getInstance();
    }

    protected UddiSpecVersionRegistry getUddiSpecVersionRegistry() {
        return UddiSpecVersionRegistry.getInstance();
    }

    public GATEWAY_API_LEVEL getApiVersion(String homeCommunityId, NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        GATEWAY_API_LEVEL result = null;
        try {
            List<UDDI_SPEC_VERSION> specVersions = getExchangeManager().getSpecVersions(homeCommunityId,
                serviceName);
            UDDI_SPEC_VERSION specVersion = ExchangeManagerHelper.getHighestUDDISpecVersion(specVersions);
            result = getHighestGatewayApiLevelSupportedBySpec(specVersion, serviceName);
        } catch (Exception ex) {
            LOG.error("Error getting API version: ", ex);
        }

        return result == null ? GATEWAY_API_LEVEL.LEVEL_g1 : result;
    }

    private GATEWAY_API_LEVEL getHighestGatewayApiLevelSupportedBySpec(UDDI_SPEC_VERSION specVersion,
        NhincConstants.NHIN_SERVICE_NAMES serviceName) {
        GATEWAY_API_LEVEL highestApiLevel = null;

        try {
            UddiSpecVersionRegistry specRegistry = getUddiSpecVersionRegistry();
            highestApiLevel = specRegistry.getSupportedGatewayAPI(specVersion, serviceName);
        } catch (Exception ex) {
            LOG.error("Error in getting highest gateway API level supported by specification: ", ex);
        }

        return highestApiLevel;
    }
}
