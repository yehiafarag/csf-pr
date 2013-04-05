package com.helperunits;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.PeptideBean;
import com.model.beans.ProteinBean;

public class FilesReader implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	FileValidator fv = new FileValidator();
	
	public ExperimentBean readTextFile(File file,String MIMEType,ExperimentBean exp) throws IOException, SQLException//method to extract data from proteins files to store them in database
	{			 
		FileReader fr = new FileReader(file);	 
		@SuppressWarnings("resource")
		BufferedReader bufRdr  = new BufferedReader(fr);
		String line = null;
		int row = 0;
		int fileType = 0;
		Map<Integer,FractionBean> fractionsList = new HashMap<Integer, FractionBean>();
		Map<Integer,PeptideBean> peptideList = null;
		line = bufRdr.readLine();
		String[] strArr = line.split("\t");
		fileType = fv.validateFile(strArr, MIMEType);//check if the file type and  file format 
		exp.setExpFile(fileType);
		Map<String,ProteinBean> proteinList = new HashMap<String,ProteinBean>();//use only in case of protein files
		if(fileType == -1)//wrong file 
			return null;
		else if(fileType == -2)//peptide file
		{
			peptideList = new HashMap<Integer,PeptideBean>();			
			exp.setExpFile(fileType);
		}
		else if(fileType == 0)//Protein file
		{
		}
		else
		{
			if(fileType > 0 ){
				exp.setFractionsNumber(fileType);//file type in case of protein fraction file return the number of fractions
				//create a number of fractions for the experiment
				for (int x = 0; x<fileType;x++)
				{
					FractionBean fb = new FractionBean();
					Map<String,ProteinBean> temProteinList = new HashMap<String,ProteinBean>();
					fb.setProteinList(temProteinList);
					fractionsList.put((x), fb);
				}
			}
		}
		int inedxId = 0;
		while((line = bufRdr.readLine()) != null && row < 1000)//loop to fill the protein beans and add it to fraction list
		{				
			strArr = line.split("\t");	
			ProteinBean prot  = null;
			if(fileType != -2){
				prot = new ProteinBean();
				prot.setAccession(strArr[0]);
				prot.setOtherProteins(strArr[1]);
				prot.setProteinInferenceClass(strArr[2]);
				prot.setDescription(strArr[3]);
			}
			
			if(fileType == -2)
			{
				PeptideBean pb = new PeptideBean();
				pb.setProtein(strArr[0]);
				pb.setOtherProteins(strArr[1]);
				pb.setPeptideProteins(strArr[2]);
				pb.setOtherProteinDescriptions(strArr[4]);
				pb.setPeptideProteinsDescriptions(strArr[5]);
				pb.setAaBefore(strArr[6]);
				pb.setSequence(strArr[7]);
				pb.setAaAfter(strArr[8]);
				pb.setPeptideStart((strArr[9]));
				pb.setPeptideEnd((strArr[10]));
				pb.setVariableModification(strArr[11]);
				pb.setLocationConfidence(strArr[12]);
				pb.setPrecursorCharges(strArr[13]);
				if(!strArr[14].equals(""))
					pb.setNumberOfValidatedSpectra(Integer.valueOf(strArr[14]));
				if(!strArr[15].equals(""))	
					pb.setScore(Double.valueOf(strArr[15]));
				if(!strArr[16].equals(""))
					pb.setConfidence(Double.valueOf(strArr[16]));
				pb.setPeptideId(inedxId);
				peptideList.put(pb.getPeptideId(),pb);
				inedxId++;
				
				
			}			
			else if(fileType == 0) //Protein file
			{				
				prot.setSequenceCoverage(Double.valueOf(strArr[4]));
				prot.setObservableCoverage(Double.valueOf(strArr[5]));
				prot.setConfidentPtmSites(strArr[6]);
				prot.setNumberConfident(strArr[7]);
				prot.setOtherPtmSites(strArr[8]);
				prot.setNumberOfOther(strArr[9]);
				prot.setNumberValidatedPeptides(Integer.valueOf(strArr[10]));
				prot.setNumberValidatedSpectra(Integer.valueOf(strArr[11]));
				prot.setEmPai(Double.valueOf(strArr[12]));
				prot.setNsaf(Double.valueOf(strArr[13]));
				prot.setMw_kDa(Double.valueOf(strArr[14]));
				prot.setScore(Double.valueOf(strArr[15]));
				prot.setConfidence(Double.valueOf(strArr[16]));
				prot.setStarred(Boolean.valueOf(strArr[17]));				
				//ProteinList.put(prot.getAccession(),prot);//store protein details in list			
				proteinList.put(prot.getAccession(),prot);
			}
			else   //Protein fraction file
			{
				ProteinBean tempProt = null;
				
				prot.setStarred(Boolean.valueOf(strArr[strArr.length-1]));
				for(int x=0 ;x<fileType;x++)
				{
					
					tempProt = new ProteinBean();
					tempProt.setAccession(prot.getAccession());
					tempProt.setOtherProteins(prot.getOtherProteins());
					tempProt.setProteinInferenceClass(prot.getProteinInferenceClass());
					tempProt.setDescription(prot.getDescription());
					tempProt.setStarred(prot.isStarred());
					try{
						tempProt.setNumberOfPeptidePerFraction(Integer.valueOf(strArr[(4+x)]));
						
					}catch(NumberFormatException e){
						double d = Double.valueOf(strArr[(4+x)]);
						tempProt.setNumberOfPeptidePerFraction((int) d);}
					
					try{tempProt.setNumberOfSpectraPerFraction(Integer.valueOf(strArr[(4+x+fileType)]));
					}catch(NumberFormatException e){
						double d = Double.valueOf(strArr[(4+x+fileType)]);
						tempProt.setNumberOfSpectraPerFraction((int) d);
						}
					tempProt.setAveragePrecursorIntensityPerFraction(Double.valueOf(strArr[(4+x+fileType+fileType)]));
					FractionBean temFb = fractionsList.get(x);
					Map<String,ProteinBean> temProteinList = temFb.getProteinList();
					temProteinList.put(tempProt.getAccession(), tempProt);
					temFb.setProteinList(temProteinList);
					fractionsList.put(x, temFb);
					
					
				}				
			}
			
		}
		if(peptideList != null && peptideList.size()>0){
			exp.setPeptideList(peptideList);
			exp.setPeptidesNumber(peptideList.size());
		}
		if(fractionsList.size() == 0){
			exp.setProteinList(proteinList);
			exp.setProteinsNumber(proteinList.size());
			
		}
		else{
			fractionsList = getFractionRange(fractionsList);//will be updated by dataset
			exp.setFractionsList(fractionsList);
		}
		return exp;
	}
	
	
	
	private Map<Integer, FractionBean> getFractionRange(Map<Integer, FractionBean> fractionsList)
	{
		Map<Integer, FractionBean> tempFractionsList = new HashMap<Integer, FractionBean>();
		for(int key :fractionsList.keySet())
		{
			FractionBean fraction=fractionsList.get(key);
			fraction.setMaxRange(0.0);
			fraction.setMaxRange(0.0);
			tempFractionsList.put(key, fraction);
			
		}
		return tempFractionsList;
	}
	

}
