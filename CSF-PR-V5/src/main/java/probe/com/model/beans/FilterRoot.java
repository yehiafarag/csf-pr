/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author y-mok_000
 */
public class FilterRoot implements Serializable{

    public void setDatasetsList(Set<QuantDatasetObject> datasetsList) {
        this.datasetsList = datasetsList;
    }
    
    
    private  Set<QuantDatasetObject> datasetsList = new HashSet<QuantDatasetObject>();
    private final boolean[] appliedFilters = new boolean[10];
    private final String[] appliedFiltersValues = new String[10];
//    private final String[] appliedFiltersTitles = new String[10];
    private QuantDatasetObject repDataset;

    public boolean[] getAttrCheck() {
        return attrCheck;
    }

    public QuantDatasetObject getRepDataset() {
        return repDataset;
    }

    public void setRepDataset(QuantDatasetObject repDataset) {
        this.repDataset = repDataset;
    }

    public void setAttrCheck(boolean[] attrCheck) {
        this.attrCheck = attrCheck;
    }
    
     private boolean[] attrCheck ;

//    public String getAppliedFiltersTitles(int index) {
//        return appliedFiltersTitles[index];
//    }
    private boolean[] masterFilters;
    private int key;

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public boolean[] getMasterFilters() {
        return masterFilters;
    }

    public void setMasterFilters(boolean[] masterFilters) {
        this.masterFilters = masterFilters;
    }

    public boolean[] getAppliedFilters() {
        return appliedFilters;
    }

   public void applyFilter(String filterId,String value,int index) {
        this.appliedFilters[index] = true;
            this.appliedFiltersValues[index]=value;
//            this.appliedFiltersTitles[index]=filterId;
       
    }

    public String[] getAppliedFiltersValues() {
        return appliedFiltersValues;
    }

    public Set<QuantDatasetObject> getDatasetsList() {
        return datasetsList;
    }

    public void addStudy(QuantDatasetObject study) {
        this.datasetsList.add(study);
    }
    public String getProperty(int index)
    {
        return appliedFiltersValues[index];
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
   
}
