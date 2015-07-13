/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body.quantdatasetsoverview;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import probe.com.model.beans.GroupsComparison;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.ProteinOverviewJFreeLineChartContainer;

/**
 *
 * @author Yehia Farag
 */
public class QuantProteinsTabsheetContainerLayout extends VerticalLayout implements CSFFilter, TabSheet.SelectedTabChangeListener {

    private final LinkedHashMap<String, ComparisonProtein[]> protSelectionMap = new LinkedHashMap<String, ComparisonProtein[]>();
    private boolean selfSelection = false;
    private final Map<Component, String> protSelectionTabMap = new HashMap<Component, String>();
    private boolean initScroll = true;

    @Override
    public void selectionChanged(String type) {
        if (type.equalsIgnoreCase("quantProtSelection") && !selfSelection) {
             
            proteinsTabsheet.removeAllComponents();
            protSelectionMap.clear();
            protSelectionTabMap.clear();
            protSelectionMap.putAll(selectionManager.getProtSelectionMap());
            if (protSelectionMap.isEmpty()) {
                proteinsTabsheet.setVisible(false);
                return;
            }
            Set<GroupsComparison> selectedComparisonList = selectionManager.getSelectedComparisonList();
            proteinsTabsheet.setVisible(true);
            Page.getCurrent().getStyles().add(".v-tabsheet-tabs .v-icon{"
                    + "    width:" + selectedComparisonList.size() * 15 + "px !important;"
                    + "    height:35px !important;"
                    + "    color:blue;"
                    + "}");

            for (final String key : protSelectionMap.keySet()) {
                String protAcc = key.replace("--", "").trim().split(",")[0].toUpperCase();
                String protName = key.replace("--", "").trim().split(",")[1];
                HorizontalLayout vlo = generateProtTab(protName, protAcc, protSelectionMap.get(key), selectedComparisonList);
                final Tab t1;
                t1 = proteinsTabsheet.addTab(vlo);
                t1.setCaption(protName);
                t1.setClosable(true);
                t1.setStyleName("tabwithcharticon");
                t1.setIcon(new ExternalResource(((ProteinOverviewJFreeLineChartContainer) vlo.getComponent(1)).getThumbChart()));
                protSelectionTabMap.put(vlo, key);//                
                redrawCharts();

            }
           if (initScroll) {
                initScroll=false;
                this.getUI().scrollIntoView(compOrederTitle);
            }

        } else {
            selfSelection = false;
        }

    }

    @Override
    public String getFilterId() {
        return "quantProteinsLayout";
    }

    @Override
    public void removeFilterValue(String value) {
//        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private final DatasetExploringCentralSelectionManager selectionManager;
    private final TabSheet proteinsTabsheet;

    public QuantProteinsTabsheetContainerLayout(final DatasetExploringCentralSelectionManager selectionManager) {
        this.selectionManager = selectionManager;
        this.selectionManager.registerFilter(QuantProteinsTabsheetContainerLayout.this);
        this.setHeight("100%");
        this.setMargin(new MarginInfo(true, false, false, false));
        proteinsTabsheet = new TabSheet();
        proteinsTabsheet.setCaptionAsHtml(true);
        proteinsTabsheet.setHeightUndefined();
        proteinsTabsheet.setStyleName(Reindeer.TABSHEET_MINIMAL);
        proteinsTabsheet.setVisible(false);
        proteinsTabsheet.addStyleName("hideoverflow");
        proteinsTabsheet.addSelectedTabChangeListener(QuantProteinsTabsheetContainerLayout.this);
        proteinsTabsheet.setCloseHandler(new TabSheet.CloseHandler() {

            @Override
            public void onTabClose(TabSheet tabsheet, Component tabContent) {
                protSelectionMap.remove(protSelectionTabMap.get(tabContent));
                if (protSelectionMap.isEmpty()) {
                    proteinsTabsheet.setVisible(false);
                }

                selectionManager.setProteinsSelection(new LinkedHashMap<String, ComparisonProtein[]>(protSelectionMap));

            }
        });

        this.addComponent(proteinsTabsheet);

    }

    private Label compOrederTitle ;
    private HorizontalLayout generateProtTab(String protName, String protAcc, ComparisonProtein[] comparisonProteins, Set<GroupsComparison> selectedComparisonList) {
        HorizontalLayout bodyLayout = new HorizontalLayout();
        Page page = Page.getCurrent();
        int pageWidth = page.getBrowserWindowWidth();
        bodyLayout.setWidthUndefined();
        bodyLayout.setSpacing(true);
        bodyLayout.setMargin(true);
        bodyLayout.setHeightUndefined();
        bodyLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        GridLayout protInfoLayout = new GridLayout(1, 6);
        protInfoLayout.setMargin(true);
        protInfoLayout.setSpacing(true);

        Label accTitle = new Label("<b>Protein Accession</b>");
        accTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(accTitle, 0, 0);
        CustomExternalLink acc = new CustomExternalLink(protAcc.toUpperCase(), "http://www.uniprot.org/uniprot/" + protAcc.toUpperCase());
        protInfoLayout.addComponent(acc, 0, 1);
        Label nameTitle = new Label("<b>Protein Name</b>");
        nameTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(nameTitle, 0, 2);

        Label nameValue = new Label("<textarea rows='4' cols='20' readonly>" + protName + "</textarea>");
        nameValue.setContentMode(ContentMode.HTML);
        nameValue.setStyleName("valuelabel");
        nameValue.setReadOnly(true);
        protInfoLayout.addComponent(nameValue, 0, 3);
        protInfoLayout.setWidth("150px");
        bodyLayout.addComponent(protInfoLayout);

        compOrederTitle = new Label("<b>Comparisons Order</b>");
        compOrederTitle.setContentMode(ContentMode.HTML);
        protInfoLayout.addComponent(compOrederTitle, 0, 4);
        protInfoLayout.setWidth("250px");
//        Label nameValue = new Label("<textarea rows='4' cols='20' readonly>" + protName + "</textarea>");
//        nameValue.setContentMode(ContentMode.HTML);
//        nameValue.setStyleName("valuelabel");
//    
        int width = (pageWidth - 300);

//        ProtOverviewLineChart overallPlotLayout = new ProtOverviewLineChart(comparisonProteins,(width-200));
        ProteinOverviewJFreeLineChartContainer overallPlotLayout = new ProteinOverviewJFreeLineChartContainer(selectionManager, comparisonProteins, selectedComparisonList, (width), protName);
        protInfoLayout.addComponent(overallPlotLayout.getOrederingOptionGroup(), 0, 5);

        bodyLayout.addComponent(overallPlotLayout);
        bodyLayout.setComponentAlignment(overallPlotLayout, Alignment.TOP_CENTER);
//        bodyLayout.setExpandRatio(overallPlotLayout, 0.3f);

//        bodyLayout.setExpandRatio(tabLayout, 0.6f);
        return bodyLayout;

    }

    public void redrawCharts() {
        if (proteinsTabsheet.getSelectedTab() == null) {
            return;
        }
        HorizontalLayout selectedTab = (HorizontalLayout) proteinsTabsheet.getSelectedTab();
        ProteinOverviewJFreeLineChartContainer overallPlotLayout = (ProteinOverviewJFreeLineChartContainer) selectedTab.getComponent(1);
        overallPlotLayout.redrawCharts();

    }

    @Override
    public void selectedTabChange(TabSheet.SelectedTabChangeEvent event) {
        redrawCharts();
    }

}
