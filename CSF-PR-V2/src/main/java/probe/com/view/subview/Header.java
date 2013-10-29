/*
 * this class represent the main header in the main layout 
 */
package probe.com.view.subview;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;

/**
 *
 * @author Yehia Farag
 */
public class Header extends VerticalLayout {

    private HorizontalLayout header = new HorizontalLayout();//Title and logo layout (top layout)
    private Link image1;//logo_1
    private Link image2;//logo_2
    private Link image3;//logo_3

    public Header(Link image1, Link image2, Link image3) {
        
        this.setWidth("100%");
        this.image1 = image1;
        this.image2 = image2;
        this.image3 = image3;
        this.setStyleName(Reindeer.LAYOUT_BLUE);

          //first spacer
        VerticalLayout spacer1 = new VerticalLayout();
        spacer1.setHeight("2px");
        spacer1.setStyleName(Reindeer.LAYOUT_BLACK);
        this.addComponent(spacer1);
        
        
        //init header
       
        header.setWidth("1300px");
        header.setHeight("150px");  
        header.setStyleName(Reindeer.LAYOUT_BLUE);
       
                                                                        //Coronetscript, cursive Helvetica
        Label csfLable = new Label("<a href='' style='text-decoration:none'><h2   align='center' ; style='font-family:verdana;color:white;width:490px;font-weight:bold;text-decoration:none '><FONT SIZE='5.0'>CSF Proteome Resource (CSF-PR)</FONT></h2></a>");
        csfLable.setContentMode(Label.CONTENT_XHTML);
        csfLable.setStyleName(Reindeer.LABEL_SMALL);
        csfLable.setWidth("100%");

        header.addComponent(csfLable);
        header.setComponentAlignment(csfLable, Alignment.MIDDLE_LEFT);
        

        VerticalLayout logoLayout = new VerticalLayout();
        logoLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        HorizontalLayout hlo = new HorizontalLayout();

        image1.setWidth("237px");
        image1.setHeight("105px");
        hlo.addComponent(image1);

        image2.setWidth("87px");
        image2.setHeight("105px");
        hlo.addComponent(image2);
        
        image3.setWidth("175px");
        image3.setHeight("105px");
        hlo.addComponent(image3);


        hlo.setComponentAlignment(image1, Alignment.TOP_LEFT);
        hlo.setComponentAlignment(image2, Alignment.TOP_CENTER);
        hlo.setComponentAlignment(image3, Alignment.TOP_RIGHT);
       
        //hlo.setWidth("499px");
        hlo.setHeight("106px");
        
        logoLayout.addComponent(hlo);
        logoLayout.setComponentAlignment(hlo, Alignment.TOP_RIGHT);
        //logoLayout.setWidth("499px");
        logoLayout.setWidth("100%");        
        
        header.addComponent(logoLayout);
        header.setWidth("100%");
        header.setHeight("106px");
        header.setSpacing(true);
        this.addComponent(header);
        
        //sec spacer
        VerticalLayout spacer2 = new VerticalLayout();
	spacer2.setHeight("2px");
	spacer2.setStyleName(Reindeer.LAYOUT_BLACK);
	this.addComponent(spacer2);
        
        
        
    }
}
