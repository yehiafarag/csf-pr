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
    private String qPeptideKey,    peptideSequance,	peptideModification,modificationComment,fc,strPvalue,pvalueComment ;

    public String getPvalueComment() {
        return pvalueComment;
    }

    public void setPvalueComment(String pvalueComment) {
        this.pvalueComment = pvalueComment;
    }
    private double fcPatientGroupIonPatientGroupII,pvalue,roc;
    private int protKey;

    public String getFc() {
        return fc;
    }

    public void setFc(String fc) {
        this.fc = fc;
    }

    public String getStrPvalue() {
        return strPvalue;
    }

    public void setStrPvalue(String strPvalue) {
        this.strPvalue = strPvalue;
    }

    public int getProtKey() {
        return protKey;
    }

    public void setProtKey(int protKey) {
        this.protKey = protKey;
    }

    public int getDsKey() {
        return dsKey;
    }

    public void setDsKey(int dsKey) {
        this.dsKey = dsKey;
    }
    private int dsKey;
    
    
    
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

    public double getPvalue() {
        return pvalue;
    }

    public void setPvalue(double pvalue) {
        this.pvalue = pvalue;
    }

    public double getRoc() {
        return roc;
    }

    public void setRoc(double roc) {
        this.roc = roc;
    }
    
}
