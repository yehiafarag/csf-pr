package com.view.subviewunits;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.Map;

import com.model.beans.PeptideBean;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;
import com.vaadin.ui.themes.Reindeer;

public class PeptideTable extends  Table  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DecimalFormat df = new DecimalFormat("#.##");	
	@SuppressWarnings("deprecation")
	public PeptideTable(Map<Integer, PeptideBean>peptideList)
	{
		this.setStyle(Reindeer.TABLE_STRONG+" "+Reindeer.TABLE_BORDERLESS);
 		this.setSelectable(true);
 		this.setColumnReorderingAllowed(true);
 		this.setColumnCollapsingAllowed(true);
 		this.setImmediate(true); // react at once when something is selected
 		this.setWidth("100%");
 		this.setHeight("200px");
 		this.addContainerProperty("Peptide Protein(s)", String.class,  null);
 		this.addContainerProperty("Sequence",String.class,  null);
 		this.addContainerProperty("AA Before",String.class,  null);	
 		this.addContainerProperty("AA After",String.class,  null);	
 		this.addContainerProperty("Peptide Start",String.class,  null);	 		
 		this.addContainerProperty("Peptide End",String.class,  null);	
 		this.addContainerProperty("# Validated Spectra",Integer.class,  null);	 		
 		this.addContainerProperty("Score",String.class, null);
 		this.addContainerProperty("Confidence",String.class,  null);
 		this.addContainerProperty("Other Protein(s)",String.class, null);
 		this.addContainerProperty("Other Prot Descrip.",String.class,  null);
 		this.addContainerProperty("Peptide Prot. Descrip.",String.class,  null); 		
 		this.addContainerProperty("Variable Modification",String.class,  null);	 		
 		this.addContainerProperty("Location Confidence",String.class,  null);	
 		this.addContainerProperty("Precursor Charge(s)",String.class,  null);	 		
 		 int index = 1;
		 for(PeptideBean pb: peptideList.values()){
			 this.addItem(new Object[] { pb.getPeptideProteins(),pb.getSequence(),pb.getAaBefore(),pb.getAaAfter(),pb.getPeptideStart(),pb.getPeptideEnd(),pb.getNumberOfValidatedSpectra(),df.format(pb.getScore()),df.format(pb.getConfidence()),pb.getOtherProteins(),pb.getOtherProteinDescriptions(),pb.getPeptideProteinsDescriptions(),pb.getVariableModification(),pb.getLocationConfidence(),pb.getPrecursorCharges()}, new Integer(index));	 
			index++;
			
		 }
		  for(Object propertyId:this.getSortableContainerPropertyIds())
			  setColumnExpandRatio(propertyId.toString(), 1.0f);
		 this.setItemDescriptionGenerator(new ItemDescriptionGenerator() {                             
			
			private static final long serialVersionUID = 6268199275509867378L;

				public String generateDescription(Component source, Object itemId, Object propertyId) {
			        if(propertyId == null){
			        	;
			        } else if(propertyId.equals("Peptide Protein(s)")) {
			            return "Peptide Protein(s)";
			        } 
			        else if(propertyId.equals("Peptide Prot. Descrip.")) {
			            return "Peptide Proteins Description(s)";
			        }
			        else if(propertyId.equals("Other Prot Descrip.")) {
			            return "Other Protein Description";
			        }
			        
				  else if(propertyId.equals("Sequence")) {
			            return "Sequence";
			        }else if(propertyId.equals("AA Before")) {
			            return "AA Before";
			        }else if(propertyId.equals("AA After")) {
			            return "AA After";
			        }else if(propertyId.equals("Peptide Start")) {
			            return "Peptide Start";
			        }else if(propertyId.equals("Peptide End")) {
			            return "Peptide End";
			        } 
			        
			        else if(propertyId.equals("# Validated Spectra")) {
			            return "# Validated Spectra";
			        }
			        else if(propertyId.equals("Score")) {
			            return "Score";
			        }
			        else if(propertyId.equals("Confidence")) {
			            return "Confidence";
			        }
			        else if(propertyId.equals("Other Protein(s)")) {
			            return "Other Protein(s)";
			        }
			        else if(propertyId.equals("Variable Modification")) {
			            return "Variable Modification";
			        }
			        else if(propertyId.equals("Location Confidence")) {
			            return "Location Confidence";
			        }
			        else if(propertyId.equals("Precursor Charge(s)")) {
			            return "Precursor Charge(s)";
			        }
				 
			        return null;
			    }
			});
		 
	}
	

}
