/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
package gov.hhs.fha.nhinc.nhinhiem.proxy.notify;

import com.sun.xml.ws.developer.WSBindingProvider;
import com.sun.xml.ws.Closeable;
import gov.hhs.fha.nhinc.auditrepository.AuditRepositoryLogger;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxy;
import gov.hhs.fha.nhinc.auditrepository.nhinc.proxy.AuditRepositoryProxyObjectFactory;
import gov.hhs.fha.nhinc.common.auditlog.LogEventRequestType;
import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.hiem.dte.marshallers.WsntSubscribeMarshaller;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.saml.extraction.SamlTokenCreator;
import java.util.Map;
import javax.xml.ws.BindingProvider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.oasis_open.docs.wsn.b_2.Notify;
import org.oasis_open.docs.wsn.bw_2.NotificationConsumer;
import org.oasis_open.docs.wsn.bw_2.NotificationConsumerService;
import org.w3c.dom.Element;
import gov.hhs.fha.nhinc.common.eventcommon.NotifyEventType;
import gov.hhs.fha.nhinc.common.nhinccommon.AcknowledgementType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.hiem.consumerreference.ReferenceParametersElements;
import gov.hhs.fha.nhinc.hiem.dte.SoapUtil;
import gov.hhs.fha.nhinc.policyengine.PolicyEngineChecker;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxy;
import gov.hhs.fha.nhinc.policyengine.adapter.proxy.PolicyEngineProxyObjectFactory;
import gov.hhs.fha.nhinc.xmlCommon.XmlUtility;
import oasis.names.tc.xacml._2_0.context.schema.os.DecisionType;

/**
 *
 * @author Jon Hoppesch
 */
public class NhinHiemNotifyWebServiceProxy implements NhinHiemNotifyProxy {

    private static Log log = LogFactory.getLog(NhinHiemNotifyWebServiceProxy.class);
    static NotificationConsumerService nhinService = new NotificationConsumerService();

   public void notify(Element notifyElement, ReferenceParametersElements referenceParametersElements,AssertionType assertion, NhinTargetSystemType target) {
        String url = null;

        log.debug("Notify element received in NhinHiemNotifyWebServiceProxy: " + XmlUtility.serializeElementIgnoreFaults(notifyElement));

        if (target != null) {
            try {
                url = ConnectionManagerCache.getInstance().getEndpontURLFromNhinTarget(target, NhincConstants.HIEM_NOTIFY_SERVICE_NAME);
            } catch (ConnectionManagerException ex) {
                log.error("Error: Failed to retrieve url for service: " + NhincConstants.HIEM_NOTIFY_SERVICE_NAME);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        try
        {
            if (NullChecker.isNotNullish(url)) {
                NotificationConsumer port = getPort(url);

                log.debug("unmarshaling notify message");
                WsntSubscribeMarshaller notifyMarshaller = new WsntSubscribeMarshaller();
                Notify notify = notifyMarshaller.unmarshalNotifyRequest(notifyElement);

//                Element reMarshalled = notifyMarshaller.marshalNotifyRequest(notify);
//                log.debug("REMARSHALLED: " + XmlUtility.serializeElementIgnoreFaults(reMarshalled));

                // Policy check
                log.debug("Calling checkPolicy");
                if(checkPolicy(notify, assertion))
                {
                    log.debug("attaching reference parameter headers");
                    SoapUtil soapUtil = new SoapUtil();
                    soapUtil.attachReferenceParameterElements((WSBindingProvider) port, referenceParametersElements);

                    auditInputMessage(notify, assertion);

                    log.debug("Calling token creator");
                    SamlTokenCreator tokenCreator = new SamlTokenCreator();
                    Map requestContext = tokenCreator.CreateRequestContext(assertion, url, NhincConstants.SUBSCRIBE_ACTION);
                    ((BindingProvider) port).getRequestContext().putAll(requestContext);

                    try {
                        log.debug("Calling notification consumer port in NhinHiemWebServiceProxy.");
                        port.notify(notify);
                    } catch (Exception ex) {
                        log.error("Error occurred while trying to invoke notify", ex);
                    }

                    ((Closeable)port).close();
                }
                else
                {
                    log.error("Failed policy check on send NHIN notify message");
                }
            } else {
                log.error("The URL for service: " + NhincConstants.HIEM_NOTIFY_SERVICE_NAME + " is null");
            }
        }
        catch(Throwable t)
        {
            // TODO: Figure out what to do with the exception
            log.error("Error sending notify to remote gateway: " + t.getMessage(), t);
        }

    }

    private NotificationConsumer getPort(String url) {
        NotificationConsumer port = nhinService.getNotificationConsumerPort();

        log.info("Setting endpoint address to Nhin Hiem Notify Service to " + url);
        ((BindingProvider) port).getRequestContext().put(javax.xml.ws.BindingProvider.ENDPOINT_ADDRESS_PROPERTY, url);

        return port;
    }

    private boolean checkPolicy(Notify notify, AssertionType assertion) {
        log.debug("In NhinHiemNotifyWebServiceProxy.checkPolicy");
        boolean policyIsValid = false;

        NotifyEventType policyCheckReq = new NotifyEventType();
        policyCheckReq.setDirection(NhincConstants.POLICYENGINE_OUTBOUND_DIRECTION);
        gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType request = new gov.hhs.fha.nhinc.common.eventcommon.NotifyMessageType();
        request.setAssertion(assertion);
        request.setNotify(notify);
        policyCheckReq.setMessage(request);

        PolicyEngineChecker policyChecker = new PolicyEngineChecker();
        CheckPolicyRequestType policyReq = policyChecker.checkPolicyNotify(policyCheckReq);
        policyReq.setAssertion(assertion);
        PolicyEngineProxyObjectFactory policyEngFactory = new PolicyEngineProxyObjectFactory();
        PolicyEngineProxy policyProxy = policyEngFactory.getPolicyEngineProxy();

        CheckPolicyResponseType policyResp = policyProxy.checkPolicy(policyReq, assertion);

        if (policyResp.getResponse() != null &&
                NullChecker.isNotNullish(policyResp.getResponse().getResult()) &&
                policyResp.getResponse().getResult().get(0).getDecision() == DecisionType.PERMIT) {
            policyIsValid = true;
        }

        log.debug("Finished NhinHiemNotifyWebServiceProxy.checkPolicy - valid: " + policyIsValid);
        return policyIsValid;
    }

    private void auditInputMessage(Notify notify, AssertionType assertion) {
        log.debug("In NhinHiemNotifyWebServiceProxy.auditInputMessage");
        AcknowledgementType ack = null;
        try
        {
            AuditRepositoryLogger auditLogger = new AuditRepositoryLogger();

            gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType message = new gov.hhs.fha.nhinc.common.nhinccommoninternalorch.NotifyRequestType();
            message.setAssertion(assertion);
            message.setNotify(notify);

            LogEventRequestType auditLogMsg = auditLogger.logNhinNotifyRequest(message, NhincConstants.AUDIT_LOG_OUTBOUND_DIRECTION, NhincConstants.AUDIT_LOG_NHIN_INTERFACE);

            if(auditLogMsg != null)
            {
                AuditRepositoryProxyObjectFactory auditRepoFactory = new AuditRepositoryProxyObjectFactory();
                AuditRepositoryProxy proxy = auditRepoFactory.getAuditRepositoryProxy();
                ack = proxy.auditLog(auditLogMsg, assertion);
            }
        }
        catch(Throwable t)
        {
            log.error("Error logging subscribe message: " + t.getMessage(), t);
        }
    }

}
