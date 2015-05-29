/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Button;
import com.vaadin.ui.OptionGroup;
import java.io.Serializable;
import probe.com.view.core.OptionGroupFilter;
import probe.com.view.core.TextAreaFilter;

/**
 *
 * @author y-mok_000
 */
public class KeywordFilter implements Serializable{
    private final TextAreaFilter searchFieldFilter;
    private final OptionGroupFilter searchbyGroup;
    private final SearchingFiltersControl control;
    public KeywordFilter(SearchingFiltersControl filtersController,String defaultText){
        this.control = filtersController;
        searchFieldFilter = new TextAreaFilter(control, 1, "Search Keywords", defaultText);
        
        searchFieldFilter.addFocusListener(new FieldEvents.FocusListener() {

            @Override
            public void focus(FieldEvents.FocusEvent event) {
                getSearchbyGroup().setEnabled(true);
//                getSearchbyGroup().setNullSelectionAllowed(false);
                if (getSearchbyGroup().getValue() == null && searchbyGroup.isEnabled()) {
                    getSearchbyGroup().select("Protein Accession");
                }
            }
        });
        searchFieldFilter.addBlurListener(new FieldEvents.BlurListener() {

            @Override
            public void blur(FieldEvents.BlurEvent event) {
                if (getSearchField().getValue().trim().equals("")) {
                    getSearchbyGroup().setEnabled(false);
//                    getSearchbyGroup().setNullSelectionAllowed(false);
                    getSearchbyGroup().select(null);
                } else {
                    getSearchbyGroup().setEnabled(true);
                    if (getSearchbyGroup().getValue() == null) {
                        getSearchbyGroup().select("Protein Accession");
                    }
                }
            }
        });

        searchFieldFilter.getFilterBtn().getCloseBtn().addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
            searchFieldFilter.setValue("");
            getSearchbyGroup().select(null);
                    getSearchbyGroup().setEnabled(false);
                      
            
            }
        
        
        });
        
       
        //init search by        
        
        searchbyGroup = new OptionGroupFilter(filtersController,"Search By:", 4, true);
        searchbyGroup.setWidth("330px");
        searchbyGroup.setDescription("Please Select Search Method");
        searchbyGroup.getOptionGroup().setEnabled(false);
        searchbyGroup.getOptionGroup().setNullSelectionAllowed(true);
        
        
        // Use the single selection mode.

        searchbyGroup.getOptionGroup().addItem("Protein Accession");
        searchbyGroup.getOptionGroup().addItem("Protein Name");
        searchbyGroup.getOptionGroup().addItem("Peptide Sequence");
        
       searchbyGroup.getFilterBtn().getCloseBtn().addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                 searchFieldFilter.setValue("");
                    getSearchbyGroup().select(null);
                    getSearchbyGroup().setEnabled(false);
            
            }
        });
       
        
        
        
    
    }

    public TextAreaFilter getSearchField() {
        return searchFieldFilter;
    }

    public OptionGroup getSearchbyGroup() {
        return searchbyGroup.getOptionGroup();
    }
    
}
