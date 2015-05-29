/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components.heatmap;

import probe.com.view.components.popupinteractivefilter.PopupInteractiveDSFiltersLayout;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;
import probe.com.model.beans.GroupsComparison;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.DatasetExploringSelectionManagerRes;
import probe.com.model.beans.QuantDSIndexes;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.view.components.HorizontalClickToDisplay;
import probe.com.view.datasetsoverview.QuantProteinsComparisonTable;
import probe.com.view.components.popupinteractivefilter.PopupInteractiveFilterComponent;
import probe.com.view.core.ListSelectDatasetExplorerFilter;
import probe.com.view.core.PatientsGroup;

/**
 * this class represents the top filters layout for exploring datasets tab the
 * class include the the patients group comparison lists, filtering heat-map and
 * pie chart filters
 *
 * @author Yehia Farag
 */
public class DatasetExploringHeatMapFilters extends GridLayout implements  CSFFilter {

    private final DatasetExploringSelectionManagerRes exploringFiltersManager;

    private ListSelectDatasetExplorerFilter patientGroupIFilter, patientSubGroupIFilter, patientGroupIIFilter, patientSubGroupIIFilter;
    private String[] patGr1, patGr2;
    private PatientsGroup[] patientsGroupArr;
    
    
    private final VerticalLayout GroupComparisonFiltersPanelLayout;
    private final VerticalLayout heatmapLayout;
    private HeatMapComponent heatMap;
    private boolean selfselected=false;

    public DatasetExploringHeatMapFilters(final DatasetExploringSelectionManagerRes exploringFiltersManager, PopupInteractiveFilterComponent filtersLayout, QuantProteinsComparisonTable compTable) {
        this.setHeight("100%");
        this.setWidth("100%");
        this.exploringFiltersManager = exploringFiltersManager;   
        
        this.setColumns(3);
        this.setRows(1);
        this.setColumnExpandRatio(0, 0.05f);
        this.setColumnExpandRatio(1, 0.3f);
        this.setColumnExpandRatio(2, 0.65f);
        // init group comparisons layout 
        
        this.GroupComparisonFiltersPanelLayout = new VerticalLayout();
        this.GroupComparisonFiltersPanelLayout.setWidth("450px");
        this.setSpacing(true);           

        this.updatePatientGroups(exploringFiltersManager.getFilteredDatasetsList());
        String[] pgArr = merge(patGr1, patGr2);
        patientGroupIFilter = new ListSelectDatasetExplorerFilter(1, "Patients Group I", pgArr);
        patientGroupIFilter.getList().setHeight("150px");
        patientGroupIFilter.getList().setWidth("380px");
        patientGroupIFilter.setMargin(new MarginInfo(false, false,true,false));
        GroupComparisonFiltersPanelLayout.addComponent(patientGroupIFilter);
        GroupComparisonFiltersPanelLayout.setComponentAlignment(patientGroupIFilter, Alignment.TOP_LEFT);
        patientGroupIFilter.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Set<String> sel1 = new TreeSet<String>();
                boolean enable = true;
                for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
                    if (patientGroupIFilter.getList().isSelected(id.toString())) {
                        sel1.add(id.toString());
                    }
                }
                if (sel1.isEmpty()) {
                   enable =false; 
                    for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
                        sel1.add(id.toString());
                    }
                    
                }
                Set<String> sel2 = new TreeSet<String>();
                Set<PatientsGroup> p = filterPatGroup2List(sel1);
                int[] indexes = new int[p.size()];
                int i = 0;
                for (PatientsGroup pg : p) {
                    indexes[i] = pg.getOriginalDatasetIndex();                    
                    i++;
                }
                 for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {                    
                        sel2.add(id.toString());
                    
                }
                 patientGroupIIFilter.setEnabled(enable);
                 QuantDSIndexes[][] values = calcHeatMapMatrix(sel1, sel2);
                heatMap.updateHeatMap(sel1, sel2,values,maxDatasetNumber);
                sel1.addAll(sel2);
                updateSelectionManager(indexes);
            }
        });
        
        patientGroupIIFilter = new ListSelectDatasetExplorerFilter(2, "Patients Group II", pgArr);
        patientGroupIIFilter.getList().setHeight("150px");
        patientGroupIIFilter.getList().setWidth("380px");
        patientGroupIIFilter.setEnabled(false);
        patientGroupIIFilter.setMargin(new MarginInfo(false, false,true,false));
        patientGroupIIFilter.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                Set<String> sel1 = new TreeSet<String>();
                for (Object id : patientGroupIFilter.getList().getItemIds().toArray()) {
                    if (patientGroupIFilter.getList().isSelected(id.toString())) {
                        sel1.add(id.toString());
                    }
                }
                Set<String> sel2 = new TreeSet<String>();
                for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
                    if (patientGroupIIFilter.getList().isSelected(id.toString())) {
                        sel2.add(id.toString());
                    }
                }
                if (sel2.isEmpty()) {
                    for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
                        sel2.add(id.toString());
                    }

                }

                
                
                Set<PatientsGroup> p = filterPGR2(sel1, sel2);
                int[] indexes = new int[p.size()];
                int i = 0;
                for (PatientsGroup pg : p) {
                    indexes[i] = pg.getOriginalDatasetIndex();
                    i++;
                }

                 QuantDSIndexes[][] values =  calcHeatMapMatrix(sel1, sel2);
                heatMap.updateHeatMap(sel1, sel2, values, maxDatasetNumber);
                updateSelectionManager(indexes);
            }
        });
        Set<String> pgSet = new TreeSet<String>(Arrays.asList(pgArr));

        GroupComparisonFiltersPanelLayout.addComponent(patientGroupIIFilter);
        GroupComparisonFiltersPanelLayout.setComponentAlignment(patientGroupIIFilter, Alignment.TOP_LEFT);

        VerticalLayout btnsLayout = new VerticalLayout();
        btnsLayout.setSpacing(true);
        btnsLayout.setMargin(new MarginInfo(true, false, false, false));
        
        PopupInteractiveDSFiltersLayout filtersButn = new PopupInteractiveDSFiltersLayout(filtersLayout);
        btnsLayout.addComponent(filtersButn);
        btnsLayout.setComponentAlignment(filtersButn, Alignment.TOP_LEFT);
        filtersButn.setDescription("Apply more filters");

        VerticalLayout clearFilterBtn = new VerticalLayout();
        clearFilterBtn.setStyleName("resetlabelbtn");
        btnsLayout.addComponent(clearFilterBtn);
        btnsLayout.setComponentAlignment(clearFilterBtn, Alignment.TOP_LEFT);
        clearFilterBtn.setDescription("Reset all applied filters");
        clearFilterBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                exploringFiltersManager.resetFilters();
            }
        });

        heatMap = new HeatMapComponent() {

            @Override
            public void updateSelectionManager(Set<GroupsComparison> selectedDsList) {
                exploringFiltersManager.updatedComparisonSelection(selectedDsList); //To change body of generated methods, choose Tools | Templates.
            }


                
          

        };
        HorizontalClickToDisplay frame = new HorizontalClickToDisplay(GroupComparisonFiltersPanelLayout, "grcompbtn", btnsLayout);
        frame.setWidth("30px");
        this.addComponent(frame);

        this.heatmapLayout = new VerticalLayout();
        this.heatmapLayout.setWidth("100%");
        this.addComponent(heatmapLayout);
        this.setComponentAlignment(heatmapLayout, Alignment.TOP_LEFT);

//        setExpandRatio(heatmapLayout, 0.4f);     
//        heatMap.setSpacing(true);
//        heatMap.setWidth("100%");
        heatmapLayout.setWidth("100%");
        heatmapLayout.addComponent(heatMap);
        heatmapLayout.setComponentAlignment(heatMap, Alignment.MIDDLE_CENTER);
//        heatmapLayout.setMargin(true);

        QuantDSIndexes[][] values = calcHeatMapMatrix(pgSet, pgSet);
        heatMap.updateHeatMap(pgSet, pgSet, values, maxDatasetNumber);
        exploringFiltersManager.registerFilter(DatasetExploringHeatMapFilters.this);
        this.addComponent(compTable);
        this.setComponentAlignment(compTable, Alignment.TOP_LEFT);

    }

    /**
     * this method update the labels value for the groups selection list
     *
     * @param quantDSArr array of the quant datasets
     */
    private void updatePatientGroups(QuantDatasetObject[] quantDSArr) {
        patGr1 = new String[quantDSArr.length];
        patGr2 = new String[quantDSArr.length];
        patientsGroupArr = new PatientsGroup[quantDSArr.length];
        int i = 0;
        for (QuantDatasetObject ds : quantDSArr) {
            PatientsGroup pg = new PatientsGroup();
            String pgI = ds.getPatientsGroup1();
            pg.setPatientsGroupI(pgI);
            String label1 = "";
            if (pgI.equalsIgnoreCase("Not Available") || pgI.equalsIgnoreCase("control")) {
                pgI = "";
            }
            String subpgI = ds.getPatientsSubGroup1();
            pg.setPatientsSubGroupI(subpgI);
            if (!subpgI.equalsIgnoreCase("") && !subpgI.equalsIgnoreCase("Not Available")) {
                pgI = subpgI;
//                subpgI = " / " + subpgI;
            } else {
//                subpgI = "";
            }
            label1 = pgI;
            pg.setPatientsGroupILabel(label1);

            String pgII = ds.getPatientsGroup2();
            pg.setPatientsGroupII(pgII);
            String label2 = "";
            if (pgII.equalsIgnoreCase("Not Available") || pgII.equalsIgnoreCase("control")) {
                pgII = "";
            }
            String subpgII = ds.getPatientsSubGroup2();
            pg.setPatientsSubGroupII(subpgII);
            if (!subpgII.equalsIgnoreCase("") && !subpgII.equalsIgnoreCase("Not Available")) {
                pgII = subpgII;
            } else {
                subpgII = "";
            }
            label2 = pgII ;
            pg.setPatientsGroupIILabel(label2);
            patientsGroupArr[i] = pg;
            patGr1[i] = label1;
            patGr2[i] = label2;
            pg.setQuantDatasetIndex(i);
            pg.setOriginalDatasetIndex(ds.getUniqId());
            i++;
        };

    }

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("filter")) {
            if (selfselected) {
                selfselected = false;
                return;
            }
            
            this.updatePatientGroups(exploringFiltersManager.getFilteredDatasetsList());
            String[] pgArr = merge(patGr1, patGr2);
            Set<String> sel1 = new TreeSet<String>();
            for (String str : pgArr) {
                if (!str.equalsIgnoreCase("")) {
                    sel1.add(str);
                }
            }
            patientGroupIFilter.updateList(sel1);
            Set<String> sel2 = new TreeSet<String>();
            Set<PatientsGroup> p = filterPatGroup2List(sel1);
            int[] indexes = new int[p.size()];
            int i = 0;
            for (PatientsGroup pg : p) {
                indexes[i] = pg.getOriginalDatasetIndex();
                i++;
            }
            for (Object id : patientGroupIIFilter.getList().getItemIds().toArray()) {
                sel2.add(id.toString());

            }
            patientGroupIIFilter.setEnabled(false);
            QuantDSIndexes[][] values = calcHeatMapMatrix(sel1, sel2);
            heatMap.updateHeatMap(sel1, sel2, values, maxDatasetNumber);

        }

    }

    @Override
    public String getFilterId() {
        return "PatientsComparFilter";
    }

    @Override
    public void removeFilterValue(String value) {
    }

    private Set<PatientsGroup> filterPatGroup2List(Set<String> sel1) {
        Set<PatientsGroup> tempPatientsGroup = new HashSet<PatientsGroup>();
        Set<String> labels = new TreeSet<String>();

        for (int i = 0; i < patientsGroupArr.length; i++) {
            PatientsGroup pg = patientsGroupArr[i];
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

    private Set<PatientsGroup> filterPGR2(Set<String> L1, Set<String> L2) {
        Set<PatientsGroup> tempPatientsGroup = new HashSet<PatientsGroup>();

        for (int i = 0; i < patientsGroupArr.length; i++) {
            PatientsGroup pg = patientsGroupArr[i];
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

    private void updateSelectionManager(int[] datasetIndexes) {
        selfselected = true;
        exploringFiltersManager.updatedSelection(new CSFFilterSelection("filter", datasetIndexes, "", new HashSet<String>()));
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
     
      private int maxDatasetNumber;
    private QuantDSIndexes[][] calcHeatMapMatrix(Set<String> rowheaders, Set<String> colheaders) {
        maxDatasetNumber = -1;
        QuantDSIndexes[][] values = new QuantDSIndexes[rowheaders.size()][colheaders.size()];
        for (int x = 0; x < rowheaders.size(); x++) {
            for (int y = 0; y < colheaders.size(); y++) {
                Set<Integer>  value = calcDsNumbers(rowheaders.toArray()[x].toString(), colheaders.toArray()[y].toString());
                int z=0;
                int[] indexes = new int[value.size()];
                for(int i:value){
                indexes[z]=i;
                z++;
                }
                QuantDSIndexes qDataset = new QuantDSIndexes();
                qDataset.setValue(value.size());
                qDataset.setIndexes(indexes);
                values[x][y] = qDataset;
                if (value.size() > maxDatasetNumber) {
                    maxDatasetNumber = value.size();
                }
            }

        }

        return values;
    }
    
    
          int counter = 0;
    private Set<Integer> calcDsNumbers(String PGI, String PGII) {
//      
        Set<Integer> indexes =new HashSet<Integer>();
        for (PatientsGroup pg : patientsGroupArr) {
            if (pg.checkLabel(PGI)) {
                if (pg.getValLabel(PGI).equalsIgnoreCase(PGII)) {
                    indexes.add(pg.getOriginalDatasetIndex());
                    counter++;
                }

            }

        }
        return indexes;

    }

    private void initIndexes(Set<String> labelesI ,Set<String> labelesII ) {
//        for (int x = 0; x < labelesI.size(); x++) { 
//             for (int y = 0; y < labelesII.size(); y++) {
//              QuantDatasetObject[] dss = exploringFiltersManager.getFilteredDatasetsList();
//                  for (QuantDatasetObject qds : dss) {
//                      String 
//                      
//                      
//                      
//                if (qds.getPatientsGroup1()) {
//
//                }
//
//             }
//           
//           
//       
//
//            }
//        }

    }


}
