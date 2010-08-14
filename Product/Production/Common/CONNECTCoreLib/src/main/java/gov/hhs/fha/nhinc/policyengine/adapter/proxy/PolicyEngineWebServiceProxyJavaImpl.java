package gov.hhs.fha.nhinc.policyengine.adapter.proxy;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyRequestType;
import gov.hhs.fha.nhinc.common.nhinccommonadapter.CheckPolicyResponseType;
import gov.hhs.fha.nhinc.policyengine.adapterpolicyengine.AdapterPolicyEngineImpl;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Neil Webb
 */
public class PolicyEngineWebServiceProxyJavaImpl implements PolicyEngineProxy
{
    private Log log = null;

    public PolicyEngineWebServiceProxyJavaImpl()
    {
        log = createLogger();
    }

    protected Log createLogger()
    {
        return LogFactory.getLog(getClass());
    }

    public CheckPolicyResponseType checkPolicy(CheckPolicyRequestType checkPolicyRequest, AssertionType assertion)
    {
        log.debug("Begin PolicyEngineWebServiceProxyJavaImpl.checkPolicy");
        CheckPolicyResponseType response = null;
        AdapterPolicyEngineImpl policyEngine = new AdapterPolicyEngineImpl();
        try
        {
            response = policyEngine.checkPolicy(checkPolicyRequest, assertion);
        }
        catch (Exception e)
        {
            String sErrorMessage = "Error occurred calling PolicyEngineWebServiceProxyJavaImpl.checkPolicy.  Error: " +
                                   e.getMessage();
            log.error(sErrorMessage, e);
            throw new RuntimeException(sErrorMessage, e);
        }
        log.debug("End PolicyEngineWebServiceProxyJavaImpl.checkPolicy");
        return response;
    }

}
