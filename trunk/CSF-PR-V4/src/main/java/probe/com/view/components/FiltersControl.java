/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.ui.HorizontalLayout;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import probe.com.dal.Query;
import probe.com.view.core.ClosableFilterLabel;

/**
 *
 * @author Yehia Farag
 */
public class FiltersControl extends HorizontalLayout {

    private final Map<String, ClosableFilterLabel> lableMap = new HashMap<String, ClosableFilterLabel>();
    private Query query ;
    

    public Query getQuery() {
        return query;
    }
    private String searchKeyWords;
    
    public void addFilterLable(ClosableFilterLabel filterBtn) {
        if (!lableMap.containsKey(filterBtn.getCaption())) {
            this.addComponent(filterBtn);
            lableMap.put(filterBtn.getCaption(), filterBtn);
        }

    }

    public void removeFilterLabel(String filterLabel) {
        if (lableMap.containsKey(filterLabel)) {
            this.removeComponent(lableMap.get(filterLabel));
            lableMap.remove(filterLabel);
        }

    }

    public Set getLabels() {
        return lableMap.keySet();
    }

    public void finalizeQuery(){
        query = new Query();
        query.setValidatedProteins(false);
        query.setSearchDataset("");
        if(searchKeyWords != null && !searchKeyWords.equalsIgnoreCase(""))
            query.setSearchKeyWords(searchKeyWords);
        for(ClosableFilterLabel filter:lableMap.values()){
        switch (filter.getFilterId()){
            case 2:
         query.setSearchDataType(filter.getCaption());
                break;
                
            case 3:
                query.setSearchDataset(filter.getCaption());
               break;
            case 4:
                query.setSearchBy(filter.getCaption());
                break;
            case 5:
                if(filter.getCaption().equalsIgnoreCase("Validated Proteins Only"))
                query.setValidatedProteins(true);
                else
                query.setValidatedProteins(false);
        
        }
        
        
        }
//        lableMap.clear();
//        searchKeyWords = null;
        
//        
//        
//        
//        
//        for(ClosableFilterLabel filter:lableMap.values()){
//            if(filter.getFilterId() == 2) {//start the query
//               
//            }
//        }
//        lableMap.remove(query.getSearchDataType());
//        if (query.getSearchDataType().equalsIgnoreCase("Identification")) {//id data
//            for (ClosableFilterLabel filter : lableMap.values()) {
//                if (filter.getFilterId() == 3) {//start the query
//                    query.setSearchDataType(filter.getCaption());
//                }
//
//            }
//        } else if (query.getSearchDataType().equalsIgnoreCase("Quantification")) {
//
//        } else {
//
//        }

    }

    public void setSearchKeyWords(String searchKeyWords) {
        this.searchKeyWords = searchKeyWords;
    }
    public boolean isValidQuery(){
        finalizeQuery();
        if(query.getSearchDataType().equalsIgnoreCase("Identification") && (searchKeyWords == null || searchKeyWords.length()<4))
        {
            System.err.println(query.getSearchDataType().equalsIgnoreCase("Identification") +"&&"+ (searchKeyWords == null || searchKeyWords.length()<4));
            return false;
        }   
//        lableMap.clear();
//        searchKeyWords = null;
        return true;
    
    }

}
