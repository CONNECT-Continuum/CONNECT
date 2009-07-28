/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gov.hhs.fha.nhinc.mpilib;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author rayj
 */
public class Patient implements java.io.Serializable {
    private static Log log = LogFactory.getLog(Patient.class);
    static final long serialVersionUID = 449060013287108219L;
    private String dateOfBirth = null;
    private String gender = "";
    //private QualifiedSubjectId RequesterSubjectId = null;
    private String ssn = "";
    private String lastName = "";
    private String firstName = "";
    private PersonName name = null;
    private Identifiers patientIdentifiers = new Identifiers();
    private Address add = null;
    private boolean optedIn = true;
    
    public Patient() {
        log.info("Patient initialized");
    }
    
    public boolean isOptedIn() {
        return optedIn;
    }
   
    public void setOptedIn(boolean optedIn) {
        this.optedIn = optedIn;
    }
    
    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String newVal) {
        this.dateOfBirth = newVal;
    }

    public Identifiers getIdentifiers() {
        return patientIdentifiers;
    }

    public void setIdentifiers(Identifiers newVal) {
        this.patientIdentifiers = newVal;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String newVal) {
        this.gender = newVal;
    }

    public String getSSN() {
        return ssn;
    }

    public void setSSN(String val) {
        this.ssn = val;
    }


    public Address getAddress()
    {
        if (add == null)
        {
            add = new Address();
        }
        
        return add;
    }
    public void setAddress(Address value)
    {
        add = value;
    }
    public PersonName getName() {
        if (name == null) {
            name = new PersonName();
        }
        return name;
    }

    public void setName(PersonName newVal) {
        this.name = newVal;
    }
    
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String newVal) {
        this.firstName = newVal;
    }
    
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String newVal) {
        this.lastName = newVal;
    }
}
