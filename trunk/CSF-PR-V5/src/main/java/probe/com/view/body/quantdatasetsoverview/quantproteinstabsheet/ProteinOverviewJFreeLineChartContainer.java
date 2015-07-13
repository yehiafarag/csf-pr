package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberTick;
import org.jfree.chart.axis.SymbolAxis;
import org.jfree.chart.axis.Tick;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.text.TextUtilities;
import org.jfree.ui.RectangleEdge;
import org.jfree.ui.TextAnchor;
import org.jfree.util.ShapeUtilities;
import org.vaadin.marcus.MouseEvents;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import probe.com.model.beans.GroupsComparison;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 * Interactive JfreeChart
 *
 * @author Yehia Farag
 */
public class ProteinOverviewJFreeLineChartContainer extends HorizontalLayout {

    private final int height;
    private final String defaultLineChartImgUrl;
    private String orderedLineChartImg;
    private final ChartRenderingInfo defaultLineChartRenderingInfo = new ChartRenderingInfo();
    private final ChartRenderingInfo orderedLineChartRenderingInfo = new ChartRenderingInfo();
    private String thumbChart = "";

    public String getThumbChart() {
        return thumbChart;
    }

    private int width;
    private final OptionGroup orederingOptionGroup = new OptionGroup();
    private final ProteinStudiesComparisonsContainerLayout studiesScatterChartsLayout;
    private final AbsoluteLayout lineChartContainer;
    private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();

    public OptionGroup getOrederingOptionGroup() {
        return orederingOptionGroup;
    }
    private final LayoutEvents.LayoutClickListener chartListener;

    public ProteinOverviewJFreeLineChartContainer(DatasetExploringCentralSelectionManager selectionManager,  final ComparisonProtein[] comparisonProteins, final Set<GroupsComparison> selectedComparisonList, int widthValue, String protId) {

        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.setSpacing(true);
        this.setHeightUndefined();

        height = 400;
        width = widthValue / 2;

        VerticalLayout leftSideLayout = new VerticalLayout();
        leftSideLayout.setWidth("100%");
        leftSideLayout.setHeightUndefined();
        leftSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        leftSideLayout.setSpacing(true);
        leftSideLayout.setMargin(new MarginInfo(true, true, false, true));
        this.addComponent(leftSideLayout);

        VerticalLayout rightSideLayout = new VerticalLayout();
        rightSideLayout.setWidth("100%");
        rightSideLayout.setHeightUndefined();
        rightSideLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        rightSideLayout.setSpacing(true);
        rightSideLayout.setMargin(new MarginInfo(true, false, false, true));
        this.addComponent(rightSideLayout);
        lineChartContainer = new AbsoluteLayout();

        //init leftside components - linechart 
        defaultLineChartImgUrl = generateLineChart(comparisonProteins, selectedComparisonList, (width - 100), height, defaultLineChartRenderingInfo);
        leftSideLayout.addComponent(lineChartContainer);
        lineChartContainer.setWidth((width - 100) + "px");
        lineChartContainer.setHeight(height + "px");

        teststyle = protId.replace(" ", "_").replace(")", "_").replace("(", "_").replace(";", "_").toLowerCase() + "linechart";
        styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        lineChartContainer.setStyleName(teststyle);
        chartListener = new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                Component c = event.getClickedComponent();
                if (c instanceof SquaredDot) {
                    GroupsComparison gc = (GroupsComparison) ((SquaredDot) c).getParam("GroupsComparison");
                    studiesScatterChartsLayout.highlightComparison(gc, true);
                } else {
                    studiesScatterChartsLayout.highlightComparison(null, false);
                }

            }
        };
        lineChartContainer.addLayoutClickListener(chartListener);

        orederingOptionGroup.setWidth("250px");
        orederingOptionGroup.setNullSelectionAllowed(false); // user can not 'unselect'
        orederingOptionGroup.setMultiSelect(false);
        orederingOptionGroup.addItem("Default order");
        orederingOptionGroup.addItem("Trend order");
        orederingOptionGroup.setValue("Default order");
        orederingOptionGroup.addStyleName("horizontal");
        orederingOptionGroup.addValueChangeListener(new Property.ValueChangeListener() {
            private ComparisonProtein[] ordComparisonProteins;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (orederingOptionGroup.getValue().toString().equalsIgnoreCase("Default order")) {
                    styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
                    lineChartContainer.setStyleName(teststyle);

                    studiesScatterChartsLayout.orderComparisons(comparisonProteins);

                } else {
                    if (orderedLineChartImg == null) {
                        //order the comparisons and proteins
                        TreeMap<String, ComparisonProtein> orderedCompProteins = new TreeMap<String, ComparisonProtein>();
                        LinkedHashSet<GroupsComparison> orederedComparisonSet = new LinkedHashSet<GroupsComparison>();
                        for (ComparisonProtein cp : comparisonProteins) {
                            if (cp == null) {
                                continue;
                            }
                            if (cp.getCellValue() < 0 && cp.getCellValue() > -1) {
                                orderedCompProteins.put((cp.getCellValue() - 1) + "-" + cp.getComparison().getComparisonHeader(), cp);
                            } else {
                                orderedCompProteins.put((cp.getCellValue()) + "-" + cp.getComparison().getComparisonHeader(), cp);
                            }
                        }
                        ordComparisonProteins = new ComparisonProtein[orderedCompProteins.size()];
                        int i = 0;
                        for (ComparisonProtein cp : orderedCompProteins.values()) {
                            ordComparisonProteins[i] = cp;
                            orederedComparisonSet.add(cp.getComparison());
                            i++;
                        }
                        for (GroupsComparison gv : selectedComparisonList) {
                            if (!orederedComparisonSet.contains(gv)) {
                                orederedComparisonSet.add(gv);
                            }

                        }

                        orderedLineChartImg = generateLineChart(ordComparisonProteins, orederedComparisonSet, (width - 100), height, orderedLineChartRenderingInfo);
                        studiesScatterChartsLayout.orderComparisons(ordComparisonProteins);
                    }
                    styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
                    lineChartContainer.setStyleName(teststyle);
                }
            }
        });
        VerticalLayout dsInfoPopupContainerLayout = new VerticalLayout();
        dsInfoPopupContainerLayout.setWidth((width - 100) + "px");
        dsInfoPopupContainerLayout.setHeight(400 + "px");
        dsInfoPopupContainerLayout.setStyleName(Reindeer.LAYOUT_WHITE);

//        init rightside components 
        studiesScatterChartsLayout = new ProteinStudiesComparisonsContainerLayout(comparisonProteins, selectedComparisonList, selectionManager, width);
        rightSideLayout.addComponent(studiesScatterChartsLayout);
        studiesScatterChartsLayout.setWidth(width * 2 + "px");
    }

    private ComparisonProtein[] inUseComparisonProteins;

    private String generateLineChart(ComparisonProtein[] comparisonProteins, Set<GroupsComparison> selectedComparisonList, double w, double h, ChartRenderingInfo chartRenderingInfo) {
        int upcounter = 0;
        int notcounter = 0;
        int downcounter = 0;
        int counter = 0;

        for (ComparisonProtein cp : comparisonProteins) {
            if (cp == null) {
                continue;
            }

            if (cp.getCellValue() > 0) {
                upcounter++;
            } else if (cp.getCellValue() == 0) {
                notcounter++;
            } else if (cp.getCellValue() < 0) {
                downcounter++;
            }
            counter++;

        }
        if (counter == 1) {
            orederingOptionGroup.setEnabled(false);

        }
        inUseComparisonProteins = new ComparisonProtein[counter];

        DefaultXYDataset dataset = new DefaultXYDataset();

        double[][] linevalues = new double[2][counter];

        double[] xLineValues = new double[counter];
        double[] yLineValues = new double[counter];

        double[][] upvalues = new double[2][upcounter];
        double[] xUpValues = new double[upcounter];
        double[] yUpValues = new double[upcounter];

        double[][] notvalues = new double[2][notcounter];
        double[] xNotValues = new double[notcounter];
        double[] yNotValues = new double[notcounter];

        double[][] downvalues = new double[2][downcounter];
        double[] xDownValues = new double[downcounter];
        double[] yDownValues = new double[downcounter];

        int upIndex = 0;
        int notIndex = 0;
        int downIndex = 0;

        int compIndex = 0;
        int comparisonIndexer = 0;
        for (ComparisonProtein cp : comparisonProteins) {
            if (cp == null) {
                comparisonIndexer++;
                continue;
            } else {
                inUseComparisonProteins[compIndex] = cp;
                xLineValues[compIndex] = comparisonIndexer;

                if (cp.getTrindCategory() == 4) {
                    yLineValues[compIndex] = 4d;
                    xUpValues[upIndex] = comparisonIndexer;
                    yUpValues[upIndex] = 4d;
                    upIndex++;
                } else if (cp.getTrindCategory() == 3) {
                    xUpValues[upIndex] = comparisonIndexer;
                    yUpValues[upIndex] = 3d;
                    upIndex++;
                    yLineValues[compIndex] = 3d;
                } else if (cp.getTrindCategory() == 2) {
                    yLineValues[compIndex] = 2d;
                    xNotValues[notIndex] = comparisonIndexer;
                    yNotValues[notIndex] = 2d;
                    notIndex++;
                } else if (cp.getTrindCategory() == 1) {
                    yLineValues[compIndex] = 1d;
                    xDownValues[downIndex] = comparisonIndexer;
                    yDownValues[downIndex] = 1d;
                    downIndex++;
                } else if (cp.getTrindCategory() == 0) {
                    yLineValues[compIndex] = 0d;
                    xDownValues[downIndex] = comparisonIndexer;
                    yDownValues[downIndex] = 0d;
                    downIndex++;
                }

            }
            compIndex++;
            comparisonIndexer++;

        }

        linevalues[0] = xLineValues;
        linevalues[1] = yLineValues;
        upvalues[0] = xUpValues;
        upvalues[1] = yUpValues;
        notvalues[0] = xNotValues;
        notvalues[1] = yNotValues;
        downvalues[0] = xDownValues;
        downvalues[1] = yDownValues;

        dataset.addSeries("line", linevalues);
        dataset.addSeries("up", upvalues);
        dataset.addSeries("not", notvalues);
        dataset.addSeries("down", downvalues);

        String[] xAxisLabels = new String[selectedComparisonList.size()];
        int x = 0;
        for (GroupsComparison comp : selectedComparisonList) {
            xAxisLabels[x] = comp.getComparisonHeader();
            x++;

        }
        SymbolAxis xAxis = new SymbolAxis(null, xAxisLabels) {
            @Override
            protected List refreshTicksHorizontal(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {
                List ticks = new java.util.ArrayList();
                Font tickLabelFont = getTickLabelFont();
                g2.setFont(tickLabelFont);
                double size = getTickUnit().getSize();
                int count = calculateVisibleTickCount();
                double lowestTickValue = calculateLowestVisibleTickValue();
                double previousDrawnTickLabelPos = 0.0;
                double previousDrawnTickLabelLength = 0.0;
                if (count <= ValueAxis.MAXIMUM_TICK_COUNT) {
                    for (int i = 0; i < count; i++) {
                        double currentTickValue = lowestTickValue + (i * size);
                        double xx = valueToJava2D(currentTickValue, dataArea, edge);
                        String tickLabel;
                        NumberFormat formatter = getNumberFormatOverride();
                        if (formatter != null) {
                            tickLabel = formatter.format(currentTickValue);
                        } else {
                            tickLabel = valueToString(currentTickValue);
                        }
                        // avoid to draw overlapping tick labels
                        Rectangle2D bounds = TextUtilities.getTextBounds(tickLabel, g2,
                                g2.getFontMetrics());
                        double tickLabelLength = isVerticalTickLabels()
                                ? bounds.getHeight() : bounds.getWidth();
                        boolean tickLabelsOverlapping = false;
                        if (i > 0) {
                            double avgTickLabelLength = (previousDrawnTickLabelLength
                                    + tickLabelLength) / 2.0;
                            if (Math.abs(xx - previousDrawnTickLabelPos)
                                    < avgTickLabelLength) {
                                tickLabelsOverlapping = true;
                            }
                        }
                        if (tickLabelsOverlapping) {
                            setVerticalTickLabels(true);
                        } else {
                            // remember these values for next comparison
                            previousDrawnTickLabelPos = xx;
                            previousDrawnTickLabelLength = tickLabelLength;
                        }
                        TextAnchor anchor;
                        TextAnchor rotationAnchor;
                        double angle = 0.0;
                        if (isVerticalTickLabels()) {
                            anchor = TextAnchor.CENTER_RIGHT;
                            rotationAnchor = TextAnchor.CENTER_RIGHT;
                            if (edge == RectangleEdge.TOP) {
                                angle = 76.5;
                            } else {
                                angle = -76.5;
                            }
                        } else {
                            if (edge == RectangleEdge.TOP) {
                                anchor = TextAnchor.BOTTOM_CENTER;
                                rotationAnchor = TextAnchor.BOTTOM_CENTER;
                            } else {
                                anchor = TextAnchor.TOP_CENTER;
                                rotationAnchor = TextAnchor.TOP_CENTER;
                            }
                        }
                        Tick tick = new NumberTick(new Double(currentTickValue),
                                tickLabel, anchor, rotationAnchor, angle);
                        ticks.add(tick);
                    }
                }
                return ticks;
            }
        };
        final Color[] labelsColor = new Color[]{new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(1, 141, 244), Color.LIGHT_GRAY, Color.RED};
        SymbolAxis yAxis = new SymbolAxis(null, new String[]{"Down Regulated", " ", "Not Regulated", " ", "Up Regulated"}) {
            int x = 0;

            @Override
            public Paint getTickLabelPaint() {
                if (x >= labelsColor.length) {
                    x = 0;
                }
                return labelsColor[x++];
            }
        };
        xAxis.setGridBandsVisible(false);
        yAxis.setGridBandsVisible(false);
        yAxis.setAxisLinePaint(Color.WHITE);

        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
        renderer.setSeriesPaint(0, Color.GRAY);

        renderer.setSeriesPaint(1, new Color(204, 0, 0));
        renderer.setSeriesPaint(2, new Color(1, 141, 244));
        renderer.setSeriesPaint(3, new Color(80, 183, 71));

        Shape notRShape = ShapeUtilities.createDiamond(6f);
        Shape leftArr = ShapeUtilities.createDownTriangle(6f);
        Shape rightArr = ShapeUtilities.createUpTriangle(6f);
        renderer.setSeriesShape(3, leftArr);
        renderer.setSeriesShape(2, notRShape);
        renderer.setSeriesShape(1, rightArr);
        renderer.setSeriesShapesVisible(0, false);
        renderer.setSeriesStroke(0, new BasicStroke(
                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
                1.0f, new float[]{10.0f, 6.0f}, 0.0f
        ));

        renderer.setSeriesLinesVisible(1, false);
        renderer.setSeriesLinesVisible(2, false);
        renderer.setSeriesLinesVisible(3, false);

        XYPlot xyplot = new XYPlot(dataset, xAxis, yAxis, renderer) {
            private int x = 0;

            @Override
            public Paint getRangeGridlinePaint() {
                if (x >= 5) {
                    x = 0;
                }
                if (x == 2) {
                    x++;
                    return super.getRangeGridlinePaint();
                } else {
                    x++;
                    return Color.WHITE;
                }
            }
        };

        xyplot.setRangeTickBandPaint(Color.WHITE);
        JFreeChart jFreeChart = new JFreeChart(null, new Font("Tahoma", 0, 18), xyplot, false);
        jFreeChart.setBackgroundPaint(Color.WHITE);
        final XYPlot plot = jFreeChart.getXYPlot();
        plot.setBackgroundPaint(Color.WHITE);
        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
        plot.setDomainGridlinesVisible(true);
        plot.setRangeGridlinesVisible(true);
        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
        plot.setOutlinePaint(Color.GRAY);
        jFreeChart.setBorderVisible(false);
        String str = saveToFile(jFreeChart, w, h, chartRenderingInfo);
        for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
            final ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
            if (entity instanceof XYItemEntity && !((XYItemEntity) entity).getArea().toString().contains("java.awt.geom.Path2")) {
                String[] arr = ((XYItemEntity) entity).getShapeCoords().split(",");
                int xSer = Integer.valueOf(arr[10]);
                int ySer = Integer.valueOf(arr[11]);
                SquaredDot square = new SquaredDot();
                square.setWidth(20 + "px");
                square.setHeight(20 + "px");
                GroupsComparison gc = inUseComparisonProteins[((XYItemEntity) entity).getItem()].getComparison();
                square.setDescription(gc.getComparisonHeader());
                square.setParam("GroupsComparison", gc);
                lineChartContainer.addComponent(square, "left: " + (xSer - 7) + "px; top: " + (ySer - 10) + "px;");
                MouseEvents.MouseOverListener mouseOverListener = new MouseEvents.MouseOverListener() {
                    private final GroupsComparison gc = inUseComparisonProteins[((XYItemEntity) entity).getItem()].getComparison();

                    @Override
                    public void mouseOver() {
                        studiesScatterChartsLayout.highlightComparison(gc, false);
                    }
                };
                MouseEvents.MouseOutListener mouseOutListener = new MouseEvents.MouseOutListener() {
                    @Override
                    public void mouseOut() {
                        studiesScatterChartsLayout.highlightComparison(null, false);
                    }
                };
                final MouseEvents mouseEvents = MouseEvents.enableFor(square);
                mouseEvents.addMouseOutListener(mouseOutListener);
                mouseEvents.addMouseOverListener(mouseOverListener);
            }
        }
        if (thumbChart.equalsIgnoreCase("")) {
            plot.setDomainGridlinesVisible(false);
            plot.setRangeGridlinesVisible(false);
            yAxis.setVisible(false);
            xAxis.setVisible(false);
            plot.setOutlineVisible(false);
            notRShape = ShapeUtilities.createDiamond(2f);
            leftArr = ShapeUtilities.createDownTriangle(2f);// ShapeUtilities.rotateShape(downArr, 1.6, downArr.getBounds().x, downArr.getBounds().y);
            rightArr = ShapeUtilities.createUpTriangle(2f);//ShapeUtilities.rotateShape(ShapeUtilities.createUpTriangle(5f), 1.6, downArr.getBounds().x, downArr.getBounds().y);
            renderer.setSeriesShape(3, leftArr);
            renderer.setSeriesShape(2, notRShape);
            renderer.setSeriesShape(1, rightArr);
            renderer.setSeriesShapesVisible(1, false);
            renderer.setSeriesShapesVisible(2, false);
            renderer.setSeriesShapesVisible(3, false);
            renderer.setSeriesPaint(0, Color.DARK_GRAY);
            renderer.setSeriesStroke(0, null);
            thumbChart = saveToFile(jFreeChart, (selectedComparisonList.size() * 15), 35, null);
        }

        return str;
    }

    private String saveToFile(final JFreeChart chart, final double width, final double height, ChartRenderingInfo chartRenderingInfo) {
        try {
            byte[] imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";

    }

    public void redrawCharts() {
        if (orederingOptionGroup.getValue().toString().equalsIgnoreCase("Default order")) {
            styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
        } else {
            styles.add("." + teststyle + " {  background-image: url(" + orderedLineChartImg + " );background-position:center; background-repeat: no-repeat; }");
        }
        lineChartContainer.setStyleName(teststyle);
        studiesScatterChartsLayout.redrawCharts();

    }
}
