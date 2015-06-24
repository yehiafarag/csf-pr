package probe.com.model.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import probe.com.model.beans.IdentificationDataset;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.model.beans.QuantProtein;
import probe.com.model.beans.StandardProteinBean;

public class FilesReader implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private FileValidator fv = new FileValidator();

    @SuppressWarnings("resource")
    public IdentificationDataset readTextFile(File file, String MIMEType, IdentificationDataset exp) throws IOException, SQLException//method to extract data from proteins files to store them in database
    {
        //if excel file
        //else text file
        int fileType = 0;
        String[] strArr = null;
        String[] lineArr = null;
        Map<Integer, FractionBean> fractionRanges = null;
        BufferedReader bufRdr = null;
        String line = null;
        int row = 0;

        if (MIMEType.equalsIgnoreCase("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") || MIMEType.equalsIgnoreCase("application/octet-stream")) {
            RangeReader msReader = new RangeReader();
            try {
                FileInputStream myInput = new FileInputStream(file);
                lineArr = msReader.readRangeFile(myInput);
                if (lineArr[0].contains("Plot Gel")) {
                    strArr = lineArr[1].split("\t");
                } else {
                    strArr = lineArr[0].split("\t");
                }

            } catch (Exception e) {
                System.err.println(e.getLocalizedMessage());
            }
        } else {
            FileReader fr = new FileReader(file);
            bufRdr = new BufferedReader(fr);
            line = bufRdr.readLine();
            if (MIMEType.equals("text/plain")) {
                strArr = line.split("\t");
            }
            
            System.out.println("str is "+line+"   ");
            for(int x=0;x<strArr.length;x++)                
                 System.out.println("index is "+x+"   value is "+strArr[x]);

        }

        Map<Integer, FractionBean> fractionsList = new HashMap<Integer, FractionBean>();
        Map<Integer, PeptideBean> peptideList = null;
        Map<String, PeptideBean> gPeptideList = new HashMap<String, PeptideBean>();      
            
        fileType = fv.validateFile(strArr, MIMEType);//check if the file type and  file format 
        exp.setExpFile(fileType);
        Map<String, IdentificationProteinBean> proteinList = new HashMap<String, IdentificationProteinBean>();//use only in case of protein files
        if (fileType == -1)//wrong file 
        {
            return null;
        }else if(fileType == -7)//glyco file
        {
             PeptideBean pb = null;
            try {
                while ((line = bufRdr.readLine()) != null && row < 1000) {
                    
                    pb = new PeptideBean();
                    strArr = line.split("\t");
                      if (strArr.length > 24 && strArr[24] != null && !strArr[24].equals("")) {
                            pb.setDeamidationAndGlycopattern(Boolean.valueOf(strArr[24]));
                       } else {
                            pb.setDeamidationAndGlycopattern(false);
                        }
                       if (strArr.length > 25 && strArr[25] != null && !strArr[25].equals("")) {
                            pb.setGlycopatternPositions((strArr[25]));
                        } else {
                            pb.setGlycopatternPositions("");
                    }
//                    
                        pb.setLikelyNotGlycosite(false);                    
                    String key = "[" + strArr[0].trim() + "][" + strArr[9].trim() + "]";
                    gPeptideList.put(key, pb);
                }
                
                
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getLocalizedMessage());
            }
            exp.setgPeptideList(gPeptideList);
            return exp;
        
        
        }
        
        else if (fileType == -2)//peptide file
        {
            peptideList = new HashMap<Integer, PeptideBean>();
            exp.setExpFile(fileType);
        } else if (fileType == -100)//Standard plot file
        {
            try{
            List<StandardProteinBean> standerdPlotProt = new ArrayList<StandardProteinBean>();
            for (int x = 2; x < lineArr.length; x++) {
                String str = lineArr[x].trim();
                if (str != null && (!str.equals(""))) {
                    StandardProteinBean spb = new StandardProteinBean();
                    spb.setMW_kDa(Double.valueOf(str.trim().split("\t")[0]));
                    spb.setName(str.split("\t")[1]);
                    double d = (Double.valueOf(str.split("\t")[2]));
                    spb.setLowerFraction((int) d);
                    d = (Double.valueOf(str.split("\t")[3]));
                    spb.setUpperFraction((int) d);
                    spb.setColor(str.split("\t")[4]);
                    standerdPlotProt.add(spb);
                }
            }  exp.setStanderdPlotProt(standerdPlotProt);
            }catch(Exception e){e.printStackTrace();}
          
            return exp;
        } else if (fileType == 0)//Protein file
        {
        } 
        else if (fileType == -5)//fraction range file
        {
            fractionRanges = new TreeMap<Integer, FractionBean>();
            for (int x = 1; x < lineArr.length; x++) {
                String str = lineArr[x];
                FractionBean fb = new FractionBean();
//                fb.setMinRange(Double.valueOf(str.split("\t")[1]));
//                fb.setMaxRange(Double.valueOf(str.split("\t")[2]));

                double keyDuble = Double.parseDouble(str.split("\t")[0]);
                int key = (int) keyDuble;
                fb.setFractionIndex(key);
                fractionRanges.put(key, fb);
            }
//            exp.setFractionsList(fractionRanges);



            return exp;
        } else {
            if (fileType > 0) {
                exp.setFractionsNumber(fileType);//file type in case of protein fraction file return the number of fractions
                //create a number of fractions for the experiment
                for (int x = 0; x < fileType; x++) {
                    FractionBean fb = new FractionBean();
                    Map<String, IdentificationProteinBean> temProteinList = new HashMap<String, IdentificationProteinBean>();
                    fb.setProteinList(temProteinList);
                    fb.setFractionIndex(x + 1);
                    fractionsList.put((x), fb);
                }
            }
        }
        int inedxId = 0;
        while (fileType != -5 && fileType != -7 && fileType != -100 && (line = bufRdr.readLine()) != null && row < 1000)//loop to fill the protein beans and add it to fraction list
        {
           

            strArr = line.split("\t");


            IdentificationProteinBean prot = null;
            if (fileType != -2) {
                prot = new IdentificationProteinBean();
                prot.setAccession(strArr[0]);
                prot.setOtherProteins(strArr[1]);
                prot.setProteinInferenceClass(strArr[2]);
                prot.setDescription(strArr[3]);
            }

            if (fileType == -2) {


                PeptideBean pb = new PeptideBean();
                pb.setProtein(strArr[0]);
                pb.setOtherProteins(strArr[1]);

                pb.setPeptideProteins(strArr[2]);
                //set prot desc 3
                pb.setOtherProteinDescriptions(strArr[4]);

                pb.setPeptideProteinsDescriptions(strArr[5]);
                pb.setProteinInference(strArr[6].toUpperCase());


                pb.setAaBefore(strArr[7]);
                pb.setSequence(strArr[8]);

                pb.setSequenceTagged(strArr[9].toUpperCase());

                pb.setAaAfter(strArr[10]);

                pb.setEnzymatic(Boolean.valueOf(strArr[11].toUpperCase()));

                pb.setPeptideStart((strArr[12]));
                pb.setPeptideEnd((strArr[13]));
                pb.setFixedModification(strArr[14]);
                pb.setVariableModification(strArr[15]);
                pb.setLocationConfidence(strArr[16]);
                pb.setPrecursorCharges(strArr[17]);
                if (!strArr[18].equals("")) {
                    pb.setNumberOfValidatedSpectra(Integer.valueOf(strArr[18]));
                }
                if (!strArr[19].equals("")) {
                    pb.setScore(Double.valueOf(strArr[19]));
                }
                if (!strArr[20].equals("")) {
                    try {
                        pb.setConfidence(Double.valueOf(strArr[20]));
                    } catch (NumberFormatException nfe) {
                        pb.setConfidence(Double.valueOf(0.0));

                    }
                }
                if (!strArr[21].equals("")) {
                    pb.setValidated(Double.valueOf(strArr[21]));
                }
                pb.setDecoy(Integer.valueOf(strArr[22]));
                pb.setStarred(Boolean.valueOf(strArr[23]));
                if (strArr.length == 24) {
                    //pb.setDeamidationAndGlycopattern();
                    //pb.setGlycopatternPositions(strArr[25]);
                } else {
                    if (strArr[24] != null) {
                        pb.setDeamidationAndGlycopattern(Boolean.valueOf(strArr[24]));
                    }
                    if (strArr[25] != null) {
                        pb.setGlycopatternPositions(strArr[25]);
                    }
                }
                if (pb.getDecoy() == 0) {
                    pb.setPeptideId(inedxId);
                    peptideList.put(pb.getPeptideId(), pb);
                    inedxId++;
                } else {
                }

            } else if (fileType == 0) //Protein file
            {
                prot.setSequenceCoverage(Double.valueOf(strArr[4]));
                prot.setObservableCoverage(Double.valueOf(strArr[5]));
                prot.setNonEnzymaticPeptides(Boolean.valueOf(strArr[6]));
                prot.setConfidentPtmSites(strArr[7]);
                prot.setNumberConfident(strArr[8]);
                prot.setOtherPtmSites(strArr[9]);
                prot.setNumberOfOther(strArr[10]);
                prot.setNumberValidatedPeptides(Integer.valueOf(strArr[11]));
                prot.setNumberValidatedSpectra(Integer.valueOf(strArr[12]));
                prot.setEmPai(Double.valueOf(strArr[13]));
                prot.setNsaf(Double.valueOf(strArr[14]));
                prot.setMw_kDa(Double.valueOf(strArr[15]));
                prot.setScore(Double.valueOf(strArr[16]));
                prot.setConfidence(Double.valueOf(strArr[17]));
                prot.setStarred(Boolean.valueOf(strArr[18]));
                proteinList.put(prot.getAccession(), prot);
            } else //Protein fraction file
            {
                IdentificationProteinBean tempProt = null;

                prot.setStarred(Boolean.valueOf(strArr[strArr.length - 1]));
                prot.setSpectrumFractionSpread_upper_range_kDa(strArr[strArr.length - 3]);
                prot.setSpectrumFractionSpread_lower_range_kDa(strArr[strArr.length - 4]);
                prot.setPeptideFractionSpread_upper_range_kDa(strArr[strArr.length - 5]);
                prot.setPeptideFractionSpread_lower_range_kDa(strArr[strArr.length - 6]);
                for (int x = 0; x < fileType; x++) {

                    tempProt = new IdentificationProteinBean();
                    tempProt.setAccession(prot.getAccession());
                    tempProt.setOtherProteins(prot.getOtherProteins());
                    tempProt.setProteinInferenceClass(prot.getProteinInferenceClass());
                    tempProt.setDescription(prot.getDescription());
                    tempProt.setStarred(prot.isStarred());
                    tempProt.setSpectrumFractionSpread_upper_range_kDa(prot.getSpectrumFractionSpread_upper_range_kDa());

                    tempProt.setPeptideFractionSpread_lower_range_kDa(prot.getPeptideFractionSpread_lower_range_kDa());

                    tempProt.setSpectrumFractionSpread_lower_range_kDa(prot.getSpectrumFractionSpread_lower_range_kDa());

                    tempProt.setPeptideFractionSpread_upper_range_kDa(prot.getPeptideFractionSpread_upper_range_kDa());

                    try {
                        tempProt.setNumberOfPeptidePerFraction(Integer.valueOf(strArr[(9 + x)]));

                    } catch (NumberFormatException e) {
                        double d = Double.valueOf(strArr[(9 + x)]);
                        tempProt.setNumberOfPeptidePerFraction((int) d);
                    }

                    try {
                        tempProt.setNumberOfSpectraPerFraction(Integer.valueOf(strArr[(9 + x + fileType)]));
                    } catch (NumberFormatException e) {
                        double d = Double.valueOf(strArr[(9 + x + fileType)]);
                        tempProt.setNumberOfSpectraPerFraction((int) d);
                    }
                    tempProt.setAveragePrecursorIntensityPerFraction(Double.valueOf(strArr[(9 + x + fileType + fileType)]));
                    FractionBean temFb = fractionsList.get(x);
                    Map<String, IdentificationProteinBean> temProteinList = temFb.getProteinList();
                    temProteinList.put(tempProt.getAccession(), tempProt);
                    temFb.setProteinList(temProteinList);
                    fractionsList.put(x, temFb);


                }
            }

        }
        if( fileType == -7)
            {
                exp.setgPeptideList(gPeptideList);
                return exp;
            
            }


        if (peptideList != null && peptideList.size() > 0) {
//            exp.setPeptideList(peptideList);
            peptideList = this.addSharedPeptides(peptideList);
            exp.setPeptidesNumber(this.getNumValidatedPep(peptideList));
        }
        if (fractionsList.isEmpty() && fractionRanges == null) {
//            exp.setProteinList(proteinList);
            exp.setProteinsNumber(proteinList.size());

        } else {
            fractionsList = getFractionRange(fractionsList);//will be updated by dataset
//            exp.setFractionsList(fractionsList);
        }
        return exp;
    }

    private Map<Integer, PeptideBean> addSharedPeptides(Map<Integer, PeptideBean> peptideList) {
        int index = peptideList.size() + 1;
        Map<Integer, PeptideBean> updatedPeptideList = new HashMap<Integer, PeptideBean>();
        updatedPeptideList.putAll(peptideList);
        for (PeptideBean pb : peptideList.values()) {
            if (pb.getProtein().trim().equalsIgnoreCase("shared peptide")) {

                String str = pb.getPeptideProteins();

                String[] strArr = str.split(",");
                for (String newProt : strArr) {
                    PeptideBean tPb = new PeptideBean();
                    tPb.setAaAfter(pb.getAaAfter());
                    tPb.setAaBefore(pb.getAaBefore());
                    tPb.setConfidence(pb.getConfidence());
                    tPb.setFixedModification(pb.getFixedModification());
                    tPb.setLocationConfidence(pb.getLocationConfidence());
                    tPb.setNumberOfValidatedSpectra(pb.getNumberOfValidatedSpectra());
                    tPb.setOtherProteinDescriptions(pb.getOtherProteinDescriptions());
                    tPb.setPeptideEnd(pb.getPeptideEnd());
                    tPb.setOtherProteins(pb.getOtherProteins());
                    tPb.setPeptideId(index);
                    tPb.setPeptideProteins(pb.getPeptideProteins());
                    tPb.setPeptideProteinsDescriptions(pb.getPeptideProteinsDescriptions());
                    tPb.setPeptideStart(pb.getPeptideStart());
                    tPb.setPrecursorCharges(pb.getPrecursorCharges());
                    tPb.setProtein(newProt.trim());
                    tPb.setScore(pb.getScore());
                    tPb.setSequence(pb.getSequence());
                    tPb.setVariableModification(pb.getVariableModification());
                    updatedPeptideList.put(index, tPb);
                    index++;


                }
            }
        }
        peptideList.clear();
        peptideList.putAll(updatedPeptideList);
        return updatedPeptideList;
    }

    private Map<Integer, FractionBean> getFractionRange(Map<Integer, FractionBean> fractionsList) {
        Map<Integer, FractionBean> tempFractionsList = new HashMap<Integer, FractionBean>();
        for (int key : fractionsList.keySet()) {
            FractionBean fraction = fractionsList.get(key);
//            fraction.setMaxRange(0.0);
//            fraction.setMaxRange(0.0);
            tempFractionsList.put(key, fraction);

        }

        return tempFractionsList;
    }

    private int getNumValidatedPep(Map<Integer, PeptideBean> peptideList) {
        int vp = 0;
        for (PeptideBean pb : peptideList.values()) {
            if (pb.getValidated() == 1.0) {
                ++vp;
            }

        }
        return vp;

    }
    
    
     public List<QuantProtein> readCSVQuantFile(File dataFile) {
//        File dataFile = new File(path);
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
