/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.dal;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class Query implements Serializable{
    private String searchDataType,searchDataset,searchBy,searchKeyWords;
    private boolean validatedProteins;

    public String getSearchDataset() {
        return searchDataset;
    }

    public void setSearchDataset(String searchDataset) {
        this.searchDataset = searchDataset;
    }

    public String getSearchBy() {
        return searchBy;
    }

    public void setSearchBy(String searchBy) {
        this.searchBy = searchBy;
    }

    

    public String getSearchDataType() {
        return searchDataType;
    }

    public void setSearchDataType(String searchDataType) {
        this.searchDataType = searchDataType;
    }

    public String getSearchKeyWords() {
        return searchKeyWords;
    }

    public void setSearchKeyWords(String searchKeyWords) {
        this.searchKeyWords = searchKeyWords;
    }

    public boolean isValidatedProteins() {
        return validatedProteins;
    }

    public void setValidatedProteins(boolean validatedProteins) {
        this.validatedProteins = validatedProteins;
    }
    
}
