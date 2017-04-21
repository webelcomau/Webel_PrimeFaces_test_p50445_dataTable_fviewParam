package com.webel.test.primefaces;

import java.io.Serializable;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import org.primefaces.context.RequestContext;

/**
 *
 * @author darrenkelly
 */
@Named
@SessionScoped
public class PrimefacesUtil implements Serializable {

    public PrimefacesUtil() {
    }
    
    public String getTestVersion() {
        return "TEST";
    }
    
    
    /** 
     * TIP from <a href="http://forum.primefaces.org/viewtopic.php?f=3&t=47078">Primefaces forum</a>.
     * 
     * @return The build version of the current Primefaces.
     */
    public String getPrimefacesVersion() {
        return RequestContext.getCurrentInstance().getApplicationContext().getConfig().getBuildVersion();
    }
    
}
