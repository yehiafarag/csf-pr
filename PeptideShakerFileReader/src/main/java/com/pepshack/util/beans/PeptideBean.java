package com.pepshack.util.beans;

import java.io.Serializable;

public class PeptideBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String protein;
    private String otherProteins;
    private String peptideProteins;
    private String otherProteinDescriptions;
    private String peptideProteinsDescriptions;
    private String aaBefore;
    private String sequence;
    private String aaAfter;
    private String peptideStart;
    private String fixedModification;
    private String peptideEnd;
    private String variableModification;
    private String locationConfidence;
    private String precursorCharges;
    private int numberOfValidatedSpectra;
    private double score;
    private double confidence;
    private int peptideId;
    private String proteinInference;
    private String sequenceTagged;
    private boolean enzymatic;
    private double validated;
    private int decoy;
    private boolean starred;
    private Boolean deamidationAndGlycopattern;
    private String glycopatternPositions;

    public String getProtein() {
        return protein;
    }

    public void setProtein(String protein) {
        this.protein = protein;
    }

    public String getOtherProteins() {
        return otherProteins;
    }

    public void setOtherProteins(String otherProteins) {
        this.otherProteins = otherProteins;
    }

    public String getPeptideProteins() {
        return peptideProteins;
    }

    public void setPeptideProteins(String peptideProteins) {
        this.peptideProteins = peptideProteins;
    }

    public String getOtherProteinDescriptions() {
        return otherProteinDescriptions;
    }

    public void setOtherProteinDescriptions(String otherProteinDescriptions) {
        this.otherProteinDescriptions = otherProteinDescriptions;
    }

    public String getPeptideProteinsDescriptions() {
        return peptideProteinsDescriptions;
    }

    public void setPeptideProteinsDescriptions(
            String peptideProteinsDescriptions) {
        this.peptideProteinsDescriptions = peptideProteinsDescriptions;
    }

    public String getAaBefore() {
        return aaBefore;
    }

    public void setAaBefore(String aaBefore) {
        this.aaBefore = aaBefore;
    }

    public String getSequence() {
        return sequence;
    }

    public void setSequence(String sequence) {
        this.sequence = sequence;
    }

    public String getAaAfter() {
        return aaAfter;
    }

    public void setAaAfter(String aaAfter) {
        this.aaAfter = aaAfter;
    }

    public String getPeptideStart() {
        return peptideStart;
    }

    public void setPeptideStart(String peptideStart) {
        this.peptideStart = peptideStart;
    }

    public int getPeptideId() {
        return peptideId;
    }

    public void setPeptideId(int peptideId) {
        this.peptideId = peptideId;
    }

    public int getNumberOfValidatedSpectra() {
        return numberOfValidatedSpectra;
    }

    public void setNumberOfValidatedSpectra(int numberOfValidatedSpectra) {
        this.numberOfValidatedSpectra = numberOfValidatedSpectra;
    }

    public String getLocationConfidence() {
        return locationConfidence;
    }

    public void setLocationConfidence(String locationConfidence) {
        this.locationConfidence = locationConfidence;
    }

    public String getVariableModification() {
        return variableModification;
    }

    public void setVariableModification(String variableModification) {
        this.variableModification = variableModification;
    }

    public String getPeptideEnd() {
        return peptideEnd;
    }

    public void setPeptideEnd(String peptideEnd) {
        this.peptideEnd = peptideEnd;
    }

    public String getPrecursorCharges() {
        return precursorCharges;
    }

    public void setPrecursorCharges(String precursorCharges) {
        this.precursorCharges = precursorCharges;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getConfidence() {
        return confidence;
    }

    public void setConfidence(double confidence) {
        this.confidence = confidence;
    }

    public String getFixedModification() {
        return fixedModification;
    }

    public void setFixedModification(String fixedModification) {
        this.fixedModification = fixedModification;
    }

    public String getProteinInference() {
        return proteinInference;
    }

    public void setProteinInference(String proteinInference) {
        this.proteinInference = proteinInference;
    }

    public String getSequenceTagged() {
        return sequenceTagged;
    }

    public void setSequenceTagged(String sequenceTagged) {
        this.sequenceTagged = sequenceTagged;
    }

    public boolean isEnzymatic() {
        return enzymatic;
    }

    public void setEnzymatic(boolean enzymatic) {
        this.enzymatic = enzymatic;
    }

    public double getValidated() {
        return validated;
    }

    public void setValidated(double validated) {
        this.validated = validated;
    }

    public int getDecoy() {
        return decoy;
    }

    public void setDecoy(int decoy) {
        this.decoy = decoy;
    }

    public boolean isStarred() {
        return starred;
    }

    public void setStarred(boolean starred) {
        this.starred = starred;
    }

    public Boolean isDeamidationAndGlycopattern() {
        return deamidationAndGlycopattern;
    }

    public void setDeamidationAndGlycopattern(boolean deamidationAndGlycopattern) {
        this.deamidationAndGlycopattern = deamidationAndGlycopattern;
    }

    public String getGlycopatternPositions() {
        return glycopatternPositions;
    }

    public void setGlycopatternPositions(String glycopatternPositions) {
        this.glycopatternPositions = glycopatternPositions;
    }
}
