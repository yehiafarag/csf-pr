
package probe.com.view;

import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import java.io.Serializable;
import probe.com.handlers.MainHandler;

/**
 *
 * @author Yehia Farag the main tables and charts layout
 */
public class Body extends VerticalLayout implements TabSheet.SelectedTabChangeListener, Button.ClickListener, Serializable {

    private final Button adminIcon;
    private TabSheet.Tab homeTab, adminTab;//tabs for Experiments Editor,Proteins, Search
    private final TabSheet mainTabSheet;//tab sheet for first menu (Experiments Editor,Proteins, Search)
    private VerticalLayout searchLayout, adminLayout;
    private WelcomeLayout welcomeLayout;
    private ProjectsLayout proteinsLayout;
    private final MainHandler handler;
/**
 * initialize body layout
 *@param handler main dataset handler
 * 
 *
 */
    public Body(MainHandler handler) {
        this.setWidth("100%");
        this.handler = handler;

        mainTabSheet = new TabSheet();
        this.addComponent(mainTabSheet);
        mainTabSheet.setHeight("100%");
        mainTabSheet.setWidth("100%");

        adminIcon = this.initAdminIcoBtn();
        initBodyLayout();
    }

    /**
     * initialize body components layout
     */
    private void initBodyLayout() {
//        home layout
        welcomeLayout = new WelcomeLayout(adminIcon);
        welcomeLayout.setWidth("100%");
//        Tab 2 content
        proteinsLayout = new ProjectsLayout(handler);
//      Tab 3 content
        searchLayout = new VerticalLayout();
        searchLayout.setMargin(true);
        SearchLayout searchLayout1 = new SearchLayout(handler);
        this.searchLayout.addComponent(searchLayout1);
//      Tab 1 login form
        adminLayout = new VerticalLayout();
        adminLayout.setMargin(true);
        adminLayout.setHeight("100%");
        adminLayout.addComponent(new AdminLayout(handler));

        homeTab = mainTabSheet.addTab(welcomeLayout, "Home", null);
        mainTabSheet.addTab(proteinsLayout, "Projects", null);
        mainTabSheet.addTab(this.searchLayout, "Search");
        adminTab = mainTabSheet.addTab(adminLayout, "Dataset Editor (Require Sign In)", null);
        mainTabSheet.addSelectedTabChangeListener(this);
        mainTabSheet.setSelectedTab(homeTab);
        mainTabSheet.markAsDirty();
        adminTab.setVisible(false);

    }

    @Override
    public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        String c = mainTabSheet.getTab(event.getTabSheet().getSelectedTab()).getCaption();
        if (c.equals("Dataset Editor (Require Sign In)")) {
            adminTab.setVisible(false);
        } else if (c.equals("Proteins")) {
            adminTab.setVisible(false);
        } else if (c.equals("Search")) {
            adminTab.setVisible(false);
        }
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        mainTabSheet.markAsDirty();
    }

    private Button initAdminIcoBtn() {
        Button b = new Button("(Admin Login)");
        b.setStyleName(Runo.BUTTON_LINK);
        b.setDescription("Dataset Editor (Require Sign In)");
        Button.ClickListener adminClickListener = new Button.ClickListener() {
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                adminTab.setVisible(true);
                mainTabSheet.setSelectedTab(adminTab);
                mainTabSheet.markAsDirty();
                adminTab.setCaption("");
            }
        };
        b.addClickListener(adminClickListener);
        return b;
    }
}
