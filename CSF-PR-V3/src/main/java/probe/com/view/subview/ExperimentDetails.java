package probe.com.view.subview;

import java.io.Serializable;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;
import probe.com.control.ExperimentHandler;
import probe.com.model.beans.ExperimentBean;
import probe.com.view.subview.util.CustomExportBtnLayout;
import probe.com.view.subview.util.Help;
import probe.com.view.subview.util.ShowLabel;

@SuppressWarnings("serial")
public class ExperimentDetails extends VerticalLayout implements Serializable, com.vaadin.event.LayoutEvents.LayoutClickListener {

    private VerticalLayout body;
    private Label ExpLable;
    private HorizontalLayout header;
    private Help help = new Help();
     
    private ShowLabel show;

    public ExperimentDetails(ExperimentBean exp, boolean visability,ExperimentHandler expHandler) {
        this.setMargin(new MarginInfo(false,true,false,true));
        this.setWidth("100%");
        //color:#000000;
        header = new HorizontalLayout();
        header.setHeight("45px");
        header.setSpacing(true);
        
        show = new ShowLabel(); 
        header.addComponent(show);
        header.setComponentAlignment(show,Alignment.BOTTOM_LEFT);
        
        ExpLable = new Label("<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Dataset  Information </strong></h4>");
        ExpLable.setContentMode(Label.CONTENT_XHTML);
        ExpLable.setHeight("45px");
        header.addComponent(ExpLable);
        header.setComponentAlignment(ExpLable,Alignment.TOP_RIGHT);        
        header.addListener(ExperimentDetails.this);
        this.addComponent(header);        
        this.body = FormWithComplexLayout(exp,expHandler);            
        
        this.addComponent(body);
        if (visability) {
            this.showDetails();
        } else {
            this.hideDetails();
        }
    }

    @SuppressWarnings("deprecation")
    private VerticalLayout FormWithComplexLayout(ExperimentBean exp,ExperimentHandler expHandler) {
        VerticalLayout vlo1 = new VerticalLayout();
        vlo1.setSpacing(true);
        vlo1.setMargin(false);
        vlo1.setSizeFull();
        HorizontalLayout hlo1 = new HorizontalLayout();
        VerticalLayout topSpacer = new VerticalLayout();
        topSpacer.setHeight("2px");
        topSpacer.setMargin(false);
        topSpacer.setStyleName(Reindeer.LAYOUT_BLACK);
        vlo1.addComponent(topSpacer);
        vlo1.addComponent(hlo1);
        VerticalLayout buttomSpacer = new VerticalLayout();
        buttomSpacer.setHeight("2px");
        buttomSpacer.setStyleName(Reindeer.LAYOUT_BLACK);
        buttomSpacer.setMargin(false);
        vlo1.addComponent(buttomSpacer);
        VerticalLayout l1 = new VerticalLayout();


        Label ExpLable1_1 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Dataset  Name:</strong><br/>" + exp.getName() + "</h5>");
        ExpLable1_1.setContentMode(Label.CONTENT_XHTML);
        ExpLable1_1.setHeight("45px");

        Label ExpLable1_2 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Species:</strong><br/>" + exp.getSpecies() + "</h5>");
        ExpLable1_2.setContentMode(Label.CONTENT_XHTML);
        ExpLable1_2.setHeight("45px");

        Label ExpLable1_3 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Sample Type:</strong><br/>" + exp.getSampleType() + "</h5>");
        ExpLable1_3.setContentMode(Label.CONTENT_XHTML);
        ExpLable1_3.setHeight("45px");


        Label ExpLable1_4 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Sample Processing:</strong><br/>" + exp.getSampleProcessing() + "</h5>");
        ExpLable1_4.setContentMode(Label.CONTENT_XHTML);
        ExpLable1_4.setHeight("45px");

        String href = null;
        Label ExpLable1_5 = null;
        if (exp.getPublicationLink().equalsIgnoreCase("NOT AVAILABLE") || exp.getPublicationLink().equalsIgnoreCase("")) {
            ExpLable1_5 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>No Publication Link Available </strong></h5>");
            ExpLable1_5.setHeight("45px");
        } else {
            href = exp.getPublicationLink().toLowerCase();
            if (href.contains("http://") || href.contains("https://"))
        		; else {
                href = "http://" + href;
            }
            ExpLable1_5 = new Label("<h5><a href='" + href + "'  target='_blank'>Publication Link</a></h5>");
            ExpLable1_5.setHeight("45px");

        }

        ExpLable1_5.setContentMode(Label.CONTENT_XHTML);
        l1.addComponent(ExpLable1_1);
        if (exp.getDescription().length() <= 100) {
            Label ExpLable2_1 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Description:</strong><br/>" + exp.getDescription() + "</h5>");
            ExpLable2_1.setContentMode(Label.CONTENT_XHTML);
            ExpLable2_1.setHeight("45px");
            l1.addComponent(ExpLable2_1);
        } else {
            Label ExpLable2_1 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Description:</strong></h5>");
            ExpLable2_1.setContentMode(Label.CONTENT_XHTML);
            ExpLable2_1.setHeight("30px");

            Label ExpLable2_2 = new Label("<h5 style='font-family:verdana;color:gray;'>" + exp.getDescription() + "</h5>");
            ExpLable2_2.setContentMode(Label.CONTENT_XHTML);


            VerticalLayout lTemp = new VerticalLayout();
            //lTemp.addComponent(ExpLable2_1);
            lTemp.addComponent(ExpLable2_2);
            lTemp.setMargin(true);
            ExpLable2_2.setSizeFull();
            Panel p = new Panel();
            p.setContent(lTemp);
            p.setWidth("80%");
            p.setHeight("80px");
            //p.sets.setScrollable(true);
            p.setScrollTop(20);
            p.setScrollLeft(50);

            p.setStyleName(Runo.PANEL_LIGHT);

            l1.addComponent(ExpLable2_1);
            // l1.setComponentAlignment(ExpLable2_1, Alignment.BOTTOM_LEFT);
            l1.addComponent(p);
            //  l1.setComponentAlignment(p, Alignment.TOP_LEFT);
        }
        
        VerticalLayout l2 = new VerticalLayout();

        Label ExpLable2_3 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Instrument Type:</strong><br/>" + exp.getInstrumentType() + "</h5>");
        ExpLable2_3.setContentMode(Label.CONTENT_XHTML);
        ExpLable2_3.setHeight("45px");
        
        Label ExpLable2_4 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Frag Mode:</strong><br/>" + exp.getFragMode() + "</h5>");
        ExpLable2_4.setContentMode(Label.CONTENT_XHTML);
        ExpLable2_4.setHeight("45px");
        Label ExpLable2_5 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Uploaded By:</strong><br/>" + exp.getUploadedByName() + "</h5>");
        ExpLable2_5.setContentMode(Label.CONTENT_XHTML);
        ExpLable2_5.setHeight("45px");

        Label ExpLable2_6 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Email:</strong><br/>" + exp.getEmail() + "</h5>");
        ExpLable2_6.setContentMode(Label.CONTENT_XHTML);
        ExpLable2_6.setHeight("45px");


        Label ExpLable2_7 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'># Fractions</strong><br/>" + exp.getFractionsNumber() + "</h5>");
        ExpLable2_7.setContentMode(Label.CONTENT_XHTML);
        ExpLable2_7.setHeight("45px");
        Label ExpLable2_8 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'># Proteins:</strong><br/>" +exp.getNumberValidProt()/* exp.getProteinsNumber()*/ + "</h5>");
        ExpLable2_8.setContentMode(Label.CONTENT_XHTML);
        ExpLable2_8.setDescription("Number of validated proteins");
        ExpLable2_8.setHeight("45px");

        HorizontalLayout pepHlo = new HorizontalLayout();
        Label ExpLable2_9 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'># Peptides</strong><br/>" + exp.getPeptidesNumber() + "</h5>");
        ExpLable2_9.setContentMode(Label.CONTENT_XHTML);
        ExpLable2_9.setDescription("Number of validated peptides");
        ExpLable2_9.setHeight("45px");
        
        
        
//        PopupView popup = new PopupView("(Export All)", (new CustomExportBtnLayout(expHandler,"allPep",exp.getExpId(),exp.getName(),null,null,null,null,0,null,null,null)) );
//	popup.setHideOnMouseOut(false);
        
       HorizontalLayout expIcon = help.getExpIcon(new CustomExportBtnLayout(expHandler, "allPep", exp.getExpId(), exp.getName(), null, null, null, null, 0, null, null, null), "Export All Peptides for " + exp.getName(), "");


        pepHlo.addComponent(ExpLable2_9);
        pepHlo.addComponent(expIcon);



        l1.addComponent(ExpLable2_3);
        
        l2.addComponent(ExpLable1_2);
        l2.addComponent(ExpLable2_4);
        l2.addComponent(ExpLable1_4);

        l2.addComponent(ExpLable1_3);
        l2.addComponent(pepHlo);
        //l2.addComponent(popup);
        pepHlo.setComponentAlignment(expIcon, Alignment.MIDDLE_CENTER);
        pepHlo.setSpacing(true);

        VerticalLayout l3 = new VerticalLayout();


        l3.addComponent(ExpLable2_7);


        l3.addComponent(ExpLable2_8);
        l3.addComponent(ExpLable2_5);
        l3.addComponent(ExpLable2_6);
        l3.addComponent(ExpLable1_5);




        hlo1.setWidth("100%");
        hlo1.addComponent(l1);
        hlo1.addComponent(l2);
        hlo1.addComponent(l3);
        hlo1.setExpandRatio(l1, 3);
        hlo1.setExpandRatio(l2, 3);
        hlo1.setExpandRatio(l3, 1);

        hlo1.setComponentAlignment(l3, Alignment.TOP_RIGHT);
        return vlo1;
    }

    @Override
    public void layoutClick(LayoutClickEvent event) {

        if (body.isVisible()) {
            this.hideDetails();
            
        } else {

            this.showDetails();
        }

    }

    private void showDetails() {
//        ExpLable.setValue("<h4  style='font-family:verdana;font-weight:bold;'><strong style='font-family:verdana;color:#000000;'>Dataset  Information</strong></h4>");
        show.updateIcon(true);
//        ExpLable.setContentMode(Label.CONTENT_XHTML);
//        ExpLable.setHeight("30px");
        body.setVisible(true);
    }

    public final void hideDetails() {
        show.updateIcon(false);
//        ExpLable.setValue("<h4  style='font-family:verdana;font-weight:bold;'><strong style='font-family:verdana;color:#000000;'>Dataset  Information </strong></h4>");
//        ExpLable.setContentMode(Label.CONTENT_XHTML);
//        ExpLable.setHeight("30px");
        body.setVisible(false);
    }

    public boolean isVisability() {
        return body.isVisible();
    }

    public void setVisability(boolean test) {
        if (test) {
            this.showDetails();
        } else {
            this.hideDetails();
        }
    }
}
