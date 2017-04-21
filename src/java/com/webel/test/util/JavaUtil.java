package com.webel.test.util;

import java.io.Serializable;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

/**
 *
 * @author darrenkelly
 */
@Named
@ApplicationScoped
public class JavaUtil implements Serializable {

    public JavaUtil() {
    }

    public String getTestVersion() {
        return "TEST";
    }

    public String getJavaVersion() {
        return System.getProperty("java.version");
    }
}
