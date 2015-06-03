/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.datasetsoverview;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutListener;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickEvent;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickHandler;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.ComparisonProtein;
import probe.com.model.beans.GroupsComparison;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringSelectionManagerRes;
import probe.com.view.core.CustomExternalLink;

/**
 *
 * @author Yehia Farag
 */
public class QuantProteinsComparisonTable extends VerticalLayout implements CSFFilter, Property.ValueChangeListener {

    private final DatasetExploringSelectionManagerRes selectionManager;
    private final Table groupsComparisonTable;
    private final MainHandler handler;
    private final HorizontalLayout topLayout, chartsLayoutContainer, bottomLayout;
    private final GridLayout searchingFieldLayout;
    private final TextField searchField;
    private final VerticalLayout searchingBtn;
    private final VerticalLayout resetSearchBtn;
    private final VerticalLayout startLayout, hideCompariosonMatrixTableBtn;
    private final Label protCounterLabel;
    private int width = 1754;
    private final OptionGroup removeUniqueProteinsOption;

    public void setLayoutWidth(int width) {
        this.width = width;
        this.setWidth(width + "px");
        this.bottomLayout.setWidth(width + "px");
        float ratio = 360f / (float) width;
        topLayout.setExpandRatio(searchingFieldLayout, ratio);
        topLayout.setExpandRatio(chartsLayoutContainer, (1f - ratio));

    }

    public QuantProteinsComparisonTable(DatasetExploringSelectionManagerRes selectionManager, MainHandler handler) {
        this.selectionManager = selectionManager;
        selectionManager.registerFilter(QuantProteinsComparisonTable.this);
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
        topLayout.setSpacing(true);
        searchingFieldLayout = new GridLayout();
        searchingFieldLayout.setWidth("100%");
        searchingFieldLayout.setHeight("250px");
        searchingFieldLayout.setColumns(4);
        searchingFieldLayout.setRows(3);
        searchingFieldLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        searchingFieldLayout.setColumnExpandRatio(0, 0.8f);
        searchingFieldLayout.setColumnExpandRatio(1, 0.05f);
        searchingFieldLayout.setColumnExpandRatio(2, 0.1f);
        searchingFieldLayout.setColumnExpandRatio(3, 0.05f);

        chartsLayoutContainer = new HorizontalLayout();
        chartsLayoutContainer.setHeight("250px");
        chartsLayoutContainer.setWidth("100%");
        chartsLayoutContainer.setStyleName(Reindeer.LAYOUT_WHITE);
//        chartsLayoutContainer.setMargin(new MarginInfo(false, false, false, true));

        topLayout.addComponent(searchingFieldLayout);
        topLayout.addComponent(chartsLayoutContainer);
//        topLayout.setComponentAlignment(chartsLayoutContainer, Alignment.TOP_LEFT);

//        topLayout.setExpandRatio(searchingFieldLayout, 0.2f);
//        topLayout.setExpandRatio(chartsLayoutContainer, 0.80f);
        // add searching field to spacer
        //allow search in 
        VerticalLayout topSpacer = new VerticalLayout();
        topSpacer.setHeight("180px");
        topSpacer.setWidth("100%");
        topSpacer.setStyleName(Reindeer.LAYOUT_WHITE);
        searchingFieldLayout.addComponent(topSpacer, 0, 0);

        searchField = new TextField();
        searchField.setDescription("Search Proteins By Name or Accession");
//        searchField.setWidthUndefined();
        searchField.setImmediate(true);
        searchField.setWidth("100%");
        searchField.setHeight("24px");
        searchField.setInputPrompt("Search...");
        searchingFieldLayout.addComponent(searchField, 0, 2);
        searchingFieldLayout.setComponentAlignment(searchField, Alignment.MIDDLE_LEFT);

        searchingBtn = new VerticalLayout();
        searchingBtn.setWidth("30px");
        searchingBtn.setHeight("24px");
        searchingBtn.setStyleName("tablesearchingbtn");
        searchingFieldLayout.addComponent(searchingBtn, 1, 2);
        searchingFieldLayout.setComponentAlignment(searchingBtn, Alignment.MIDDLE_CENTER);

        protCounterLabel = new Label("");
        protCounterLabel.setWidth("100%");
        protCounterLabel.setHeight("24px");
        protCounterLabel.setContentMode(ContentMode.HTML);
        searchingFieldLayout.addComponent(protCounterLabel, 0, 1);
        searchingFieldLayout.setComponentAlignment(protCounterLabel, Alignment.BOTTOM_LEFT);

        resetSearchBtn = new VerticalLayout();
        resetSearchBtn.setDescription("Reset table");
        resetSearchBtn.setStyleName("smallresetbtn");
        resetSearchBtn.setWidth("70px");
        resetSearchBtn.setHeight("24px");
        searchingFieldLayout.addComponent(resetSearchBtn, 2, 2);
        searchingFieldLayout.setComponentAlignment(resetSearchBtn, Alignment.MIDDLE_CENTER);

        hideCompariosonMatrixTableBtn = new VerticalLayout();
        hideCompariosonMatrixTableBtn.setWidth("30px");
        hideCompariosonMatrixTableBtn.setHeight("24px");
        hideCompariosonMatrixTableBtn.setStyleName("matrixbtn");
        hideCompariosonMatrixTableBtn.setDescription("hide/show comparison table");
        searchingFieldLayout.addComponent(hideCompariosonMatrixTableBtn, 3, 2);
        searchingFieldLayout.setComponentAlignment(hideCompariosonMatrixTableBtn, Alignment.MIDDLE_CENTER);
        hideCompariosonMatrixTableBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                for (ComparisonChart chart : chartSet) {
                    chart.resizeChart();
                }

            }
        });
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
        groupsComparisonTable.addValueChangeListener(QuantProteinsComparisonTable.this);
        groupsComparisonTable.setMultiSelect(true);
        groupsComparisonTable.setMultiSelectMode(MultiSelectMode.DEFAULT);

        //add searching listeners 
        searchingBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                removeUniqueProteinsOption.unselect("Hide unique proteins");
                Set<String> subAccList = searchProteins(searchField.getValue());
                if (subAccList.isEmpty()) {
                    Notification.show("Not available");
                } else {
                    filterTable(subAccList, compArr, -1);
                }
                updateProtCountLabel(subAccList.size());

            }
        });

        Button b = new Button();
        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Set<String> subAccList = searchProteins(searchField.getValue());
                filterTable(subAccList, compArr, -1);
            }
        });
        searchField.addShortcutListener(new Button.ClickShortcut(b, ShortcutListener.KeyCode.ENTER));

        resetSearchBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                searchField.clear();
                updateTableData(compArr);
//                filterTable(new HashSet<String>(accessionMap.values()), compArr, -1,false);
                updateProtCountLabel(accessionMap.size());
            }
        });
        bottomLayout = new HorizontalLayout();
//        bottomLayout.setSpacing(true);
//        bottomLayout.setWidth("100%");
        bottomLayout.setHeight("100%");
        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        HorizontalLayout leftBottomLayout = new HorizontalLayout();
        leftBottomLayout.setWidthUndefined();
        bottomLayout.addComponent(leftBottomLayout);
        bottomLayout.setComponentAlignment(leftBottomLayout, Alignment.TOP_LEFT);

        removeUniqueProteinsOption = new OptionGroup();
        leftBottomLayout.addComponent(removeUniqueProteinsOption);
        leftBottomLayout.setComponentAlignment(removeUniqueProteinsOption, Alignment.TOP_LEFT);
        removeUniqueProteinsOption.setWidth("150px");
//        removeUniqueProteinsOption.setHeight("40px");
        removeUniqueProteinsOption.setNullSelectionAllowed(true); // user can not 'unselect'
        removeUniqueProteinsOption.setMultiSelect(true);

        removeUniqueProteinsOption.addItem("Hide unique proteins");
        removeUniqueProteinsOption.addStyleName("horizontal");
        removeUniqueProteinsOption.addValueChangeListener(new Property.ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (removeUniqueProteinsOption.getValue().toString().equalsIgnoreCase("[Hide unique proteins]")) {
                    Set<Object> itemIds = new HashSet<Object>(groupsComparisonTable.getItemIds());
                    for (Object id : itemIds) {
                        Item item = groupsComparisonTable.getItem(id);
                        for (GroupsComparison gc : compArr) {
                            if (item.getItemProperty(gc.getComparisonHeader()).getValue() == null) {
                                groupsComparisonTable.removeItem(id);
                                break;
                            }
                        }
                    }

                    updateProtCountLabel(groupsComparisonTable.getItemIds().size());

                } else {
                    filterTable(new HashSet<String>(accessionMap.values()), compArr, -1);
                    updateProtCountLabel(accessionMap.size());

                }
            }
        });

        Button selectAllBtn = new Button("Select all");
        selectAllBtn.setHeight("30px");
        selectAllBtn.setWidth("70px");
//        unSelectAllBtn.setStyleName("grcompbtn");

        selectAllBtn.setStyleName(Reindeer.BUTTON_LINK);

        leftBottomLayout.addComponent(selectAllBtn);
        leftBottomLayout.setComponentAlignment(selectAllBtn, Alignment.TOP_LEFT);
        selectAllBtn.setDescription("Select all data");

        selectAllBtn.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                selectAll();
            }
        });
        Button unSelectAllBtn = new Button("Unselect all");
        unSelectAllBtn.setHeight("30px");
        unSelectAllBtn.setWidth("70px");
//        unSelectAllBtn.setStyleName("grcompbtn");

        unSelectAllBtn.setStyleName(Reindeer.BUTTON_LINK);

        leftBottomLayout.addComponent(unSelectAllBtn);
        leftBottomLayout.setComponentAlignment(unSelectAllBtn, Alignment.TOP_LEFT);
        unSelectAllBtn.setDescription("Unselect all data");

        unSelectAllBtn.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                unSelectAll();
            }
        });

        Button exportTableBtn = new Button("Export Proteins");
        exportTableBtn.setHeight("30px");
        exportTableBtn.setWidth("80px");
        exportTableBtn.setStyleName(Reindeer.BUTTON_LINK);
        bottomLayout.addComponent(exportTableBtn);
        bottomLayout.setComponentAlignment(exportTableBtn, Alignment.TOP_RIGHT);
        exportTableBtn.setDescription("Export table data");
        bottomLayout.setHeight("100%");
        bottomLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        exportTableBtn.addClickListener(new ClickListener() {

            @Override
            public void buttonClick(ClickEvent event) {
                //export data
                System.out.println("export table");
            }
        });
        bottomLayout.setVisible(false);

    }

    public HorizontalLayout getBottomLayout() {
        return bottomLayout;
    }

    public VerticalLayout getHideCompariosonTableBtn() {
        return hideCompariosonMatrixTableBtn;
    }

    private final Set<GroupsComparison> comparisonMap = new HashSet<GroupsComparison>();
    private final Map<String, String> accessionMap = new HashMap<String, String>();
    private GroupsComparison[] compArr = new GroupsComparison[]{};

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("DSSelection")) {
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

                startLayout.setVisible(false);
                topLayout.setVisible(true);
                groupsComparisonTable.setVisible(true);
                bottomLayout.setVisible(true);
            }
            updateTableData(compArr);
            updateProtCountLabel(accessionMap.size());

        }

    }

    private void updateProtCountLabel(int number) {
        protCounterLabel.setValue("<center><b>(" + number + "/" + accessionMap.size() + ")</b></center>");

    }

    @Override
    public String getFilterId() {
        return "comparisonTable";
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

//    private final Set<ComparisonChart> comparisonChartsSet= new HashSet<ComparisonChart>();
    private void updateTableData(GroupsComparison[] comparisonMap) {
        this.chartsLayoutContainer.removeAllComponents();
        this.groupsComparisonTable.removeAllItems();
        chartSet.clear();
        accessionMap.clear();
        boolean useRatio = false;
        if (comparisonMap.length > 1) {
            if ((comparisonMap.length * 400) > (width - 360)) {
                useRatio = true;
                chartsLayoutContainer.setWidth("100%");
            } else {
                chartsLayoutContainer.setWidth((comparisonMap.length * 400) + "px");
                useRatio = false;
            }
        } else {
            chartsLayoutContainer.setWidth((comparisonMap.length * 400) + "px");
        }
        List<Object> arr = new ArrayList<Object>();
        arr.addAll(groupsComparisonTable.getContainerPropertyIds());
        for (Object col : arr) {
            groupsComparisonTable.removeContainerProperty(col);
        }
        this.groupsComparisonTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        this.groupsComparisonTable.addContainerProperty("Accession", CustomExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        this.groupsComparisonTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);

        Map<String, ComparisonProtein[]> protSetMap = new HashMap<String, ComparisonProtein[]>();
        for (int compIndex = 0; compIndex < comparisonMap.length; compIndex++) {
            GroupsComparison comp = comparisonMap[compIndex];
            ComparisonChart chartPlot = generateColumnBarChart(comp, compIndex);
            this.chartsLayoutContainer.addComponent(chartPlot);
            this.chartsLayoutContainer.setComponentAlignment(chartPlot, Alignment.MIDDLE_RIGHT);
            this.groupsComparisonTable.addContainerProperty(comp.getComparisonHeader(), ComparisonProtein.class, null, comp.getComparisonHeader() + " (#Proteins: " + comp.getComparProtsMap().size() + ")", null, Table.Align.CENTER);
            Map<String, ComparisonProtein> protList = comp.getComparProtsMap();

            for (String key2 : protList.keySet()) {
                ComparisonProtein prot = protList.get(key2);
                accessionMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), prot.getUniProtAccess());
                if (!protSetMap.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                    protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new ComparisonProtein[comparisonMap.length]);
                }
                ComparisonProtein[] tCompArr = protSetMap.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                tCompArr[compIndex] = prot;
                protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), tCompArr);
            }
        }
        int index = 0;
        for (String protAccName : protSetMap.keySet()) {
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
                ComparisonProtein cp = protSetMap.get(protAccName)[i - 3];
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

        if (comparisonMap.length > 0) {
            this.groupsComparisonTable.sort(new String[]{((GroupsComparison) comparisonMap[0]).getComparisonHeader()}, new boolean[]{false});
        }
        this.groupsComparisonTable.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.groupsComparisonTable.getItemIds()) {
            Item item = this.groupsComparisonTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        indexing = 1;

        float ratio = 360f / (float) width;
        topLayout.setExpandRatio(searchingFieldLayout, ratio);
        topLayout.setExpandRatio(chartsLayoutContainer, (1f - ratio));
        groupsComparisonTable.setColumnWidth("Index", 47);
        groupsComparisonTable.setColumnWidth("Accession", 87);
        groupsComparisonTable.setColumnWidth("Name", 187);
        if ((groupsComparisonTable.getSortableContainerPropertyIds().size() - 3) > 1 && useRatio) {
//            groupsComparisonTable.setColumnExpandRatio("Index", 0.01f);
//            groupsComparisonTable.setColumnExpandRatio("Accession", 0.05f);
//            groupsComparisonTable.setColumnExpandRatio("Name", 0.14f);            

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
    private final Set<ComparisonChart> chartSet = new HashSet<ComparisonChart>();

    private ComparisonChart generateColumnBarChart(final GroupsComparison comparison, final int index) {
        final ComparisonChart chart = new ComparisonChart(comparison);
        ChartDataClickHandler chartDataClickHandler = new ChartDataClickHandler() {
            private final Map<Integer, Set<String>> localCompProtMap = chart.getCompProtMap();
            private final int compIndex = index;

            @Override
            public void onChartDataClick(ChartDataClickEvent event) {
                Integer i = (int) (long) event.getChartData().getPointIndex();
                filterTable(localCompProtMap.get(i), compArr, compIndex);
                updateProtCountLabel(localCompProtMap.get(i).size());
            }
        };

        LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

            private final GroupsComparison localComparison = comparison;

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Set<GroupsComparison> selectedComparisonList = selectionManager.getSelectedComparisonList();
                selectedComparisonList.remove(localComparison);
                selectionManager.updatedComparisonSelection(selectedComparisonList);
            }
        };
        chart.getCloseCompariosonBtn().addLayoutClickListener(closeListener);

        chart.setChartDataClickHandler(chartDataClickHandler);
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

    private void filterTable(Set<String> accessions, GroupsComparison[] comparisonMap, int sortCompIndex) {
        groupsComparisonTable.removeValueChangeListener(QuantProteinsComparisonTable.this);
        this.groupsComparisonTable.removeAllItems();
        Map<String, ComparisonProtein[]> protSetMap = new HashMap<String, ComparisonProtein[]>();
        for (int compIndex = 0; compIndex < comparisonMap.length; compIndex++) {
            GroupsComparison comp = comparisonMap[compIndex];
            Map<String, ComparisonProtein> protList = comp.getComparProtsMap();
            for (String key2 : protList.keySet()) {
                ComparisonProtein prot = protList.get(key2);
                if (!accessions.contains(prot.getUniProtAccess())) {
                    continue;
                }

                if (!protSetMap.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                    protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new ComparisonProtein[comparisonMap.length]);
                }
                ComparisonProtein[] tCompArr = protSetMap.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                tCompArr[compIndex] = prot;
                protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), tCompArr);
            }
        }

        int index = 0;
        for (String protAccName : protSetMap.keySet()) {
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
                ComparisonProtein cp = protSetMap.get(protAccName)[i - 3];
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
        if (sortCompIndex == -1) {
            sortCompIndex = 0;
        }

        if (comparisonMap.length > 0) {
            this.groupsComparisonTable.sort(new String[]{((GroupsComparison) comparisonMap[sortCompIndex]).getComparisonHeader()}, new boolean[]{false});
        }
        this.groupsComparisonTable.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.groupsComparisonTable.getItemIds()) {
            Item item = this.groupsComparisonTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        indexing = 1;

//          groupsComparisonTable.setColumnWidth("Index",60);
//            groupsComparisonTable.setColumnWidth("Accession", 100);
//            groupsComparisonTable.setColumnWidth("Name", 200);
//          float ratio = 360f/(float)width;
//          topLayout.setExpandRatio(searchingFieldLayout, ratio);
//            topLayout.setExpandRatio(chartsLayoutContainer,(1f-ratio));
//        if ((groupsComparisonTable.getSortableContainerPropertyIds().size() - 3) > 3) {
////            groupsComparisonTable.setColumnExpandRatio("Index", 0.01f);
////            groupsComparisonTable.setColumnExpandRatio("Accession", 0.05f);
////            groupsComparisonTable.setColumnExpandRatio("Name", 0.14f);            
//
//            float factor =(1f-ratio) / ((float) groupsComparisonTable.getSortableContainerPropertyIds().size() - 3);
//            for (Object propertyId : groupsComparisonTable.getSortableContainerPropertyIds()) {
//                if (propertyId.toString().equalsIgnoreCase("Index") || propertyId.toString().equalsIgnoreCase("Accession") || propertyId.toString().equalsIgnoreCase("Name")) {
//                    continue;
//                }
//                groupsComparisonTable.setColumnExpandRatio(propertyId, factor);
//
//            }
//            
//        } else {
//            for (Object propertyId : groupsComparisonTable.getSortableContainerPropertyIds()) {
//                if (propertyId.toString().equalsIgnoreCase("Index") || propertyId.toString().equalsIgnoreCase("Accession") || propertyId.toString().equalsIgnoreCase("Name")) {
//                    continue;
//                }
//                groupsComparisonTable.setColumnWidth(propertyId, 400);
//
//            }
//
//        }
        groupsComparisonTable.addValueChangeListener(QuantProteinsComparisonTable.this);
        this.updateChartsWithSelectedProteins(accessions, false);
//        groupsComparisonTable.setValue(groupsComparisonTable.getItemIds());

    }

    private void selectAll() {
        groupsComparisonTable.setValue(groupsComparisonTable.getItemIds());

    }

    private void unSelectAll() {
        groupsComparisonTable.setValue(null);

    }

    private final Set<Integer> proteinskeys = new HashSet<Integer>();
    private final Set<CustomExternalLink> lastSelectedProts = new HashSet<CustomExternalLink>();
    //start table selection

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (groupsComparisonTable.getValue() != null) {
            proteinskeys.clear();
            proteinskeys.addAll((Set) groupsComparisonTable.getValue());
        } else {
            proteinskeys.clear();
//            return;
        }
        if (!lastSelectedProts.isEmpty()) {
            for (CustomExternalLink uniprot : lastSelectedProts) {
                uniprot.rePaintLable("black");
            }
        }
        lastSelectedProts.clear();
        for (int proteinskey : proteinskeys) {

            final Item item = groupsComparisonTable.getItem(proteinskey);
            CustomExternalLink lastSelectedProt = (CustomExternalLink) item.getItemProperty("Accession").getValue();
            lastSelectedProt.rePaintLable("white");
            lastSelectedProts.add(lastSelectedProt);
        }
        Set<String> accessions = new HashSet<String>();
        for (CustomExternalLink str : lastSelectedProts) {
            accessions.add(str.toString());
        }
        updateChartsWithSelectedProteins(accessions, true);
    }

    private void updateChartsWithSelectedProteins(Set<String> accessions, boolean tableSelection) {

        for (ComparisonChart chart : chartSet) {
            chart.updateSelection(accessions, tableSelection);
        }

    }

}
