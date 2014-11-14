/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.event.FieldEvents;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Select;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import com.vaadin.ui.themes.Runo;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.vaadin.actionbuttontextfield.ActionButtonTextField;
import org.vaadin.actionbuttontextfield.widgetset.client.ActionButtonType;
import probe.com.control.ExperimentHandler;
import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.subview.util.CustomExportBtnLayout;
import probe.com.view.subview.util.CustomExternalLink;
import probe.com.view.subview.util.GeneralUtil;
import probe.com.view.subview.util.Help;

/**
 *
 * @author Yehia Farag
 */
public final class ProteinsLayout extends VerticalLayout implements Serializable, Property.ValueChangeListener {

    private final TreeMap<Integer, String> expListStr;
    private boolean visability = false;
    private Select selectExp;
    private int key = -1;
    private VerticalLayout l1;
    private Map<Integer, ExperimentBean> expList = null;
    private ExperimentDetails expDetails;
    private CustomExternalLink l;
    private VerticalLayout peptideLayout;
    private ExperimentBean exp;
    private final String s = "\t \t \t Please Select Dataset";
    private int starter = 1;
    private final ExperimentHandler expHandler;
    private ProteinsTableLayout protTableLayout;
    private String accession;
    private String otherAccession;
    private Map<Integer, FractionBean> fractionsList = null;
    private PeptidesTableLayout cpeptideLayout;
    private VerticalLayout fractionLayout;
    private Map<String, ProteinBean> proteinsList;
    private final VerticalLayout typeILayout;
    private final GeneralUtil util = new GeneralUtil();
    private TreeMap<Integer, Integer> selectionIndexes;
    private int nextIndex;
    private Help help = new Help();
    private final Map<Integer,Integer> datasetIndex;

    public ProteinsLayout(ExperimentHandler expHandler, TreeMap<Integer, String> expListStr, Map<Integer, ExperimentBean> expList,Map<Integer,Integer> datasetIndex) {
        this.expHandler = expHandler;
        this.expListStr = expListStr;
        this.expList = expList;
        this.datasetIndex = datasetIndex;
        this.setSizeFull();
        setMargin(true);
        typeILayout = new VerticalLayout();
        typeILayout.setVisible(false);
        buildMainLayout();
    }

    public void buildMainLayout() {
        this.setWidth("100%");
        this.setHeight("100%");
        this.removeAllComponents();
        VerticalLayout vlo = new VerticalLayout();
        vlo.setStyleName(Reindeer.LAYOUT_WHITE);
        vlo.setHeight("70px");
        vlo.setWidth("100%");
        this.addComponent(vlo);
        if (expList == null || expList.isEmpty()) {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(Label.CONTENT_XHTML);
            vlo.addComponent(noExpLable);
        } else {
            Label expLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Select Dataset :</h4>");
            expLable.setContentMode(Label.CONTENT_XHTML);
            expLable.setHeight("35px");
            vlo.addComponent(expLable);
            vlo.setComponentAlignment(expLable, Alignment.TOP_LEFT);


            HorizontalLayout selectLayout = new HorizontalLayout();
            selectLayout.setWidth("100%");
            vlo.addComponent(selectLayout);
            selectExp = new Select();

            selectExp.setStyleName(Runo.PANEL_LIGHT);
            selectExp.addItem(s);
            selectExp.setValue(s);
            selectExp.setNullSelectionAllowed(false);
            selectExp.setValue(s);
            selectExp.focus();

            for (String str : expListStr.values()) {
                selectExp.addItem(str);
            }
            selectExp.setImmediate(true);
            selectExp.addListener(this);
            selectExp.setWidth("90%");
//            selectExp.setDescription("Please Select Dataset");
            selectExp.setNullSelectionAllowed(false);
            selectLayout.addComponent(selectExp);
            selectLayout.setComponentAlignment(selectExp, Alignment.TOP_LEFT);
            //selectLayout.setExpandRatio(selectExp, 0.4f);

            Label infoLable = new Label("<div style='border:1px outset black;text-align:justify;text-justify:inter-word;'><h3 style='font-family:verdana;color:black;font-weight:bold;margin-left:20px;margin-right:20px;'>Information</h3><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Select an experiment in the roll down menu on top to view all proteins identified in the selected experiment. Select a protein to see below all Peptides identified for the protein, and if the experiment was based on SDS-PAGE, the protein’s distribution in the gel is displayed under Fractions. To show information about the experiment, press Dataset Information.  Use the search box to navigate in the experiment selected.</p><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Under Fractions, bar charts show the distribution of the selected protein across the fractions cut from the gel. Three charts show number of peptides, number of spectra and average precursor intensity. The fraction number represents the gel pieces cut from top to bottom. Protein standards <font color='#CDE1FF'>(light blue bars)</font> indicate the molecular weight range of each fraction. <font color='#79AFFF'>Darker blue bars</font> mark between which two standards the protein's theoretical mass suggests the protein should be found.</p></div>");
            infoLable.setContentMode(Label.CONTENT_XHTML);
            infoLable.setWidth("450px");
            
            // infoLable.setStyleName(Reindeer.LAYOUT_BLUE);

            help = new Help();
            HorizontalLayout infoIco = help.getInfoNote(infoLable);
            selectLayout.addComponent(infoIco);
            selectLayout.setComponentAlignment(infoIco, Alignment.TOP_RIGHT);
//            lowerLayout.setExpandRatio(helpIco, 0.4f);
            this.addComponent(typeILayout);
        }
    }

    public TreeMap<Integer, String> getExpListStr() {
        return expListStr;
    }

    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        Object o = selectExp.getValue();
        if (o != null && (!o.toString().equals(s))) {

            String str = selectExp.getValue().toString();
            key = util.getKey(expListStr, str,datasetIndex);
            exp = expList.get(key);
            //layout for dataset type 1
            if (exp != null && exp.getExpType() == 1) {
                typeILayout.removeAllComponents();
                typeILayout.setVisible(true);
                if (exp.getReady() != 2 && exp.getFractionsNumber() > 0 || exp.getProteinsNumber() == 0) {
                    Notification.show("THIS DATASET NOT READY YET!");
                } else {
                    starter = 1;
                    if (expDetails != null) {
                        visability = expDetails.isVisability();
                        typeILayout.removeComponent(expDetails);
                    }
                    expDetails = this.buildExpView(exp, visability);//get experiment details view
                    typeILayout.addComponent(expDetails);
                    typeILayout.setComponentAlignment(expDetails, Alignment.TOP_LEFT);
                    proteinsList = expHandler.getProteinsList(exp.getExpId(), expList);
                    exp.setProteinList(proteinsList);
                    expList.put(exp.getExpId(), exp);
                    if (protTableLayout != null) {
                        typeILayout.removeComponent(protTableLayout);
                    }
                    protTableLayout = new ProteinsTableLayout(proteinsList, exp, expList, expHandler);
                    typeILayout.addComponent(protTableLayout);
                    typeILayout.setComponentAlignment(protTableLayout, Alignment.TOP_LEFT);

                    ValueChangeListener listener = new Property.ValueChangeListener() {
                        /*
                         *
                         */
                        private static final long serialVersionUID = 1L;

                        @Override
                        public synchronized void valueChange(Property.ValueChangeEvent event) {
                            if (l != null) {
                                l.rePaintLable("black");
                            }
                            if (starter != 1) {
                                expDetails.hideDetails();
                            }
                            starter++;
                            String desc = "";

                            //fraction layout
                            if (fractionLayout != null) {
                                removeComponent(fractionLayout);
                            }
                            fractionLayout = new VerticalLayout();
                            addComponent(fractionLayout);
                            fractionLayout.setWidth("100%");

                            //peptide layout
                            if (peptideLayout != null) {
                                removeComponent(peptideLayout);
                            }
                            peptideLayout = new VerticalLayout();
                            addComponent(peptideLayout);
                            peptideLayout.setWidth("100%");


                            if (protTableLayout.getProtTable().getValue() != null) {
                                key = (Integer) protTableLayout.getProtTable().getValue();
                            }
                            final Item item = protTableLayout.getProtTable().getItem(key);
                            l = (CustomExternalLink) item.getItemProperty("Accession").getValue();
                            l.rePaintLable("white");
                            try {
                                Thread.sleep(10);
                            } catch (InterruptedException iexp) {
                                System.out.println(iexp.getLocalizedMessage());
                            }
                            desc = item.getItemProperty("Description").toString();
                            accession = item.getItemProperty("Accession").toString();
                            otherAccession = item.getItemProperty("Other Protein(s)").toString();

                            CustomExportBtnLayout ce1 = new CustomExportBtnLayout(expHandler, "allProtPep", exp.getExpId(), exp.getName(), accession, otherAccession, expList, null, 0, null, null, null);
                            CustomExportBtnLayout ce2 = (new CustomExportBtnLayout(expHandler, "prots", exp.getExpId(), exp.getName(), accession, otherAccession, expList, proteinsList, exp.getFractionsNumber(), null, null, null));

                            PopupView p1 = new PopupView("Export Peptides from All Datasets for ( "+accession+" )", ce1);
                            p1.setDescription("Export CSF-PR Peptides for ( "+accession+" ) from All Datasets");
                            PopupView p2 = new PopupView("Export All Proteins from Selected Dataset", ce2);
                            p2.setDescription("Export All Proteins from ( "+ exp.getName()+" ) Dataset");
                            
                           //   HorizontalLayout p1 = help.getExpIcon(ce1,"Export CSF-PR Peptides for ( "+accession+" ) for all datasets.","CSF-PR Peptides for ( "+accession+" )");
                           //   HorizontalLayout p2 = help.getExpIcon(ce2,"Export CSF-PR Proteins for ( "+ exp.getName()+" )","Export CSF-PR Proteins for ( "+ exp.getName()+" )" );
       

                            protTableLayout.setExpBtnProtAllPepTable(p1, p2);
                            if (key < 0) {
                            } else {
                                exp = expList.get(exp.getExpId());
                                Set<Integer> expProPepIds = expHandler.getExpPepProIds(exp.getExpId(), accession, otherAccession);

                                Map<Integer, PeptideBean> pepProtList = expHandler.getPeptidesProtList(exp.getPeptideList(), expProPepIds);
                                if (exp.getPeptideList() == null) {
                                    exp.setPeptideList(pepProtList);
                                } else {
                                    exp.getPeptideList().putAll(pepProtList);
                                }
                                expList.put(exp.getExpId(), exp);
                                if (pepProtList.isEmpty()) {
                                } else {
                                    int validPep = util.getValidatedPepNumber(pepProtList);
                                    if (cpeptideLayout != null) {
                                        peptideLayout.removeComponent(cpeptideLayout);
                                    }
                                    cpeptideLayout = new PeptidesTableLayout(validPep, pepProtList.size(), desc, pepProtList, accession, exp.getName());
                                    peptideLayout.setMargin(true);
                                    cpeptideLayout.setHeight("" + protTableLayout.getHeight());
                                    peptideLayout.setHeight("" + protTableLayout.getHeight());
                                    peptideLayout.addComponent(cpeptideLayout);
                                    CustomExportBtnLayout ce3 = new CustomExportBtnLayout(expHandler, "protPep", exp.getExpId(), exp.getName(), accession, otherAccession, expList, null, 0, pepProtList, null, null);
                                    PopupView p3 = new PopupView("Export Peptides from Selected Dataset for ( " + accession + " )", ce3);
                                    p3.setDescription("Export Peptides from ( "+exp.getName()+" ) Dataset for ( " + accession + " )");
                                    cpeptideLayout.setExpBtnPepTable(p3);


                                }
                                fractionsList = expHandler.getFractionsList(exp.getExpId(), expList);
                                List<StandardProteinBean> standardProtPlotList = expHandler.getStandardProtPlotList(exp.getExpId());
                                if (exp == null || standardProtPlotList == null || standardProtPlotList.isEmpty() || fractionsList == null || fractionsList.isEmpty()) {
                                    if (protTableLayout.getProtTable() != null) {
                                        protTableLayout.getProtTable().setHeight("267.5px");
                                        protTableLayout.setProtSize("267.5px");
                                    }


                                } else {

                                    if (exp != null && exp.getProteinList() != null) {
                                        exp.setFractionsList(fractionsList);
                                        expList.put(exp.getExpId(), exp);
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
                                        }
                                        while (true) {
                                            if (fractionsList.size() > 0) {
                                                break;
                                            }
                                        }
                                        Map<Integer, ProteinBean> proteinFractionAvgList = expHandler.getProteinFractionAvgList(accession+ "," + otherAccession, fractionsList, exp.getExpId());
                                        if (proteinFractionAvgList == null || proteinFractionAvgList.isEmpty()) {
                                            fractionLayout.removeAllComponents();
                                        } else {
                                            fractionLayout.addComponent(new FractionsLayout(
                                                    accession, mw, proteinFractionAvgList, standardProtPlotList, exp.getName()));
                                        }
                                    } else {
//                                        Notification.show("YOU NEED TO SELECT A READY DATASET FIRST!");
                                    }
                                }
                            }
                        }
                    };
                    //add prot table listener
                    protTableLayout.getProtTable().addListener(listener);
                    protTableLayout.setListener(listener);
                    selectionIndexes = util.getSearchIndexesSet(protTableLayout.getProtTable().getTableSearchMap(), protTableLayout.getProtTable().getTableSearchMapIndex(), protTableLayout.getSearchField().getValue().toString().toUpperCase().trim());
                    protTableLayout.getProtTable().setCurrentPageFirstItemId(protTableLayout.getProtTable().getFirstIndex());
                    protTableLayout.getProtTable().select(protTableLayout.getProtTable().getFirstIndex());
                    protTableLayout.getProtTable().commit();
                    ActionButtonTextField searchButtonTextField = ActionButtonTextField.extend(protTableLayout.getSearchField());
                    searchButtonTextField.getState().type = ActionButtonType.ACTION_SEARCH;
                    searchButtonTextField.addClickListener(new ActionButtonTextField.ClickListener() {
                        @Override
                        public void buttonClick(ActionButtonTextField.ClickEvent clickEvent) {
                            selectionIndexes = util.getSearchIndexesSet(protTableLayout.getProtTable().getTableSearchMap(), protTableLayout.getProtTable().getTableSearchMapIndex(), protTableLayout.getSearchField().getValue().toString().toUpperCase().trim());
                            if (!selectionIndexes.isEmpty()) {
                                if (selectionIndexes.size() > 1) {
                                    protTableLayout.getNextSearch().setEnabled(true);
                                    protTableLayout.getNextSearch().focus();
                                } else {
                                    protTableLayout.getNextSearch().setEnabled(false);
                                }
                                protIndex = 1;
                                nextIndex = selectionIndexes.firstKey();
                                protTableLayout.getProtCounter().setValue("( " + (protIndex++) + " of " + selectionIndexes.size() + " )");
                                protTableLayout.getProtTable().setCurrentPageFirstItemId(selectionIndexes.get(nextIndex));
                                protTableLayout.getProtTable().select(selectionIndexes.get(nextIndex));
                                protTableLayout.getProtTable().commit();

                            } else {
                                Notification.show("Not Exist");
                                protIndex = 1;
                            }

                        }
                    });

                    protTableLayout.getSearchField().addFocusListener(new FieldEvents.FocusListener() {
                        @Override
                        public void focus(FieldEvents.FocusEvent event) {
                            protTableLayout.getNextSearch().setEnabled(false);
                            protTableLayout.getProtCounter().setValue("");

                            protIndex = 1;

                        }
                    });

                    protTableLayout.getNextSearch().addListener(new Button.ClickListener() {
                        @Override
                        public void buttonClick(Button.ClickEvent event) {
                            nextIndex = selectionIndexes.higherKey(nextIndex);
                            protTableLayout.getProtTable().setCurrentPageFirstItemId(selectionIndexes.get(nextIndex));

                            protTableLayout.getProtTable().select(selectionIndexes.get(nextIndex));
                            protTableLayout.getProtTable().commit();
                            protTableLayout.getProtCounter().setValue("( " + (protIndex++) + " of " + selectionIndexes.size() + " )");
                            if (nextIndex == selectionIndexes.lastKey()) {
                                nextIndex = selectionIndexes.firstKey() - 1;
                                protIndex = 1;
                            }
                        }
                    });


                }
            }
        }
    }
    private int protIndex = 1;

    public Map<Integer, ExperimentBean> getExpList() {
        return expList;
    }

    public void setExpList(Map<Integer, ExperimentBean> expList) {
        this.expList = expList;
    }

    private ExperimentDetails buildExpView(ExperimentBean exp, boolean visability2) {
        return new ExperimentDetails(exp, visability2, expHandler);
    }
}