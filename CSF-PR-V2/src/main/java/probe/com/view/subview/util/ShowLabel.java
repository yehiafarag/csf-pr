/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview.util;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Label;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public final class ShowLabel extends Label implements Serializable {

    boolean show = false;

    public ShowLabel() {
        updateIcon(show);
    }
     public ShowLabel(boolean v) {
        updateIcon(v);
    }

    public void updateIcon(boolean show) {
        if (show) {
            show = false;
            setIcon(new ExternalResource("https://fbcdn-sphotos-d-a.akamaihd.net/hphotos-ak-prn2/1175716_173022812881319_804705945_n.jpg"));

        } else {
            show = true;
            setIcon(new ExternalResource("https://fbcdn-sphotos-f-a.akamaihd.net/hphotos-ak-prn2/1185892_173022819547985_676735970_n.jpg"));


        }
    }
}
