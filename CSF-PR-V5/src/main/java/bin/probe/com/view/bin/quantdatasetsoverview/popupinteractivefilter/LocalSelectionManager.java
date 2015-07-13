/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bin.probe.com.view.bin.quantdatasetsoverview.popupinteractivefilter;

import com.vaadin.server.VaadinSession;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class LocalSelectionManager implements Serializable, CSFFilter {
    private boolean  selectionPerformed=false;

    private final Set<InteractiveFilter> registeredFilters = new HashSet<InteractiveFilter>();
    private final Map<String, String> titleMap = new HashMap<String, String>();
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final String Filter_ID = "interactiveChartsFilter";
    private final Map<String, Set<String>> appliedFilterList = new TreeMap<String, Set<String>>();
    private final boolean[] activeFilters;
//    private final QuantDatasetObject[] fullDatasetArr;
    private QuantDatasetObject[] filteredDatasetArr;
    private boolean selfselection = false;

    public LocalSelectionManager(DatasetExploringCentralSelectionManager exploringFiltersManager) {
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
        this.exploringFiltersManager = exploringFiltersManager;
//        this.fullDatasetArr=exploringFiltersManager.getFilteredDatasetsList();
        exploringFiltersManager.registerFilter(LocalSelectionManager.this);
        activeFilters = exploringFiltersManager.getActiveFilters();
    }

    public void registerFilter(InteractiveFilter filter) {
        this.registeredFilters.add(filter);
    }

    @Override
    public void selectionChanged(String type) {
        if (type.equals("Disease_Groups_Level")) {
            if (selfselection) {
                selfselection = false;
                return;
            }
           
            this.updateChartsFilters(type,exploringFiltersManager.getFilteredDatasetArr(),null);
        }
    }

    @Override
    public String getFilterId() {
        return Filter_ID;
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public String getFilterTitle(String filterId) {
        if (titleMap.containsKey(filterId)) {
            return titleMap.get(filterId);
        } else {
            return filterId;
        }

    }

    public void updateSelection(CSFFilterSelection selection,boolean selfselection) {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();
            selectionPerformed = true;
            int x = 0;
            QuantDatasetObject[] arr = new QuantDatasetObject[selection.getDatasetIndexes().length];
            for (int i : selection.getDatasetIndexes()) {
                arr[x] = exploringFiltersManager.getFullDatasetArr()[i];
                x++;
            }
            updateChartsFilters(selection.getType(),arr,selection);

        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
//            this.updateCentralSelectionManager();
        }

    }

    private void updateChartsFilters(String type, QuantDatasetObject[] filteredDS,CSFFilterSelection selection) {
        if (type == null) {
            return;
        }
        if (type.equalsIgnoreCase("interactivefilter")) {
            int[] dsIndexes = selection.getDatasetIndexes();
            if (selection.getValues().isEmpty()) {
                appliedFilterList.remove(selection.getFilterId());
                activeFilters[selection.getFilterIndex()] = false;//selection.isActive();
                dsIndexes= null;
//                FilterStudies(null);
//                dsIndexes=selectedDatasetsIndexes;
               
            } else {
                appliedFilterList.put(selection.getFilterId(), selection.getValues());
                activeFilters[selection.getFilterIndex()] = true;
            }
            this.FilterStudies(dsIndexes);

        } else if (type.equalsIgnoreCase("Disease_Groups_Level")) {

//            HashSet<QuantDatasetObject> filteredList = new HashSet<QuantDatasetObject>();
////            QuantDatasetObject[] subdss = new QuantDatasetObject[selection.getDatasetIndexes().length];
////            int z = 0;
//
//            for (QuantDatasetObject ds : filteredDS) {
////                QuantDatasetObject ds = exploringFiltersManager.getFullDatasetArr()[i];
//                if (ds != null) {
//                    filteredList.add(ds);
//                }
//
//            }
//
           
//
//            for (String key : appliedFilterList.keySet()) {
//                if (appliedFilterList.get(key) != null && !appliedFilterList.get(key).isEmpty()) {
//                    filteredList = filterStudies(filteredList, appliedFilterList.get(key), key);
//                }
//            }
            filteredDatasetArr = filteredDS;//new QuantDatasetObject[filteredList.size()];
            selectedDatasetsIndexes = new int[filteredDatasetArr.length];
            int x = 0;
            for (QuantDatasetObject ds : filteredDatasetArr) {
//                filteredDatasetArr[x] = ds;
                selectedDatasetsIndexes[x] = ds.getUniqId();
                x++;
            }
        } 
        this.updateCharts();

    }

    private int[] selectedDatasetsIndexes;

    private void FilterStudies(int[] selectedIndexes) {
//        filteredList.addAll(Arrays.asList(exploringFiltersManager.getFilteredDatasetsList()));
//        for (String key : appliedFilterList.keySet()) {
//            if (appliedFilterList.get(key) != null && !appliedFilterList.get(key).isEmpty()) {
//                filteredList = filterStudies(filteredList, appliedFilterList.get(key), key);
//            }
//
//        }
        if (selectedIndexes == null) {
            filteredDatasetArr = exploringFiltersManager.getFullDatasetArr();
            selectedDatasetsIndexes = new int[filteredDatasetArr.length];
            for (int index = 0; index < filteredDatasetArr.length; index++) {
                selectedDatasetsIndexes[index] = index;
            }

        } else {
            filteredDatasetArr = new QuantDatasetObject[selectedIndexes.length];
            selectedDatasetsIndexes = selectedIndexes;
            int x = 0;
            for (int index : selectedIndexes) {
                QuantDatasetObject ds = exploringFiltersManager.getFullDatasetArr()[index];
                filteredDatasetArr[x] = ds;
//            selectedDatasetsIndexes[x] = ds.getUniqId();
                x++;
            }

        }
    }

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

//    }

    public Map<String, Set<String>> getAppliedFilterList() {
        return appliedFilterList;
    }

//    private void updateSelectedFilter(int filterIndex, boolean value) {
//        activeFilters[filterIndex] = value;
//
//    }
    private void updateCharts() {
        for (InteractiveFilter ifilter : registeredFilters) {
            ifilter.selectionChanged(selectedDatasetsIndexes, activeFilters);
        }
    }

    public void updateCentralSelectionManager(boolean selfselection) {
        this.selfselection = selfselection;
        if (selectionPerformed) {
            exploringFiltersManager.setStudyLevelFilterSelection(new CSFFilterSelection("filter", selectedDatasetsIndexes, Filter_ID, null));
        }
        selectionPerformed = false;

    }

}
