/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.bin;

import bin.probe.com.view.bin.quantdatasetsoverview.popupinteractivefilter.LocalSelectionManager;
import bin.probe.com.view.bin.quantdatasetsoverview.popupinteractivefilter.InteractiveFilter;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class PopupInteractiveFilterComponent extends VerticalLayout{
    private final Map<String,List<Object>> fullFilterList = new HashMap<String, List<Object>>();
    private final LocalSelectionManager internalSelectionManager;
    

    public PopupInteractiveFilterComponent(DatasetExploringCentralSelectionManager exploringFiltersManager) {

        this.setWidth("100%");
        this.setSpacing(true);
        boolean[] activeFilters = exploringFiltersManager.getActiveFilters();
        QuantDatasetObject[] quantDatasetArr = exploringFiltersManager.getFilteredDatasetsList();
        internalSelectionManager = new LocalSelectionManager(exploringFiltersManager);

        int filtersLayers = 0;
        for (boolean f : activeFilters) {
            if (f) {
                filtersLayers++;
            }
        }
        if (filtersLayers > 4) {
            filtersLayers = 2;
        }
        for (int x = 0; x < filtersLayers; x++) {
            HorizontalLayout layer = new HorizontalLayout();
            layer.setWidth("100%");
            this.addComponent(layer);

        }
        this.setHeight(((filtersLayers * 400) + (filtersLayers * 2)) + "px");
//        filterframeLayout.setHeight((this.getHeight()+22.0f), Sizeable.Unit.PIXELS);
        
        
//        filterframeLayout.addComponent(this);        
        int compCounter = 0;
        for (int x = 0; x < activeFilters.length; x++) {
            String filterId = "";
            if (activeFilters[x]) {
                Map<String,List<Integer>> dsIndexesMap = new HashMap<String, List<Integer>>();
                List<Object> valueSet = new ArrayList<Object>();
                switch (x) {
                    case 0:
//                        filterId = "identifiedProteinsNumber";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            int value = pb.getIdentifiedProteinsNumber();
//                            valueSet.add(value);
//                        }
                        break;

                    case 1:
//                        filterId = "quantifiedProteinsNumber";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            int value = pb.getQuantifiedProteinsNumber();
//                            valueSet.add(value);
//
//                        }
                        break;

                    case 2:
//                        filterId = "diseaeGroups";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            if (pb == null) {
//                                continue;
//                            }
//                            String value = pb.getDiseaseGroups();
//                            valueSet.add(value);
//
//                        }
                        break;

                    case 3:
                        filterId = "rawDataUrl";
                        for (QuantDatasetObject pb : quantDatasetArr) {

                            if (pb == null) {
                                continue;
                            }
                            if (!dsIndexesMap.containsKey(pb.getRawDataUrl())) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getRawDataUrl(), list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getRawDataUrl());
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(pb.getRawDataUrl(), list);
                            valueSet.add(pb.getRawDataUrl());
                            

                        }
                        break;
                    case 4:
                        
                        filterId = "year";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                            if (!dsIndexesMap.containsKey(pb.getYear()+"")) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getYear()+"", list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getYear()+"");
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(pb.getYear()+"", list);
                            int value = pb.getYear();
                            valueSet.add(value);
                        }
                        break;
                    case 5:
                        filterId = "typeOfStudy";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                            if (!dsIndexesMap.containsKey(pb.getTypeOfStudy())) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getTypeOfStudy(), list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getTypeOfStudy());
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(pb.getTypeOfStudy(), list);
                            String value = pb.getTypeOfStudy();
                            valueSet.add(value);
                        }
                        break;
                    case 6:
                        filterId = "sampleType";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                             if (!dsIndexesMap.containsKey(pb.getSampleType())) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getSampleType(), list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getSampleType());
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(pb.getSampleType(), list);
                            String value = pb.getSampleType();
                            valueSet.add(value);
                        }
                        break;
                    case 7:
                        filterId = "sampleMatching";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                             if (!dsIndexesMap.containsKey(pb.getSampleMatching())) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getSampleMatching(), list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getSampleMatching());
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(pb.getSampleMatching(), list);
                            
                            String value = pb.getSampleMatching();
                            valueSet.add(value);
                        }
                        break;
                    case 8:
                        filterId = "technology";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getTechnology();
                            if (value == null || value.equalsIgnoreCase("")) {
                                value = "Not Available";
                            }
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(value);
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;
                    case 9:

                        filterId = "analyticalApproach";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getAnalyticalApproach();
                            if (value == null || value.equalsIgnoreCase("")) {
                                value = "Not Available";
                            }
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getAnalyticalApproach());
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;
                    case 10:
                        filterId = "enzyme";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getEnzyme();
                            if (value == null || value.equalsIgnoreCase("")) {
                                value = "Not Available";
                            }
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(value);
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;
                    case 11:
                        filterId = "shotgunTargeted";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getShotgunTargeted();
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(value);
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;

                    case 12:
                        filterId = "quantificationBasis";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getQuantificationBasis();
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(value);
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;
                    case 13:
                        filterId = "quantBasisComment";
                        for (QuantDatasetObject pb : quantDatasetArr) {
                            if (pb == null) {
                                continue;
                            }
                            String value = pb.getQuantBasisComment();
                            if (!dsIndexesMap.containsKey(value)) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(value, list);

                            }
                            List<Integer> list = dsIndexesMap.get(value);
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(value, list);
                            valueSet.add(value);
                        }
                        break;
                    case 14:
//                        for (QuantDatasetObject pb : QuantDatasetListObject) {
//                            int value = pb.getQuantifiedProteinsNumber();
//                            valueSet.add(value);
//                        }
                        break;
                    case 15:
//                        for (QuantDatasetObject pb : QuantDatasetListObject) {
//                            int value = pb.getPatientsGroup1Number();
//                            valueSet.add(value);
//                        }
                        break;
                    case 16:
//                        for (QuantDatasetObject pb : QuantDatasetListObject) {
//                            int value = pb.getPatientsGroup2Number();
//                            valueSet.add(value);
//                        }
                        break;
                    case 17:
//                        for (QuantDatasetObject pb : QuantDatasetListObject) {
//                            String value = pb.getNormalizationStrategy();
//                            valueSet.add(value);
//                        }
                        break;

                }
                if (!valueSet.isEmpty()) {
                    compCounter++;
                    InteractiveFilter iFilter = new InteractiveFilter(valueSet, filterId,x, internalSelectionManager,dsIndexesMap);
                    fullFilterList.put(filterId, valueSet);
                    if (compCounter <= 4) {
                        ((HorizontalLayout) this.getComponent(0)).addComponent(iFilter);
                        ((HorizontalLayout) this.getComponent(0)).setComponentAlignment(iFilter, Alignment.MIDDLE_CENTER);
                        iFilter.showChart();
                    } else {
                        ((HorizontalLayout) this.getComponent(1)).addComponent(iFilter);
                        ((HorizontalLayout) this.getComponent(1)).setComponentAlignment(iFilter, Alignment.MIDDLE_CENTER);
                        iFilter.showChart();
                    }
                }

            }

        }
    }
    
    public void updateSelectionManager(boolean selfSelection){
        internalSelectionManager.updateCentralSelectionManager(selfSelection);
    
    }
    
    
    
    

//    @Override
//    public void selectionChanged(String type) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public String getFilterId() {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public void removeFilterValue(String value) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//    public Map<String,List<Object>> getFullFilterList() {
//        return fullFilterList;
//    }
    
    
    
    
}
