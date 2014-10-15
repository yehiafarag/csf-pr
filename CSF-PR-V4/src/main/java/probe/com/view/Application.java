package probe.com.view;

import java.io.Serializable;
import com.vaadin.ui.VerticalLayout;
import probe.com.handlers.MainHandler;

/**
 * the main layout
 */
public class Application extends VerticalLayout implements Serializable {

    private static final long serialVersionUID = 1490961570483515444L;
    private final MainHandler handler;

    public Application(MainHandler handler) {
        this.handler = handler;
        buildMainLayout();

    }

    /**
     * initialize the header and main body layout
     *
     *
     */
    private void buildMainLayout() {
        //header part
        Header header = new Header();
        this.addComponent(header);
        //body (tables)
        Body body = new Body(handler);
        this.addComponent(body);

    }
}
