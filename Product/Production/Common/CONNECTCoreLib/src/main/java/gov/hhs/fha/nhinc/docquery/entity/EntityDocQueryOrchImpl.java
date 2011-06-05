/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.docquery.entity;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.common.auditlog.AdhocQueryResponseMessageType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetCommunitiesType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifierType;
import gov.hhs.fha.nhinc.common.nhinccommon.QualifiedSubjectIdentifiersType;
import gov.hhs.fha.nhinc.docquery.DocQueryAuditLog;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocumentConstants;
import java.util.List;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryRequest;
import oasis.names.tc.ebxml_regrep.xsd.query._3.AdhocQueryResponse;
import oasis.names.tc.ebxml_regrep.xsd.rim._3.SlotType1;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryError;
import oasis.names.tc.ebxml_regrep.xsd.rs._3.RegistryErrorList;
import gov.hhs.fha.nhinc.common.nhinccommonentity.RespondingGatewayCrossGatewayQuerySecuredRequestType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.connectmgr.data.CMUrlInfos;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocQueryRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.GetAggResultsDocQueryResponseType;
import gov.hhs.fha.nhinc.gateway.aggregator.StartTransactionDocQueryRequestType;
import gov.hhs.fha.nhinc.gateway.aggregator.document.DocQueryAggregator;
import gov.hhs.fha.nhinc.perfrepo.PerformanceManager;
import gov.hhs.fha.nhinc.util.HomeCommunityMap;
import java.sql.Timestamp;
import java.util.HashMap;

public class EntityDocQueryOrchImpl {

    private Log log = null;
    private String localHomeCommunity = null;
    private static final long SLEEP_MS = 1000;
    private static final long AGGREGATOR_TIMEOUT_MS = 40000;

    public EntityDocQueryOrchImpl() {
        log = createLogger();
    }

    protected Log createLogger() {
        return ((log != null) ? log : LogFactory.getLog(getClass()));
    }

    /**
     *
     * @param adhocQueryRequest
     * @param assertion
     * @param targets
     * @return <code>AdhocQueryResponse</code>
     */
    public AdhocQueryResponse respondingGatewayCrossGatewayQuery(AdhocQueryRequest adhocQueryRequest,
            AssertionType assertion, NhinTargetCommunitiesType targets) {
        log.debug("Entering EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");

        AdhocQueryResponse response = null;
        CMUrlInfos urlInfoList = null;
        boolean isTargeted = false;

        DocQueryAuditLog auditLog = createAuditLog();
        RespondingGatewayCrossGatewayQuerySecuredRequestType request = new RespondingGatewayCrossGatewayQuerySecuredRequestType();
        request.setAdhocQueryRequest(adhocQueryRequest);
        request.setNhinTargetCommunities(targets);
        String homeCommunityId = HomeCommunityMap.getLocalHomeCommunityId();
        auditDocQueryRequest(request, assertion, auditLog, homeCommunityId);

        try {
            DocQueryAggregator aggregator = createDocQueryAggregator();

            if (targets != null &&
                    NullChecker.isNotNullish(targets.getNhinTargetCommunity())) {
                isTargeted = true;
            }

            // Obtain all the URLs for the targets being sent to
            try {
                urlInfoList = ConnectionManagerCache.getEndpontURLFromNhinTargetCommunities(targets, NhincConstants.DOC_QUERY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Failed to obtain target URLs", ex);
                return null;
            }

            // Validate that the message is not null
            if (adhocQueryRequest != null &&
                    adhocQueryRequest.getAdhocQuery() != null &&
                    NullChecker.isNotNullish(adhocQueryRequest.getAdhocQuery().getSlot())) {
                List<SlotType1> slotList = adhocQueryRequest.getAdhocQuery().getSlot();
                String localAA = new EntityDocQueryHelper().getLocalAssigningAuthority(slotList);
                String uniquePatientId = new EntityDocQueryHelper().getUniquePatientId(slotList);
                log.debug("respondingGatewayCrossGatewayQuery EntityDocQueryHelper uniquePatientId: " + uniquePatientId);

                List<QualifiedSubjectIdentifierType> correlationsResult = new EntityDocQueryHelper().retreiveCorrelations(slotList, urlInfoList, assertion, isTargeted, homeCommunityId);

                // Make sure the valid results back
                if (NullChecker.isNotNullish(correlationsResult)) {

                    QualifiedSubjectIdentifiersType subjectIds = new QualifiedSubjectIdentifiersType();
                    for (QualifiedSubjectIdentifierType subjectId : correlationsResult) {
                        if (subjectId != null) {
                            subjectIds.getQualifiedSubjectIdentifier().add(subjectId);
                        }
                    }

                    // Log the start of the entity performance record
                    Timestamp starttime = new Timestamp(System.currentTimeMillis());
                    Long logId = PerformanceManager.getPerformanceManagerInstance().logPerformanceStart(starttime, NhincConstants.DOC_QUERY_SERVICE_NAME, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, homeCommunityId);

                    String transactionId = startTransaction(aggregator, subjectIds);

                    sendQueryMessages(transactionId, correlationsResult, adhocQueryRequest, assertion, localAA, uniquePatientId);

                    response = retrieveDocQueryResults(aggregator, transactionId);

                    // Log the end of the entity performance record
                    Timestamp stoptime = new Timestamp(System.currentTimeMillis());
                    PerformanceManager.getPerformanceManagerInstance().logPerformanceStop(logId, starttime, stoptime);
                } else {
                    log.error("No patient correlations found.");
                    response = createErrorResponse("No patient correlations found.");
                }
            } else {
                log.error("Incomplete doc query message");
                response = createErrorResponse("Incomplete doc query message");
            }
        } catch (Throwable t) {
            log.error("Error occured processing doc query on entity interface: " + t.getMessage(), t);
            response = createErrorResponse("Fault encountered processing internal document query");
        }
        auditDocQueryResponse(response, assertion, auditLog, homeCommunityId);
        log.debug("Exiting EntityDocQuerySecuredImpl.respondingGatewayCrossGatewayQuery...");
        return response;
    }

    protected DocQueryAuditLog createAuditLog() {
        return new DocQueryAuditLog();
    }

    protected DocQueryAggregator createDocQueryAggregator() {
        return new DocQueryAggregator();
    }

    private void auditDocQueryRequest(RespondingGatewayCrossGatewayQuerySecuredRequestType request, AssertionType assertion, DocQueryAuditLog auditLog, String targetHomeCommunityId) {
        if (auditLog != null) {
            auditLog.auditDQRequest(request.getAdhocQueryRequest(), assertion, NhincConstants.AUDIT_LOG_INBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, targetHomeCommunityId);
        }
    }

    private void auditDocQueryResponse(AdhocQueryResponse response, AssertionType assertion, DocQueryAuditLog auditLog, String targetHomeCommunityId) {
        if (auditLog != null) {
            AdhocQueryResponseMessageType auditMsg = new AdhocQueryResponseMessageType();
            auditMsg.setAdhocQueryResponse(response);
            auditMsg.setAssertion(assertion);
            auditLog.auditDQResponse(response, assertion, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_ENTITY_INTERFACE, targetHomeCommunityId);
        }
    }

    protected boolean getPropertyBoolean(String sPropertiesFile, String sPropertyName) {
        boolean sPropertyValue = false;
        try {
            sPropertyValue = PropertyAccessor.getPropertyBoolean(sPropertiesFile, sPropertyName);
        } catch (PropertyAccessException ex) {
            log.error(ex.getMessage());
        }
        return sPropertyValue;
    }

    protected String getLocalHomeCommunityId() {
        String sHomeCommunity = null;

        if (localHomeCommunity != null) {
            sHomeCommunity = localHomeCommunity;
        } else {
            try {
                sHomeCommunity = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
            } catch (PropertyAccessException ex) {
                log.error(ex.getMessage());
            }
        }
        return sHomeCommunity;
    }

    private void sendQueryMessages(String transactionId, List<QualifiedSubjectIdentifierType> correlationsResult, AdhocQueryRequest queryRequest, AssertionType assertion, String localAA, String localPatientId) {
        for (QualifiedSubjectIdentifierType subId : correlationsResult) {
            DocQuerySender querySender = new DocQuerySender(transactionId, assertion, subId, queryRequest, localAA, localPatientId);
            querySender.sendMessage();
        }
    }

    private AdhocQueryResponse retrieveDocQueryResults(DocQueryAggregator aggregator, String transactionId) {
        log.debug("Begin retrieveDocQueryResults");
        AdhocQueryResponse response = null;
        boolean retrieveTimedOut = false;

        // Agggregator query request message
        GetAggResultsDocQueryRequestType aggResultsRequest = new GetAggResultsDocQueryRequestType();
        aggResultsRequest.setTransactionId(transactionId);
        aggResultsRequest.setTimedOut(retrieveTimedOut);

        // Loop until responses are received
        long startTime = System.currentTimeMillis();
        while (response == null) {
            GetAggResultsDocQueryResponseType aggResultsResponse = aggregator.getAggResults(aggResultsRequest);
            String retrieveStatus = aggResultsResponse.getStatus();
            if (DocumentConstants.COMPLETE_TEXT.equals(retrieveStatus)) {
                response = aggResultsResponse.getAdhocQueryResponse();
            } else if (DocumentConstants.FAIL_TEXT.equals(retrieveStatus)) {
                log.error("Document query aggregator reports failure - returning error");
                response = createErrorResponse("Processing internal document query");
            } else {
                retrieveTimedOut = retrieveTimedOut(startTime);
                if (retrieveTimedOut) {
                    aggResultsRequest.setTimedOut(retrieveTimedOut);
                    aggResultsResponse = aggregator.getAggResults(aggResultsRequest);
                    if (DocumentConstants.COMPLETE_TEXT.equals(retrieveStatus)) {
                        response = aggResultsResponse.getAdhocQueryResponse();
                    } else {
                        log.warn("Document query aggregation timeout - returning error.");
                        response = createErrorResponse("Processing internal document query - failure in timeout");
                    }
                } else {
                    try {
                        Thread.sleep(SLEEP_MS);
                    } catch (Throwable t) {
                    }
                }
            }
        }
        log.debug("End retrieveDocQueryResults");
        return response;
    }

    private boolean retrieveTimedOut(long startTime) {
        long timeout = startTime + AGGREGATOR_TIMEOUT_MS;
        return (timeout < System.currentTimeMillis());
    }

    private String startTransaction(DocQueryAggregator aggregator, QualifiedSubjectIdentifiersType subjectIds) {
        StartTransactionDocQueryRequestType docQueryStartTransaction = new StartTransactionDocQueryRequestType();
        docQueryStartTransaction.setQualifiedPatientIdentifiers(subjectIds);

        HashMap<String, String> assigningAuthorityToHomeCommunityMap = new HashMap<String, String>();
        log.debug("Starting doc query transaction");
        String transactionId = aggregator.startTransaction(docQueryStartTransaction, assigningAuthorityToHomeCommunityMap);
        if (log.isDebugEnabled()) {
            log.debug("Doc query transaction id: " + transactionId);
        }
        return transactionId;
    }

    private AdhocQueryResponse createErrorResponse(String codeContext) {
        AdhocQueryResponse response = new AdhocQueryResponse();

        RegistryErrorList regErrList = new RegistryErrorList();
        response.setRegistryErrorList(regErrList);
        response.setStatus("urn:oasis:names:tc:ebxml-regrep:ResponseStatusType:Failure");
        RegistryError regErr = new RegistryError();
        regErrList.getRegistryError().add(regErr);
        regErr.setCodeContext(codeContext);
        regErr.setErrorCode("XDSRepositoryError");
        regErr.setSeverity("Error");
        return response;
    }

}
