package com.view;

import java.io.Serializable;
import java.util.Map;
import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.User;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.BaseTheme;
import com.vaadin.ui.themes.Reindeer;
import com.view.subviewunits.SearchUnit;

public class MainWindow extends VerticalLayout implements   TabSheet.SelectedTabChangeListener, Button.ClickListener,Serializable{

	private static final long serialVersionUID = 1490961570483515444L;

	private VerticalLayout fullAppLayout = new VerticalLayout();
	private HorizontalLayout header = new HorizontalLayout();//Title and logo layout (top layout)
	private VerticalLayout headerLayout = new VerticalLayout();
	private VerticalLayout body = new VerticalLayout();//Second layout (main layout)
	private VerticalLayout layout3 = new VerticalLayout();// sub main layout
	private LoginView layout4 ;
	//private VerticalLayout footerLayout = new VerticalLayout();// sub main layout
	
	
	private TabSheet t;//tab sheet for first menu (Experiments Editor,Proteins, Search)
	@SuppressWarnings("unused")
	private Tab t1, t2, t3,t4;//tabs for Experiments Editor,Proteins, Search
								//Tabs content layouts
	private VerticalLayout  l1;
	private VerticalLayout l2;
	private VerticalLayout l3,l4;
	private Map<Integer,ExperimentBean> expList = null;//experiments list
	private ProteinView pv;
	private Map<Integer, FractionBean> fractionsList;//fraction list
	private User user;
	private String url,dbName,driver,userName,  password;//database data
	
	private Link image1;//logo_1
	private Link image2;//logo_2
	private Link image3;//logo_3
	
	private Button adminIcon;
	
	private boolean isLoginView = false;
	
	public MainWindow(String url,String dbName,String driver,String userName, String password, Link image12, Link image22, Link image32) {
		
		this.url= url;
		this.image1 = image12;
		this.image2 = image22;
		this.image3 = image32;
		this.dbName = dbName;
		this.driver = driver;
		this.userName = userName;
		this.password  = password;	
		t = new TabSheet();
		adminIcon = this.getAdminIco();
		pv = new ProteinView(url,dbName,driver,userName,  password,adminIcon);
		layout4 = new LoginView(url,dbName,driver,userName,  password,t);
		buildMainLayout();
		
	}


	@SuppressWarnings("deprecation")
	private void buildMainLayout() {
		
	
		VerticalLayout spacer1 = new VerticalLayout();//first spaces
		spacer1.setHeight("2px");
		spacer1.setStyle(Reindeer.LAYOUT_BLACK);
		headerLayout.addComponent(spacer1);	
		headerLayout.setComponentAlignment(spacer1, Alignment.TOP_CENTER);		
        Label welcomeLable = new Label("<h2   align='center' ; style='font-family:verdana;color:white;width:490px; '><FONT SIZE='5.0'>CSF Proteome Resource (CSF-PR)</FONT></h2>");
        welcomeLable.setContentMode(Label.CONTENT_XHTML);
        welcomeLable.setWidth("15%");
        {
	        header.setStyle(Reindeer.LAYOUT_BLUE);
	        header.setWidth("1300px");
	        header.setHeight("150px");
	        header.addComponent(welcomeLable);  
	        header.setComponentAlignment(welcomeLable, Alignment.MIDDLE_LEFT);
        }
        VerticalLayout logoLayout = new VerticalLayout();
        logoLayout.setStyle(Reindeer.LAYOUT_BLUE);
        HorizontalLayout hlo = new HorizontalLayout();   
		
        image1.setWidth("237px");
		image1.setHeight("105px");     
		hlo.addComponent(image1);
		        
		image2.setWidth("87px");
		image2.setHeight("105px");
		hlo.addComponent(image2);
		       
		image3.setWidth("175px");
		image3.setHeight("105px");
		hlo.addComponent(image3);
        
        
        hlo.setComponentAlignment(image1, Alignment.TOP_CENTER);
		hlo.setComponentAlignment(image2, Alignment.TOP_CENTER);
		hlo.setComponentAlignment(image3, Alignment.TOP_CENTER);
		hlo.setWidth("499px");
		hlo.setHeight("106px");     
        logoLayout.addComponent(hlo);
		logoLayout.setComponentAlignment(hlo, Alignment.TOP_RIGHT);
		logoLayout.setSizeFull();         
		header.addComponent(logoLayout);
		header.setComponentAlignment(logoLayout, Alignment.MIDDLE_RIGHT);
		header.setWidth("100%");
		header.setHeight("106px");
		
		headerLayout.addComponent(header);
		headerLayout.setComponentAlignment(header, Alignment.TOP_CENTER);
		VerticalLayout spacer2 = new VerticalLayout();
		spacer2.setHeight("2px");
		spacer2.setStyle(Reindeer.LAYOUT_BLACK);
		headerLayout.addComponent(spacer2);		
		fullAppLayout.addComponent(headerLayout);
				
		this.addComponent(fullAppLayout);
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
        l4.setHeight("100%");
        l4.addComponent(layout4);
        
       
        
        body.addComponent(t);
        t.setStyle(Reindeer.TABSHEET_MINIMAL);
        t.setHeight("100%");
        t.setWidth("100%");
        
       
       	t4 = t.addTab(l4, "Dataset Editor (Require Sign In)", null);
        
        t2 = t.addTab(l2, "Proteins", null); 
        t3 = t.addTab(l3, "Search", null);
        t.addListener(this);        
       
        t.setSelectedTab(t2);
        t.requestRepaint();
        {

	       	 layout3.removeAllComponents();
		   	 pv.updateExpTable();
		   	 pv.buildMainLayout();
		        layout3.addComponent(pv);		       
		        l2.addComponent(layout3);
		   	 expList = pv.getExpList();
        }        
        this.addComponent(body);
        this.setExpandRatio(body, 100.0f);
		t4.setVisible(false);
		
	}


	 public void selectedTabChange(SelectedTabChangeEvent event) {
		   String c = t.getTab(event.getTabSheet().getSelectedTab()).getCaption();
	        if(c.equals("Dataset Editor (Require Sign In)"))
	        {
	        	isLoginView = true;
	        	
	        }        
	          else if(c.equals("Proteins"))
	          {
	        	  t4.setVisible(false);
	        	 layout3.removeAllComponents();
	        	 if(isLoginView)
	        	 {
	        		 
	        		 pv.resetExpTable();
	        		 pv.buildMainLayout();
		             layout3.addComponent(pv);
		             l2.addComponent(layout3);
		        	 expList = pv.resetExpList();
	        		 isLoginView = false;       		 
	        		 
	        	 }
	        	 else
	        	 {
		        	 layout3.removeAllComponents();
		        	 pv.updateExpTable();
		        	 pv.buildMainLayout();
		             layout3.addComponent(pv);	            
		             l2.addComponent(layout3);
		        	 expList = pv.getExpList();
	        	 }
	          }
	        else if(c.equals("Search"))
	        {
	        	t4.setVisible(false);
	        	if (pv!=null)
	        		expList = pv.getUpdatedExpList();
	        	l3.removeAllComponents();
	        	layout3.removeAllComponents();
	        	pv.updateExpTable();
	        	expList = pv.getExpList();
	        	SearchUnit searchUnit = new SearchUnit(expList,url,dbName,driver,userName,  password,this.adminIcon);
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
		 public void serUser(User user)//set user when user sign in
		 {
			 this.user = user;
		 }
		 public User getUser()//get user
		 {
			 return this.user;
		 }	 
		 @SuppressWarnings("deprecation")
		public void setBodyHeight(int i)
		 {
			// i = i-header.- footerLayout.getHeightUnits();
			 if(body != null)
				 body.setHeight((1f));
		 }
		 
		 private Button getAdminIco()
		 {
			 
			 Button b = new Button();		       
			 b.setStyleName(BaseTheme.BUTTON_LINK);
			 b.setDescription("Dataset Editor (Require Sign In)");
			 b.setIcon(new ExternalResource("http://sphotos-c.ak.fbcdn.net/hphotos-ak-ash3/600305_124728624377405_1884648661_n.jpg"));
		     b.addListener(new ClickListener() {			
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;
			public void buttonClick(ClickEvent event) {
				t4.setVisible(true);
				t.setSelectedTab(t4);
				t.requestRepaint();
				t4.setCaption("");
			}
		});
		return b; 
	}
}
