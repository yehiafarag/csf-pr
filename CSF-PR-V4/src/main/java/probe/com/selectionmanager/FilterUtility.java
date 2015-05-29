/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.selectionmanager;

import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.model.beans.QuantDatasetListObject;

/**
 *
 * @author Yehia Farag
 */
public class FilterUtility implements Serializable{
    
    private final boolean[] activeHeaders;
    private final boolean[] activeFilters;
//    private final Map<String,List<Object>> fullFilterList = new HashMap<String, List<Object>>();
  
    private final VerticalLayout filtersLayout = new VerticalLayout();
    ;
     private final VerticalLayout minLayout = new VerticalLayout();
    private final QuantDatasetObject[] quantDatasetArr;

    public FilterUtility(MainHandler handler) {
        QuantDatasetListObject quantDatasetListObject = handler.getQuantDatasetListObject();
        this.quantDatasetArr = quantDatasetListObject.getQuantDatasetList();
        activeHeaders = quantDatasetListObject.getActiveHeaders();
        //which fillters are exist
        activeFilters = handler.getActiveFilters();

    }

    public boolean[] getActiveHeaders() {
        return activeHeaders;
    }

    public boolean[] getActiveFilters() {
        return activeFilters;
    }

    public QuantDatasetObject[] getQuantDatasetArr() {
        return quantDatasetArr;
    }
    
//    public VerticalLayout initPopupFiltersLayout(DatasetExploringSelectionManagerRes exploringFiltersManager) {
//        
//    
//        
//        
//        
//        VerticalLayout tfiltersLayout = new VerticalLayout();
//        tfiltersLayout.setWidth("100%");
//        tfiltersLayout.setSpacing(true);   
//        
//        
//
//        
//        
//        int filtersLayers = 0;
//        for (boolean f : activeFilters) {
//            if (f) {
//                filtersLayers++;
//            }
//        }
//        if (filtersLayers > 4) {
//            filtersLayers = 2;
//        }
//        for (int x = 0; x < filtersLayers; x++) {
//            HorizontalLayout layer = new HorizontalLayout();
//            layer.setWidth("100%");
//            tfiltersLayout.addComponent(layer);
//
//        }
//        tfiltersLayout.setHeight(((filtersLayers * 400) + (filtersLayers * 2)) + "px");
////        filterframeLayout.setHeight((tfiltersLayout.getHeight()+22.0f), Sizeable.Unit.PIXELS);
//        
//        
////        filterframeLayout.addComponent(tfiltersLayout);
//        
//        int compCounter = 0;
//
//        for (int x = 0; x < activeFilters.length; x++) {
//            String filterId = "";
//            if (activeFilters[x]) {
//                List<Object> valueSet = new ArrayList<Object>();
//                switch (x) {
//                    case 0:
//                        filterId = "identifiedProteinsNumber";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            int value = pb.getIdentifiedProteinsNumber();
//                            valueSet.add(value);
//                        }
//                        break;
//
//                    case 1:
//                        filterId = "quantifiedProteinsNumber";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            int value = pb.getQuantifiedProteinsNumber();
//                            valueSet.add(value);
//
//                        }
//                        break;
//
//                    case 2:
//                        filterId = "diseaeGroups";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getDiseaseGroups();
//                            valueSet.add(value);
//
//                        }
//                        break;
//
//                    case 3:
//                        filterId = "rawDataUrl";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                                valueSet.add(pb.getRawDataUrl());
//                        }
//                        break;
//                    case 4:
//                        filterId = "year";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            int value = pb.getYear();
//                            valueSet.add(value);
//                        }
//                        break;
//                    case 5:
//                        filterId = "typeOfStudy";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getTypeOfStudy();
//                            valueSet.add(value);
//                        }
//                        break;
//                    case 6:
//                        filterId = "sampleType";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getSampleType();
//                            valueSet.add(value);
//                        }
//                        break;
//                    case 7:
//                        filterId = "sampleMatching";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getSampleMatching();
//                            valueSet.add(value);
//                        }
//                        break;
//                    case 8:
//                        filterId = "technology";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getTechnology();
//                            if (value == null || value.equalsIgnoreCase("")) {
//                                value = "Not Available";
//                            }
//                            valueSet.add(value);
//                        }
//                        break;
//                    case 9:
//
//                        filterId = "analyticalApproach";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getAnalyticalApproach();
//                            if (value == null || value.equalsIgnoreCase("")) {
//                                value = "Not Available";
//                            }
//                            valueSet.add(value);
//                        }
//                        break;
//                    case 10:
//                        filterId = "enzyme";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getEnzyme();
//                            if (value == null || value.equalsIgnoreCase("")) {
//                                value = "Not Available";
//                            }
//                            valueSet.add(value);
//                        }
//                        break;
//                    case 11:
//                        filterId = "shotgunTargeted";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getShotgunTargeted();
//                            valueSet.add(value);
//                        }
//                        break;
//
//                    case 12:
//                        filterId = "quantificationBasis";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getQuantificationBasis();
//                            valueSet.add(value);
//                        }
//                        break;
//                    case 13:
//                        filterId = "quantBasisComment";
//                        for (QuantDatasetObject pb : quantDatasetArr) {
//                            String value = pb.getQuantBasisComment();
//                            valueSet.add(value);
//                        }
//                        break;
//                    case 14:
////                        for (QuantDatasetObject pb : QuantDatasetListObject) {
////                            int value = pb.getQuantifiedProteinsNumber();
////                            valueSet.add(value);
////                        }
//                        break;
//                    case 15:
////                        for (QuantDatasetObject pb : QuantDatasetListObject) {
////                            int value = pb.getPatientsGroup1Number();
////                            valueSet.add(value);
////                        }
//                        break;
//                    case 16:
////                        for (QuantDatasetObject pb : QuantDatasetListObject) {
////                            int value = pb.getPatientsGroup2Number();
////                            valueSet.add(value);
////                        }
//                        break;
//                    case 17:
////                        for (QuantDatasetObject pb : QuantDatasetListObject) {
////                            String value = pb.getNormalizationStrategy();
////                            valueSet.add(value);
////                        }
//                        break;
//
//                }
//                if (!valueSet.isEmpty()) {
//                    compCounter++;
//                    InteractiveFilter iFilter = new InteractiveFilter(valueSet, filterId,x, exploringFiltersManager);
//                    fullFilterList.put(filterId, valueSet);
//                    if (compCounter <= 4) {
//                        ((HorizontalLayout) tfiltersLayout.getComponent(0)).addComponent(iFilter);
//                        ((HorizontalLayout) tfiltersLayout.getComponent(0)).setComponentAlignment(iFilter, Alignment.MIDDLE_CENTER);
//                        iFilter.showChart();
//                    } else {
//                        ((HorizontalLayout) tfiltersLayout.getComponent(1)).addComponent(iFilter);
//                        ((HorizontalLayout) tfiltersLayout.getComponent(1)).setComponentAlignment(iFilter, Alignment.MIDDLE_CENTER);
//                        iFilter.showChart();
//                    }
//                }
//
//            }
//
//        }
//        return tfiltersLayout;
//
//    }
    
    
    
    
    
      public VerticalLayout getFiltersLayout() {
        return filtersLayout;
    }

    public VerticalLayout getMinLayout() {
        return minLayout;
    }

//    public Map<String,List<Object>> getFullFilterList() {
//        return fullFilterList;
//    }
    
  
}
