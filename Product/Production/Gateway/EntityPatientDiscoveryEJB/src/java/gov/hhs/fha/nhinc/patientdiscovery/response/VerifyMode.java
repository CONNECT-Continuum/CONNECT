/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.patientdiscovery.response;
import org.hl7.v3.PRPAIN201305UV02;
import org.hl7.v3.PRPAIN201306UV02;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenExtractor;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationSecuredPortType;
import gov.hhs.fha.nhinc.nhinccomponentpatientcorrelation.PatientCorrelationServiceSecured;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import javax.xml.ws.WebServiceContext;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxy;
import gov.hhs.fha.nhinc.mpi.proxy.AdapterMpiProxyObjectFactory;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PatientTransforms;
import gov.hhs.fha.nhinc.transform.subdisc.HL7PRPA201305Transforms;
import java.util.List;
import javax.xml.bind.JAXBElement;
import org.hl7.v3.II;
import org.hl7.v3.PRPAIN201305UV02QUQIMT021001UV01ControlActProcess;
import org.hl7.v3.PRPAMT201301UV02Patient;
import org.hl7.v3.PRPAMT201301UV02Person;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectAdministrativeGender;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthPlaceAddress;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthPlaceName;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectBirthTime;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectId;
import org.hl7.v3.PRPAMT201306UV02LivingSubjectName;
import org.hl7.v3.PRPAMT201306UV02MothersMaidenName;
import org.hl7.v3.PRPAMT201306UV02ParameterList;
import org.hl7.v3.PRPAMT201306UV02QueryByParameter;
import org.hl7.v3.PRPAMT201310UV02Patient;


/**
 *
 * @author dunnek
 */
public class VerifyMode implements ResponseMode{
    private Log log = null;
    
    public VerifyMode() {
        super();
        log = createLogger();
    }
    public PRPAIN201306UV02 processResponse(ResponseParams params)
    {
        log.debug("begin processResponse");
        PRPAIN201306UV02 response = params.response;
        WebServiceContext context = params.context;
        PRPAIN201305UV02 requestMsg = params.origRequest.getPRPAIN201305UV02();

        PRPAIN201306UV02 result = response;

        // Create an assertion class from the contents of the SAML token
        AssertionType assertion = SamlTokenExtractor.GetAssertion(context);

        if(patientExistsLocally(requestMsg, assertion, response))
        {
            log.debug("patient exists locally, adding correlation");
            new TrustMode().processResponse(params);
        }
        else
        {
            log.warn("Patient does not exist locally, correlation not added");
        }
        return result;
    }
    protected Log createLogger()
    {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    protected PRPAMT201301UV02Patient getPatient(PRPAMT201306UV02ParameterList paramList)
    {
        return HL7PatientTransforms.create201301Patient(paramList);
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
    protected PRPAMT201301UV02Patient extractPatient(PRPAIN201306UV02 response)
    {
         PRPAMT201310UV02Patient remotePatient = response.getControlActProcess().getSubject().get(0).getRegistrationEvent().getSubject1().getPatient();
         return HL7PatientTransforms.createPRPAMT201301UVPatient(remotePatient);
    }
    protected PRPAIN201305UV02 convert201306to201305(PRPAIN201306UV02 response)
    {
        PRPAIN201305UV02 result;
        String localHCID = getLocalHomeCommunityId();
        PRPAMT201301UV02Patient patient = extractPatient(response);
        result = HL7PRPA201305Transforms.createPRPA201305(patient, localHCID, localHCID, localHCID);

        return result;
    }
    protected boolean patientExistsLocally(PRPAIN201305UV02 query, AssertionType assertion, PRPAIN201306UV02 response)
    {
        boolean result = false;
        
        //PRPAMT201301UV02Patient patient = getPatient(response.getControlActProcess().getQueryByParameter().getValue().getParameterList());
        //query.getControlActProcess().setQueryByParameter(response.getControlActProcess().getQueryByParameter());

        PRPAIN201305UV02 mpiQuery;
        
        PRPAIN201305UV02QUQIMT021001UV01ControlActProcess controlActProcess = new PRPAIN201305UV02QUQIMT021001UV01ControlActProcess();
        
        mpiQuery =convert201306to201305(response);

        mpiQuery.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId().clear();
        PRPAIN201306UV02 mpiResult = queryMpi(mpiQuery, assertion);

        if(mpiResult != null)
        {
            try
            {
                List<PRPAMT201306UV02LivingSubjectId> localIds;
                List<PRPAMT201306UV02LivingSubjectId> remoteIds;

                localIds = mpiResult.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId();
                remoteIds = response.getControlActProcess().getQueryByParameter().getValue().getParameterList().getLivingSubjectId();

                result = compareId(localIds, remoteIds);

            }
            catch(Exception ex)
            {
                log.error(ex.getMessage(), ex);
                result = false;
            }
        }
        return result;


    }

    protected boolean compareId(List<PRPAMT201306UV02LivingSubjectId> localIds, List<PRPAMT201306UV02LivingSubjectId> remoteIds)
    {
        boolean result = false;

        try
        {
            for(PRPAMT201306UV02LivingSubjectId localId : localIds)
            {
                for(PRPAMT201306UV02LivingSubjectId remoteId : remoteIds)
                {
                    if(compareId(localId, remoteId))
                    {
                        result = true;
                        break;
                    }
                }
            }
        }
        catch(Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }
        return result;
    }
    protected boolean compareId(PRPAMT201306UV02LivingSubjectId localId, PRPAMT201306UV02LivingSubjectId remoteId)
    {
        boolean result = false;

        for(II localII : localId.getValue())
        {
            for(II remoteII :remoteId.getValue())
            {
                if (compareId(localII, remoteII))
                {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }
    private boolean compareId(II localId, II remoteId)
    {
        boolean result = false;

        if(localId != null && remoteId != null)
        {
            String localExt = localId.getExtension();
            String localRoot = localId.getRoot();

            if(remoteId.getExtension().equalsIgnoreCase(localExt) &&
                      remoteId.getRoot().equalsIgnoreCase(localRoot))
            {
                result = true;
            }
        }

        return result;
    }
    protected PRPAIN201306UV02 queryMpi(PRPAIN201305UV02 query, AssertionType assertion) {
        PRPAIN201306UV02 queryResults = new PRPAIN201306UV02();

        if (query != null) {
            // Query the MPI to see if the patient is found
            AdapterMpiProxyObjectFactory mpiFactory = new AdapterMpiProxyObjectFactory();
            AdapterMpiProxy mpiProxy = mpiFactory.getAdapterMpiProxy();
            log.info("Sending query to the Secured MPI");
            queryResults =  mpiProxy.findCandidates(query, assertion);

        } else {
            log.error("MPI Request is null");
            queryResults =
                    null;
        }

        return queryResults;
    }
    private JAXBElement<PRPAMT201306UV02QueryByParameter> copyParameterList(PRPAMT201306UV02QueryByParameter input)
    {
 
        PRPAMT201306UV02QueryByParameter result = new PRPAMT201306UV02QueryByParameter();

        result.setParameterList(copyParameterList(input.getParameterList()));
        
        javax.xml.namespace.QName xmlqname = new javax.xml.namespace.QName("urn:hl7-org:v3", "PRPAMT201306UV02QueryByParameter");
        JAXBElement<PRPAMT201306UV02QueryByParameter> jaxbParams= new JAXBElement<PRPAMT201306UV02QueryByParameter>(xmlqname, PRPAMT201306UV02QueryByParameter.class, result);


        return jaxbParams;
    }
    private PRPAMT201306UV02ParameterList copyParameterList(PRPAMT201306UV02ParameterList input)
    {
        PRPAMT201306UV02ParameterList result = new PRPAMT201306UV02ParameterList();

        for(PRPAMT201306UV02LivingSubjectAdministrativeGender gender : input.getLivingSubjectAdministrativeGender())
        {
            result.getLivingSubjectAdministrativeGender().add(gender);
        }
        for(PRPAMT201306UV02LivingSubjectBirthPlaceAddress birthPlace : input.getLivingSubjectBirthPlaceAddress())
        {
            result.getLivingSubjectBirthPlaceAddress().add(birthPlace);
        }
        for(PRPAMT201306UV02LivingSubjectBirthPlaceName birthPlaceName : input.getLivingSubjectBirthPlaceName())
        {
            result.getLivingSubjectBirthPlaceName().add(birthPlaceName);
        }
        for(PRPAMT201306UV02LivingSubjectBirthTime birthTime : input.getLivingSubjectBirthTime())
        {
            result.getLivingSubjectBirthTime().add(birthTime);
        }
        for(PRPAMT201306UV02LivingSubjectName subjectName : input.getLivingSubjectName())
        {
            result.getLivingSubjectName().add(subjectName);
        }
        for(PRPAMT201306UV02MothersMaidenName motherName : input.getMothersMaidenName())
        {
            result.getMothersMaidenName().add(motherName);
        }


        result.setId(input.getId());

        return result;
    }
}
