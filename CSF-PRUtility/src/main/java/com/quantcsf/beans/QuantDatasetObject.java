package com.quantcsf.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this class represent the publication study object that has all publication
 * information
 *
 * @author Yehia Farag
 */
public class QuantDatasetObject implements Serializable, Comparable {

        
    private int x;
    private int y;
    private List<QuantProtein> quantProtList = new ArrayList<QuantProtein>();
    
    
   
    private int uniqId;
    
    
    public void addQuantProt(QuantProtein qp){
        quantProtList.add(qp);
    
    }
    public List<QuantProtein> getQuantProtList(){
        return quantProtList;
    }

    public Object[] getValues() {
        return values;
    }
    private final Object[] values = new Object[27];
    private final Map<String, Object> valuesMap = new HashMap<String, Object>();
    private String uniqueValues;
    private final String[] headers = new String[]{"Author", "Year", "#Identified Proteins", "#Quantified Proteins", "Disease Groups", "Raw Data", "#Files", "Study Type", "Sample Type", "Sample Matching", "Shotgun/Targeted", "Technology", "Analytical Approach", "Enzyme", "Quantification Basis", "Quantification Basis Comment","Normalization Strategy", "PumedID","Patients Gr.I", "#Patients Gr.I", "Patients Gr.I Comments", "Patients Sub-Gr.I", "Patients Gr.II","#Patients Gr.II", "Patients Gr.II Comments", "Patients Sub-Gr.II", "Additional Comments"};

    public String getFilterTitle(int index) {
        return headers[index];
    }

    public String getAuthor() {
        return values[0].toString();
    }

    public void setAuthor(String author) {
        values[0] = author;
        valuesMap.put("author", author);
    }

    public int getYear() {
        return (Integer) values[1];
    }

    public void setYear(int year) {
        values[1] = year;
        valuesMap.put("year", year);
    }

    public int getIdentifiedProteinsNumber() {
        return (Integer) values[2];
    }

    public void setIdentifiedProteinsNumber(int identifiedProteinsNumber) {
        values[2] = identifiedProteinsNumber;
        valuesMap.put("identifiedProteinsNumber", identifiedProteinsNumber);
    }

    public int getQuantifiedProteinsNumber() {
        return (Integer) values[3];
    }

    public void setQuantifiedProteinsNumber(int quantifiedProteinsNumber) {
        values[3] = quantifiedProteinsNumber;
        valuesMap.put("quantifiedProteinsNumber", quantifiedProteinsNumber);
    }

    public String getDiseaseGroups() {
        return values[4].toString();
    }

    public void setDiseaseGroups(String diseaeGroups) {
        values[4] = diseaeGroups;
        valuesMap.put("diseaeGroups", diseaeGroups);
    }

    public String getRawDataUrl() {
        return values[5].toString();
    }

    public void setRawDataUrl(String rawDataUrl) {
        values[5] = rawDataUrl;
        valuesMap.put("rawDataUrl", rawDataUrl);
    }

    public int getFilesNumber() {
        return (Integer) values[6];
    }

    public void setFilesNumber(int filesNumber) {
        values[6] = filesNumber;
        valuesMap.put("filesNumber", filesNumber);
    }

    public String getTypeOfStudy() {
        return values[7].toString();
    }

    public void setTypeOfStudy(String typeOfStudy) {
        values[7] = typeOfStudy;
        valuesMap.put("typeOfStudy", typeOfStudy);
    }

    public String getSampleType() {
        return values[8].toString();
    }

    public void setSampleType(String sampleType) {
        values[8] = sampleType;
        valuesMap.put("sampleType", sampleType);
    }

    public String getSampleMatching() {
        return values[9].toString();
    }

    public void setSampleMatching(String sampleMatching) {
        values[9] = sampleMatching;
        valuesMap.put("sampleMatching", sampleMatching);
    }

    public String getShotgunTargeted() {
        return values[10].toString();
    }

    public void setShotgunTargeted(String shotgunTargeted) {
        values[10] = shotgunTargeted;
        valuesMap.put("shotgunTargeted", shotgunTargeted);
    }

    public String getTechnology() {
        return values[11].toString();
    }

    public void setTechnology(String technology) {
        values[11] = technology;
        valuesMap.put("technology", technology);
    }

    public String getAnalyticalApproach() {
        return values[12].toString();
    }

    public void setAnalyticalApproach(String analyticalApproach) {
        values[12] = analyticalApproach;
        valuesMap.put("analyticalApproach", analyticalApproach);
    }

    public String getEnzyme() {
        return values[13].toString();
    }

    public void setEnzyme(String enzyme) {
        values[13] = enzyme;
        valuesMap.put("enzyme", enzyme);
    }

    public String getQuantificationBasis() {
        return values[14].toString();
    }

    public void setQuantificationBasis(String quantificationBasis) {
        values[14] = quantificationBasis;
        valuesMap.put("quantificationBasis", quantificationBasis);
    }

    public String getQuantBasisComment() {
        return values[15].toString();
    }

    public void setQuantBasisComment(String quantBasisComment) {
        values[15] = quantBasisComment;
        valuesMap.put("quantBasisComment", quantBasisComment);
    }

  

    public String getNormalizationStrategy() {
        return values[16].toString();
    }

    public void setNormalizationStrategy(String normalizationStrategy) {

        values[16] = normalizationStrategy;
        valuesMap.put("normalizationStrategy", normalizationStrategy);
    }

    public String getPumedID() {
        return values[17].toString();
    }

    public void setPumedID(String pumedID) {
        values[17] = pumedID;
        valuesMap.put("pumedID", pumedID);
    }
      

  
    

    public String getPatientsGroup1() {
        return (String) values[18];
    }

    public void setPatientsGroup1(String patientsGroup1) {
        values[18] = patientsGroup1;
        valuesMap.put("patientsGroup1", patientsGroup1);
    }

    public int getPatientsGroup1Number() {
        return (Integer) values[19];
    }

    public void setPatientsGroup1Number(int patientsGroup1Number) {
        values[19] = patientsGroup1Number;
        valuesMap.put("patientsGroup1Number", patientsGroup1Number);
    }

    public String getPatientsGroup1Comm() {
        return (String) values[20];
    }

    public void setPatientsGroup1Comm(String patientsGroup1Comm) {
        values[20] = patientsGroup1Comm;
        valuesMap.put("patientsGroup1Comm", patientsGroup1Comm);
    }

    public String getPatientsSubGroup1() {
        return (String) values[21];
    }

    public void setPatientsSubGroup1(String patientsSubGroup1) {
        values[21] = patientsSubGroup1;
        valuesMap.put("patientsSubGroup1", patientsSubGroup1);
    }

    public String getPatientsGroup2() {
        return (String) values[22];
    }

    public void setPatientsGroup2(String patientsGroup2) {
        values[22] = patientsGroup2;
        valuesMap.put("patientsGroup2", patientsGroup2);
    }

    public int getPatientsGroup2Number() {
        return (Integer) values[23];
    }

    public void setPatientsGroup2Number(int patientsGroup2Number) {
        values[23] = patientsGroup2Number;
        valuesMap.put("patientsGroup2Number", patientsGroup2Number);
    }

  
    public String getPatientsGroup2Comm() {
        return (String) values[24];
    }

    public void setPatientsGroup2Comm(String patientsGroup2Comm) {
        values[24] = patientsGroup2Comm;
        valuesMap.put("patientsGroup2Comm", patientsGroup2Comm);
    }

    public String getPatientsSubGroup2() {
        return (String) values[25];
    }

    public void setPatientsSubGroup2(String patientsSubGroup2) {
        values[25] = patientsSubGroup2;
        valuesMap.put("patientsSubGroup2", patientsSubGroup2);
    }

    public void setAdditionalcomments(String additionalComments) {
        values[26] = additionalComments;
        valuesMap.put("additionalComments", additionalComments);
    }

    public String getAdditionalcomments() {
        return (String) values[26];
    }

    @Override
    public int compareTo(Object t) {
        if ((Integer) ((QuantDatasetObject) t).values[1] > (Integer) this.values[1]) {
            return 1;
        } else {
            return -1;
        }
    }

    public Object getProperty(String propertyName) {
        return valuesMap.get(propertyName);
    }

//    
    public Object getProperty(int propertyIndex) {
        if (propertyIndex < values.length) {
            return values[propertyIndex];
        }

        return null;

    }
     public void setProperty(int propertyIndex,Object value) {
        if (propertyIndex >= values.length) {
            return ;
        }
         
        values[propertyIndex] = value;

    }

    public String getUniqueValues() {
        return uniqueValues;
    }

    public void setUniqueValues(String uniqueValues) {
        this.uniqueValues = uniqueValues;
    }

    public int getUniqId() {
        return uniqId;
    }

    public void setUniqId(int uniqId) {
        this.uniqId = uniqId;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

}
