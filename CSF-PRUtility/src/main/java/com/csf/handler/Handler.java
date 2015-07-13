/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csf.handler;

import com.csf.DAL.DAL;
import com.pepshaker.util.beans.ExperimentBean;
import com.quantcsf.QuantDataHandler;
import com.quantcsf.beans.QuantDatasetObject;
import com.quantcsf.beans.QuantPeptide;
import com.quantcsf.beans.QuantProtein;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
/*
 * @author Yehia Farag
 */

public class Handler {

    private final DAL dal;
    private final QuantDataHandler qDataHandler;

    public Handler(String url, String dbName, String driver, String userName, String password) throws SQLException {
        dal = new DAL(url, dbName, driver, userName, password);
        qDataHandler = new QuantDataHandler();
    }

    public boolean handelPeptideShakerProject(ExperimentBean exp) {
        boolean test = false;
        int expId = dal.storeExperiment(exp);
        exp.setExpId(expId);
        if (!exp.getProteinList().isEmpty()) {
            test = dal.storeProteinsList(exp);
        }
        if (!exp.getPeptideList().isEmpty()) {
            test = dal.storePeptidesList(exp);
        }
        if (!exp.getFractionsList().isEmpty()) {
            test = dal.storeFractionsList(exp);
        }
        System.gc();
        return test;
    }

    public boolean checkName(String name) throws SQLException {
        boolean test = dal.checkName(name);
        return test;
    }

    public void exportDataBase(String mysqldumpUrl,String sqlFileUrl) {
        dal.exportDataBase(mysqldumpUrl, sqlFileUrl);
    }

    public boolean restoreDB(String source) {
        return dal.restoreDB(source);
    }

    public boolean handelQuantPubData(String path) {

        //1.read file
        List<QuantProtein> qProtList = qDataHandler.readCSVQuantFile(path);
        //2.store full data
        boolean test  = dal.storeCombinedQuantProtTable(qProtList);
        //3.update dataset table
        dal.storeQuantDatasets();
        //handel quant prot     
        Set<QuantDatasetObject> datasetsList = dal.getQuantDatasetListObject();
        qProtList = this.handleQuantData(datasetsList,qProtList);
        int protIndex = dal.getCurrentProtIndex();
        List<QuantPeptide> peptidesList = handelQuantPeptides(qProtList,protIndex);        
        
        //foldchange
        
        defineProtFoldChange(qProtList, peptidesList);
        
        
        //store quant protiens
        dal.storeQuantitiveProteins(qProtList);
        
        //store quant peptides
        dal.storeQuantitivePeptides(peptidesList);
        
        System.out.println("final prot list updated (should be smaller)  " + qProtList.size() + "  prer " + peptidesList.size());

        return true;
    }
     public boolean handelProtSequanceData(String path) {
         System.out.println("file path "+path);
          
        //1.read file 
         Map<String,String> protSeqMap=qDataHandler.readSequanceFile(path);
        
//        //2.store full data
        boolean test  = dal.updateProtSequances(protSeqMap);
//        //3.update dataset table
//        dal.storeQuantDatasets();
//        //handel quant prot     
//        Set<QuantDatasetObject> datasetsList = dal.getQuantDatasetListObject();
//        qProtList = this.handleQuantData(datasetsList,qProtList);
//        int protIndex = dal.getCurrentProtIndex();
//        List<QuantPeptide> peptidesList = handelQuantPeptides(qProtList,protIndex);        
//        
//        //foldchange
//        
//        defineProtFoldChange(qProtList, peptidesList);
//        
//        
//        //store quant protiens
//        dal.storeQuantitiveProteins(qProtList);
//        
//        //store quant peptides
//        dal.storeQuantitivePeptides(peptidesList);
//        
//        System.out.println("final prot list updated (should be smaller)  " + qProtList.size() + "  prer " + peptidesList.size());

        return test;
    }

    private List<QuantPeptide> handelQuantPeptides(List<QuantProtein> qProtList, int protIndex) {
//        Map<String, QuantPeptide> peptides = new HashMap<String, QuantPeptide>();
        Map<String, QuantProtein> proteins = new HashMap<String, QuantProtein>();
        List<QuantPeptide> peptidesList = new ArrayList<QuantPeptide>();
//        List<QuantProtein> updatedQuantProtList = new ArrayList<QuantProtein>();
        for (QuantProtein prot : qProtList) {
            QuantPeptide pep = new QuantPeptide();
            pep.setDsKey(prot.getDsKey());
            pep.setFc(prot.getStringFCValue());
            pep.setPvalue(prot.getpValue());
            pep.setRoc(prot.getRocAuc());
            pep.setFcPatientGroupIonPatientGroupII(prot.getFcPatientGroupIonPatientGroupII());
            pep.setModificationComment(prot.getModificationComment());
            pep.setPeptideModification(prot.getPeptideModification());
            pep.setPeptideSequance(prot.getPeptideSequance());
            pep.setStrPvalue(prot.getStringPValue());
            pep.setPvalueComment(prot.getPvalueComment());

            String acc = prot.getUniprotAccession();
            if (acc.equalsIgnoreCase("Not Available")) {
                acc = prot.getPublicationAccNumber();
            }
            if (!proteins.containsKey(prot.getDsKey() + "-" + acc)) {
                pep.setProtKey(protIndex);
                prot.setProtKey(protIndex);
                proteins.put(prot.getDsKey() + "-" + acc, prot);
                protIndex++;

            } else {
                pep.setProtKey(proteins.get(prot.getDsKey() + "-" + acc).getProtKey());
            }
            peptidesList.add(pep);
        }
        qProtList.clear();
        for (QuantProtein qprot : proteins.values()) {
            qProtList.add(qprot);

        }

//        Set<String> ids = new HashSet<String>();
//        for( QuantPeptide pep  :peptidesList)
//            ids.add(pep.getProtKey()+"");
//
//        System.out.println("at qProtList " + qProtList.size() + "   -- filtered to " + proteins.size() + "   peptides are " + peptidesList.size()+"  ids size "+ids.size());
//        
//        ready to store in db
        return peptidesList;
    }

    private List<QuantProtein> handleQuantData(Set<QuantDatasetObject> dss, List<QuantProtein> qProtList) {

        List<QuantProtein> updatedQuantProtList = new ArrayList<QuantProtein>();
        for (QuantProtein qp : qProtList) {
            for (QuantDatasetObject ds : dss) {

                if (!ds.getPumedID().equalsIgnoreCase(qp.getPumedID())) {
                    continue;
                }

                if (!ds.getAuthor().equalsIgnoreCase(qp.getAuthor())) {
                    continue;
                }

                if (!ds.getAdditionalcomments().equalsIgnoreCase(qp.getAdditionalComments())) {
                    continue;
                }
//                if (ds.getFilesNumber() != (qp.getFilesNum())) {
//                    continue;
//                }

                if (ds.getIdentifiedProteinsNumber() != (qp.getIdentifiedProteinsNum())) {
                    continue;
                }
                if (ds.getQuantifiedProteinsNumber() != (qp.getQuantifiedProteinsNumber())) {
                    continue;
                }

//                if (!ds.getDiseaseGroups().equalsIgnoreCase(qp.getDiseaseGroups())) {
//                    continue;
//                }
                if (!ds.getRawDataUrl().equalsIgnoreCase(qp.getRawDataAvailable())) {

                    continue;
                }

                if (!ds.getTypeOfStudy().equalsIgnoreCase(qp.getTypeOfStudy())) {
                    continue;
                }

                if (ds.getYear() != (qp.getYear())) {

                    continue;
                }

                if (!ds.getSampleType().equalsIgnoreCase(qp.getSampleType())) {
                    continue;
                }
                if (!ds.getSampleMatching().equalsIgnoreCase(qp.getSampleMatching())) {

                    continue;
                }
                if (!ds.getTechnology().equalsIgnoreCase(qp.getTechnology())) {

                    continue;
                }

                if (!ds.getAnalyticalApproach().replace("-", " ").equalsIgnoreCase(qp.getAnalyticalApproach().replace("-", " "))) {

                    continue;
                }
                if (!ds.getEnzyme().equalsIgnoreCase(qp.getEnzyme())) {

                    continue;
                }
                if (!ds.getShotgunTargeted().equalsIgnoreCase(qp.getShotgunOrTargetedQquant())) {

                    continue;
                }
                if (!ds.getQuantificationBasis().equalsIgnoreCase(qp.getQuantificationBasis())) {

                    continue;
                }

                if (!ds.getQuantBasisComment().equalsIgnoreCase(qp.getQuantBasisComment())) {
                    continue;
                }
                if (!ds.getPatientsGroup1().equalsIgnoreCase(qp.getPatientGroupI())) {

                    continue;
                }
                if (!ds.getNormalizationStrategy().equalsIgnoreCase(qp.getNormalizationStrategy())) {

                    continue;
                }

                if (!ds.getPatientsGroup1Comm().equalsIgnoreCase(qp.getPatientGrIComment())) {

                    continue;
                }
                if (!ds.getPatientsSubGroup1().equalsIgnoreCase(qp.getPatientSubGroupI())) {
                    continue;
                }

                if (!ds.getPatientsGroup2().equalsIgnoreCase(qp.getPatientGroupII())) {

                    continue;
                }

                if (!ds.getPatientsGroup2Comm().equalsIgnoreCase(
                        qp.getPatientGrIIComment())) {
                    continue;
                }
                if (!ds.getPatientsSubGroup2().equalsIgnoreCase(qp.getPatientSubGroupII())) {

                    continue;
                }
                if (ds.getPatientsGroup1Number() != (qp.getPatientsGroupINumber())) {

                    continue;
                }
                if (ds.getPatientsGroup2Number() != (qp.getPatientsGroupIINumber())) {

                    continue;
                }

                qp.setDsKey(ds.getUniqId());
                ds.addQuantProt(qp);
                updatedQuantProtList.add(qp);
                break;

            }

        }
        return updatedQuantProtList;

    }

    private void defineProtFoldChange(List<QuantProtein> qProtList, List<QuantPeptide> quantPepList) {
        for (QuantProtein prot : qProtList) {
            List<Integer> calcList = new ArrayList<Integer>();
            for (QuantPeptide pep : quantPepList) {
                if ((pep.getDsKey() == prot.getDsKey()) && pep.getProtKey() == prot.getProtKey()) {
                    if (pep.getFc().equalsIgnoreCase("Not Provided")) {
                        calcList.add(0);
                    } else if (pep.getFc().equalsIgnoreCase("Increased")) {
                        calcList.add(1);
                    } else if (pep.getFc().equalsIgnoreCase("Decreased")) {
                        calcList.add(-1);
                    }
                }

            }
            int fc = 0;
            for (int i : calcList) {
                fc += i;
            }
            if (fc == 0) {
                prot.setStringFCValue("Not Provided");
            } else if (fc > 0) {
                prot.setStringFCValue("Increased");
            } else {
                prot.setStringFCValue("Decreased");
            }

        }

    }
    public void correctProtInfo(){
    dal.correctProtInfo();
    
    
    }
}
