package com.view.subviewunits;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.Map;

import com.helperunits.MyEmbedded;
import com.helperunits.MyLink;
import com.helperunits.PI;
import com.model.beans.ProteinBean;
import com.vaadin.data.Item;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.ui.Component;
import com.vaadin.ui.Table;

public class ProteinsTable extends  Table implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DecimalFormat df = null;
	public ProteinsTable(Map<String, ProteinBean> proteinsList,int fractionNumber)
	{
		//this.setStyle(Reindeer.TABLE_STRONG);
 		this.setSelectable(true);
 		this.setColumnReorderingAllowed(true);
 		this.setColumnCollapsingAllowed(true);
 		this.setImmediate(true); // react at once when something is selected
 		this.setWidth("100%");
 		this.setHeight("160px"); 		
 		this.addContainerProperty("Index",Integer.class,  null,"",null,com.vaadin.ui.Table.ALIGN_RIGHT);	
 		this.addContainerProperty("Accession", MyLink.class,  null);
 		String Protein_Inference = "Protein Inference";
 		this.addContainerProperty(Protein_Inference,PI.class, null,"PI",null,com.vaadin.ui.Table.ALIGN_CENTER); 		
 		this.addContainerProperty("Other Protein(s)", String.class,  null);
 		this.addContainerProperty("Description",String.class,  null);	 		
 		this.addContainerProperty("Sequence Coverage(%)", Double.class, null,"Coverage(%)",null,com.vaadin.ui.Table.ALIGN_CENTER);
 		this.addContainerProperty("Non Enzymatic Peptides", MyEmbedded.class, null,"Non Enzymatic Peptides",null,com.vaadin.ui.Table.ALIGN_CENTER);
 		this.addContainerProperty("# Validated Peptides", Integer.class,  null,"#Peptides",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		this.addContainerProperty("# Validated Spectra",Integer.class,  null,"#Spectra",null,com.vaadin.ui.Table.ALIGN_RIGHT);	 		
 		this.addContainerProperty("NSAF",Double.class, null,"NSAF",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		this.addContainerProperty("MW", Double.class,  null,"MW",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 		String Confidence = "Confidence";
 		this.addContainerProperty(Confidence, Double.class,  null,Confidence,null,com.vaadin.ui.Table.ALIGN_CENTER);
 		if(fractionNumber > 0)
 		{
 			this.addContainerProperty("SpectrumFractionSpread lower range_kDa", Double.class,  null,"Spectrum Lower Range",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 			this.addContainerProperty("SpectrumFractionSpread upper range kDa", Double.class,  null,"Spectrum Upper Range",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 			this.addContainerProperty("PeptideFractionSpread lower range kDa", Double.class,  null,"Peptide Lower Range",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 			this.addContainerProperty("PeptideFractionSpread upper range kDa", Double.class,  null,"Peptide Upper Range",null,com.vaadin.ui.Table.ALIGN_RIGHT);
 	 		this.setColumnCollapsed("SpectrumFractionSpread lower range_kDa", true);
 	 		this.setColumnCollapsed("SpectrumFractionSpread upper range kDa", true);
 	 		this.setColumnCollapsed("PeptideFractionSpread lower range kDa", true);
 	 		this.setColumnCollapsed("PeptideFractionSpread upper range kDa", true);
 			
 		}
 		 /* Add a few items in the table. */	

		int index = 1;
		
		DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
		otherSymbols.setGroupingSeparator('.'); 
		df=  new DecimalFormat("#.##",otherSymbols);
		 MyLink link = null;
		 MyEmbedded nonEnz = null;
		 Resource res = null;;
		 Double d1 = null;
		 Double d2 = null;
		 Double d3 = null;
		 Double d4 = null;
		 PI pi = null;
		 Resource res2 = null;
		 for(ProteinBean pb: proteinsList.values()){
			
			 link =new MyLink(pb.getAccession(), new ExternalResource("http://www.uniprot.org/uniprot/"+pb.getAccession()));
			 link.setTargetName("_blank");
			 link.setDescription("UniProt link for "+pb.getAccession());
			 
		 if(pb.isNonEnzymaticPeptides())
			 res = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-ash3/550027_118467228336878_534577050_n.jpg");
		 else
			 res = new ExternalResource("http://sphotos-e.ak.fbcdn.net/hphotos-ak-prn1/66728_108335936016674_28773541_n.jpg");
		 
		 nonEnz  = new MyEmbedded(pb.isNonEnzymaticPeptides(), res);
		 nonEnz.setWidth("16px");
		 nonEnz.setHeight("16px");
		 nonEnz.setDescription(""+pb.isNonEnzymaticPeptides());
		 
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
			try{
				 d1 = Double.valueOf(pb.getSpectrumFractionSpread_lower_range_kDa());
			 }catch(Exception nfx){d1=null;}
			try{
				d2 = Double.valueOf(pb.getSpectrumFractionSpread_upper_range_kDa());
			 }catch(Exception nfx){d2=null;}
	     
			try{
				d3 = Double.valueOf(pb.getPeptideFractionSpread_lower_range_kDa());
			 }catch(Exception nfx){}
			try{
				d4 = Double.valueOf(pb.getPeptideFractionSpread_upper_range_kDa());
			 }catch(Exception nfx){}
	     
			 
			 if(fractionNumber <= 0)
				 this.addItem(new Object[] {index,link, pi,pb.getOtherProteins(),pb.getDescription(),Double.valueOf(df.format(pb.getSequenceCoverage())),nonEnz,pb.getNumberValidatedPeptides(),pb.getNumberValidatedSpectra(),Double.valueOf(df.format(pb.getNsaf())),Double.valueOf(df.format(pb.getMw_kDa())),Double.valueOf(df.format(pb.getConfidence()))}, new Integer(index));	 
			 else
				 this.addItem(new Object[] {index,link, pi,pb.getOtherProteins(),pb.getDescription(),Double.valueOf(df.format(pb.getSequenceCoverage())),nonEnz,pb.getNumberValidatedPeptides(),pb.getNumberValidatedSpectra(),Double.valueOf(df.format(pb.getNsaf())),Double.valueOf(df.format(pb.getMw_kDa())),Double.valueOf(df.format(pb.getConfidence())),d1,d2,d3,d4}, new Integer(index));	 
				
			 index++;
			
		 }
		 this.setSortContainerPropertyId(Confidence);
		 this.setSortAscending(false);
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
			//  setColumnWidth(Confidence, 35);
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
				        } else if(propertyId.equals("Accession")) {
				            return "Accession";
				        } 
				        
				        
				        else if(propertyId.equals("SpectrumFractionSpread_lower_range_kDa")) {
				            return "Spectrum Lower Range (kDa)";
				        }
				        
				        else if(propertyId.equals("SpectrumFractionSpread_upper_range_kDa")) {
				            return "Spectrum Upper Range (kDa)";
				        } 
				        else if(propertyId.equals("PeptideFractionSpread_lower_range_kDa")) {
				            return "Peptide lowerrange (kDa)";
				        } 
				        else if(propertyId.equals("PeptideFractionSpread_upper_range_kDa")) {
				            return "Peptide Upper Range (kDa)";
				        } 
				        
				        
				        
				        
				        else if(propertyId.equals("Non Enzymatic_Peptides")) {
				            return "Non Enzymatic_Peptides";
				        } 
				        else if(propertyId.equals("Protein Inference")) {
				            return "Protein Inference";
				        }
				        else if(propertyId.equals("Other Protein(s)")) {
				            return "Other Protein(s)";
				        }
				        
					  else if(propertyId.equals("Description")) {
				            return "Description";
				        }else if(propertyId.equals("Sequence Coverage(%)")) {
				            return "Sequence Coverage(%)";
				        }else if(propertyId.equals("# Validated Peptides")) {
				            return "# Validated Peptides";
				        }else if(propertyId.equals("# Validated Spectra")) {
				            return "# Validated Spectra";
				        }else if(propertyId.equals("NSAF")) {
				            return "NSAF";
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
					 
				        return null;
				    }
				});
				 
	}

}
