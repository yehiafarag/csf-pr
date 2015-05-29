/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class ClosableFilterLabel extends HorizontalLayout implements Serializable, LayoutEvents.LayoutClickListener{

    private String value;

    public String getFilterValue() {
        return value;
    }
    private final int filterId;
    private final String filterTitle;
//         private final Label filterTitleLabel;
    private final Label filterValueLabel;
    private final Button closeBtn;
//    private boolean closable;
    private String space = "&nbsp; &nbsp; ";

    public ClosableFilterLabel(String filterTitle, String value, int filterId, boolean closable) {
        filterValueLabel = new Label();
        if (closable) {

            filterValueLabel.setStyleName("filterClosableBtnLabel");
//            width = ((value.length() * 7)) + "px";
//            closeBtn.setWidth("17px");
//            closeBtn.setHeight("17px");            
//            closeBtn.setIcon(new ThemeResource("img/remove-icon.jpg"));

        } else {
            space = "";
            filterValueLabel.setStyleName("filterNonClosableBtnLabel");
        }

//        this.setMargin(new MarginInfo(false, false, false, false));
//        this.setSpacing(false);
        filterValueLabel.setValue(space + value);
        filterValueLabel.setContentMode(ContentMode.HTML);
        this.setVisible(true);
//        this.closable = closable;
        this.setStyleName(Reindeer.LAYOUT_WHITE);
//        this.setHeight("17px");
//        this.addComponent(filterValueLabel);

//             this.filterValueLabel = new Label(value);
        this.filterId = filterId;
        this.value = value;
        this.filterTitle = filterTitle;

        this.closeBtn = new Button(value);
//        String width = ((value.length() * 7)) + "px";

//        this.setWidth(width);
        closeBtn.setVisible(false);
        this.addComponent(closeBtn);
        this.setComponentAlignment(closeBtn, Alignment.MIDDLE_LEFT);
        this.addComponent(filterValueLabel);
        this.setComponentAlignment(filterValueLabel, Alignment.TOP_LEFT);
//        this.setExpandRatio(filterValueLabel, 10);
//        this.setExpandRatio(closeBtn, 0.1f);
        this.addLayoutClickListener(ClosableFilterLabel.this);
    }

    @Override
    public String toString() {
        return value;

    }

    @Override
    public String getCaption() {
        return filterTitle + "," + value;
    }

    public int getFilterId() {
        return filterId;
    }

    public Button getCloseBtn() {
        return closeBtn;
    }

    public void setValue(String value) {
        this.value = value;
        closeBtn.setCaption(value);
        this.filterValueLabel.setValue(space+this.value);
//        if(closable)
//         this.setWidth(((value.length() * 7) + 25) + "px");
//        else
//            this.setWidth(((value.length() * 7)) + "px");

    }

    public String getFilterTitle() {
        return filterTitle;
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {        
        closeBtn.click();
         }
    

}
