package gov.hhs.fha.nhinc.docretrieve.deferred.nhin.proxy.request;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.RespondingGatewayCrossGatewayRetrieveRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayRetrieveSecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.nhindocretrievedeferredrequest.RespondingGatewayDeferredRequestRetrievePortType;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.service.ServiceUtil;
import gov.hhs.healthit.nhin.DocRetrieveAcknowledgementType;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.BindingProvider;
import javax.xml.ws.Service;
import java.util.Map;

/**
 * Created by
 * User: ralph
 * Date: Jul 26, 2010
 * Time: 11:46:39 AM
 */
public class NhinDocRetrieveDeferredReqWebServiceImpl implements NhinDocRetrieveDeferredReqProxy {
    private static Service cachedService = null;
    private static final String NAMESPACE_URI = "urn:gov:hhs:fha:nhinc:nhindocretrievedeferredrequest";
    private static final String SERVICE_LOCAL_PART = "RespondingGatewayDeferredRequest_Retrieve_Service";
    private static final String PORT_LOCAL_PART = "RespondingGatewayDeferredRequest_Retrieve_Port_Soap";
    private static final String WSDL_FILE = "NhinDocRetrieveDeferredReq.wsdl";
    private Log log = null;

    public NhinDocRetrieveDeferredReqWebServiceImpl() {
        log = LogFactory.getLog(getClass());
    }

    public DocRetrieveAcknowledgementType sendToRespondingGateway(RespondingGatewayCrossGatewayRetrieveSecuredRequestType body, AssertionType assertion) {
        String url = null;
        DocRetrieveAcknowledgementType result = new DocRetrieveAcknowledgementType();
        RegistryResponseType resp = new RegistryResponseType();

        resp.setStatus("Success");
        result.setMessage(resp);

        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_REQUEST);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " +
                    NhincConstants.NHIN_DOCRETRIEVE_DEFERRED_REQUEST + " for local home community");
            log.error(ex.getMessage());
        }

        if (NullChecker.isNotNullish(url)) {
            RespondingGatewayDeferredRequestRetrievePortType port = getPort(url);

            SamlTokenCreator tokenCreator = new SamlTokenCreator();
            Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.DOCRETRIEVEDEFERRED_REQUEST_ACTION);

            ((BindingProvider) port).getRequestContext().putAll(requestContext);

            result = port.respondingGatewayDeferredRequestCrossGatewayRetrieve(body.getRetrieveDocumentSetRequest());
        }

        return result;
    }

    protected RespondingGatewayDeferredRequestRetrievePortType getPort(String url) {

        RespondingGatewayDeferredRequestRetrievePortType port = null;
        Service service = getService();
        if(service != null)
        {
            log.debug("Obtained service - creating port.");
            port = service.getPort(new QName(NAMESPACE_URI, PORT_LOCAL_PART), RespondingGatewayDeferredRequestRetrievePortType.class);
            setEndpointAddress(port, url);
        }
        else
        {
            log.error("Unable to obtain serivce - no port created.");
        }
        return port;
    }


    protected Service getService()
    {
        if(cachedService == null)
        {
            try
            {
                cachedService = new ServiceUtil().createService(WSDL_FILE, NAMESPACE_URI, SERVICE_LOCAL_PART);
            }
            catch(Throwable t)
            {
                log.error("Error creating service: " + t.getMessage(), t);
            }
        }
        return cachedService;
    }


    protected void setEndpointAddress(RespondingGatewayDeferredRequestRetrievePortType port, String url)
    {
        if(port == null)
        {
            log.error("Port was null - not setting endpoint address.");
        }
        else if((url == null) || (url.length() < 1))
        {
            log.error("URL was null or empty - not setting endpoint address.");
        }
        else
        {
            log.info("Setting endpoint address to Document Retrieve Request Secure Service to " + url);
            ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
        }
    }

}
