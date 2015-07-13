/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.handlers.MainHandler;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import probe.com.model.beans.GroupsComparison;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.jfreeutil.SquaredDot;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonChartContainer;

/**
 *
 * @author y-mok_000
 */
public class QuantProteinsComparisonsContainer extends VerticalLayout implements CSFFilter, Property.ValueChangeListener {

    private final DatasetExploringCentralSelectionManager selectionManager;
    private final Table groupsComparisonTable;
    private final MainHandler handler;
    private final HorizontalLayout topLayout, chartsLayoutContainer, bottomLayout;
    private final GridLayout searchingFieldLayout;
    private final TextField searchField;
    private final VerticalLayout searchingBtn;
    private final Button resetSearchBtn;
    private final VerticalLayout startLayout;
    private final VerticalLayout hideCompBtn;
    private final Label protCounterLabel;
    private int width = 0;
    private final OptionGroup hideUniqueProteinsOption;
    private String sortComparisonTableColumn;

    public void setLayoutWidth(int width) {
        this.width = width;
        this.setWidth(width + "px");
        this.bottomLayout.setWidth(width + "px");
        float ratio = 360f / (float) width;
        int columnWidth = 400;
        boolean useRatio = false;
        if (compArr.length > 1) {
            if ((compArr.length * 400) > (width - 360)) {
                useRatio = true;
                int persWidth = (int) (100.0 - (16.0 * 100.0 / (double) width));
                chartsLayoutContainer.setWidth(persWidth + "%");
                int contWid = (persWidth * (width - 360) / 100);
                columnWidth = contWid / compArr.length;

            } else {
                useRatio = false;
                chartsLayoutContainer.setWidth((compArr.length * 400) + "px");
            }
        } else {//ok
            chartsLayoutContainer.setWidth((compArr.length * 400) + "px");
        }

        topLayout.setExpandRatio(searchingFieldLayout, ratio);
        topLayout.setExpandRatio(chartsLayoutContainer, (1f - ratio));
        resizeCharts(columnWidth);
        resizeTable(useRatio, ratio);
        updateTableLabels(columnWidth);

    }
    
    private void updateTableLabels(int columnWidth) {
        if (comparisonProteinSet != null) {
            for (ComparisonProtein[] cpArr : comparisonProteinSet.values()) {
                for (ComparisonProtein cp : cpArr) {
                    if (cp != null) {
                        cp.updateWidth(columnWidth);
                    }

                }

            }

        }


    }

    public QuantProteinsComparisonsContainer(DatasetExploringCentralSelectionManager selectionManager, final MainHandler handler) {
        this.selectionManager = selectionManager;
        selectionManager.registerFilter(QuantProteinsComparisonsContainer.this);
        this.handler = handler;
        this.setWidth("100%");
        this.setHeight("100%");
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setMargin(false);
        this.setSpacing(true);

        startLayout = new VerticalLayout();
        this.addComponent(startLayout);
        startLayout.setWidth("100%");
        startLayout.setHeightUndefined();

        VerticalLayout spacer = new VerticalLayout();
        spacer.setHeight("250px");
        spacer.setWidth("100%");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);
        startLayout.addComponent(spacer);

        Label startLabel = new Label("<center><h2 style='color:gray;'><b>Select comparison from the table</b></h2></center>");
        startLabel.setContentMode(ContentMode.HTML);

        startLayout.addComponent(startLabel);
        startLayout.setComponentAlignment(startLabel, Alignment.MIDDLE_CENTER);

        Image handleft = new Image();
        handleft.setSource(new ThemeResource("img/handleft.png"));
        startLayout.addComponent(handleft);
        startLayout.setComponentAlignment(handleft, Alignment.MIDDLE_CENTER);

        topLayout = new HorizontalLayout();
        topLayout.setVisible(false);
        topLayout.setHeight("250px");
        topLayout.setWidth("100%");
        topLayout.setSpacing(false);
        searchingFieldLayout = new GridLayout();
//        searchingFieldLayout.setWidth("100%");
        searchingFieldLayout.setSpacing(true);
        searchingFieldLayout.setHeight("250px");
        searchingFieldLayout.setColumns(2);
        searchingFieldLayout.setRows(2);
        searchingFieldLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        searchingFieldLayout.setColumnExpandRatio(0, 0.4f);
        searchingFieldLayout.setColumnExpandRatio(1, 0.6f);

        chartsLayoutContainer = new HorizontalLayout();
        chartsLayoutContainer.setHeight("250px");
        int persWidth = (int) (100.0 - (16.0 * 100.0 / (double) width));
        chartsLayoutContainer.setWidth(persWidth + "%");
        chartsLayoutContainer.setStyleName(Reindeer.LAYOUT_WHITE);
//        chartsLayoutContainer.setMargin(new MarginInfo(false, false, false, true));

        topLayout.addComponent(searchingFieldLayout);
        topLayout.addComponent(chartsLayoutContainer);
        // add searching field to spacer
        //allow search in 
        VerticalLayout topSpacer = new VerticalLayout();
        topSpacer.setHeight("180px");
        topSpacer.setWidth("50%");
        searchingFieldLayout.addComponent(topSpacer, 1, 0);
        searchingFieldLayout.setComponentAlignment(topSpacer, Alignment.MIDDLE_CENTER);
        topSpacer.setStyleName(Reindeer.LAYOUT_WHITE);
        topSpacer.setMargin(new MarginInfo(true, false, false, false));

        //hide show comp table 
        hideCompBtn = new VerticalLayout();
        hideCompBtn.setMargin(new MarginInfo(true, false, false, false));
        hideCompBtn.setWidth("150px");
        hideCompBtn.setHeight("150px");
        hideCompBtn.setVisible(false);
        hideCompBtn.setDescription("Show Comparison Table");
        hideCompBtn.setStyleName("matrixbtn");
        Label l = new Label("Show Comparsions");
        hideCompBtn.addComponent(l);
        hideCompBtn.setComponentAlignment(l, Alignment.BOTTOM_CENTER);

        searchingFieldLayout.addComponent(hideCompBtn, 0, 0);
        searchingFieldLayout.setComponentAlignment(hideCompBtn, Alignment.MIDDLE_CENTER);

        HorizontalLayout searchFieldContainerLayout = new HorizontalLayout();
        searchFieldContainerLayout.setWidthUndefined();
        searchFieldContainerLayout.setSpacing(true);
        searchField = new TextField();
        searchField.setDescription("Search Proteins By Name or Accession");
//        searchField.setWidthUndefined();
        searchField.setImmediate(true);
        searchField.setWidth("100%");
        searchField.setHeight("24px");
        searchField.setInputPrompt("Search...");
        searchFieldContainerLayout.addComponent(searchField);

        searchingBtn = new VerticalLayout();
        searchingBtn.setWidth("30px");
        searchingBtn.setHeight("24px");
        searchingBtn.setStyleName("tablesearchingbtn");
        searchFieldContainerLayout.addComponent(searchingBtn);
        searchFieldContainerLayout.setComponentAlignment(searchingBtn, Alignment.MIDDLE_CENTER);
        searchingFieldLayout.addComponent(searchFieldContainerLayout, 0, 1);
        searchingFieldLayout.setComponentAlignment(searchFieldContainerLayout, Alignment.BOTTOM_LEFT);
        protCounterLabel = new Label("");
        protCounterLabel.setWidth("100%");
        protCounterLabel.setHeight("24px");
        protCounterLabel.setContentMode(ContentMode.HTML);
        searchingFieldLayout.addComponent(protCounterLabel, 1, 1);
        searchingFieldLayout.setComponentAlignment(protCounterLabel, Alignment.BOTTOM_LEFT);

        resetSearchBtn = new Button("Reset Table");
        resetSearchBtn.setStyleName(Reindeer.BUTTON_LINK);
        resetSearchBtn.setWidth("76px");
        resetSearchBtn.setHeight("24px");

        this.addComponent(topLayout);
        groupsComparisonTable = new Table();
        groupsComparisonTable.setVisible(false);
        this.addComponent(groupsComparisonTable);
        this.setComponentAlignment(groupsComparisonTable, Alignment.MIDDLE_CENTER);
        this.groupsComparisonTable.setSelectable(true);
        this.groupsComparisonTable.setColumnReorderingAllowed(true);
        this.groupsComparisonTable.setColumnCollapsingAllowed(true);
        this.groupsComparisonTable.setImmediate(true); // react at once when something is selected
        groupsComparisonTable.setWidth("100%");
        groupsComparisonTable.setHeight("400px");
        groupsComparisonTable.addValueChangeListener(QuantProteinsComparisonsContainer.this);
        groupsComparisonTable.setMultiSelect(true);
        groupsComparisonTable.setMultiSelectMode(MultiSelectMode.DEFAULT);

        //add searching listeners 
        searchingBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                hideUniqueProteinsOption.unselect("Hide unique proteins");
                Set<String> subAccList = searchProteins(searchField.getValue());
                if (subAccList.isEmpty()) {
                    Notification.show("Not available");
                    return;
                } else {
                    filterTable(subAccList, compArr, sortComparisonTableColumn);
                    resetAllCharts();
                }
                updateProtCountLabel(subAccList.size());

            }
        });

        Button b = new Button();
        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                hideUniqueProteinsOption.unselect("Hide unique proteins");
                Set<String> subAccList = searchProteins(searchField.getValue());
                if (subAccList.isEmpty()) {
                    Notification.show("Not available");
                    return;
                } else {
//                    updateTableData(subAccList.);
                    filterTable(subAccList, compArr, sortComparisonTableColumn);
                    resetAllCharts();
                }
                updateProtCountLabel(subAccList.size());
            }
        });
        searchField.addShortcutListener(new Button.ClickShortcut(b, ShortcutListener.KeyCode.ENTER));

        resetSearchBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                searchField.clear();
                updateTableData(compArr, null);
                updateProtCountLabel(accessionMap.size());
            }
        });
        bottomLayout = new HorizontalLayout();
        bottomLayout.setHeight("100%");
        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        HorizontalLayout leftBottomLayout = new HorizontalLayout();
        leftBottomLayout.setWidthUndefined();
        bottomLayout.addComponent(leftBottomLayout);
        bottomLayout.setComponentAlignment(leftBottomLayout, Alignment.TOP_LEFT);

        hideUniqueProteinsOption = new OptionGroup();
        leftBottomLayout.addComponent(hideUniqueProteinsOption);
        leftBottomLayout.setComponentAlignment(hideUniqueProteinsOption, Alignment.TOP_LEFT);
        hideUniqueProteinsOption.setWidth("150px");
        hideUniqueProteinsOption.setNullSelectionAllowed(true); // user can not 'unselect'
        hideUniqueProteinsOption.setMultiSelect(true);

        hideUniqueProteinsOption.addItem("Hide unique proteins");
        hideUniqueProteinsOption.addStyleName("horizontal");
        hideUniqueProteinsOption.addValueChangeListener(new Property.ValueChangeListener() {
            private final Set<String> protAccssions = new HashSet<String>();
            private final HashMap<Object, Item> tableItems = new HashMap<Object, Item>();

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (hideUniqueProteinsOption.getValue().toString().equalsIgnoreCase("[Hide unique proteins]")) {
                    protAccssions.clear();
                    tableItems.clear();
                    HashSet<Object> itemIds = new HashSet<Object>(groupsComparisonTable.getItemIds());
                    for (Object id : itemIds) {

                        Item item = groupsComparisonTable.getItem(id);
                        tableItems.put(id, item);
                        protAccssions.add(item.getItemProperty("Accession").toString());
                        for (GroupsComparison gc : compArr) {
                            if (item.getItemProperty(gc.getComparisonHeader()).getValue() == null) {
                                groupsComparisonTable.removeItem(id);
                                break;
                            }
                        }
                    }

                } else {

                    filterTable(protAccssions, compArr, sortComparisonTableColumn);
                }
                updateProtCountLabel(groupsComparisonTable.getItemIds().size());
            }
        });

        Button selectAllBtn = new Button("Select all");
        selectAllBtn.setHeight("30px");
        selectAllBtn.setWidth("60px");

        selectAllBtn.setStyleName(Reindeer.BUTTON_LINK);

        leftBottomLayout.addComponent(selectAllBtn);
        leftBottomLayout.setComponentAlignment(selectAllBtn, Alignment.TOP_LEFT);
        selectAllBtn.setDescription("Select all data");

        selectAllBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                selectAll();
            }
        });
        Button unSelectAllBtn = new Button("Unselect all");
        unSelectAllBtn.setHeight("30px");
        unSelectAllBtn.setWidth("74px");

        unSelectAllBtn.setStyleName(Reindeer.BUTTON_LINK);

        leftBottomLayout.addComponent(unSelectAllBtn);
        leftBottomLayout.setComponentAlignment(unSelectAllBtn, Alignment.TOP_LEFT);
        unSelectAllBtn.setDescription("Unselect all data");

        unSelectAllBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                unSelectAll();
            }
        });

        leftBottomLayout.addComponent(resetSearchBtn);
        leftBottomLayout.setComponentAlignment(resetSearchBtn, Alignment.TOP_LEFT);

        Button exportTableBtn = new Button("Export Table");
        exportTableBtn.setHeight("30px");
        exportTableBtn.setWidth("83px");
        exportTableBtn.setStyleName(Reindeer.BUTTON_LINK);
        bottomLayout.addComponent(exportTableBtn);
        bottomLayout.setComponentAlignment(exportTableBtn, Alignment.TOP_RIGHT);
        exportTableBtn.setDescription("Export table data");
        bottomLayout.setHeight("100%");
        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        exportTableBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                CsvExport csvExport = new CsvExport(groupsComparisonTable, "CSF-PR  Quant Comparisons Proteins");
                csvExport.setReportTitle("CSF-PR / Quant Comparisons / Proteins ");
                csvExport.setExportFileName("CSF-PR - Quant Comparisons - Proteins" + ".csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();

            }
        });
        bottomLayout.setVisible(false);

    }

    public HorizontalLayout getBottomLayout() {
        return bottomLayout;
    }

    public VerticalLayout getHideCompariosonTableBtn() {
        return hideCompBtn;
    }

    private final Set<GroupsComparison> comparisonMap = new HashSet<GroupsComparison>();
    private final Map<String, String> accessionMap = new HashMap<String, String>();
    private GroupsComparison[] compArr = new GroupsComparison[]{};
    private boolean selfselection = false;

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("ComparisonSelection")) {
            Set<String> selectedAccessions = null;
            Set<GroupsComparison> selectedComparisonList = selectionManager.getSelectedComparisonList();
            Set<GroupsComparison> newComparisons = new HashSet<GroupsComparison>();
            Set<GroupsComparison> removingComparisons = new HashSet<GroupsComparison>();
            for (GroupsComparison comparison : selectedComparisonList) {
                if (!comparisonMap.contains(comparison)) {
                    newComparisons.add(comparison);
                }
            }
            for (GroupsComparison comparison : comparisonMap) {
                if (!selectedComparisonList.contains(comparison)) {
                    removingComparisons.add(comparison);
                }
            }
            newComparisons = handler.getComparisonProtList(newComparisons);
            for (GroupsComparison comparison : removingComparisons) {
                comparisonMap.remove(comparison);
            }
            GroupsComparison[] tcompArr = new GroupsComparison[comparisonMap.size() + newComparisons.size()];
            int u = 0;
            for (GroupsComparison comparison : compArr) {
                if (comparisonMap.contains(comparison)) {
                    tcompArr[u] = comparison;
                    u++;
                }
            }
            for (GroupsComparison comparison : newComparisons) {
                tcompArr[u] = comparison;
                u++;
            }
            compArr = tcompArr;
            comparisonMap.clear();
            comparisonMap.addAll(Arrays.asList(compArr));
            if (comparisonMap.isEmpty()) {
                startLayout.setVisible(true);
                topLayout.setVisible(false);
                groupsComparisonTable.setVisible(false);
                bottomLayout.setVisible(false);
                searchField.clear();
            } else {
                selectedAccessions = selectionManager.getProtSelectionMap().keySet();
                startLayout.setVisible(false);
                topLayout.setVisible(true);
                groupsComparisonTable.setVisible(true);
                bottomLayout.setVisible(true);
            }
            selectionManager.updateSelectedComparisonList(new LinkedHashSet<GroupsComparison>(Arrays.asList(compArr)));
            updateTableData(compArr, selectedAccessions);
            updateProtCountLabel(accessionMap.size());

        } else if (type.equalsIgnoreCase("quantProtSelection") && !selfselection) {
            externalSelection = true;
            selfselection = false;
            Set<String> selectedAccessions = selectionManager.getProtSelectionMap().keySet();
            if (selectedAccessions.isEmpty()) {
                updateTableData(compArr, null);
            } else {
                updateTableData(compArr, selectedAccessions);
            }

        } else if (selfselection) {
            selfselection = false;
        }

    }

    private boolean externalSelection = false;

    private void updateProtCountLabel(int number) {
        protCounterLabel.setValue("<b>(" + number + "/" + accessionMap.size() + ")</b>");

    }

    @Override
    public String getFilterId() {
        return "comparisonTable";
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void updateTableData(GroupsComparison[] comparisonMap, Set<String> selectedAccessions) {
        this.chartsLayoutContainer.removeAllComponents();
        this.groupsComparisonTable.removeAllItems();
        chartSet.clear();
        accessionMap.clear();
        boolean useRatio = false;
        int columnWidth = 400;
        if (comparisonMap.length > 1) {
            if ((comparisonMap.length * 400) > (width - 360)) {
                useRatio = true;
                int persWidth = (int) (100.0 - (16.0 * 100.0 / (double) width));
                chartsLayoutContainer.setWidth(persWidth + "%");
                int contWid = (persWidth * (width - 360) / 100);
                columnWidth = contWid / comparisonMap.length;

            } else {
                chartsLayoutContainer.setWidth((comparisonMap.length * 400) + "px");
                useRatio = false;
            }
        } else {
            chartsLayoutContainer.setWidth((comparisonMap.length * 400) + "px");
        }
        float ratio = 360f / (float) width;
        topLayout.setExpandRatio(searchingFieldLayout, ratio);
        topLayout.setExpandRatio(chartsLayoutContainer, (1f - ratio));
        List<Object> arr = new ArrayList<Object>();
        arr.addAll(groupsComparisonTable.getContainerPropertyIds());
        for (Object col : arr) {
            groupsComparisonTable.removeContainerProperty(col);
        }
        this.groupsComparisonTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        this.groupsComparisonTable.addContainerProperty("Accession", CustomExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        this.groupsComparisonTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);

        comparisonProteinSet = new HashMap<String, ComparisonProtein[]>();
        for (int compIndex = 0; compIndex < comparisonMap.length; compIndex++) {
            GroupsComparison comp = comparisonMap[compIndex];
            ComparisonChartContainer chartPlot = generateColumnBarChart(comp, columnWidth);
            this.chartsLayoutContainer.addComponent(chartPlot);
            this.chartsLayoutContainer.setComponentAlignment(chartPlot, Alignment.MIDDLE_CENTER);
            this.groupsComparisonTable.addContainerProperty(comp.getComparisonHeader(), ComparisonProtein.class, null, comp.getComparisonHeader() + " (#Proteins: " + comp.getComparProtsMap().size() + ")", null, Table.Align.CENTER);
            groupsComparisonTable.setColumnWidth(comp.getComparisonHeader(), 100);

            Map<String, ComparisonProtein> protList = comp.getComparProtsMap();

            for (String key2 : protList.keySet()) {
                ComparisonProtein prot = protList.get(key2);
                accessionMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), prot.getUniProtAccess());
                if (!comparisonProteinSet.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                    comparisonProteinSet.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new ComparisonProtein[comparisonMap.length]);
                }
                ComparisonProtein[] tCompArr = comparisonProteinSet.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                tCompArr[compIndex] = prot;
                comparisonProteinSet.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), tCompArr);
            }
        }
        int index = 0;
        Set<Object> selectedProtId = new HashSet<Object>();
        for (String protAccName : comparisonProteinSet.keySet()) {
            int i = 0;
            String protAcc = protAccName.replace("--", "").trim().split(",")[0];
            String protName = protAccName.replace("--", "").trim().split(",")[1];
            Object[] tableRow = new Object[3 + comparisonMap.length];
            CustomExternalLink acc = new CustomExternalLink(protAcc.toUpperCase(), "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase());
            acc.setDescription("UniProt link for " + protAcc.toUpperCase());

            tableRow[i++] = index;
            tableRow[i++] = acc;
            tableRow[i++] = protName;
            for (GroupsComparison cg : comparisonMap) {
                ComparisonProtein cp = comparisonProteinSet.get(protAccName)[i - 3];
                if (cp == null) {
                    tableRow[i] = null;
                } else {
                    cp.updateWidth(columnWidth);
                    cp.updateLabelLayout();
                    tableRow[i] = cp;
                }
                i++;
            }
            this.groupsComparisonTable.addItem(tableRow, index);
            if (selectedAccessions != null && selectedAccessions.contains(protAccName)) {
                selectedProtId.add(index);
            }
            index++;
        }
        if (comparisonMap.length > 0) {
            sortComparisonTableColumn = ((GroupsComparison) comparisonMap[comparisonMap.length - 1]).getComparisonHeader();
        }
        this.groupsComparisonTable.sort(new String[]{sortComparisonTableColumn}, new boolean[]{false});
        this.groupsComparisonTable.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.groupsComparisonTable.getItemIds()) {
            Item item = this.groupsComparisonTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        this.resizeTable(useRatio, ratio);
        groupsComparisonTable.addHeaderClickListener(new Table.HeaderClickListener() {
            @Override
            public void headerClick(Table.HeaderClickEvent event) {
                sortComparisonTableColumn = event.getPropertyId().toString();
            }
        });
        if (!selectedProtId.isEmpty()) {
            groupsComparisonTable.setValue(selectedProtId);
        } else {
            groupsComparisonTable.setValue(null);
        }
    }

    private void resizeTable(boolean useRatio, float ratio) {
        groupsComparisonTable.setColumnWidth("Index", 47);
        groupsComparisonTable.setColumnWidth("Accession", 87);
        groupsComparisonTable.setColumnWidth("Name", 187);
        if ((groupsComparisonTable.getSortableContainerPropertyIds().size() - 3) > 1 && useRatio) {
            float factor = (1f - ratio) / ((float) groupsComparisonTable.getSortableContainerPropertyIds().size() - 3);
            for (Object propertyId : groupsComparisonTable.getSortableContainerPropertyIds()) {
                if (propertyId.toString().equalsIgnoreCase("Index") || propertyId.toString().equalsIgnoreCase("Accession") || propertyId.toString().equalsIgnoreCase("Name")) {
                    continue;
                }
                groupsComparisonTable.setColumnExpandRatio(propertyId, factor);
            }

        } else {
            for (Object propertyId : groupsComparisonTable.getSortableContainerPropertyIds()) {
                if (propertyId.toString().equalsIgnoreCase("Index") || propertyId.toString().equalsIgnoreCase("Accession") || propertyId.toString().equalsIgnoreCase("Name")) {
                    continue;
                }
                groupsComparisonTable.setColumnWidth(propertyId, 387);
            }
        }

    }
    private final Set<ComparisonChartContainer> chartSet = new HashSet<ComparisonChartContainer>();
    private ComparisonChartContainer lastHeighlitedChart;

    private ComparisonChartContainer generateColumnBarChart(final GroupsComparison comparison, int width) {
        final ComparisonChartContainer chart = new ComparisonChartContainer(comparison, width);
        LayoutEvents.LayoutClickListener chartDataClickHandler = new LayoutEvents.LayoutClickListener() {
            private final Map<Integer, Set<String>> localCompProtMap = chart.getCompProtMap();
            private final String compName = comparison.getComparisonHeader();
            private final ComparisonChartContainer localchart = chart;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component c = event.getClickedComponent();
                if (c instanceof SquaredDot) {
                    if (lastHeighlitedChart != null) {
                        lastHeighlitedChart.setHeghlighted(false);
                    }
                    int i = (Integer) ((SquaredDot) c).getParam("barIndex");
                    localchart.setHeghlighted(true);
                    lastHeighlitedChart = localchart;
                    sortComparisonTableColumn = compName;
                    filterTable(localCompProtMap.get(i), compArr, sortComparisonTableColumn);
                    updateProtCountLabel(localCompProtMap.get(i).size());
                    localchart.heighLightBar(i);
                    updateChartsWithSelectedChartColumn(sortComparisonTableColumn, new HashSet<String>(localCompProtMap.get(i)));
                }
            }
        };
        chart.addChartListener(chartDataClickHandler);

        LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

            private final GroupsComparison localComparison = comparison;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Set<GroupsComparison> selectedComparisonList = selectionManager.getSelectedComparisonList();
                selectedComparisonList.remove(localComparison);
                selectionManager.setComparisonSelection(selectedComparisonList);
            }
        };
        chart.addCloseListiner(closeListener);
        chartSet.add(chart);
        return chart;

    }

    private Set<String> searchProteins(String keyword) {
        Set<String> subAccessionMap = new HashSet<String>();
        for (String key : accessionMap.keySet()) {
            if (key.trim().contains(keyword.toLowerCase().trim())) {
                subAccessionMap.add(accessionMap.get(key));
            }
        }
        return subAccessionMap;
    }

    private Map<String, ComparisonProtein[]> comparisonProteinSet ;
    private void filterTable(Set<String> accessions, GroupsComparison[] comparisonMap, String sortCompColumnName) {
        groupsComparisonTable.removeValueChangeListener(QuantProteinsComparisonsContainer.this);
        this.groupsComparisonTable.removeAllItems();
        comparisonProteinSet = new HashMap<String, ComparisonProtein[]>();
        for (int compIndex = 0; compIndex < comparisonMap.length; compIndex++) {
            GroupsComparison comp = comparisonMap[compIndex];
            Map<String, ComparisonProtein> protList = comp.getComparProtsMap();
            for (String key2 : protList.keySet()) {
                ComparisonProtein prot = protList.get(key2);
                if (!accessions.contains(prot.getUniProtAccess())) {
                    continue;
                }

                if (!comparisonProteinSet.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                    comparisonProteinSet.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new ComparisonProtein[comparisonMap.length]);
                }
                ComparisonProtein[] tCompArr = comparisonProteinSet.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                tCompArr[compIndex] = prot;
                comparisonProteinSet.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), tCompArr);
            }
        }

        int index = 0;
        for (String protAccName : comparisonProteinSet.keySet()) {
            int i = 0;
            String protAcc = protAccName.replace("--", "").trim().split(",")[0];
            String protName = protAccName.replace("--", "").trim().split(",")[1];
            Object[] tableRow = new Object[3 + comparisonMap.length];
            CustomExternalLink acc = new CustomExternalLink(protAcc.toUpperCase(), "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase());
            acc.setDescription("UniProt link for " + protAcc.toUpperCase());

            tableRow[i++] = index;
            tableRow[i++] = acc;
            tableRow[i++] = protName;
            for (GroupsComparison cg : comparisonMap) {
                ComparisonProtein cp = comparisonProteinSet.get(protAccName)[i - 3];
                if (cp == null) {
                    tableRow[i] = null;
                } else {

                    cp.updateLabelLayout();
                    tableRow[i] = cp;
                }
                i++;
            }
            this.groupsComparisonTable.addItem(tableRow, index);
            index++;
        }
        if (sortCompColumnName.equalsIgnoreCase("")) {
            sortCompColumnName = ((GroupsComparison) comparisonMap[0]).getComparisonHeader();
        }

        if (comparisonMap.length > 0) {
            this.groupsComparisonTable.sort(new String[]{sortCompColumnName}, new boolean[]{false});
        }
        this.groupsComparisonTable.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.groupsComparisonTable.getItemIds()) {
            Item item = this.groupsComparisonTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        groupsComparisonTable.addValueChangeListener(QuantProteinsComparisonsContainer.this);
    }

    private void selectAll() {
        groupsComparisonTable.setValue(groupsComparisonTable.getItemIds());

    }

    private void unSelectAll() {
        groupsComparisonTable.setValue(null);

    }
    private final Set<Integer> proteinskeys = new HashSet<Integer>();
    private final TreeMap<Integer, CustomExternalLink> lastSelectedProts = new TreeMap<Integer, CustomExternalLink>();
    //start table selection

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (groupsComparisonTable.getValue() != null) {
            proteinskeys.clear();
            proteinskeys.addAll((Set) groupsComparisonTable.getValue());
        } else {
            proteinskeys.clear();
        }
        if (!lastSelectedProts.isEmpty()) {
            for (CustomExternalLink uniprot : lastSelectedProts.values()) {
                uniprot.rePaintLable("black");
            }
        }
        lastSelectedProts.clear();
        for (int proteinskey : proteinskeys) {

            final Item item = groupsComparisonTable.getItem(proteinskey);
            CustomExternalLink lastSelectedProt = (CustomExternalLink) item.getItemProperty("Accession").getValue();
            Integer index = (Integer) item.getItemProperty("Index").getValue();
            lastSelectedProt.rePaintLable("white");
            lastSelectedProts.put(index, lastSelectedProt);
        }
        List<String> accessions = new ArrayList<String>();
        for (int index : lastSelectedProts.keySet()) {
            CustomExternalLink str = lastSelectedProts.get(index);
            accessions.add(str.toString());
        }
        this.updateChartsWithTableSelectedProteins(new HashSet<String>(accessions));
        LinkedHashMap<String, ComparisonProtein[]> protSelectionMap = new LinkedHashMap<String, ComparisonProtein[]>();
        for (String accession : accessions) {
            for (int compIndex = 0; compIndex < compArr.length; compIndex++) {
                GroupsComparison comp = compArr[compIndex];
                Map<String, ComparisonProtein> protList = comp.getComparProtsMap();
                for (ComparisonProtein prot : protList.values()) {
                    if (prot.getUniProtAccess().equalsIgnoreCase(accession)) {
                        if (!protSelectionMap.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                            protSelectionMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new ComparisonProtein[compArr.length]);
                        }
                        ComparisonProtein[] tCompArr = protSelectionMap.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                        tCompArr[compIndex] = prot;
                        protSelectionMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), tCompArr);

                    }
                }

            }
        }

        if (externalSelection) {
            externalSelection = false;
        } else {
            selfselection = true;
            selectionManager.setProteinsSelection(protSelectionMap);
        }

    }

    private void updateChartsWithTableSelectedProteins(Set<String> accessions) {

        for (ComparisonChartContainer chart : chartSet) {
            chart.updateExternalSelection(accessions);
        }

    }

    private void updateChartsWithSelectedChartColumn(String selectedComparisonHeader, Set<String> accessions) {

        for (ComparisonChartContainer chart : chartSet) {
            if (chart.getComparisonHeader().equalsIgnoreCase(selectedComparisonHeader)) {
                chart.updateChartsWithSelectedChartColumn(accessions, false);
            } else {
                chart.updateChartsWithSelectedChartColumn(accessions, true);
            }
        }

    }

    private void resetAllCharts() {
        for (ComparisonChartContainer chart : chartSet) {
            chart.reset();
        }

    }

    public void resizeCharts(int width) {
        for (ComparisonChartContainer chart : chartSet) {
            chart.resizeChart(width);
        }

    }

}
