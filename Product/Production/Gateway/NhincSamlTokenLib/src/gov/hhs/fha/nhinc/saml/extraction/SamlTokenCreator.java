/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.saml.extraction;

import gov.hhs.fha.nhinc.common.nhinccommon.AssertionType;
import gov.hhs.fha.nhinc.nhinclib.NhincConstants;
import gov.hhs.fha.nhinc.nhinclib.NullChecker;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author Jon Hoppesch
 */
public class SamlTokenCreator {

    private static Log log = LogFactory.getLog(SamlTokenCreator.class);

    /**
     * This method will populate a Map with information from the assertion that is used within
     * the SAML Token.  This Map can be used to set up the requestContext prior to sending
     * a message on the Nhin.
     *
     * @param assertion The assertion object that contains information required by the SAML Token.
     * @param url The URL to the destination service.
     * @param action The specified Action for this message.
     * @return A Map containing all of the information needed for creation of the SAML Token.
     */
    public Map CreateRequestContext(AssertionType assertion, String url, String action) {
        log.debug("Entering SamlTokenCreator.CreateRequestContext...");

        Map requestContext = new HashMap();

        // This will be overwritten if a value is available in
        // assertion.getSamlAuthzDecisionStatement().getAction()
        if (NullChecker.isNotNullish(action)) {
            requestContext.put(NhincConstants.ACTION_PROP, action);
        }
        // This will be overwritten if a value is available in
        // assertion.getSamlAuthzDecisionStatement().getResource()
        if (NullChecker.isNotNullish(url)) {
            requestContext.put(NhincConstants.RESOURCE_PROP, url);
        }

        if (assertion != null) {
            // This is maintained but will be overwritten if a value is available in
            // assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions.getNotOnOrAfter()
            if (NullChecker.isNotNullish(assertion.getExpirationDate())) {
                requestContext.put(NhincConstants.EXPIRE_PROP, assertion.getExpirationDate());
            }
            // This is maintained but will be overwritten if a value is available in
            // assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions.getNotBefore()
            if (NullChecker.isNotNullish(assertion.getDateOfSignature())) {
                requestContext.put(NhincConstants.SIGN_PROP, assertion.getDateOfSignature());
            }
            // This is maintained but will be overwritten if a value is available in
            // assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContentReference()
            if (NullChecker.isNotNullish(assertion.getClaimFormRef())) {
                requestContext.put(NhincConstants.CONTENT_REF_PROP, assertion.getClaimFormRef());
            }
            // This is maintainted but will be overwritten if a value is available in
            // assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContent()
            if (assertion.getClaimFormRaw() != null) {
                requestContext.put(NhincConstants.CONTENT_PROP, assertion.getClaimFormRaw());
            }
            if (assertion.getUserInfo() != null) {
                if (NullChecker.isNotNullish(assertion.getUserInfo().getUserName())) {
                    requestContext.put(NhincConstants.USER_NAME_PROP, assertion.getUserInfo().getUserName());
                }
                if (assertion.getUserInfo().getOrg() != null) {
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getOrg().getName())) {
                        requestContext.put(NhincConstants.USER_ORG_PROP, assertion.getUserInfo().getOrg().getName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion user org is null");
                }
                if (assertion.getUserInfo().getRoleCoded() != null) {
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getCode())) {
                        requestContext.put(NhincConstants.USER_CODE_PROP, assertion.getUserInfo().getRoleCoded().getCode());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getCodeSystem())) {
                        requestContext.put(NhincConstants.USER_SYST_PROP, assertion.getUserInfo().getRoleCoded().getCodeSystem());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getCodeSystemName())) {
                        requestContext.put(NhincConstants.USER_SYST_NAME_PROP, assertion.getUserInfo().getRoleCoded().getCodeSystemName());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getRoleCoded().getDisplayName())) {
                        requestContext.put(NhincConstants.USER_DISPLAY_PROP, assertion.getUserInfo().getRoleCoded().getDisplayName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion user info role is null");
                }
                if (assertion.getUserInfo().getPersonName() != null) {
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getPersonName().getGivenName())) {
                        requestContext.put(NhincConstants.USER_FIRST_PROP, assertion.getUserInfo().getPersonName().getGivenName());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getPersonName().getSecondNameOrInitials())) {
                        requestContext.put(NhincConstants.USER_MIDDLE_PROP, assertion.getUserInfo().getPersonName().getSecondNameOrInitials());
                    }
                    if (NullChecker.isNotNullish(assertion.getUserInfo().getPersonName().getFamilyName())) {
                        requestContext.put(NhincConstants.USER_LAST_PROP, assertion.getUserInfo().getPersonName().getFamilyName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion user person name is null");
                }
            } else {
                log.error("Error: samlSendOperation input assertion user info is null");
            }
            if (assertion.getPurposeOfDisclosureCoded() != null) {
                if (assertion.getPurposeOfDisclosureCoded() != null) {
                    if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCode())) {
                        requestContext.put(NhincConstants.PURPOSE_CODE_PROP, assertion.getPurposeOfDisclosureCoded().getCode());
                    }
                    if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCodeSystem())) {
                        requestContext.put(NhincConstants.PURPOSE_SYST_PROP, assertion.getPurposeOfDisclosureCoded().getCodeSystem());
                    }
                    if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getCodeSystemName())) {
                        requestContext.put(NhincConstants.PURPOSE_SYST_NAME_PROP, assertion.getPurposeOfDisclosureCoded().getCodeSystemName());
                    }
                    if (NullChecker.isNotNullish(assertion.getPurposeOfDisclosureCoded().getDisplayName())) {
                        requestContext.put(NhincConstants.PURPOSE_DISPLAY_PROP, assertion.getPurposeOfDisclosureCoded().getDisplayName());
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion purpose coded is null");
                }
            } else {
                log.error("Error: samlSendOperation input assertion purpose is null");
            }
            if (assertion.getSamlAuthnStatement() != null) {
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getAuthInstant())) {
                    requestContext.put(NhincConstants.AUTHN_INSTANT_PROP, assertion.getSamlAuthnStatement().getAuthInstant());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getSessionIndex())) {
                    requestContext.put(NhincConstants.AUTHN_SESSION_INDEX_PROP, assertion.getSamlAuthnStatement().getSessionIndex());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getAuthContextClassRef())) {
                    requestContext.put(NhincConstants.AUTHN_CONTEXT_CLASS_PROP, assertion.getSamlAuthnStatement().getAuthContextClassRef());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getSubjectLocalityAddress())) {
                    requestContext.put(NhincConstants.SUBJECT_LOCALITY_ADDR_PROP, assertion.getSamlAuthnStatement().getSubjectLocalityAddress());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthnStatement().getSubjectLocalityDNSName())) {
                    requestContext.put(NhincConstants.SUBJECT_LOCALITY_DNS_PROP, assertion.getSamlAuthnStatement().getSubjectLocalityDNSName());
                }
            } else {
                log.error("Error: samlSendOperation input assertion AuthnStatement is null");
            }
            if (assertion.getSamlAuthzDecisionStatement() != null) {
                if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getAction())) {
                    requestContext.put(NhincConstants.ACTION_PROP, assertion.getSamlAuthzDecisionStatement().getAction());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getResource())) {
                    requestContext.put(NhincConstants.RESOURCE_PROP, assertion.getSamlAuthzDecisionStatement().getResource());
                }
                if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getDecision())) {
                    requestContext.put(NhincConstants.AUTHZ_DECISION_PROP, assertion.getSamlAuthzDecisionStatement().getDecision());
                }
                if (assertion.getSamlAuthzDecisionStatement().getEvidence() != null && assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion() != null) {
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId())) {
                        requestContext.put(NhincConstants.EVIDENCE_ID_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getId());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant())) {
                        requestContext.put(NhincConstants.EVIDENCE_INSTANT_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssueInstant());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion())) {
                        requestContext.put(NhincConstants.EVIDENCE_VERSION_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getVersion());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer())) {
                        requestContext.put(NhincConstants.EVIDENCE_ISSUER_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuer());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuerFormat())) {
                        requestContext.put(NhincConstants.EVIDENCE_ISSUER_FORMAT_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getIssuerFormat());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContentReference())) {
                        requestContext.put(NhincConstants.EVIDENCE_CONTENT_REF_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContentReference());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContentType())) {
                        requestContext.put(NhincConstants.EVIDENCE_CONTENT_TYPE_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContentType());
                    }
                    if (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContent() != null) {
                        requestContext.put(NhincConstants.EVIDENCE_CONTENT_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getContent());
                    }
                    if (assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions() != null) {
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore())) {
                        requestContext.put(NhincConstants.EVIDENCE_CONDITION_NOT_BEFORE_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotBefore());
                    }
                    if (NullChecker.isNotNullish(assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter())) {
                        requestContext.put(NhincConstants.EVIDENCE_CONDITION_NOT_AFTER_PROP, assertion.getSamlAuthzDecisionStatement().getEvidence().getAssertion().getConditions().getNotOnOrAfter());
                    }
                    } else {
                        log.error("Error: samlSendOperation input assertion AuthzDecisionStatement Evidence Conditions is null");
                    }
                } else {
                    log.error("Error: samlSendOperation input assertion AuthzDecisionStatement Evidence is null");
                }
            } else {
                log.error("Error: samlSendOperation input assertion AuthzDecisionStatement is null");
            }
        } else {
            log.error("Error: samlSendOperation input assertion is null");
        }

        log.info("Request Context:");
        Set allKeys = requestContext.keySet();
        for (Object key : allKeys) {
            log.info(key + " = " + requestContext.get(key));
        }

        log.debug("Exiting SamlTokenCreator.CreateRequestContext...");
        return requestContext;
    }
}
