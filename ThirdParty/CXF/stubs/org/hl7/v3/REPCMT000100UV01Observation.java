
package org.hl7.v3;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for REPC_MT000100UV01.Observation complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="REPC_MT000100UV01.Observation">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;group ref="{urn:hl7-org:v3}InfrastructureRootElements"/>
 *         &lt;element name="id" type="{urn:hl7-org:v3}II" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="code" type="{urn:hl7-org:v3}CD" minOccurs="0"/>
 *         &lt;element name="derivationExpr" type="{urn:hl7-org:v3}ST" minOccurs="0"/>
 *         &lt;element name="text" type="{urn:hl7-org:v3}ED_explicit" minOccurs="0"/>
 *         &lt;element name="statusCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="effectiveTime" type="{urn:hl7-org:v3}SXCM_TS_explicit" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="availabilityTime" type="{urn:hl7-org:v3}TS_explicit" minOccurs="0"/>
 *         &lt;element name="priorityCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="confidentialityCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="repeatNumber" type="{urn:hl7-org:v3}IVL_INT" minOccurs="0"/>
 *         &lt;element name="uncertaintyCode" type="{urn:hl7-org:v3}CE" minOccurs="0"/>
 *         &lt;element name="languageCode" type="{urn:hl7-org:v3}CS" minOccurs="0"/>
 *         &lt;element name="value" type="{urn:hl7-org:v3}ANY" minOccurs="0"/>
 *         &lt;element name="interpretationCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="methodCode" type="{urn:hl7-org:v3}CE" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="targetSiteCode" type="{urn:hl7-org:v3}CD" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="subject" type="{urn:hl7-org:v3}REPC_MT000100UV01.Subject4" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="recordTarget" type="{urn:hl7-org:v3}REPC_MT000100UV01.RecordTarget" minOccurs="0"/>
 *         &lt;element name="responsibleParty" type="{urn:hl7-org:v3}REPC_MT000100UV01.ResponsibleParty" minOccurs="0"/>
 *         &lt;element name="performer" type="{urn:hl7-org:v3}REPC_MT000100UV01.Performer3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="author" type="{urn:hl7-org:v3}REPC_MT000100UV01.Author3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="dataEnterer" type="{urn:hl7-org:v3}REPC_MT000100UV01.DataEnterer" minOccurs="0"/>
 *         &lt;element name="informant" type="{urn:hl7-org:v3}REPC_MT000100UV01.Informant12" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="verifier" type="{urn:hl7-org:v3}REPC_MT000100UV01.Verifier" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="location" type="{urn:hl7-org:v3}REPC_MT000100UV01.Location" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="definition" type="{urn:hl7-org:v3}REPC_MT000100UV01.Definition2" minOccurs="0"/>
 *         &lt;element name="conditions" type="{urn:hl7-org:v3}REPC_MT000100UV01.Conditions" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="referenceRange" type="{urn:hl7-org:v3}REPC_MT000100UV01.ReferenceRange2" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="sourceOf" type="{urn:hl7-org:v3}REPC_MT000100UV01.SourceOf3" maxOccurs="unbounded" minOccurs="0"/>
 *         &lt;element name="targetOf" type="{urn:hl7-org:v3}REPC_MT000100UV01.SourceOf" maxOccurs="unbounded" minOccurs="0"/>
 *       &lt;/sequence>
 *       &lt;attGroup ref="{urn:hl7-org:v3}InfrastructureRootAttributes"/>
 *       &lt;attribute name="nullFlavor" type="{urn:hl7-org:v3}NullFlavor" />
 *       &lt;attribute name="classCode" use="required" type="{urn:hl7-org:v3}ActClassObservation" />
 *       &lt;attribute name="moodCode" use="required" type="{urn:hl7-org:v3}x_ClinicalStatementObservationMood" />
 *       &lt;attribute name="negationInd" type="{urn:hl7-org:v3}bl" />
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "REPC_MT000100UV01.Observation", propOrder = {
    "realmCode",
    "typeId",
    "templateId",
    "id",
    "code",
    "derivationExpr",
    "text",
    "statusCode",
    "effectiveTime",
    "availabilityTime",
    "priorityCode",
    "confidentialityCode",
    "repeatNumber",
    "uncertaintyCode",
    "languageCode",
    "value",
    "interpretationCode",
    "methodCode",
    "targetSiteCode",
    "subject",
    "recordTarget",
    "responsibleParty",
    "performer",
    "author",
    "dataEnterer",
    "informant",
    "verifier",
    "location",
    "definition",
    "conditions",
    "referenceRange",
    "sourceOf",
    "targetOf"
})
public class REPCMT000100UV01Observation {

    protected List<CS> realmCode;
    protected II typeId;
    protected List<II> templateId;
    protected List<II> id;
    protected CD code;
    protected ST derivationExpr;
    protected EDExplicit text;
    protected CS statusCode;
    protected List<SXCMTSExplicit> effectiveTime;
    protected TSExplicit availabilityTime;
    protected CE priorityCode;
    protected List<CE> confidentialityCode;
    protected IVLINT repeatNumber;
    protected CE uncertaintyCode;
    protected CS languageCode;
    protected ANY value;
    protected List<CE> interpretationCode;
    protected List<CE> methodCode;
    protected List<CD> targetSiteCode;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Subject4> subject;
    @XmlElementRef(name = "recordTarget", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01RecordTarget> recordTarget;
    @XmlElementRef(name = "responsibleParty", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01ResponsibleParty> responsibleParty;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Performer3> performer;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Author3> author;
    @XmlElementRef(name = "dataEnterer", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01DataEnterer> dataEnterer;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Informant12> informant;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Verifier> verifier;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Location> location;
    @XmlElementRef(name = "definition", namespace = "urn:hl7-org:v3", type = JAXBElement.class, required = false)
    protected JAXBElement<REPCMT000100UV01Definition2> definition;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01Conditions> conditions;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01ReferenceRange2> referenceRange;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01SourceOf3> sourceOf;
    @XmlElement(nillable = true)
    protected List<REPCMT000100UV01SourceOf> targetOf;
    @XmlAttribute(name = "nullFlavor")
    protected List<String> nullFlavor;
    @XmlAttribute(name = "classCode", required = true)
    protected List<String> classCode;
    @XmlAttribute(name = "moodCode", required = true)
    protected XClinicalStatementObservationMood moodCode;
    @XmlAttribute(name = "negationInd")
    protected Boolean negationInd;

    /**
     * Gets the value of the realmCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the realmCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getRealmCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CS }
     * 
     * 
     */
    public List<CS> getRealmCode() {
        if (realmCode == null) {
            realmCode = new ArrayList<CS>();
        }
        return this.realmCode;
    }

    /**
     * Gets the value of the typeId property.
     * 
     * @return
     *     possible object is
     *     {@link II }
     *     
     */
    public II getTypeId() {
        return typeId;
    }

    /**
     * Sets the value of the typeId property.
     * 
     * @param value
     *     allowed object is
     *     {@link II }
     *     
     */
    public void setTypeId(II value) {
        this.typeId = value;
    }

    /**
     * Gets the value of the templateId property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the templateId property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTemplateId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getTemplateId() {
        if (templateId == null) {
            templateId = new ArrayList<II>();
        }
        return this.templateId;
    }

    /**
     * Gets the value of the id property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the id property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getId().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link II }
     * 
     * 
     */
    public List<II> getId() {
        if (id == null) {
            id = new ArrayList<II>();
        }
        return this.id;
    }

    /**
     * Gets the value of the code property.
     * 
     * @return
     *     possible object is
     *     {@link CD }
     *     
     */
    public CD getCode() {
        return code;
    }

    /**
     * Sets the value of the code property.
     * 
     * @param value
     *     allowed object is
     *     {@link CD }
     *     
     */
    public void setCode(CD value) {
        this.code = value;
    }

    /**
     * Gets the value of the derivationExpr property.
     * 
     * @return
     *     possible object is
     *     {@link ST }
     *     
     */
    public ST getDerivationExpr() {
        return derivationExpr;
    }

    /**
     * Sets the value of the derivationExpr property.
     * 
     * @param value
     *     allowed object is
     *     {@link ST }
     *     
     */
    public void setDerivationExpr(ST value) {
        this.derivationExpr = value;
    }

    /**
     * Gets the value of the text property.
     * 
     * @return
     *     possible object is
     *     {@link EDExplicit }
     *     
     */
    public EDExplicit getText() {
        return text;
    }

    /**
     * Sets the value of the text property.
     * 
     * @param value
     *     allowed object is
     *     {@link EDExplicit }
     *     
     */
    public void setText(EDExplicit value) {
        this.text = value;
    }

    /**
     * Gets the value of the statusCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getStatusCode() {
        return statusCode;
    }

    /**
     * Sets the value of the statusCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setStatusCode(CS value) {
        this.statusCode = value;
    }

    /**
     * Gets the value of the effectiveTime property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the effectiveTime property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getEffectiveTime().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link SXCMTSExplicit }
     * 
     * 
     */
    public List<SXCMTSExplicit> getEffectiveTime() {
        if (effectiveTime == null) {
            effectiveTime = new ArrayList<SXCMTSExplicit>();
        }
        return this.effectiveTime;
    }

    /**
     * Gets the value of the availabilityTime property.
     * 
     * @return
     *     possible object is
     *     {@link TSExplicit }
     *     
     */
    public TSExplicit getAvailabilityTime() {
        return availabilityTime;
    }

    /**
     * Sets the value of the availabilityTime property.
     * 
     * @param value
     *     allowed object is
     *     {@link TSExplicit }
     *     
     */
    public void setAvailabilityTime(TSExplicit value) {
        this.availabilityTime = value;
    }

    /**
     * Gets the value of the priorityCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getPriorityCode() {
        return priorityCode;
    }

    /**
     * Sets the value of the priorityCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setPriorityCode(CE value) {
        this.priorityCode = value;
    }

    /**
     * Gets the value of the confidentialityCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the confidentialityCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConfidentialityCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getConfidentialityCode() {
        if (confidentialityCode == null) {
            confidentialityCode = new ArrayList<CE>();
        }
        return this.confidentialityCode;
    }

    /**
     * Gets the value of the repeatNumber property.
     * 
     * @return
     *     possible object is
     *     {@link IVLINT }
     *     
     */
    public IVLINT getRepeatNumber() {
        return repeatNumber;
    }

    /**
     * Sets the value of the repeatNumber property.
     * 
     * @param value
     *     allowed object is
     *     {@link IVLINT }
     *     
     */
    public void setRepeatNumber(IVLINT value) {
        this.repeatNumber = value;
    }

    /**
     * Gets the value of the uncertaintyCode property.
     * 
     * @return
     *     possible object is
     *     {@link CE }
     *     
     */
    public CE getUncertaintyCode() {
        return uncertaintyCode;
    }

    /**
     * Sets the value of the uncertaintyCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CE }
     *     
     */
    public void setUncertaintyCode(CE value) {
        this.uncertaintyCode = value;
    }

    /**
     * Gets the value of the languageCode property.
     * 
     * @return
     *     possible object is
     *     {@link CS }
     *     
     */
    public CS getLanguageCode() {
        return languageCode;
    }

    /**
     * Sets the value of the languageCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link CS }
     *     
     */
    public void setLanguageCode(CS value) {
        this.languageCode = value;
    }

    /**
     * Gets the value of the value property.
     * 
     * @return
     *     possible object is
     *     {@link ANY }
     *     
     */
    public ANY getValue() {
        return value;
    }

    /**
     * Sets the value of the value property.
     * 
     * @param value
     *     allowed object is
     *     {@link ANY }
     *     
     */
    public void setValue(ANY value) {
        this.value = value;
    }

    /**
     * Gets the value of the interpretationCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the interpretationCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInterpretationCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getInterpretationCode() {
        if (interpretationCode == null) {
            interpretationCode = new ArrayList<CE>();
        }
        return this.interpretationCode;
    }

    /**
     * Gets the value of the methodCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the methodCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getMethodCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CE }
     * 
     * 
     */
    public List<CE> getMethodCode() {
        if (methodCode == null) {
            methodCode = new ArrayList<CE>();
        }
        return this.methodCode;
    }

    /**
     * Gets the value of the targetSiteCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the targetSiteCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTargetSiteCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link CD }
     * 
     * 
     */
    public List<CD> getTargetSiteCode() {
        if (targetSiteCode == null) {
            targetSiteCode = new ArrayList<CD>();
        }
        return this.targetSiteCode;
    }

    /**
     * Gets the value of the subject property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the subject property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSubject().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01Subject4 }
     * 
     * 
     */
    public List<REPCMT000100UV01Subject4> getSubject() {
        if (subject == null) {
            subject = new ArrayList<REPCMT000100UV01Subject4>();
        }
        return this.subject;
    }

    /**
     * Gets the value of the recordTarget property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01RecordTarget }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01RecordTarget> getRecordTarget() {
        return recordTarget;
    }

    /**
     * Sets the value of the recordTarget property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01RecordTarget }{@code >}
     *     
     */
    public void setRecordTarget(JAXBElement<REPCMT000100UV01RecordTarget> value) {
        this.recordTarget = value;
    }

    /**
     * Gets the value of the responsibleParty property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01ResponsibleParty }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01ResponsibleParty> getResponsibleParty() {
        return responsibleParty;
    }

    /**
     * Sets the value of the responsibleParty property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01ResponsibleParty }{@code >}
     *     
     */
    public void setResponsibleParty(JAXBElement<REPCMT000100UV01ResponsibleParty> value) {
        this.responsibleParty = value;
    }

    /**
     * Gets the value of the performer property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the performer property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getPerformer().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01Performer3 }
     * 
     * 
     */
    public List<REPCMT000100UV01Performer3> getPerformer() {
        if (performer == null) {
            performer = new ArrayList<REPCMT000100UV01Performer3>();
        }
        return this.performer;
    }

    /**
     * Gets the value of the author property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the author property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getAuthor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01Author3 }
     * 
     * 
     */
    public List<REPCMT000100UV01Author3> getAuthor() {
        if (author == null) {
            author = new ArrayList<REPCMT000100UV01Author3>();
        }
        return this.author;
    }

    /**
     * Gets the value of the dataEnterer property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01DataEnterer }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01DataEnterer> getDataEnterer() {
        return dataEnterer;
    }

    /**
     * Sets the value of the dataEnterer property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01DataEnterer }{@code >}
     *     
     */
    public void setDataEnterer(JAXBElement<REPCMT000100UV01DataEnterer> value) {
        this.dataEnterer = value;
    }

    /**
     * Gets the value of the informant property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the informant property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getInformant().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01Informant12 }
     * 
     * 
     */
    public List<REPCMT000100UV01Informant12> getInformant() {
        if (informant == null) {
            informant = new ArrayList<REPCMT000100UV01Informant12>();
        }
        return this.informant;
    }

    /**
     * Gets the value of the verifier property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the verifier property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getVerifier().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01Verifier }
     * 
     * 
     */
    public List<REPCMT000100UV01Verifier> getVerifier() {
        if (verifier == null) {
            verifier = new ArrayList<REPCMT000100UV01Verifier>();
        }
        return this.verifier;
    }

    /**
     * Gets the value of the location property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the location property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getLocation().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01Location }
     * 
     * 
     */
    public List<REPCMT000100UV01Location> getLocation() {
        if (location == null) {
            location = new ArrayList<REPCMT000100UV01Location>();
        }
        return this.location;
    }

    /**
     * Gets the value of the definition property.
     * 
     * @return
     *     possible object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Definition2 }{@code >}
     *     
     */
    public JAXBElement<REPCMT000100UV01Definition2> getDefinition() {
        return definition;
    }

    /**
     * Sets the value of the definition property.
     * 
     * @param value
     *     allowed object is
     *     {@link JAXBElement }{@code <}{@link REPCMT000100UV01Definition2 }{@code >}
     *     
     */
    public void setDefinition(JAXBElement<REPCMT000100UV01Definition2> value) {
        this.definition = value;
    }

    /**
     * Gets the value of the conditions property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the conditions property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getConditions().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01Conditions }
     * 
     * 
     */
    public List<REPCMT000100UV01Conditions> getConditions() {
        if (conditions == null) {
            conditions = new ArrayList<REPCMT000100UV01Conditions>();
        }
        return this.conditions;
    }

    /**
     * Gets the value of the referenceRange property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the referenceRange property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getReferenceRange().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01ReferenceRange2 }
     * 
     * 
     */
    public List<REPCMT000100UV01ReferenceRange2> getReferenceRange() {
        if (referenceRange == null) {
            referenceRange = new ArrayList<REPCMT000100UV01ReferenceRange2>();
        }
        return this.referenceRange;
    }

    /**
     * Gets the value of the sourceOf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the sourceOf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getSourceOf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01SourceOf3 }
     * 
     * 
     */
    public List<REPCMT000100UV01SourceOf3> getSourceOf() {
        if (sourceOf == null) {
            sourceOf = new ArrayList<REPCMT000100UV01SourceOf3>();
        }
        return this.sourceOf;
    }

    /**
     * Gets the value of the targetOf property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the targetOf property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getTargetOf().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link REPCMT000100UV01SourceOf }
     * 
     * 
     */
    public List<REPCMT000100UV01SourceOf> getTargetOf() {
        if (targetOf == null) {
            targetOf = new ArrayList<REPCMT000100UV01SourceOf>();
        }
        return this.targetOf;
    }

    /**
     * Gets the value of the nullFlavor property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the nullFlavor property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getNullFlavor().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getNullFlavor() {
        if (nullFlavor == null) {
            nullFlavor = new ArrayList<String>();
        }
        return this.nullFlavor;
    }

    /**
     * Gets the value of the classCode property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the classCode property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getClassCode().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link String }
     * 
     * 
     */
    public List<String> getClassCode() {
        if (classCode == null) {
            classCode = new ArrayList<String>();
        }
        return this.classCode;
    }

    /**
     * Gets the value of the moodCode property.
     * 
     * @return
     *     possible object is
     *     {@link XClinicalStatementObservationMood }
     *     
     */
    public XClinicalStatementObservationMood getMoodCode() {
        return moodCode;
    }

    /**
     * Sets the value of the moodCode property.
     * 
     * @param value
     *     allowed object is
     *     {@link XClinicalStatementObservationMood }
     *     
     */
    public void setMoodCode(XClinicalStatementObservationMood value) {
        this.moodCode = value;
    }

    /**
     * Gets the value of the negationInd property.
     * 
     * @return
     *     possible object is
     *     {@link Boolean }
     *     
     */
    public Boolean isNegationInd() {
        return negationInd;
    }

    /**
     * Sets the value of the negationInd property.
     * 
     * @param value
     *     allowed object is
     *     {@link Boolean }
     *     
     */
    public void setNegationInd(Boolean value) {
        this.negationInd = value;
    }

}
