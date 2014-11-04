/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;
import probe.com.view.components.FiltersControl;

/**
 *
 * @author Yehia Farag
 */
public class TextFieldFilter extends HorizontalLayout implements Button.ClickListener,Property.ValueChangeListener{

    private final TextField textField;
    private final ClosableBtn filterBtn;
    private final FiltersControl control;
    private final int filterId;

    public TextFieldFilter(FiltersControl control ,int filterId,String label) {
        this.control = control;
        this.filterId=filterId;
        textField = new TextField();
        textField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        textField.setHeight("20px");
        textField.setWidth("120px");
        Label captionLabel = new Label(label);
        captionLabel.setWidth("70px");
        captionLabel.setStyleName("custLabel");
        if (label != null) {
            textField.setDescription(label);
        }
        this.addComponent(captionLabel);
        this.addComponent(textField);
        filterBtn = new ClosableBtn(label,filterId, true);
        textField.addValueChangeListener(this);
        filterBtn.addClickListener(this);
        

    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        control.removeFilterLabel(filterBtn.getCaption());
        if(textField.getValue()!= null && !textField.getValue().trim().equalsIgnoreCase(""))
        {
            filterBtn.setCaption(textField.getValue().trim());
            control.addFilterLable(filterBtn);
        
        }
    }
    

    @Override
    public void buttonClick(Button.ClickEvent event) {
        textField.setValue("");
        
    }
    
}

