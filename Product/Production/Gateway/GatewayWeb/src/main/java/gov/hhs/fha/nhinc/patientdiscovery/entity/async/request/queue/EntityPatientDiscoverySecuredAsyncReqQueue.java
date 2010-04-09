/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.entity.async.request.queue;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;

/**
 *
 * @author JHOPPESC
 */
@WebService(serviceName = "EntityPatientDiscoverySecuredAsyncReqQueue", portName = "EntityPatientDiscoverySecuredAsyncReqQueuePortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoverysecuredasyncreqqueue.EntityPatientDiscoverySecuredAsyncReqQueuePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecuredasyncreqqueue", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoverySecuredAsyncReqQueue/EntityPatientDiscoverySecuredAsyncReqQueue.wsdl")
@BindingType(value = "http://java.sun.com/xml/ns/jaxws/2003/05/soap/bindings/HTTP/")
public class EntityPatientDiscoverySecuredAsyncReqQueue {
    @Resource
    private WebServiceContext context;

    public org.hl7.v3.MCCIIN000002UV01 addPatientDiscoveryAsyncReq(org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType addPatientDiscoveryAsyncReqAsyncRequest) {
        return new EntityPatientDiscoverySecuredAsyncReqQueueImpl().addPatientDiscoveryAsyncReq(addPatientDiscoveryAsyncReqAsyncRequest, context);
    }

}
