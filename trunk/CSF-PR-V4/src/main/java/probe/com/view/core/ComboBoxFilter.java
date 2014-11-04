/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import java.util.HashMap;
import java.util.Map;
import probe.com.view.components.FiltersControl;

/**
 *
 * @author Yehia Farag
 */
public class ComboBoxFilter extends ComboBox implements Property.ValueChangeListener,Button.ClickListener{
    private final FiltersControl control;
    private final  Map<String,ClosableBtn> localBtns = new HashMap<String, ClosableBtn>();
    private final String defaultLabel;
    private final int filterId;  
   
    
    @SuppressWarnings("LeakingThisInConstructor")
      public ComboBoxFilter(FiltersControl control,int filterId,String defaultLabel,String[] datasetNamesList){
       this.control = control;
       this.filterId=filterId;
       this.defaultLabel = defaultLabel;
       this.setNullSelectionAllowed(false);
       this.addItem(defaultLabel);
        this.setValue(defaultLabel);
        for (String str : datasetNamesList) {
            this.addItem(str);
        }
        this.setImmediate(true);       
        this.addValueChangeListener(this);      
//         ClosableBtn btn = new ClosableBtn(defaultLabel, true);
//                localBtns.put(btn.getCaption(), btn);
//                control.addFilterLable(localBtns.get(defaultLabel), true);
    }
    
    
    @Override
    public final void valueChange(Property.ValueChangeEvent event) {
        for (Object id : this.getItemIds().toArray()) {
            control.removeFilterLabel(id.toString());
        }
        if(event.getProperty()!= null && (!event.getProperty().toString().equalsIgnoreCase(defaultLabel))&& this.getItemIds().contains(event.getProperty().toString())){

            if (localBtns.containsKey(event.getProperty().toString())) {
                control.addFilterLable(localBtns.get(event.getProperty().toString()));
            } else {
                ClosableBtn btn = new ClosableBtn(event.getProperty().toString(),filterId, true);
                btn.addClickListener(this);
                localBtns.put(btn.getCaption(), btn);
                control.addFilterLable(localBtns.get(event.getProperty().toString()));
            }
        }

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {        
        this.select(defaultLabel);
    }
    
}
