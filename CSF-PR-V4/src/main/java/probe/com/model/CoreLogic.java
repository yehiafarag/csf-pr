package probe.com.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import probe.com.dal.DataAccess;
import probe.com.dal.Query;
import probe.com.model.beans.ComparisonProtein;
import probe.com.model.beans.IdentificationDataset;
import probe.com.model.beans.DatasetDetailsBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.GroupsComparison;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.model.beans.QuantProtein;
import probe.com.model.beans.StandardProteinBean;
import probe.com.model.beans.QuantDatasetListObject;
import probe.com.model.util.FileExporter;
import probe.com.model.util.FilesReader;

/**
 * @author Yehia Farag
 */
public class CoreLogic implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private final DataAccess da;
    private int mainDatasetId;
    private final String filesURL;
    private final TreeMap<Integer, String> datasetNamesList = new TreeMap<Integer, String>();//for dropdown select list
    private Map<Integer, IdentificationDataset> datasetList;
    private final Map<Integer, Integer> datasetIndex = new HashMap<Integer, Integer>();
    private final FileExporter exporter = new FileExporter();
    
    public CoreLogic(String url, String dbName, String driver, String userName, String password, String filesURL) {
        da = new DataAccess(url, dbName, driver, userName, password);

        //just to arrange the already stored datasets -->> to be removed in the future
        datasetIndex.put(8, 1);
        datasetIndex.put(14, 2);
        datasetIndex.put(4, 3);
        datasetIndex.put(17, 4);
        datasetIndex.put(15, 5);
        datasetIndex.put(16, 6);
        datasetIndex.put(9, 7);
        datasetList = da.getDatasets();
        this.filesURL = filesURL;
    }

    /**
     * get the datasets names required for initializing drop down select list
     *
     * @return datasetNamesList
     */
    public TreeMap<Integer, String> getDatasetNamesList() {
        if (datasetList == null) {
            datasetList = getDatasetList();
        }
        for (int datasetkey : datasetList.keySet()) {
            //for re-indexing the stored datasets, to be removed in the future
            if (datasetIndex.containsKey(datasetkey)) {
                IdentificationDataset dataset = datasetList.get(datasetkey);
                datasetNamesList.put(datasetIndex.get(datasetkey), "\t" + dataset.getName());

            } else {
                IdentificationDataset dataset = datasetList.get(datasetkey);
                datasetNamesList.put(datasetkey, "\t" + dataset.getName());
                datasetIndex.put(datasetkey, datasetkey);
            }
        }
        return datasetNamesList;

    }

    /**
     * read and store datasets files in the database
     *
     * @param file the dataset file
     * @param MIMEType the file type (txt or xls)
     * @param dataset dataset bean (in case of update existing dataset)
     * @return test boolean
     * @exception IOException
     * @exception SQLException
     */
    public boolean handelDatasetFile(File file, String MIMEType, IdentificationDataset dataset) throws IOException, SQLException {

        boolean test = false;

        if (dataset.getDatasetFile() == -100)//Standard plot file
        {
            test = da.updateStandardPlotProt(dataset);

        }

        return test;

    }
    /**
     * read and store Quant data files in the database
     *
     * @param file the dataset file
     * @param MIMEType the file type (csv)
     * @param dataset dataset bean (in case of update existing dataset)
     * @return test boolean
     * @exception IOException
     * @exception SQLException
     */
    public boolean handelQuantDataFile(File file, String MIMEType) throws IOException, SQLException {
        FilesReader fr = new FilesReader();
        boolean test = false;    
        List<QuantProtein> quantProtList  = fr.readCSVQuantFile(file);
        test = da.storeQuantProt(quantProtList);

        

        return test;

    }

    /**
     * get the available datasets
     *
     * @return datasetsList
     */
    public Map<Integer, IdentificationDataset> getDatasetList() {
        if (datasetList == null || datasetList.isEmpty()) {
            datasetList = da.getDatasets();
        }
        return datasetList;
    }

    /**
     * get selected dataset
     *
     * @param datasetId
     * @return dataset
     */
    public IdentificationDataset getDataset(int datasetId) {
        IdentificationDataset dataset = datasetList.get(datasetId);
        if (dataset == null) {
            dataset = da.getDataset(datasetId);
            datasetList.put(datasetId, dataset);
        }
        return dataset;
    }

    /**
     * get proteins map for especial dataset
     *
     * @param datasetId
     * @return proteinsList
     */
    public Map<String, IdentificationProteinBean> retriveProteinsList(int datasetId) {
        Map<String, IdentificationProteinBean> proteinsList = null;
//        if (datasetList.get(datasetId).getProteinList() == null || datasetList.get(datasetId).getProteinList().isEmpty()) {
            proteinsList = da.getProteinsList(datasetId);
//            datasetList.get(datasetId).setProteinList(proteinsList);
            
//        }
//        else{
//        proteinsList = datasetList.get(datasetId).getProteinList();
//        }

        return proteinsList;
    }

    /**
     * check if exporting file is available in export folder
     *
     * @param fileName
     * @return test boolean (available or not available)
     */
    public boolean checkFileAvailable(String fileName) {
        File f = new File(filesURL, fileName);
        boolean exist = f.exists();
        f = null;
        return exist;
    }

    /**
     * check if exporting file is available in export folder
     *
     * @param fileName
     * @return url string path to the file
     */
    public String getFileUrl(String fileName) {
        File f = new File(filesURL, fileName);
        String path = f.getPath();
        return path;
    }

    /**
     * get dataset peptides list
     *
     * @param datasetId
     * @return dataset peptide List
     */
//    public Map<Integer, PeptideBean> getPeptidesList(int datasetId) {
////        Map<Integer, PeptideBean> peptidesList = datasetList.get(datasetId).getPeptideList();
//        Map<Integer, PeptideBean> updatedPeptidesList = new HashMap<Integer, PeptideBean>();
////        if (peptidesList == null || peptidesList.isEmpty()) {
//
//           Map<Integer, PeptideBean>   peptidesList = da.getPeptidesList(datasetId);
//            Map<String, IdentificationProteinBean> protList = retriveProteinsList(datasetId);
//
//            for (int key : peptidesList.keySet()) {
//                PeptideBean pb = peptidesList.get(key);
//                if (pb.getProteinInference().equalsIgnoreCase("Single Protein")) {
//                    pb.setPeptideProteins(pb.getProtein());
//                    pb.setPeptideProteinsDescriptions(datasetList.get(datasetId).getProteinList().get(pb.getProtein()).getDescription());
//                } else if (pb.getProteinInference().trim().equalsIgnoreCase("Related Proteins") && (!pb.getProtein().equalsIgnoreCase("SHARED PEPTIDE"))) {
//                    String desc = "";
//                    if (pb.getOtherProteins() == null || pb.getOtherProteins().trim().equalsIgnoreCase("")) {
//                        pb.setPeptideProteins(pb.getProtein() + "," + pb.getPeptideProteins());
//                        desc = protList.get(pb.getProtein()).getDescription() + ";" + pb.getPeptideProteinsDescriptions();
//                    } else {
//                        desc = protList.get(pb.getProtein() + "," + pb.getOtherProteins().replaceAll("\\p{Z}", "")).getDescription() + ";" + pb.getOtherProteinDescriptions();
//                    }
//                    pb.setPeptideProteinsDescriptions(desc);
//                }
//                updatedPeptidesList.put(key, pb);
//            }
////        } else {
////            updatedPeptidesList.putAll(peptidesList);
////        }
//        return updatedPeptidesList;
//    }

    /**
     * get dataset peptides list (valid peptides or all peptides)
     *
     * @param datasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public Map<Integer, PeptideBean> getAllDatasetPeptidesList(int datasetId, boolean validated) {

         return da.getPeptidesList(datasetId,validated);
//        Map<Integer, PeptideBean> peptidesList = getPeptidesList(datasetId);
//        if (validated) {
//            Map<Integer, PeptideBean> validatedPtidesList = new HashMap<Integer, PeptideBean>();
//            int x = 0;
//            for (PeptideBean pb : peptidesList.values()) {
//
//                if (pb.getValidated() == 1.0) {
//                    validatedPtidesList.put(x++, pb);
//                }
//            }
//            return validatedPtidesList;
//        } else {
//            return peptidesList;
//        }
    }
    
    
    /**
     * get dataset peptides number (valid peptides or all peptides)
     *
     * @param datasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public int getAllDatasetPeptidesNumber(int datasetId, boolean validated) {

   return da.getAllDatasetPeptidesNumber(datasetId, validated);
    }

    /**
     * get dataset fractions list
     *
     * @param datasetId
     * @return fractions list for the selected dataset
     */
    public Map<Integer, IdentificationProteinBean> getProtGelFractionsList(int datasetId,String accession,String otherAccession) {
//        Map<Integer, FractionBean> fractionsList;
//        if (datasetList.containsKey(datasetId) && datasetList.get(datasetId).getFractionsList() != null && (!datasetList.get(datasetId).getFractionsList().isEmpty())) {
//            //check if dataset updated if not
//            fractionsList = datasetList.get(datasetId).getFractionsList();
//
//        } else {
            return da.getProtGelFractionsList(datasetId,accession, otherAccession);
            
            
//            datasetList.get(datasetId).setFractionsList(fractionsList);
//        }
//
//        return fractionsList;
    }

    ///new v-2
    /**
     * search for proteins by accession keywords
     *
     * @param searchArr array of query words
     * @param searchDatasetType type of search
     * @param validatedOnly only validated proteins results
     * @return datasetProtList
     */
    public Map<Integer, IdentificationProteinBean> searchProteinByAccession(String searchArr, String searchDatasetType, boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> datasetProtList = new HashMap<Integer, IdentificationProteinBean>();
        IdentificationDataset dataset = null;
        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
            for (IdentificationDataset tempDataset : datasetList.values()) {
                if (tempDataset.getName().equalsIgnoreCase(searchDatasetType)) {
                    dataset = tempDataset;
                    break;

                }
            }
            if (dataset != null) {
                datasetProtList = searchProteinByAccession(searchArr, dataset.getDatasetId(), validatedOnly);
            }
        } else {
            for (IdentificationDataset tempDataset : datasetList.values()) {
                Map<Integer, IdentificationProteinBean> protTempList = searchProteinByAccession(searchArr, tempDataset.getDatasetId(), validatedOnly);
                if (protTempList != null) {
                    datasetProtList.putAll(protTempList);
                }

            }
        }

        return datasetProtList;

    }

    /**
     * search for proteins by accession keywords
     *
     * @param accession array of query words
     * @param datasetId
     * @param validatedOnly only validated proteins results
     * @return datasetProtList
     */
    private Map<Integer, IdentificationProteinBean> searchProteinByAccession(String accession, int datasetId, boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> protDatasetpList = da.searchProteinByAccession(accession, datasetId, validatedOnly);
        return protDatasetpList;
    }

    /**
     * get peptides list for selected protein in selected dataset
     *
     * @param datasetId
     * @param accession
     * @param otherAccession
     * @return peptides list for the selected protein group in the selected
     * dataset
     */
    public Map<Integer, PeptideBean> getPeptidesProtList(int datasetId, String accession, String otherAccession) {
//        Set<Integer> peptideIds = this.getDatasetProteinPeptidesIds(datasetId, accession, otherAccession);
        Map<Integer, PeptideBean> peptidesProtList = da.getPeptidesList(accession, otherAccession, datasetId);
//        datasetList.get(datasetId).setPeptidesIds(peptideIds);
        return peptidesProtList;
    }
    
   
    
    

    /**
     * search for proteins by description keywords
     *
     * @param proteinDescriptionKeyword array of query words
     * @param searchDatasetType type of search
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchProteinByName(String proteinDescriptionKeyword, String searchDatasetType, boolean validatedOnly) {

        Map<Integer, IdentificationProteinBean> datasetProteinsSearchList = new HashMap<Integer, IdentificationProteinBean>();

        IdentificationDataset dataset = null;
        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
            for (IdentificationDataset tempDataset : datasetList.values()) {
                if (tempDataset.getName().equalsIgnoreCase(searchDatasetType)) {
                    dataset = tempDataset;
                    break;

                }
            }
            if (dataset != null) {
                datasetProteinsSearchList = searchProteinByName(proteinDescriptionKeyword, dataset.getDatasetId(), validatedOnly);
            }

        } else {
            for (IdentificationDataset tempDataset : datasetList.values()) {
                datasetProteinsSearchList.putAll(searchProteinByName(proteinDescriptionKeyword, tempDataset.getDatasetId(), validatedOnly));

            }
        }

        return datasetProteinsSearchList;

    }

    /**
     * search for proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    private Map<Integer, IdentificationProteinBean> searchProteinByName(String protSearchKeyword, int datasetId, boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> proteinsList = da.searchProteinByName(protSearchKeyword, datasetId, validatedOnly);
        return proteinsList;
    }

    /**
     * search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param searchDatasetType type of search
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword, String searchDatasetType, boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> protDatasetList = new HashMap<Integer, IdentificationProteinBean>();
        IdentificationDataset dataset = null;
        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
            for (IdentificationDataset tempDataset : datasetList.values()) {
                if (tempDataset.getName().equalsIgnoreCase(searchDatasetType)) {
                    dataset = tempDataset;
                    break;

                }
            }
            if (dataset != null) {
                protDatasetList = searchProteinByPeptideSequence(peptideSequenceKeyword, dataset.getDatasetId(), validatedOnly);
            }

        } else {
            for (IdentificationDataset tempDataset : datasetList.values()) {
                Map<Integer, IdentificationProteinBean> protTempList = searchProteinByPeptideSequence(peptideSequenceKeyword, tempDataset.getDatasetId(), validatedOnly);
                if (protTempList != null && (!protTempList.isEmpty())) {
                    protDatasetList.putAll(protTempList);
                }

            }
        }
        return protDatasetList;

    }

    /**
     * search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword,
            int datasetId, boolean validatedOnly) {
//        Map<Integer, IdentificationProteinBean> proteinsList = da.searchProteinByPeptideSequence(peptideSequenceKeyword, datasetId, validatedOnly);
        return null;
    }

    /**
     * get proteins fractions average list
     *
     * @param accession
     * @param datasetId
     * @return dataset peptide List
     */
    public Map<Integer, FractionBean> getProteinFractionList(String accession,
            int datasetId) {
        Map<Integer, FractionBean> protionFractList = da.getProteinFractionList(accession, datasetId);
        return protionFractList;
    }

    /**
     * get peptides data for a database using peptides ids
     *
     * @param peptideIds
     * @return list of peptides
     */
//    public Map<Integer, PeptideBean> getPeptidesList(List<Integer> peptideIds) {
//        Map<Integer, PeptideBean> peptidesProtList = da.getPeptidesList(peptideIds);
//        return peptidesProtList;
//    }

    /**
     * get peptides id list for selected protein in selected dataset
     *
     * @param datasetId
     * @param accession
     * @param otherAccession
     * @return peptides id list for the selected protein group in the selected
     * dataset
     */
    private Set<Integer> getDatasetProteinPeptidesIds(int datasetId, String accession, String otherAccession) {
        Set<Integer> datasetProteinPeptidesIds = da.getDatasetProteinsPeptidesIds(datasetId, accession);
        if (otherAccession != null && !otherAccession.equals("")) {
            String[] otherAccessionArr = otherAccession.split(",");
            for (String str : otherAccessionArr) {
                datasetProteinPeptidesIds.addAll(da.getDatasetProteinsPeptidesIds(datasetId, str.trim()));
            }
        }

        return datasetProteinPeptidesIds;
    }

    /**
     * retrieve standard proteins data for fraction plot
     */
//    public List<StandardProteinBean> retriveStandardProtPlotList(int datasetId) {
//        return da.getStandardProtPlotList(datasetId);
////        getMainDataset().setStanderdPlotProt(standardPlotList);
//    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param datasetId
     */
    public List<StandardProteinBean>  retriveStandardProtPlotList(int datasetId) {
        if(datasetList.get(datasetId).getStanderdPlotProt() != null )
            return datasetList.get(datasetId).getStanderdPlotProt();
        else
            return da.getStandardProtPlotList(datasetId);
//        getMainDataset().setStanderdPlotProt(standardPlotList);
    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param dataset
     * @return test boolean
     */
    public boolean updateDatasetData(IdentificationDataset dataset) {
        boolean test = da.updateDatasetData(dataset);
        return test;

    }

    /**
     * get datasetIndex List to be removed in the future this function to
     * re-arrange the already stored datasets in the database and return the
     * dataset id
     *
     * @return datasetIndexList
     */
    public Map<Integer, Integer> getDatasetIndexList() {
        return datasetIndex;
    }

    /**
     * get the main dataset
     *
     * @return mainDataset
     */
    public int getMainDataset() {
        return mainDatasetId;
    }

    public int getDatasetKey(String datasetString) {
        for (int key1 : datasetNamesList.keySet()) {
            if (datasetString.equalsIgnoreCase(datasetNamesList.get(key1))) {
                for (int k : datasetIndex.keySet()) {
                    int value = datasetIndex.get(k);
                    if (value == key1) {
                        return k;
                    }
                }
                return datasetIndex.get(key1);
            }
        }
        return 0;
    }

    /**
     * set the main dataset selected by user
     *
     * @param datasetString string from drop down select list
     * @return datasetId
     */
    public int setMainDataset(String datasetString) {
        for (int tempDatasetIndex : datasetNamesList.keySet()) {
            if (datasetString.trim().equalsIgnoreCase(datasetNamesList.get(tempDatasetIndex).trim())) {

                for (int k : datasetIndex.keySet()) {
                    int value = datasetIndex.get(k);
                    if (value == tempDatasetIndex) {
                        setMainDataset(k);
                        return k;
                    }
                }
                setMainDataset(tempDatasetIndex);
                return tempDatasetIndex;
            }
        }
        return 0;
    }

    /**
     * set the selected dataset as main dataset in the logic layer
     *
     * @param datasetId
     */
    public void setMainDataset(int datasetId) {
        this.mainDatasetId = datasetId;
    }

    /**
     * get dataset details list that has basic information for datasets
     *
     * @return datasetDetailsList
     */
    public Map<Integer, DatasetDetailsBean> getDatasetDetailsList() {
        Map<Integer, DatasetDetailsBean> datasetDetailsList = new HashMap<Integer, DatasetDetailsBean>();
        for (IdentificationDataset dataset : datasetList.values()) {
            DatasetDetailsBean datasetDetails = new DatasetDetailsBean();
            datasetDetails.setName(dataset.getName());
            datasetDetails.setFragMode(dataset.getFragMode());
            datasetDetails.setInstrumentType(dataset.getInstrumentType());
            datasetDetails.setSampleType(dataset.getSampleType());
            datasetDetails.setSampleProcessing(dataset.getSampleProcessing());
            datasetDetails.setSpecies(dataset.getSpecies());
            datasetDetailsList.put(dataset.getDatasetId(), datasetDetails);

        }
        return datasetDetailsList;

    }

    /**
     * get number of validated peptides for protein
     *
     * @param pepProtList
     *
     * @return number of valid peptides
     */
    public int getValidatedPepNumber(Map<Integer, PeptideBean> pepProtList) {
        int count = 0;
        for (PeptideBean pb : pepProtList.values()) {
            if (pb.getValidated() == 1.0) {
                count++;
            }
        }
        return count;
    }

    public TreeMap<Integer, Object> getSearchIndexesSet(Map<String, Integer> searchMap, Map<String, Integer> searchMapIndex, String keySearch) {
        TreeMap<Integer, Object> treeSet = new TreeMap<Integer, Object>();
        for (String key : searchMap.keySet()) {
            if (key.contains(keySearch)) {
                treeSet.put(searchMapIndex.get(key), searchMap.get(key));
            }
        }

        return treeSet;
    }
    
     /**
     * get validated proteins list
     *
     * @param proteinsList
     *
     * @return vProteinsList list of valid proteins
     */
    public Map<Integer, IdentificationProteinBean> getValidatedProteinsList(Map<Integer, IdentificationProteinBean> proteinsList) {
        Map<Integer, IdentificationProteinBean> vProteinsList = new HashMap<Integer, IdentificationProteinBean>();
        for (int str : proteinsList.keySet()) {
            IdentificationProteinBean pb = proteinsList.get(str);
            if (pb.isValidated()) {
                vProteinsList.put(str, pb);
            }

        }
        return vProteinsList;

    }

    public QuantDatasetListObject getQuantDatasetListObject() {
        return da.getQuantDatasetListObject();

    }
    
     public boolean[] getActiveFilters(){
     return da.getActiveFilters();
     
     }

    /**
     * this function to be use for csv peptides exporting with large datasets
     *
     * @param datasetId
     * @param validated boolean the peptides type
     * @param datasetName
     * @param dataType validated/all
     * @param exportFileType csv or xls
     */
    public void exportPeptidesToFile(int datasetId, boolean validated, String datasetName, String dataType,String exportFileType) {
        Map<Integer, PeptideBean> allPeptides = getAllDatasetPeptidesList(datasetId, validated);
        if(exportFileType.equalsIgnoreCase("csv"))
            exporter.expotPeptidesToCSV(allPeptides, datasetName, dataType, filesURL);
        else            
            exporter.expotPeptidesToXLS(allPeptides, datasetName, dataType, filesURL);
    }
    
    
    
    
    
    /*             *********************************************************8       */
     /**
     * search for proteins by description keywords
     *
     * @param query   query words
     * @return datasetProteinsSearchList
     */
    public Map<Integer, IdentificationProteinBean> searchIdentficationProtein(Query query) {

        Map<Integer, IdentificationProteinBean> datasetProteinsSearchList = new HashMap<Integer, IdentificationProteinBean>();
        if (query.getSearchDataType().equals("Identification Data")) {
            if (query.getSearchDataset() == null || query.getSearchDataset().isEmpty())//search in all identification datasets
            {
                if (query.getSearchBy().equalsIgnoreCase("Protein Accession"))//"Protein Name" "Peptide Sequence"
                {
                    return da.searchIdentificationProteinAllDatasetsByAccession(query.getSearchKeyWords(), query.isValidatedProteins());
                } else if (query.getSearchBy().equalsIgnoreCase("Protein Name")) {
                    return da.searchProteinAllDatasetsByName(query.getSearchKeyWords(), query.isValidatedProteins());

                } else if (query.getSearchBy().equalsIgnoreCase("Peptide Sequence")) {

                    return da.SearchProteinAllDatasetsByPeptideSequence(query.getSearchKeyWords(), query.isValidatedProteins());
                }

            }
            else{ //search for identification data in special dataset  "Quantification", "Both"
             int tempDatasetIndex=-1;
             for(IdentificationDataset ds:datasetList.values())
                    {
                        if (ds.getName().trim().equalsIgnoreCase(query.getSearchDataset().trim())){
                            tempDatasetIndex = ds.getDatasetId();
                            System.out.println("dataset Index "+tempDatasetIndex);
                            break;
                        }
                    }
             if (query.getSearchBy().equalsIgnoreCase("Protein Accession"))//"Protein Name" "Peptide Sequence"
                {
                    return da.searchProteinByAccession(query.getSearchKeyWords(),tempDatasetIndex, query.isValidatedProteins());
                } else if (query.getSearchBy().equalsIgnoreCase("Protein Name")) {
                    return da.searchProteinByName(query.getSearchKeyWords(),tempDatasetIndex, query.isValidatedProteins());

                } else if (query.getSearchBy().equalsIgnoreCase("Peptide Sequence")) {

                    return da.SearchProteinByPeptideSequence(query.getSearchKeyWords(),tempDatasetIndex, query.isValidatedProteins());
                }
                
            }
        
        
        }else {
        //quantification and both

        }

//        IdentificationDataset dataset = null;
//        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
//            for (IdentificationDataset tempDataset : datasetList.values()) {
//                if (tempDataset.getName().equalsIgnoreCase(searchDatasetType)) {
//                    dataset = tempDataset;
//                    break;
//
//                }
//            }
//            if (dataset != null) {
//                datasetProteinsSearchList = searchProteinByName(proteinDescriptionKeyword, dataset.getDatasetId(), validatedOnly);
//            }
//
//        } 
        return datasetProteinsSearchList;

    }

    /**
     * search for proteins by description keywords
     *
     * @param query query words
     * @return datasetProteinsSearchList
     */
    public List<QuantProtein> searchQuantificationProteins(Query query) {

        if (query.getSearchDataType().equals("Quantification Data")) {
            List<QuantProtein> datasetQuantificationProteinsSearchList = da.searchQuantificationProteins(query);
            return datasetQuantificationProteinsSearchList;
        }
        return null;

    }

    /**
     * search for proteins by protein description keywords
     *
     * @param protSearchKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
//    private Map<Integer, IdentificationProteinBean> searchProteinByName(String protSearchKeyword, int datasetId, boolean validatedOnly) {
//        Map<Integer, IdentificationProteinBean> proteinsList = da.searchProteinByName(protSearchKeyword, datasetId, validatedOnly);
//        return proteinsList;
//    }
    /**
     * search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param searchDatasetType type of search
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
////    public Map<Integer, IdentificationProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword, String searchDatasetType, boolean validatedOnly) {
////        Map<Integer, IdentificationProteinBean> protDatasetList = new HashMap<Integer, IdentificationProteinBean>();
////        IdentificationDataset dataset = null;
////        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
////            for (IdentificationDataset tempDataset : datasetList.values()) {
////                if (tempDataset.getName().equalsIgnoreCase(searchDatasetType)) {
////                    dataset = tempDataset;
////                    break;
////
////                }
////            }
////            if (dataset != null) {
////                protDatasetList = searchProteinByPeptideSequence(peptideSequenceKeyword, dataset.getDatasetId(), validatedOnly);
////            }
////
////        } else {
////            for (IdentificationDataset tempDataset : datasetList.values()) {
////                Map<Integer, IdentificationProteinBean> protTempList = searchProteinByPeptideSequence(peptideSequenceKeyword, tempDataset.getDatasetId(), validatedOnly);
////                if (protTempList != null && (!protTempList.isEmpty())) {
////                    protDatasetList.putAll(protTempList);
////                }
////
////            }
////        }
////        return protDatasetList;
////
////    }
    /**
     * search for proteins by peptide sequence keywords
     *
     * @param peptideSequenceKeyword array of query words
     * @param datasetId dataset Id
     * @param validatedOnly only validated proteins results
     * @return datasetProteinsSearchList
     */
//    public Map<Integer, IdentificationProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword,
//            int datasetId, boolean validatedOnly) {
//        Map<Integer, IdentificationProteinBean> proteinsList = da.searchProteinByPeptideSequence(peptideSequenceKeyword, datasetId, validatedOnly);
//        return proteinsList;
//    }
    /**
     * get quant proteins for comparison table
     *
     * @param selectedComparisonList selected groups comparison list
     *
     * @return updated Selected Comparison List 
     */
    public Set<GroupsComparison>  getComparisonProtList(Set<GroupsComparison> selectedComparisonList) {
        Set<GroupsComparison> updatedSelectedComparisonList = new HashSet<GroupsComparison>();
        for (GroupsComparison comparison : selectedComparisonList) {
            Set<QuantProtein> comparisonMap = new HashSet<QuantProtein>();
            for (int dsID : comparison.getDatasetIndexes()) {
                comparisonMap.addAll(da.getQuantificationProteins(dsID));

            }
            Map<String, ComparisonProtein> comparProtList = new HashMap<String, ComparisonProtein>();
            for (QuantProtein quant : comparisonMap) {
                if (!comparProtList.containsKey(quant.getUniprotAccession())) {
                    comparProtList.put(quant.getUniprotAccession(), new ComparisonProtein(comparison.getDatasetIndexes().length));
                }
                ComparisonProtein comProt = comparProtList.get(quant.getUniprotAccession());

                if (quant.getStringFCValue().equalsIgnoreCase("Decreased")) {
                    comProt.addDown(1);
                } else if (quant.getStringFCValue().equalsIgnoreCase("Increased")) {
                    comProt.addUp(1);
                } else if (quant.getStringFCValue().equalsIgnoreCase("Not Provided")) {
                    comProt.addNotProvided(1);
                } else if (quant.getStringFCValue().equalsIgnoreCase("Not Regulated")) {
                    comProt.addNotReg(1);
                }
                comProt.setUniProtAccess(quant.getUniprotAccession());
                String protName = quant.getUniprotProteinName();
                if (protName == null || protName.equalsIgnoreCase("Not Available")) {
                    protName = quant.getPublicationProteinName();
                }
                comProt.setProtName(protName);
                comparProtList.put(quant.getUniprotAccession(), comProt);
            }
            //sort the protiens map
             Map<String, ComparisonProtein> sortedcomparProtList = new TreeMap<String, ComparisonProtein>(Collections.reverseOrder());
             for(String Key:comparProtList.keySet()){
                 ComparisonProtein temp = comparProtList.get(Key);
                 sortedcomparProtList.put((temp.getUp()+"_"+Key), temp);
             }
            
            comparison.setComparProtsMap(sortedcomparProtList);
            updatedSelectedComparisonList.add(comparison);

        }

        return updatedSelectedComparisonList;

        
//        
//        Map<String, Map<String, ComparisonProtein>> comProtList = new HashMap<String, Map<String, ComparisonProtein>>();
//        int compcounter = 0;
//        int protcounter = 0;
//        Set<QuantProtein> comparisonMap = new HashSet<QuantProtein>();
//        for (QuantDatasetObject ds : datasets) {
//            String key = ds.getPatientsGroup1().toLowerCase() + "-" + ds.getPatientsSubGroup1().toLowerCase() + "-" + ds.getPatientsGroup2().toLowerCase() + "-" + ds.getPatientsSubGroup2().toLowerCase();
//
//            comparisonMap = (da.getQuantificationProteins(ds.getUniqId()));
//            for (QuantProtein quant : comparisonMap) {
//                if (!comProtList.containsKey(quant.getUniprotAccession())) {
//                    comProtList.put(quant.getUniprotAccession(), new HashMap<String, ComparisonProtein>());
//                    compcounter++;
//                }
//                Map<String, ComparisonProtein> comProtmap = comProtList.get(quant.getUniprotAccession());
//                if (!comProtmap.containsKey(key)) {
//                    comProtmap.put(key, new ComparisonProtein());
//                    protcounter++;
//
//                }
//
//                ComparisonProtein comProt = comProtmap.get(key);
//                if (quant.getStringFCValue().equalsIgnoreCase("Decreased")) {
//                    comProt.addDown(1);
//                } else if (quant.getStringFCValue().equalsIgnoreCase("Increased")) {
//                    comProt.addUp(1);
//                } else if (quant.getStringFCValue().equalsIgnoreCase("Not Provided")) {
//                    comProt.addNotProvided(1);
//                } else if (quant.getStringFCValue().equalsIgnoreCase("Not Regulated")) {
//                    comProt.addNotReg(1);
//                }
//                comProtmap.put(key, comProt);
//                comProtList.put(quant.getUniprotAccession(), comProtmap);
//            }
//
//        }
//
//        
//          ComparisonProtein[][] compArr = new ComparisonProtein[protcounter][compcounter];
//          int x = 0;
//          int y = 0;
//         for (String key : comProtList.keySet()) {
//                Map<String, ComparisonProtein> protList = comProtList.get(key);
//                for (String key2 : protList.keySet()) {                    
//                    ComparisonProtein prot = protList.get(key2);
//                    compArr[x][y]=prot;
//                    y++;
//                    
//                    
//                }
//                x++;
//           }
//
//          
//        
//        return comProtList;
    
    
    } 
    
   

    
}
