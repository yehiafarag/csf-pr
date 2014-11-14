/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.csf.handler;

import com.csf.DAL.DAL;
import com.pepshack.util.beans.ExperimentBean;
import java.sql.SQLException;
/*
 * @author Yehia Farag
 */

public class Handler {

    private final DAL dal;

    public Handler(String url, String dbName, String driver, String userName, String password) throws SQLException {
        dal = new DAL(url, dbName, driver, userName, password);
    }

    public boolean handelExp(ExperimentBean exp) {
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
    
     
    public void exportDataBase(){
        dal.exportDataBase();
    }
      public boolean restoreDB(String source) {
        return dal.restoreDB(source);
     }
}