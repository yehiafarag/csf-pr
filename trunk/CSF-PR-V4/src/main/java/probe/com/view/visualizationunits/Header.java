/*
 * this class represent the main header in the main layout 
 */
package probe.com.view.visualizationunits;

import com.vaadin.server.ExternalResource;
import com.vaadin.server.ThemeResource;
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

    private final  HorizontalLayout header = new HorizontalLayout();//Title and logo layout (top layout)
    

    public Header() {

        this.setWidth("100%");
         this.setStyleName(Reindeer.LAYOUT_WHITE);
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
        
        
        Link probe_ico = new Link(null, new ExternalResource("http://www.uib.no/rg/probe"));
        probe_ico.setIcon(new ThemeResource("img/probe.jpg"));
        probe_ico.setTargetName("_blank");
        probe_ico.setWidth("237px");
        probe_ico.setHeight("58px");
        hlo.addComponent(probe_ico);

        Link uib_ico = new Link(null, new ExternalResource("http://www.uib.no/"));
        uib_ico.setIcon(new ThemeResource("img/uib.jpg"));
        uib_ico.setTargetName("_blank");
        uib_ico.setWidth("87px");
        uib_ico.setHeight("58px");
        hlo.addComponent(uib_ico);

        Link kgj_ico = new Link(null, new ExternalResource("http://www.stiftkgj.no/"));
        kgj_ico.setIcon(new ThemeResource("img/kgj.jpg"));
        kgj_ico.setTargetName("_blank");
        kgj_ico.setHeight("58px");
        hlo.addComponent(kgj_ico);
        Label spacer = new Label("<p   align='left' ; style='margin-left:40px;font-family:verdana;color:#4d749f;font-weight:bold;text-decoration:none '></p>");
        spacer.setContentMode(Label.CONTENT_XHTML);
        spacer.setStyleName(Reindeer.LABEL_SMALL);

        hlo.addComponent(spacer);


        hlo.setComponentAlignment(probe_ico, Alignment.MIDDLE_RIGHT);
        hlo.setComponentAlignment(uib_ico, Alignment.MIDDLE_RIGHT);
        hlo.setComponentAlignment(kgj_ico, Alignment.MIDDLE_RIGHT);

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

    }
}
