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
        this.setStyleName(Reindeer.LAYOUT_WHITE);

        //first spacer
//        Label spacer1 = new Label("<div   style='background:#4d749f;width:100%;height:2px;'></div>");
//        spacer1.setContentMode(Label.CONTENT_XHTML);
//        spacer1.setStyleName(Reindeer.LABEL_SMALL);
//        spacer1.setWidth("100%");
//        VerticalLayout spacer1 = new VerticalLayout();
//        spacer1.setHeight("2px");
////        spacer1.setPrimaryStyleName("spacer");
//        this.addComponent(spacer1);


        //init header

        header.setWidth("1300px");
        header.setHeight("60px");
        header.setStyleName(Reindeer.LAYOUT_WHITE);

        //Coronetscript, cursive Helvetica
        Label csfLable = new Label("<a href='' style='text-decoration:none'><p   align='left' ; style='margin-left:40px;font-family:verdana;color:#4d749f;font-weight:bold;text-decoration:none '><FONT SIZE='5.5'>CSF Proteome Resource (CSF-PR)</FONT></p></a>");
        csfLable.setContentMode(Label.CONTENT_XHTML);
        csfLable.setStyleName(Reindeer.LABEL_SMALL);
        csfLable.setWidth("100%");

        header.addComponent(csfLable);
        header.setComponentAlignment(csfLable, Alignment.MIDDLE_LEFT);


        VerticalLayout logoLayout = new VerticalLayout();
        logoLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        HorizontalLayout hlo = new HorizontalLayout();

        image1.setWidth("237px");
        image1.setHeight("58px");
        hlo.addComponent(image1);

        image2.setWidth("87px");
        image2.setHeight("58px");
        hlo.addComponent(image2);

        //  image3.setWidth("175px");
        image3.setHeight("58px");
        hlo.addComponent(image3);
        Label spacer = new Label("<p   align='left' ; style='margin-left:40px;font-family:verdana;color:#4d749f;font-weight:bold;text-decoration:none '></p>");
        spacer.setContentMode(Label.CONTENT_XHTML);
        spacer.setStyleName(Reindeer.LABEL_SMALL);

        hlo.addComponent(spacer);


        hlo.setComponentAlignment(image1, Alignment.MIDDLE_RIGHT);
        hlo.setComponentAlignment(image2, Alignment.MIDDLE_RIGHT);
        hlo.setComponentAlignment(image3, Alignment.MIDDLE_RIGHT);

        hlo.setComponentAlignment(spacer, Alignment.MIDDLE_RIGHT);


        //hlo.setWidth("499px");
        hlo.setHeight("60px");

        logoLayout.addComponent(hlo);
        logoLayout.setComponentAlignment(hlo, Alignment.MIDDLE_RIGHT);
        //logoLayout.setWidth("499px");
        logoLayout.setWidth("100%");

        header.addComponent(logoLayout);
        header.setComponentAlignment(logoLayout, Alignment.MIDDLE_RIGHT);
        header.setWidth("100%");
        header.setHeight("60px");
        header.setSpacing(true);
        this.addComponent(header);

        //sec spacer
//        Label spacer2 = new Label("<div   style='background:#4d749f;width:100%;height:2px;'></div>");
//        spacer2.setContentMode(Label.CONTENT_XHTML);
//        spacer2.setStyleName(Reindeer.LABEL_SMALL);
//        spacer2.setWidth("100%");
//        

//        VerticalLayout spacer2 = new VerticalLayout();
//	spacer2.setHeight("2px");
//	spacer2.setStyleName(Reindeer.LAYOUT_BLACK);
        //this.addComponent(spacer2);



    }
}
