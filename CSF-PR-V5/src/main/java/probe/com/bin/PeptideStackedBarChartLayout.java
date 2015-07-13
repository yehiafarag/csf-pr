/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.bin;

import com.vaadin.server.Page;
import com.vaadin.ui.AbsoluteLayout;
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
import java.util.List;
import java.util.Set;
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
import probe.com.model.beans.QuantPeptide;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 *
 * @author yfa041
 */
public class PeptideStackedBarChartLayout extends VerticalLayout {

//    private final String defaultLineChartImgUrl;
    private final Set<QuantPeptide> quantPepSet;
//    private final AbsoluteLayout lineChartContainer;
//    private final String teststyle;
//    private final Page.Styles styles = Page.getCurrent().getStyles();
//    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();

    public PeptideStackedBarChartLayout(int width, Set<QuantPeptide> quantPepSet,String compHeader, String protSequance,int x) {
        this.setWidth(width + "px");
        this.setHeight(250 + "px");
        this.setMargin(true);
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.quantPepSet = quantPepSet;
//        lineChartContainer = new AbsoluteLayout();
//        defaultLineChartImgUrl = generateLineChart((width), 250);
        
//        lineChartContainer.setWidth((width) + "px");
//        lineChartContainer.setHeight(250 + "px");
//        teststyle = compHeader.replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "linechart";
//        styles.add("." + teststyle + " {  background-image: url(" + defaultLineChartImgUrl + " );background-position:center; background-repeat: no-repeat; }");
//        lineChartContainer.setStyleName(teststyle);
    }

//   private String seq ="";
        
   

//    private String generateLineChart(int width, int height) {
//        int upcounter = 0;
//        int notcounter = 0;
//        int downcounter = 0;
//        int counter = 0;
//
//        for (QuantPeptide cp : quantPepSet) {
//            if (cp.getString_fc_value().trim().equalsIgnoreCase("") && cp.getFc_value() == -1000000000) {
//                System.out.println("at counter "+cp.getString_fc_value()+"   "+ cp.getFc_value() );
//                continue;
//            }
//            
//            if (cp.getString_fc_value().equalsIgnoreCase("Increased") ||cp.getString_fc_value().equalsIgnoreCase("Up") ) {
//                upcounter++;
//            } else if (cp.getString_fc_value()== null||cp.getString_fc_value().equalsIgnoreCase("")) {
//                System.out.println("not reg "+ cp.getFc_value());
//                notcounter++;
//            } else if (cp.getString_fc_value().equalsIgnoreCase("Decreased")||cp.getString_fc_value().equalsIgnoreCase("down")) {
//                downcounter++;
//            }
//            counter++;
//
//        }
//        
//        QuantPeptide[] inUseComparisonProteins = new QuantPeptide[counter];
//
//        DefaultXYDataset dataset = new DefaultXYDataset();
//
//        double[][] linevalues = new double[2][counter];
//
//        double[] xLineValues = new double[counter];
//        double[] yLineValues = new double[counter];
//
//        double[][] upvalues = new double[2][upcounter];
//        double[] xUpValues = new double[upcounter];
//        double[] yUpValues = new double[upcounter];
//
//        double[][] notvalues = new double[2][notcounter];
//        double[] xNotValues = new double[notcounter];
//        double[] yNotValues = new double[notcounter];
//
//        double[][] downvalues = new double[2][downcounter];
//        double[] xDownValues = new double[downcounter];
//        double[] yDownValues = new double[downcounter];
//
//        int upIndex = 0;
//        int notIndex = 0;
//        int downIndex = 0;
//
//        int pepIndex = 0;
//        int comparisonIndexer = 0;
//        for (QuantPeptide cp : quantPepSet) {
//             if(cp.getString_fc_value().trim().equalsIgnoreCase("") && cp.getFc_value()==-1000000000)
//                continue;
//
//            inUseComparisonProteins[pepIndex] = cp;
//            xLineValues[pepIndex] = comparisonIndexer;
//
//            if (cp.getString_fc_value().equalsIgnoreCase("Increased") || cp.getString_fc_value().equalsIgnoreCase("Up")) {
//                yLineValues[pepIndex] = 4d;
//                xUpValues[upIndex] = comparisonIndexer;
//                yUpValues[upIndex] = 4d;
//                upIndex++;
//            } else if (cp.getString_fc_value() == null || cp.getString_fc_value().equalsIgnoreCase("")) {
//                yLineValues[pepIndex] = 2d;
//                xNotValues[notIndex] = comparisonIndexer;
//                yNotValues[notIndex] = 2d;
//                notIndex++;
//            } else if (cp.getString_fc_value().equalsIgnoreCase("Decreased") || cp.getString_fc_value().equalsIgnoreCase("down")) {
//                yLineValues[pepIndex] = 0d;
//                xDownValues[downIndex] = comparisonIndexer;
//                yDownValues[downIndex] = 0d;
//                downIndex++;
//            }
//            else
//                System.out.println("at unknown else ");
//
//            pepIndex++;
//            comparisonIndexer++;
//
//        }
//
//        linevalues[0] = xLineValues;
//        linevalues[1] = yLineValues;
//        upvalues[0] = xUpValues;
//        upvalues[1] = yUpValues;
//        notvalues[0] = xNotValues;
//        notvalues[1] = yNotValues;
//        downvalues[0] = xDownValues;
//        downvalues[1] = yDownValues;
//
//        dataset.addSeries("line", linevalues);
//        dataset.addSeries("up", upvalues);
//        dataset.addSeries("not", notvalues);
//        dataset.addSeries("down", downvalues);
//
//        String[] xAxisLabels = new String[counter];
//        
//        for (int x = 0;x<counter;x++) {
//            xAxisLabels[x] = "Peptide "+(x+1);
//        }
//        SymbolAxis xAxis = new SymbolAxis(null, xAxisLabels) {
//
//            @Override
//            protected List refreshTicksHorizontal(Graphics2D g2, Rectangle2D dataArea, RectangleEdge edge) {
//
//                List ticks = new java.util.ArrayList();
//
//                Font tickLabelFont = getTickLabelFont();
//                g2.setFont(tickLabelFont);
//
//                double size = getTickUnit().getSize();
//                int count = calculateVisibleTickCount();
//                double lowestTickValue = calculateLowestVisibleTickValue();
//
//                double previousDrawnTickLabelPos = 0.0;
//                double previousDrawnTickLabelLength = 0.0;
//
//                if (count <= ValueAxis.MAXIMUM_TICK_COUNT) {
//                    for (int i = 0; i < count; i++) {
//                        double currentTickValue = lowestTickValue + (i * size);
//                        double xx = valueToJava2D(currentTickValue, dataArea, edge);
//                        String tickLabel;
//                        NumberFormat formatter = getNumberFormatOverride();
//                        if (formatter != null) {
//                            tickLabel = formatter.format(currentTickValue);
//                        } else {
//                            tickLabel = valueToString(currentTickValue);
//                        }
//
//                        // avoid to draw overlapping tick labels
//                        Rectangle2D bounds = TextUtilities.getTextBounds(tickLabel, g2,
//                                g2.getFontMetrics());
//                        double tickLabelLength = isVerticalTickLabels()
//                                ? bounds.getHeight() : bounds.getWidth();
//                        boolean tickLabelsOverlapping = false;
//                        if (i > 0) {
//                            double avgTickLabelLength = (previousDrawnTickLabelLength
//                                    + tickLabelLength) / 2.0;
//                            if (Math.abs(xx - previousDrawnTickLabelPos)
//                                    < avgTickLabelLength) {
//                                tickLabelsOverlapping = true;
//                            }
//                        }
//                        if (tickLabelsOverlapping) {
//                            setVerticalTickLabels(true);
////                            tickLabelLength = bounds.getHeight();
////                    tickLabel = ""; // don't draw this tick label
//                        } else {
//                            // remember these values for next comparison
//                            previousDrawnTickLabelPos = xx;
//                            previousDrawnTickLabelLength = tickLabelLength;
//                        }
//
//                        TextAnchor anchor;
//                        TextAnchor rotationAnchor;
//                        double angle = 0.0;
//                        if (isVerticalTickLabels()) {
//                            anchor = TextAnchor.CENTER_RIGHT;
//                            rotationAnchor = TextAnchor.CENTER_RIGHT;
//                            if (edge == RectangleEdge.TOP) {
//                                angle = 76.5;//Math.PI / 2.0;
//                            } else {
//                                angle = -76.5;//Math.PI / 2.0;
//                            }
//                        } else {
//                            if (edge == RectangleEdge.TOP) {
//                                anchor = TextAnchor.BOTTOM_CENTER;
//                                rotationAnchor = TextAnchor.BOTTOM_CENTER;
//                            } else {
//                                anchor = TextAnchor.TOP_CENTER;
//                                rotationAnchor = TextAnchor.TOP_CENTER;
//                            }
//                        }
//                        Tick tick = new NumberTick(new Double(currentTickValue),
//                                tickLabel, anchor, rotationAnchor, angle);
//                        ticks.add(tick);
//                    }
//                }
//                return ticks;
//            }
//
//        };
//
//        final Color[] labelsColor = new Color[]{new Color(80, 183, 71), Color.LIGHT_GRAY, new Color(205, 225, 255), Color.LIGHT_GRAY, Color.RED};
//        SymbolAxis yAxis = new SymbolAxis(null, new String[]{"Down Regulated", " ", "Not Regulated", " ", "Up Regulated"}) {
//            int x = 0;
//
//            @Override
//            public Paint getTickLabelPaint() {
//                if (x >= labelsColor.length) {
//                    x = 0;
//                }
//                return labelsColor[x++];//super.getTickLabelPaint(); //To change body of generated methods, choose Tools | Templates.
//            }
//
//        };
//        xAxis.setGridBandsVisible(false);
//        yAxis.setGridBandsVisible(false);
//        yAxis.setAxisLinePaint(Color.WHITE);
//
//        XYLineAndShapeRenderer renderer = new XYLineAndShapeRenderer();
//
////      XYDifferenceRenderer renderer = new XYDifferenceRenderer(new Color(255, 0, 0), new Color(80, 183, 71), true);
//        renderer.setSeriesPaint(0, Color.GRAY);// new Color(90, 162, 244));
//
//        renderer.setSeriesPaint(1, new Color(204, 0, 0));
//        renderer.setSeriesPaint(2, new Color(205, 225, 255));
//        renderer.setSeriesPaint(3, new Color(80, 183, 71));
//
//        Shape notRShape = ShapeUtilities.createDiamond(3f);
//        Shape leftArr = ShapeUtilities.createDownTriangle(3f);// ShapeUtilities.rotateShape(downArr, 1.6, downArr.getBounds().x, downArr.getBounds().y);
//        Shape rightArr = ShapeUtilities.createUpTriangle(3f);//ShapeUtilities.rotateShape(ShapeUtilities.createUpTriangle(5f), 1.6, downArr.getBounds().x, downArr.getBounds().y);
//        renderer.setSeriesShape(3, leftArr);
//        renderer.setSeriesShape(2, notRShape);
//        renderer.setSeriesShape(1, rightArr);
//        renderer.setSeriesShapesVisible(0, false);
//        renderer.setSeriesStroke(0, new BasicStroke(
//                1.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
//                1.0f, new float[]{10.0f, 6.0f}, 0.0f
//        ));
//
//        renderer.setSeriesLinesVisible(1, false);
//        renderer.setSeriesLinesVisible(2, false);
//        renderer.setSeriesLinesVisible(3, false);
//
//        XYPlot xyplot = new XYPlot(dataset, xAxis, yAxis, renderer);
//        xyplot.setRangeTickBandPaint(Color.WHITE);
//        JFreeChart jFreeChart = new JFreeChart(null, new Font("Tahoma", 0, 18), xyplot, false);
//        jFreeChart.setBackgroundPaint(Color.WHITE);
//        final XYPlot plot = jFreeChart.getXYPlot();
//        plot.setBackgroundPaint(Color.WHITE);
//        plot.setDomainGridlinePaint(Color.LIGHT_GRAY);
//        plot.setDomainGridlinesVisible(true);
//        plot.setRangeGridlinesVisible(false);
//        plot.setRangeGridlinePaint(Color.LIGHT_GRAY);
//        plot.setOutlinePaint(Color.GRAY);
//        jFreeChart.setBorderVisible(false);
//        String str = saveToFile(jFreeChart, width, height, chartRenderingInfo);
//        
//        for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
//            final ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
//            if (entity instanceof XYItemEntity && !((XYItemEntity) entity).getArea().toString().contains("java.awt.geom.Path2")) {
//                String[] arr = ((XYItemEntity) entity).getShapeCoords().split(",");
//                int xSer = Integer.valueOf(arr[10]);
//                int ySer = Integer.valueOf(arr[11]);
//
//                SquaredDot square = new SquaredDot();
//
//                square.setWidth(20 + "px");
//                square.setHeight(20 + "px");
//                QuantPeptide gc = inUseComparisonProteins[((XYItemEntity) entity).getItem()];
//                square.setDescription(gc.getPeptideSequance());
//                
//                
////                System.out.println("pep seq "+gc.getPeptideSequance());
//                
//                
//                square.setParam("GroupsComparison", gc);
//                lineChartContainer.addComponent(square, "left: " + (xSer - 10) + "px; top: " + (ySer - 10) + "px;");
//
//                MouseEvents.MouseOverListener mouseOverListener = new MouseEvents.MouseOverListener() {
//
//                    @Override
//                    public void mouseOver() {
//                        
//
//                    }
//                };
//                MouseEvents.MouseOutListener mouseOutListener = new MouseEvents.MouseOutListener() {
//
//                    @Override
//                    public void mouseOut() {
//                        
//                    }
//                };
//                final MouseEvents mouseEvents = MouseEvents.enableFor(square);
//                mouseEvents.addMouseOutListener(mouseOutListener);
//                mouseEvents.addMouseOverListener(mouseOverListener);
//
//            }
//
//        }
//
//       
//
//
//
//        return str;
//
//    }
//     private String saveToFile(final JFreeChart chart, final double width, final double height, ChartRenderingInfo chartRenderingInfo) {
//
//        try {
//
//            byte[] imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
//
//            String base64 = Base64.encodeBase64String(imageData);
//            base64 = "data:image/png;base64," + base64;
//            return base64;
//        } catch (IOException e) {
//            System.err.println("at error " + e.getMessage());
//        }
//        return "";
//
//    }

}
