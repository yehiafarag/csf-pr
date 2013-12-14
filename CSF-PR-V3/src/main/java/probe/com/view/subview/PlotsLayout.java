package probe.com.view.subview;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.base.elements.XYseries;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.metadata.TooltipAxes;
import org.dussan.vaadin.dcharts.metadata.TooltipFadeSpeeds;
import org.dussan.vaadin.dcharts.metadata.TooltipMoveSpeeds;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
import org.dussan.vaadin.dcharts.metadata.Yaxes;
import org.dussan.vaadin.dcharts.metadata.locations.TooltipLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.TickRenderers;
import org.dussan.vaadin.dcharts.metadata.ticks.TickLabelPositions;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Grid;
import org.dussan.vaadin.dcharts.options.Highlighter;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.Series;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.renderers.tick.CanvasAxisTickRenderer;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
/*
 * @author Yehia Farag
 */

public class PlotsLayout extends VerticalLayout implements Serializable {

////    private GeneralUtil util = new GeneralUtil("chart");
//    private Notification notification;

    public PlotsLayout(String lable, Map<Integer, ProteinBean> protienFractionList, Map<String, List<StandardProteinBean>> standProtList, double mw) {
        if (!protienFractionList.isEmpty()) {

//            TreeSet<String> alfabet = util.getAlphabetSet();
            Label pepLable = new Label("<h5 style='font-family:verdana;color:#4d749f;text-align:left'>" + lable + "</h5>");
            pepLable.setContentMode(Label.CONTENT_XHTML);
            pepLable.setHeight("30px");
            pepLable.setWidth("150px");
            this.addComponent(pepLable);
            this.setComponentAlignment(pepLable, Alignment.BOTTOM_LEFT);
            this.setMargin(false);
//            final Map<Integer, StandardProteinBean> standProtMap = new HashMap<Integer, StandardProteinBean>();

            List<StandardProteinBean> borderList = standProtList.get("#79AFFF");
            List<StandardProteinBean> standardList = standProtList.get("#CDE1FF");            
           
            Double[] initRealValue = new Double[(protienFractionList.size() + borderList.size() + standardList.size())];
           
             //
            Map<StandardProteinBean,Double[]> standardValuesList = new HashMap<StandardProteinBean,Double[]>();
            Map<StandardProteinBean,Double[]> tempStandardValuesList = new HashMap<StandardProteinBean,Double[]>();
           double lower = -1;
           double upper = -2;
            for(StandardProteinBean spb: borderList)
            {
                 Double[] standardValues = new Double[initRealValue.length];
                 standardValues[0] = 0d;
                 spb.setColor("#79AFFF");
                 spb.setTheoretical(true);
                 
                 if(upper< spb.getMW_kDa()){  
                     lower = upper;
                     upper = spb.getMW_kDa();
                    // lower = spb.getMW_kDa();
                 }
                 else
                     lower = spb.getMW_kDa();
                 standardValuesList.put(spb, standardValues);            
            }
             for(StandardProteinBean spb: standardList)
            {
                 Double[] standardValues = new Double[initRealValue.length];
                 standardValues[0] = 0d;
                 spb.setColor("#CDE1FF");
                 standardValuesList.put(spb, standardValues);         
            }
            
            //
           // Integer[] initBordersValues = new Integer[initRealValue.length];
           // Integer[] initStandardValues = new Integer[((protienFractionList.size() + borderList.size() + standardList.size()))];


            //for testing 
//            Integer[] initAllStandardValues = new Integer[initRealValue.length];
            Object[] strArr = new String[initRealValue.length];
            int x = 0;
            int f = 1;
            double highScore = -1;
         //   initStandardValues[0] = 0;
            initRealValue[0] = 0d;
        //    initBordersValues[0] = 0;
            //for testing
            strArr[0] = "";
            String formater="";
            int ang = 0;
            if (lable.equalsIgnoreCase("#Peptides")) {
              
                for (int index : protienFractionList.keySet()) {
                    ProteinBean pb = protienFractionList.get(index);
                    initRealValue[x] = (double) pb.getNumberOfPeptidePerFraction();
                    strArr[x] = f + "";
                     for (StandardProteinBean spb : standardValuesList.keySet()) {
                        if (spb.getLowerFraction() == f) {
                            ++x;
                            initRealValue[x] = 0.0d;
                            strArr[x] =" ";
                            spb.setFractionIndicator(strArr[x].toString());
                            break;
                        }
                     }
                    
                    
                    
//                    
//                    
//                    
//                    for (StandardProteinBean spb : borderList) {
//                        if (spb.getLowerFraction() == f) {
//                            ++x;
//                            initRealValue[x] = 0.0d;
//                            strArr[x] =" ";// alfabet.pollFirst();
//                            spb.setFractionIndicator(strArr[x].toString());
//                            standProtMap.put(x, spb);
//                            break;
//                        }
//
//                    }
//                    for (StandardProteinBean spb : standardList) {
//                        if (spb.getLowerFraction() == f) {
//                            ++x;
//                            initRealValue[x] = 0.0d;
//                            strArr[x] = " ";//alfabet.pollFirst();
//                            spb.setFractionIndicator(strArr[x].toString());
//                            standProtMap.put(x, spb);
//                        }
//
//                    }

                    if (highScore < pb.getNumberOfPeptidePerFraction()) {
                        highScore = pb.getNumberOfPeptidePerFraction();
                        if(highScore <= 10 )
                            highScore =10d;
                    }
                    x++;
                    f++;
                }
                 String str = "";
                 if(highScore <= 60)
                     str = StringUtils.leftPad("", 9);
                 else if(highScore <= 1000)
                     str = StringUtils.leftPad("", 7);
                formater=str+"%d";
            } else if (lable.equalsIgnoreCase("#Spectra")) {
                ang =0;// -25;
//                 String str = StringUtils.leftPad("", 7);
//                formater="%d"+str;
                 //formater="%.0f     ";

                for (int index : protienFractionList.keySet()) {
                    ProteinBean pb = protienFractionList.get(index);
                    initRealValue[x] = (double) pb.getNumberOfSpectraPerFraction();
                    strArr[x] = f + "";
                    
                     ////////////////////////////////////////
                     for (StandardProteinBean spb : standardValuesList.keySet()) {
                        if (spb.getLowerFraction() == f) {
                            ++x;
                            initRealValue[x] = 0.0d;
                            strArr[x] =" ";// alfabet.pollFirst();
                            spb.setFractionIndicator(strArr[x].toString());
//                            standProtMap.put(x, spb);
                            break;
                        }
                     }
                        ///////
//                    for (StandardProteinBean spb : borderList) {
//                        if (spb.getLowerFraction() == f) {
//                            ++x;
//                            initRealValue[x] = 0.0d;
//                            strArr[x] = " ";//alfabet.pollFirst();
//                            spb.setFractionIndicator(strArr[x].toString());
//                            standProtMap.put(x, spb);
//                            break;
//                        }
//
//                    }
//                    for (StandardProteinBean spb : standardList) {
//                        if (spb.getLowerFraction() == f) {
//                            ++x;
//                            initRealValue[x] = 0.0d;
//                            strArr[x] = " ";//alfabet.pollFirst();
//                            spb.setFractionIndicator(strArr[x].toString());
//                            standProtMap.put(x, spb);
//                        }
//
//                    }

                    if (highScore < pb.getNumberOfSpectraPerFraction()) {
                        highScore = pb.getNumberOfSpectraPerFraction();
                        if(highScore <= 10 )
                            highScore =10d;
                    }
                    x++;
                    f++;
                }
                String str = "";
                 if(highScore <= 60)
                     str = StringUtils.leftPad("",9);
                 else if(highScore <= 1000)
                     str = StringUtils.leftPad("",7);
                 else                     
                     str = StringUtils.leftPad("",5);
                formater=str+"%d";
            } else if (lable.equalsIgnoreCase("Avg. Precursor Intensity")) {

                ang=0;//-75;
                formater="%d";
                for (int index : protienFractionList.keySet()) {
                    ProteinBean pb = protienFractionList.get(index);
                    double avg =( pb.getAveragePrecursorIntensityPerFraction());
                    initRealValue[x] = avg;
                    strArr[x] = f + "";
                     ////////////////////////////////////////
                     for (StandardProteinBean spb : standardValuesList.keySet()) {
                        if (spb.getLowerFraction() == f) {
                            ++x;
                            initRealValue[x] = 0.0d;
                            strArr[x] =" ";// alfabet.pollFirst();
                            spb.setFractionIndicator(strArr[x].toString());
//                            standProtMap.put(x, spb);
                            break;
                        }
                     }
                        ///////
//                    for (StandardProteinBean spb : borderList) {
//                        if (spb.getLowerFraction() == f) {
//                            ++x;
//                            initRealValue[x] = 0.0d;
//                            strArr[x] =" ";// alfabet.pollFirst();
//                            spb.setFractionIndicator(strArr[x].toString());
//                            standProtMap.put(x, spb);
//                            break;
//                        }
//
//                    }
//                    for (StandardProteinBean spb : standardList) {
//                        if (spb.getLowerFraction() == f) {
//                            ++x;
//                            initRealValue[x] = 0.0d;
//                            strArr[x] =" ";// alfabet.pollFirst();
//                            spb.setFractionIndicator(strArr[x].toString());
//                            standProtMap.put(x, spb);
//                        }
//
//                    }

                    if (highScore < avg) {
                        highScore = avg;   
                          if(highScore <= 10 )
                            highScore =10d;
                    }
                    x++;
                    f++;
                }
                
                
                String str = "";
                 if(highScore <= 500000)
                     str = StringUtils.leftPad("",2);                 
                formater=str+"%d";
            
//                if(highScore <= 0.0000000000d)
//                            highScore =1d;
            }



            tempStandardValuesList.clear();
            for (int index = 1; index < strArr.length; index++) {
               // initBordersValues[index] = 0;
                int indexLable = 0;
                try {
                    indexLable = Integer.valueOf(strArr[index].toString());
                } catch (Exception e) {
                    continue;
                }
                for (StandardProteinBean spb : standardValuesList.keySet()) {
                    if (spb.getLowerFraction() == indexLable) {
                        Double[] values = standardValuesList.get(spb);
                        for(int localx=0;localx<values.length;localx++)
                        {
                            if(localx == index)
                            {
                                values[index] = 0d;
                                values[index + 1] = (double) highScore;
                                localx++;
                                continue;
                            }
                            values[localx] = 0d;
                        
                        }                
                        tempStandardValuesList.put(spb, values);
                        break;
                    }
                    
                }
            }
            standardValuesList.clear();
            standardValuesList.putAll(tempStandardValuesList);
            tempStandardValuesList.clear();


//            for (int index = 1; index < strArr.length; index++) {
//                initStandardValues[index] = 0;
//                int indexLable = 0;
//                try {
//                    indexLable = Integer.valueOf(strArr[index].toString());
//                } catch (Exception e) {
//                    continue;
//                }
//                for (StandardProteinBean spb : standardList) {
//                    if (spb.getLowerFraction() == indexLable) {
//                        initStandardValues[index + 1] = (int) highScore;
//                        index++;
//                        break;
//                    }
//                }
//            }

//            initAllStandardValues[0] = 0;
            //for testing 
//            for (int index = 1; index < initAllStandardValues.length; index++) {
//                if (initStandardValues[index] > 0) {
//                    initAllStandardValues[index] = initStandardValues[index];
//                } else if (initBordersValues[index] > 0) {
//                    initAllStandardValues[index] = initBordersValues[index];
//                } else {
//                    initAllStandardValues[index] = 0;
//                }
//
//
////            }
//
//            for(StandardProteinBean spb:standardValuesList.keySet())
//            {
//                 System.out.print(spb.getName()+" :  ");
//                Integer[] value = standardValuesList.get(spb);
//                for(int i:value)
//                    System.out.print(i+"  ");  
//                System.out.println();
//            
//            }
            
//            Object[] bordersValues;
//            bordersValues = new Integer[initBordersValues.length];
//            for (int b = 0; b < initBordersValues.length; b++) {
//                bordersValues[b] = Integer.valueOf(initBordersValues[b]);
//            }
//            bordersValues[0] = Integer.valueOf(initBordersValues[0]);
//
//            Object[] standardValues;
//            standardValues = new Integer[initStandardValues.length];
//            for (int b = 0; b < initStandardValues.length; b++) {
//                standardValues[b] = Integer.valueOf(initStandardValues[b]);
//            }
//            standardValues[0] = Integer.valueOf(initStandardValues[0]);
//

            Object[] realValue = new Double[(initRealValue.length)];
            System.arraycopy(initRealValue, 0, realValue, 0, realValue.length);
            realValue[0] = Double.valueOf(initRealValue[0]);

            DataSeries dataSeries = new DataSeries();
            dataSeries.add(realValue);
            String[] colours = new String[standardValuesList.size()+1];
            colours[0] = "#50B747";
            
            String[]lables = new String[standardValuesList.size()];
            
            int z=1;
            for(StandardProteinBean spb:standardValuesList.keySet())
            {
                Double[] initValues = standardValuesList.get(spb);
                Object[] values = new Double[(initValues.length)];
                System.arraycopy(initValues, 0, values, 0, values.length);
                values[0] = Double.valueOf(initValues[0]);
                dataSeries.add(values);
                colours[z] = spb.getColor();
                String lab="";
                if(spb.getMW_kDa() == lower)
                    lab = (int)spb.getMW_kDa()+ " kDa "+spb.getName()+" [MW Standard] Lower Theoretical ";
                else if(spb.getMW_kDa() == upper)
                    lab = (int)spb.getMW_kDa()+ " kDa "+spb.getName()+" [MW Standard] Upper Theoretical ";
               else
                    lab = (int)spb.getMW_kDa()+ " kDa "+spb.getName()+" [MW Standard] ";
                lables[z-1] = lab;    
                z++;
            }
//            dataSeries.add(bordersValues);
//            dataSeries.add(standardValues);

            SeriesDefaults seriesDefaults = new SeriesDefaults()
                    .setFillToZero(true)
                    .setRenderer(SeriesRenderers.BAR)
                    .setLineWidth(0.2f)
                    .setGridBorderWidth(2.5f)
                    .setYaxis(Yaxes.Y)
                    ;
            Series series = new Series()
                    .addSeries(new XYseries().setYaxis(Yaxes.Y).setIndex(0).setLabel(lable).setShowLabel(false).setShadow(false).setDisableStack(false));//.setPointLabels(realVallab))
                    
            for(int k=0;k<lables.length;k++)
                   series.addSeries(new XYseries().setYaxis(Yaxes.Y).setIndex(k+1).setLabel(lables[k]).setShowLabel(false).setShadow(false).setDisableStack(false).setShowMarker(true));
                   // .addSeries(new XYseries().setIndex(1).setLabel("Standard Protein").setShowLabel(false).setShadow(false).setDisableStack(false));//.setPointLabels(thiolab ))

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
                    .addAxis(new XYaxis().setDrawMajorTickMarks(true).setAutoscale(false)
                    .setBorderColor("#CED8F6").setDrawMajorGridlines(false).setDrawMinorGridlines(false)
                    .setRenderer(AxisRenderers.CATEGORY).setTickSpacing(1).setTickInterval(0.1f)
                    .setTicks(new Ticks().add(strArr)).setTickRenderer(TickRenderers.CANVAS)
                    
                    //.setNumberTicks(4)
                    .setTickOptions(
                    new CanvasAxisTickRenderer()
                    .setFontSize("8pt")
                    .setShowMark(true)
                    .setShowGridline(true)))                    
                    
                    .addAxis(
                    new XYaxis(XYaxes.Y)//.setLabel(spaceLab)
                    .setNumberTicks(3)
                    .setAutoscale(false).setMax(highScore)
                    .setTickOptions(
                    new CanvasAxisTickRenderer().setLabelPosition(TickLabelPositions.END).setAngle(ang).setFormatString(formater).setShowLabel(true).setShowMark(true)
                    ))
                    
//                     .addAxis(
//                    new XYaxis(XYaxes.Y)
//                    .setTickOptions(
//                    new AxisTickRenderer().setShowLabel(false)
//                    ))
                    ;
            
//             axes.addAxis(
//                    new XYaxis(XYaxes.Y2)
//                   // .setNumberTicks(3)
//                    .setAutoscale(true)
//                    .setTickOptions(
//                    new AxisTickRenderer().setShowLabel(true)
//                    .setFormatString("%d")));
            
                    
                    //.addAxis(new XYaxis(XYaxes.Y2));
            
            //System.out.println("the axces size for "+lable+"  is "+axes.getAxis(XYaxes.Y).getTickOptions().getMarkSize() );

            Grid grid = new Grid().setDrawBorder(false).setBackground("#FFFFFF").setBorderColor("#CED8F6").setGridLineColor("#CED8F6").setShadow(false);

            Options options = new Options()
                    .setSeriesDefaults(seriesDefaults)
                    .setSeries(series)
                    .setAxes(axes)
                    .setSyncYTicks(false)
                    .setHighlighter(highlighter)
                    
                    .setSeriesColors(colours)//"#50B747", "#79AFFF", "#CDE1FF")
                  
                    .setAnimate(false)
                    .setAnimateReplot(false)
                    .setStackSeries(true)                    
                    .setGrid(grid);
            

            DCharts chart = new DCharts().setDataSeries(dataSeries).setOptions(options).show();
            chart.setWidth("100%");
            chart.setHeight("120px");            
            chart.setMarginRight(10);
           // chart.setMarginLeft(10);
//            if (lable.equalsIgnoreCase("#Spectra")){
//              //  chart.setMarginRight(25);
////                    Unit wid = chart.getWidthUnits();
////                    System.out.println();
////                   chart.setWidth(lable);
//            }
           
          
            
            this.setWidth("100%");
            this.addComponent(chart);
           
////            chart.setEnableChartDataClickEvent(true);
////            chart.setEnableChartDataMouseEnterEvent(true);
////            chart.setEnableChartDataMouseLeaveEvent(true);
////            chart.setEnableChartDataRightClickEvent(true);
            this.setComponentAlignment(chart, Alignment.MIDDLE_RIGHT);
//            chart.addHandler(new ChartDataClickHandler() {
//                @Override
//                public void onChartDataClick(ChartDataClickEvent event) {
//                    if (standProtMap.containsKey(event.getChartData().getPointIndex().intValue())) {
//                        StandardProteinBean spb = standProtMap.get(event.getChartData().getPointIndex().intValue());
//                        String desc = "";
//                        if (spb.isTheoretical()) {
//                            desc = "Theoretical Protein\nMW: " + spb.getMW_kDa() + " kDa\nBetween Fractions " + spb.getLowerFraction() + " and " + spb.getUpperFraction();
//                        } else {
//                            desc = "Standared Protein\nMW: " + spb.getMW_kDa() + " kDa\nBetween Fractions: " + spb.getLowerFraction() + " and " + spb.getUpperFraction();
//
//                        }
//                        notification = new Notification(" " + spb.getFractionIndicator() + "  " + spb.getName(), desc, Type.TRAY_NOTIFICATION, false);
//                        notification.setPosition(Position.BOTTOM_RIGHT);
//                        notification.show(Page.getCurrent());
//                        notification.setDelayMsec(1);
//                    }
//                }
//            });
//            chart.addHandler(new ChartDataMouseEnterHandler() {
//                @Override
//                public void onChartDataMouseEnter(ChartDataMouseEnterEvent event) {
//
//                    if (standProtMap.containsKey(event.getChartData().getPointIndex().intValue())) {
//                        StandardProteinBean spb = standProtMap.get(event.getChartData().getPointIndex().intValue());
//                        String desc = "";
//                        if (spb.isTheoretical()) {
//                            desc = "<p width='250px'>"/*Index:  " + spb.getFractionIndicator() + " <br/>*/+"Theoretical Protein<br/>MW: " + spb.getMW_kDa() + " kDa<br/>Between Fractions: " + spb.getLowerFraction() + " and " + spb.getUpperFraction() + "</p>";
//                        } else {
//                            desc = "<p width='150px'>"/*Index:  " + spb.getFractionIndicator() + " <br/>*/+"Standared Protein<br/>MW: " + spb.getMW_kDa() + " kDa<br/>Between Fractions: " + spb.getLowerFraction() + " and " + spb.getUpperFraction() + "</p>";
//
//                        }
//                        notification = new Notification("" + spb.getName() + "", desc, Type.TRAY_NOTIFICATION, true);
//                        notification.setPosition(Position.BOTTOM_RIGHT);
//                        notification.show(Page.getCurrent());
//                         notification.setDelayMsec(1);
//                    }
//
//                }
//            });
//            chart.addHandler(new ChartDataMouseLeaveHandler() {
//                @Override
//                public void onChartDataMouseLeave(ChartDataMouseLeaveEvent event) {
//                    if (notification != null) {
//                        notification.setDelayMsec(1);
//                    }
//
//                }
//            });
//            if (lable.equalsIgnoreCase("#Peptides")&&highScore!=1) {
//                chart.setMarginLeft(25);
//            } else if (lable.equalsIgnoreCase("#Spectra")&&highScore!=1) {
//                chart.setMarginLeft(25);
//            }
        }
    }
}
