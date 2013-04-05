package com.view.subviewunits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

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
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;
import com.view.ProteinView;

public class FractionViewByProteins extends  CustomComponent implements Property.ValueChangeListener,Serializable {
	
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
	
	
	public FractionViewByProteins(ExperimentBean exp, Map<Integer, ExperimentBean> expList, ProteinView proteinView,String url,String dbName, String driver,String userName,String  password) {
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
        protTable = new ProteinsTable(exp.getProteinList());
        if(protLabel != null )
    		root.removeComponent(protLabel);
        protLabel = new Label("<h3>Proteins Table For Experiment ( "+exp.getExpId()+" )</h3>");
    	protLabel.setContentMode(Label.CONTENT_XHTML);
    	root.addComponent(protLabel);
    	root.addComponent(protTable);		
    	protTable.setHeight("150px");
    	protTable.addListener(this);			
		
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
    	hsplit.setSplitPosition(35.0f);
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
    	ranges = fractionUti.updateFractionRange(exp);
        fractTable = this.getFractionTable(proteinFractionList,ranges);
        
        hsplit.addComponent(fractTable);
       
        FractionsPlots2 fractionPlotView = new FractionsPlots2(proteinFractionList,ranges,mw);
        fractionPlotView.setSizeFull();
        hsplit.addComponent(fractionPlotView);
	}
	
	
	
	@SuppressWarnings("deprecation")
	private Table getFractionTable(Map<Integer,ProteinBean> proteinFractionList, ArrayList<String> ranges2)
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
			 table.addItem(new Object[] { ranges.get(x).split("\t")[1],pb.getNumberOfPeptidePerFraction(),pb.getNumberOfSpectraPerFraction(),pb.getAveragePrecursorIntensityPerFraction()}, new Integer(x));	
			 x++;			
		 }
		 for(Object propertyId:table.getSortableContainerPropertyIds())
			 table.setColumnExpandRatio(propertyId.toString(), 1.0f);
		return table;
	}
	
	
	

}
