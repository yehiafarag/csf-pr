package com.view.subviewunits;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import com.helperunits.MyEmbedded;
import com.helperunits.MyLabel;
import com.helperunits.PI;
import com.model.beans.PeptideBean;
import com.vaadin.data.Item;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class PeptideTable extends  Table  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DecimalFormat df=null;	
	
	
	public PeptideTable(Map<Integer, PeptideBean>peptideList, Set<String> pepSet)
	{
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		otherSymbols.setGroupingSeparator('.'); 
		df=  new DecimalFormat("#.##",otherSymbols);		
		//this.setStyle(Reindeer.TABLE_STRONG);
 		this.setSelectable(false);
 		this.setColumnReorderingAllowed(true);
 		this.setColumnCollapsingAllowed(true);
 		this.setImmediate(true); // react at once when something is selected
 		
 		
 		this.setWidth("100%");
 		this.setHeight("167px");
 		this.addContainerProperty("Index",Integer.class,  null,"",null,com.vaadin.ui.Table.ALIGN_RIGHT);	 		
 		
 		this.addContainerProperty("Peptide Protein(s)", String.class,  null);
 		this.setColumnCollapsed("Peptide Protein(s)", true);
 		String Protein_Inference = "Protein Inference";
 		this.addContainerProperty(Protein_Inference,PI.class, null,"PI",null,com.vaadin.ui.Table.ALIGN_CENTER); 		
 		
 		final String sequence = "Sequence";
 		this.addContainerProperty(sequence,MyLabel.class,  null);
 		this.addContainerProperty("AA Before",String.class,  null);	
 		this.addContainerProperty("AA After",String.class,  null);
 		this.setColumnCollapsed("AA Before", true);
 		this.setColumnCollapsed("AA After", true);
 		this.addContainerProperty("Peptide Start",String.class,  null,"Start",null,com.vaadin.ui.Table.ALIGN_RIGHT);	 		
 		this.addContainerProperty("Peptide End",String.class,  null,"End",null,com.vaadin.ui.Table.ALIGN_RIGHT);	
 		this.setColumnCollapsed("Peptide Start", true);
 		this.setColumnCollapsed("Peptide End", true);
 		
 		this.addContainerProperty("# Validated Spectra",Integer.class,  null,"#Spectra",null,com.vaadin.ui.Table.ALIGN_RIGHT);	 		
 		//this.addContainerProperty("Score",Double.class, null,"Score",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		String Confidence = "Confidence";
 		this.addContainerProperty(Confidence,Double.class,  null,Confidence,null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		this.addContainerProperty("Other Protein(s)",String.class, null);
 		this.addContainerProperty("Other Prot Descrip.",String.class,  null);
 		
 		this.setColumnCollapsed("Other Protein(s)", true);
 		
 		this.addContainerProperty("Peptide Prot. Descrip.",String.class,  null);
 		this.setColumnCollapsed("Peptide Prot. Descrip.", true);
 		//this.addContainerProperty("Fixed Modification",String.class,  null);	
 		this.addContainerProperty("Variable Modification",String.class,  null);	 		
 		this.addContainerProperty("Location Confidence",String.class,  null);
 		this.setColumnCollapsed("Variable Modification", true);
 		this.setColumnCollapsed("Location Confidence", true);
 		
 		this.addContainerProperty("Precursor Charge(s)",String.class,  null,"Precursor Charge(s)",null,com.vaadin.ui.Table.ALIGN_RIGHT);	 		
 		 
 		this.addContainerProperty("Sequence Tagged",String.class,  null,"Sequence Annotated",null,com.vaadin.ui.Table.ALIGN_LEFT);	 		
 		this.addContainerProperty("Enzymatic",MyEmbedded.class,  null,"Enzymatic",null,com.vaadin.ui.Table.ALIGN_CENTER);	
 		this.addContainerProperty("Validated",PI.class,  null,"Validated",null,com.vaadin.ui.Table.ALIGN_CENTER);	 		
 	//	this.addContainerProperty("Starred",MyEmbedded.class,  null,"Starred",null,com.vaadin.ui.Table.ALIGN_CENTER);	 		
 		
 		
 	
 		this.addContainerProperty("Deamidation & Glycopattern",MyEmbedded.class,  null,"Deamidation & Glycopattern",null,com.vaadin.ui.Table.ALIGN_CENTER);
 		
 		this.addContainerProperty("Glycopattern Positions",String.class, null,"Glycopattern Positions",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		
 		
 		
 		 MyEmbedded enz = null;
 	//	 MyEmbedded starred = null;
		 Resource res = null;
		 PI pi = null;
		 Resource res2 = null;
		 Resource res3 = null;
		 
		 MyLabel seq = null;
		 
		 MyEmbedded deamidationAndGlycopattern = null;
		 
		 PI validated = null;
 		int index = 1;
		 for(PeptideBean pb: peptideList.values()){
			 if(pb.isEnzymatic())
				 res = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
			 else
				 res = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
			 
			 enz  = new MyEmbedded(pb.isEnzymatic(), res);
			 enz.setWidth("16px");
			 enz.setHeight("16px");
			 enz.setDescription(""+pb.isEnzymatic());
			 
		/*	 if(pb.isStarred())
				 res = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
			else
				 res = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
			  
			 starred  = new MyEmbedded(pb.isStarred(), res);
			 starred.setWidth("16px");
			 starred.setHeight("16px");
			 starred.setDescription(""+pb.isStarred());
		 */
			 
			 if(pb.isDeamidationAndGlycopattern() == null)
			 {
				 res3 = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
				 deamidationAndGlycopattern  = new MyEmbedded(false, res3);
				 deamidationAndGlycopattern.setWidth("16px");
				 deamidationAndGlycopattern.setHeight("16px");
				 deamidationAndGlycopattern.setDescription("FALSE");			 
				 
			 }
			 else if(pb.isDeamidationAndGlycopattern()){
				 res3 = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
				 deamidationAndGlycopattern  = new MyEmbedded(pb.isDeamidationAndGlycopattern(), res3);
				 deamidationAndGlycopattern.setWidth("16px");
				 deamidationAndGlycopattern.setHeight("16px");
				 deamidationAndGlycopattern.setDescription(""+pb.isDeamidationAndGlycopattern());
			 }else {
				 res3 = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");

				 deamidationAndGlycopattern  = new MyEmbedded(pb.isDeamidationAndGlycopattern(), res3);
				 deamidationAndGlycopattern.setWidth("16px");
				 deamidationAndGlycopattern.setHeight("16px");
				 deamidationAndGlycopattern.setDescription(""+pb.isDeamidationAndGlycopattern());
			 
			 }
			 
			 
			 if(pb.getProteinInference() == null) { }
			 else if(pb.getProteinInference().equalsIgnoreCase("SINGLE PROTEIN")){
				 res2 = new ExternalResource("http://sphotos-d.ak.fbcdn.net/hphotos-ak-snc6/263426_116594491857485_1503571748_n.jpg");
				pi = new PI(pb.getProteinInference(), res2);
				 pi.setDescription(pb.getProteinInference());
				     
			 }else  if(pb.getProteinInference().equalsIgnoreCase("UNRELATED PROTEINS")){
			 
				 res2 = new ExternalResource("http://sphotos-h.ak.fbcdn.net/hphotos-ak-prn1/549354_116594531857481_1813966302_n.jpg");
				 pi = new PI(pb.getProteinInference(), res2);
				 pi.setDescription(pb.getProteinInference());
				     
			 } else if(pb.getProteinInference().equalsIgnoreCase("ISOFORMS"))
			 {
				 res2 = new ExternalResource("http://sphotos-f.ak.fbcdn.net/hphotos-ak-snc7/312343_116594485190819_1629145620_n.jpg");
				 pi = new PI(pb.getProteinInference(), res2);
				 pi.setDescription(pb.getProteinInference());
				     
			 } 
			 else if (pb.getProteinInference().equalsIgnoreCase("UNRELATED ISOFORMS")||pb.getProteinInference().equalsIgnoreCase("ISOFORMS AND UNRELATED PROTEIN(S)"))
			 {
				 res2 = new ExternalResource("http://sphotos-a.ak.fbcdn.net/hphotos-ak-prn1/544345_116594495190818_129866024_n.jpg");
				 pi = new PI(pb.getProteinInference(), res2);
				 pi.setDescription(pb.getProteinInference());
			 }
		     
		     if(pb.getValidated() == 1.0)
				 res2 = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
			 else
				 res2 = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
			 
		     validated = new PI(String.valueOf(pb.getValidated()), res2);
		     validated.setDescription(String.valueOf(pb.getValidated()));
		     
		     if(pepSet != null){
		    	
		    	 for(String str: pepSet)
		    	 {
		    		 if(pb.getSequence().contains(str))
		    		 {
		    			 seq = new MyLabel(pb.getSequence(), "red");
		    			 break;
		    		 }
		    		 seq = new MyLabel(pb.getSequence(), "black");
		    		 
		    	 }
		    	 
		     }	
		     else
		    	 seq = new MyLabel(pb.getSequence(), "black");
				seq.setDescription("The Peptide Sequence: "+pb.getSequence());
		     
		     
			 
			 this.addItem(new Object[] {index, pb.getPeptideProteins(),pi,seq,pb.getAaBefore(),pb.getAaAfter(),pb.getPeptideStart(),pb.getPeptideEnd(),pb.getNumberOfValidatedSpectra(),
					 //Double.valueOf(df.format(pb.getScore())),
					 Double.valueOf(df.format(pb.getConfidence())),pb.getOtherProteins(),pb.getOtherProteinDescriptions(),pb.getPeptideProteinsDescriptions(),
					 //pb.getFixedModification(),
					 pb.getVariableModification(),pb.getLocationConfidence(),pb.getPrecursorCharges(),pb.getSequenceTagged(),enz,validated,deamidationAndGlycopattern,pb.getGlycopatternPositions()}, new Integer(index));	 
			 index++;
			
		 }
		 
		 this.setSortContainerPropertyId(Confidence);
		 this.setSortAscending(false);
		 
		 
		 int indexing =1;
		 for(Object id:this.getItemIds())
		 {
			 Item item = this.getItem(id);
			 item.getItemProperty("Index").setValue(indexing);
			 indexing++;
			 
		 }
		 
		 for(Object propertyId:this.getSortableContainerPropertyIds()){		
				// if(propertyId.toString().equals("Sequence Coverage(%)")||propertyId.toString().equals(Confidence))
					 // setColumnExpandRatio(propertyId.toString(), 1.8f);
				
				 if(propertyId.toString().equals(sequence))
					 setColumnExpandRatio(propertyId, 2.0f);
				 else
					 setColumnExpandRatio(propertyId.toString(), 0.5f);
				// else
				//	 
			 }
				  setColumnWidth("Index", 35);
			//	  setColumnWidth(Confidence, 35);
		 // setColumnExpandRatio(sequence, 2.0f);
		 this.setItemDescriptionGenerator(new ItemDescriptionGenerator() {                             
			
			private static final long serialVersionUID = 6268199275509867378L;

				public String generateDescription(Component source, Object itemId, Object propertyId) {
			        if(propertyId == null){
			        	;
			        } else if(propertyId.equals("Peptide Protein(s)")) {
			            return "Peptide Protein(s)";
			        } 
			        else if(propertyId.equals("Protein Inference")) {
			            return "Protein Inference";
			        } 
			        else if(propertyId.equals("Peptide Prot. Descrip.")) {
			            return "Peptide Proteins Description(s)";
			        }
			        else if(propertyId.equals("Other Prot Descrip.")) {
			            return "Other Protein Description";
			        }
			        
				  else if(propertyId.equals(sequence)) {
			            return "Sequence";
			            
			        }
				  else if(propertyId.equals("Enzymatic")) {
			            return "Enzymatic";
			            
			        }
				  else if(propertyId.equals("Sequence Tagged")) {
			            return "Sequence Annotated";
				  }
				  
				  else if(propertyId.equals("AA Before")) {
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
			     /*   else if(propertyId.equals("Starred")) {
			            return "Starred";
			        }
			    */  else if(propertyId.equals("Validated")) {
			            return "Validated";
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
