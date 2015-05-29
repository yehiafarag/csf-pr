package probe.com.selectionmanager;

import com.vaadin.server.VaadinSession;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import probe.com.model.beans.GroupsComparison;
import probe.com.model.beans.QuantDatasetObject;

/**
 *
 * @author Yehia Farag
 */
public class DatasetExploringSelectionManagerRes {

    private final QuantDatasetObject[] fullDatasetArr;
    private QuantDatasetObject[] filteredDatasetArr;

    private final Set<CSFFilter> registeredFilterSet = new HashSet<CSFFilter>();

//    private final Map<String, String> titleMap = new HashMap<String, String>();    
//    private final Map<String, Set<String>> appliedFilterList = new TreeMap<String, Set<String>>();    
    private final boolean[] activeFilters;
//    private final Map<String, List<Object>> fullFilterList;

    public QuantDatasetObject[] getFilteredDatasetArr() {
        return filteredDatasetArr;
    }

    public int getSelectedDataset() {
        return selectedDataset;
    }

    public void setSelectedDataset(int selectedDataset) {
        this.selectedDataset = selectedDataset;
    }

    private int selectedDataset = -1;

    public DatasetExploringSelectionManagerRes(QuantDatasetObject[] datasetsList, boolean[] activeFilters) {//, Map<String, List<Object>> fullFilterList) {
//        this.fullFilterList = fullFilterList;    
        this.fullDatasetArr = datasetsList;
//        titleMap.put("diseaeGroups", "Disease Groups");
//        titleMap.put("rawDataUrl", "Raw Data");
//        titleMap.put("year", "Year");
//        titleMap.put("typeOfStudy", "Study Type");
//        titleMap.put("sampleType", "Sample Type");
//        titleMap.put("sampleMatching", "Sample Matching");
//        titleMap.put("technology", "Technology");
//        titleMap.put("analyticalApproach", "Analytical Approach");
//        titleMap.put("enzyme", "Enzyme");
//        titleMap.put("shotgunTargeted", "Shotgun/Targeted");
        this.activeFilters = activeFilters;

    }

    private void updateFilteredDatasetList(int[] datasetIndexes) {
        if (datasetIndexes.length == 0) {
            filteredDatasetArr = fullDatasetArr;
            return;
        }

        filteredDatasetArr = new QuantDatasetObject[datasetIndexes.length];
        int z = 0;
        for (int i : datasetIndexes) {
            filteredDatasetArr[z] = fullDatasetArr[i];
            z++;
        }

    }

    private QuantDatasetObject[] selectedDSIndexes;

    public QuantDatasetObject[] getSelectedDSIndexes() {
        return selectedDSIndexes;
    }

    private Set<GroupsComparison> selectedComparisonList;

    public CSFFilterSelection getFilterSelection() {
        return filterSelection;
    }
    private CSFFilterSelection filterSelection;

    public void updatedSelection(CSFFilterSelection selection) {
        try {

//            VaadinSession.getCurrent().getLockInstance().lock();
            filterSelection = selection;
            if (selection.getType().equalsIgnoreCase("filter")) {
                updateFilteredDatasetList(selection.getDatasetIndexes());
                this.SelectionChanged(selection.getType());
            }

        } finally {
//            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    public void updatedComparisonSelection(Set<GroupsComparison> selectedComparisonList) {
        try {

            VaadinSession.getCurrent().getLockInstance().lock();
            this.selectedComparisonList = selectedComparisonList;
            this.SelectionChanged("DSSelection");

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    public void resetFilters() {
        filteredDatasetArr = fullDatasetArr;
        this.SelectionChanged("filter");

    }

    public boolean[] getActiveFilters() {
        return activeFilters;
    }

//    public Map<String, Set<String>> getAppliedFilterList() {
//        return appliedFilterList;
//    }
//    public Map<String, List<Object>> getFullFilterList() {
//        return fullFilterList;
//    }
    public void removeFilterValue(String filterId, String filterValue) {
        for (CSFFilter filter : registeredFilterSet) {
            if (filter.getFilterId().equalsIgnoreCase(filterId)) {
                filter.removeFilterValue(filterValue);
                break;
            }

        }

    }

//    private void FilterStudies() {
//        HashSet<QuantDatasetObject> filteredList = new HashSet<QuantDatasetObject>();
//        filteredList.addAll(Arrays.asList(fullDatasetArr));
//        for (String key : appliedFilterList.keySet()) {
//            if (appliedFilterList.get(key) != null && !appliedFilterList.get(key).isEmpty()) {
//                filteredList = filterStudies(filteredList, appliedFilterList.get(key), key);
//            }
//
//        }
//        filteredDatasetArr = new QuantDatasetObject[filteredList.size()];
//        int x=0;
//        for(QuantDatasetObject ds :filteredList){
//        filteredDatasetArr[x]=ds;
//        x++;
//        }
////        .addAll(filteredList);
//
//    }
    private void SelectionChanged(String type) {
        for (CSFFilter filter : registeredFilterSet) {
            filter.selectionChanged(type);
        }
    }

    public void registerFilter(final CSFFilter iFilter) {
        registeredFilterSet.add(iFilter);
    }

    public QuantDatasetObject[] getFilteredDatasetsList() {
        if (filteredDatasetArr == null || filteredDatasetArr.length == 0) {
            return fullDatasetArr;
        }
        return filteredDatasetArr;
    }

//
//    public void updateSelectedFilter(int filterIndex, boolean value) {
//        activeFilters[filterIndex] = value;
//
//    }
//    public String getFilterTitle(String filterId) {
//        if (titleMap.containsKey(filterId)) {
//            return titleMap.get(filterId);
//        } else {
//            return filterId;
//        }
//
//    }
//    private HashSet<QuantDatasetObject> filterStudies(HashSet<QuantDatasetObject> studies, Set<String> filter, String filterId) {
//        HashSet<QuantDatasetObject> subset = new HashSet<QuantDatasetObject>();
//        for (QuantDatasetObject study : studies) {
//            for (String f : filter) {
//                if (study.getProperty(filterId).toString().equalsIgnoreCase(f)) {
//                    subset.add(study);
//                }
//
//            }
//
//        }
//        return subset;
//
//    }
//    
//    public void updateSelection(CSFFilterSelection selection,int z) {
//        try { VaadinSession.getCurrent().getLockInstance().lock();
//            if(selection.getType().equalsIgnoreCase("filter")){
//           
//            if (selection.getValues().isEmpty()) {
//                appliedFilterList.remove(selection.getFilterId());
//                activeFilters[selection.getFilterIndex()] = false;//selection.isActive();
//            } else {
//                appliedFilterList.put(selection.getFilterId(), selection.getValues());
//                activeFilters[selection.getFilterIndex()] = true;
//            }
//            this.FilterStudies();
//            
//            }
//            else if(selection.getType().equalsIgnoreCase("comparisonfilter")){
//                updateFilteredDatasetList(selection.getDatasetIndexes());            
//            }
//            this.SelectionChanged(selection.getType());
//        } finally {
//            VaadinSession.getCurrent().getLockInstance().unlock();
//        }
//
//    }
    public QuantDatasetObject[] getFullDatasetArr() {
        return fullDatasetArr;
    }

    public Set<GroupsComparison> getSelectedComparisonList() {
        return selectedComparisonList;
    }

}
