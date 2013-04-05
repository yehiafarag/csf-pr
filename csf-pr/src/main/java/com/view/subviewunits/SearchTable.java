package com.view.subviewunits;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.helperunits.MyButton;
import com.helperunits.MyLink;
import com.helperunits.PI;
import com.model.beans.ExperimentBean;
import com.model.beans.ProteinBean;
import com.vaadin.data.Item;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.Window;
import com.view.ProteinView;

public class SearchTable  extends  Table  implements Serializable,Button.ClickListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);	
	private DecimalFormat df = null;
	
	
	private Map<Integer,ExperimentBean>expList;
	private ProteinView pv;
	///v-2
	public SearchTable(Map<Integer,ExperimentBean>expList,   List<Map<Integer, List<ProteinBean>>> listOfExpProtList, List<Map<Integer, Map<Integer, ProteinBean>>> listOfProtExpFullList,ProteinView pv)
	{
		this.expList = expList;
		this.pv = pv;
		//this.setStyle(Reindeer.TABLE_STRONG);
 		this.setSelectable(true);
 		this.setColumnReorderingAllowed(true);
 		this.setColumnCollapsingAllowed(true);
 		this.setImmediate(true); // react at once when something is selected
 		this.setWidth("100%");
 		this.setHeight("150px");

 		this.addContainerProperty("Index",Integer.class,  null,"",null,com.vaadin.ui.Table.ALIGN_RIGHT);	
 		this.addContainerProperty("Experiment", MyButton.class,  null);
 		this.addContainerProperty("Accession", MyLink.class,  null);
 		
 		this.addContainerProperty("Species",String.class, null);
 		this.addContainerProperty("Sample Type",String.class, null); 
		
 		this.addContainerProperty("Sample Processing", String.class,  null);
 		this.addContainerProperty("Instrument Type",String.class, null);
 		this.addContainerProperty("Frag. Mode",String.class, null); 
		
 		
 		
 		
 		String Protein_Inference = "Protein Inference";
 		this.addContainerProperty(Protein_Inference,PI.class, null,"PI",null,com.vaadin.ui.Table.ALIGN_CENTER); 		
 		
 		this.addContainerProperty("Other Protein(s)", String.class,  null);
 		this.addContainerProperty("Description",String.class,  null);	 		
 		this.addContainerProperty("Sequence Coverage(%)",Double.class, null,"Coverage(%)",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		this.addContainerProperty("# Validated Peptides", Integer.class,  null,"#Peptides",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		this.addContainerProperty("# Validated Spectra",Integer.class,  null,"#Spectra",null,com.vaadin.ui.Table.ALIGN_RIGHT);	 		
 		this.addContainerProperty("NSAF",Double.class, null,"NSAF",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		this.addContainerProperty("MW", String.class,  null,"MW",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		String Confidence = "Confidence";
 		this.addContainerProperty(Confidence, Double.class,  null,Confidence,null,com.vaadin.ui.Table.ALIGN_CENTER);
 		

		//TableCellPercentChart conf = null;
 		MyLink link = null;
 		MyButton experimentLink = null;
 		PI pi = null;
		 Resource res2 = null;
 		if(listOfProtExpFullList == null){
			 int index = 1;
			 df=  new DecimalFormat("#.##",otherSymbols);
			
			Map<String,String> filterList = new HashMap<String,String>();
			 for(Map<Integer, List<ProteinBean>> expProList:listOfExpProtList){
				 for(int key: expProList.keySet()){
					ExperimentBean exp = expList.get(key);
				 	List<ProteinBean> pbList = expProList.get(key);		
					 for(ProteinBean pb: pbList){
						 
						 
						 
					     
					     
						 
						 String filter = ""+exp.getExpId()+","+pb.getAccession();
						 if(filterList.containsKey(filter))
						 {
						 }
						 else{
							 
							 if(pb.getProteinInferenceClass().equalsIgnoreCase("SINGLE PROTEIN"))
								 res2 = new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-snc6/263426_116594491857485_1503571748_n.jpg");
							 else  if(pb.getProteinInferenceClass().equalsIgnoreCase("UNRELATED PROTEINS"))
								 res2 = new ExternalResource("http://sphotos-h.ak.fbcdn.net/hphotos-ak-prn1/549354_116594531857481_1813966302_n.jpg");
							 else if(pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS"))
								 res2 = new ExternalResource("http://sphotos-f.ak.fbcdn.net/hphotos-ak-snc7/312343_116594485190819_1629145620_n.jpg");
							 else if (pb.getProteinInferenceClass().equalsIgnoreCase("UNRELATED ISOFORMS")||pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS AND UNRELATED PROTEIN(S)"))
								 res2 = new ExternalResource("http://sphotos-a.ak.fbcdn.net/hphotos-ak-prn1/544345_116594495190818_129866024_n.jpg");
							
					     pi = new PI(pb.getProteinInferenceClass(), res2);
					     pi.setDescription(pb.getProteinInferenceClass());
							 link =new MyLink(pb.getAccession(), new ExternalResource("http://www.uniprot.org/uniprot/"+pb.getAccession()));
							 link.setTargetName("_blank");
							 link.setDescription("UniProt link for "+pb.getAccession());
							 
							// conf = new TableCellPercentChart(pb.getConfidence());	
							// conf.setDescription(Confidence+" : "+df.format(pb.getConfidence())+" %");
									 
							 experimentLink = new MyButton(exp.getName(),key,this);
							 experimentLink.setDescription("GET "+exp.getName()+" DETAILS");
							 this.addItem(new Object[] {index,experimentLink,link,exp.getSpecies(), exp.getSampleType(),exp.getSampleProcessing(),exp.getInstrumentType(),exp.getFragMode(),pi,pb.getOtherProteins(),pb.getDescription(),Double.valueOf(df.format(pb.getSequenceCoverage())),pb.getNumberValidatedPeptides(),pb.getNumberValidatedSpectra(),Double.valueOf(df.format(pb.getNsaf())),Double.valueOf(df.format(pb.getMw_kDa())),Double.valueOf(df.format(pb.getConfidence()))}, new Integer(index));	 
							 index++;
							 filterList.put(filter, filter);
						 }
				 	}
			 	} 
			 }
			 if(index == 1)
				 this.setVisible(false);
 		}
 		else
 		{
			Map<String,String> filterList = new HashMap<String,String>();
 			df=  new DecimalFormat("#.##",otherSymbols);
 			 int index = 1;
 			 for(Map<Integer, Map<Integer, ProteinBean>> fullProtExpList:listOfProtExpFullList)
 			 {
 				 for(Map<Integer, ProteinBean> temProtExpList:fullProtExpList.values()){
					 for(int key: temProtExpList.keySet()){
						 ExperimentBean exp = expList.get(key);
						 ProteinBean pb = temProtExpList.get(key);
						 String filter = ""+exp.getExpId()+","+pb.getAccession();
						 if(filterList.containsKey(filter))
						 {
						 }
						 else{
							 if(pb.getProteinInferenceClass() == null)
								 ;
							 else if(pb.getProteinInferenceClass().equalsIgnoreCase("SINGLE PROTEIN"))
								 res2 = new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-snc6/263426_116594491857485_1503571748_n.jpg");
							 else  if(pb.getProteinInferenceClass().equalsIgnoreCase("UNRELATED PROTEINS"))
								 res2 = new ExternalResource("http://sphotos-h.ak.fbcdn.net/hphotos-ak-prn1/549354_116594531857481_1813966302_n.jpg");
							 else if(pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS"))
								 res2 = new ExternalResource("http://sphotos-f.ak.fbcdn.net/hphotos-ak-snc7/312343_116594485190819_1629145620_n.jpg");
							 else if (pb.getProteinInferenceClass().equalsIgnoreCase("UNRELATED ISOFORMS")||pb.getProteinInferenceClass().equalsIgnoreCase("ISOFORMS AND UNRELATED PROTEIN(S)"))
								 res2 = new ExternalResource("http://sphotos-a.ak.fbcdn.net/hphotos-ak-prn1/544345_116594495190818_129866024_n.jpg");
							if(pb.getProteinInferenceClass() != null){
								
							     pi = new PI(pb.getProteinInferenceClass(), res2);
							     pi.setDescription(pb.getProteinInferenceClass());
							}
						     
						     
							 
							 link =new MyLink(pb.getAccession(), new ExternalResource("http://www.uniprot.org/uniprot/"+pb.getAccession()));
							 link.setTargetName("_blank");
							 link.setDescription("UniProt link for "+pb.getAccession());
							// conf = new TableCellPercentChart(pb.getConfidence());	
							//	conf.setDescription(Confidence+" : "+df.format(pb.getConfidence())+" %");
									 
							 experimentLink = new MyButton(exp.getName(),key,this);
							 experimentLink.setDescription("GET "+exp.getName()+" DETAILS");
							 this.addItem(new Object[] {index, experimentLink,link,exp.getSpecies(), exp.getSampleType(),exp.getSampleProcessing(),exp.getInstrumentType(),exp.getFragMode(),pi,pb.getOtherProteins(),pb.getDescription(),Double.valueOf(df.format(pb.getSequenceCoverage())),pb.getNumberValidatedPeptides(),pb.getNumberValidatedSpectra(),Double.valueOf(df.format(pb.getNsaf())),Double.valueOf(df.format(pb.getMw_kDa())),Double.valueOf(df.format(pb.getConfidence()))}, new Integer(index));	 
							index++;
							 filterList.put(filter, filter);
						 }
					 }
 				 }
 			 }
 			if(index == 1)
				 this.setVisible(false);
 			
 		}
 		for(Object propertyId:this.getSortableContainerPropertyIds()){		
			// if(propertyId.toString().equals("Sequence Coverage(%)")||propertyId.toString().equals(Confidence))
				 // setColumnExpandRatio(propertyId.toString(), 1.8f);
			
			 if(propertyId.toString().equals("Description"))
				 setColumnExpandRatio(propertyId, 4.0f);
			 else
				 setColumnExpandRatio(propertyId.toString(), 0.5f);
			// else
			//	 
		 }
			  setColumnWidth("Index", 35);
		 this.setSortContainerPropertyId(Confidence);
		 this.setSortAscending(false);
		 int indexing =1;
		 for(Object id:this.getItemIds())
		 {
			 Item item = this.getItem(id);
			 item.getItemProperty("Index").setValue(indexing);
			 indexing++;
			 
		 }
		 this.setItemDescriptionGenerator(new ItemDescriptionGenerator() {                             
				
				private static final long serialVersionUID = 6268199275509867378L;

				
				
					public String generateDescription(Component source, Object itemId, Object propertyId) {
				        if(propertyId == null){
				        	;
				        }
				        else if(propertyId.equals("Experiment")) {
				            return "Experiment";
				        } else if(propertyId.equals("Accession")) {
				            return "Accession";
				        } 
				        else if(propertyId.equals("Sample Type")) {
				            return "Sample Type";
				        } 
				        else if(propertyId.equals("Species")) {
				            return "Species";
				        }
				        else if(propertyId.equals("Sample Processing")) {
				            return "Sample Processing";
				        }
				        
					  else if(propertyId.equals("Instrument Type")) {
				            return "Instrument Type";
				            
				        }else if(propertyId.equals("Other Protein(s)")) {
				            return "Other Protein(s)";
				            
				            
				        }else if(propertyId.equals("Description")) {
				            return "Description";
				            
				            
				        }else if(propertyId.equals("Sequence Coverage(%)")) {
				            return "Sequence Coverage(%)";
				            
				            
				        }else if(propertyId.equals("# Validated Peptides")) {
				            return "# Validated Peptides";
				        } 
				        
				        else if(propertyId.equals("# Validated Spectra")) {
				            return "# Validated Spectra";
				        }
				        else if(propertyId.equals("MW")) {
				            return "MW";
				        }
				        else if(propertyId.equals("Confidence")) {
				            return "Confidence";
				        }			
				        else if(propertyId.equals("NSAF")) {
				            return "NSAF";
				        }	
				        else if(propertyId.equals("Protein Inference")) {
				            return "Protein Inference";
				        }				   
				        else if(propertyId.equals("Frag. Mode")) {
				            return "Frag. Mode";
				        }	
				        return null;
				    }
				});
		
		
	}
	public void buttonClick(ClickEvent event) {
		MyButton ml = (MyButton) event.getSource();
		int key = ml.getKey();
		
		// Create a window that contains what you want to print
        Window window = new Window("Experiment Details");
       
        ExperimentBean exp = expList.get(key);
        // Have some content to print
        window.addComponent(pv.showExpProt(exp));
        
        // Add the printing window as a new application-level
        // window
        getApplication().addWindow(window);

        // Open it as a popup window with no decorations
        
        getWindow().open (new ExternalResource(window.getURL()),
                "_blank", 1100, 600,  // Width and height 
                Window.BORDER_NONE); // No decorations
       
    }

		
	
}
