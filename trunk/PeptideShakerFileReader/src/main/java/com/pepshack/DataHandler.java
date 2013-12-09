/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepshack;

import com.pepshack.util.FilesReader;
import com.pepshack.util.beans.ExperimentBean;
import com.pepshack.util.beans.PeptideBean;
import com.pepshack.util.beans.ProteinBean;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;

/**
 *
 * @author Yehia Farag
 */
public class DataHandler {

    private UpdatedOutputGenerator exporter;

    public ExperimentBean handelData(PSFileImporter importer, ExperimentBean exp, JLabel label) {
        label.setText("Start Proteins processing...");
        exporter = new UpdatedOutputGenerator(importer);
        exp.setProteinList(this.getProteins());
        exp.setProteinsNumber(exp.getProteinList().size());
        int numberValidProt = getValidatedProtNumber(exp.getProteinList());
        exp.setNumberValidProt(numberValidProt);
        label.setText("Start Peptides processing...");
        exp.setPeptideList(this.getPeptides());
        exp.setPeptidesNumber(this.getValidatedPeptideNuumber(exp.getPeptideList()));
        if (exp.getPeptideList().isEmpty()) {
            exp.setPeptidesInclude(0);
        } else {
            exp.setPeptidesInclude(1);
        }
        label.setText("Start Fractions processing...");
        exp = this.getFractionList(exp);
        if (exp.getFractionsList().isEmpty()) {//|| exp.getFractionsList().size() != 1) {
            exp.setFractionsNumber(0);
        } else {
            exp.setFractionsNumber(exp.getFractionsList().size());
        }
        importer.clearData(true);
        return exp;
    }

    private Map<String, ProteinBean> getProteins() {
        Map<String, ProteinBean> proteinList = exporter.getProteinsOutput();
        return proteinList;
    }

    private Map<Integer, PeptideBean> getPeptides() {
        Map<Integer, PeptideBean> peptideList = exporter.getPeptidesOutput();
        return peptideList;
    }

    private ExperimentBean getFractionList(ExperimentBean exp) {
        return exporter.getFractionsOutput(exp);
    }

    private int getValidatedPeptideNuumber(Map<Integer, PeptideBean> pepList) {
        int number = 0;
        for (PeptideBean pepb : pepList.values()) {
            if (pepb.getDecoy()!=1 && pepb.getValidated() == 1.0) {
                number++;
                
            }
        }
        
        System.out.println("all pep number "+pepList.size());
        System.out.println("v pep number "+number);
        return number;
    }

    public  ExperimentBean addGlicoPep(File glycopeptide, ExperimentBean exp) {
        if (!glycopeptide.exists())
               ; else {
            FilesReader reader = new FilesReader();
            Map<String, PeptideBean> pepList = reader.readGlycoFile(glycopeptide);
            exp = updatePeptideList(pepList, exp);
        }
        return exp;
    }

    private ExperimentBean updatePeptideList(Map<String, PeptideBean> pepList, ExperimentBean exp) {
        Map<Integer, PeptideBean> updatedList = new HashMap<Integer, PeptideBean>();
        for (int index : exp.getPeptideList().keySet()) {
            PeptideBean pb = exp.getPeptideList().get(index);
            pb.setLikelyNotGlycosite(false);
             pb.setDeamidationAndGlycopattern(false);
             pb.setGlycopatternPositions("");
                
            String key = "[" + pb.getProtein() + "][" + pb.getSequenceTagged() + "]";
            if (pepList.containsKey(key)) {
                PeptideBean temPb = pepList.get(key);
                if (temPb.getGlycopatternPositions() != null) {
                    pb.setGlycopatternPositions(temPb.getGlycopatternPositions());
                }
                if (temPb.isDeamidationAndGlycopattern() != null) {
                    pb.setDeamidationAndGlycopattern(temPb.isDeamidationAndGlycopattern());
                }
                if (temPb.isLikelyNotGlycopeptide()) {
                    pb.setLikelyNotGlycosite(temPb.isLikelyNotGlycopeptide());
                }
            }
            updatedList.put(index, pb);
        }
        exp.setPeptideList(updatedList);
        return exp;
    }

    private int getValidatedProtNumber(Map<String, ProteinBean> proteinsList) {
        int counter = 0;
        for (ProteinBean pb : proteinsList.values()) {
            if (pb.isValidated()) {
                counter++;
            }
        }
        return counter;
    }

}
