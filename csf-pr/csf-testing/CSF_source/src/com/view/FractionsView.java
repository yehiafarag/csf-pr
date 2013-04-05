package com.view;


import java.io.Serializable;
import java.util.Map;

import com.model.beans.ExperimentBean;

import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
//import com.virtuallypreinstalled.hene.charts.BarChart;
import com.vaadin.ui.themes.Reindeer;
import com.view.subviewunits.FractionViewByProteins;
import com.view.subviewunits.FractionsViewByFraction;


public class FractionsView extends VerticalLayout implements  Button.ClickListener, TabSheet.SelectedTabChangeListener,Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	//private	Panel root = new Panel(" ");       // Root element for contained components.      
	private ExperimentBean exp;	
	private ProteinView proteinView;
	//private ExperimentHandler eh ;
	private VerticalLayout layout1 = new VerticalLayout();
	private VerticalLayout layout2 = new VerticalLayout();
	private TabSheet t;
	@SuppressWarnings("unused")
	private Tab t1, t2;
	private VerticalLayout  l1;
	private VerticalLayout l2;
	private String url,dbName,driver,userName,  password;
	//private ExperimentsTable expTable ;
	private Map<Integer, ExperimentBean> expList;
	
	public FractionsView(ExperimentBean exp, Map<Integer, ExperimentBean> expList, ProteinView proteinView,String url,String dbName, String driver,String userName,String  password) {
	//	eh = new ExperimentHandler(url,dbName,driver,userName,  password);
		this.exp = exp;
		this.expList = expList;
		this.url= url;
		this.dbName = dbName;
		this.driver = driver;
		this.userName = userName;
		this.password  = password;
		this.proteinView = proteinView; 
		this.updateComponents();
	}
	
	@SuppressWarnings("deprecation")
	public void updateComponents()
	{
		
		if(layout1 !=null){
			layout1.removeAllComponents();
			}
		layout1.setHeight("100%");
		
		 // Tab 1 content
        l1 = new VerticalLayout();
        l1.setMargin(true);
        layout2.removeAllComponents();         
  	  	FractionViewByProteins fbp = new FractionViewByProteins(exp, expList, proteinView, url, dbName, driver, userName, password);
        fbp.updateComponents();
        layout2.addComponent(fbp);
        l1.addComponent(layout2); 	 
        
        // Tab 2 content
        l2 = new VerticalLayout();
        l2.setMargin(true);
        l2.setHeight("100%");
        
       
        
        t = new TabSheet();
        layout1.addComponent(t);
        t.setStyle(Reindeer.TABSHEET_MINIMAL);
        t.setHeight("100%");
        t.setWidth("100%");
       
        t1 = t.addTab(l1, "Show by Proteins", null);
        t2 = t.addTab(l2, "Show by Fractions", null);
        
        t.addListener(this);       
        
        this.addComponent(layout1);
        t.setSelectedTab(t1);
        		
		
	}


	public void buttonClick(ClickEvent event) {
		 t.requestRepaint();
		
	}
	public void selectedTabChange(SelectedTabChangeEvent event) {
		String c = t.getTab(event.getTabSheet().getSelectedTab()).getCaption();
	    if(c.equals("Show by Proteins"))
        {      
	    	l1.removeAllComponents();
	    	layout2.removeAllComponents();         
	    	FractionViewByProteins fbp = new FractionViewByProteins(exp, expList, proteinView, url, dbName, driver, userName, password);
	    	fbp.updateComponents();
	    	layout2.addComponent(fbp);
	    	l1.addComponent(layout2); 	 
          
         
        }
        else
          {
        	l2.removeAllComponents();
        	layout2.removeAllComponents();         
	    	FractionsViewByFraction fbf = new FractionsViewByFraction(exp, expList, proteinView, url, dbName, driver, userName, password);
	    	fbf.updateComponents();
	    	layout2.addComponent(fbf);
	    	l2.addComponent(layout2); 	
        	
          }
	
	}
	
	

}
