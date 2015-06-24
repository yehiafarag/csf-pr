/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.quantdatasetsoverview.heatmap;

import probe.com.view.quantdatasetsoverview.popupinteractivefilter.PopupInteractiveDSFiltersLayout;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
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
import probe.com.view.quantdatasetsoverview.quantcomparisontable.QuantProteinsComparisonTable;
import probe.com.view.quantdatasetsoverview.popupinteractivefilter.PopupInteractiveFilterComponent;
import probe.com.view.core.ListSelectDatasetExplorerFilter;
import probe.com.view.core.PatientsGroup;
import probe.com.view.quantdatasetsoverview.quantcomparisontable.UpdatedQuantProteinsComparisonTable;

/**
 * this class represents the top filters layout for exploring datasets tab the
 * class include the the patients group comparison lists, filtering heat-map and
 * pie chart filters
 *
 * @author Yehia Farag
 */
public class DatasetExploringHeatMapFilters extends GridLayout implements CSFFilter {

    private final DatasetExploringSelectionManagerRes exploringFiltersManager;

    private ListSelectDatasetExplorerFilter patientGroupIFilter, patientSubGroupIFilter, patientGroupIIFilter, patientSubGroupIIFilter;
    private String[] patGr1, patGr2;
    private PatientsGroup[] patientsGroupArr;

    private final OptionGroup optionGroup;
    private final VerticalLayout GroupComparisonFiltersPanelLayout;
    private final VerticalLayout heatmapLayout;
    private HeatMapComponent heatMap;
    private boolean selfselected = false;
    private final HorizontalLayout btnsLayout;
    private final UpdatedQuantProteinsComparisonTable compTable;
    private final int tableWidth,heatmapW;
    private final float tableRatio;
    private final float heatmapRatio;


    public DatasetExploringHeatMapFilters(final DatasetExploringSelectionManagerRes exploringFiltersManager, PopupInteractiveFilterComponent filtersLayout,final UpdatedQuantProteinsComparisonTable compTable) {
        this.setHeight("100%");
        this.setWidth("100%");
        this.setSpacing(true);
        this.setColumns(2);
        this.setMargin(true);
        this.setRows(2);
        Page page = Page.getCurrent();
        int pageWidth = page.getBrowserWindowWidth();

        this.exploringFiltersManager = exploringFiltersManager;
        this.compTable=compTable;
        // init group comparisons layout 
        this.GroupComparisonFiltersPanelLayout = new VerticalLayout();
        this.GroupComparisonFiltersPanelLayout.setWidth("450px");
        this.updatePatientGroups(exploringFiltersManager.getFilteredDatasetsList());
        String[] pgArr = merge(patGr1, patGr2);

        patientGroupIFilter = new ListSelectDatasetExplorerFilter(1, "Patients Group I", pgArr);
        initGroupsIFilter();

        patientGroupIIFilter = new ListSelectDatasetExplorerFilter(2, "Patients Group II", pgArr);
        initGroupsIIFilter();
        Set<String> pgSet = new TreeSet<String>(Arrays.asList(pgArr));

        GroupComparisonFiltersPanelLayout.addComponent(patientGroupIIFilter);
        GroupComparisonFiltersPanelLayout.setComponentAlignment(patientGroupIIFilter, Alignment.TOP_LEFT);

        btnsLayout = new HorizontalLayout();
        btnsLayout.setSpacing(true);
//        btnsLayout.setMargin(new MarginInfo(true, false, false, false));

        PopupInteractiveDSFiltersLayout filtersButn = new PopupInteractiveDSFiltersLayout(filtersLayout);
        btnsLayout.addComponent(filtersButn);
        btnsLayout.setComponentAlignment(filtersButn, Alignment.TOP_LEFT);
        filtersButn.setDescription("Apply more filters");

        Button clickableComparisonIcon = new Button("Table Filter");
        clickableComparisonIcon.setHeight("30px");
//        clickableComparisonIcon.setWidth("140px");
        clickableComparisonIcon.setStyleName(Reindeer.BUTTON_LINK);
//        Label l = new Label("<center><p style='font-family: Verdana; font-size: 14px;font-weight: bold;'>Table Filter</p><center>");
//        l.setContentMode(ContentMode.HTML);
//        clickableComparisonIcon.addComponent(l);
//        clickableComparisonIcon.setComponentAlignment(l, Alignment.MIDDLE_CENTER);
        btnsLayout.addComponent(clickableComparisonIcon);
        btnsLayout.setComponentAlignment(clickableComparisonIcon, Alignment.MIDDLE_CENTER);

        VerticalLayout popupLayout = new VerticalLayout();
        popupLayout.setSpacing(true);
        popupLayout.setWidth("500px");
        popupLayout.setHeightUndefined();
        final PopupView container = new PopupView("", popupLayout);

        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidth("100%");
        titleLayout.setHeight(22, Sizeable.Unit.PIXELS);

        Label title = new Label("&nbsp;&nbsp;Patients Groups Comparisons");
        title.setContentMode(ContentMode.HTML);
        title.setStyleName("custLabel");
        title.setHeight("20px");
        titleLayout.addComponent(title);

        VerticalLayout minmIcon = new VerticalLayout();
        minmIcon.setWidth("16px");
        minmIcon.setHeight("16px");
        minmIcon.setStyleName("closelabel");
        minmIcon.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
               container.setPopupVisible(!container.isPopupVisible());
            }
        });
        
         clickableComparisonIcon.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                  container.setPopupVisible(!container.isPopupVisible());
            }
             
           
        });
         
        titleLayout.addComponent(minmIcon);
        titleLayout.setComponentAlignment(minmIcon, Alignment.TOP_RIGHT);
        popupLayout.addComponent(titleLayout);
        GroupComparisonFiltersPanelLayout.setHeightUndefined();
        popupLayout.addComponent(GroupComparisonFiltersPanelLayout);
        popupLayout.setComponentAlignment(GroupComparisonFiltersPanelLayout,Alignment.BOTTOM_CENTER);
        
        
           
        
        btnsLayout.addComponent(container);
        btnsLayout.setComponentAlignment(container, Alignment.MIDDLE_CENTER); 
        
        container.setHideOnMouseOut(false);
        
        
        Button clearFilterBtn = new Button("Reset");
        clearFilterBtn.setHeight("30px");
        clearFilterBtn.setStyleName(Reindeer.BUTTON_LINK);
//        clearFilterBtn.setStyleName("grcompbtn");
//         Label l2 = new Label("<center><p style='font-family:Verdana; font-size: 14px;font-weight: bold;'>Reset</p><center>");
//        l2.setContentMode(ContentMode.HTML);
//       clearFilterBtn.addComponent(l2);
//       clearFilterBtn.setComponentAlignment(l2,Alignment.MIDDLE_CENTER);
        btnsLayout.addComponent(clearFilterBtn);
        btnsLayout.setComponentAlignment(clearFilterBtn, Alignment.TOP_LEFT);
        clearFilterBtn.setDescription("Reset all applied filters");
        btnsLayout.setWidthUndefined();
        btnsLayout.setHeight("100%");
        clearFilterBtn.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                 exploringFiltersManager.resetFilters();
            }
            

        });
        
         this.optionGroup = new OptionGroup();
         btnsLayout.addComponent(optionGroup);
         btnsLayout.setComponentAlignment(optionGroup, Alignment.TOP_CENTER);
         optionGroup.setWidth("150px");
//        optionGroup.setHeight("40px");
        optionGroup.setNullSelectionAllowed(false); // user can not 'unselect'
        optionGroup.setMultiSelect(false);

        optionGroup.addItem("Single selection");
        optionGroup.addItem("Multiple selection");
        optionGroup.setValue("Single selection");
        optionGroup.addStyleName("horizontal");
        optionGroup.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if(optionGroup.getValue().toString().equalsIgnoreCase("Single selection"))
                    heatMap.setSingleSelection(true);
                else
                    heatMap.setSingleSelection(false);
            }
        });
//        HorizontalClickToDisplay butnsFramLayout = new HorizontalClickToDisplay(GroupComparisonFiltersPanelLayout, "grcompbtn", btnsLayout);
//        butnsFramLayout.setWidth("150px");
//        butnsFramLayout.setHeight("150px");
       
        
        
        heatMap = new HeatMapComponent() {

            @Override
            public void updateSelectionManager(Set<GroupsComparison> selectedDsList) {
                
                exploringFiltersManager.setComparisonSelection(selectedDsList); //To change body of generated methods, choose Tools | Templates.
            }


                
          

        };
       
        this.addComponent(btnsLayout,0,1);

         heatmapW = 156 + (50 * pgSet.size());
         tableWidth = (pageWidth - heatmapW-150);
         heatmapRatio = (float) heatmapW / (float) (pageWidth);
         tableRatio =  (float) (tableWidth) / (float) (pageWidth);
        

        this.heatmapLayout = new VerticalLayout();
        this.heatmapLayout.setWidth(heatmapW + "px");
        
        this.heatmapLayout.setHeight("100%");        
        this.heatmapLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(heatmapLayout,0,0);
        this.setComponentAlignment(heatmapLayout, Alignment.TOP_LEFT);

//        setExpandRatio(heatmapLayout, 0.4f);     
//        heatMap.setSpacing(true);
//        heatMap.setWidth("100%");
        heatmapLayout.addComponent(heatMap);
        heatmapLayout.setComponentAlignment(heatMap, Alignment.MIDDLE_CENTER);
//        heatmapLayout.setMargin(true);

        QuantDSIndexes[][] values = calcHeatMapMatrix(pgSet, pgSet);
        heatMap.updateHeatMap(pgSet, pgSet, values, maxDatasetNumber);
        exploringFiltersManager.registerFilter(DatasetExploringHeatMapFilters.this);
        
        this.addComponent(compTable,1,0);
        this.setComponentAlignment(compTable, Alignment.TOP_CENTER);
        
        compTable.setLayoutWidth(tableWidth);
        this.setColumnExpandRatio(0, heatmapW);
        this.setColumnExpandRatio(1, tableWidth);
         this.addComponent(compTable.getBottomLayout(),1,1);
         this.setComponentAlignment(compTable.getBottomLayout(), Alignment.TOP_CENTER);
         LayoutEvents.LayoutClickListener hideShowCompTableListener = new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                
                compTable.getHideCompariosonTableBtn().setVisible(heatmapLayout.isVisible());
                heatmapLayout.setVisible(!heatmapLayout.isVisible());
                btnsLayout.setVisible(!btnsLayout.isVisible());
                if (!btnsLayout.isVisible()) {
                    compTable.setLayoutWidth((tableWidth + heatmapW));
                    setColumnExpandRatio(0, 0f);
                    setColumnExpandRatio(1, 1f);
                } else {
                    compTable.setLayoutWidth(tableWidth );
                    setColumnExpandRatio(0, heatmapW);
                    setColumnExpandRatio(1, tableWidth);
                    
                }
//                compTable.resizeCharts();
            }
        };
         
          compTable.getHideCompariosonTableBtn().addLayoutClickListener(hideShowCompTableListener);
          heatMap.getHideCompBtn().addLayoutClickListener(hideShowCompTableListener);
         

    }

    private void initGroupsIFilter(){
     patientGroupIFilter.getList().setHeight("150px");
        patientGroupIFilter.getList().setWidth("380px");
        patientGroupIFilter.setMargin(new MarginInfo(false, false, true, false));
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
                    enable = false;
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
                heatMap.updateHeatMap(sel1, sel2, values, maxDatasetNumber);
                sel1.addAll(sel2);
                updateSelectionManager(indexes);
            }
        });
    
    
    }
    
     private void initGroupsIIFilter(){
     
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
        });}

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
        if (selfselected) {
            selfselected = false;
            return;
        }

        if (type.equalsIgnoreCase("filter")) {

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

        } else if (type.equalsIgnoreCase("ComparisonSelection")) {
            heatMap.updateDsCellSelection(exploringFiltersManager.getSelectedComparisonList());
            if (exploringFiltersManager.getSelectedComparisonList().isEmpty()) {
                viewComparisonHeatmap(true);
            }

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
        exploringFiltersManager.setFilterSelection(new CSFFilterSelection("filter", datasetIndexes, "", new HashSet<String>()));
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

    private void viewComparisonHeatmap(boolean view) {
        heatmapLayout.setVisible(view);
        btnsLayout.setVisible(view);
        if (view) {
            compTable.setLayoutWidth(tableWidth);
            setColumnExpandRatio(0, heatmapRatio);
            setColumnExpandRatio(1, tableRatio);

        } else {
            compTable.setLayoutWidth((tableWidth + heatmapW));
            setColumnExpandRatio(0, 0f);
            setColumnExpandRatio(1, 1f);

        }

    }


}
