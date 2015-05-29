/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Layout;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class HorizontalClickToDisplay extends HorizontalLayout implements LayoutEvents.LayoutClickListener{
    private final Layout mainLayout;
        private PopupView container;

    public HorizontalClickToDisplay(Layout mainLayout,String clickableStyle, Layout clickableComponents){
        this.mainLayout = mainLayout;
        this.setWidth("100px");
        this.setHeight("500px");
        this.setSpacing(true);

        VerticalLayout clickableLayout = new VerticalLayout();
        clickableLayout.setHeight("100%");
        clickableLayout.setWidth("100px");

        VerticalLayout clickable = new VerticalLayout();
        clickable.setHeight("100%");
        clickable.setWidth("100px");
        clickable.setStyleName(clickableStyle);

//        clickableLayout.addComponent(clickable);
        clickableLayout.addComponent(clickableComponents);
        clickableLayout.setComponentAlignment(clickableComponents,Alignment.BOTTOM_LEFT);
        clickable.addLayoutClickListener(HorizontalClickToDisplay.this);
        this.addComponent(clickableLayout);
        this.setComponentAlignment(clickableLayout,Alignment.BOTTOM_LEFT);

        VerticalLayout popupLayout = new VerticalLayout();
         popupLayout.setSpacing(true);
         popupLayout.setWidth("500px");

      popupLayout.setHeightUndefined();
//        popupLayout.setHeight(mainLayout.getWidth(),Sizeable.Unit.PIXELS);

        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidth("100%");
        titleLayout.setHeight(22, Sizeable.Unit.PIXELS);

        Label title = new Label("&nbsp;&nbsp;Patients Groups Comparisons");
        title.setContentMode(ContentMode.HTML);
        title.setStyleName("custLabel");
        title.setHeight("20px");
        titleLayout.addComponent(title);

        VerticalLayout minmIcon = new VerticalLayout();
        minmIcon.setWidth("16px");
        minmIcon.setHeight("16px");
        minmIcon.setStyleName("closelabel");
        minmIcon.addLayoutClickListener(HorizontalClickToDisplay.this);
        titleLayout.addComponent(minmIcon);
        titleLayout.setComponentAlignment(minmIcon, Alignment.TOP_RIGHT);
        popupLayout.addComponent(titleLayout);
        mainLayout.setHeightUndefined();
        
     

        popupLayout.addComponent(mainLayout);
        popupLayout.setComponentAlignment(mainLayout,Alignment.BOTTOM_CENTER);
        
        
        
        container = new PopupView("", popupLayout);
        clickable.addComponent(container);
        clickable.setComponentAlignment(container, Alignment.MIDDLE_CENTER);
        
        
        container.setHideOnMouseOut(false);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        container.setPopupVisible(!container.isPopupVisible());
    }
    
    
}
