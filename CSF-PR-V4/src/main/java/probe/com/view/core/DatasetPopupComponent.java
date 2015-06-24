/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author Yehia Farag
 */
public class DatasetPopupComponent extends PopupView {


    private final VerticalLayout mainBody;

    public DatasetPopupComponent(VerticalLayout mainBody, int width) {
        super("", mainBody);
        this.mainBody = mainBody;
        mainBody.setWidth((width - 100) + "px");
        mainBody.setHeight("400px");
        mainBody.setStyleName(Reindeer.LAYOUT_BLUE);
    }

    public void updateForm(){}

}
