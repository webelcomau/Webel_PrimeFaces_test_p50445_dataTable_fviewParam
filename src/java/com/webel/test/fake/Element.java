package com.webel.test.fake;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * 
 * @author Originally: Darren Kelly (Webel IT Australia)
 */
public class Element extends FakeEntity {
    
    private static final Logger logger = Logger.getLogger(Element.class.getName());
    
    public Element() {
    }

    /**
     * Constructor that uses a recursive deep clone of a given fake entity.
     * 
     * The resulting fake entity will be marked as {@link #detached}.
     * 
     * @param deepCloneMe 
     */
    public Element(Element deepCloneMe) {
        super(deepCloneMe);
        
        for (Link l: deepCloneMe.getLinks()) {
            Link lClone = new Link(l);
            links.add(lClone);
        }
    }
    
    public Element clone() throws CloneNotSupportedException {
        return new Element(this);
    }
    
    
    private List<Link> links = new ArrayList<>();

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
    
    public boolean addLink(Link l) {        
        return links.add(l);
    }
    
    
}
