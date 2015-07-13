

package probe.com.view.body.quantdatasetsoverview.quantproteinstabsheet.peptidescontainer;

import com.vaadin.data.Property;
import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.AbsoluteLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.GridLayout;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import probe.com.model.beans.QuantPeptide;
import probe.com.view.core.InformationField;
import probe.com.view.core.jfreeutil.StackedBarPeptideComponent;

/**
 *
 * @author yfa041
 */
public class PeptideSequanceLocationOverview extends VerticalLayout {

    private boolean noPeptide = true;
    private DecimalFormat df = null;
    private AbsoluteLayout sigProtSeqBar, allPepProtSegBar;
    private AbsoluteLayout allPeptidesContainer, significantPeptidesContainer;
    private int significantPeptidesNumber = 0;
    private int totalPeptidesNumber = 0;
    private OptionGroup showSigneficantPeptidesOnly;

    public PeptideSequanceLocationOverview(String sequance, Set<QuantPeptide> quantPepSet, int width) {
        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
        otherSymbols.setGroupingSeparator('.');
        df = new DecimalFormat("#.###", otherSymbols);
        this.setWidth(width + "px");
        this.setHeightUndefined();
        this.setSpacing(true);
        this.setMargin(new MarginInfo(true, false, true, false));
        this.setStyleName(Reindeer.LAYOUT_WHITE);

        //init peptides form layout  
        peptideForm = initPeptidesForm(width);
        peptideForm.setVisible(false);

        if (!sequance.equalsIgnoreCase("Not Available")) {
            //init containers
            allPeptidesContainer = new AbsoluteLayout();
            significantPeptidesContainer = new AbsoluteLayout();
            significantPeptidesContainer.setVisible(false);
            allPeptidesContainer.setStyleName(Reindeer.LAYOUT_WHITE);
            allPeptidesContainer.setWidth(width + "px");
            significantPeptidesContainer.setWidth(width + "px");

            allPeptidesContainer.setHeightUndefined();
            significantPeptidesContainer.setHeightUndefined();

            showSigneficantPeptidesOnly = new OptionGroup();
            this.addComponent(showSigneficantPeptidesOnly);
            this.setComponentAlignment(showSigneficantPeptidesOnly, Alignment.TOP_RIGHT);
            showSigneficantPeptidesOnly.setWidth("150px");
            showSigneficantPeptidesOnly.setNullSelectionAllowed(true); // user can not 'unselect'
            showSigneficantPeptidesOnly.setMultiSelect(true);

            showSigneficantPeptidesOnly.addItem("Significant Peptides Only");
            showSigneficantPeptidesOnly.addStyleName("horizontal");
            showSigneficantPeptidesOnly.addValueChangeListener(new Property.ValueChangeListener() {
                @Override
                public void valueChange(Property.ValueChangeEvent event) {
                    if (lastselectedPeptideComp != null) {
                        lastselectedPeptideComp.heighlight(false);
                        lastselectedPeptideComp = null;
                    }

                    if (showSigneficantPeptidesOnly.getValue().toString().equalsIgnoreCase("[Significant Peptides Only]")) {
                        allPeptidesContainer.setVisible(false);
                        significantPeptidesContainer.setVisible(true);
                        if (significantPeptidesContainer.getComponentCount() == 1) {
                            peptideForm.setVisible(false);
                        }

                    } else {
                        allPeptidesContainer.setVisible(true);
                        significantPeptidesContainer.setVisible(false);
                        if (allPeptidesContainer.getComponentCount() == 2) {
                            peptideForm.setVisible(true);
                        } else {
                            peptideForm.setVisible(false);
                        }

                    }
                }
            });

            this.addComponent(significantPeptidesContainer);
            this.addComponent(allPeptidesContainer);
            //init protein bar
            allPepProtSegBar = new AbsoluteLayout();
            allPepProtSegBar.setWidth(width + "px");
            allPepProtSegBar.setHeight("15px");
            allPepProtSegBar.setStyleName("lightgraylayout");
            allPeptidesContainer.addComponent(allPepProtSegBar, "left: " + (0) + "px; top: " + (0) + "px;");

            sigProtSeqBar = new AbsoluteLayout();
            sigProtSeqBar.setWidth(width + "px");
            sigProtSeqBar.setHeight("15px");
            sigProtSeqBar.setStyleName("lightgraylayout");
            significantPeptidesContainer.addComponent(sigProtSeqBar, "left: " + (0) + "px; top: " + (0) + "px;");
            //init barchart components
            LinkedHashSet<StackedBarPeptideComponent> allPeptidesStackedBarComponentsMap = this.initAllBarChartComponents(false, width, sequance, quantPepSet, true);
            LinkedHashSet<StackedBarPeptideComponent> significantPeptidesStackedBarComponents = this.initAllBarChartComponents(true, width, sequance, quantPepSet, false);

            //sort peptides based on sequance length
            this.initPeptidesStackedBarComponentsLayout(allPeptidesStackedBarComponentsMap, allPeptidesContainer);
            this.initPeptidesStackedBarComponentsLayout(significantPeptidesStackedBarComponents, significantPeptidesContainer);

            if (totalPeptidesNumber == 1) {
                updatePeptidesForm((QuantPeptide) quantPepSet.toArray()[0]);

            }

        }
        if (noPeptide) {
            updateProtForm((QuantPeptide) quantPepSet.toArray()[0]);
            if (allPeptidesContainer != null) {
                allPeptidesContainer.setVisible(false);
            }
            if (significantPeptidesContainer != null) {
                significantPeptidesContainer.setVisible(false);
            }
            if (showSigneficantPeptidesOnly != null) {
                showSigneficantPeptidesOnly.setVisible(false);
            }
        }

        this.addComponent(peptideForm);

    }

    public boolean isNoPeptide() {
        return noPeptide;
    }
    private final GridLayout peptideForm;
    private StackedBarPeptideComponent lastselectedPeptideComp, lastselectedSigPeptideComp;
    private InformationField pepSequance, peptideModification, modificationComment, pValue, pValueComm, foldChange, roc;

    private GridLayout initPeptidesForm(int width) {
        GridLayout peptideFormLayout = new GridLayout(3, 4);
        peptideFormLayout.setWidth(width + "px");
        peptideFormLayout.setHeightUndefined();
        pepSequance = new InformationField("Peptide Sequance");
        peptideFormLayout.addComponent(pepSequance, 0, 0);
        peptideModification = new InformationField("Peptide Modification");
        peptideFormLayout.addComponent(peptideModification, 1, 0);
        modificationComment = new InformationField("Modification Comment");
        peptideFormLayout.addComponent(modificationComment, 2, 0);
        pValue = new InformationField("P Value");
        peptideFormLayout.addComponent(pValue, 0, 1);
        pValueComm = new InformationField("P Value Comments");
        peptideFormLayout.addComponent(pValueComm, 1, 1);
        foldChange = new InformationField("Fold Change");
        peptideFormLayout.addComponent(foldChange, 2, 1);
        roc = new InformationField("Roc Auc");
        peptideFormLayout.addComponent(roc, 0, 2);
        return peptideFormLayout;
    }

    private void updatePeptidesForm(QuantPeptide peptide) {

        pepSequance.setVisible(true);
        peptideModification.setVisible(true);
        modificationComment.setVisible(true);
        pepSequance.setValue(peptide.getPeptideSequance(), null);
        peptideModification.setValue(peptide.getPeptideModification(), null);
        modificationComment.setValue(peptide.getModification_comment(), null);
        String pval;
        if (peptide.getString_p_value() != null && !peptide.getString_p_value().equalsIgnoreCase("") && !peptide.getString_p_value().equalsIgnoreCase("Not Available")) {
            pval = peptide.getString_p_value() + " ";
        } else {
            pval = "          ";
        }
        if (peptide.getP_value() != -1000000000) {
            pval += "(" + df.format(peptide.getP_value()) + ")";
        }
        pValue.setValue(pval, null);

        pValueComm.setValue(peptide.getP_value_comments(), null);

        String foldChane;
        if (peptide.getString_fc_value() != null && !peptide.getString_fc_value().equalsIgnoreCase("") && !peptide.getString_fc_value().equalsIgnoreCase("Not Available")) {
            foldChane = peptide.getString_fc_value() + " ";
        } else {
            foldChane = "          ";
        }
        if (peptide.getFc_value() != -1000000000) {
            foldChane += "(" + df.format(peptide.getFc_value()) + ")";
        }
        foldChange.setValue(foldChane, null);
        String rocv;
        if (peptide.getRoc_auc() != -1000000000) {
            rocv = df.format(peptide.getRoc_auc()) + "";
        } else {
            rocv = "Not Available";
        }
        this.roc.setValue(rocv, null);
        this.peptideForm.setVisible(true);
    }

    private void updateProtForm(QuantPeptide peptide) {

        pepSequance.setVisible(false);
        peptideModification.setVisible(false);
        modificationComment.setVisible(false);

        String pval;
        if (peptide.getString_p_value() != null && !peptide.getString_p_value().equalsIgnoreCase("") && !peptide.getString_p_value().equalsIgnoreCase("Not Available")) {
            pval = peptide.getString_p_value() + " ";
        } else {
            pval = "          ";
        }
        if (peptide.getP_value() != -1000000000) {
            pval += "(" + this.df.format(peptide.getP_value()) + ")";
        }
        pValue.setValue(pval, null);

        pValueComm.setValue(peptide.getP_value_comments(), null);

        String foldChane;
        if (peptide.getString_fc_value() != null && !peptide.getString_fc_value().equalsIgnoreCase("") && !peptide.getString_fc_value().equalsIgnoreCase("Not Available")) {
            foldChane = peptide.getString_fc_value() + " ";
        } else {
            foldChane = "          ";
        }
        if (peptide.getFc_value() != -1000000000) {
            foldChane += "(" + df.format(peptide.getFc_value()) + ")";
        }
        foldChange.setValue(foldChane, null);
        String rocv;
        if (peptide.getRoc_auc() != -1000000000) {
            rocv = df.format(peptide.getRoc_auc()) + "";
        } else {
            rocv = "Not Available";
        }
        this.roc.setValue(rocv, null);
        this.peptideForm.setVisible(true);

    }

    private boolean checkIntersect(StackedBarPeptideComponent smallXComp, StackedBarPeptideComponent bigXComp) {
        int area = smallXComp.getX0() + smallXComp.getWidthArea();
        return bigXComp.getX0() < area;

    }

    private LinkedHashSet<StackedBarPeptideComponent> initAllBarChartComponents(boolean significatOnly, int width, String sequance, Set<QuantPeptide> quantPepSet, boolean count) {
        LinkedHashSet<StackedBarPeptideComponent> barComponentMap = new LinkedHashSet<StackedBarPeptideComponent>();

        double charW = (double) width / (double) sequance.length();
        for (QuantPeptide quantPeptide : quantPepSet) {
            if (quantPeptide.getPeptideSequance().equalsIgnoreCase("not available")) {
                continue;
            }
            noPeptide = false;

            String peptideSequance = quantPeptide.getPeptideSequance().trim();
            if (peptideSequance.contains(".")) {
                peptideSequance = quantPeptide.getPeptideSequance().replace(".", "").trim().substring(1, quantPeptide.getPeptideSequance().length() - 2);
            }
            double peptideLayoutWidth = peptideSequance.length() * charW;
            int x0 = (int) (sequance.split(peptideSequance)[0].length() * charW);
            if (!significatOnly) {
                StackedBarPeptideComponent peptideStackedBarComponent = new StackedBarPeptideComponent(x0, (int) (peptideLayoutWidth - 2));
                peptideStackedBarComponent.setWidth(peptideLayoutWidth - 2 + "px");
                peptideStackedBarComponent.setDescription("Sequance: " + quantPeptide.getPeptideSequance());
                peptideStackedBarComponent.setParam("peptide", quantPeptide);
                peptideStackedBarComponent.setParam("sequance", quantPeptide.getPeptideSequance());
                if (count) {
                    this.totalPeptidesNumber++;
                }

                if (quantPeptide.getString_p_value().equalsIgnoreCase("Significant")) {
                    peptideStackedBarComponent.setSignificant(true);
                    if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Up")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("redstackedlayout");
                    } else if (quantPeptide.getString_fc_value() == null || quantPeptide.getString_fc_value().equalsIgnoreCase("") || quantPeptide.getString_fc_value().trim().equalsIgnoreCase("Not Provided")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("lightbluestackedlayout");
                    } else {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("greenstackedlayout");

                    }

                } else {
                    peptideStackedBarComponent.setSignificant(false);
                    if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Up")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("midredstackedlayout");
                    } else if (quantPeptide.getString_fc_value() == null || quantPeptide.getString_fc_value().equalsIgnoreCase("") || quantPeptide.getString_fc_value().trim().equalsIgnoreCase("Not Provided")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("lightbluestackedlayout");
                    } else {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("midgreenstackedlayout");

                    }
                }

                //add listener
                peptideStackedBarComponent.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                    @Override
                    public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                        Component pepComp = event.getComponent();
                        if (pepComp instanceof StackedBarPeptideComponent) {
                            StackedBarPeptideComponent pepBarComp = (StackedBarPeptideComponent) pepComp;
                            if (lastselectedPeptideComp == pepBarComp) {
                                pepBarComp.heighlight(false);
                                if (totalPeptidesNumber != 1) {
                                    peptideForm.setVisible(false);
                                }
                                lastselectedPeptideComp = null;
                                return;
                            }

                            if (lastselectedPeptideComp != null) {
                                lastselectedPeptideComp.heighlight(false);
                            }

                            pepBarComp.heighlight(true);
                            lastselectedPeptideComp = pepBarComp;
                            QuantPeptide peptide = (QuantPeptide) pepBarComp.getParam("peptide");
                            updatePeptidesForm(peptide);
                        }
                    }
                });

                barComponentMap.add(peptideStackedBarComponent);

            } else {
                if (quantPeptide.getString_p_value().equalsIgnoreCase("Significant")) {
                    significantPeptidesNumber++;
                    StackedBarPeptideComponent peptideStackedBarComponent = new StackedBarPeptideComponent(x0, (int) (peptideLayoutWidth - 2));
                    peptideStackedBarComponent.setSignificant(true);
                    peptideStackedBarComponent.setWidth(peptideLayoutWidth - 2 + "px");
                    peptideStackedBarComponent.setDescription("Sequance: " + quantPeptide.getPeptideSequance());//                
                    peptideStackedBarComponent.setParam("peptide", quantPeptide);
                    peptideStackedBarComponent.setParam("sequance", quantPeptide.getPeptideSequance());

                    if (quantPeptide.getString_fc_value().equalsIgnoreCase("Increased") || quantPeptide.getString_fc_value().equalsIgnoreCase("Up")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("redstackedlayout");
                    } else if (quantPeptide.getString_fc_value() == null || quantPeptide.getString_fc_value().equalsIgnoreCase("") || quantPeptide.getString_fc_value().trim().equalsIgnoreCase("Not Provided")) {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("lightbluestackedlayout");
                    } else {
                        peptideStackedBarComponent.setDefaultStyleShowAllMode("greenstackedlayout");

                    }

                    peptideStackedBarComponent.addLayoutClickListener(new LayoutEvents.LayoutClickListener() {
                        @Override
                        public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                            Component pepComp = event.getComponent();
                            if (pepComp instanceof StackedBarPeptideComponent) {
                                StackedBarPeptideComponent pepBarComp = (StackedBarPeptideComponent) pepComp;
                                if (lastselectedPeptideComp == pepBarComp) {
                                    pepBarComp.heighlight(false);
                                    peptideForm.setVisible(false);
                                    lastselectedPeptideComp = null;
                                    return;
                                }

                                if (lastselectedPeptideComp != null) {
                                    lastselectedPeptideComp.heighlight(false);
                                }

                                pepBarComp.heighlight(true);
                                lastselectedPeptideComp = pepBarComp;
                                QuantPeptide peptide = (QuantPeptide) pepBarComp.getParam("peptide");
                                updatePeptidesForm(peptide);
                            }
                        }
                    });

                    barComponentMap.add(peptideStackedBarComponent);
                }
            }

        }
        return barComponentMap;

    }

    private void initPeptidesStackedBarComponentsLayout(LinkedHashSet<StackedBarPeptideComponent> stackedBarComponents, AbsoluteLayout peptidesComponentsContainer) {
        int top = 0;
        List< StackedBarPeptideComponent> initLevel = new ArrayList<StackedBarPeptideComponent>(stackedBarComponents);
        List< StackedBarPeptideComponent> updatedLevel = new ArrayList<StackedBarPeptideComponent>(stackedBarComponents);
        List< StackedBarPeptideComponent> nextLevel = new ArrayList<StackedBarPeptideComponent>();

        boolean intersect = true;
        while (intersect) {
            intersect = false;
            for (int x = 0; x < initLevel.size() && initLevel.size() > 1; x++) {
                StackedBarPeptideComponent pepBarComp = (StackedBarPeptideComponent) initLevel.get(x);
                for (int y = 0; y < initLevel.size(); y++) {
                    if (y <= x) {
                        continue;
                    }
                    StackedBarPeptideComponent pepBarComp2 = (StackedBarPeptideComponent) initLevel.get(y);
                    boolean check;
                    if (pepBarComp.getX0() > pepBarComp2.getX0()) {
                        check = checkIntersect(pepBarComp2, pepBarComp);
                    } else //if(barComp.getX0()>pepBarComp.getX0())
                    {
                        check = checkIntersect(pepBarComp, pepBarComp2);
                    }
                    if (check) {
                        intersect = true;
                        if (pepBarComp.getWidthArea() > pepBarComp2.getWidthArea()) {
                            updatedLevel.remove(y);
                            pepBarComp2.setLevel(pepBarComp2.getLevel() + 1);
                            nextLevel.add(pepBarComp2);
                        } else if (pepBarComp.getWidthArea() == pepBarComp2.getWidthArea()) {
                            if (!pepBarComp2.isSignificant()) {
                                updatedLevel.remove(y);
                                pepBarComp2.setLevel(pepBarComp2.getLevel() + 1);
                                nextLevel.add(pepBarComp2);
                            } else {
                                updatedLevel.remove(x);
                                pepBarComp.setLevel(pepBarComp.getLevel() + 1);
                                nextLevel.add(pepBarComp);

                            }

                        } else {
                            updatedLevel.remove(x);
                            pepBarComp.setLevel(pepBarComp.getLevel() + 1);
                            nextLevel.add(pepBarComp);
                        }
                        initLevel.clear();
                        initLevel.addAll(updatedLevel);

                        break;
                    }

                }
                if (intersect) {
                    break;
                }

            }

            if (!intersect) {
                for (StackedBarPeptideComponent pepBarComp : updatedLevel) {
                    peptidesComponentsContainer.addComponent(pepBarComp, "left: " + pepBarComp.getX0() + "px; top: " + (top) + "px;");
                }
                updatedLevel.clear();
                updatedLevel.addAll(initLevel);
                initLevel.clear();

            }
            if (!intersect && !nextLevel.isEmpty()) {

                initLevel.clear();
                updatedLevel.clear();
                initLevel.addAll(nextLevel);
                updatedLevel.addAll(nextLevel);
                nextLevel.clear();
                intersect = true;
                top = top + 20;
            }

        }
        peptidesComponentsContainer.setHeight(Math.max(40, top) + "px");

    }

    public int getSignificantPeptidesNumber() {
        return significantPeptidesNumber;
    }

    public int getTotalPeptidesNumber() {
        return totalPeptidesNumber;
    }

}
