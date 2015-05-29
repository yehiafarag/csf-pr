/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Label;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class FilterConfirmLabel extends Label implements Serializable{
    
    public FilterConfirmLabel(){
    
     this.setIcon(new ThemeResource("img/true_s.jpg"));
        this.setStyleName("custLabel");
        this.setContentMode(ContentMode.HTML);
        this.setVisible(false);
        this.setDescription("Filter Applied");
        this.setHeight("20px");
    }
    
}
