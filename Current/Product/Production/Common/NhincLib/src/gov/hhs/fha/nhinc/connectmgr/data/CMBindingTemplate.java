package gov.hhs.fha.nhinc.connectmgr.data;

/**
 * @author westbergl
 * @version 1.0
 * @created 20-Oct-2008 12:06:46 PM
 */
public class CMBindingTemplate
{
    private String bindingKey = "";
    private String endpointURL = "";
    private String wsdlURL = "";

    /**
     * Default constructor.
     */
    public CMBindingTemplate()
    {
        clear();
    }    

    /**
     * Clear the contents of this and set it to a default state.
     */
    public void clear()
    {
        endpointURL = "";
        wsdlURL = "";
        bindingKey = "";
    }
    
    /**
     * Returns true of the contents of the object are the same as the one
     * passed in.
     * 
     * @param oCompare The object to compare.
     * @return TRUE if the contents are the same as the one passed in.
     */
    public boolean equals(CMBindingTemplate oCompare)
    {
        if ((!this.bindingKey.equals(oCompare.bindingKey)) ||
            (!this.endpointURL.equals(oCompare.endpointURL)) ||
            (!this.wsdlURL.equals(oCompare.wsdlURL)))
        {
            return false;
        }
        
        // If we got here then everything is the same...
        //----------------------------------------------
        return true;
    }
    

    /**
     * Returns the binding key for this binding.
     * 
     * @return The binding key for this binding.
     */
    public String getBindingKey()
    {
        return bindingKey;
    }

    /**
     * Sets the binding key for this binding.
     * 
     * @param bindingKey The binding key for this binding.
     */
    public void setBindingKey(String bindingKey)
    {
        this.bindingKey = bindingKey;
    }

    /**
     * Returns the end point URL for this binding.
     * 
     * @return The end point URL for this binding.
     */
    public String getEndpointURL()
    {
        return endpointURL;
    }

    /**
     * Sets the end point URL for this binding.
     * 
     * @param endpointURL The end point URL for this binding.
     */
    public void setEndpointURL(String endpointURL)
    {
        this.endpointURL = endpointURL;
    }

    /**
     * Returns the URL for the WSDL for this binding.
     * 
     * @return The URL for the WSDL for this binding.
     */
    public String getWsdlURL()
    {
        return wsdlURL;
    }

    /**
     * Sets the URL for the WSDL for this binding.
     * 
     * @param wsdlURL The URL for the WSDL for this binding.
     */
    public void setWsdlURL(String wsdlURL)
    {
        this.wsdlURL = wsdlURL;
    }
    
    
}