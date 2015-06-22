/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.quantdatasetsoverview;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.base.elements.XYseries;
import org.dussan.vaadin.dcharts.base.renderers.MarkerRenderer;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.metadata.TooltipAxes;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
import org.dussan.vaadin.dcharts.metadata.Yaxes;
import org.dussan.vaadin.dcharts.metadata.lines.LinePatterns;
import org.dussan.vaadin.dcharts.metadata.locations.TooltipLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.TickRenderers;
import org.dussan.vaadin.dcharts.metadata.styles.CursorStyles;
import org.dussan.vaadin.dcharts.metadata.styles.MarkerStyles;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Cursor;
import org.dussan.vaadin.dcharts.options.Grid;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.Series;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.renderers.tick.AxisTickRenderer;
import org.dussan.vaadin.dcharts.renderers.tick.CanvasAxisTickRenderer;
import probe.com.model.beans.ComparisonProtein;

/**
 *
 * @author Yehia Farag
 */
public class ProtOverviewLineChart extends HorizontalLayout{
    
     private SeriesDefaults seriesDefaults2;
    private Axes axes2;
    private Options options2;
    private final DCharts chart2;
    private int height;
//    private VerticalLayout overallPlotLayout;
    public ProtOverviewLineChart(ComparisonProtein[] comparisonProteins, int width){

        this.setHeight("100%");
        this.initLineChart();
        this.setWidthUndefined();
        if (comparisonProteins.length == 1) {
            height=110;
        } else {
            height= (comparisonProteins.length * 90) + 20 ;
            
        }
        this.setStyleName(Reindeer.LAYOUT_WHITE);

        DataSeries dataSeries = new DataSeries();
        DataSeries dataSeries2 = dataSeries.newSeries();
        DataSeries dataSeries1 = dataSeries.newSeries();
        for (ComparisonProtein cp : comparisonProteins) {
            if (cp == null) {
                continue;
            }
            if (cp.getCellValue() < 0) {
                dataSeries1.add(cp.getComparison().getComparisonHeader(), cp.getCellValue() * 100);
            }

            dataSeries2.add(cp.getComparison().getComparisonHeader(), cp.getCellValue() * 100);
        }
        this.setHeight(height + "px");

        VerticalLayout ticksLayout = new VerticalLayout();
        ticksLayout.setWidth("80px");
        ticksLayout.setHeight((height - 30) + "px");
        ticksLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        ticksLayout.setSpacing(false);
        ticksLayout.setMargin(new MarginInfo(false, false, false, true));
        this.addComponent(ticksLayout);

        Label upLab = new Label("Up Reg");
        upLab.setStyleName("upregchartlabel");
        upLab.setHeight("30px");
        ticksLayout.addComponent(upLab);
        ticksLayout.setComponentAlignment(upLab, Alignment.TOP_CENTER);
        
//        VerticalLayout subupLab = new VerticalLayout();
//        subupLab.setStyleName(Reindeer.LAYOUT_BLUE);
//        subupLab.setHeight("100%");
//        subupLab.setWidth("20px");
//        ticksLayout.addComponent(subupLab);
//         ticksLayout.setExpandRatio(subupLab, 0.2f);
//        ticksLayout.setComponentAlignment(subupLab, Alignment.BOTTOM_CENTER);
        Label notLab = new Label("Not Reg");
        notLab.setStyleName("notregchartlabel");
        notLab.setHeight("30px");
        ticksLayout.addComponent(notLab);
        ticksLayout.setComponentAlignment(notLab, Alignment.MIDDLE_CENTER);
       
//         VerticalLayout subdownLab = new VerticalLayout();
//        subdownLab.setStyleName(Reindeer.LAYOUT_BLUE);
//        subdownLab.setHeight("100%");
//        ticksLayout.addComponent(subdownLab);
//        ticksLayout.setComponentAlignment(subdownLab, Alignment.BOTTOM_CENTER);
//         ticksLayout.setExpandRatio(subdownLab, 0.2f);

        Label downLab = new Label("Down Reg");
        downLab.setStyleName("downregchartlabel");
        downLab.setHeight("30px");
        ticksLayout.addComponent(downLab);        
        ticksLayout.setComponentAlignment(downLab, Alignment.BOTTOM_CENTER);
        



        chart2 = new DCharts()
                .setDataSeries(dataSeries)
                .setOptions(options2).setMarginLeft(0).setMarginTop(0);
//                    .show();
        this.addComponent(chart2.show());
        chart2.setWidth((width-100)+"px");
        chart2.setHeight("100%");
      
        

}
    
    private void initLineChart() {
        seriesDefaults2 = new SeriesDefaults().setShadow(false).setUseNegativeColors(true);
        Series series = new Series().addSeries(
                new XYseries(Yaxes.Y).
                setShowLine(true).setLinePattern(LinePatterns.DASHED).setShadow(false).setUseNegativeColors(true)
                .setMarkerOptions(
                        new MarkerRenderer().setShadow(false)
                        .setSize(7)
                        .setStyle(MarkerStyles.FILLED_CIRLCE)));
        axes2 = new Axes()
                .addAxis(
                        new XYaxis()
                        .setRenderer(AxisRenderers.CATEGORY).setTickRenderer(TickRenderers.CANVAS).setTickOptions(new CanvasAxisTickRenderer().setFontStretch(0.5f).setFontSize("10px").setFontFamily("Verdana"))
                )
                .addAxis(
                        new XYaxis(XYaxes.Y).setNumberTicks(5).setAutoscale(true).setMax(105).setMin(-105).setShow(false)
                        .setTickOptions(
                                //                                new CanvasAxisTickRenderer().setAngle(45).setFontSize("10px")
                                new AxisTickRenderer()
                                .setFormatString("%d%")
                        //                        
                        ).setShowTicks(false));

        Highlighter highlighter = new Highlighter()
                .setShow(true)
                .setSizeAdjust(10)
                .setTooltipLocation(TooltipLocations.NORTH)
                .setTooltipAxes(TooltipAxes.X)
                .setUseAxesFormatters(false);

        options2 = new Options().setGrid(new Grid().setShadow(false).setBorderColor("lightgray").setBackground("whitesmoke"))
                .setSeriesDefaults(seriesDefaults2).setHighlighter(highlighter)
                .setSeries(series).setCursor(new Cursor().setShow(true).setStyle(CursorStyles.POINTER))
                .setAxes(axes2).setNegativeSeriesColors("green").setSeriesColors("rgb(59, 90, 122)");

//	.addOption(highlighter)
//	.addOption(cursor);
//        Series series = new Series()
//                .addSeries(
//                        new XYseries()
//                        .setLineWidth(2)
//                        .setMarkerOptions(
//                                new MarkerRenderer()
//                                .setStyle(MarkerStyles.DIAMOND)))
////                .addSeries(
////                        new XYseries(Yaxes.Y2).
////                        setShowLine(false)
////                        .setMarkerOptions(
////                                new MarkerRenderer()
////                                .setSize(7)
////                                .setStyle(MarkerStyles.X)))
//               ;
//
//        AxesDefaults axesDefaults = new AxesDefaults()
//                .setUseSeriesColor(true)
//                .setRendererOptions(
//                        new LinearAxisRenderer()
//                        .setAlignTicks(true));
//
//        Axes axes = new Axes()
//                .addAxis(
//                        new XYaxis(XYaxes.Y))
////                .addAxis(
////                        new XYaxis(XYaxes.Y2))
//                ;

//         options2 = new Options()
//                .setSeries(series)
//                .setAxesDefaults(axesDefaults)
//                .setAxes(axes);

    }
    
}
