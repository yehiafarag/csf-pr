/*
 */
package probe.com.view.visualizationunits;

import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.DatasetBean;

/**
 *
 * @author Yehia Farag
 * the main tables layout 
 */
public class Body extends VerticalLayout implements TabSheet.SelectedTabChangeListener, Button.ClickListener, Serializable {

//    private TreeMap<Integer, String> datasetNamesList;//for dropdown select list
    private final Button adminIcon;
    private TabSheet.Tab homeTab, adminTab;//tabs for Experiments Editor,Proteins, Search
    private final TabSheet mainTabSheet;//tab sheet for first menu (Experiments Editor,Proteins, Search)
    private VerticalLayout  searchLayout, adminLayout;
    private WelcomeLayout welcomeLayout;
    private ProteinsLayout proteinsLayout;
    private final MainHandler handler;
//    private Map<Integer, DatasetBean> datasetList;    
//    private final Map<Integer,Integer> datasetIndex ;

    public Body(MainHandler handler) {
        this.setWidth("100%");
        this.handler = handler;
//        datasetList = handler.getDatasetsList(datasetList);
        
        
        mainTabSheet = new TabSheet();
        this.addComponent(mainTabSheet);
        mainTabSheet.setHeight("100%");
        mainTabSheet.setWidth("100%");
        
        
        adminIcon = this.initAdminIcoBtn();        
        //just to arrange the already stored datasets -->> to be removed in the future
//        datasetIndex.put(8,1);
//        datasetIndex.put(14,2);
//        datasetIndex.put(4,3);
//        datasetIndex.put(17,4);
//        datasetIndex.put(15, 5);
//        datasetIndex.put(16, 6);
//        datasetIndex.put(9,7);        
        initBodyLayout();
    }

    private void initBodyLayout() {        
//        datasetNamesList = new TreeMap<Integer, String>();
//        if (datasetList == null) {
//            datasetList = new HashMap<Integer, DatasetBean>();
//        }
//        
//               for (int datasetkey : datasetList.keySet()) {
//            //for re-indexing the stored datasets, to be removed in the future
//            if (datasetIndex.containsKey(datasetkey)) {
//                DatasetBean dataset = datasetList.get(datasetkey);
//                datasetNamesList.put(datasetIndex.get(datasetkey), "\t" + dataset.getName());
//
//            } else {
//                DatasetBean dataset = datasetList.get(datasetkey);
//                datasetNamesList.put(datasetkey, "\t" + dataset.getName());
//                datasetIndex.put(datasetkey, datasetkey);
//            }
//        }
//        home layout
        welcomeLayout = new WelcomeLayout(adminIcon);
        welcomeLayout.setWidth("100%");
//        Tab 2 content
        proteinsLayout = new ProteinsLayout(handler);      
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
        mainTabSheet.addTab(proteinsLayout, "Proteins", null);
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
