/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csf.DAL;

import com.pepshaker.util.beans.ExperimentBean;
import com.pepshaker.util.beans.ProteinBean;
import com.quantcsf.beans.QuantDatasetObject;
import com.quantcsf.beans.QuantPeptide;
import com.quantcsf.beans.QuantProtein;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class DAL {

    private DB database;

    public DAL(String url, String dbName, String driver, String userName, String password) throws SQLException {
        database = new DB(url, dbName, driver, userName, password);
        database.createTables();
    }

    public int storeExperiment(ExperimentBean exp) {
        int expId = database.setupExperiment(exp);
        return expId;
    }

    public boolean storeProteinsList(ExperimentBean exp) {
        for (String key : exp.getProteinList().keySet()) {
            ProteinBean pb = exp.getProteinList().get(key);
            database.insertProteinExper(exp.getExpId(), pb, pb.getAccession() + "," + pb.getOtherProteins());
//            database.insertProt(pb.getAccession(), pb.getDescription());
        }

        return true;
    }

    public boolean storePeptidesList(ExperimentBean exp) {
        boolean test = database.updatePeptideFile(exp);
        return test;
    }

    public boolean storeFractionsList(ExperimentBean exp) {
        boolean test = database.insertFractions(exp);
        return test;
    }

    public boolean checkName(String name)  throws SQLException {
        boolean test = database.checkName(name);
        return test;

    }
     
    public void exportDataBase(String mysqldumpUrl,String sqlFileUrl){
        database.exportDataBase( mysqldumpUrl, sqlFileUrl);
        
    
    }
     public boolean restoreDB(String source) {
        return database.restoreDB(source);
     }
     
     
     ///quant data store 
     public  boolean storeCombinedQuantProtTable(List<QuantProtein> qProtList){
         return database.storeCombinedQuantProtTable(qProtList);
     
     
     }
     //quant data store 
     public  boolean updateProtSequances( Map<String,String> protSeqMap){
         return database.updateProtSequances(protSeqMap);
     
     
     }
     
     public void storeQuantDatasets(){
         database.storeQuantDatasets();
     }
     
     public void storeQuantitiveProteins(List<QuantProtein> qProtList){
         database.storeQuantitiveProteins(qProtList);
     }
     
      public void storeQuantitivePeptides(List<QuantPeptide> qPeptidestList ){
         database.storeQuantitivePeptides(qPeptidestList);
     }
      public int getCurrentProtIndex() {
       return database.getCurrentProtIndex();
       
       
       }
     
       public Set<QuantDatasetObject> getQuantDatasetListObject() {
       return database.getQuantDatasetListObject();
       }
       
       public void correctProtInfo(){
           database.correctProtInfo();
           
           

}
}
