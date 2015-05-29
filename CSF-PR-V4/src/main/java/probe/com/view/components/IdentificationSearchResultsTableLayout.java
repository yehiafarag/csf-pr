/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.data.Property;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.Map;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.DatasetDetailsBean;
import probe.com.model.beans.IdentificationProteinBean;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.TableResizeSet;

/**
 *
 * @author Yehia Farag
 */
public class IdentificationSearchResultsTableLayout extends VerticalLayout implements Serializable {

    private IdentificationSearchResultsTable searcheResultsTable, currentTable, validatedProteinsTable;
    private VerticalLayout searchResultsTableLayout = new VerticalLayout();
    private final VerticalLayout exportAllPepLayout = new VerticalLayout();
    private PopupView expBtnProtAllPepTable;
    private final VerticalLayout exportSearchTableLayout = new VerticalLayout();
    private final PopupView expBtnSearchResultsTable;
    private Property.ValueChangeListener listener;
    private Label searchResultstLabel = new Label();

    public IdentificationSearchResultsTableLayout(MainHandler handler, final Map<Integer, DatasetDetailsBean> datasetDetailsList, final Map<Integer, IdentificationProteinBean> fullExpProtList, boolean validatedOnly) {

        this.setWidth("100%");
        this.setSpacing(true);
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        searchResultstLabel.setContentMode(ContentMode.HTML);
        searchResultstLabel.setHeight("30px");
        this.addComponent(searchResultstLabel);
        this.addComponent(searchResultsTableLayout);
        this.setComponentAlignment(searchResultsTableLayout, Alignment.MIDDLE_CENTER);

        final Map<Integer, IdentificationProteinBean> vProteinsList = handler.getValidatedProteinsList(fullExpProtList);
        searchResultstLabel.setValue("<h4 style='font-family:verdana;color:black;'> Search Results (" + vProteinsList.size() + ")</h4>");
        searcheResultsTable = new IdentificationSearchResultsTable(datasetDetailsList, vProteinsList);
        searchResultsTableLayout.addComponent(searcheResultsTable);
        currentTable = searcheResultsTable;

        HorizontalLayout lowerLayout = new HorizontalLayout();
        lowerLayout.setWidth("100%");
        lowerLayout.setHeight("25px");
        this.addComponent(lowerLayout);
        this.setComponentAlignment(lowerLayout, Alignment.TOP_CENTER);

        HorizontalLayout lowerLeftLayout = new HorizontalLayout();
        lowerLeftLayout.setSpacing(true);
        lowerLayout.addComponent(lowerLeftLayout);
        lowerLeftLayout.setMargin(new MarginInfo(false, false, false, false));
        lowerLayout.setComponentAlignment(lowerLeftLayout, Alignment.MIDDLE_LEFT);

        HorizontalLayout lowerRightLayout = new HorizontalLayout();
        lowerRightLayout.setSpacing(true);
        lowerLayout.addComponent(lowerRightLayout);
        lowerLayout.setComponentAlignment(lowerRightLayout, Alignment.BOTTOM_RIGHT);
        final OptionGroup selectionType = new OptionGroup();
        selectionType.setMultiSelect(true);
        Object itemId = selectionType.addItem("\t\tShow Validated Proteins Only");
        selectionType.select("\t\tShow Validated Proteins Only");
        selectionType.setReadOnly(validatedOnly);

        selectionType.setHeight("15px");
        lowerLeftLayout.addComponent(selectionType);
        lowerLeftLayout.setComponentAlignment(selectionType, Alignment.BOTTOM_LEFT);

        final TableResizeSet trs1 = new TableResizeSet(currentTable, currentTable.getHeight() + "");//resize tables 
        lowerLeftLayout.addComponent(trs1);
        lowerLeftLayout.setComponentAlignment(trs1, Alignment.BOTTOM_CENTER);

        exportAllPepLayout.setWidth("300px");
        lowerRightLayout.addComponent(exportAllPepLayout);
        lowerRightLayout.setComponentAlignment(exportAllPepLayout, Alignment.BOTTOM_RIGHT);
        exportAllPepLayout.setVisible(true);

        exportSearchTableLayout.setWidth("200px");
        lowerRightLayout.addComponent(exportSearchTableLayout);
        lowerRightLayout.setComponentAlignment(exportSearchTableLayout, Alignment.BOTTOM_RIGHT);

        CustomExportBtnLayout ce2 = new CustomExportBtnLayout(handler, "searchResult", 0, null, null, null, null, 0, null, null, fullExpProtList, null);
        expBtnSearchResultsTable = new PopupView("Export CSF-PR Search Results", ce2);
        exportSearchTableLayout.removeAllComponents();
        expBtnSearchResultsTable.setHideOnMouseOut(false);
        exportSearchTableLayout.addComponent(expBtnSearchResultsTable);
        expBtnSearchResultsTable.setDescription("Export CSF-PR Search Results");
        exportSearchTableLayout.setComponentAlignment(expBtnSearchResultsTable, Alignment.MIDDLE_LEFT);

        selectionType.setImmediate(true);
        selectionType.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (!selectionType.isSelected("\t\tShow Validated Proteins Only")) {
                    searchResultstLabel.setValue("<h4 style='font-family:verdana;color:black;'> Search Results (" + vProteinsList.size() + "/" + fullExpProtList.size() + ")</h4>");
                    searchResultsTableLayout.removeAllComponents();
                    validatedProteinsTable = new IdentificationSearchResultsTable(datasetDetailsList, fullExpProtList);
                    searchResultsTableLayout.addComponent(validatedProteinsTable);
                    trs1.setTable(validatedProteinsTable);
                    validatedProteinsTable.setHeight(getSearchTable().getHeight() + "");
                    getSearchTable().removeValueChangeListener(getListener());
                    validatedProteinsTable.addValueChangeListener(getListener());
                    currentTable = validatedProteinsTable;
                } else {
                    searchResultstLabel.setValue("<h4 style='font-family:verdana;color:black;'> Search Results (" + vProteinsList.size() + ")</h4>");

                    searchResultsTableLayout.removeAllComponents();
                    searchResultsTableLayout.addComponent(searcheResultsTable);
                    trs1.setTable(searcheResultsTable);
                    currentTable = searcheResultsTable;
                    validatedProteinsTable.removeValueChangeListener(getListener());
                    searcheResultsTable.addValueChangeListener(getListener());
                }
            }

        });
        if (validatedOnly) {
            selectionType.select(itemId);
            selectionType.setVisible(true);
            selectionType.setReadOnly(true);

        } else {
            selectionType.setVisible(true);
            selectionType.setReadOnly(false);
        }

    }

    public Property.ValueChangeListener getListener() {
        return listener;
    }

    public void setListener(Property.ValueChangeListener listener) {
        this.listener = listener;
    }

    public PopupView getExpBtnProtAllPepTable() {
        return expBtnProtAllPepTable;
    }

    public void setExpBtnProtAllPepTable(PopupView expBtnProtAllPepTable) {
        this.expBtnProtAllPepTable = expBtnProtAllPepTable;
        updateExportLayouts();

    }

    private void updateExportLayouts() {
        exportAllPepLayout.removeAllComponents();
        expBtnProtAllPepTable.setHideOnMouseOut(false);
        exportAllPepLayout.addComponent(expBtnProtAllPepTable);
        exportAllPepLayout.setComponentAlignment(expBtnProtAllPepTable, Alignment.MIDDLE_LEFT);
    }

    public IdentificationSearchResultsTable getSearchTable() {
        return currentTable;
    }

}
