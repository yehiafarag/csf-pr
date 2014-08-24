package probe.com;

import com.vaadin.annotations.Theme;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import javax.servlet.ServletContext;
import probe.com.handlers.MainHandler;
import probe.com.view.MainWindow;

/**
 * The Application's "start point" class
 */
@SuppressWarnings("serial")
@Theme("dario-theme")
public class AppController extends UI {

    private String dbURL, dbName, dbDriver, dbUserName, dbPassword;
    private MainHandler expHandler;

    /*
     *  initialize context parameters 
     */
    @Override
    protected void init(VaadinRequest request) {
        //init param for DB
        ServletContext scx = VaadinServlet.getCurrent().getServletContext();
        dbURL = (scx.getInitParameter("url"));
        dbName = (scx.getInitParameter("dbName"));
        dbDriver = (scx.getInitParameter("driver"));
        dbUserName = (scx.getInitParameter("userName"));
        dbPassword = (scx.getInitParameter("password"));
        //init experment handler
        expHandler = new MainHandler(dbURL, dbName, dbDriver, dbUserName, dbPassword);
        //init main layout
        MainWindow mw = new MainWindow(expHandler);
        this.getPage().setTitle("CSF Proteome Resource (CSF-PR)");
        setContent(mw);
    }


}
