/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import probe.com.view.components.FiltersControl;

/**
 *
 * @author y-mok_000
 */
public class TextAreaFilter extends TextArea implements Property.ValueChangeListener, Serializable {

    private final ClosableFilterLabel filterBtn;
    private final FiltersControl control;
    private final int filterId;    ;
    private final String filterTitle;

    public ClosableFilterLabel getFilterBtn() {
        return filterBtn;
    }

    public TextAreaFilter(FiltersControl controller, int filterId, String filterTitle, String defaultText) {
        this.control = controller;
        this.filterId = filterId;
        this.filterTitle = filterTitle;
        this.setWidth("350px");
        this.setImmediate(true);
        this.setStyleName(Reindeer.TEXTFIELD_SMALL);
        this.setInputPrompt(defaultText);
        this.addValueChangeListener(this);

//        filterBtn.getCloseBtn().addClickListener(this);
//        okBtn.addClickListener(new Button.ClickListener() {
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                
//        if(textField.getValue()!= null && !textField.getValue().trim().equalsIgnoreCase(""))
////        {
//        
//            filterBtn.setCaption(textField.getValue().trim());
//            control.addFilter(filterBtn);
////        
//        }}
//        }); 
        filterBtn = new ClosableFilterLabel(filterTitle, "", filterId, true);

    }
    private final Set<String> keywordsSet = new HashSet<String>();

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        keywordsSet.clear();
        for (String str : this.getValue().split("\n")) {
            keywordsSet.add(str.toUpperCase());
        }
        control.removeFilter(filterBtn.getCaption());
        if (this.getValue() != null && !this.getValue().trim().equalsIgnoreCase("")) {
            String key = keywordsSet.toString().trim();
            key = key.substring(1, key.length()-1);
            filterBtn.setValue(key);
            control.addFilter(filterBtn);
//        
        }
    }

}
