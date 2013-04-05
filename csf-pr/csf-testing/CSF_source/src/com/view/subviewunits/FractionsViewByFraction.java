package com.view.subviewunits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.handlers.ExperimentHandler;
import com.model.FractionRangeUtilitie;
import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.ProteinBean;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.VerticalLayout;
import com.view.ProteinView;

public class FractionsViewByFraction extends  CustomComponent implements Property.ValueChangeListener,Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	Panel root = new Panel(" ");         // Root element for contained components.      
	private ProteinsTable protTable;
	private ExperimentBean exp;
	private int key;
	private Label protLabel ; //Protein table label
	private HorizontalSplitPanel hsplit;
	private Table fractTable;
	private Label fractionLabel ;
	private ProteinView proteinView;
	private ExperimentHandler eh ;
	private FractionRangeUtilitie fractionUti = new FractionRangeUtilitie();
	private ArrayList<String> ranges;
	//private ArrayList<String>rangesList;
	
	public FractionsViewByFraction(ExperimentBean exp, Map<Integer, ExperimentBean> expList, ProteinView proteinView,String url,String dbName, String driver,String userName,String  password) {
		eh = new ExperimentHandler(url,dbName,driver,userName,  password);
		this.exp = exp;
		this.proteinView = proteinView;       
	}
	
	public void updateComponents()
	{
		
		if(root !=null){
			root.removeAllComponents();
			}
		root.setHeight("700px");
        setCompositionRoot(root);
        
        VerticalLayout selectLayOut = new VerticalLayout();
        root.addComponent(selectLayOut);
        Label selectFractionLabel = new Label("<h3 Style='padding-right: 10px;'>Please Select Fraction to Display Proteins)</h3>");
        selectFractionLabel.setContentMode(Label.CONTENT_XHTML);
        selectFractionLabel.setWidth("30%");
        
       
        selectLayOut.addComponent(selectFractionLabel);
        ranges = fractionUti.updateFractionRange(exp);
        
        Select group = new Select("Fraction Range: ", ranges);
      
        group.setImmediate(true);
        group.addListener(new Property.ValueChangeListener(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void valueChange(ValueChangeEvent event) {
				exp = this.updateProteinFractionList(exp,event.getProperty().getValue().toString().split("\t")[0]);			
												
				updateProteinFractionView(exp.getFractionsList().get(Integer.valueOf(event.getProperty().getValue().toString().split("\t")[0])).getProteinList(),event.getProperty().getValue().toString().split("\t")[1]);
				
			}

			private ExperimentBean updateProteinFractionList( ExperimentBean exp, String keyString) {
				Map<String, ProteinBean> proteinList = exp.getProteinList();
				int keyInt = Integer.valueOf(keyString);
				FractionBean fb = exp.getFractionsList().get(keyInt);
				Map<String, ProteinBean> proteinFractionList = fb.getProteinList();
				
				for(String keys:proteinFractionList.keySet())
				{	
					if(proteinList.containsKey(keys))
					{
						proteinFractionList.put(keys, proteinList.get(keys));
						
					}
					
				}
				fb.setProteinList(proteinFractionList);
				exp.getFractionsList().put(keyInt, fb);
				return exp;
			}});
         
        selectLayOut.addComponent(group);      		
		
	}

	@SuppressWarnings("deprecation")
	public void valueChange(ValueChangeEvent event) {
		if(protTable.getValue() !=  null)
			key = (Integer) protTable.getValue();
		Item item = protTable.getItem(key);		
        protTable.getItem(key);
        String protAcc = item.getItemProperty("Accession").toString();
        double mw = Double.valueOf(item.getItemProperty("MW").toString());
        
        if (fractionLabel != null)
        	root.removeComponent(fractionLabel);
        fractionLabel = new Label("<h3>Fractions For ( "+protAcc+" )</h3>");
        fractionLabel.setContentMode(Label.CONTENT_XHTML);
    	root.addComponent(fractionLabel);
    	if(hsplit != null)
    		root.removeComponent(hsplit);
    	hsplit = new HorizontalSplitPanel(); 
    	hsplit.setHeight("400px");
    	hsplit.setSplitPosition(40.0f);
    	hsplit.setStyle(Reindeer.SPLITPANEL_SMALL);
    	root.addComponent(hsplit);
        hsplit.removeAllComponents();
        Map<Integer, FractionBean> fractionsList;
		while(true)
        {
        	fractionsList = proteinView.getFractionsList();
        	if(fractionsList.size() > 0)
        		break;
        }
        Map<Integer,ProteinBean> proteinFractionList = eh.getProteinFractionList(protAcc, fractionsList,exp.getExpId());
        fractTable = this.getFractionTable(proteinFractionList, ranges);
        
        hsplit.addComponent(fractTable);
        
        FractionsPlots2 fractionPlotView = new FractionsPlots2(proteinFractionList,ranges,mw);
        hsplit.addComponent(fractionPlotView);
	}
	public void updateProteinFractionView(Map<String, ProteinBean> protList,String fractId)
	{
			
		    	if(protLabel != null )
		    		root.removeComponent(protLabel);
		        protLabel = new Label("<h3>Proteins Table For Experiment ( "+exp.getExpId()+" ) And Fraction Range ( "+ fractId+" )</h3>");
		    	protLabel.setContentMode(Label.CONTENT_XHTML);
		    	protTable = new ProteinsTable(protList);
		    	root.addComponent(protLabel);
		    	root.addComponent(protTable);		
		    	protTable.setHeight("150px");
		    	protTable.addListener(this);	
		
		
	}
	
	
	
	@SuppressWarnings("deprecation")
	private Table getFractionTable(Map<Integer,ProteinBean> proteinFractionList,ArrayList<String> rangesList)
	{
		Table table= new Table();
		table.setStyle(Reindeer.TABLE_STRONG+" "+Reindeer.TABLE_BORDERLESS);
		table.setHeight("150px");
		table.setWidth("90%");
		table.setSelectable(true);
		table.setColumnReorderingAllowed(true);
	    table.setColumnCollapsingAllowed(true);
	    table.setImmediate(true); // react at once when something is selected
		 table.addContainerProperty("Fraction Range",String.class, null);
		 table.addContainerProperty("# Peptides ",Integer.class, null);
		 table.addContainerProperty("# Spectra ",Integer.class, null);
		 table.addContainerProperty("Average Precursor Intensity",Double.class, null); 
		 /* Add a few items in the table. */	
		 int x = 0;
		 for(ProteinBean pb: proteinFractionList.values()){
			 table.addItem(new Object[] {rangesList.get(x).split("\t")[1],pb.getNumberOfPeptidePerFraction(),pb.getNumberOfSpectraPerFraction(),pb.getAveragePrecursorIntensityPerFraction()}, new Integer(x));	
			 x++;			
		 }
		 for(Object propertyId:table.getSortableContainerPropertyIds())
			 table.setColumnExpandRatio(propertyId.toString(), 1.0f);
		return table;
	}
	
	
	

}
