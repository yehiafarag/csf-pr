package probe.com.view.subview;

import com.vaadin.server.Page;
import com.vaadin.shared.Position;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.base.elements.XYseries;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickEvent;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickHandler;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterEvent;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterHandler;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveEvent;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveHandler;
import org.dussan.vaadin.dcharts.metadata.TooltipAxes;
import org.dussan.vaadin.dcharts.metadata.TooltipFadeSpeeds;
import org.dussan.vaadin.dcharts.metadata.TooltipMoveSpeeds;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
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
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.subview.util.GeneralUtil;
/*
 * @author Yehia Farag
 */

public class PlotsLayout extends VerticalLayout implements Serializable {

    private GeneralUtil util = new GeneralUtil("chart");
    private Notification notidication;

    public PlotsLayout(String lable, Map<Integer, ProteinBean> protienFractionList, Map<String, List<StandardProteinBean>> standProtList, double mw) {
        if (!protienFractionList.isEmpty()) {

            TreeSet<String> alfabet = util.getAlphabetSet();
            Label pepLable = new Label("<h5 style='font-family:verdana;color:#497482;text-align:left'>" + lable + "</h5>");
            pepLable.setContentMode(Label.CONTENT_XHTML);
            pepLable.setHeight("30px");
            pepLable.setWidth("100%");
            this.addComponent(pepLable);
            this.setMargin(false);
            final Map<Integer, StandardProteinBean> standProtMap = new HashMap<Integer, StandardProteinBean>();

            List<StandardProteinBean> borderList = standProtList.get("#79AFFF");
            List<StandardProteinBean> standardList = standProtList.get("#CDE1FF");

            Double[] initRealValue = new Double[(protienFractionList.size() + borderList.size() + standardList.size())];
            Integer[] initBordersValues = new Integer[initRealValue.length];
            Integer[] initStandardValues = new Integer[((protienFractionList.size() + borderList.size() + standardList.size()))];


            //for testing 
            Integer[] initAllStandardValues = new Integer[initRealValue.length];
            Object[] strArr = new String[initRealValue.length];
            int x = 0;
            int f = 1;
            double highScore = -1;
            initStandardValues[0] = 0;
            initRealValue[0] = 0d;
            initBordersValues[0] = 0;
            //for testing
            strArr[0] = "";
            if (lable.equalsIgnoreCase("#Peptides")) {
                for (int index : protienFractionList.keySet()) {
                    ProteinBean pb = protienFractionList.get(index);
                    initRealValue[x] = (double) pb.getNumberOfPeptidePerFraction();
                    strArr[x] = f + "";
                    for (StandardProteinBean spb : borderList) {
                        if (spb.getLowerFraction() == f) {
                            ++x;
                            initRealValue[x] = 0.0d;
                            strArr[x] = alfabet.pollFirst();
                            spb.setFractionIndicator(strArr[x].toString());
                            standProtMap.put(x, spb);
                            break;
                        }

                    }
                    for (StandardProteinBean spb : standardList) {
                        if (spb.getLowerFraction() == f) {
                            ++x;
                            initRealValue[x] = 0.0d;
                            strArr[x] = alfabet.pollFirst();
                            spb.setFractionIndicator(strArr[x].toString());
                            standProtMap.put(x, spb);
                        }

                    }

                    if (highScore < pb.getNumberOfPeptidePerFraction()) {
                        highScore = pb.getNumberOfPeptidePerFraction();
                    }
                    x++;
                    f++;
                }
            } else if (lable.equalsIgnoreCase("#Spectra")) {

                for (int index : protienFractionList.keySet()) {
                    ProteinBean pb = protienFractionList.get(index);
                    initRealValue[x] = (double) pb.getNumberOfSpectraPerFraction();
                    strArr[x] = f + "";
                    for (StandardProteinBean spb : borderList) {
                        if (spb.getLowerFraction() == f) {
                            ++x;
                            initRealValue[x] = 0.0d;
                            strArr[x] = alfabet.pollFirst();
                            spb.setFractionIndicator(strArr[x].toString());
                            standProtMap.put(x, spb);
                            break;
                        }

                    }
                    for (StandardProteinBean spb : standardList) {
                        if (spb.getLowerFraction() == f) {
                            ++x;
                            initRealValue[x] = 0.0d;
                            strArr[x] = alfabet.pollFirst();
                            spb.setFractionIndicator(strArr[x].toString());
                            standProtMap.put(x, spb);
                        }

                    }

                    if (highScore < pb.getNumberOfSpectraPerFraction()) {
                        highScore = pb.getNumberOfSpectraPerFraction();
                    }
                    x++;
                    f++;
                }
            } else if (lable.equalsIgnoreCase("Avg. Precursor Intensity")) {

                for (int index : protienFractionList.keySet()) {
                    ProteinBean pb = protienFractionList.get(index);
                    double avg = pb.getAveragePrecursorIntensityPerFraction();
                    initRealValue[x] = avg;
                    strArr[x] = f + "";
                    for (StandardProteinBean spb : borderList) {
                        if (spb.getLowerFraction() == f) {
                            ++x;
                            initRealValue[x] = 0.0d;
                            strArr[x] = alfabet.pollFirst();
                            spb.setFractionIndicator(strArr[x].toString());
                            standProtMap.put(x, spb);
                            break;
                        }

                    }
                    for (StandardProteinBean spb : standardList) {
                        if (spb.getLowerFraction() == f) {
                            ++x;
                            initRealValue[x] = 0.0d;
                            strArr[x] = alfabet.pollFirst();
                            spb.setFractionIndicator(strArr[x].toString());
                            standProtMap.put(x, spb);
                        }

                    }

                    if (highScore < avg) {
                        highScore = avg;
                    }
                    x++;
                    f++;
                }
            }



            for (int index = 1; index < strArr.length; index++) {
                initBordersValues[index] = 0;
                int indexLable = 0;
                try {
                    indexLable = Integer.valueOf(strArr[index].toString());
                } catch (Exception e) {
                    continue;
                }
                for (StandardProteinBean spb : borderList) {
                    if (spb.getLowerFraction() == indexLable) {
                        initBordersValues[index + 1] = (int) highScore;
                        index++;
                        break;
                    }
                }
            }


            for (int index = 1; index < strArr.length; index++) {
                initStandardValues[index] = 0;
                int indexLable = 0;
                try {
                    indexLable = Integer.valueOf(strArr[index].toString());
                } catch (Exception e) {
                    continue;
                }
                for (StandardProteinBean spb : standardList) {
                    if (spb.getLowerFraction() == indexLable) {
                        initStandardValues[index + 1] = (int) highScore;
                        index++;
                        break;
                    }
                }
            }

            initAllStandardValues[0] = 0;
            //for testing 
            for (int index = 1; index < initAllStandardValues.length; index++) {
                if (initStandardValues[index] > 0) {
                    initAllStandardValues[index] = initStandardValues[index];
                } else if (initBordersValues[index] > 0) {
                    initAllStandardValues[index] = initBordersValues[index];
                } else {
                    initAllStandardValues[index] = 0;
                }


            }

            Object[] bordersValues;
            bordersValues = new Integer[initBordersValues.length];
            for (int b = 0; b < initBordersValues.length; b++) {
                bordersValues[b] = Integer.valueOf(initBordersValues[b]);
            }
            bordersValues[0] = Integer.valueOf(initBordersValues[0]);

            Object[] standardValues;
            standardValues = new Integer[initStandardValues.length];
            for (int b = 0; b < initStandardValues.length; b++) {
                standardValues[b] = Integer.valueOf(initStandardValues[b]);
            }
            standardValues[0] = Integer.valueOf(initStandardValues[0]);


            Object[] realValue = new Double[(initRealValue.length)];
            System.arraycopy(initRealValue, 0, realValue, 0, realValue.length);
            realValue[0] = Double.valueOf(initRealValue[0]);

            DataSeries dataSeries = new DataSeries();
            dataSeries.add(realValue);
            dataSeries.add(bordersValues);
            dataSeries.add(standardValues);

            SeriesDefaults seriesDefaults = new SeriesDefaults()
                    .setFillToZero(false)
                    .setRenderer(SeriesRenderers.BAR)
                    .setLineWidth(0.2f)
                    .setGridBorderWidth(2.5f);
            Series series = new Series()
                    .addSeries(new XYseries().setIndex(0).setLabel("Real Values").setShowLabel(false).setShadow(false).setDisableStack(false))//.setPointLabels(realVallab))
                    .addSeries(new XYseries().setIndex(2).setLabel("Theoretical Protien").setShowLabel(false).setShadow(false).setDisableStack(false))
                    .addSeries(new XYseries().setIndex(1).setLabel("Standared Protien").setShowLabel(false).setShadow(false).setDisableStack(false));//.setPointLabels(thiolab ))

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

            Axes axes = new Axes()
                    .addAxis(new XYaxis().setDrawMajorTickMarks(true).setAutoscale(true).setBorderColor("#CED8F6").setDrawMajorGridlines(false).setDrawMinorGridlines(false).setRenderer(AxisRenderers.CATEGORY).setTickInterval(0.01f).setTicks(new Ticks().add(strArr)).setTickRenderer(TickRenderers.CANVAS)
                    .setNumberTicks(4)
                    .setTickOptions(
                    new CanvasAxisTickRenderer()
                    .setFontSize("8pt")
                    .setShowMark(true)
                    .setShowGridline(true)))
                    .addAxis(
                    new XYaxis(XYaxes.Y)
                    .setNumberTicks(3)
                    .setAutoscale(true)
                    .setTickOptions(
                    new AxisTickRenderer()
                    .setFormatString("%d")));

            Grid grid = new Grid().setDrawBorder(false).setBorderColor("#CED8F6").setGridLineColor("#CED8F6").setShadow(false);

            Options options = new Options()
                    .setSeriesDefaults(seriesDefaults)
                    .setSeries(series)
                    .setAxes(axes)
                    .setSyncYTicks(false)
                    .setHighlighter(highlighter)
                    .setSeriesColors("#50B747", "#79AFFF", "#CDE1FF")
                    .setAnimate(false)
                    .setAnimateReplot(false)
                    .setStackSeries(true)
                    .setGrid(grid);

            DCharts chart = new DCharts().setDataSeries(dataSeries).setOptions(options).show();
            chart.setWidth("98%");
            chart.setHeight("120px");
            chart.setMarginRight(10);
            this.setWidth("100%");
            this.addComponent(chart);
            chart.setEnableChartDataClickEvent(true);
            chart.setEnableChartDataMouseEnterEvent(true);
            chart.setEnableChartDataMouseLeaveEvent(true);
            chart.setEnableChartDataRightClickEvent(true);
            this.setComponentAlignment(chart, Alignment.MIDDLE_LEFT);
            chart.addHandler(new ChartDataClickHandler() {
                @Override
                public void onChartDataClick(ChartDataClickEvent event) {
                    if (standProtMap.containsKey(event.getChartData().getPointIndex().intValue())) {
                        StandardProteinBean spb = standProtMap.get(event.getChartData().getPointIndex().intValue());
                        String desc = "";
                        if (spb.isTheoretical()) {
                            desc = "Theoretical Protien\nMW: " + spb.getMW_kDa() + " kDa\nBetween Fractions " + spb.getLowerFraction() + " and " + spb.getUpperFraction();
                        } else {
                            desc = "Standared Protien\nMW: " + spb.getMW_kDa() + " kDa\nBetween Fractions: " + spb.getLowerFraction() + " and " + spb.getUpperFraction();

                        }
                        notidication = new Notification(" " + spb.getFractionIndicator() + "  " + spb.getName(), desc, Type.TRAY_NOTIFICATION, false);
                        notidication.setPosition(Position.BOTTOM_RIGHT);
                        notidication.show(Page.getCurrent());
                    }
                }
            });
            chart.addHandler(new ChartDataMouseEnterHandler() {
                @Override
                public void onChartDataMouseEnter(ChartDataMouseEnterEvent event) {

                    if (standProtMap.containsKey(event.getChartData().getPointIndex().intValue())) {
                        StandardProteinBean spb = standProtMap.get(event.getChartData().getPointIndex().intValue());
                        String desc = "";
                        if (spb.isTheoretical()) {
                            desc = "<p width='250px'>Index:  " + spb.getFractionIndicator() + " <br/>Theoretical Protien<br/>MW: " + spb.getMW_kDa() + " kDa<br/>Between Fractions: " + spb.getLowerFraction() + " and " + spb.getUpperFraction() + "</p>";
                        } else {
                            desc = "<p width='150px'>Index:  " + spb.getFractionIndicator() + " <br/>Standared Protien<br/>MW: " + spb.getMW_kDa() + " kDa<br/>Between Fractions: " + spb.getLowerFraction() + " and " + spb.getUpperFraction() + "</p>";

                        }
                        notidication = new Notification("<h4>" + spb.getName() + "</h4>", desc, Type.TRAY_NOTIFICATION, true);
                        notidication.setPosition(Position.BOTTOM_RIGHT);
                        notidication.show(Page.getCurrent());
                    }

                }
            });
            chart.addHandler(new ChartDataMouseLeaveHandler() {
                @Override
                public void onChartDataMouseLeave(ChartDataMouseLeaveEvent event) {
                    if (notidication != null) {
                        notidication.setDelayMsec(0);
                    }

                }
            });
            if (lable.equalsIgnoreCase("#Peptides")) {
                chart.setMarginLeft(25);
            } else if (lable.equalsIgnoreCase("#Spectra")) {
                chart.setMarginLeft(25);
            }
        }
    }
}
