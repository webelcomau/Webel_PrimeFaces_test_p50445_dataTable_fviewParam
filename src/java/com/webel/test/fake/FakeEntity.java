package com.webel.test.fake;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * 
 * @author Originally: Darren Kelly (Webel IT Australia)
 */
public class FakeEntity implements Serializable, Cloneable {
    
    private static final Logger logger = Logger.getLogger(FakeEntity.class.getName());    
    
    private Long id;

    final public Long getId() {
        return id;
    }

    final public void setId(Long id) {
        this.id = id;
    }
    private String name;
    
    final public String getName() {
        return name;
    }

    final public void setName(String name) {
        this.name = name;
    }

    final static private boolean DEBUG_DETACHED = false;
    
    private boolean detached = true;
    
    /**
     * Helps track whether this mimics a "detached" JPA entity.
     * 
     * @return 
     */
    public boolean isDetached() {
        return detached;
    }

    final public void setDetached(boolean detached) {
        this.detached = detached;
        if (DEBUG_DETACHED) 
            logger.log(Level.INFO, "DEBUG: Set detached({0}) on fake entity: {1}", new Object[]{detached, this});
    }
        
    
    public FakeEntity() {
    }

    public FakeEntity(String name) {
        this.name = name;
    }

    @Override
    public FakeEntity clone() throws CloneNotSupportedException {
        return new FakeEntity(this);
    }

    /**
     * Constructor that uses a recursive deep clone of a given fake entity.
     * 
     * The resulting fake entity will be marked as {@link #detached}.
     * 
     * @param deepCloneMe 
     */
    public FakeEntity(FakeEntity deepCloneMe) {
        setId(deepCloneMe.getId());
        setName(deepCloneMe.getName());
        setDetached(true);        
    }
    
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " ["+getId()+"] '"+getName()+"' "+ "{ detached="+isDetached()+"}";                
    }
        
}
