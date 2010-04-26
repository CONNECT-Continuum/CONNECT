package gov.hhs.fha.nhinc.xdr.entity;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityXDR_Service", portName = "EntityXDR_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincentityxdr.EntityXDRPortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincentityxdr", wsdlLocation = "WEB-INF/wsdl/EntityXDR/EntityXDR.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityXDR {

    public oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryResponseType provideAndRegisterDocumentSetB(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayProvideAndRegisterDocumentSetRequestType respondingGatewayProvideAndRegisterDocumentSetRequest) {
        return new EntityXDRImpl().provideAndRegisterDocumentSetB(respondingGatewayProvideAndRegisterDocumentSetRequest);
    }

}
