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
package gov.hhs.fha.nhinc.docquery.adapter.deferred.request.proxy;

import gov.hhs.fha.nhinc.adapterdocquerydeferredrequest.AdapterDocQueryDeferredRequestPortType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayQueryRequestType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.ADAPTER_API_LEVEL;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import gov.hhs.healthit.nhin.DocQueryAcknowledgementType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author JHOPPESC
 */
public class AdapterDocQueryDeferredRequestProxyWebServiceUnsecuredImpl implements AdapterDocQueryDeferredRequestProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredrequest";
    private static final String SERVICE_LOCAL_PART = "AdapterDocQueryDeferredRequest";
    private static final String PORT_LOCAL_PART = "AdapterDocQueryDeferredRequestPortSoap";
    private static final String WSDL_FILE = "AdapterDocQueryDeferredRequest.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:adapterdocquerydeferredrequest:RespondingGateway_CrossGatewayQueryRequestMessage";
    private WebServiceProxyHelper oProxyHelper = null;

    public AdapterDocQueryDeferredRequestProxyWebServiceUnsecuredImpl() {
        log = createLogger();
        oProxyHelper = createWebServiceProxyHelper();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    protected WebServiceProxyHelper createWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    /**
     * This method retrieves and initializes the port.
     * 
     * @param url The URL for the web service.
     * @return The port object for the web service.
     */
    protected AdapterDocQueryDeferredRequestPortType getPort(String url, String wsAddressingAction,
            AssertionType assertion) {
        AdapterDocQueryDeferredRequestPortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART),
                    AdapterDocQueryDeferredRequestPortType.class);
            oProxyHelper
                    .initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
        } else {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }

    /**
     * Retrieve the service class for this web service.
     * 
     * @return The service class for this web service.
     */
    protected Service getService() {
        if (cachedService == null) {
            try {
                cachedService = oProxyHelper.createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            } catch (Throwable t) {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }

    public DocQueryAcknowledgementType respondingGatewayCrossGatewayQuery(AdhocQueryRequest msg, AssertionType assertion) {
        log.debug("Begin respondingGatewayCrossGatewayQuery");
        DocQueryAcknowledgementType response = null;

        try {
            String url = oProxyHelper
                    .getAdapterEndPointFromConnectionManager(NhincConstants.ADAPTER_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME);
            if (NullChecker.isNotNullish(url)) {
                AdapterDocQueryDeferredRequestPortType port = getPort(url, WS_ADDRESSING_ACTION, assertion);

                if (msg == null) {
                    log.error("Message was null");
                } else if (assertion == null) {
                    log.error("AssertionType was null");
                } else if (port == null) {
                    log.error("port was null");
                } else {
                    RespondingGatewayCrossGatewayQueryRequestType request = new RespondingGatewayCrossGatewayQueryRequestType();
                    request.setAdhocQueryRequest(msg);
                    request.setAssertion(assertion);

                    response = (DocQueryAcknowledgementType) oProxyHelper
                            .invokePort(port, AdapterDocQueryDeferredRequestPortType.class,
                                    "respondingGatewayCrossGatewayQuery", request);
                }
            } else {
                log.error("Failed to call the web service ("
                        + NhincConstants.ADAPTER_DOCUMENT_QUERY_DEFERRED_REQ_SERVICE_NAME + ").  The URL is null.");
            }
        } catch (Exception ex) {
            log.error("Error calling respondingGatewayCrossGatewayQuery: " + ex.getMessage(), ex);
            response = new DocQueryAcknowledgementType();
            RegistryResponseType regResp = new RegistryResponseType();
            regResp.setStatus(NhincConstants.DOC_QUERY_DEFERRED_REQ_ACK_STATUS_MSG);
            response.setMessage(regResp);
        }

        log.debug("End respondingGatewayCrossGatewayQuery");
        return response;
    }

}
