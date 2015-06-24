package probe.com.view.quantdatasetsoverview.quantcomparisontable;

import com.alsnightsoft.vaadin.widgets.canvasplus.CanvasPlus;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.MouseEventDetails;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
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
import org.jfree.chart.entity.XYItemEntity;
import org.jfree.chart.labels.CategoryItemLabelGenerator;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.renderer.category.CategoryItemRendererState;
import org.jfree.chart.renderer.category.StandardBarPainter;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import probe.com.model.beans.ComparisonProtein;
import probe.com.model.beans.GroupsComparison;

/**
 * this class is the container for the comparison bar chart the class developed
 * using JfreeChart and DiVA concept this class support both comparisons
 * selection and table data selection
 *
 * @author Yehia Farag
 */
public class JfreeComparisonBarChart extends HorizontalLayout {

    private final CanvasPlus chartImgContainer;
    private final VerticalLayout closeCompariosonBtn;
    private String defaultURL;
    private int imgWidth;
    private JFreeChart barchart;
    private final Map<Integer, Set<String>> compProtMap;
    private String[] downReglabels, midDownlabels, notReglabels, midUplabels, upReglabels;
    private final GroupsComparison comparison; 
    private final Color[] defaultColors = new Color[]{new Color(80, 183, 71), new Color(142, 204, 163), new Color(205, 225, 255), new Color(204, 112, 127), new Color(204, 0, 0)};
    private final Color[] grayColors = new Color[]{new Color(229,229,229),new Color(229,229,229),new Color(229,229,229),new Color(229,229,229),new Color(229,229,229)};//{"#B9E2B5","#D2EBDA","#E6F0FF","#EBC6CC","#EB9999"};
    private Color[] colorsInUse = defaultColors;
    
    public Map<Integer, Set<String>> getCompProtMap() {
        return compProtMap;
    }

    public void addCloseListiner(LayoutEvents.LayoutClickListener closeListener) {

        closeCompariosonBtn.addLayoutClickListener(closeListener);

    }

    public void addChartListener(CanvasPlus.CanvasClickListener chartListener) {
        this.chartImgContainer.addClickListener(chartListener);

    }

    public JfreeComparisonBarChart(GroupsComparison comparison, int width) {
        this.setStyleName("lightborder");
        this.setWidthUndefined();
        this.setHeight("250px");
        this.setMargin(false);
        this.imgWidth = width - 26;
        this.compProtMap = new HashMap<Integer, Set<String>>();
        this.defaultURL = initBarChart(imgWidth, 250, comparison);
        this.comparison=comparison;
        chartImgContainer = new CanvasPlus();
        this.addComponent(chartImgContainer);
        chartImgContainer.setWidth(imgWidth + "px");
        chartImgContainer.setHeight("250px");
        redrawChart();
        closeCompariosonBtn = new VerticalLayout();
        closeCompariosonBtn.setWidth("20px");
        closeCompariosonBtn.setHeight("20px");
        closeCompariosonBtn.setStyleName("closebtn");

        this.addComponent(closeCompariosonBtn);
        this.chartImgContainer.addMouseMoveListener(new CanvasPlus.CanvasMouseMoveListener() {
            @Override
            public void onMove(MouseEventDetails mouseDetails) {

                ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(mouseDetails.getRelativeX(), mouseDetails.getRelativeY());
                if (entity instanceof CategoryItemEntity) {
                    chartImgContainer.setStyleName("cursorcanvas");

//                    updateLineChartTooltip(inUseComparisonProteins[y].getComparison().getComparisonHeader());//("X  " + ((XYItemEntity) entity).getDataset().getX(x, y)) + ("----- Y  " + ((XYItemEntity) entity).getDataset().getY(x, y)));
                } else {
                    chartImgContainer.setStyleName("defaultcursorcanvas");
//                    updateLineChartTooltip(null);
                }

            }
        });

    }

    private String initBarChart(int w, int h, GroupsComparison comparison) {

        Map<String, ComparisonProtein> protList = comparison.getComparProtsMap();
        double[] values = new double[5];
        downReglabels = new String[(values.length)];
        midDownlabels = new String[(values.length)];
        notReglabels = new String[(values.length)];
        midUplabels = new String[values.length];
        upReglabels = new String[(values.length)];
        double maxIndexerValue = 0.0;

        //init values 
        for (String key2 : protList.keySet()) {
            ComparisonProtein prot = protList.get(key2);
            prot.updateLabelLayout();
            if (maxIndexerValue < Math.abs(prot.getCellValue())) {
                maxIndexerValue = Math.abs(prot.getCellValue());
            }

        }
        for (String key2 : protList.keySet()) {
            ComparisonProtein prot = protList.get(key2);
            int indexer = (int) (prot.getCellValue() / maxIndexerValue * 10.0);
//            indexer = indexer + 10;
            if (indexer == 10) {
                indexer = 4;
            } else if (indexer < 10 && indexer > 0) {
                indexer = 3;
            } else if (indexer == 0) {
                indexer = 2;
            } else if (indexer < 0 && indexer > -10) {
                indexer = 1;
            } else if (indexer == -10) {
                indexer = 0;
            }

            if (!compProtMap.containsKey(indexer)) {
                compProtMap.put(indexer, new HashSet<String>());
            }
            values[indexer] = (Double) values[indexer] + 1.0;
            Set<String> protSet = compProtMap.get(indexer);
            protSet.add(prot.getUniProtAccess());
            compProtMap.put(indexer, protSet);
        }

        values = scaleValues(values, protList.size());

        DefaultCategoryDataset bardataset = new DefaultCategoryDataset();
        bardataset.setValue(values[0], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", "Down");
        bardataset.setValue(values[1], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", "");
        bardataset.setValue(values[2], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", "Not Regulated");
        bardataset.setValue(values[3], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", " ");
        bardataset.setValue(values[4], "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", "Up");

//     DefaultCategoryDataset bardataset = new DefaultCategoryDataset();  
//     bardataset.setValue(6,"Marks" ,"Aditi" );  
//     bardataset.setValue(3,"Marks" ,"Pooja" );  
//     bardataset.setValue(10,"Marks" ,"Ria" );  
//     bardataset.setValue(5,"Marks" ,"Twinkle" );  
//     bardataset.setValue(20,"Marks" ,"Rutvi" );  
        barchart = ChartFactory.createBarChart(
                null, //Title  
                null, // X-axis Label  
                "Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", // Y-axis Label  
                bardataset, // Dataset  
                PlotOrientation.VERTICAL, //Plot orientation  
                false, // Show legend  
                true, // Use tooltips  
                false // Generate URLs  
        );
        // Set the colour of the title  
        Font titleFont = new Font("Verdana", Font.PLAIN, 12);
        TextTitle title = new TextTitle("Proteins Percentage (total# " + comparison.getComparProtsMap().size() + ")", titleFont);
        title.setPaint(Color.BLACK);
        barchart.setTitle(title);
        barchart.setBackgroundPaint(Color.WHITE);    // Set the background colour of the chart  
        CategoryPlot cp = barchart.getCategoryPlot();  // Get the Plot object for a bar graph  

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
                if (labels[i1] == null) {
                    return "";
                }
                //i1 is the counter
                return labels[i1];
            }
        });
        renderer.setBaseItemLabelFont(titleFont);
         renderer.setBaseItemLabelPaint(Color.BLUE);
        
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

        String imgUrl = saveToFile(barchart, w, h);
        return imgUrl;

    }

    private ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();

    private String saveToFile(final JFreeChart chart, final double width, final double height) {
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

    public final void redrawChart() {
        chartImgContainer.drawImage(defaultURL, 0d, 0d, (double) imgWidth, 250.0);

    }

    public String getUrl() {

        return defaultURL;
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
        }
        return result;
    }

    private final Set<Integer> lastselectedIndex = new HashSet<Integer>();
    private final Set<Integer> lastLabelIndex = new HashSet<Integer>();
    private String lastTotalUpIndexer = "", lastTotalMidUpIndexer = "", lastTotalNotIndexer = "", lastTotalmidDownIndexer = "", lastTotalDownIndexer = "";
    private final String[] labels = new String[5];

    public void updateSelection(Set<String> accessions, boolean tableSelection, boolean sortedChart) {
        colorsInUse = defaultColors;
        if (!tableSelection) {
            lastTotalUpIndexer = "";
            lastTotalMidUpIndexer = "";
            lastTotalNotIndexer = "";
            lastTotalmidDownIndexer = "";
            lastTotalDownIndexer = "";
            lastLabelIndex.clear();
        }
        List<Integer> selectedIndexes = new ArrayList<Integer>();
        for (int index : compProtMap.keySet()) {
            for (String accession : accessions) {
                if (compProtMap.get(index).contains(accession)) {
                    selectedIndexes.add(index);
                }
            }
        }

        for (int z : lastselectedIndex) {
            if (z == 0) {
                downReglabels[z] = " ";
            } else if (z == 1) {
                midDownlabels[z] = " ";
            } else if (z == 2) {
                notReglabels[z] = " ";
            } else if (z == 3) {
                midUplabels[z] = " ";

            } else if (z == 4) {
                upReglabels[z] = " ";
            }
        }
        lastselectedIndex.clear();

        int[] tdownvalueslabel = new int[downReglabels.length];
        int[] tmiddownvalueslabel = new int[downReglabels.length];
        int[] tnotvalueslabel = new int[notReglabels.length];
        int[] tmidupvalueslabel = new int[upReglabels.length];
        int[] tupvalueslabel = new int[upReglabels.length];
        for (int z : selectedIndexes) {
            if (z == 0) {
                tdownvalueslabel[z] = tdownvalueslabel[z] + 1;
            } else if (z == 1) {
                tmiddownvalueslabel[z] = tmiddownvalueslabel[z] + 1;
            } else if (z == 2) {
                tnotvalueslabel[z] = tnotvalueslabel[z] + 1;
            } else if (z == 3) {
                tmidupvalueslabel[z] = tmidupvalueslabel[z] + 1;

            } else if (z == 4) {
                tupvalueslabel[z] = tupvalueslabel[z] + 1;
            }
        }
        lastselectedIndex.addAll(selectedIndexes);

        for (int x : lastselectedIndex) {

            if (x == 0) {
                if (tableSelection) {
                    downReglabels[x] =  tdownvalueslabel[x] + lastTotalDownIndexer;

                } else {
                    if (tdownvalueslabel[x] > 0) {
                        lastTotalDownIndexer = "/" + tdownvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    downReglabels[x] = tdownvalueslabel[x]+"" ;
                }
            } else if (x == 1) {
                if (tableSelection) {
                    midDownlabels[x] =  tmiddownvalueslabel[x] + lastTotalmidDownIndexer ;
                } else {
                    if (tmiddownvalueslabel[x] > 0) {
                        lastTotalmidDownIndexer = "/" + tmiddownvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    midDownlabels[x] = ""+tmiddownvalueslabel[x] ;
                }
            } else if (x == 2) {
                if (tableSelection) {
                    notReglabels[x] = tnotvalueslabel[x] + lastTotalNotIndexer ;
                } else {
                    if (tnotvalueslabel[x] > 0) {
                        lastTotalNotIndexer = "/" + tnotvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    notReglabels[x] =  tnotvalueslabel[x] + "";

                }

            } else if (x == 3) {
                if (tableSelection) {
                    midUplabels[x] =  tmidupvalueslabel[x] + lastTotalMidUpIndexer ;
                } else {
                    if (tmidupvalueslabel[x] > 0) {
                        lastTotalMidUpIndexer = "/" + tmidupvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    midUplabels[x] = tmidupvalueslabel[x] + "";
                }

            } else if (x == 4) {
                if (tableSelection) {
                    upReglabels[x] =  tupvalueslabel[x] + lastTotalUpIndexer ;
                } else {
                    if (tupvalueslabel[x] > 0) {
                        lastTotalUpIndexer = "/" + tupvalueslabel[x];
                        lastLabelIndex.add(x);
                    }
                    upReglabels[x] =  tupvalueslabel[x] + "";
                }
            }

        }
        for (int x : lastLabelIndex) {
            if (lastselectedIndex.contains(x)) {
                continue;
            }
            if (x == 0) {
                if (tableSelection) {
                    downReglabels[x] = lastTotalDownIndexer + "";
                }
            } else if (x == 1) {
                if (tableSelection) {
                    midDownlabels[x] =  lastTotalmidDownIndexer + "";
                }
            } else if (x == 2) {
                if (tableSelection) {
                    notReglabels[x] = lastTotalNotIndexer;
                }
            } else if (x == 3) {
                if (tableSelection) {
                    midUplabels[x] =  lastTotalMidUpIndexer ;
                }
            } else if (x == 4) {
                if (tableSelection) {
                    upReglabels[x] =  lastTotalUpIndexer ;
                }

            }
        }

        labels[0] = (downReglabels[0]);
        labels[1] = (downReglabels[1]);
        labels[2] = (downReglabels[2]);
        labels[3] = (downReglabels[3]);
        labels[4] = (downReglabels[4]);
        defaultURL = saveToFile(barchart, imgWidth, 250);
        redrawChart();

    }

    public void resizeChart(int width) {
        imgWidth = width-26;
        chartImgContainer.setWidth(imgWidth+"px");
        redrawChart();
       
        

    }

    public int getBarIndex(int x, int y) {

        ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(x, y);

        if (entity instanceof CategoryItemEntity) {
           return ((CategoryItemEntity) entity).getCategoryIndex();
        }
        return -1;

    }

    public String getComparisonHeader() {
        return comparison.getComparisonHeader();

    }

    public void setHeghlighted(boolean hl) {
        if (hl) {
            this.setStyleName("lightselectedborder");
        } else {
            this.setStyleName("lightborder");
        }

    }

    public void heighLightBar(int barIndex) {
        colorsInUse = new Color[grayColors.length];
        System.arraycopy(grayColors, 0, colorsInUse, 0, colorsInUse.length);

        colorsInUse[barIndex] = defaultColors[barIndex];
        defaultURL = saveToFile(barchart, imgWidth, 250);
        redrawChart();

    }

}
