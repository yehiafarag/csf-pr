
package probe.com.bin;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.dussan.vaadin.dcharts.DCharts;
import org.dussan.vaadin.dcharts.base.elements.PointLabels;
import org.dussan.vaadin.dcharts.base.elements.XYaxis;
import org.dussan.vaadin.dcharts.data.DataSeries;
import org.dussan.vaadin.dcharts.data.Ticks;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickEvent;
import org.dussan.vaadin.dcharts.events.click.ChartDataClickHandler;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterEvent;
import org.dussan.vaadin.dcharts.events.mouseenter.ChartDataMouseEnterHandler;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveEvent;
import org.dussan.vaadin.dcharts.events.mouseleave.ChartDataMouseLeaveHandler;
import org.dussan.vaadin.dcharts.metadata.XYaxes;
import org.dussan.vaadin.dcharts.metadata.directions.BarDirections;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.metadata.ticks.TickFormatters;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Grid;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.renderers.series.BarRenderer;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.core.InformationField;

/**
 *
 * @author Yehia Farag
 */
public class StudiesChartComponentII extends VerticalLayout implements CSFFilter{
    
    private final String filter_id="overallStudiesChartII";
    String[] lightColors = new String[]{"#BAE7F0","#FFC782","#FBE6B1","#95FBA6","#AAC98D","#CFD25D","#EF97F4","#94A1FA","#FCD162","#FFA278","#8DD7FC","#DF98E1","#DFF79A"};
    String[] darkColors = new String[]{"#79AFBA","#E68A19","#AC9E79","#2C903C","#607749","#717330","#9D40A1","#273CC0","#CEA438","#E25C1E","#47A2D0","#DC53DF","#93C604"};
    
    private Options options ;
    private Axes axes ;
    private final DCharts studiesChart;
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final HorizontalLayout studiesFilterLayout = new HorizontalLayout();
    private final VerticalLayout studiesChartLayout = new VerticalLayout();
    private final VerticalLayout studiesInformationLayout = new VerticalLayout();
    private final Panel infoLayoutPanel ;
    private QuantDatasetObject currentSelectedStudy;
    private QuantDatasetObject [][] studiesMatrix ;
    
    public StudiesChartComponentII(DatasetExploringCentralSelectionManager exploringFiltersManager){
        this.setHeight("100%");
        studiesFilterLayout.setMargin(false);
        studiesFilterLayout.setWidth("100%");
        HideOnClickLayout mainBodyLayout = new HideOnClickLayout("Studies Overview II", studiesFilterLayout, null);
        this.addComponent(mainBodyLayout);

        this.exploringFiltersManager = exploringFiltersManager;
        studiesFilterLayout.addComponent(studiesChartLayout);
        studiesFilterLayout.addComponent(studiesInformationLayout);

        exploringFiltersManager.registerFilter(StudiesChartComponentII.this);

        studiesFilterLayout.setExpandRatio(studiesChartLayout, 0.7f);
        studiesFilterLayout.setExpandRatio(studiesInformationLayout, 0.29f);

        studiesChart = new DCharts();
        this.initChart();
          studiesChart.setEnableChartDataClickEvent(true);
        studiesChart.setEnableChartDataMouseEnterEvent(true);
        studiesChart.setEnableChartDataMouseLeaveEvent(true);
        studiesChart.addHandler(new ChartDataMouseEnterHandler() {

             @Override
             public void onChartDataMouseEnter(ChartDataMouseEnterEvent event) {
                  int x = (int) (long) event.getChartData().getSeriesIndex();
                int y = (int) (long) event.getChartData().getPointIndex();
                QuantDatasetObject study = studiesMatrix[y][x];
                if (study != null) {
                    containerLayout.setVisible(true);
                    updateStudyInformationLayout(study);

                } else {
                    if (currentSelectedStudy == null) {
                        containerLayout.setVisible(false);
                    }
                    else{
                    updateStudyInformationLayout(currentSelectedStudy);
                    }
                }
            }
        });
        
        
         studiesChart.addHandler(new ChartDataMouseLeaveHandler() {

             @Override
             public void onChartDataMouseLeave(ChartDataMouseLeaveEvent event) {
                 if (!keepStudyInfoLayout) {
                     containerLayout.setVisible(false);

                 } else {
                     updateStudyInformationLayout(currentSelectedStudy);
                 }

             }
         });
         
        studiesChart.addHandler(new ChartDataClickHandler() {

            @Override
            public void onChartDataClick(ChartDataClickEvent event) {
                int x = (int) (long) event.getChartData().getSeriesIndex();
                int y = (int) (long) event.getChartData().getPointIndex();
                QuantDatasetObject study = studiesMatrix[y][x];
                if (study != null) {
                    keepStudyInfoLayout=true;
                    updateStudyInformationLayout(study);
                    currentSelectedStudy = study;
                }else
                {
                    keepStudyInfoLayout=false;
                }

             }
         });
        
        
      
        infoLayoutPanel = initStudyInformationLayout();
        studiesChartLayout.addComponent(studiesChart);
        studiesInformationLayout.addComponent(infoLayoutPanel);
        updateStudyChart(exploringFiltersManager.getFilteredDatasetsList());

    }
    private boolean keepStudyInfoLayout = false;
    private void initChart() {

        SeriesDefaults seriesDefaults = new SeriesDefaults().setShadow(false)
                                .setPointLabels(
                                        new PointLabels()
                                        .setShow(false).setHideZeros(true).setFormatter(TickFormatters.DEFAULT).setEscapeHTML(false)
                        )
                .setRenderer(SeriesRenderers.BAR).setRendererOptions(
                        new BarRenderer().setBarWidth(30)
                        .setBarDirection(BarDirections.HOTIZONTAL));
        axes = new Axes()
                .addAxis(
                        new XYaxis(XYaxes.Y).setShowMinorTicks(false)
                        .setRenderer(AxisRenderers.CATEGORY)
                ).addAxis(new XYaxis(XYaxes.X).setShowLabel(false).setShowTicks(false).setMax(100));

        options = new Options().setGrid(new Grid().setDrawGridlines(true).setDrawBorder(true).setBackground("#ffffff").setShadow(false)).setSortData(true)
                .setSeriesDefaults(seriesDefaults).setStackSeries(true)
                .setAxes(axes);
        studiesChart.setOptions(options);
        studiesChart.setWidth("85%");
        studiesChart.setHeight("550px");

    }


     private void updateStudyChart(QuantDatasetObject[] updatedStudiesList) {    
         
         Map<String, List<QuantDatasetObject>>   studySeriesSet = generateSeriousMap(updatedStudiesList);             
        Object[] ticksValue = new Object[studySeriesSet.size()];        
        Object [][] dataMatrix = new Object[studySeriesSet.size()][13];
        studiesMatrix = new QuantDatasetObject[studySeriesSet.size()][13];
     int maxValue =0;
         int x = 0;
         for (String key : studySeriesSet.keySet()) {
             Object[] ser = new Object[13];
             QuantDatasetObject[] studies=  new QuantDatasetObject[13];
             if(maxValue < studySeriesSet.get(key).size())
                 maxValue = studySeriesSet.get(key).size();
             for (int i = 0; i < studySeriesSet.get(key).size(); i++) {
                 ser[i] = 10;
                 studies[i] = studySeriesSet.get(key).get(i);
             }
             dataMatrix[x] = ser;
             studiesMatrix[x]=studies;
             x++;

         }
        
     
         DataSeries dataSeries = new DataSeries();
         for (int v = 0; v < 13; v++) {
             DataSeries ds = dataSeries.newSeries();
             int z = 0;
             for (int f = 0; f < dataMatrix.length; f++) {
                 if (dataMatrix[f][v] != null) {
                     ds.add(10, f + 1);
                 } else {
                     ds.add(0, f + 1);
                 }
                 z++;
             }
         }

         maxValue = maxValue *10 +10;
         if (maxValue < 100) {
             maxValue = 100;
         }

         int z = 0;
         for (String ss : studySeriesSet.keySet()) {
             ticksValue[z] = ss;
             z++;
         }
        Ticks ticks =new Ticks().add(ticksValue);
        
        options = studiesChart.getOptions();
        axes = options.getAxes();
        axes.getAxis(XYaxes.Y).setTicks(ticks).setLabel("PubMed ID");
        axes.getAxis(XYaxes.X).setMax(maxValue);
        
        options.setAxes(axes);
        studiesChart.setOptions(options);        
        studiesChart.setDataSeries(dataSeries);
        
        
        
    }
     
    private Map<String, List<QuantDatasetObject>> generateSeriousMap(QuantDatasetObject[] updatedStudiesList) {
        Map<String, Set<QuantDatasetObject>> seriousSetMap = new HashMap<String, Set<QuantDatasetObject>>();
        for (QuantDatasetObject pb : updatedStudiesList) {
            Set<QuantDatasetObject> studySet = null;
            if (!seriousSetMap.containsKey(pb.getPumedID())) {
                studySet = new HashSet<QuantDatasetObject>();
                studySet.add(pb);

            } else {
                studySet = seriousSetMap.get(pb.getPumedID());
                studySet.add(pb);

            }
            seriousSetMap.put(pb.getPumedID(), studySet);

        }

        Map<String, List<QuantDatasetObject>> seriousMap = new HashMap<String, List<QuantDatasetObject>>();
        for (String key : seriousSetMap.keySet()) {
            List<QuantDatasetObject> l = new ArrayList<QuantDatasetObject>();
            l.addAll(seriousSetMap.get(key));
            seriousMap.put(key, l);
        }
        return seriousMap;

    }
    public void show() {
        studiesChart.show();
    }
    @Override
    public void selectionChanged(String type) {
        this.updateStudyChart(exploringFiltersManager.getFilteredDatasetsList());
        this.studiesChart.show();
        
        
    }

    @Override
    public String getFilterId() {
        return filter_id;
    }
    private Label titleLabel ;
    private InformationField filesNumber, identifiedProteinsNumber, quantifiedProteinsNumber, diseaseGroup, rawData, year, typeOfStudy, sampleType, sampleMatching, shotgunTargeted, technology, analyticalApproach, enzyme, quantificationBasis, quantBasisComment, patientsGroup1Number, patientsGroup2Number, normalization_strategy;

    private  VerticalLayout containerLayout ;
    private Panel initStudyInformationLayout() {

        Panel containerPanel = new Panel();
        containerPanel.setHeight("550px");
        containerPanel.setStyleName("grayborder");
        containerLayout = new VerticalLayout();
//        containerPanel.setHeight("500px");

        containerLayout.setSpacing(false);
        containerLayout.setMargin(false);

        containerPanel.setContent(containerLayout);
        //title pubmid id
        titleLabel = new Label("PubMed ID");
        titleLabel.setStyleName("titleLabel");

        titleLabel.setWidth(("test title".length() * 10) + "px");
        containerLayout.addComponent(titleLabel);
        containerLayout.setComponentAlignment(titleLabel, Alignment.MIDDLE_CENTER);
        HorizontalLayout labelsContainer = new HorizontalLayout();
        //level 2 the information        
        containerLayout.addComponent(labelsContainer);
        containerLayout.setComponentAlignment(labelsContainer, Alignment.MIDDLE_CENTER);
        // left side
        VerticalLayout leftSideLayout = new VerticalLayout();
        leftSideLayout.setSpacing(true);
        labelsContainer.addComponent(leftSideLayout);
        labelsContainer.setComponentAlignment(leftSideLayout, Alignment.MIDDLE_LEFT);
        leftSideLayout.setWidth("100%");
        leftSideLayout.setHeight("100%");
        filesNumber = new InformationField("# Files");
        leftSideLayout.addComponent(filesNumber);

        diseaseGroup = new InformationField("Disease Group");
        leftSideLayout.addComponent(diseaseGroup);

        typeOfStudy = new InformationField("Type of Study");
        leftSideLayout.addComponent(typeOfStudy);

        shotgunTargeted = new InformationField("Shotgun/Targeted");
        leftSideLayout.addComponent(shotgunTargeted);

        enzyme = new InformationField("Enzyme");
        leftSideLayout.addComponent(enzyme);

        patientsGroup1Number = new InformationField("#Patients Gr.I");
        leftSideLayout.addComponent(patientsGroup1Number);
        // middle layout         
        VerticalLayout middleLayout = new VerticalLayout();
        middleLayout.setSpacing(true);
        middleLayout.setWidth("100%");
        middleLayout.setHeight("100%");
        
        labelsContainer.addComponent(middleLayout);
        labelsContainer.setComponentAlignment(middleLayout, Alignment.MIDDLE_LEFT);

        identifiedProteinsNumber = new InformationField("# Identified Proteins");
        middleLayout.addComponent(identifiedProteinsNumber);

        rawData = new InformationField("Raw Data");
        middleLayout.addComponent(rawData);

        sampleType = new InformationField("Sample Type");
        middleLayout.addComponent(sampleType);

        technology = new InformationField("Technology");
        middleLayout.addComponent(technology);

        quantificationBasis = new InformationField("Quantification Basis");
        middleLayout.addComponent(quantificationBasis);
        patientsGroup2Number = new InformationField("#Patients Gr.II");
        middleLayout.addComponent(patientsGroup2Number);
        //right side
        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setWidth("100%");
        rightSideLayout.setSpacing(true);
        rightSideLayout.setHeight("100%");
        labelsContainer.addComponent(rightSideLayout);
        labelsContainer.setComponentAlignment(rightSideLayout, Alignment.MIDDLE_LEFT);

        quantifiedProteinsNumber = new InformationField("# Quantified Proteins");
        rightSideLayout.addComponent(quantifiedProteinsNumber);

        year = new InformationField("Year");
        rightSideLayout.addComponent(year);

        sampleMatching = new InformationField("Sample Matching");
        rightSideLayout.addComponent(sampleMatching);

        analyticalApproach = new InformationField("Analytical Approach");
        rightSideLayout.addComponent(analyticalApproach);

        quantBasisComment = new InformationField("Quantification BasisComment");
        rightSideLayout.addComponent(quantBasisComment);

        normalization_strategy = new InformationField("Normalization Strategy");
        rightSideLayout.addComponent(normalization_strategy);
        
        containerLayout.setVisible(false);
        return containerPanel;

    }

    private void updateStudyInformationLayout(QuantDatasetObject study) {
        
        titleLabel.setValue("PubMed ID: " + study.getPumedID()); 
        titleLabel.setWidth((titleLabel.getValue().length()* 10) + "px");
//        filesNumber.setValue(study.getFilesNumber() + "");
//        identifiedProteinsNumber.setValue(study.getIdentifiedProteinsNumber() + "");
//        quantifiedProteinsNumber.setValue(study.getQuantifiedProteinsNumber() + "");
//        diseaseGroup.setValue(study.getDiseaseGroups());
//        rawData.setValue(study.getRawDataUrl());
//        year.setValue(study.getYear() + "");
//        typeOfStudy.setValue(study.getTypeOfStudy());
//        sampleType.setValue(study.getSampleType());
//        sampleMatching.setValue(study.getSampleMatching());
//        shotgunTargeted.setValue(study.getShotgunTargeted());
//        technology.setValue(study.getTechnology());
//        analyticalApproach.setValue(study.getAnalyticalApproach());
//        enzyme.setValue(study.getEnzyme());
//        quantificationBasis.setValue(study.getQuantificationBasis());
//        quantBasisComment.setValue(study.getQuantBasisComment());
//        patientsGroup1Number.setValue(study.getPatientsGroup1Number() + "");
//        patientsGroup2Number.setValue(study.getPatientsGroup2Number() + "");
//        normalization_strategy.setValue(study.getNormalizationStrategy());
        patientsGroup1Number.setHeight(normalization_strategy.getHeight(),normalization_strategy.getHeightUnits());
        patientsGroup2Number.setHeight(normalization_strategy.getHeight(),normalization_strategy.getHeightUnits());

    }
     @Override
    public void removeFilterValue(String value) {
    }

}
