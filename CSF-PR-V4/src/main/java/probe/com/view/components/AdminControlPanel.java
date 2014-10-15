/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import probe.com.view.core.ExperimentUploadPanel;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.User;

/**
 *
 * @author Yehia Farag
 */
public class AdminControlPanel extends VerticalLayout implements Serializable {

    private final VerticalLayout bodyLayout = new VerticalLayout();
    private final User user;
    private final MainHandler handler;
    private final VerticalLayout layout1 = new VerticalLayout();
    private final TabSheet t;
    private TabSheet.Tab t1;
    private VerticalLayout l1, l2, l3, l4;

    /**
     *
     * @param user authenticated user
     * @param handler main application handler
     */
    public AdminControlPanel(User user, MainHandler handler) {
        this.handler = handler;
        this.user = user;

        this.setWidth("100%");
        this.setSpacing(true);

        this.t = new TabSheet();
        t.setWidth("100%");

        Label welcomeAdminLable = new Label("<h3 align='center' ; style='font-family:verdana;color:gray;;font-weight:bold;text-decoration:none '>Welcome " + user.getUsername() + "</h3>");
        welcomeAdminLable.setContentMode(ContentMode.HTML);
        welcomeAdminLable.setStyleName(Reindeer.LABEL_SMALL);
        welcomeAdminLable.setWidth("100%");

        this.addComponent(welcomeAdminLable);
        this.setComponentAlignment(welcomeAdminLable, Alignment.MIDDLE_LEFT);
        this.addComponent(bodyLayout);
        bodyLayout.setWidth("100%");
        this.buildMainLayout();

    }

    /**
     *
     * initialize main layout for admin control panel
     */
    private void buildMainLayout() {
        // Tab 1 content
        l1 = new VerticalLayout();
        l1.setHeight("100%");
        l1.setWidth("100%");
        l1.addComponent(layout1);
        this.initFirsTabLayout();

//      Tab 2 content
        l2 = new VerticalLayout();
        l2.setMargin(true);
        l2.setHeight("100%");

//        Tab 3 content
        l3 = new VerticalLayout();
        l3.setMargin(true);
        l3.setHeight("100%");

//      Tab 4 edit exp details
        l4 = new VerticalLayout();
        l4.setMargin(true);
        l4.setHeight("100%");
        l4.addComponent(new UpdateExperDetailsLayout(handler, user));

        t.setStyleName(Reindeer.TABSHEET_MINIMAL);
        t.setHeight("100%");
        t1 = t.addTab(l1, "Experiment Handler", null);
        t.addTab(l4, "Update Experiment Details");
        t.addTab(l2, "Change Password", null);
        if (user.isAdmin()) //add user form
        {
            t.addTab(l3, "Admin", null);
        }

        bodyLayout.addComponent(t);//
        t.setSelectedTab(t1);

    }

    private void initFirsTabLayout() {
        layout1.removeAllComponents();
        layout1.setWidth("100%");
        ExperimentUploadPanel uploader = new ExperimentUploadPanel(handler, user);
        layout1.addComponent(uploader);
    }
}
