/**
 * this is help class to view help notes 
 * 
 */

package probe.com.view.subview.util;

import com.vaadin.server.Resource;
import com.vaadin.server.ThemeResource;
import java.io.Serializable;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Embedded;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
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
        private  Resource res;

	public HorizontalLayout getHelpNote( Label label) {
        PopupView popup = null;
        res = new ThemeResource("../runo/icons/" + 16 + "/help.png");
        HorizontalLayout helpLayout = new HorizontalLayout();
        popup = new PopupView("HELP", label);
        popup.setHideOnMouseOut(true);
        popup.setWidth("40%");
        popup.addListener(new PopupView.PopupVisibilityListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void popupVisibilityChange(PopupVisibilityEvent event) {
                if (!event.isPopupVisible()) {
                }

            }
        });
        helpLayout.addComponent(popup);
        helpLayout.setComponentAlignment(popup, Alignment.BOTTOM_CENTER);

        Embedded e = new Embedded(null, res);
        e.setWidth("16px");
        e.setHeight("16px");
        helpLayout.addComponent(e);
        return helpLayout;

    }
       
        
        public HorizontalLayout getInfoNote(final Label label) {
        PopupView popup = null;
//        res = new ThemeResource("info_icon.jpg");
      // res = new ExternalResource("http://cfaeplanaltobeirao.webege.com/images/info.png");
        HorizontalLayout helpLayout = new HorizontalLayout();    
        
           PopupView.Content content = new PopupView.Content() {
            @Override
            public String getMinimizedValueAsHTML() {//Protein Standards
                return "<img src='https://fbcdn-sphotos-e-a.akamaihd.net/hphotos-ak-ash3/578459_178345075682426_187244436_n.jpg' alt='Information'>";//https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-prn2/1175716_173022812881319_804705945_n.jpg
            }

            @Override
            public Component getPopupComponent() {
                return label;

            }
        };
        
        popup = new PopupView("", label);
        popup.setContent(content);
        popup.setHideOnMouseOut(true);
        popup.setWidth("40%");
        popup.addListener(new PopupView.PopupVisibilityListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void popupVisibilityChange(PopupVisibilityEvent event) {
                if (!event.isPopupVisible()) {
                }

            }
        });
        helpLayout.addComponent(popup);
        helpLayout.setComponentAlignment(popup, Alignment.TOP_CENTER);

//        Embedded e = new Embedded(null, res);
//        e.setWidth("16px");
//        e.setHeight("16px");
//        helpLayout.addComponent(e);
        return helpLayout;

    }

    
}
