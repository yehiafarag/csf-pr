/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepshack;

import com.pepshack.util.FilesReader;
import com.pepshack.util.ProgressDialog;
import com.pepshack.util.beans.ExperimentBean;
import com.pepshack.util.beans.PeptideBean;
import com.pepshack.util.beans.ProteinBean;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;

/**
 *
 * @author Yehia Mokhtar
 */
public class DataHandler {
	
	private CustomOutputGenerator outputGenerator;
	public ExperimentBean handelData(PSFileImporter importer,ExperimentBean exp,JLabel label)	{
//            ProgressDialog progressDialog = gui.getProgressDialog();
            label.setText("Start Proteins processing...");
	    outputGenerator = new CustomOutputGenerator(importer);
            exp.setProteinList(this.getProteins());
            System.out.println(" exp.setProteinList "+ exp.getProteinList().size());
            exp.setProteinsNumber(exp.getProteinList().size());
            label.setText("Start Peptides processing...");
            exp.setPeptideList(this.getPeptides());
            exp.setPeptidesNumber(this.getValidatedPeptideNuumber(exp.getPeptideList()));
            if(! exp.getPeptideList().isEmpty())
                exp.setPeptidesInclude(1);
            else
                exp.setPeptidesInclude(0);
            label.setText("Start Fractions processing...");
            exp = this.getFractionList(exp);
            if(!exp.getFractionsList().isEmpty() ||exp.getFractionsList().size()  != 1)
                exp.setFractionsNumber(exp.getFractionsList().size());
            else
                exp.setFractionsNumber(0);
            
            importer.clearData(true);
            return exp;
           }
	/*
	 * (ArrayList<String> aProteinKeys, boolean aIndexes, boolean aOnlyValidated, boolean aMainAccession, boolean aOtherAccessions, boolean aPiDetails,
            boolean aDescription, boolean aNPeptides, boolean aEmPAI, boolean aSequenceCoverage, boolean aPtmSummary, boolean aNSpectra, boolean aNsaf,
            boolean aScore, boolean aConfidence, boolean aMW, boolean aIncludeHeader, boolean aOnlyStarred, boolean aShowStar, boolean aIncludeHidden, boolean aMaximalProteinSet,
            boolean aShowNonEnzymaticPeptidesColumn)
	 */
	
	private Map<String,ProteinBean> getProteins()
	
	{
             Map<String,ProteinBean>  proteinList  = outputGenerator.getProteinsOutput();
            
             return proteinList;
         
	}
        
        private Map<Integer, PeptideBean> getPeptides()
        {
              Map<Integer, PeptideBean>  peptideList  = outputGenerator.getPeptidesOutput();      
              System.out.println("peptide lis is exsist "+peptideList.size());
              return peptideList;
        
        }
        private ExperimentBean getFractionList(ExperimentBean exp)
        {
            return outputGenerator.getFractionsOutput(exp);
        
        }
        
        private int getValidatedPeptideNuumber(Map<Integer, PeptideBean> pepList)
        {
            int number= 0;
            for(PeptideBean pepb:pepList.values())
            {
                if(pepb.getValidated()==1.0)
                    number++;
            }
            return number;
        
        }
        
       public ExperimentBean addGlicoPep(File glycopeptide,ExperimentBean exp)
       {
           if(!glycopeptide.exists())
               ;
           else{
               FilesReader reader = new FilesReader();
               Map<String,PeptideBean> pepList =  reader.readGlycoFile(glycopeptide);
               exp = updatePeptideList(pepList,exp);
           }
       
           return exp;
       }
       
       private ExperimentBean updatePeptideList(Map<String,PeptideBean> pepList,ExperimentBean exp)
       {
           Map<Integer,PeptideBean> updatedList = new HashMap<Integer, PeptideBean>();
           for(int index:exp.getPeptideList().keySet())
           {
               PeptideBean pb = exp.getPeptideList().get(index);
               String key = "["+pb.getProtein()+"]["+pb.getSequenceTagged()+"]";
               if(pepList.containsKey(key))
               {
                   System.out.println("Updating is working");
                   PeptideBean temPb = pepList.get(key);
                   if(temPb.getGlycopatternPositions()!= null)
                        pb.setGlycopatternPositions(temPb.getGlycopatternPositions());
                   if(temPb.isDeamidationAndGlycopattern() != null)
                        pb.setDeamidationAndGlycopattern(temPb.isDeamidationAndGlycopattern());
                   
               }
               updatedList.put(index, pb);
           
           }
           exp.setPeptideList(updatedList);
           return exp;
       
       
       }
	
	

}
