/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;

/**
 *
 * @author yfa041
 */
public class PieChartsSelectionManager implements Serializable, CSFFilter {

    private boolean externalSelectionChanged;

    @Override
    public void selectionChanged(String type) {

        if (type.equalsIgnoreCase("Disease_Groups_Level")) {
            if (selfselection) {
                selfselection = false;
                return;
            }
            externalSelectionChanged = true;
            selectedDsIds.clear();
            for (QuantDatasetObject qDs : centralSelectionManager.getFilteredDatasetArr()) {
                selectedDsIds.add(qDs.getUniqId());
            }

        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
            if (selfselection) {
                selfselection = false;
                return;
            }
            selectedDsIds.clear();
            resetToInitState();
            unselectAll();

        }

    }

    private final Set<JfreeDivaPieChartFilter> registeredFilters = new HashSet<JfreeDivaPieChartFilter>();
    private final String Filter_ID = "interactiveChartsFilter";

    private final Map<String, String> titleMap = new HashMap<String, String>();
    private final DatasetExploringCentralSelectionManager centralSelectionManager;

    public PieChartsSelectionManager(DatasetExploringCentralSelectionManager centralSelectionManager) {
        centralSelectionManager.registerFilter(PieChartsSelectionManager.this);
        this.centralSelectionManager = centralSelectionManager;
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
    }

    public void registerFilter(JfreeDivaPieChartFilter filter) {
        this.registeredFilters.add(filter);
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

    private final Set<Integer> selectedDsIds = new HashSet<Integer>();

    public void jfreeUpdateChartsFilters(String filterId) {
        this.updateDsIndexSelection();
        this.updatePieChartCharts(filterId);

    }

    private void updatePieChartCharts(String filterId) {
        for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
            if (ifilter.getFilter_Id().equalsIgnoreCase(filterId)) {
                continue;
            }
            ifilter.selectionChanged(selectedDsIds);
        }
    }

    public void resetPieChartCharts() {

        if (externalSelectionChanged) {
            externalSelectionChanged = false;
            for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
                ifilter.resetFilterWithUpdatedFilters(selectedDsIds, true);
            }

        }

    }

    public void resetToInitState() {
        selectedDsIds.clear();
        for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
            ifilter.resetFilterToClearState();
        }

    }

    public void unselectAll() {
        for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
            ifilter.unselectFilter();
        }

        Set<Integer> tempSelectedDsIds = new HashSet<Integer>();
        for (QuantDatasetObject qDs : centralSelectionManager.getFilteredDatasetArr()) {
            tempSelectedDsIds.add(qDs.getUniqId());
        }
        tempSelectedDsIds.removeAll(selectedDsIds);
        if (tempSelectedDsIds.isEmpty()) {
            selectedDsIds.clear();
        } else {
            selectedDsIds.addAll(tempSelectedDsIds);
        }

        updatePieChartCharts("");
    }

    private boolean selfselection;

    public void updateCentralSelectionManager(boolean selfselection) {
        this.selfselection = selfselection;
        int[] dsIds = new int[selectedDsIds.size()];
        for (int i = 0; i < selectedDsIds.size(); i++) {
            dsIds[i] = (Integer) selectedDsIds.toArray()[i];
        }
        centralSelectionManager.setStudyLevelFilterSelection(new CSFFilterSelection("Disease_Groups_Level", dsIds, Filter_ID, null));

    }

    public void updateDsIndexSelection() {
        selectedDsIds.clear();
        Set<JfreeDivaPieChartFilter> activeFilterSet = new HashSet<JfreeDivaPieChartFilter>();
        Set<Integer> activeSelectedDsIds = new HashSet<Integer>();
        for (JfreeDivaPieChartFilter ifilter : registeredFilters) {
            if (!ifilter.getSelectedDsIds().isEmpty()) {
                activeFilterSet.add(ifilter);
                activeSelectedDsIds.addAll(ifilter.getSelectedDsIds());
            }
        }
        selectedDsIds.addAll(activeSelectedDsIds);
        for (int dsId : activeSelectedDsIds) {
            boolean check;
            for (JfreeDivaPieChartFilter iFilter : activeFilterSet) {
                check = iFilter.getSelectedDsIds().contains(dsId);
                if (!check) {
                    selectedDsIds.remove(dsId);
                    break;
                }
            }

        }

    }

    public void resetCentralSelectionManager() {
        selfselection = true;
        centralSelectionManager.resetFilters();

    }
}
