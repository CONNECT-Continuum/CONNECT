/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.transform.audit;

/**
 *
 * @author MFLYNN02
 */
public class AuditDataTransformConstants {
    
    public static final String EVENT_ACTION_CODE_CREATE = "C";
    public static final String EVENT_ACTION_CODE_READ = "R";
    public static final String EVENT_ACTION_CODE_UPDATE = "U";
    public static final String EVENT_ACTION_CODE_DELETE = "D";
    public static final String EVENT_ACTION_CODE_EXECUTE = "E";
    public static final Integer EVENT_OUTCOME_INDICATOR_SUCCESS = 0;
    public static final Integer EVENT_OUTCOME_INDICATOR_MINOR_FAILURE = 4;
    public static final Integer EVENT_OUTCOME_INDICATOR_SERIOUS_FAILURE = 8;
    public static final Integer EVENT_OUTCOME_INDICATOR_MAJOR_FAILURE = 12;
    public static final Short NETWORK_ACCESSOR_PT_TYPE_CODE_IP = 2;
    public static final Short PARTICIPANT_OJB_TYPE_CODE_PERSON = 1;
    public static final Short PARTICIPANT_OJB_TYPE_CODE_ROLE_PATIENT = 1;
    public static final String PARTICIPANT_OJB_ID_TYPE_CODE_PATIENTNUM = "2";
    public static final String EVENT_ID_CODE_SYS_NAME_DOC = "DCM";
    public static final String EVENT_ID_CODE_SYS_NAME_SDN = "SDN";    
    public static final String EVENT_ID_CODE_SYS_NAME_SDR = "SDR";
    public static final String EVENT_ID_CODE_SYS_NAME_SDD = "SDD";
    public static final String EVENT_ID_CODE_SYS_NAME_SD = "ESD";
    public static final String EVENT_ID_CODE_SYS_NAME_SR = "ESR";
    public static final String EVENT_ID_CODE_SYS_NAME_ACK = "ACK";
    public static final String EVENT_ID_CODE_SYS_NAME_ADQ = "ADQ";
    public static final String EVENT_ID_CODE_SYS_NAME_SRI = "SRI";
    public static final String EVENT_ID_CODE_SYS_NAME_SRO = "SRO";
    public static final String EVENT_ID_CODE_SYS_NAME_SUB = "SUB";
    public static final String EVENT_ID_CODE_SYS_NAME_UNS = "UNS";
    public static final String EVENT_ID_CODE_SYS_NAME_NOT = "NOT";
    public static final String EVENT_ID_DISPLAY_NAME_DOCQUERY = "Query";
    public static final String EVENT_ID_DISPLAY_NAME_DOCRETRIEVE = "Retrieve";
    public static final String EVENT_ID_DISPLAY_NAME_SDNEW = "Subject Discovery New";
    public static final String EVENT_ID_DISPLAY_NAME_SDREV = "Subject Discovery Revised";
    public static final String EVENT_ID_DISPLAY_NAME_SDDEL = "Subject Discovery Revoke";
    public static final String EVENT_ID_DISPLAY_NAME_SDRID = "Subject Discovery Reidentification";
    public static final String EVENT_ID_DISPLAY_NAME_ENTITY_DOCQUERY = "EntityQuery";
    public static final String EVENT_ID_DISPLAY_NAME_ENTITY_DOCRETRIEVE = "EntityRetrieve";
    public static final String EVENT_ID_DISPLAY_NAME_ENTITY_SD = "EntityAnnouncePatient";
    public static final String EVENT_ID_DISPLAY_NAME_ENTITY_SDDEL = "EntityAnnounceRevoke";
    public static final String EVENT_ID_DISPLAY_NAME_ACK = "Acknowledge";
    public static final String EVENT_ID_DISPLAY_NAME_SUBSCRIBE = "Subscribe";
    public static final String EVENT_ID_DISPLAY_NAME_UNSUBSCRIBE = "Unsubscribe";
    public static final String EVENT_ID_DISPLAY_NAME_NOTIFY = "Notify";
    public static final String EBXML_RESPONSE_PATIENTID_IDENTIFICATION_SCHEME = "urn:uuid:6b5aea1a-874d-4603-a4bc-96a0a7b38446";
    public static final String EBXML_RESPONSE_PATIENTID_NAME = "XDSDocumentEntry.patientId";

}
