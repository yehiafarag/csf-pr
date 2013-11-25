package probe.com;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
import com.vaadin.annotations.Theme;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import javax.servlet.ServletContext;
import probe.com.view.MainWindow;

/**
 * * The Application's "main" class
 */
@SuppressWarnings("serial")
@Theme("dario-theme")
public class CSFPR extends UI {

    private String url, dbName, driver, userName, password;

    public String getUrl() {
        return url;

    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    protected void init(VaadinRequest request) {

        //init param for DB
        ServletContext scx = VaadinServlet.getCurrent().getServletContext();
        url = (scx.getInitParameter("url"));
        dbName = (scx.getInitParameter("dbName"));
        driver = (scx.getInitParameter("driver"));
        userName = (scx.getInitParameter("userName"));
        password = (scx.getInitParameter("password"));


        initLayout();
    }

    private void initLayout() {


        //ExternalResource ico_1 = new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-ash3/574636_108340259349575_2027925130_n.jpg");
        ThemeResource ico_1= new ThemeResource("img/probe.jpg");
        Link image1 = new Link(null, new ExternalResource("http://www.uib.no/rg/probe"));
        image1.setIcon(ico_1);
        image1.setTargetName("_blank");

        Link image2 = new Link(null, new ExternalResource("http://www.uib.no/"));
//        image2.setIcon(new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-prn1/533227_118477988335802_947238298_n.jpg"));
        image2.setIcon(new ThemeResource("img/uib.jpg"));
        image2.setTargetName("_blank");
        image2.setWidth("105px");

        Link image3 = new Link(null, new ExternalResource("http://www.stiftkgj.no/"));
        image3.setIcon(new ThemeResource("img/kgj.jpg"));
        image3.setTargetName("_blank");

        MainWindow mw = new MainWindow(url, dbName, driver, userName, password, image1, image2, image3);
        this.getPage().setTitle("CSF Proteome Resource (CSF-PR)");
        setContent(mw);
        
    }
}
