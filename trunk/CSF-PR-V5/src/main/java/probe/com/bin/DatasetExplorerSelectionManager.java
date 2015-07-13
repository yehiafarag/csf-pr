/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.bin;

import com.vaadin.server.VaadinSession;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.CSFFilterSelection;

/**
 *
 * @author y-mok_000
 */
public class DatasetExplorerSelectionManager {

    private final Map<String, String> titleMap = new HashMap<String, String>();
    private final Set<CSFFilter> registeredFilterSet = new HashSet<CSFFilter>();
    private final Map<String, Set<String>> appliedFilterList = new TreeMap<String, Set<String>>();
    private final QuantDatasetObject[] DatasetList;
    private QuantDatasetObject[] filteredDatasetsList;
    private final boolean[] activeFilters;
    private final Map<String, List<Object>> fullFilterList;

    public void setFilteredDatasetsList(QuantDatasetObject[] filteredDatasetsList) {
        this.filteredDatasetsList = filteredDatasetsList;
    }

    public int getSelectedDataset() {
        return selectedDataset;
    }
    

    public void setSelectedDataset(int selectedDataset) {
        this.selectedDataset = selectedDataset;
    }
    
    private int selectedDataset = -1;
    
//    private final QuantDatasetObject[] datasetMap;

    public DatasetExplorerSelectionManager(QuantDatasetObject[]datasetsList, int activeFiltersNumber, Map<String, List<Object>> fullFilterList) {
        this.fullFilterList = fullFilterList;
    
        this.DatasetList = datasetsList;
        titleMap.put("diseaeGroups", "Disease Groups");
        titleMap.put("rawDataUrl", "Raw Data");
        titleMap.put("year", "Year");
        titleMap.put("typeOfStudy", "Study Type");
        titleMap.put("sampleType", "Sample Type");
        titleMap.put("sampleMatching", "Sample Matching");
        titleMap.put("technology", "Technology");
        titleMap.put("analyticalApproach", "Analytical Approach");
        titleMap.put("enzyme", "Enzyme");
        titleMap.put("shotgunTargeted", "Shotgun/Targeted");
        this.activeFilters = new boolean[activeFiltersNumber];
        

    }

    public boolean[] getActiveFilters() {
        return activeFilters;
    }

    public Map<String, Set<String>> getAppliedFilterList() {
        return appliedFilterList;
    }

    public Map<String, List<Object>> getFullFilterList() {
        return fullFilterList;
    }

    public void updateSelection(CSFFilterSelection selection) {
        try { VaadinSession.getCurrent().getLockInstance().lock();
            if(selection.getType().equalsIgnoreCase("Disease_Groups_Level")){
           
            if (selection.getValues().isEmpty()) {
                appliedFilterList.remove(selection.getFilterId());
                activeFilters[selection.getFilterIndex()] = false;//selection.isActive();
            } else {
                appliedFilterList.put(selection.getFilterId(), selection.getValues());
                activeFilters[selection.getFilterIndex()] = true;
            }
            this.FilterStudies();
            
            }
            else if(selection.getType().equalsIgnoreCase("comparisonfilter")){            
            
            }
            this.SelectionChanged(selection.getType());
        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }
    public void removeFilterValue(String filterId, String filterValue) {
        for (CSFFilter filter : registeredFilterSet) {
            if (filter.getFilterId().equalsIgnoreCase(filterId)) {
                filter.removeFilterValue(filterValue);
                break;
            }

        }

    }
    private void FilterStudies() {

        HashSet<QuantDatasetObject> filteredList = new HashSet<QuantDatasetObject>();
        filteredList.addAll(Arrays.asList(DatasetList));
        for (String key : appliedFilterList.keySet()) {
            if (appliedFilterList.get(key) != null && !appliedFilterList.get(key).isEmpty()) {
                filteredList = filterStudies(filteredList, appliedFilterList.get(key), key);
            }

        }
        filteredDatasetsList = new QuantDatasetObject[filteredList.size()];
        int x=0;
        for(QuantDatasetObject ds :filteredList){
        filteredDatasetsList[x]=ds;
        x++;
        }
//        .addAll(filteredList);

    }

    private void SelectionChanged(String type) {
        for (CSFFilter filter : registeredFilterSet) {
            filter.selectionChanged(type);
        }
    }

    public void registerFilter(final CSFFilter iFilter) {
        registeredFilterSet.add(iFilter);
    }

    public QuantDatasetObject[] getFilteredDatasetsList() {
        if (filteredDatasetsList== null || filteredDatasetsList.length==0) {
            return DatasetList;
        }
        return filteredDatasetsList;
    }

    public void updateSelectedFilter(int filterIndex, boolean value) {
        activeFilters[filterIndex] = value;

    }

    public String getFilterTitle(String filterId) {
        if (titleMap.containsKey(filterId)) {
            return titleMap.get(filterId);
        } else {
            return filterId;
        }

    }

    private HashSet<QuantDatasetObject> filterStudies(HashSet<QuantDatasetObject> studies, Set<String> filter, String filterId) {
        HashSet<QuantDatasetObject> subset = new HashSet<QuantDatasetObject>();
        for (QuantDatasetObject study : studies) {
            for (String f : filter) {
                if (study.getProperty(filterId).toString().equalsIgnoreCase(f)) {
                    subset.add(study);
                }

            }

        }
        return subset;

    }

}
