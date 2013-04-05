package com.handlers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import com.model.ExperimentModel;
import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.PeptideBean;
import com.model.beans.ProteinBean;
import com.vaadin.Application;

public class ExperimentHandler implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private ExperimentModel em ;
	public ExperimentHandler(String url,String dbName,String driver,String userName, String password)
	{
		 em = new ExperimentModel(url,dbName,driver,userName,  password);
		
	}
	
	
	public boolean handelExperimentFile(File file,String MIMEType,int expId,String expName,String species,  String sampleType, String sampleProcessing, String instrumentType, String fragMode, String uploadedByName, String email, String publicationLink) throws IOException, SQLException
	{
		boolean test = false;
		test = em.handelExperiment(file, MIMEType, expId, expName, species,   sampleType,  sampleProcessing,  instrumentType,  fragMode,  uploadedByName, email, publicationLink);		
		return test;
	
	}
	
	public Map <Integer,ExperimentBean> getExperiments(Map<Integer, ExperimentBean> expList)
	{
		if(expList == null){
		 expList = em.getExperiments();
		}
		else{
			
			//perform check on updates if no new updates then do nothing(Future work)
			Map <Integer,ExperimentBean>  expList2 = em.getExperiments();
			for(int key :expList2.keySet())
			{
				if (expList.containsKey(key))
				{
					ExperimentBean exp = expList.get(key);
					if(exp.getProteinList() != null && exp.getProteinList().size()> 0){
						if(exp.getFractionsNumber()<expList2.get(key).getFractionsNumber()){
							exp.setFractionsNumber(expList2.get(key).getFractionsNumber());
							expList.put(key, exp);
						}
						
					}
					else if(exp.getPeptideList() != null && exp.getPeptideList().size()> 0)
						;
					else					
						expList.put(key, expList2.get(key));
				}
				else
				{
					expList.put(key, expList2.get(key));
				}
			}
		}
		return expList;
	}

	public Map<String,ProteinBean> getProteinsList(int expId, Map<Integer, ExperimentBean> expList)
	{
		Map<String,ProteinBean> protList = expList.get(expId).getProteinList();
		if(protList == null || protList.size()==0)		
			protList = em.getProteinsList(expId);		
		return protList;
	}
	public ExperimentBean getExperiment(int expId,String x) {
		// TODO Auto-generated method stub
		ExperimentBean exp= em.getExperiment(expId);
		return exp;
	}

	public Map<Integer, PeptideBean> getPeptidesList(int expId, Map<Integer, ExperimentBean> expList) {
		Map<Integer, PeptideBean> peptidesList = expList.get(expId).getPeptideList();
		if(peptidesList == null|| peptidesList.size()==0)				
			peptidesList = em.getPeptidesList(expId);
		return peptidesList;
	}

	public Map<Integer, PeptideBean> getPeptidesProtList( Map<Integer, PeptideBean> pepList, String accession) {
		Map<Integer, PeptideBean> peptidesProtList = new HashMap<Integer, PeptideBean>();
		for(PeptideBean pepb: pepList.values())
		{
			if(pepb.getProtein().equalsIgnoreCase(accession))
					peptidesProtList.put(pepb.getPeptideId(), pepb);
			
		}	
		return peptidesProtList;
	}
	public Map<Integer,FractionBean> getFractionsList(int expId, Map<Integer, ExperimentBean> expList)
	{
		Map<Integer, FractionBean> fractionsList = null;
		if(expList.containsKey(expId) && expList.get(expId).getFractionsList() != null )
		{
			//check if exp updated if not
			fractionsList = expList.get(expId ).getFractionsList();
		}else
			fractionsList = em.getFractionsList(expId);		
		
		return fractionsList;
		
	}
	public Map<Integer,ProteinBean> getProteinFractionList(String accession,Map<Integer, FractionBean> fractionsList,int expId)
	{
		Map<Integer,ProteinBean> protionFractList = new HashMap<Integer,ProteinBean>();
		if(fractionsList == null){
			fractionsList = em.getProteinFractionList(accession,expId);
		}
			for(FractionBean fb:fractionsList.values())
			{
				if(fb.getProteinList().containsKey(accession))
					protionFractList.put(fb.getFractionId(), fb.getProteinList().get(accession));
			}
		
		return protionFractList;
	}
	
	
	///v-2
	public Map<Integer,List<ProteinBean>> searchProteinByAccession(String searchArr,Map<Integer,ExperimentBean> expList)
	{
		Map<Integer,List<ProteinBean>> protExpList = new HashMap<Integer,List<ProteinBean>>();
		
		if(expList == null){
			return null;
		}
		else{
			for(ExperimentBean exp1:expList.values())
			{
				List<ProteinBean> protList = new ArrayList<ProteinBean>();
				if(exp1.getProteinList() != null && exp1.getProteinList().containsKey(searchArr))
				{					
					ProteinBean pb = exp1.getProteinList().get(searchArr);
					protList.add(pb);
				}
				if(exp1.getProteinList() != null)
				{
					for(ProteinBean pb : exp1.getProteinList().values())
					{
						if(pb.getOtherProteins().contains(searchArr))
							protList.add(pb);
					}
					
					
				}
				else
				{
					protList = em.searchProtein(searchArr,exp1.getExpId(),protList);
					
						
				}
				protExpList.put(exp1.getExpId(), protList);
			}
		}

		return protExpList;
		
	}

	public Map<Integer, PeptideBean> getPeptidesProtExpList(  Map<Integer, ExperimentBean> expList, String accession,int expId)
	{
		Map<Integer, PeptideBean> peptidesProtList = new HashMap<Integer, PeptideBean>();
		if(expList.get(expId).getPeptideList() != null && expList.get(expId).getPeptideList().size() > 0){
			for(PeptideBean pepb: expList.get(expId).getPeptideList().values())
			{
				if(pepb.getProtein().equalsIgnoreCase(accession))
					peptidesProtList.put(pepb.getPeptideId(), pepb);
				
			}
		}else
		{
			peptidesProtList.putAll(em.getPeptidesProtList(expId,accession));
		}
		
	return peptidesProtList;
	}

	public Map<Integer,Map<Integer, ProteinBean>> searchProteinByName(String protSearch,	Map<Integer, ExperimentBean> expList) {
		Map<Integer,Map<Integer, ProteinBean>> protExpFullList = new HashMap<Integer, Map<Integer,ProteinBean>>();
		if(expList == null){
			return null;
		}
		else{
			int index = 0;
			for(ExperimentBean exp1:expList.values())
			{
				Map<Integer,ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();
				
				if(exp1.getProteinList() != null)
				{
					for(ProteinBean pb : exp1.getProteinList().values()){
						if(pb.getDescription().contains(protSearch.toUpperCase()))
						{
							protExpList.put(exp1.getExpId(), pb);
							protExpFullList.put(index++, protExpList);
							protExpList = new HashMap<Integer, ProteinBean>();
							
						}
					}
				}
				else
				{
					List<ProteinBean> proteinsList = em.searchProteinByName(protSearch,exp1.getExpId());
					if(proteinsList != null)
					{
						for(ProteinBean pb : proteinsList){
							protExpList.put(exp1.getExpId(), pb);
							protExpFullList.put(index++, protExpList);
							protExpList = new HashMap<Integer, ProteinBean>();
							
						}
					}
						
				}
			}
		}

		return protExpFullList;
	}

	
	///v-2
	public Map<Integer, Map<Integer, ProteinBean>> searchProteinByPeptideSequence( String protSearch, Map<Integer, ExperimentBean> expList) {
		Map<Integer,Map<Integer, ProteinBean>> protExpFullList = new HashMap<Integer, Map<Integer,ProteinBean>>();
		if(expList == null){
			return null;
		}
		else
		{
			int index = 0;
			for(ExperimentBean exp1:expList.values())
			{
				Map<Integer,ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();
				
				if(exp1.getPeptideList() != null && exp1.getPeptideList().size() != 0)
				{
					for(PeptideBean pepb : exp1.getPeptideList().values()){
						if(pepb.getSequence().equalsIgnoreCase(protSearch))
						{
							String accession = pepb.getProtein();
							List<ProteinBean> pbList = em.searchProtein(accession, exp1.getExpId(),null);
							if(pbList.size()>0)								
								protExpList.put(exp1.getExpId(),pbList.get(0));
							protExpFullList.put(index++, protExpList);
							protExpList = new HashMap<Integer, ProteinBean>();
							
						}
					}
				}
				else
				{
					List<ProteinBean> proteinsList = em.searchProteinByPeptideSequence(protSearch,exp1.getExpId());
					
					if(proteinsList != null)
					{
						for(ProteinBean pb : proteinsList){
							protExpList.put(exp1.getExpId(), pb);
							protExpFullList.put(index++, protExpList);
							protExpList = new HashMap<Integer, ProteinBean>();
							
						}
					}
						
				}
			}
		}

		return protExpFullList;
	}
	
	public void FarwardUrl(HttpServletResponse resp,String id) throws IOException
	{
		
		 if (resp != null)
        {
			 if(id.equalsIgnoreCase(""))
				 resp.sendRedirect("http://facebook.com");
			 else if(id.equals(""))
				 resp.sendRedirect("http://facebook.com");
			 else
				 resp.sendRedirect("http://facebook.com");
        }
	}
}
