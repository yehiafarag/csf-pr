/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.data.Property;
import com.vaadin.ui.Button;
import com.vaadin.ui.ListSelect;
import java.util.HashMap;
import java.util.Map;
import probe.com.view.components.FiltersControl;

/**
 *
 * @author Yehia Farag
 */
public class ListSelectFilter extends ListSelect implements Button.ClickListener, Property.ValueChangeListener {

    private final FiltersControl control;
    private final Map<String, ClosableBtn> localBtns = new HashMap<String, ClosableBtn>();
    private final String defaultLabel;
    private final int filterId;

    public ListSelectFilter(FiltersControl control,int filterId, String defaultLabel, String[] datasetNamesList) {
        super(defaultLabel);
        this.filterId =filterId;
        this.setStyleName("custLabel");
        this.control = control;
        this.defaultLabel = defaultLabel;
        this.setNullSelectionAllowed(true);
        this.setMultiSelect(true);
        this.setHeight("70px");
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
        for (Object id : this.getItemIds().toArray()) {
            if (this.isSelected(id.toString())) {
                handelFilter(id.toString());
            }
        }

    }

    private void handelFilter(String lable) {

        if (localBtns.containsKey(lable)) {
            control.addFilterLable(localBtns.get(lable));
        } else {

            ClosableBtn btn = new ClosableBtn(lable,filterId, true);
            btn.addClickListener(this);
            localBtns.put(btn.getCaption(), btn);
            control.addFilterLable(localBtns.get(lable));

        }
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        select(null);
        unselect(event.getButton().getCaption());
    }

}
