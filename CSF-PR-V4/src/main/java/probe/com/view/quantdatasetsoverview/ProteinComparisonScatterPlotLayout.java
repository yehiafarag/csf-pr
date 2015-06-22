/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.quantdatasetsoverview;

import com.alsnightsoft.vaadin.widgets.canvasplus.CanvasPlus;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.io.IOException;
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
import probe.com.model.beans.ComparisonProtein;

/**
 *
 * @author Yehia Farag
 */
public class ProteinComparisonScatterPlotLayout extends GridLayout {

    private final Label comparisonTitle;
    private final VerticalLayout closeBtn;
    private final CanvasPlus ScatterPlotContainer;
    private final int imgWidth;

    public VerticalLayout getCloseBtn() {
        return closeBtn;
    }

    private String defaultScatterPlottImgUrl, heighlightedScatterPlottImgUrl;
    private final ChartRenderingInfo defaultScatterPlotRenderingInfo = new ChartRenderingInfo();

    public ProteinComparisonScatterPlotLayout(ComparisonProtein cp, int width) {

        this.setColumns(2);
        this.setRows(2);
        this.setSpacing(true);
        this.setMargin(new MarginInfo(false, false, true, false));
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidthUndefined();
        topLayout.setHeight("20px");
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
//        this.addComponent(topLayout);

        comparisonTitle = new Label(cp.getComparison().getComparisonHeader() + " (#Studies " + cp.getComparison().getDatasetIndexes().length + ")");
        comparisonTitle.setContentMode(ContentMode.HTML);
        comparisonTitle.setStyleName("custChartLabelHeader");
        comparisonTitle.setWidth((width - 115) + "px");
        this.addComponent(comparisonTitle,0,0);
        this.setComponentAlignment(comparisonTitle, Alignment.TOP_LEFT);

        closeBtn = new VerticalLayout();
        closeBtn.setWidth("20px");
        closeBtn.setHeight("20px");
        closeBtn.setStyleName("closebtn");
        this.addComponent(closeBtn,1,0);
        this.setComponentAlignment(closeBtn, Alignment.TOP_RIGHT);

        imgWidth = (width - 115);
        generateScatterplotchart(cp, imgWidth, 150);
        ScatterPlotContainer = new CanvasPlus();
        this.addComponent(ScatterPlotContainer,0,1);
        ScatterPlotContainer.setWidth(imgWidth + "px");
        ScatterPlotContainer.setHeight(150 + "px");
        ScatterPlotContainer.drawImage(defaultScatterPlottImgUrl, 0d, 0d, (double) imgWidth, 150.0);
        ScatterPlotContainer.addClickListener(new CanvasPlus.CanvasClickListener() {
            @Override
            public void onClick(MouseEventDetails mouseDetails) {

            }
        });
        ScatterPlotContainer.addMouseMoveListener(new CanvasPlus.CanvasMouseMoveListener() {
            @Override
            public void onMove(MouseEventDetails mouseDetails) {

//                ChartEntity entity = defaultScatterPlotRenderingInfo.getEntityCollection().getEntity(mouseDetails.getRelativeX(), mouseDetails.getRelativeY());
//                if (entity instanceof XYItemEntity) {
//                    ScatterPlotContainer.setStyleName("cursorcanvas");
//                    int x = ((XYItemEntity) entity).getSeriesIndex();
//                    int y = ((XYItemEntity) entity).getItem();
//
////                    updateLineChartTooltip(inUseComparisonProteins[y].getComparison().getComparisonHeader());//("X  " + ((XYItemEntity) entity).getDataset().getX(x, y)) + ("----- Y  " + ((XYItemEntity) entity).getDataset().getY(x, y)));
//                } else {
//                    ScatterPlotContainer.setStyleName("defaultcursorcanvas");
////                    updateLineChartTooltip(null);
//                }
            }
        });
        Label xAxisLabel = new Label("&nbsp;&nbsp;#Patients");
        xAxisLabel.setWidth("65px");
        xAxisLabel.setContentMode(ContentMode.HTML);
        this.addComponent(xAxisLabel, 1, 1);
        this.setComponentAlignment(xAxisLabel, Alignment.BOTTOM_RIGHT);

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
//                    lastUpI = i;
                }
            } else if (patNum.equalsIgnoreCase("down")) {
                for (int i : cp.getPatientsNumToTrindMap().get(patNum)) {
                    if (lastDownI != -1) {
                        lastDownI = lastDownI + 0.5;
                    } else {
                        lastDownI = i;
                    }
                    downSer.add(downCounter, i);
//                    lastDownI = i;
                }
            } else {
                for (int i : cp.getPatientsNumToTrindMap().get(patNum)) {
                    if (lastNotI != -1) {
                        lastNotI = lastNotI + 0.5;
                    } else {
                        lastNotI = i;
                    }
                    notSer.add(notCounter, lastNotI);
//                    lastNotI = i;
                }
            }

        }
        dataset.addSeries(upSer);
        dataset.addSeries(notSer);
        dataset.addSeries(downSer);

        final String[] labels = new String[]{" ", ("Down Regulated (" + cp.getDown() + ")"), " ", ("Not Regulated (" + cp.getNotProvided() + ")"), " ", ("Up Regulated (" + cp.getUp() + ")"), ""};
        final Color[] labelsColor = new Color[]{Color.LIGHT_GRAY, new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(205, 225, 255), Color.LIGHT_GRAY, Color.RED, Color.LIGHT_GRAY};
        final SymbolAxis domainAxis = new SymbolAxis("X", labels) {

            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= labels.length) {
                    x = 0;
                }
                return labelsColor[x++];//super.getTickLabelPaint(); //To change body of generated methods, choose Tools | Templates.
            }

        };
        domainAxis.setAutoRangeIncludesZero(false);
        Font f = new Font("Verdana", Font.PLAIN, 10);
        domainAxis.setTickLabelFont(f);
        domainAxis.setAutoRange(false);
        domainAxis.setLabel(null);
        domainAxis.setGridBandsVisible(false);
        String xTile = null;

        JFreeChart jFreeChart = ChartFactory.createScatterPlot(null, // jFreeChart
                // title
                null, // domain axis label
                xTile, // range axis label
                dataset, // data
                PlotOrientation.HORIZONTAL, // orientation
                false, // include legend
                false, // tooltips?
                false // URLs?
        );

        // NOW DO SOME OPTIONAL CUSTOMISATION OF THE CHART...
        // set the background color for the jFreeChart...
        jFreeChart.setBackgroundPaint(Color.WHITE);
        XYPlot plot = (XYPlot) jFreeChart.getPlot();

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
        
       
        va.setRange(0, 6);
        plot.setBackgroundPaint(Color.WHITE);

        renderer.setSeriesPaint(2, new Color(80, 183, 71));
        renderer.setSeriesPaint(1, new Color(205, 225, 255));
        renderer.setSeriesPaint(0, Color.RED);
        Shape notRShape = ShapeUtilities.createDiamond(5f);
        Shape leftArr = ShapeUtilities.createDownTriangle(5f);// ShapeUtilities.rotateShape(downArr, 1.6, downArr.getBounds().x, downArr.getBounds().y);
        Shape rightArr = ShapeUtilities.createUpTriangle(5f);//ShapeUtilities.rotateShape(ShapeUtilities.createUpTriangle(5f), 1.6, downArr.getBounds().x, downArr.getBounds().y);
        renderer.setSeriesShape(2, leftArr);
        renderer.setSeriesShape(1, notRShape);
        renderer.setSeriesShape(0, rightArr);
        jFreeChart.setBorderVisible(false);

        defaultScatterPlottImgUrl = saveToFile(jFreeChart, w, h, defaultScatterPlotRenderingInfo);
        plot.setBackgroundPaint(c);
        heighlightedScatterPlottImgUrl = saveToFile(jFreeChart, w, h, defaultScatterPlotRenderingInfo);
    }

    private String saveToFile(final JFreeChart chart, final double width, final double height, ChartRenderingInfo chartRenderingInfo) {
        byte imageData[] = null;
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

    public void highlight(boolean heighlight) {
        if (heighlight ) {
            ScatterPlotContainer.drawImage(heighlightedScatterPlottImgUrl, 0d, 0d, (double) imgWidth, 150.0);
        } else if(!heighlight){
            ScatterPlotContainer.drawImage(defaultScatterPlottImgUrl, 0d, 0d, (double) imgWidth, 150.0);
        }

    }
    public void redrawChart(){
    ScatterPlotContainer.drawImage(defaultScatterPlottImgUrl, 0d, 0d, (double) imgWidth, 150.0);
    
    }

}
