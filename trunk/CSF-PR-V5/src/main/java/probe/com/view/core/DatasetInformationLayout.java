/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.TabSheet.Tab;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import probe.com.handlers.MainHandler;
import probe.com.view.body.IdentificationDatasetLayout;

/**
 *
 * @author Yehia Farag
 */
public class DatasetInformationLayout extends VerticalLayout implements Serializable,Button.ClickListener {

//    private final IconGenerator excelExporterIconGenerator = new IconGenerator();
    private final  VerticalLayout miniLayout = new VerticalLayout();
    private final int datasetId;
    private final TabSheet mainTabSheet;
    private final MainHandler handler;
    private Tab homeTab =null;
    private IdentificationDatasetLayout datasetLayout;
            
    

    public DatasetInformationLayout(MainHandler handler, int datasetId,TabSheet mainTabSheet) {
        this.datasetId = datasetId;
        this.mainTabSheet=mainTabSheet;
        this.handler=handler;
        this.setSpacing(true);
        this.setMargin(true);
        this.setSizeFull();
        miniLayout.setSpacing(false);
        miniLayout.setMargin(false);
        HorizontalLayout miniLabelsGroupILayout = new HorizontalLayout();
        miniLabelsGroupILayout.setSpacing(true);
        miniLabelsGroupILayout.setMargin(new MarginInfo(false, false, false, false));
        miniLayout.addComponent(miniLabelsGroupILayout);
        miniLabelsGroupILayout.addComponent(this.generateMiniLabel("Instrument Type:", handler.getDataset(datasetId).getInstrumentType()));
        miniLabelsGroupILayout.addComponent(this.generateMiniLabel("Species:", handler.getDataset(datasetId).getSpecies()));
        HorizontalLayout miniProtNumberLayout = this.generateMiniLabel("Proteins Number:", "" + handler.getDataset(datasetId).getNumberValidProt());
        miniProtNumberLayout.setDescription("Number of validated proteins");
        miniLabelsGroupILayout.addComponent(miniProtNumberLayout);        
        miniLabelsGroupILayout.addComponent(this.generateMiniLabel("Sample Processing:", handler.getDataset(datasetId).getSampleProcessing()));

        HorizontalLayout miniLabelsGroupIILayout = new HorizontalLayout();
        miniLabelsGroupIILayout.setSpacing(true);

        miniLabelsGroupIILayout.setMargin(new MarginInfo(false, false, false, false));
        miniLayout.addComponent(miniLabelsGroupIILayout);

        miniLabelsGroupIILayout.addComponent(this.generateMiniLabel("Sample Type:", handler.getDataset(datasetId).getSampleType()));

        miniLabelsGroupIILayout.addComponent(this.generateMiniLabel("Frag Mode:", handler.getDataset(datasetId).getFragMode()));
        HorizontalLayout miniPepNumberLayout = this.generateMiniLabel("Peptides Number:", "" + handler.getDataset(datasetId).getPeptidesNumber());
        miniPepNumberLayout.setDescription("Number of validated peptides");
        miniLabelsGroupIILayout.addComponent(miniPepNumberLayout);
        miniLabelsGroupIILayout.addComponent(this.generateMiniLabel("Fraction Numbers:", "" + handler.getDataset(datasetId).getFractionsNumber()));

        

        HorizontalLayout topLabelsGroupILayout = new HorizontalLayout();
        topLabelsGroupILayout.setSpacing(true);
        topLabelsGroupILayout.setMargin(new MarginInfo(false, true, false, false));
        this.addComponent(topLabelsGroupILayout);
        this.addComponent(topLabelsGroupILayout);

        topLabelsGroupILayout.addComponent(this.generateLabel("Dataset  Name: ", handler.getDataset(datasetId).getName()));
        topLabelsGroupILayout.addComponent(this.generateLabel("Uploaded By:", handler.getDataset(datasetId).getUploadedByName()));
        topLabelsGroupILayout.addComponent(this.generateLabel("E-mail:", handler.getDataset(datasetId).getEmail()));
        topLabelsGroupILayout.addComponent(new VerticalLayout());
        topLabelsGroupILayout.setExpandRatio(topLabelsGroupILayout.getComponent(0), 3);
        topLabelsGroupILayout.setExpandRatio(topLabelsGroupILayout.getComponent(1), 2);

        topLabelsGroupILayout.setExpandRatio(topLabelsGroupILayout.getComponent(2), 2);

        topLabelsGroupILayout.setExpandRatio(topLabelsGroupILayout.getComponent(3), 0.5f);

        this.addComponent(generateLabel("Description:", handler.getDataset(datasetId).getDescription()));

        HorizontalLayout bottomLabelsGroupILayout = new HorizontalLayout();
        bottomLabelsGroupILayout.setSpacing(true);
        bottomLabelsGroupILayout.setMargin(new MarginInfo(false, true, false, false));
        this.addComponent(bottomLabelsGroupILayout);
        
      
        bottomLabelsGroupILayout.addComponent(this.generateLabel("Instrument Type:", handler.getDataset(datasetId).getInstrumentType()));
        bottomLabelsGroupILayout.addComponent(this.generateLabel("Species:", handler.getDataset(datasetId).getSpecies()));
        HorizontalLayout protNumberLayout = this.generateLabel("Proteins Number:", "" + handler.getDataset(datasetId).getNumberValidProt());
        protNumberLayout.setDescription("Number of validated proteins");
        bottomLabelsGroupILayout.addComponent(protNumberLayout);
        
        
        Button exportProtBtn = new Button("Export Proteins");
//        exportProtBtn.setIcon(new ThemeResource("img/excel.jpg"));
        exportProtBtn.setStyleName(Reindeer.BUTTON_SMALL);
        exportProtBtn.setWidth("100px");
        bottomLabelsGroupILayout.addComponent(exportProtBtn);

        HorizontalLayout bottomLabelsGroupIILayout = new HorizontalLayout();
        bottomLabelsGroupIILayout.setSpacing(true);

        bottomLabelsGroupIILayout.setMargin(new MarginInfo(false, true, false, false));
        this.addComponent(bottomLabelsGroupIILayout);

        bottomLabelsGroupIILayout.addComponent(this.generateLabel("Sample Processing:", handler.getDataset(datasetId).getSampleProcessing()));

        bottomLabelsGroupIILayout.addComponent(this.generateLabel("Frag Mode:", handler.getDataset(datasetId).getFragMode()));
        HorizontalLayout pepNumberLayout = this.generateLabel("Peptides Number:", "" + handler.getDataset(datasetId).getPeptidesNumber());
        pepNumberLayout.setDescription("Number of validated peptides");
        bottomLabelsGroupIILayout.addComponent(pepNumberLayout);
        Button exportPeptidestBtn = new Button("Export Peptides");
        exportPeptidestBtn.setStyleName(Reindeer.BUTTON_SMALL);
        exportPeptidestBtn.setWidth("100px");
        bottomLabelsGroupIILayout.addComponent(exportPeptidestBtn);

        HorizontalLayout bottomLabelsGroupIIILayout = new HorizontalLayout();
        bottomLabelsGroupIIILayout.setSpacing(true);

        bottomLabelsGroupIIILayout.setMargin(new MarginInfo(false, true, false, false));
        this.addComponent(bottomLabelsGroupIIILayout);
        bottomLabelsGroupIIILayout.addComponent(this.generateLabel("Sample Type:", handler.getDataset(datasetId).getSampleType()));

        String href = null;
        if (handler.getDataset(datasetId).getPublicationLink().equalsIgnoreCase("NOT AVAILABLE") || handler.getDataset(datasetId).getPublicationLink().equalsIgnoreCase("")) {

            bottomLabelsGroupIIILayout.addComponent(this.generateLabel("Publication Link:", "Not Available"));

        } else {
            href = handler.getDataset(datasetId).getPublicationLink().toLowerCase();
            if ((!href.contains("http://")) && (!href.contains("https://"))) {
                href = "http://" + href;
            }

            bottomLabelsGroupIIILayout.addComponent(this.generateLabel("Publication Link:", "<a href='" + href + "'  target='_blank'>Click to Visit</a>"));
        }
        bottomLabelsGroupIIILayout.addComponent(this.generateLabel("Fraction Numbers:", "" + handler.getDataset(datasetId).getFractionsNumber()));

        if(mainTabSheet != null){
        Button loadDatasetBtn = new Button("Load Dataset");
        loadDatasetBtn.setStyleName(Reindeer.BUTTON_SMALL);
        loadDatasetBtn.setWidth("100px");
        loadDatasetBtn.addClickListener(DatasetInformationLayout.this);
        bottomLabelsGroupIIILayout.addComponent(loadDatasetBtn);
        }
        else{
             bottomLabelsGroupIIILayout.addComponent(new VerticalLayout());        
        }

        topLabelsGroupILayout.setWidth("100%");
        bottomLabelsGroupILayout.setWidth("100%");
        bottomLabelsGroupILayout.setExpandRatio(bottomLabelsGroupILayout.getComponent(0), 3);
        bottomLabelsGroupILayout.setExpandRatio(bottomLabelsGroupILayout.getComponent(1), 2);
        bottomLabelsGroupILayout.setExpandRatio(bottomLabelsGroupILayout.getComponent(2), 2);
        bottomLabelsGroupILayout.setExpandRatio(exportProtBtn, 0.5f);

        bottomLabelsGroupIILayout.setWidth("100%");
        bottomLabelsGroupIILayout.setExpandRatio(bottomLabelsGroupIILayout.getComponent(0), 3);
        bottomLabelsGroupIILayout.setExpandRatio(bottomLabelsGroupIILayout.getComponent(1), 2);
        bottomLabelsGroupIILayout.setExpandRatio(bottomLabelsGroupIILayout.getComponent(2), 2);
        bottomLabelsGroupIILayout.setExpandRatio(exportPeptidestBtn, 0.5f);

        bottomLabelsGroupIIILayout.setWidth("100%");
        bottomLabelsGroupIIILayout.setExpandRatio(bottomLabelsGroupIIILayout.getComponent(0), 3);
        bottomLabelsGroupIIILayout.setExpandRatio(bottomLabelsGroupIIILayout.getComponent(1), 2);
        bottomLabelsGroupIIILayout.setExpandRatio(bottomLabelsGroupIIILayout.getComponent(2), 2);
        bottomLabelsGroupIIILayout.setExpandRatio(bottomLabelsGroupIIILayout.getComponent(3), 0.5f);
//        
//        miniLayout.setWidth("100%");
//        miniLabelsGroupILayout.setWidth("100%");
//        miniLabelsGroupIILayout.setWidth("100%");
        miniLabelsGroupILayout.setExpandRatio(miniLabelsGroupILayout.getComponent(0), 1);
        miniLabelsGroupILayout.setExpandRatio(miniLabelsGroupILayout.getComponent(1), 1);
        miniLabelsGroupILayout.setExpandRatio(miniLabelsGroupILayout.getComponent(2), 1);
        miniLabelsGroupILayout.setExpandRatio(miniLabelsGroupILayout.getComponent(3), 3);
        
        miniLabelsGroupIILayout.setExpandRatio(miniLabelsGroupIILayout.getComponent(0), 1);
        miniLabelsGroupIILayout.setExpandRatio(miniLabelsGroupIILayout.getComponent(1), 1);
        miniLabelsGroupIILayout.setExpandRatio(miniLabelsGroupIILayout.getComponent(2), 1);
        miniLabelsGroupIILayout.setExpandRatio(miniLabelsGroupIILayout.getComponent(3), 3);

    }

    private Label generateTitleLabel(String headerTitle) {
        Label titleLabel = new Label(headerTitle);
        titleLabel.setContentMode(ContentMode.HTML);
        titleLabel.setStyleName("datasetInfoHeaders");
        return titleLabel;

    }

    private Label generateValueLabel(String value) {

        if (value.length() > 300) {
            value = "<textarea readonly>" + value + "</textarea>";

        }

        Label valueLabel = new Label(value);

        valueLabel.setContentMode(ContentMode.HTML);
        valueLabel.setStyleName("datasetInfoValues");
        return valueLabel;

    }

    private HorizontalLayout generateLabel(String title, String value) {
        Label datasetNameHeader = generateTitleLabel(title);
        Label datasetNameValue = generateValueLabel(value);
        HorizontalLayout datasetNameLayout = new HorizontalLayout();
        datasetNameLayout.addComponent(datasetNameHeader);
        datasetNameLayout.addComponent(datasetNameValue);
        datasetNameLayout.setSpacing(true);
        return datasetNameLayout;

    }
     private HorizontalLayout generateMiniLabel(String title, String value) {
        Label titleLabel = new Label(title);
        titleLabel.setContentMode(ContentMode.HTML);
        titleLabel.setStyleName("miniDatasetInfoHeaders");
        
          Label valueLabel = new Label(value);
        valueLabel.setContentMode(ContentMode.HTML);
        valueLabel.setStyleName("miniDatasetInfoValues");
        
        HorizontalLayout datasetlabelLayout = new HorizontalLayout();
        datasetlabelLayout.addComponent(titleLabel);
        datasetlabelLayout.addComponent(valueLabel);
        datasetlabelLayout.setSpacing(false);
        return datasetlabelLayout;

    }

    public VerticalLayout getMiniLayout() {
        return miniLayout;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if(datasetLayout == null)
            datasetLayout = new IdentificationDatasetLayout(handler, datasetId);
        if (homeTab == null || mainTabSheet.getTabPosition(homeTab) < 0) {
            homeTab = mainTabSheet.addTab(datasetLayout, handler.getDataset(datasetId).getName(), null);
            homeTab.setClosable(true);
        }
        mainTabSheet.setSelectedTab(homeTab);

    }

    
}
