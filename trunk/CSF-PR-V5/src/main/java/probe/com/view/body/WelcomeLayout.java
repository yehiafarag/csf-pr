/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.body;

import com.vaadin.server.ThemeResource;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Link;
import com.vaadin.ui.VerticalLayout;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 *
 * static layout has main project information and access to admin login
 */
public class WelcomeLayout extends VerticalLayout implements Serializable {

    /**
     * initialize body layout
     *
     * @param adminIcon the access button for admin Layout
     *
     *
     */
    public WelcomeLayout(Button adminIcon) {

        this.setWidth("100%");
        HorizontalLayout mainBodyHLayout = new HorizontalLayout();
        mainBodyHLayout.setWidth("100%");
        this.addComponent(mainBodyHLayout);

        VerticalLayout mainBody = new VerticalLayout();
        mainBody.setWidth("100%");
        mainBodyHLayout.addComponent(mainBody);
        HorizontalLayout topLayout = new HorizontalLayout();
        topLayout.setWidth("100%");
        mainBody.addComponent(topLayout);

        VerticalLayout bottomLayout = new VerticalLayout();
        bottomLayout.setWidth("100%");
        bottomLayout.setSpacing(true);
        bottomLayout.setMargin(new MarginInfo(true, true, true, true));
        mainBody.addComponent(bottomLayout);
        mainBody.setComponentAlignment(bottomLayout, Alignment.BOTTOM_CENTER);

        VerticalLayout leftLayout = new VerticalLayout();
        leftLayout.setWidth("100%");
        leftLayout.setMargin(new MarginInfo(true, true, true, false));
        topLayout.addComponent(leftLayout);

        VerticalLayout rightLayout = new VerticalLayout();
        rightLayout.setWidth("100%");
        topLayout.addComponent(rightLayout);

        Label infoLable = new Label("<h2 style='margin-left:40px;'>Welcome to CSF Proteome Resource (CSF-PR)</h2>");
        infoLable.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(infoLable);
        leftLayout.setComponentAlignment(infoLable, Alignment.MIDDLE_LEFT);

        Label para_1 = new Label("<p align='justify' Style='margin-left:40px;color:#585858;'><font size=\"2\">CSF Proteome Resource (CSF-PR) is an online repository of mass spectrometry based proteomics "
                + "experiments on human cerebrospinal fluid (CSF). CSF is in direct contact with the central nervous"
                + "system (CNS) and can give indications about the state of the CNS.This is particularly relevant for "
                + "neurodegenerative diseases, such as Multiple Sclerosis, where CSF would be a natural place to look "
                + "for disease biomarkers.</font></p>");

        para_1.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(para_1);

        Label para_2 = new Label("<p align='justify' Style='margin-left:40px;color:#585858;'><font size=\"2\">The data can be viewed by selecting individual experiments or by searching for key words (protein "
                + "name/accession number or peptide sequence) across all experiments. For GeLC-MS experiments the "
                + "distribution of the identified proteins in the gel is also displayed.</font></p>");

        para_2.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(para_2);

        ThemeResource img1 = new ThemeResource("img/overview.jpg");
        Link image1 = new Link(null, new ThemeResource("img/fulloverview.jpg"));
        image1.setIcon(img1);
        image1.setTargetName("_blank");

        rightLayout.addComponent(image1);
        rightLayout.setMargin(new MarginInfo(true, true, true, true));
        rightLayout.setComponentAlignment(image1, Alignment.MIDDLE_CENTER);

        Label para_3 = new Label("<p align='justify' Style='margin-left:40px;color:#585858;'><font size=\"2\">CSF-PR is being developed by the <a Style='color:#585858;' href='http://www.uib.no/rg/probe' target=\"_blank\">Proteomics Unit</a> at the<a Style='color:#585858;' href='http://www.uib.no/biomedisin/en' target=\"_blank\"> Department of Biomedicine at the University of Bergen</a>, Norway, in close collaboration with <a Style='color:#585858;' href='http://haukeland.no/en/OmOss/Avdelinger/ms/Sider/om-oss.aspx' target=\"_blank\">The Norwegian Multiple Sclerosis Competence Centre</a>, Haukeland University Hospital, Bergen, Norway.</font></p>");
        para_3.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(para_3);

        Label para_4 = new Label("<p align='justify' Style='margin-left:40px;color:#585858;'><font size=\"2\">See also: <a Style='color:#585858;' href='http://www.mcponline.org/content/13/11/3152.full.pdf+html' target=\"_blank\">Guldbrandsen et al.: In-depth Characterization of the Cerebrospinal Fluid (CSF) Proteome Displayed Through the CSF Proteome Resource (CSF-PR). Mol Cell Proteomics. 2014.</a></font></p>");
        para_4.setContentMode(ContentMode.HTML);
        leftLayout.addComponent(para_4);

        bottomLayout.addComponent(adminIcon);
        bottomLayout.setComponentAlignment(adminIcon, Alignment.BOTTOM_RIGHT);
        bottomLayout.setExpandRatio(adminIcon, 0.05f);

    }
}
