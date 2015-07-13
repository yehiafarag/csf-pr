/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quantcsf;

import com.quantcsf.beans.QuantDatasetObject;
import com.quantcsf.beans.QuantProtein;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class QuantDataHandler {
    
    
    private Map<String,QuantDatasetObject> authormap = new HashMap<String, QuantDatasetObject>();
    public QuantDataHandler(){
    
       QuantDatasetObject qa7 = new QuantDatasetObject();
        qa7.setYear(2011);
        qa7.setFilesNumber(10);
         qa7.setDiseaseGroups("Disease Group I");
        qa7.setAuthor("Li, Yun, et al.");
        authormap.put("21445879", qa7);
        
          QuantDatasetObject qa8 = new QuantDatasetObject();
        qa8.setYear(2014);
        qa8.setFilesNumber(10);
         qa8.setDiseaseGroups("Disease Group I");
        qa8.setAuthor("Liguori, Maria, et al.");
        authormap.put("25098164", qa8);
        
         QuantDatasetObject qa9 = new QuantDatasetObject();
        qa9.setYear(2010);
        qa9.setFilesNumber(10);
         qa9.setDiseaseGroups("Disease Group I");
        qa9.setAuthor("Ottervald, Jan, et al");
        authormap.put("20093204", qa9);
        
        
        
        QuantDatasetObject qa1 = new QuantDatasetObject();
        qa1.setYear(2010);
        qa1.setFilesNumber(10);
         qa1.setDiseaseGroups("Disease Group I");
        qa1.setAuthor("Comabella, Manuel, et al.");
        authormap.put("20237129", qa1);
        
         QuantDatasetObject qa2 = new QuantDatasetObject();
        qa2.setYear(2010);
        qa2.setAuthor("Harris, Violaine K., et al.");
        qa2.setFilesNumber(15);
        qa2.setDiseaseGroups("Disease Group II");
        authormap.put("20600910", qa2);
        
         
        QuantDatasetObject qa3 = new QuantDatasetObject();
        qa3.setYear(2012);
        qa3.setFilesNumber(13);
        qa3.setDiseaseGroups("Disease Group II");
        qa3.setAuthor("Dhaunchak, Ajit Singh, et al.");
        authormap.put("22473675", qa3);
        
         QuantDatasetObject qa4 = new QuantDatasetObject();
        qa4.setYear(2012);
        qa4.setFilesNumber(14);
        qa4.setDiseaseGroups("Disease Group I");
        qa4.setAuthor("Jia, Yan et al.");
        authormap.put("22846148", qa4);
        
         QuantDatasetObject qa5 = new QuantDatasetObject();
        qa5.setYear(2013);
        qa5.setFilesNumber(5);
        qa5.setDiseaseGroups("Disease Group III");
        qa5.setAuthor("Kroksveen, Ann C., et al.");
        authormap.put("23059536", qa5);
        
         QuantDatasetObject qa6 = new QuantDatasetObject();
        qa6.setYear(2012);
        qa6.setFilesNumber(16);
        qa6.setAuthor("Kroksveen, Ann C., et al.");  
        qa6.setDiseaseGroups("Disease Group II");
        authormap.put("23278663", qa6);
    }
    
    
    public List<QuantProtein> readCSVQuantFile(String path) {
        int counter = 0;
        File dataFile = new File(path);
        List<QuantProtein> QuantProtList = new ArrayList<QuantProtein>();

        try {

            FileReader fr = new FileReader(dataFile);
            BufferedReader bufRdr = new BufferedReader(fr);
            String header = bufRdr.readLine();
            String[] headerArr = header.split(",");
            int index = 1;           

            int row = 1;
            String line;
            while ((line = bufRdr.readLine()) != null && row < 1000000000) {                
                index = 0;
                QuantProtein qProt = new QuantProtein();
                String[] rowArr = line.split(",");
                
                String[] updatedRowArr = new String[headerArr.length];
                if (rowArr.length < headerArr.length) {
                    System.arraycopy(rowArr, 0, updatedRowArr, 0, rowArr.length);
                } else {
                    updatedRowArr = rowArr;
                }
                qProt.setPumedID(updatedRowArr[index++].trim().toUpperCase());
                qProt.setAuthor(authormap.get(qProt.getPumedID()).getAuthor());
                qProt.setDiseaseGroups(authormap.get(qProt.getPumedID()).getDiseaseGroups());
                qProt.setYear(authormap.get(qProt.getPumedID()).getYear());
                qProt.setFilesNum(authormap.get(qProt.getPumedID()).getFilesNumber());
                
                
                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setQuantifiedProteinsNumber(Integer.valueOf(updatedRowArr[index]));
                } else {
                    qProt.setQuantifiedProteinsNumber(-1);    
                }
                index++;
                
                
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setUniprotAccession("Not Available");
                    index++;
                } else {
                    qProt.setUniprotAccession(updatedRowArr[index++]);
                }
                
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setUniprotProteinName("Not Available");
                    index++;
                } else {
                    qProt.setUniprotProteinName(updatedRowArr[index++]);
                }
                
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPublicationAccNumber("Not Available");
                    index++;
                } else {
                    qProt.setPublicationAccNumber(updatedRowArr[index++]);
                }
                
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPublicationProteinName("Not Available");
                    index++;
                } else {
                    qProt.setPublicationProteinName(updatedRowArr[index++]);
                }
                
                

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setRawDataAvailable("Raw Data Not Available");

                } else {
                    if (updatedRowArr[index].equalsIgnoreCase("Yes")) {
                        qProt.setRawDataAvailable("Raw Data Available");
                    } else {
                        qProt.setRawDataAvailable("Raw Data Not Available");
                    }

                }
                index++;

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    
                    qProt.setPeptideIdNumb(Integer.valueOf(updatedRowArr[index]));
                } else {
                    qProt.setPeptideIdNumb(-1);                   
                }
                index++;
                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setQuantifiedPeptidesNumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setQuantifiedPeptidesNumber(-1);
                    index++;
                }
                //fill peptides 
                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) { //peptide sequance 
                    qProt.setPeptideProt(true);
                } else {
                    qProt.setPeptideProt(false);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPeptideSequance("Not Available");
                    index++;
                } else {
                    qProt.setPeptideSequance(updatedRowArr[index++]);
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPeptideModification("Not Available");
                    index++;
                } else {
                    qProt.setPeptideModification(updatedRowArr[index++]);
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setModificationComment("Not Available");
                    index++;
                } else {
                    qProt.setModificationComment(updatedRowArr[index++]);
                }

                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setTypeOfStudy("Not Available");
                    index++;
                } else {
                    qProt.setTypeOfStudy(updatedRowArr[index++]);
                }
                
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setSampleType("Not Available");
                    index++;
                } else {
                    qProt.setSampleType(updatedRowArr[index++]);
                }


                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientsGroupINumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setPatientsGroupINumber(-1);
                    index++;
                }
                
                
                 if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientGroupI("Not Available");
                    index++;
                } else {
                    qProt.setPatientGroupI(updatedRowArr[index++]);
                }
                 
                   if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientSubGroupI("Not Available");
                    index++;
                } else {
                    qProt.setPatientSubGroupI(updatedRowArr[index++]);
                }
                   
                     if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientGrIComment("Not Available");
                    index++;
                } else {
                    qProt.setPatientGrIComment(updatedRowArr[index++]);
                }
                 
                 
                 
                 
                 
               

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientsGroupIINumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setPatientsGroupIINumber(-1);
                    index++;
                }
             if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientGroupII("Not Available");
                    index++;
                } else {
                    qProt.setPatientGroupII(updatedRowArr[index++].replace("Controls", "CONTROL"));
                }
                 
                   if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientSubGroupII("Not Available");
                    index++;
                } else {
                    qProt.setPatientSubGroupII(updatedRowArr[index++]);
                }
                   
                     if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setPatientGrIIComment("Not Available");
                    index++;
                } else {
                    qProt.setPatientGrIIComment(updatedRowArr[index++]);
                }
                 
                 

                     if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setSampleMatching("Not Available");
                    index++;
                } else {
                    qProt.setSampleMatching(updatedRowArr[index++]);
                }
                     
                     
                     if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setNormalizationStrategy("Not Available");
                    index++;
                } else {
                    qProt.setNormalizationStrategy(updatedRowArr[index++]);
                }
                     
                                     if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    try {
                        qProt.setFcPatientGroupIonPatientGroupII(Double.valueOf(updatedRowArr[index]));
                        if (qProt.getFcPatientGroupIonPatientGroupII() > 0) {
                            qProt.setStringFCValue("Increased");
                        } else {
                            qProt.setStringFCValue("Decreased");
                        }
                    } catch (NumberFormatException exp) {
                        qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
                        if (!updatedRowArr[index].trim().isEmpty()) {
                            qProt.setStringFCValue(updatedRowArr[index].trim());
                        } else {
                            qProt.setStringFCValue("Not Provided");
                        }
                    } finally {
                        index++;
                    }
                } else {
                    qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
                    qProt.setStringFCValue(updatedRowArr[index++]);
                }

                if (!updatedRowArr[index].trim().trim().equalsIgnoreCase("")) {

                    qProt = definePValue(qProt, updatedRowArr[index]);
                    index++;
                } else {
                    qProt.setpValue(-1000000000.0);
                    qProt.setStringPValue("Not Available");
                    qProt.setPvalueComment("Not Available");
                    index++;
                }

                if (!updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setRocAuc(Double.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setRocAuc(-1000000000.0);
                    index++;
                }
                
                
                
                
                 if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setTechnology("Not Available");
                    index++;
                } else {
                    qProt.setTechnology(updatedRowArr[index++]);
                }
                
                 if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setAnalyticalApproach("Not Available");
                    index++;
                } else {
                    qProt.setAnalyticalApproach(updatedRowArr[index++]);
                }
               
                  if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setEnzyme("Not Available");
                    index++;
                } else {
                    qProt.setEnzyme(updatedRowArr[index++]);
                }
                   if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setShotgunOrTargetedQquant("Not Available");
                    index++;
                } else {
                    qProt.setShotgunOrTargetedQquant(updatedRowArr[index++]);
                }
                    if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setQuantificationBasis("Not Available");
                    index++;
                } else {
                    qProt.setQuantificationBasis(updatedRowArr[index++]);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setQuantBasisComment("Not Available");
                    index++;
                } else {
                    qProt.setQuantBasisComment(updatedRowArr[index++]);
                }
                if (updatedRowArr[index] == null || updatedRowArr[index].trim().equalsIgnoreCase("")) {
                    qProt.setAdditionalComments("Not Available");
                    index++;
                } else {
                    qProt.setAdditionalComments(updatedRowArr[index++]);
                }

                if(qProt.isPeptideProt()){
                String pepKey = qProt.getPumedID()+"_"+qProt.getUniprotAccession()+"_"+qProt.getTypeOfStudy()+"_"+qProt.getAnalyticalApproach();
                qProt.setqPeptideKey(pepKey);                
                }else{
                qProt.setqPeptideKey(""); 
                
                }

                if ((!qProt.getPatientGroupI().equalsIgnoreCase("Not Available")) && qProt.getPatientSubGroupI().equalsIgnoreCase("Not Available")) {
                    qProt.setPatientGroupI(qProt.getPatientGroupI() + " (Undefined)");

                }
                if ((!qProt.getPatientGroupII().equalsIgnoreCase("Not Available")) && qProt.getPatientSubGroupII().equalsIgnoreCase("Not Available")) {
                    qProt.setPatientGroupII(qProt.getPatientGroupII() + " (Undefined)");
                }
               qProt.setIdentifiedProteinsNum(-1);                    
                QuantProtList.add(qProt);

            } //       
            bufRdr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ssleeping error " + ex.getMessage());
        }

        return QuantProtList;

    }
    
           private QuantProtein defineFoldChange(QuantProtein prot, String pValue) {
        try {
            if(pValue.contains(">")){
                prot.setStringPValue("Not Significant");
                prot.setPvalueComment("Threshold defined at "+pValue.trim().substring(1));
                prot.setpValue(-1000000000.0);    
            
            }else if(pValue.contains("<")){
             prot.setStringPValue("Significant");
                prot.setPvalueComment("Threshold defined at "+pValue.trim().substring(1));
                prot.setpValue(-1000000000.0);
            }
            else if(Double.valueOf(pValue)<0.05){
             prot.setStringPValue("Significant");
                prot.setPvalueComment("Threshold defined by CSF-Pr at 0.05");
                prot.setpValue(Double.valueOf(pValue));    
            }
             else if(Double.valueOf(pValue)>0.05){
             prot.setStringPValue("Not Significant");
                prot.setPvalueComment("Threshold defined by CSF-Pr at 0.05");
                prot.setpValue(Double.valueOf(pValue));    
            }            

        } catch (NumberFormatException exp) {
            prot.setpValue(-1000000000.0);
            prot.setStringPValue("Not Available");
            prot.setPvalueComment("Not Available");
            
        } 
//        System.out.println("values  "+prot.getpValue()+"  "+prot.getStringPValue()+"  "+prot.getPvalueComment());

        return prot;
    }
    
    
        private QuantProtein definePValue(QuantProtein prot, String pValue) {
        try {
            if(pValue.contains(">")){
                prot.setStringPValue("Not Significant");
                prot.setPvalueComment("Threshold defined at "+pValue.trim().substring(1));
                prot.setpValue(-1000000000.0);    
            
            }else if(pValue.contains("<")){
             prot.setStringPValue("Significant");
                prot.setPvalueComment("Threshold defined at "+pValue.trim().substring(1));
                prot.setpValue(-1000000000.0);
            }
            else if(Double.valueOf(pValue)<0.05){
             prot.setStringPValue("Significant");
                prot.setPvalueComment("Threshold defined by CSF-Pr at 0.05");
                prot.setpValue(Double.valueOf(pValue));    
            }
             else if(Double.valueOf(pValue)>0.05){
             prot.setStringPValue("Not Significant");
                prot.setPvalueComment("Threshold defined by CSF-Pr at 0.05");
                prot.setpValue(Double.valueOf(pValue));    
            }            

        } catch (NumberFormatException exp) {
            prot.setpValue(-1000000000.0);
            prot.setStringPValue("Not Available");
            prot.setPvalueComment("Not Available");
            
        } 
//        System.out.println("values  "+prot.getpValue()+"  "+prot.getStringPValue()+"  "+prot.getPvalueComment());

        return prot;
    }
        
        public Map<String,String> readSequanceFile(String path){
        
         File dataFile = new File(path);
         String lastKey="";
        Map<String,String> protSeqMap = new HashMap<String,String>();
        try {

            FileReader fr = new FileReader(dataFile);
            BufferedReader bufRdr = new BufferedReader(fr);
            String header = bufRdr.readLine();
            String[] headerArr = header.split("\t");
            System.out.println(header + "  headers "+headerArr[0]);         
            String line;
            while ((line = bufRdr.readLine()) != null) {
                String[] lineArr = line.split("\t");
                if (lineArr.length > 1) {
                    protSeqMap.put(lineArr[0].trim(), lineArr[1].trim());
                    lastKey= lineArr[0];
                }else{
                String str= protSeqMap.get(lastKey)+lineArr[0].trim();
                 protSeqMap.put(lastKey,str);
                    System.out.println("str");
                
                }

            }
        }catch(Exception exp){
        
        exp.printStackTrace();
        }
            System.out.println("size "+protSeqMap.size());
        return protSeqMap;
        }
        
        
        
    
}
