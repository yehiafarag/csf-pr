/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.shared.ui.label.ContentMode;
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
public class TextFieldFilter extends HorizontalLayout implements Button.ClickListener,FieldEvents.FocusListener{

    private final TextField textField;
    private final ClosableFilterLabel filterBtn;
    private final FiltersControl control;
    private final int filterId;
    private final Button okBtn;
    private final Label filterConfirmLabel;
//    private final String filterTitle;

    @SuppressWarnings("LeakingThisInConstructor")
    public TextFieldFilter(FiltersControl controller ,int filterId,String filterTitle) {
        this.control = controller;
        this.filterId=filterId;
        this.setSpacing(true);
        
        okBtn = new Button("ok");
        okBtn.setStyleName(Reindeer.BUTTON_SMALL);
        textField = new TextField();
        textField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        textField.setHeight("20px");
        textField.setWidth("120px");
        Label captionLabel = new Label(filterTitle);
        captionLabel.setWidth("70px");
        captionLabel.setStyleName("custLabel");
        if (filterTitle != null) {
            textField.setDescription(filterTitle);
        }
        this.addComponent(captionLabel);
        this.addComponent(textField);
        this.addComponent(okBtn);
        
        filterConfirmLabel = new Label("");
        filterConfirmLabel.setStyleName("custLabel");
        filterConfirmLabel.setContentMode(ContentMode.HTML);
        this.addComponent(filterConfirmLabel);
        
        filterBtn = new ClosableFilterLabel(filterTitle,"",filterId, true);
        filterBtn.getCloseBtn().addClickListener(this);
        okBtn.addClickListener(new Button.ClickListener() {
            @Override
            public void buttonClick(Button.ClickEvent event) {
                control.removeFilterLabel(filterBtn.getCaption());
                filterConfirmLabel.setValue("");
        if(textField.getValue()!= null && !textField.getValue().trim().equalsIgnoreCase(""))
        {
            filterBtn.setValue(textField.getValue().trim().toUpperCase());
            control.addFilterLable(filterBtn);
            filterConfirmLabel.setValue(textField.getValue().trim().toUpperCase());
            filterConfirmLabel.setWidth((textField.getValue().length()*7)+"px");
        
        }}
        });
        textField.addFocusListener(this);
        

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

