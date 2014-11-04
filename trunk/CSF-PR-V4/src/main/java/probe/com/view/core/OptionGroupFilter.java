/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.OptionGroup;
import java.util.HashMap;
import java.util.Map;
import probe.com.view.components.FiltersControl;

/**
 *
 * @author Yehia Farag
 */
public class OptionGroupFilter extends OptionGroup implements Property.ValueChangeListener, Button.ClickListener {

    private final FiltersControl control;
    private final Map<String, ClosableBtn> localBtns = new HashMap<String, ClosableBtn>();
    private final boolean closable;
    private final int filterId;

    @SuppressWarnings("LeakingThisInConstructor")
    public OptionGroupFilter(FiltersControl control,int filterId, boolean closable) {
        this.control = control;
        this.filterId = filterId;
        this.setNullSelectionAllowed(false); // user can not 'unselect'
        this.setMultiSelect(false);
        this.addValueChangeListener(this);
        this.closable = closable;

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (isNullSelectionAllowed()) {
            select(null);
            unselect(event.getButton().getCaption());
        } else {
            event.getButton().setIcon(null);
        }
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {

        for (Object id : this.getItemIds().toArray()) {
            control.removeFilterLabel(id.toString());
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

    private void handelFilter(String lable) {

        if (localBtns.containsKey(lable)) {
            control.addFilterLable(localBtns.get(lable));
        } else {

            ClosableBtn btn = new ClosableBtn(lable,filterId, closable);
            if (closable) {
                btn.addClickListener(this);
            }
            localBtns.put(btn.getCaption(), btn);
            control.addFilterLable(localBtns.get(lable));

        }
    }

}
