/**
 * this is help class to view help notes
 *
 */
package probe.com.view.core;

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
import com.vaadin.ui.themes.Reindeer;

/**
 * **
 *
 *
 * @author Yehia Mokhtar
 *
 */
public class IconGenerator implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private Resource res;

    public HorizontalLayout getHelpNote(Label label) {
        PopupView popup = null;
        res = new ThemeResource("../runo/icons/" + 16 + "/help.png");
        HorizontalLayout helpLayout = new HorizontalLayout();
        popup = new PopupView("HELP", label);
        popup.setHideOnMouseOut(true);
        popup.setWidth("40%");
        popup.addPopupVisibilityListener(new PopupView.PopupVisibilityListener() {
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
        HorizontalLayout helpLayout = new HorizontalLayout();
        helpLayout.setWidth("16px");
        helpLayout.setHeight("16px");
        PopupView.Content content = new PopupView.Content() {
            @Override
            public String getMinimizedValueAsHTML() {//Protein Standards
                return "<img src='VAADIN/themes/dario-theme/img/info.jpg' alt='Information'>";//https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-prn2/1175716_173022812881319_804705945_n.jpg
            }

            @Override
            public Component getPopupComponent() {
                return label;

            }
        };

        popup = new PopupView("", label);
        popup.setContent(content);
        popup.setHideOnMouseOut(true);
//        popup.setWidth("40%");
        popup.setStyleName(Reindeer.LAYOUT_BLACK);
        popup.addPopupVisibilityListener(new PopupView.PopupVisibilityListener() {
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
        return helpLayout;

    }

    public HorizontalLayout getExpIcon(final CustomExportBtnLayout comp, final String desc, final String title) {
        PopupView popup = null;
        HorizontalLayout helpLayout = new HorizontalLayout();
        PopupView.Content content = new PopupView.Content() {
            @Override
            public String getMinimizedValueAsHTML() {
                return "<img style='height:16px;margin-top:10px;' src='VAADIN/themes/dario-theme/img/excel.jpg' alt='" + desc + "' title='" + desc + "'> ";//https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-prn2/1175716_173022812881319_804705945_n.jpg
            }

            @Override
            public Component getPopupComponent() {
                return comp;

            }
        };

        popup = new PopupView(title, comp);
        popup.setCaption(title);
        popup.setContent(content);
        popup.setHideOnMouseOut(true);
        popup.setWidth("40%");
        popup.setStyleName(Reindeer.LAYOUT_BLACK);
        popup.addPopupVisibilityListener(new PopupView.PopupVisibilityListener() {
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
        return helpLayout;

    }

}
