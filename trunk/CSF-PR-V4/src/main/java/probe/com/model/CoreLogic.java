package probe.com.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.dal.DataAccess;
import probe.com.model.beans.DatasetBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.model.util.FilesReader;

public class CoreLogic implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final DataAccess da;
    private final FilesReader fr = new FilesReader();
    
    private final TreeMap<Integer, String> datasetNamesList = new TreeMap<Integer, String>();//for dropdown select list
    private Map<Integer, DatasetBean> datasetList;
    private final Map<Integer,Integer> datasetIndex = new HashMap<Integer, Integer>();

    public CoreLogic(String url, String dbName, String driver, String userName, String password) {
        da = new DataAccess(url, dbName, driver, userName, password);
        
        //just to arrange the already stored datasets -->> to be removed in the future
        datasetIndex.put(8,1);
        datasetIndex.put(14,2);
        datasetIndex.put(4,3);
        datasetIndex.put(17,4);
        datasetIndex.put(15, 5);
        datasetIndex.put(16, 6);
        datasetIndex.put(9,7);   
    }
    
   
    
    public TreeMap<Integer, String> getDatasetNamesList(){
        if (datasetList == null) {
            datasetList = getDatasetList();
        }
        for (int datasetkey : datasetList.keySet()) {
            //for re-indexing the stored datasets, to be removed in the future
            if (datasetIndex.containsKey(datasetkey)) {
                DatasetBean dataset = datasetList.get(datasetkey);
                datasetNamesList.put(datasetIndex.get(datasetkey), "\t" + dataset.getName());

            } else {
                DatasetBean dataset = datasetList.get(datasetkey);
                datasetNamesList.put(datasetkey, "\t" + dataset.getName());
                datasetIndex.put(datasetkey, datasetkey);
            }
        }
        return datasetNamesList;
        
        
        
               
    
    
    }

    public boolean handelExperiment(File file, String MIMEType, DatasetBean exp) throws IOException, SQLException {


        exp = fr.readTextFile(file, MIMEType, exp);//method to extract data from proteins files to store them in database       
        boolean test = false;
        if (exp == null)//exp is null
        {
            test = false;
        } else if (exp.getExpFile() == -7) {
            //update glycofile
            System.out.println("start updating glyco");
            if (exp.getExpId() != -1 || !exp.getgPeptideList().isEmpty())//new exp
            {
                
            System.out.println("start updating glyco -->> "+exp.getExpId());
                Map<Integer,PeptideBean> pepList = this.getPeptidesList(exp.getExpId());
                
            System.out.println("start updating glyco -->> "+pepList.size()+"  gpep "+exp.getgPeptideList().size() );
                exp.setPeptideList(updatePeptideList(exp.getgPeptideList(),pepList));                
            System.out.println("before da ");
                test = da.updatePeptideFile(exp);
                return test;
                
                
                
            } else {
                System.out.println("its new dataset");
                return false;
            }

        } else if (exp.getExpFile() == -5) {
            //if new file or no fraction file cancel update 
            if (exp.getExpId() == -1 || exp.getFractionsNumber() == 0)//new exp
            {
                test = false;
            } else {
                test = true;//da.updateFractionRange(exp);
            }

        } else if (exp.getExpFile() == 0)//Protein  file
        {

            if (exp.getExpId() == -1)//new exp
            {
                test = da.setProteinFile(exp);
            } else {
                test = da.updateProteinFile(exp);
            }
        } else if (exp.getExpFile() == -2)//peptide file
        {
            if (exp.getExpId() == -1)//new exp
            {
                test = da.setPeptideFile(exp);
            } else {
                test = da.updatePeptideFile(exp);
            }
        } else if (exp.getExpFile() == -100)//Standard plot file
        {
            if (exp.getExpId() == -1)//new exp
            {
                System.out.println("standard plot prot for new expermint");
                test = da.setStandardPlotProt(exp);//not implemented yet
            } else {
                System.out.println("standard plot prot under processing");
                test = da.updateStandardPlotProt(exp);
            }
        } else //Protein fraction file
        {
            exp.setFractionRange(0);
            if (exp.getExpId() == -1)//new exp
            {
                test = false;//da.setProteinFractionFile(exp);
            } else {
                test = da.updateProteinFractionFile(exp);
            }
        }
        return test;

    }

    public Map<Integer, DatasetBean> getDatasetList() {
        datasetList = da.getExperiments();
        return datasetList;
    }

    public DatasetBean getExperiment(int expId) {
        DatasetBean exp = da.getExperiment(expId);
        return exp;
    }

    public Map<String, ProteinBean> getProteinsList(int expId) {
        Map<String, ProteinBean> proteinsList = da.getProteinsList(expId);
        return proteinsList;
    }

    public Map<Integer, PeptideBean> getPeptidesList(int expId) {
        Map<Integer, PeptideBean> peptidesList = da.getPeptidesList(expId);
        
        return peptidesList;
    }
    

    public Map<Integer, FractionBean> getFractionsList(int expId) {
        Map<Integer, FractionBean> fractionsList = da.getFractionsList(expId);
        return fractionsList;
    }

    ///new v-2
    public Map<Integer, ProteinBean> searchProtein(String accession, int expId, boolean validatedOnly) {
        Map<Integer, ProteinBean> protExpList = da.searchProtein(accession, expId, validatedOnly);
        return protExpList;
    }

    public Map<Integer, PeptideBean> getPeptidesProtList(Set<Integer> peptideIds) {
        Map<Integer, PeptideBean> peptidesProtList = da.getPeptidesProtList(peptideIds);
        return peptidesProtList;
    }

    public Map<Integer, FractionBean> getProteinFractionList(String accession,
            int expId) {
        Map<Integer, FractionBean> protionFractList = da.getProteinFractionList(accession, expId);
        return protionFractList;
    }

    public Map<Integer, ProteinBean> searchProteinByName(String protSearch, int expId, boolean validatedOnly) {
        Map<Integer, ProteinBean> proteinsList = da.searchProteinByName(protSearch, expId, validatedOnly);
        return proteinsList;
    }

    public Map<Integer, ProteinBean> searchProteinByPeptideSequence(String protSearch,
            int expId, boolean validatedOnly) {
        Map<Integer, ProteinBean> proteinsList = da.searchProteinByPeptideSequence(protSearch, expId, validatedOnly);
        return proteinsList;
    }

    public Map<Integer, PeptideBean> getPeptidesList(List<Integer> peptideIds,
            String accession) {
        Map<Integer, PeptideBean> peptidesProtList = da.getPeptidesList(peptideIds, accession);
        return peptidesProtList;
    }

    public Set<Integer> getExpPepProIds(int expId, String accession) {
        Set<Integer> expProPepIds = da.getExpPepProIds(expId, accession);

        return expProPepIds;
    }

    public List<StandardProteinBean> getStandardProtPlotList(int expId) {
        List<StandardProteinBean> standardPlotList = da.getStandardProtPlotList(expId);


        return standardPlotList;
    }

    public boolean updateExpData(DatasetBean exp) {
        boolean test = da.updateExpData(exp);
        return test;

    }
    private  Map<Integer, PeptideBean> updatePeptideList(Map<String, PeptideBean> gPepList, Map<Integer, PeptideBean> pepList) {
        Map<Integer, PeptideBean> updatedList = new HashMap<Integer, PeptideBean>();
        for (int index : pepList.keySet()) {
            PeptideBean pb = pepList.get(index);
            pb.setLikelyNotGlycosite(false);
             pb.setDeamidationAndGlycopattern(false);
             pb.setGlycopatternPositions("");
                
            String key = "[" + pb.getProtein() + "][" + pb.getSequenceTagged() + "]";
            if (gPepList.containsKey(key)) {
                PeptideBean temPb = gPepList.get(key);
                if (temPb.getGlycopatternPositions() != null) {
                    pb.setGlycopatternPositions(temPb.getGlycopatternPositions());
                }
                if (temPb.isDeamidationAndGlycopattern() != null) {
                    pb.setDeamidationAndGlycopattern(temPb.isDeamidationAndGlycopattern());
                    if(temPb.isDeamidationAndGlycopattern())
                        System.out.print("positive delmation is "+pb.getProtein());
                }
//                if (temPb.isLikelyNotGlycopeptide()) {
//                    pb.setLikelyNotGlycosite(temPb.isLikelyNotGlycopeptide());
//                }
            }
            updatedList.put(index, pb);
        }
        return updatedList;
    }

    public Map<Integer,Integer> getDatasetIndex() {
        return datasetIndex;
    }
}