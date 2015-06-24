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
public class DatasetDetailsBean implements Serializable{
   
private String name,species, sampleType, sampleProcessing, instrumentType, fragMode;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
    
}
