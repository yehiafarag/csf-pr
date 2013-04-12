package com.view.subviewunits;

import java.io.Serializable;
import java.util.Map;

import com.handlers.ExperimentHandler;
import com.helperunits.Help;
import com.helperunits.CustomExternalLink;
import com.model.beans.ExperimentBean;
import com.model.beans.ProteinBean;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

public class ExperimentsTable extends CustomComponent implements Property.ValueChangeListener,Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Table table ;
	private Map<Integer,ExperimentBean> expList = null;
	private Map<String,ProteinBean> proteinsList;
	private ProteinsTable protTable;
	private int  key;
	private  ExperimentHandler eh;
	private ExperimentBean exp;
	private Help help = new Help();
	private HorizontalLayout helpNote ;
	private VerticalLayout butn1Layout; 
	private VerticalLayout butn2Layout; 
	
	
	private Button expBtnExpTable;
	private Button expBtnProtTable;
	private	VerticalLayout root = new VerticalLayout(); 
	
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
			root.setStyle(Reindeer.LAYOUT_WHITE);
			root.setMargin(false,false,false,true);
			//root.setSizeFull();
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
				root.setHeight("220px");
				root.setWidth("100%");
				
		        Label expLabel = new Label("<h3 Style='color:blue;'>Experiments </h3>");
		        expLabel.setHeight("20px");
		        
		        expLabel.setContentMode(Label.CONTENT_XHTML);
		        
		         if(butn1Layout != null)
		        	root.removeComponent(butn1Layout);
		        butn1Layout = new VerticalLayout();
		        
		        butn1Layout.setHeight("220px");
		        butn1Layout.setWidth("100%");
		        butn1Layout.setMargin(true,true,false,false);
		        if(helpNote != null)
		        	butn1Layout.removeComponent(helpNote);
		        Label label = new Label("Please Select Experiment To View The Experiment Proteins ");
		        helpNote = help.getHelpNote(label);
		        helpNote.setMargin(false, true, false, false);
		        //helpNote.setHeight("50px");
		        butn1Layout.addComponent(expLabel);
		       
		        if(expBtnExpTable !=null)
		        	butn1Layout.removeComponent(expBtnExpTable);
		        expBtnExpTable = new Button("Export Experiments");
		        //expBtnExpTable.setHeight("50px");
		        expBtnExpTable.setStyle(Reindeer.BUTTON_SMALL);	        
		        if(table !=null)
		        	butn1Layout.removeComponent(table);
				table= new Table();
				table.setStyle(Reindeer.TABLE_STRONG);
				table.setHeight("100px");
				table.setWidth("100%");
				table.setSelectable(true);
				table.setColumnReorderingAllowed(true);
			    table.setColumnCollapsingAllowed(true);
			    table.setImmediate(true); // react at once when something is selected
				
			    table.setWidth("100%");
			    //table.setSizeUndefined();
				String email = "Email";
				String uploadedBy="Uploaded By";
				table.addContainerProperty("Exp Name", String.class,  null);
				table.addContainerProperty("Species",String.class, null);
				table.addContainerProperty("Sample Type",String.class, null); 
				
				table.addContainerProperty("Sample Processing", String.class,  null);
				table.addContainerProperty("Instrument Type",String.class, null);
				table.addContainerProperty("Frag. Mode",String.class, null); 
				
				table.addContainerProperty("# Fractions", Integer.class,  null,"# Fractions",null,com.vaadin.ui.Table.ALIGN_RIGHT);
				table.addContainerProperty("# Proteins",Integer.class, null,"# Proteins",null,com.vaadin.ui.Table.ALIGN_RIGHT);
				table.addContainerProperty("# Peptides",Integer.class, null,"# Peptides",null,com.vaadin.ui.Table.ALIGN_RIGHT); 
				table.addContainerProperty(uploadedBy,String.class, null);
				table.addContainerProperty(email,String.class, null); 
				
				
				table.addContainerProperty("Publication link",CustomExternalLink.class, null); 
				
				/* Add a few items in the table. */
			
				 for(ExperimentBean exp: expList.values()){
					 CustomExternalLink link = null;
					 if(exp.getPublicationLink()!=null){
						 if(exp.getPublicationLink().toLowerCase().contains("http"))
							 link = new CustomExternalLink(exp.getPublicationLink(),    exp.getPublicationLink().toLowerCase());
						 else 
							 link = new CustomExternalLink(exp.getPublicationLink(),"http://"+exp.getPublicationLink().toLowerCase());
					 	link.setDescription("Go To "+exp.getPublicationLink());
					 	//link.setTargetName("_blank");
					 }
					 else
					 {
						 link = new CustomExternalLink("No Publication Link Available","");
						 link.setDescription(" No Publication Link Available");
					 }
					
					 table.addItem(new Object[] {exp.getName(),exp.getSpecies(), exp.getSampleType(),exp.getSampleProcessing(),exp.getInstrumentType(),exp.getFragMode(),exp.getFractionsNumber(),exp.getProteinsNumber(),exp.getPeptidesNumber(),exp.getUploadedByName(),exp.getEmail(),link}, new Integer(exp.getExpId()));	 
				 }
				 
				 for(Object propertyId:table.getSortableContainerPropertyIds())
					  table.setColumnExpandRatio(propertyId.toString(), 1.0f);
				 table.setColumnCollapsed(uploadedBy, true);
				 table.setColumnCollapsed(email, true);
				 this.expList = expList2;
				 table.addListener(this);	
				 butn1Layout.addComponent(table);
				// root.setComponentAlignment(table, Alignment.TOP_CENTER);
				 butn1Layout.setMargin(false,true,false,false);
				 butn1Layout.addComponent(expBtnExpTable);
				 butn1Layout.addComponent(helpNote);
				 butn1Layout.setComponentAlignment(expBtnExpTable, Alignment.TOP_RIGHT);
				 butn1Layout.setComponentAlignment(helpNote, Alignment.TOP_RIGHT);
				 //butn1Layout.setHeight("100px");
				 root.addComponent(butn1Layout);
				 root.setComponentAlignment(butn1Layout,Alignment.TOP_CENTER);;
			     
			        
				 expBtnExpTable.focus();
				 expBtnExpTable.addListener(new ClickListener() {
			            private static final long serialVersionUID = -73954695086117200L;
			            private ExcelExport excelExport;

			            public void buttonClick(ClickEvent event) {
			                excelExport = new ExcelExport(table);
			                excelExport.excludeCollapsedColumns();
			                excelExport.setReportTitle("Experiments");
			                excelExport.export();
			            }

			        });
		}
	}
	@SuppressWarnings("deprecation")
	public synchronized void valueChange(ValueChangeEvent event) {
	
		if (table.getValue() != null)
			key = (Integer) table.getValue();
        this.updateComponents(expList);
        table.removeListener(this);
        table.select(key);
        table.addListener(this);
        exp = expList.get(key);
        int ready = exp.getReady();
        if(ready != 2 && exp.getFractionsNumber() >0 || exp.getProteinsNumber()==0)
        {
        		this.getWindow().showNotification("THIS EXPERIMENT NOT READY YET!");
        	
        }
        else
        {
        	proteinsList  = eh.getProteinsList(key,expList);        	
        	exp.setProteinList(proteinsList);
        	expList.put(key, exp);        	
        	protTable = new ProteinsTable(proteinsList,exp.getFractionsNumber());
        	if(butn2Layout !=null)
        		root.removeComponent(butn2Layout);
        	butn2Layout = new VerticalLayout();
        	butn2Layout.setHeight("250px");
        	Label protLabel = new Label("<h3 Style='color:blue'>Proteins For ( "+exp.getName()+" )</h3>");
        	
        	protLabel.setContentMode(Label.CONTENT_XHTML);
        	butn2Layout.addComponent(protLabel);
        	if(expBtnProtTable != null)
        		butn2Layout.removeComponent(expBtnProtTable);
        	 expBtnProtTable = new Button("Export Proteins");
        	 expBtnProtTable.setStyle(Reindeer.BUTTON_SMALL);
        	 expBtnProtTable.focus();
        	 expBtnProtTable.addListener(new ClickListener() {
		            private static final long serialVersionUID = -73954695086117200L;
		            private ExcelExport excelExport;

		            public void buttonClick(ClickEvent event) {
		                excelExport = new ExcelExport(protTable);
		                excelExport.excludeCollapsedColumns();
		                excelExport.setReportTitle("Proteins for ( "+exp.getName()+" )");
		                excelExport.export();
		            }

		        });
        	// protTable.setHeight("150px");
        	 butn2Layout.addComponent(protTable);     	
        	
        	butn2Layout.addComponent(expBtnProtTable);
        	butn2Layout.setComponentAlignment(expBtnProtTable, Alignment.MIDDLE_RIGHT);
        	butn2Layout.setMargin(false, true, false, false);
        	//butn2Layout.setHeight("10%");
        	root.setHeight("470px");
        	root.addComponent(butn2Layout);
        	root.setComponentAlignment(butn2Layout, Alignment.TOP_CENTER);

			        	
			        
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
	public Map<Integer,ExperimentBean> resetExperiments()
	{
		this.expList =  eh.getExperiments(null);
		return this.expList;
	}

}
