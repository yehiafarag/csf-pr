/*
 */
package probe.com.view;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.LayoutEvents.LayoutClickListener;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.PeptideBean;
import probe.com.model.beans.ProteinBean;
import probe.com.model.beans.StandardProteinBean;
import probe.com.view.components.DoubleBetweenValuesFilter;
import probe.com.view.components.FiltersControl;
import probe.com.view.components.KeywordFilter;
import probe.com.view.components.SearchResultsTableLayout;
import probe.com.view.core.ComboBoxFilter;
import probe.com.view.core.CustomErrorLabel;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.IconGenerator;
import probe.com.view.core.IntegerTextFieldFilter;
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
//    private TextArea searchField;
    private ComboBoxFilter selectDatasetDropdownList; //select dataset the search method
//    private OptionGroupFilter searchbyGroup;
    private final VerticalLayout topMiddleSearchButtonsLayout = new VerticalLayout();
    private final VerticalLayout advancedSearchLayoutContainer = new VerticalLayout();
    private final HorizontalLayout advancedSearchLayout = new HorizontalLayout();
    private final Button searchButton = new Button("");
    private final Button advancedSearchButton = new Button("Show Filters");
    private final MainHandler handler;
    private String defaultText = "Please use one key-word per line and choose the search options";
    private final String Select_All_Dataset_Str = "Search All Datasets";
    private Label searchByLabel, errorLabelI;// = new  Label();
    private CustomErrorLabel errorLabelII;
    private final TreeMap<Integer, String> datasetNamesList;

    private final VerticalLayout searchTableLayout = new VerticalLayout();
    private final VerticalLayout protSerarchLayout = new VerticalLayout();
    private final VerticalLayout peptidesLayout = new VerticalLayout();
    private final VerticalLayout fractionLayout = new VerticalLayout();
    private CustomExternalLink searchTableAccessionLable;
    private int key = -1;
    private String accession;
    private String otherAccession, desc;
    private PeptidesTableLayout peptideTableLayout;
    private Map<Integer, ProteinBean> fractionsList = null;
    private final FiltersControl filtersController = new FiltersControl();
    private final OptionGroupFilter validatedResults = new OptionGroupFilter(filtersController,"Validated Proteins Only", 5, true);
    private final HorizontalLayout searchProtLayout = new HorizontalLayout();
    private boolean validatedOnly = true;
    private HorizontalLayout searchDatatypeLayout;

    private final OptionGroupFilter searchDatatypeSelectFilter = new OptionGroupFilter(filtersController,"Data Type", 2, false);
    private int fractionNumber = 0;
//    final OptionGroup typeOfStudySelection = new OptionGroup();
//    private String typeofStudy, sampleType, patientGroupValueI, patientGroupValueII, patientSubGroupValueI, patientSubGroupValueII, technologyType, analyticalApproachI, analyticalMethod, shotgunTargetedQuant;

    private final Label doneFilterBtn = new Label("DONE");

    /**
     *
     * @param handler dataset handler
     */
    public SearchLayout(MainHandler handler) {
        this.handler = handler;
        this.datasetNamesList = handler.getDatasetNamesList();
        this.setStyleName(Reindeer.LAYOUT_WHITE);
        this.addComponent(topLayout);
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

        protSerarchLayout.addComponent(fractionLayout);
        protSerarchLayout.addComponent(peptidesLayout);

        fractionLayout.setWidth("100%");
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
private KeywordFilter keywordFilter;
    /**
     * initialize top left side for the search layout
     */
    private void initTopLeftLayout() {

        //search form layout
        topLeftLayout.removeAllComponents();
        keywordFilter = new KeywordFilter(filtersController,defaultText);
         topLeftLayout.addComponent(keywordFilter.getSearchField());
        //secound main filter for identification -quantification or both        
        initDataTypeFilter();
        //init search by (3rd filter)
        initSearchByFilterLabel();
        searchProtLayout.addComponent(keywordFilter.getSearchbyGroup());
        initValidProtFilter();

    }

   
    private void initDataTypeFilter() {
        searchDatatypeSelectFilter.addItems(Arrays.asList(new String[]{"Identification Data", "Quantification Data", "Both"}));
        searchDatatypeSelectFilter.setWidth("150px");
        Property.ValueChangeListener searchDatatypeListener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectDatasetDropdownList.select(Select_All_Dataset_Str);
                selectDatasetDropdownList.commit();
                if (searchDatatypeSelectFilter.getFieldValue()!=null && searchDatatypeSelectFilter.getValue().toString().equalsIgnoreCase("Identification Data")) {
                    selectDatasetDropdownList.setEnabled(true);
                    validatedResults.setEnabled(true);
                    validatedResults.select("Validated Proteins Only");                    
                    advancedSearchLayout.setVisible(false);
                    advancedSearchButton.setEnabled(false);
                    topMiddleSearchButtonsLayout.setVisible(true);
                    filtersController.setVisible(true);
                } else {
                    selectDatasetDropdownList.select(defaultText);
                    selectDatasetDropdownList.setEnabled(false);
                    validatedResults.unselect("Validated Proteins Only");
                    validatedResults.setEnabled(false);                    
                    advancedSearchButton.setEnabled(true);
                    advancedSearchButton.click();
                    
                    

                }
            }
        };
        searchDatatypeSelectFilter.addValueChangeListener(searchDatatypeListener);

        //select dataset for searching
        String[] temArr = new String[datasetNamesList.values().size()];
        int index = 0;
        for (String str : datasetNamesList.values()) {
            temArr[index++] = str;
        }
        selectDatasetDropdownList = new ComboBoxFilter(filtersController, 3, Select_All_Dataset_Str, temArr);
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
        searchDatatypeLayout.addComponent(searchDatatypeSelectFilter);
        searchDatatypeLayout.addComponent(selectDatasetDropdownList);
        searchDatatypeLayout.setComponentAlignment(selectDatasetDropdownList, Alignment.TOP_RIGHT);
        searchDatatypeSelectFilter.select("Identification Data");
        searchDatatypeSelectFilter.commit();
        selectDatasetDropdownList.select(Select_All_Dataset_Str);
        selectDatasetDropdownList.commit();

    }

    private void initSearchByFilterLabel() {
        topLeftLayout.addComponent(searchDatatypeLayout);
        searchByLabel = labelGenerator("Search By:");
        topLeftLayout.addComponent(searchByLabel);
        topLeftLayout.addComponent(searchProtLayout);
        searchProtLayout.setWidth("300px");
        
        
    }

    private void initValidProtFilter() {
        validatedResults.setMultiSelect(true);
        validatedResults.setNullSelectionAllowed(true);
        validatedResults.addItem("Validated Proteins Only");
        validatedResults.setHeight("15px");
        searchProtLayout.addComponent(validatedResults);
        searchProtLayout.setComponentAlignment(validatedResults, Alignment.MIDDLE_RIGHT);

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
    private void initTopRightLayout() {
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
                if(topMiddleSearchButtonsLayout.isVisible())
                advancedSearchLayoutContainer.setWidth("700px");
                else
                    advancedSearchLayoutContainer.setWidth("818px");
                doneFilterBtn.setVisible(true);
                filtersController.setVisible(false);

            }
        });

        //init advanced search layout container
        advancedSearchLayoutContainer.setWidth("700px");
        advancedSearchLayoutContainer.setHeight("100%");
        advancedSearchLayoutContainer.addComponent(advancedSearchLayout);
        advancedSearchLayoutContainer.addComponent(filtersController);
        advancedSearchLayout.setSizeFull();
        advancedSearchLayout.removeAllComponents();
        advancedSearchLayout.setMargin(new MarginInfo(false, true, false, true));
        advancedSearchLayout.setVisible(false);
        
        

        initUpdateFiltersLayout();
        
       
//        initAdvancedSearchLeftLayout();
//        initAdvancedSearchMiddleLayout();
//        initAdvancedSearchRightLayout();

        topMiddleSearchButtonsLayout.setSpacing(true);
        topMiddleSearchButtonsLayout.addComponent(searchButton);
        topMiddleSearchButtonsLayout.addComponent(advancedSearchButton);
        topMiddleSearchButtonsLayout.setComponentAlignment(advancedSearchButton, Alignment.BOTTOM_CENTER);

        topRightLayout.addComponent(topMiddleSearchButtonsLayout);
        topRightLayout.setComponentAlignment(topMiddleSearchButtonsLayout, Alignment.TOP_LEFT);
        topRightLayout.setExpandRatio(topMiddleSearchButtonsLayout, 0.9f);
        topRightLayout.addComponent(advancedSearchLayoutContainer);
        topRightLayout.setExpandRatio(advancedSearchLayoutContainer, 3);
        topRightLayout.setMargin(new MarginInfo(true, true, true, false));

//         doneFilterBtn.setStyleName("doneFilterBtn");
//         doneFilterBtn.setContentMode(ContentMode.HTML);
//         doneFilterBtn.setVisible(false);
//         
        searchButton.addClickListener(this);
        
        
//        doneFilterBtn.setIcon(new ThemeResource("img/search_22.png"));
        
//        topRightLayout.addComponent(doneFilterBtn);
//        topRightLayout.setComponentAlignment(doneFilterBtn, Alignment.MIDDLE_CENTER);
//        topRightLayout.setExpandRatio(doneFilterBtn, 0.9f);

        
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

    
    
    
    
    private void initUpdateFiltersLayout(){
        //advancedSearchLayout
        VerticalLayout filtersLabelLayout = new VerticalLayout();
        filtersLabelLayout.setWidth("190px");
        filtersLabelLayout.setSpacing(true);
        VerticalLayout filtersResultsLayout = new VerticalLayout();
        filtersResultsLayout.setHeight("243px");
        filtersResultsLayout.setSpacing(true);
        filtersResultsLayout.setStyleName(Reindeer.LAYOUT_WHITE);

        updatedFilterArea.setHeight("210px");
        updatedFilterArea.setStyleName(Reindeer.LAYOUT_WHITE);
        filtersResultsLayout.addComponent(updatedFilterArea);

        VerticalLayout doneBtnLayout = new VerticalLayout();
        doneBtnLayout.setHeight("30px");
        doneBtnLayout.setWidth("100%");
        doneBtnLayout.setStyleName(Reindeer.LAYOUT_WHITE); 
        filtersResultsLayout.addComponent(doneBtnLayout);               
        filtersResultsLayout.setComponentAlignment(doneBtnLayout, Alignment.BOTTOM_CENTER);


        doneFilterBtn.setStyleName("doneFilterBtn");
        doneFilterBtn.setContentMode(ContentMode.HTML);
        doneFilterBtn.setVisible(true);
        doneFilterBtn.setWidth("70px");
        doneBtnLayout.addComponent(doneFilterBtn);
        doneBtnLayout.setComponentAlignment(doneFilterBtn, Alignment.BOTTOM_LEFT);
//        topRightLayout.setExpandRatio(doneFilterBtn, 0.9f);
//        advancedSearchLayout.addComponent(doneBtnLayout);
//        advancedSearchLayout.setComponentAlignment(doneBtnLayout, Alignment.MIDDLE_CENTER);
//        advancedSearchLayout.setExpandRatio(doneBtnLayout, 0.5f);
        doneBtnLayout.addLayoutClickListener(new LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {

                if (event.getClickedComponent()!=null && event.getClickedComponent().toString().equalsIgnoreCase("DONE")) {
                    doneFilterBtn.setVisible(false);
                    advancedSearchLayoutContainer.setWidth("700px");
                    filtersController.setVisible(true);
                    advancedSearchLayout.setVisible(!advancedSearchLayout.isVisible());
                    topMiddleSearchButtonsLayout.setVisible(!topMiddleSearchButtonsLayout.isVisible());


                }
            }
        });

    
        
        LayoutClickListener listener = new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {

                updatedFilterArea.removeAllComponents();
                if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("PumedID")) {
                    initPumedIdFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("Raw Data Available Only")) {
                    initRawDataAvailableFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("Study Type")) {
                    initStudyTypeFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("Sample Type")) {
                    initSampleTypeFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("Technology")) {
                    initTechnologyFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("Analytical Approach 1")) {
                    initAnalyticalApproachFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("Analytical Method")) {
                    initAnalyticalMethodFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("Patients Group")) {
                    initPatientGroupFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("ROC AUC")) {
                    initRocFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().substring(28).equalsIgnoreCase("P Value")) {
                    initPValueFilter(updatedFilterArea);
                }
            }
        };

        for (String str : new String[]{"PumedID", "Raw Data Available Only", "Study Type", "Sample Type", "Technology", "Analytical Approach 1", "Analytical Method", "Patients Group", "ROC AUC", "P Value"}) {
            VerticalLayout filter = generateFilterLabel(str);
            filter.addLayoutClickListener(listener);
            filtersLabelLayout.addComponent(filter);
        }

        advancedSearchLayout.addComponent(filtersLabelLayout);
        advancedSearchLayout.setComponentAlignment(filtersLabelLayout, Alignment.TOP_LEFT);
        advancedSearchLayout.addComponent(filtersResultsLayout);
        advancedSearchLayout.setComponentAlignment(filtersResultsLayout, Alignment.TOP_LEFT);
        advancedSearchLayout.setExpandRatio(filtersLabelLayout, 1.5f);
        advancedSearchLayout.setExpandRatio(filtersResultsLayout, 3);

    }
    
    private VerticalLayout generateFilterLabel(String title){
    Label filterLabel = new Label("&nbsp; &nbsp; &nbsp; &nbsp; "+title);
        filterLabel.setContentMode(ContentMode.HTML);
        filterLabel.setStyleName("filterLabel");
        
        VerticalLayout layout = new VerticalLayout();
//        layout.setId(title);
        layout.addComponentAsFirst(filterLabel);
        
        return layout;
    
    }
    private final VerticalLayout updatedFilterArea= new VerticalLayout();
    
    
    
    
    
//    private void initAdvancedSearchLeftLayout() {
//
//        VerticalLayout rightFiltersLayout = new VerticalLayout();
//        rightFiltersLayout.setSpacing(true);
//        advancedSearchLayout.addComponent(rightFiltersLayout);
//        //pumedID filter
//        initPumedIdFilter(rightFiltersLayout);
//        initRawDataAvailableFilter(rightFiltersLayout);
//        //select study type
//        initStudyTypeFilter(rightFiltersLayout);
//        initSampleTypeFilter(rightFiltersLayout);
//        // select Technology
//        initTechnologyFilter(rightFiltersLayout);
//
//        // Analytical approach 1
////        / Label technologyLabel = labelGenerator("Technology:");
////        rightFiltersLayout.addComponent(technologyLabel);
////        rightFiltersLayout.setComponentAlignment(technologyLabel, Alignment.TOP_LEFT);
//        final ComboBox selectAnalyticalApproach1 = new ComboBox();
//        selectAnalyticalApproach1.addItem("Analytical Approach 1");
//        selectAnalyticalApproach1.setValue("Analytical Approach 1");
//        selectAnalyticalApproach1.setNullSelectionAllowed(false);
//        for (String str : new String[]{"label-free", "SELDI", "MALDI", "TMT"}) {
//            selectAnalyticalApproach1.addItem(str);
//        }
//        selectAnalyticalApproach1.setImmediate(true);
//        selectAnalyticalApproach1.addValueChangeListener(new Property.ValueChangeListener() {
//            /**
//             *
//             */
//            private static final long serialVersionUID = 6456118889864963868L;
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                if (!selectAnalyticalApproach1.getValue().toString().equalsIgnoreCase("Analytical Approach 1")) {
//                    analyticalApproachI = selectAnalyticalApproach1.getValue().toString();
//                } else {
//                    technologyType = "";
//                }
//                System.out.println("Analytical Approach 1  " + analyticalApproachI);
//            }
//        });
//        selectAnalyticalApproach1.setNullSelectionAllowed(false);
//        selectAnalyticalApproach1.setWidth("190px");
//
//        rightFiltersLayout.addComponent(selectAnalyticalApproach1);
//        rightFiltersLayout.setComponentAlignment(selectAnalyticalApproach1, Alignment.TOP_LEFT);
//
//        //Analytical method
//        final ComboBox selectAnalyticalMethod = new ComboBox();
//        selectAnalyticalMethod.addItem("Analytical Method");
//        selectAnalyticalMethod.setValue("Analytical Method");
//        selectAnalyticalMethod.setNullSelectionAllowed(false);
//        for (String str : new String[]{"SIS", "SRM"}) {
//            selectAnalyticalMethod.addItem(str);
//        }
//        selectAnalyticalMethod.setImmediate(true);
//        selectAnalyticalMethod.addValueChangeListener(new Property.ValueChangeListener() {
//            /**
//             *
//             */
//            private static final long serialVersionUID = 6456118889864963868L;
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                if (!selectAnalyticalMethod.getValue().toString().equalsIgnoreCase("Analytical Method")) {
//                    analyticalMethod = selectAnalyticalMethod.getValue().toString();
//                } else {
//                    analyticalMethod = "";
//                }
//                System.out.println("Analytical Method  " + analyticalMethod);
//            }
//        });
//        selectAnalyticalMethod.setNullSelectionAllowed(false);
//        selectAnalyticalMethod.setWidth("190px");
//
//        rightFiltersLayout.addComponent(selectAnalyticalMethod);
//        rightFiltersLayout.setComponentAlignment(selectAnalyticalMethod, Alignment.TOP_LEFT);
//
//    }

    private TextFieldFilter pumedIdFilter;
    private void initPumedIdFilter(VerticalLayout rightFiltersLayout) {
        if(pumedIdFilter == null){
        pumedIdFilter = new TextFieldFilter(filtersController, 6, "PumedID");
        }
        rightFiltersLayout.addComponent(pumedIdFilter);
    }

    private  OptionGroupFilter rawDataAvailableFilter;
    private void initRawDataAvailableFilter(VerticalLayout rightFiltersLayout) {

        if(rawDataAvailableFilter == null){
            rawDataAvailableFilter = new OptionGroupFilter(filtersController,"Raw Data Available Only", 7, true);
        rawDataAvailableFilter.setMultiSelect(true);
        rawDataAvailableFilter.addItem("Raw Data Available Only");
        rawDataAvailableFilter.setNullSelectionAllowed(true);
        }
        rightFiltersLayout.addComponent(rawDataAvailableFilter);
        rightFiltersLayout.setComponentAlignment(rawDataAvailableFilter, Alignment.TOP_LEFT);
    }

    private ComboBoxFilter selectStudyType;

    private void initStudyTypeFilter(VerticalLayout rightFiltersLayout) {
        if (selectStudyType == null) {
            selectStudyType = new ComboBoxFilter(filtersController, 8, "Study Type", new String[]{"Discovery", "verification"});
            selectStudyType.setWidth("190px");
        }
        rightFiltersLayout.addComponent(selectStudyType);
        rightFiltersLayout.setComponentAlignment(selectStudyType, Alignment.TOP_LEFT);
    }

    private ListSelectFilter selectSampleTypeFilter;

    private void initSampleTypeFilter(VerticalLayout rightFiltersLayout) {
        if (selectSampleTypeFilter == null) {
            selectSampleTypeFilter = new ListSelectFilter(filtersController, 9, "Sample Type", new String[]{"CSF", "Plasma", "Serum"});
            selectSampleTypeFilter.setWidth("190px");
        }
        rightFiltersLayout.addComponent(selectSampleTypeFilter);
        rightFiltersLayout.setComponentAlignment(selectSampleTypeFilter, Alignment.TOP_LEFT);

    }

    private ListSelectFilter selectTechnologyFilter;
    private void initTechnologyFilter(VerticalLayout rightFiltersLayout) {
        if(selectTechnologyFilter == null){
        selectTechnologyFilter = new ListSelectFilter(filtersController, 10, "Technology", new String[]{"Mass spectrometry", "WB", "ELISA", "2DE"});
//        selectTechnologyFilter.setWidth("190px");
        }
        rightFiltersLayout.addComponent(selectTechnologyFilter);
        rightFiltersLayout.setComponentAlignment(selectTechnologyFilter, Alignment.TOP_LEFT);
    }
    
    private ListSelectFilter analyticalApproachFilter;
    private void initAnalyticalApproachFilter(VerticalLayout rightFiltersLayout) {
        if(analyticalApproachFilter == null){
        analyticalApproachFilter = new ListSelectFilter(filtersController, 11, "Analytical Approach 1",new String[]{"label-free", "SELDI", "MALDI", "TMT"});
//        analyticalApproachFilter.setWidth("200px");
        }
        rightFiltersLayout.addComponent(analyticalApproachFilter);
        rightFiltersLayout.setComponentAlignment(analyticalApproachFilter, Alignment.TOP_LEFT);
    }
    
     private ListSelectFilter analyticalMethodFilter;
    private void initAnalyticalMethodFilter(VerticalLayout rightFiltersLayout) {
        if(analyticalMethodFilter == null){
        analyticalMethodFilter = new ListSelectFilter(filtersController, 12, "Analytical Method",new String[]{"SIS-SRM"});
//        analyticalApproachFilter.setWidth("200px");
        }
        rightFiltersLayout.addComponent(analyticalMethodFilter);
        rightFiltersLayout.setComponentAlignment(analyticalMethodFilter, Alignment.TOP_LEFT);
    }
    
     private DoubleBetweenValuesFilter rocFilter;
    private void initRocFilter(VerticalLayout rightFiltersLayout) {
        if(rocFilter == null){
        rocFilter = new DoubleBetweenValuesFilter(filtersController, 20, "ROC AUC");
//        analyticalApproachFilter.setWidth("200px");
        }
        rightFiltersLayout.addComponent(rocFilter);
        rightFiltersLayout.setComponentAlignment(rocFilter, Alignment.TOP_LEFT);
    }
    
      private DoubleBetweenValuesFilter pValueFilter;
    private void initPValueFilter(VerticalLayout rightFiltersLayout) {
        if(pValueFilter == null){
            pValueFilter = new DoubleBetweenValuesFilter(filtersController, 20, "P Value");
//        analyticalApproachFilter.setWidth("200px");
        }
        rightFiltersLayout.addComponent(pValueFilter);
        rightFiltersLayout.setComponentAlignment(pValueFilter, Alignment.TOP_LEFT);
    }
    
//    private ComboBoxFilter selectStudyType;
//
//    private void initStudyTypeFilter(VerticalLayout rightFiltersLayout) {
//        if (selectStudyType == null) {
//            selectStudyType = new ComboBoxFilter(filtersController, 8, "Study Type", new String[]{"Discovery", "verification"});
//            selectStudyType.setWidth("190px");
//        }
//        rightFiltersLayout.addComponent(selectStudyType);
//        rightFiltersLayout.setComponentAlignment(selectStudyType, Alignment.TOP_LEFT);
//    }
    
    
    private DoubleBetweenValuesFilter fcPatientsGrI_patGrIIFilter;
    private void initPatientGroupFilter(VerticalLayout rightFiltersLayout){
        VerticalLayout filterLayout = new VerticalLayout();
        filterLayout.setSpacing(true);
        rightFiltersLayout.addComponent(filterLayout);
        filterLayout.addComponent(initPatientGrLayout(1));
        filterLayout.addComponent(initPatientGrLayout(2));
        if(fcPatientsGrI_patGrIIFilter == null)
            fcPatientsGrI_patGrIIFilter = new DoubleBetweenValuesFilter(filtersController, 19, "FC Patient Gr 1 / Patient Gr 2:");
        filterLayout.addComponent(fcPatientsGrI_patGrIIFilter);
//        
//        HorizontalLayout hlo = new HorizontalLayout();
//        filterLayout.addComponent(hlo);
//        //FC Patient group 1 / Patient group 2 Filter on number >than, <than
//        Label fcPatientsGrI_patGrII = labelGenerator("FC Patient Gr 1 / Patient Gr 2");
//        hlo.addComponent(fcPatientsGrI_patGrII);
//
//        HorizontalLayout tripleSearchField1 = new HorizontalLayout();
//       hlo.addComponent(tripleSearchField1);
////        tripleSearchField1.setWidth("200px");
//        tripleSearchField1.setHeight("20px");//Filter on number >than, <than
//
//        final TextField minValueField = textFieldGenerator("Only Double Allowed");
//        minValueField.setWidth("50px");
//        minValueField.setNullRepresentation(" ");
//
//        minValueField.setConverter(new StringToIntegerConverter());
//        minValueField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /*
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                minValueField.validate();
//                if (minValueField.isValid()) {
//                    minValueField.setComponentError(null);
////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        tripleSearchField1.setSpacing(false);
//        tripleSearchField1.addComponent(minValueField);
//        tripleSearchField1.setComponentAlignment(minValueField, Alignment.MIDDLE_LEFT);
//        Label betweenLabel = new Label(">Than & <Than");
//        betweenLabel.setWidth("100px");
//        betweenLabel.setStyleName(Reindeer.LABEL_SMALL);
//           tripleSearchField1.addComponent(betweenLabel);
//        tripleSearchField1.setComponentAlignment(betweenLabel, Alignment.MIDDLE_CENTER);
//
//        final TextField maxValueField = textFieldGenerator("Only Double Allowed");
//        maxValueField.setWidth("50px");
//        maxValueField.setNullRepresentation(" ");
//
//        maxValueField.setConverter(new StringToIntegerConverter());
//        maxValueField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /*
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                maxValueField.validate();
//                if (maxValueField.isValid()) {
//                    maxValueField.setComponentError(null);
//////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        tripleSearchField1.addComponent(maxValueField);
//        tripleSearchField1.setComponentAlignment(maxValueField, Alignment.MIDDLE_RIGHT);


        
        
    
    }
    
    private IntegerTextFieldFilter patientNumberIFilter,patientNumberIIFilter;
    private ComboBoxFilter patientGroupIFilter, patientSubGroupIFilter,patientGroupIIFilter, patientSubGroupIIFilter;
    private VerticalLayout initPatientGrLayout(int groupIndex ){
    VerticalLayout patientGroupLayout = new VerticalLayout();
    patientGroupLayout.setSpacing(true);
    HorizontalLayout patGrNumLayout = new HorizontalLayout();
//        patGrNumLayout.setWidth("300px");
        patGrNumLayout.setHeight("20px");
        patientGroupLayout.addComponent(patGrNumLayout);
        patGrNumLayout.setSpacing(true);
        Label patientsGroupINum = labelGenerator("Number of  Patients (Group "+groupIndex+"):");
        patGrNumLayout.addComponent(patientsGroupINum);

        Label lessThanLabel = new Label("Less Than <");
        lessThanLabel.setStyleName(Reindeer.LABEL_SMALL);
        patGrNumLayout.addComponent(lessThanLabel);
        patGrNumLayout.setComponentAlignment(lessThanLabel, Alignment.MIDDLE_CENTER);
        if(groupIndex == 1){
        if(patientNumberIFilter == null)
            patientNumberIFilter = new IntegerTextFieldFilter(filtersController, 13, "Only Integer Value Allowed");
        patGrNumLayout.addComponent(patientNumberIFilter);
        }else{
             if(patientNumberIIFilter == null)
            patientNumberIFilter = new IntegerTextFieldFilter(filtersController, 14, "Only Integer Value Allowed");
        patGrNumLayout.addComponent(patientNumberIFilter);       
        }
        if (groupIndex == 1) {
            if (patientGroupIFilter == null) {
                patientGroupIFilter = new ComboBoxFilter(filtersController, 15, "Patients Group 1", new String[]{"group name1", "group name2", "group name3"});
                patientSubGroupIFilter = new ComboBoxFilter(filtersController, 16,  "Patients Sub-Group 1", new String[]{"sub-group name1", "sub-group name2", "sub-group name3"});
                patientSubGroupIFilter.setEnabled(false);
            }
      patientGroupLayout.addComponent(patientGroupIFilter);      
      patientGroupLayout.addComponent(patientSubGroupIFilter);
       
        patientGroupIFilter.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (!patientGroupIFilter.getValue().toString().equalsIgnoreCase("Patients Group 1")) {
                    patientSubGroupIFilter.setEnabled(true);                   
                } else {
                    patientSubGroupIFilter.select("Patients Sub-Group 1");
                   patientSubGroupIFilter.setEnabled(false);
                }
            }
        });
          }
        else{
            
             if (patientGroupIIFilter == null) {
                patientGroupIIFilter = new ComboBoxFilter(filtersController, 17, "Patients Group 2", new String[]{"group name1", "group name2", "group name3"});
                patientSubGroupIIFilter = new ComboBoxFilter(filtersController, 18,  "Patients Sub-Group 2", new String[]{"sub-group name1", "sub-group name2", "sub-group name3"});
                patientSubGroupIIFilter.setEnabled(false);
            }
      patientGroupLayout.addComponent(patientGroupIIFilter);      
      patientGroupLayout.addComponent(patientSubGroupIIFilter);
       
        patientGroupIFilter.addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;

            @Override
            public void valueChange(Property.ValueChangeEvent event) {

                if (!patientGroupIIFilter.getValue().toString().equalsIgnoreCase("Patients Group 2")) {
                    patientSubGroupIIFilter.setEnabled(true);                   
                } else {
                    patientSubGroupIIFilter.select("Patients Sub-Group 2");
                   patientSubGroupIIFilter.setEnabled(false);
                }
            }
        });
        
        }
    
    return patientGroupLayout;
    }
    

    

//    private void initAdvancedSearchMiddleLayout() {
//        VerticalLayout middleFiltersLayout = new VerticalLayout();
//        middleFiltersLayout.setSpacing(true);
//        advancedSearchLayout.addComponent(middleFiltersLayout);
//        Label patientsGroupINum = labelGenerator("# Patients Group 1:");
//        middleFiltersLayout.addComponent(patientsGroupINum);
//
//        HorizontalLayout intSearchField = new HorizontalLayout();
//        intSearchField.setWidth("200px");
//        intSearchField.setHeight("20px");
//        Label lessThanLabel = new Label("Less Than <");
//        lessThanLabel.setStyleName(Reindeer.LABEL_SMALL);
//
//        intSearchField.addComponent(lessThanLabel);
//
//        final TextField patientsGroupINumField = textFieldGenerator("Only Digits Allowed");
//        patientsGroupINumField.setWidth("95px");
//        patientsGroupINumField.setNullRepresentation(" ");
//
//        patientsGroupINumField.setConverter(new StringToIntegerConverter());
//        patientsGroupINumField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /*
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                patientsGroupINumField.validate();
//                if (patientsGroupINumField.isValid()) {
//                    patientsGroupINumField.setComponentError(null);
////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        intSearchField.addComponent(patientsGroupINumField);
//        middleFiltersLayout.addComponent(intSearchField);
//
////patient groups dropdown list
////         Label patientsGroupI = labelGenerator("Patients group 1");
////        middleFiltersLayout.addComponent(patientsGroupI);
//        final ComboBox patientGroupI = new ComboBox();
//        final ComboBox patientSubGroupI = new ComboBox();
//
//
//         
//        patientGroupI.addItem("Patients Group 1");
//        patientGroupI.setValue("Patients Group 1");
//        patientGroupI.setNullSelectionAllowed(false);
//        for (String str : new String[]{"group name1", "group name2", "group name3"}) {
//            patientGroupI.addItem(str);
//        }
//        patientGroupI.setImmediate(true);
//        patientGroupI.addValueChangeListener(new Property.ValueChangeListener() {
//            /**
//             *
//             */
//            private static final long serialVersionUID = 6456118889864963868L;
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                if (!patientGroupI.getValue().toString().equalsIgnoreCase("Patients Group 1")) {
//                    patientGroupValueI = patientGroupI.getValue().toString();
//                    for (String str : new String[]{"group name1", "group name2", "group name3"}) {
//                        patientSubGroupI.addItem(str);
//                        patientSubGroupI.setEnabled(true);
//                    }
//                } else {
//                    patientGroupValueI = "";
//                }
//                System.out.println("patientGroupValue  " + patientGroupValueI);
//            }
//        });
//        patientGroupI.setNullSelectionAllowed(false);
//        patientGroupI.setWidth("200px");
//
//        middleFiltersLayout.addComponent(patientGroupI);
//        middleFiltersLayout.setComponentAlignment(patientGroupI, Alignment.TOP_LEFT);
//
//        //subgroup list
//        patientSubGroupI.addItem("Patients Sub-Group 1");
//        patientSubGroupI.setValue("Patients Sub-Group 1");
//        patientSubGroupI.setNullSelectionAllowed(false);
////        patientSubGroupIFilter.setm
////        for (String str : new String[]{"group name1", "group name2", "group name3"}) {
////            patientSubGroupIFilter.addItem(str);
////        }
//        patientSubGroupI.setImmediate(true);
//        patientSubGroupI.setEnabled(false);
//        patientSubGroupI.addValueChangeListener(new Property.ValueChangeListener() {
//            /**
//             *
//             */
//            private static final long serialVersionUID = 6456118889864963868L;
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                if (!patientSubGroupI.getValue().toString().equalsIgnoreCase("Patients Sub-Group 1")) {
//                    patientSubGroupValueI = patientSubGroupI.getValue().toString();
//                } else {
//                    patientSubGroupValueI = "";
//                }
//                System.out.println("patientSubGroupValueI  " + patientSubGroupValueI);
//            }
//        });
//        patientSubGroupI.setNullSelectionAllowed(false);
//        patientSubGroupI.setWidth("200px");
//
//        middleFiltersLayout.addComponent(patientSubGroupI);
//        middleFiltersLayout.setComponentAlignment(patientSubGroupI, Alignment.TOP_LEFT);
//
//        //FC Patient group 1 / Patient group 2 Filter on number >than, <than
//        Label fcPatientsGrI_patGrII = labelGenerator("FC Patient Gr 1 / Patient Gr 2");
//        middleFiltersLayout.addComponent(fcPatientsGrI_patGrII);
//
//        HorizontalLayout tripleSearchField1 = new HorizontalLayout();
//        tripleSearchField1.setWidth("200px");
//        tripleSearchField1.setHeight("20px");//Filter on number >than, <than
//
//        final TextField minValueField = textFieldGenerator("Only Double Allowed");
//        minValueField.setWidth("50px");
//        minValueField.setNullRepresentation(" ");
//
//        minValueField.setConverter(new StringToIntegerConverter());
//        minValueField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /*
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                minValueField.validate();
//                if (minValueField.isValid()) {
//                    minValueField.setComponentError(null);
////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        tripleSearchField1.setSpacing(false);
//        tripleSearchField1.addComponent(minValueField);
//        tripleSearchField1.setComponentAlignment(minValueField, Alignment.MIDDLE_LEFT);
//        Label betweenLabel = new Label(">Than & <Than");
//        betweenLabel.setWidth("100px");
//        betweenLabel.setStyleName(Reindeer.LABEL_SMALL);
//
//        tripleSearchField1.addComponent(betweenLabel);
//        tripleSearchField1.setComponentAlignment(betweenLabel, Alignment.MIDDLE_CENTER);
//        middleFiltersLayout.addComponent(tripleSearchField1);
//
//        final TextField maxValueField = textFieldGenerator("Only Double Allowed");
//        maxValueField.setWidth("50px");
//        maxValueField.setNullRepresentation(" ");
//
//        maxValueField.setConverter(new StringToIntegerConverter());
//        maxValueField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /*
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                maxValueField.validate();
//                if (maxValueField.isValid()) {
//                    maxValueField.setComponentError(null);
//////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        tripleSearchField1.addComponent(maxValueField);
//        tripleSearchField1.setComponentAlignment(maxValueField, Alignment.MIDDLE_RIGHT);
//
//        //ROC AUC Filter on number >than, <than
//        Label rocAucLabel = new Label("ROC AUC");//labelGenerator("ROC AUC");
//        middleFiltersLayout.addComponent(rocAucLabel);
//        rocAucLabel.setStyleName("custLabel");
//
//        HorizontalLayout tripleSearchField2 = new HorizontalLayout();
//        tripleSearchField2.setWidth("200px");
//        tripleSearchField2.setHeight("20px");//Filter on number >than, <than
//
//        final TextField minRocValueField = textFieldGenerator("Only Double Allowed");
//        minRocValueField.setWidth("50px");
//        minRocValueField.setNullRepresentation(" ");
//
//        minRocValueField.setConverter(new StringToIntegerConverter());
//        minRocValueField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /*
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                minRocValueField.validate();
//                if (minRocValueField.isValid()) {
//                    minRocValueField.setComponentError(null);
////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        tripleSearchField2.setSpacing(false);
//        tripleSearchField2.addComponent(minRocValueField);
//        tripleSearchField2.setComponentAlignment(minRocValueField, Alignment.MIDDLE_LEFT);
//        Label betweenRocLabel = new Label(">Than & <Than");
//        betweenRocLabel.setWidth("100px");
//        betweenRocLabel.setStyleName(Reindeer.LABEL_SMALL);
//
//        tripleSearchField2.addComponent(betweenRocLabel);
//        tripleSearchField2.setComponentAlignment(betweenRocLabel, Alignment.MIDDLE_CENTER);
//        middleFiltersLayout.addComponent(tripleSearchField2);
//
//        final TextField maxRocValueField = textFieldGenerator("Only Double Allowed");
//        maxRocValueField.setWidth("50px");
//        maxRocValueField.setNullRepresentation(" ");
//
//        maxRocValueField.setConverter(new StringToIntegerConverter());
//        maxRocValueField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /*
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                maxRocValueField.validate();
//                if (maxRocValueField.isValid()) {
//                    maxRocValueField.setComponentError(null);
////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        tripleSearchField2.addComponent(maxRocValueField);
//        tripleSearchField2.setComponentAlignment(maxRocValueField, Alignment.MIDDLE_RIGHT);
//
//    }

//    private void initAdvancedSearchRightLayout() {
//        VerticalLayout rightFiltersLayout = new VerticalLayout();
//        rightFiltersLayout.setSpacing(true);
//        advancedSearchLayout.addComponent(rightFiltersLayout);
//        Label patientsGroupIINum = labelGenerator("# Patients Group 2");
//        rightFiltersLayout.addComponent(patientsGroupIINum);
//
//        HorizontalLayout intSearchField = new HorizontalLayout();
//        intSearchField.setWidth("200px");
//        Label lessThanLabel = new Label("Less Than < ");
//        lessThanLabel.setStyleName(Reindeer.LABEL_SMALL);
//
//        intSearchField.addComponent(lessThanLabel);
//
//        final TextField patientsGroupIINumField = textFieldGenerator("Only Digits Allowed");
//        patientsGroupIINumField.setWidth("95px");
//        patientsGroupIINumField.setNullRepresentation(" ");
//
//        patientsGroupIINumField.setConverter(new StringToIntegerConverter());
//        patientsGroupIINumField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /**
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                patientsGroupIINumField.validate();
//                if (patientsGroupIINumField.isValid()) {
//                    patientsGroupIINumField.setComponentError(null);
////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        intSearchField.addComponent(patientsGroupIINumField);
//        rightFiltersLayout.addComponent(intSearchField);
//
//        //patient groups dropdown list
////         Label patientsGroupII = labelGenerator("Patients group 2");
////        rightFiltersLayout.addComponent(patientsGroupII);
//        final ComboBox patientGroupII = new ComboBox();
//        final ComboBox patientSubGroupII = new ComboBox();
//        patientGroupII.addItem("Patients Group 2");
//        patientGroupII.setValue("Patients Group 2");
//        patientGroupII.setNullSelectionAllowed(false);
//        for (String str : new String[]{"group name1", "group name2", "group name3"}) {
//            patientGroupII.addItem(str);
//        }
//        patientGroupII.setImmediate(true);
//        patientGroupII.addValueChangeListener(new Property.ValueChangeListener() {
//            /**
//             *
//             */
//            private static final long serialVersionUID = 6456118889864963868L;
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                if (!patientGroupII.getValue().toString().equalsIgnoreCase("Patients Group 2")) {
//                    patientGroupValueII = patientGroupII.getValue().toString();
//                    patientSubGroupII.setEnabled(true);
//                    for (String str : new String[]{"sub-group name1", "sub-group name2", "sub-group name3"}) {
//                        patientSubGroupII.addItem(str);
//                    }
//                } else {
//                    patientGroupValueII = "";
//                }
//                System.out.println("patientGroupValue  " + patientGroupValueII);
//            }
//        });
//        patientGroupII.setNullSelectionAllowed(false);
//        patientGroupII.setWidth("200px");
//
//        rightFiltersLayout.addComponent(patientGroupII);
//        rightFiltersLayout.setComponentAlignment(patientGroupII, Alignment.TOP_LEFT);
//
//        //patient sub groups dropdown list
////         Label patientsGroupII = labelGenerator("Patients group 2");
////        rightFiltersLayout.addComponent(patientsGroupII);
//        patientSubGroupII.addItem("Patients Sub-Group 2");
//        patientSubGroupII.setValue("Patients Sub-Group 2");
//        patientSubGroupII.setNullSelectionAllowed(false);
//        patientSubGroupII.setEnabled(false);
//        patientSubGroupII.setImmediate(true);
//        patientSubGroupII.addValueChangeListener(new Property.ValueChangeListener() {
//            /**
//             *
//             */
//            private static final long serialVersionUID = 6456118889864963868L;
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                if (!patientSubGroupII.getValue().toString().equalsIgnoreCase("Patients Sub-Group 2")) {
//                    patientSubGroupValueII = patientSubGroupII.getValue().toString();
//                } else {
//                    patientSubGroupValueII = "";
//                }
//                System.out.println("patientSubGroupValueII  " + patientSubGroupValueII);
//            }
//        });
//        patientSubGroupII.setNullSelectionAllowed(false);
//        patientSubGroupII.setWidth("200px");
//
//        rightFiltersLayout.addComponent(patientSubGroupII);
//        rightFiltersLayout.setComponentAlignment(patientSubGroupII, Alignment.TOP_LEFT);
//
//        //  P value  ROC AUC
//        Label pValueLabel = labelGenerator("P Value");
//        rightFiltersLayout.addComponent(pValueLabel);
//
//        HorizontalLayout tripleSearchField2 = new HorizontalLayout();
//        tripleSearchField2.setWidth("200px");
//        tripleSearchField2.setHeight("20px");//Filter on number >than, <than
//
//        final TextField minPValueField = textFieldGenerator("Only Double Allowed");
//        minPValueField.setWidth("50px");
//        minPValueField.setNullRepresentation(" ");
//
//        minPValueField.setConverter(new StringToIntegerConverter());
//        minPValueField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /*
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                minPValueField.validate();
//                if (minPValueField.isValid()) {
//                    minPValueField.setComponentError(null);
////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        tripleSearchField2.setSpacing(false);
//        tripleSearchField2.addComponent(minPValueField);
//        tripleSearchField2.setComponentAlignment(minPValueField, Alignment.MIDDLE_LEFT);
//        Label betweenPLabel = new Label(">Than & <Than");
//        betweenPLabel.setWidth("100px");
//        betweenPLabel.setStyleName(Reindeer.LABEL_SMALL);
//
//        tripleSearchField2.addComponent(betweenPLabel);
//        tripleSearchField2.setComponentAlignment(betweenPLabel, Alignment.MIDDLE_CENTER);
//        rightFiltersLayout.addComponent(tripleSearchField2);
//
//        final TextField maxPValueField = textFieldGenerator("Only Double Allowed");
//        maxPValueField.setWidth("50px");
//        maxPValueField.setNullRepresentation(" ");
//
//        maxPValueField.setConverter(new StringToIntegerConverter());
//        maxPValueField.addValueChangeListener(new Property.ValueChangeListener() {
//            /*
//             *the main listener for search table
//             */
//            private static final long serialVersionUID = 1L;
//
//            /*
//             *
//             * @param event value change on search table selection
//             */
//            @Override
//            public synchronized void valueChange(Property.ValueChangeEvent event) {
//                maxPValueField.validate();
//                if (maxPValueField.isValid()) {
//                    maxPValueField.setComponentError(null);
////                    System.out.println(" its ok ");
//                }
//            }
//        });
//        tripleSearchField2.addComponent(maxPValueField);
//        tripleSearchField2.setComponentAlignment(maxPValueField, Alignment.MIDDLE_RIGHT);
//
//        //Shotgun/targeted quant
//        final ComboBox ShotgunTargetedQuantSelect = new ComboBox();
//        ShotgunTargetedQuantSelect.addItem("Shotgun/Targeted Quant");
//        ShotgunTargetedQuantSelect.setValue("Shotgun/Targeted Quant");
//        ShotgunTargetedQuantSelect.setNullSelectionAllowed(false);
//        for (String str : new String[]{"targeted", "group name2", "group name3"}) {
//            ShotgunTargetedQuantSelect.addItem(str);
//        }
//        ShotgunTargetedQuantSelect.setImmediate(true);
//        ShotgunTargetedQuantSelect.addValueChangeListener(new Property.ValueChangeListener() {
//            /**
//             *
//             */
//            private static final long serialVersionUID = 6456118889864963868L;
//
//            @Override
//            public void valueChange(Property.ValueChangeEvent event) {
//
//                if (!ShotgunTargetedQuantSelect.getValue().toString().equalsIgnoreCase("Select Shotgun/Targeted Quant")) {
//                    shotgunTargetedQuant = ShotgunTargetedQuantSelect.getValue().toString();
//                } else {
//                    patientGroupValueII = "";
//                }
//                System.out.println("shotgunTargetedQuant  " + shotgunTargetedQuant);
//            }
//        });
//        ShotgunTargetedQuantSelect.setNullSelectionAllowed(false);
//        ShotgunTargetedQuantSelect.setWidth("200px");
//
//        rightFiltersLayout.addComponent(ShotgunTargetedQuantSelect);
//        rightFiltersLayout.setComponentAlignment(ShotgunTargetedQuantSelect, Alignment.TOP_LEFT);
//
//        doneFilterBtn.setStyleName(Reindeer.BUTTON_SMALL);
//        rightFiltersLayout.addComponent(doneFilterBtn);
//        rightFiltersLayout.setComponentAlignment(doneFilterBtn, Alignment.TOP_RIGHT);
//        doneFilterBtn.addClickListener(new Button.ClickListener() {
//
//            @Override
//            public void buttonClick(Button.ClickEvent event) {
//                advancedSearchLayout.setVisible(!advancedSearchLayout.isVisible());
//                topMiddleSearchButtonsLayout.setVisible(!topMiddleSearchButtonsLayout.isVisible());
//                advancedSearchLayoutContainer.setWidth("700px");
//            }
//        });
//
//    }

//    private TextField textFieldGenerator(String filterName) {
//        TextField filterField = new TextField();
//        filterField.setStyleName(Reindeer.TEXTFIELD_SMALL);
//        filterField.setHeight("20px");
//        filterField.setWidth("190px");
////     filterField.setCaption(filterName);
//        if (filterName != null) {
//            filterField.setDescription(filterName);
////     filterField.setValue(filterName);
//            filterField.setInputPrompt(filterName);
//        }
//        return filterField;
//
//    }

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
        fractionLayout.removeAllComponents();
        errorLabelI.setVisible(false);
        errorLabelII.setVisible(false);
        filtersController.setSearchKeyWords(keywordFilter.getSearchField().getValue());

        boolean validQuery = filtersController.isValidQuery();
        if (!validQuery) {
            errorLabelI.setVisible(true);
            keywordFilter.getSearchField().focus();
        } //will we allow empty search??
        //        if (protSearchObject == null || protSearchObject.toString().equals("") || protSearchObject.toString().length() < 4 ) {
        //            errorLabelI.setVisible(true);
        //            searchField.focus();
        //        } 
        else {

//            String searchDatasetType = searchDatasetTypeObject.toString().trim();
//            String protSearch = protSearchObject.toString().trim().toUpperCase();
//            String searchMethod = searchbyGroup.getValue().toString();
            defaultText = filtersController.getQuery().getSearchKeyWords();
            keywordFilter.getSearchField().setValue(defaultText);

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
                        fractionLayout.removeAllComponents();

                        String datasetName = item.getItemProperty("Experiment").toString();
                        accession = item.getItemProperty("Accession").toString();
                        otherAccession = item.getItemProperty("Other Protein(s)").toString();
                        desc = item.getItemProperty("Description").toString();

                        handler.setMainDatasetId(datasetName);

                        fractionNumber = handler.getDataset(handler.getMainDatasetId()).getFractionsNumber();
                        if (handler.getMainDatasetId() != 0 && handler.getDataset(handler.getMainDatasetId()).getDatasetType() == 1) {
//                              Map<String, ProteinBean> protList = handler.retriveProteinsList(handler.getMainDataset());
                            CustomExportBtnLayout exportAllProteinPeptidesLayout = new CustomExportBtnLayout(handler, "allProtPep", handler.getMainDatasetId(), datasetName, accession, otherAccession, null, 0, null, null, null, desc);
                            PopupView exportAllProteinPeptidesPopup = new PopupView("Export Peptides from All Datasets for (" + accession + " )", exportAllProteinPeptidesLayout);
                            exportAllProteinPeptidesPopup.setDescription("Export CSF-PR Peptides for ( " + accession + " ) for All Available Datasets");
                            searcheResultsTableLayout.setExpBtnProtAllPepTable(exportAllProteinPeptidesPopup);// new PopupView("Export Proteins", (new CustomExportBtnLayout(handler, "prots",datasetId, datasetName, accession, otherAccession, datasetList, proteinsList, dataset.getFractionsNumber(), null,null))));
                            if (key >= 0) {

                                Map<Integer, PeptideBean> pepProtList = handler.getPeptidesProtList(handler.getMainDatasetId(), accession, otherAccession);
//                                Map<Integer, PeptideBean> peptideList = handler.getPeptidesList(key, validatedOnly)
//                                if (handler.getMainDataset().getPeptideList() == null) {
//                                    handler.getMainDataset().setPeptideList(pepProtList);
//                                } else {
//                                    handler.getMainDataset().getPeptideList().putAll(pepProtList);
//                                }
                                if (!pepProtList.isEmpty()) {
                                    int validPep = handler.getValidatedPepNumber(pepProtList);
                                    if (peptideTableLayout != null) {
                                        peptidesLayout.removeComponent(peptideTableLayout);
                                    }
                                    peptideTableLayout = new PeptidesTableLayout(validPep, pepProtList.size(), desc, pepProtList, accession, handler.getDataset(handler.getMainDatasetId()).getName());
                                    peptidesLayout.setMargin(false);
                                    peptidesLayout.addComponent(peptideTableLayout);

                                    CustomExportBtnLayout exportAllProteinsPeptidesLayout = new CustomExportBtnLayout(handler, "protPep", handler.getMainDatasetId(), handler.getDataset(handler.getMainDatasetId()).getName(), accession, otherAccession, null, 0, pepProtList, null, null, desc);
                                    PopupView exportAllProteinsPeptidesPopup = new PopupView("Export Peptides from Selected Dataset for (" + accession + " )", exportAllProteinsPeptidesLayout);

                                    exportAllProteinsPeptidesPopup.setDescription("Export Peptides from ( " + handler.getDataset(handler.getMainDatasetId()).getName() + " ) Dataset for ( " + accession + " )");
                                    peptideTableLayout.setExpBtnPepTable(exportAllProteinsPeptidesPopup);

                                }
//                                fractionsList = handler.getProtGelFractionsList(handler.getMainDatasetId(),accession, otherAccession);
                                List<StandardProteinBean> standerdProtList = handler.retrieveStandardProtPlotList(handler.getMainDatasetId());//                          

                                if (fractionNumber == 0 || handler.getMainDatasetId() == 0 || standerdProtList == null || standerdProtList.isEmpty()) {
                                    fractionLayout.removeAllComponents();
                                    if (searcheResultsTableLayout.getSearchTable() != null) {
                                        searcheResultsTableLayout.getSearchTable().setHeight("267.5px");
//                                    searcheResultsTableLayout.setProtTableHeight("267.5px");
                                    }
                                    if (peptideTableLayout.getPepTable() != null) {
                                        peptideTableLayout.getPepTable().setHeight("267.5px");
                                        peptideTableLayout.setPeptideTableHeight("267.5px");
                                    }
                                } else {
                                    fractionsList = handler.getProtGelFractionsList(handler.getMainDatasetId(), accession, otherAccession);

                                    if (fractionsList != null && !fractionsList.isEmpty()) {

//                                if (handler.getMainDatasetId() != 0 ){//&& handler.getMainDataset().getProteinList() != null) {
//                                    handler.getMainDataset().setFractionsList(fractionsList);
//                                    handler.getDatasetList().put(handler.getMainDataset().getDatasetId(), handler.getMainDataset());
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

//                                    Map<Integer, ProteinBean> proteinFractionAvgList = handler.getProteinFractionAvgList(accession + "," + otherAccession, fractionsList, handler.getMainDatasetId());
//                                    if (fractionsList==null || fractionsList.isEmpty()){//(proteinFractionAvgList == null || proteinFractionAvgList.isEmpty()) {
//                                        fractionLayout.removeAllComponents();
//                                    } else {
                                        fractionLayout.addComponent(new GelFractionsLayout(accession, mw, fractionsList, standerdProtList, handler.getDataset(handler.getMainDatasetId()).getName()));
//                                    }
//                                }

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
