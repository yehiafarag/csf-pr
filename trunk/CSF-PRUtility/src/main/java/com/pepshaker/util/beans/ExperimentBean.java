package com.pepshaker.util.beans;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExperimentBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int expFile;//on upload file to get file type
    private int ready;//Complete experience is ready to show
    private int expId;
    private int fractionRange;
    private int peptidesInclude;
    private String name, description;
    private int fractionsNumber;
    private Map<Integer, FractionBean> fractionsList; //the key is fraction id
    private Map<String, ProteinBean> proteinList; //the we use it only in case of protein file
    private Map<Integer, PeptideBean> peptideList = new HashMap<Integer, PeptideBean>();
    private String species, sampleType, sampleProcessing, instrumentType, fragMode, UploadedByName, email, publicationLink;
    private int proteinsNumber, peptidesNumber, expType;
    private int numberValidProt;
    private List<Integer> fractionIds;
    private List<Integer> peptidesIds;

    public void setExpId(int expId) {
        this.expId = expId;
    }

    public int getExpId() {
        return expId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setFractionsNumber(int fractionsNumber) {
        this.fractionsNumber = fractionsNumber;
    }

    public int getFractionsNumber() {
        return fractionsNumber;
    }

    public void setFractionsList(Map<Integer, FractionBean> fractionsList) {
        this.fractionsList = fractionsList;
    }

    public Map<Integer, FractionBean> getFractionsList() {
        return fractionsList;
    }

    public void setExpFile(int expFile) {
        this.expFile = expFile;
    }

    public int getExpFile() {
        return expFile;
    }

    public void setReady(int ready) {
        this.ready = ready;
    }

    public int getReady() {
        return ready;
    }

    public void setProteinList(Map<String, ProteinBean> proteinList) {
        this.proteinList = proteinList;
    }

    public Map<String, ProteinBean> getProteinList() {
        return proteinList;
    }

    public Map<Integer, PeptideBean> getPeptideList() {
        return peptideList;
    }

    public void setPeptideList(Map<Integer, PeptideBean> peptideList) {
        this.peptideList = peptideList;
    }

    public int getPeptidesInclude() {
        return peptidesInclude;
    }

    public void setPeptidesInclude(int peptidesInclude) {
        this.peptidesInclude = peptidesInclude;
    }

    public String getSpecies() {
        return species;
    }

    public void setSpecies(String species) {
        this.species = species;
    }

    public String getSampleType() {
        return sampleType;
    }

    public void setSampleType(String sampleType) {
        this.sampleType = sampleType;
    }

    public String getSampleProcessing() {
        return sampleProcessing;
    }

    public void setSampleProcessing(String sampleProcessing) {
        this.sampleProcessing = sampleProcessing;
    }

    public String getInstrumentType() {
        return instrumentType;
    }

    public void setInstrumentType(String instrumentType) {
        this.instrumentType = instrumentType;
    }

    public String getFragMode() {
        return fragMode;
    }

    public void setFragMode(String fragMode) {
        this.fragMode = fragMode;
    }

    public String getUploadedByName() {
        return UploadedByName;
    }

    public void setUploadedByName(String uploadedByName) {
        UploadedByName = uploadedByName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPublicationLink() {
        return publicationLink;
    }

    public void setPublicationLink(String publicationLink) {
        this.publicationLink = publicationLink;
    }

    public int getProteinsNumber() {
        return proteinsNumber;
    }

    public void setProteinsNumber(int proteinsNumber) {
        this.proteinsNumber = proteinsNumber;
    }

    public int getPeptidesNumber() {
        return peptidesNumber;
    }

    public void setPeptidesNumber(int peptidesNumber) {
        this.peptidesNumber = peptidesNumber;
    }

    public int getFractionRange() {
        return fractionRange;
    }

    public void setFractionRange(int fractionRange) {
        this.fractionRange = fractionRange;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getFractionIds() {
        return fractionIds;
    }

    public void setFractionIds(List<Integer> fractionIds) {
        this.fractionIds = fractionIds;
    }

    public List<Integer> getPeptidesIds() {
        return peptidesIds;
    }

    public void setPeptidesIds(List<Integer> peptidesIds) {
        this.peptidesIds = peptidesIds;
    }

    public int getExpType() {
        return expType;
    }

    public void setExpType(int expType) {
        this.expType = expType;
    }

    public int getNumberValidProt() {
        return numberValidProt;
    }

    public void setNumberValidProt(int numberValidProt) {
        this.numberValidProt = numberValidProt;
    }
}
