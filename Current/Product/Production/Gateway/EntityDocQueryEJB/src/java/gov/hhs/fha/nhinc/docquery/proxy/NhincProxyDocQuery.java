/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.docquery.proxy;

import gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQueryPortType;
import javax.ejb.Stateless;
import javax.jws.WebService;

/**
 *
 * @author jhoppesc
 */
@WebService(serviceName = "NhincProxyDocQuery", portName = "NhincProxyDocQueryPortSoap11", endpointInterface = "gov.hhs.fha.nhinc.nhincproxydocquery.NhincProxyDocQueryPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxydocquery", wsdlLocation = "META-INF/wsdl/NhincProxyDocQuery/NhincProxyDocQuery.wsdl")
@Stateless
public class NhincProxyDocQuery implements NhincProxyDocQueryPortType {

    public oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse respondingGatewayCrossGatewayQuery(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayCrossGatewayQueryRequestType respondingGatewayCrossGatewayQueryRequest) {
        NhincProxyDocQueryImpl nhincDocQueryProxy = new NhincProxyDocQueryImpl();
        return nhincDocQueryProxy.respondingGatewayCrossGatewayQuery(respondingGatewayCrossGatewayQueryRequest);
    }

}
