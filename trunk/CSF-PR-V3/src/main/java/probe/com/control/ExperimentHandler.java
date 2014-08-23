package probe.com.control;

import java.sql.SQLException;


import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.model.ExperimentModel;
import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.subview.PeptideTable;

public class ExperimentHandler implements Serializable {

    /**
     * Yehia Farag
     */
    private static final long serialVersionUID = 1L;
    private final ExperimentModel em;
    private final Authenticator authenticator;

    public ExperimentHandler(String url, String dbName, String driver, String userName, String password) {
        em = new ExperimentModel(url, dbName, driver, userName, password);
        authenticator =new Authenticator(url, dbName, driver, userName, password);
    }

    public boolean handelExperimentFile(File file, String MIMEType, ExperimentBean exp) throws IOException, SQLException {
        boolean test = false;
        test = em.handelExperiment(file, MIMEType, exp);
        return test;

    }
    //first step

    public Map<Integer, ExperimentBean> getExperiments(Map<Integer, ExperimentBean> expList) {
        if (expList == null) {
            expList = em.getExperiments();
        } else {

            //perform check on updates if no new updates then do nothing(Future work)
            Map<Integer, ExperimentBean> expList2 = em.getExperiments();
            for (int key : expList2.keySet()) {
                if (expList.containsKey(key)) {
                    ExperimentBean exp = expList.get(key);
                    if (exp.getProteinList() != null && exp.getProteinList().size() > 0) {
                        if (exp.getFractionsNumber() < expList2.get(key).getFractionsNumber()) {
                            exp.setFractionsNumber(expList2.get(key).getFractionsNumber());
                            expList.put(key, exp);
                        }

                    } else if (exp.getPeptideList() != null && exp.getPeptideList().size() > 0)
                    {
                    //to be removed
                    }
                    else {
                        expList.put(key, expList2.get(key));
                    }
                } else {
                    expList.put(key, expList2.get(key));
                }
            }
        }
        return expList;
    }

    public Map<String, ProteinBean> getProteinsList(int expId, Map<Integer, ExperimentBean> expList) {
        Map<String, ProteinBean> protList = expList.get(expId).getProteinList();
        if (protList == null || protList.isEmpty()) {
            protList = em.getProteinsList(expId);
        }
        return protList;
    }

    public ExperimentBean getExperiment(int expId, String x) {
        ExperimentBean exp = em.getExperiment(expId);
        return exp;
    }

    public Map<Integer, PeptideBean> getPeptidesList(int expId, Map<Integer, ExperimentBean> expList) {
        Map<Integer, PeptideBean> peptidesList = expList.get(expId).getPeptideList();
        if (peptidesList == null || peptidesList.isEmpty()) {
            peptidesList = em.getPeptidesList(expId);
        }
        return peptidesList;
    }

    public Map<Integer, PeptideBean> getPeptidesList(int expId, boolean validated) {

        Map<Integer, PeptideBean> peptidesList = em.getPeptidesList(expId);
        if (validated) {
            Map<Integer, PeptideBean> validatedPtidesList = new HashMap<Integer, PeptideBean>();
            int x = 0;
            for (PeptideBean pb : peptidesList.values()) {

                if (pb.getValidated() == 1.0) {
                    validatedPtidesList.put(x++, pb);
                }
            }
            return validatedPtidesList;
        } else {
            return peptidesList;
        }
    }
    
    

    public Map<Integer, PeptideBean> getPeptidesProtList(Map<Integer, PeptideBean> pepList, Set<Integer> peptideIds) {
        Map<Integer, PeptideBean> peptidesProtList = new HashMap<Integer, PeptideBean>();
//        if (pepList != null) {
//            for (PeptideBean pepb : pepList.values()) {
//               // if (pepb.getProtein().equalsIgnoreCase(accession)) {
//                    peptidesProtList.put(pepb.getPeptideId(), pepb);
//             //   }
//
//            }
//        }
//        if (peptidesProtList.isEmpty()) {
        peptidesProtList = em.getPeptidesProtList(peptideIds);
//        }
        return peptidesProtList;
    }
    
    

    public Map<Integer, FractionBean> getProtFractionsList(int expId, Map<Integer, ExperimentBean> expList,String protKey) {
        Map<Integer, FractionBean> fractionsList;
        if (expList.containsKey(expId) && expList.get(expId).getFractionsList() != null && (!expList.get(expId).getFractionsList().isEmpty())) {
            //check if exp updated if not
            fractionsList = expList.get(expId).getFractionsList();
            
        } else {
            fractionsList = em.getFractionsList(expId);
        }

        return fractionsList;

    }

   

    public Map<Integer, FractionBean> getFractionsList(int expId, Map<Integer, ExperimentBean> expList) {
        Map<Integer, FractionBean> fractionsList;
        if (expList.containsKey(expId) && expList.get(expId).getFractionsList() != null && (!expList.get(expId).getFractionsList().isEmpty())) {
            //check if exp updated if not
            fractionsList = expList.get(expId).getFractionsList();
        } else {
            fractionsList = em.getFractionsList(expId);
        }

        return fractionsList;

    }

    public Map<Integer, ProteinBean> getProteinFractionAvgList(String accession, Map<Integer, FractionBean> fractionsList, int expId) {
        Map<Integer, ProteinBean> proteinFractList = new TreeMap<Integer, ProteinBean>();

        Map<Integer, FractionBean> treeFractList = new TreeMap<Integer, FractionBean>();

        if (fractionsList == null) {
            fractionsList = em.getProteinFractionList(accession, expId);
        }


        treeFractList.putAll(fractionsList);
        for (int k : treeFractList.keySet()) {
            FractionBean fb = fractionsList.get(k);

            if (fb.getProteinList().containsKey(accession)) {
                proteinFractList.put(fb.getFractionIndex(), fb.getProteinList().get(accession));
                
            }
           
        }
        return proteinFractList;
    }

    ///v-2
//    public Map<Integer, List<ProteinBean>> searchProteinByAccession1(String searchArr, Map<Integer, ExperimentBean> expList,String searchDatasetType) {
//        Map<Integer, List<ProteinBean>> protExpList = new HashMap<Integer, List<ProteinBean>>();
//        List<ProteinBean> protList = new ArrayList<ProteinBean>();
//        ExperimentBean exp=null;
//        if (! searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
//            for(ExperimentBean exp1:expList.values())
//            {
//                if(exp1.getName().equalsIgnoreCase(searchDatasetType))
//                {
//                    exp = exp1;
//                    break;
//                
//                }
//            }
//           protList = em.searchProtein(searchArr, exp.getExpId(), protList);
//           protExpList.put(exp.getExpId(), protList);
//        
//        } else {
//            for (ExperimentBean exp1 : expList.values()) {                
//                protList = em.searchProtein(searchArr, exp1.getExpId(), protList);
//                protExpList.put(exp1.getExpId(), protList);
//            }
//        }
//
//        return protExpList;
//
//    }
    public Map<Integer, ProteinBean> searchProteinByAccession(String searchArr, Map<Integer, ExperimentBean> expList, String searchDatasetType,boolean validatedOnly) {
        Map<Integer, ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();

        ExperimentBean exp = null;
        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
            for (ExperimentBean exp1 : expList.values()) {
                if (exp1.getName().equalsIgnoreCase(searchDatasetType)) {
                    exp = exp1;
                    break;

                }
            }
            protExpList = em.searchProtein(searchArr, exp.getExpId(),validatedOnly);


        } else {
            for (ExperimentBean exp1 : expList.values()) {
                  Map<Integer, ProteinBean> protTempList = em.searchProtein(searchArr, exp1.getExpId(),validatedOnly);
                  if(protTempList != null)
                        protExpList.putAll(protTempList);

            }
        }

        return protExpList;

    }

    public Map<Integer, PeptideBean> getPeptidesProtExpList(Map<Integer, ExperimentBean> expList, String accession, int expId, int z) {

        Map<Integer, PeptideBean> peptidesProtList = new HashMap<Integer, PeptideBean>();
        if (expList.get(expId).getPeptideList() != null && expList.get(expId).getPeptideList().size() > 0) {
            for (PeptideBean pepb : expList.get(expId).getPeptideList().values()) {
                if (pepb.getProtein().equalsIgnoreCase(accession)) {
                    peptidesProtList.put(pepb.getPeptideId(), pepb);
                }

            }
        } else {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                System.err.println(e.getLocalizedMessage());
            }
            peptidesProtList.putAll(em.getPeptidesProtList(expList.get(expId).getPeptidesIds()));
        }

        return peptidesProtList;
    }

    public Map<Integer, ProteinBean> searchProteinByName(String protSearch, Map<Integer, ExperimentBean> expList, String searchDatasetType,boolean validatedOnly) {

        Map<Integer, ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();

        ExperimentBean exp = null;
        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
            for (ExperimentBean exp1 : expList.values()) {
                if (exp1.getName().equalsIgnoreCase(searchDatasetType)) {
                    exp = exp1;
                    break;

                }
            }
            protExpList = em.searchProteinByName(protSearch, exp.getExpId(),validatedOnly);

        } else {
            for (ExperimentBean exp1 : expList.values()) {
                protExpList.putAll(em.searchProteinByName(protSearch, exp1.getExpId(),validatedOnly));

            }
        }

        return protExpList;
//        
//        
//        
//        
//        
//        
//        
//        
//        
//        Map<Integer, ProteinBean> protExpFullList = new HashMap<Integer,  ProteinBean>();
//        if (expList == null) {
//            return null;
//        } else {
//            int index = 0;
//            for (ExperimentBean exp1 : expList.values()) {
//                Map<Integer, ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();
//
//                List<ProteinBean> proteinsList = em.searchProteinByName(protSearch, exp1.getExpId());
//
//                if (proteinsList != null) {
//
//                    for (ProteinBean pb : proteinsList) {
//                        protExpList.put(exp1.getExpId(), pb);
//                        protExpFullList.put(index++, protExpList);
//                        protExpList = new HashMap<Integer, ProteinBean>();
//
//
//                    }
//                }
//
//            }
//        }
//
//        return protExpFullList;
    }

    ///v-2
    public Map<Integer, ProteinBean> searchProteinByPeptideSequence(String protSearch, Map<Integer, ExperimentBean> expList, String searchDatasetType,boolean validatedOnly) {
        Map<Integer, ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();
        ExperimentBean exp = null;
        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
            for (ExperimentBean exp1 : expList.values()) {
                if (exp1.getName().equalsIgnoreCase(searchDatasetType)) {
                    exp = exp1;
                    break;

                }
            }
            protExpList = em.searchProteinByPeptideSequence(protSearch, exp.getExpId(), validatedOnly);

        } else {
            for (ExperimentBean exp1 : expList.values()) {
                Map<Integer, ProteinBean> protTempList =em.searchProteinByPeptideSequence(protSearch, exp1.getExpId(), validatedOnly);
                if(protTempList !=null||! protTempList.isEmpty())
                    protExpList.putAll(protTempList);

            }
        }
        return protExpList;




//        
//        
//        
//        if (expList == null) {
//            return null;
//        } else {
//            int index = 0;
//            for (ExperimentBean exp1 : expList.values()) {
//                Map<Integer, ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();
//
//                List<ProteinBean> proteinsList = em.searchProteinByPeptideSequence(protSearch, exp1.getExpId());
//
//                if (proteinsList != null) {
//                    for (ProteinBean pb : proteinsList) {
//                        protExpList.put(exp1.getExpId(), pb);
//                        protExpFullList.put(index++, protExpList);
//                        protExpList = new HashMap<Integer, ProteinBean>();
//
//                    }
//                }
//
//
//            }
//        }
//
//        return protExpFullList;
    }

    public List<Map<Integer, Map<Integer, ProteinBean>>> filterSearch(List<Map<Integer, Map<Integer, ProteinBean>>> listOfProtExpFullList) {
        List<Map<Integer, Map<Integer, ProteinBean>>> filteredList = new ArrayList<Map<Integer, Map<Integer, ProteinBean>>>();
        Map<Integer, Map<Integer, ProteinBean>> map1Updated = new HashMap<Integer, Map<Integer, ProteinBean>>();
        Map<Integer, ProteinBean> map2Updated = new HashMap<Integer, ProteinBean>();
        Set<String> filterSet = new HashSet<String>();
        int x = 0;
        for (Map<Integer, Map<Integer, ProteinBean>> map1 : listOfProtExpFullList) {
            for (int key1 : map1.keySet()) {
                Map<Integer, ProteinBean> map2 = map1.get(key1);
                for (int key2 : map2.keySet()) {
                    ProteinBean pb = map2.get(key2);
                    filterSet.add("" + key2 + "," + pb.getAccession());
                    if (x != filterSet.size()) {
                        map2Updated.put(key2, pb);

                    }
                    x = filterSet.size();

                }
                map1Updated.put(key1, map2Updated);

            }
            filteredList.add(map1Updated);

        }


        return filteredList;

    }

    public Set<Integer> getExpPepProIds(int expId, String accession, String otherAccession) {
        String queryWord = accession;
        Set<Integer> expProPepIds = em.getExpPepProIds(expId, queryWord.trim());
        
        if (otherAccession != null && !otherAccession.equals("")) {
            String[] otherAccessionArr = otherAccession.split(",");
            for (String str : otherAccessionArr) {
                expProPepIds.addAll(em.getExpPepProIds(expId, str.trim()));
              }
        }

        return expProPepIds;
    }

    public List<StandardProteinBean> getStandardProtPlotList(int expId) {
        List<StandardProteinBean> standardPlotList = em.getStandardProtPlotList(expId);
        return standardPlotList;
    }

    public boolean updateExpData(ExperimentBean exp) {
        boolean test = em.updateExpData(exp);
        return test;

    }

    public Set getDatasetType(Map<Integer, ExperimentBean> expList) {

        Set<String> datasetTypes = new HashSet<String>();
        for (ExperimentBean exp : expList.values()) {
            datasetTypes.add(exp.getExpType() + "");
        }
        datasetTypes.add("Type 2");
        datasetTypes.add("Type 3");
        return datasetTypes;

    }

    public Map<String, PeptideTable> getProtAllPep(String accession, String otherAccession, Map<Integer, ExperimentBean> expList, boolean validated) {
        Map<String, PeptideTable> tl = new HashMap<String, PeptideTable>();
        for (ExperimentBean temExp : expList.values()) {

            Set<Integer> expProPepIds = getExpPepProIds(temExp.getExpId(), accession, otherAccession);
            Map<Integer, PeptideBean> pepProtList = getPeptidesProtList(temExp.getPeptideList(), expProPepIds);

            if (pepProtList.size() > 0) {
                if (validated) {
                    Map<Integer, PeptideBean> vPepProtList = new HashMap<Integer, PeptideBean>();
                    for (int key : pepProtList.keySet()) {
                        PeptideBean pb = pepProtList.get(key);
                        if (pb.getValidated() == 1) {
                            vPepProtList.put(key, pb);
                        }

                    }
                    PeptideTable pepTable = new PeptideTable(vPepProtList, null,false);
                    pepTable.setVisible(false);
                    tl.put(temExp.getName(), pepTable);

                } else {
                    PeptideTable pepTable = new PeptideTable(pepProtList, null,false);
                    pepTable.setVisible(false);
                    tl.put(temExp.getName(), pepTable);
                }
            }

        }
        return tl;
    }

    public Authenticator getAuthenticator() {
        return authenticator;
    }
    
    
    
}
