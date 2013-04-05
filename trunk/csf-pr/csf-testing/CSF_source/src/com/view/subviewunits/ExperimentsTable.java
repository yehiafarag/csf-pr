package com.view.subviewunits;

import java.io.Serializable;
import java.util.Map;


import com.handlers.ExperimentHandler;
import com.model.beans.ExperimentBean;
import com.model.beans.PeptideBean;
import com.model.beans.ProteinBean;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;

public class ExperimentsTable extends CustomComponent implements Property.ValueChangeListener,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Table table ;
	private Map<Integer,ExperimentBean> expList = null;
	private Map<String,ProteinBean> proteinsList;
	private Map<Integer,PeptideBean> pepList;
	private ProteinsTable protTable;
	private PeptideTable pepTable;
	private Label pepLabel = null;
	private int  key;
	private  ExperimentHandler eh;
	private ExperimentBean exp;
	private int key2;
	
	private	Panel root = new Panel(" "); 
	private Label noExpLable = null;// Root element for contained components.
 	public  ExperimentsTable(String url,String dbName, String driver,String userName,String  password) {
		eh = new ExperimentHandler(url,dbName,driver,userName,  password);
		this.updateComponents(expList);	
	}
	@SuppressWarnings("deprecation")
	public void updateComponents(Map<Integer, ExperimentBean> expList2)
	{
		expList = this.getExperiments(expList);
		
		if(root !=null){
			root.removeAllComponents();
			}		
        setCompositionRoot(root);
		if (expList2 == null || expList2.size() == 0)
		{
			root.setHeight("200px");
			if(noExpLable != null)
				root.removeComponent(noExpLable);
			noExpLable =  new Label("<h3>Sorry No Experiment Availabe Now !</h3>");
			noExpLable.setContentMode(Label.CONTENT_XHTML);
			root.addComponent(noExpLable);
		}
		else{	
				root.setHeight("700px");
		        Label expLabel = new Label("<h3 Style='color:blue;'>Experiments Table</h3>");
		        expLabel.setContentMode(Label.CONTENT_XHTML);
		        root.addComponent(expLabel);
				table= new Table();
				table.setStyle(Reindeer.TABLE_STRONG+" "+Reindeer.TABLE_BORDERLESS);
				table.setHeight("100px");
				table.setSelectable(true);
				table.setColumnReorderingAllowed(true);
			    table.setColumnCollapsingAllowed(true);
			    table.setImmediate(true); // react at once when something is selected
				table.setWidth("100%");
				
				table.addContainerProperty("Exp Name", String.class,  null);
				table.addContainerProperty("Species",String.class, null);
				table.addContainerProperty("Sample Type",String.class, null); 
				
				table.addContainerProperty("Sample Processing", String.class,  null);
				table.addContainerProperty("Instrument Type",String.class, null);
				table.addContainerProperty("Frag. Mode",String.class, null); 
				
				table.addContainerProperty("# Fractions", Integer.class,  null);
				table.addContainerProperty("# Proteins",Integer.class, null);
				table.addContainerProperty("# Peptides",Integer.class, null); 
				table.addContainerProperty("Uploaded By ",String.class, null);
				table.addContainerProperty("Email",String.class, null); 
				table.addContainerProperty("Publication link",String.class, null); 
				
				/* Add a few items in the table. */
			
				 for(ExperimentBean exp: expList.values()){
					 table.addItem(new Object[] {exp.getName(),exp.getSpecies(), exp.getSampleType(),exp.getSampleProcessing(),exp.getInstrumentType(),exp.getFragMode(),exp.getFractionsNumber(),exp.getProteinsNumber(),exp.getPeptidesNumber(),exp.getUploadedByName(),exp.getEmail(),exp.getPublicationLink()}, new Integer(exp.getExpId()));	 
				 }
				 this.expList = expList2;
				 table.addListener(this);	
				 root.addComponent(table);
				 Label l = new Label("<br/>");
				 l.setContentMode(Label.CONTENT_XHTML);
				 root.addComponent(l);
		}
	}
	public void valueChange(ValueChangeEvent event) {
	
		if (table.getValue() != null)
			key = (Integer) table.getValue();
        this.updateComponents(expList);
        table.removeListener(this);
        table.select(key);
        table.addListener(this);
        exp = expList.get(key);
        int ready = exp.getReady();
        if(ready != 2)
        {
        	this.getWindow().showNotification("THIS EXPERIMENT NOT READY YET!");
        }
        else
        {
        	proteinsList  = eh.getProteinsList(key,expList);        	
        	exp.setProteinList(proteinsList);
        	expList.put(key, exp);        	
        	protTable = new ProteinsTable(proteinsList);
        	Label protLabel = new Label("<h3>Proteins Table</h3>");
        	protLabel.setContentMode(Label.CONTENT_XHTML);
        	root.addComponent(protLabel);
        	root.addComponent(protTable);
        	protTable.addListener(new Property.ValueChangeListener(){
				/**
				 * 
				 */
				private static final long serialVersionUID = 1L;

				public void valueChange(ValueChangeEvent event) {
					if(pepTable != null)
						root.removeComponent(pepTable);
					if(protTable.getValue() != null)
						key2 = (Integer) protTable.getValue();
					Item item = protTable.getItem(key2);
					String accession = item.getItemProperty("Accession").toString();

					pepList = eh.getPeptidesList(key,expList);
					exp = expList.get(key);
		        	exp.setPeptideList(pepList);
		        	expList.put(key, exp);
		        	Map<Integer,PeptideBean> pepProtList= eh.getPeptidesProtList(pepList, accession);       					
					if(pepProtList.size() == 0)
						ExperimentsTable.super.getWindow().showNotification("No Peptides Data Available For This Protein in this Experiment!");
					else{
						if(pepLabel != null)
							root.removeComponent(pepLabel);
						pepTable  = new PeptideTable(pepProtList);
						pepLabel = new Label("<h3>Peptides Table</h3>");
						pepLabel.setContentMode(Label.CONTENT_XHTML);
			        	root.addComponent(pepLabel);
						root.addComponent(pepTable);
					}
					
				}});     	
        }		
	}
	public Map<Integer,ExperimentBean> getExpList()
	{
		return this.expList;
	}
	public ProteinsTable getProtTable()
	 {
		 return protTable;
	 }
	public ExperimentBean getExp() {
        return exp;
    }
	public Map<Integer,ExperimentBean> getExperiments(Map<Integer,ExperimentBean> expList)
	{
		this.expList =  eh.getExperiments(expList);
		return this.expList;
	}

}
