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
import probe.com.model.beans.DatasetDetailsBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.model.util.FileExporter;

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
    private Map<Integer, DatasetBean> datasetList;
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
    public boolean handelDatasetFile(File file, String MIMEType, DatasetBean dataset) throws IOException, SQLException {

        boolean test = false;

        if (dataset.getDatasetFile() == -100)//Standard plot file
        {
            test = da.updateStandardPlotProt(dataset);

        }

        return test;

    }

    /**
     * get the available datasets
     *
     * @return datasetsList
     */
    public Map<Integer, DatasetBean> getDatasetList() {
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
    public DatasetBean getDataset(int datasetId) {
        DatasetBean dataset = datasetList.get(datasetId);
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
    public Map<String, ProteinBean> retriveProteinsList(int datasetId) {
        Map<String, ProteinBean> proteinsList = null;
        if (datasetList.get(datasetId).getProteinList() == null || datasetList.get(datasetId).getProteinList().isEmpty()) {
            proteinsList = da.getProteinsList(datasetId);
            datasetList.get(datasetId).setProteinList(proteinsList);
            
        }
        else{
        proteinsList = datasetList.get(datasetId).getProteinList();
        }

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
    public Map<Integer, PeptideBean> getPeptidesList(int datasetId) {
//        Map<Integer, PeptideBean> peptidesList = datasetList.get(datasetId).getPeptideList();
        Map<Integer, PeptideBean> updatedPeptidesList = new HashMap<Integer, PeptideBean>();
//        if (peptidesList == null || peptidesList.isEmpty()) {

           Map<Integer, PeptideBean>   peptidesList = da.getPeptidesList(datasetId);
            Map<String, ProteinBean> protList = retriveProteinsList(datasetId);

            for (int key : peptidesList.keySet()) {
                PeptideBean pb = peptidesList.get(key);
                if (pb.getProteinInference().equalsIgnoreCase("Single Protein")) {
                    pb.setPeptideProteins(pb.getProtein());
                    pb.setPeptideProteinsDescriptions(datasetList.get(datasetId).getProteinList().get(pb.getProtein()).getDescription());
                } else if (pb.getProteinInference().trim().equalsIgnoreCase("Related Proteins") && (!pb.getProtein().equalsIgnoreCase("SHARED PEPTIDE"))) {
                    String desc = "";
                    if (pb.getOtherProteins() == null || pb.getOtherProteins().trim().equalsIgnoreCase("")) {
                        pb.setPeptideProteins(pb.getProtein() + "," + pb.getPeptideProteins());
                        desc = protList.get(pb.getProtein()).getDescription() + ";" + pb.getPeptideProteinsDescriptions();
                    } else {
                        desc = protList.get(pb.getProtein() + "," + pb.getOtherProteins().replaceAll("\\p{Z}", "")).getDescription() + ";" + pb.getOtherProteinDescriptions();
                    }
                    pb.setPeptideProteinsDescriptions(desc);
                }
                updatedPeptidesList.put(key, pb);
            }
//        } else {
//            updatedPeptidesList.putAll(peptidesList);
//        }
        return updatedPeptidesList;
    }

    /**
     * get dataset peptides list (valid peptides or all peptides)
     *
     * @param datasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public Map<Integer, PeptideBean> getAllDatasetPeptidesList(int datasetId, boolean validated) {

        Map<Integer, PeptideBean> peptidesList = getPeptidesList(datasetId);
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
    
    
    /**
     * get dataset peptides number (valid peptides or all peptides)
     *
     * @param datasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public int getAllDatasetPeptidesNumber(int datasetId, boolean validated) {

        Map<Integer, PeptideBean> peptidesList =  da.getPeptidesList(datasetId);
       int index = 0;
        if (validated) {
           int x = 0;
            for (PeptideBean pb : peptidesList.values()) {

                if (pb.getValidated() == 1.0) {
                   index++;
                }
            }
            return index;
        } else {
            return peptidesList.size();
        }
    }

    /**
     * get dataset fractions list
     *
     * @param datasetId
     * @return fractions list for the selected dataset
     */
    public Map<Integer, FractionBean> getFractionsList(int datasetId) {
        Map<Integer, FractionBean> fractionsList;
        if (datasetList.containsKey(datasetId) && datasetList.get(datasetId).getFractionsList() != null && (!datasetList.get(datasetId).getFractionsList().isEmpty())) {
            //check if dataset updated if not
            fractionsList = datasetList.get(datasetId).getFractionsList();

        } else {
            fractionsList = da.getFractionsList(datasetId);
            datasetList.get(datasetId).setFractionsList(fractionsList);
        }

        return fractionsList;
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
    public Map<Integer, ProteinBean> searchProteinByAccession(String searchArr, String searchDatasetType, boolean validatedOnly) {
        Map<Integer, ProteinBean> datasetProtList = new HashMap<Integer, ProteinBean>();
        DatasetBean dataset = null;
        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
            for (DatasetBean tempDataset : datasetList.values()) {
                if (tempDataset.getName().equalsIgnoreCase(searchDatasetType)) {
                    dataset = tempDataset;
                    break;

                }
            }
            if (dataset != null) {
                datasetProtList = searchProteinByAccession(searchArr, dataset.getDatasetId(), validatedOnly);
            }
        } else {
            for (DatasetBean tempDataset : datasetList.values()) {
                Map<Integer, ProteinBean> protTempList = searchProteinByAccession(searchArr, tempDataset.getDatasetId(), validatedOnly);
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
    private Map<Integer, ProteinBean> searchProteinByAccession(String accession, int datasetId, boolean validatedOnly) {
        Map<Integer, ProteinBean> protDatasetpList = da.searchProteinByAccession(accession, datasetId, validatedOnly);
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
        Set<Integer> peptideIds = this.getDatasetProteinPeptidesIds(datasetId, accession, otherAccession);
        Map<Integer, PeptideBean> peptidesProtList = da.getPeptidesList(peptideIds);
        datasetList.get(datasetId).setPeptidesIds(peptideIds);
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
    public Map<Integer, ProteinBean> searchProteinByName(String proteinDescriptionKeyword, String searchDatasetType, boolean validatedOnly) {

        Map<Integer, ProteinBean> datasetProteinsSearchList = new HashMap<Integer, ProteinBean>();

        DatasetBean dataset = null;
        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
            for (DatasetBean tempDataset : datasetList.values()) {
                if (tempDataset.getName().equalsIgnoreCase(searchDatasetType)) {
                    dataset = tempDataset;
                    break;

                }
            }
            if (dataset != null) {
                datasetProteinsSearchList = searchProteinByName(proteinDescriptionKeyword, dataset.getDatasetId(), validatedOnly);
            }

        } else {
            for (DatasetBean tempDataset : datasetList.values()) {
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
    private Map<Integer, ProteinBean> searchProteinByName(String protSearchKeyword, int datasetId, boolean validatedOnly) {
        Map<Integer, ProteinBean> proteinsList = da.searchProteinByName(protSearchKeyword, datasetId, validatedOnly);
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
    public Map<Integer, ProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword, String searchDatasetType, boolean validatedOnly) {
        Map<Integer, ProteinBean> protDatasetList = new HashMap<Integer, ProteinBean>();
        DatasetBean dataset = null;
        if (!searchDatasetType.equalsIgnoreCase("Search All Datasets")) {
            for (DatasetBean tempDataset : datasetList.values()) {
                if (tempDataset.getName().equalsIgnoreCase(searchDatasetType)) {
                    dataset = tempDataset;
                    break;

                }
            }
            if (dataset != null) {
                protDatasetList = searchProteinByPeptideSequence(peptideSequenceKeyword, dataset.getDatasetId(), validatedOnly);
            }

        } else {
            for (DatasetBean tempDataset : datasetList.values()) {
                Map<Integer, ProteinBean> protTempList = searchProteinByPeptideSequence(peptideSequenceKeyword, tempDataset.getDatasetId(), validatedOnly);
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
    public Map<Integer, ProteinBean> searchProteinByPeptideSequence(String peptideSequenceKeyword,
            int datasetId, boolean validatedOnly) {
        Map<Integer, ProteinBean> proteinsList = da.searchProteinByPeptideSequence(peptideSequenceKeyword, datasetId, validatedOnly);
        return proteinsList;
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
    public Map<Integer, PeptideBean> getPeptidesList(List<Integer> peptideIds) {
        Map<Integer, PeptideBean> peptidesProtList = da.getPeptidesList(peptideIds);
        return peptidesProtList;
    }

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
    public void retriveStandardProtPlotList() {
        List<StandardProteinBean> standardPlotList = da.getStandardProtPlotList(mainDatasetId);
        getMainDataset().setStanderdPlotProt(standardPlotList);
    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param datasetId
     */
    public void retriveStandardProtPlotList(int datasetId) {
        List<StandardProteinBean> standardPlotList = da.getStandardProtPlotList(datasetId);
        getMainDataset().setStanderdPlotProt(standardPlotList);
    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param dataset
     * @return test boolean
     */
    public boolean updateDatasetData(DatasetBean dataset) {
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
    public DatasetBean getMainDataset() {
        return datasetList.get(mainDatasetId);
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
        for (DatasetBean dataset : datasetList.values()) {
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

    public TreeMap<Integer, Integer> getSearchIndexesSet(Map<String, Integer> searchMap, Map<String, Integer> searchMapIndex, String keySearch) {
        TreeMap<Integer, Integer> treeSet = new TreeMap<Integer, Integer>();
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
    public Map<Integer, ProteinBean> getValidatedProteinsList(Map<Integer, ProteinBean> proteinsList) {
        Map<Integer, ProteinBean> vProteinsList = new HashMap<Integer, ProteinBean>();
        for (int str : proteinsList.keySet()) {
            ProteinBean pb = proteinsList.get(str);
            if (pb.isValidated()) {
                vProteinsList.put(str, pb);
            }

        }
        return vProteinsList;

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

    
}
