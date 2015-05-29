/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import probe.com.view.components.SearchingFiltersControl;

/**
 *
 * @author y-mok_000
 */
public class IntegerTextFieldFilter  extends HorizontalLayout implements Serializable,Button.ClickListener,Property.ValueChangeListener{
      private final TextField textField;
    private final ClosableFilterLabel filterBtn;
    private final SearchingFiltersControl control;
    private final int filterId;
    private final Button okBtn;
    private final FilterConfirmLabel filterConfirmLabel ;
    
    public void setAddBtnCaption(String title){
    okBtn.setCaption(title);
    
    }
    public IntegerTextFieldFilter(SearchingFiltersControl controller ,int filterId,final String filterTitle,String label){
    this.control = controller;
        this.filterId=filterId;
        this.setSpacing(true);
        okBtn = new Button("ok");
        okBtn.setStyleName(Reindeer.BUTTON_SMALL);
        textField = new TextField();
        textField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        textField.setHeight("20px");
        textField.setWidth("100px"); 
        textField.setNullRepresentation(" ");
        textField.setConverter(new StringToIntegerConverter());
        textField.setDescription(label);
        
       
        this.addComponent(textField);
        this.addComponent(okBtn);
         filterConfirmLabel = new FilterConfirmLabel();       
        this.addComponent(filterConfirmLabel);
        
        filterBtn = new ClosableFilterLabel(label,label,filterId, true);
        filterBtn.getCloseBtn().addClickListener(IntegerTextFieldFilter.this);
        okBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                filterConfirmLabel.setVisible(false);
                control.removeFilter(filterBtn.getCaption());
//                textField.validate();
        if (textField.isValid() && textField.getValue()!= null) {
            textField.setComponentError(null);
             filterBtn.setValue(textField.getValue().trim());
            control.addFilter(filterBtn);
            filterConfirmLabel.setVisible(true);
        }
      
            }
        });
        textField.addValueChangeListener(IntegerTextFieldFilter.this);
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        textField.validate();        
        if (textField.isValid()) {
            textField.setComponentError(null);
           
        }
        if(textField.getValue()== null || textField.getValue().trim().equalsIgnoreCase(""))
            okBtn.click();
    }


    @Override
    public void buttonClick(Button.ClickEvent event) {
     textField.setValue(" ");    
    }
    
}
