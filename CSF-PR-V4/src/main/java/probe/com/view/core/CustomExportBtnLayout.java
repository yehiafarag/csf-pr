package probe.com.view.core;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.server.FileResource;
import com.vaadin.server.Page;
import com.vaadin.server.Resource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import com.vaadin.ui.themes.Reindeer;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.DatasetDetailsBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.view.components.PeptideTable;
import probe.com.view.components.ProteinsTableComponent;
import probe.com.view.components.SearchResultsTable;

/**
 *
 * @author Yehia Farag
 */
public class CustomExportBtnLayout extends VerticalLayout implements Serializable, Button.ClickListener {

    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final VerticalLayout bottomLayout = new VerticalLayout();
    private OptionGroup typeGroup, exportGroup;
    private final String type;
    private final MainHandler handler;
    private Map<Integer, PeptideBean> peptidesList;
    private final int datasetId;
    private final String datasetName;
    private final String accession;
    private final String otherAccession;
    private final Map<String, ProteinBean> proteinsList;
    private final int fractionNumber;
    private CsvExport csvExport = null;
    private ExcelExport excelExport = null;
    private final Table fractionTable;
    private final Map<Integer, ProteinBean> fullExpProtList;
    private String updatedExpName;
//    private final DatasetBean dataset;
    private final String mainProtDesc;
    private Button exportBtn;

    public CustomExportBtnLayout(MainHandler handler, String type, int datasetId, String datasetName, String accession, String otherAccession, Map<String, ProteinBean> proteinsList, int fractionNumber, Map<Integer, PeptideBean> peptidesList, Table fractionTable, Map<Integer, ProteinBean> fullExpProtList, String mainProtDesc) {

        this.type = type;
        this.handler = handler;
        this.datasetId = datasetId;
        this.datasetName = datasetName;
        if (datasetName != null) {
            this.updatedExpName = datasetName.replaceAll("[-+.^:,/]", " ");
        }

        this.accession = accession;
        this.otherAccession = otherAccession;
//        this.datasetList = handler.getDatasetList();
        this.proteinsList = proteinsList;
        this.mainProtDesc = mainProtDesc;
        this.fractionNumber = fractionNumber;
        this.peptidesList = peptidesList;
        this.fractionTable = fractionTable;
        this.fullExpProtList = fullExpProtList;
//        this.dataset = handler.getMainDataset();

        //layout init
        this.addStyleName(Reindeer.LAYOUT_BLUE);
        this.setHeight("120px");
        this.setWidth("200px");
        this.setSpacing(true);

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
        }
        exportBtn = new Button("Export");
        exportBtn.addClickListener(this);
        exportBtn.setStyleName(Reindeer.BUTTON_SMALL);
        bottomLayout.addComponent(exportBtn);
        bottomLayout.setComponentAlignment(exportBtn, Alignment.BOTTOM_CENTER);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (type.equalsIgnoreCase("allPep")) {
            //check if file available                
            if (handler.checkFileAvailable("CSF-PR - " + datasetName + " - All - " + typeGroup.getValue().toString() + " - Peptides." + exportGroup.getValue().toString())) {
                String fileURL = handler.getFileUrl("CSF-PR - " + datasetName + " - All - " + typeGroup.getValue().toString() + " - Peptides." + exportGroup.getValue().toString());
                Resource res = new FileResource(new File(fileURL));
                Page.getCurrent().open(res, null, false);
                return;
            }

            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {

                peptidesList = handler.getPeptidesList(datasetId, true);
            } else if (typeGroup.getValue().toString().equalsIgnoreCase("All")) {
                peptidesList = handler.getPeptidesList(datasetId, false);
            }

            PeptideTable pepTable = new PeptideTable(peptidesList, null, true, null);
            pepTable.setVisible(false);
            this.addComponent(pepTable);
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                csvExport = new CsvExport(pepTable, "CSF-PR  " + updatedExpName + "   All Peptides");
                csvExport.setReportTitle("CSF-PR / " + datasetName + " / All Peptides");
                csvExport.setExportFileName("CSF-PR - " + datasetName + " - All - " + typeGroup.getValue().toString() + " - Peptides." + "csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();

            } else {
                excelExport = new ExcelExport(pepTable, "CSF-PR   " + updatedExpName + "   All Peptides");
                excelExport.setReportTitle("CSF-PR / " + datasetName + " / All Peptides");
                excelExport.setExportFileName("CSF-PR - " + datasetName + " - All - " + typeGroup.getValue().toString() + " - Peptides." + "xls");
                excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.export();
            }
        } else if (type.equalsIgnoreCase("allProtPep")) {

            Map<String, PeptideTable> pepList = null;
            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {
                pepList = handler.getProteinPeptidesAllDatasets(accession, otherAccession, true);
            } else if (typeGroup.getValue().toString().equalsIgnoreCase("All")) {
                pepList = handler.getProteinPeptidesAllDatasets(accession, otherAccession, false);
            }
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                exportAllPepCsv(pepList, accession);
            } else {
                exportAllPepXls(pepList, accession);
            }
        } else if (type.equalsIgnoreCase("searchResult")) {
            Map<Integer, ProteinBean> tempFullExpProtList = null;
            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {
                tempFullExpProtList = this.getVprotList(fullExpProtList);
            } else if (typeGroup.getValue().toString().equalsIgnoreCase("All")) {
                tempFullExpProtList = fullExpProtList;
            }
            Map<Integer, DatasetDetailsBean> datasetDetailsList = handler.getDatasetDetailsList();
            SearchResultsTable searcheResultsTable = new SearchResultsTable(datasetDetailsList, tempFullExpProtList);
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

            if (handler.checkFileAvailable("CSF-PR - " + datasetName + " - " + typeGroup.getValue().toString() + " - Proteins." + exportGroup.getValue().toString())) {
                String fileURL = handler.getFileUrl("CSF-PR - " + datasetName + " - " + typeGroup.getValue().toString() + " - Proteins." + exportGroup.getValue().toString());
                Resource res = new FileResource(new File(fileURL));
                Page.getCurrent().open(res, null, false);
                return;
            }

            ProteinsTableComponent protTable = null;
            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {
                Map<String, ProteinBean> vProteinsList = new HashMap<String, ProteinBean>();
                for (String key : proteinsList.keySet()) {
                    ProteinBean pb = proteinsList.get(key);
                    if (pb.isValidated()) {
                        vProteinsList.put(key, pb);
                    }
                }
                protTable = new ProteinsTableComponent(vProteinsList, fractionNumber);
            } else// if (typeGroup.getValue().toString().equalsIgnoreCase("All"))
            {
                protTable = new ProteinsTableComponent(proteinsList, fractionNumber);
            }
            protTable.setVisible(false);
            this.addComponent(protTable);
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                exportCsv(protTable, "Proteins", datasetName, updatedExpName, accession, typeGroup.getValue().toString());
            } else {
                exportXls(protTable, "Proteins", datasetName, updatedExpName, accession, typeGroup.getValue().toString());
            }
        } else if (type.equalsIgnoreCase("protPep")) {
            Map<Integer, PeptideBean> vPeptidesList = null;
            if (typeGroup.getValue().toString().equalsIgnoreCase("Validated")) {
                vPeptidesList = getVpeptideList(peptidesList);
            } else {
                vPeptidesList = new HashMap<Integer, PeptideBean>();
                vPeptidesList.putAll(peptidesList);
            }
            PeptideTable pepTable = new PeptideTable(vPeptidesList, null, false, mainProtDesc);
            pepTable.setVisible(false);
            this.addComponent(pepTable);
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                csvExport = new CsvExport(pepTable, "CSF-PR " + updatedExpName + "  " + accession + "  Peptides");
                csvExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Peptides");
                csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Peptides.csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();
            } else {
                excelExport = new ExcelExport(pepTable, "CSF-PR   " + updatedExpName + "   " + accession + "   Peptides");
                excelExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Peptides");
                excelExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Peptides.xls");
                excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.export();
            }
        } else if (type.equalsIgnoreCase("fractions")) {
            if (exportGroup.getValue().toString().equalsIgnoreCase("csv")) {
                csvExport = new CsvExport(fractionTable, "CSF-PR   " + updatedExpName + "   " + accession + "   Fractions");
                csvExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Fractions");
                csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Fractions.csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.export();
            } else {
                excelExport = new ExcelExport(fractionTable, "CSF-PR   " + updatedExpName + "   " + accession + "   Fractions");
                excelExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Fractions");
                excelExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Fractions.xls");
                excelExport.setMimeType(ExcelExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.export();
            }
        }
        update();
    }

    private void exportCsv(Table t, String type, String datasetName, String updatedName, String accession, String validated) {
        if (type.equalsIgnoreCase("Proteins")) {
            csvExport = new CsvExport(t, "CSF-PR   " + updatedName + " Proteins");
            csvExport.setReportTitle("CSF-PR / " + datasetName + " / Proteins / ");
            csvExport.setExportFileName("CSF-PR - " + datasetName + " - " + validated + " - Proteins" + ".csv");
        } else if (type.equalsIgnoreCase("Peptides")) {
            csvExport = new CsvExport(t, "CSF-PR   " + updatedName + "   " + accession + "   Peptides   ");
            csvExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Peptides / ");
            csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Peptides / " + ".csv");
        } else if (type.equalsIgnoreCase("Fractions")) {
            csvExport = new CsvExport(t, ("CSF-PR   " + updatedName + "   " + accession + "   Fractions "));
            csvExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Fractions ");
            csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Fractions .csv");
        }
        csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
        csvExport.setDisplayTotals(false);
        csvExport.export();
    }

    private void exportXls(Table table, String type, String datasetName, String updatedName, String accession, String validated) {
        if (type.equalsIgnoreCase("Proteins")) {
            excelExport = new ExcelExport(table, ("CSF-PR  " + updatedName + "  Proteins  "));
            excelExport.setReportTitle("CSF-PR / " + datasetName + " / Proteins  ");
            excelExport.setExportFileName("CSF-PR - " + datasetName + " - " + validated + " - Proteins" + ".xls");
        } else if (type.equalsIgnoreCase("Peptides")) {
            excelExport = new ExcelExport(table, ("CSF-PR  " + updatedName + "  " + accession + "  Peptides  "));
            excelExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Peptides");
            excelExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Peptides " + ".csv");
        } else if (type.equalsIgnoreCase("Fractions")) {
            excelExport = new ExcelExport(table, ("CSF-PR  " + updatedName + "  " + accession + "  Fractions "));
            excelExport.setReportTitle("CSF-PR / " + datasetName + " / " + accession + " / Fractions ");
            csvExport.setExportFileName("CSF-PR / " + datasetName + " / " + accession + " / Fractions .xls");
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
                csvExport = new CsvExport(pt, ("CSF-PR " + " " + accession + " Peptides"));
                csvExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
                csvExport.setExportFileName("CSF-PR / " + accession + " / Peptides.csv");
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
                excelExport = new ExcelExport(pt, "CSF-PR  " + "  " + accession + "  Peptides");
                excelExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
                excelExport.setExportFileName("CSF-PR / " + accession + " / Peptides.xls");
                excelExport.setMimeType(CsvExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.convertTable();
                index++;
            } else {
                excelExport.setReportTitle("CSF-PR / " + key + " / " + accession + " / Peptides");
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
