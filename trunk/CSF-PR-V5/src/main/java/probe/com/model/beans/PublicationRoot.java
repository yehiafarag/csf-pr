/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

import java.util.Set;

/**
 *
 * @author Yehia Farag
 */
public class PublicationRoot {
    private String commonAttr;
    private int labelsNumber;

    public int getLabelsNumber() {
        return labelsNumber;
    }

    public void setLabelsNumber(int labelsNumber) {
        this.labelsNumber = labelsNumber;
    }
   
    
    private boolean[] attrCheck ;

    public boolean[] getAttrCheck() {
        return attrCheck;
    }

    public void setAttrCheck(boolean[] attrCheck) {
        this.attrCheck = attrCheck;
    }
    private Set<QuantDatasetObject> includedStudies;
    private String publicationId;

    public String getCommonAttr() {
        return commonAttr;
    }

    public void setCommonAttr(String commonAttr) {
        this.commonAttr = commonAttr;
    }

   

    public Set<QuantDatasetObject> getIncludedDatasets() {
        return includedStudies;
    }

    public void setIncludedStudies(Set<QuantDatasetObject> includedStudies) {
        this.includedStudies = includedStudies;
    }

    public String getPublicationId() {
        return publicationId;
    }

    public void setPublicationId(String publicationId) {
        this.publicationId = publicationId;
    }

    
    
    

    public Object[] getValues() {
        return values;
    }
    private final Object[] values = new Object[27];
//    private final Map<String,Object> valuesMap= new HashMap<String, Object>();
    private String uniqueValues;
    
     public String getAuthor() {
        return values[0].toString();
    }

    public void setAuthor(String author) {
        values[0] = author;
    }

    public int getYear() {
        return (Integer) values[1];
    }

    public void setYear(int year) {
        values[1] = year;
    }

    public int getIdentifiedProteinsNumber() {
        return (Integer) values[2];
    }

    public void setIdentifiedProteinsNumber(int identifiedProteinsNumber) {
        values[2] = identifiedProteinsNumber;
    }

    public int getQuantifiedProteinsNumber() {
        return (Integer) values[3];
    }

    public void setQuantifiedProteinsNumber(int quantifiedProteinsNumber) {
        values[3] = quantifiedProteinsNumber;
    }

    public String getDiseaseGroups() {
        return values[4].toString();
    }

    public void setDiseaseGroups(String diseaeGroups) {
        values[4] = diseaeGroups;
    }

    public String getRawDataUrl() {
        return values[5].toString();
    }

    public void setRawDataUrl(String rawDataUrl) {
        values[5] = rawDataUrl;
    }

    public int getFilesNumber() {
        return (Integer) values[6];
    }

    public void setFilesNumber(int filesNumber) {
        values[6] = filesNumber;
    }

    public String getTypeOfStudy() {
        return values[7].toString();
    }

    public void setTypeOfStudy(String typeOfStudy) {
        values[7] = typeOfStudy;
    }

    public String getSampleType() {
        return values[8].toString();
    }

    public void setSampleType(String sampleType) {
        values[8] = sampleType;
    }

    public String getSampleMatching() {
        return values[9].toString();
    }

    public void setSampleMatching(String sampleMatching) {
        values[9] = sampleMatching;
    }

    public String getShotgunTargeted() {
        return values[10].toString();
    }

    public void setShotgunTargeted(String shotgunTargeted) {
        values[10] = shotgunTargeted;
    }

    public String getTechnology() {
        return values[11].toString();
    }

    public void setTechnology(String technology) {
        values[11] = technology;
    }

    public String getAnalyticalApproach() {
        return values[12].toString();
    }

    public void setAnalyticalApproach(String analyticalApproach) {
        values[12] = analyticalApproach;
    }

    public String getEnzyme() {
        return values[13].toString();
    }

    public void setEnzyme(String enzyme) {
        values[13] = enzyme;
    }

    public String getQuantificationBasis() {
        return values[14].toString();
    }

    public void setQuantificationBasis(String quantificationBasis) {
        values[14] = quantificationBasis;
    }

    public String getQuantBasisComment() {
        return values[15].toString();
    }

    public void setQuantBasisComment(String quantBasisComment) {
        values[15] = quantBasisComment;
    }

  

    public String getNormalizationStrategy() {
        return values[16].toString();
    }

    public void setNormalizationStrategy(String normalizationStrategy) {

        values[16] = normalizationStrategy;
    }

    public String getPumedID() {
        return values[17].toString();
    }

    public void setPumedID(String pumedID) {
        values[17] = pumedID;
    }
      

  
    

    public String getPatientsGroup1() {
        return (String) values[18];
    }

    public void setPatientsGroup1(String patientsGroup1) {
        values[18] = patientsGroup1;
    }

    public int getPatientsGroup1Number() {
        return (Integer) values[19];
    }

    public void setPatientsGroup1Number(int patientsGroup1Number) {
        values[19] = patientsGroup1Number;
    }

    public String getPatientsGroup1Comm() {
        return (String) values[20];
    }

    public void setPatientsGroup1Comm(String patientsGroup1Comm) {
        values[20] = patientsGroup1Comm;
    }

    public String getPatientsSubGroup1() {
        return (String) values[21];
    }

    public void setPatientsSubGroup1(String patientsSubGroup1) {
        values[21] = patientsSubGroup1;
    }

    public String getPatientsGroup2() {
        return (String) values[22];
    }

    public void setPatientsGroup2(String patientsGroup2) {
        values[22] = patientsGroup2;
    }

    public int getPatientsGroup2Number() {
        return (Integer) values[23];
    }

    public void setPatientsGroup2Number(int patientsGroup2Number) {
        values[23] = patientsGroup2Number;
    }

  
    public String getPatientsGroup2Comm() {
        return (String) values[24];
    }

    public void setPatientsGroup2Comm(String patientsGroup2Comm) {
        values[24] = patientsGroup2Comm;
    }

    public String getPatientsSubGroup2() {
        return (String) values[25];
    }

    public void setPatientsSubGroup2(String patientsSubGroup2) {
        values[25] = patientsSubGroup2;
    }

    public void setAdditionalcomments(String additionalComments) {
        values[26] = additionalComments;
    }

    public String getAdditionalcomments() {
        return (String) values[26];
    }


    
    

    
    
    
    
    
    

    
    
    
    
  
   

 
  

   

   

   

   

    

   

   

   


    

   

    
    
//    
    public Object getProperty(int propertyIndex) {
        if (propertyIndex < values.length) {
            return values[propertyIndex];
        }

        return null;

    }

    public String getUniqueValues() {
        return uniqueValues;
    }

    public void setUniqueValues(String uniqueValues) {
        this.uniqueValues = uniqueValues;
    }

    public void setProperty(int index, Object value) {
        if (index < values.length) {
            values[index] = value;
        }

    }

}
