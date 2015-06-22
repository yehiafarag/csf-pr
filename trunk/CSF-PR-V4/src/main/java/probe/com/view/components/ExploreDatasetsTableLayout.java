/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.util.Locale;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.CSFFilterSelection;
import probe.com.selectionmanager.DatasetExploringSelectionManagerRes;
import probe.com.model.beans.QuantDatasetObject;
import probe.com.view.core.CSFTable;
import probe.com.view.core.CustomExternalLink;

/**
 *
 * @author Yehia Farag
 */
public class ExploreDatasetsTableLayout extends VerticalLayout implements CSFFilter, Property.ValueChangeListener {

    private final DatasetExploringSelectionManagerRes exploringFiltersManager;
    private final String filter_id = "datasetsTable";
    private final Table datasetsTable;
    private CustomExternalLink pumedLabel;
    private CustomExternalLink rawDataLabel;
    private int pumedkey;
    private final CSFTable tableLayout;
    private final int totalStudiesNumber;
    private int[] dsIndexes;

    public ExploreDatasetsTableLayout(DatasetExploringSelectionManagerRes exploringFiltersManager, boolean[] activeHeaders) {
        this.exploringFiltersManager = exploringFiltersManager;
        exploringFiltersManager.registerFilter(ExploreDatasetsTableLayout.this);
        this.datasetsTable = new Table();
        this.totalStudiesNumber = exploringFiltersManager.getFilteredDatasetsList().length;
        tableLayout = new CSFTable(datasetsTable, "Dataset Table", totalStudiesNumber + "/" + totalStudiesNumber, null, null, null);
        initStudiesTable(activeHeaders);
        this.addComponent(tableLayout);
        tableLayout.setShowTable(false);
    }

    private void initStudiesTable(boolean[] activeHeaders) {
        datasetsTable.setSelectable(true);
        datasetsTable.setLocale(Locale.UK);
        datasetsTable.setColumnReorderingAllowed(true);
        datasetsTable.setColumnCollapsingAllowed(true);
        datasetsTable.setDragMode(Table.TableDragMode.NONE);
        datasetsTable.setMultiSelect(false);//        
        datasetsTable.setImmediate(true); // react at once when something is selected
        datasetsTable.setWidth("100%");
        datasetsTable.addContainerProperty("Index", Integer.class, null, "", null, Table.Align.RIGHT);
        datasetsTable.addContainerProperty("Author", String.class, null);
        datasetsTable.setColumnCollapsed("Author", false);
        datasetsTable.addContainerProperty("Year", String.class, null, "Year", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("Year", false);
        datasetsTable.addContainerProperty("#Identified Proteins", Integer.class, null, "#Identified Proteins", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("#Identified Proteins", !activeHeaders[2]);
        datasetsTable.addContainerProperty("#Quantified Proteins ", Integer.class, null, "#Quantified Proteins", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("#Quantified Proteins", !activeHeaders[3]);

        datasetsTable.addContainerProperty("Disease group ", String.class, null);
        datasetsTable.setColumnCollapsed("Disease group", !activeHeaders[4]);

        datasetsTable.addContainerProperty("Raw Data", CustomExternalLink.class, null);
        datasetsTable.setColumnCollapsed("Raw Data", !activeHeaders[5]);
        datasetsTable.addContainerProperty("#Files", Integer.class, null, "#Files", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("#Files", !activeHeaders[6]);
        datasetsTable.addContainerProperty("typeOfStudy", String.class, null, "Study Type", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("typeOfStudy", !activeHeaders[7]);

        datasetsTable.addContainerProperty("sampleType", String.class, null, "Sample Type", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("sampleType", !activeHeaders[8]);

        datasetsTable.addContainerProperty("sampleMatching", String.class, null, "Sample Matching", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("sampleMatching", !activeHeaders[9]);

        datasetsTable.addContainerProperty("shotgunTargeted", String.class, null, "Shotgun/Targeted", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("shotgunTargeted", !activeHeaders[10]);

        datasetsTable.addContainerProperty("technology", String.class, null, "Technology", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("technology", !activeHeaders[11]);

        datasetsTable.addContainerProperty("analyticalApproach", String.class, null, "Analytical Approach", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("analyticalApproach", !activeHeaders[12]);

        datasetsTable.addContainerProperty("enzyme", String.class, null, "Enzyme", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("enzyme", !activeHeaders[13]);

        datasetsTable.addContainerProperty("quantificationBasis", String.class, null, "Quant Basis", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("quantificationBasis", !activeHeaders[14]);

        datasetsTable.addContainerProperty("quantBasisComment", String.class, null, "Quant Basis Comment", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("quantBasisComment", !activeHeaders[15]);

        datasetsTable.addContainerProperty("normalization_strategy", String.class, null, "Normalization Strategy", null, Table.Align.LEFT);
        datasetsTable.setColumnCollapsed("normalization_strategy", !activeHeaders[16]);

        datasetsTable.addContainerProperty("PumedID", CustomExternalLink.class, null);
        datasetsTable.setColumnCollapsed("PumedID", !activeHeaders[17]);

        datasetsTable.addContainerProperty("patientsGroup1", String.class, null, "Patients Gr.I", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup1", !activeHeaders[18]);

        datasetsTable.addContainerProperty("patientsGroup1Number", Integer.class, null, "#Patients Gr.I", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup1Number", !activeHeaders[19]);

        datasetsTable.addContainerProperty("patientsGroup1Comm", String.class, null, "Patients Gr.I Comm", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup1Comm", !activeHeaders[20]);

        datasetsTable.addContainerProperty("patientsSubGroup1", String.class, null, "Patients Sub-Gr.I", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsSubGroup1", !activeHeaders[21]);

        datasetsTable.addContainerProperty("patientsGroup2", String.class, null, "Patients Gr.II", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup2", !activeHeaders[22]);

        datasetsTable.addContainerProperty("patientsGroup2Number", Integer.class, null, "#Patients Gr.II", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup2Number", !activeHeaders[23]);

        datasetsTable.addContainerProperty("patientsGroup2Comm", String.class, null, "Patients Gr.II Comm", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsGroup2Comm", !activeHeaders[24]);

        datasetsTable.addContainerProperty("patientsSubGroup2", String.class, null, "Patients Sub-Gr.II", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("patientsSubGroup2", !activeHeaders[25]);

        datasetsTable.addContainerProperty("additionalComments", String.class, null, "Comments", null, Table.Align.RIGHT);
        datasetsTable.setColumnCollapsed("additionalComments", !activeHeaders[26]);



//        updateTableRecords(filteredStudiesList);

        datasetsTable.addValueChangeListener(this);
     
        selectionChanged("filter");

    }

    
    private void updateTableRecords(QuantDatasetObject[] updatedStudiesList) {
        dsIndexes = new int[updatedStudiesList.length];
       
            datasetsTable.removeAllItems();
//            if (updatedStudiesList.size() != studiesList.size()) {
//                filteredStudiesList.clear();
//                filteredStudiesList.addAll(updatedStudiesList);
//            } else {
//                filteredStudiesList.clear();
//                filteredStudiesList.addAll(studiesList);
//            }

            tableLayout.updateCounter(updatedStudiesList.length+ "/" +totalStudiesNumber);
            int index = 0;

            for (QuantDatasetObject pb : updatedStudiesList) {
                CustomExternalLink rawDatalink = null;
                if (pb.getRawDataUrl().equalsIgnoreCase("Raw Data Not Available")) {
                    rawDatalink = null;
                } else {
                    rawDatalink = new CustomExternalLink("Get Raw Data", pb.getRawDataUrl());
                    rawDatalink.setDescription("Link To Raw Data");
                }
                Integer quantProtNum = pb.getQuantifiedProteinsNumber();
                if (quantProtNum == -1) {
                    quantProtNum = null;
                }

                Integer patGr1Num = pb.getPatientsGroup1Number();
            if (patGr1Num == -1) {
                patGr1Num = null;
            }
            Integer patGr2Num = pb.getPatientsGroup2Number();
            if (patGr2Num == -1) {
                patGr2Num = null;
            }

            CustomExternalLink pumedID = new CustomExternalLink(pb.getPumedID(), "http://www.ncbi.nlm.nih.gov/pubmed/" + pb.getPumedID());
            datasetsTable.addItem(new Object[]{index, pb.getAuthor(), pb.getYear()+"",pb.getIdentifiedProteinsNumber(),quantProtNum,pb.getDiseaseGroups(),   rawDatalink,  pb.getFilesNumber(),pb.getTypeOfStudy(),pb.getSampleType(),pb.getSampleMatching(),pb.getShotgunTargeted(),pb.getTechnology(),pb.getAnalyticalApproach(),pb.getEnzyme(),pb.getQuantificationBasis(),pb.getQuantBasisComment(),pb.getNormalizationStrategy(),pumedID,pb.getPatientsGroup1(),patGr1Num,pb.getPatientsGroup1Comm(),pb.getPatientsSubGroup1(),pb.getPatientsGroup2(),patGr2Num,pb.getPatientsGroup2Comm(),pb.getPatientsSubGroup2(),pb.getAdditionalcomments()}, index);
            dsIndexes[index] = pb.getUniqId();
            index++;
        }
        datasetsTable.sort(new Object[]{"Year"}, new boolean[]{false});
       

    }
    
    
    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        if (datasetsTable.getValue() != null) {
            pumedkey = (Integer) datasetsTable.getValue();
        } else {
            return;
        }
        if (pumedLabel != null) {
            pumedLabel.rePaintLable("black");
           
        }
         if (rawDataLabel != null) {
            rawDataLabel.rePaintLable("black");
           
        }
        final Item item = datasetsTable.getItem(pumedkey);
        pumedLabel = (CustomExternalLink) item.getItemProperty("PumedID").getValue();
        rawDataLabel = (CustomExternalLink) item.getItemProperty("Raw Data").getValue();
        pumedLabel.rePaintLable("white");
        if (rawDataLabel != null) {
            rawDataLabel.rePaintLable("white");
        }
        int dsIndex = dsIndexes[(Integer)item.getItemProperty("Index").getValue()];
        exploringFiltersManager.setSelectedDataset(dsIndex);
        exploringFiltersManager.setFilterSelection(new CSFFilterSelection("dsSelection",new int[]{dsIndex},filter_id, null));
        
    }

    @Override
    public void selectionChanged(String type) {
        if(type.equalsIgnoreCase("filter")){
        updateTableRecords(exploringFiltersManager.getFilteredDatasetsList());        
        }else{
        
            int datasetId = exploringFiltersManager.getSelectedDataset();
            int i = 0;
            for (; i < dsIndexes.length; i++) {
                if (dsIndexes[i] == datasetId) {
                    datasetsTable.select(i);
                    break;
                }

            }

//            selectDataset(datasetId);
        }
    }

    @Override
    public String getFilterId() {
        return filter_id;
    }

    @Override
    public void removeFilterValue(String value) {
    }
    

}
