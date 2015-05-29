/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view;

import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;
import java.util.Map;
import probe.com.handlers.MainHandler;
import probe.com.model.beans.DatasetDetailsBean;
import probe.com.view.core.DatasetInformationLayout;
import probe.com.view.core.HideOnClickLayout;

/**
 *
 * @author Yehia Farag
 */
public class UpdatedProjectsLayout extends VerticalLayout implements Serializable, Button.ClickListener{
    private final MainHandler handler;
    public UpdatedProjectsLayout(MainHandler handler,TabSheet mainTabSheet)
    {
        this.handler = handler;
        this.setSpacing(true);
        this.setMargin(true);
//        Label infoLable = new Label("<h2>Available Datasets in (CSF-PR)</h2>");
//        infoLable.setContentMode(ContentMode.HTML);
//        this.addComponent(infoLable);
//        this.setComponentAlignment(infoLable, Alignment.MIDDLE_LEFT);
         if (handler.getDatasetList() == null || handler.getDatasetList().isEmpty()) {
            Label noExpLable = new Label("<h4 style='font-family:verdana;color:black;font-weight:bold;'>Sorry No Dataset Availabe Now !</h4>");
            noExpLable.setContentMode(ContentMode.HTML);
            this.addComponent(noExpLable);
        }
         else{
             Map<Integer, DatasetDetailsBean> dsList = handler.getDatasetDetailsList();
            for (int x : dsList.keySet()) {
                HideOnClickLayout dslayout = initDatasetLayout(x, dsList.get(x),mainTabSheet);  
                this.addComponent(dslayout);
                
        Label spacer = new Label();
        spacer.setContentMode(ContentMode.HTML);      
        spacer.setStyleName("spacer");
        this.addComponent(spacer);

            }
        }

        
        
    
    
    
    }
    
    
    private HideOnClickLayout initDatasetLayout(int dsId,DatasetDetailsBean ds,TabSheet mainTabSheet){      
        DatasetInformationLayout datasetInfoLayout =new DatasetInformationLayout(handler, dsId,mainTabSheet);
        HideOnClickLayout dsLayout = new HideOnClickLayout(ds.getName(), datasetInfoLayout,datasetInfoLayout.getMiniLayout());
        dsLayout.setMargin(new MarginInfo(false,false,true,false));
        return dsLayout;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    
}
