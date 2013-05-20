/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csf.DAL;

import com.pepshack.util.beans.ExperimentBean;
import com.pepshack.util.beans.ProteinBean;

/**
 *
 * @author Yehia Mokhtar
 */
public class DAL { 
    private DB database;
    public DAL(String url, String dbName, String driver, String userName, String password) 
    {
           database = new DB(url, dbName, driver, userName, password);
           database.createTables();
    }
    
    public int storeExperiment(ExperimentBean exp)
    {
       
        int expId = database.setupExperiment(exp);
        return expId;
    }
    
    public boolean storeProteinsList(ExperimentBean exp)
    {
        for (ProteinBean pb : exp.getProteinList().values()) {
                   database.insertProteinExper(exp.getExpId(), pb);
                   database.insertProt(pb.getAccession(), pb.getDescription());
                }
        
        return true;
    }
    public boolean storePeptidesList(ExperimentBean exp)
    {
        boolean test = database.updatePeptideFile(exp);
        return test;
    }
    public boolean storeFractionsList(ExperimentBean exp)
    {
       boolean test =database.insertFractions(exp);
        return test;
    }
    public boolean checkName(String name)
    {
        boolean test = database.checkName(name);
        return test;
        
    }
    
}
