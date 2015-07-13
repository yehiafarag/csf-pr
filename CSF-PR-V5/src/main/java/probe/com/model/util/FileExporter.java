/*
 * To change this license titleRow, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.util;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Table;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons.ComparisonProtein;
import probe.com.model.beans.GroupsComparison;
import probe.com.model.beans.PeptideBean;
import probe.com.view.core.CustomExternalLink;
import probe.com.bin.ComparisonChart;

/**
 *
 * @author Yehia Farag
 */
public class FileExporter {

    /**
     * this function to be use for csv peptides exporting with large datasets
     *
     * @param allPeptides peptides to be exported
     * @param datasetName
     * @param dataType validated/all
     * @param path for the file csf-pr file system
     */
    public void expotPeptidesToCSV(Map<Integer, PeptideBean> allPeptides, String datasetName, String dataType, String path) {
        File csvText = new File(path,"CSF-PR - " + datasetName + " - All - " + dataType+ " - Peptides.csv");// "CSF-PR - " + datasetName + " - All - " + dataType + "- Peptides" + ".csv");
        PrintWriter out1 = null;
        try {
            if (csvText.exists()) {
                return;
            }
            csvText.createNewFile();
            FileWriter outFile = new FileWriter(csvText, true);
            out1 = new PrintWriter(outFile);
            String title = "CSF-PR / " + datasetName + " / " + dataType + " Peptides";
            out1.append(title);
            out1.append('\n');
            String header = " ,PI,Peptide Protein(s),Peptide Prot. Descrip.,Sequence,AA Before,AA After,Start,End,#Spectra,Other Protein(s),Other Prot Descrip.,Variable Modification,Location Confidence,Precursor Charge(s),Enzymatic,Sequence Annotated,Glycopeptide,Glyco Position(s),Confidence,Validated";
            String[] headerArr = header.split(",");
            int index = 0;
            for (String str : headerArr) {
                out1.append(str);
                if (index != headerArr.length - 1) {
                    out1.append(',');
                }
                index++;

            }
            out1.append('\n');

            index = 0;
            for (PeptideBean pb : allPeptides.values()) {
                out1.append("" + index++);
                out1.append(',');
                out1.append(pb.getProteinInference());
                out1.append(',');
                out1.append(pb.getPeptideProteins().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getPeptideProteinsDescriptions().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getSequence());
                out1.append(',');
                out1.append(pb.getAaBefore());
                out1.append(',');
                out1.append(pb.getAaAfter());
                out1.append(',');
                out1.append(pb.getPeptideStart());
                out1.append(',');
                out1.append(pb.getPeptideEnd());
                out1.append(',');
                out1.append("" + pb.getNumberOfValidatedSpectra());
                out1.append(',');
                out1.append(pb.getOtherProteins().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getOtherProteinDescriptions().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getVariableModification().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getLocationConfidence().replaceAll(",", ";"));
                out1.append(',');
                out1.append(pb.getPrecursorCharges().replaceAll(",", ";"));
                out1.append(',');
                out1.append("" + pb.isEnzymatic());
                out1.append(',');
                out1.append(pb.getSequenceTagged());
                out1.append(',');
                out1.append(("" + pb.isDeamidationAndGlycopattern()).toUpperCase());
                out1.append(',');
                if(pb.getGlycopatternPositions() !=null)
                    out1.append("" + pb.getGlycopatternPositions());
                else
                    out1.append("");
                out1.append(',');
                int x = (int) pb.getConfidence();
                out1.append("" + x);
                out1.append(',');
                if (pb.getValidated() == 1.0) {
                    out1.append("TRUE");
                } else {
                    out1.append("FALSE");
                }
                out1.append('\n');

            }       
            
            
            out1.flush();
            out1.close();
        } catch (Exception exp) {
            System.err.println(exp.getLocalizedMessage());
            if(out1!=null){
            out1.flush();
            out1.close();
            }
        }
    }
    
    /**
     * this function to be use for xls peptides exporting with large datasets
     *
     * @param allPeptides peptides to be exported
     * @param datasetName
     * @param dataType validated/all
     * @param path for the file csf-pr file system
     */
    public void expotPeptidesToXLS(Map<Integer, PeptideBean> allPeptides, String datasetName, String dataType, String path) {
        File xlsText = new File(path,"CSF-PR - " + datasetName + " - All - " + dataType+ " - Peptides.xls");// "CSF-PR - " + datasetName + " - All - " + dataType + "- Peptides" + ".csv");
        
        try {
            if (xlsText.exists()) {
                return;
            }

            FileOutputStream fileOut = new FileOutputStream(xlsText);
            HSSFWorkbook workbook = new HSSFWorkbook();
            HSSFSheet worksheet = workbook.createSheet("POI Worksheet");
            //title
            String title = "CSF-PR / " + datasetName + " / " + dataType + " Peptides";
            HSSFRow titleRow = worksheet.createRow(0);
            HSSFCell cellA1 = titleRow.createCell(0);
            cellA1.setCellValue(title);
            //header
            String header = " ,PI,Peptide Protein(s),Peptide Prot. Descrip.,Sequence,AA Before,AA After,Start,End,#Spectra,Other Protein(s),Other Prot Descrip.,Variable Modification,Location Confidence,Precursor Charge(s),Enzymatic,Sequence Annotated,Glycopeptide,Glyco Position(s),Confidence,Validated";
            String[] headerArr = header.split(",");
            HSSFRow headerRow = worksheet.createRow(1);
            int index = 0;
            for (String str : headerArr) {
                HSSFCell cell = headerRow.createCell(index);
                cell.setCellValue(str);
                index++;

            }                        
            //data

             index = 0;
            for (PeptideBean pb : allPeptides.values()) {
                HSSFRow peptideRow = worksheet.createRow(index+2);
                HSSFCell cell0 = peptideRow.createCell(0);
                cell0.setCellValue(index);
                  HSSFCell cell1 = peptideRow.createCell(1);
                cell1.setCellValue(pb.getProteinInference());
                 HSSFCell cell2 = peptideRow.createCell(2);
                cell2.setCellValue(pb.getPeptideProteins().replaceAll(",", ";"));
                HSSFCell cell3 = peptideRow.createCell(3);
                cell3.setCellValue(pb.getPeptideProteinsDescriptions().replaceAll(",", ";"));
                HSSFCell cell4 = peptideRow.createCell(4);
                cell4.setCellValue(pb.getSequence());
                 HSSFCell cell5 = peptideRow.createCell(5);
                cell5.setCellValue(pb.getAaBefore());
                HSSFCell cell6 = peptideRow.createCell(6);
                cell6.setCellValue(pb.getAaAfter());
                 HSSFCell cell7 = peptideRow.createCell(7);
                cell7.setCellValue(pb.getPeptideStart());
                 HSSFCell cell8 = peptideRow.createCell(8);
                cell8.setCellValue(pb.getPeptideEnd());
                 HSSFCell cell9 = peptideRow.createCell(9);
                cell9.setCellValue(pb.getNumberOfValidatedSpectra());
                 HSSFCell cell10 = peptideRow.createCell(10);
                cell10.setCellValue(pb.getOtherProteins().replaceAll(",", ";"));
                 HSSFCell cell11 = peptideRow.createCell(11);
                cell11.setCellValue(pb.getOtherProteinDescriptions().replaceAll(",", ";"));
                HSSFCell cell12 = peptideRow.createCell(12);
                cell12.setCellValue(pb.getVariableModification().replaceAll(",", ";"));
                HSSFCell cell13 = peptideRow.createCell(13);
                cell13.setCellValue(pb.getLocationConfidence().replaceAll(",", ";"));
                HSSFCell cell14 = peptideRow.createCell(14);
                cell14.setCellValue(pb.getPrecursorCharges().replaceAll(",", ";"));
               HSSFCell cell15 = peptideRow.createCell(15);
                cell15.setCellValue(pb.isEnzymatic());
               HSSFCell cell16 = peptideRow.createCell(16);
                cell16.setCellValue(pb.getSequenceTagged());
                 HSSFCell cell17 = peptideRow.createCell(17);
                cell17.setCellValue(pb.isDeamidationAndGlycopattern());
                 HSSFCell cell18 = peptideRow.createCell(18);
                if(pb.getGlycopatternPositions() !=null)
                 cell18.setCellValue(pb.getGlycopatternPositions());
                else
                  cell18.setCellValue("");  
                int x = (int) pb.getConfidence();
                HSSFCell cell19 = peptideRow.createCell(19);
                cell19.setCellValue(x);
                
               HSSFCell cell20 = peptideRow.createCell(20);
               
                if (pb.getValidated() == 1.0) {
                     cell20.setCellValue("TRUE");
                } else {
                    cell20.setCellValue("FALSE");
                }
                index++;

            }       

            workbook.write(fileOut);
            fileOut.flush();
            fileOut.close();
        } catch (Exception exp) {
            System.err.println(exp.getMessage());
        }

    }
    
    
    public void exportQuantComparisonTable(GroupsComparison[]  comparisonMap){
        Map<String, String> accessionMap = new HashMap<String, String>();
          int compIndex = 0;
          int t = 0;
          String[] columnHeaders = new String[comparisonMap.length+1];
          
          columnHeaders[t++] = "Accession";
          
        Map<String, ComparisonProtein[]> protSetMap = new HashMap<String, ComparisonProtein[]>();
 for (GroupsComparison comp : comparisonMap) {
             columnHeaders[t++] = comp.getComparisonHeader();
          Map<String, ComparisonProtein> protList = comp.getComparProtsMap();
            for (String key2 : protList.keySet()) {
                ComparisonProtein prot = protList.get(key2);
                accessionMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), prot.getUniProtAccess());
                if (!protSetMap.containsKey(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim())) {
                    protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), new ComparisonProtein[comparisonMap.length]);
                }
                ComparisonProtein[] compArr = protSetMap.get(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim());
                compArr[compIndex] = prot;
                protSetMap.put(("--" + prot.getUniProtAccess().toLowerCase().trim() + "," + prot.getProtName().toLowerCase().trim()).toLowerCase().trim(), compArr);
            }
            compIndex++;
        }
 
        String[][] values = new String[protSetMap.size()+1][columnHeaders.length];
        int b = 0;
        for(String str:columnHeaders){
            values[0][b]=str;
            b++;
        }
        
        
        int index = 1;
        for (String protAccName : protSetMap.keySet()) {
            int i = 0;
            String protAcc = protAccName.replace("--", "").trim().split(",")[0];
            Object[] tableRow = new Object[1 + comparisonMap.length];            
            tableRow[i++] = protAcc.toUpperCase();
            for (GroupsComparison cg : comparisonMap) {
                ComparisonProtein cp = protSetMap.get(protAccName)[i - 1];
                if (cp == null) {
                    tableRow[i] = null;
                } else {

                    cp.updateLabelLayout();
                    tableRow[i] = cp.getCellValue();
                }
                i++;
            }

            for (int f = 0; f < tableRow.length; f++) {
                if(tableRow[f]== null)
                    tableRow[f]="0.0";
                values[index][f] = tableRow[f].toString();

            }

//            this.groupsComparisonTable.addItem(tableRow, index);
            index++;
        }

        ///write data to file
        File text = new File("C:\\divaFiles", "csf.txt");
        PrintWriter out1 = null;
        FileWriter outFile = null;
        try {
            if (text.exists()) {
                text.delete();
            }
            text.createNewFile();
            outFile = new FileWriter(text, true);
            out1 = new PrintWriter(outFile);

            for (String[] strArr : values) {
                String line = "";
                for (String cell : strArr) {
                    line += cell + "\t";
                }
                line = line.substring(0, line.length() - 2);
                out1.append(line);
                out1.println();

            }

            out1.flush();
            out1.close();
            outFile.flush();
            outFile.close();
            
           
        } catch (Exception e) {
//            System.err.println(e.getMessage());
        } finally {
            System.gc();
        }
        


    
    }

}
