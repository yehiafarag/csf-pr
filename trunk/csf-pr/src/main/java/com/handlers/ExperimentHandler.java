package com.handlers;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


import com.model.ExperimentModel;
import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.PeptideBean;
import com.model.beans.ProteinBean;
import com.model.beans.StandardProteinBean;

public class ExperimentHandler implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private ExperimentModel em;

    public ExperimentHandler(String url, String dbName, String driver, String userName, String password) {
        em = new ExperimentModel(url, dbName, driver, userName, password);

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
						; else {
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
        if (protList == null || protList.size() == 0) {
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
        if (peptidesList == null || peptidesList.size() == 0) {
            peptidesList = em.getPeptidesList(expId);
        }
        return peptidesList;
    }

    public Map<Integer, PeptideBean> getPeptidesProtList(Map<Integer, PeptideBean> pepList, String accession, List<Integer> peptideIds) {
        Map<Integer, PeptideBean> peptidesProtList = new HashMap<Integer, PeptideBean>();
        if (pepList != null) {
            for (PeptideBean pepb : pepList.values()) {
                if (pepb.getProtein().equalsIgnoreCase(accession)) {
                    peptidesProtList.put(pepb.getPeptideId(), pepb);
                }

            }
        }
        if (peptidesProtList.size() == 0) {
            peptidesProtList = em.getPeptidesProtList(peptideIds, accession);
        }
        return peptidesProtList;
    }

    public Map<Integer, FractionBean> getFractionsList(int expId, Map<Integer, ExperimentBean> expList) {
        Map<Integer, FractionBean> fractionsList = null;
        if (expList.containsKey(expId) && expList.get(expId).getFractionsList() != null) {
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
    public Map<Integer, List<ProteinBean>> searchProteinByAccession(String searchArr, Map<Integer, ExperimentBean> expList) {
        Map<Integer, List<ProteinBean>> protExpList = new HashMap<Integer, List<ProteinBean>>();

        if (expList == null) {
            return null;
        } else {
            for (ExperimentBean exp1 : expList.values()) {
                List<ProteinBean> protList = new ArrayList<ProteinBean>();

                protList = em.searchProtein(searchArr, exp1.getExpId(), protList);

                protExpList.put(exp1.getExpId(), protList);
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
                e.printStackTrace();
            }
            peptidesProtList.putAll(em.getPeptidesProtList(expList.get(expId).getPeptidesIds(), accession));
        }

        return peptidesProtList;
    }

    public Map<Integer, Map<Integer, ProteinBean>> searchProteinByName(String protSearch, Map<Integer, ExperimentBean> expList) {
        Map<Integer, Map<Integer, ProteinBean>> protExpFullList = new HashMap<Integer, Map<Integer, ProteinBean>>();
        if (expList == null) {
            return null;
        } else {
            int index = 0;
            for (ExperimentBean exp1 : expList.values()) {
                Map<Integer, ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();

                List<ProteinBean> proteinsList = em.searchProteinByName(protSearch, exp1.getExpId());

                if (proteinsList != null) {

                    for (ProteinBean pb : proteinsList) {
                        protExpList.put(exp1.getExpId(), pb);
                        protExpFullList.put(index++, protExpList);
                        protExpList = new HashMap<Integer, ProteinBean>();


                    }
                }

            }
        }

        return protExpFullList;
    }

    ///v-2
    public Map<Integer, Map<Integer, ProteinBean>> searchProteinByPeptideSequence(String protSearch, Map<Integer, ExperimentBean> expList) {
        Map<Integer, Map<Integer, ProteinBean>> protExpFullList = new HashMap<Integer, Map<Integer, ProteinBean>>();
        if (expList == null) {
            return null;
        } else {
            int index = 0;
            for (ExperimentBean exp1 : expList.values()) {
                Map<Integer, ProteinBean> protExpList = new HashMap<Integer, ProteinBean>();

                List<ProteinBean> proteinsList = em.searchProteinByPeptideSequence(protSearch, exp1.getExpId());

                if (proteinsList != null) {
                    for (ProteinBean pb : proteinsList) {
                        protExpList.put(exp1.getExpId(), pb);
                        protExpFullList.put(index++, protExpList);
                        protExpList = new HashMap<Integer, ProteinBean>();

                    }
                }


            }
        }

        return protExpFullList;
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

    public List<Integer> getExpPepProIds(int expId, String accession) {
        List<Integer> expProPepIds = em.getExpPepProIds(expId, accession);

        return expProPepIds;
    }

    public List<StandardProteinBean> getStandardProtPlotList(int expId) {
        List<StandardProteinBean> standardPlotList = em.getStandardProtPlotList(expId);
        return standardPlotList;
    }
}
