package probe.com.handlers;

import java.sql.SQLException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.dal.Query;
import probe.com.model.CoreLogic;
import probe.com.model.beans.IdentificationDataset;
import probe.com.model.beans.DatasetDetailsBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.model.beans.QuantificationProteinsBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.components.PeptideTable;

/**
 * @admin Yehia Farag
 */
public class MainHandler implements Serializable {

    private static final long serialVersionUID = 1L;
    private final CoreLogic computing;
    private final AuthenticatorHandler authenticatorHandler;

    /**
     *
     * @param url database url
     * @param dbName database name
     * @param driver database driver
     * @param userName database username
     * @param password database password
     */
    public MainHandler(String url, String dbName, String driver, String userName, String password, String filesURL) {
        computing = new CoreLogic(url, dbName, driver, userName, password, filesURL);
        authenticatorHandler = new AuthenticatorHandler(url, dbName, driver, userName, password);
    }

    /**
     * get the datasets names required for initializing drop down select list
     *
     * @return datasetNamesList
     */
    public TreeMap<Integer, String> getDatasetNamesList() {
        return computing.getDatasetNamesList();
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
        test = computing.handelDatasetFile(file, MIMEType, dataset);
        return test;

    }

    /**
     * get the available datasets
     *
     * @return datasetsList
     */
    public Map<Integer, IdentificationDataset> getDatasetList() {
        return computing.getDatasetList();
    }

    /**
     * get datasetIndex List to be removed in the future this function to
     * re-arrange the already stored datasets in the database and return the
     * dataset id
     *
     * @return datasetIndexList
     */
    public Map<Integer, Integer> getDatasetIndexList() {
        return computing.getDatasetIndexList();
    }

    /**
     * get proteins map for especial dataset
     *
     * @param datasetId
     * @return proteinsList
     */
    public Map<String, IdentificationProteinBean> retriveProteinsList(int datasetId) {
        Map<String, IdentificationProteinBean> protList = computing.retriveProteinsList(datasetId);
        return protList;
    }

    /**
     * set the selected dataset as main dataset in the logic layer
     *
     * @param datasetId
     */
    public void setMainDataset(int datasetId) {
        computing.setMainDataset(datasetId);
    }

    /**
     * set the selected dataset as main dataset in the logic layer
     *
     * @param datasetString
     * @return datasetId
     */
    public int setMainDatasetId(String datasetString) {
        return computing.setMainDataset(datasetString);
    }

    /**
     * get the main dataset
     *
     * @return mainDataset
     */
    public int getMainDatasetId() {
        return computing.getMainDataset();
    }

    /**
     * get selected dataset
     *
     * @param datasetId
     * @return dataset
     */
    public IdentificationDataset getDataset(int datasetId) {
        IdentificationDataset dataset = computing.getDataset(datasetId);
        return dataset;
    }

    /**
     * get dataset peptides list
     *
     * @param datasetId
     * @return dataset peptide List
     */
    public Map<Integer, PeptideBean> getAllDatasetPeptidesList(int datasetId,boolean validated) {
        return computing.getAllDatasetPeptidesList(datasetId,validated);
    }

    /**
     * check if exporting file is available in export folder
     *
     * @param fileName
     * @return test boolean (available or not available)
     */
    public boolean checkFileAvailable(String fileName) {

        return computing.checkFileAvailable(fileName);
    }

    /**
     * check if exporting file is available in export folder
     *
     * @param fileName
     * @return url string path to the file
     */
    public String getFileUrl(String fileName) {

        return computing.getFileUrl(fileName);
    }

    /**
     * get dataset peptides list (valid peptides or all peptides)
     *
     * @param datasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public Map<Integer, PeptideBean> getPeptidesList(int datasetId, boolean validated) {

        return computing.getAllDatasetPeptidesList(datasetId, validated);

    }
  
    
     /**
     * get dataset peptides number (valid peptides or all peptides)
     *
     * @param datasetId
     * @param validated validated peptides (true/false)
     * @return dataset peptide List
     */
    public int getPeptidesNumber(int datasetId, boolean validated) {

        return computing.getAllDatasetPeptidesNumber(datasetId, validated);

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

        Map<Integer, PeptideBean> peptidesProtList = computing.getPeptidesProtList(datasetId, accession, otherAccession);

        return peptidesProtList;
    }

    /**
     * get dataset fractions list
     *
     * @param datasetId
     * @return fractions list for the selected dataset
     */
    public Map<Integer, IdentificationProteinBean> getProtGelFractionsList(int datasetId,String accession,String otherAccession) {

        return computing.getProtGelFractionsList(datasetId,accession, otherAccession);

    }

    /**
     * get proteins fractions average list
     *
     * @param accession
     * @param fractionsList
     * @param datasetId
     * @return dataset peptide List
     */
    public Map<Integer, IdentificationProteinBean> getProteinFractionAvgList(String accession, Map<Integer, FractionBean> fractionsList, int datasetId) {
        Map<Integer, IdentificationProteinBean> proteinFractList = new TreeMap<Integer, IdentificationProteinBean>();
        Map<Integer, FractionBean> treeFractList = new TreeMap<Integer, FractionBean>();
        if (fractionsList == null) {
            fractionsList = computing.getProteinFractionList(accession, datasetId);
        }
        treeFractList.putAll(fractionsList);
        for (int key : treeFractList.keySet()) {
            FractionBean fb = fractionsList.get(key);
            if (fb.getProteinList().containsKey(accession)) {
                proteinFractList.put(fb.getFractionIndex(), fb.getProteinList().get(accession));
            }
        }
        return proteinFractList;
    }
    
    /* search for identification proteins*/
      public Map<Integer, IdentificationProteinBean> searchIdentificationProtein(Query query) {
      return computing.searchIdentficationProtein(query);
      
      }
      /*search for quantification proteins*/
      public Map<Integer, QuantificationProteinsBean> searchQuantificationProtein(Query query) {
        return computing.searchQuantificationProteins(query);
      
      }

    /**
     * search for proteins by accession keywords
     *
     * @param searchKeywordArray array of query words
     * @param searchDatasetType type of search
     * @param validatedOnly only validated proteins results
     * @return fractions list for the selected dataset
     */
    public Map<Integer, IdentificationProteinBean> searchProteinByAccession(String searchKeywordArray, String searchDatasetType, boolean validatedOnly) {
        Map<Integer, IdentificationProteinBean> protDatasetList = computing.searchProteinByAccession(searchKeywordArray, searchDatasetType, validatedOnly);
        return protDatasetList;
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

        Map<Integer, IdentificationProteinBean> protDatasetList = computing.searchProteinByName(proteinDescriptionKeyword, searchDatasetType, validatedOnly);

        return protDatasetList;

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
        Map<Integer, IdentificationProteinBean> protDatasetList = computing.searchProteinByPeptideSequence(peptideSequenceKeyword, searchDatasetType, validatedOnly);
        return protDatasetList;
    }

    /**
     * retrieve standard proteins data for fraction plot
     */
    public List<StandardProteinBean> retrieveStandardProtPlotList(int datasetId) {
        return computing.retriveStandardProtPlotList(datasetId);
    }

    /**
     * retrieve standard proteins data for fraction plot
     *
     * @param dataset
     * @return test boolean
     */
    public boolean updatedatasetData(IdentificationDataset dataset) {
        boolean test = computing.updateDatasetData(dataset);
        return test;

    }

    /**
     * get protein peptides from all available datasets
     *
     * @param accession
     * @param otherAccession
     * @param validated
     * @return peptideTableList map of all available peptides for the protein
     */
    public Map<String, PeptideTable> getProteinPeptidesAllDatasets(String accession, String otherAccession, boolean validated) {
        Map<String, PeptideTable> peptideTableList = new HashMap<String, PeptideTable>();
        for (IdentificationDataset tempDataset : computing.getDatasetList().values()) {

            Map<Integer, PeptideBean> pepProtList = getPeptidesProtList(tempDataset.getDatasetId(), accession, otherAccession);

            if (pepProtList.size() > 0) {
                if (validated) {
                    Map<Integer, PeptideBean> vPepProtList = new HashMap<Integer, PeptideBean>();
                    for (int key : pepProtList.keySet()) {
                        PeptideBean pb = pepProtList.get(key);
                        if (pb.getValidated() == 1) {
                            vPepProtList.put(key, pb);
                        }

                    }
                    PeptideTable pepTable = new PeptideTable(vPepProtList, null, false, null);
                    pepTable.setVisible(false);
                    peptideTableList.put(tempDataset.getName(), pepTable);

                } else {
                    PeptideTable pepTable = new PeptideTable(pepProtList, null, false, null);
                    pepTable.setVisible(false);
                    peptideTableList.put(tempDataset.getName(), pepTable);
                }
            }

        }
        return peptideTableList;
    }

    /**
     * get Authenticator Handler instance
     *
     * @return authenticatorHandler
     */
    public AuthenticatorHandler getAuthenticatorHandler() {
        return authenticatorHandler;
    }

    /**
     * get dataset details list that has basic information for datasets
     *
     * @return datasetDetailsList
     */
    public Map<Integer, DatasetDetailsBean> getDatasetDetailsList() {
        Map<Integer, DatasetDetailsBean> datasetDetailsList = computing.getDatasetDetailsList();
        return datasetDetailsList;

    }

    public int getValidatedPepNumber(Map<Integer, PeptideBean> pepProtList) {
        int count = computing.getValidatedPepNumber(pepProtList);
        return count;
    }

    public TreeMap<Integer, Object> getSearchIndexesSet(Map<String, Integer> searchMap, Map<String, Integer> searchMapIndex, String keySearch) {
        TreeMap<Integer, Object> treeSet = computing.getSearchIndexesSet(searchMap, searchMapIndex, keySearch);

        return treeSet;
    }
    
    public Map<Integer, IdentificationProteinBean> getValidatedProteinsList(Map<Integer, IdentificationProteinBean> proteinsList) {
        Map<Integer, IdentificationProteinBean> vProteinsList = computing.getValidatedProteinsList(proteinsList);
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
    public void  exportPeptidesToFile(int datasetId, boolean validated,String datasetName,String dataType,String exportFileType){
         computing.exportPeptidesToFile(datasetId, validated, datasetName, dataType,exportFileType);
    
    
    }

    public String filterSearchingKeywords(Map<Integer, IdentificationProteinBean> protList, String SearchingKeys, String searchBy) {
 if (protList == null || protList.isEmpty()) {
            return SearchingKeys;
        }
        HashSet<String> usedKeys = new HashSet<String>();
        HashSet<String> tUsedKeys = new HashSet<String>();
        for(String key: SearchingKeys.split("\n"))
            usedKeys.add(key);
        tUsedKeys.addAll(usedKeys);
       for(IdentificationProteinBean pb:protList.values()){
       if(searchBy.equals("Protein Accession"))
       {
           if(usedKeys.contains(pb.getAccession()))
               usedKeys.remove(pb.getAccession());
           if(usedKeys.isEmpty())
               return "";
       }
       else if (searchBy.equals("Protein Name")) {
               for (String key : tUsedKeys) {
                   if (pb.getDescription().contains(key)) {
                       usedKeys.remove(key);
                   }
                   if (usedKeys.isEmpty()) {
                       return "";
                   }
               }

           }
//       else
//           return "";
//       else if(searchBy.equals("Peptide Sequence"))
//       {
//           if(usedKeys.contains(pb.get))
//               usedKeys.remove(pb.getAccession());
//           if(usedKeys.isEmpty())
//               return null;
//       }
//       
       }
       return "";

    }
    
    

}
