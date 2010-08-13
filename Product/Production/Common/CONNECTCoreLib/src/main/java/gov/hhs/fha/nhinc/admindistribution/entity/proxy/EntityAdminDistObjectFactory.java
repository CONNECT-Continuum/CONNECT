/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gov.hhs.fha.nhinc.admindistribution.entity.proxy;
import gov.hhs.fha.nhinc.proxy.ComponentProxyObjectFactory;

/**
 *
 * @author dunnek
 */
public class EntityAdminDistObjectFactory extends ComponentProxyObjectFactory{
    private static final String CONFIG_FILE_NAME = "EntityAdminDistProxyConfig.xml";
    private static final String BEAN_NAME_AUDIT_REPOSITORY = "entityadmindist";

    protected String getConfigFileName()
    {
        return CONFIG_FILE_NAME;
    }
    /**
     * Retrieve an adapter audit query implementation using the IOC framework.
     * This method retrieves the object from the framework that has an
     * identifier of "adapterauditquery."
     *
     * @return AdapterAuditQueryProxy instance
     */
    public EntityAdminDistProxy getAdapterAuditQueryProxy() {
        return getBean(BEAN_NAME_AUDIT_REPOSITORY, EntityAdminDistProxy.class);
    }

}
