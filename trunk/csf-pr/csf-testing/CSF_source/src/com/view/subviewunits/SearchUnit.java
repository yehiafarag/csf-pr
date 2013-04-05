package com.view.subviewunits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.handlers.ExperimentHandler;
import com.model.FractionRangeUtilitie;
import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.PeptideBean;
import com.model.beans.ProteinBean;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalSplitPanel;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.Reindeer;

public class SearchUnit extends CustomComponent implements ClickListener,Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private	Panel root = new Panel(" ");         // Root element for contained components.
	private TextArea searchField;
	private Button searchButton = new Button("Search");
	private ExperimentHandler eh ;
	private Label protLabel;
	private Label errorLabel;
	private PeptideTable pepTable;
	private Label pepLabel;
	private SearchTable protExpTab = null;
	private Label fractionLabel;
	private HorizontalSplitPanel hsplit;
	private Table fractTable;
	private  FractionsPlots2 fractionPlotView;
	private int key2;
	private Select select; //select the search method
	private Label selectLabel ;
	private Map<Integer, Map<Integer, ProteinBean>> protExpFullList ;
	private Map<Integer, ExperimentBean> expList;
	private FractionRangeUtilitie fractionUti = new FractionRangeUtilitie();
	private ArrayList<String> ranges;
	public SearchUnit(Map<Integer, ExperimentBean> expList,String url,String dbName, String driver,String userName,String  password) {
		eh = new ExperimentHandler(url,dbName,driver,userName,  password);	
		this.expList =expList; 
		this.updateComponents();   
    }
	
	///v-2
	@SuppressWarnings("deprecation")
	private void updateComponents() {
		final Form newSearchForm = new Form();
		searchField = new TextArea("Search Key ");		
		root.setStyle(Reindeer.PANEL_LIGHT);
		root.removeAllComponents();
	    setCompositionRoot(root);
	    searchField.setStyle(Reindeer.TEXTFIELD_SMALL);
	    newSearchForm.addField(1,searchField);	   
	    select = new Select("Search By ");
	    searchField.setWidth(160.0f);
	    Object itemId1 = select.addItem();
	    select.setItemCaption(itemId1, "Protein Accession");
	    Object itemId2 = select.addItem();
	    select.setItemCaption(itemId2,"Protein Name" );
	    Object itemId3 = select.addItem();
	    select.setItemCaption(itemId3, "Peptide Sequence");
        newSearchForm.addField(Integer.valueOf(2),select);
        searchButton.setStyle(Reindeer.BUTTON_SMALL);
	    newSearchForm.addField(4,searchButton);
	    root.addComponent(newSearchForm);	    
	    select.select(itemId1);		    
	    searchButton.addListener(this);
	    Label l = new Label("<h4 Style='color:blue;'>For Multiple Search...Please Use One key Per Line !</h4>");
		l.setContentMode(Label.CONTENT_XHTML);
		root.addComponent(l);
	}
	public void buttonClick(ClickEvent event) {
		
		Object seatchTypeObject = select.getValue();
		Object protSearchObject = searchField.getValue();
		updateComponents() ;
		if(protSearchObject == null || protSearchObject.toString().equals(""))
		{
			if(selectLabel !=null)
				root.removeComponent(selectLabel);
			selectLabel = new Label("<h4 Style='color:red;'>Please Enter Valid Key Word !</h4>");
			selectLabel.setContentMode(Label.CONTENT_XHTML);
			root.addComponent(selectLabel);
		}
		else if(seatchTypeObject == null)
		{
			if(selectLabel !=null)
				root.removeComponent(selectLabel);
			selectLabel = new Label("<h4 Style='color:red;'>Please Select Search by Method! </h4>");
			selectLabel.setContentMode(Label.CONTENT_XHTML);
			root.addComponent(selectLabel);
		}
		else
		{
			
			if(selectLabel !=null)
				root.removeComponent(selectLabel);			
			String searchType = seatchTypeObject.toString();
			searchField.setValue("");
			String protSearch = protSearchObject.toString().trim().toUpperCase();
			String[] searchArr = protSearch.split("\n");	
			Set <String> searchSet = new HashSet<String>();
			for(String str:searchArr)
				searchSet.add(str.trim());
			if(errorLabel != null)
    			root.removeComponent(errorLabel);
			
			
			
			
			
			
			///v-2
					
				Map<Integer, List<ProteinBean>> expProtList = null;
				List<Map<Integer, List<ProteinBean>>> ListOfExpProtList = new ArrayList<Map<Integer,List<ProteinBean>>>();
				List<Map<Integer, Map<Integer, ProteinBean>>> ListOfProtExpFullList = new ArrayList<Map<Integer,Map<Integer,ProteinBean>>>();
				
				String notFound = "";
				if(searchType.equals("1"))//case of protein accession
				{
					ListOfProtExpFullList = null;
					for(String searchStr :searchSet){
						expProtList = eh.searchProteinByAccession(searchStr.trim(),expList);
						int tag = 0;
						for(List<ProteinBean> pbl:expProtList.values()){
							if(pbl.size()>0){
								tag++;
								break;
							}
						}				
							
						if(tag==0)
							notFound+=searchStr+"\t";
						else
							ListOfExpProtList.add(expProtList);
					}
				//	ListOfExpProtList = this.filterListOfExpProtList(ListOfExpProtList);
					protSearch = this.filterTableTitle(searchSet,notFound);
				}
				else if (searchType.equals("2")) //case of protein name
				{
					for(String searchStr :searchSet)
					{
						protExpFullList = eh.searchProteinByName(searchStr.trim(), expList);
						int tag = 0;
						for( Map<Integer, ProteinBean> pbl:protExpFullList.values()){
							for(ProteinBean pb : pbl.values()){
								if(pb != null){
									tag++;
									break;
								}
							}
							if(tag > 0)
								break;
						}
						if(tag==0)
							notFound+=searchStr+"\t";
						else
							ListOfProtExpFullList.add(protExpFullList);
						
					}
				//	ListOfProtExpFullList = this.filterListOfProtExpFullList(ListOfProtExpFullList);
					protSearch = this.filterTableTitle(searchSet,notFound);
				
				}
				else //find protein by peptide sequence
				{
					for(String searchStr :searchSet)
					{
						protExpFullList = eh.searchProteinByPeptideSequence(searchStr.trim(), expList);
						int tag = 0;
						for( Map<Integer, ProteinBean> pbl:protExpFullList.values()){
							for(ProteinBean pb : pbl.values()){
								if(pb != null){
									tag++;
									break;
								}
							}
							if(tag > 0)
								break;
						}
						if(tag==0)
							notFound+=searchStr+"\t";
						else
							ListOfProtExpFullList.add(protExpFullList);
						
					}
				//	ListOfProtExpFullList = this.filterListOfProtExpFullList(ListOfProtExpFullList);
					protSearch = this.filterTableTitle(searchSet,notFound);
					
				}
				if(!notFound.equals(""))
					notFind(notFound);
				if(protLabel != null)
					root.removeComponent(protLabel);
				protLabel = new Label("<h3>Protein  "+protSearch+"  Table</h3>");
		    	protLabel.setContentMode(Label.CONTENT_XHTML);
		    	
		    	if(protExpTab != null )
		    		root.removeComponent(protExpTab);
		    	protExpTab = new SearchTable(expList,ListOfExpProtList,ListOfProtExpFullList);

		    	if(protExpTab != null && ((ListOfExpProtList != null && ListOfExpProtList.size()>0)||(ListOfProtExpFullList != null && ListOfProtExpFullList.size()>0 ))){
		    		if(protExpTab.isVisible()){	    		
		    		root.addComponent(protLabel);
					root.addComponent(protExpTab);
		    		}else
		    		{
		    			notFind( protSearch);
		    		}
					protExpTab.addListener(new Property.ValueChangeListener(){
			
						/**
						 * 
						 */
						private static final long serialVersionUID = 1L;
			
						@SuppressWarnings("deprecation")
						public void valueChange(ValueChangeEvent event) {
							
							if(pepTable != null)
								root.removeComponent(pepTable);
							if(protExpTab.getValue() != null)
								key2 = (Integer) protExpTab.getValue();
							Item item = protExpTab.getItem(key2);
							String accession = item.getItemProperty("Accession").toString();
							
							double mw = Double.valueOf(item.getItemProperty("MW").toString()); 
							
							String str = item.getItemProperty("Experiment").toString();
							String[] strArr = str.split("\t");
							int expId = Integer.valueOf(strArr[0]);
							Map<Integer, PeptideBean> pepProExpList = eh.getPeptidesProtExpList(expList,accession,expId);
							if(pepProExpList.size() == 0)
								;//SearchUnit.super.getWindow().showNotification("No Peptides Data Available For This Protein in this Experiment!");
							else
							{
								if(pepLabel != null)
									root.removeComponent(pepLabel);
								pepTable  = new PeptideTable(pepProExpList);
								pepLabel = new Label("<h3>Peptides of ( " +accession+") Table</h3>");
								pepLabel.setContentMode(Label.CONTENT_XHTML);
					        	root.addComponent(pepLabel);
								root.addComponent(pepTable);
							}
							
							
							
							
							
							/// fraction part
							 if (fractionLabel != null)
						        	root.removeComponent(fractionLabel);
						     fractionLabel = new Label("<h3>Fractions For ( "+accession+" )</h3>");
						     fractionLabel.setContentMode(Label.CONTENT_XHTML);
						     root.addComponent(fractionLabel);
						     if(hsplit != null)
						    	root.removeComponent(hsplit);
						    hsplit = new HorizontalSplitPanel(); 
						    hsplit.setHeight("300px");
						    hsplit.setSplitPosition(40.0f);
						    root.addComponent(hsplit);
						    Map<Integer, FractionBean> fractionsList;
						    ExperimentBean exp = null;
							while(true)
						    {
								exp = expList.get(expId);
						       	fractionsList =exp.getFractionsList();
						       	if( fractionsList == null || fractionsList.size() > 0)
						        		break;
						    }
						    Map<Integer,ProteinBean> proteinFractionList = eh.getProteinFractionList(accession, fractionsList,expId);
						   
						    ranges = fractionUti.updateFractionRange(exp);
						    fractTable = getFractionTable(proteinFractionList,ranges);
						    hsplit.setStyle(Reindeer.SPLITPANEL_SMALL);
						    hsplit.addComponent(fractTable);
						   //getWindow().showNotification("the fraction table for protein "+accession+" !!");
							if(fractionPlotView != null)
						      	hsplit.removeComponent(fractionPlotView);
						    fractionPlotView = new FractionsPlots2(proteinFractionList,ranges,mw);
						    hsplit.addComponent(fractionPlotView); 		
						}
					});
		    	}else
		    	{
		    		notFind( notFound);
		    	}
		    	if(protExpFullList != null)
		    		protExpFullList = null;
				
			
			
		}
		
		
		
	}

	
	
	
	@SuppressWarnings("deprecation")
	public Table getFractionTable( Map<Integer,ProteinBean> proteinFractionList,ArrayList<String> ranges)
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
		 int x = 1;
		 for(ProteinBean pb: proteinFractionList.values()){
			 table.addItem(new Object[] { ranges.get(x).split("\t")[1],pb.getNumberOfPeptidePerFraction(),pb.getNumberOfSpectraPerFraction(),pb.getAveragePrecursorIntensityPerFraction()}, new Integer(x));	
			 x++;			
		 }
		 for(Object propertyId:table.getSortableContainerPropertyIds())
			 table.setColumnExpandRatio(propertyId.toString(), 1.0f);
		return table;
	}
	
	/*private List<Map<Integer, Map<Integer, ProteinBean>>> filterListOfProtExpFullList(List<Map<Integer, Map<Integer, ProteinBean>>> listOfProtExpFullList) {
		Map<Integer,Map<Integer, ProteinBean>> temProtExpFullList = new HashMap<Integer, Map<Integer,ProteinBean>>();
		Set<String> tester = new HashSet<String>();
		int tag = 0;
		for(Map<Integer, Map<Integer, ProteinBean>> protExpFullList:listOfProtExpFullList)
		{
			for(int key:protExpFullList.keySet()){				
				Map<Integer, ProteinBean> pbl = protExpFullList.get(key);
				for(int expId:pbl.keySet())
				{
					if(pbl.get(key) != null){
						String value = ""+expId+","+pbl.get(key).getAccession();
						if(tester.add(value))
						{
							Map<Integer, ProteinBean> temp = new HashMap<Integer, ProteinBean>();
							temp.put(key, pbl.get(key));
							temProtExpFullList.put(tag,temp);
							tag++;
						}
					}
				}
			}
			
		}
		listOfProtExpFullList.clear();
		listOfProtExpFullList.add(temProtExpFullList);		
		return listOfProtExpFullList;
	}
	
	*/

	private String filterTableTitle(Set<String> searchSet, String notFound) {
		
		String[] strArr = notFound.split("\t");
		for(String str:strArr)
			searchSet.remove(str);
		
		return searchSet.toString();
	}

/*	private List<Map<Integer, List<ProteinBean>>> filterListOfExpProtList(List<Map<Integer, List<ProteinBean>>> listOfExpProtList) {
		// TODO Auto-generated method stub
		return listOfExpProtList;
	}
	
	*
	*/

	private void notFind(String protSearch)
	{
		if(errorLabel != null)
			root.removeComponent(errorLabel);
		errorLabel = new Label("<h3 Style='color:red;'>Sorry No Results Found for ( "+protSearch+" ) !!</h3>");
		errorLabel.setContentMode(Label.CONTENT_XHTML);    		
    	root.addComponent(errorLabel);
	}

}
