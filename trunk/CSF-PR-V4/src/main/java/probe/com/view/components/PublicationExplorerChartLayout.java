/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import probe.com.view.core.SingleDatasetPublicationLayout;
import static com.google.gwt.resources.client.ImageResource.RepeatStyle.Vertical;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Tree;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.HashSet;
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
import org.dussan.vaadin.dcharts.metadata.locations.PointLabelLocations;
import org.dussan.vaadin.dcharts.metadata.renderers.AxisRenderers;
import org.dussan.vaadin.dcharts.metadata.renderers.SeriesRenderers;
import org.dussan.vaadin.dcharts.metadata.styles.CursorStyles;
import org.dussan.vaadin.dcharts.metadata.ticks.TickFormatters;
import org.dussan.vaadin.dcharts.options.Axes;
import org.dussan.vaadin.dcharts.options.Cursor;
import org.dussan.vaadin.dcharts.options.Grid;
import org.dussan.vaadin.dcharts.options.Options;
import org.dussan.vaadin.dcharts.options.SeriesDefaults;
import org.dussan.vaadin.dcharts.options.Title;
import org.dussan.vaadin.dcharts.renderers.series.BarRenderer;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringSelectionManagerRes;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.model.beans.PublicationRoot;
import probe.com.view.core.HideOnClickLayout;
import probe.com.view.core.InformationField;

/**
 *
 * @author Yehia Farag
 */
public class PublicationExplorerChartLayout extends VerticalLayout implements CSFFilter {

    private final Tree publicationTree;
    private final DatasetExploringSelectionManagerRes exploringFiltersManager;
    private final VerticalLayout mainbodyLayout = new VerticalLayout();
    private final HorizontalLayout chartsContainerLayout = new HorizontalLayout();
    private String[] headers = new String[20];
    private final HideOnClickLayout layout;

    public PublicationExplorerChartLayout(DatasetExploringSelectionManagerRes exploringFiltersManager) {

        headers[2] = "#ID:";
        headers[3] = "#Quant:";
        headers[16] = "#PGr1:";
        headers[17] = "#PGr2:";
        
        
        layout = new HideOnClickLayout("Publications Overview", mainbodyLayout, null);
        
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.exploringFiltersManager = exploringFiltersManager;
        this.addComponent(layout);
       mainbodyLayout.setWidth("100%");
        updateStudiesNodes(exploringFiltersManager.getFilteredDatasetsList());
       
        mainbodyLayout.addComponent(chartsContainerLayout);
        chartsContainerLayout.setWidth("100%");

        studiesChart = new DCharts();
        initChartVisualization();
        chartsContainerLayout.addComponent(studiesChart);
        chartsContainerLayout.addComponent(infoLayoutPanel);
        chartsContainerLayout.setSpacing(false);
        chartsContainerLayout.setExpandRatio(studiesChart, 0.65f);
        chartsContainerLayout.setExpandRatio(infoLayoutPanel, 0.35f);
        chartsContainerLayout.setComponentAlignment(infoLayoutPanel, Alignment.MIDDLE_CENTER);

        updateStudyChart();
        studiesChart.show();
        exploringFiltersManager.registerFilter(PublicationExplorerChartLayout.this);
        //temp remove 
        
         publicationTree = new Tree("");
        
//        Tree.ItemStyleGenerator styleGen = new Tree.ItemStyleGenerator() {
//            
//            @Override
//            public String getStyle(Tree source, Object itemId) {
//                if (!source.areChildrenAllowed(itemId)) {
//                    return "treeValue";
//                }
//                return "treeHeader";
//            }
//        };
//        publicationTree.setItemStyleGenerator(styleGen);
//        publicationTree.setImmediate(true);
//        mainbodyLayout.addComponent(publicationTree);
//        updateTreeNodes();
         layout.setVisability(true);

    }

    private final DCharts studiesChart;
    private SeriesDefaults seriesDefaults;
    private Options options;
    private DataSeries dataSeries;
    private String[] labels;
    private Axes axes;
    private Panel infoLayoutPanel;

    private void initChartVisualization() {
        studiesChart.setWidth("100%");
        studiesChart.setHeight("550px");
        studiesChart.setMarginRight(4);
        studiesChart.setImmediate(true);
        studiesChart.setEnableChartImageChangeEvent(true);
        

        seriesDefaults = new SeriesDefaults().setShadow(false)
                .setRenderer(SeriesRenderers.BAR).setRendererOptions(
                        new BarRenderer().setBarWidth(30)
                        .setBarDirection(BarDirections.HOTIZONTAL));
        axes = new Axes()
                .addAxis(
                        new XYaxis(XYaxes.Y).setShowMinorTicks(false)
                        .setRenderer(AxisRenderers.CATEGORY)
                ).addAxis(new XYaxis(XYaxes.X).setShowLabel(false).setShowTicks(false));

        options = new Options().setGrid(new Grid().setDrawGridlines(true).setDrawBorder(true).setBorderColor("#dce0e0").setBorderWidth(1).setGridLineColor("#F8F8F8").setBackground("#ffffff").setShadow(false)).setSortData(true)
                .setSeriesDefaults(seriesDefaults).setStackSeries(false).setTitle(new Title().setShow(false))
                .setAxes(axes).setCursor(new Cursor().setStyle(CursorStyles.POINTER).setShow(true).setShowTooltip(false));

        studiesChart.setOptions(options);
        studiesChart.setEnableChartDataClickEvent(true);
        studiesChart.setEnableChartDataMouseEnterEvent(true);
        studiesChart.setEnableChartDataMouseLeaveEvent(true);
        studiesChart.addHandler(new ChartDataMouseEnterHandler() {

            @Override
            public void onChartDataMouseEnter(ChartDataMouseEnterEvent event) {
                int x = (int) (long) event.getChartData().getSeriesIndex();
                int y = (int) (long) event.getChartData().getPointIndex();
                PublicationRoot node = nodes.get(nodeKeys[y]);
                if (node != null) {
                    containerLayout.setVisible(true);
                    updateStudyInformationLayout(node);

                } else {
                    if (currentSelectedStudy == null) {
                        containerLayout.setVisible(false);
                    } else {
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
                if (event.getChartData() != null) {
                    try {
                        int x = (int) (long) event.getChartData().getSeriesIndex();
                        int y = (int) (long) event.getChartData().getPointIndex();
                        PublicationRoot node = nodes.get(nodeKeys[y]);
                        if (node != null) {
                            keepStudyInfoLayout = true;
                            updateStudyInformationLayout(node);
                            selectPublication(y);
                            currentSelectedStudy = node;
                        } else {
                            keepStudyInfoLayout = false;
                        }

                    } catch (NullPointerException npe) {
                    }
                
            }
        }
    }
    );

        infoLayoutPanel = initStudyInformationLayout();

    }
    private boolean keepStudyInfoLayout = false;
    private PublicationRoot currentSelectedStudy;

    private void updateStudyChart() {
        dataSeries = new DataSeries();
        DataSeries ds = dataSeries.newSeries();
        Object[] ticksValue = null;
        int x = 1;
        ticksValue = new Object[nodes.size()];
        labels = new String[nodes.size()];
        for (String key : nodes.keySet()) {
            ds.add(/*nodes.get(key).getIncludedDatasets().size()*/1, x);
            ticksValue[x - 1] = "Publication " + x;
            labels[x - 1] = ("<b><i><span style='color:#000000;font-size: 13px;'>" + nodes.get(key).getCommonAttr() + "</span><span style='color:rgb(59, 90, 122);font-size: 13px;'> [#Studies:" + nodes.get(key).getIncludedDatasets().size() + "]</span></i></b> ");

            
            x++;
        }
        seriesDefaults = options.getSeriesDefaults().setPointLabels(
                new PointLabels()
                .setShow(true).setLabels(labels).setLabelsFromSeries(false).setHideZeros(true).setFormatter(TickFormatters.DEFAULT).setLocation(PointLabelLocations.EAST).setEscapeHTML(false)
        );
        

        Ticks ticks = new Ticks().add(ticksValue);
        axes.getAxis(XYaxes.Y).setTicks(ticks);
        axes.getAxis(XYaxes.X).setMax(40);
        options.setAxes(axes);
        options.setSeriesDefaults(seriesDefaults);
        studiesChart.setOptions(options);
        studiesChart.setDataSeries(dataSeries);

    }
    
    private void selectPublication(int publicationIndex) {
        int index = 0;
        for (String str : labels) {
            if (str.contains("#B40404")) {
                str = str.replace("#B40404", "#000000");
                labels[index] = str;
               
            } 
            index++;
        }
        labels[publicationIndex] = labels[publicationIndex].replace("#000000", "#B40404");
        seriesDefaults = options.getSeriesDefaults();
        PointLabels pointsLabels = seriesDefaults.getPointLabels().setLabels(labels);
        seriesDefaults.setPointLabels(pointsLabels);
        options.setSeriesDefaults(seriesDefaults);
        studiesChart.setOptions(options);
        this.studiesChart.show();
    }

    @Override
    public void selectionChanged(String type) {
        if(type.equalsIgnoreCase("filter")){
        updateStudiesNodes(exploringFiltersManager.getFilteredDatasetsList());
//        updateTreeNodes();
        this.updateStudyChart();
        keepStudyInfoLayout = false;
        containerLayout.setVisible(false);
        this.currentSelectedStudy = null;
        this.studiesChart.show();
        }
        
    }

    @Override
    public String getFilterId() {
        return "Publication Layout";
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private Map<String, PublicationRoot> nodes = new HashMap<String, PublicationRoot>();
    private String[] nodeKeys;

    private void updateStudiesNodes(QuantDatasetObject[] updatedStudiesList) {
        nodes.clear();
        for (QuantDatasetObject pb : updatedStudiesList) {
            if (!nodes.containsKey(pb.getPumedID())) {
                nodes.put(pb.getPumedID(), getRootNode(pb.getPumedID(), updatedStudiesList));

            }

        }
        nodeKeys = new String[nodes.size()];
        int i = 0;
        for (String key : nodes.keySet()) {
            nodeKeys[i++] = key;
        }
        layout.updateFilterLabel("Publications Overview ("+ nodes.size()+"/"+updatedStudiesList.length+")");

    }

    private void updateTreeNodes() {
        publicationTree.removeAllItems();
        for (PublicationRoot root : nodes.values()) {
            String parent = root.getPublicationId() + "   ( " + root.getCommonAttr() + " )";
            publicationTree.addItem(parent);
            int x = 1;
            for (QuantDatasetObject pb : root.getIncludedDatasets()) {
                String child = root.getPublicationId() + " - Study " + (x++) + "   ( " + pb.getUniqueValues() + " )";
                publicationTree.addItem(child);
                publicationTree.setParent(child, parent);
                publicationTree.setChildrenAllowed(child, false);
            }

        }

//        for (final Object id : publicationTree.rootItemIds()) {
//            publicationTree.expandItemsRecursively(id);
//        }
    }

    private PublicationRoot getRootNode(String publicationId, QuantDatasetObject[] updatedStudiesList) {
        PublicationRoot root = new PublicationRoot();
        root.setPublicationId(publicationId);
        Set<QuantDatasetObject> studies = new HashSet<QuantDatasetObject>();
        for (QuantDatasetObject pb : updatedStudiesList) {
            if (pb.getPumedID().equalsIgnoreCase(publicationId)) {
                studies.add(pb);
            }

        }
        root.setIncludedStudies(studies);
        root = filterStudyRoot(root);

        return root;
    }

    private PublicationRoot filterStudyRoot(PublicationRoot root) {
        boolean[] attrCheck = new boolean[20];
        Object[][] attrValues = new Object[root.getIncludedDatasets().size()][20];

        int x = 0;
        for (QuantDatasetObject pb : root.getIncludedDatasets()) {
            attrValues[x] = pb.getValues();
            x++;
        }

        for (int y = 0; y < 20; y++) {
            for (int i = 0; i < attrValues.length; i++) {
                if (i == attrValues.length - 1) {
                    break;
                }
                if (!attrValues[i][y].toString().equalsIgnoreCase(attrValues[i + 1][y].toString())) {
                    attrCheck[y] = true;
                    break;

                }
            }
        }

        String comm = " ";
        for (int i = 0; i < 20; i++) {
            if (i == 19 || i == 2 || i == 3 || i == 5 || i == 6 || i == 16 || i == 17 || i == 18) {
                continue;
            }
            if (!attrCheck[i]) {
                Object prop = root.getIncludedDatasets().iterator().next().getProperty(i);
                if (!prop.toString().equalsIgnoreCase("Not Available") && !prop.toString().equalsIgnoreCase("-1000000000") && !prop.toString().trim().equalsIgnoreCase("")) {
                    comm += prop + " , ";
                }

            }
        }
        if (comm.length() >= 2) {
            comm = comm.substring(0, (comm.length() - 2));
        }

        root.setCommonAttr(comm);
        root.setAttrCheck(attrCheck);

        Set<QuantDatasetObject> studies = new HashSet<QuantDatasetObject>();
        for (QuantDatasetObject pb : root.getIncludedDatasets()) {
            studies.add(filterStudyChild(pb, attrCheck));
        }
        root.setIncludedStudies(studies);

        //set common values 
        QuantDatasetObject study = root.getIncludedDatasets().iterator().next();
        for (int i = 0; i < root.getAttrCheck().length; i++) {
            if (!root.getAttrCheck()[i] || i == 6) {
                root.setProperty(i, study.getProperty(i));

            }

        }

        return root;

    }

    private QuantDatasetObject filterStudyChild(QuantDatasetObject child, boolean[] attrCheck) {

        String unique = "";
        for (int i = 0; i < attrCheck.length; i++) {

            if (attrCheck[i]) {

                Object prop = child.getProperty(i);
                if (!prop.toString().equalsIgnoreCase("Not Available")) {
                    if (prop instanceof Integer) {
                        prop = headers[i] + prop;
                    }
                    if (prop.toString().contains("-1000000000")) {
                        prop = prop.toString().replace("-1000000000", "N/A");
                    }
                    unique += prop + " , ";
                }

            }
        }
        if (unique.length() >= 2) {
            unique = unique.substring(0, (unique.length() - 2));
        }
        child.setUniqueValues(unique);

        return child;
    }

    private Label authorLabel;
    private InformationField pumedId, filesNumber, identifiedProteinsNumber, quantifiedProteinsNumber, diseaseGroup, rawData, year, typeOfStudy, sampleType, sampleMatching, shotgunTargeted, technology, analyticalApproach, enzyme, quantificationBasis, quantBasisComment, patientsGroup1Number, patientsGroup2Number, normalization_strategy;

    private VerticalLayout containerLayout;
    private GridLayout commonInfoLayout;
    private VerticalLayout studyInfoLayout;
    private Component[] publicationInfoLayoutComponents = new Component[20];

    private Panel initStudyInformationLayout() {

        Panel containerPanel = new Panel();
        containerPanel.setHeight("530px");
        containerPanel.setStyleName("grayborder");
        containerLayout = new VerticalLayout();

        containerLayout.setSpacing(true);
        containerLayout.setMargin(false);

        containerPanel.setContent(containerLayout);
        //title author

        authorLabel = new Label(" ");
        authorLabel.setStyleName("titleLabel");
        authorLabel.setWidth(("test title".length() * 10) + "px");
        containerLayout.addComponent(authorLabel);
        containerLayout.setComponentAlignment(authorLabel, Alignment.MIDDLE_CENTER);
        publicationInfoLayoutComponents[0] = authorLabel;
        //toplayout
        GridLayout topLayout = new GridLayout(3, 1);
        topLayout.setWidth("100%");
        containerLayout.addComponent(topLayout);
         containerLayout.setComponentAlignment(topLayout, Alignment.MIDDLE_CENTER);

        pumedId = new InformationField("Pumed Id");
        topLayout.addComponent(pumedId);
        publicationInfoLayoutComponents[19] = pumedId;
        filesNumber = new InformationField("# Files");
        topLayout.addComponent(filesNumber);
        publicationInfoLayoutComponents[6] = filesNumber;
//        year = new InformationField("Year");
//        topLayout.addComponent(year);
        publicationInfoLayoutComponents[1] = year;
        rawData = new InformationField("Raw Data");
        topLayout.addComponent(rawData);
        publicationInfoLayoutComponents[5] = rawData;

        //common attr layout 
        commonInfoLayout = new GridLayout();
        commonInfoLayout.setWidth("100%");
        commonInfoLayout.setColumns(3);
        containerLayout.addComponent(commonInfoLayout);
        containerLayout.setComponentAlignment(commonInfoLayout, Alignment.MIDDLE_CENTER);
        //
        studyInfoLayout = new VerticalLayout();
        studyInfoLayout.setWidth("100%");
        studyInfoLayout.setSpacing(true);
        containerLayout.addComponent(studyInfoLayout);
        containerLayout.setComponentAlignment(studyInfoLayout, Alignment.MIDDLE_CENTER);
        //

        diseaseGroup = new InformationField("Disease Group");
        publicationInfoLayoutComponents[4] = diseaseGroup;

        typeOfStudy = new InformationField("Type of Study");
        publicationInfoLayoutComponents[7] = typeOfStudy;

        shotgunTargeted = new InformationField("Shotgun/Targeted");
        publicationInfoLayoutComponents[10] = shotgunTargeted;

        enzyme = new InformationField("Enzyme");
        publicationInfoLayoutComponents[13] = enzyme;

        patientsGroup1Number = new InformationField("#Patients Gr.I");
        publicationInfoLayoutComponents[16] = patientsGroup1Number;

        identifiedProteinsNumber = new InformationField("# Identified Proteins");
        publicationInfoLayoutComponents[2] = identifiedProteinsNumber;

        sampleType = new InformationField("Sample Type");
        publicationInfoLayoutComponents[8] = sampleType;

        technology = new InformationField("Technology");
        publicationInfoLayoutComponents[11] = technology;

        quantificationBasis = new InformationField("Quantification Basis");
        publicationInfoLayoutComponents[14] = quantificationBasis;
        patientsGroup2Number = new InformationField("#Patients Gr.II");
        quantifiedProteinsNumber = new InformationField("# Quantified Proteins");
        publicationInfoLayoutComponents[3] = quantifiedProteinsNumber;

        sampleMatching = new InformationField("Sample Matching");
        publicationInfoLayoutComponents[9] = sampleMatching;

        analyticalApproach = new InformationField("Analytical Approach");
        publicationInfoLayoutComponents[12] = analyticalApproach;

        quantBasisComment = new InformationField("Quantification BasisComment");
        publicationInfoLayoutComponents[15] = quantBasisComment;

        normalization_strategy = new InformationField("Normalization Strategy");
        publicationInfoLayoutComponents[18] = normalization_strategy;

        containerLayout.setVisible(false);
        return containerPanel;

    }

    private void updateStudyInformationLayout(PublicationRoot publication) {
        commonInfoLayout.removeAllComponents();
        studyInfoLayout.removeAllComponents();

        authorLabel.setValue("" + publication.getAuthor() + " (" + publication.getYear() + ")");
        authorLabel.setWidth((authorLabel.getValue().length() * 10) + "px");

        pumedId.setValue(publication.getPublicationId(), "http://www.ncbi.nlm.nih.gov/pubmed/" + publication.getPublicationId());
        filesNumber.setValue(publication.getFilesNumber() + "", null);
//        year.setValue(publication.getYear() + "", null);
        if (publication.getRawDataUrl() == null || publication.getRawDataUrl().equalsIgnoreCase("Raw Data Not Available")) {
            rawData.setValue("Not Available", null);
        } else {
            rawData.setValue(publication.getRawDataUrl(), publication.getRawDataUrl());
        }

        for (int i = 0; i < publication.getAttrCheck().length; i++) {
            if (i == 0 || i == 1 || i == 5 || i == 6 || i == 19) {
                continue;
            }
            boolean check = publication.getAttrCheck()[i];
            if (!check) {
                if (publicationInfoLayoutComponents[i] instanceof InformationField && !publication.getProperty(i).toString().trim().equalsIgnoreCase("Not Available") && !publication.getProperty(i).toString().trim().equalsIgnoreCase("0")) {
                    commonInfoLayout.addComponent(publicationInfoLayoutComponents[i]);
                    ((InformationField) publicationInfoLayoutComponents[i]).setValue(publication.getProperty(i).toString(), null);
                }

            }

        }
        if(publication.getIncludedDatasets().size() <= 1) {

            Button btn = new Button("Load Study");
            btn.setStyleName(Reindeer.BUTTON_SMALL);
            int rowNum = commonInfoLayout.getRows();
            if (commonInfoLayout.getComponentCount() % 3 != 0) {
                commonInfoLayout.addComponent(btn, 2, rowNum - 1);
            } else {
                commonInfoLayout.setRows(rowNum + 1);
                commonInfoLayout.addComponent(btn, 2, rowNum);
            }

            commonInfoLayout.setComponentAlignment(btn, Alignment.MIDDLE_CENTER);

        } else {
            int x = 1;
        for (QuantDatasetObject study : publication.getIncludedDatasets()) {
            studyInfoLayout.addComponent(new SingleDatasetPublicationLayout(study, x, publication.getAttrCheck()));
            x++;

        }
        }
    }

}
