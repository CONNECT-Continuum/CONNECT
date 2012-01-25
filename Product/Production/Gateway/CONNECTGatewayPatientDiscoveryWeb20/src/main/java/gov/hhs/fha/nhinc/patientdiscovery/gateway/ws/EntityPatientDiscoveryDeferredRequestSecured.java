/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.patientdiscovery.gateway.ws;

import gov.hhs.fha.nhinc.patientdiscovery.entity.deferred.request.EntityPatientDiscoveryDeferredRequestImpl;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.BindingType;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.soap.Addressing;

import org.hl7.v3.MCCIIN000002UV01;
import org.hl7.v3.RespondingGatewayPRPAIN201305UV02SecuredRequestType;

@WebService(serviceName = "EntityPatientDiscoverySecuredAsyncReq", portName = "EntityPatientDiscoverySecuredAsyncReqPortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitypatientdiscoverysecuredasyncreq.EntityPatientDiscoverySecuredAsyncReqPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitypatientdiscoverysecuredasyncreq", wsdlLocation = "WEB-INF/wsdl/EntityPatientDiscoveryDeferredRequestSecured/EntityPatientDiscoverySecuredAsyncReq.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
@Addressing(enabled = true)
public class EntityPatientDiscoveryDeferredRequestSecured extends
		PatientDiscoveryBase {

	@Resource
	private WebServiceContext context;

	public EntityPatientDiscoveryDeferredRequestSecured() {
		super();
	}

	public EntityPatientDiscoveryDeferredRequestSecured(
			PatientDiscoveryServiceFactory serviceFactory) {
		super(serviceFactory);
	}

	public MCCIIN000002UV01 processPatientDiscoveryAsyncReq(
			RespondingGatewayPRPAIN201305UV02SecuredRequestType request) {
		MCCIIN000002UV01 response = null;

		EntityPatientDiscoveryDeferredRequestImpl serviceImpl = getServiceFactory()
		.getEntityPatientDiscoveryDeferredRequestImpl();
		if (serviceImpl != null) {
			response = serviceImpl.processPatientDiscoveryAsyncRequestSecured(
					request, getWebServiceContext());
		}
		return response;
	}

	protected WebServiceContext getWebServiceContext() {
		return context;
	}
}
