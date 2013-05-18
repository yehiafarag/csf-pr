package com.view.subviewunits;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.handlers.ExperimentHandler;
import com.helperunits.CustomInternalLink;
import com.helperunits.CustomExternalLink;
import com.helperunits.CustomPI;
//import com.helperunits.Help;
import com.model.FractionRangeUtilitie;
import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.PeptideBean;
import com.model.beans.ProteinBean;
import com.model.beans.StandardProteinBean;
import com.vaadin.addon.tableexport.CsvExport;
import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.FieldEvents.FocusEvent;
import com.vaadin.event.ShortcutAction.KeyCode;
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Form;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.view.ProteinView;

public class SearchUnit extends CustomComponent implements ClickListener, Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private VerticalLayout root = new VerticalLayout();         // Root element for contained components.
    private TextArea searchField;
    private Button searchButton = new Button("Search");
    private ExperimentHandler eh;
    private Label protLabel;
    private Label errorLabel;
    private PeptideTable pepTable;
    private Label pepLabel;
    private Button expBtnFractionTable;
    private SearchTable protExpTab = null;
    private Label fractionLabel;
    private Button expBtnPepTable;
    private Table fractTable;
    private FractionsPlots fractionPlotView;
    private TableResizeSet trs2;
    private VerticalLayout buttomSpacer;
    private int key2;
    private Button expBtnProtTable;
    private Select select; //select the search method
    private Label selectLabel;
    private Map<Integer, Map<Integer, ProteinBean>> protExpFullList;
    private Map<Integer, ExperimentBean> expList;
    private FractionRangeUtilitie fractionUti = new FractionRangeUtilitie();
    //private ArrayList<String> ranges;
    private VerticalLayout fractionLayout, peptideLayout;
    private String defaultText = "For Multiple Search...Please Use One key word Per Line !";
    private Button adminIco;
    private int pepSearch = 0;
    private Set<String> pepSet;
    private String selectStr = "Please Select Search Method";
    private VerticalLayout searchTableLayout;
    private ProteinView pv;
    private String protSize = "160px", pepSize = "167px";
    private CustomExternalLink myLink;

    public SearchUnit(Map<Integer, ExperimentBean> expList, String url, String dbName, String driver, String userName, String password, Button adminIco) {
        eh = new ExperimentHandler(url, dbName, driver, userName, password);
        this.adminIco = adminIco;
        pv = new ProteinView(url, dbName, driver, userName, password, this.adminIco);
        this.expList = expList;
        this.root.setMargin(true);
        this.updateComponents();
    }

    ///v-2
    @SuppressWarnings("deprecation")
    private void updateComponents() {
        //main layout
        root.removeAllComponents();
        setCompositionRoot(root);


        //search form layout
        final Form newSearchForm = new Form();
        searchField = new TextArea("Search Key ");
        searchField.setDescription("For Multiple Search...Please Use One key word Per Line !");
        searchField.setValue(defaultText);
        searchField.setImmediate(true);
        searchField.addListener(new FieldEvents.FocusListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;
            @Override
            public void focus(FocusEvent event) {
                if (defaultText.equals("For Multiple Search...Please Use One key word Per Line !")) {
                    searchField.setValue("");
                }

            }
        });

        searchField.setStyle(Reindeer.TEXTFIELD_SMALL);
        newSearchForm.addField(1, searchField);
        select = new Select("Search By ");
        select.addItem(selectStr);
        select.setValue(selectStr);
        select.setNullSelectionAllowed(false);
        select.setValue(selectStr);
        {
            Object itemId1 = select.addItem();
            select.setItemCaption(itemId1, "Protein Accession");
            Object itemId2 = select.addItem();
            select.setItemCaption(itemId2, "Protein Name");
            Object itemId3 = select.addItem();
            select.setItemCaption(itemId3, "Peptide Sequence");

        }
        select.setImmediate(true);
        select.addListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;
            @Override
            public void valueChange(ValueChangeEvent event) {
                searchButton.setClickShortcut(KeyCode.ENTER);
            }
        });
        select.setDescription(selectStr);
        select.setNullSelectionAllowed(false);

        searchField.setWidth("350px");
        select.setWidth("350px");

        newSearchForm.addField(Integer.valueOf(2), select);

        searchButton.setStyle(Reindeer.BUTTON_SMALL);
        newSearchForm.addField(4, searchButton);
        root.addComponent(newSearchForm);
        //select.select(itemId1);		      
        searchButton.addListener(this);
    }

    @SuppressWarnings("deprecation")
    @Override
    public synchronized void buttonClick(ClickEvent event) {
        pepSearch = 0;
        pepSet = null;
        if (searchTableLayout != null) {
            root.removeComponent(searchTableLayout);
        }
        searchTableLayout = new VerticalLayout();
        root.addComponent(searchTableLayout);
        Object seatchTypeObject = select.getValue();
        Object protSearchObject = searchField.getValue();
        if (protSearchObject == null || protSearchObject.toString().equals("") || protSearchObject.toString().equals("For Multiple Search...Please Use One key word Per Line !")) {
            if (selectLabel != null) {
                searchTableLayout.removeComponent(selectLabel);
            }
            selectLabel = new Label("<h4 Style='color:red;'>Please Enter Valid Key Word </h4>");
            selectLabel.setContentMode(Label.CONTENT_XHTML);
            searchTableLayout.addComponent(selectLabel);
            searchField.focus();
        } else if (seatchTypeObject == null || seatchTypeObject.toString().equals(selectStr)) {
            if (selectLabel != null) {
                searchTableLayout.removeComponent(selectLabel);
            }
            selectLabel = new Label("<h4 Style='color:red;'>Please Select Search by Method </h4>");
            selectLabel.setContentMode(Label.CONTENT_XHTML);
            searchTableLayout.addComponent(selectLabel);
            select.focus();
        } else {
            if (selectLabel != null) {
                searchTableLayout.removeComponent(selectLabel);
            }
            String searchType = seatchTypeObject.toString();
            String protSearch = protSearchObject.toString().trim().toUpperCase();
            defaultText = protSearch;
            searchField.setValue(defaultText);
            String[] searchArr = protSearch.split("\n");
            Set<String> searchSet = new HashSet<String>();
            for (String str : searchArr) {
                searchSet.add(str.trim());
            }
            if (errorLabel != null) {
                searchTableLayout.removeComponent(errorLabel);
            }

            //start the search
            Map<Integer, List<ProteinBean>> expProtList = null;
            List<Map<Integer, List<ProteinBean>>> ListOfExpProtList = new ArrayList<Map<Integer, List<ProteinBean>>>();
            List<Map<Integer, Map<Integer, ProteinBean>>> ListOfProtExpFullList = new ArrayList<Map<Integer, Map<Integer, ProteinBean>>>();

            String notFound = "";
            if (searchType.equals("1"))//case of protein accession
            {
                ListOfProtExpFullList = null;
                for (String searchStr : searchSet) {
                    expProtList = eh.searchProteinByAccession(searchStr.trim(), expList);
                    int tag = 0;
                    for (List<ProteinBean> pbl : expProtList.values()) {
                        if (pbl.size() > 0) {
                            tag++;
                            break;
                        }
                    }

                    if (tag == 0) {
                        notFound += searchStr + "\t";
                    } else {
                        ListOfExpProtList.add(expProtList);
                    }
                }
                protSearch = this.filterTableTitle(searchSet, notFound);
            } else if (searchType.equals("2")) //case of protein name
            {



                for (String searchStr : searchSet) {
                    protExpFullList = eh.searchProteinByName(searchStr.trim(), expList);
                    int tag = 0;
                    for (Map<Integer, ProteinBean> pbl : protExpFullList.values()) {
                        for (ProteinBean pb : pbl.values()) {
                            if (pb != null) {
                                tag++;
                                break;
                            }
                        }
                        if (tag > 0) {
                            break;
                        }
                    }
                    if (tag == 0) {
                        notFound += searchStr + "\t";
                    } else {
                        ListOfProtExpFullList.add(protExpFullList);
                    }


                }

                protSearch = this.filterTableTitle(searchSet, notFound);

            } else //find protein by peptide sequence
            {
                pepSearch = 1;
                pepSet = searchSet;
                for (String searchStr : searchSet) {
                    protExpFullList = eh.searchProteinByPeptideSequence(searchStr.trim(), expList);
                    int tag = 0;
                    for (Map<Integer, ProteinBean> pbl : protExpFullList.values()) {
                        for (ProteinBean pb : pbl.values()) {
                            if (pb != null) {
                                tag++;
                                break;
                            }
                        }
                        if (tag > 0) {
                            break;
                        }
                    }
                    if (tag == 0) {
                        notFound += searchStr + "\t";
                    } else {
                        ListOfProtExpFullList.add(protExpFullList);
                    }

                }

                protSearch = this.filterTableTitle(searchSet, notFound);

            }
            if (!notFound.equals("")) {
                notFind(notFound);
            }
            if (protLabel != null) {
                searchTableLayout.removeComponent(protLabel);
            }
            protLabel = new Label("<h4 style='font-family:verdana;color:black;'> Search Results </h4>");
            protLabel.setContentMode(Label.CONTENT_XHTML);
            protLabel.setHeight("20px");
            if (expBtnProtTable != null) {
                searchTableLayout.removeComponent(expBtnProtTable);
            }
            expBtnProtTable = new Button("Export Search Results");
            expBtnProtTable.setStyle(Reindeer.BUTTON_SMALL);
            searchTableLayout.setSpacing(true);


            if (protExpTab != null) {
                searchTableLayout.removeComponent(protExpTab);
            }
            protExpTab = new SearchTable(expList, ListOfExpProtList, ListOfProtExpFullList, pv);

            if (protExpTab != null && ((ListOfExpProtList != null && ListOfExpProtList.size() > 0) || (ListOfProtExpFullList != null && ListOfProtExpFullList.size() > 0))) {
                if (protExpTab.isVisible()) {
                    searchTableLayout.addComponent(protLabel);
                    searchTableLayout.addComponent(protExpTab);

                    TableResizeSet trs = new TableResizeSet(protExpTab, protSize);//resize tables
                    trs.setWidth("350px");
                    expBtnProtTable.setHeight("19px");
                    trs.addComponent(expBtnProtTable);
                    trs.setComponentAlignment(expBtnProtTable, Alignment.BOTTOM_RIGHT);
                    searchTableLayout.addComponent(trs);
                    searchTableLayout.setComponentAlignment(trs, Alignment.BOTTOM_RIGHT);
                    //searchTableLayout.addComponent(expBtnProtTable);					
                    //searchTableLayout.setComponentAlignment(expBtnProtTable, Alignment.BOTTOM_RIGHT);
                    expBtnProtTable.addListener(new ClickListener() {
                        /**
                         *
                         */
                        private static final long serialVersionUID = 1L;
                        private CsvExport excelExport;

                        public void buttonClick(ClickEvent event) {
                            excelExport = new CsvExport(protExpTab,"Search Results");
                            excelExport.setReportTitle("Search Results");
                            excelExport.setExportFileName("Search Results.csv");
                            excelExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                            excelExport.setDisplayTotals(false);
                            excelExport.export();
                        }
                    });
                    Button expBtnProtAllPepTable = new Button("Export Protein's Peptides ");
                    expBtnProtAllPepTable.setDescription("Export all Protien's Peptides from all Data Sets");
                    expBtnProtAllPepTable.setStyle(Reindeer.BUTTON_SMALL);
                    expBtnProtAllPepTable.setHeight("19px");
                    expBtnProtAllPepTable.addListener(new ClickListener() {
                        private static final long serialVersionUID = -73954695086117200L;
                        private CsvExport excelExport;
                        @Override
                        public void buttonClick(ClickEvent event) {
                            if (protExpTab.getValue() != null) {
                                key2 = (Integer) protExpTab.getValue();
                                Item item = protExpTab.getItem(key2);
                                final String accession = item.getItemProperty("Accession").toString();
                                Map<String, PeptideTable> pl = getPepList(accession);
                                int index = 0;
                                for (String key : pl.keySet()) {
                                    PeptideTable pt = pl.get(key);
                                    root.addComponent(pt);
                                    if (index == 0) {
                                        excelExport = new CsvExport(pt,"Protein");
                                        excelExport.setReportTitle("Protein's Peptides for  ( " + accession + " ) from ( " + key + " ) Data Set");
                                        excelExport.setExportFileName("Protein's Peptides for ( " + accession + " ).csv");
                                        excelExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                                        excelExport.setDisplayTotals(false);
                                        excelExport.convertTable();
                                        index++;
                                    } else {
                                        excelExport.setReportTitle("Protein's Peptides for  ( " + accession + " ) from ( " + key + " ) Data Set");
                                        excelExport.setDisplayTotals(false);
                                        excelExport.setRowHeaders(false);
                                        excelExport.setNextTable(pt, key);
                                        excelExport.setDisplayTotals(false);
                                        excelExport.convertTable();
                                    }
                                    index++;
                                }

                                excelExport.export();
                            } else {
                                //	System.out.println("you need to select protien first");
                            }

                        }

                        private Map<String, PeptideTable> getPepList(String accession) {
                            Map<String, PeptideTable> tl = new HashMap<String, PeptideTable>();
                            for (ExperimentBean temExp : expList.values()) {

                                List<Integer> expProPepIds = eh.getExpPepProIds(temExp.getExpId(), accession);
                                Map<Integer, PeptideBean> pepProtList = eh.getPeptidesProtList(temExp.getPeptideList(), accession, expProPepIds);
                                if (pepProtList.size() > 0) {
                                    PeptideTable pepTable = new PeptideTable(pepProtList, null);
                                    pepTable.setVisible(false);
                                    tl.put(temExp.getName(), pepTable);
                                }

                            }
                            return tl;

                        }
                    });
                    trs.addComponent(expBtnProtAllPepTable);
                    trs.setComponentAlignment(expBtnProtAllPepTable, Alignment.BOTTOM_RIGHT);

                } else {
                    notFind(protSearch);
                }
                protExpTab.addListener(new Property.ValueChangeListener() {
                    /**
                     * peptides, fractions ranges, fraction plots layout
                     *
                     */
                    private static final long serialVersionUID = 1L;
                    @Override
                    public synchronized void valueChange(ValueChangeEvent event) {

                        if (myLink != null) {
                            myLink.rePaintLable("black");
                        }
                        if (peptideLayout != null) {
                            peptideLayout.removeAllComponents();
                            searchTableLayout.removeComponent(peptideLayout);
                        }
                        if (protExpTab.getValue() != null) {
                            key2 = (Integer) protExpTab.getValue();
                        }
                        Item item = protExpTab.getItem(key2);
                        final String accession = item.getItemProperty("Accession").toString();
                        myLink = (CustomExternalLink) item.getItemProperty("Accession").getValue();
                        myLink.rePaintLable("white");

                        String desc = item.getItemProperty("Description").toString();
                        double mw = Double.valueOf(item.getItemProperty("MW").toString());

                        final Property myExpPro = item.getItemProperty("Experiment");
                        CustomInternalLink myExp = (CustomInternalLink) myExpPro.getValue();
                        int expId = Integer.valueOf(myExp.getKey());


                        ExperimentBean exp = null;
                        while (true) {
                            exp = expList.get(expId);
                            break;
                        }

                        List<Integer> expProPepIds = eh.getExpPepProIds(exp.getExpId(), accession);
                        Map<Integer, PeptideBean> pepProExpList = eh.getPeptidesProtList(exp.getPeptideList(), accession, expProPepIds);
                        if (buttomSpacer != null) {
                            searchTableLayout.removeComponent(buttomSpacer);
                        }
                        buttomSpacer = new VerticalLayout();
                        buttomSpacer.setVisible(false);
                        buttomSpacer.setHeight("2px");
                        buttomSpacer.setStyle(Reindeer.LAYOUT_BLACK);
                        buttomSpacer.setMargin(true, true, false, false);
                        buttomSpacer.setSpacing(true);

                        if (exp.getPeptideList() == null) {
                            exp.setPeptideList(pepProExpList);
                        } else {
                            exp.getPeptideList().putAll(pepProExpList);
                        }

                        expList.put(exp.getExpId(), exp);


                        if (pepProExpList.isEmpty()) {
                            if (peptideLayout != null) {
                                peptideLayout.removeAllComponents();
                                searchTableLayout.removeComponent(peptideLayout);
                            }
                            peptideLayout = new VerticalLayout();
                            if (pepLabel != null) {
                                peptideLayout.removeComponent(pepLabel);
                            }
                            //pepLabel = new Label("<h4 style='color:red;'>No Peptides Available for  ( " +accession+") !</h4>");
                            //pepLabel.setContentMode(Label.CONTENT_XHTML);
                            //peptideLayout.addComponent(pepLabel);
                            //searchTableLayout.addComponent(peptideLayout);

                        } else {

                            if (peptideLayout != null) {
                                peptideLayout.removeAllComponents();
                                searchTableLayout.removeComponent(peptideLayout);
                            }
                            peptideLayout = new VerticalLayout();
                            searchTableLayout.addComponent(peptideLayout);
                            searchTableLayout.addComponent(buttomSpacer);
                            buttomSpacer.setVisible(true);
                            int validPep = this.getValidatedPepNumber(pepProExpList);
                            if (pepLabel != null) {
                                peptideLayout.removeComponent(pepLabel);
                            }
                            if (pepSearch == 1) {
                                pepTable = new PeptideTable(pepProExpList, pepSet);
                            } else {
                                pepTable = new PeptideTable(pepProExpList, null);
                            }

                            if (trs2 != null) {
                                pepSize = trs2.getCurrentSize();
                                peptideLayout.removeComponent(trs2);
                            }


                            pepTable.setHeight(pepSize);
                            pepLabel = new Label("<h4 style='font-family:verdana;color:black;'>Peptides (" + validPep + "/" + pepProExpList.size() + ") " + desc + "</h4>");
                            pepLabel.setContentMode(Label.CONTENT_XHTML);
                            pepLabel.setHeight("20px");
                            if (expBtnPepTable != null) {
                                peptideLayout.removeComponent(expBtnPepTable);
                            }
                            expBtnPepTable = new Button("Export Peptides");
                            expBtnPepTable.setStyle(Reindeer.BUTTON_SMALL);

                            peptideLayout.addComponent(pepLabel);
                            peptideLayout.setSpacing(true);
                            peptideLayout.addComponent(pepTable);

                            trs2 = new TableResizeSet(pepTable, pepSize);//resize tables
                            trs2.addComponent(expBtnPepTable);
                            trs2.setComponentAlignment(expBtnPepTable, Alignment.BOTTOM_RIGHT);
                            peptideLayout.addComponent(trs2);
                            peptideLayout.setComponentAlignment(trs2, Alignment.BOTTOM_RIGHT);



                            expBtnPepTable.addListener(new ClickListener() {
                                /**
                                 *
                                 */
                                private static final long serialVersionUID = 1L;
                                private CsvExport excelExport;
                                @Override
                                public synchronized void buttonClick(ClickEvent event) {
                                    excelExport = new CsvExport(pepTable,"Peptides");
                                    excelExport.setReportTitle("Peptides for ( " + accession + " ) Data Set ( " + myExpPro.toString() + " )");
                                    excelExport.setExportFileName("Peptides for ( " + accession + " ) Data Set ( " + myExpPro.toString() + " )");
                                    excelExport.setExportFileName(accession + " Peptides.csv");
                                    excelExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                                    excelExport.setDisplayTotals(false);
                                    excelExport.export();
                                }
                            });
                        }





                        /// fraction part

                        List<StandardProteinBean> standardProtPlotList = eh.getStandardProtPlotList(exp.getExpId());
                        Map<Integer, FractionBean> fractionsList;
                        while (true) {
                            exp = expList.get(expId);
                            fractionsList = exp.getFractionsList();
                            if (fractionsList == null || (fractionsList.size() > 0)) {
                                break;
                            }
                        }
                        if (exp.getFractionRange() == 0 || standardProtPlotList==null ||standardProtPlotList.isEmpty()) {
                            if (fractionLayout != null) {
                                searchTableLayout.removeComponent(fractionLayout);
                            }
                            fractionLayout = new VerticalLayout();
                            searchTableLayout.addComponent(fractionLayout);
                            if (fractionLabel != null) {
                                fractionLayout.removeComponent(fractionLabel);
                            }
                            if (protExpTab != null) {
                                protExpTab.setHeight("260px");
                                protSize = "260px";
                            }
                            if (pepTable != null) {
                                pepTable.setHeight("267px");
                                pepSize = "267px";
                            }
                            // fractionLabel = new Label("<h4 style='color:red;'>No Fractions Available!</h4>");
                            //fractionLabel.setContentMode(Label.CONTENT_XHTML);
                            // fractionLayout.addComponent(fractionLabel);



                        } else {
                            if (fractionLayout != null) {
                                searchTableLayout.removeComponent(fractionLayout);
                            }
                            fractionLayout = new VerticalLayout();
                            if (fractionLabel != null) {
                                fractionLayout.removeComponent(fractionLabel);
                            }
                            fractionLabel = new Label("<h4 style='font-family:verdana;color:black;'>Fractions (Protein: " + accession + "  MW: " + mw + " kDa)</h4>");
                            fractionLabel.setContentMode(Label.CONTENT_XHTML);
                            fractionLabel.setHeight("15px");

                            if (expBtnFractionTable != null) {
                                fractionLayout.removeComponent(expBtnFractionTable);
                            }
                            expBtnFractionTable = new Button("Export Fractions");
                            expBtnFractionTable.setStyle(Reindeer.BUTTON_SMALL);

                            fractionLayout.addComponent(fractionLabel);
                            searchTableLayout.addComponent(fractionLayout);

                            if (exp.getFractionsList() == null) {
                                exp.setFractionsList(eh.getFractionsList(exp.getExpId(), expList));
                            }


                            Map<Integer, ProteinBean> proteinFractionAvgList = eh.getProteinFractionAvgList(accession, exp.getFractionsList(), exp.getExpId());


                           // ranges = fractionUti.getFractionRange(exp);

                            //    Table fractionRangeTable = fractionUti.getRangeTable(ranges, mw);
                            Label fractionProtLable = new Label("<h4 style='font-family:verdana;color:black;'>Protein: " + accession + "<br/>MW: " + mw + " kDa</h4>");
                            fractionProtLable.setContentMode(Label.CONTENT_XHTML);
                            fractionProtLable.setHeight("20px");


                            fractTable = getFractionTable(proteinFractionAvgList);
                            fractTable.setWidth("100%");
                            fractTable.setVisible(false);
                            expBtnFractionTable.addListener(new ClickListener() {
                                /**
                                 *
                                 */
                                private static final long serialVersionUID = 1L;
                                private CsvExport excelExport;
                                @Override
                                public synchronized void buttonClick(ClickEvent event) {
                                    excelExport = new CsvExport(fractTable,"Fractions");
                                    excelExport.setReportTitle("Fractions for ( " + accession + " ) Data Set ( " + myExpPro.toString() + " )");
                                    excelExport.setExportFileName(accession + " Fractions.csv");
                                    excelExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                                    excelExport.setDisplayTotals(false);
                                    excelExport.export();
                                }
                            });
                          //  List<StandardProteinBean> standardProtPlotList = eh.getStandardProtPlotList(exp.getExpId());

                            fractionPlotView = new FractionsPlots(proteinFractionAvgList, mw, standardProtPlotList);

                            
                            
                            HorizontalLayout fractionsDataLayout = new HorizontalLayout();
                            fractionsDataLayout.setWidth("100%");
                            fractionsDataLayout.addComponent(fractionPlotView);
                            fractionsDataLayout.setComponentAlignment(fractionPlotView, Alignment.TOP_LEFT);
                            VerticalLayout fractLablesVLO = new VerticalLayout();
                            fractionsDataLayout.addComponent(fractLablesVLO);
                            fractionsDataLayout.setExpandRatio(fractionPlotView, 3f);
                            fractionsDataLayout.setExpandRatio(fractLablesVLO, 1f);
                            // fractLablesVLO.addComponent(fractionProtLable);
                            Table t = getStandardPlotTable(fractionPlotView.getStandProtGroups());
                            Label standPlotLab = new Label("<h4 style='font-family:verdana;color:black;'>Protein Standards</h4>");
                            standPlotLab.setContentMode(Label.CONTENT_XHTML);
                            standPlotLab.setHeight("20px");
                            fractLablesVLO.setSpacing(true);
                            fractLablesVLO.addComponent(standPlotLab);
                            fractLablesVLO.addComponent(t);
                            // fractLablesVLO.setSpacing(true);
                            // fractLablesVLO.setComponentAlignment(fractionProtLable, Alignment.BOTTOM_CENTER);

                            fractionsDataLayout.setComponentAlignment(fractLablesVLO, Alignment.TOP_RIGHT);
                            fractionLayout.addComponent(fractionsDataLayout);
                            fractionLayout.setComponentAlignment(fractionsDataLayout, Alignment.TOP_CENTER);

                            HorizontalLayout fractionsLastLayout = new HorizontalLayout();
                            fractionsLastLayout.setWidth("100%");
                            //  Label commLable = new Label("<h5 style='font-family:verdana;'>"+"[] Indicate Theoretical MW"+"</h5>");
                            //   commLable.setContentMode(Label.CONTENT_XHTML);


                            //   fractionsLastLayout.addComponent(commLable);
                            //   fractionsLastLayout.setComponentAlignment(commLable, Alignment.TOP_LEFT);
                            fractionsLastLayout.addComponent(expBtnFractionTable);
                            fractionsLastLayout.setComponentAlignment(expBtnFractionTable, Alignment.MIDDLE_RIGHT);
                            fractionLayout.addComponent(fractionsLastLayout);
//                            HorizontalLayout fractionDataLayout = new HorizontalLayout();
//                            fractionDataLayout.setWidth("100%");
//                            fractionDataLayout.addComponent(fractionPlotView);
//                            fractionDataLayout.setComponentAlignment(fractionPlotView, Alignment.TOP_LEFT);
//                            //   fractionDataLayout.addComponent(fractionRangeTable);
//                            //   fractionDataLayout.setExpandRatio(fractionPlotView, 3.5f);
//                            //   fractionDataLayout.setExpandRatio(fractionRangeTable, 1.5f);
//                            //   fractionDataLayout.setComponentAlignment(fractionRangeTable, Alignment.TOP_RIGHT);
//                            fractionDataLayout.addComponent(fractionProtLable);
//                            fractionDataLayout.setExpandRatio(fractionPlotView, 5f);
//                            fractionDataLayout.setExpandRatio(fractionProtLable, 1f);
//                            fractionDataLayout.setComponentAlignment(fractionProtLable, Alignment.MIDDLE_RIGHT);
//
//
//                            fractionLayout.addComponent(fractionDataLayout);
//                            fractionDataLayout.addComponent(fractTable);
//                            fractionLayout.addComponent(expBtnFractionTable);
//                            fractionLayout.setSpacing(true);
//                            fractionLayout.setComponentAlignment(expBtnFractionTable, Alignment.TOP_RIGHT);

                        }
                    }

                    private int getValidatedPepNumber(
                            Map<Integer, PeptideBean> pepProtList) {
                        int count = 0;
                        for (PeptideBean pb : pepProtList.values()) {
                            if (pb.getValidated() == 1.0) {
                                count++;
                            }
                        }
                        return count;
                    }
                });


            } else {
                notFind(notFound);
            }
            if (protExpFullList != null) {
                protExpFullList = null;
            }



        }



    }

     @SuppressWarnings("deprecation")
    private Table getFractionTable(Map<Integer, ProteinBean> proteinFractionAvgList) {
        Table table = new Table();
        table.setStyle(Reindeer.TABLE_STRONG + " " + Reindeer.TABLE_BORDERLESS);
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
            ProteinBean pb =proteinFractionAvgList.get(index);
            table.addItem(new Object[]{ new Integer(index), pb.getNumberOfPeptidePerFraction(), pb.getNumberOfSpectraPerFraction(), pb.getAveragePrecursorIntensityPerFraction()}, new Integer(x + 1));
            x++;
        }
        for (Object propertyId : table.getSortableContainerPropertyIds()) {
            table.setColumnExpandRatio(propertyId.toString(), 1.0f);
        }

        return table;
    }

    private String filterTableTitle(Set<String> searchSet, String notFound) {

        String[] strArr = notFound.split("\t");
        for (String str : strArr) {
            searchSet.remove(str);
        }
        return searchSet.toString();
    }

    private void notFind(String protSearch) {
        if (errorLabel != null) {
            searchTableLayout.removeComponent(errorLabel);
        }
        errorLabel = new Label("<h4 Style='color:red;'>Sorry No Results Found for ( " + protSearch + " ) </h4>");
        errorLabel.setContentMode(Label.CONTENT_XHTML);
        searchTableLayout.addComponent(errorLabel);
    }
    
    @SuppressWarnings("deprecation")
    private Table getStandardPlotTable(Map<String, List<StandardProteinBean>> standProtGroups) {
        Table table = new Table();
        table.setStyle(Reindeer.TABLE_BORDERLESS);
        table.setHeight("240px");
        table.setWidth("100%");
        table.setSelectable(false);
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);
        table.setImmediate(true); // react at once when something is selected
        table.addContainerProperty("Col", CustomPI.class, null, "", null, com.vaadin.ui.Table.ALIGN_CENTER);

        table.addContainerProperty("Protein", String.class, null, "Protein", null, com.vaadin.ui.Table.ALIGN_CENTER);
        table.addContainerProperty("MW", Double.class, null, "MW", null, com.vaadin.ui.Table.ALIGN_CENTER);
        /* Add a few items in the table. */
        int x = 0;
        for (String key : standProtGroups.keySet()) {
            CustomPI ce = null;
            if (key.equalsIgnoreCase("#79AFFF")) {
                List<StandardProteinBean> lsp = standProtGroups.get(key);
                for (StandardProteinBean spb : lsp) {
                    ce = new CustomPI("Selected Standard Plot", new ExternalResource("https://fbcdn-sphotos-g-a.akamaihd.net/hphotos-ak-prn1/488024_137541363096131_1104414259_n.jpg"));
                    table.addItem(new Object[]{ce, spb.getName(), spb.getMW_kDa()}, new Integer(x + 1));
                    x++;
                }
            } else {
                List<StandardProteinBean> lsp = standProtGroups.get(key);
                for (StandardProteinBean spb : lsp) {
                    ce = new CustomPI("Standard Plot", new ExternalResource("https://fbcdn-sphotos-a-a.akamaihd.net/hphotos-ak-ash3/528295_137541356429465_212869913_n.jpg"));
                    table.addItem(new Object[]{ce, spb.getName(), spb.getMW_kDa()}, new Integer(x + 1));
                    x++;
                }
            }

        }
        for (Object propertyId : table.getSortableContainerPropertyIds()) {
            if (propertyId.toString().equals("Protein")) {
                table.setColumnExpandRatio(propertyId.toString(), 2.5f);
            } else {
                table.setColumnExpandRatio(propertyId.toString(), 1.0f);
            }
        }
        table.setSortContainerPropertyId("MW");
        table.setSortAscending(false);
        return table;
    }
}
