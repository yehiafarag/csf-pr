
package probe.com.view.core;

import com.vaadin.server.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;

/**
 *
 * @author Yehia Farag
 */
public class InformationField extends VerticalLayout {
    
    private final Link valueLabel;
    
    public InformationField(String title) {
        
        this.setHeight("60px");
        this.setSpacing(true);
        this.setMargin(true);
        Label titleLabel = new Label(title + " : ");
        this.addComponent(titleLabel);
        titleLabel.setStyleName("caption");
        this.setComponentAlignment(titleLabel, Alignment.TOP_LEFT);
        valueLabel = new Link();
        
        valueLabel.setStyleName("valuelabel");
        valueLabel.setTargetName("_blank");

//        valueLabel.setContentMode(ContentMode.HTML);
        this.addComponent(valueLabel);
        this.setComponentAlignment(valueLabel, Alignment.TOP_LEFT);
        

    
    }

    public void setValue(String value,String urlAddress) {
        if (value == null || value.trim().equalsIgnoreCase("")  || value.equalsIgnoreCase("-1")) {
//            this.setVisible(false);
            valueLabel.setCaption("NOT AVAILABLE");
             valueLabel.setStyleName("valuelabel");
            return;
        }

        if (value.toCharArray().length > 25) {
            valueLabel.setCaption("<textarea rows='3' cols='25' readonly>" + value + "</textarea>");
            valueLabel.setCaptionAsHtml(true);
//            this.setHeight("100px");
        } else {
            valueLabel.setCaption(value);
        }
        if(urlAddress != null){
         valueLabel.setStyleName("valuelink");
         valueLabel.setResource(new ExternalResource(urlAddress));
        }
        else
             valueLabel.setStyleName("valuelabel");
//        this.setVisible(true);
    }
}
