/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class MiniStudyFilter extends HorizontalLayout implements Serializable, LayoutEvents.LayoutClickListener {

    private String value;

    public String getFilterValue() {
        return value;
    }
    private final String filterId;
    private final Label filterValueLabel;
    private final String space = "&nbsp; &nbsp; ";
    private final DatasetExploringCentralSelectionManager Filter_Manager;

    public MiniStudyFilter(String filterId, String value, DatasetExploringCentralSelectionManager Filter_Manager, String filterLabel) {
        filterValueLabel = new Label();
        this.Filter_Manager = Filter_Manager;

        filterValueLabel.setStyleName("studyFilterClosableBtnLabel");

//        filterValueLabel.setValue(space + value);
        filterValueLabel.setContentMode(ContentMode.HTML);
        this.setVisible(true);
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.filterId = filterId;
        this.value = value;
        if (value.trim().equalsIgnoreCase("Not Available") || value.trim().equalsIgnoreCase("none")) {
            this.filterValueLabel.setValue(space + filterLabel + " " + this.value);
        } else {
            this.filterValueLabel.setValue(space + this.value);
        }
        this.addComponent(filterValueLabel);
        this.setComponentAlignment(filterValueLabel, Alignment.TOP_LEFT);
        this.addLayoutClickListener(MiniStudyFilter.this);
    }

    @Override
    public String toString() {
        return value;

    }

    public void setValue(String value) {
        this.value = value;

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        Filter_Manager.removeFilterValue(filterId, value);
    }

    public float getExpandRatio() {
        return filterValueLabel.getValue().length();
    }

}
