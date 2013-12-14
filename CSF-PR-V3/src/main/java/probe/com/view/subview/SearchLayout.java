/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
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
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.control.ExperimentHandler;
import probe.com.model.beans.ExperimentBean;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.subview.util.CustomErrorLabel;
import probe.com.view.subview.util.CustomExportBtnLayout;
import probe.com.view.subview.util.CustomExternalLink;
import probe.com.view.subview.util.GeneralUtil;
import probe.com.view.subview.util.Help;

/**
 *
 * @author Yehia Farag
 */
public class SearchLayout extends VerticalLayout implements Serializable, Button.ClickListener {

    private HorizontalLayout topLayout = new HorizontalLayout();
    private VerticalLayout downLayout = new VerticalLayout();
    private HorizontalLayout topRightLayout = new HorizontalLayout();
    private VerticalLayout topLeftLayout = new VerticalLayout();
    private TextArea searchField;
    private Select selectExp; //select exp the search method
    private OptionGroup searchbyGroup;
    private Button searchButton = new Button("");
    private ExperimentHandler handler;
    private String defaultText = "Please use one key-word per line and choose the search options";
    private String selectDatasetStr = "Search All Datasets";
    private Label searchByLabel, errorLabelI;// = new  Label();
    private CustomErrorLabel errorLabelII;
    private String selectMethodStr = "Please Select Search Method";
    private TreeMap<Integer, String> expListStr;
    private Map<Integer, ExperimentBean> expList = null;
    private int pepSearch = 0;
    private Set<String> pepSet;
    private VerticalLayout searchTableLayout = new VerticalLayout();
    private VerticalLayout protSerarchLayout = new VerticalLayout();
    private VerticalLayout pepTableLayout = new VerticalLayout();
    private VerticalLayout fractionsLayout = new VerticalLayout();
    private CustomExternalLink tableAccessionLable;
    private int key = -1;
    private String accession;
    private String otherAccession, desc;
    private GeneralUtil util = new GeneralUtil();
    private PeptidesTableLayout cpeptideLayout;
    private Map<Integer, FractionBean> fractionsList = null;
    private Map<String, ProteinBean> proteinsList;
    private OptionGroup validatedResults = new OptionGroup();
    private HorizontalLayout searchPropLayout = new HorizontalLayout();
    private boolean validatedOnly =true;

    public SearchLayout(ExperimentHandler handler, TreeMap<Integer, String> expListStr, Map<Integer, ExperimentBean> expList) {
        this.handler = handler;
        this.expListStr = expListStr;
        this.expList = expList;
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(topLayout);
        this.addComponent(downLayout);
        topLayout.addComponent(topLeftLayout);
        topLayout.addComponent(topRightLayout);
        topLeftLayout.setSpacing(true);
        topLeftLayout.setMargin(true);
        topRightLayout.setSpacing(true);
        topRightLayout.setMargin(true);
        downLayout.setSpacing(true);
        downLayout.setMargin(new MarginInfo(false, false, true, false));       

        downLayout.addComponent(searchTableLayout);
        searchTableLayout.setWidth("100%");
        downLayout.addComponent(protSerarchLayout);

        protSerarchLayout.addComponent(fractionsLayout);
        protSerarchLayout.addComponent(pepTableLayout);

        fractionsLayout.setWidth("100%");
        pepTableLayout.setWidth("100%");

        downLayout.setVisible(false);
        buildMainLayout();

    }

    private void buildMainLayout() {
        topRightLayout.removeAllComponents();
        topLeftLayout.removeAllComponents();

        //search form layout
        searchField = new TextArea();//("Searching Key ");
       // searchField.setDescription("one keyword per line, and choose the search options");
        searchField.setValue(defaultText);
        searchField.setWidth("350px");
        searchField.setImmediate(true);
        searchField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        searchField.addListener(new FieldEvents.FocusListener() {
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
        selectExp = new Select();
        selectExp.addItem(selectDatasetStr);
        selectExp.setValue(selectDatasetStr);
        selectExp.setNullSelectionAllowed(false);
        selectExp.setValue(selectDatasetStr);
        for (String str : expListStr.values()) {
            selectExp.addItem(str);
        }
        selectExp.setImmediate(true);
        selectExp.addListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                searchButton.setClickShortcut(ShortcutAction.KeyCode.ENTER);
            }
        });
        //selectExp.setDescription(selectDatasetStr);
        selectExp.setNullSelectionAllowed(false);
        selectExp.setWidth("350px");
        topLeftLayout.addComponent(selectExp);


        searchByLabel = new Label("<h4 style='font-family:verdana;color:black;'>Search By:</h4>");
        searchByLabel.setContentMode(Label.CONTENT_XHTML);
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
        validatedResults.addListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                if (validatedResults.isSelected("Validated Proteins Only")) {
                    validatedOnly = true;

                } else {
                    validatedOnly = false;
                }
            }
        });

        //topright layout
        topRightLayout.setWidth("100%");
        searchButton.setStyleName(Reindeer.BUTTON_LINK);
//        searchButton.setIcon(new ExternalResource("http://icons.iconarchive.com/icons/ampeross/qetto-2/96/search-icon.png"));
        searchButton.setIcon(new ThemeResource("img/search_22.png"));
        //searchButton.setDescription("Search");
        topRightLayout.addComponent(searchButton);
        topRightLayout.setComponentAlignment(searchButton, Alignment.BOTTOM_LEFT);
        topRightLayout.setExpandRatio(searchButton, 0.9f);
        topRightLayout.setMargin(new MarginInfo(true,true, true,false));

        Label infoLable = new Label("<div style='border:1px outset black;text-align:justify;text-justify:inter-word;'><h3 style='font-family:verdana;color:black;font-weight:bold;margin-left:20px;margin-right:20px;'>Information</h3><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Type in search keywords (one per line) and choose the search type. All experiments containing protein(s) where the keyword is found are listed. View the information about each protein from each experiment separately by selecting them from the list.</p></div>");
        infoLable.setContentMode(Label.CONTENT_XHTML);
        infoLable.setWidth("300px");
        infoLable.setStyleName(Reindeer.LAYOUT_BLUE);

      


        //errorLabelI error in search keyword 
        errorLabelI = new Label("<h3 Style='color:red;'>Please Enter Valid Key Word </h3>");
        errorLabelI.setContentMode(Label.CONTENT_XHTML);
        
        topRightLayout.addComponent(errorLabelI);
        topRightLayout.setComponentAlignment(errorLabelI, Alignment.MIDDLE_CENTER);
        errorLabelI.setVisible(false);

        topRightLayout.setExpandRatio(errorLabelI, 0.1f);

        errorLabelII = new CustomErrorLabel();
        topRightLayout.addComponent(errorLabelII);
        topRightLayout.setComponentAlignment(errorLabelII, Alignment.MIDDLE_CENTER);
        topRightLayout.setExpandRatio(errorLabelII, 0.1f);

        Help help = new Help();
        HorizontalLayout infoIco = help.getInfoNote(infoLable);
        infoIco.setMargin(new MarginInfo(false, true, false, true));
        topRightLayout.addComponent(infoIco);
        //topRightLayout.setExpandRatio(infoIco, 0.8f);
        topRightLayout.setComponentAlignment(infoIco, Alignment.MIDDLE_RIGHT);
        errorLabelII.setVisible(false);

        searchButton.addListener(this);

    }

    @Override
    public void buttonClick(Button.ClickEvent event) {

        pepSearch = 0;
        pepSet = null;
        downLayout.setVisible(false);
        searchTableLayout.removeAllComponents();
        pepTableLayout.removeAllComponents();
        fractionsLayout.removeAllComponents();

        errorLabelI.setVisible(false);
        errorLabelII.setVisible(false);
        Object searchDatasetTypeObject = selectExp.getValue();
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
                    searchProtList = handler.searchProteinByAccession(searchStr.trim(), expList, searchDatasetType,validatedOnly);
                    if (searchProtList == null || searchProtList.isEmpty()) {
                        notFound += searchStr + "\t";
                    } else {
                        fullExpProtList.putAll(searchProtList);
                    }

                }

            } else if (searchMethod.equals("Protein Name")) //case of protein name
            {
                for (String searchStr : searchSet) {
                    searchProtList = handler.searchProteinByName(searchStr.trim(), expList, searchDatasetType,validatedOnly);
                    if (searchProtList == null || searchProtList.isEmpty()) {
                        notFound += searchStr + "\t";
                    } else {
                        fullExpProtList.putAll(searchProtList);
                    }

                }
            } else //find protein by peptide sequence
            {
                pepSearch = 1;
                pepSet = searchSet;
                for (String searchStr : searchSet) {
                    searchProtList = handler.searchProteinByPeptideSequence(searchStr.trim(), expList, searchDatasetType, validatedOnly);
                    if (searchProtList == null || searchProtList.isEmpty()) {
                        notFound += searchStr + "\t";
                    } else {
                        fullExpProtList.putAll(searchProtList);
                    }

                }

            }

            //searching end here
            if (!notFound.equals("")) {
                notFind(notFound);
            }
            if (fullExpProtList == null || fullExpProtList.isEmpty()) {
                downLayout.setVisible(false);
            } else {

                downLayout.setVisible(true);
                
                final SearchResultsTableLayout searcheResultsTableLayout = new SearchResultsTableLayout(handler, expList, fullExpProtList,validatedOnly);
                searchTableLayout.addComponent(searcheResultsTableLayout);
                Property.ValueChangeListener listener = new Property.ValueChangeListener() {
                    /*
                     *
                     */
                    private static final long serialVersionUID = 1L;

                    @Override
                    public synchronized void valueChange(Property.ValueChangeEvent event) {

                        if (tableAccessionLable != null) {
                            tableAccessionLable.rePaintLable("black");
                        }

                        if (searcheResultsTableLayout.getCurrentTable().getValue() != null) {
                            key = (Integer) searcheResultsTableLayout.getCurrentTable().getValue();
                        }
                        final Item item = searcheResultsTableLayout.getCurrentTable().getItem(key);
                        tableAccessionLable = (CustomExternalLink) item.getItemProperty("Accession").getValue();
                        tableAccessionLable.rePaintLable("white");
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException iexp) {
                            System.out.println(iexp.getLocalizedMessage());
                        }
                        pepTableLayout.removeAllComponents();
                        fractionsLayout.removeAllComponents();

                        String expName = item.getItemProperty("Experiment").toString();
                        accession = item.getItemProperty("Accession").toString();
                        otherAccession = item.getItemProperty("Other Protein(s)").toString();
                        desc = item.getItemProperty("Description").toString();
                        int expId = util.getExpId(expName, expList);
                        ExperimentBean exp = expList.get(expId);
                        proteinsList = handler.getProteinsList(exp.getExpId(), expList);
                        exp.setProteinList(proteinsList);
                        expList.put(exp.getExpId(), exp);
                        CustomExportBtnLayout ce1 = new CustomExportBtnLayout(handler, "allProtPep", expId, expName, accession, otherAccession, expList, null, 0, null, null, null);
                        PopupView p1 = new PopupView("Export Peptides from All Datasets for (" + accession + " )", ce1);
                        p1.setDescription("Export CSF-PR Peptides for ( "+accession+" ) for All Available Datasets");
                            
//                        CustomExportBtnLayout ce1 = new CustomExportBtnLayout(handler, "allProtPep", expId, expName, accession, otherAccession, expList, null, 0, null, null, null);
//                        PopupView p1 = new PopupView("Export All (" + accession + ")'s Peptides", ce1);
                        searcheResultsTableLayout.setExpBtnProtAllPepTable(p1);// new PopupView("Export Proteins", (new CustomExportBtnLayout(handler, "prots",expId, expName, accession, otherAccession, expList, proteinsList, exp.getFractionsNumber(), null,null))));

                        if (key < 0) {
                        } else {
                            Set<Integer> expProPepIds = handler.getExpPepProIds(exp.getExpId(), accession, otherAccession);
                            Map<Integer, PeptideBean> pepProtList = handler.getPeptidesProtList(exp.getPeptideList(), expProPepIds);
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
                                    pepTableLayout.removeComponent(cpeptideLayout);
                                }
                                cpeptideLayout = new PeptidesTableLayout(validPep, pepProtList.size(), desc, pepProtList, accession, exp.getName());
                                pepTableLayout.setMargin(false);
                                //cpeptideLayout.setHeight("" + searcheResultsTableLayout.getCurrentTable().getHeight());
                                // pepTableLayout.setHeight("" + searcheResultsTableLayout.getCurrentTable().getHeight());
                                pepTableLayout.addComponent(cpeptideLayout);
                                CustomExportBtnLayout ce3 = new CustomExportBtnLayout(handler, "protPep", exp.getExpId(), exp.getName(), accession, otherAccession, expList, null, 0, pepProtList, null, null);
                                PopupView p3 = new PopupView("Export Peptides from Selected Dataset for (" + accession + " )", ce3);
                                cpeptideLayout.setExpBtnPepTable(p3,accession,exp.getName());


                            }
                            fractionsList = handler.getFractionsList(exp.getExpId(), expList);
                            
                            
                            List<StandardProteinBean> standardProtPlotList = handler.getStandardProtPlotList(exp.getExpId());
                            if (exp == null || exp.getReady() != 2 || standardProtPlotList == null || standardProtPlotList.isEmpty() || fractionsList == null || fractionsList.isEmpty()) {
                                if (searcheResultsTableLayout.getCurrentTable() != null) {
                                    searcheResultsTableLayout.getCurrentTable().setHeight("267.5px");
                                    if( cpeptideLayout.getPepTable() != null)
                                        cpeptideLayout.getPepTable().setHeight("267.5px");
                                }

                            } else {

                                if (exp != null && exp.getProteinList() != null) {
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
//                                    while (true) {
//                                        if (fractionsList.size() > 0) {
//                                            break;
//                                        }
//                                    }
                                    Map<Integer, ProteinBean> proteinFractionAvgList = handler.getProteinFractionAvgList(accession+","+otherAccession, fractionsList, exp.getExpId());
                                     if (proteinFractionAvgList == null || proteinFractionAvgList.isEmpty()) {
                                            fractionsLayout.removeAllComponents();
                                        } else {
                                    FractionsLayout flo = new FractionsLayout(accession, mw, proteinFractionAvgList, standardProtPlotList, exp.getName());
                                    flo.setMargin(new MarginInfo(false, false, false, false));
                                    fractionsLayout.addComponent(flo);
                                     }
                                } else {
//                                    Notification.show("YOU NEED TO SELECT A READY DATASET FIRST!");
                                }
                            }
                        }
                    }
                };
                searcheResultsTableLayout.setListener(listener);
                searcheResultsTableLayout.getCurrentTable().addListener(listener);
                downLayout.setVisible(true);
            }
        }
    }

    private void notFind(String notFound) {
        errorLabelII.updateErrot(notFound);
        errorLabelII.setVisible(true);


    }
}
