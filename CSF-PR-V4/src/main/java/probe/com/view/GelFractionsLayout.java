/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.Align;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.ShowLabel;

/**
 *
 * @author Yehia Farag
 */
public class GelFractionsLayout extends VerticalLayout implements Serializable {

    private VerticalLayout mainLayout;
    private ShowLabel show;
    private boolean stat;
    private final VerticalLayout exportFracLayout = new VerticalLayout();
    private final PopupView expBtnFracTable;

    public GelFractionsLayout(final String accession, final double mw, Map<Integer, ProteinBean> proteinFractionAvgList, List<StandardProteinBean> standardProtPlotList, final String datasetName) {
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
        fractionLabel.setContentMode(ContentMode.HTML);
        fractionLabel.setHeight("45px");
        clickableheaderLayout.addComponent(fractionLabel);
        clickableheaderLayout.setComponentAlignment(fractionLabel, Alignment.TOP_RIGHT);
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
        lowerLayout.setSpacing(false);

        mainLayout.addComponent(lowerLayout);
        mainLayout.setComponentAlignment(lowerLayout, Alignment.TOP_CENTER);

        exportFracLayout.setWidth("300px");
        lowerLayout.addComponent(exportFracLayout);
        lowerLayout.setComponentAlignment(exportFracLayout, Alignment.MIDDLE_RIGHT);
        lowerLayout.setExpandRatio(exportFracLayout, 0.1f);
        final Table fractTable = getFractionTable(proteinFractionAvgList);
        fractTable.setVisible(false);
        this.addComponent(fractTable);
        expBtnFracTable = new PopupView("Export Fractions from Selected Dataset for ( " + accession + " )", new CustomExportBtnLayout(null, "fractions", 0, datasetName, accession, accession, null, 0, null, fractTable, null, null));
        expBtnFracTable.setDescription("Export Fractions from ( " + datasetName + " ) Dataset for ( " + accession + " )");
        exportFracLayout.addComponent(expBtnFracTable);
        exportFracLayout.setMargin(new MarginInfo(false, true, false, false));
        exportFracLayout.setComponentAlignment(expBtnFracTable, Alignment.BOTTOM_RIGHT);

        clickableheaderLayout.addLayoutClickListener(new com.vaadin.event.LayoutEvents.LayoutClickListener() {
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

    private Table getFractionTable(Map<Integer, ProteinBean> proteinFractionAvgList) {
        Table table = new Table();
        table.setStyleName(Reindeer.TABLE_STRONG + " " + Reindeer.TABLE_BORDERLESS);
        table.setHeight("150px");
        table.setWidth("100%");
        table.setSelectable(true);
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);
        table.setImmediate(true); // react at once when something is selected
        table.addContainerProperty("Fraction Index", Integer.class, null, "Fraction Index", null, Align.CENTER );
        table.addContainerProperty("# Peptides ", Integer.class, null, "# Peptides ", null, Align.CENTER );
        table.addContainerProperty("# Spectra ", Integer.class, null, "# Spectra", null, Align.CENTER );
        table.addContainerProperty("Average Precursor Intensity", Double.class, null, "Average Precursor Intensity", null, com.vaadin.ui.Table.ALIGN_CENTER);
        /* Add a few items in the table. */
        int x = 0;
        for (int index : proteinFractionAvgList.keySet()) {
            ProteinBean pb = proteinFractionAvgList.get(index);
            table.addItem(new Object[]{index, pb.getNumberOfPeptidePerFraction(), pb.getNumberOfSpectraPerFraction(), pb.getAveragePrecursorIntensityPerFraction()}, x + 1);
            x++;
       
            
        }
        for (Object propertyId : table.getSortableContainerPropertyIds()) {
            table.setColumnExpandRatio(propertyId.toString(), 1.0f);
        }

        return table;
    }

}
