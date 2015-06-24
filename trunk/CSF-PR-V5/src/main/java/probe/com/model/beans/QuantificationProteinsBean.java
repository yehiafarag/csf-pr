/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class QuantificationProteinsBean implements Serializable{
  //  Normalization strategy				
    private String uniprotAccession, uniprotProteinName, publicationAccNumber,publicationProteinName,	peptideModification,modificationComment ,pumedID,typeOfStudy,sampleType,patientGroupI,patientSubGroupI,patientGrIComment,patientGroupII,patientSubGroupII,patientGrIIComment,sampleMatching,normalizationStrategy,technology,analyticalApproachI,enzyme,shotgunOrTargetedQquant,analyticalMethod,quantificationBasis,quantBasisComment,additionalComments;		
    private String rawDataAvailable;
    private int quantifiedProteinsNumber,patientsGroupINumber,patientsGroupIINumber;
    private double fcPatientGroupIonPatientGroupII,pValue,rocAuc;

    public String getUniprotProteinName() {
        return uniprotProteinName;
    }

    public void setUniprotProteinName(String uniprotProteinName) {
        this.uniprotProteinName = uniprotProteinName;
    }

    
    public String getPublicationProteinName() {
        return publicationProteinName;
    }

    public void setPublicationProteinName(String publicationProteinName) {
        this.publicationProteinName = publicationProteinName;
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

    public String getPumedID() {
        return pumedID;
    }

    public void setPumedID(String pumedID) {
        this.pumedID = pumedID;
    }

    public String getTypeOfStudy() {
        return typeOfStudy;
    }

    public void setTypeOfStudy(String typeOfStudy) {
        this.typeOfStudy = typeOfStudy;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getPatientGroupI() {
        return patientGroupI;
    }

    public void setPatientGroupI(String patientGroupI) {
        this.patientGroupI = patientGroupI;
    }

    public String getPatientSubGroupI() {
        return patientSubGroupI;
    }

    public void setPatientSubGroupI(String patientSubGroupI) {
        this.patientSubGroupI = patientSubGroupI;
    }

    public String getPatientGrIComment() {
        return patientGrIComment;
    }

    public void setPatientGrIComment(String patientGrIComment) {
        this.patientGrIComment = patientGrIComment;
    }

    public String getPatientGroupII() {
        return patientGroupII;
    }

    public void setPatientGroupII(String patientGroupII) {
        this.patientGroupII = patientGroupII;
    }

    public String getPatientSubGroupII() {
        return patientSubGroupII;
    }

    public void setPatientSubGroupII(String patientSubGroupII) {
        this.patientSubGroupII = patientSubGroupII;
    }

    public String getPatientGrIIComment() {
        return patientGrIIComment;
    }

    public void setPatientGrIIComment(String patientGrIIComment) {
        this.patientGrIIComment = patientGrIIComment;
    }

    public String getSampleMatching() {
        return sampleMatching;
    }

    public void setSampleMatching(String sampleMatching) {
        this.sampleMatching = sampleMatching;
    }

    public String getNormalizationStrategy() {
        return normalizationStrategy;
    }

    public void setNormalizationStrategy(String normalizationStrategy) {
        this.normalizationStrategy = normalizationStrategy;
    }

    public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }

    public String getAnalyticalApproachI() {
        return analyticalApproachI;
    }

    public void setAnalyticalApproachI(String analyticalApproachI) {
        this.analyticalApproachI = analyticalApproachI;
    }

    public String getEnzyme() {
        return enzyme;
    }

    public void setEnzyme(String enzyme) {
        this.enzyme = enzyme;
    }

    public String getShotgunOrTargetedQquant() {
        return shotgunOrTargetedQquant;
    }

    public void setShotgunOrTargetedQquant(String shotgunOrTargetedQquant) {
        this.shotgunOrTargetedQquant = shotgunOrTargetedQquant;
    }

    public String getAnalyticalMethod() {
        return analyticalMethod;
    }

    public void setAnalyticalMethod(String analyticalMethod) {
        this.analyticalMethod = analyticalMethod;
    }

    public String getQuantificationBasis() {
        return quantificationBasis;
    }

    public void setQuantificationBasis(String quantificationBasis) {
        this.quantificationBasis = quantificationBasis;
    }

    public String getQuantBasisComment() {
        return quantBasisComment;
    }

    public void setQuantBasisComment(String quantBasisComment) {
        this.quantBasisComment = quantBasisComment;
    }

    public String getAdditionalComments() {
        return additionalComments;
    }

    public void setAdditionalComments(String additionalComments) {
        this.additionalComments = additionalComments;
    }

    

    public int getQuantifiedProteinsNumber() {
        return quantifiedProteinsNumber;
    }

    public void setQuantifiedProteinsNumber(int quantifiedProteinsNumber) {
        this.quantifiedProteinsNumber = quantifiedProteinsNumber;
    }

    public int getPatientsGroupINumber() {
        return patientsGroupINumber;
    }

    public void setPatientsGroupINumber(int patientsGroupINumber) {
        this.patientsGroupINumber = patientsGroupINumber;
    }

    public int getPatientsGroupIINumber() {
        return patientsGroupIINumber;
    }

    public void setPatientsGroupIINumber(int patientsGroupIINumber) {
        this.patientsGroupIINumber = patientsGroupIINumber;
    }

    public double getFcPatientGroupIonPatientGroupII() {
        return fcPatientGroupIonPatientGroupII;
    }

    public void setFcPatientGroupIonPatientGroupII(double fcPatientGroupIonPatientGroupII) {
        this.fcPatientGroupIonPatientGroupII = fcPatientGroupIonPatientGroupII;
    }

    public double getpValue() {
        return pValue;
    }

    public void setpValue(double pValue) {
        this.pValue = pValue;
    }

    public double getRocAuc() {
        return rocAuc;
    }

    public void setRocAuc(double rocAuc) {
        this.rocAuc = rocAuc;
    }

    public String getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(String uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }

    public String getPublicationAccNumber() {
        return publicationAccNumber;
    }

    public void setPublicationAccNumber(String publicationAccNumber) {
        this.publicationAccNumber = publicationAccNumber;
    }

    public String getRawDataAvailable() {
        return rawDataAvailable;
    }

    public void setRawDataAvailable(String rawDataAvailable) {
        this.rawDataAvailable = rawDataAvailable;
    }
}
