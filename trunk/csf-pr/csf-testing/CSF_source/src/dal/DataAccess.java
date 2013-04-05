package dal;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.PeptideBean;
import com.model.beans.ProteinBean;

public class DataAccess implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7011020617952045934L;
	private DataBase db;
	public DataAccess(String url,String dbName,String driver,String userName, String password)
	{
		db = new DataBase(url,dbName,driver, userName, password);
	}

	public boolean createTable()
	{
		
		 boolean test = db.createTables();
		return test;
		
		
	}
	public boolean setProteinFile(ExperimentBean exp)
	{
		ExperimentBean tempExp  = db.readyExper(exp.getExpId());//confirm that the experiment is new
		if(tempExp.getReady() == 0){
			boolean test = false;
			test =  db.setProteinFile(exp);
			return test;
		}
			else
				return false;
	}
	public boolean updateProteinFile(ExperimentBean exp)
	{
		boolean test = false;
		ExperimentBean tempExp = db.readyExper(exp.getExpId());//check the previous uploaded file
		if(tempExp.getReady()==1 && tempExp.getFractionsNumber()>0)//we need to update ready number to 2 -- previous file was protein fraction file
		{
			tempExp.setReady(2);
			tempExp.setProteinsNumber(exp.getProteinsNumber());
			test = db.updateExperiment(tempExp);			
		}		
		for(ProteinBean pb: exp.getProteinList().values()){
			test = db.checkProteinExisting(exp.getExpId(), pb.getAccession());
			if(test)//existing protein
				test = db.updateProtein(pb,exp.getExpId());
			else
				test = db.addProtine(pb,exp.getExpId());
		}
		return test;
		
		
	}
	public boolean setProteinFractionFile(ExperimentBean exp)
	{
		boolean test = false;
		ExperimentBean tempExp  = db.readyExper(exp.getExpId());//confirm that the exp is new
		if(tempExp.getReady() == 0){
			test =  db.setProteinFractionFile(exp);
			return test;
		}else 
			return false;
	}
	public boolean updateProteinFractionFile(ExperimentBean exp)
	{
		boolean test = false;
		ExperimentBean tempExp = db.readyExper(exp.getExpId());//check the previous uploaded file
		
		if(tempExp.getReady()==1 && tempExp.getFractionsNumber()==0)//we need to update ready number to 2 -- previous file was protein fraction file
		{
			tempExp.setFractionsNumber(exp.getFractionsNumber());
			tempExp.setReady(2);
			test = db.updateExperiment(tempExp);	//update exp table
			for(FractionBean fb:exp.getFractionsList().values())
			{
				 db.insertFraction(fb, exp.getExpId());// update fraction-exp table and fraction table
				for(ProteinBean pb:fb.getProteinList().values())
				{
					 db.insertProtDescription(pb.getAccession(),pb.getDescription());//update protein table
					
				}
				
			}
			
			
		}
		else
		{
			tempExp.setFractionsNumber(exp.getFractionsNumber());
			tempExp.setReady(2);
			test = db.updateExperiment(tempExp);	//update exp table
			for(FractionBean fb:exp.getFractionsList().values())
			{
				 db.updateFractions(fb, exp.getExpId());// update fraction-exp table and fraction table
				for(ProteinBean pb:fb.getProteinList().values())
				{
					 db.insertProtDescription(pb.getAccession(),pb.getDescription());//update protein table
					
				}
				
			}
			
		}
		
		return test;
		
	}
	public boolean removeExperiment(int expId)
	{
		boolean test = db.removeExperiment(expId);
		return test;
	}
	

	
	public Map<Integer,ExperimentBean> getExperiments()//get experiments list
	{
		Map<Integer,ExperimentBean> expList = db.getExperiments();
		return expList;
	}
	public ExperimentBean getExperiment(int expId)
	{
		ExperimentBean exp = db.getExperiment(expId);
		return exp;
	}
	public boolean setPeptideFile(ExperimentBean exp) {
		boolean test = db.setPeptideFile(exp);
		return test;
	}
	public boolean updatePeptideFile(ExperimentBean exp) {
		boolean test = false;
		ExperimentBean tempExp = db.readyExper(exp.getExpId());//check the previous uploaded file
		if(tempExp.getPeptidesInclude() == 0)//we need to update peptide file number to 1 
		{
			tempExp.setPeptidesInclude(1);
			tempExp.setPeptidesNumber(exp.getPeptidesNumber());
			test = db.updateExperiment(tempExp);
			for(PeptideBean pepb: exp.getPeptideList().values()){
				db.insertPeptide(-1, pepb,tempExp.getExpId());
			}
		}	
		else
		{
			tempExp.setPeptidesNumber(exp.getPeptidesNumber());
			test = db.updateExperiment(tempExp);
			for(PeptideBean pepb: exp.getPeptideList().values()){
				test= db.checkPeptideExisting(pepb.getSequence());
					if(test)
					{
						test = db.updatePeptide(pepb);
						
					}
					else
					{
						 db.insertPeptide(-1, pepb,tempExp.getExpId());
					}
					
			}
			
			
		}
		
		
		return test;
	}
	public Map<String, ProteinBean> getProteinsList(int expId) {
		 Map<String,ProteinBean> proteinsList = db.getExpProteinsList(expId);
		return proteinsList;
	}
	public Map<Integer, PeptideBean> getPeptidesList(int expId) {
		Map<Integer, PeptideBean> peptidesList = db.getExpPeptides(expId);
		return peptidesList;
	}
	public Map<Integer,FractionBean> getFractionsList( int expId)
	{
		Map<Integer,FractionBean> fractionsList = db.getFractionsList(expId);
		return fractionsList;
		
	}
	
	
	///new v-2
	public List<ProteinBean> searchProtein(String accession,int expId, List<ProteinBean> protList) {
		
		ProteinBean pb = db.searchProtein(accession, expId);
		if(pb!= null && protList != null)
			protList.add(pb);
		if(protList != null)
			protList = db.searchOtherProteins(accession,expId, protList);
		else{
			protList = new ArrayList<ProteinBean>();
			protList.add(pb);
			
		}
		return protList;
	}
	public Map<Integer, PeptideBean> getPeptidesProtList(int expId,
			String accession) {
		Map<Integer, PeptideBean> peptidesProtList = db.getPeptidesProtList(expId, accession);
		return peptidesProtList;
	}
	public Map<Integer, FractionBean> getProteinFractionList(String accession,int expId) {
		Map<Integer, FractionBean> protionFractList = db.getProteinFractionList(accession,expId);
		return protionFractList;
	}
	public List<ProteinBean> searchProteinByName(String protSearch, int expId) {
		List<ProteinBean> proteinsList = db.searchProteinByName(protSearch, expId);
		return proteinsList;
	}
	public List<ProteinBean> searchProteinByPeptideSequence( String protSearch,			int expId) {
		
		List<ProteinBean> proteinsList = db.searchProteinByPeptideSequence(protSearch, expId);
			return proteinsList;
	}
}
