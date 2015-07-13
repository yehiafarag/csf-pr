/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.shared.ui.window.WindowMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.Reindeer;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.core.ListSelectDatasetExplorerFilter;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author yfa041
 */
public class DiseaseGroupsListFilter extends VerticalLayout implements CSFFilter, Button.ClickListener {

    private final ListSelectDatasetExplorerFilter patientGroupIFilter, patientGroupIIFilter;
    private String[] patGr1, patGr2;
    private DiseaseGroup[] patientsGroupArr;
    private Set<String> diseaseGroupsSet;
    private Button diseaseGroupsFilterBtn;
    private final Window popupWindow;
    private int[] studiesIndexes;
    private final Set<String> selectedRows = new TreeSet<String>();
    private final Set<String> selectedColumns = new TreeSet<String>();
    private final DatasetExploringCentralSelectionManager centralSelectionManager;
    private boolean selfselected = false;
    private Property.ValueChangeListener disGrIListener, disGrIIListener;

    public Set<String> getDiseaseGroupsSet() {
        return diseaseGroupsSet;
    }

    public Button getDiseaseGroupsFilterBtn() {
        return diseaseGroupsFilterBtn;
    }

    @Override
    public void selectionChanged(String type) {
        if (selfselected) {
            selfselected = false;
            return;
        }
        if (type.equalsIgnoreCase("Disease_Groups_Level")) {
            this.updatePatientGroups(centralSelectionManager.getFilteredDatasetsList());
            String[] pgArr = merge(patGr1, patGr2);

            selectedRows.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("")) {
                    selectedRows.add(str);
                }
            }
            patientGroupIFilter.updateList(selectedRows);
            selectedColumns.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("")) {
                    selectedColumns.add(str);
                }
            }
            patientGroupIIFilter.updateList(selectedColumns);
            patientGroupIIFilter.setEnabled(false);
            Set<DiseaseGroup> p = filterPGR2(selectedRows, selectedColumns);
            studiesIndexes = new int[p.size()];
            int i = 0;
            for (DiseaseGroup pg : p) {
                studiesIndexes[i] = pg.getOriginalDatasetIndex();
                i++;
            }
            updateSelectionManager(null);
        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
            this.updatePatientGroups(centralSelectionManager.getFilteredDatasetsList());
            String[] pgArr = merge(patGr1, patGr2);
            selectedRows.clear();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("")) {
                    selectedRows.add(str);
                }
            }
            patientGroupIFilter.updateList(selectedRows);
            patientGroupIIFilter.updateList(selectedRows);
            patientGroupIIFilter.setEnabled(false);
            selectedColumns.clear();
            selectedColumns.addAll(selectedRows);
            Set<DiseaseGroup> p = filterPGR2(selectedRows, selectedColumns);
            studiesIndexes = new int[p.size()];
            int i = 0;
            for (DiseaseGroup pg : p) {
                studiesIndexes[i] = pg.getOriginalDatasetIndex();
                i++;
            }
            updateSelectionManager(null);
        }
    }

    @Override
    public String getFilterId() {
        return DiseaseGroupsListFilter.class.getName();
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public DiseaseGroupsListFilter(DatasetExploringCentralSelectionManager centralSelectionManager) {
        this.centralSelectionManager = centralSelectionManager;
        this.setWidth("450px");
        this.centralSelectionManager.registerFilter(DiseaseGroupsListFilter.this);

        this.updatePatientGroups(centralSelectionManager.getFilteredDatasetsList());
        String[] pgArr = merge(patGr1, patGr2);
        this.patientGroupIFilter = new ListSelectDatasetExplorerFilter(1, "Patients Group I", pgArr);
        initGroupsIFilter();

        this.patientGroupIIFilter = new ListSelectDatasetExplorerFilter(2, "Patients Group II", pgArr);
        initGroupsIIFilter();
        diseaseGroupsSet = new TreeSet<String>(Arrays.asList(pgArr));

        this.addComponent(patientGroupIIFilter);

        diseaseGroupsFilterBtn = new Button("Disease Groups Filter");
        diseaseGroupsFilterBtn.setHeight("30px");
        diseaseGroupsFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
        diseaseGroupsFilterBtn.addClickListener(DiseaseGroupsListFilter.this);

        VerticalLayout popupBody = new VerticalLayout();
        popupBody.setSpacing(true);
        popupBody.setWidth("500px");
        popupBody.setHeightUndefined();

        popupWindow = new Window() {
            @Override
            public void close() {
                popupWindow.setVisible(false);

            }
        };
        popupWindow.setContent(popupBody);
        popupWindow.setWindowMode(WindowMode.NORMAL);
        popupWindow.setWidth((540) + "px");
        popupWindow.setHeight((500) + "px");
        popupWindow.setVisible(false);
        popupWindow.setResizable(false);
        popupWindow.setClosable(false);
        popupWindow.setStyleName(Reindeer.WINDOW_LIGHT);
        popupWindow.setModal(true);
        popupWindow.setDraggable(false);
        popupWindow.center();
        popupWindow.setCaption("&nbsp;&nbsp;Patients Groups Comparisons");

        UI.getCurrent().addWindow(popupWindow);
        popupWindow.center();

        popupWindow.setCaptionAsHtml(true);
        popupWindow.setClosable(true);

        this.setHeightUndefined();
        popupBody.addComponent(DiseaseGroupsListFilter.this);
        popupBody.setComponentAlignment(DiseaseGroupsListFilter.this, Alignment.BOTTOM_CENTER);

    }

    /**
     * this method update the labels value for the groups selection list
     *
     * @param quantDSArr array of the quant datasets
     */
    private void updatePatientGroups(QuantDatasetObject[] quantDSArr) {
        patGr1 = new String[quantDSArr.length];
        patGr2 = new String[quantDSArr.length];
        patientsGroupArr = new DiseaseGroup[quantDSArr.length];
        int i = 0;
        for (QuantDatasetObject ds : quantDSArr) {
            DiseaseGroup pg = new DiseaseGroup();
            String pgI = ds.getPatientsGroup1();
            pg.setPatientsGroupI(pgI);
            String label1;
            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
                pgI = "";
            }
            String subpgI = ds.getPatientsSubGroup1();
            pg.setPatientsSubGroupI(subpgI);
            if (!subpgI.equalsIgnoreCase("") && !subpgI.equalsIgnoreCase("Not Available")) {
                pgI = subpgI;
            } else {
//                subpgI = "";
            }
            label1 = pgI;
            pg.setPatientsGroupILabel(label1);

            String pgII = ds.getPatientsGroup2();
            pg.setPatientsGroupII(pgII);
            String label2;
            if (pgII.equalsIgnoreCase("Not Available") || pgII.equalsIgnoreCase("control")) {
                pgII = "";
            }
            String subpgII = ds.getPatientsSubGroup2();
            pg.setPatientsSubGroupII(subpgII);
            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
                pgII = subpgII;
            }
            label2 = pgII;
            pg.setPatientsGroupIILabel(label2);
            patientsGroupArr[i] = pg;
            patGr1[i] = label1;
            patGr2[i] = label2;
            pg.setQuantDatasetIndex(i);
            pg.setOriginalDatasetIndex(ds.getUniqId());
            i++;
        }

    }

    private String[] merge(String[] arr1, String[] arr2) {
        String[] newArr = new String[arr1.length + arr2.length];
        int i = 0;
        for (String str : arr1) {
            newArr[i] = str;
            i++;
        }
        for (String str : arr2) {
            newArr[i] = str;
            i++;
        }
        Arrays.sort(newArr);
        return newArr;

    }

    private void initGroupsIFilter() {
        patientGroupIFilter.getList().setHeight("150px");
        patientGroupIFilter.getList().setWidth("380px");
        patientGroupIFilter.setMargin(new MarginInfo(true, false, true, false));
        this.addComponent(patientGroupIFilter);
        this.setComponentAlignment(patientGroupIFilter, Alignment.MIDDLE_CENTER);
        disGrIListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectedRows.clear();
                boolean enable = true;
                for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
                    if (patientGroupIFilter.getList().isSelected(id.toString())) {
                        selectedRows.add(id.toString());
                    }
                }
                if (selectedRows.isEmpty()) {
                    enable = false;
                    for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
                        selectedRows.add(id.toString());
                    }

                }
                selectedColumns.clear();
                Set<DiseaseGroup> p = filterPatGroup2List(selectedRows);
                studiesIndexes = new int[p.size()];
                int i = 0;
                for (DiseaseGroup pg : p) {
                    studiesIndexes[i] = pg.getOriginalDatasetIndex();
                    i++;
                }
                for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
                    selectedColumns.add(id.toString());

                }
                patientGroupIIFilter.setEnabled(enable);
                updateSelectionManager(studiesIndexes);
            }
        };
        patientGroupIFilter.addValueChangeListener(disGrIListener);

    }

    private void updateSelectionManager(int[] datasetIndexes) {
        selfselected = true;
        if (datasetIndexes != null) {
            centralSelectionManager.setStudyLevelFilterSelection(new CSFFilterSelection("Disease_Groups_Level", datasetIndexes, "", new HashSet<String>()));
        }
        centralSelectionManager.setHeatMapLevelSelection(selectedRows, selectedColumns, patientsGroupArr);
    }

    private void initGroupsIIFilter() {
        patientGroupIIFilter.getList().setHeight("150px");
        patientGroupIIFilter.getList().setWidth("380px");
        patientGroupIIFilter.setEnabled(false);
        patientGroupIIFilter.setMargin(new MarginInfo(false, false, true, false));
         this.addComponent(patientGroupIIFilter);
        this.setComponentAlignment(patientGroupIIFilter, Alignment.MIDDLE_CENTER);
        disGrIIListener = new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectedRows.clear();
                for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
                    if (patientGroupIFilter.getList().isSelected(id.toString())) {
                        selectedRows.add(id.toString());
                    }
                }
                selectedColumns.clear();
                for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
                    if (patientGroupIIFilter.getList().isSelected(id.toString())) {
                        selectedColumns.add(id.toString());
                    }
                }
                if (selectedColumns.isEmpty()) {
                    for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
                        selectedColumns.add(id.toString());
                    }

                }

                Set<DiseaseGroup> p = filterPGR2(selectedRows, selectedColumns);
                studiesIndexes = new int[p.size()];
                int i = 0;
                for (DiseaseGroup pg : p) {
                    studiesIndexes[i] = pg.getOriginalDatasetIndex();
                    i++;
                }
                updateSelectionManager(studiesIndexes);
            }
        };
        patientGroupIIFilter.addValueChangeListener(disGrIIListener);
    }

    private Set<DiseaseGroup> filterPatGroup2List(Set<String> sel1) {
        Set<DiseaseGroup> tempPatientsGroup = new HashSet<DiseaseGroup>();
        Set<String> labels = new TreeSet<String>();

        for (DiseaseGroup pg : patientsGroupArr) {
            for (String label : sel1) {
                if (pg.checkLabel(label)) {
                    tempPatientsGroup.add(pg);
                    labels.add(pg.getValLabel(label));
                }
            }
        }

        patientGroupIIFilter.updateList(labels);
        return tempPatientsGroup;
    }

    private Set<DiseaseGroup> filterPGR2(Set<String> L1, Set<String> L2) {
        Set<DiseaseGroup> tempPatientsGroup = new HashSet<DiseaseGroup>();

        for (DiseaseGroup pg : patientsGroupArr) {
            boolean breakfor = false;
            for (String label : L1) {
                if (pg.checkLabel(label)) {
                    for (String label2 : L2) {
                        if (pg.checkLabel(label2)) {
                            tempPatientsGroup.add(pg);
                            breakfor = true;
                            break;
                        }
                    }
                    if (breakfor) {
                        break;
                    }
                }
            }
        }

        return tempPatientsGroup;

    }

    public Set<String> getSelectedRows() {
        return selectedRows;
    }

    public Set<String> getSelectedColumns() {
        return selectedColumns;
    }

//    public QuantDSIndexes[][] getValues() {
//        return values;
//    }
//    public int getMaxDatasetNumber() {
//        return maxDatasetNumber;
//    }
//    private int maxDatasetNumber;
//    public void selectionChanged(QuantDatasetObject[] dsArr, String type) {
//        System.out.println("type at list fileter "+type);
//        if (type.equalsIgnoreCase("Disease_Groups_Level")) {
//
//            this.updatePatientGroups(dsArr);
//            String[] pgArr = merge(patGr1, patGr2);
//            selectedRows.clear();
//            for (String str : pgArr) {
//                if (!str.equalsIgnoreCase("")) {
//                    selectedRows.add(str);
//                }
//            }
//            patientGroupIFilter.updateList(selectedRows);
//            selectedColumns.clear();
//
//            for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
//                selectedColumns.add(id.toString());
//
//            }
//            patientGroupIIFilter.updateList(selectedColumns);
//            patientGroupIIFilter.setEnabled(false);
//            Set<PatientsGroup> p = filterPGR2(selectedRows, selectedColumns);
//            int[] indexes = new int[p.size()];
//            int i = 0;
//            for (DiseaseGroup pg : p) {
//                indexes[i] = pg.getOriginalDatasetIndex();
//                i++;
//            }
//
////            parent.updateHeatmap(selectedRows, sel2, indexes, patientsGroupArr);
////           values = calcHeatMapMatrix(selectedRows, selectedColumns);
////            heatMap.updateHeatMap(selectedRows, selectedColumns, values, maxDatasetNumber);
//
//        } else if (type.equalsIgnoreCase("Reset_Disease_Groups_Level")) {
//            this.updatePatientGroups(dsArr);
//            String[] pgArr = merge(patGr1, patGr2);
//            selectedRows.clear();
//            for (String str : pgArr) {
//                if (!str.equalsIgnoreCase("")) {
//                    selectedRows.add(str);
//                }
//            }
//            patientGroupIFilter.updateList(selectedRows);
//            patientGroupIIFilter.updateList(selectedRows);
//            patientGroupIIFilter.setEnabled(false);
//            selectedColumns.clear();
//            selectedColumns.addAll(selectedRows);
//            Set<PatientsGroup> p = filterPGR2(selectedRows, selectedColumns);
//            int[] indexes = new int[p.size()];
//            int i = 0;
//            for (DiseaseGroup pg : p) {
//                indexes[i] = pg.getOriginalDatasetIndex();
//                i++;
//            }
////            parent.updateHeatmap(selectedRows, selectedColumns, indexes, patientsGroupArr);
//
//        } else if (type.equalsIgnoreCase("ComparisonSelection")) {
////            parent.updateDsCellSelection();
////            heatMap.updateDsCellSelection(exploringFiltersManager.getSelectedComparisonList());
////            if (exploringFiltersManager.getSelectedComparisonList().isEmpty()) {
////                viewComparisonHeatmap(true);
////            }
//
//        }
//
//    }
    public DiseaseGroup[] getPatientsGroupArr() {
        return patientsGroupArr;
    }

    public int[] getStudiesIndexes() {
        return studiesIndexes;
    }

    @Override
    public void buttonClick(Button.ClickEvent arg0) {
        popupWindow.setVisible(!popupWindow.isVisible());
    }

}
