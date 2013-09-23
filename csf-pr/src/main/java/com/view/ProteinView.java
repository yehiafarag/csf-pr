package com.view;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.handlers.ExperimentHandler;
import com.helperunits.CustomExternalLink;
import com.helperunits.CustomPI;
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
import com.vaadin.terminal.ExternalResource;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Select;
import com.vaadin.ui.Table;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ChameleonTheme;
import com.view.subviewunits.ExperimentsTable;
import com.view.subviewunits.ExperimentDetails;
import com.view.subviewunits.FractionsPlots;
import com.view.subviewunits.PeptideTable;
import com.view.subviewunits.ProteinsTable;
import com.view.subviewunits.TableResizeSet;

public class ProteinView extends VerticalLayout implements Serializable, Property.ValueChangeListener {

    /**
     *
     */
    private static final long serialVersionUID = -4542745719375999737L;
    private VerticalLayout layout1 = new VerticalLayout();
    private VerticalLayout layout2 = new VerticalLayout();
    private TreeMap<Integer, String> expListStr;
    private boolean visability = false;
    private Select selectExp;
    private VerticalLayout l1;
    private Map<Integer, ExperimentBean> expList = null;
    private ExperimentHandler eh;
    private ExperimentsTable expTable;
    private Map<Integer, FractionBean> fractionsList = null;
    private String url, dbName, driver, userName, password;
    private ExperimentDetails expDetails;
    private CustomExternalLink l;
    private String protSize = "160px", PepSize = "167px";
    private PeptideTable pepTable;
    private Label pepLabel;
    private Button expBtnPepTable;
    private ExperimentBean exp;
    private String accession;
    private int key = -1;
    private FractionRangeUtilitie fractionUti = new FractionRangeUtilitie();
    private Table fractTable;
    private String s = "\t \t \t Please Select Dataset";
    private VerticalLayout buttomSpacer;
    private int starter = 1;
    private Button adminLogin = null;
    private HorizontalLayout hlo;
    private TableResizeSet trs2;

    public ProteinView(String url, String dbName, String driver, String userName, String password, Button adminLoginIco) {
        eh = new ExperimentHandler(url, dbName, driver, userName, password);
        expTable = new ExperimentsTable(url, dbName, driver, userName, password);
        this.url = url;
        this.dbName = dbName;
        this.driver = driver;
        this.userName = userName;
        this.password = password;
        this.setWidth("100%");
        this.setSizeFull();
        this.adminLogin = adminLoginIco;
        buildMainLayout();

    }

    public void buildMainLayout() {

        // Tab 1 content
        l1 = new VerticalLayout();
        l1.setWidth("100%");



        layout2.setWidth("100%");
        expList = expTable.getExpList();
        if (expList == null || expList.isEmpty()) {
            layout1.removeAllComponents();


            hlo = new HorizontalLayout();
            hlo.setWidth("100%");
            hlo.setMargin(false, true, false, false);
            Label noExpLable = new Label("<h4>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(Label.CONTENT_XHTML);
            hlo.addComponent(noExpLable);
            hlo.addComponent(adminLogin);
            hlo.setComponentAlignment(adminLogin, Alignment.TOP_RIGHT);
            layout1.addComponent(hlo);
        } else {
            layout1.removeAllComponents();
            Label expLable = new Label("<h4 style='font-family:verdana;color:black;'>Select Dataset :</h4>");
            expLable.setContentMode(Label.CONTENT_XHTML);
            expLable.setHeight("20px");

            hlo = new HorizontalLayout();
            hlo.setWidth("100%");
            hlo.setMargin(false, true, false, false);
            hlo.addComponent(expLable);
            hlo.addComponent(adminLogin);
            hlo.setComponentAlignment(adminLogin, Alignment.TOP_RIGHT);
            layout1.addComponent(hlo);

            // layout1.addComponent(expLable);
            expTable = new ExperimentsTable(url, dbName, driver, userName, password);
            layout2.addComponent(expTable);
            l1.addComponent(layout2);
            expListStr = new TreeMap<Integer, String>();
            for (int key : expList.keySet()) {
                ExperimentBean expB = expList.get(key);
                expListStr.put(key, "\t" + expB.getName());
            }

            selectExp = new Select(null);

            selectExp.addItem(s);
            selectExp.setValue(s);
            selectExp.setNullSelectionAllowed(false);
            selectExp.setValue(s);
            for (String str : expListStr.values()) {
                selectExp.addItem(str);

            }
            selectExp.setImmediate(true);
            selectExp.addListener(this);
            selectExp.setWidth("40%");
            selectExp.setDescription("Please Select Dataset");
            layout1.addComponent(selectExp);
            selectExp.setNullSelectionAllowed(false);
        }
        addComponent(layout1);
    }

    public void valueChange(ValueChangeEvent event) {

        Object o = selectExp.getValue();
        PepSize = "167px";
        if (o != null && (!o.toString().equals(s))) {
            String str = selectExp.getValue().toString();
            int key = this.getKey(expListStr, str);
            exp = expList.get(key);

            if (exp.getReady() != 2 && exp.getFractionsNumber() > 0 || exp.getProteinsNumber() == 0) {
                this.getWindow().showNotification("THIS DATASET NOT READY YET!");
            } else {
                starter = 1;
                //mw.setHeight("100%");
                //mw.setWidth("100%");				
                if (expDetails != null && layout2 != null) {
                    visability = expDetails.isVisability();
                    layout2.removeComponent(expDetails);
                    this.removeComponent(layout2);
                    layout2.removeAllComponents();
                }

                layout2 = new VerticalLayout();
                this.addComponent(layout2);
                layout2.setWidth("100%");
                expDetails = this.buildExpView(exp, visability);//get experiment details view	

                layout2.addComponent(expDetails);

                layout2.setComponentAlignment(expDetails, Alignment.MIDDLE_CENTER);
                VerticalLayout protTableLayout = this.addProteinsTable();
                layout2.addComponent(protTableLayout);


            }
        }

    }

    @SuppressWarnings("deprecation")
    private VerticalLayout addProteinsTable() {
        Map<String, ProteinBean> proteinsList = eh.getProteinsList(exp.getExpId(), expList);
        exp.setProteinList(proteinsList);
        expList.put(exp.getExpId(), exp);
        final ProteinsTable protTable = new ProteinsTable(proteinsList, exp.getFractionsNumber());

        VerticalLayout protTableLayout = new VerticalLayout();
        protTableLayout.setWidth("100%");
        Label protLabel = new Label("<h4 style='font-family:verdana;color:black;'>Proteins (" + exp.getProteinsNumber() + ")</h4>");
        protLabel.setContentMode(Label.CONTENT_XHTML);
        protLabel.setHeight("20px");




        protTableLayout.setSpacing(true);
        protTableLayout.addComponent(protLabel);
        protTableLayout.addComponent(protTable);
        protTableLayout.setComponentAlignment(protTable, Alignment.TOP_CENTER);
        protTableLayout.setMargin(true, false, false, false);
        Button expBtnProtTable = new Button("Export Proteins");
        expBtnProtTable.setStyle(Reindeer.BUTTON_SMALL);//Reindeer.BUTTON_SMALL);
        expBtnProtTable.setHeight("19px");
        expBtnProtTable.addListener(new ClickListener() {
            private static final long serialVersionUID = -73954695086117200L;
            private CsvExport excelExport;

            public void buttonClick(ClickEvent event) {
                excelExport = new CsvExport(protTable,"Proteins");
                excelExport.setReportTitle("Proteins for Data Set ( " + exp.getName() + " )");
                excelExport.setExportFileName("Proteins for ( " + exp.getName() + " ).csv");
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

            public void buttonClick(ClickEvent event) {

                Map<String, PeptideTable> pl = getPepList(accession);
                int index = 0;
                for (String key : pl.keySet()) {
                    PeptideTable pt = pl.get(key);
                    layout1.addComponent(pt);
                    if (index == 0) {
                        excelExport = new CsvExport(pt,"Peptides");
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

        TableResizeSet trs1 = new TableResizeSet(protTable, protSize);//resize tables
        trs1.addComponent(expBtnProtTable);
        trs1.setWidth("320px");
        trs1.addComponent(expBtnProtAllPepTable);
        trs1.setComponentAlignment(expBtnProtAllPepTable, Alignment.BOTTOM_RIGHT);

        trs1.setComponentAlignment(expBtnProtTable, Alignment.BOTTOM_RIGHT);
        protTableLayout.addComponent(trs1);
        protTableLayout.setComponentAlignment(trs1, Alignment.BOTTOM_RIGHT);
        protTableLayout.setSpacing(true);

        //for  peptides information (table) view
        final VerticalLayout peptideLayout = new VerticalLayout();
        protTableLayout.addComponent(peptideLayout);
        peptideLayout.setWidth("100%");
        peptideLayout.setMargin(false, false, true, false);
        if (buttomSpacer != null) {
            protTableLayout.removeComponent(buttomSpacer);
        }
        buttomSpacer = new VerticalLayout();
        buttomSpacer.setVisible(false);
        buttomSpacer.setHeight("2px");
        buttomSpacer.setStyle(Reindeer.LAYOUT_BLACK);
        buttomSpacer.setMargin(false, true, false, false);
        buttomSpacer.setSpacing(true);
        protTableLayout.addComponent(buttomSpacer);
        //for Fraction information (Plots) view
        final VerticalLayout fractionLayout = new VerticalLayout();
        protTableLayout.addComponent(fractionLayout);
        fractionLayout.setWidth("100%");
        
        protTable.addListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 1L;

            public synchronized void valueChange(ValueChangeEvent event) {
                //	mw.setWidth("100%");
                if (l != null) {
                    l.rePaintLable("black");
                }
                if (starter != 1) {
                    expDetails.hideDetails();
                }
                starter++;
                String desc = "";
                peptideLayout.removeAllComponents();
                fractionLayout.removeAllComponents();
                if (protTable.getValue() != null) {
                    key = (Integer) protTable.getValue();
                }
                final Item item = protTable.getItem(key);
                desc = item.getItemProperty("Description").toString();
                if (item != null) {
                    accession = item.getItemProperty("Accession").toString();
                    l = (CustomExternalLink) item.getItemProperty("Accession").getValue();
                    l.rePaintLable("white");


                }
                if (key < 0 || item == null)
						; else {

                    exp = expList.get(exp.getExpId());
                    List<Integer> expProPepIds = eh.getExpPepProIds(exp.getExpId(), accession);
                    Map<Integer, PeptideBean> pepProtList = eh.getPeptidesProtList(exp.getPeptideList(), accession, expProPepIds);
                    if (exp.getPeptideList() == null) {
                        exp.setPeptideList(pepProtList);
                    } else {
                        exp.getPeptideList().putAll(pepProtList);
                    }

                    expList.put(exp.getExpId(), exp);
                    if (pepProtList.size() == 0) {
                        //	Label noPeptideLable = new Label("<h4>Sorry No Peptides Data Available Now !</h4>");
                        //	noPeptideLable.setContentMode(Label.CONTENT_XHTML);
                        //	fractionLayout.addComponent(noPeptideLable);
                    } else {
                        int validPep = this.getValidatedPepNumber(pepProtList);
                        if (pepLabel != null) {
                            peptideLayout.removeComponent(pepLabel);
                        }
                        pepLabel = new Label("<h4 style='font-family:verdana;color:black;'>Peptides (" + validPep + "/" + pepProtList.size() + ") " + desc + "</h4>");
                        pepLabel.setContentMode(Label.CONTENT_XHTML);
                        pepLabel.setHeight("20px");
                        pepTable = new PeptideTable(pepProtList, null);
                        if (trs2 != null) {
                            PepSize = trs2.getCurrentSize();
                            peptideLayout.removeComponent(trs2);
                        }
                        if (PepSize.equals("160px")) {
                            PepSize = "167.5px";
                        }
                        pepTable.setHeight(PepSize);
                        if (expBtnPepTable != null) {
                            peptideLayout.removeComponent(expBtnPepTable);
                        }
                        expBtnPepTable = new Button("Export Peptides");
                        expBtnPepTable.setStyle(Reindeer.BUTTON_SMALL);
                        expBtnPepTable.setHeight("19px");
                        expBtnPepTable.addListener(new ClickListener() {
                            private static final long serialVersionUID = -73954695086117200L;
                            private CsvExport excelExport;

                            public synchronized void buttonClick(ClickEvent event) {
                                excelExport = new CsvExport(pepTable,"Peptides");
                                excelExport.setReportTitle("Peptides for ( " + accession + " ) Data Set ( " + exp.getName() + " )");
                                excelExport.setExportFileName("Peptides for ( " + accession + " ).csv");
                                excelExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                                excelExport.setDisplayTotals(false);
                                excelExport.export();
                            }
                        });

                        peptideLayout.addComponent(pepLabel);
                        peptideLayout.addComponent(pepTable);
                        peptideLayout.setSpacing(true);

                        trs2 = new TableResizeSet(pepTable, PepSize);//resize tables
                        trs2.addComponent(expBtnPepTable);
                        trs2.setComponentAlignment(expBtnPepTable, Alignment.BOTTOM_RIGHT);
                        peptideLayout.addComponent(trs2);
                        peptideLayout.setComponentAlignment(trs2, Alignment.BOTTOM_RIGHT);
                        // PepSize = trs2.getCurrentSize();
                    }

                     List<StandardProteinBean> standardProtPlotList = eh.getStandardProtPlotList(exp.getExpId());

                    if (exp == null || exp.getReady() != 2 || standardProtPlotList==null||standardProtPlotList.isEmpty()) {

                        buttomSpacer.setVisible(false);
                        if (protTable != null) {
                            protTable.setHeight("260px");
                            protSize = "260px";
                        }
                        if (pepTable != null && PepSize.equals("167px")) {
                            pepTable.setHeight("267px");
                            PepSize = "267px";
                        }
                        // Label noFractionLable = new Label("<h4>Sorry No Fractions Data Available Now !</h4>");
                        //  noFractionLable.setContentMode(Label.CONTENT_XHTML);
                        //  fractionLayout.addComponent(noFractionLable);

                    } else {
                        // mw.setSizeUndefined();
                        // mw.setWidth("100%");

                        if (exp != null && exp.getProteinList() != null) {
                            buttomSpacer.setVisible(true);


                            fractionsList = eh.getFractionsList(exp.getExpId(), expList);
                            exp.setFractionsList(fractionsList);
                            expList.put(exp.getExpId(), exp);

                            double mw = 0.0;
                            try {
                                mw = Double.valueOf(item.getItemProperty("MW").toString());
                            } catch (Exception e) {
                                String str = item.getItemProperty("MW").toString();
                                String[] strArr = str.split(",");
                                if (strArr.length > 1) {
                                    str = strArr[0] + "." + strArr[1];
                                }
                                mw = Double.valueOf(str);
                            }

                            Label fractionLabel = new Label("<h4 style='font-family:verdana;color:black;'>Fractions (Protein: " + accession + "  MW: " + mw + " kDa)</h4>");
                            fractionLabel.setContentMode(Label.CONTENT_XHTML);
                            fractionLabel.setHeight("15px");
                            fractionLayout.addComponent(fractionLabel);
                            fractionLayout.setSpacing(true);

                            Button expBtnFractionTable = new Button("Export Fractions");
                            expBtnFractionTable.setStyle(Reindeer.BUTTON_SMALL);

                            Map<Integer, FractionBean> fractionsList;
                            while (true) {
                                fractionsList = getFractionsList();
                                if (fractionsList.size() > 0) {
                                    break;
                                }
                            }
                            Map<Integer, ProteinBean> proteinFractionAvgList = eh.getProteinFractionAvgList(accession, fractionsList, exp.getExpId());
                           // ArrayList<String> ranges = fractionUti.getFractionRange(exp);

                            // Table rangeTable = fractionUti.getRangeTable(ranges,mw);
                            //  fractionLayout.addComponent(rangeTable);

                            //   Label fractionProtLable = new Label("<h4 style='font-family:verdana;color:black;'>Protein: "+accession+"<br/>MW: "+mw+" kDa</h4>");
                            //    fractionProtLable.setContentMode(Label.CONTENT_XHTML);
                            //    fractionProtLable.setHeight("40px");
                            //  fractionLayout.addComponent(fractionProtLable);

                            fractTable = getFractionTable(proteinFractionAvgList);
                            fractTable.setWidth("100%");
                            fractTable.setVisible(false);
                            fractionLayout.addComponent(fractTable);

                            expBtnFractionTable.addListener(new ClickListener() {
                                /**
                                 *
                                 */
                                private static final long serialVersionUID = 1L;
                                private CsvExport excelExport;

                                public void buttonClick(ClickEvent event) {
                                    excelExport = new CsvExport(fractTable,"Fractions");
                                    excelExport.setReportTitle("Fractions for ( " + accession + " ) Data Set ( " + exp.getName() + " )");
                                    excelExport.setExportFileName(accession + "Fractions.csv");
                                    excelExport.setMimeType(CsvExport.CSV_MIME_TYPE);
                                    excelExport.setDisplayTotals(false);
                                    excelExport.export();
                                }
                            });
//                            List<StandardProteinBean> standardProtPlotList = eh.getStandardProtPlotList(exp.getExpId());

                            FractionsPlots fractionPlotView = new FractionsPlots(proteinFractionAvgList, mw,standardProtPlotList);

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

                        } else {
                            getWindow().showNotification("YOU NEED TO SELECT A READY DATASET FIRST!");

                        }
                    }
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
        protTable.select(1);
        protTable.commit();





        return protTableLayout;

    }

    public Map<Integer, ExperimentBean> getExpList() {
        return expList;
    }

    public Map<Integer, FractionBean> getFractionsList() {
        return fractionsList;
    }

    public Map<Integer, ExperimentBean> getUpdatedExpList() {
        if (expTable != null) {
            expList = expTable.getExpList();
        }
        return expList;
    }

    public void updateExpTable() {
        expList = expTable.getExperiments(expList);
        expTable.updateComponents(expList);
    }

    public Map<Integer, ExperimentBean> resetExpList() {
        expList = expTable.resetExperiments();
        return expList;
    }

    public void resetExpTable() {
        expList = expTable.resetExperiments();
        expTable.updateComponents(expList);

    }

    private int getKey(Map<Integer, String> l, String str) {
        for (int key : l.keySet()) {
            if (str.equalsIgnoreCase(l.get(key))) {
                return key;
            }
        }
        return 0;
    }

    private ExperimentDetails buildExpView(ExperimentBean exp, boolean visability2) {
        return new ExperimentDetails(exp, visability2);
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

    public ProteinView showExpProt(ExperimentBean expBean) {
        this.removeAllComponents();
        l1 = new VerticalLayout();
        l1.setWidth("100%");


        layout2.setWidth("100%");
        expList = expTable.getExpList();
        if (expList == null || expList.size() == 0) {
            layout1.removeAllComponents();
            Label noExpLable = new Label("<h4>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(Label.CONTENT_XHTML);
            layout1.addComponent(noExpLable);
        } else {
            layout1.removeAllComponents();
            Label expLable = new Label("<h4 style='font-family:verdana;color:blue;'>Result for " + expBean.getName() + " Dataset</h4>");
            expLable.setContentMode(Label.CONTENT_XHTML);
            layout1.addComponent(expLable);
            expTable = new ExperimentsTable(url, dbName, driver, userName, password);
            layout2.addComponent(expTable);
            l1.addComponent(layout2);
            expListStr = new TreeMap<Integer, String>();
            Integer index = null;
            for (int key : expList.keySet()) {
                index = new Integer(key);
                ExperimentBean expB = expList.get(key);
                expListStr.put(index, "\t" + expB.getName());
            }
            selectExp = new Select(null);
            selectExp.addItem(s);
            selectExp.setValue(s);
            selectExp.setNullSelectionAllowed(false);
            selectExp.setValue(s);
            for (String str : expListStr.values()) {
                selectExp.addItem(str);

            }
            selectExp.setImmediate(true);
            selectExp.addListener(this);
            selectExp.setWidth("40%");
            selectExp.setVisible(false);
            selectExp.setDescription("Please Select Dataset ");
            layout1.addComponent(selectExp);
            index = new Integer(expBean.getExpId());
            selectExp.select(index);
            selectExp.commit();
            addComponent(layout1);
            boolean checkV = false;
            if (visability) {
                checkV = true;
            };
            selectExp.select("\t" + expBean.getName());
            selectExp.commit();
            expDetails.setVisability(checkV);

        }

        return this;
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
