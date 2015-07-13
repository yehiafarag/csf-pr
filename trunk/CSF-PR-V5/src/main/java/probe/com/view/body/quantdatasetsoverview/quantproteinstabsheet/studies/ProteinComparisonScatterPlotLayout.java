/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.studies;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.util.List;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.util.ShapeUtilities;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 *
 * @author Yehia Farag
 */
public class ProteinComparisonScatterPlotLayout extends GridLayout {

    private final Label comparisonTitle;
    private final VerticalLayout closeBtn;
    private final AbsoluteLayout ProteinScatterPlotContainer;
    private final int imgWidth;

    public VerticalLayout getCloseBtn() {
        return closeBtn;
    }

    private String defaultScatterPlottImgUrl, heighlightedScatterPlottImgUrl;
    private final ChartRenderingInfo defaultScatterPlotRenderingInfo = new ChartRenderingInfo();
    private final DatasetExploringCentralSelectionManager exploringFiltersManager;
    private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();
    private final ComparisonProtein comparisonProtein;
    private final PeptidesStackedBarChartsControler peptidesOverviewLayoutManager;

    public ProteinComparisonScatterPlotLayout(final ComparisonProtein cp, int width, DatasetExploringCentralSelectionManager exploringFiltersManagerinst) {
        this.exploringFiltersManager = exploringFiltersManagerinst;
        this.setColumns(4);
        this.setRows(2);
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, true, false));
        this.comparisonProtein = cp;
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        topLayout.setHeight("20px");
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        int numb = cp.getDown() + cp.getNotProvided() + cp.getNotReg() + cp.getUp();

        comparisonTitle = new Label(cp.getComparison().getComparisonHeader() + " (#Studies " + numb + "/" + cp.getComparison().getDatasetIndexes().length + ")");
        comparisonTitle.setContentMode(ContentMode.HTML);
        comparisonTitle.setStyleName("custChartLabelHeader");
        comparisonTitle.setWidth((width - 70) + "px");
        this.addComponent(comparisonTitle, 1, 0);
        this.setComponentAlignment(comparisonTitle, Alignment.TOP_LEFT);

        closeBtn = new VerticalLayout();
        closeBtn.setWidth("20px");
        closeBtn.setHeight("20px");
        closeBtn.setStyleName("closebtn");
        this.addComponent(closeBtn, 2, 0);
        this.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);

        imgWidth = (width - 70);
        ProteinScatterPlotContainer = new AbsoluteLayout();
        this.generateScatterplotchart(cp, imgWidth, 150);

        this.addComponent(ProteinScatterPlotContainer, 1, 1);
        ProteinScatterPlotContainer.setWidth(imgWidth + "px");
        ProteinScatterPlotContainer.setHeight(150 + "px");

        teststyle = cp.getUniProtAccess().replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "_" + cp.getComparison().getComparisonHeader().replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "_scatterplot";
        styles.add("." + teststyle + " {  background-image: url(" + defaultScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        ProteinScatterPlotContainer.setStyleName(teststyle);
        ProteinScatterPlotContainer.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                final ChartEntity entity = defaultScatterPlotRenderingInfo.getEntityCollection().getEntity(event.getRelativeX(), event.getRelativeY());
                if (entity instanceof XYItemEntity) {
                    int x = ((XYItemEntity) entity).getSeriesIndex();
                    int y = ((XYItemEntity) entity).getItem();
                    QuantDatasetObject ds = exploringFiltersManager.getFullDatasetArr()[comparisonProtein.getDSID(x, y)];
                    exploringFiltersManager.setSelectedDataset(ds.getUniqId());
                    exploringFiltersManager.setStudyLevelFilterSelection(new CSFFilterSelection("StudySelection", new int[]{ds.getUniqId()}, "scatter", null));//               
                    peptidesOverviewLayoutManager.updateSelectedProteinInformation(ds.getUniqId(), ds, cp.getUniProtAccess());
                }

            }
        });
        peptidesOverviewLayoutManager = new PeptidesStackedBarChartsControler(width, cp);
    }

    public Label getComparisonTitle() {
        return comparisonTitle;
    }

    /**
     * Creates a sample jFreeChart.
     *
     * @param dataset the dataset.
     *
     * @return The jFreeChart.
     */
    private void generateScatterplotchart(ComparisonProtein cp, int w, int h) {

        final XYSeriesCollection dataset = new XYSeriesCollection();
        XYSeries downSer = new XYSeries(0);
        XYSeries notSer = new XYSeries(1);
        XYSeries upSer = new XYSeries(2);
        double downCounter = 1;
        double notCounter = 3;
        double upCounter = 5;
        double lastUpI = -1.0;
        double lastNotI = -1.0;
        double lastDownI = -1.0;
        for (String patNum : cp.getPatientsNumToTrindMap().keySet()) {
            if (patNum.equalsIgnoreCase("up")) {
                for (int i : cp.getPatientsNumToTrindMap().get(patNum)) {
                    if (lastUpI != -1) {
                        lastUpI = lastUpI + 0.5;
                    } else {
                        lastUpI = i;
                    }
                    upSer.add(upCounter, (lastUpI));
                }
            } else if (patNum.equalsIgnoreCase("down")) {
                for (int i : cp.getPatientsNumToTrindMap().get(patNum)) {
                    if (lastDownI != -1) {
                        lastDownI = lastDownI + 0.5;
                    } else {
                        lastDownI = i;
                    }
                    downSer.add(downCounter, lastDownI);
                }
            } else {
                for (int i : cp.getPatientsNumToTrindMap().get(patNum)) {
                    if (lastNotI != -1) {
                        lastNotI = lastNotI + 0.5;
                    } else {
                        lastNotI = i;
                    }
                    notSer.add(notCounter, lastNotI);
                }
            }

        }
        dataset.addSeries(upSer);
        dataset.addSeries(notSer);
        dataset.addSeries(downSer);
        final String[] labels = new String[]{" ", ("Down Regulated (" + cp.getDown() + ")"), " ", ("Not Regulated (" + cp.getNotProvided() + ")"), " ", ("Up Regulated (" + cp.getUp() + ")"), ""};
        final Color[] labelsColor = new Color[]{Color.LIGHT_GRAY, new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, Color.RED, Color.LIGHT_GRAY};
        final SymbolAxis domainAxis = new SymbolAxis("X", labels) {
            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= labels.length) {
                    x = 0;
                }
                return labelsColor[x++];
            }

        };
        domainAxis.setAutoRangeIncludesZero(false);
        Font f = new Font("Verdana", Font.PLAIN, 10);
        domainAxis.setTickLabelFont(f);
        domainAxis.setAutoRange(false);
        domainAxis.setLabel(null);
        domainAxis.setGridBandsVisible(false);
        String xTile = "#Patients";

        JFreeChart jFreeChart = ChartFactory.createScatterPlot(null,
                null, // domain axis label
                null, // range axis label
                dataset, // data
                PlotOrientation.HORIZONTAL, // orientation
                false, // include legend
                false, // tooltips?
                false // URLs?
        );
        XYPlot plot1 = (XYPlot) jFreeChart.getPlot();
        XYPlot plot = new XYPlot(dataset, plot1.getDomainAxis(), plot1.getRangeAxis(), plot1.getRenderer()) {
            @Override
            protected void drawDomainGridlines(Graphics2D g2, Rectangle2D dataArea, List ticks) {
                super.drawDomainGridlines(g2, dataArea, ticks); //To change body of generated methods, choose Tools | Templates.
            }
            private int x = 0;

            @Override
            public Paint getDomainGridlinePaint() {
                if (x >= labels.length) {
                    x = 0;
                }
                if (x == 1 || x == 3 || x == 5) {
                    x++;
                    return Color.WHITE;
                } else {
                    x++;
                    return super.getDomainGridlinePaint(); //To change body of generated methods, choose Tools | Templates.
                }
            }
        };
        plot.setOrientation(PlotOrientation.HORIZONTAL);
        JFreeChart scatter = new JFreeChart(plot);
        scatter.setBackgroundPaint(Color.WHITE);
        scatter.getLegend().setVisible(false);
        Color c = new Color(242, 242, 242);
        plot.setDomainAxis(domainAxis);
        plot.setDomainGridlinePaint(Color.GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.GRAY);
        plot.setOutlinePaint(Color.GRAY);
        XYItemRenderer renderer = plot.getRenderer();
        ValueAxis va = plot.getDomainAxis();
        va.setAutoRange(false);
        va.setMinorTickCount(0);
        va.setVisible(true);

        plot.getRangeAxis().setRange(0, 100);
        NumberAxis rangeAxis = (NumberAxis) plot.getRangeAxis();
        rangeAxis.setTickUnit(new NumberTickUnit(10));
        rangeAxis.setLabel(xTile);
        rangeAxis.setLabelFont(f);
        rangeAxis.setLabelPaint(Color.GRAY);

        va.setRange(0, 6);
        plot.setBackgroundPaint(Color.WHITE);

        renderer.setSeriesPaint(2, new Color(80, 183, 71));
        renderer.setSeriesPaint(1, new Color(1, 141, 244));
        renderer.setSeriesPaint(0, new Color(204, 0, 0));
        Shape notRShape = ShapeUtilities.createDiamond(5f);
        Shape leftArr = ShapeUtilities.createDownTriangle(5f);
        Shape rightArr = ShapeUtilities.createUpTriangle(5f);
        renderer.setSeriesShape(2, leftArr);
        renderer.setSeriesShape(1, notRShape);
        renderer.setSeriesShape(0, rightArr);
        scatter.setBorderVisible(false);
        defaultScatterPlottImgUrl = saveToFile(scatter, w, h, defaultScatterPlotRenderingInfo);
        for (int i = 0; i < defaultScatterPlotRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            final ChartEntity entity = defaultScatterPlotRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity) {
                System.out.println("coords "+((XYItemEntity) entity).getShapeCoords());
                String[] arr = ((XYItemEntity) entity).getShapeCoords().split(",");
                int xSer = Integer.valueOf(arr[0]);
                int ySer = Integer.valueOf(arr[1]);
                 int ySerEnd = Integer.valueOf(arr[3]);
                int x = ((XYItemEntity) entity).getSeriesIndex();
                int y = ((XYItemEntity) entity).getItem();
                QuantDatasetObject ds = exploringFiltersManager.getFullDatasetArr()[comparisonProtein.getDSID(x, y)];
                SquaredDot square = new SquaredDot();
                square.setWidth(10 + "px");
                square.setHeight(10 + "px");
                square.setDescription(ds.getAuthor() + " (" + ds.getYear() + ")");
                square.setParam("dsIndex", ds.getUniqId());
                square.setParam("QuantDatasetObject", ds);
                int top = (ySer - 4);
                if(ySer>ySerEnd)
                    top=ySerEnd-3;
                ProteinScatterPlotContainer.addComponent(square, "left: " + (xSer-5 ) + "px; top: " + top + "px;");
            }
        }
        plot.setBackgroundPaint(c);
        heighlightedScatterPlottImgUrl = saveToFile(scatter, w, h, defaultScatterPlotRenderingInfo);
    }

    private String saveToFile(final JFreeChart chart, final double width, final double height, ChartRenderingInfo chartRenderingInfo) {
        byte imageData[];
        try {
            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";

    }
    private boolean isclicked;

    public void highlight(boolean heighlight, boolean clicked) {

        if (heighlight) {
            styles.add("." + teststyle + " {  background-image: url(" + heighlightedScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");

        } else if (!heighlight) {
            if (!isclicked) {
                styles.add("." + teststyle + " {  background-image: url(" + defaultScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");
            } else {
                isclicked = false;
            }
        }
        if (clicked) {
            isclicked = true;
            this.getUI().scrollIntoView(this.ProteinScatterPlotContainer);
        }

    }

    public void redrawChart() {
        styles.add("." + teststyle + " {  background-image: url(" + defaultScatterPlottImgUrl + " );background-position:center; background-repeat: no-repeat; }");

    }

    public String getUrl() {

        return heighlightedScatterPlottImgUrl;
    }

}
