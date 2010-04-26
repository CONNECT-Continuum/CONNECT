package gov.hhs.fha.nhinc.docretrieve.entity;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Sai Valluripalli
 */
@WebService(serviceName = "EntityDocRetrieve", portName = "EntityDocRetrievePortSoap", endpointInterface = "gov.hhs.fha.nhinc.entitydocretrieve.EntityDocRetrievePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:entitydocretrieve", wsdlLocation = "WEB-INF/wsdl/EntityDocRetrieve/EntityDocRetrieve.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class EntityDocRetrieve {

    public ihe.iti.xds_b._2007.RetrieveDocumentSetResponseType respondingGatewayCrossGatewayRetrieve(gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayRetrieveRequestType respondingGatewayCrossGatewayRetrieveRequest) {
        return new EntityDocRetrieveImpl().respondingGatewayCrossGatewayRetrieve(respondingGatewayCrossGatewayRetrieveRequest);
    }

}
