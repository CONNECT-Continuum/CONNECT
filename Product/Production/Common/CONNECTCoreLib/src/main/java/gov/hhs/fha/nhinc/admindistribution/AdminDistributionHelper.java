/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *  
 * Copyright 2010(Year date of delivery) United States Government, as represented by the Secretary of Health and Human Services.  All rights reserved.
 *  
 */
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.admindistribution;

import gov.hhs.fha.nhinc.common.nhinccommon.HomeCommunityType;
import gov.hhs.fha.nhinc.common.nhinccommon.NhinTargetSystemType;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerCache;
import gov.hhs.fha.nhinc.connectmgr.ConnectionManagerException;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants.GATEWAY_API_LEVEL;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import gov.hhs.fha.nhinc.properties.PropertyAccessException;
import gov.hhs.fha.nhinc.properties.PropertyAccessor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import gov.hhs.fha.nhinc.webserviceproxy.WebServiceProxyHelper;
import javax.xml.ws.Service;

/**
 *
 * @author dunnek
 */
public class AdminDistributionHelper {

    private Log log = null;

    public AdminDistributionHelper() {
        log = createLogger();
    }

    protected Log createLogger() {
        return LogFactory.getLog(getClass());
    }

    public NhinTargetSystemType createNhinTargetSystemType(String targetHCID) {

        if (NullChecker.isNotNullish(targetHCID)) {
            HomeCommunityType hc = new HomeCommunityType();
            hc.setHomeCommunityId(targetHCID);
            return createNhinTargetSystemType(hc);
        } else {
            log.error("Target ID is null");
        }
        return null;
    }

    public NhinTargetSystemType createNhinTargetSystemType(HomeCommunityType hc) {
        NhinTargetSystemType result = new NhinTargetSystemType();
        result.setHomeCommunity(hc);

        return result;
    }

    public String getLocalCommunityId() {
        return readStringGatewayProperty(NhincConstants.HOME_COMMUNITY_ID_PROPERTY);
    }

    public String getUrl(String targetHCID, String targetSystem, GATEWAY_API_LEVEL apiLevel) {
        log.debug("begin getUrl targetHCID/targetSystem: " + targetHCID + " / " + targetSystem);

        NhinTargetSystemType ts = createNhinTargetSystemType(targetHCID);
        return getUrl(ts, targetSystem, apiLevel);
    }

    public String getUrl(NhinTargetSystemType target, String targetSystem, GATEWAY_API_LEVEL apiLevel) {
        log.debug("begin getUrl target/targetSystem: " + target + " / " + targetSystem);
        String url = null;

        if (target != null) {
            try {
                url = getWebServiceProxyHelper().getUrlFromTargetSystemByGatewayAPILevel(target, targetSystem, apiLevel);

            } catch (Exception ex) {
                log.error("Error: Failed to retrieve url for service: " + targetSystem);
                log.error(ex.getMessage());
            }
        } else {
            log.error("Target system passed into the proxy is null");
        }

        return url;
    }

    public String getAdapterUrl(String adapterServcice, NhincConstants.ADAPTER_API_LEVEL adapterApiLevel) {
        try {
            return ConnectionManagerCache.getInstance().getAdapterEndpontURL(adapterServcice, adapterApiLevel);
        } catch (ConnectionManagerException ex) {
            log.error("Error: Failed to retrieve url for service: " + NhincConstants.ADAPTER_ADMIN_DIST_SECURED_SERVICE_NAME);
            log.error(ex.getMessage());
        }

        return null;
    }
    
    public Service getService(String wsdl, String uri, String service) {
        try {
            WebServiceProxyHelper proxyHelper = new WebServiceProxyHelper();
            return proxyHelper.createService(wsdl, uri, service);
        } catch (Throwable t) {
            log.error("Error creating service: " + t.getMessage(), t);
        }
        return null;
    }

    public WebServiceProxyHelper getWebServiceProxyHelper() {
        return new WebServiceProxyHelper();
    }

    public boolean isInPassThroughMode() {
        return readBooleanGatewayProperty(NhincConstants.NHIN_ADMIN_DIST_SERVICE_PASSTHRU_PROPERTY);
    }

    public boolean isServiceEnabled() {
        return readBooleanGatewayProperty(NhincConstants.NHIN_ADMIN_DIST_SERVICE_ENABLED);
    }

    public boolean readBooleanGatewayProperty(String propertyName) {
        boolean result = false;
        try {
            result = PropertyAccessor.getPropertyBoolean(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
        } catch (PropertyAccessException ex) {
            log.error("Error: Failed to retrieve " + propertyName + " from property file: " + NhincConstants.GATEWAY_PROPERTY_FILE);
            log.error(ex.getMessage());
        }
        return result;
    }

    public String readStringGatewayProperty(String propertyName) {
        String result = "";
        try {
            result = PropertyAccessor.getProperty(NhincConstants.GATEWAY_PROPERTY_FILE, propertyName);
        } catch (Exception ex) {
            log.error("Unable to retrieve " + propertyName + " from Gateway.properties");
            log.error(ex);
        }
        log.debug("begin Gateway property: " + propertyName + " - " + result);
        return result;
    }

    public NhinTargetSystemType buildHomeCommunity(String homeCommunityId) {
        NhinTargetSystemType nhinTargetSystem = new NhinTargetSystemType();
        HomeCommunityType homeCommunity = new HomeCommunityType();
        homeCommunity.setHomeCommunityId(homeCommunityId);
        nhinTargetSystem.setHomeCommunity(homeCommunity);
        return nhinTargetSystem;
    }
}
