/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.data.Item;
import com.vaadin.data.Property;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.PopupView;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import probe.com.model.beans.QuantProtein;
import probe.com.view.core.CustomExportBtnLayout;
import probe.com.view.core.CustomExternalLink;
import probe.com.view.core.ShowLabel;
import probe.com.view.core.TableResizeSet;

/**
 *
 * @author Yehia Farag
 */
public class QuantDataSearchTable extends Table implements Serializable, Property.ValueChangeListener{

//    private OptionGroup columnController = new OptionGroup();
    
 private HorizontalLayout mainLayout;
 private OptionGroup control1,control2,control3,control4,control5,control6;

    public QuantDataSearchTable( List<QuantProtein>  quantProtList,Set<String> filtersList) {
//        columnController.setMultiSelect(true);
        this.setSelectable(true);
        this.setColumnReorderingAllowed(true);
        this.setColumnCollapsingAllowed(true);
        
      
        this.setImmediate(true); // react at once when something is selected
        this.setWidth("100%");
        this.setHeight("200px");

        this.addContainerProperty("Index", Integer.class, null,"", null, Table.Align.RIGHT);

        String uniprotAcc ="Uniprot/Nextprot Accession";
        this.addContainerProperty(uniprotAcc, CustomExternalLink.class, null);
        this.setColumnCollapsible(uniprotAcc, false);
//        columnController.addItem(uniprotAcc);
        String uniprotName ="Uniprot Protein Name";
        this.addContainerProperty(uniprotName, String.class, null);
//         columnController.addItem(uniprotName);
        String pubAcc ="Publication Accession";
        this.addContainerProperty(pubAcc, String.class, null);
//         columnController.addItem(pubAcc);
         String pubProtName = "Publication Protein Name";
        this.addContainerProperty(pubProtName, String.class, null);
//        columnController.addItem(pubProtName);
        
        String rawDataLink = "Raw Data Link";        
        this.addContainerProperty(rawDataLink, CustomExternalLink.class, null);
//        columnController.addItem(rawDataLink);
        String numQuantProt = "# Quantified Proteins";
        this.addContainerProperty(numQuantProt, Integer.class, null, numQuantProt, null, Table.Align.RIGHT);
//        columnController.addItem(numQuantProt);
        
//        String PepModi = "Peptide Modification";
//        this.addContainerProperty(PepModi, String.class, null);
////        columnController.addItem(PepModi);
//        this.addContainerProperty("modification Comment", String.class, null);
        this.addContainerProperty("PumedID", CustomExternalLink.class, null);
        this.addContainerProperty("Study Type", String.class, null);
        this.addContainerProperty("Sample Type", String.class, null);
        this.addContainerProperty("Patient Group 1", String.class, null);
        this.addContainerProperty("# Patients Group 1", Integer.class, null, "# Patients Group 1", null, Table.Align.RIGHT);
        this.addContainerProperty("Patient Sub-Group 1", String.class, null);
        this.addContainerProperty("Comment Patient group 1", String.class, null);

        this.addContainerProperty("Patient Group 2", String.class, null);
        this.addContainerProperty("# Patients Group 2", Integer.class, null, "# Patients Group 2", null, Table.Align.RIGHT);
        this.addContainerProperty("Patient Sub-Group 2", String.class, null);
        this.addContainerProperty("Comment Patient group 2", String.class, null);

        this.addContainerProperty("Sample Matching(V.or Amount)", String.class, null);
        this.addContainerProperty("Normalization Strategy", String.class, null);

        this.addContainerProperty("Fold Change Patient Group 1 / Patient Group 2", Double.class, null, "Fold Change Patient Group 1 / Patient Group 2", null, Table.Align.RIGHT);
        this.addContainerProperty("P Value", Double.class, null, "P Value", null, Table.Align.RIGHT);
        this.addContainerProperty("ROC AUC", Double.class, null, "ROC AUC", null, Table.Align.RIGHT);

        this.addContainerProperty("Technology", String.class, null);

        this.addContainerProperty("Analytical Approach", String.class, null);

        this.addContainerProperty("Enzyme", String.class, null);

        this.addContainerProperty("Shotgun/Targeted Quant", String.class, null);

        this.addContainerProperty("Analytical Method", String.class, null);

       
         this.addContainerProperty("Comment Quantification Basis", String.class, null);
        this.addContainerProperty("Additional Comments", String.class, null);
         this.addContainerProperty("key", String.class, null,"",null,Table.Align.RIGHT);
        this.setColumnCollapsed("key", true);
        this.setColumnCollapsible("key", true);
        int index = 1;
        for (QuantProtein pb : quantProtList) {
            CustomExternalLink uniprotAccessionlink = new CustomExternalLink(pb.getUniprotAccession(), "http://www.uniprot.org/uniprot/" + pb.getUniprotAccession());
            uniprotAccessionlink.setDescription("UniProt link for " + pb.getUniprotAccession());
            CustomExternalLink rawDatalink = new CustomExternalLink("Link To Raw Data", pb.getRawDataAvailable());
            rawDatalink.setDescription("Link To Raw Data");
            CustomExternalLink pumedID = new CustomExternalLink(pb.getPumedID(), "http://www.ncbi.nlm.nih.gov/pubmed/"+pb.getPumedID());
            rawDatalink.setDescription("Link to PumedID: " + pb.getPumedID());
            Double pvalue = pb.getpValue();
            if(pvalue ==  -1000000000)
                pvalue = null;
            Double fc = pb.getFcPatientGroupIonPatientGroupII();
            if(fc ==  -1000000000)
                fc = null;
            Double roc = pb.getRocAuc();
            if(roc ==  -1000000000)
                roc = null;
            
            Integer grn = pb.getPatientsGroupINumber();
            if(grn ==  -1000000000)
                grn = null;
             Integer grni = pb.getPatientsGroupIINumber();
            if(grni ==  -1000000000)
                grni = null;
           
            
            this.addItem(new Object[]{index, uniprotAccessionlink, pb.getUniprotProteinName(), pb.getPublicationAccNumber(), pb.getPublicationProteinName(), rawDatalink,
                pb.getQuantifiedProteinsNumber(),/* pb.getPeptideModification(), pb.getModificationComment(),*/ pumedID, pb.getTypeOfStudy(), pb.getSampleType(), pb.getPatientGroupI(), grn, pb.getPatientSubGroupI(), pb.getPatientGrIComment(), pb.getPatientGroupII(), grni, pb.getPatientSubGroupII(), pb.getPatientGrIIComment(), pb.getSampleMatching(), pb.getNormalizationStrategy(), fc, pvalue, roc, pb.getTechnology(), pb.getAnalyticalApproach(), pb.getEnzyme(), pb.getShotgunOrTargetedQquant(), pb.getAnalyticalApproach(), pb.getQuantBasisComment(), pb.getAdditionalComments(),pb.getqPeptideKey()}, index);
            index++;
        }
       initColumnControllerLayout();
        for (Object o : this.getColumnHeaders()) {
           
            if (o.toString().equalsIgnoreCase("Uniprot/Nextprot Accession")){//|| o.toString().equalsIgnoreCase("Uniprot Protein Name") ||o.toString().equalsIgnoreCase("PumedID")  || o.toString().equalsIgnoreCase("Analytical Approach") ||o.toString().equalsIgnoreCase("Analytical Approach")) {
                this.setColumnCollapsed(o, false);
//                columnController.select(o);
                continue;
            }            
            this.setColumnCollapsed(o, true);
            
        }
        if (filtersList != null && !filtersList.isEmpty()) {
            for (String header : filtersList) {
                if (header.equalsIgnoreCase("Uniprot Protein Name")){// || header.equalsIgnoreCase("PumedID") || header.equalsIgnoreCase("Analytical Approach") || header.equalsIgnoreCase("Analytical Approach")) {
                    this.setColumnCollapsed(header, false);
                } else {
                    this.setColumnCollapsed(header, true);
                }

            }

        }
        this.addValueChangeListener(QuantDataSearchTable.this);
         setColumnWidth("Index", 33);
       
        
    }
    
//    @Override
//    public void valueChange(Property.ValueChangeEvent event) {
//        selfSelect = false;
//        System.out.println("value changed  "+event.getProperty().getValue());
//        this.setColumnCollapsingAllowed(true);
//        Set<Object> values = (Set<Object>) event.getProperty().getValue();
//        for (Object value : values) {
//            if (this.control1.getItemIds().contains(value)) {                
//                this.setColumnCollapsed(value, !control1.isSelected(value));
//            }
//            else if (this.control2.getItemIds().contains(value)) {
//               this.setColumnCollapsed(value, !control1.isSelected(value));
//            }
//              else if (this.control3.getItemIds().contains(value)) {
//                this.setColumnCollapsed(value, !control3.isSelected(value));
//            }
//              else if (this.control4.getItemIds().contains(value)) {
//                this.setColumnCollapsed(value, !control4.isSelected(value));
//            }
//              else if (this.control5.getItemIds().contains(value)) {
//               this.setColumnCollapsed(value, !control5.isSelected(value));
//            }
//              else if (this.control6.getItemIds().contains(value)) {
//               this.setColumnCollapsed(value, !control6.isSelected(value));
//            }
//            
//        }
//       for(Object header: this.getColumnHeaders()){
//       if()
//       
//       
//       }
        
//        for (Object header : this.control1.getItemIds()) {
//            if (values.contains(header)) {
//                this.setColumnCollapsed(header, false);
//            } else {
//                this.setColumnCollapsed(header, true);
//            }
////            System.out.println("value selected : " + header);
//        }
//        for (Object header : this.control2.getItemIds()) {
//            if (values.contains(header)) {
//                this.setColumnCollapsed(header, false);
//            } else {
//                this.setColumnCollapsed(header, true);
//            }
////            System.out.println("value selected : " + header);
//        }
//        for (Object header : this.control3.getItemIds()) {
//            if (values.contains(header)) {
//                this.setColumnCollapsed(header, false);
//            } else {
//                this.setColumnCollapsed(header, true);
//            }
////            System.out.println("value selected : " + header);
//        }
//        for (Object header : this.control4.getItemIds()) {
//            if (values.contains(header)) {
//                this.setColumnCollapsed(header, false);
//            } else {
//                this.setColumnCollapsed(header, true);
//            }
////            System.out.println("value selected : " + header);
//        }
//        for (Object header : this.control5.getItemIds()) {
//            if (values.contains(header)) {
//                this.setColumnCollapsed(header, false);
//            } else {
//                this.setColumnCollapsed(header, true);
//            }
////            System.out.println("value selected : " + header);
//        }
//        for (Object header : this.control6.getItemIds()) {
//            if (values.contains(header)) {
//                this.setColumnCollapsed(header, false);
//            } else {
//                this.setColumnCollapsed(header, true);
//            }
////            System.out.println("value selected : " + header);
//        }
//        this.commit();
//        selfSelect = true;
//        
////        this.setColumnCollapsingAllowed(false);
//        
//    }
    
    public HorizontalLayout getColumnController() {
        return mainLayout;
    }
    private void initColumnControllerLayout() {
        mainLayout = new HorizontalLayout();
         mainLayout.setSpacing(true);
        mainLayout.setMargin(new MarginInfo(true,true,false,true));
        control1 = new OptionGroup();
        control1.setMultiSelect(true);
        control2 = new OptionGroup();
        control2.setMultiSelect(true);
         control3 = new OptionGroup();
        control3.setMultiSelect(true);
         control4 = new OptionGroup();
        control4.setMultiSelect(true);
         control5 = new OptionGroup();
        control5.setMultiSelect(true);
         control6 = new OptionGroup();
        control6.setMultiSelect(true);
        for (Object o : this.getColumnHeaders()) {
            if (o.toString().equalsIgnoreCase("")|| o.toString().equalsIgnoreCase("Uniprot/Nextprot Accession")) {
                continue;
            }
            if (control1.getItemIds().size() < 5) {
                
                control1.addItem(o);
                continue;
            }            
            if (control2.getItemIds().size() < 5) {
                
                control2.addItem(o);
                continue;
            }            
            if (control3.getItemIds().size() < 5) {                
                control3.addItem(o);
                continue;
            }            
             if (control4.getItemIds().size() < 5) {                
                control4.addItem(o);
                continue;
            }      
              if (control5.getItemIds().size() < 5) {                
                control5.addItem(o);
                continue;
            }     
               if (control6.getItemIds().size() < 5) {                
                control6.addItem(o);
                
            }     
         
//            if(o.toString().equalsIgnoreCase("Uniprot/Nextprot Accession"))
//            {
//                this.setColumnCollapsed(o, false);
//                columnController.select(o);
//                continue;
//            } 
//            this.setColumnCollapsed(o,true);
        }
           mainLayout.addComponent(control1);
           control1.addValueChangeListener(initValueChangeListener(control1));
            mainLayout.addComponent(control2);
            control2.addValueChangeListener(initValueChangeListener(control2));
            mainLayout.addComponent(control3);
            control3.addValueChangeListener(initValueChangeListener(control3));
            mainLayout.addComponent(control4);
            control4.addValueChangeListener(initValueChangeListener(control4));
            mainLayout.addComponent(control5);
            control5.addValueChangeListener(initValueChangeListener(control5));
            mainLayout.addComponent(control6);
            control6.addValueChangeListener(initValueChangeListener(control6));
//            init =false;
    }
    private boolean init =false;
    private boolean selfSelect= true;
     @Override
    public final void setColumnCollapsed(Object propertyId, boolean collapsed)
            throws IllegalStateException {
              
        super.setColumnCollapsed(propertyId, collapsed);
        if(init&&selfSelect)
            selectFilter(propertyId,collapsed);
       
    }

    private void selectFilter(Object filterId, boolean select) {
//        selfSelect = false;
//    for (Object header : this.control1.getItemIds()) {
        if (control1.getItemIds().contains(filterId)) {
            control1.removeValueChangeListener(this);
            if (select) {
                control1.unselect(filterId);
            } else {
                control1.select(filterId);
            }
            control1.addValueChangeListener(this);

        } else if (control2.getItemIds().contains(filterId)) {
            if (select) {
                control2.unselect(filterId);
            } else {
                control2.select(filterId);
            }

        } else if (control3.getItemIds().contains(filterId)) {
            if (select) {
                control3.unselect(filterId);
            } else {
                control3.select(filterId);
            }

        } else if (control4.getItemIds().contains(filterId)) {
            if (select) {
                control4.unselect(filterId);
            } else {
                control4.select(filterId);
            }

        } else if (control5.getItemIds().contains(filterId)) {
            if (select) {
                control5.unselect(filterId);
            } else {
                control5.select(filterId);
            }

        } else if (control6.getItemIds().contains(filterId)) {
            if (select) {
                control6.unselect(filterId);
            } else {
                control6.select(filterId);
            }

        }
//            System.out.println("value selected : " + header);
//        }

    }

    public void setInit(boolean init) {
        this.init = init;
    }
    
    private ValueChangeListener initValueChangeListener(final OptionGroup control){
    ValueChangeListener listener = new ValueChangeListener() {

            @Override
            public void valueChange(Property.ValueChangeEvent event) {
                selfSelect = false;

                for (Object header : control.getItemIds()) {
                    setColumnCollapsed(header, !control.isSelected(header));
                }

           commit();
        selfSelect = true;
            }
        };
        control.addValueChangeListener(listener);
        return listener;

    }
       private int protKey;
    private CustomExternalLink searchTableAccessionLable,rawDataLinkLabel,pumedIdLabel;
    
    @Override
    public void valueChange(Property.ValueChangeEvent event) {
        //externalLinkTableLabel
        if(searchTableAccessionLable!= null)
            searchTableAccessionLable.rePaintLable("black");
         if(rawDataLinkLabel!= null)
            rawDataLinkLabel.rePaintLable("black");
         if(pumedIdLabel!= null)
            pumedIdLabel.rePaintLable("black");
         
        if (this.getValue() != null) {
            protKey = (Integer) this.getValue();
            System.out.println("prot key " + protKey);
        } else {
            return;
        }
        final Item item = this.getItem(protKey);
        searchTableAccessionLable = (CustomExternalLink) item.getItemProperty("Uniprot/Nextprot Accession").getValue();
        searchTableAccessionLable.rePaintLable("white");
         rawDataLinkLabel = (CustomExternalLink) item.getItemProperty("Raw Data Link").getValue();
        rawDataLinkLabel.rePaintLable("white");
        
        pumedIdLabel = (CustomExternalLink) item.getItemProperty("PumedID").getValue();
        pumedIdLabel.rePaintLable("white");     
        
       
       
       
       }
    
    

}
