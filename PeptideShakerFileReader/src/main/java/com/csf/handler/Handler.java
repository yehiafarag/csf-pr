/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.csf.handler;

import com.csf.DAL.DAL;
import com.pepshack.util.beans.ExperimentBean;

/**
 *
 * @author Yehia Mokhtar
 */
public class Handler {
    private DAL dal ;
    public Handler(String url, String dbName, String driver, String userName, String password)
    {
        dal = new DAL(url,dbName,driver,userName,password);
    }
    public boolean handelExp(ExperimentBean exp)
    {
        boolean test = false;
        int expId = dal.storeExperiment(exp);
        exp.setExpId(expId);
        System.out.println(exp.getProteinList()== null);
        System.out.println(exp.getPeptideList()== null);
        System.out.println(exp.getFractionsList() == null);
        if(! exp.getProteinList().isEmpty())
            test = dal.storeProteinsList(exp);
        if(!exp.getPeptideList().isEmpty())
            test = dal.storePeptidesList(exp);
        if(!exp.getFractionsList().isEmpty())
            test = dal.storeFractionsList(exp);
        return test;
    }
    public boolean checkName(String name)
    {
        boolean test = dal.checkName(name);
        
        return test;
    }
    
}
