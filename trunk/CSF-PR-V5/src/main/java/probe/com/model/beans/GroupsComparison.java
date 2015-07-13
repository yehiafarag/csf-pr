/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import java.io.Serializable;
import java.util.Map;

/**
 *
 * @author Yehia Farag
 */
public class GroupsComparison implements Serializable, Comparable<GroupsComparison>{
    private String comparisonHeader;
    private int[] datasetIndexes;
    private Map<String, ComparisonProtein> comparProtsMap;

    public String getComparisonHeader() {
        return comparisonHeader;
    }

    public void setComparisonHeader(String comparisonHeader) {
        this.comparisonHeader = comparisonHeader;
    }

    public int[] getDatasetIndexes() {
        return datasetIndexes;
    }

    public void setDatasetIndexes(int[] datasetIndexes) {
        this.datasetIndexes = datasetIndexes;
    }

    public Map<String, ComparisonProtein>  getComparProtsMap() {
        return comparProtsMap;
    }

    public void setComparProtsMap(Map<String, ComparisonProtein>  comparProtsMap) {
        this.comparProtsMap = comparProtsMap;
    }

    @Override
    public int compareTo(GroupsComparison t) {
        return this.comparisonHeader.compareTo(t.comparisonHeader);
    }

   
    
}
