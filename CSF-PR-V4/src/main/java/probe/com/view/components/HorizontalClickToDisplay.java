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

    public HorizontalClickToDisplay(Layout mainLayout,String clickableStyle, VerticalLayout clickableComponents){
        this.mainLayout = mainLayout;
        this.setWidth("150px");
        this.setHeight("100%");
        this.setSpacing(true);

        VerticalLayout frameLayout = new VerticalLayout();
        frameLayout.setHeight("100%");
        frameLayout.setWidth("150px");       
        this.addComponent(clickableComponents);
        this.setComponentAlignment(clickableComponents, Alignment.TOP_CENTER); 
        frameLayout.setSpacing(true);      
        
        
        
        VerticalLayout clickableComparisonIcon = new VerticalLayout();
        clickableComparisonIcon.setHeight("40px");
        clickableComparisonIcon.setWidth("150px");
        clickableComparisonIcon.setStyleName(clickableStyle);
        clickableComparisonIcon.addLayoutClickListener(HorizontalClickToDisplay.this);
        clickableComponents.addComponent(clickableComparisonIcon,0);
        clickableComponents.setComponentAlignment(clickableComparisonIcon,Alignment.MIDDLE_CENTER);
//        frameLayout.addComponent(clickableComparisonIcon);
//        frameLayout.setComponentAlignment(clickableComparisonIcon,Alignment.MIDDLE_CENTER);
//        frameLayout.addComponent(clickableComponents);
//        frameLayout.setComponentAlignment(clickableComponents,Alignment.MIDDLE_CENTER);
        
               

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
        clickableComparisonIcon.addComponent(container);
        clickableComparisonIcon.setComponentAlignment(container, Alignment.MIDDLE_CENTER); 
        
        container.setHideOnMouseOut(false);

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        container.setPopupVisible(!container.isPopupVisible());
    }
    
    
}
