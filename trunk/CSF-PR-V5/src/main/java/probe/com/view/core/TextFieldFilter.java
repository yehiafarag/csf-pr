/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.themes.Reindeer;
import probe.com.view.components.SearchingFiltersControl;

/**
 *
 * @author Yehia Farag
 */
public class TextFieldFilter extends HorizontalLayout implements Button.ClickListener,FieldEvents.FocusListener{

    private final TextArea textField;
    private final ClosableFilterLabel filterBtn;
    private final SearchingFiltersControl control;
    private final int filterId;
    private final Button okBtn;
    private final Label filterConfirmLabel;
//    private final String filterTitle;

    public TextFieldFilter(SearchingFiltersControl controller ,int filterId,String filterTitle) {
        this.control = controller;
        this.filterId=filterId;
        this.setSpacing(true);
 
        
        okBtn = new Button("ok");
        okBtn.setStyleName(Reindeer.BUTTON_SMALL);
        textField = new TextArea();
        textField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        textField.setHeight("60px");
        textField.setWidth("200px");
        textField.setInputPrompt("Please use one key-word per line");
        Label captionLabel = new Label(filterTitle);
        captionLabel.setWidth("70px");
        captionLabel.setStyleName("custLabel");
        if (filterTitle != null) {
            textField.setDescription(filterTitle);
        }
        this.addComponent(captionLabel);
        this.addComponent(textField);
        this.addComponent(okBtn);
        
        filterConfirmLabel = new FilterConfirmLabel();       
        this.addComponent(filterConfirmLabel);
        
        filterBtn = new ClosableFilterLabel(filterTitle,"",filterId, true);
        filterBtn.getCloseBtn().addClickListener(TextFieldFilter.this);
        okBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                control.removeFilter(filterBtn.getCaption());
                filterConfirmLabel.setVisible(false);
        if(textField.getValue()!= null && !textField.getValue().trim().equalsIgnoreCase(""))
        {
            filterBtn.setValue(textField.getValue().trim().toUpperCase());
            control.addFilter(filterBtn);
            filterConfirmLabel.setVisible(true);
        
        }}
        });
        textField.addFocusListener(TextFieldFilter.this);
        this.setEnabled(true);
        this.focus();
        

    }

    @Override
    public void focus(FieldEvents.FocusEvent event){
    okBtn.setClickShortcut(ShortcutAction.KeyCode.ENTER);    
    }

   

    @Override
    public void buttonClick(Button.ClickEvent event) {
        
        textField.setValue("");
        okBtn.click();
        
    }
    
}

