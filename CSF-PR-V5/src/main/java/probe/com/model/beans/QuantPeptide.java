/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

import java.io.Serializable;

/**
 *
 * @author yfa041
 */
public class QuantPeptide implements Serializable {

    private int uniqueId;
    private int protIndex;
    private int DsKey;
    private String peptideSequance;
    private String string_p_value;
    private String peptideModification;
    private String modification_comment;
    private String p_value_comments;

    public String getUniprotProteinAccession() {
        return uniprotProteinAccession;
    }

    public void setUniprotProteinAccession(String uniprotProteinAccession) {
        this.uniprotProteinAccession = uniprotProteinAccession;
    }
    private double p_value;
    private String string_fc_value;
    private String uniprotProteinAccession;

    public int getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(int uniqueId) {
        this.uniqueId = uniqueId;
    }

    public int getProtIndex() {
        return protIndex;
    }

    public void setProtIndex(int protIndex) {
        this.protIndex = protIndex;
    }

    public int getDsKey() {
        return DsKey;
    }

    public void setDsKey(int DsKey) {
        this.DsKey = DsKey;
    }

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

    public String getModification_comment() {
        return modification_comment;
    }

    public void setModification_comment(String modification_comment) {
        this.modification_comment = modification_comment;
    }

    public String getString_fc_value() {
        return string_fc_value;
    }

    public void setString_fc_value(String string_fc_value) {
        this.string_fc_value = string_fc_value;
    }

    public String getString_p_value() {
        return string_p_value;
    }

    public void setString_p_value(String string_p_value) {
        this.string_p_value = string_p_value;
    }

    public String getP_value_comments() {
        return p_value_comments;
    }

    public void setP_value_comments(String p_value_comments) {
        this.p_value_comments = p_value_comments;
    }
    double roc_auc, fc_value;

    public double getP_value() {
        return p_value;
    }

    public void setP_value(double p_value) {
        this.p_value = p_value;
    }

    public double getRoc_auc() {
        return roc_auc;
    }

    public void setRoc_auc(double roc_auc) {
        this.roc_auc = roc_auc;
    }

    public double getFc_value() {
        return fc_value;
    }

    public void setFc_value(double fc_value) {
        this.fc_value = fc_value;
    }
    
    @Override
    public String toString() {
        return "prot index "+protIndex+" ----- ds "+DsKey+" ----- pepSequance  " + peptideSequance;
    }
    
    

}
