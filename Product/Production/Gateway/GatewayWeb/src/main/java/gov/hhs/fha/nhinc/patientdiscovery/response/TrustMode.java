/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201306UV02;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201301UV02;
import org.hl7.v3.AddPatientCorrelationSecuredRequestType;
import org.hl7.v3.AddPatientCorrelationSecuredResponseType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201301Transforms;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationServiceSecured;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.patientdiscovery.PatientDiscovery201305Processor;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
/**
 *
 * @author dunnek
 */
public class TrustMode implements ResponseMode{
    private Log log = null;
    private static PatientCorrelationServiceSecured service = new PatientCorrelationServiceSecured();
    public TrustMode() {
        super();
        log = createLogger();
    }

    public PRPAIN201306UV02 processResponse(ResponseParams params)
    {
        log.debug("begin processResponse");


         PRPAIN201306UV02 response = params.response;
         AssertionType assertion = params.assertion;
         PRPAIN201305UV02 requestMsg = params.origRequest.getPRPAIN201305UV02();

        PRPAIN201301UV02 requestPatient = null;

        AddPatientCorrelationSecuredRequestType request = new AddPatientCorrelationSecuredRequestType();
        AddPatientCorrelationSecuredResponseType responseType;
        String url;

        url = getPCUrl();

        if(requestHasLivingSubjectId(requestMsg))
        {
            try
            {
                requestPatient = mergeIds(createPRPA201301(response), getPatientId(requestMsg));
                request.setPRPAIN201301UV02(requestPatient);
                PatientCorrelationSecuredPortType port =  getPCPort(url, assertion);

                responseType = port.addPatientCorrelation(request);
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage(),ex);
            }
        }
        else
        {
            log.debug("original request did not have a living subject id");
        }
        
        return response;
    }
    
    public PRPAIN201306UV02 processResponse(PRPAIN201306UV02 response, AssertionType assertion, II localPatId)
    {
        log.debug("begin processResponse");

        PRPAIN201301UV02 requestPatient = null;

        AddPatientCorrelationSecuredRequestType request = new AddPatientCorrelationSecuredRequestType();
        AddPatientCorrelationSecuredResponseType responseType;
        String url;

        url = getPCUrl();

        if(localPatId != null &&
                NullChecker.isNotNullish(localPatId.getExtension()) &&
                NullChecker.isNotNullish(localPatId.getRoot()))
        {
            try
            {
                requestPatient = mergeIds(createPRPA201301(response), localPatId);
                request.setPRPAIN201301UV02(requestPatient);
                PatientCorrelationSecuredPortType port =  getPCPort(url, assertion);

                responseType = port.addPatientCorrelation(request);
            }
            catch(Exception ex)
            {
                log.error(ex.getMessage(),ex);
            }
        }
        else
        {
            log.warn("Local Patient Id was not provided, no correlation will be attempted");
        }
        
        return response;
    }

    protected boolean requestHasLivingSubjectId(PRPAIN201305UV02 request)
    {
        boolean result = false;

        result = (getPatientId(request) != null);

        return result;
    }
 
    protected II getPatientId(PRPAIN201305UV02 request) {       
        return new PatientDiscovery201305Processor().extractPatientIdFrom201305(request);
    }

    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }
    protected PRPAIN201301UV02 createPRPA201301(PRPAIN201306UV02 input)
    {
        PRPAIN201301UV02 result = null;

        result = HL7PRPA201301Transforms.createPRPA201301(input, getLocalHomeCommunityId());

        return result;
    }
    protected String getLocalHomeCommunityId()
    {
        String result = "";

        try
        {
            result = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }
    protected PatientCorrelationSecuredPortType getPCPort(String url,  AssertionType assertion) {
        PatientCorrelationSecuredPortType port = service.getPatientCorrelationSecuredPort();

        log.info("Setting endpoint address to Nhin Patient Corrleation Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);
         SamlTokenCreator tokenCreator = new SamlTokenCreator();
         Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.PAT_CORR_ACTION);

         ((BindingProvider) port).getRequestContext().putAll(requestContext);
        return port;
    }
    private String getPCUrl() {
        String url = null;


        try {
            url = ConnectionManagerCache.getLocalEndpointURLByServiceName(NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.PATIENT_CORRELATION_SECURED_SERVICE_NAME);
            log.error(ex.getMessage());
        }


        return url;
    }

    private PRPAIN201301UV02 mergeIds(PRPAIN201301UV02 patient, II localId)
    {
        PRPAIN201301UV02 result = patient;
        
        II remoteId;

        log.debug("begin MergeIds");
        try
        {
            remoteId = result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().get(0);

            log.debug("Local Id = " + localId.getExtension() + "; remote id = " + remoteId.getExtension());

            //clear Id's
            result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().clear();
            
            //add both the local and remote id. 
            result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(localId);
            result.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient().getId().add(remoteId);

        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }
}


