package gov.hhs.fha.nhinc.xdr.response.proxy;

import javax.jws.WebService;
import javax.xml.ws.BindingType;

/**
 *
 * @author Neil Webb
 */
@WebService(serviceName = "ProxyXDRResponse_Service", portName = "ProxyXDRResponse_Port", endpointInterface = "gov.hhs.fha.nhinc.nhincproxyxdr.async.response.ProxyXDRResponsePortType", targetNamespace = "urn:gov:hhs:fha:nhinc:nhincproxyxdr:async:response", wsdlLocation = "WEB-INF/wsdl/NhincProxyXDRResponse/NhincProxyXDRResponse.wsdl")
@BindingType(value = javax.xml.ws.soap.SOAPBinding.SOAP12HTTP_BINDING)
public class NhincProxyXDRResponse
{

    public ihe.iti.xdr._2007.AcknowledgementType provideAndRegisterDocumentSetBResponse(gov.hhs.fha.nhinc.common.nhinccommonproxy.RespondingGatewayProvideAndRegisterDocumentSetResponseRequestType provideAndRegisterResponseRequest)
    {
        return new NhincProxyXDRResponseImpl().provideAndRegisterDocumentSetBResponse(provideAndRegisterResponseRequest);
    }

}
