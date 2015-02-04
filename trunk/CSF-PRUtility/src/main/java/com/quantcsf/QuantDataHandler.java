/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quantcsf;

import com.quantcsf.beans.QuantProtein;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Yehia Farag
 */
public class QuantDataHandler {
    
    
    public List<QuantProtein> readCSVQuantFile(String path) {
        File dataFile = new File(path);
        List<QuantProtein> QuantProtList = new ArrayList<QuantProtein>();

        try {

            FileReader fr = new FileReader(dataFile);
            BufferedReader bufRdr = new BufferedReader(fr);
            String header = bufRdr.readLine();
            String[] headerArr = header.split(",");
            int index = 1;
            for (String str : headerArr) {
                System.out.println(index++ + " " + str);
            }

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
                qProt.setPumedID(updatedRowArr[index++]);
                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setQuantifiedProteinsNumber(Integer.valueOf(updatedRowArr[index]));
                } else {
                    qProt.setQuantifiedProteinsNumber(-1000000000);    
                }
                index++;
                qProt.setUniprotAccession(updatedRowArr[index++]);
                qProt.setUniprotProteinName(updatedRowArr[index++]);
                qProt.setPublicationAccNumber(updatedRowArr[index++]);
                qProt.setPublicationProteinName(updatedRowArr[index++]);
                qProt.setRawDataAvailable(updatedRowArr[index++]);
                
                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    
                    qProt.setPeptideIdNumb(Integer.valueOf(updatedRowArr[index]));
                } else {
                    qProt.setPeptideIdNumb(-1000000000);                   
                }
                index++;
                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setQuantifiedPeptidesNumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setQuantifiedPeptidesNumber(-1000000000);
                    index++;
                }
                //fill peptides 
                if (!updatedRowArr[index].equalsIgnoreCase("")) { //peptide sequance 
                    qProt.setPeptideProt(true);
                } else {
                    qProt.setPeptideProt(false);
                }
                qProt.setPeptideSequance(updatedRowArr[index++]);
                qProt.setPeptideModification(updatedRowArr[index++]);
                qProt.setModificationComment(updatedRowArr[index++]);
                qProt.setTypeOfStudy(updatedRowArr[index++]);
                qProt.setSampleType(updatedRowArr[index++]);

                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setPatientsGroupINumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setPatientsGroupINumber(-1000000000);
                    index++;
                }
                qProt.setPatientGroupI(updatedRowArr[index++]);
                qProt.setPatientSubGroupI(updatedRowArr[index++]);
                qProt.setPatientGrIComment(updatedRowArr[index++]);

                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setPatientsGroupIINumber(Integer.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setPatientsGroupIINumber(-1000000000);
                    index++;
                }
                qProt.setPatientGroupII(updatedRowArr[index++]);
                qProt.setPatientSubGroupII(updatedRowArr[index++]);
                qProt.setPatientGrIIComment(updatedRowArr[index++]);

                qProt.setSampleMatching(updatedRowArr[index++]);
                qProt.setNormalizationStrategy(updatedRowArr[index++]);
                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    try{
                    qProt.setFcPatientGroupIonPatientGroupII(Double.valueOf(updatedRowArr[index]));
                    if(qProt.getFcPatientGroupIonPatientGroupII() > 0)
                        qProt.setStringFCValue("Increased"); 
                    else{
                        qProt.setStringFCValue("Decreased"); 
                    }
                    }catch(NumberFormatException exp){
                     qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
                     qProt.setStringFCValue(updatedRowArr[index]);                    
                    }finally{                    
                    index++;
                    }
                } else {
                    qProt.setFcPatientGroupIonPatientGroupII(-1000000000.0);
                     qProt.setStringFCValue(updatedRowArr[index++]); 
                }
                
                
                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    try{
                    qProt.setpValue(Double.valueOf(updatedRowArr[index]));
                  
                   
                    }catch(NumberFormatException exp){
                     qProt.setpValue(-1000000000.0);                  
                    }
                    finally{  
                        qProt.setStringPValue(updatedRowArr[index]); 
                        index++;
                    }
                } else {
                    qProt.setpValue(-1000000000.0);
                     qProt.setStringPValue(updatedRowArr[index++]); 
                }
                
              
                if (!updatedRowArr[index].equalsIgnoreCase("")) {
                    qProt.setRocAuc(Double.valueOf(updatedRowArr[index++]));
                } else {
                    qProt.setRocAuc(-1000000000.0);
                    index++;
                }
                qProt.setTechnology(updatedRowArr[index++]);
                qProt.setAnalyticalApproach(updatedRowArr[index++]);
                qProt.setEnzyme(updatedRowArr[index++]);
                qProt.setShotgunOrTargetedQquant(updatedRowArr[index++]);
                qProt.setQuantificationBasis(updatedRowArr[index++]);
                qProt.setQuantBasisComment(updatedRowArr[index++]);
                qProt.setAdditionalComments(updatedRowArr[index++]);
                
                if(qProt.isPeptideProt()){
                String pepKey = qProt.getPumedID()+"_"+qProt.getUniprotAccession()+"_"+qProt.getTypeOfStudy()+"_"+qProt.getAnalyticalApproach();
                qProt.setqPeptideKey(pepKey);                
                }else{
                qProt.setqPeptideKey(""); 
                
                }
                QuantProtList.add(qProt);

            } //       
            System.out.println("index is " + index);
            bufRdr.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("ssleeping error " + ex.getMessage());
        }

        return QuantProtList;

    }
}
