/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepshack;

import com.model.beans.ExperimentBean;
import com.model.beans.PeptideBean;
import com.model.beans.ProteinBean;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yehia Mokhtar
 */
public class DataHandler {
	
	CustomOutputGenerator outputGenerator;
	public void handelData(PSFileImporter importer)
	{
            ExperimentBean exp = new ExperimentBean();
	    outputGenerator = new CustomOutputGenerator(importer);
		exp.setProteinList(this.getProteins());
                exp.setPeptideList(this.getPeptides());
                exp = this.getFractionList(exp);
                System.out.println(exp.getProteinList().size()+"  -- "+exp.getPeptideList().size()+" -- "+exp.getFractionsList().size());
                System.exit(0);
	}
	/*
	 * (ArrayList<String> aProteinKeys, boolean aIndexes, boolean aOnlyValidated, boolean aMainAccession, boolean aOtherAccessions, boolean aPiDetails,
            boolean aDescription, boolean aNPeptides, boolean aEmPAI, boolean aSequenceCoverage, boolean aPtmSummary, boolean aNSpectra, boolean aNsaf,
            boolean aScore, boolean aConfidence, boolean aMW, boolean aIncludeHeader, boolean aOnlyStarred, boolean aShowStar, boolean aIncludeHidden, boolean aMaximalProteinSet,
            boolean aShowNonEnzymaticPeptidesColumn)
	 */
	
	private Map<String,ProteinBean> getProteins()
	
	{
             Map<String,ProteinBean>  proteinList  = outputGenerator.getProteinsOutput( null);
             System.out.println("p list size "+proteinList.size());
             return proteinList;
         
	}
        
        private Map<Integer, PeptideBean> getPeptides()
        {
              Map<Integer, PeptideBean>  peptideList  = outputGenerator.getPeptidesOutput();
              System.out.println("pep 3 list size "+peptideList.size());
              return peptideList;
        
        }
        private ExperimentBean getFractionList(ExperimentBean exp)
        {
            return outputGenerator.getFractionsOutput(exp);
        
        }
	
	

}
