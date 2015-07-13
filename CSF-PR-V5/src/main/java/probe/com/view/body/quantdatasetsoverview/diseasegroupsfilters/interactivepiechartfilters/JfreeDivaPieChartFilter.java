/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.interactivepiechartfilters;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.ui.VerticalLayout;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.io.IOException;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.commons.codec.binary.Base64;
import org.jfree.chart.ChartRenderingInfo;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.entity.ChartEntity;
import org.jfree.chart.entity.PieSectionEntity;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.title.TextTitle;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

/**
 *
 * @author yfa041
 */
public class JfreeDivaPieChartFilter extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private final String defaultImgURL, teststyle;
    private String inUseImgURL;
    private final Color selectedColor = new Color(59, 90, 122);
    private final Color lightGreen = new Color(10, 255, 14);
    private final Color[] defaultColors = new Color[]{new Color(110, 177, 206), new Color(219, 169, 1), new Color(213, 8, 8), new Color(4, 180, 95), new Color(174, 180, 4), lightGreen, new Color(244, 250, 88), new Color(255, 0, 64), new Color(246, 216, 206), new Color(189, 189, 189), new Color(255, 128, 0), Color.WHITE};

    private final Map<String, Color> defaultKeyColorMap = new HashMap<String, Color>();
    private final Page.Styles styles = Page.getCurrent().getStyles();
    private String[] labels;
    private int[] values;
    private final Map<String, String> valuesMap = new HashMap<String, String>();
    private final String filter_Id;
    private final PieChartsSelectionManager Local_Filter_Manager;
    private final int width;
    private final int height;
    private final Map<String, List<Integer>> dsIndexesMap;
    private Map<String, List<Integer>> inuseDsIndexesMap;
    private final HashSet<Integer> selectedDsIds = new HashSet<Integer>();

    public JfreeDivaPieChartFilter(String filterId, int filterIndex, PieChartsSelectionManager Local_Filter_Manager, Map<String, List<Integer>> dsIndexesMap, int filterWidth) {
        this.filter_Id = filterId;
        this.Local_Filter_Manager = Local_Filter_Manager;
        this.dsIndexesMap = dsIndexesMap;
        this.inuseDsIndexesMap = dsIndexesMap;

        this.updateLabelsAndValues(null, false);
        for (int z = 0; z < labels.length; z++) {
            defaultKeyColorMap.put(labels[z], defaultColors[z]);
        }
        width = filterWidth;
        height = 300;
        this.setWidth(width + "px");
        this.setHeight(height + "px");
        this.defaultImgURL = initPieChart(width, height);
        teststyle = "pieChartCssfilter_" + filterId;
        styles.add("." + teststyle + " :focus { outline: none !important;}");
        inUseImgURL = defaultImgURL;
        redrawChart();
        this.addLayoutClickListener(JfreeDivaPieChartFilter.this);
        Local_Filter_Manager.registerFilter(JfreeDivaPieChartFilter.this);
    }

    private void updateLabelsAndValues(Set<Integer> dsIndexes, boolean reset) {
        if (dsIndexes == null || dsIndexes.isEmpty()) {
            this.labels = new String[inuseDsIndexesMap.size()];
            this.values = new int[inuseDsIndexesMap.size()];
            int x = 0;
            int notAvailableIndex = -1;
            for (String str : inuseDsIndexesMap.keySet()) {
                if (str.equalsIgnoreCase("Not Available")) {
                    notAvailableIndex = x;
                }
                labels[x] = str;
                values[x] = inuseDsIndexesMap.get(str).size();
                valuesMap.put(labels[x], values[x] + "");
                x++;
            }
            if (notAvailableIndex != -1) {
                labels[notAvailableIndex] = labels[labels.length - 1];
                labels[labels.length - 1] = "Not Available";
                int notAvaiValue = values[notAvailableIndex];
                values[notAvailableIndex] = values[values.length - 1];
                values[values.length - 1] = notAvaiValue;
                defaultColors[labels.length - 1] = Color.LIGHT_GRAY;
            }

        } else {

            Map<String, List<Integer>> filteredDssMap = new HashMap<String, List<Integer>>();
            Map<String, Integer> filteredDsIndexesMap = new HashMap<String, Integer>();
            for (int y : dsIndexes) {
                for (String key : dsIndexesMap.keySet()) {
                    if (dsIndexesMap.get(key).contains(y)) {
                        if (!filteredDsIndexesMap.containsKey(key)) {
                            filteredDsIndexesMap.put(key, 0);
                            filteredDssMap.put(key, new ArrayList<Integer>());
                        }
                        int val = filteredDsIndexesMap.get(key);
                        val++;
                        List<Integer> dsIds = filteredDssMap.get(key);
                        dsIds.add(y);
                        filteredDsIndexesMap.put(key, val);
                        filteredDssMap.put(key, dsIds);
                    }
                }

            }

            this.labels = new String[filteredDsIndexesMap.size()];
            this.values = new int[filteredDsIndexesMap.size()];
            int x = 0;
            int notAvailableIndex = -1;

            for (String str : filteredDsIndexesMap.keySet()) {
                if (str.equalsIgnoreCase("Not Available")) {
                    notAvailableIndex = x;
                }
                labels[x] = str;
                values[x] = filteredDsIndexesMap.get(str);
                valuesMap.put(labels[x], values[x] + "");
                x++;
            }
            if (notAvailableIndex != -1) {
                labels[notAvailableIndex] = labels[labels.length - 1];
                labels[labels.length - 1] = "Not Available";
                int notAvaiValue = values[notAvailableIndex];
                values[notAvailableIndex] = values[values.length - 1];
                values[values.length - 1] = notAvaiValue;
            }
            if (reset) {
                inuseDsIndexesMap = filteredDssMap;
            }

        }

    }

    public String getFilter_Id() {
        return filter_Id;
    }

    private PiePlot plot;
    private JFreeChart chart;

    private String initPieChart(int width, int height) {

        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int x = 0; x < labels.length; x++) {
            dataset.setValue(labels[x], new Double(values[x]));

        }
        plot = new PiePlot(dataset);
        plot.setNoDataMessage("No data available");
        plot.setCircular(true);

        plot.setLabelGap(0);

        plot.setLabelFont(new Font("Verdana", Font.BOLD, 15));
        plot.setLabelGenerator(new PieSectionLabelGenerator() {

            @Override
            public String generateSectionLabel(PieDataset pd, Comparable cmprbl) {
                return valuesMap.get(cmprbl.toString());
            }

            @Override
            public AttributedString generateAttributedSectionLabel(PieDataset pd, Comparable cmprbl) {
                throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        plot.setSimpleLabels(true);

        plot.setLabelBackgroundPaint(null);
        plot.setLabelShadowPaint(null);
        plot.setLabelPaint(Color.WHITE);
        plot.setLabelOutlinePaint(null);
        plot.setBackgroundPaint(Color.WHITE);
        plot.setInteriorGap(0);
        plot.setShadowPaint(Color.WHITE);
        plot.setOutlineVisible(false);
        plot.setBaseSectionOutlinePaint(Color.WHITE);
        plot.setSectionOutlinesVisible(true);
        plot.setBaseSectionOutlineStroke(new BasicStroke(1.2f));
        plot.setInteriorGap(0.05);
        for (String label : labels) {
            plot.setSectionPaint(label, defaultKeyColorMap.get(label));
        }

        chart = new JFreeChart(plot);
        chart.setTitle(new TextTitle(Local_Filter_Manager.getFilterTitle(filter_Id), new Font("Verdana", Font.BOLD, 13)));
        chart.setBorderPaint(null);
        chart.setBackgroundPaint(null);
        chart.getLegend().setFrame(BlockBorder.NONE);
        chart.getLegend().setItemFont(new Font("Verdana", Font.PLAIN, 12));
        String imgUrl = saveToFile(chart, width, height);
        return imgUrl;

    }
    private final ChartRenderingInfo chartRenderingInfo = new ChartRenderingInfo();

    private String saveToFile(final JFreeChart chart, final double width, final double height) {
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

    public final void redrawChart() {
        styles.add("." + teststyle + " {  background-image: url(" + inUseImgURL + " );background-position:center; background-repeat: no-repeat; cursor: pointer; }");
        this.setStyleName(teststyle);
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        ChartEntity entity = chartRenderingInfo.getEntityCollection().getEntity(event.getRelativeX(), event.getRelativeY());

        if (entity != null && entity instanceof PieSectionEntity) {
            PieSectionEntity pieEnt = (PieSectionEntity) entity;
            selectSlice(pieEnt.getSectionKey().toString());

        }
    }

    private void selectSlice(String sliceKey) {

        if (plot.getExplodePercent(sliceKey) == 0.1) {
            plot.setExplodePercent(sliceKey, 0);
            plot.setSectionPaint(sliceKey, defaultKeyColorMap.get(sliceKey));
            selectedDsIds.removeAll(inuseDsIndexesMap.get(sliceKey));
            if (selectedDsIds.isEmpty()) {
                Local_Filter_Manager.jfreeUpdateChartsFilters("");
                return;
            }
        } else {

            plot.setExplodePercent(sliceKey, 0.1);
            plot.setSectionPaint(sliceKey, selectedColor);
            selectedDsIds.addAll(inuseDsIndexesMap.get(sliceKey));

        }

        Local_Filter_Manager.jfreeUpdateChartsFilters(filter_Id);
        inUseImgURL = saveToFile(chart, width, height);
        redrawChart();

    }

    public String updatePieChart(int width, int height) {
        System.out.println("update piechart");
        DefaultPieDataset dataset = new DefaultPieDataset();
        for (int x = 0; x < labels.length; x++) {
            dataset.setValue(labels[x], new Double(values[x]));

        }
        plot.setDataset(dataset);
        String chartImgUrl = saveToFile(chart, width, height);
        return chartImgUrl;

    }

    public void selectionChanged(Set<Integer> dsIndexes) {
        updateLabelsAndValues(dsIndexes, false);
        inUseImgURL = updatePieChart(width, height);
        redrawChart();

    }

    public HashSet<Integer> getSelectedDsIds() {
        return selectedDsIds;
    }

    public void resetFilterWithUpdatedFilters(Set<Integer> dsIndexes, boolean reset) {
        updateLabelsAndValues(dsIndexes, reset);
        inUseImgURL = initPieChart(width, height);
        redrawChart();

    }

    public void resetFilterToClearState() {
        inuseDsIndexesMap = dsIndexesMap;
        updateLabelsAndValues(null, false);
        inUseImgURL = initPieChart(width, height);
        redrawChart();

    }

    public void unselectFilter() {
        selectedDsIds.clear();
        for (String sliceKey : inuseDsIndexesMap.keySet()) {
            if (plot.getExplodePercent(sliceKey) == 0.1) {
                plot.setExplodePercent(sliceKey, 0);
                plot.setSectionPaint(sliceKey, defaultKeyColorMap.get(sliceKey));
                selectedDsIds.removeAll(inuseDsIndexesMap.get(sliceKey));

            }
        }     

    }

}
