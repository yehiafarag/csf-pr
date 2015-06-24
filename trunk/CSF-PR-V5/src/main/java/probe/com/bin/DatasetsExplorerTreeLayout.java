//package probe.com.bin;
//
//import com.vaadin.event.LayoutEvents;
//import com.vaadin.shared.ui.MarginInfo;
//import com.vaadin.ui.Alignment;
//import com.vaadin.ui.Button;
//import com.vaadin.ui.Component;
//import com.vaadin.ui.GridLayout;
//import com.vaadin.ui.HorizontalLayout;
//import com.vaadin.ui.Label;
//import com.vaadin.ui.Panel;
//import com.vaadin.ui.VerticalLayout;
//import com.vaadin.ui.themes.Reindeer;
//import java.util.Arrays;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.HashSet;
//import java.util.Map;
//import java.util.Set;
//import java.util.TreeMap;
//import java.util.TreeSet;
//import java.util.concurrent.Executors;
//import java.util.concurrent.ScheduledExecutorService;
//import probe.com.selectionmanager.CSFFilter;
//import probe.com.selectionmanager.DatasetExploringSelectionManagerRes;
//import probe.com.model.beans.QuantDatasetObject;
//import probe.com.model.beans.FilterRoot;
//import probe.com.model.util.SparkLineGen;
//import probe.com.view.core.FilterRow;
//import probe.com.view.core.HideOnClickLayout;
//import probe.com.view.core.InformationField;
//import probe.com.view.core.ListGridCell;
//import probe.com.view.core.SingleStudyFilterLayout;

/**
 *
 * @author Yehia Farag
 */
//public class DatasetsExplorerTreeLayout extends VerticalLayout implements CSFFilter {
//
//    private final DatasetExploringSelectionManagerRes exploringFiltersManager;
//    private final VerticalLayout mainbodyLayout = new VerticalLayout();
//    private final HorizontalLayout treeContainerLayout = new HorizontalLayout();
//    private final String[] headers = new String[27];
//    private final HideOnClickLayout layout;
//    private final int totalDatasetNumber;
//    private Panel studiesTreePanel;
//    private GridLayout studiesLayout;
//    private final Map<String, Integer> filtersMap = new HashMap<String, Integer>();
//    private final Map<String, FilterRow> rowMap = new TreeMap<String, FilterRow>(Collections.reverseOrder());
//    private String[] labelsValues;
//    private Panel infoLayoutPanel;
//    private String lastSelectedKey = null;
//    private FilterRoot currentSelectedFilterRoot;
//    private int maxStudyNumber;
//    private final TreeMap<String, FilterRoot> nodes = new TreeMap<String, FilterRoot>(Collections.reverseOrder());
//    private Label commonInfoLabel, uniqueIinfoLabel;
//    private VerticalLayout containerLayout;
//    private GridLayout commonInfoLayout;
//    private VerticalLayout datasetInfoLayout;
////    private final Map<String, Set<String>> fullFilterList;
//    final ScheduledExecutorService timerService = Executors.newSingleThreadScheduledExecutor();
//
//    public DatasetsExplorerTreeLayout(DatasetExploringSelectionManagerRes exploringFiltersManager) {
//
//        this.setVisible(true);
//        headers[1] = "";
//        headers[2] = "#ID: ";
//        headers[3] = "#Quant: ";
//        headers[6] = "#Files ";
//        headers[19] = "#PGr1: ";
//        headers[23] = "#PGr2: ";
//        filtersMap.put("diseaeGroups", 0);
//        filtersMap.put("rawDataUrl", 1);
//        filtersMap.put("year", 2);
//        filtersMap.put("typeOfStudy", 3);
//        filtersMap.put("sampleType", 4);
//        filtersMap.put("sampleMatching", 5);
//        filtersMap.put("technology", 6);
//        filtersMap.put("analyticalApproach", 7);
//        filtersMap.put("enzyme", 8);
//        filtersMap.put("shotgunTargeted", 9);
//
//        layout = new HideOnClickLayout("Studies Overview", mainbodyLayout, null);
//
//        this.setStyleName(Reindeer.LAYOUT_WHITE);
//        this.exploringFiltersManager = exploringFiltersManager;
//        this.addComponent(layout);
//        mainbodyLayout.setWidth("100%");
//
////        fullFilterList = new HashMap<String, Set<String>>();
////        for (String key : exploringFiltersManager.getFullFilterList().keySet()) {
////            Set<String> filterValues = new HashSet<String>();
////            for (Object val : exploringFiltersManager.getFullFilterList().get(key)) {
////                filterValues.add(val.toString());
////
////            }
////            fullFilterList.put(key, filterValues);
////
////        }
//
//        mainbodyLayout.addComponent(treeContainerLayout);
//        treeContainerLayout.setWidth("100%");
//        initDatasetsTree();
//        treeContainerLayout.addComponent(studiesTreePanel);
//        treeContainerLayout.setComponentAlignment(studiesTreePanel, Alignment.MIDDLE_LEFT);
//        treeContainerLayout.addComponent(infoLayoutPanel);
//        treeContainerLayout.setSpacing(true);
//        treeContainerLayout.setExpandRatio(studiesTreePanel, 0.6f);
//        treeContainerLayout.setExpandRatio(infoLayoutPanel, 0.45f);
//
//        treeContainerLayout.setComponentAlignment(infoLayoutPanel, Alignment.MIDDLE_RIGHT);
//        totalDatasetNumber = exploringFiltersManager.getFilteredDatasetsList().length;
//        updateDatasetsNodes();
//        updateStudyTree();
//        exploringFiltersManager.registerFilter(DatasetsExplorerTreeLayout.this);
//        layout.setVisability(true);
//
//    }
//
//    private void initDatasetsTree() {
//        studiesTreePanel = new Panel();
//        studiesTreePanel.setHeight("550px");
//        studiesTreePanel.setStyleName("grayborder");
//        studiesLayout = new GridLayout();
//        studiesTreePanel.setContent(studiesLayout);
//        studiesLayout.setMargin(new MarginInfo(true, false, false, true));
//        studiesLayout.setSpacing(true);
//        studiesLayout.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
//            @Override
//            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
//                Component c = event.getClickedComponent();
//                if (c != null) {
//                    ListGridCell cell = ((ListGridCell) c.getParent());
//                    if (cell != null) {
//                        datasetSelected(cell.getCellId(), -1);
//                    }
//                }
//
//            }
//        });
//        infoLayoutPanel = initStudyInformationLayout();
//
//    }
//
//    private void datasetSelected(String cellId, int dsIndex) {
//
//        if (lastSelectedKey != null) {
//            FilterRow row = rowMap.get(lastSelectedKey);
//            if (row != null) {
//                row.layoutUnSelected();
//            }
//        }
//        lastSelectedKey = cellId;
//        rowMap.get(lastSelectedKey).layoutSelected(dsIndex);
//    }
//
//    private void updateStudyTree() {
//        studiesLayout.removeAllComponents();
//        initMasterColumn();
//        int colNumber = exploringFiltersManager.getAppliedFilterList().size() + 3;
//        if (colNumber == 3) {
//            colNumber = 13;
//        }
//        studiesLayout.setColumns(colNumber);
//        SparkLineGen sparkLineGen = new SparkLineGen(0, maxStudyNumber);
//        rowMap.clear();
//        for (String key : nodes.keySet()) {
//            FilterRoot rootSet = nodes.get(key);
//            FilterRow row = new FilterRow(rootSet, colNumber, masterColumn, DatasetsExplorerTreeLayout.this, sparkLineGen);
//            rowMap.put(rootSet.getKey() + "", row);
//            int f = 0;
//            for (; f < colNumber; f++) {
//                ListGridCell l = row.getColumns()[f];
//                studiesLayout.addComponent(l);
//                studiesLayout.setColumnExpandRatio(f, l.getCellValue());
//
//            }
//
//        }
//
//    }
//
//    @Override
//    public void selectionChanged(String type) {
//        if (type.equalsIgnoreCase("filter")) {
//
//            updateDatasetsNodes();
//            this.updateStudyTree();
//            containerLayout.setVisible(false);
//            this.currentSelectedFilterRoot = null;
//            this.setVisible(true);
//        } else {
//
//            int datasetId = exploringFiltersManager.getSelectedDataset();
//            selectDataset(datasetId);
//        }
//
//    }
//
//    private void updateDatasetsNodes() {
//        String[][] combFilters = null;
//        if (!exploringFiltersManager.getAppliedFilterList().isEmpty()) {
//            combFilters = merge(exploringFiltersManager.getAppliedFilterList());
//            fillStudiesNodes(combFilters, exploringFiltersManager.getFilteredDatasetsList(), exploringFiltersManager.getAppliedFilterList());
//
//        } else {
////            combFilters = merge(fullFilterList);
////            fillStudiesNodes(combFilters, exploringFiltersManager.getFilteredDatasetsList(), fullFilterList);
//
//        }
//
//        layout.updateFilterLabel("Datasets Overview (" + exploringFiltersManager.getFilteredDatasetsList().length + "/" + totalDatasetNumber + ") ");
//
//    }
//
//    private void fillStudiesNodes(String[][] combFilters, QuantDatasetObject[] updatedStudiesList, Map<String, Set<String>> appliedFilterList) {
//
//        nodes.clear();
//        for (QuantDatasetObject pb : updatedStudiesList) {
//            String[] studyValues = new String[appliedFilterList.size()];
//            int filterIndex = 0;
//            for (String filterId : appliedFilterList.keySet()) {
//                studyValues[filterIndex] = pb.getProperty(filterId).toString();
//                filterIndex++;
//            }
//
//            for (int z = 0; z < combFilters.length; z++) {
//                String[] strArr = combFilters[z];
//                if (compare(strArr, studyValues)) {
//
//                    if (!nodes.containsKey((z + 1) + "")) {
//                        FilterRoot root = new FilterRoot();
//                        root.setKey((z + 1));//                 
//                        nodes.put((z + 1) + "", root);
//                        for (String filterId : appliedFilterList.keySet()) {
//                            root.applyFilter(exploringFiltersManager.getFilterTitle(filterId), pb.getProperty(filterId).toString(), filtersMap.get(filterId));
//                        }
//                    }
//                    FilterRoot root = nodes.get((z + 1) + "");
//                    root.addStudy(pb);
//
//                }
//            }
//
//        }
//
//        TreeMap<String, FilterRoot> arrNodes = new TreeMap<String, FilterRoot>(Collections.reverseOrder());
//        maxStudyNumber = 0;
//        for (FilterRoot sr : nodes.values()) {
//            FilterRoot updatedFilterRoot = filterFilterRoot(sr);
//
//            if (sr.getDatasetsList().size() > maxStudyNumber) {
//                maxStudyNumber = sr.getDatasetsList().size();
//            }
//            int recent = -100;
//            Set<QuantDatasetObject> arrset = new TreeSet<QuantDatasetObject>();
//            arrset.addAll(sr.getDatasetsList());
//            sr.setDatasetsList(arrset);
//            for (QuantDatasetObject so : sr.getDatasetsList()) {
//                if (recent < so.getYear()) {
//                    recent = so.getYear();
//                }
//
//            }
//            arrNodes.put(recent + "-" + updatedFilterRoot.getKey(), updatedFilterRoot);
//
//        }
//        nodes.clear();
//        nodes.putAll(arrNodes);
//    }
//    
//    private FilterRoot filterFilterRoot(FilterRoot root) {
//        boolean[] attrCheck = new boolean[27];
//        Object[][] attrValues = new Object[root.getDatasetsList().size()][27];
//        
//        int x = 0;
//        for (QuantDatasetObject pb : root.getDatasetsList()) {
//            
//            attrValues[x] = pb.getValues();
//            x++;
//        }
//        
//        for (int y = 0; y < attrCheck.length; y++) {
//            for (int i = 0; i < attrValues.length; i++) {
//                if (i == attrValues.length - 1) {
//                    break;
//                }
//                if (!attrValues[i][y].toString().equalsIgnoreCase(attrValues[i + 1][y].toString())) {
//                    attrCheck[y] = true;
//                    break;
//                    
//                }
//            }
//        }
//        root.setAttrCheck(attrCheck);
//        QuantDatasetObject repDataset = null;
//        Set<QuantDatasetObject> studies = new HashSet<QuantDatasetObject>();
//        for (QuantDatasetObject pb : root.getDatasetsList()) {
//            studies.add(filterStudyChild(pb, attrCheck));
//            if (repDataset == null) {
//                repDataset = pb;
//            }
//        }
//        root.setDatasetsList(studies);
//        root.setRepDataset(repDataset);
//        return root;
//
//    }
//
//    private QuantDatasetObject filterStudyChild(QuantDatasetObject child, boolean[] attrCheck) {
//
//        String unique = "";
//        for (int i = 0; i < attrCheck.length; i++) {
//
//            if (attrCheck[i]) {
//                Object prop = child.getProperty(i);
//                if (!prop.toString().equalsIgnoreCase("Not Available")) {
//                    if (prop instanceof Integer) {
//                        prop = headers[i] + prop;
//                    }
//                    if (prop.toString().contains("-1")) {
//                        prop = prop.toString().replace("-1", "N/A");
//                    }
//                    unique += prop + " , ";
//                }
//
//            }
//        }
//        if (unique.length() >= 2) {
//            unique = unique.substring(0, (unique.length() - 2));
//        }
//        child.setUniqueValues(unique);
//
//        return child;
//    }
//
//    private boolean compare(String[] A, String[] B) {
//        return Arrays.equals(A, B);
//    }
//
//    private String[][] merge(Map<String, Set<String>> appliedFilterList) {
//        String[] merged = null;
//        for (Set<String> filterset : appliedFilterList.values()) {
//            String[] filter = filterset.toArray(new String[filterset.size()]);
//            merged = unionArrays(merged, filter);
//
//        }
//        if (merged == null) {
//            return null;
//        }
//
//        String[][] filtersComb = new String[merged.length][];
//        int x = 0;
//
//        labelsValues = new String[merged.length];
//        for (String str : merged) {
//            String[] StrArr = str.split(",");
//            filtersComb[x] = StrArr;
//            labelsValues[x] = str;
//            x++;
//
//        }
//        return filtersComb;
//
//    }
//
//    private void selectDataset(int dsIndex) {
//        for (FilterRow row : rowMap.values()) {
//            if (row.includeDataset(dsIndex)) {
//                datasetSelected(row.getKey(), dsIndex);
//            }
//
//        }
//
//    }
//
//    private Panel initStudyInformationLayout() {
//
//        Panel containerPanel = new Panel();
//        containerPanel.setHeight("550px");
//        containerPanel.setStyleName("grayborder");
//        containerLayout = new VerticalLayout();
//
//        containerLayout.setSpacing(true);
//        containerLayout.setMargin(false);
//
//        containerPanel.setContent(containerLayout);
//        //title author
//
//        commonInfoLabel = new Label("Common Datasets Information");
//        commonInfoLabel.setStyleName("captionunderline");
//        commonInfoLabel.setWidth(200 + "px");
//        containerLayout.addComponent(commonInfoLabel);
//        containerLayout.setComponentAlignment(commonInfoLabel, Alignment.MIDDLE_LEFT);
//        containerLayout.setMargin(true);
//
//        commonInfoLayout = new GridLayout();
//        commonInfoLayout.setWidth("100%");
//        commonInfoLayout.setColumns(3);
//        containerLayout.addComponent(commonInfoLayout);
//        containerLayout.setComponentAlignment(commonInfoLayout, Alignment.MIDDLE_LEFT);
//
//        uniqueIinfoLabel = new Label("Unique Datasets Information");
//        uniqueIinfoLabel.setStyleName("captionunderline");
//        uniqueIinfoLabel.setWidth(200 + "px");
//        containerLayout.addComponent(uniqueIinfoLabel);
//        //
//        datasetInfoLayout = new VerticalLayout();
//        datasetInfoLayout.setWidth("100%");
//        datasetInfoLayout.setSpacing(true);
//        containerLayout.addComponent(datasetInfoLayout);
//        containerLayout.setComponentAlignment(datasetInfoLayout, Alignment.MIDDLE_CENTER);
//        //
//        containerLayout.setVisible(false);
//        return containerPanel;
//
//    }
//
//    public void updateStudyInformationLayout(FilterRoot datasetRoot, boolean selected, int datasetIndex) {
//
//        if (selected) {
//            currentSelectedFilterRoot = datasetRoot;
//        }
//        if (datasetRoot == null && currentSelectedFilterRoot == null) {
//            containerLayout.setVisible(false);
//            return;
//
//        }
//        if (datasetRoot == null && currentSelectedFilterRoot != null) {
//            datasetRoot = currentSelectedFilterRoot;
//        }
//        commonInfoLayout.removeAllComponents();
//        datasetInfoLayout.removeAllComponents();
//        QuantDatasetObject datasetRep = datasetRoot.getRepDataset();
//        for (int i = 0; i < datasetRoot.getAttrCheck().length; i++) {
//            boolean check = datasetRoot.getAttrCheck()[i];
//            if (!check) {
//                if (true){//!datasetRep.getProperty(i).toString().trim().equalsIgnoreCase("Not Available") && !datasetRep.getProperty(i).toString().trim().equalsIgnoreCase("0")&& !datasetRep.getProperty(i).toString().trim().equalsIgnoreCase("-1")) {
//                    String commvalue = datasetRep.getProperty(i).toString();
//                    String url = null;
//                    InformationField info = new InformationField(datasetRep.getFilterTitle(i));
//                    if (i == 5) {
//                        if (!commvalue.equalsIgnoreCase("Raw Data Not Available")) {
//                            url = commvalue;
//                        }
//                    } else if (i == 17) {
//                        url = "http://www.ncbi.nlm.nih.gov/pubmed/" + commvalue;
//                    }
//                    info.setValue(commvalue, url);
//                    commonInfoLayout.addComponent(info);
//                }
//
//            }
//
//        }
//
//        if (datasetRoot.getDatasetsList().size() <= 1) {
//            commonInfoLabel.setVisible(false);
//            uniqueIinfoLabel.setVisible(false);
//            Button btn = new Button("Load Dataset");
//            btn.setStyleName(Reindeer.BUTTON_SMALL);
//            int rowNum = commonInfoLayout.getRows();
//            VerticalLayout btnLayout = new VerticalLayout();
//            btnLayout.setMargin(true);
//            btnLayout.setHeight("30px");
//            btnLayout.addComponent(btn);
//            if (commonInfoLayout.getComponentCount() % 3 != 0) {
//                commonInfoLayout.addComponent(btnLayout, 2, rowNum - 1);
//            } else {
//                commonInfoLayout.setRows(rowNum + 1);
//                commonInfoLayout.addComponent(btnLayout, 2, rowNum);
//            }
//            commonInfoLayout.setComponentAlignment(btnLayout, Alignment.BOTTOM_LEFT);
//
//        } else {
//            commonInfoLabel.setVisible(true);
//            uniqueIinfoLabel.setVisible(true);
//            int x = 1;
//            for (QuantDatasetObject study : datasetRoot.getDatasetsList()) {
//                boolean view = false;
//                if (study.getUniqId() == datasetIndex) {
//                    view = true;
//                }
//                datasetInfoLayout.addComponent(new SingleStudyFilterLayout(study, x, datasetRoot.getAttrCheck(), view));
//                x++;
//
//            }
//        }
//
//        containerLayout.setVisible(true);
//    }
//
//    private String[] unionArrays(String[] A, String[] B) {
//        if (A == null) {
//            return B;
//        }
//        String[] merged = new String[(A.length * B.length)];
//        int x = 0;
//        for (String A1 : A) {
//            for (String B1 : B) {
//                merged[x] = A1 + "," + B1;
//                x++;
//            }
//        }
//        return merged;
//    }
//    private boolean[] masterColumn;
//
//    private void initMasterColumn() {
//        masterColumn = new boolean[10];
//        if (!exploringFiltersManager.getAppliedFilterList().keySet().isEmpty()) {
//            for (String i : exploringFiltersManager.getAppliedFilterList().keySet()) {
//                masterColumn[filtersMap.get(i)] = true;
//            }
//        } else {
////            for (String i : fullFilterList.keySet()) {
////                masterColumn[filtersMap.get(i)] = true;
////            }
//        }
//
//    }
//
//    @Override
//    public void removeFilterValue(String value) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
//    }
//
//    @Override
//    public String getFilterId() {
//        return "Datasets tree Layout";
//    }
//
//}
