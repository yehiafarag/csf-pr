package probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters;

import probe.com.view.body.quantdatasetsoverview.diseasegroupsfilters.heatmap.HeatMapComponent;
import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashSet;
import java.util.Set;
import probe.com.model.beans.GroupsComparison;
import probe.com.model.beans.QuantDSIndexes;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;
import probe.com.view.core.DiseaseGroup;

/**
 *
 * @author yfa041
 */
public class HeatMapFilter extends VerticalLayout implements CSFFilter {

    @Override
    public void selectionChanged(String type) {
        if (selfselected) {
            selfselected = false;
            return;
        }
        if (type.equalsIgnoreCase("HeatMap_Update_level")) {
            this.updateHeatmap(centralFiltersSelectionManager.getSelectedHeatMapRows(), centralFiltersSelectionManager.getSelectedHeatMapColumns(), centralFiltersSelectionManager.getDiseaseGroupsArr());
        } else if (type.equalsIgnoreCase("ComparisonSelection")) {
            this.updateCellSelection(centralFiltersSelectionManager.getSelectedComparisonList());

        }
    }

    @Override
    public String getFilterId() {
        return this.getClass().getName();
    }

    @Override
    public void removeFilterValue(String value) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private int maxDatasetNumber;
    private QuantDSIndexes[][] values;
    private final HeatMapComponent heatMap;
    private boolean selfselected = false;
    private final DatasetExploringCentralSelectionManager centralFiltersSelectionManager;

    public HeatMapFilter(final DatasetExploringCentralSelectionManager centralFiltersSelectionManager, int heatmapW, Set<String> rowheaders, Set<String> colheaders, DiseaseGroup[] patientsGroupArr) {

        this.setWidth(heatmapW + "px");
        this.setHeight("100%");
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.centralFiltersSelectionManager = centralFiltersSelectionManager;

        heatMap = new HeatMapComponent() {
            @Override
            public void updateSelectionManager(Set<GroupsComparison> selectedDsList) {
                centralFiltersSelectionManager.setComparisonSelection(selectedDsList);
            }
        };

        this.addComponent(heatMap);
        this.setComponentAlignment(heatMap, Alignment.MIDDLE_CENTER);
        this.calcHeatMapMatrix(rowheaders, colheaders, patientsGroupArr);
        heatMap.updateHeatMap(rowheaders, colheaders, values, maxDatasetNumber);
        centralFiltersSelectionManager.registerFilter(HeatMapFilter.this);
    }

    public void updateHeatmap(Set<String> rowheaders, Set<String> colheaders, DiseaseGroup[] patientsGroupArr) {

        this.calcHeatMapMatrix(rowheaders, colheaders, patientsGroupArr);
        heatMap.updateHeatMap(rowheaders, colheaders, values, maxDatasetNumber);
        rowheaders.addAll(colheaders);

    }

    private void calcHeatMapMatrix(Set<String> rowheaders, Set<String> colheaders, DiseaseGroup[] patientsGroupArr) {
        maxDatasetNumber = -1;
        values = new QuantDSIndexes[rowheaders.size()][colheaders.size()];
        for (int x = 0; x < rowheaders.size(); x++) {
            for (int y = 0; y < colheaders.size(); y++) {
                Set<Integer> value = calcDsNumbers(rowheaders.toArray()[x].toString(), colheaders.toArray()[y].toString(), patientsGroupArr);
                int z = 0;
                int[] indexes = new int[value.size()];
                for (int i : value) {
                    indexes[z] = i;
                    z++;
                }
                QuantDSIndexes qDataset = new QuantDSIndexes();
                qDataset.setValue(value.size());
                qDataset.setIndexes(indexes);
                values[x][y] = qDataset;
                if (value.size() > maxDatasetNumber) {
                    maxDatasetNumber = value.size();
                }
            }

        }
    }

    private Set<Integer> calcDsNumbers(String PGI, String PGII, DiseaseGroup[] patientsGroupArr) {
        Set<Integer> indexes = new HashSet<Integer>();
        for (DiseaseGroup pg : patientsGroupArr) {
            if (pg.checkLabel(PGI)) {
                if (pg.getValLabel(PGI).equalsIgnoreCase(PGII)) {
                    indexes.add(pg.getOriginalDatasetIndex());
                }

            }

        }
        return indexes;
    }

    public void updateHeatmap(Set<String> selectedRows, Set<String> selectedColumns) {
        heatMap.updateHeatMap(selectedRows, selectedColumns, values, maxDatasetNumber);
    }

    public void setSingleSelection(boolean singleSelection) {
        heatMap.setSingleSelection(singleSelection);
    }

    public void addHideHeatmapBtnListener(LayoutEvents.LayoutClickListener listener) {
        heatMap.getHideCompBtn().addLayoutClickListener(listener);
    }

    public void updateCellSelection(Set<GroupsComparison> selectedComparisonList) {
        heatMap.updateDsCellSelection(selectedComparisonList);
    }

}
