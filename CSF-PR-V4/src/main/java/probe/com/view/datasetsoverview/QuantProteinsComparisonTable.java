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
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
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
public class QuantProteinsComparisonTable extends VerticalLayout implements CSFFilter,Property.ValueChangeListener {

    private final DatasetExploringSelectionManagerRes selectionManager;
    private final Table groupsComparisonTable;
    private final MainHandler handler;
    private final HorizontalLayout topLayout, chartsLayoutContainer;
    private final GridLayout searchingFieldLayout;
    private final TextField searchField;
    private final VerticalLayout searchingBtn;
    private final VerticalLayout resetSearchBtn;
    private final VerticalLayout startLayout;
    private final Label protCounterLabel;

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
        searchingFieldLayout.setColumns(3);
        searchingFieldLayout.setRows(3);
        searchingFieldLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        searchingFieldLayout.setColumnExpandRatio(0, 0.8f);
        searchingFieldLayout.setColumnExpandRatio(1, 0.1f);
        searchingFieldLayout.setColumnExpandRatio(2, 0.1f);

        chartsLayoutContainer = new HorizontalLayout();
        chartsLayoutContainer.setHeight("250px");
        chartsLayoutContainer.setWidth("100%");
        chartsLayoutContainer.setStyleName(Reindeer.LAYOUT_WHITE);

        topLayout.addComponent(searchingFieldLayout);
        topLayout.addComponent(chartsLayoutContainer);
        topLayout.setExpandRatio(searchingFieldLayout, 0.15f);
        topLayout.setExpandRatio(chartsLayoutContainer, 0.85f);

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
        searchField.setHeight("23px");
        searchField.setInputPrompt("Search...");
        searchingFieldLayout.addComponent(searchField, 0, 2);
        searchingFieldLayout.setComponentAlignment(searchField, Alignment.MIDDLE_LEFT);

        searchingBtn = new VerticalLayout();
        searchingBtn.setWidth("30px");
        searchingBtn.setHeight("23px");
        searchingBtn.setStyleName("tablesearchingbtn");
        searchingFieldLayout.addComponent(searchingBtn, 1, 2);
        searchingFieldLayout.setComponentAlignment(searchingBtn, Alignment.MIDDLE_CENTER);

        protCounterLabel = new Label("");
        protCounterLabel.setWidth("100%");
        protCounterLabel.setHeight("23px");
        protCounterLabel.setContentMode(ContentMode.HTML);
        searchingFieldLayout.addComponent(protCounterLabel, 0, 1);
        searchingFieldLayout.setComponentAlignment(protCounterLabel, Alignment.BOTTOM_LEFT);

        resetSearchBtn = new VerticalLayout();
        resetSearchBtn.setDescription("Reset table");
        resetSearchBtn.setStyleName("tablesearchingnextbtn");
        resetSearchBtn.setWidth("70px");
        resetSearchBtn.setHeight("23px");
        searchingFieldLayout.addComponent(resetSearchBtn, 2, 2);
        searchingFieldLayout.setComponentAlignment(resetSearchBtn, Alignment.MIDDLE_LEFT);

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
                Set<String> subAccList = searchProteins(searchField.getValue());
                filterTable(subAccList, comparisonMap);
                updateProtCountLabel(subAccList.size());
            }
        });

        Button b = new Button();
        b.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                Set<String> subAccList = searchProteins(searchField.getValue());
                filterTable(subAccList, comparisonMap);
            }
        });
        searchField.addShortcutListener(new Button.ClickShortcut(b, ShortcutListener.KeyCode.ENTER));

        resetSearchBtn.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                updateProtCountLabel(0);
                searchField.clear();
                filterTable(new HashSet<String>(accessionMap.values()), comparisonMap);
            }
        });

    }
    private Set<GroupsComparison> comparisonMap;
    private final Map<String, String> accessionMap = new HashMap<String, String>();

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("DSSelection")) {
            Set<GroupsComparison> selectedComparisonList = selectionManager.getSelectedComparisonList();
            comparisonMap = handler.getComparisonProtList(selectedComparisonList);
            if (comparisonMap.isEmpty()) {
                startLayout.setVisible(true);
                topLayout.setVisible(false);
                groupsComparisonTable.setVisible(false);
                searchField.clear();

            } else {

                startLayout.setVisible(false);
                topLayout.setVisible(true);
                groupsComparisonTable.setVisible(true);

            }
            updateTableData(comparisonMap);
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
    private void updateTableData(Set<GroupsComparison> comparisonMap) {
        this.chartsLayoutContainer.removeAllComponents();
        this.groupsComparisonTable.removeAllItems();
        chartSet.clear();;
//        comparisonChartsSet.clear();
        accessionMap.clear();
        List<Object> arr = new ArrayList<Object>();
        arr.addAll(groupsComparisonTable.getContainerPropertyIds());
        for (Object col : arr) {
            groupsComparisonTable.removeContainerProperty(col);
        }
        this.groupsComparisonTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        this.groupsComparisonTable.addContainerProperty("Accession", CustomExternalLink.class, null, "Accession", null, Table.Align.LEFT);
        this.groupsComparisonTable.addContainerProperty("Name", String.class, null, "Name", null, Table.Align.LEFT);

        int compIndex = 0;
        Map<String, ComparisonProtein[]> protSetMap = new HashMap<String, ComparisonProtein[]>();
        for (GroupsComparison comp : comparisonMap) {
             ComparisonChart chartPlot = generateColumnBarChart(comp);
            this.chartsLayoutContainer.addComponent(chartPlot);
            this.chartsLayoutContainer.setComponentAlignment(chartPlot, Alignment.MIDDLE_CENTER);
            this.groupsComparisonTable.addContainerProperty(comp.getComparisonHeader(), ComparisonProtein.class, null, comp.getComparisonHeader() + " (#Proteins: " + comp.getComparProtsMap().size() + ")", null, Table.Align.CENTER);
            Map<String, ComparisonProtein> protList = comp.getComparProtsMap();

            for (String key2 : protList.keySet()) {
                ComparisonProtein prot = protList.get(key2);
                accessionMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), prot.getUniProtAccess());
                if (!protSetMap.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                    protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new ComparisonProtein[comparisonMap.size()]);
                }
                ComparisonProtein[] compArr = protSetMap.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                compArr[compIndex] = prot;
                protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), compArr);
            }
            compIndex++;
        }
        int index = 0;
        for (String protAccName : protSetMap.keySet()) {
            int i = 0;
            String protAcc = protAccName.replace("--", "").trim().split(",")[0];
            String protName = protAccName.replace("--", "").trim().split(",")[1];
            Object[] tableRow = new Object[3 + comparisonMap.size()];
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

        if (!comparisonMap.isEmpty()) {
            this.groupsComparisonTable.sort(new String[]{((GroupsComparison) comparisonMap.toArray()[0]).getComparisonHeader()}, new boolean[]{false});
        }
        this.groupsComparisonTable.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.groupsComparisonTable.getItemIds()) {
            Item item = this.groupsComparisonTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        indexing = 1;

        groupsComparisonTable.setColumnExpandRatio("Index", 0.01f);
        groupsComparisonTable.setColumnExpandRatio("Accession", 0.05f);
        groupsComparisonTable.setColumnExpandRatio("Name", 0.09f);

        float factor = 0.85f / ((float) groupsComparisonTable.getSortableContainerPropertyIds().size() - 3);
        for (Object propertyId : groupsComparisonTable.getSortableContainerPropertyIds()) {
            if (propertyId.toString().equalsIgnoreCase("Index") || propertyId.toString().equalsIgnoreCase("Accession") || propertyId.toString().equalsIgnoreCase("Name")) {
                continue;
            }
            groupsComparisonTable.setColumnExpandRatio(propertyId, factor);

        }

    }
    private final Set<ComparisonChart> chartSet = new HashSet<ComparisonChart>();
    private ComparisonChart generateColumnBarChart(final GroupsComparison comparison) {
//        Map<String, ComparisonProtein> protList = comparison.getComparProtsMap();
//        final Map<Integer, Set<String>> compProtMap = new HashMap<Integer, Set<String>>();
//        double[] values = new double[21];
//        Object[] labels = new String[21];
//        double maxIndexerValue = 0.0;
//        //init values 
//        for (String key2 : protList.keySet()) {
//            ComparisonProtein prot = protList.get(key2);
//            prot.updateLabelLayout();
//            if (maxIndexerValue < Math.abs(prot.getCellValue())) {
//                maxIndexerValue = Math.abs(prot.getCellValue());
//            }
//
//        }
//
//        for (String key2 : protList.keySet()) {
//            ComparisonProtein prot = protList.get(key2);
//            int indexer = (int) (prot.getCellValue() / maxIndexerValue * 10.0);
//            indexer = indexer + 10;
//            if (!compProtMap.containsKey(indexer)) {
//                compProtMap.put(indexer, new HashSet<String>());
//            }
//            values[indexer] = (Double) values[indexer] + 1.0;
//            Set<String> protSet = compProtMap.get(indexer);
//            protSet.add(prot.getUniProtAccess());
//            compProtMap.put(indexer, protSet);
//        }
//
//        int z = 0;
//        DataSeries dataSeries = new DataSeries();
//        int counter = -100;
//
//        Object[] upValue = new Double[(values.length)];
//        Object[] downValues = new Double[(values.length)];
//        Object[] notRegValues = new Double[(values.length)];
//        Object[] selectedValues = new Double[(values.length)];
//        values = scaleValues(values, protList.size());
//        for (double d : values) {
//            if (counter < 0) {
//                upValue[z] = 0.0;
//                downValues[z] = d;
//                notRegValues[z] = 0.0;
//
//            } else if (counter == 0) {
//                upValue[z] = 0.0;
//                downValues[z] = 0.0;
//                notRegValues[z] = d;
//            } else {
//                upValue[z] = d;
//                downValues[z] = 0.0;
//                notRegValues[z] = 0.0;
//
//            }
//            selectedValues[z]=0.0;
//            labels[z] = " ";
//            z++;
//            counter = counter + 10;
//        }
//        
//        selectedValues[0] = 5.0;
//        dataSeries.add(downValues);
//        dataSeries.add(upValue);
//
//        dataSeries.add(notRegValues);
//        dataSeries.add(selectedValues);
//
//        labels[0] = "Down -->";
//        labels[10] = "<-- Not Regulated -->";
//        labels[20] = "<-- Up";
//
//        SeriesDefaults seriesDefaults = new SeriesDefaults()
//                .setFillToZero(true)
//                .setRenderer(SeriesRenderers.BAR)
//                .setLineWidth(0.2f).setShadow(false)
//                .setGridBorderWidth(2.5f)
//                .setYaxis(Yaxes.Y);
//        Series series = new Series()
//                .addSeries(new XYseries().setYaxis(Yaxes.Y).setIndex(0).setLabel("").setShowLabel(false).setShadow(false).setDisableStack(false));
//
//        Highlighter highlighter = new Highlighter()
//                .setUseAxesFormatters(true)
//                .setShow(true)
//                .setTooltipFadeSpeed(TooltipFadeSpeeds.FAST)
//                .setDefault(true)
//                .setTooltipMoveSpeed(TooltipMoveSpeeds.FAST)
//                .setFadeTooltip(true)
//                .setShowTooltip(true)
//                .setKeepTooltipInsideChart(true)
//                .setBringSeriesToFront(false)
//                .setTooltipAxes(TooltipAxes.Y_BAR)
//                .setTooltipLocation(TooltipLocations.NORTH)
//                .setShowMarker(false);
//
//        Axes axes = new Axes()
//                .addAxis(new XYaxis(XYaxes.X).setDrawMajorTickMarks(false).setAutoscale(false)
//                        .setBorderColor("#CED8F6").setDrawMajorGridlines(false).setDrawMinorGridlines(false)
//                        .setRenderer(AxisRenderers.CATEGORY).setTickSpacing(1).setTickInterval(1.5f)
//                        .setTicks(new Ticks().add(labels)).setTickRenderer(TickRenderers.CANVAS).setTickSpacing(40)
//                        .setNumberTicks(21)
//                        .setTickOptions(
//                                new CanvasAxisTickRenderer()
//                                .setFontSize("8pt")
//                                .setShowMark(true)
//                                .setShowGridline(false)))
//                .addAxis(
//                        new XYaxis(XYaxes.Y).setAutoscale(true).setMax(100).setTickOptions(new AxisTickRenderer()
//                                .setFormatString("%d" + "%")));
//
//        Grid grid = new Grid().setDrawBorder(false).setBackground("#FFFFFF").setBorderColor("#CED8F6").setGridLineColor("#CED8F6").setShadow(false);
//
//        Options options = new Options()
//                .setSeriesDefaults(seriesDefaults)
//                .setSeries(series)
//                .setAxes(axes)
//                .setSyncYTicks(false)
//                .setHighlighter(highlighter)
//                .setSeriesColors("#50B747", "#cc0000", "#CDE1FF","#466c90")
//                .setAnimate(false)
//                .setAnimateReplot(false)
//                .setStackSeries(true)
//                .setGrid(grid);
//
//        DCharts chart = new DCharts().setDataSeries(dataSeries).setOptions(options);
//        chart.setWidth("90%");
//        chart.setHeight("250px");
//        chart.setMarginRight(30);
//        chart.setEnableChartDataClickEvent(true);
        final ComparisonChart chart = new ComparisonChart(comparison);
        ChartDataClickHandler chartDataClickHandler = new ChartDataClickHandler() {
            private final Map<Integer, Set<String>> localCompProtMap = chart.getCompProtMap();
            @Override
            public void onChartDataClick(ChartDataClickEvent event) {
                Integer i = (int) (long) event.getChartData().getPointIndex();
                System.out.println("chartLayout clicked");
                filterTable(localCompProtMap.get(i), comparisonMap);
                updateProtCountLabel(localCompProtMap.get(i).size());
            }
        };

        chart.setChartDataClickHandler(chartDataClickHandler);
        chartSet.add(chart);

        return chart;

    }
  
    
    

//    private double[] scaleValues(double[] vals, double listSize) {
//        double[] result = new double[vals.length];
//        double min = 0d;
//        double max = listSize;
//        double scaleFactor = max - min;
//        // scaling between [0..1] for starters. Will generalize later.
//        for (int x = 0; x < vals.length; x++) {
//            result[x] = ((vals[x] - min) / scaleFactor) * 100.0;
//            if (result[x] > 0) {
//                result[x] = result[x] + 0.5;
//            }
//        }
//        return result;
//    }

    private Set<String> searchProteins(String keyword) {
        Set<String> subAccessionMap = new HashSet<String>();
        for (String key : accessionMap.keySet()) {
            if (key.trim().contains(keyword.toLowerCase().trim())) {
                subAccessionMap.add(accessionMap.get(key));
            }

        }
        return subAccessionMap;

    }

    private void filterTable(Set<String> accessions, Set<GroupsComparison> comparisonMap) {
        groupsComparisonTable.removeValueChangeListener(QuantProteinsComparisonTable.this);
        this.groupsComparisonTable.removeAllItems();
        Map<String, ComparisonProtein[]> protSetMap = new HashMap<String, ComparisonProtein[]>();
        int compIndex = 0;
        for (GroupsComparison comp : comparisonMap) {
            Map<String, ComparisonProtein> protList = comp.getComparProtsMap();
            for (String key2 : protList.keySet()) {
                ComparisonProtein prot = protList.get(key2);
                if (!accessions.contains(prot.getUniProtAccess())) {
                    continue;
                }

                if (!protSetMap.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                    protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new ComparisonProtein[comparisonMap.size()]);
                }
                ComparisonProtein[] compArr = protSetMap.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                compArr[compIndex] = prot;
                protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), compArr);
            }
            compIndex++;
        }

        int index = 0;
        for (String protAccName : protSetMap.keySet()) {
            int i = 0;
            String protAcc = protAccName.replace("--", "").trim().split(",")[0];
            String protName = protAccName.replace("--", "").trim().split(",")[1];
            Object[] tableRow = new Object[3 + comparisonMap.size()];
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

        if (!comparisonMap.isEmpty()) {
            this.groupsComparisonTable.sort(new String[]{((GroupsComparison) comparisonMap.toArray()[0]).getComparisonHeader()}, new boolean[]{false});
        }
        this.groupsComparisonTable.setSortAscending(false);
        int indexing = 1;
        for (Object id : this.groupsComparisonTable.getItemIds()) {
            Item item = this.groupsComparisonTable.getItem(id);
            item.getItemProperty("Index").setValue(indexing);
            indexing++;
        }
        indexing = 1;

        groupsComparisonTable.setColumnWidth("Index", 33);
        groupsComparisonTable.setColumnWidth("Accession", 60);
        groupsComparisonTable.setColumnWidth("Name", 60);

        for (Object propertyId : groupsComparisonTable.getSortableContainerPropertyIds()) {
            if (propertyId.toString().equalsIgnoreCase("Index") || propertyId.toString().equalsIgnoreCase("Accession") || propertyId.toString().equalsIgnoreCase("Name")) {
                continue;
            }
            groupsComparisonTable.setColumnExpandRatio(propertyId, 0.4f);

        }
        groupsComparisonTable.addValueChangeListener(QuantProteinsComparisonTable.this);
        groupsComparisonTable.setValue(groupsComparisonTable.getItemIds());

    }

    Set<Integer> proteinskeys = new HashSet<Integer>() ;
    Set<CustomExternalLink> lastSelectedProts =new HashSet<CustomExternalLink>(); 
    //start table selection

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (groupsComparisonTable.getValue() != null) {
            proteinskeys.clear();
            proteinskeys.addAll((Set) groupsComparisonTable.getValue());
        } else {
            return;
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
        updateChartsWithSelectedProteins(lastSelectedProts);

    }

    private void updateChartsWithSelectedProteins(Set<CustomExternalLink> accessions) {

        for (ComparisonChart chart : chartSet) {
            chart.updateSelection(accessions);
        }

    
    }
    
    
    
    

}
