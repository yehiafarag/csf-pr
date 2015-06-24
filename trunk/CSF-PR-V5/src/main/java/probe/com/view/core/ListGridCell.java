/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import org.vaadin.marcus.MouseEvents;

/**
 *
 * @author y-mok_000
 */
public class ListGridCell extends HorizontalLayout {

    private final String cellId;
    private Label label;
    private VerticalLayout sparkline = new VerticalLayout();
    private final ThemeResource blueSrc =  new ThemeResource("img/dot.png");
    private final ThemeResource darkblueSrc = new ThemeResource("img/blackdot.png");
    private final Image icon = new Image(null,blueSrc);
    private final float cellvalue;
    public String getCellId() {
        return cellId;
    }

    public ListGridCell(String value, String cellId, MouseEvents.MouseOverListener mouseOverListener, MouseEvents.MouseOutListener mouseOutListener, boolean showIcon) {
        this.cellId = cellId;
        label = new Label(value);
        if (showIcon) {
           

            this.addComponent(icon);
            this.setSpacing(true);
            this.setComponentAlignment(icon, Alignment.MIDDLE_RIGHT);

        }

        label.setContentMode(ContentMode.HTML);
        final MouseEvents mouseEvents = MouseEvents.enableFor(label);
        mouseEvents.addMouseOutListener(mouseOutListener);
        mouseEvents.addMouseOverListener(mouseOverListener);
        this.addComponent(label);
        this.setStyleName("publicationoverviewlabel");
        cellvalue= (float) ((label.getValue()).length());
        this.setWidth("500px");

    }

    //sparkline constractor
    public ListGridCell(VerticalLayout sparkline, String cellId, MouseEvents.MouseOverListener mouseOverListener, MouseEvents.MouseOutListener mouseOutListener) {
        this.cellId = cellId;
        this.sparkline=sparkline;
        final MouseEvents mouseEvents = MouseEvents.enableFor(this.sparkline);
        mouseEvents.addMouseOutListener(mouseOutListener);
        mouseEvents.addMouseOverListener(mouseOverListener);
        this.addComponent(sparkline);
        cellvalue=100;
    }
    
    
    
    

    public float getCellValue() {
        return cellvalue;
    }

    public void setSelected(boolean selected) {
        if (selected) {
            icon.setSource(darkblueSrc);
            this.setStyleName("selectedpublicationoverviewlabel");
            this.sparkline.setStyleName("selectedbluesparkline");
            

        } else {
            icon.setSource(blueSrc);
            this.setStyleName("publicationoverviewlabel");
             this.sparkline.setStyleName("bluesparkline");
        }
    }
    
}
