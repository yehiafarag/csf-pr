/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import probe.com.view.core.ClosableFilterLabel;
import probe.com.view.core.DoubleTextField;

/**
 *
 * @author y-mok_000
 */
public class DoubleBetweenValuesFilter extends HorizontalLayout implements Serializable,Button.ClickListener{
    private final FiltersControl control;
    
    private final ClosableFilterLabel filterBtn;
    private final String defaultLabel;
    private final int filterId; 
    private final Button okBtn;
    private  DoubleTextField minValueField ,maxValueField;
    public DoubleBetweenValuesFilter(FiltersControl control,int filterId,String defaultLabel){
       this.control = control;
       this.filterId=filterId;
        this.defaultLabel = defaultLabel;
        this.setSpacing(true);
        Label fcPatientsGrI_patGrII = new Label(defaultLabel);
        fcPatientsGrI_patGrII.setStyleName("custLabel");
        this.addComponent(fcPatientsGrI_patGrII);

       minValueField  = new DoubleTextField();
       this.addComponent(minValueField);
       Label betweenLabel = new Label("< Than & < Than");
//        betweenLabel.setWidth("100px");
        betweenLabel.setStyleName(Reindeer.LABEL_SMALL);
       this.addComponent(betweenLabel);
       this.setComponentAlignment(betweenLabel, Alignment.MIDDLE_CENTER);
       maxValueField  = new DoubleTextField();
       this.addComponent(maxValueField);
        okBtn = new Button("ok");
        this.addComponent(okBtn);
        okBtn.setStyleName(Reindeer.BUTTON_SMALL);
        okBtn.addClickListener(this);
         filterBtn = new ClosableFilterLabel("Tiltle:",defaultLabel,filterId, true);
        filterBtn.getCloseBtn().addClickListener(new Button.ClickListener() {

           @Override
            public void buttonClick(Button.ClickEvent event) {
                maxValueField.setValue(" ");
                minValueField.setValue(" ");
            }
       });
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        control.removeFilterLabel(filterBtn.getCaption());
        if (maxValueField.isValid() && minValueField.isValid()) {
            if(minValueField.getDoubleValue()>maxValueField.getDoubleValue()){
            okBtn.setComponentError(new ErrorMessage() {
        @Override
        public ErrorMessage.ErrorLevel getErrorLevel() {
            return ErrorMessage.ErrorLevel.ERROR;
        }

        @Override
        public String getFormattedHtmlMessage() {
            return "Min Value > Max Value";
        }
    });
            }
            else{
                okBtn.setComponentError(null);
                filterBtn.setCaption("Between "+minValueField.getDoubleValue()+" AND "+maxValueField.getDoubleValue());
            control.addFilterLable(filterBtn);
        }
//            minValueField.setComponentError(null);
//            maxValueField.setComponentError(null);
             
        }   
    }
    
}