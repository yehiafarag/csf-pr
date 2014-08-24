package probe.com.view;

import java.io.Serializable;
import com.vaadin.ui.VerticalLayout;
import probe.com.handlers.MainHandler;
import probe.com.view.visualizationunits.Body;
import probe.com.view.visualizationunits.Header;

/**
 * the main layout class
 */
public class MainWindow extends VerticalLayout implements Serializable {

    private static final long serialVersionUID = 1490961570483515444L;
    private final MainHandler expHandler;

    public MainWindow(MainHandler expHandler) {

        this.expHandler = expHandler;
        
        buildMainLayout();

    }

    private void buildMainLayout() {
        //header part
        Header header = new Header();
        this.addComponent(header);
        //body (tables)
        Body body = new Body(expHandler);
        this.addComponent(body);

    }
}
