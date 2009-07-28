/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.adapterdocumentregistry.proxy;

import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.docregistryadapter.proxy.AdapterDocumentRegistryProxy;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import ihe.iti.xds_b._2007.DocumentRegistryPortType;
import ihe.iti.xds_b._2007.DocumentRegistryService;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author svalluripalli
 */
public class AdapterDocumentRegistryWebServiceProxy implements AdapterDocumentRegistryProxy
{
    private static Log log = LogFactory.getLog(AdapterDocumentRegistryWebServiceProxy.class);
    private static DocumentRegistryService documentRegistryservice = new DocumentRegistryService();

    /**
     * 
     * @param request
     * @return AdhocQueryResponse
     */
    public AdhocQueryResponse registryStoredQuery(AdhocQueryRequest request) {
        AdhocQueryResponse result = null;
        try {
            String url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.ADAPTER_DOC_REGISTRY_SERVICE_NAME);
            DocumentRegistryPortType port = getPort(url);
            result = port.documentRegistryRegistryStoredQuery(request);
            System.out.println("Result = "+result);
        } catch (Exception ex) {
            log.error(ex.getMessage());
        }
        return result;
    }

    /**
     * 
     * @param url
     * @return DocumentRegistryPortType
     */
    private DocumentRegistryPortType getPort(String url)
    {
        DocumentRegistryPortType port = documentRegistryservice.getDocumentRegistryPortSoap();

        log.info("Setting endpoint address to Audit Repository Service to " + url);
        ((javax.xml.ws.BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }
    
}
