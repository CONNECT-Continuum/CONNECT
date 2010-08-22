/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.queue.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.entitypatientdiscoveryasyncreqqueue.EntityPatientDiscoveryAsyncReqQueuePortType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.transform.subdisc.HL7AckTransforms;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02RequestType;

/**
 *
 * @author JHOPPESC
 */
public class EntityPatientDiscoveryDeferredReqQueueProxyWebServiceUnsecuredImpl implements EntityPatientDiscoveryDeferredReqQueueProxy {
    private Log log = null;
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncreqqueue";
    private static final String SERVICE_LOCAL_PART = "EntityPatientDiscoveryAsyncReqQueue";
    private static final String PORT_LOCAL_PART = "EntityPatientDiscoveryAsyncReqQueuePortSoap";
    private static final String WSDL_FILE = "EntityPatientDiscoveryAsyncReqQueue.wsdl";
    private static final String WS_ADDRESSING_ACTION = "urn:gov:hhs:fha:nhinc:entitypatientdiscoveryasyncreqqueue:AddPatientDiscoveryAsyncReqAsyncRequest";
    private WebServiceProxyHelper oProxyHelper = null;

    public EntityPatientDiscoveryDeferredReqQueueProxyWebServiceUnsecuredImpl() {
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
    protected EntityPatientDiscoveryAsyncReqQueuePortType getPort(String url, String serviceAction, String wsAddressingAction, AssertionType assertion) {
        EntityPatientDiscoveryAsyncReqQueuePortType port = null;
        Service service = getService();
        if (service != null) {
            log.debug("Obtained service - creating port.");

            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), EntityPatientDiscoveryAsyncReqQueuePortType.class);
            oProxyHelper.initializeUnsecurePort((javax.xml.ws.BindingProvider) port, url, wsAddressingAction, assertion);
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

    public MCCIIN000002UV01 addPatientDiscoveryAsyncReq(PRPAIN201305UV02 request, AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.debug("Begin addPatientDiscoveryAsyncReq");
        MCCIIN000002UV01 ack = null;

        try
        {
            String url = oProxyHelper.getUrlLocalHomeCommunity(NhincConstants.PATIENT_DISCOVERY_ENTITY_ASYNC_REQ_QUEUE_SERVICE_NAME);
            EntityPatientDiscoveryAsyncReqQueuePortType port = getPort(url, NhincConstants.PATIENT_DISCOVERY_ACTION, WS_ADDRESSING_ACTION, assertion);

            if(request == null)
            {
                log.error("Request was null");
            }
            else if (assertion == null)
            {
                log.error("assertion was null");
            }
            else if (targets == null)
            {
                log.error("targets was null");
            }
            else if(port == null)
            {
                log.error("port was null");
            }
            else
            {
                RespondingGatewayPRPAIN201305UV02RequestType msg = new RespondingGatewayPRPAIN201305UV02RequestType();
                msg.setAssertion(assertion);
                msg.setPRPAIN201305UV02(request);
                msg.setNhinTargetCommunities(targets);

                ack = (MCCIIN000002UV01)oProxyHelper.invokePort(port, EntityPatientDiscoveryAsyncReqQueuePortType.class, "addPatientDiscoveryAsyncReq", msg);
            }
        }
        catch (Exception ex)
        {
            log.error("Error calling addPatientDiscoveryAsyncReq: " + ex.getMessage(), ex);
            ack = HL7AckTransforms.createAckFrom201305(request, NhincConstants.PATIENT_DISCOVERY_ANSWER_NOT_AVAIL_ERR_CODE);
        }

        log.debug("End addPatientDiscoveryAsyncReq");
        return ack;
    }

}
