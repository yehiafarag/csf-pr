/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.server.ErrorMessage;
import com.vaadin.ui.Button;
import com.vaadin.ui.TextField;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;

/**
 *
 * @author y-mok_000
 */
public class DoubleTextField extends TextField implements Serializable,Property.ValueChangeListener{
 
  private double value;

    @Override
    public boolean isValid() {
        return valid; //To change body of generated methods, choose Tools | Templates.
    }
  private boolean valid = false;
    public DoubleTextField(){
        
        this.setStyleName(Reindeer.TEXTFIELD_SMALL);
        this.setHeight("20px");
        this.setWidth("40px"); 
        this.setNullRepresentation(" ");
        this.setDescription("Only Double Value Allowed");
        this.addValueChangeListener(this);
       
    
    }
    
   
    @Override
    public void valueChange(Property.ValueChangeEvent event) {
   this.setComponentError(null);
        try{
         value = Double.valueOf(this.getValue());
         valid=true;
    }catch(NumberFormatException exp){
        valid = false;
    this.setComponentError(new ErrorMessage() {
        @Override
        public ErrorMessage.ErrorLevel getErrorLevel() {
            return ErrorLevel.ERROR;
        }

        @Override
        public String getFormattedHtmlMessage() {
            return "Only Double Value Allowed";
        }
    });
    }    
    }

    public double getDoubleValue() {
        return value;
    }
    
}
