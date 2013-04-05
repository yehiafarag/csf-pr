package com.model.beans;

import java.io.Serializable;

public class ProteinBean implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String accession;
	private String Description;
	
	
	private String otherProteins;
	private String proteinInferenceClass;
	private double sequenceCoverage; 
	private double observableCoverage;
	private String confidentPtmSites;
	private String numberConfident;
	private String otherPtmSites;
	private String numberOfOther;
	
	private int numberValidatedPeptides;
	private int numberValidatedSpectra;
	private double emPai;
	private double nsaf;
	private double mw_kDa;
	private double score;
	private double confidence;
	private boolean Starred;
	
	
	
	
	private String peptideFractionSpread_lower_range_kDa;
	private String peptideFractionSpread_upper_range_kDa;
	private String spectrumFractionSpread_lower_range_kDa;
	private String spectrumFractionSpread_upper_range_kDa;
	 
	private boolean nonEnzymaticPeptides;
	
	private int numberOfPeptidePerFraction;
	private int numberOfSpectraPerFraction;
	private double AveragePrecursorIntensityPerFraction;
	
	public void setAccession(String accession) {
		this.accession = accession;
	}
	public String getAccession() {
		return accession;
	}
	public void setDescription(String description) {
		Description = description;
	}
	public String getDescription() {
		return Description;
	}
	public void setOtherProteins(String otherProteins) {
		this.otherProteins = otherProteins;
	}
	public String getOtherProteins() {
		return otherProteins;
	}
	public void setProteinInferenceClass(String proteinInferenceClass) {
		this.proteinInferenceClass = proteinInferenceClass;
	}
	public String getProteinInferenceClass() {
		return proteinInferenceClass;
	}
	public void setSequenceCoverage(double sequenceCoverage) {
		this.sequenceCoverage = sequenceCoverage;
	}
	public double getSequenceCoverage() {
		return sequenceCoverage;
	}
	public void setObservableCoverage(double observableCoverage) {
		this.observableCoverage = observableCoverage;
	}
	public double getObservableCoverage() {
		return observableCoverage;
	}
	public void setConfidentPtmSites(String confidentPtmSites) {
		this.confidentPtmSites = confidentPtmSites;
	}
	public String getConfidentPtmSites() {
		return confidentPtmSites;
	}
	public void setNumberConfident(String numberConfident) {
		this.numberConfident = numberConfident;
	}
	public String getNumberConfident() {
		return numberConfident;
	}
	public void setOtherPtmSites(String otherPtmSites) {
		this.otherPtmSites = otherPtmSites;
	}
	public String getOtherPtmSites() {
		return otherPtmSites;
	}
	public void setNumberOfOther(String numberOfOther) {
		this.numberOfOther = numberOfOther;
	}
	public String getNumberOfOther() {
		return numberOfOther;
	}
	public void setNumberValidatedPeptides(int numberValidatedPeptides) {
		this.numberValidatedPeptides = numberValidatedPeptides;
	}
	public int getNumberValidatedPeptides() {
		return numberValidatedPeptides;
	}
	public void setNumberValidatedSpectra(int numberValidatedSpectra) {
		this.numberValidatedSpectra = numberValidatedSpectra;
	}
	public int getNumberValidatedSpectra() {
		return numberValidatedSpectra;
	}
	public void setEmPai(double emPai) {
		this.emPai = emPai;
	}
	public double getEmPai() {
		return emPai;
	}
	public void setNsaf(double nsaf) {
		this.nsaf = nsaf;
	}
	public double getNsaf() {
		return nsaf;
	}
	public void setMw_kDa(double mw_kDa) {
		this.mw_kDa = mw_kDa;
	}
	public double getMw_kDa() {
		return mw_kDa;
	}
	public void setScore(double score) {
		this.score = score;
	}
	public double getScore() {
		return score;
	}
	public void setConfidence(double confidence) {
		this.confidence = confidence;
	}
	public double getConfidence() {
		return confidence;
	}
	public void setStarred(boolean starred) {
		Starred = starred;
	}
	public boolean isStarred() {
		return Starred;
	}
	public void setNumberOfPeptidePerFraction(int numberOfPeptidePerFraction) {
		this.numberOfPeptidePerFraction = numberOfPeptidePerFraction;
	}
	public int getNumberOfPeptidePerFraction() {
		return numberOfPeptidePerFraction;
	}
	public void setNumberOfSpectraPerFraction(int numberOfSpectraPerFraction) {
		this.numberOfSpectraPerFraction = numberOfSpectraPerFraction;
	}
	public int getNumberOfSpectraPerFraction() {
		return numberOfSpectraPerFraction;
	}
	public void setAveragePrecursorIntensityPerFraction(
			double averagePrecursorIntensityPerFraction) {
		AveragePrecursorIntensityPerFraction = averagePrecursorIntensityPerFraction;
	}
	public double getAveragePrecursorIntensityPerFraction() {
		return AveragePrecursorIntensityPerFraction;
	}
	public boolean isNonEnzymaticPeptides() {
		return nonEnzymaticPeptides;
	}
	public void setNonEnzymaticPeptides(boolean nonEnzymaticPeptides) {
		this.nonEnzymaticPeptides = nonEnzymaticPeptides;
	}
	public String getPeptideFractionSpread_lower_range_kDa() {
		return peptideFractionSpread_lower_range_kDa;
	}
	public void setPeptideFractionSpread_lower_range_kDa(
			String peptideFractionSpread_lower_range_kDa) {
		this.peptideFractionSpread_lower_range_kDa = peptideFractionSpread_lower_range_kDa;
	}
	public String getPeptideFractionSpread_upper_range_kDa() {
		return peptideFractionSpread_upper_range_kDa;
	}
	public void setPeptideFractionSpread_upper_range_kDa(
			String peptideFractionSpread_upper_range_kDa) {
		this.peptideFractionSpread_upper_range_kDa = peptideFractionSpread_upper_range_kDa;
	}
	public String getSpectrumFractionSpread_lower_range_kDa() {
		return spectrumFractionSpread_lower_range_kDa;
	}
	public void setSpectrumFractionSpread_lower_range_kDa(
			String spectrumFractionSpread_lower_range_kDa) {
		this.spectrumFractionSpread_lower_range_kDa = spectrumFractionSpread_lower_range_kDa;
	}
	public String getSpectrumFractionSpread_upper_range_kDa() {
		return spectrumFractionSpread_upper_range_kDa;
	}
	public void setSpectrumFractionSpread_upper_range_kDa(
			String spectrumFractionSpread_upper_range_kDa) {
		this.spectrumFractionSpread_upper_range_kDa = spectrumFractionSpread_upper_range_kDa;
	}

}
