package probe.com.model.beans;

import java.io.Serializable;

public class ProteinBean implements Serializable,Comparable<ProteinBean> {

    public ProteinBean() {
    }

    public ProteinBean(ProteinBean pb) {
        this.accession = pb.getAccession();
        this.Description = pb.getDescription();
        this.validated = pb.isValidated();
        this.otherProteins = pb.getOtherProteins();
        this.proteinInferenceClass = pb.getProteinInferenceClass();
        this.sequenceCoverage = pb.getSequenceCoverage();
        this.observableCoverage = pb.getObservableCoverage();
        this.confidentPtmSites = pb.getConfidentPtmSites();
        this.numberConfident = pb.getNumberConfident();
        this.otherPtmSites = pb.getOtherPtmSites();
        this.numberOfOther = pb.getNumberOfOther();
        this.numberValidatedPeptides = pb.getNumberValidatedPeptides();
        this.numberValidatedSpectra = pb.getNumberValidatedSpectra();
        this.emPai = pb.getEmPai();
        this.nsaf = pb.getNsaf();
        this.mw_kDa = pb.getMw_kDa();
        this.score = pb.getScore();
        this.confidence = pb.getConfidence();
        this.Starred = pb.isStarred();
        this.peptideFractionSpread_lower_range_kDa = pb.getPeptideFractionSpread_lower_range_kDa();
        this.peptideFractionSpread_upper_range_kDa = pb.getPeptideFractionSpread_upper_range_kDa();
        this.spectrumFractionSpread_lower_range_kDa = pb.getSpectrumFractionSpread_lower_range_kDa();
        this.spectrumFractionSpread_upper_range_kDa = pb.getSpectrumFractionSpread_upper_range_kDa();
        this.nonEnzymaticPeptides = pb.isNonEnzymaticPeptides();
        this.geneName = pb.getGeneName();
        this.chromosomeNumber = pb.getChromosomeNumber();
        this.numberPeptides = pb.getNumberPeptides();
        this.numberSpectra = pb.getNumberSpectra();
        this.expId = pb.getExpId();
        this.decoy = pb.getDecoy();
        this.protGroupId= pb.getProtGroupId();


    }
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String accession;
    private int expId;
    private String Description;
    private boolean validated;
    private int decoy;
    private String geneName;
    private String chromosomeNumber;
    private int protGroupId;

    public String getGeneName() {
        return geneName;
    }

    public void setGeneName(String geneName) {
        this.geneName = geneName;
    }

    public String getChromosomeNumber() {
        return chromosomeNumber;
    }

    public void setChromosomeNumber(String chromosomeNumber) {
        this.chromosomeNumber = chromosomeNumber;
    }

    public boolean isValidated() {
        return validated;
    }

    public void setValidated(boolean validated) {
        this.validated = validated;
    }
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
    private int numberSpectra;
    private int numberPeptides;
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

    public int getNumberSpectra() {
        return numberSpectra;
    }

    public void setNumberSpectra(int numberSpectra) {
        this.numberSpectra = numberSpectra;
    }

    public int getNumberPeptides() {
        return numberPeptides;
    }

    public void setNumberPeptides(int numberPeptides) {
        this.numberPeptides = numberPeptides;
    }

    public int getDecoy() {
        return decoy;
    }

    public void setDecoy(int decoy) {
        this.decoy = decoy;
    }

    public int getExpId() {
        return expId;
    }

    public void setExpId(int expId) {
        this.expId = expId;
    }

    public int getProtGroupId() {
        return protGroupId;
    }

    public void setProtGroupId(int protGroupId) {
        this.protGroupId = protGroupId;
    }

    @Override
    public int compareTo(ProteinBean o) {
        if(this.nsaf > o.getNsaf())
            return 1;
        else
            return -1;
    
    }
}
