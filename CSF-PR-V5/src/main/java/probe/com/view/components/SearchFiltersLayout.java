/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.data.Property;
import com.vaadin.event.FieldEvents;
import com.vaadin.event.LayoutEvents;
import com.vaadin.event.ShortcutAction;
import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeMap;
import probe.com.handlers.MainHandler;
import probe.com.view.core.ComboBoxFilter;
import probe.com.view.core.IconGenerator;
import probe.com.view.core.IntegerTextFieldFilter;
import probe.com.view.core.ListSelectFilter;
import probe.com.view.core.OptionGroupFilter;
import probe.com.view.core.ShowLabel;
import probe.com.view.core.TextFieldFilter;

/**
 *
 * @author Yehia Farag
 */
public class SearchFiltersLayout extends VerticalLayout implements Serializable, com.vaadin.event.LayoutEvents.LayoutClickListener {
//     private final VerticalLayout mainComponentBodyLayout = new VerticalLayout();

    
    private final String[] studyTypeValues,sampleTypeValues,technologyValues,analyticalApproachValues,patGr1,patGr2,patSubGr1,patSubGr2;
    private final Label searchingTitle;
    private final HorizontalLayout titleLayout;
    private final ShowLabel show;
    private final HorizontalLayout topLayout = new HorizontalLayout();
    private final HorizontalLayout topFrameLayout = new HorizontalLayout();
    private final HorizontalLayout topRightLayout = new HorizontalLayout();
    private final VerticalLayout topLeftLayout = new VerticalLayout();
    private KeywordFilter keywordFilter;
    private final SearchingFiltersControl filtersController = new SearchingFiltersControl();
    private final OptionGroupFilter validatedResults = new OptionGroupFilter(filtersController, "Validated Proteins Only", 5, true);
    private final OptionGroupFilter searchDatatypeSelectFilter = new OptionGroupFilter(filtersController, "Data Type", 2, false);
    private ComboBoxFilter selectDatasetDropdownList; //select dataset the search method
    private final VerticalLayout filtersLabelsLayout = new VerticalLayout();
    private final HorizontalLayout searchButtonsLayout = new HorizontalLayout();
    private final HorizontalLayout advancedSearchLayoutContainer = new HorizontalLayout();
//    private final HorizontalLayout advancedSearchLayout = new HorizontalLayout();
    private final Button searchingBtn = new Button("");
    private final Button enableAdvancedSearchBtn = new Button("Show Filters");
    private final String Select_All_Dataset_Str = "Search All Datasets";
    private Label searchByLabel,dataTypeLabel,datasetLabel;
    private final TreeMap<Integer, String> datasetNamesList;
    private final HorizontalLayout searchProtLayout = new HorizontalLayout();
    
    private VerticalLayout searchDatatLayout;
    private final HorizontalLayout searchDatatypeLayout = new HorizontalLayout();
    private final HorizontalLayout selectDatasetLayout = new HorizontalLayout();
    private final VerticalLayout updatedFilterArea = new VerticalLayout();
    private final Button doneFilterBtn = new Button("Show Applied Filters");
    private final Button clearFiltersBtn = new Button("Clear Filters");
    private final Button miniSearchBtn = new Button("Search");
    private boolean extendedView = false;
    private String defaultText = "Please use one key-word per line and choose the search options";
    private final VerticalLayout minFilterLabelsLayout = new VerticalLayout();
    
    
    @Override
    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
        if (extendedView) {
            this.hideLayout();
            
        } else {
            
            this.showLayout();
        }
        
    }
    
    public SearchFiltersLayout(MainHandler handler) {
        this.setMargin(new MarginInfo(false, false, false, false));
        this.setWidth("100%");
        this.setHeightUndefined();
        this.datasetNamesList = handler.getDatasetNamesList();
        
        
        //init filters values
        studyTypeValues =  new String[]{"Discovery", "Verification"};
        sampleTypeValues = new String[]{"CSF", "Plasma", "Serum"};
        technologyValues = new String[]{"Mass spectrometry", "WB", "ELISA", "2DE"};
        analyticalApproachValues = new String[]{"label-free", "SELDI", "MALDI", "TMT"};
        patGr1 = new String[]{"MS", "CONTROL"};
        patSubGr1 =  new String[]{"SPMS", "PPMS","CIS-MS"};
        patGr2 = new String[]{"group name1", "group name2", "group name3"};
        patSubGr2 =new String[]{"group name2", "group name2", "group name3"};
        
        
        
        
        HorizontalLayout clickableTitle = new HorizontalLayout();
        clickableTitle.setSizeUndefined();
        clickableTitle.setSpacing(true);
        titleLayout = new HorizontalLayout();
        titleLayout.setHeight("20px");
        titleLayout.setWidth("100%");
        titleLayout.setSpacing(true);
        show = new ShowLabel();
        show.setHeight("20px");
        show.setWidth("16px");
        clickableTitle.addComponent(show);
        clickableTitle.setComponentAlignment(show, Alignment.BOTTOM_LEFT);

        searchingTitle = new Label("Searching Filters");//"<h4  style='font-family:verdana;font-weight:bold;'><strong aligen='center' style='font-family:verdana;color:#00000;'>Searching Filters</strong></h4>");
        searchingTitle.setContentMode(ContentMode.HTML);

        searchingTitle.setStyleName("filterShowLabel");
        searchingTitle.setHeight("20px");
        searchingTitle.setWidth("200px");

        clickableTitle.addComponent(searchingTitle);
        clickableTitle.setComponentAlignment(searchingTitle, Alignment.TOP_LEFT);
        clickableTitle.addLayoutClickListener(SearchFiltersLayout.this);
         titleLayout.addComponent(clickableTitle);
        
        
        Label infoLable = new Label("<div style='border:1px outset black;text-align:justify;text-justify:inter-word;'><h3 style='font-family:verdana;color:black;font-weight:bold;margin-left:20px;margin-right:20px;'>Information</h3><p  style='font-family:verdana;color:black;margin-left:20px;margin-right:20px;'>Type in search keywords (one per line) and choose the search type. All experiments containing protein(s) where the keyword is found are listed. View the information about each protein from each experiment separately by selecting them from the list.</p></div>");
        infoLable.setContentMode(ContentMode.HTML);
        infoLable.setWidth("300px");
        infoLable.setStyleName(Reindeer.LAYOUT_BLUE);
        //init last part info lable layout
        IconGenerator help = new IconGenerator();
        HorizontalLayout infoIco = help.getInfoNote(infoLable);
        infoIco.setMargin(new MarginInfo(false, true, false, true));
        infoIco.setWidth("16px");
        infoIco.setHeight("16px");
        titleLayout.addComponent(infoIco);
        titleLayout.setComponentAlignment(infoIco, Alignment.TOP_RIGHT);
       
        titleLayout.setExpandRatio(clickableTitle, 0.05f);
//        titleLayout.setExpandRatio(searchingTitle, 0.01f);
        titleLayout.setExpandRatio(infoIco, 0.98f);
        
        
        this.addComponent(titleLayout); 
        this.addComponent(topLayout);
        initMainComponentBodyLayout();
       
        

    }
    
   
    

    private void initMainComponentBodyLayout() {
       
        
        topLayout.setHeight("270px");
        topLayout.setWidthUndefined();
        topLayout.addComponent(topFrameLayout);
        topFrameLayout.setWidthUndefined();
        topFrameLayout.setHeight("270px");
        topLayout.setStyleName(Reindeer.LAYOUT_WHITE);
       topFrameLayout.setSpacing(true);
        
        topFrameLayout.addComponent(topLeftLayout);
        topFrameLayout.addComponent(topRightLayout);
        
         filtersController.getFullFilterLayout().setVisible(true);
        filtersController.getFullFilterLayout().setSpacing(true);
         topFrameLayout.addComponent(filtersLabelsLayout);
        topFrameLayout.setComponentAlignment(filtersLabelsLayout, Alignment.TOP_LEFT);
        filtersLabelsLayout.setHeight("100%");

//        minFilterLabelsLayout.setWidth("400px");
//        minFilterLabelsLayout.setVisible(true);
//        minFilterLabelsLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        filtersLabelsLayout.addComponent(filtersController.getFullFilterLayout());
        filtersLabelsLayout.setComponentAlignment(filtersController.getFullFilterLayout(), Alignment.TOP_LEFT);
        filtersLabelsLayout.addComponent(minFilterLabelsLayout);
//        filtersLabelsLayout.addComponent(searchButtonsLayout);

//        filtersLabelsLayout.setComponentAlignment(searchButtonsLayout, Alignment.BOTTOM_LEFT);
        
        filtersLabelsLayout.setSpacing(true);
        
//        topFrameLayout.addComponent(  filtersController.getFullFilterLayout());
        
         
      
        topLeftLayout.setSpacing(true);
        topLeftLayout.setMargin(true);
        topRightLayout.setSpacing(true);
        topRightLayout.setMargin(true);
//        topRightLayout.setWidth("700px");
//        topRightLayout.setStyleName(Reindeer.LAYOUT_BLACK);
        
       
        
        initTopLeftLayout();
        initTopRightLayout();
//        this.setWidth("100%");
    }

    /**
     * initialize top left side for the search layout
     */
    private void initTopLeftLayout() {
        //search form layout
        topLeftLayout.removeAllComponents();
        topLeftLayout.setWidthUndefined();
        keywordFilter = new KeywordFilter(filtersController, defaultText);
        topLeftLayout.addComponent(keywordFilter.getSearchField());
        keywordFilter.getSearchField().addFocusListener(new FieldEvents.FocusListener() {
            @Override
            public void focus(FieldEvents.FocusEvent event) {
                showLayout();
            }
        });
        //secound main filter for identification -quantification or both        
        initDataTypeFilter();
        //init search by (3rd filter)
        initSearchByFilterLabel();
        keywordFilter.getSearchbyGroup().addStyleName("horizontal");
        searchProtLayout.setWidthUndefined();
        searchProtLayout.addComponent(keywordFilter.getSearchbyGroup());
        searchProtLayout.setComponentAlignment(keywordFilter.getSearchbyGroup(),Alignment.BOTTOM_LEFT);
        searchProtLayout.setSpacing(true);
        searchProtLayout.setHeight("50px");
        initValidProtFilter();
        initSeachingBtnsLayout();           
        showLayout();
    }
    
    
    private void initSeachingBtnsLayout(){
     searchButtonsLayout.setHeight("32px");
        searchButtonsLayout.setWidth("200px");
        searchButtonsLayout.addComponent(searchingBtn);
        searchButtonsLayout.setComponentAlignment(searchingBtn, Alignment.BOTTOM_LEFT);
        searchButtonsLayout.addComponent(enableAdvancedSearchBtn);
        searchButtonsLayout.setComponentAlignment(enableAdvancedSearchBtn, Alignment.MIDDLE_RIGHT);

        
//        topRightLayout.setExpandRatio(filtersLabelsLayout, 0.9f);
        
        
        //middle layout search buttons
        searchingBtn.setStyleName(Reindeer.BUTTON_LINK);
        searchingBtn.setIcon(new ThemeResource("img/search_22.png"));
        enableAdvancedSearchBtn.setDescription("Activated with Quantification Data Search");
        enableAdvancedSearchBtn.setStyleName(Reindeer.BUTTON_LINK);
        enableAdvancedSearchBtn.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                advancedSearchLayoutContainer.setVisible(! advancedSearchLayoutContainer.isVisible());
                if (enableAdvancedSearchBtn.getCaption().equals("Show Filters")) {
                    enableAdvancedSearchBtn.setCaption("Hide Filters");
                } else {
                    enableAdvancedSearchBtn.setCaption("Show Filters");
                }
//                filtersLabelsLayout.setVisible(!filtersLabelsLayout.isVisible());
//                if (filtersLabelsLayout.isVisible()) {
////                    advancedSearchLayoutContainer.setWidth("640px");
//                } else {
////                    advancedSearchLayoutContainer.setWidth("758px");
//                }
                doneFilterBtn.setVisible(false);
//                getFiltersController().getFullFilterLayout().setVisible(false);
                
            }
        });
    
    bottomLeftLayout.addComponent(searchButtonsLayout);
        bottomLeftLayout.setComponentAlignment(searchButtonsLayout, Alignment.BOTTOM_RIGHT);
    bottomLeftLayout.setHeight("50px");
    
    
    }

    /**
     * initialize top right side for the search layout
     */
    private void initTopRightLayout() {
        //topright layout
//        topRightLayout.removeAllComponents();
        topRightLayout.setWidthUndefined();
        topRightLayout.setHeight("100%");

        
        //main advanced search layout
        topRightLayout.addComponent(advancedSearchLayoutContainer);
        advancedSearchLayoutContainer.setVisible(false);
        advancedSearchLayoutContainer.setSpacing(true);
//        topRightLayout.setExpandRatio(advancedSearchLayoutContainer, 3);
        
        
        

       

       

        //init advanced search layout container
        advancedSearchLayoutContainer.setWidthUndefined();
        advancedSearchLayoutContainer.setHeight("270px");        
//        advancedSearchLayoutContainer.addComponent(advancedSearchLayout);        
//        advancedSearchLayout.setSizeFull();
//        advancedSearchLayout.removeAllComponents();
        advancedSearchLayoutContainer.setMargin(new MarginInfo(false, true, false, true));
        initQuantificationFiltersLayout();       
        topRightLayout.setMargin(new MarginInfo(true, true, true, false)); 

    }

    

    private void showLayout() {
        show.updateIcon(true);        
        extendedView = true;        
//        enableAdvancedSearchBtn.setVisible(true);
        topLayout.setHeight("270px");
        topFrameLayout.setHeight("270px");
         topLeftLayout.setVisible(true);
//        searchByLabel.setVisible(true);
         this.searchDatatLayout.setVisible(true);
        this.searchProtLayout.setVisible(true);
        this.bottomLeftLayout.setVisible(true);
        
//        keywordFilter.getSearchbyGroup().setVisible(true);
//        validatedResults.setVisible(true);
        minFilterLabelsLayout.setVisible(false);
        filtersController.getFullFilterLayout().setVisible(true);
//        searchButtonsLayout.setVisible(true);
    }

    public final void hideLayout() {        
        show.updateIcon(false);
        topLayout.setHeight("20px");
        topFrameLayout.setHeight("15px");
        topLeftLayout.setVisible(false);
        
//        topFrameLayout.setHeight("75px");
//        enableAdvancedSearchBtn.setVisible(false);
        doneFilterBtn.click();
        extendedView = false;        
        minFilterLabelsLayout.removeAllComponents();
        minFilterLabelsLayout.setVisible(true);
        filtersController.getFullFilterLayout().setVisible(false);
        minFilterLabelsLayout.addComponent(filtersController.getMinimumFilterLayout());
        
        this.searchDatatLayout.setVisible(false);
        this.searchProtLayout.setVisible(false);
        this.bottomLeftLayout.setVisible(false);
        
//        searchByLabel.setVisible(false);
//        keywordFilter.getSearchbyGroup().setVisible(false);
        
//        validatedResults.setVisible(false);
        
        
//        topLayout.setVisible(false);
    }

    public boolean isVisability() {
        return extendedView;
    }
    
    public void setVisibility(boolean test) {
        if (test) {
            this.showLayout();
        } else {
            this.hideLayout();
        }
    }
    
    public SearchingFiltersControl getFiltersController() {
        return filtersController;
    }
    
    private void initDataTypeFilter() {
        searchDatatypeSelectFilter.getOptionGroup().addItems(Arrays.asList(new String[]{"Identification Data", "Quantification Data","All Data"}));
        searchDatatypeSelectFilter.setWidth("367px");
        Property.ValueChangeListener searchDatatypeListener = new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selectDatasetDropdownList.getComboBox().select(Select_All_Dataset_Str);
                selectDatasetDropdownList.getComboBox().commit();
                if (searchDatatypeSelectFilter.getFieldValue() != null && searchDatatypeSelectFilter.getOptionGroup().getValue().toString().equalsIgnoreCase("Identification Data")) {
                    identificationDataFilterReset();
                    
                } else {
                    quantificationDataFilterReset();
                    
                }
            }
        };
        searchDatatypeSelectFilter.getOptionGroup().addValueChangeListener(searchDatatypeListener);

        //select dataset for searching
        String[] temArr = new String[datasetNamesList.values().size()];
        int index = 0;
        for (String str : datasetNamesList.values()) {
            temArr[index++] = str;
        }
        selectDatasetDropdownList = new ComboBoxFilter(filtersController, 3, "Data Set", Select_All_Dataset_Str, temArr);
        selectDatasetDropdownList.getComboBox().addValueChangeListener(new Property.ValueChangeListener() {
            /**
             *
             */
            private static final long serialVersionUID = 6456118889864963868L;
            
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                getMainSearchButton().setClickShortcut(ShortcutAction.KeyCode.ENTER);
            }
        });
       
        selectDatasetDropdownList.getComboBox().setWidth("351px");
        searchDatatLayout = new VerticalLayout(); 
        searchDatatLayout.setWidthUndefined();
        searchDatatLayout.setSpacing(true);
//        searchDatatLayout.setWidthUndefined();
        
        dataTypeLabel = labelGenerator("Data Type:");
        dataTypeLabel.setWidth("70px");
        searchDatatypeSelectFilter.getOptionGroup().addStyleName("horizontal");     
        searchDatatypeLayout.addComponent(dataTypeLabel);
        searchDatatypeLayout.setComponentAlignment(dataTypeLabel, Alignment.BOTTOM_LEFT);
        searchDatatypeLayout.addComponent(searchDatatypeSelectFilter);
         searchDatatLayout.addComponent(searchDatatypeLayout);
         searchDatatypeLayout.setSpacing(true);
        
        
         datasetLabel = labelGenerator("Dataset:");
        datasetLabel.setWidth("70px");    
        selectDatasetLayout.addComponent(datasetLabel);  
        selectDatasetLayout.setComponentAlignment(datasetLabel, Alignment.TOP_LEFT);
        selectDatasetLayout.addComponent(selectDatasetDropdownList);
        selectDatasetLayout.setComponentAlignment(selectDatasetDropdownList, Alignment.TOP_RIGHT);
        searchDatatLayout.addComponent(selectDatasetLayout);
        searchDatatLayout.setSpacing(true);
        searchDatatLayout.setHeight("50px");
        
        
             
        
        
        searchDatatypeSelectFilter.getOptionGroup().select("Identification Data");
        searchDatatypeSelectFilter.getOptionGroup().commit();
        selectDatasetDropdownList.getComboBox().select(Select_All_Dataset_Str);
        selectDatasetDropdownList.getComboBox().commit();
        
    }
    
    private void initSearchByFilterLabel() {
        topLeftLayout.addComponent(searchDatatLayout);
        searchByLabel = labelGenerator("Search By:");
        searchByLabel.setWidth("70px");
        
        searchProtLayout.addComponent(searchByLabel);  
        searchProtLayout.setComponentAlignment(searchByLabel, Alignment.BOTTOM_LEFT);
        topLeftLayout.addComponent(searchProtLayout);
        topLeftLayout.setComponentAlignment(searchProtLayout, Alignment.BOTTOM_LEFT);
//        searchProtLayout.setWidth("100%");
        
    }
    private final HorizontalLayout bottomLeftLayout = new HorizontalLayout();
    private void initValidProtFilter() {
        bottomLeftLayout.setWidth("437px");
        topLeftLayout.addComponent(bottomLeftLayout);
        
        validatedResults.getOptionGroup().setMultiSelect(true);
        validatedResults.getOptionGroup().setNullSelectionAllowed(true);
        validatedResults.getOptionGroup().addItem("Validated Proteins Only");
        validatedResults.setHeight("15px");
        
        bottomLeftLayout.addComponent(validatedResults);
        bottomLeftLayout.setComponentAlignment(validatedResults, Alignment.MIDDLE_LEFT);
        
        validatedResults.getOptionGroup().addValueChangeListener(new Property.ValueChangeListener() {
            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                validatedResults.getOptionGroup().isSelected("Validated Proteins Only");
            }
        });
        validatedResults.getOptionGroup().select("Validated Proteins Only");
    }
    
    private void identificationDataFilterReset() {
        selectDatasetDropdownList.setEnabled(true);
        validatedResults.setEnabled(true);
        validatedResults.getOptionGroup().select("Validated Proteins Only");
         advancedSearchLayoutContainer.setVisible(false);
        enableAdvancedSearchBtn.setEnabled(false);
        filtersLabelsLayout.setVisible(true);
        filtersController.resetQuntificationFilters();
        filtersController.getFullFilterLayout().setVisible(true);
        
    }
    
    private void quantificationDataFilterReset() {
        
        selectDatasetDropdownList.getComboBox().select(defaultText);
        filtersController.resetIdentificationFilters();
        selectDatasetDropdownList.setEnabled(false);
        validatedResults.getOptionGroup().unselect("Validated Proteins Only");
        validatedResults.setEnabled(false);        
        enableAdvancedSearchBtn.setEnabled(true);
        enableAdvancedSearchBtn.click();
        
    }
    
    private void initQuantificationFiltersLayout() {
        //advancedSearchLayout
        final VerticalLayout filtersLabelLayout = new VerticalLayout();
        filtersLabelLayout.setWidth("190px");
        filtersLabelLayout.setSpacing(true);        
        final Set<Component> labelset = new HashSet<Component>();
        Label filtersTitle = new Label("Available Filters");
        filtersTitle.setStyleName("custFilterLabelHeader");
        filtersLabelLayout.addComponent(filtersTitle);
        filtersLabelLayout.setComponentAlignment(filtersTitle, Alignment.TOP_CENTER);
        VerticalLayout filtersResultsLayout = new VerticalLayout();
        filtersResultsLayout.setHeight("243px");
        filtersResultsLayout.setSpacing(true);
        filtersResultsLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        
        updatedFilterArea.setHeight("210px");
        updatedFilterArea.setWidth("500px");
        updatedFilterArea.setStyleName(Reindeer.LAYOUT_WHITE);
        filtersResultsLayout.addComponent(updatedFilterArea);
        
        HorizontalLayout doneBtnLayout = new HorizontalLayout();
        doneBtnLayout.setSpacing(true);
        doneBtnLayout.setHeight("30px");
        doneBtnLayout.setStyleName(Reindeer.LAYOUT_WHITE);        
//        filtersResultsLayout.addComponent(doneBtnLayout);        
//        filtersResultsLayout.setComponentAlignment(doneBtnLayout, Alignment.BOTTOM_CENTER);
        
        doneFilterBtn.setStyleName(Reindeer.BUTTON_SMALL);
        doneFilterBtn.setVisible(false);
        doneFilterBtn.setWidth("150px");
        doneBtnLayout.addComponent(doneFilterBtn);
        doneBtnLayout.setComponentAlignment(doneFilterBtn, Alignment.BOTTOM_LEFT);
        doneFilterBtn.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                doneFilterBtn.setVisible(false);
//                advancedSearchLayoutContainer.setWidth("640px");
                filtersController.getFullFilterLayout().setVisible(true);
                advancedSearchLayoutContainer.setVisible(false);
                filtersLabelsLayout.setVisible(true);                
            }
        });
        
        clearFiltersBtn.setStyleName(Reindeer.BUTTON_SMALL);
        clearFiltersBtn.setVisible(true);
        clearFiltersBtn.setWidth("90px");
        doneBtnLayout.addComponent(clearFiltersBtn);
        doneBtnLayout.setComponentAlignment(clearFiltersBtn, Alignment.BOTTOM_LEFT);
        clearFiltersBtn.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                filtersController.clearAllFilters(LayoutEvents.LayoutClickEvent.createEvent(topLayout, null, clearFiltersBtn));
            }
        });
        
        miniSearchBtn.setStyleName(Reindeer.BUTTON_SMALL);
        miniSearchBtn.setVisible(true);
        miniSearchBtn.setWidth("90px");
        doneBtnLayout.addComponent(miniSearchBtn);
        doneBtnLayout.setComponentAlignment(miniSearchBtn, Alignment.BOTTOM_LEFT);
        miniSearchBtn.addClickListener(new Button.ClickListener() {
            
            @Override
            public void buttonClick(Button.ClickEvent event) {
                System.out.println("start search");
                getMainSearchButton().click();
            }
        });
        
        LayoutEvents.LayoutClickListener listener = new LayoutEvents.LayoutClickListener() {
            
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                
                updatedFilterArea.removeAllComponents();
                for (Component label : labelset) {
                    label.setStyleName("filterLabel");
                }
                event.getChildComponent().setStyleName("filterBtnLabelClicked");
                if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("PumedID")) {
                    
                    initPumedIdFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("Raw Data Available Only")) {
                    initRawDataAvailableFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("Study Type")) {
                    initStudyTypeFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("Sample Type")) {
                    initSampleTypeFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("Technology")) {
                    initTechnologyFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("Analytical Approach")) {
                    initAnalyticalApproachFilter(updatedFilterArea);
                } 
//                else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("Analytical Method")) {
//                    initAnalyticalMethodFilter(updatedFilterArea);
//                } 
                else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("Patients Group")) {
                    initPatientGroupFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("ROC AUC")) {
                    initRocFilter(updatedFilterArea);
                } else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("P Value")) {
                    initPValueFilter(updatedFilterArea);
                }
                else if (event.getChildComponent().toString().replace("&nbsp;", "").trim().equalsIgnoreCase("Fold Change")) {
                    initFoldChangeValueFilter(updatedFilterArea);
                }
            }
        };
        
        for (String str : new String[]{"PumedID", "Raw Data Available Only", "Study Type", "Sample Type", "Technology", "Analytical Approach"/*, "Analytical Method"*/, "Patients Group", "ROC AUC", "P Value","Fold Change"}) {
            VerticalLayout filter = generateFilterLabel(str);
            filter.addLayoutClickListener(listener);
            labelset.add(filter.getComponent(0));
            filtersLabelLayout.addComponent(filter);
        }
        
        advancedSearchLayoutContainer.addComponent(filtersLabelLayout);
        advancedSearchLayoutContainer.setComponentAlignment(filtersLabelLayout, Alignment.TOP_LEFT);
        advancedSearchLayoutContainer.addComponent(filtersResultsLayout);
        advancedSearchLayoutContainer.setComponentAlignment(filtersResultsLayout, Alignment.TOP_LEFT);
//        advancedSearchLayout.setExpandRatio(filtersLabelLayout, 1.5f);
//        advancedSearchLayout.setExpandRatio(filtersResultsLayout, 3);
        
    }
    
    private VerticalLayout generateFilterLabel(String title) {
        Label filterLabel = new Label("&nbsp; &nbsp; &nbsp; &nbsp; " + title);
        filterLabel.setContentMode(ContentMode.HTML);
        filterLabel.setStyleName("filterLabel");
        
        VerticalLayout layout = new VerticalLayout();
        layout.addComponentAsFirst(filterLabel);
        
        return layout;
        
    }
    private TextFieldFilter pumedIdFilter;

    private void initPumedIdFilter(VerticalLayout rightFiltersLayout) {
        if (pumedIdFilter == null) {
            pumedIdFilter = new TextFieldFilter(filtersController, 6, "PumedID");
            
        }
//        pumedIdFilter.setWidth("420px");
        rightFiltersLayout.addComponent(pumedIdFilter);
        rightFiltersLayout.setComponentAlignment(pumedIdFilter, Alignment.TOP_CENTER);
    }
    
    private OptionGroupFilter rawDataAvailableFilter;

    private void initRawDataAvailableFilter(VerticalLayout rightFiltersLayout) {
        
        if (rawDataAvailableFilter == null) {
            rawDataAvailableFilter = new OptionGroupFilter(filtersController, "Raw Data Available Only", 7, true);
            rawDataAvailableFilter.getOptionGroup().setMultiSelect(true);
            rawDataAvailableFilter.getOptionGroup().addItem("Raw Data Available Only");
            rawDataAvailableFilter.getOptionGroup().setNullSelectionAllowed(true);
        }
        rightFiltersLayout.addComponent(rawDataAvailableFilter);
        rightFiltersLayout.setComponentAlignment(rawDataAvailableFilter, Alignment.TOP_CENTER);
    }
    
    private ListSelectFilter selectStudyType;
    
    private void initStudyTypeFilter(VerticalLayout rightFiltersLayout) {
//        Label captionLabel = new Label("Study Type");
//        captionLabel.setWidth("70px");
//        captionLabel.setStyleName("custLabel");    
//        VerticalLayout filterLayout =new VerticalLayout();
//        filterLayout.setSizeUndefined();
//        rightFiltersLayout.addComponent(filterLayout);
//        rightFiltersLayout.setComponentAlignment(filterLayout, Alignment.TOP_CENTER);
        
//        filterLayout.addComponent(captionLabel);
//         filterLayout.setComponentAlignment(captionLabel, Alignment.TOP_LEFT);
//        captionLabel.setHeight("20px");
        if (selectStudyType == null) {
            selectStudyType = new ListSelectFilter(filtersController, 8, "Study Type",studyTypeValues);
            selectStudyType.setWidth("190px");
//            selectStudyType.setHeight("20px");
        }
     
//        rightFiltersLayout.setSizeUndefined();
         rightFiltersLayout.addComponent(selectStudyType);
        rightFiltersLayout.setComponentAlignment(selectStudyType, Alignment.TOP_CENTER);
        
        
    }
    
    private ListSelectFilter selectSampleTypeFilter;
    
    private void initSampleTypeFilter(VerticalLayout rightFiltersLayout) {
        if (selectSampleTypeFilter == null) {
            selectSampleTypeFilter = new ListSelectFilter(filtersController, 9, "Sample Type", sampleTypeValues);
            selectSampleTypeFilter.setWidth("190px");
        }
        rightFiltersLayout.addComponent(selectSampleTypeFilter);
        rightFiltersLayout.setComponentAlignment(selectSampleTypeFilter, Alignment.TOP_CENTER);
        
    }
    
    private ListSelectFilter selectTechnologyFilter;

    private void initTechnologyFilter(VerticalLayout rightFiltersLayout) {
        if (selectTechnologyFilter == null) {
            selectTechnologyFilter = new ListSelectFilter(filtersController, 10, "Technology", technologyValues);
        }
        rightFiltersLayout.addComponent(selectTechnologyFilter);
        rightFiltersLayout.setComponentAlignment(selectTechnologyFilter, Alignment.TOP_CENTER);
    }
    
    private ListSelectFilter analyticalApproachFilter;

    private void initAnalyticalApproachFilter(VerticalLayout rightFiltersLayout) {
        if (analyticalApproachFilter == null) {
            analyticalApproachFilter = new ListSelectFilter(filtersController, 11, "Analytical Approach", analyticalApproachValues);
        }
        rightFiltersLayout.addComponent(analyticalApproachFilter);
        rightFiltersLayout.setComponentAlignment(analyticalApproachFilter, Alignment.TOP_CENTER);
    }
//    
    private DoubleBetweenValuesFilter rocFilter;

    private void initRocFilter(VerticalLayout rightFiltersLayout) {
        if (rocFilter == null) {
            rocFilter = new DoubleBetweenValuesFilter(filtersController, 20, "ROC AUC");
        }
        rightFiltersLayout.addComponent(rocFilter);
        rightFiltersLayout.setComponentAlignment(rocFilter, Alignment.TOP_CENTER);
    }
    
    private ListSelectFilter pValueFilter;

    private void initPValueFilter(VerticalLayout rightFiltersLayout) {
        if (pValueFilter == null) {
//            pValueFilter = new DoubleBetweenValuesFilter(filtersController, 20, "P Value");
               pValueFilter = new ListSelectFilter(filtersController, 20, "P Value",  new String[]{"Significant","Not Significan"});
                pValueFilter.getList().setHeight("60px");
                pValueFilter.getList().setWidth("380px");
        }
        
        rightFiltersLayout.addComponent(pValueFilter);
        rightFiltersLayout.setComponentAlignment(pValueFilter, Alignment.TOP_CENTER);
    }
    
    private void initFoldChangeValueFilter(VerticalLayout rightFiltersLayout) {
        if (fcPatientsGrI_patGrIIFilter == null) {
//            pValueFilter = new DoubleBetweenValuesFilter(filtersController, 20, "P Value");
               fcPatientsGrI_patGrIIFilter = new ListSelectFilter(filtersController, 20, "Fold Chang Patient Gr. 1 / Patient. Gr 2",  new String[]{"Increased ","Decreased","No Change"});
                fcPatientsGrI_patGrIIFilter.getList().setHeight("60px");
                fcPatientsGrI_patGrIIFilter.getList().setWidth("380px");
                
        }
        
        rightFiltersLayout.addComponent(fcPatientsGrI_patGrIIFilter);
        rightFiltersLayout.setComponentAlignment(fcPatientsGrI_patGrIIFilter, Alignment.TOP_CENTER);
    }
    
    private ListSelectFilter fcPatientsGrI_patGrIIFilter;
    
    private void initPatientGroupFilter(VerticalLayout rightFiltersLayout) {
        VerticalLayout filterLayout = new VerticalLayout();
        filterLayout.setSpacing(true);
        filterLayout.setWidthUndefined();
        rightFiltersLayout.addComponent(filterLayout);
        rightFiltersLayout.setComponentAlignment(filterLayout, Alignment.TOP_CENTER);
//        rightFiltersLayout.setStyleName(Reindeer.LAYOUT_BLUE);
        filterLayout.addComponent(initPatientGrLayout(1));
//        filterLayout.addComponent(initPatientGrLayout(2));
        if (fcPatientsGrI_patGrIIFilter == null) {
//            fcPatientsGrI_patGrIIFilter = new DoubleBetweenValuesFilter(filtersController, 19, "Fold Chang Patient Gr. 1 / Patient. Gr 2:");
        }
//        filterLayout.addComponent(fcPatientsGrI_patGrIIFilter);
          
    }
    
    private IntegerTextFieldFilter patientNumberIFilter, patientNumberIIFilter;
    private ListSelectFilter patientGroupIFilter, patientSubGroupIFilter, patientGroupIIFilter, patientSubGroupIIFilter;
    
    private VerticalLayout initPatientGrLayout(int groupIndex) {
        VerticalLayout patientGroupLayout = new VerticalLayout();
        patientGroupLayout.setSpacing(true);
        patientGroupLayout.setWidth("450px");
       
       
        if (groupIndex == 1) {
            if (patientGroupIFilter == null) {
                patientGroupIFilter = new ListSelectFilter(filtersController, 15,"Patients Group", patGr1);
                patientGroupIFilter.getList().setHeight("60px");
                patientGroupIFilter.getList().setWidth("380px");
                patientSubGroupIFilter = new ListSelectFilter(filtersController, 16, "Patients Sub-Group",  new String[]{});
                patientSubGroupIFilter.getList().setHeight("60px");
                patientSubGroupIFilter.getList().setWidth("380px");
                patientSubGroupIFilter.setEnabled(false);
            }
            VerticalLayout pGILayout = new VerticalLayout();
            pGILayout.setSpacing(true);
            pGILayout.setWidth("380px");
            pGILayout.setMargin(new MarginInfo(false, false, true, false));
            patientGroupLayout.addComponent(pGILayout);
            pGILayout.addComponent(patientGroupIFilter);
//            pGILayout.addComponent(patientSubGroupIFilter);
//             pGILayout.setComponentAlignment(patientSubGroupIFilter,Alignment.TOP_RIGHT);
            
            patientGroupIFilter.getList().addValueChangeListener(new Property.ValueChangeListener() {
                /**
                 *
                 */
                private static final long serialVersionUID = 6456118889864963868L;
                
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                  patientSubGroupIFilter.getList().removeAllItems();
                  patientSubGroupIFilter.getList().commit();
                    if (!patientGroupIFilter.getList().getValue().toString().equalsIgnoreCase("CONTROL")) {
                       
                        for (String str : patSubGr1) {
                            patientSubGroupIFilter.getList().addItem(str);
                        }
                        patientSubGroupIFilter.setEnabled(true);

                    } else {
                        patientSubGroupIFilter.getList().addItem("OND");
                         patientSubGroupIFilter.setEnabled(true);
//                        patientSubGroupIFilter.setEnabled(false);
                        
                    }
                    patientNumberIFilter.setEnabled(true);
                }
            });
//         HorizontalLayout pGILayout = new HorizontalLayout();
//            pGILayout.setSpacing(true);
//            pGILayout.setWidth("450px");
//            pGILayout.setMargin(new MarginInfo(false, false, true, false));
            patientGroupLayout.addComponent(pGILayout);
//            pGILayout.addComponent(patientGroupIIFilter);
            pGILayout.addComponent(patientSubGroupIFilter);
        
        
        
        
        
        
        } else {
            
//            if (patientGroupIIFilter == null) {
//                patientGroupIIFilter = new ListSelectFilter(filtersController, 17,"Patients Group 2",patGr2);
//                patientGroupIIFilter.getList().setHeight("45px");
//                patientSubGroupIIFilter = new ListSelectFilter(filtersController, 18, "Patients Sub-Group 2", patSubGr2);
//                patientSubGroupIIFilter.getList().setHeight("45px");
//                patientSubGroupIIFilter.setEnabled(false);
//            }
//            
            HorizontalLayout pGILayout = new HorizontalLayout();
            pGILayout.setSpacing(true);
            pGILayout.setWidth("380px");
            pGILayout.setMargin(new MarginInfo(false, false, true, false));
            patientGroupLayout.addComponent(pGILayout);
//            pGILayout.addComponent(patientGroupIIFilter);
            pGILayout.addComponent(patientSubGroupIFilter);
            
//            patientGroupIIFilter.getList().addValueChangeListener(new Property.ValueChangeListener() {
//                /**
//                 *
//                 */
//                private static final long serialVersionUID = 6456118889864963868L;
//                
//                @Override
//                public void valueChange(Property.ValueChangeEvent event) {
//                    
//                    if (!patientGroupIIFilter.getList().getValue().toString().equalsIgnoreCase("Patients Group 2")) {
//                        patientSubGroupIIFilter.setEnabled(true);
//                    } else {
//                        patientSubGroupIIFilter.getList().select("Patients Sub-Group 2");
//                        patientSubGroupIIFilter.setEnabled(false);
//                    }
//                }
//            });
            
        }
        
         HorizontalLayout patGrNumLayout = new HorizontalLayout();
        patGrNumLayout.setHeight("20px");
        patGrNumLayout.setWidth("380px");
        patientGroupLayout.addComponent(patGrNumLayout);
        patientGroupLayout.setComponentAlignment(patGrNumLayout,Alignment.TOP_LEFT);
        patGrNumLayout.setSpacing(true);
        patGrNumLayout.setMargin(new MarginInfo(false, false, false, false));
        
         Label patientsGroupINum = labelGenerator("Minimum Number of Patients:");
        patientsGroupINum.setWidth("200px");
        patGrNumLayout.addComponent(patientsGroupINum);
        
//        Label lessThanLabel = new Label("Less Than <");
//        lessThanLabel.setStyleName(Reindeer.LABEL_SMALL);
//        patGrNumLayout.addComponent(lessThanLabel);
//        lessThanLabel.setWidth("70px");
//        patGrNumLayout.setComponentAlignment(lessThanLabel, Alignment.MIDDLE_CENTER);
        if (groupIndex == 1) {
            if (patientNumberIFilter == null) {
                patientNumberIFilter = new IntegerTextFieldFilter(filtersController, 13, "Number of  Patients (Group " + groupIndex + ")", "Only Integer Value Allowed");
                patientNumberIFilter.setAddBtnCaption("Add");
                patientNumberIFilter.setEnabled(false);
            }
            patGrNumLayout.addComponent(patientNumberIFilter);
        } else {
            if (patientNumberIIFilter == null) {
                patientNumberIFilter = new IntegerTextFieldFilter(filtersController, 14, "Number of  Patients (Group " + groupIndex + ")", "Only Integer Value Allowed");
            }
            patGrNumLayout.addComponent(patientNumberIFilter);
        }
        
        return patientGroupLayout;
    }
    
    private Label labelGenerator(String text) {
        Label label = new Label(text);
        label.setStyleName("custLabel");
        return label;
        
    }
    
    public String getDefaultText() {
        return defaultText;
    }
    
    public void setDefaultText(String defaultText) {
        this.defaultText = defaultText;
    }
    
    public KeywordFilter getKeywordFilter() {
        return keywordFilter;
    }
    
    public Button getMainSearchButton() {
        return searchingBtn;
    }
    
}
