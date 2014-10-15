/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

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
        Label  errorLabel = new Label("<h3 Style='color:red;'>No results found for ( " + protName + " ) </h3>");
        errorLabel.setContentMode(Label.CONTENT_XHTML);
        this.addComponent(errorLabel);
    
    }
}
