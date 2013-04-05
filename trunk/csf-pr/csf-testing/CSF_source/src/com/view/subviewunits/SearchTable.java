package com.view.subviewunits;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import com.model.beans.ExperimentBean;
import com.model.beans.ProteinBean;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;

public class SearchTable  extends  Table  implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	///v-2
	@SuppressWarnings("deprecation")
	public SearchTable(Map<Integer,ExperimentBean>expList,   List<Map<Integer, List<ProteinBean>>> listOfExpProtList, List<Map<Integer, Map<Integer, ProteinBean>>> listOfProtExpFullList)
	{
		this.setStyle(Reindeer.TABLE_STRONG+" "+Reindeer.TABLE_BORDERLESS);
 		this.setSelectable(true);
 		this.setColumnReorderingAllowed(true);
 		this.setColumnCollapsingAllowed(true);
 		this.setImmediate(true); // react at once when something is selected
 		this.setWidth("100%");
 		this.setHeight("100px");
 		this.addContainerProperty("Experiment", String.class,  null);
 		
 		this.addContainerProperty("Species",String.class, null);
 		this.addContainerProperty("Sample Type",String.class, null); 
		
 		this.addContainerProperty("Sample Processing", String.class,  null);
 		this.addContainerProperty("Instrument Type",String.class, null);
 		this.addContainerProperty("Frag. Mode",String.class, null); 
		
 		
 		
 		
 		this.addContainerProperty("Protein Inference",String.class, null);
 		this.addContainerProperty("Accession", String.class,  null);
 		this.addContainerProperty("Other Protein(s)", String.class,  null);
 		this.addContainerProperty("Description",String.class,  null);	 		
 		this.addContainerProperty("Sequence Coverage(%)",Double.class, null);
 		this.addContainerProperty("# Validated Peptides", Integer.class,  null);
 		this.addContainerProperty("# Validated Spectra",Integer.class,  null);	 		
 		this.addContainerProperty("NSAF",Double.class, null);
 		this.addContainerProperty("MW", String.class,  null);
 		this.addContainerProperty("Confidence",Double.class,  null);
 		this.addContainerProperty("Starred",Boolean.class,  null);
 		 /* Add a few items in the table. */	

 		if(listOfProtExpFullList == null){
			 int index = 1;
			 for(Map<Integer, List<ProteinBean>> expProList:listOfExpProtList){
				 for(int key: expProList.keySet()){
					ExperimentBean exp = expList.get(key);
				 	List<ProteinBean> pbList = expProList.get(key);					 
					 for(ProteinBean pb: pbList){
						 this.addItem(new Object[] {exp.getName(),exp.getSpecies(), exp.getSampleType(),exp.getSampleProcessing(),exp.getInstrumentType(),exp.getFragMode(),pb.getProteinInferenceClass(),pb.getAccession(),pb.getOtherProteins(),pb.getDescription(),pb.getSequenceCoverage(),pb.getNumberValidatedPeptides(),pb.getNumberValidatedSpectra(),pb.getNsaf(),pb.getMw_kDa(),pb.getConfidence(),pb.isStarred()}, new Integer(index));	 
						 index++;
				 	}
			 	} 
			 }
			 if(index == 1)
				 this.setVisible(false);
 		}
 		else
 		{
 			 int index = 1;
 			 for(Map<Integer, Map<Integer, ProteinBean>> fullProtExpList:listOfProtExpFullList)
 			 {
 				 for(Map<Integer, ProteinBean> temProtExpList:fullProtExpList.values()){
					 for(int key: temProtExpList.keySet()){
						 ExperimentBean exp = expList.get(key);
						 ProteinBean pb = temProtExpList.get(key);
						 this.addItem(new Object[] { key+"\t	"+exp.getName(),exp.getSpecies(), exp.getSampleType(),exp.getSampleProcessing(),exp.getInstrumentType(),exp.getFragMode(),pb.getProteinInferenceClass(),pb.getAccession(),pb.getOtherProteins(),pb.getDescription(),pb.getSequenceCoverage(),pb.getNumberValidatedPeptides(),pb.getNumberValidatedSpectra(),pb.getNsaf(),pb.getMw_kDa(),pb.getConfidence(),pb.isStarred()}, new Integer(index));	 
						index++;
					 }
 				 }
 			 }
 			if(index == 1)
				 this.setVisible(false);
 			
 		}
		 for(Object propertyId:this.getSortableContainerPropertyIds())
			  setColumnExpandRatio(propertyId.toString(), 1.0f);
		 
		
		
	}
}
