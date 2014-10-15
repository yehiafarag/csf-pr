/*
 */
package probe.com.view;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Select;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.view.components.SearchResultsTableLayout;
import probe.com.view.core.CustomErrorLabel;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.IconGenerator;

/**
 *
 * @author Yehia Farag
 */
public class SearchLayout extends VerticalLayout implements Serializable, Button.ClickListener {

    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final VerticalLayout searchLayout = new VerticalLayout();
    private final HorizontalLayout topRightLayout = new HorizontalLayout();
    private final VerticalLayout topLeftLayout = new VerticalLayout();
    private TextArea searchField;
    private Select selectDatasetDropdownList; //select dataset the search method
    private OptionGroup searchbyGroup;
    private final Button searchButton = new Button("");
    private final MainHandler handler;
    private String defaultText = "Please use one key-word per line and choose the search options";
    private final String selectDatasetStr = "Search All Datasets";
    private Label searchByLabel, errorLabelI;// = new  Label();
    private CustomErrorLabel errorLabelII;
    private final String selectMethodStr = "Please Select Search Method";
    private final TreeMap<Integer, String> datasetNamesList;

    private final VerticalLayout searchTableLayout = new VerticalLayout();
    private final VerticalLayout protSerarchLayout = new VerticalLayout();
    private final VerticalLayout peptidesLayout = new VerticalLayout();
    private final VerticalLayout fractionsLayout = new VerticalLayout();
    private CustomExternalLink searchTableAccessionLable;
    private int key = -1;
    private String accession;
    private String otherAccession, desc;
    private PeptidesTableLayout peptideTableLayout;
    private Map<Integer, FractionBean> fractionsList = null;
    private OptionGroup validatedResults = new OptionGroup();
    private final HorizontalLayout searchPropLayout = new HorizontalLayout();
    private boolean validatedOnly = true;

    /**
     *
     * @param handler dataset handler
     */
    public SearchLayout(MainHandler handler) {
        this.handler = handler;
        this.datasetNamesList = handler.getDatasetNamesList();
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(topLayout);
        this.addComponent(searchLayout);
        topLayout.addComponent(topLeftLayout);
        topLayout.addComponent(topRightLayout);
        topLeftLayout.setSpacing(true);
        topLeftLayout.setMargin(true);
        topRightLayout.setSpacing(true);
        topRightLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.setMargin(new MarginInfo(false, false, true, false));

        searchLayout.addComponent(searchTableLayout);
        searchTableLayout.setWidth("100%");
        searchLayout.addComponent(protSerarchLayout);

        protSerarchLayout.addComponent(fractionsLayout);
        protSerarchLayout.addComponent(peptidesLayout);

        fractionsLayout.setWidth("100%");
        peptidesLayout.setWidth("100%");

        searchLayout.setVisible(false);
        buildMainLayout();

    }

    /**
     * initialize main search tab layout initialize drop down select dataset and
     * different filters
     */
    private void buildMainLayout() {
        topRightLayout.removeAllComponents();
        topLeftLayout.removeAllComponents();

        //search form layout
        searchField = new TextArea();
        searchField.setValue(defaultText);
        searchField.setWidth("350px");
        searchField.setImmediate(true);
        searchField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        searchField.addFocusListener(new FieldEvents.FocusListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            @Override
            public void focus(FieldEvents.FocusEvent event) {
                if (defaultText.equals("Please use one key-word per line and choose the search options")) {
                    searchField.setValue("");
                }

            }
        });
        topLeftLayout.addComponent(searchField);

        //select dataset for searching
        selectDatasetDropdownList = new Select();
        selectDatasetDropdownList.addItem(selectDatasetStr);
        selectDatasetDropdownList.setValue(selectDatasetStr);
        selectDatasetDropdownList.setNullSelectionAllowed(false);
        selectDatasetDropdownList.setValue(selectDatasetStr);
        for (String str : datasetNamesList.values()) {
            selectDatasetDropdownList.addItem(str);
        }
        selectDatasetDropdownList.setImmediate(true);
        selectDatasetDropdownList.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                searchButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            }
        });
        selectDatasetDropdownList.setNullSelectionAllowed(false);
        selectDatasetDropdownList.setWidth("350px");
        topLeftLayout.addComponent(selectDatasetDropdownList);

        searchByLabel = new Label("<h4 style='font-family:verdana;color:black;'>Search By:</h4>");
        searchByLabel.setContentMode(ContentMode.HTML);
        searchByLabel.setHeight("30px");
        searchByLabel.setWidth("350px");
        topLeftLayout.addComponent(searchByLabel);

        topLeftLayout.addComponent(searchPropLayout);
        searchPropLayout.setWidth("300px");
        searchbyGroup = new OptionGroup();
        searchbyGroup.setWidth("350px");
        searchbyGroup.setDescription(selectMethodStr);
        // Use the single selection mode.
        searchbyGroup.setMultiSelect(false);
        searchbyGroup.addItem("Protein Accession");
        searchbyGroup.addItem("Protein Name");
        searchbyGroup.addItem("Peptide Sequence");
        searchbyGroup.select("Protein Accession");
        searchPropLayout.addComponent(searchbyGroup);

        validatedResults = new OptionGroup();
        validatedResults.setMultiSelect(true);
        validatedResults.addItem("Validated Proteins Only");
        validatedResults.select("Validated Proteins Only");
        validatedResults.setHeight("15px");
        searchPropLayout.addComponent(validatedResults);
        searchPropLayout.setComponentAlignment(validatedResults, Alignment.MIDDLE_RIGHT);

        validatedResults.setImmediate(true);
        validatedResults.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                validatedOnly = validatedResults.isSelected("Validated Proteins Only");
            }
        });

        //topright layout
        topRightLayout.setWidth("100%");
        searchButton.setStyleName(Reindeer.BUTTON_LINK);
        searchButton.setIcon(new ThemeResource("img/search_22.png"));
        topRightLayout.addComponent(searchButton);
        topRightLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_LEFT);
        topRightLayout.setExpandRatio(searchButton, 0.9f);
        topRightLayout.setMargin(new MarginInfo(true, true, true, false));
        Label infoLable = new Label("<div style='border:1px outset black;text-align:justify;text-justify:inter-word;'><h3 style='font-family:verdana;color:black;font-weight:bold;margin-left:20px;margin-right:20px;'>Information</h3><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Type in search keywords (one per line) and choose the search type. All experiments containing protein(s) where the keyword is found are listed. View the information about each protein from each experiment separately by selecting them from the list.</p></div>");
        infoLable.setContentMode(ContentMode.HTML);
        infoLable.setWidth("300px");
        infoLable.setStyleName(Reindeer.LAYOUT_BLUE);

        //errorLabelI error in search keyword 
        errorLabelI = new Label("<h3 Style='color:red;'>Please Enter Valid Key Word </h3>");
        errorLabelI.setContentMode(ContentMode.HTML);

        topRightLayout.addComponent(errorLabelI);
        topRightLayout.setComponentAlignment(errorLabelI, Alignment.MIDDLE_CENTER);
        errorLabelI.setVisible(false);

        topRightLayout.setExpandRatio(errorLabelI, 0.1f);

        errorLabelII = new CustomErrorLabel();
        topRightLayout.addComponent(errorLabelII);
        topRightLayout.setComponentAlignment(errorLabelII, Alignment.MIDDLE_CENTER);
        topRightLayout.setExpandRatio(errorLabelII, 0.1f);

        IconGenerator help = new IconGenerator();
        HorizontalLayout infoIco = help.getInfoNote(infoLable);
        infoIco.setMargin(new MarginInfo(false, true, false, true));
        topRightLayout.addComponent(infoIco);
        topRightLayout.setComponentAlignment(infoIco, Alignment.MIDDLE_RIGHT);
        errorLabelII.setVisible(false);
        searchButton.addClickListener(this);

    }

    /**
     * on click validate search inputs and start searching process
     *
     * @param event user search click buttons
     */
    @Override
    public void buttonClick(Button.ClickEvent event) {

        searchLayout.setVisible(false);
        searchTableLayout.removeAllComponents();
        peptidesLayout.removeAllComponents();
        fractionsLayout.removeAllComponents();

        errorLabelI.setVisible(false);
        errorLabelII.setVisible(false);
        Object searchDatasetTypeObject = selectDatasetDropdownList.getValue();
        Object protSearchObject = searchField.getValue();

        if (protSearchObject == null || protSearchObject.toString().equals("") || protSearchObject.toString().length() < 4 || protSearchObject.toString().equals("Please use one key-word per line and choose the search options")) {
            errorLabelI.setVisible(true);
            searchField.focus();
        } else {

            String searchDatasetType = searchDatasetTypeObject.toString().trim();
            String protSearch = protSearchObject.toString().trim().toUpperCase();
            String searchMethod = searchbyGroup.getValue().toString();

            defaultText = protSearch;
            searchField.setValue(defaultText);

            String[] searchArr = protSearch.split("\n");
            Set<String> searchSet = new HashSet<String>();
            for (String str : searchArr) {
                searchSet.add(str.trim());
            }
            String notFound = "";
            Map<Integer, ProteinBean> searchProtList = null;
            Map<Integer, ProteinBean> fullExpProtList = new HashMap<Integer, ProteinBean>();

            //start searching process
            if (searchMethod.equals("Protein Accession"))//case of protein accession
            {
                for (String searchStr : searchSet) {
                    searchProtList = handler.searchProteinByAccession(searchStr.trim(), searchDatasetType, validatedOnly);
                    if (searchProtList == null || searchProtList.isEmpty()) {
                        notFound += searchStr + "\t";
                    } else {
                        fullExpProtList.putAll(searchProtList);
                    }

                }

            } else if (searchMethod.equals("Protein Name")) //case of protein name
            {
                for (String searchStr : searchSet) {
                    searchProtList = handler.searchProteinByName(searchStr.trim(), searchDatasetType, validatedOnly);
                    if (searchProtList == null || searchProtList.isEmpty()) {
                        notFound += searchStr + "\t";
                    } else {
                        fullExpProtList.putAll(searchProtList);
                    }

                }
            } else //find protein by peptide sequence
            {

                for (String searchStr : searchSet) {
                    searchProtList = handler.searchProteinByPeptideSequence(searchStr.trim(), searchDatasetType, validatedOnly);
                    if (searchProtList == null || searchProtList.isEmpty()) {
                        notFound += searchStr + "\t";
                    } else {
                        fullExpProtList.putAll(searchProtList);
                    }

                }

            }
            //searching process  end here
            if (!notFound.equals("")) {
                notFind(notFound);
            }
            if (fullExpProtList.isEmpty()) {
                searchLayout.setVisible(false);
            } else {
                searchLayout.setVisible(true);
                final SearchResultsTableLayout searcheResultsTableLayout = new SearchResultsTableLayout(handler, handler.getDatasetDetailsList(), fullExpProtList, validatedOnly);
                searchTableLayout.addComponent(searcheResultsTableLayout);
                Property.ValueChangeListener listener = new Property.ValueChangeListener() {
                    /*
                     *the main listener for search table
                     */
                    private static final long serialVersionUID = 1L;

                    /**
                     * on select search table value initialize the peptides
                     * table and fractions plots if exist * process
                     *
                     * @param event value change on search table selection
                     */
                    @Override
                    public synchronized void valueChange(Property.ValueChangeEvent event) {

                        if (searchTableAccessionLable != null) {
                            searchTableAccessionLable.rePaintLable("black");
                        }

                        if (searcheResultsTableLayout.getSearchTable().getValue() != null) {
                            key = (Integer) searcheResultsTableLayout.getSearchTable().getValue();
                        }
                        final Item item = searcheResultsTableLayout.getSearchTable().getItem(key);
                        searchTableAccessionLable = (CustomExternalLink) item.getItemProperty("Accession").getValue();
                        searchTableAccessionLable.rePaintLable("white");
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException iexp) {
                            System.out.println(iexp.getLocalizedMessage());
                        }
                        peptidesLayout.removeAllComponents();
                        fractionsLayout.removeAllComponents();

                        String datasetName = item.getItemProperty("Experiment").toString();
                        accession = item.getItemProperty("Accession").toString();
                        otherAccession = item.getItemProperty("Other Protein(s)").toString();
                        desc = item.getItemProperty("Description").toString();

                        handler.setMainDataset(datasetName);
                        if (handler.getMainDataset() != null && handler.getMainDataset().getDatasetType() == 1) {
                            handler.retriveProteinsList(handler.getMainDataset().getDatasetId());
                            CustomExportBtnLayout exportAllProteinPeptidesLayout = new CustomExportBtnLayout(handler, "allProtPep", handler.getMainDataset().getDatasetId(), datasetName, accession, otherAccession, null, 0, null, null, null, desc);
                            PopupView exportAllProteinPeptidesPopup = new PopupView("Export Peptides from All Datasets for (" + accession + " )", exportAllProteinPeptidesLayout);
                            exportAllProteinPeptidesPopup.setDescription("Export CSF-PR Peptides for ( " + accession + " ) for All Available Datasets");
                            searcheResultsTableLayout.setExpBtnProtAllPepTable(exportAllProteinPeptidesPopup);// new PopupView("Export Proteins", (new CustomExportBtnLayout(handler, "prots",datasetId, datasetName, accession, otherAccession, datasetList, proteinsList, dataset.getFractionsNumber(), null,null))));
                            if (key >= 0) {

                                Map<Integer, PeptideBean> pepProtList = handler.getPeptidesProtList(handler.getMainDataset().getDatasetId(), accession, otherAccession);
                                if (handler.getMainDataset().getPeptideList() == null) {
                                    handler.getMainDataset().setPeptideList(pepProtList);
                                } else {
                                    handler.getMainDataset().getPeptideList().putAll(pepProtList);
                                }
                                if (!pepProtList.isEmpty()) {
                                    int validPep = handler.getValidatedPepNumber(pepProtList);
                                    if (peptideTableLayout != null) {
                                        peptidesLayout.removeComponent(peptideTableLayout);
                                    }
                                    peptideTableLayout = new PeptidesTableLayout(validPep, pepProtList.size(), desc, pepProtList, accession, handler.getMainDataset().getName());
                                    peptidesLayout.setMargin(false);
                                    peptidesLayout.addComponent(peptideTableLayout);
                                    CustomExportBtnLayout exportAllProteinsPeptidesLayout = new CustomExportBtnLayout(handler, "protPep", handler.getMainDataset().getDatasetId(), handler.getMainDataset().getName(), accession, otherAccession, null, 0, pepProtList, null, null, desc);
                                    PopupView exportAllProteinsPeptidesPopup = new PopupView("Export Peptides from Selected Dataset for (" + accession + " )", exportAllProteinsPeptidesLayout);

                                    exportAllProteinsPeptidesPopup.setDescription("Export Peptides from ( " + handler.getMainDataset().getName() + " ) Dataset for ( " + accession + " )");
                                    peptideTableLayout.setExpBtnPepTable(exportAllProteinsPeptidesPopup);

                                }
                                fractionsList = handler.getFractionsList(handler.getMainDataset().getDatasetId());
                                handler.retrieveStandardProtPlotList();//                          

                                if (handler.getMainDataset() == null || handler.getMainDataset().getStanderdPlotProt() == null || handler.getMainDataset().getStanderdPlotProt().isEmpty() || fractionsList == null || fractionsList.isEmpty()) { //(handler.getMainDataset() != null && handler.getMainDataset().getProteinList() != null) {
                                    if (searcheResultsTableLayout.getSearchTable() != null) {
                                        searcheResultsTableLayout.getSearchTable().setHeight("267.5px");
                                    }
                                    if (peptideTableLayout.getPepTable() != null) {
                                        peptideTableLayout.getPepTable().setHeight("267.5px");
                                        peptideTableLayout.setPeptideTableHeight("267.5px");
                                        System.err.println("pep not null");
                                    }
                                    fractionsLayout.removeAllComponents();
                                } else {
                                    if (handler.getMainDataset() != null && handler.getMainDataset().getProteinList() != null) {
                                        handler.getMainDataset().setFractionsList(fractionsList);
                                        handler.getDatasetList().put(handler.getMainDataset().getDatasetId(), handler.getMainDataset());
                                        double mw = 0.0;
                                        try {
                                            mw = Double.valueOf(item.getItemProperty("MW").toString());
                                        } catch (NumberFormatException e) {
                                            String str = item.getItemProperty("MW").toString();
                                            String[] strArr = str.split(",");
                                            if (strArr.length > 1) {
                                                str = strArr[0] + "." + strArr[1];
                                            }
                                            mw = Double.valueOf(str);
                                        }//                                    
                                        Map<Integer, ProteinBean> proteinFractionAvgList = handler.getProteinFractionAvgList(accession + "," + otherAccession, fractionsList, handler.getMainDataset().getDatasetId());
                                        if (proteinFractionAvgList == null || proteinFractionAvgList.isEmpty()) {
                                            fractionsLayout.removeAllComponents();
                                        } else {
                                            FractionsLayout flo = new FractionsLayout(accession, mw, proteinFractionAvgList, handler.getMainDataset().getStanderdPlotProt(), handler.getMainDataset().getName());
                                            flo.setMargin(new MarginInfo(false, false, false, false));
                                            fractionsLayout.addComponent(flo);
                                        }
                                    }
                                }

                            }
                        }
                    }

                };
                searcheResultsTableLayout.setListener(listener);
                searcheResultsTableLayout.getSearchTable().addValueChangeListener(listener);
                searchLayout.setVisible(true);
            }
        }
    }

    /**
     * no searching results found
     *
     * @param notFound
     */
    private void notFind(String notFound) {
        errorLabelII.updateErrot(notFound);
        errorLabelII.setVisible(true);

    }
}
