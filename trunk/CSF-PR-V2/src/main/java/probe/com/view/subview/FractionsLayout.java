/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview;

import com.vaadin.event.LayoutEvents;
import com.vaadin.server.ExternalResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.subview.util.CustomExportBtnLayout;
import probe.com.view.subview.util.CustomPI;
import probe.com.view.subview.util.Help;
import probe.com.view.subview.util.ShowLabel;

/**
 *
 * @author Yehia Farag
 */
public class FractionsLayout extends VerticalLayout implements Serializable {

    private VerticalLayout mainLayout;
    private ShowLabel show;
    private boolean stat;
    private VerticalLayout exportFracLayout = new VerticalLayout();
    private PopupView expBtnFracTable;

    public FractionsLayout(final String accession, final double mw, Map<Integer, ProteinBean> proteinFractionAvgList, List<StandardProteinBean> standardProtPlotList, final String expName) {
        this.setSpacing(false);
        this.setWidth("100%");
        this.setMargin(new MarginInfo(false, false, false, true));

        final HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setHeight("45px");
        headerLayout.setSpacing(true);
        
        final HorizontalLayout clickableheaderLayout = new HorizontalLayout();
        clickableheaderLayout.setHeight("45px");
        clickableheaderLayout.setSpacing(true);
        headerLayout.addComponent(clickableheaderLayout);
        headerLayout.setComponentAlignment(clickableheaderLayout, Alignment.BOTTOM_LEFT);
        
        
        
        show = new ShowLabel(true);
        clickableheaderLayout.addComponent(show);
        clickableheaderLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);

        stat = true;

        Label fractionLabel = new Label("<h4 style='font-family:verdana;color:black;'>Fractions (Protein: " + accession + "  MW: " + mw + " kDa)</h4>");
        fractionLabel.setContentMode(Label.CONTENT_XHTML);
        fractionLabel.setHeight("45px");
        clickableheaderLayout.addComponent(fractionLabel);
        clickableheaderLayout.setComponentAlignment(fractionLabel, Alignment.TOP_RIGHT);


//        Label infoLable = new Label("<center style='background-color:#E6E6FA;'><p  style='background-color:#E6E6FA;font-family:verdana;color:black;font-weight:bold;'>Bar charts showing the distribution of the protein in the fractions cut from the gel.<br/>Three charts show number of peptides, number of spectra and average precursor intensity.<br/>The fraction number represents the gel pieces cut from top to bottom.<br/>Protein standards (dark blue bars) indicate the molecular weight range of each fraction. Darker blue bars mark the area where the protein's theoretical mass suggests the protein should occur. </p></center>");
//        infoLable.setContentMode(Label.CONTENT_XHTML);
//        infoLable.setWidth("300px");
//        infoLable.setStyleName(Reindeer.LAYOUT_BLUE);
//
//        Help help = new Help();
//        HorizontalLayout infoIco = help.getInfoNote(infoLable);
//        infoIco.setMargin(new MarginInfo(false, false, false, true));
//        headerLayout.addComponent(infoIco);
//        headerLayout.setComponentAlignment(infoIco, Alignment.MIDDLE_LEFT);


        this.addComponent(headerLayout);

        mainLayout = new VerticalLayout();
        this.addComponent(mainLayout);

        FractionPlotLayout plotsLayout = new FractionPlotLayout(proteinFractionAvgList, mw, standardProtPlotList);
        mainLayout.addComponent(plotsLayout);
        mainLayout.setComponentAlignment(plotsLayout, Alignment.MIDDLE_CENTER);



        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.setWidth("100%");
        lowerLayout.setHeight("25px");
        lowerLayout.setMargin((new MarginInfo(false, true, false, true)));
        lowerLayout.setSpacing(true);

        Panel toolbar = new Panel(lowerLayout);
        toolbar.setStyleName(Reindeer.PANEL_LIGHT);
        toolbar.setHeight("35px");
        toolbar.setWidth("100%");
        mainLayout.addComponent(toolbar);
        mainLayout.setComponentAlignment(toolbar, Alignment.TOP_CENTER);

        exportFracLayout.setWidth("100px");
        lowerLayout.addComponent(exportFracLayout);
        lowerLayout.setComponentAlignment(exportFracLayout, Alignment.MIDDLE_RIGHT);
        lowerLayout.setExpandRatio(exportFracLayout, 0.1f);
        final Table fractTable = getFractionTable(proteinFractionAvgList);
        fractTable.setVisible(false);
        this.addComponent(fractTable);
        expBtnFracTable = new PopupView("Export Fractions", new CustomExportBtnLayout(null, "fractions", 0, expName, accession, accession, null, null, 0, null, fractTable, null));

        exportFracLayout.addComponent(expBtnFracTable);
        exportFracLayout.setMargin(new MarginInfo(false, true, false, false));
        exportFracLayout.setComponentAlignment(expBtnFracTable, Alignment.BOTTOM_CENTER);


        clickableheaderLayout.addListener(  new com.vaadin.event.LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {

                if (stat) {
                    stat = false;
                    show.updateIcon(false);
                    mainLayout.setVisible(false);
                } else {
                    stat = true;
                    show.updateIcon(true);
                    mainLayout.setVisible(true);
                }
            }
        });
        
        
        


    }

    @SuppressWarnings("deprecation")
    private Table getFractionTable(Map<Integer, ProteinBean> proteinFractionAvgList) {
        Table table = new Table();
        table.setStyleName(Reindeer.TABLE_STRONG + " " + Reindeer.TABLE_BORDERLESS);
        table.setHeight("150px");
        table.setWidth("100%");
        table.setSelectable(true);
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);
        table.setImmediate(true); // react at once when something is selected
        table.addContainerProperty("Fraction Index", Integer.class, null, "Fraction Index", null, com.vaadin.ui.Table.ALIGN_CENTER);
        table.addContainerProperty("# Peptides ", Integer.class, null, "# Peptides ", null, com.vaadin.ui.Table.ALIGN_CENTER);
        table.addContainerProperty("# Spectra ", Integer.class, null, "# Spectra", null, com.vaadin.ui.Table.ALIGN_CENTER);
        table.addContainerProperty("Average Precursor Intensity", Double.class, null, "Average Precursor Intensity", null, com.vaadin.ui.Table.ALIGN_CENTER);
        /* Add a few items in the table. */
        int x = 0;
        for (int index : proteinFractionAvgList.keySet()) {
            ProteinBean pb = proteinFractionAvgList.get(index);
            table.addItem(new Object[]{new Integer(index), pb.getNumberOfPeptidePerFraction(), pb.getNumberOfSpectraPerFraction(), pb.getAveragePrecursorIntensityPerFraction()}, new Integer(x + 1));
            x++;
        }
        for (Object propertyId : table.getSortableContainerPropertyIds()) {
            table.setColumnExpandRatio(propertyId.toString(), 1.0f);
        }

        return table;
    }

//    @SuppressWarnings("deprecation")
//    private Table getStandardPlotTable(Map<String, List<StandardProteinBean>> standProtGroups) {
//        Table table = new Table();
//        table.setStyleName(Reindeer.TABLE_BORDERLESS);
//        table.setHeight("240px");
//        table.setWidth("100%");
//        table.setSelectable(false);
//        table.setColumnReorderingAllowed(true);
//        table.setColumnCollapsingAllowed(true);
//        table.setImmediate(true); // react at once when something is selected
//        table.addContainerProperty("Col", CustomPI.class, null, "", null, com.vaadin.ui.Table.ALIGN_CENTER);
//
//        table.addContainerProperty("Protein", String.class, null, "Protein", null, com.vaadin.ui.Table.ALIGN_CENTER);
//        table.addContainerProperty("MW", Double.class, null, "MW", null, com.vaadin.ui.Table.ALIGN_CENTER);
//        /* Add a few items in the table. */
//        int x = 0;
//        for (String key : standProtGroups.keySet()) {
//            CustomPI ce = null;
//            if (key.equalsIgnoreCase("#79AFFF")) {
//                List<StandardProteinBean> lsp = standProtGroups.get(key);
//                for (StandardProteinBean spb : lsp) {
//                    ce = new CustomPI("Selected Standard Plot", new ExternalResource("https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-prn1/488024_137541363096131_1104414259_n.jpg"));
//                    table.addItem(new Object[]{ce, spb.getName(), spb.getMW_kDa()}, new Integer(x + 1));
//                    x++;
//                }
//            } else {
//                List<StandardProteinBean> lsp = standProtGroups.get(key);
//                for (StandardProteinBean spb : lsp) {
//                    ce = new CustomPI("Standard Plot", new ExternalResource("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-ash3/528295_137541356429465_212869913_n.jpg"));
//                    table.addItem(new Object[]{ce, spb.getName(), spb.getMW_kDa()}, new Integer(x + 1));
//                    x++;
//                }
//            }
//
//        }
//        for (Object propertyId : table.getSortableContainerPropertyIds()) {
//            if (propertyId.toString().equals("Protein")) {
//                table.setColumnExpandRatio(propertyId.toString(), 2.5f);
//            } else {
//                table.setColumnExpandRatio(propertyId.toString(), 1.0f);
//            }
//        }
//        table.setSortContainerPropertyId("MW");
//        table.setSortAscending(false);
//        return table;
//    }
}
