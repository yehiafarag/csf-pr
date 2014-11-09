/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author y-mok_000
 */
public class ClosableFilterLabel extends HorizontalLayout {

    private  String value;
    private final int filterId;
    private final String filterTitle;
//         private final Label filterTitleLabel;
    private final Label filterValueLabel;
    private final Button closeBtn;
    private boolean closable;

    @SuppressWarnings("LeakingThisInConstructor")
    public ClosableFilterLabel(String filterTitle, String value, int filterId, boolean closable) {
        filterValueLabel = new Label(value);
        filterValueLabel.setStyleName("filterBtnLabel");
        this.setMargin(new MarginInfo(false, false, false, false));
        this.setSpacing(true);
        filterValueLabel.setContentMode(ContentMode.HTML);
        this.setVisible(true);
       this.closable = closable;
        this.setStyleName(Reindeer.LAYOUT_BLACK);
        this.setHeight("17px");
//        this.addComponent(filterValueLabel);

//             this.filterValueLabel = new Label(value);
        this.filterId = filterId;
        this.value = value;
        this.filterTitle = filterTitle;

        this.closeBtn = new Button("");
        this.closeBtn.setStyleName(Reindeer.BUTTON_LINK);
        String width = ((value.length() * 7)) + "px";
        if (closable) { 
            width = ((value.length() * 7) + 25) + "px";
            closeBtn.setWidth("17px");
            closeBtn.setHeight("17px");            
            closeBtn.setIcon(new ThemeResource("img/ico-close.png"));

        }
        this.setWidth(width);
        this.addComponent(closeBtn);
        this.setComponentAlignment(closeBtn, Alignment.MIDDLE_LEFT);
        this.addComponent(filterValueLabel);

        this.setComponentAlignment(filterValueLabel, Alignment.MIDDLE_LEFT);
        this.setExpandRatio(filterValueLabel, 10);
        this.setExpandRatio(closeBtn, 0.1f);
    }

    @Override
    public String toString() {
        return value;

    }

    @Override
    public String getCaption() {
        return filterTitle +","+ value;
    }

    public int getFilterId() {
        return filterId;
    }

    public Button getCloseBtn() {
        return closeBtn;
    }
    public void setValue(String value){
        this.value = value;
        this.filterValueLabel.setValue(value);
        if(closable)
         this.setWidth(((value.length() * 7) + 25) + "px");
        else
            this.setWidth(((value.length() * 7)) + "px");
    
    }

}
