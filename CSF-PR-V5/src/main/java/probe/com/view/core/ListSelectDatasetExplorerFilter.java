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
import com.vaadin.ui.ListSelect;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * This class represents the list select filter for exploring datasets
 *
 * @author Yehia Farag
 */
public class ListSelectDatasetExplorerFilter extends VerticalLayout implements Button.ClickListener {

//    private final DatasetExploringSelectionManager exploringFiltersManager;
    private final Map<String, ClosableFilterLabel> localBtns = new HashMap<String, ClosableFilterLabel>();
    private final String defaultLabel;
    private final int filterId;
    private final ListSelect list;
    private final Button clearBtn;

    public void reset() {
        clearBtn.click();
    }

    public ListSelectDatasetExplorerFilter(int filterId, String defaultLabel, String[] pgArr) {
        this.setSizeUndefined();
        this.filterId = filterId;

//        this.exploringFiltersManager = exploringFiltersManager;
        this.setSpacing(true);

        this.defaultLabel = defaultLabel;
        Label captionLabel = new Label(defaultLabel);
        captionLabel.setStyleName("custLabel");

        if (!defaultLabel.equalsIgnoreCase("")) {
            this.addComponent(captionLabel);
        }
        list = new ListSelect();
        list.setWidth("200px");
        list.setHeight("90px");
        list.setNullSelectionAllowed(true);
        list.setMultiSelect(true);
        for (String str : pgArr) {
            if (!str.equalsIgnoreCase("")) {
                list.addItem(str);
            }
        }

        list.setImmediate(true);
//        
        HorizontalLayout hlo = new HorizontalLayout();
        hlo.setSpacing(true);
        hlo.addComponent(list);
        this.addComponent(hlo);
        clearBtn = new Button("Clear");
        clearBtn.setStyleName(Reindeer.BUTTON_SMALL);
        clearBtn.addClickListener(ListSelectDatasetExplorerFilter.this);
        hlo.addComponent(clearBtn);
//        exploringFiltersManager.registerFilter(ListSelectDatasetExplorerFilter.this);
    }
private Property.ValueChangeListener listener;
    public void addValueChangeListener(Property.ValueChangeListener listener) {
        this.listener=listener;
        list.addValueChangeListener(listener);

    }

//
//    @Override
//    public final void valueChange(Property.ValueChangeEvent event) {
//        for (Object id : list.getItemIds().toArray()) {
//            if (list.isSelected(id.toString())) { 
//                
//                System.out.println("at selection list "+id.toString());
//                handelFilter(id.toString());
//            }
//        }
//
//    }
    private void handelFilter(String value) {

//        if (localBtns.containsKey(defaultLabel+","+value)) {
//            control.addFilter(localBtns.get(defaultLabel+","+value));
//        } else {
//
//            ClosableFilterLabel btn = new ClosableFilterLabel(defaultLabel,value,filterId, true);
//            btn.getCloseBtn().addClickListener(this);
//            localBtns.put(btn.getCaption(), btn);
//            control.addFilter(btn);
//
//        }
////        filterConfirmLabel.setVisible(true);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton().getCaption().equalsIgnoreCase("Clear")) {
            for (Object id : list.getItemIds().toArray()) {
                list.unselect(id);
            }
            for (ClosableFilterLabel btn : localBtns.values()) {
                btn.getCloseBtn().click();

            }
        } else {
            list.unselect(event.getButton().getCaption());
            list.select(null);
        }
    }

    public ListSelect getList() {
        return list;
    }

    public void updateList(Set<String> arr) {
        if (arr == null || arr.isEmpty()) {
            this.setEnabled(false);
            return;
        }
        list.removeValueChangeListener(listener);
        list.removeAllItems();
        for (String str : arr) {
            if (!str.equalsIgnoreCase("")) {
                list.addItem(str);
            }
        }
        this.setEnabled(true);
        list.addValueChangeListener(listener);

    }

}
