/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.selectionmanager;

import java.io.Serializable;
import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class CSFFilterSelection implements Serializable {

    private final String filterId;
    private  Set<String> values;
    private boolean active;
    private int filterIndex;
    private String filterColor;
    private final String type;
    private  int[] datasetIndex;
    private Set<Integer> datasetIndexesSet;

    
    public CSFFilterSelection(String filterId, Set<String> values, boolean active, int filterIndex, String filterColor, String type, int[] datasetIndex) {
        this.filterId = filterId;
        this.values = values;
        this.active = active;
        this.filterIndex = filterIndex;
        this.filterColor = filterColor;
        this.type = type;
        this.datasetIndex = datasetIndex;

    }

    public CSFFilterSelection(String type, int[] datasetIndex, String filterId, Set<String> values) {
        this.type = type;
        this.datasetIndex = datasetIndex;
        this.filterId = filterId;
        this.values = values;

    }
    
    public CSFFilterSelection(String filterId,String type, Set<Integer> datasetIndexesSet ) {
        this.type = type;
        this.datasetIndexesSet = datasetIndexesSet;
        this.filterId = filterId;

    }

    public String getFilterId() {
        return filterId;
    }

    public Set<Integer> getDatasetIndexesSet() {
        return datasetIndexesSet;
    }

    public Set<String> getValues() {
        return values;
    }

    public int[] getDatasetIndexes() {
        return datasetIndex;
    }

    public boolean isActive() {
        return active;
    }

    public int getFilterIndex() {
        return filterIndex;
    }

    public String getType() {
        return type;
    }

}
