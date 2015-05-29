/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.datasetsoverview.popupinteractivefilter;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class PopupInteractiveDSFiltersLayout extends VerticalLayout implements LayoutEvents.LayoutClickListener {

    private PopupView container;

    public PopupInteractiveDSFiltersLayout(final PopupInteractiveFilterComponent filtersLayout) {
        this.setStyleName("filterlabelbtn");
        VerticalLayout filterframeLayout = new VerticalLayout();

        filterframeLayout.setSpacing(true);

        int width = Page.getCurrent().getBrowserWindowWidth();
        width = width * 95 / 100;
        int height = Page.getCurrent().getBrowserWindowHeight();
        height = height * 80 / 100;
        filterframeLayout.setWidth(width, Sizeable.Unit.PIXELS);
//        filterframeLayout.setHeight(height,Sizeable.Unit.PIXELS);

        HorizontalLayout titleLayout = new HorizontalLayout();
        titleLayout.setWidth("100%");
        titleLayout.setHeight(22, Sizeable.Unit.PIXELS);

        Label title = new Label("&nbsp;&nbsp;Dataset Expolorer Filters");
        title.setContentMode(ContentMode.HTML);
        title.setStyleName("custLabel");
        title.setHeight("20px");
        titleLayout.addComponent(title);

        VerticalLayout minmIcon = new VerticalLayout();
        minmIcon.setWidth("16px");
        minmIcon.setHeight("16px");
        minmIcon.setStyleName("closelabel");
        titleLayout.addComponent(minmIcon);
        titleLayout.setComponentAlignment(minmIcon, Alignment.TOP_RIGHT);
        filterframeLayout.addComponent(titleLayout);

        filterframeLayout.addComponent(filtersLayout);

        container = new PopupView("", filterframeLayout);
        this.addComponent(container);
        container.setHideOnMouseOut(false);
        this.addLayoutClickListener(PopupInteractiveDSFiltersLayout.this);

        minmIcon.addLayoutClickListener(PopupInteractiveDSFiltersLayout.this);

        container.addPopupVisibilityListener(new PopupView.PopupVisibilityListener() {

            @Override
            public void popupVisibilityChange(PopupView.PopupVisibilityEvent event) {
                if (!container.isPopupVisible()) {
                    filtersLayout.updateSelectionManager(true);

                }
            }
        });

    }

    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        container.setPopupVisible(!container.isPopupVisible());

    }

}
