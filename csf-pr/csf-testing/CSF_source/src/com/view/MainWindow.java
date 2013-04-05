package com.view;

import java.io.IOException;
import java.io.Serializable;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import com.handlers.ExperimentHandler;
import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.User;
import com.vaadin.event.MouseEvents.ClickListener;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;
import com.view.subviewunits.SearchUnit;

public class MainWindow extends VerticalLayout implements   TabSheet.SelectedTabChangeListener, Button.ClickListener,Serializable{

	private static final long serialVersionUID = 1490961570483515444L;

	private HorizontalLayout layout1 = new HorizontalLayout();
	private VerticalLayout layout2 = new VerticalLayout();
	private VerticalLayout layout3 = new VerticalLayout();
	private LoginView layout4 ;
	
	private TabSheet t;
	@SuppressWarnings("unused")
	private Tab t1, t2, t3,t4;
	private VerticalLayout  l1;
	private VerticalLayout l2;
	private VerticalLayout l3,l4;
	private Map<Integer,ExperimentBean> expList = null;
	private ProteinView pv;
	private Map<Integer, FractionBean> fractionsList;
	private User user;
	private String url,dbName,driver,userName,  password;
	private Embedded image1;
	private Embedded image2;
	private Embedded image3;
	private HttpServletResponse resp;
	private ExperimentHandler expHand = new ExperimentHandler(url, dbName, driver, userName, password);

	public MainWindow(String url,String dbName,String driver,String userName, String password, Embedded image, Embedded image2, Embedded image3, HttpServletResponse resp) {
		layout4 = new LoginView(url,dbName,driver,userName,  password);
		this.url= url;
		this.image1 = image;
		this.image2 = image2;
		this.image3 = image3;
		this.dbName = dbName;
		this.driver = driver;
		this.userName = userName;
		this.password  = password;
		this.resp = resp;
		pv = new ProteinView(url,dbName,driver,userName,  password);
		buildMainLayout();
		
	}


	@SuppressWarnings("deprecation")
	private void buildMainLayout() {
		VerticalLayout spacer1 = new VerticalLayout();
		spacer1.setHeight("2px");
		spacer1.setStyle(Reindeer.LAYOUT_BLACK);
		this.addComponent(spacer1);	
        Label welcomeLable = new Label("<h2  align='center' ; style='font-family:verdana;color:white; '><FONT SIZE='5.0'>CSF PROTEOME RESOURCE (CSF PR)</FONT></h2>");
        welcomeLable.setContentMode(Label.CONTENT_XHTML);
        welcomeLable.setWidth("490px");
        layout1.setStyle(Reindeer.LAYOUT_BLUE);
        layout1.setWidth("1300px");
        layout1.setHeight("220px");
        layout1.addComponent(welcomeLable);  
        layout1.setComponentAlignment(welcomeLable, Alignment.MIDDLE_LEFT);
       
        
        HorizontalLayout hlo = new HorizontalLayout();       
        image1.setWidth("301px");
        image1.setHeight("161px");
      /* image1.addListener(new ClickListener(){

			/**
			 * 
			 *
	//		private static final long serialVersionUID = 1L;

	//		public void click(com.vaadin.event.MouseEvents.ClickEvent event) {
				try {
					System.out.println(image1.getCaption());
				//	expHand.FarwardUrl(resp, image1.getCaption());
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}});*/
        hlo.addComponent(image1);
        image2.setWidth("141px");
        image2.setHeight("161px");
        hlo.addComponent(image2);
        image3.setWidth("277px");
        image3.setHeight("161px");
        hlo.addComponent(image3);
        hlo.setComponentAlignment(image1, Alignment.TOP_CENTER);
        hlo.setComponentAlignment(image2, Alignment.TOP_CENTER);
        hlo.setComponentAlignment(image3, Alignment.TOP_CENTER);
        hlo.setWidth("719px");
        hlo.setHeight("190px");
      
        VerticalLayout logoLayout = new VerticalLayout();
        logoLayout.setStyle(Reindeer.LAYOUT_BLUE);
        logoLayout.addComponent(hlo);
        logoLayout.setComponentAlignment(hlo, Alignment.TOP_RIGHT);
        
        logoLayout.setWidth("760px");;
        logoLayout.setHeight("220px");
         
        layout1.addComponent(logoLayout);
        layout1.setComponentAlignment(logoLayout, Alignment.TOP_RIGHT);
        
       
        
       // layout1.addComponent(hsp);
        
		layout1.setMargin(false, false, false, true);
		layout1.setWidth("100%");
		layout1.setHeight("180px");
		//layout1.setSizeFull();	
		// Create the stream resource with some initial filename.
		
		
		
		
		this.addComponent(layout1);
		VerticalLayout spacer2 = new VerticalLayout();
		spacer2.setHeight("2px");
		spacer2.setStyle(Reindeer.LAYOUT_BLACK);
		this.addComponent(spacer2);
		 // Tab 1 content
        l1 = new VerticalLayout();
        l1.setMargin(true);
        
        // Tab 2 content
        l2 = new VerticalLayout();
        l2.setMargin(true);
        l2.setHeight("100%");
        // Tab 3 content
        
        l3 = new VerticalLayout();
        l3.setMargin(true);
        
        //Tab 1 login form
        l4 = new VerticalLayout();
        l4.setMargin(true);
        l4.addComponent(layout4);
        
        t = new TabSheet();
        layout2.addComponent(t);
        t.setStyle(Reindeer.TABSHEET_MINIMAL);
        t.setHeight("100%");
        t.setWidth("100%");
        if(user != null)
        	t1 = t.addTab(l1, "Experiments Editor", null);
        else
        	t4 = t.addTab(l4, "Experiments Editor (Require Sign In)", null);
        t2 = t.addTab(l2, "Proteins", null);
        t3 = t.addTab(l3, "Search", null);
        
        t.addListener(this);        
        addComponent(layout2);
        t.setSelectedTab(t2);
		
	}


	 public void selectedTabChange(SelectedTabChangeEvent event) {
		   String c = t.getTab(event.getTabSheet().getSelectedTab()).getCaption();
	        if(c.equals("Experiments Editor (Require Sign In)"))
	        {
	        	
	        	
	        }        
	          else if(c.equals("Proteins"))
	          {
	        	  layout3.removeAllComponents();
	        	 // pv = new ProteinView();
	        	  pv.updateExpTable();
	              layout3.addComponent(pv);
	              l2.addComponent(layout3);
	        	  expList = pv.getExpList();
	          }
	        else if(c.equals("Search"))
	        {
	        	if (pv!=null)
	        		expList = pv.getUpdatedExpList();
	        	l3.removeAllComponents();
	        	layout3.removeAllComponents();
	        	pv.updateExpTable();
	        	expList = pv.getExpList();
	        	SearchUnit searchUnit = new SearchUnit(expList,url,dbName,driver,userName,  password);
	        	
	        	layout3.addComponent(searchUnit);
	            l3.addComponent(layout3);
	        }
	    }

	    public void buttonClick(ClickEvent event) {        
	       
	        t.requestRepaint();
	    }
	    public Map<Integer,ExperimentBean> getExpList()
	    {
	    	return expList;
	    }


		 public Map<Integer, FractionBean> getFractionsList()
    {
    	
		return  fractionsList;
    }
		 public User getUser()
		 {
			 return this.user;
		 }
}
