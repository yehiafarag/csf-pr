/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import java.util.HashMap;
import java.util.Map;
import probe.com.view.components.FiltersControl;

/**
 *
 * @author Yehia Farag
 */
public class ComboBoxFilter extends HorizontalLayout implements Property.ValueChangeListener,Button.ClickListener{
    private final FiltersControl control;
    private final  Map<String,ClosableFilterLabel> localBtns = new HashMap<String, ClosableFilterLabel>();
    private final String defaultLabel;
    private final int filterId;  
    private final ComboBox comboBox;
    private final FilterConfirmLabel filterConfirmLabel ;
   private final String filterTitle;
    
    @SuppressWarnings("LeakingThisInConstructor")
      public ComboBoxFilter(FiltersControl control,int filterId,String filterTitle,String defaultLabel,String[] datasetNamesList){
       this.control = control;
       this.filterId=filterId;
       this.filterTitle =filterTitle;
       this.defaultLabel = defaultLabel;
       
       this.setSpacing(true);
       this.setMargin(false);
       comboBox = new ComboBox();
       this.addComponent(comboBox);
       comboBox.setWidth("150px");
       comboBox.setNullSelectionAllowed(false);
       comboBox.addItem(defaultLabel);
        comboBox.setValue(defaultLabel);
        for (String str : datasetNamesList) {
            comboBox.addItem(str);
        }
        this.setImmediate(true);       
        comboBox.addValueChangeListener(this);   
         filterConfirmLabel = new FilterConfirmLabel();       
        this.addComponent(filterConfirmLabel);
//         ClosableFilterLabel btn = new ClosableFilterLabel(defaultLabel, true);
//                localBtns.put(btn.getCaption(), btn);
//                control.addFilter(localBtns.get(defaultLabel), true);
    }
    
    
    @Override
    public final void valueChange(Property.ValueChangeEvent event) {
        for (Object id : comboBox.getItemIds().toArray()) {
            control.removeFilter(filterTitle+","+id.toString());
        }
        filterConfirmLabel.setVisible(false);
        if(event.getProperty()!= null && (!event.getProperty().toString().equalsIgnoreCase(defaultLabel))&& comboBox.getItemIds().contains(event.getProperty().toString())){

            if (localBtns.containsKey(filterTitle+","+event.getProperty().toString())) {
                control.addFilter(localBtns.get(filterTitle+","+event.getProperty().toString()));
                filterConfirmLabel.setVisible(true);
            } else {
                ClosableFilterLabel btn = new ClosableFilterLabel(filterTitle,event.getProperty().toString(),filterId, true);
                btn.getCloseBtn().addClickListener(this);
                localBtns.put(btn.getCaption(), btn);
                control.addFilter(localBtns.get(btn.getCaption()));                
                filterConfirmLabel.setVisible(true);
            }
        }

    }

    public ComboBox getComboBox() {
        return comboBox;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {        
        comboBox.select(defaultLabel);
    }
    
}
