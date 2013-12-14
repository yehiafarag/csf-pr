package probe.com.view;

import java.io.Serializable;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import probe.com.control.ExperimentHandler;
import probe.com.view.subview.Body;
import probe.com.view.subview.Header;

public class MainWindow extends VerticalLayout implements Serializable {

    private static final long serialVersionUID = 1490961570483515444L;
    private Link image1;//logo_1
    private Link image2;//logo_2
    private Link image3;//logo_3
    private ExperimentHandler expHandler;

    public MainWindow(String url, String dbName, String driver, String userName, String password, Link image12, Link image22, Link image32) {

        this.image1 = image12;
        this.image2 = image22;
        this.image3 = image32;
        this.expHandler = new ExperimentHandler(url, dbName, driver, userName, password);
        buildMainLayout();

    }

    @SuppressWarnings("deprecation")
    private void buildMainLayout() {
        //header
        Header header = new Header(image1, image2, image3);
        this.addComponent(header);
        //body
        Body body = new Body(expHandler);
        this.addComponent(body);
        


    }
}
