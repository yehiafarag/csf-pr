package com.view.subviewunits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;



import com.bibounde.vprotovis.chart.bar.BarTooltipFormatter;
import com.model.FractionRangeUtilitie;
import com.model.beans.ProteinBean;
import com.model.beans.StandardProteinBean;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Runo;

import customchart.CustomBarChartComponent;

@SuppressWarnings("unused")
public class FractionsPlots extends VerticalLayout implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Map<Integer, ProteinBean> protienFractionList;
    private double mw;
    private ArrayList<String> ranges;
    private Button close;
    int index = 0;
    private Map<String, List<StandardProteinBean>> standProtGroups;
    int marker = 0;
    String sr = "";
    private FractionRangeUtilitie fru = new FractionRangeUtilitie();

    @SuppressWarnings("deprecation")
    public FractionsPlots(Map<Integer, ProteinBean> protienFractionList, double mw,  List<StandardProteinBean> standProtList) {
        this.mw = mw;
        this.ranges = ranges;
        setSpacing(true);
        this.setWidth("80%");
        this.protienFractionList = protienFractionList;
        VerticalLayout vlo = plotFull(protienFractionList, mw, standProtList);
        vlo.setStyle(Runo.PANEL_LIGHT);
        vlo.setWidth("100%");
        this.setWidth("100%");
        this.addComponent(vlo);






    }

    private VerticalLayout plotFull(Map<Integer, ProteinBean> protienFractionList, double mw, List<StandardProteinBean> standProtList) {
       

        // int defaultPosition = this.getdefaultPos(ranges,mw); 
        VerticalLayout vlo = new VerticalLayout();
        vlo.setSizeUndefined();
        standProtGroups = initGroups(standProtList, mw);

        CustomBarChartComponent bar1 = new CustomBarChartComponent();
        Label pepLable = new Label("<h5 style='font-family:verdana;color:#497482;'>" + "# Peptides" + "</h5>");
        pepLable.setContentMode(Label.CONTENT_XHTML);
        pepLable.setHeight("12px");
        bar1.setMarginLeft(50.0d);
        bar1.setMarginRight(30.0d);
        bar1.setMarginBottom(15d);
        bar1.setXAxisVisible(true);
        bar1.setXAxisLabelVisible(true);
        bar1.setYAxisVisible(true);
        bar1.setYAxisLabelVisible(true);
        bar1.setYAxisGridVisible(true);
        bar1.setLegendAreaWidth(10.0d);
        bar1.setLegendVisible(false);
        bar1.setBarInset(0.0001d);
        bar1.setGroupInset(1d);
        int highScore1 = 0;
        //add all colours plus the normal values colour
        String[] colors = new String[]{"#79AFFF", "#CDE1FF", "#50B747"};
        bar1.setColors(colors);

        double[] fractionRanges1 = new double[protienFractionList.size() * 2];
        //  double[]defaultRanges1 = new double[protienFractionList.size()];
        String[] rangeValues1 = new String[((protienFractionList.size() * 2))];


        int x = 0;
        boolean tag = false;
        for (int index : protienFractionList.keySet()) {
            rangeValues1[x] = "";
            fractionRanges1[x] = 0;
            ProteinBean pb = protienFractionList.get(index);
            fractionRanges1[x + 1] = pb.getNumberOfPeptidePerFraction();
            rangeValues1[x + 1] = "" + (index);


            if (highScore1 < pb.getNumberOfPeptidePerFraction()) {
                highScore1 = pb.getNumberOfPeptidePerFraction();
            }

            x++;
            x++;
        }


        Map<String, double[]> standredPlotGroup = initStandaredPlotValues(rangeValues1, standProtGroups, highScore1);

        final Map<String, StandardProteinBean[]> standredPlotGroupBeans = initStandaredPlotBeans(rangeValues1, standProtGroups, mw);

        standredPlotGroup.put("#50B747", fractionRanges1);
        double step = plotYStepOpt(highScore1);
        bar1.setYAxisLabelStep(step);

        for (String color : colors) {
            double[] g = standredPlotGroup.get(color);
            if (g != null) {
                bar1.addSerie(color, g);//fractions values
            }
        }
        bar1.setGroupNames(rangeValues1);
        bar1.setXAxisLabelVisible(true);
        bar1.setChartWidth(plotWidthOpt(rangeValues1.length));//1250.0d);
        bar1.setChartHeight(120.0d);


        //bar1.set


        BarTooltipFormatter tooltipFormatter = new BarTooltipFormatter() {
            /*
             * 
             */
            private static final long serialVersionUID = 1L;

            public String getTooltipHTML(String serieName, double value, String groupName) {
                String tooltipName1 = "";
                if (!sr.equals(serieName)) {
                    marker = 0;
                    sr = serieName;
                }

                StandardProteinBean[] spArr = standredPlotGroupBeans.get(serieName);

                if (spArr != null) {
                    StandardProteinBean spb = spArr[marker];
                    if (spb != null) {
                        tooltipName1 = "" + spb.getMW_kDa() + " kDa " + "[" + spb.getName() + "]";
                    }
                } else {
                    tooltipName1 = "Value :" + value;
                }

                marker++;


                return tooltipName1;
            }
        };

        bar1.setTooltipFormatter(tooltipFormatter);



        CustomBarChartComponent bar2 = new CustomBarChartComponent();
        Label specLable = new Label("<h5 style='font-family:verdana;color:#497482;'>" + "# Spectra" + "</h5>");
        specLable.setContentMode(Label.CONTENT_XHTML);
        specLable.setHeight("12px");
        bar2.setMarginLeft(50.0d);
        bar2.setMarginRight(30.0d);
        bar2.setMarginBottom(15d);
        bar2.setXAxisVisible(true);
        bar2.setXAxisLabelVisible(true);
        bar2.setYAxisVisible(true);
        bar2.setYAxisLabelVisible(true);
        bar2.setYAxisGridVisible(true);
        bar2.setLegendAreaWidth(50.0d);
        bar2.setLegendVisible(false);
        bar2.setBarInset(0.0001d);
        bar2.setGroupInset(1d);
        int highScore2 = 0;
        bar2.setColors(colors);
        double[] fractionRanges2 = new double[protienFractionList.size() * 2];
        //  double[]defaultRanges1 = new double[protienFractionList.size()];
        String[] rangeValues2 = new String[((protienFractionList.size() * 2))];


        int x2 = 0;
        for (int index : protienFractionList.keySet()) {
            rangeValues2[x2] = "";
            fractionRanges2[x2] = 0;
            ProteinBean pb = protienFractionList.get(index);
            fractionRanges2[x2 + 1] = pb.getNumberOfSpectraPerFraction();
            rangeValues2[x2 + 1] = "" + (index);


            if (highScore2 < pb.getNumberOfSpectraPerFraction()) {
                highScore2 = pb.getNumberOfSpectraPerFraction();
            }

            x2++;
            x2++;
        }


        Map<String, double[]> standredPlotGroup2 = updateStandaredPlotValues(standredPlotGroup, highScore2);
        standredPlotGroup2.put("#50B747", fractionRanges2);
        double step2 = plotYStepOpt(highScore2);
        bar2.setYAxisLabelStep(step2);

        for (String color : colors) {
            double[] g = standredPlotGroup2.get(color);
            if (g != null) {
                bar2.addSerie(color, g);//fractions values
            }
        }
        bar2.setGroupNames(rangeValues2);
        bar2.setXAxisLabelVisible(true);
        bar2.setChartWidth(plotWidthOpt(rangeValues2.length));//1250.0d);
        bar2.setChartHeight(120.0d);
        marker = 0;
        sr = "";
        bar2.setTooltipFormatter(tooltipFormatter);


        CustomBarChartComponent bar3 = new CustomBarChartComponent();

        Label intensityLable = new Label("<h5 style='font-family:verdana;color:#497482;'>" + "Avg Precursor Intensity" + "</h5>");
        intensityLable.setContentMode(Label.CONTENT_XHTML);
        intensityLable.setHeight("12px");
        bar3.setMarginLeft(50.0d);
        bar3.setMarginRight(30.0d);
        bar3.setMarginBottom(15d);
        bar3.setXAxisVisible(true);
        bar3.setXAxisLabelVisible(true);
        bar3.setYAxisVisible(true);
        bar3.setYAxisLabelVisible(true);
        bar3.setYAxisGridVisible(true);
        bar3.setLegendAreaWidth(50.0d);
        bar3.setLegendVisible(false);
        bar3.setBarInset(0.0001d);
        bar3.setGroupInset(1d);
        bar3.setColors(colors);

        double highScore3 = 0d;

        double[] fractionRanges3 = new double[protienFractionList.size() * 2];
        //  double[]defaultRanges1 = new double[protienFractionList.size()];
        String[] rangeValues3 = new String[((protienFractionList.size() * 2))];


        int x3 = 0;
        for (int index : protienFractionList.keySet()) {
            rangeValues3[x3] = "";
            fractionRanges3[x3] = 0;
            ProteinBean pb = protienFractionList.get(index);
            fractionRanges3[x3 + 1] = pb.getAveragePrecursorIntensityPerFraction();
            rangeValues3[x3 + 1] = "" + (index);


            if (highScore3 < pb.getAveragePrecursorIntensityPerFraction()) {
                highScore3 = pb.getAveragePrecursorIntensityPerFraction();
            }

            x3++;
            x3++;
        }


        Map<String, double[]> standredPlotGroup3 = updateStandaredPlotValues(standredPlotGroup, highScore3);
        standredPlotGroup3.put("#50B747", fractionRanges3);
        double step3 = plotYStepOpt(highScore3);
        bar3.setYAxisLabelStep(step3);

        for (String color : colors) {
            double[] g = standredPlotGroup3.get(color);
            if (g != null) {
                bar3.addSerie(color, g);//fractions values
            }
        }
        bar3.setGroupNames(rangeValues3);
        bar3.setXAxisLabelVisible(true);
        bar3.setChartWidth(plotWidthOpt(rangeValues3.length));//1250.0d);
        bar3.setChartHeight(120.0d);
        marker = 0;
        sr = "";
        bar3.setTooltipFormatter(tooltipFormatter);
        vlo.addComponent(pepLable);
        vlo.addComponent(bar1);
        vlo.addComponent(specLable);
        vlo.addComponent(bar2);
        vlo.addComponent(intensityLable);
        vlo.addComponent(bar3);



        return vlo;
    }

    private Map<String, StandardProteinBean[]> initStandaredPlotBeans(String[] rangeValues1, Map<String, List<StandardProteinBean>> standProtGroups, double mw2) {
        Map<String, StandardProteinBean[]> grupsMap = new Hashtable<String, StandardProteinBean[]>();


        for (String key : standProtGroups.keySet()) {
            List<StandardProteinBean> spl = standProtGroups.get(key);
            StandardProteinBean[] group = new StandardProteinBean[rangeValues1.length];
            int x = 0;
            int lowFract = 0;
            for (String str : rangeValues1) {
                if (!str.equals("")) {
                    try {
                        lowFract = Integer.valueOf(str);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    group[x] = null;

                } else {

                    for (StandardProteinBean spb : spl) {
                        if (spb.getLowerFraction() == 0) {
                            group[x] = spb;

                        } else if (spb.getLowerFraction() == lowFract) {
                            group[x] = spb;


                        }

                    }
                }
                x++;

            }
            grupsMap.put(key, group);
        }
        return grupsMap;
    }

    private Map<String, double[]> initStandaredPlotValues(String[] rangeValues1, Map<String, List<StandardProteinBean>> standProtGroups, double value) {
        Map<String, double[]> grupsMap = new Hashtable<String, double[]>();
        for (String key : standProtGroups.keySet()) {
            List<StandardProteinBean> spl = standProtGroups.get(key);
            double[] group = new double[rangeValues1.length];
            int x = 0;
            int lowFract = 0;
            for (String str : rangeValues1) {
                if (!str.equals("") && !str.equals("*")) {
                    lowFract = Integer.valueOf(str);
                    group[x] = 0d;



                } else {
                    for (StandardProteinBean spb : spl) {
                        if (spb.getLowerFraction() == 0) {
                            group[x] = value;


                        } else if (spb.getLowerFraction() == lowFract) {
                            group[x] = value;

                        }

                    }
                }
                x++;

            }
            grupsMap.put(key, group);
        }
        return grupsMap;
    }

    private Map<String, double[]> updateStandaredPlotValues(Map<String, double[]> grupsMap, double value) {
        Map<String, double[]> updatedMap = new Hashtable<String, double[]>();
        //updatedMap.putAll(grupsMap);
        for (String key : grupsMap.keySet()) {
            double[] nor = grupsMap.get(key);
            double[] update = new double[nor.length];

            for (int x = 0; x < update.length; x++) {
                double d = nor[x];
                if (d > 0) {
                    update[x] = value;
                } else {
                    update[x] = 0;
                }
            }
            updatedMap.put(key, update);

        }
        return updatedMap;

    }

    private String[] initColorSet(
            Map<String, List<StandardProteinBean>> standProtGroups,
            String color) {
        LinkedHashSet<String> hscolor = new LinkedHashSet<String>();
        hscolor.addAll(standProtGroups.keySet());
        String[] colors = new String[(standProtGroups.size() + 1)];
        int index = 0;
        colors[1] = color;
        for (String col : standProtGroups.keySet()) {
            if (index == 1) {
                index++;
            }
            colors[index] = col.toLowerCase();
            index++;
        }



        return colors;
    }

    private Map<String, List<StandardProteinBean>> initGroups(List<StandardProteinBean> standProtList, double mw) {
        Map<String, List<StandardProteinBean>> colorMap = new HashMap<String, List<StandardProteinBean>>();
        List<StandardProteinBean> blueList = new ArrayList<StandardProteinBean>();
        List<StandardProteinBean> redList = new ArrayList<StandardProteinBean>();
        List<StandardProteinBean> lowerList = new ArrayList<StandardProteinBean>();
        List<StandardProteinBean> upperList = new ArrayList<StandardProteinBean>();
        for (StandardProteinBean spb : standProtList) {
            if (spb.getMW_kDa() > mw) {
                upperList.add(spb);
            } else {
                lowerList.add(spb);
            }
        }

        StandardProteinBean closeLowe = new StandardProteinBean();
        closeLowe.setMW_kDa(-10000);
        StandardProteinBean closeUpper = new StandardProteinBean();
        closeUpper.setMW_kDa((10000 * 1000));;
        for (StandardProteinBean spb : lowerList) {
            if (closeLowe.getMW_kDa() <= spb.getMW_kDa()) {
                closeLowe = spb;
            }

        }
        for (StandardProteinBean spb : upperList) {
            if (closeUpper.getMW_kDa() >= spb.getMW_kDa()) {
                closeUpper = spb;
            }

        }


        for (StandardProteinBean spb : standProtList) {

            if ((spb.getMW_kDa() == closeLowe.getMW_kDa() && spb.getName() == closeLowe.getName()) || (spb.getMW_kDa() == closeUpper.getMW_kDa() && spb.getName() == closeUpper.getName())) {
                redList.add(spb);
            } else {
                blueList.add(spb);
            }
        }

        colorMap.put("#CDE1FF", blueList);
        colorMap.put("#79AFFF", redList);

        return colorMap;

    }

    private double[] convertorToArray(ArrayList<Double> rangeValuesList) {
        double[] arr = new double[rangeValuesList.size()];
        for (int x = 0; x < rangeValuesList.size(); x++) {
            arr[x] = rangeValuesList.get(x);
        }
        return arr;
    }

    private String[] convertorToStringArray(ArrayList<String> rangeValuesList) {
        String[] arr = new String[rangeValuesList.size()];
        for (int x = 0; x < rangeValuesList.size(); x++) {
            arr[x] = rangeValuesList.get(x);
        }
        return arr;
    }

    private int getdefaultPos(ArrayList<String> ranges2, double mw2) {
        List<Integer> defaultPosFractions = new ArrayList<Integer>();
        double minRange = 0d;
        double maxRange = 0d;
        int index = 0;
        for (int x = 0; x < ranges2.size(); x++) {
            index = Integer.valueOf((ranges2.get(x).split("\t")[0]));
            minRange = Double.valueOf((ranges2.get(x).split("\t")[1]));

            try {
                maxRange = Double.valueOf(ranges2.get(x).split("\t")[2]);//

            } catch (java.lang.NumberFormatException e) {
                if ((ranges2.get(x).split("\t")[1].split("-")[1]).contains(">")) {
                    maxRange = Double.valueOf(0);
                    if (mw2 >= minRange) {
                        return x + 1;
                    }
                }
            }

            if (mw2 >= minRange && mw2 < maxRange) {
                return x + 1;
            }
        }
        return 0;
    }

    /*

     private int getdefaultPos( ArrayList<String> ranges2, double mw2) {
     double minRange = 0d;
     double maxRange  = 0d;
     for(int x=0;x<ranges2.size();x++){
     minRange = Double.valueOf((ranges2.get(x).split("\t")[1]).split("-")[0]);
			
     try{
     maxRange = Double.valueOf(ranges2.get(x).split("\t")[1].split("-")[1].split(" ")[0]);
			
     }catch(java.lang.NumberFormatException e){
     if((ranges2.get(x).split("\t")[1].split("-")[1]).contains(">"))
     {
     maxRange = Double.valueOf(0);
     if(mw2>=minRange)
     return x;
     }
     }
			 
     if(mw2>=minRange && mw2<maxRange)
     return  x;
     }
     return 0;
     }
     */
    private double plotWidthOpt(int rangesNumber) {
        return Double.valueOf(13 * rangesNumber);

    }

    private double plotYStepOpt(double highScore) {
        return Double.valueOf((int) highScore / 5);
    }

    private double plotXStepOpt(double maxRange) {
        return Double.valueOf((int) maxRange / 30);
    }

    private double plotLengthOpt(double highScore, double step) {

        double stepsNumber = highScore / step;
        if (stepsNumber <= 5) {
            return 200.1d;
        } else if (stepsNumber > 5 && stepsNumber <= 10) {
            return 400.0d;
        } else if (stepsNumber > 10 && stepsNumber <= 20) {
            return 400.0d;
        } else if (stepsNumber > 20 && stepsNumber <= 30) {
            return 500.0d;
        } else {
            return 700.0d;
        }

    }

    public Map<String, List<StandardProteinBean>> getStandProtGroups() {
        return this.standProtGroups;
    }
}