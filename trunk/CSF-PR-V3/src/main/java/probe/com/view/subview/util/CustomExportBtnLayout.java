/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview.util;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.Map;
import probe.com.control.ExperimentHandler;
import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.view.subview.PeptideTable;
import probe.com.view.subview.ProteinsTable;
import probe.com.view.subview.SearchResultsTable;

/**
 *
 * @author Yehia Farag
 */
public class CustomExportBtnLayout extends VerticalLayout implements Serializable, Button.ClickListener {

    private HorizontalLayout topLayout = new HorizontalLayout();
    private VerticalLayout bottomLayout = new VerticalLayout();
    private OptionGroup typeGroup;
    private OptionGroup exportGroup;
    private String type;
    private ExperimentHandler handler;
    private Map<Integer, PeptideBean> peptidesList;
    private int expId;
    private String expName;
    private String accession;
    private String otherAccession;
    private Map<Integer, ExperimentBean> expList;
    private Map<String, ProteinBean> proteinsList;
    private int fractionNumber;
    private CsvExport csvExport = null;
    private ExcelExport excelExport = null;
    private Table fractionTable;
    private Map<Integer, ProteinBean> fullExpProtList;
    private String updatedExpName;

    /*
     * type = allPep for exporting all peptides for one expriment  
     * 
     * 
     */
    public CustomExportBtnLayout(ExperimentHandler handler, String type, int expId, String expName, String accession, String otherAccession, Map<Integer, ExperimentBean> expList, Map<String, ProteinBean> proteinsList, int fractionNumber, Map<Integer, PeptideBean> peptidesList, Table fractionTable, Map<Integer, ProteinBean> fullExpProtList) {

        this.addStyleName(Reindeer.LAYOUT_BLUE);
        this.setHeight("120px");
        this.setWidth("200px");
        this.setSpacing(true);
//        this.setHeight("120px");
        this.type = type;
        this.handler = handler;
        this.expId = expId;
        this.expName = expName;
        if(expName != null)
            this.updatedExpName = expName.replaceAll("[-+.^:,/]", " ");


        this.accession = accession;
        this.otherAccession = otherAccession;
        this.expList = expList;
        this.proteinsList = proteinsList;
        this.fractionNumber = fractionNumber;
        this.peptidesList = peptidesList;
        this.fractionTable = fractionTable;
        this.fullExpProtList = fullExpProtList;
        this.setMargin(false);
        topLayout.setWidth("100%");
        bottomLayout.setWidth("100%");
        bottomLayout.setHeight("40px");
        bottomLayout.setMargin(true);
        topLayout.setHeight("80px");
        topLayout.setMargin(true);
        this.addComponent(topLayout);
        this.addComponent(bottomLayout);
        this.setComponentAlignment(bottomLayout, Alignment.MIDDLE_CENTER);
        this.setComponentAlignment(topLayout, Alignment.TOP_CENTER);
        update();
    }

    private void update() {
        topLayout.removeAllComponents();
        bottomLayout.removeAllComponents();
        typeGroup = new OptionGroup("");
        // Use the single selection mode.
        typeGroup.setMultiSelect(false);
        typeGroup.addItem("Validated");
        typeGroup.addItem("All");
        typeGroup.select("Validated");
        topLayout.addComponent(typeGroup);
        exportGroup = new OptionGroup("");
        // Use the single selection mode.
        exportGroup.setMultiSelect(false);
        exportGroup.addItem("csv");
        exportGroup.addItem("xls");
        exportGroup.select("csv");
        topLayout.addComponent(exportGroup);
        topLayout.setExpandRatio(typeGroup, 0.5f);
        topLayout.setExpandRatio(exportGroup, 0.5f);
        topLayout.setComponentAlignment(typeGroup, Alignment.MIDDLE_CENTER);
        topLayout.setComponentAlignment(exportGroup, Alignment.MIDDLE_CENTER);
        if (type.equals("fractions")) {
            topLayout.removeComponent(typeGroup);
//            this.setWidth("120px");
//            this.setHeight("120px");
        }
        Button btn = new Button("Export");
        btn.addClickListener(this);
        btn.setStyleName(Reindeer.BUTTON_SMALL);
        bottomLayout.addComponent(btn);
        bottomLayout.setComponentAlignment(btn, Alignment.BOTTOM_CENTER);
    }
    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (type.equalsIgnoreCase("allPep")) {
            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {
                peptidesList = handler.getPeptidesList(expId, true);
            } else if (typeGroup.getValue().toString().equalsIgnoreCase("All")) {
                peptidesList = handler.getPeptidesList(expId, false);
            }
            PeptideTable pepTable = new PeptideTable(peptidesList, null,true);
            pepTable.setVisible(false);
            this.addComponent(pepTable);
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                csvExport = new CsvExport(pepTable, "CSF-PR  " + updatedExpName + "   All Peptides");
                csvExport.setReportTitle("CSF-PR / " + expName + " / All Peptides");
                csvExport.setExportFileName("CSF-PR / " + expName + " / All Peptides.csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();

            } else {
                excelExport = new ExcelExport(pepTable, "CSF-PR   " + updatedExpName + "   All Peptides");
                excelExport.setReportTitle("CSF-PR / " + expName + " / All Peptides");
                excelExport.setExportFileName("CSF-PR / " + expName + " / All Peptides.xls");
                excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.export();
            }
        } else if (type.equalsIgnoreCase("allProtPep")) {
            Map<String, PeptideTable> pl = null;
            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {
                pl = handler.getProtAllPep(accession, otherAccession, expList, true);
            } else if (typeGroup.getValue().toString().equalsIgnoreCase("All")) {
                pl = handler.getProtAllPep(accession, otherAccession, expList, false);
            }
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                exportAllPepCsv(pl, accession);
            } else {
                exportAllPepXls(pl, accession);
            }
        } else if (type.equalsIgnoreCase("searchResult")) {
            Map<Integer, ProteinBean> tempFullExpProtList = null;
            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {
                tempFullExpProtList = this.getVprotList(fullExpProtList);
            } else if (typeGroup.getValue().toString().equalsIgnoreCase("All")) {
                tempFullExpProtList = fullExpProtList;
            }
            SearchResultsTable searcheResultsTable = new SearchResultsTable(expList, tempFullExpProtList);
            searcheResultsTable.setVisible(false);
            this.addComponent(searcheResultsTable);
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                csvExport = new CsvExport(searcheResultsTable, "CSF-PR   Search Results");
                csvExport.setReportTitle(" CSF-PR / Search Results");
                csvExport.setExportFileName(" CSF-PR / Search Results.csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();
            } else {
                excelExport = new ExcelExport(searcheResultsTable, "CSF-PR   Search Results");
                excelExport.setReportTitle(" CSF-PR / Search Results");
                excelExport.setExportFileName(" CSF-PR / Search Results.xls");
                excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.export();
            }
        } else if (type.equalsIgnoreCase("prots")) {
            ProteinsTable protTable = null;
            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {
                Map<String, ProteinBean> vProteinsList = new HashMap<String, ProteinBean>();
                for (String key : proteinsList.keySet()) {
                    ProteinBean pb = proteinsList.get(key);
                    if (pb.isValidated()) {
                        vProteinsList.put(key, pb);
                    }
                }
                protTable = new ProteinsTable(vProteinsList, fractionNumber);
            } else if (typeGroup.getValue().toString().equalsIgnoreCase("All")) {
                protTable = new ProteinsTable(proteinsList, fractionNumber);
            }
            protTable.setVisible(false);
            this.addComponent(protTable);
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                exportCsv(protTable, "Proteins", expName, updatedExpName, accession);
            } else {
                exportXls(protTable, "Proteins", expName, updatedExpName, accession);
                // exportAllPepXls(pl, accession);                
            }
        } else if (type.equalsIgnoreCase("protPep")) {
            Map<Integer, PeptideBean> vPeptidesList = null;
            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {
                vPeptidesList = getVpeptideList(peptidesList);
            } else {
                vPeptidesList = new HashMap<Integer, PeptideBean>();
                vPeptidesList.putAll(peptidesList);
            }
            PeptideTable pepTable = new PeptideTable(vPeptidesList, null,false);
            pepTable.setVisible(false);
            this.addComponent(pepTable);
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                csvExport = new CsvExport(pepTable, "CSF-PR " + updatedExpName + "  " + accession + "  Peptides");
                csvExport.setReportTitle("CSF-PR / " + expName + " / " + accession + " / Peptides");
                csvExport.setExportFileName("CSF-PR / " + expName + " / " + accession + " / Peptides.csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();
            } else {
                excelExport = new ExcelExport(pepTable, "CSF-PR   " + updatedExpName + "   " + accession + "   Peptides");
                excelExport.setReportTitle("CSF-PR / " + expName + " / " + accession + " / Peptides");
                excelExport.setExportFileName("CSF-PR / " + expName + " / " + accession + " / Peptides.xls");
                excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.export();
            }
        } else if (type.equalsIgnoreCase("fractions")) {
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                csvExport = new CsvExport(fractionTable, "CSF-PR   " + updatedExpName + "   " + accession + "   Fractions");
                csvExport.setReportTitle("CSF-PR / " + expName + " / " + accession + " / Fractions");
                csvExport.setExportFileName("CSF-PR / " + expName + " / " + accession + " / Fractions.csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();
            } else {
                excelExport = new ExcelExport(fractionTable, "CSF-PR   " + updatedExpName + "   " + accession + "   Fractions");
                excelExport.setReportTitle("CSF-PR / " + expName + " / " + accession + " / Fractions");
                excelExport.setExportFileName("CSF-PR / " + expName + " / " + accession + " / Fractions.xls");
                excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.export();
            }
        }
        update();
    }
    private void exportCsv(Table t, String type, String name, String updatedName, String accession) {
        if (type.equalsIgnoreCase("Proteins")) {
            csvExport = new CsvExport(t, "CSF-PR   " + updatedName + "   Proteins   " );
            csvExport.setReportTitle("CSF-PR / " + name + " / Proteins / " );
            csvExport.setExportFileName("CSF-PR / " + name + " / Proteins / "  + ".csv");
        } else if (type.equalsIgnoreCase("Peptides")) {
            csvExport = new CsvExport(t, "CSF-PR   " + updatedName + "   " + accession + "   Peptides   " );
            csvExport.setReportTitle("CSF-PR / " + name + " / " + accession + " / Peptides / " );
            csvExport.setExportFileName("CSF-PR / " + name + " / " + accession + " / Peptides / "  + ".csv");
        } else if (type.equalsIgnoreCase("Fractions")) {
            csvExport = new CsvExport(t, ("CSF-PR   " + updatedName + "   " + accession + "   Fractions "));
            csvExport.setReportTitle("CSF-PR / " + name + " / " + accession + " / Fractions ");
            csvExport.setExportFileName("CSF-PR / " + name + " / " + accession + " / Fractions .csv");
        }
        csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
        csvExport.setDisplayTotals(false);
        csvExport.export();
    }
    private void exportXls(Table table, String type, String name, String updatedName, String accession) {
        if (type.equalsIgnoreCase("Proteins")) {
            excelExport = new ExcelExport(table, ("CSF-PR  " + updatedName + "  Proteins  " ));
            excelExport.setReportTitle("CSF-PR / " + name + " / Proteins  " );
            excelExport.setExportFileName("CSF-PR / " + name + " / Proteins  "  + ".xls");
        } else if (type.equalsIgnoreCase("Peptides")) {
            excelExport = new ExcelExport(table, ("CSF-PR  " + updatedName + "  " + accession + "  Peptides  " ));
            excelExport.setReportTitle("CSF-PR / " + name + " / " + accession + " / Peptides");
            excelExport.setExportFileName("CSF-PR / " + name + " / " + accession + " / Peptides " + ".csv");
        } else if (type.equalsIgnoreCase("Fractions")) {
            excelExport = new ExcelExport(table, ("CSF-PR  " + updatedName + "  " + accession + "  Fractions "));
            excelExport.setReportTitle("CSF-PR / " + name + " / " + accession + " / Fractions ");
            csvExport.setExportFileName("CSF-PR / " + name + " / " + accession + " / Fractions .xls");
        }
        excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
        excelExport.setDisplayTotals(false);
        excelExport.export();
    }
    private void exportAllPepCsv(Map<String, PeptideTable> pl, String accession) {
        int index = 0;
        for (String key : pl.keySet()) {
            PeptideTable pt = pl.get(key);
            addComponent(pt);
            if (index == 0) {
                csvExport = new CsvExport(pt, ("CSF-PR " +" " + accession + " Peptides"));
                csvExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
                csvExport.setExportFileName("CSF-PR / "  + accession + " / Peptides.csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.convertTable();
                index++;
            } else {
                csvExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
                csvExport.setDisplayTotals(false);
                csvExport.setRowHeaders(false);
                csvExport.setNextTable(pt, key.replaceAll("[-+.^:,/]", " "));
                csvExport.setDisplayTotals(false);
                csvExport.convertTable();
            }
            index++;
        }
        csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
        csvExport.setDisplayTotals(false);
        csvExport.export();
    }
    private void exportAllPepXls(Map<String, PeptideTable> pl, String accession) {
        int index = 0;
        for (String key : pl.keySet()) {
            PeptideTable pt = pl.get(key);
            addComponent(pt);
            if (index == 0) {
                excelExport = new ExcelExport(pt, "CSF-PR  " +"  " + accession + "  Peptides");
                excelExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
                excelExport.setExportFileName("CSF-PR / " + accession + " / Peptides.xls");
                excelExport.setMimeType(CsvExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.convertTable();
                index++;
            } else {
                excelExport.setReportTitle("CSF-PR / "+ key + " / " + accession + " / Peptides");
                excelExport.setDisplayTotals(false);
                excelExport.setRowHeaders(false);
                excelExport.setNextTable(pt, "CSF-PR  " + key.replaceAll("[-+.^:,/]", " ") + "  " + accession + "   Peptides");
                excelExport.setDisplayTotals(false);
                excelExport.convertTable();
            }
            index++;
        }
        excelExport.setMimeType(CsvExport.EXCEL_MIME_TYPE);
        excelExport.setDisplayTotals(false);
        excelExport.export();

    }

    private Map<Integer, PeptideBean> getVpeptideList(Map<Integer, PeptideBean> peptideList) {
        Map<Integer, PeptideBean> vPeptideList = new HashMap<Integer, PeptideBean>();
        for (int key : peptideList.keySet()) {
            PeptideBean pb = peptideList.get(key);
            if (pb.getValidated() == 1) {
                vPeptideList.put(key, pb);
            }
        }
        return vPeptideList;

    }

    private Map<Integer, ProteinBean> getVprotList(Map<Integer, ProteinBean> protList) {
        Map<Integer, ProteinBean> vPeptideList = new HashMap<Integer, ProteinBean>();
        for (int key : protList.keySet()) {
            ProteinBean pb = protList.get(key);
            if (pb.isValidated()) {
                vPeptideList.put(key, pb);
            }
        }
        return vPeptideList;

    }
}
