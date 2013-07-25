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
        label.setText("Start Peptides processing...");
        exp.setPeptideList(this.getPeptides());
        exp.setPeptidesNumber(this.getValidatedPeptideNuumber(exp.getPeptideList()));
        if (!exp.getPeptideList().isEmpty()) {
            exp.setPeptidesInclude(1);
        } else {
            exp.setPeptidesInclude(0);
        }
        label.setText("Start Fractions processing...");
        exp = this.getFractionList(exp);
        if (!exp.getFractionsList().isEmpty() || exp.getFractionsList().size() != 1) {
            exp.setFractionsNumber(exp.getFractionsList().size());
        } else {
            exp.setFractionsNumber(0);
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
            if (pepb.getValidated() == 1.0) {
                number++;
            }
        }
        return number;
    }

    public ExperimentBean addGlicoPep(File glycopeptide, ExperimentBean exp) {
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
            String key = "[" + pb.getProtein() + "][" + pb.getSequenceTagged() + "]";
            if (pepList.containsKey(key)) {
                System.out.println("Updating is working");
                PeptideBean temPb = pepList.get(key);
                if (temPb.getGlycopatternPositions() != null) {
                    pb.setGlycopatternPositions(temPb.getGlycopatternPositions());
                }
                if (temPb.isDeamidationAndGlycopattern() != null) {
                    pb.setDeamidationAndGlycopattern(temPb.isDeamidationAndGlycopattern());
                }
            }
            updatedList.put(index, pb);
        }
        exp.setPeptideList(updatedList);
        return exp;
    }
}
