/*
 */
package probe.com.view;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.util.converter.StringToIntegerConverter;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.TextArea;
import com.vaadin.ui.TextField;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.FractionBean;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.view.components.FiltersControl;
import probe.com.view.components.SearchResultsTableLayout;
import probe.com.view.core.ComboBoxFilter;
import probe.com.view.core.CustomErrorLabel;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.IconGenerator;
import probe.com.view.core.ListSelectFilter;
import probe.com.view.core.OptionGroupFilter;
import probe.com.view.core.TextFieldFilter;

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
    private ComboBoxFilter selectDatasetDropdownList; //select dataset the search method
    private OptionGroupFilter searchbyGroup;
    private final VerticalLayout topMiddleSearchButtonsLayout = new VerticalLayout();
    private final VerticalLayout advancedSearchLayoutContainer = new VerticalLayout();
    private final HorizontalLayout advancedSearchLayout = new HorizontalLayout();
    private final Button searchButton = new Button("");
    private final Button advancedSearchButton = new Button("Advanced Search");
    private final MainHandler handler;
    private String defaultText = "Please use one key-word per line and choose the search options";
    private final String Select_All_Dataset_Str = "Search All Datasets";
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
    private final FiltersControl filtersController = new FiltersControl();
    private final OptionGroupFilter validatedResults = new OptionGroupFilter(filtersController,5, true);
    private final HorizontalLayout searchPropLayout = new HorizontalLayout();
    private boolean validatedOnly = true, availableRawData = false;
    private HorizontalLayout searchDatatypeLayout;
    
    private final OptionGroupFilter searchDatatypeSelect = new OptionGroupFilter(filtersController,2,false);
    
//    final OptionGroup typeOfStudySelection = new OptionGroup();
    private String typeofStudy, sampleType, patientGroupValueI, patientGroupValueII, patientSubGroupValueI, patientSubGroupValueII, technologyType, analyticalApproachI, analyticalMethod, shotgunTargetedQuant;

    
    private final Button doneFilterBtn = new Button("Done");
    /**
     *
     * @param handler dataset handler
     */
    public SearchLayout(MainHandler handler) {
        this.handler = handler;
        this.datasetNamesList = handler.getDatasetNamesList();
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(topLayout);
        this.addComponent(filtersController);
        //errorLabelI error in search keyword 
        initErrorLabels();

        this.addComponent(searchLayout);
        topLayout.addComponent(topLeftLayout);
        topLayout.addComponent(topRightLayout);
        topLeftLayout.setSpacing(true);
        topLeftLayout.setMargin(true);
        topRightLayout.setSpacing(true);
        topRightLayout.setMargin(true);
        searchLayout.setSpacing(true);
        searchLayout.setMargin(new MarginInfo(false, false, true, false));   
        filtersController.setVisible(true);
        filtersController.setSpacing(true);
        searchLayout.addComponent(searchTableLayout);
        searchTableLayout.setWidth("100%");
        searchLayout.addComponent(protSerarchLayout);

        protSerarchLayout.addComponent(fractionsLayout);
        protSerarchLayout.addComponent(peptidesLayout);

        fractionsLayout.setWidth("100%");
        peptidesLayout.setWidth("100%");

        searchLayout.setVisible(false);
        buildMainSearchPanelLayout();

    }

    /**
     * initialize main search tab layout initialize drop down select dataset and
     * different filters
     */
    private void buildMainSearchPanelLayout() {        
        initTopLeftLayout();
        initTopRightLayout();
        
        
    }

   
    /**
     * initialize top left side for the search layout
     */
    private void initTopLeftLayout() {

        //search form layout
        topLeftLayout.removeAllComponents();
        initSearchTextArea();
        //secound main filter for identification -quantification or both        
        initDataTypeFilter();
        //init search by (3rd filter)
        initSearchByFilter();
        initValidProtFilter();
       
    }
    
    private void initSearchTextArea(){
        
         searchField = new TextArea();
        searchField.setWidth("350px");
        searchField.setImmediate(true);
        searchField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        searchField.setInputPrompt(defaultText);
        searchField.addFocusListener(new FieldEvents.FocusListener() {

            @Override
            public void focus(FieldEvents.FocusEvent event) {
                searchbyGroup.setEnabled(true); 
                searchbyGroup.setNullSelectionAllowed(false);
                    if (searchbyGroup.getValue() == null) {
                        searchbyGroup.select("Protein Accession");
                    }
            }
        });
        searchField.addBlurListener(new FieldEvents.BlurListener() {

            @Override
            public void blur(FieldEvents.BlurEvent event) {
                if (searchField.getValue().trim().equals("")) {
                    searchbyGroup.setEnabled(false);

                    searchbyGroup.setNullSelectionAllowed(true);
                    searchbyGroup.select(null);
                } else {
                    searchbyGroup.setEnabled(true);

                    searchbyGroup.setNullSelectionAllowed(false);
                    if (searchbyGroup.getValue() == null) {
                        searchbyGroup.select("Protein Accession");
                    }
                }
            }
        });

        topLeftLayout.addComponent(searchField);

    
    
    
    }
    private void initDataTypeFilter(){
        searchDatatypeSelect.addItems(Arrays.asList(new String[]{"Identification", "Quantification", "Both"}));
        searchDatatypeSelect.setWidth("150px");
        Property.ValueChangeListener searchDatatypeListener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectDatasetDropdownList.select(Select_All_Dataset_Str);
                selectDatasetDropdownList.commit();
                if (searchDatatypeSelect.getValue().toString().equalsIgnoreCase("Identification")) {
                    selectDatasetDropdownList.setVisible(true);

                    advancedSearchLayout.setVisible(false);
                    advancedSearchButton.setEnabled(false);

                } else {
                    selectDatasetDropdownList.setVisible(false);
                    advancedSearchButton.setEnabled(true);
                    selectDatasetDropdownList.select(defaultText);
                    
                }
            }
        };
        searchDatatypeSelect.addValueChangeListener(searchDatatypeListener);

        //select dataset for searching
         String[] temArr = new String[datasetNamesList.values().size()]; 
        int index=0;
        for (String str : datasetNamesList.values()) {
            temArr[index++]=str;
        }
        selectDatasetDropdownList = new ComboBoxFilter(filtersController,3,Select_All_Dataset_Str,temArr);
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
        selectDatasetDropdownList.setWidth("200px");
        searchDatatypeLayout = new HorizontalLayout();
        searchDatatypeLayout.setWidth("100%");
        searchDatatypeLayout.addComponent(searchDatatypeSelect);
        searchDatatypeLayout.addComponent(selectDatasetDropdownList);
        searchDatatypeLayout.setComponentAlignment(selectDatasetDropdownList, Alignment.TOP_RIGHT);
        searchDatatypeSelect.select("Identification");
        searchDatatypeSelect.commit();
        selectDatasetDropdownList.select(Select_All_Dataset_Str);
        selectDatasetDropdownList.commit();
        

    
    }
    private void initSearchByFilter(){
        topLeftLayout.addComponent(searchDatatypeLayout);
        searchByLabel = labelGenerator("Search By:");
        topLeftLayout.addComponent(searchByLabel);
        topLeftLayout.addComponent(searchPropLayout);
        searchPropLayout.setWidth("300px");        
        //init search by        
        searchbyGroup = new OptionGroupFilter(filtersController,4,true);
        searchbyGroup.setWidth("350px");
        searchbyGroup.setDescription(selectMethodStr);
        searchbyGroup.setEnabled(false);
        searchbyGroup.setNullSelectionAllowed(true);
        // Use the single selection mode.
        
        searchbyGroup.addItem("Protein Accession");
        searchbyGroup.addItem("Protein Name");
        searchbyGroup.addItem("Peptide Sequence");
        searchPropLayout.addComponent(searchbyGroup);}
    private void initValidProtFilter(){
     validatedResults.setMultiSelect(true);
        validatedResults.setNullSelectionAllowed(true);

        validatedResults.addItem("Validated Proteins Only");
        validatedResults.setHeight("15px");
        searchPropLayout.addComponent(validatedResults);
        searchPropLayout.setComponentAlignment(validatedResults, Alignment.MIDDLE_RIGHT);

//        validatedResults.setImmediate(true);
        validatedResults.addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                validatedOnly = validatedResults.isSelected("Validated Proteins Only");
            }
        });
        validatedResults.select("Validated Proteins Only");
}
    private void initErrorLabels() {
        errorLabelI = new Label("<h4 Style='color:red;'>Please Enter Valid Key Word </h4>");
        errorLabelI.setContentMode(ContentMode.HTML);
        errorLabelI.setHeight("30px");
        this.addComponent(errorLabelI);
        this.setComponentAlignment(errorLabelI, Alignment.TOP_LEFT);
        errorLabelI.setVisible(false);
        errorLabelII = new CustomErrorLabel();
        this.addComponent(errorLabelII);
        this.setComponentAlignment(errorLabelII, Alignment.TOP_LEFT);
        errorLabelII.setVisible(false);

    }
    /**
     * initialize top right side for the search layout
     */
    private void initTopRightLayout(){
     //topright layout
        topRightLayout.removeAllComponents();
        topRightLayout.setWidth("100%");
        topRightLayout.setHeight("100%");
        
        //middle layout search buttons
        searchButton.setStyleName(Reindeer.BUTTON_LINK);
        searchButton.setIcon(new ThemeResource("img/search_22.png"));
        advancedSearchButton.setDescription("Activated with Quantification Data Search");
        advancedSearchButton.setStyleName(Reindeer.BUTTON_LINK);
        advancedSearchButton.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
                advancedSearchLayout.setVisible(!advancedSearchLayout.isVisible());
                topMiddleSearchButtonsLayout.setVisible(!topMiddleSearchButtonsLayout.isVisible());
                advancedSearchLayoutContainer.setWidth("850px");

            }
        });

        //init advanced search layout container
        advancedSearchLayoutContainer.setWidth("700px");
        advancedSearchLayoutContainer.setHeight("100%");
        advancedSearchLayoutContainer.addComponent(advancedSearchLayout);

        advancedSearchLayout.setSizeFull();
        advancedSearchLayout.removeAllComponents();
        advancedSearchLayout.setMargin(new MarginInfo(false, true, false, true));
        advancedSearchLayout.setVisible(false);
        
        initAdvancedSearchLeftLayout();
        initAdvancedSearchMiddleLayout();
        initAdvancedSearchRightLayout();

        topMiddleSearchButtonsLayout.setSpacing(true);
        topMiddleSearchButtonsLayout.addComponent(searchButton);
        topMiddleSearchButtonsLayout.addComponent(advancedSearchButton);
        topMiddleSearchButtonsLayout.setComponentAlignment(advancedSearchButton, Alignment.BOTTOM_CENTER);

        topRightLayout.addComponent(topMiddleSearchButtonsLayout);
        topRightLayout.setComponentAlignment(topMiddleSearchButtonsLayout, Alignment.TOP_LEFT);
        topRightLayout.setExpandRatio(topMiddleSearchButtonsLayout, 0.9f);
        topRightLayout.addComponent(advancedSearchLayoutContainer);
        topRightLayout.setExpandRatio(advancedSearchLayoutContainer, 4);
        topRightLayout.setMargin(new MarginInfo(true, true, true, false));
       
        Label infoLable = new Label("<div style='border:1px outset black;text-align:justify;text-justify:inter-word;'><h3 style='font-family:verdana;color:black;font-weight:bold;margin-left:20px;margin-right:20px;'>Information</h3><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Type in search keywords (one per line) and choose the search type. All experiments containing protein(s) where the keyword is found are listed. View the information about each protein from each experiment separately by selecting them from the list.</p></div>");
        infoLable.setContentMode(ContentMode.HTML);
        infoLable.setWidth("300px");
        infoLable.setStyleName(Reindeer.LAYOUT_BLUE);
        //init last part info lable layout
        IconGenerator help = new IconGenerator();
        HorizontalLayout infoIco = help.getInfoNote(infoLable);
        infoIco.setMargin(new MarginInfo(false, true, false, true));
        topRightLayout.addComponent(infoIco);
        topRightLayout.setComponentAlignment(infoIco, Alignment.TOP_RIGHT);
        searchButton.addClickListener(this);
    }

    private void initAdvancedSearchLeftLayout() {
      
        VerticalLayout rightFiltersLayout = new VerticalLayout();
        rightFiltersLayout.setSpacing(true);
        advancedSearchLayout.addComponent(rightFiltersLayout);  
        //pumedID filter
        initPubmedIdFilter(rightFiltersLayout);
        initRawDataAvailableFilter(rightFiltersLayout);
        //select study type
        initStudyTypeFilter(rightFiltersLayout);
        initSampleTypeFilter(rightFiltersLayout);
         // select Technology
        initTechnologyFilter(rightFiltersLayout);
        
        // Analytical approach 1
//        / Label technologyLabel = labelGenerator("Technology:");
//        rightFiltersLayout.addComponent(technologyLabel);
//        rightFiltersLayout.setComponentAlignment(technologyLabel, Alignment.TOP_LEFT);
        final ComboBox selectAnalyticalApproach1 = new ComboBox();
        selectAnalyticalApproach1.addItem("Analytical Approach 1");
        selectAnalyticalApproach1.setValue("Analytical Approach 1");
        selectAnalyticalApproach1.setNullSelectionAllowed(false);
        for (String str : new String[]{"label-free", "SELDI", "MALDI", "TMT"}) {
            selectAnalyticalApproach1.addItem(str);
        }
        selectAnalyticalApproach1.setImmediate(true);
        selectAnalyticalApproach1.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (!selectAnalyticalApproach1.getValue().toString().equalsIgnoreCase("Analytical Approach 1")) {
                    analyticalApproachI = selectAnalyticalApproach1.getValue().toString();
                } else {
                    technologyType = "";
                }
                System.out.println("Analytical Approach 1  " + analyticalApproachI);
            }
        });
        selectAnalyticalApproach1.setNullSelectionAllowed(false);
        selectAnalyticalApproach1.setWidth("190px");

        rightFiltersLayout.addComponent(selectAnalyticalApproach1);
        rightFiltersLayout.setComponentAlignment(selectAnalyticalApproach1, Alignment.TOP_LEFT);

        //Analytical method
        final ComboBox selectAnalyticalMethod = new ComboBox();
        selectAnalyticalMethod.addItem("Analytical Method");
        selectAnalyticalMethod.setValue("Analytical Method");
        selectAnalyticalMethod.setNullSelectionAllowed(false);
        for (String str : new String[]{"SIS", "SRM"}) {
            selectAnalyticalMethod.addItem(str);
        }
        selectAnalyticalMethod.setImmediate(true);
        selectAnalyticalMethod.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (!selectAnalyticalMethod.getValue().toString().equalsIgnoreCase("Analytical Method")) {
                    analyticalMethod = selectAnalyticalMethod.getValue().toString();
                } else {
                    analyticalMethod = "";
                }
                System.out.println("Analytical Method  " + analyticalMethod);
            }
        });
        selectAnalyticalMethod.setNullSelectionAllowed(false);
        selectAnalyticalMethod.setWidth("190px");

        rightFiltersLayout.addComponent(selectAnalyticalMethod);
        rightFiltersLayout.setComponentAlignment(selectAnalyticalMethod, Alignment.TOP_LEFT);

    }

    private void initPubmedIdFilter(VerticalLayout rightFiltersLayout){
         rightFiltersLayout.addComponent(new TextFieldFilter(filtersController,6,"PumedID"));
    }
    private void initRawDataAvailableFilter(VerticalLayout rightFiltersLayout){
        
        final OptionGroupFilter rawDataAvailable = new OptionGroupFilter(filtersController,7, true);
        rawDataAvailable.setMultiSelect(true);
        rawDataAvailable.addItem("Raw Data Available");
        rawDataAvailable.setNullSelectionAllowed(true);
        rightFiltersLayout.addComponent(rawDataAvailable);
        rightFiltersLayout.setComponentAlignment(rawDataAvailable, Alignment.TOP_LEFT);  
    }
    private void initStudyTypeFilter(VerticalLayout rightFiltersLayout){
     final ComboBoxFilter selectStudyType = new ComboBoxFilter(filtersController,8, "Study Type", new String[]{"Discovery", "verification"});
        selectStudyType.setWidth("190px");
        rightFiltersLayout.addComponent(selectStudyType);
        rightFiltersLayout.setComponentAlignment(selectStudyType, Alignment.TOP_LEFT);
    }
    private void initSampleTypeFilter(VerticalLayout rightFiltersLayout) {
        final ComboBoxFilter selectSampleType = new ComboBoxFilter(filtersController,9, "Sample Type", new String[]{"CSF", "Plasma", "Serum"});
        selectSampleType.setWidth("190px");
        rightFiltersLayout.addComponent(selectSampleType);
        rightFiltersLayout.setComponentAlignment(selectSampleType, Alignment.TOP_LEFT);

    }
    private void initTechnologyFilter(VerticalLayout rightFiltersLayout) {
        final ListSelectFilter selectTechnology = new ListSelectFilter(filtersController,10, "Technology", new String[]{"Mass spectrometry", "WB", "ELISA", "2DE"});
        selectTechnology.setWidth("190px");
        rightFiltersLayout.addComponent(selectTechnology);
        rightFiltersLayout.setComponentAlignment(selectTechnology, Alignment.TOP_LEFT);
    }


    
    
    
    
    
    
    
    private void initAdvancedSearchMiddleLayout() {
        VerticalLayout middleFiltersLayout = new VerticalLayout();
        middleFiltersLayout.setSpacing(true);
        advancedSearchLayout.addComponent(middleFiltersLayout);
        Label patientsGroupINum = labelGenerator("# Patients Group 1:");
        middleFiltersLayout.addComponent(patientsGroupINum);

        HorizontalLayout intSearchField = new HorizontalLayout();
        intSearchField.setWidth("200px");
        intSearchField.setHeight("20px");
        Label lessThanLabel = new Label("Less Than <");
        lessThanLabel.setStyleName(Reindeer.LABEL_SMALL);

        intSearchField.addComponent(lessThanLabel);

        final TextField patientsGroupINumField = textFieldGenerator("Only Digits Allowed");
        patientsGroupINumField.setWidth("95px");
        patientsGroupINumField.setNullRepresentation(" ");

        patientsGroupINumField.setConverter(new StringToIntegerConverter());
        patientsGroupINumField.addValueChangeListener(new Property.ValueChangeListener() {
            /*
             *the main listener for search table
             */
            private static final long serialVersionUID = 1L;

            /*
             *
             * @param event value change on search table selection
             */
            @Override
            public synchronized void valueChange(Property.ValueChangeEvent event) {
                patientsGroupINumField.validate();
                if (patientsGroupINumField.isValid()) {
                    patientsGroupINumField.setComponentError(null);
//                    System.out.println(" its ok ");
                }
            }
        });
        intSearchField.addComponent(patientsGroupINumField);
        middleFiltersLayout.addComponent(intSearchField);

//patient groups dropdown list
//         Label patientsGroupI = labelGenerator("Patients group 1");
//        middleFiltersLayout.addComponent(patientsGroupI);
        final ComboBox patientGroupI = new ComboBox();
        patientGroupI.addItem("Patients Group 1");
        patientGroupI.setValue("Patients Group 1");
        patientGroupI.setNullSelectionAllowed(false);
        for (String str : new String[]{"group name1", "group name2", "group name3"}) {
            patientGroupI.addItem(str);
        }
        patientGroupI.setImmediate(true);
        patientGroupI.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (!patientGroupI.getValue().toString().equalsIgnoreCase("Patients Group 1")) {
                    patientGroupValueI = patientGroupI.getValue().toString();
                } else {
                    patientGroupValueI = "";
                }
                System.out.println("patientGroupValue  " + patientGroupValueI);
            }
        });
        patientGroupI.setNullSelectionAllowed(false);
        patientGroupI.setWidth("200px");

        middleFiltersLayout.addComponent(patientGroupI);
        middleFiltersLayout.setComponentAlignment(patientGroupI, Alignment.TOP_LEFT);

        //subgroup list
        final ComboBox patientSubGroupII = new ComboBox();
        patientSubGroupII.addItem("Patients Sub-Group 1");
        patientSubGroupII.setValue("Patients Sub-Group 1");
        patientSubGroupII.setNullSelectionAllowed(false);
//        patientSubGroupII.setm
        for (String str : new String[]{"group name1", "group name2", "group name3"}) {
            patientSubGroupII.addItem(str);
        }
        patientSubGroupII.setImmediate(true);
        patientSubGroupII.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (!patientSubGroupII.getValue().toString().equalsIgnoreCase("Patients Sub-Group 1")) {
                    patientSubGroupValueI = patientSubGroupII.getValue().toString();
                } else {
                    patientSubGroupValueI = "";
                }
                System.out.println("patientSubGroupValueI  " + patientSubGroupValueI);
            }
        });
        patientSubGroupII.setNullSelectionAllowed(false);
        patientSubGroupII.setWidth("200px");

        middleFiltersLayout.addComponent(patientSubGroupII);
        middleFiltersLayout.setComponentAlignment(patientSubGroupII, Alignment.TOP_LEFT);

        //FC Patient group 1 / Patient group 2 Filter on number >than, <than
        Label fcPatientsGrI_patGrII = labelGenerator("FC Patient Gr 1 / Patient Gr 2");
        middleFiltersLayout.addComponent(fcPatientsGrI_patGrII);

        HorizontalLayout tripleSearchField1 = new HorizontalLayout();
        tripleSearchField1.setWidth("200px");
        tripleSearchField1.setHeight("20px");//Filter on number >than, <than

        final TextField minValueField = textFieldGenerator("Only Double Allowed");
        minValueField.setWidth("50px");
        minValueField.setNullRepresentation(" ");

        minValueField.setConverter(new StringToIntegerConverter());
        minValueField.addValueChangeListener(new Property.ValueChangeListener() {
            /*
             *the main listener for search table
             */
            private static final long serialVersionUID = 1L;

            /*
             *
             * @param event value change on search table selection
             */
            @Override
            public synchronized void valueChange(Property.ValueChangeEvent event) {
                minValueField.validate();
                if (minValueField.isValid()) {
                    minValueField.setComponentError(null);
//                    System.out.println(" its ok ");
                }
            }
        });
        tripleSearchField1.setSpacing(false);
        tripleSearchField1.addComponent(minValueField);
        tripleSearchField1.setComponentAlignment(minValueField, Alignment.MIDDLE_LEFT);
        Label betweenLabel = new Label(">Than & <Than");
        betweenLabel.setWidth("100px");
        betweenLabel.setStyleName(Reindeer.LABEL_SMALL);

        tripleSearchField1.addComponent(betweenLabel);
        tripleSearchField1.setComponentAlignment(betweenLabel, Alignment.MIDDLE_CENTER);
        middleFiltersLayout.addComponent(tripleSearchField1);

        final TextField maxValueField = textFieldGenerator("Only Double Allowed");
        maxValueField.setWidth("50px");
        maxValueField.setNullRepresentation(" ");

        maxValueField.setConverter(new StringToIntegerConverter());
        maxValueField.addValueChangeListener(new Property.ValueChangeListener() {
            /*
             *the main listener for search table
             */
            private static final long serialVersionUID = 1L;

            /*
             *
             * @param event value change on search table selection
             */
            @Override
            public synchronized void valueChange(Property.ValueChangeEvent event) {
                maxValueField.validate();
                if (maxValueField.isValid()) {
                    maxValueField.setComponentError(null);
////                    System.out.println(" its ok ");
                }
            }
        });
        tripleSearchField1.addComponent(maxValueField);
        tripleSearchField1.setComponentAlignment(maxValueField, Alignment.MIDDLE_RIGHT);

        //ROC AUC Filter on number >than, <than
        Label rocAucLabel = new Label("ROC AUC");//labelGenerator("ROC AUC");
        middleFiltersLayout.addComponent(rocAucLabel);
        rocAucLabel.setStyleName("custLabel");

        HorizontalLayout tripleSearchField2 = new HorizontalLayout();
        tripleSearchField2.setWidth("200px");
        tripleSearchField2.setHeight("20px");//Filter on number >than, <than

        final TextField minRocValueField = textFieldGenerator("Only Double Allowed");
        minRocValueField.setWidth("50px");
        minRocValueField.setNullRepresentation(" ");

        minRocValueField.setConverter(new StringToIntegerConverter());
        minRocValueField.addValueChangeListener(new Property.ValueChangeListener() {
            /*
             *the main listener for search table
             */
            private static final long serialVersionUID = 1L;

            /*
             *
             * @param event value change on search table selection
             */
            @Override
            public synchronized void valueChange(Property.ValueChangeEvent event) {
                minRocValueField.validate();
                if (minRocValueField.isValid()) {
                    minRocValueField.setComponentError(null);
//                    System.out.println(" its ok ");
                }
            }
        });
        tripleSearchField2.setSpacing(false);
        tripleSearchField2.addComponent(minRocValueField);
        tripleSearchField2.setComponentAlignment(minRocValueField, Alignment.MIDDLE_LEFT);
        Label betweenRocLabel = new Label(">Than & <Than");
        betweenRocLabel.setWidth("100px");
        betweenRocLabel.setStyleName(Reindeer.LABEL_SMALL);

        tripleSearchField2.addComponent(betweenRocLabel);
        tripleSearchField2.setComponentAlignment(betweenRocLabel, Alignment.MIDDLE_CENTER);
        middleFiltersLayout.addComponent(tripleSearchField2);

        final TextField maxRocValueField = textFieldGenerator("Only Double Allowed");
        maxRocValueField.setWidth("50px");
        maxRocValueField.setNullRepresentation(" ");

        maxRocValueField.setConverter(new StringToIntegerConverter());
        maxRocValueField.addValueChangeListener(new Property.ValueChangeListener() {
            /*
             *the main listener for search table
             */
            private static final long serialVersionUID = 1L;

            /*
             *
             * @param event value change on search table selection
             */
            @Override
            public synchronized void valueChange(Property.ValueChangeEvent event) {
                maxRocValueField.validate();
                if (maxRocValueField.isValid()) {
                    maxRocValueField.setComponentError(null);
//                    System.out.println(" its ok ");
                }
            }
        });
        tripleSearchField2.addComponent(maxRocValueField);
        tripleSearchField2.setComponentAlignment(maxRocValueField, Alignment.MIDDLE_RIGHT);

    }

    private void initAdvancedSearchRightLayout() {
        VerticalLayout rightFiltersLayout = new VerticalLayout();
        rightFiltersLayout.setSpacing(true);
        advancedSearchLayout.addComponent(rightFiltersLayout);
        Label patientsGroupIINum = labelGenerator("# Patients Group 2");
        rightFiltersLayout.addComponent(patientsGroupIINum);

        HorizontalLayout intSearchField = new HorizontalLayout();
        intSearchField.setWidth("200px");
        Label lessThanLabel = new Label("Less Than < ");
        lessThanLabel.setStyleName(Reindeer.LABEL_SMALL);

        intSearchField.addComponent(lessThanLabel);

        final TextField patientsGroupIINumField = textFieldGenerator("Only Digits Allowed");
        patientsGroupIINumField.setWidth("95px");
        patientsGroupIINumField.setNullRepresentation(" ");

        patientsGroupIINumField.setConverter(new StringToIntegerConverter());
        patientsGroupIINumField.addValueChangeListener(new Property.ValueChangeListener() {
            /*
             *the main listener for search table
             */
            private static final long serialVersionUID = 1L;

            /**
             *
             * @param event value change on search table selection
             */
            @Override
            public synchronized void valueChange(Property.ValueChangeEvent event) {
                patientsGroupIINumField.validate();
                if (patientsGroupIINumField.isValid()) {
                    patientsGroupIINumField.setComponentError(null);
//                    System.out.println(" its ok ");
                }
            }
        });
        intSearchField.addComponent(patientsGroupIINumField);
        rightFiltersLayout.addComponent(intSearchField);

        //patient groups dropdown list
//         Label patientsGroupII = labelGenerator("Patients group 2");
//        rightFiltersLayout.addComponent(patientsGroupII);
        final ComboBox patientGroupII = new ComboBox();
        patientGroupII.addItem("Patients Group 2");
        patientGroupII.setValue("Patients Group 2");
        patientGroupII.setNullSelectionAllowed(false);
        for (String str : new String[]{"group name1", "group name2", "group name3"}) {
            patientGroupII.addItem(str);
        }
        patientGroupII.setImmediate(true);
        patientGroupII.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (!patientGroupII.getValue().toString().equalsIgnoreCase("Patients Group 2")) {
                    patientGroupValueII = patientGroupII.getValue().toString();
                } else {
                    patientGroupValueII = "";
                }
                System.out.println("patientGroupValue  " + patientGroupValueII);
            }
        });
        patientGroupII.setNullSelectionAllowed(false);
        patientGroupII.setWidth("200px");

        rightFiltersLayout.addComponent(patientGroupII);
        rightFiltersLayout.setComponentAlignment(patientGroupII, Alignment.TOP_LEFT);

        //patient sub groups dropdown list
//         Label patientsGroupII = labelGenerator("Patients group 2");
//        rightFiltersLayout.addComponent(patientsGroupII);
        final ComboBox patientSubGroupII = new ComboBox();
        patientSubGroupII.addItem("Patients Sub-Group 2");
        patientSubGroupII.setValue("Patients Sub-Group 2");
        patientSubGroupII.setNullSelectionAllowed(false);
        for (String str : new String[]{"group name1", "group name2", "group name3"}) {
            patientSubGroupII.addItem(str);
        }
        patientSubGroupII.setImmediate(true);
        patientSubGroupII.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (!patientSubGroupII.getValue().toString().equalsIgnoreCase("Patients Sub-Group 2")) {
                    patientSubGroupValueII = patientSubGroupII.getValue().toString();
                } else {
                    patientSubGroupValueII = "";
                }
                System.out.println("patientSubGroupValueII  " + patientSubGroupValueII);
            }
        });
        patientSubGroupII.setNullSelectionAllowed(false);
        patientSubGroupII.setWidth("200px");

        rightFiltersLayout.addComponent(patientSubGroupII);
        rightFiltersLayout.setComponentAlignment(patientSubGroupII, Alignment.TOP_LEFT);

          //  P value  ROC AUC
        Label pValueLabel = labelGenerator("P value");
        rightFiltersLayout.addComponent(pValueLabel);

        HorizontalLayout tripleSearchField2 = new HorizontalLayout();
        tripleSearchField2.setWidth("200px");
        tripleSearchField2.setHeight("20px");//Filter on number >than, <than

        final TextField minPValueField = textFieldGenerator("Only Double Allowed");
        minPValueField.setWidth("50px");
        minPValueField.setNullRepresentation(" ");

        minPValueField.setConverter(new StringToIntegerConverter());
        minPValueField.addValueChangeListener(new Property.ValueChangeListener() {
            /*
             *the main listener for search table
             */
            private static final long serialVersionUID = 1L;

            /*
             *
             * @param event value change on search table selection
             */
            @Override
            public synchronized void valueChange(Property.ValueChangeEvent event) {
                minPValueField.validate();
                if (minPValueField.isValid()) {
                    minPValueField.setComponentError(null);
//                    System.out.println(" its ok ");
                }
            }
        });
        tripleSearchField2.setSpacing(false);
        tripleSearchField2.addComponent(minPValueField);
        tripleSearchField2.setComponentAlignment(minPValueField, Alignment.MIDDLE_LEFT);
        Label betweenPLabel = new Label(">Than & <Than");
        betweenPLabel.setWidth("100px");
        betweenPLabel.setStyleName(Reindeer.LABEL_SMALL);

        tripleSearchField2.addComponent(betweenPLabel);
        tripleSearchField2.setComponentAlignment(betweenPLabel, Alignment.MIDDLE_CENTER);
        rightFiltersLayout.addComponent(tripleSearchField2);

        final TextField maxPValueField = textFieldGenerator("Only Double Allowed");
        maxPValueField.setWidth("50px");
        maxPValueField.setNullRepresentation(" ");

        maxPValueField.setConverter(new StringToIntegerConverter());
        maxPValueField.addValueChangeListener(new Property.ValueChangeListener() {
            /*
             *the main listener for search table
             */
            private static final long serialVersionUID = 1L;

            /*
             *
             * @param event value change on search table selection
             */
            @Override
            public synchronized void valueChange(Property.ValueChangeEvent event) {
                maxPValueField.validate();
                if (maxPValueField.isValid()) {
                    maxPValueField.setComponentError(null);
//                    System.out.println(" its ok ");
                }
            }
        });
        tripleSearchField2.addComponent(maxPValueField);
        tripleSearchField2.setComponentAlignment(maxPValueField, Alignment.MIDDLE_RIGHT);

        //Shotgun/targeted quant
        final ComboBox ShotgunTargetedQuantSelect = new ComboBox();
        ShotgunTargetedQuantSelect.addItem("Shotgun/Targeted Quant");
        ShotgunTargetedQuantSelect.setValue("Shotgun/Targeted Quant");
        ShotgunTargetedQuantSelect.setNullSelectionAllowed(false);
        for (String str : new String[]{"targeted", "group name2", "group name3"}) {
            ShotgunTargetedQuantSelect.addItem(str);
        }
        ShotgunTargetedQuantSelect.setImmediate(true);
        ShotgunTargetedQuantSelect.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (!ShotgunTargetedQuantSelect.getValue().toString().equalsIgnoreCase("Select Shotgun/Targeted Quant")) {
                    shotgunTargetedQuant = ShotgunTargetedQuantSelect.getValue().toString();
                } else {
                    patientGroupValueII = "";
                }
                System.out.println("shotgunTargetedQuant  " + shotgunTargetedQuant);
            }
        });
        ShotgunTargetedQuantSelect.setNullSelectionAllowed(false);
        ShotgunTargetedQuantSelect.setWidth("200px");

        rightFiltersLayout.addComponent(ShotgunTargetedQuantSelect);
        rightFiltersLayout.setComponentAlignment(ShotgunTargetedQuantSelect, Alignment.TOP_LEFT);
        
        doneFilterBtn.setStyleName(Reindeer.BUTTON_SMALL);
          rightFiltersLayout.addComponent(doneFilterBtn);
        rightFiltersLayout.setComponentAlignment(doneFilterBtn, Alignment.TOP_RIGHT);
        doneFilterBtn.addClickListener(new Button.ClickListener() {

            @Override
            public void buttonClick(Button.ClickEvent event) {
            advancedSearchLayout.setVisible(!advancedSearchLayout.isVisible());
                topMiddleSearchButtonsLayout.setVisible(!topMiddleSearchButtonsLayout.isVisible());
                advancedSearchLayoutContainer.setWidth("700px");
            }
        });

    }

    private TextField textFieldGenerator(String filterName) {
        TextField filterField = new TextField();
        filterField.setStyleName(Reindeer.TEXTFIELD_SMALL);
        filterField.setHeight("20px");
        filterField.setWidth("190px");
//     filterField.setCaption(filterName);
        if (filterName != null) {
            filterField.setDescription(filterName);
//     filterField.setValue(filterName);
            filterField.setInputPrompt(filterName);
        }
        return filterField;

    }

    private Label labelGenerator(String text) {
        Label label = new Label(text);//"<h4 style='font-family:verdana;color:black;'>"+text+"</h4>");
//        label.setContentMode(ContentMode.HTML);
//        label.setHeight("30px");
        label.setStyleName("custLabel");
//        label.setWidth("350px");
        return label;

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
        filtersController.setSearchKeyWords(searchField.getValue());
    
        
        
        boolean validQuery = filtersController.isValidQuery();
        if (!validQuery) {
            errorLabelI.setVisible(true);
            searchField.focus();
           }
        
       
        

        //will we allow empty search??
//        if (protSearchObject == null || protSearchObject.toString().equals("") || protSearchObject.toString().length() < 4 ) {
//            errorLabelI.setVisible(true);
//            searchField.focus();
//        } 
        else {

//            String searchDatasetType = searchDatasetTypeObject.toString().trim();
//            String protSearch = protSearchObject.toString().trim().toUpperCase();
//            String searchMethod = searchbyGroup.getValue().toString();

            defaultText = filtersController.getQuery().getSearchKeyWords();
            searchField.setValue(defaultText);

//            String[] searchArr = protSearch.split("\n");
//            Set<String> searchSet = new HashSet<String>();
//            for (String str : searchArr) {
//                searchSet.add(str.trim());
//            }
            String notFound = "";
            Map<Integer, ProteinBean> searchProtList = null;
//            Map<Integer, ProteinBean> fullExpProtList = new HashMap<Integer, ProteinBean>();

            //start updated search 
            searchProtList = handler.searchProtein(filtersController.getQuery());
            
            //start searching process
//            if (searchMethod.equals("Protein Accession"))//case of protein accession
//            {
//                for (String searchStr : searchSet) {
//                    searchProtList = handler.searchProteinByAccession(searchStr.trim(), searchDatasetType, validatedOnly);
//                    if (searchProtList == null || searchProtList.isEmpty()) {
//                        notFound += searchStr + "\t";
//                    } else {
//                        fullExpProtList.putAll(searchProtList);
//                    }
//
//                }
//
//            } else if (searchMethod.equals("Protein Name")) //case of protein name
//            {
//                for (String searchStr : searchSet) {
//                    searchProtList = handler.searchProteinByName(searchStr.trim(), searchDatasetType, validatedOnly);
//                    if (searchProtList == null || searchProtList.isEmpty()) {
//                        notFound += searchStr + "\t";
//                    } else {
//                        fullExpProtList.putAll(searchProtList);
//                    }
//
//                }
//            } else //find protein by peptide sequence
//            {
//
//                for (String searchStr : searchSet) {
//                    searchProtList = handler.searchProteinByPeptideSequence(searchStr.trim(), searchDatasetType, validatedOnly);
//                    if (searchProtList == null || searchProtList.isEmpty()) {
//                        notFound += searchStr + "\t";
//                    } else {
//                        fullExpProtList.putAll(searchProtList);
//                    }
//
//                }
//
//            }
            //searching process  end here
            if (!notFound.equals("")) {
                notFind(notFound);
            }
            if (searchProtList.isEmpty()) {
                searchLayout.setVisible(false);
            } else {
                searchLayout.setVisible(true);
                final SearchResultsTableLayout searcheResultsTableLayout = new SearchResultsTableLayout(handler, handler.getDatasetDetailsList(), searchProtList, validatedOnly);
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
