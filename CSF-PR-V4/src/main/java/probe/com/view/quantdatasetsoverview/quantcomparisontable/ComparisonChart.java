/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.quantdatasetsoverview.quantcomparisontable;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.PointLabels;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.base.elements.XYseries;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickHandler;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterEvent;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterHandler;
import org.dussan.vaadin.dcharts.metadata.TooltipAxes;
import org.dussan.vaadin.dcharts.metadata.TooltipFadeSpeeds;
import org.dussan.vaadin.dcharts.metadata.TooltipMoveSpeeds;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
import org.dussan.vaadin.dcharts.metadata.Yaxes;
import org.dussan.vaadin.dcharts.metadata.locations.PointLabelLocations;
import org.dussan.vaadin.dcharts.metadata.locations.TooltipLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.TickRenderers;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Grid;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.Series;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.renderers.tick.AxisTickRenderer;
import org.dussan.vaadin.dcharts.renderers.tick.CanvasAxisTickRenderer;
import probe.com.model.beans.ComparisonProtein;
import probe.com.model.beans.GroupsComparison;

/**
 *
 * @author Yehia Farag
 */
public class ComparisonChart extends GridLayout {

    private final Map<Integer, Set<String>> compProtMap;
    private final Object[] upValue, midUpValues;
    private final Object[] downValues, midDownValues;
    private final Object[] notRegValues;
    private final String[] downReglabels, midDownlabels, notReglabels, midUplabels, upReglabels;
    private final Options options;
    private final Series series;
    private DCharts chart;
    private final DataSeries dataSeries;
    private ChartDataClickHandler chartDataClickHandler;
    private final ChartDataMouseEnterHandler localChartDataClickHandler;
    private final VerticalLayout closeCompariosonBtn, spacer1,spacer2;
    private final GroupsComparison comparison;
    private final  Label title;
    private String[] defaultSerColours = new String[]{"#50B747", "#8ECCA3", "#CDE1FF", "#CC707F", "#cc0000"};
    private  String []serColor = new String[]{"#E5E5E5","#E5E5E5","#E5E5E5","#E5E5E5","#E5E5E5"};

    public void setChartDataClickHandler(ChartDataClickHandler chartDataClickHandler) {
        this.chartDataClickHandler = chartDataClickHandler;
        chart.addHandler(chartDataClickHandler);
    }

    public Map<Integer, Set<String>> getCompProtMap() {
        return compProtMap;
    }

    public VerticalLayout getCloseCompariosonBtn() {
        return closeCompariosonBtn;
    }

    public GroupsComparison getComparison() {
        return comparison;
    }

    public ComparisonChart(GroupsComparison comparison) {
        this.setStyleName("lightborder");
        this.setWidth("99%");
        this.setHeight("100%");
        this.setMargin(true);
//        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setColumns(3);
        this.setRows(2);
        this.setColumnExpandRatio(0, 20f);
        this.setColumnExpandRatio(1, 360f);
        this.setColumnExpandRatio(2, 20f);
        spacer1 = new VerticalLayout();
        spacer1.setWidth("20px");
        spacer1.setHeight("100%");
        spacer1.setStyleName(Reindeer.LAYOUT_WHITE);
         spacer2 = new VerticalLayout();
        spacer2.setWidth("200px");
        spacer2.setHeight("20px");
        spacer2.setStyleName(Reindeer.LAYOUT_WHITE);
        Label yAxisLabel = new Label("<center><b><font size='1'>Proteins Percentage (total# "+comparison.getComparProtsMap().size()+")"+ "</font></b></center>");
        yAxisLabel.setContentMode(ContentMode.HTML);
        spacer2.addComponent(yAxisLabel);
        spacer2.setStyleName("rotatedlabel");
        title = new Label("<center><b><font size='1'>"+comparison.getComparisonHeader()+" - #Studies ("+comparison.getDatasetIndexes().length+")"+"</font></b(></center>");
        title.setContentMode(ContentMode.HTML);
        title.setWidth("100%");
        
//        this.setMargin(new MarginInfo(false, false, false, true));
        this.comparison = comparison;
        Map<String, ComparisonProtein> protList = comparison.getComparProtsMap();
        compProtMap = new HashMap<Integer, Set<String>>();
        double[] values = new double[5];
        Object[] labels = new String[5];
        double maxIndexerValue = 0.0;
        downReglabels = new String[(values.length)];
        midDownlabels = new String[(values.length)];
        notReglabels = new String[(values.length)];
        midUplabels = new String[values.length];
        upReglabels = new String[(values.length)];
        //init values 
        for (String key2 : protList.keySet()) {
            ComparisonProtein prot = protList.get(key2);
            prot.updateLabelLayout();
            if (maxIndexerValue < Math.abs(prot.getCellValue())) {
                maxIndexerValue = Math.abs(prot.getCellValue());
            }

        }
        for (String key2 : protList.keySet()) {
            ComparisonProtein prot = protList.get(key2);
            int indexer = (int) (prot.getCellValue() / maxIndexerValue * 10.0);
//            indexer = indexer + 10;
            if (indexer == 10) {
                indexer = 4;
            } else if (indexer < 10 && indexer > 0) {
                indexer = 3;
            } else if (indexer == 0) {
                indexer = 2;
            } else if (indexer < 0 && indexer > -10) {
                indexer = 1;
            } else if (indexer == -10) {
                indexer = 0;
            }

            if (!compProtMap.containsKey(indexer)) {
                compProtMap.put(indexer, new HashSet<String>());
            }
            values[indexer] = (Double) values[indexer] + 1.0;
            Set<String> protSet = compProtMap.get(indexer);
            protSet.add(prot.getUniProtAccess());
            compProtMap.put(indexer, protSet);
        }

        dataSeries = new DataSeries();
        upValue = new Double[(values.length)];
        downValues = new Double[(values.length)];
        midUpValues = new Double[(values.length)];
        midDownValues = new Double[(values.length)];
        notRegValues = new Double[(values.length)];
        for (int z = 0; z < values.length; z++) {
            downReglabels[z] = " ";
            midDownlabels[z] = " ";
            notReglabels[z] = " ";
            midUplabels[z] = " ";
            upReglabels[z] = " ";

        }
        int z = 0;
        values = scaleValues(values, protList.size());
        for (double d : values) {
            if (z == 0) {
                downValues[z] = d;
                midDownValues[z] = 0.0;
                notRegValues[z] = 0.0;
                midUpValues[z] = 0.0;
                upValue[z] = 0.0;

            } else if (z == 1) {
                upValue[z] = 0.0;
                midUpValues[z] = 0.0;
                downValues[z] = 0.0;
                midDownValues[z] = d;
                notRegValues[z] = 0.0;
            } else if (z == 2) {
                downValues[z] = 0.0;
                midDownValues[z] = 0.0;
                notRegValues[z] = d;
                midUpValues[z] = 0.0;
                upValue[z] = 0.0;
            } else if (z == 3) {
                downValues[z] = 0.0;
                midDownValues[z] = 0.0;
                midUpValues[z] = d;
                notRegValues[z] = 0.0;
                upValue[z] = 0.0;

            } else if (z == 4) {
                downValues[z] = 0.0;
                midDownValues[z] = 0.0;
                notRegValues[z] = 0.0;
                midUpValues[z] = 0.0;
                upValue[z] = d;
            }
            labels[z] = " ";
            z++;

        }

        dataSeries.add(downValues);
        dataSeries.add(midDownValues);
        dataSeries.add(notRegValues);
        dataSeries.add(midUpValues);
        dataSeries.add(upValue);

        labels[0] = "Down";
        labels[2] = "Not Regulated";
        labels[4] = "Up";

        SeriesDefaults seriesDefaults = new SeriesDefaults()
                .setFillToZero(true)
                .setRenderer(SeriesRenderers.BAR)
//                .setLineWidth(0.15f).setShadow(false)
                .setGridBorderWidth(2.5f)
                .setYaxis(Yaxes.Y);
        series = new Series()
                .addSeries(new XYseries().setYaxis(Yaxes.Y).setIndex(0).setLabel("").setShowLabel(false).setShadow(false).setDisableStack(false).setPointLabels(new PointLabels().setLabels(downReglabels).setXpadding(10).setShow(true).setHideZeros(false).setStackedValue(true).setLocation(PointLabelLocations.NORTH).setEdgeTolerance(-15).setEscapeHTML(false)))
                .addSeries(new XYseries().setYaxis(Yaxes.Y).setIndex(1).setLabel("").setShowLabel(false).setShadow(false).setDisableStack(false).setPointLabels(new PointLabels().setLabels(midDownlabels).setXpadding(10).setShow(true).setHideZeros(false).setStackedValue(true).setLocation(PointLabelLocations.NORTH).setEdgeTolerance(-15).setEscapeHTML(false)))
                .addSeries(new XYseries().setYaxis(Yaxes.Y).setIndex(2).setLabel("").setShowLabel(false).setShadow(false).setDisableStack(false).setPointLabels(new PointLabels().setLabels(notReglabels).setXpadding(10).setShow(true).setHideZeros(false).setStackedValue(true).setLocation(PointLabelLocations.NORTH).setEdgeTolerance(-15).setEscapeHTML(false)))
                .addSeries(new XYseries().setYaxis(Yaxes.Y).setIndex(3).setLabel("").setShowLabel(false).setShadow(false).setDisableStack(false).setPointLabels(new PointLabels().setLabels(midUplabels).setXpadding(10).setShow(true).setHideZeros(false).setStackedValue(true).setLocation(PointLabelLocations.NORTH).setEdgeTolerance(-15).setEscapeHTML(false)))
                .addSeries(new XYseries().setYaxis(Yaxes.Y).setIndex(4).setLabel("").setShowLabel(false).setShadow(false).setDisableStack(false).setPointLabels(new PointLabels().setLabels(upReglabels).setXpadding(10).setShow(true).setHideZeros(false).setStackedValue(true).setLocation(PointLabelLocations.NORTH).setEdgeTolerance(-15).setEscapeHTML(false)));

        Highlighter highlighter = new Highlighter()
                .setUseAxesFormatters(true)
                .setShow(true)
                .setTooltipFadeSpeed(TooltipFadeSpeeds.FAST)
                .setDefault(true)
                .setTooltipMoveSpeed(TooltipMoveSpeeds.FAST)
                .setFadeTooltip(true)
                .setShowTooltip(true)
                .setKeepTooltipInsideChart(true)
                .setBringSeriesToFront(false)
                .setTooltipAxes(TooltipAxes.Y_BAR)
                .setTooltipLocation(TooltipLocations.NORTH)
                .setShowMarker(false);
       

        Axes axes = new Axes().addAxis(new XYaxis(XYaxes.X).setDrawMajorTickMarks(false).setAutoscale(false).setShowLabel(false)
                        .setBorderColor("#CED8F6").setDrawMajorGridlines(false).setDrawMinorGridlines(false)
                        .setRenderer(AxisRenderers.CATEGORY).setTickSpacing(1).setTickInterval(1.5f)
                        .setTicks(new Ticks().add(labels)).setTickRenderer(TickRenderers.CANVAS).setTickSpacing(40)
                        .setNumberTicks(5)
                        .setTickOptions(
                                new CanvasAxisTickRenderer()
                                .setFontSize("8pt")
                                .setShowMark(true)
                                .setShowGridline(false)));
        
        
//        System.out.println("ang before "+labelRend.getAngle());
//        labelRend.setAngle(90);
//        System.out.println("ang after "+labelRend.getAngle());
        
                axes.addAxis(
                        new XYaxis(XYaxes.Y).setShowLabel(false).setAutoscale(true).setMax(80).setTickOptions(new AxisTickRenderer()
                                .setFormatString("%d" + "%")));

        Grid grid = new Grid().setDrawBorder(false).setBackground("#FFFFFF").setBorderColor("#CED8F6").setGridLineColor("#CED8F6").setShadow(false);

    
        options = new Options()
                .setSeriesDefaults(seriesDefaults)
                .setSeries(series)
                .setAxes(axes)
                .setSyncYTicks(false)
                .setHighlighter(highlighter)
                .setSeriesColors(defaultSerColours)
                .setAnimate(false)
                .setAnimateReplot(false)
                .setStackSeries(true)
                .setGrid(grid);
        chart = new DCharts().setDataSeries(dataSeries).setOptions(options).show();        
        chart.setMarginRight(100);
        chart.setEnableChartDataClickEvent(true);
        chart.setEnableChartDataMouseEnterEvent(true);
      

        closeCompariosonBtn = new VerticalLayout();
        closeCompariosonBtn.setWidth("20px");
        closeCompariosonBtn.setHeight("20px");
        closeCompariosonBtn.setStyleName("closebtn");  
        
        this.addComponent(spacer1,0,0);
        this.addComponent(title,1,0);
        this.setComponentAlignment(title, Alignment.TOP_CENTER);
        this.addComponent(closeCompariosonBtn,2,0);
        this.setComponentAlignment(closeCompariosonBtn, Alignment.TOP_RIGHT);
        this.addComponent(spacer2, 0, 1);

        this.addComponent(chart, 1, 1);
        this.setComponentAlignment(chart, Alignment.TOP_CENTER);
        chart.setWidth("100%");
        chart.setHeight("210px");

        localChartDataClickHandler = new ChartDataMouseEnterHandler() {

            @Override
            public void onChartDataMouseEnter(ChartDataMouseEnterEvent event) {
                if (event.getChartData() != null && event.getChartData().getPointIndex() != null) {

                    Integer i = (int) (long) event.getChartData().getPointIndex();
                    serColor = new String[]{"#E5E5E5", "#E5E5E5", "#E5E5E5", "#E5E5E5", "#E5E5E5"};//{"#B9E2B5","#D2EBDA","#E6F0FF","#EBC6CC","#EB9999"};

                    serColor[i] = defaultSerColours[i];
                    System.err.println("at " + i + "  value " + serColor[i]);
                }

//                options.setSeriesColors(serColor);
            }
        };
        chart.addHandler(localChartDataClickHandler);

    }

    private double[] scaleValues(double[] vals, double listSize) {
        double[] result = new double[vals.length];
        double min = 0d;
        double max = listSize;
        double scaleFactor = max - min;
        // scaling between [0..1] for starters. Will generalize later.
        for (int x = 0; x < vals.length; x++) {
            result[x] = ((vals[x] - min) / scaleFactor) * 100.0;
            if (result[x] > 0) {
                result[x] = result[x] + 0.5;
            }
        }
        return result;
    }

    private final Set<Integer> lastselectedIndex = new HashSet<Integer>();
    private final Set<Integer> lastLabelIndex = new HashSet<Integer>();
    private String lastTotalUpIndexer = "", lastTotalMidUpIndexer = "", lastTotalNotIndexer = "", lastTotalmidDownIndexer = "", lastTotalDownIndexer = "";

    public void updateSelection(Set<String> accessions, boolean tableSelection, boolean sortedChart) {

        if (!tableSelection) {
            lastTotalUpIndexer = "";
            lastTotalMidUpIndexer = "";
            lastTotalNotIndexer = "";
            lastTotalmidDownIndexer = "";
            lastTotalDownIndexer = "";
            lastLabelIndex.clear();
        }
        List<Integer> selectedIndexes = new ArrayList<Integer>();
        for (int index : compProtMap.keySet()) {
            for (String accession : accessions) {
                if (compProtMap.get(index).contains(accession)) {
                    selectedIndexes.add(index);
                }
            }
        }

        for (int z : lastselectedIndex) {
            if (z == 0) {
                downReglabels[z] = " ";
            } else if (z == 1) {
                midDownlabels[z] = " ";
            } else if (z == 2) {
                notReglabels[z] = " ";
            } else if (z == 3) {
                midUplabels[z] = " ";

            } else if (z == 4) {
                upReglabels[z] = " ";
            }
        }
        lastselectedIndex.clear();

        int[] tdownvalueslabel = new int[downReglabels.length];
        int[] tmiddownvalueslabel = new int[downReglabels.length];
        int[] tnotvalueslabel = new int[notReglabels.length];
        int[] tmidupvalueslabel = new int[upReglabels.length];
        int[] tupvalueslabel = new int[upReglabels.length];
            for (int z : selectedIndexes) {
                if (z == 0) {
                    tdownvalueslabel[z] = tdownvalueslabel[z] + 1;
                } else if (z == 1) {
                    tmiddownvalueslabel[z] = tmiddownvalueslabel[z] + 1;
                } else if (z == 2) {
                    tnotvalueslabel[z] = tnotvalueslabel[z] + 1;
                } else if (z == 3) {
                    tmidupvalueslabel[z] = tmidupvalueslabel[z] + 1;

                } else if (z == 4) {
                    tupvalueslabel[z] = tupvalueslabel[z] + 1;
                }
            }        
        lastselectedIndex.addAll(selectedIndexes);

        for (int x : lastselectedIndex) {

            if (x == 0) {
                if (tableSelection) {
                    downReglabels[x] = "<font  color='blue'>" + tdownvalueslabel[x] + lastTotalDownIndexer + "</font>";

                } else {
                    if (tdownvalueslabel[x] > 0) {
                        lastTotalDownIndexer = "/" + tdownvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    downReglabels[x] = "<font  color='blue'>" + tdownvalueslabel[x] + "</font>";
                }
            } else if (x == 1) {
                if (tableSelection) {
                    midDownlabels[x] = "<font  color='blue'>" + tmiddownvalueslabel[x] + lastTotalmidDownIndexer + "</font>";
                } else {
                    if (tmiddownvalueslabel[x] > 0) {
                        lastTotalmidDownIndexer = "/" + tmiddownvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    midDownlabels[x] = "<font  color='blue'>" + tmiddownvalueslabel[x] + "</font>";
                }
            } else if (x == 2) {
                if (tableSelection) {
                    notReglabels[x] = "<font  color='blue'>" + tnotvalueslabel[x] + lastTotalNotIndexer + "</font>";
                } else {
                    if (tnotvalueslabel[x] > 0) {
                        lastTotalNotIndexer = "/" + tnotvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    notReglabels[x] = "<font  color='blue'> " + tnotvalueslabel[x] + "</font>";

                }

            } else if (x == 3) {
                if (tableSelection) {
                    midUplabels[x] = "<font  color='blue'>" + tmidupvalueslabel[x] + lastTotalMidUpIndexer + "</font>";
                } else {
                    if (tmidupvalueslabel[x] > 0) {
                        lastTotalMidUpIndexer = "/" + tmidupvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    midUplabels[x] = "<font  color='blue'>" + tmidupvalueslabel[x] + "</font>";
                }

            } else if (x == 4) {
                if (tableSelection) {
                    upReglabels[x] = "<font  color='blue'>" + tupvalueslabel[x] + lastTotalUpIndexer + "</font>";
                } else {
                    if (tupvalueslabel[x] > 0) {
                        lastTotalUpIndexer = "/" + tupvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    upReglabels[x] = "<font  color='blue'>" + tupvalueslabel[x] + "</font>";
                }
            }

        }
        for (int x : lastLabelIndex) {
            if (lastselectedIndex.contains(x)) {
                continue;
            }
            if (x == 0) {
                if (tableSelection) {
                    downReglabels[x] = "<font  color='blue'>0" + lastTotalDownIndexer + "</font>";
                }
            } else if (x == 1) {
                if (tableSelection) {
                    midDownlabels[x] = "<font  color='blue'>0" + lastTotalmidDownIndexer + "</font>";
                }
            } else if (x == 2) {
                if (tableSelection) {
                    notReglabels[x] = "<font  color='blue'>0" + lastTotalNotIndexer + "</font>";
                }
            } else if (x == 3) {
                if (tableSelection) {
                    midUplabels[x] = "<font  color='blue'>0" + lastTotalMidUpIndexer + "</font>";
                }
            } else if (x == 4) {
                if (tableSelection) {
                    upReglabels[x] = "<font  color='blue'>0" + lastTotalUpIndexer + "</font>";
                }

            }
        }

        series.getSeries().get(0).getPointLabels().setLabels(downReglabels);
        series.getSeries().get(1).getPointLabels().setLabels(midDownlabels);
        series.getSeries().get(2).getPointLabels().setLabels(notReglabels);
        series.getSeries().get(3).getPointLabels().setLabels(midUplabels);
        series.getSeries().get(4).getPointLabels().setLabels(upReglabels);
        this.options.setSeries(series);
       

        if (!tableSelection) {
            if (sortedChart) {
                this.options.setSeriesColors(serColor);
                this.setStyleName("lightselectedborder");
            } else {
                this.options.setSeriesColors(defaultSerColours);
                this.setStyleName("lightborder");

            }
        }
        
//        chart.setOptions(options);
        chart.removeHandler(chartDataClickHandler);
        chart.removeHandler(localChartDataClickHandler);
        
        this.removeAllComponents();
        chart = null;
        chart = new DCharts().setDataSeries(dataSeries).setOptions(options).show();
       
        chart.setMarginRight(100);
        chart.setEnableChartDataClickEvent(true);
        chart.setEnableChartDataMouseEnterEvent(true);
        chart.addHandler(localChartDataClickHandler);
        chart.addHandler(chartDataClickHandler);
        

        this.addComponent(spacer1,0,0);
         this.addComponent(spacer2,0,1);
        this.addComponent(title,1,0);
        this.setComponentAlignment(title, Alignment.TOP_CENTER);
        this.addComponent(closeCompariosonBtn,2,0);
        this.setComponentAlignment(closeCompariosonBtn, Alignment.TOP_RIGHT);
        this.addComponent(chart,1,1);
        this.setComponentAlignment(chart, Alignment.TOP_LEFT);
        this.setComponentAlignment(chart, Alignment.TOP_CENTER);
        chart.setWidth("100%");
        chart.setHeight("210px");
        
        

    }

    public void resizeChart() {
        chart.removeHandler(chartDataClickHandler);
        chart.removeHandler(localChartDataClickHandler);
        this.removeAllComponents();
        chart = null;
        chart = new DCharts().setDataSeries(dataSeries).setOptions(options).show();
        
        chart.setMarginRight(100);
        chart.setEnableChartDataClickEvent(true);
        chart.setEnableChartDataMouseEnterEvent(true);
        chart.addHandler(localChartDataClickHandler);
        chart.addHandler(chartDataClickHandler);
        
        this.addComponent(spacer1,0,0);
        this.addComponent(spacer2,0,1);
        this.addComponent(title,1,0);
        this.setComponentAlignment(title, Alignment.TOP_CENTER);
        this.addComponent(closeCompariosonBtn,2,0);
        this.setComponentAlignment(closeCompariosonBtn, Alignment.TOP_RIGHT);
        this.addComponent(chart,1,1);
        this.setComponentAlignment(chart, Alignment.TOP_LEFT);
        this.setComponentAlignment(chart, Alignment.TOP_CENTER);
        chart.setWidth("100%");
        chart.setHeight("210px");
    }
    

}
