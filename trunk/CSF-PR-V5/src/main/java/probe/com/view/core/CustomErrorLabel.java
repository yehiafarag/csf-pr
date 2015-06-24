/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class CustomErrorLabel extends VerticalLayout implements Serializable{
    public void updateErrot(String protName)
    {
        this.removeAllComponents();
        Label  errorLabel = new Label("<h4 Style='color:red;'>No results found for ( " + protName + " ) </h4>");
        errorLabel.setContentMode(ContentMode.HTML);
        this.addComponent(errorLabel);
    
    }
}
