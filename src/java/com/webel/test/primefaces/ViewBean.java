package com.webel.test.primefaces;

import com.webel.test.fake.Element;
import com.webel.test.fake.FakeEntity;
import com.webel.test.fake.FakeQuery;
import com.webel.test.fake.Link;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import org.apache.commons.validator.routines.UrlValidator;
import org.primefaces.event.RowEditEvent;

/**
 * A CDI-compliant ViewScoped JSF backing bean for interacting with {@link FakeEntity} test items.
 *
 * Includes also some convenient formatted logging methods.
 *
 * 2017-04-21 This version is adapted to demonstrate a suspected issue
 * with p:dataTable invoking f:viewParam on the 1st row edit.
 *
 * https://forum.primefaces.org/viewtopic.php?f=3&t=50445&p=154868#p154868
 *
 * @author Darren Kelly (Webel IT Australia).
 */
@Named
@ViewScoped
public class ViewBean implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(ViewBean.class.getName());

    private static void log_echo(String method, String name, Object value) {
        LOGGER.log(Level.INFO, "[{0}] {1}({2})", new Object[]{method, name, value});
    }

    private static void log_echo(String name, Object value) {
        LOGGER.log(Level.INFO, "{0}({1})", new Object[]{name, value});
    }

    private static void log_info(String s) {
        LOGGER.info(s);
    }

    private static void log_warn(String s) {
        LOGGER.warning(s);
    }

    private static void log_error(Exception e) {
        LOGGER.log(Level.SEVERE, e.getMessage());
    }

    private static void log_error(String $error) {
        LOGGER.log(Level.SEVERE, $error);
    }

    public ViewBean() {
        log_info(this.getClass() + ": Created");
    }

    private FakeQuery query = new FakeQuery();

    /**
     * Simulates a database merge of the currently {@link #selected} element.
     *
     * Issues faces message diagnostics.
     *
     * If successful, redirects to the standard view page of the element.
     *
     * @return Redirection outcome for an entity view page by id, or null if fails.
     */
    public String update() {
        String $i = "update";
        if (selected == null) {
            String msg = $i + ": Can't update null selection";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            log_warn(msg);
            return null;
        }
        try {
            checkLinks(selected);
            query.merge(selected);
            String msg = $i + ": merged: entity: " + selected;
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            log_info(msg);
            //return "/examples/view?faces-redirect=true&id=" + selected.getId();
            return "/index?faces-redirect=true";
        } catch (InvalidUrlException ex) {
            String msg = ex.getMessage();
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            log_error(ex);
            return null;
        }

    }

    /**
     * Basic row edit handler for a p:dataTable of {@link FakeEntity} items.
     *
     * @param event
     */
    public void updateRow(RowEditEvent event) {
        String $i = "updateRow";
        if (event.getObject() == null) {
            String msg = $i + ": SKIPPING: no entity object found from row edit event";
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_WARN, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            log_warn(msg);
        } else {
            Element e = (Element) event.getObject();
            log_echo($i, "element", e);
            try {
                checkLinks(e);
                query.merge(e);
                String msg = $i + ": merged: entity: " + e;
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                log_info(msg);
            } catch (InvalidUrlException ex) {
                String msg = ex.getMessage();
                FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
                FacesContext.getCurrentInstance().addMessage(null, facesMsg);
                log_error(ex);
            }
        }
    }

    private Long id;

    /**
     * Target this from a view or edit page using f:viewParam.
     *
     * @param id
     */
    public void setId(Long id) {
        String $i = "setId";
        log_echo($i, "id", id);
        if (id == null) {
            throw new IllegalArgumentException($i + ": null id !");
        }
        FakeEntity found = query.find(id);
        if (found != null && found instanceof Element) {
            log_echo($i, "selected Element entity", found);
            selected = (Element) found;
            this.id = id;
        } else {
            log_warn("Could not find element entity with id:" + id);
        }
    }

    public Long getId() {
        return id;
    }

    private Element selected;

    /**
     * The entity "selected" by id using an f:viewParam and {@link #setId(java.lang.Long)}.
     *
     * @return May be null if there is no valid selection yet.
     */
    public Element getSelected() {
        return selected;
    }

    public void onLinkRowEdit(RowEditEvent event) {
        String $i = "onLinkRowEdit";
        Link link = (Link) event.getObject();
        log_echo($i, "link", link);
        try {
            checkLink(link);
            String msg = $i + "Link: " + link;
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
        } catch (InvalidUrlException ex) {
            String msg = ex.getMessage();
            FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
            FacesContext.getCurrentInstance().addMessage(null, facesMsg);
            log_error(ex);
        }
        //!IMPORTANT: the changes to the Link are not saved to database until
        //the parent Element is saved via update().
        //!IMPORTANT: the changes to the Link are not saved to database until
        //the parent Element is saved via update().
    }

    /**
     * View-scoped bean instance retained by returning void (or null).
     *
     * Reference: <a href="http://stackoverflow.com/questions/8744162/difference-between-returning-null-and-from-a-jsf-action">
     * Difference between returning null and “” from a JSF action</a>
     */
    public void refresh() {
        log_info("refresh");
    }

    protected void checkLink(Link l) throws InvalidUrlException {
        String $i = "checkLink";
        log_echo($i, "link", l);
        if (l == null) {
            throw new IllegalArgumentException("null Link !");
        }
        if (l.getUrlAsString() == null) {
            String msg = "Invalid URL for Link: must not be null (empty) !";
            throw new InvalidUrlException(msg);
        }
        UrlValidator validator = new UrlValidator();
        if (!validator.isValid(l.getUrlAsString())) {
            String msg = "Invalid URL (" + l.getUrlAsString() + ") for Link with name(" + l.getName() + ")";
            throw new InvalidUrlException(msg);
        }
    }

    protected void checkLinks(Element e) throws InvalidUrlException {
        String $i = "checkLink";
        log_echo($i, "element", e);
        for (Link l : e.getLinks()) {
            checkLink(l);
        }
    }

}
