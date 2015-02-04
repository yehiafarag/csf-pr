/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.quantcsf.beans;

/**
 *
 * @author Yehia Farag
 */
public class QuantPeptide {
    private String qPeptideKey,    peptideSequance,	peptideModification,modificationComment ;
    private double fcPatientGroupIonPatientGroupII;
    
    
     public String getPeptideSequance() {
        return peptideSequance;
    }

    public void setPeptideSequance(String peptideSequance) {
        this.peptideSequance = peptideSequance;
    }
    public String getPeptideModification() {
        return peptideModification;
    }

    public void setPeptideModification(String peptideModification) {
        this.peptideModification = peptideModification;
    }
    public String getModificationComment() {
        return modificationComment;
    }

    public void setModificationComment(String modificationComment) {
        this.modificationComment = modificationComment;
    }

    public String getqPeptideKey() {
        return qPeptideKey;
    }

    public void setqPeptideKey(String qPeptideKey) {
        this.qPeptideKey = qPeptideKey;
    }
      public double getFcPatientGroupIonPatientGroupII() {
        return fcPatientGroupIonPatientGroupII;
    }

    public void setFcPatientGroupIonPatientGroupII(double fcPatientGroupIonPatientGroupII) {
        this.fcPatientGroupIonPatientGroupII = fcPatientGroupIonPatientGroupII;
    }
    
}
