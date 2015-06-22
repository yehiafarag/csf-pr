/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.quantdatasetsoverview;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.VaadinSession;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import probe.com.model.beans.ComparisonProtein;
import probe.com.model.beans.GroupsComparison;
import probe.com.selectionmanager.DatasetExploringSelectionManagerRes;

/**
 *
 * @author Yehia Farag
 */
public class StudiesScatterChartsLayout extends VerticalLayout {

    private final Map<GroupsComparison, ProteinComparisonScatterPlotLayout> compLayoutMap = new LinkedHashMap<GroupsComparison, ProteinComparisonScatterPlotLayout>();
    private final GridLayout mainbodyLayout;

    public StudiesScatterChartsLayout(ComparisonProtein[] protList, Set<GroupsComparison> selectedComparisonList, final DatasetExploringSelectionManagerRes selectionManager, int width) {
        setStyleName(Reindeer.LAYOUT_WHITE);
        this.setWidth("100%");
        this.setHeightUndefined();
        mainbodyLayout = new GridLayout(1, selectedComparisonList.size() + 1);
        this.addComponent(mainbodyLayout);
        mainbodyLayout.setWidthUndefined();
        mainbodyLayout.setHeightUndefined();
        int rowIndex = 0;
        for (final ComparisonProtein cp : protList) {

            if (cp == null) {
                continue;
            }
            ProteinComparisonScatterPlotLayout protCompLayout = new ProteinComparisonScatterPlotLayout(cp, width);
            mainbodyLayout.addComponent(protCompLayout, 0, rowIndex);
            mainbodyLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_CENTER);
            compLayoutMap.put(cp.getComparison(), protCompLayout);
            LayoutEvents.LayoutClickListener closeListener = new LayoutEvents.LayoutClickListener() {

                private final GroupsComparison localComparison = cp.getComparison();

                @Override
                public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                    Set<GroupsComparison> selectedComparisonList = selectionManager.getSelectedComparisonList();
                    selectedComparisonList.remove(localComparison);
                    selectionManager.setComparisonSelection(selectedComparisonList);
                }
            };
            protCompLayout.getCloseBtn().addLayoutClickListener(closeListener);

            rowIndex++;
        }

    }

    public void orderComparisons(ComparisonProtein[] ordComparisonProteins) {
        mainbodyLayout.removeAllComponents();
        int rowIndex = 0;
        for (final ComparisonProtein cp : ordComparisonProteins) {

            if (cp == null) {
                continue;
            }
            ProteinComparisonScatterPlotLayout protCompLayout = compLayoutMap.get(cp.getComparison());
            mainbodyLayout.addComponent(protCompLayout, 0, rowIndex);
            mainbodyLayout.setComponentAlignment(protCompLayout, Alignment.MIDDLE_CENTER);

            rowIndex++;
        }

    }
    private ProteinComparisonScatterPlotLayout lastheighlitedlayout;

    public void highlightComparison(GroupsComparison groupComp) {

        try {
            VaadinSession.getCurrent().getLockInstance().lock();//          
            if (lastheighlitedlayout != null) {
                lastheighlitedlayout.highlight(false);
            }
            ProteinComparisonScatterPlotLayout layout = compLayoutMap.get(groupComp);
            if (layout == null) {
                return;
            }
            lastheighlitedlayout = layout;
            layout.highlight(true);
        } finally {
            VaadinSession.getCurrent().getLockInstance().unlock();
        }

    }

    public void redrawCharts() {
        for (ProteinComparisonScatterPlotLayout layout : compLayoutMap.values()) {
            layout.redrawChart();
        }

    }

}
