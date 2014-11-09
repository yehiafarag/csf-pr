/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.OptionGroup;
import probe.com.view.components.FiltersControl;

/**
 *
 * @author Yehia Farag
 */
public class OptionGroupFilter extends OptionGroup implements Property.ValueChangeListener, Button.ClickListener {

    private final FiltersControl control;
    private final ClosableFilterLabel filterBtn ;//= new HashMap<String, ClosableFilterLabel>();
    private final boolean closable;
    private final int filterId;
    private final String filterLabel;
    private String fieldValue;

    public String getFieldValue() {
        return fieldValue;
    }

    public void setFieldValue(String value) {
        this.fieldValue = value;
    }

    @SuppressWarnings("LeakingThisInConstructor")
    public OptionGroupFilter(FiltersControl control,String filterLabel,int filterId, boolean closable) {
        this.control = control;
        this.filterLabel = filterLabel;
        this.fieldValue = filterLabel;
        this.filterId = filterId;
        this.setNullSelectionAllowed(false); // user can not 'unselect'
        this.setMultiSelect(false);
        this.addValueChangeListener(this);
        this.closable = closable;
         filterBtn = new ClosableFilterLabel(filterLabel,"",filterId, closable);
         filterBtn.getCloseBtn().addClickListener(this);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (isNullSelectionAllowed()) {
            select(null);
            unselect(event.getButton().getParent().toString());
        } else {
            event.getButton().setIcon(null);
        }
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

       
        for (Object id : this.getItemIds().toArray()) {
            control.removeFilterLabel(filterLabel+","+id.toString());
        }
      
        if (event.getProperty().getValue() instanceof String) {
            if (this.getItemIds().contains(event.getProperty().toString())) {
                handelFilter(event.getProperty().toString());
            }
        } else { 
            for (Object id : this.getItemIds().toArray()) {
                if (this.isSelected(id.toString())) {
                    handelFilter(id.toString());
                }
            }

        }

    }

    private void handelFilter(String valueKey) {

        control.removeFilterLabel(filterBtn.getCaption());
        if(valueKey!= null && !valueKey.trim().equalsIgnoreCase(""))
        {
            filterBtn.setValue(valueKey);
            control.addFilterLable(filterBtn);
        
        }
    }
//        });
//        textField.addFocusListener(this);
        
//        if (closable..containsKey(valueKey)) {
//            control.addFilterLable(localBtns.get(filterLabel+","+valueKey));
//        } else {
//
//            ClosableFilterLabel btn = new ClosableFilterLabel(filterLabel,valueKey,filterId, closable);
//            if (closable) {
//                btn.getCloseBtn().addClickListener(this);
//            }
//            localBtns.put(btn.getCaption(), btn);
//            control.addFilterLable(localBtns.get(filterLabel+","+valueKey));
//
//        }

    public ClosableFilterLabel getFilterBtn() {
        return filterBtn;
    }
    

    

}
