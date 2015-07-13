/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters;

import com.vaadin.server.Page;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.PopupInteractiveDSFiltersLayout;

/**
 *
 * @author Yehia Farag
 */
public class StudiesPieChartFiltersContainerLayout extends GridLayout {

    private final PieChartsSelectionManager internalSelectionManager;
    private PopupInteractiveDSFiltersLayout pieChartFiltersBtn;

    public StudiesPieChartFiltersContainerLayout(DatasetExploringCentralSelectionManager exploringFiltersManager) {

        int layoutHeight = Page.getCurrent().getBrowserWindowHeight() - 200;
        int layoutWidth = Page.getCurrent().getBrowserWindowWidth() - 200;
        this.setWidth(layoutWidth + "px");
        this.setHeight(layoutHeight + "px");
        int filterWidth = layoutWidth / 4;
        this.setSpacing(true);
        boolean[] activeFilters = exploringFiltersManager.getActiveFilters();
        QuantDatasetObject[] quantDatasetArr = exploringFiltersManager.getFilteredDatasetsList();
        internalSelectionManager = new PieChartsSelectionManager(exploringFiltersManager);
        this.setRows(3);
        this.setColumns(4);
        int colCounter = 0;
        int rowCounter = 0;

        for (int x = 0; x < activeFilters.length; x++) {
            String filterId = "";
            if (activeFilters[x]) {
                Map<String, List<Integer>> dsIndexesMap = new HashMap<String, List<Integer>>();
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
                            if (!dsIndexesMap.containsKey(pb.getYear() + "")) {
                                List<Integer> list = new ArrayList<Integer>();
                                dsIndexesMap.put(pb.getYear() + "", list);

                            }
                            List<Integer> list = dsIndexesMap.get(pb.getYear() + "");
                            list.add(pb.getUniqId());
                            dsIndexesMap.put(pb.getYear() + "", list);
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
                    //do we need valueSet;;
                    JfreeDivaPieChartFilter iFilter = new JfreeDivaPieChartFilter(filterId, x, internalSelectionManager, dsIndexesMap, filterWidth);
//                    fullFilterList.put(filterId, valueSet);
                    this.addComponent(iFilter, colCounter++, rowCounter);
                    this.setComponentAlignment(iFilter, Alignment.MIDDLE_CENTER);
                    if (colCounter == 4) {
                        colCounter = 0;
                        rowCounter++;
                    }
                }

            }

        }
        HorizontalLayout btnLayout = new HorizontalLayout();
        btnLayout.setHeightUndefined();
        btnLayout.setWidthUndefined();
        btnLayout.setSpacing(true);
        btnLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(btnLayout, 0, 2);
        this.setComponentAlignment(btnLayout, Alignment.MIDDLE_CENTER);
        Button applyFilters = new Button("Apply Filters");
        applyFilters.setStyleName(Reindeer.BUTTON_SMALL);
        btnLayout.addComponent(applyFilters);
        applyFilters.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                pieChartFiltersBtn.closePupupWindow();
            }
        });

        Button unselectAllBtn = new Button("Unselect All");
        unselectAllBtn.setStyleName(Reindeer.BUTTON_SMALL);
        btnLayout.addComponent(unselectAllBtn);
        unselectAllBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {

                internalSelectionManager.unselectAll();

            }
        });

        Button resetFiltersBtn = new Button("Reset");
        resetFiltersBtn.setStyleName(Reindeer.BUTTON_SMALL);
        btnLayout.addComponent(resetFiltersBtn);
        resetFiltersBtn.setDescription("Reset all applied filters");
        resetFiltersBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                internalSelectionManager.resetToInitState();
                internalSelectionManager.resetCentralSelectionManager();
            }
        });
        pieChartFiltersBtn = new PopupInteractiveDSFiltersLayout(this);
    }

    public PopupInteractiveDSFiltersLayout getPieChartFiltersBtn() {
        return pieChartFiltersBtn;
    }

    public void updatePieChartCharts() {
        internalSelectionManager.resetPieChartCharts();

    }

    public void updateSelectionManager(boolean selfSelection) {
        internalSelectionManager.updateCentralSelectionManager(selfSelection);

    }

}
