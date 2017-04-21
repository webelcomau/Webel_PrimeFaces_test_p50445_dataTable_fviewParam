package com.webel.test.fake;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.persistence.*;

/** 
 * A persistent hyperlink with name (as title) and URL.
 *
 * @author darrenkelly
 */
//@Entity
public class Link extends FakeEntity {

    private static final long serialVersionUID = 1L;

    private static final Logger logger = Logger.getLogger(Link.class.getName());
    
    //@Transient 
    public URL getUrl() {
        try {
            return new URL(urlAsString);
        } catch (MalformedURLException ex) {
            logger.log(Level.SEVERE, ex.getMessage());
            return null;
        }        
    }
    
    private String urlAsString;

    public String getUrlAsString() {
        return urlAsString;
    }

    public void setUrlAsString(String urlAsString) {
        this.urlAsString = urlAsString;
    }

    public Link() {
    }

    public Link(String name, String urlAsString)
    {
        super(name);
        this.urlAsString = urlAsString;
    }

    public Link(String urlAsString)
    {
        this(null, urlAsString);
    }

    /**
     * 
     * @param cloneMe 
     */
    public Link(Link cloneMe)
    {
        super(cloneMe);
        setUrlAsString(cloneMe.getUrlAsString());
    }
    
    protected Link clone() throws CloneNotSupportedException {
        return new Link(this);
    }
    
    
    @Override
    public String toString() {
        return Link.class.getSimpleName() + "[" + getId() + "] name(" + getName() + "), URL(" + getUrlAsString() + ")";
    }
}
