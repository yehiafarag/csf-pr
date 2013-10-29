/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Button;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;
import java.io.Serializable;
import java.util.Map;
import java.util.TreeMap;
import probe.com.control.ExperimentHandler;
import probe.com.model.beans.ExperimentBean;

/**
 *
 * @author Yehia Farag
 */
public class Body extends VerticalLayout implements TabSheet.SelectedTabChangeListener, Button.ClickListener, Serializable {

    private TreeMap<Integer, String> expListStr;
    private Button adminIcon;
    private TabSheet.Tab t1, t2, t3, t4;//tabs for Experiments Editor,Proteins, Search
    private TabSheet t;//tab sheet for first menu (Experiments Editor,Proteins, Search)
    private VerticalLayout l2, l3, l4;
    private HomeLayout homeLayout;
    private ProteinsLayout proteinsLayout;
    private ExperimentHandler expHandler;
    private Map<Integer, ExperimentBean> expList = null;

    public Body(ExperimentHandler expHandler) {
        this.expHandler = expHandler;
        expList = expHandler.getExperiments(expList);
        t = new TabSheet();
        this.setWidth("100%");
        adminIcon = this.getAdminIco();
        initBody();
    }

    private void initBody() {

        expListStr = new TreeMap<Integer, String>();
        for (int key2 : expList.keySet()) {
            ExperimentBean expB = expList.get(key2);
            expListStr.put(key2, "\t" + expB.getName());
        }
//        home layout
        homeLayout = new HomeLayout(adminIcon);
        homeLayout.setWidth("100%");
//        Tab 2 content
        proteinsLayout = new ProteinsLayout(expHandler, expListStr, expList);
//        Tab 2 content
        l2 = new VerticalLayout();
        l2.setMargin(true);
        l2.setHeight("100%");
//      Tab 3 content
        l3 = new VerticalLayout();
        l3.setMargin(true);
        SearchLayout searchLayout = new SearchLayout(expHandler, expListStr, expList);
        l3.addComponent(searchLayout);
//      Tab 1 login form
        l4 = new VerticalLayout();
        l4.setMargin(true);
        l4.setHeight("100%");
        l4.addComponent(new AdminLayout(expHandler));
        this.addComponent(t);
        t.setHeight("100%");
        t.setWidth("100%");
        t1 = t.addTab(homeLayout, "Home", null);
        t2 = t.addTab(proteinsLayout, "Proteins", null);
        t2.setDescription("Select an experiment to view all proteins identified in the given experiment. Select a protein to see all peptides identified for the protein and, if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed. ");       
//        ThemeResource icon = new ThemeResource("img/search25x25.png");
        ExternalResource icon = new ExternalResource("http://www.akpress.org/skin/frontend/ak_amphib/default//images/search_icon.png");
        t3 = t.addTab(l3, "Search", icon);        
        t3.setDescription("Search Proteins");
        t4 = t.addTab(l4, "Dataset Editor (Require Sign In)", null);
        t.addListener(this);
        t.setSelectedTab(t1);
        t.requestRepaint();
        t4.setVisible(false);

    }

    @Override
    public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        String c = t.getTab(event.getTabSheet().getSelectedTab()).getCaption();
        if (c.equals("Dataset Editor (Require Sign In)")) {
//	        	isLoginView = true;
        } else if (c.equals("Proteins")) {
//	        	  t4.setVisible(false);
//	        	 layout3.removeAllComponents();
//	        	 if(isLoginView)
//	        	 {
//	        		 
////	        		 pv.resetExpTable();
////	        		 pv.buildMainLayout();
////		             layout3.addComponent(pv);
//		             l2.addComponent(layout3);
////		        	 expList = pv.resetExpList();
//	        		 isLoginView = false;       		 
//	        		 
//	        	 }
//	        	 else
//	        	 {
//		        	 layout3.removeAllComponents();
////		        	 pv.updateExpTable();
////		        	 pv.buildMainLayout();
////		             layout3.addComponent(pv);	            
//		             l2.addComponent(layout3);
////		        	 expList = pv.getExpList();
//	        	 }
        } else if (c.equals("Search")) {
            t4.setVisible(false);
//	        	if (pv!=null)
//	        		expList = pv.getUpdatedExpList();
//            l3.removeAllComponents();
//	        	layout3.removeAllComponents();
//	        	pv.updateExpTable();
//	        	expList = pv.getExpList();
//	        	SearchUnit searchUnit = new SearchUnit(expList,url,dbName,driver,userName,  password,this.adminIcon);
//	        	layout3.addComponent(searchUnit);	        	
//	            l3.addComponent(layout3);
        }
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        t.requestRepaint();
    }

    private Button getAdminIco() {
        Button b = new Button("(Admin Login)");
//        b.setH)eight("50px");
        b.setStyleName(Runo.BUTTON_LINK);        
        b.setDescription("Dataset Editor (Require Sign In)");
      //  b.setIcon(new ExternalResource("http://sphotos-c.ak.fbcdn.net/hphotos-ak-ash3/600305_124728624377405_1884648661_n.jpg"));
        b.addListener(new Button.ClickListener() {            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void buttonClick(Button.ClickEvent event) {
                t4.setVisible(true);
                t.setSelectedTab(t4);
                t.requestRepaint();
                t4.setCaption("");
            }
        });
        return b;
    }
}
