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
import probe.com.view.components.FiltersControl;

/**
 *
 * @author Yehia Farag
 */
public class ListSelectFilter extends VerticalLayout implements Button.ClickListener, Property.ValueChangeListener {

    private final FiltersControl control;
    private final Map<String, ClosableFilterLabel> localBtns = new HashMap<String, ClosableFilterLabel>();
    private final String defaultLabel;
    private final int filterId;
    private final ListSelect list;
    private final Button clearBtn;
    private final FilterConfirmLabel filterConfirmLabel ;

    @SuppressWarnings("LeakingThisInConstructor")
    public ListSelectFilter(FiltersControl control,int filterId, String defaultLabel, String[] datasetNamesList) {
        this.filterId =filterId;
        this.setStyleName("custLabel");
        this.control = control;
        this.setSpacing(true);
        int widthInt = defaultLabel.length()*7;
        String width = "200px";
        if(widthInt>200)
            width = widthInt+"px";
//        this.setHeight("100px");
        this.defaultLabel = defaultLabel;
        Label captionLabel = new Label(defaultLabel);
        captionLabel.setWidth(width);
        captionLabel.setStyleName("custLabel");
        
        this.addComponent(captionLabel);
        list = new ListSelect();  
        list.setWidth(width);
        list.setHeight("90px");
        list.setNullSelectionAllowed(true);
        list.setMultiSelect(true);
        
        for (String str : datasetNamesList) {
            list.addItem(str);
        }
        list.setImmediate(true);
        list.addValueChangeListener(this);
        HorizontalLayout hlo = new HorizontalLayout();
        hlo.setSpacing(true);
        hlo.addComponent(list);
         filterConfirmLabel = new FilterConfirmLabel();       
        hlo.addComponent(filterConfirmLabel);
        this.addComponent(hlo);
//         ClosableFilterLabel btn = new ClosableFilterLabel(defaultLabel, true);
//                localBtns.put(btn.getCaption(), btn);
//                control.addFilter(localBtns.get(defaultLabel), true);
        clearBtn = new Button("Clear Selection");
        clearBtn.setStyleName(Reindeer.BUTTON_SMALL);
        clearBtn.addClickListener(this);
        this.addComponent(clearBtn);
    }

    @Override
    public final void valueChange(Property.ValueChangeEvent event) {
        filterConfirmLabel.setVisible(false);
        for (Object id : list.getItemIds().toArray()) {
            control.removeFilter(defaultLabel+","+id.toString());
        }
        for (Object id : list.getItemIds().toArray()) {
            if (list.isSelected(id.toString())) {
                handelFilter(id.toString());
            }
        }

    }

    private void handelFilter(String value) {

        if (localBtns.containsKey(defaultLabel+","+value)) {
            control.addFilter(localBtns.get(defaultLabel+","+value));
        } else {

            ClosableFilterLabel btn = new ClosableFilterLabel(defaultLabel,value,filterId, true);
            btn.getCloseBtn().addClickListener(this);
            localBtns.put(btn.getCaption(), btn);
            control.addFilter(btn);

        }
        filterConfirmLabel.setVisible(true);
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (event.getButton().getCaption().equalsIgnoreCase("Clear Selection")) {
            for (Object id : list.getItemIds().toArray()) {
                list.unselect(id);
            }
            for(ClosableFilterLabel btn:localBtns.values()){
                btn.getCloseBtn().click();
                
            }
        }
        else{
        list.unselect(event.getButton().getCaption());
        list.select(null);
        }
    }
    
}
