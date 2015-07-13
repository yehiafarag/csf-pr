/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons;

import com.vaadin.server.Page;
import com.vaadin.ui.AbsoluteLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.entity.CategoryItemEntity;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import probe.com.model.beans.GroupsComparison;
import probe.com.view.core.jfreeutil.SquaredDot;

/**
 *
 * @author yfa041
 */
public class JFreeBarchartDivaWrapper extends AbsoluteLayout  {
    private final String defaultImgURL;
    private String inUseImgURL;
    private int imgWidth;
    private final String teststyle;
    private final Page.Styles styles = Page.getCurrent().getStyles();

    private final int[] protNumbers = new int[5];
    private JFreeChart barchart;
    private final Map<Integer, Set<String>> compProtMap;

    private final Color[] defaultColors = new Color[]{new Color(0, 153, 0), new Color(0, 229, 132), new Color(1, 141, 244), new Color(255, 51, 51), new Color(204, 0, 0)};
    private final Color[] grayColors = new Color[]{new Color(229, 229, 229), new Color(229, 229, 229), new Color(229, 229, 229), new Color(229, 229, 229), new Color(229, 229, 229)};//{"#B9E2B5","#D2EBDA","#E6F0FF","#EBC6CC","#EB9999"};
    private Color[] colorsInUse = defaultColors;
    private final String[] itemLabels = new String[5];
    private final String[] itemTotalLabels = new String[]{"", "", "", "", ""};
    private final Set<String> upAccessions, midUpAccessions, notRegAccessions, midDownAccessions, downAccessions, notDefinedAccession;

    public JFreeBarchartDivaWrapper(int imgWidth, GroupsComparison comparison) {

        this.setWidth(imgWidth + "px");
        this.setHeight("250px");
        this.imgWidth = imgWidth;
        this.upAccessions = new HashSet<String>();
        this.midUpAccessions = new HashSet<String>();
        this.notRegAccessions = new HashSet<String>();
        this.midDownAccessions = new HashSet<String>();
        this.downAccessions = new HashSet<String>();
        this.notDefinedAccession = new HashSet<String>();
        this.compProtMap = new HashMap<Integer, Set<String>>();
        this.defaultImgURL = initBarChart(imgWidth, 250, comparison);
        inUseImgURL = defaultImgURL;
        teststyle = comparison.getComparisonHeader().replace(" ", "_").replace(")", "_").replace("(", "_").toLowerCase() + "barchart";
        this.redrawChart();

    }

    private String initBarChart(int width, int height, GroupsComparison comparison) {
        Map<String, ComparisonProtein> protList = comparison.getComparProtsMap();
        double[] values = new double[5];
//        }
        for (String key2 : protList.keySet()) {
            ComparisonProtein prot = protList.get(key2);
            prot.updateLabelLayout();
            int indexer = prot.getTrindCategory();//(int) (prot.getCellValue() / maxIndexerValue * 10.0);
            switch (indexer) {
                case (0):
                    downAccessions.add(prot.getUniProtAccess());
                    break;
                case (1):
                    midDownAccessions.add(prot.getUniProtAccess());
                    break;
                case (2):
                    notRegAccessions.add(prot.getUniProtAccess());
                    break;
                case (3):
                    midUpAccessions.add(prot.getUniProtAccess());
                    break;
                case (4):
                    upAccessions.add(prot.getUniProtAccess());
                    break;
            }

            if (!compProtMap.containsKey(indexer)) {
                compProtMap.put(indexer, new HashSet<String>());
            }
            values[indexer] = (Double) values[indexer] + 1.0;
            Set<String> protSet = compProtMap.get(indexer);
            protSet.add(prot.getUniProtAccess());
            compProtMap.put(indexer, protSet);
        }
        protNumbers[0] = downAccessions.size();
        protNumbers[1] = midDownAccessions.size();
        protNumbers[2] = notRegAccessions.size();
        protNumbers[3] = midUpAccessions.size();
        protNumbers[4] = upAccessions.size();
        values = scaleValues(values, protList.size());

        DefaultCategoryDataset bardataset = new DefaultCategoryDataset();
        bardataset.setValue(values[0], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", "Down");
        bardataset.setValue(values[1], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", "");
        bardataset.setValue(values[2], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", "Not Regulated");
        bardataset.setValue(values[3], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", " ");
        bardataset.setValue(values[4], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", "Up");

        barchart = ChartFactory.createBarChart(
                null, //Title  
                null, // X-axis Label  
                "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", // Y-axis Label  
                bardataset, // Dataset  
                PlotOrientation.VERTICAL, //Plot orientation  
                false, // Show legend  
                false, // Use tooltips  
                false // Generate URLs  
        );
        // Set the colour of the title  
        Font titleFont = new Font("Verdana", Font.PLAIN, 12);
        TextTitle title = new TextTitle(comparison.getComparisonHeader() + " (Studies# " + comparison.getDatasetIndexes().length + ")", titleFont);
        title.setPaint(Color.BLACK);
        barchart.setTitle(title);
        barchart.setBackgroundPaint(Color.WHITE);    // Set the background colour of the chart  
        CategoryPlot cp = barchart.getCategoryPlot();  // Get the Plot object for a bar graph  

        cp.setOutlineVisible(false);
        BarRenderer renderer = new BarRenderer() {

            @Override
            public Paint getItemPaint(final int row, final int column) {
                return colorsInUse[column];
            }

            @Override
            public void drawItem(Graphics2D g2, CategoryItemRendererState state, Rectangle2D dataArea, CategoryPlot plot, CategoryAxis domainAxis, ValueAxis rangeAxis, CategoryDataset dataset, int row, int column, int pass) {
                super.drawItem(g2, state, dataArea, plot, domainAxis, rangeAxis, dataset, row, column, pass); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public CategoryItemLabelGenerator getItemLabelGenerator(int row, int column) {

                return super.getItemLabelGenerator(row, column); //To change body of generated methods, choose Tools | Templates.
            }

        };
        renderer.setShadowVisible(false);
        renderer.setBarPainter(new StandardBarPainter());
        renderer.setBaseItemLabelsVisible(true);
        renderer.setBaseItemLabelGenerator(new CategoryItemLabelGenerator() {

            @Override
            public String generateRowLabel(CategoryDataset cd, int i) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String generateColumnLabel(CategoryDataset cd, int i) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public String generateLabel(CategoryDataset cd, int i, int i1) {
                if (itemLabels[i1] == null) {
                    return "";
                }
                //i1 is the counter
                return itemLabels[i1];
            }
        });
        Font itemLabelFont = new Font("Verdana", Font.PLAIN, 10);
        renderer.setBaseItemLabelFont(itemLabelFont);
        renderer.setBaseItemLabelPaint(new Color(4, 95, 180));

        cp.setRenderer(renderer);
        Font axisFont = new Font("Verdana", Font.PLAIN, 10);

        CategoryAxis domainAxis = cp.getDomainAxis();
        domainAxis.setTickLabelFont(axisFont);
        domainAxis.setFixedDimension(100);
        domainAxis.setMaximumCategoryLabelWidthRatio(5.5f);

        NumberAxis rangeAxis = (NumberAxis) cp.getRangeAxis();
        rangeAxis.setTickLabelFont(axisFont);
        rangeAxis.setLabelFont(titleFont);
        rangeAxis.setUpperBound(0.8);
        NumberFormat nf = NumberFormat.getPercentInstance(Locale.US);
        nf.setMinimumFractionDigits(0);
        nf.setMaximumFractionDigits(1);
        rangeAxis.setNumberFormatOverride(nf);

        cp.setBackgroundPaint(Color.WHITE);       // Set the plot background colour  
        cp.setRangeGridlinePaint(Color.LIGHT_GRAY);      // Set the colour of the plot gridlines  

        String imgUrl = saveToFile(barchart, width, height);
        return imgUrl;

    }

    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();

    private String saveToFile(final JFreeChart chart, final double width, final double height) {
        byte imageData[];
        try {

            imageData = ChartUtilities.encodeAsPNG(chart.createBufferedImage((int) width, (int) height, chartRenderingInfo));
            this.removeAllComponents();
            int barCounter = 0;
            for (int i = 0; i < chartRenderingInfo.getEntityCollection().getEntityCount(); i++) {
                ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(i);
                if (entity instanceof CategoryItemEntity) {
                    CategoryItemEntity catEnt = (CategoryItemEntity) entity;
                    SquaredDot square = new SquaredDot();
                    String[] coords = catEnt.getShapeCoords().split(",");
                    int sqheight = Integer.valueOf(coords[3]) - Integer.valueOf(coords[1]);
                    if (sqheight < 2) {
                        barCounter++;
                        continue;
                    } else if (sqheight < 14) {
                        coords[1] = (Integer.valueOf(coords[1]) - (14 - sqheight)) + "";
                    }

                    int sqwidth = Integer.valueOf(coords[2]) - Integer.valueOf(coords[0]);
                    square.setWidth(sqwidth + "px");
                    square.setHeight(sqheight + "px");
                    square.setDescription("#Proteins " + protNumbers[barCounter++]);
                    square.setParam("barIndex", ((CategoryItemEntity) entity).getCategoryIndex());
                    this.addComponent(square, "left: " + coords[0] + "px; top: " + coords[1] + "px;");
                }
            }
            String base64 = Base64.encodeBase64String(imageData);
            base64 = "data:image/png;base64," + base64;
            return base64;
        } catch (IOException e) {
            System.err.println("at error " + e.getMessage());
        }
        return "";
    }

    public final void redrawChart() {
        styles.add("." + teststyle + " {  background-image: url(" + inUseImgURL + " );background-position:center; background-repeat: no-repeat; }");
        this.setStyleName(teststyle);
    }

    private double[] scaleValues(double[] vals, double listSize) {
        double[] result = new double[vals.length];
        double min = 0d;
        double max = listSize;
        double scaleFactor = max - min;
        // scaling between [0..1] for starters. Will generalize later.
        for (int x = 0; x < vals.length; x++) {
            result[x] = ((vals[x] - min) / scaleFactor) * 100.0;
            if (result[x] > 0) {
                result[x] = result[x] + 0.5;
            }
            result[x] = result[x] / 100.0;
            result[x] = Math.round(result[x] * 100.0) / 100.0;
        }
        return result;
    }

    public void updateExternalTableSelection(Set<String> accessions) {
        int downCounter = 0, midDownCounter = 0, notRegCounter = 0, midUpCounter = 0, upCounter = 0;
        for (String accession : accessions) {
            if (upAccessions.contains(accession)) {
                upCounter++;

            } else if (midUpAccessions.contains(accession)) {
                midUpCounter++;

            } else if (notRegAccessions.contains(accession)) {
                notRegCounter++;

            } else if (midDownAccessions.contains(accession)) {
                midDownCounter++;

            } else if (downAccessions.contains(accession)) {
                downCounter++;

            }

        }

        if (downCounter > 0 || !itemTotalLabels[0].equalsIgnoreCase("")) {
            itemLabels[0] = (downCounter + itemTotalLabels[0]);
        } else {
            itemLabels[0] = ("");
        }
        if (midDownCounter > 0 || !itemTotalLabels[1].equalsIgnoreCase("")) {
            itemLabels[1] = (midDownCounter + itemTotalLabels[1]);
        } else {
            itemLabels[1] = ("");
        }
        if (notRegCounter > 0 || !itemTotalLabels[2].equalsIgnoreCase("")) {
            itemLabels[2] = (notRegCounter + itemTotalLabels[2]);
        } else {
            itemLabels[2] = ("");
        }
        if (midUpCounter > 0 || !itemTotalLabels[3].equalsIgnoreCase("")) {
            itemLabels[3] = (midUpCounter + itemTotalLabels[3]);
        } else {
            itemLabels[3] = ("");
        }
        if (upCounter > 0 || !itemTotalLabels[4].equalsIgnoreCase("")) {
            itemLabels[4] = (upCounter + itemTotalLabels[4]);
        } else {
            itemLabels[4] = ("");
        }

        inUseImgURL = saveToFile(barchart, imgWidth, 250);
        redrawChart();

    }

    public void updateChartsWithSelectedChartColumn(Set<String> accessions, boolean reset) {
        if (reset) {
            colorsInUse = defaultColors;

        }
        itemLabels[0] = "";
        itemLabels[1] = "";
        itemLabels[2] = "";
        itemLabels[3] = "";
        itemLabels[4] = "";
        Set<String> tupAccessions, tmidUpAccessions, tnotRegAccessions, tmidDownAccessions, tdownAccessions;

        tupAccessions = new HashSet<String>(upAccessions);
        tupAccessions.retainAll(accessions);

        tmidUpAccessions = new HashSet<String>(midUpAccessions);
        tmidUpAccessions.retainAll(accessions);

        tnotRegAccessions = new HashSet<String>(notRegAccessions);
        tnotRegAccessions.retainAll(accessions);

        tmidDownAccessions = new HashSet<String>(midDownAccessions);
        tupAccessions.retainAll(accessions);

        tdownAccessions = new HashSet<String>(downAccessions);
        tdownAccessions.retainAll(accessions);

        if (tdownAccessions.isEmpty()) {
            itemTotalLabels[0] = "";
        } else {
            itemTotalLabels[0] = "/" + tdownAccessions.size();
            itemLabels[0] = (0 + itemTotalLabels[0]);
        }

        if (tmidDownAccessions.isEmpty()) {
            itemTotalLabels[1] = "";
        } else {
            itemTotalLabels[1] = "/" + tmidDownAccessions.size();
            itemLabels[1] = (0 + itemTotalLabels[1]);
        }

        if (tnotRegAccessions.isEmpty()) {
            itemTotalLabels[2] = "";
        } else {
            itemTotalLabels[2] = "/" + tnotRegAccessions.size();
            itemLabels[2] = (0 + itemTotalLabels[2]);
        }

        if (tmidUpAccessions.isEmpty()) {
            itemTotalLabels[3] = "";
        } else {
            itemTotalLabels[3] = "/" + tmidUpAccessions.size();
            itemLabels[3] = (0 + itemTotalLabels[3]);
        }

        if (tupAccessions.isEmpty()) {
            itemTotalLabels[4] = "";
        } else {
            itemTotalLabels[4] = "/" + tupAccessions.size();
            itemLabels[4] = (0 + itemTotalLabels[4]);
        }
        inUseImgURL = saveToFile(barchart, imgWidth, 250);
        redrawChart();

    }

    public void reset() {
        colorsInUse = defaultColors;
        for (int x = 0; x < itemTotalLabels.length; x++) {
            itemTotalLabels[x] = "";
            itemLabels[x] = "";
        }
        inUseImgURL = saveToFile(barchart, imgWidth, 250);
        redrawChart();

    }

    public void heighLightBar(int barIndex) {
        colorsInUse = new Color[grayColors.length];
        System.arraycopy(grayColors, 0, colorsInUse, 0, colorsInUse.length);
        colorsInUse[barIndex] = defaultColors[barIndex];
        inUseImgURL = saveToFile(barchart, imgWidth, 250);
        redrawChart();

    }

    public Map<Integer, Set<String>> getCompProtMap() {
        return compProtMap;
    }

    public void resize(int width) {
        imgWidth = width;
        this.setWidth(imgWidth + "px");

        inUseImgURL = saveToFile(barchart, imgWidth, 250);
        redrawChart();

    }

}
