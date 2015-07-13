package probe.com.selectionmanager;

import com.vaadin.server.VaadinSession;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import probe.com.model.beans.GroupsComparison;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author Yehia Farag
 */
public class DatasetExploringCentralSelectionManager {

    public Set<String> getSelectedHeatMapRows() {
        return selectedHeatMapRows;
    }

    public Set<String> getSelectedHeatMapColumns() {
        return selectedHeatMapColumns;
    }

    public DiseaseGroup[] getDiseaseGroupsArr() {
        return diseaseGroupsArr;
    }

    
    public  enum Selection_Type { Disease_Groups_Level ,Reset_Disease_Groups_Level,HeatMap_Update_level 
    
    }
    private final QuantDatasetObject[] fullDatasetArr;
    private QuantDatasetObject[] filteredDatasetArr = new QuantDatasetObject[]{};

    private final Set<CSFFilter> registeredFilterSet = new HashSet<CSFFilter>();
   
    private final boolean[] activeFilters;

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

    public DatasetExploringCentralSelectionManager(QuantDatasetObject[] datasetsList, boolean[] activeFilters) {
        this.fullDatasetArr = datasetsList;
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
    
    public void updateSelectedComparisonList(Set<GroupsComparison> selectedComparisonList){
        this.selectedComparisonList = selectedComparisonList;
    
    
    }

    public CSFFilterSelection getFilterSelection() {
        return filterSelection;
    }
    private CSFFilterSelection filterSelection;

    public void setStudyLevelFilterSelection(CSFFilterSelection selection) {
        try {
            filterSelection = selection;
            if (selection.getType().equalsIgnoreCase("Disease_Groups_Level")) {
                updateFilteredDatasetList(selection.getDatasetIndexes());
                this.SelectionChanged(selection.getType());
            }
            else
            {
                 this.SelectionChanged("StudySelection");
            }

        } finally {
            
        }

    }
    
    
    
    private Set<String> selectedHeatMapRows ;
    private Set<String> selectedHeatMapColumns;
    private DiseaseGroup[] diseaseGroupsArr;
    
    public void setHeatMapLevelSelection(Set<String> selectedRows,Set<String> selectedColumns,DiseaseGroup[] diseaseGroupsArr){
        this.diseaseGroupsArr=diseaseGroupsArr;
        this.selectedHeatMapRows=selectedRows;
        this.selectedHeatMapColumns=selectedColumns;
        this.SelectionChanged("HeatMap_Update_level");
    
    
    }
    

    public void setComparisonSelection(Set<GroupsComparison> selectedComparisonList) {
        try {

            VaadinSession.getCurrent().getLockInstance().lock();
            this.selectedComparisonList = selectedComparisonList;
            this.SelectionChanged("ComparisonSelection");

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }
    private Map<String, ComparisonProtein[]> protSelectionMap ;
     public void setProteinsSelection(Map<String, ComparisonProtein[]> protSelectionMap) {
        try {

            VaadinSession.getCurrent().getLockInstance().lock();
            this.protSelectionMap = protSelectionMap;
            this.SelectionChanged("quantProtSelection");

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    public void resetFilters() {
        filteredDatasetArr = fullDatasetArr;
        this.SelectionChanged("Reset_Disease_Groups_Level");

    }

    public boolean[] getActiveFilters() {
        return activeFilters;
    }

    public void removeFilterValue(String filterId, String filterValue) {
        for (CSFFilter filter : registeredFilterSet) {
            if (filter.getFilterId().equalsIgnoreCase(filterId)) {
                filter.removeFilterValue(filterValue);
                break;
            }

        }

    }
    private void SelectionChanged(String type) {
        for (CSFFilter filter : registeredFilterSet) {
            filter.selectionChanged(type);
        }
    }

    public Map<String, ComparisonProtein[]> getProtSelectionMap() {
        return protSelectionMap;
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


    public QuantDatasetObject[] getFullDatasetArr() {
        return fullDatasetArr;
    }

    public Set<GroupsComparison> getSelectedComparisonList() {
        return selectedComparisonList;
    }
    
  

}
