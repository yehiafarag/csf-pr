package com.view;

import java.io.Serializable;
import java.util.Map;

import com.handlers.ExperimentHandler;
import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.SelectedTabChangeEvent;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.VerticalLayout;
import com.view.subviewunits.ExperimentsTable;

public class ProteinView extends VerticalLayout implements   TabSheet.SelectedTabChangeListener, Button.ClickListener,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4542745719375999737L;
	private VerticalLayout layout1 = new VerticalLayout();
	private VerticalLayout layout2 = new VerticalLayout();
	private TabSheet t;
	private Tab t1;
	private VerticalLayout  l1;
	private VerticalLayout l2;
	private Map<Integer,ExperimentBean> expList = null;
	private ExperimentHandler eh;
	private ExperimentsTable expTable ;
	private Map<Integer, FractionBean> fractionsList = null;
	private String url,dbName,driver,userName,  password;
	
	public ProteinView(String url,String dbName, String driver,String userName,String  password) {
		eh = new ExperimentHandler(url,dbName,driver,userName,  password);
		expTable = new ExperimentsTable(url,dbName,driver,userName,  password);
		this.url= url;
		this.dbName = dbName;
		this.driver = driver;
		this.userName = userName;
		this.password  = password;
		buildMainLayout();
		
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void buildMainLayout() {
		Label protLable = new Label("<h3  style='font-family:verdana;color:blue;'>Protein Information</h3>");
		protLable.setContentMode(Label.CONTENT_XHTML);
		layout1.addComponent(protLable);
		 // Tab 1 content
        l1 = new VerticalLayout();
        l1.setMargin(true);
        
        // Tab 2 content
        l2 = new VerticalLayout();
        l2.setMargin(true);
        l2.setHeight("100%");
        expTable = new ExperimentsTable(url,dbName,driver,userName,  password);
        layout2.addComponent(expTable);
        expList = expTable.getExpList();
        l1.addComponent(layout2);
       
        t = new TabSheet();
        t.setStyle(Reindeer.TABSHEET_MINIMAL);
        layout1.addComponent(t);
        t.setHeight("100%");
        t.setWidth("100%");
        t1 = t.addTab(l1, "Proteins Overview", null);
        t.addTab(l2, "Fractions", null);
        t.addListener(this);          
        t.setSelectedTab(t1);
        addComponent(layout1);
		
	}



	public void buttonClick(ClickEvent event) {
		 t.requestRepaint();
		
	}

	public void selectedTabChange(SelectedTabChangeEvent event) {
		String c = t.getTab(event.getTabSheet().getSelectedTab()).getCaption();
	    if(c.equals("Fractions"))
        {
        	
          ExperimentBean exp = expTable.getExp();
          expList = expTable.getExpList();	 
    	  layout2.removeAllComponents();
          if(exp != null && exp.getProteinList() != null){
        	  
        	  l2.removeAllComponents();
        	  FractionsView fv  = new FractionsView(exp,expList,this,url,dbName,driver,userName,  password);
        	  fv.updateComponents();
        	  layout2.addComponent(fv);
        	  l2.addComponent(layout2);
        	  fractionsList = eh.getFractionsList(exp.getExpId(),expList);
        	  exp.setFractionsList(fractionsList);
        	  expList.put(exp.getExpId(), exp);
          }
          else
          {
        	  getWindow().showNotification("YOU NEED TO SELECT A READY EXPERIMENT FIRST!");
        	  t.setSelectedTab(t1);
          }
         
        }
        else if(c.equals("Proteins Overview"))
          {
        	
        	expList = expTable.getExpList();
        	layout2.removeAllComponents();
              //expTable = new ExperimentsTable(this);
        	expTable.updateComponents(expList);
            layout2.addComponent(expTable);
            l1.addComponent(layout2);
          }
		
	}
	public Map<Integer,ExperimentBean> getExpList()
    {
    	return expList;
    }	    
    public Map<Integer, FractionBean> getFractionsList()
    {
    	return  fractionsList;
    }
    public Map<Integer,ExperimentBean> getUpdatedExpList()
    {
    	if(expTable != null)
    	{
    		expList = expTable.getExpList();
    	}
    	return expList;
    }
    public void updateExpTable()
    {
    	expList = expTable.getExperiments(expList);
    	expTable.updateComponents(expList);
    }
    
    

}
