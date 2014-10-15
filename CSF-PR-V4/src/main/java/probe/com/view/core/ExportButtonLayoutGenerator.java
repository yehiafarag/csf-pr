/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.addon.tableexport.ExcelExport;
import com.vaadin.ui.Button;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.Map;
/**
 *
 * @author Yehia Farag
 */
import org.vaadin.dialogs.ConfirmDialog;
import probe.com.view.components.PeptideTable;

public class ExportButtonLayoutGenerator extends VerticalLayout implements Serializable {

    private boolean clicked = false;
    private CsvExport csvExport = null;
    private ExcelExport excelExport = null;

    ;
 
    public ExportButtonLayoutGenerator(final Table t, UI ui, final String type, final String name, final Map<String, PeptideTable> pl, final String accession) {

        final OptionGroup group1 = new OptionGroup();
        group1.addItem("CVS");
        group1.addItem("EXCEL");
        final ConfirmDialog d = ConfirmDialog  
                
                .show(
                ui,
                "Export Type".toUpperCase(),
                "<b>Please Choose Export File Format</b> <br/>"
                + "<input type=\"radio\" name=\"group1\" value=\"CSV\"> CSV<br/> \n" +
                "<input type=\"radio\" name=\"group1\" value=\"EXCEL\" checked>EXCEL"
                ,
                "OK", "Cancel", new ConfirmDialog.Listener() {
            @Override
            public void onClose(ConfirmDialog dialog) {

                if (pl == null) {
                    if (dialog.isConfirmed()) {
                        exportCsv(t, type, name, accession);
                    } else if (!dialog.isConfirmed() && clicked) {
                        // exportXls(t, type, name);
                    } else {
                        dialog.close();
                    }
                } else {

                    if (dialog.isConfirmed()) {
                        exportAllPepCsv(pl, accession);
                    } else if (!dialog.isConfirmed() && clicked) {
                        //  exportAllPepXls(pl, accession);
                    } else {
                        dialog.close();
                    }
                }
                clicked = false;
            }
        });
        d.setStyleName(Reindeer.WINDOW_BLACK);
        d.setContentMode(ConfirmDialog.ContentMode.HTML);
        d.setHeight("10em");
        d.setClosable(true);
        d.getCancelButton().addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                clicked = true;
                if (pl != null) {
                    exportAllPepXls(pl, accession);
                } else {
                    exportXls(t, type, name, accession);
                }
            }
        });



    }

    private void exportCsv(Table t, String type, String name, String accession) {

        csvExport = new CsvExport(t, type);
        if (type.equalsIgnoreCase("Proteins")) {
            csvExport.setReportTitle(type + " for Data Set ( CSF-PR / " + name + " )");
            csvExport.setExportFileName(type + " for ( " + name + " ).csv");
        } else if (type.equalsIgnoreCase("Peptides")) {
            csvExport.setReportTitle("Peptides for ( " + accession + " ) Data Set ( CSF-PR /  " + name + " )");
            csvExport.setExportFileName("Peptides for ( " + accession + " ).csv");
        } else if (type.equalsIgnoreCase("Fractions")) {
            csvExport.setReportTitle("Fractions for ( " + accession + " ) Data Set ( CSF-PR / " + name + " )");
            csvExport.setExportFileName("Fractions for ( " + accession + " ).csv");

        }
        csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
        csvExport.setDisplayTotals(false);
        csvExport.export();


    }

    private void exportXls(Table t, String type, String name, String accession) {
        excelExport = new ExcelExport(t);
        if (type.equalsIgnoreCase("Proteins")) {
            excelExport.setReportTitle(type + " for Data Set ( CSF-PR / " + name + " )");
            excelExport.setExportFileName(type + " for ( " + name + " ).xls");
        } else if (type.equalsIgnoreCase("Peptides")) {
            excelExport.setReportTitle("Peptides for ( " + accession + " ) Data Set ( CSF-PR / " + name + " )");
            excelExport.setExportFileName("Peptides for ( " + accession + " ).csv");

        } else if (type.equalsIgnoreCase("Fractions")) {
            excelExport.setReportTitle("Fractions for ( " + accession + " ) Data Set ( CSF-PR / " + name + " )");
            excelExport.setExportFileName("Fractions for ( " + accession + " ).xls");

        }
        excelExport.setMimeType(excelExport.EXCEL_MIME_TYPE);
        excelExport.setDisplayTotals(false);
        excelExport.export();


    }

    private void exportAllPepCsv(Map<String, PeptideTable> pl, String accession) {
        int index = 0;
        for (String key : pl.keySet()) {
            PeptideTable pt = pl.get(key);
            addComponent(pt);
            if (index == 0) {
                csvExport = new CsvExport(pt, "Peptides");
                csvExport.setReportTitle("Protein's Peptides for  ( " + accession + " ) from ( CSF-PR / " + key + " ) Data Set");
                csvExport.setExportFileName("Protein's Peptides for ( " + accession + " ).csv");
                csvExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                csvExport.setDisplayTotals(false);
                csvExport.convertTable();
                index++;
            } else {
                csvExport.setReportTitle("Protein's Peptides for  ( " + accession + " ) from ( CSF-PR / " + key + " ) Data Set");
                csvExport.setDisplayTotals(false);
                csvExport.setRowHeaders(false);
                csvExport.setNextTable(pt, key);
                csvExport.setDisplayTotals(false);
                csvExport.convertTable();
            }
            index++;
        }
        csvExport.export();

    }

    private void exportAllPepXls(Map<String, PeptideTable> pl, String accession) {

        int index = 0;
        for (String key : pl.keySet()) {
            PeptideTable pt = pl.get(key);
            addComponent(pt);
            if (index == 0) {
                excelExport = new ExcelExport(pt, "Peptides");
                excelExport.setReportTitle("Protein's Peptides for  ( " + accession + " ) from ( CSF-PR / " + key + " ) Data Set");
                excelExport.setExportFileName("Protein's Peptides for ( " + accession + " ).xls");
                excelExport.setMimeType(CsvExport.EXCEL_MIME_TYPE);
                excelExport.setDisplayTotals(false);
                excelExport.convertTable();
                index++;
            } else {
                excelExport.setReportTitle("Protein's Peptides for  ( " + accession + " ) from ( CSF-PR / " + key + " ) Data Set");
                excelExport.setDisplayTotals(false);
                excelExport.setRowHeaders(false);
                excelExport.setNextTable(pt, key);
                excelExport.setDisplayTotals(false);
                excelExport.convertTable();
            }
            index++;
        }
        excelExport.export();

    }
}
