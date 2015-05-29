/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class HideOnClickLayout  extends VerticalLayout implements Serializable, LayoutEvents.LayoutClickListener{
    
    private final Label filterstLabel;
    private final HorizontalLayout titleLayout;
    private final ShowLabel show;
    private final Layout fullBodyLayout;
    private final VerticalLayout miniBodyLayout;

    public HideOnClickLayout(String title, Layout fullBodyLayout,VerticalLayout miniBodyLayout) {
        this.setMargin(new MarginInfo(false, false, false, false));
        this.setWidth("100%");
        this.fullBodyLayout = fullBodyLayout;
        this.fullBodyLayout.setVisible(false);
        this.miniBodyLayout =miniBodyLayout;
     
        titleLayout = new HorizontalLayout();
        titleLayout.setHeight("20px");
        titleLayout.setSpacing(true);
        show = new ShowLabel();
        show.setHeight("20px");
        titleLayout.addComponent(show);
        titleLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);

        filterstLabel = new Label(title);
        filterstLabel.setContentMode(ContentMode.HTML);

        filterstLabel.setStyleName("filterShowLabel");
        filterstLabel.setHeight("20px");
        titleLayout.addComponent(filterstLabel);
        titleLayout.setComponentAlignment(filterstLabel, Alignment.TOP_RIGHT);
        titleLayout.addLayoutClickListener(HideOnClickLayout.this);
        this.addComponent(titleLayout);
        this.addComponent(this.fullBodyLayout);
        if(miniBodyLayout != null) {
            titleLayout.addComponent(this.miniBodyLayout);
//            miniBodyLayout.setStyleName("filterShowLabel");
            miniBodyLayout.addLayoutClickListener(HideOnClickLayout.this);
        }
    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (fullBodyLayout.isVisible()) {
            this.hideLayout();

        } else {
            this.showLayout();
        }

    }

    private void showLayout() {
        show.updateIcon(true);
        fullBodyLayout.setVisible(true);
//        if(miniBodyLayout!=null)
//            miniBodyLayout.setVisible(!fullBodyLayout.isVisible());
    }

    public final void hideLayout() {
        show.updateIcon(false);
        fullBodyLayout.setVisible(false);        
//         if(miniBodyLayout!=null)
//            miniBodyLayout.setVisible(!fullBodyLayout.isVisible());

    }

    public boolean isVisability() {
        return fullBodyLayout.isVisible();
    }

    public void setVisability(boolean test) {
        if (test) {
            this.showLayout();
        } else {
            this.hideLayout();
        }
    }
    public void updateFilterLabel(String label){
    
    filterstLabel.setValue(label);
    }

}
