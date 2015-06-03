/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.datasetsoverview.heatmap;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.GroupsComparison;
import probe.com.model.beans.QuantDSIndexes;

/**
 *
 * @author Yehia Farag
 */
public class HeatMapComponent extends VerticalLayout {

    private final GridLayout heatmapBody;
    private final GridLayout columnHeader;
    private final GridLayout rowHeader;
//    private final PatientsGroup[] patientsGroupArr;

    private HeaderCell[] columnCells;
    private HeaderCell[] rowCells;
    private final Map<String,HeatmapCell>comparisonsCellsMap = new HashMap<String, HeatmapCell>();
    private final Set<GroupsComparison> selectedDsList = new HashSet<GroupsComparison>();
    private boolean singleSelection = true;
    private boolean selfSelection = false;

    public HeatMapComponent() {
        this.setMargin(false);
        this.setSpacing(true);
        this.setWidth("100%");
        this.columnHeader = new GridLayout();
        this.rowHeader = new GridLayout();
        this.heatmapBody = new GridLayout();

        HorizontalLayout h1Layout = new HorizontalLayout();
        VerticalLayout spacer = new VerticalLayout();
        spacer.setWidth("150px");
        spacer.setHeight("50px");
        spacer.setStyleName(Reindeer.LAYOUT_WHITE);
       
       
        
        h1Layout.addComponent(spacer);
        h1Layout.setSpacing(true);
        h1Layout.addComponent(columnHeader);
//        h1Layout.setWidth("100%");
        h1Layout.setComponentAlignment(columnHeader, Alignment.TOP_LEFT);
        this.addComponent(h1Layout);

        final HorizontalLayout h2Layout = new HorizontalLayout();
        h2Layout.addComponent(rowHeader);
        h2Layout.addComponent(heatmapBody);
        h2Layout.setSpacing(true);
//        h2Layout.setWidth("100%");
        h2Layout.setComponentAlignment(heatmapBody, Alignment.MIDDLE_LEFT);
        this.addComponent(h2Layout); 
        

    }

    public void setSingleSelection(boolean singleSelection) {
        this.singleSelection = singleSelection;
    }

    public void updateHeatMap(Set<String> rowsLbels, Set<String> columnsLbels, QuantDSIndexes[][] values, int maxDatasetValue) {
        if (rowsLbels.isEmpty() || columnsLbels.isEmpty()) {
            return;
        }
        updateHeatMapLayout(values, rowsLbels, columnsLbels, maxDatasetValue);

    }

    private final List<HeatmapCell> selectedCells = new ArrayList<HeatmapCell>();

    protected void addSelectedDs(GroupsComparison comparison, HeatmapCell cell) {
        if (singleSelection) {
            for (HeatmapCell tcell : selectedCells) {
                tcell.unselect();
            }
            selectedDsList.clear();
            selectedCells.clear();
        }
        
        this.selectedDsList.add(comparison);
        this.selectedCells.add(cell);
        String kI = cell.getComparison().getComparisonHeader();
        String[] k1Arr = kI.split(" vs ");
        String kII = k1Arr[1] + " vs " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        equalCall.select();
        this.selectedDsList.add(comparison);
        this.selectedCells.add(cell);
        this.selectedDsList.add(equalCall.getComparison());
        this.selectedCells.add(equalCall);
        updateSelectionManagerIndexes();
    }

    public void updateDsCellSelection(Set<GroupsComparison> selectedDsList) {

        if (selfSelection) {
            selfSelection = false;
            return;
        }
        List<HeatmapCell> localSelectedCells = new ArrayList<HeatmapCell>();
        localSelectedCells.addAll(this.selectedCells);
        for (GroupsComparison gr : this.selectedDsList) {
            String kI = gr.getComparisonHeader();
                String[] k1Arr = kI.split(" vs ");
                String kII = k1Arr[1] + " vs " + k1Arr[0];
                Set<String> keymap = new HashSet<String>();
                keymap.add(kI);
                keymap.add(kII);
            if (!selectedDsList.contains(gr) && !selectedDsList.contains(comparisonsCellsMap.get(kII).getComparison())) {
                
                for (HeatmapCell cell : selectedCells) {
                    String kI2 = cell.getComparison().getComparisonHeader();
                    String[] kI2Arr = kI2.split(" vs ");
                    String kII2 = kI2Arr[1] + " vs " + kI2Arr[0];
                    if (keymap.contains(kI2) && keymap.contains(kII2)) {
                        cell.unselect();
                        localSelectedCells.remove(cell);
                        
                    }

                }

            }
        }
        this.selectedCells.clear();
        this.selectedCells.addAll(localSelectedCells);
        this.selectedDsList.clear();
        this.selectedDsList.addAll(selectedDsList);


    }

    protected void removeSelectedDs(GroupsComparison comparison, HeatmapCell cell) {
        this.selectedDsList.remove(comparison);
        this.selectedCells.remove(cell);
        String kI = cell.getComparison().getComparisonHeader();
        String[] k1Arr = kI.split(" vs ");
        String kII = k1Arr[1] + " vs " + k1Arr[0];
        HeatmapCell equalCall = comparisonsCellsMap.get(kII);
        equalCall.unselect();
        this.selectedDsList.remove(equalCall.getComparison());
        this.selectedCells.remove(equalCall);
        updateSelectionManagerIndexes();

    }

    private void updateSelectionManagerIndexes() {
        //filter datasets
        selfSelection =true;
        Map<String, GroupsComparison> filteredComp = new HashMap<String, GroupsComparison>();
        for (GroupsComparison comp : selectedDsList) {
            String kI = comp.getComparisonHeader();
            String[] k1Arr = kI.split(" vs ");
            String kII = k1Arr[1] + " vs " + k1Arr[0];
            if (filteredComp.containsKey(kI) || filteredComp.containsKey(kII)) {
                continue;
            }
            filteredComp.put(kI, comp);

        }
        Set<GroupsComparison> filteredDelectedDsList = new HashSet<GroupsComparison>();
        filteredDelectedDsList.addAll(filteredComp.values());        
        updateSelectionManager(filteredDelectedDsList);

    }

    private void updateHeatMapLayout(QuantDSIndexes[][] values, Set<String> rowheaders, Set<String> colheaders, int maxDatasetValue) {
        HeatmapColorGenerator hmColorGen = new HeatmapColorGenerator(maxDatasetValue, 0);
        columnHeader.removeAllComponents();
        rowHeader.removeAllComponents();
        heatmapBody.removeAllComponents();
        heatmapBody.setMargin(new MarginInfo(false, true, true, false));

        columnHeader.setColumns(colheaders.size());
        columnHeader.setRows(1);
        columnHeader.setWidth((colheaders.size() * 50) + "px");
        columnHeader.setHeight("150px");
        columnCells = new HeaderCell[colheaders.size()];

        rowHeader.setColumns(1);
        rowHeader.setRows(rowheaders.size());
        rowHeader.setWidth("150px");
        rowHeader.setHeight((50 * rowheaders.size()) + "px");
        rowCells = new HeaderCell[rowheaders.size()];

        heatmapBody.setColumns(colheaders.size());
        heatmapBody.setRows(rowheaders.size());
        heatmapBody.setWidth((colheaders.size() * 50) + "px");
        heatmapBody.setHeight((50 * rowheaders.size()) + "px");

//init col headers
        for (int i = 0; i < colheaders.size(); i++) {
            String la = colheaders.toArray()[i].toString();
            if (la.equalsIgnoreCase("")) {
                la = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(false, la, i);
            columnHeader.addComponent(headerCell, i, 0);
            columnHeader.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            columnCells[i] = headerCell;
            this.selectedDsList.clear();
            updateSelectionManagerIndexes();

        }

        //init row headers
        for (int i = 0; i < rowheaders.size(); i++) {
            String la = rowheaders.toArray()[i].toString();
            if (la.equalsIgnoreCase("")) {
                la = "Not Available";
            }
            HeaderCell headerCell = new HeaderCell(true, la, i);
            rowHeader.addComponent(headerCell, 0, i);
            rowHeader.setComponentAlignment(headerCell, Alignment.MIDDLE_CENTER);
            rowCells[i] = headerCell;
        }

        for (int x = 0; x < values.length; x++) {
            for (int y = 0; y < values[x].length; y++) {
                String headerTitle = rowheaders.toArray()[x].toString()+" vs "+colheaders.toArray()[y].toString();
                 double value = values[x][y].getValue();
                 String color  = "#EFF2FB";
                if(!rowheaders.toArray()[x].toString().equalsIgnoreCase(colheaders.toArray()[y].toString()))
                    color = hmColorGen.getColor((float)value);
                
                HeatmapCell cell = new HeatmapCell(value, color, values[x][y].getIndexes(), x, y, null, HeatMapComponent.this,headerTitle);
                comparisonsCellsMap.put(headerTitle, cell);
                heatmapBody.addComponent(cell, y, x);
            }

        }

    }

    public void highlightHeaders(int col, int row) {
        columnCells[col].heighlightCellStyle();
        rowCells[row].heighlightCellStyle();
    }

    public void resetHeadersStyle(int col, int row) {
        columnCells[col].resetCellStyle();
        rowCells[row].resetCellStyle();

    }
    
     public void selectHeadersStyle(int col, int row) {
        columnCells[col].resetCellStyle();
        rowCells[row].resetCellStyle();

    }
      public void unSelectHeadersStyle(int col, int row) {
        columnCells[col].resetCellStyle();
        rowCells[row].resetCellStyle();

    }

    public void updateSelectionManager(Set<GroupsComparison> selectedDsList) {
        ///to be overided
    }

}
