/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

/**
 *
 * @author Yehia Farag
 */
public class QuantProtein {
    private String pumedID,uniprotAccession, uniprotProteinName, publicationAccNumber,publicationProteinName,rawDataAvailable,typeOfStudy,sampleType,patientGroupI,patientSubGroupI,patientGrIComment,patientGroupII,patientSubGroupII,patientGrIIComment,sampleMatching,normalizationStrategy,technology,analyticalApproach,enzyme,shotgunOrTargetedQquant,quantificationBasis,quantBasisComment,additionalComments;		
    private int quantifiedProteinsNumber,peptideIdNumb,quantifiedPeptidesNumber, patientsGroupINumber,patientsGroupIINumber,year,filesNum,IdentifiedProteinsNum, dsKey,protKey;    
    private String qPeptideKey,    peptideSequance,	peptideModification,modificationComment , stringFCValue,stringPValue,author,diseaseGroups,pvalueComment; 

    public int getDsKey() {
        return dsKey;
    }

    public String getPvalueComment() {
        return pvalueComment;
    }

    public void setPvalueComment(String pvalueComment) {
        this.pvalueComment = pvalueComment;
    }

    public int getProtKey() {
        return protKey;
    }

    public void setProtKey(int protKey) {
        this.protKey = protKey;
    }

    public void setDsKey(int dsKey) {
        this.dsKey = dsKey;
    }
    

    public String getStringPValue() {
        return stringPValue;
    }

    public void setStringPValue(String stringPValue) {
        this.stringPValue = stringPValue;
    }
    private double pValue,rocAuc;
    private double fcPatientGroupIonPatientGroupII;
    private boolean peptideProt;

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getFilesNum() {
        return filesNum;
    }

    public void setFilesNum(int filesNum) {
        this.filesNum = filesNum;
    }

    public int getIdentifiedProteinsNum() {
        return IdentifiedProteinsNum;
    }

    public void setIdentifiedProteinsNum(int IdentifiedProteinsNum) {
        this.IdentifiedProteinsNum = IdentifiedProteinsNum;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDiseaseGroups() {
        return diseaseGroups;
    }

    public void setDiseaseGroups(String diseaseGroups) {
        this.diseaseGroups = diseaseGroups;
    }

    public String getStringFCValue() {
        return stringFCValue;
    }

    public void setStringFCValue(String StringFC) {
        this.stringFCValue = StringFC;
    }

    public boolean isPeptideProt() {
        return peptideProt;
    }

    public void setPeptideProt(boolean peptideProt) {
        this.peptideProt = peptideProt;
    }


   

    public String getPumedID() {
        return pumedID;
    }
    public void setPumedID(String pumedID) {
        this.pumedID = pumedID;
    }
        
    public int getQuantifiedProteinsNumber() {
        return quantifiedProteinsNumber;
    }

    public void setQuantifiedProteinsNumber(int quantifiedProteinsNumber) {
        this.quantifiedProteinsNumber = quantifiedProteinsNumber;
    }
    
       public String getUniprotAccession() {
        return uniprotAccession;
    }

    public void setUniprotAccession(String uniprotAccession) {
        this.uniprotAccession = uniprotAccession;
    }
     public String getUniprotProteinName() {
        return uniprotProteinName;
    }

    public void setUniprotProteinName(String uniprotProteinName) {
        this.uniprotProteinName = uniprotProteinName;
    }
    
     public String getPublicationAccNumber() {
        return publicationAccNumber;
    }

    public void setPublicationAccNumber(String publicationAccNumber) {
        this.publicationAccNumber = publicationAccNumber;
    }
     public String getPublicationProteinName() {
        return publicationProteinName;
    }

    public void setPublicationProteinName(String publicationProteinName) {
        this.publicationProteinName = publicationProteinName;
    }
    

    public String getRawDataAvailable() {
        return rawDataAvailable;
    }

    public void setRawDataAvailable(String rawDataAvailable) {
        this.rawDataAvailable = rawDataAvailable;
    }
    
    
    public int getPeptideIdNumb() {
        return peptideIdNumb;
    }

    public void setPeptideIdNumb(int peptideIdNumb) {
        this.peptideIdNumb = peptideIdNumb;
    }
    
      public int getQuantifiedPeptidesNumber() {
        return quantifiedPeptidesNumber;
    }

    public void setQuantifiedPeptidesNumber(int quantifiedPeptidesNumber) {
        this.quantifiedPeptidesNumber = quantifiedPeptidesNumber;
    }
     
    //move to peptideProt?? 
    
    // dublicate in both??
     public double getFcPatientGroupIonPatientGroupII() {
        return fcPatientGroupIonPatientGroupII;
    }

    public void setFcPatientGroupIonPatientGroupII(double fcPatientGroupIonPatientGroupII) {
        this.fcPatientGroupIonPatientGroupII = fcPatientGroupIonPatientGroupII;
    }
    
     //can dublicate in both??
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

    public int getPatientsGroupINumber() {
        return patientsGroupINumber;
    }

    public void setPatientsGroupINumber(int patientsGroupINumber) {
        this.patientsGroupINumber = patientsGroupINumber;
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
     public int getPatientsGroupIINumber() {
        return patientsGroupIINumber;
    }

    public void setPatientsGroupIINumber(int patientsGroupIINumber) {
        this.patientsGroupIINumber = patientsGroupIINumber;
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
public String getTechnology() {
        return technology;
    }

    public void setTechnology(String technology) {
        this.technology = technology;
    }
 public String getAnalyticalApproach() {
        return analyticalApproach;
    }

    public void setAnalyticalApproach(String analyticalApproach) {
        this.analyticalApproach = analyticalApproach;
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
    
 
     
   

    

    

   

   



    
    

    
    

    

  
    


   
   

   
    
}
