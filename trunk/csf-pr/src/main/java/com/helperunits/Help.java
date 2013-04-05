/**
 * this is help class to view help notes 
 * 
 */

package com.helperunits;

import java.io.Serializable;

import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.ProgressIndicator;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.PopupView.PopupVisibilityEvent;

/****
 * 
 * 
 * @author Yehia Mokhtar
 *
 */
public class Help implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public HorizontalLayout getHelpNote(Label label)
	{
		
		HorizontalLayout helpLayout = new HorizontalLayout();
		 PopupView popup = new PopupView("Help",label);
	     popup.setHideOnMouseOut(true);
	     popup.setWidth("40%");
	     popup.addListener(new PopupView.PopupVisibilityListener(){

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			public void popupVisibilityChange(PopupVisibilityEvent event) {
				 if (!event.isPopupVisible()) {
			        }
				
			}});
	     helpLayout.addComponent(popup);
	     helpLayout.setComponentAlignment(popup, Alignment.BOTTOM_CENTER);
	     Resource res = new ThemeResource("../runo/icons/" + 16 + "/help.png");
	     Embedded e = new Embedded(null, res);
	     e.setWidth("16px");
         e.setHeight("16px");
         helpLayout.addComponent(e);     
	     return helpLayout;
	     
	}
	
	public Window getWitingtWindow()
	{
		final Window witingtWindow = new Window(" Please Wait !");
		witingtWindow.removeAllComponents();
        // ...and make it modal
		witingtWindow.setModal(true);		
        // Configure the windws layout; by default a VerticalLayout
        VerticalLayout layout = (VerticalLayout) witingtWindow.getContent();
        layout.setMargin(true);
        layout.setSpacing(true);     
        
        
        ProgressIndicator pi = new ProgressIndicator();
        Panel p = new Panel("Status");
        p.setSizeUndefined();
        FormLayout lo = new FormLayout();
        lo.setCaption("lo is me");
        lo.setMargin(true);
        p.setContent(lo);
       
        pi.setCaption("Progress");
        pi.setVisible(true);
        lo.addComponent(pi);
        witingtWindow.addComponent(p);
        
       
		return witingtWindow;
	}
}
