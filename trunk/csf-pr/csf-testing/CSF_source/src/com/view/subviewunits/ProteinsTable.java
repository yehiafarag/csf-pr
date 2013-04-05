package com.view.subviewunits;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;

import com.model.beans.ProteinBean;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;

public class ProteinsTable extends  Table implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DecimalFormat df = new DecimalFormat("#.##");
	@SuppressWarnings("deprecation")
	public ProteinsTable(Map<String, ProteinBean> proteinsList)
	{
		this.setStyle(Reindeer.TABLE_STRONG+" "+Reindeer.TABLE_BORDERLESS);
 		this.setSelectable(true);
 		this.setColumnReorderingAllowed(true);
 		this.setColumnCollapsingAllowed(true);
 		this.setImmediate(true); // react at once when something is selected
 		this.setWidth("100%");
 		this.setHeight("150px");
 		this.addContainerProperty("Protein Inference",String.class, null);
 		this.addContainerProperty("Accession", String.class,  null);
 		this.addContainerProperty("Other Protein(s)", String.class,  null);
 		this.addContainerProperty("Description",String.class,  null);	 		
 		this.addContainerProperty("Sequence Coverage(%)",String.class, null);
 		this.addContainerProperty("# Validated Peptides", Integer.class,  null);
 		this.addContainerProperty("# Validated Spectra",Integer.class,  null);	 		
 		this.addContainerProperty("NSAF",String.class, null);
 		this.addContainerProperty("MW", String.class,  null);
 		this.addContainerProperty("Confidence",String.class,  null);
 		this.addContainerProperty("Starred",Boolean.class,  null);
 		 /* Add a few items in the table. */	

		int index = 1;
		 for(ProteinBean pb: proteinsList.values()){
			 this.addItem(new Object[] { pb.getProteinInferenceClass(),pb.getAccession(),pb.getOtherProteins(),pb.getDescription(),df.format(pb.getSequenceCoverage()),pb.getNumberValidatedPeptides(),pb.getNumberValidatedSpectra(),df.format(pb.getNsaf()),df.format(pb.getMw_kDa()),df.format(pb.getConfidence()),pb.isStarred()}, new Integer(index));	 
			index++;
			
		 }
		 for(Object propertyId:this.getSortableContainerPropertyIds())
			  setColumnExpandRatio(propertyId.toString(), 1.0f);
		 
	}

}
