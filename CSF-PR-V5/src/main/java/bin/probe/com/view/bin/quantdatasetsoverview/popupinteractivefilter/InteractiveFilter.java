package bin.probe.com.view.bin.quantdatasetsoverview.popupinteractivefilter;

import com.vaadin.ui.VerticalLayout;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickEvent;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickHandler;
import org.dussan.vaadin.dcharts.metadata.DataLabels;
import org.dussan.vaadin.dcharts.metadata.LegendPlacements;
import org.dussan.vaadin.dcharts.metadata.SeriesToggles;
import org.dussan.vaadin.dcharts.metadata.locations.LegendLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.LegendRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.options.Grid;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Legend;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.options.Title;
import org.dussan.vaadin.dcharts.renderers.legend.EnhancedLegendRenderer;
import org.dussan.vaadin.dcharts.renderers.series.PieRenderer;
import probe.com.selectionmanager.CSFFilterSelection;

/**
 *
 * @author Yehia Farag
 */
public class InteractiveFilter extends VerticalLayout implements ChartDataClickHandler {

    private final String[] defaultColors = new String[]{"#6EB1CE", "#DBA901", "#7B3C4B", "#04B45F", "#AEB404", "#0AE10E", "#F4FA58", "#FF0040", "#F6D8CE", "#BDBDBD", "#FF8000", "#ffffff"};
    private final String[] tempColor;
    private final boolean[] selection;
    private DCharts chartFilter;
    private final SeriesDefaults seriesDefaults;
    private final Highlighter highlighter;
    private final Set<String> selectionValues = new HashSet<String>();
    private final String filterId;
    private final String selectedColor = "rgb(59, 90, 122)";//#B40404
    private final LocalSelectionManager Filter_Manager;
    private boolean selected = false;
    private final int filterIndex;
    private final  Map<String,List<Integer>> dsIndexesMap ;
    private int[] filteredDatasetsIndexes;

    public void selectionChanged(int[] indexes, boolean[] activeFilters) {

//        if(type.equalsIgnoreCase("Disease_Groups_Level")){
        if (!selected) {
//            QuantDatasetObject[] studiesSet = Filter_Manager.getFilteredDatasetsList();
//            boolean[] activeFilters = Filter_Manager.getActiveFilters();/      

            int onlyFilter = 0;
            if (activeFilters[filterIndex]) {
                for (boolean b : activeFilters) {
                    if (b) {
                        onlyFilter++;
                    }
                }

            }

            int[] tempValues = null;
            if (onlyFilter == 1) {
                tempValues = values;

            } else {                

                Map<String, Integer> label_values = new HashMap<String, Integer>();
                for (String label : dsIndexesMap.keySet()) {
                    
                    List<Integer> list = dsIndexesMap.get(label);
                    for (int dsIndx : indexes) {
                        if (list.contains(dsIndx)) {
                            if (!label_values.containsKey(label)) {
                                label_values.put(label, 0);

                            }
                            int x = label_values.get(label);
                            x++;
                            label_values.put(label, x);
                        }
                    }

                }

                tempValues = new int[values.length];
                System.arraycopy(new int[tempValues.length], 0, tempValues, 0, tempValues.length);
//                for (QuantDatasetObject study : studiesSet) {
                for (int x = 0; x < labels.length; x++) {
                    if (label_values.containsKey(labels[x])) {
                        tempValues[x] = label_values.get(labels[x]); //                        if (study.getProperty(filterId).toString().equalsIgnoreCase(labels[x])) {
                    }//                            tempValues[x] = tempValues[x] + 1;
//
//                        }
                }

//                }
            }

            dataSeries = new DataSeries();
            for (int z = 0; z < labels.length; z++) {
                dataSeries.newSeries().add(labels[z], tempValues[z]);

            }

        }
        addCUpdatedChart();
        chartFilter.show();
        selected = false;
//        }
    }

    public boolean isActive() {
        return active;
    }
    private boolean active;
    private Options options;
    private DataSeries dataSeries;
    private final String[] labels;
    private final int[] values;
    private final Legend legend;
    private final PieRenderer renderer;

    public InteractiveFilter(List<Object> dataList, String filterId, int filterIndex, LocalSelectionManager Local_Filter_Manager,  Map<String,List<Integer>> dsIndexesMap ) {

        this.filterIndex = filterIndex;
        this.Filter_Manager = Local_Filter_Manager;
        this.filterId = filterId;
        this.dsIndexesMap = dsIndexesMap;
        this.setMargin(true);

        Map<Object, Integer> intFilterMap = new TreeMap<Object, Integer>();
        for (Object object : dataList) {
            if (intFilterMap.containsKey(object)) {
                int val = intFilterMap.get(object);
                val++;
                intFilterMap.put(object, val);

            } else {
                intFilterMap.put(object, 1);
            }

        }
        this.labels = new String[intFilterMap.size()];
        this.values = new int[intFilterMap.size()];

        int x = 0;
        for (Object object : intFilterMap.keySet()) {
            labels[x] = object.toString().trim() + "";
            if (labels[x].equalsIgnoreCase("Not Available")) {
                defaultColors[x] = "#B40404";
            }
            values[x] = intFilterMap.get(object);
            x++;
        }

        tempColor = new String[values.length];
        System.arraycopy(defaultColors, 0, tempColor, 0, tempColor.length);
        selection = new boolean[values.length];

        legend = new Legend().setShowLabels(true).setBorder("solid 0 #ffffff").setShow(true)
                .setRenderer(LegendRenderers.TABLE).setPreDraw(true)
                .setRendererOptions(
                        new EnhancedLegendRenderer()
                        .setSeriesToggle(SeriesToggles.FAST)
                        .setSeriesToggleReplot(false).setSeriesToggle(false).setNumberRows(2))
                .setPlacement(LegendPlacements.INSIDE).setLocation(LegendLocations.SOUTH);

        dataSeries = new DataSeries();
        for (int z = 0; z < labels.length; z++) {
            selection[z] = Boolean.FALSE;
            dataSeries.newSeries().add(labels[z], values[z]);

        }
        legend.setLabels(labels);
        renderer = new PieRenderer().setDataLabelFormatString("<b><i><span style='color:#ffffff;'>%d</span></i></b> ").setFill(true).setDataLabels(DataLabels.VALUE).setSliceMargin(3).setShowDataLabels(true);

        seriesDefaults = new SeriesDefaults()
                .setRenderer(SeriesRenderers.PIE).setFillAndStroke(true).setShadow(false).setFillColor("#ffffff").setDisableStack(false)
                .setRendererOptions(renderer
                );

        highlighter = new Highlighter()
                .setShow(true)
                .setShowTooltip(true)
                .setTooltipAlwaysVisible(true)
                .setKeepTooltipInsideChart(true);

        options = new Options().setSortData(false).setTitle(new Title("<strong>" + Local_Filter_Manager.getFilterTitle(filterId) + "</strong>").setFontSize("13px").setEscapeHtml(false))
                .setLegend(legend).setStackSeries(true)
                .setGrid(new Grid().setDrawBorder(false).setBackground("#ffffff").setShadow(false))
                .setSeriesDefaults(seriesDefaults).setAnimate(true).setSeriesColors(tempColor)
                .setFontFamily("Verdana")
                .setHighlighter(highlighter);

        //init chart
        this.Filter_Manager.registerFilter(InteractiveFilter.this);
        addCUpdatedChart();

    }
    
    public void removeFilterValue(String value){
        int x = 0;
        for(;x<labels.length;x++){
        if(value.equalsIgnoreCase(labels[x]))
            break;   
        
        }
            
      
            selected = true;
            String filterColor = "";
           
            boolean b = selection[x];
            filterColor = defaultColors[x];
            
            if (b) {
                selectionValues.remove(labels[x]);
                tempColor[x] = defaultColors[x];
                selection[x] = false;
            } else if (!selection[x]) {
                selectionValues.add(labels[x]);
                tempColor[x] = selectedColor;
                selection[x] = true;

            }
            active = !selectionValues.isEmpty();
            CSFFilterSelection selectionEvent = new CSFFilterSelection(filterId, selectionValues, active, filterIndex, filterColor,"interactivefilter",null);
            Filter_Manager.updateSelection(selectionEvent,false);
      
    
    
    }
    
    @Override
    public void onChartDataClick(ChartDataClickEvent event) {

        if (event.getChartData().getPointIndex() != null) {
            selected = true;
            String filterColor = "";
            int x = (int) (long) event.getChartData().getSeriesIndex();
            boolean b = selection[x];
            filterColor = defaultColors[x];
            event = null;
            if (b) {
                selectionValues.remove(labels[x]);
                tempColor[x] = defaultColors[x];
                selection[x] = false;
            } else if (!selection[x]) {
                selectionValues.add(labels[x]);
                tempColor[x] = selectedColor;
                selection[x] = true;

            }
            active = !selectionValues.isEmpty();
            
            Set<Integer> fullSelectionIndexes = new HashSet<Integer>();
          
            for(String key:selectionValues){
            fullSelectionIndexes.addAll(dsIndexesMap.get(key));
                    
            }  
            int i=0;
            int[] dsIndex = new int[fullSelectionIndexes.size()];
             for(int key:fullSelectionIndexes){
            dsIndex[i] = key;
                    i++;
            }
            CSFFilterSelection selectionEvent = new CSFFilterSelection(filterId, selectionValues, active, filterIndex, filterColor,"interactivefilter",dsIndex);
            Filter_Manager.updateSelection(selectionEvent,true);
        }

    }

    public void showChart() {
        chartFilter.show();

    }

    public boolean[] getSelection() {
        return selection;
    }

    public Set<String> getSelectionValues() {
        return selectionValues;
    }

    private void addCUpdatedChart() {
        if (chartFilter != null) {
            chartFilter.removeHandler(this);
            if (chartFilter.isAttached()) {
                chartFilter.detach();
            }
            chartFilter = null;
        }
        this.removeAllComponents();
        chartFilter = this.updateChart();
        chartFilter.addHandler(this);
        this.addComponent(chartFilter);

    }

    private DCharts updateChart() {
        DCharts updatedchartFilter = new DCharts();
        updatedchartFilter.setEnableChartDataMouseEnterEvent(true);
        updatedchartFilter.setEnableChartDataMouseLeaveEvent(true);
        updatedchartFilter.setEnableChartDataClickEvent(true);
        options.setSeriesColors(tempColor);
        updatedchartFilter = updatedchartFilter.setDataSeries(dataSeries)
                .setOptions(options);
        return updatedchartFilter;

    }
    public void addChartDataClickHandler(ChartDataClickHandler chartDataClickHandler) {
        chartFilter.addHandler(chartDataClickHandler);

    }
    
    public String getFilterId() {
        return filterId;
    }

}
