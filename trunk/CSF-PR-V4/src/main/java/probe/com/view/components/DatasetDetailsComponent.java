package probe.com.view.components;

import java.io.Serializable;

import com.vaadin.event.LayoutEvents.LayoutClickEvent;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;
import probe.com.handlers.MainHandler;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.IconGenerator;
import probe.com.view.core.ShowLabel;

@SuppressWarnings("serial")
public class DatasetDetailsComponent extends VerticalLayout implements Serializable, com.vaadin.event.LayoutEvents.LayoutClickListener {

    private final VerticalLayout mainComponentBodyLayout;
    private final Label datasetLabel;
    private final HorizontalLayout titleLayout;
    private final IconGenerator excelExporterIconGenerator = new IconGenerator();

    private final ShowLabel show;
    public DatasetDetailsComponent(boolean visability, MainHandler handler) {
        
        this.setMargin(new MarginInfo(false, true, false, true));
        this.setWidth("100%");
        
        titleLayout = new HorizontalLayout();
        titleLayout.setHeight("45px");
        titleLayout.setSpacing(true);
        show = new ShowLabel();
        titleLayout.addComponent(show);
        titleLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);

        datasetLabel = new Label("<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Dataset  Information </strong></h4>");
        datasetLabel.setContentMode(ContentMode.HTML);
        datasetLabel.setHeight("45px");
        
        titleLayout.addComponent(datasetLabel);
        titleLayout.setComponentAlignment(datasetLabel, Alignment.TOP_RIGHT);        
        titleLayout.addLayoutClickListener(DatasetDetailsComponent.this);
        this.addComponent(titleLayout);
        this.mainComponentBodyLayout = FormWithComplexLayout(handler);

        this.addComponent(mainComponentBodyLayout);
        if (visability) {
            this.showDetails();
        } else {
            this.hideDetails();
        }
    }

    @SuppressWarnings("deprecation")
    private VerticalLayout FormWithComplexLayout(MainHandler handler) {
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

        Label ExpLable1_1 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Dataset  Name:</strong><br/>" + handler.getMainDataset().getName() + "</h5>");
        ExpLable1_1.setContentMode(ContentMode.HTML);
        ExpLable1_1.setHeight("45px");

        Label ExpLable1_2 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Species:</strong><br/>" + handler.getMainDataset().getSpecies() + "</h5>");
        ExpLable1_2.setContentMode(ContentMode.HTML);
        ExpLable1_2.setHeight("45px");

        Label ExpLable1_3 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Sample Type:</strong><br/>" + handler.getMainDataset().getSampleType() + "</h5>");
        ExpLable1_3.setContentMode(ContentMode.HTML);
        ExpLable1_3.setHeight("45px");

        Label ExpLable1_4 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Sample Processing:</strong><br/>" + handler.getMainDataset().getSampleProcessing() + "</h5>");
        ExpLable1_4.setContentMode(ContentMode.HTML);
        ExpLable1_4.setHeight("45px");

        String href = null;
        Label ExpLable1_5 = null;
        if (handler.getMainDataset().getPublicationLink().equalsIgnoreCase("NOT AVAILABLE") || handler.getMainDataset().getPublicationLink().equalsIgnoreCase("")) {
            ExpLable1_5 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>No Publication Link Available </strong></h5>");
            ExpLable1_5.setHeight("45px");
        } else {
            href = handler.getMainDataset().getPublicationLink().toLowerCase();
            if ((!href.contains("http://")) && (!href.contains("https://"))) {
                href = "http://" + href;
            }
            ExpLable1_5 = new Label("<h5><a href='" + href + "'  target='_blank'>Publication Link</a></h5>");
            ExpLable1_5.setHeight("45px");

        }

        ExpLable1_5.setContentMode(ContentMode.HTML);
        l1.addComponent(ExpLable1_1);
        if (handler.getMainDataset().getDescription().length() <= 100) {
            Label ExpLable2_1 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Description:</strong><br/>" + handler.getMainDataset().getDescription() + "</h5>");
            ExpLable2_1.setContentMode(ContentMode.HTML);
            ExpLable2_1.setHeight("45px");
            l1.addComponent(ExpLable2_1);
        } else {
            Label ExpLable2_1 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Description:</strong></h5>");
            ExpLable2_1.setContentMode(ContentMode.HTML);
            ExpLable2_1.setHeight("30px");

            Label ExpLable2_2 = new Label("<h5 style='font-family:verdana;color:gray;'>" + handler.getMainDataset().getDescription() + "</h5>");
            ExpLable2_2.setContentMode(ContentMode.HTML);

            VerticalLayout lTemp = new VerticalLayout();
            lTemp.addComponent(ExpLable2_2);
            lTemp.setMargin(true);
            ExpLable2_2.setSizeFull();
            Panel p = new Panel();
            p.setContent(lTemp);
            p.setWidth("80%");
            p.setHeight("80px");
            p.setScrollTop(20);
            p.setScrollLeft(50);

            p.setStyleName(Runo.PANEL_LIGHT);

            l1.addComponent(ExpLable2_1);
            l1.addComponent(p);
        }

        VerticalLayout l2 = new VerticalLayout();

        Label ExpLable2_3 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Instrument Type:</strong><br/>" + handler.getMainDataset().getInstrumentType() + "</h5>");
        ExpLable2_3.setContentMode(ContentMode.HTML);
        ExpLable2_3.setHeight("45px");

        Label ExpLable2_4 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Frag Mode:</strong><br/>" + handler.getMainDataset().getFragMode() + "</h5>");
        ExpLable2_4.setContentMode(ContentMode.HTML);
        ExpLable2_4.setHeight("45px");
        Label ExpLable2_5 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Uploaded By:</strong><br/>" + handler.getMainDataset().getUploadedByName() + "</h5>");
        ExpLable2_5.setContentMode(ContentMode.HTML);
        ExpLable2_5.setHeight("45px");

        Label ExpLable2_6 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'>Email:</strong><br/>" + handler.getMainDataset().getEmail() + "</h5>");
        ExpLable2_6.setContentMode(ContentMode.HTML);
        ExpLable2_6.setHeight("45px");

        Label ExpLable2_7 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'># Fractions</strong><br/>" + handler.getMainDataset().getFractionsNumber() + "</h5>");
        ExpLable2_7.setContentMode(ContentMode.HTML);
        ExpLable2_7.setHeight("45px");
        Label ExpLable2_8 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'># Protein Groups:</strong><br/>" + handler.getMainDataset().getNumberValidProt()/* handler.getMainDataset().getProteinsNumber()*/ + "</h5>");
        ExpLable2_8.setContentMode(ContentMode.HTML);
        ExpLable2_8.setDescription("Number of validated proteins");
        ExpLable2_8.setHeight("45px");

        HorizontalLayout pepHlo = new HorizontalLayout();
        Label ExpLable2_9 = new Label("<h5  style='font-family:verdana;color:gray;'><strong style='font-family:verdana;color:#424242;'># Peptides</strong><br/>" + handler.getMainDataset().getPeptidesNumber() + "</h5>");
        ExpLable2_9.setContentMode(ContentMode.HTML);
        ExpLable2_9.setDescription("Number of validated peptides");
        ExpLable2_9.setHeight("45px");

        HorizontalLayout expIcon = excelExporterIconGenerator.getExpIcon(new CustomExportBtnLayout(handler, "allPep", handler.getMainDataset().getDatasetId(), handler.getMainDataset().getName(), null, null, null, 0, null, null, null, null), "Export All Peptides for " + handler.getMainDataset().getName(), "");

        pepHlo.addComponent(ExpLable2_9);
        pepHlo.addComponent(expIcon);

        l1.addComponent(ExpLable2_3);

        l2.addComponent(ExpLable1_2);
        l2.addComponent(ExpLable2_4);
        l2.addComponent(ExpLable1_4);

        l2.addComponent(ExpLable1_3);
        l2.addComponent(pepHlo);
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

        if (mainComponentBodyLayout.isVisible()) {
            this.hideDetails();

        } else {

            this.showDetails();
        }

    }

    private void showDetails() {
        show.updateIcon(true);
        mainComponentBodyLayout.setVisible(true);
    }
    public final void hideDetails() {
        show.updateIcon(false);
        mainComponentBodyLayout.setVisible(false);
    }
    public boolean isVisability() {
        return mainComponentBodyLayout.isVisible();
    }

    public void setVisability(boolean test) {
        if (test) {
            this.showDetails();
        } else {
            this.hideDetails();
        }
    }
}
