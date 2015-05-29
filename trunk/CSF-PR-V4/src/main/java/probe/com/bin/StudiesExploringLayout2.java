package probe.com.bin;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MultiSelectMode;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.PointLabels;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.metadata.DataLabels;
import org.dussan.vaadin.dcharts.metadata.LegendPlacements;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
import org.dussan.vaadin.dcharts.metadata.directions.BarDirections;
import org.dussan.vaadin.dcharts.metadata.locations.LegendLocations;
import org.dussan.vaadin.dcharts.metadata.locations.PointLabelLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Grid;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Legend;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.renderers.series.BarRenderer;
import org.dussan.vaadin.dcharts.renderers.series.DonutRenderer;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.model.beans.QuantDatasetListObject;
import probe.com.view.core.CSFTable;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.HideOnClickLayout;

/**
 * This is the studies layout include publication filters and publication table
 *
 * @author Yehia Farag
 */
public class StudiesExploringLayout2 extends VerticalLayout implements Property.ValueChangeListener {

    private final VerticalLayout filtersLayout;
    private final Table studiesTable;
    private CustomExternalLink pumedLabel;
    private CustomExternalLink rawDataLabel;
    private int pumedkey;
//    private final CSFTable tableLayout;
    private final QuantDatasetObject[] studiesList;
    final ScheduledExecutorService t = Executors.newSingleThreadScheduledExecutor();

    private final Set<QuantDatasetObject> filteredStudiesList = new HashSet<QuantDatasetObject>();

    private final Map<String, Set<String>> appliedFilterList = new HashMap<String, Set<String>>();
    private final boolean[] activeHeaders;
    private final boolean[] activeFilters;

    public StudiesExploringLayout2(MainHandler handler) {
        QuantDatasetListObject studies = handler.getQuantDatasetListObject();
        this.studiesList = studies.getQuantDatasetList();
        activeHeaders = studies.getActiveHeaders();
        activeFilters = handler.getActiveFilters();
        filteredStudiesList.addAll(Arrays.asList(studiesList));
        this.setMargin(true);
        this.setSpacing(true);
        filtersLayout = new VerticalLayout();
        filtersLayout.addComponent(initFiltersLayout());    //init level 1 filters
        HideOnClickLayout Level1FilterLayout = new HideOnClickLayout("Explore Data", filtersLayout, null);
        this.addComponent(Level1FilterLayout);
        studiesTable = new Table();
//        tableLayout = new CSFTable(studiesTable, "Studies", studiesList.size() + "/" + studiesList.size(), null, null, null);
        initStudiesTable();
//        this.addComponent(tableLayout);
    }

//    private void filterTable() {
//        if (appliedFilterList.isEmpty()) {
//            updateTableRecords(studiesList);
//            return;
//        }
//        Set<DatasetObject> filteredList = new HashSet<DatasetObject>();
//        Set<DatasetObject> tempFilteredList = new HashSet<DatasetObject>();
//        tempFilteredList.addAll(studiesList);
//        for (String filter : appliedFilterList.keySet()) {
//
//            if (filter.equalsIgnoreCase("FIlter1")) {
//                for (QuantDatasetObject pb : tempFilteredList) {
//                    if (appliedFilterList.get(filter).contains(pb.getDiseaseGroups())) {
//                        filteredList.add(pb);
//                    }
//
//                }
//            } else if (filter.equalsIgnoreCase("FIlter2")) {
//                for (QuantDatasetObject pb : tempFilteredList) {
//                    if (appliedFilterList.get(filter).contains(pb.getYear() + "")) {
//                        filteredList.add(pb);
//                    }
//
//                }
//            }
//            tempFilteredList.clear();
//            tempFilteredList.addAll(filteredList);
//            filteredList.clear();
//        }
//
//        updateTableRecords(tempFilteredList);
//
//    }

    private VerticalLayout initFiltersLayout() {

        VerticalLayout tfiltersLayout = new VerticalLayout();
        tfiltersLayout.setWidth("100%");
        tfiltersLayout.setSpacing(true);
        int compCounter = 0;

//        DataSeries dataSeries = new DataSeries();
//
////                
////                
//                dataSeries.newSeries()
//                .add("Diseae Group 1", 18)
//                .add("Diseae Group 2", 17);
//        
//        
//        
//        
//        
//        
//        dataSeries.newSeries()
//                .add("Raw Data Available", 6)
//                .add("Raw Data Not Available ", 29);
//        
//        
//        dataSeries.newSeries()
//                .add("2012", 18)
//                .add("2014 ", 17);
//        
//        
//         dataSeries.newSeries()
//                .add("Discovery", 13)
//                .add("Verifacation", 22);
//        
//        
//        
//        
//        
//        
//        dataSeries.newSeries()
//                .add("Serum", 3)
//                .add("CSF", 32);
//        
//        
//        
//        dataSeries.newSeries()
//                .add("Mass Spectrometry", 23)
//                .add("Elisa ", 35)
//                .add("Not Available ", 4);
//        
//        
//        
//         dataSeries.newSeries()
//                .add("Trypsin", 21)
//                .add("None", 2)
//         .add("Not Available ", 12);
//        
//        
//        
//        
//        
//        
//        dataSeries.newSeries()
//                .add("Targeted", 22)
//                .add("Shotgun ", 11)
//                .add("Not Available", 2);
//       
//        
//        
        
        
        
        
        
        DataSeries dataSeries = new DataSeries();
dataSeries.newSeries()
	.add(2, 1)
	.add(4, 2)
	.add(6, 3)
	.add(3, 4);
dataSeries.newSeries()
	.add(5, 1)
	.add(1, 2)
	.add(3, 3)
	.add(4, 4);
dataSeries.newSeries()
	.add(4, 1)
	.add(7, 2)
	.add(1, 3)
	.add(2, 4);

SeriesDefaults seriesDefaults = new SeriesDefaults()
	.setRenderer(SeriesRenderers.BAR)
	.setPointLabels(
		new PointLabels()
			.setShow(true)
			.setLocation(PointLabelLocations.EAST)
			.setEdgeTolerance(-15))
	.setShadowAngle(135)
	.setRendererOptions(
		new BarRenderer()
			.setBarDirection(BarDirections.HOTIZONTAL));

Axes axes = new Axes()
	.addAxis(
		new XYaxis(XYaxes.Y)
			.setRenderer(AxisRenderers.CATEGORY));

Options options = new Options()
	.setSeriesDefaults(seriesDefaults)
	.setAxes(axes);

DCharts chart = new DCharts()
	.setDataSeries(dataSeries)
	.setOptions(options)
	.show();
        
        
        
        
        
        
        
        
        
        
//        
//        
//        
//        
//        
//        
//        
//        
//        SeriesDefaults seriesDefaults = new SeriesDefaults()
//                .setRenderer(SeriesRenderers.DONUT)
//                .setRendererOptions(
//                        new DonutRenderer()
//                        .setSliceMargin(3)
//                        .setStartAngle(-90)
//                        .setShowDataLabels(true)
//                        .setDataLabels(DataLabels.VALUE)
//                );
//
//        Highlighter highlighter = new Highlighter()
//                .setShow(true)
//                .setShowTooltip(true)
//                .setTooltipAlwaysVisible(true)
//                .setKeepTooltipInsideChart(true);
//
//        Legend legend = new Legend()
//                .setShow(true)
//                .setPlacement(LegendPlacements.OUTSIDE_GRID)
//                .setLocation(LegendLocations.WEST);
//
//        Options options = new Options()
//                .setSeriesDefaults(seriesDefaults)
//                .setLegend(legend)
//                .setGrid(new Grid().setDrawBorder(false).setBackground("#ffffff").includeDefault().setShadow(false))
//                //                .setSeriesDefaults(seriesDefaults).setAnimate(false).setSeriesColors(tempColor)
//                .setFontFamily("Arial, Helvetica, Tahoma, Verdana, sans-serif").setFontSize("12px").setTextColor("black")
//                .setHighlighter(highlighter);;
//
//        DCharts chart = new DCharts()
//                .setDataSeries(dataSeries)
//                .setOptions(options)
//                .show();
        chart.setHeight("500px");
        tfiltersLayout.addComponent(chart);
        return tfiltersLayout;

    }

    private void generateSerious(Set<Object> dataList, String filterId, DataSeries dataSeries) {
        String[] labels = null;
        int[] values = null;

        Map<Object, Integer> intFilterMap = new HashMap<Object, Integer>();
        for (Object object : dataList) {

            if (intFilterMap.containsKey(object)) {
                int val = intFilterMap.get(object);
                val++;
                intFilterMap.put(object, val);

            } else {
                intFilterMap.put(object, 1);
            }

        }
        labels = new String[intFilterMap.size()];
        values = new int[intFilterMap.size()];

        int x = 0;
        for (Object object : intFilterMap.keySet()) {
            labels[x] = object.toString() + "";
            values[x] = intFilterMap.get(object);
            x++;
        }
        for (int i = 0; i < labels.length; x++) {
            dataSeries.add(labels[i], values[i]);
        }

    }
    
    private void initStudiesTable() {
        studiesTable.setSelectable(true);
        studiesTable.setColumnReorderingAllowed(true);
        studiesTable.setColumnCollapsingAllowed(true);
        studiesTable.setDragMode(Table.TableDragMode.NONE);
        studiesTable.setMultiSelect(true);
        studiesTable.setMultiSelectMode(MultiSelectMode.DEFAULT);
//        
        studiesTable.setImmediate(true); // react at once when something is selected
        studiesTable.setWidth("100%");
        studiesTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        studiesTable.addContainerProperty("PumedID", CustomExternalLink.class, null);
        studiesTable.setColumnCollapsed("PumedID", !activeHeaders[0]);

        studiesTable.addContainerProperty("#Files", Integer.class, null, "#Files", null, Table.Align.RIGHT);
        studiesTable.setColumnCollapsed("#Files", !activeHeaders[2]);

        studiesTable.addContainerProperty("#Identified Proteins", Integer.class, null, "#Identified Proteins", null, Table.Align.RIGHT);
        studiesTable.setColumnCollapsed("#Identified Proteins", !activeHeaders[4]);

        studiesTable.addContainerProperty("#Quantified Proteins ", Integer.class, null, "#Quantified Proteins", null, Table.Align.RIGHT);
        studiesTable.setColumnCollapsed("#Quantified Proteins", !activeHeaders[5]);

        studiesTable.addContainerProperty("Disease group ", String.class, null);
        studiesTable.setColumnCollapsed("Disease group", !activeHeaders[1]);

        studiesTable.addContainerProperty("Raw Data", CustomExternalLink.class, null);
        studiesTable.setColumnCollapsed("Raw Data", !activeHeaders[6]);

        studiesTable.addContainerProperty("Year", Integer.class, null, "Year", null, Table.Align.RIGHT);
        studiesTable.setColumnCollapsed("Year", !activeHeaders[3]);

        studiesTable.addContainerProperty("typeOfStudy", String.class, null, "Study Type", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("typeOfStudy", !activeHeaders[7]);

        studiesTable.addContainerProperty("sampleType", String.class, null, "Sample Type", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("sampleType", !activeHeaders[8]);

        studiesTable.addContainerProperty("sampleMatching", String.class, null, "Sample Matching", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("sampleMatching", !activeHeaders[9]);

        studiesTable.addContainerProperty("shotgunTargeted", String.class, null, "Shotgun/Targeted", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("shotgunTargeted", !activeHeaders[13]);

        studiesTable.addContainerProperty("technology", String.class, null, "Technology", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("technology", !activeHeaders[10]);

        studiesTable.addContainerProperty("analyticalApproach", String.class, null, "Analytical Approach", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("analyticalApproach", !activeHeaders[11]);

        studiesTable.addContainerProperty("enzyme", String.class, null, "Enzyme", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("enzyme", !activeHeaders[12]);

        studiesTable.addContainerProperty("quantificationBasis", String.class, null, "Quant Basis", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("quantificationBasis", !activeHeaders[14]);

        studiesTable.addContainerProperty("quantBasisComment", String.class, null, "Quant Basis Comment", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("quantBasisComment", !activeHeaders[15]);

        studiesTable.addContainerProperty("patientsGroup1Number", Integer.class, null, "#Patients Gr.I", null, Table.Align.RIGHT);
        studiesTable.setColumnCollapsed("patientsGroup1Number", !activeHeaders[16]);

        studiesTable.addContainerProperty("patientsGroup2Number", Integer.class, null, "#Patients Gr.II", null, Table.Align.RIGHT);
        studiesTable.setColumnCollapsed("patientsGroup2Number", !activeHeaders[17]);

        studiesTable.addContainerProperty("normalization_strategy", String.class, null, "Normalization Strategy", null, Table.Align.LEFT);
        studiesTable.setColumnCollapsed("normalization_strategy", !activeHeaders[18]);

        updateTableRecords(filteredStudiesList);
        studiesTable.setMultiSelectMode(MultiSelectMode.DEFAULT);

    }

    public void updateTableRecords(Set<QuantDatasetObject> updatedStudiesList) {
        try {
            VaadinSession.getCurrent().getLockInstance().lock();
            studiesTable.removeAllItems();
            if (updatedStudiesList.size() != studiesList.length) {
                filteredStudiesList.clear();
                filteredStudiesList.addAll(updatedStudiesList);
            } else {
                filteredStudiesList.clear();
//                filteredStudiesList.addAll(studiesList);
            }

//            tableLayout.updateCounter(updatedStudiesList.size() + "/" + studiesList.length);
            int index = 0;

            for (QuantDatasetObject pb : updatedStudiesList) {
                CustomExternalLink rawDatalink = null;
                if (pb.getRawDataUrl().equalsIgnoreCase("NO")) {
                    rawDatalink = null;
                } else {
                    rawDatalink = new CustomExternalLink("Get Raw Data", pb.getRawDataUrl()); 
                    rawDatalink.setDescription("Link To Raw Data");
                }
                Integer quantProyNum = pb.getQuantifiedProteinsNumber();
                if (quantProyNum.intValue() == -1000000000) {
                    quantProyNum = null;
                }

                Integer patGr1Num = pb.getPatientsGroup1Number();
                if (patGr1Num.intValue() == -1000000000) {
                    patGr1Num = null;
                }
                Integer patGr2Num = pb.getPatientsGroup2Number();
                if (patGr2Num.intValue() == -1000000000) {
                    patGr2Num = null;
                }
                CustomExternalLink pumedID = new CustomExternalLink(pb.getPumedID(), "http://www.ncbi.nlm.nih.gov/pubmed/" + pb.getPumedID());
                studiesTable.addItem(new Object[]{index, pumedID, pb.getFilesNumber(), pb.getIdentifiedProteinsNumber(), quantProyNum, pb.getDiseaseGroups(), rawDatalink, pb.getYear(), pb.getTypeOfStudy(), pb.getSampleType(), pb.getSampleMatching(), pb.getTechnology(), pb.getAnalyticalApproach(), pb.getEnzyme(), pb.getShotgunTargeted(), pb.getQuantificationBasis(), pb.getQuantBasisComment(), patGr1Num, patGr2Num, pb.getNormalizationStrategy()}, index);
                index++;
            }
        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (studiesTable.getValue() != null) {
            pumedkey = (Integer) studiesTable.getValue();
        } else {
            return;
        }
        if (pumedLabel != null) {
            pumedLabel.rePaintLable("black");
            rawDataLabel.rePaintLable("black");
        }
        final Item item = studiesTable.getItem(pumedkey);
        pumedLabel = (CustomExternalLink) item.getItemProperty("PumedID").getValue();
        rawDataLabel = (CustomExternalLink) item.getItemProperty("Raw Data").getValue();
        pumedLabel.rePaintLable("white");
        rawDataLabel.rePaintLable("white");
    }

}
