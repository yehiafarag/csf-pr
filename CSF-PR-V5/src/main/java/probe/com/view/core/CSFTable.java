package probe.com.view.core;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class CSFTable extends VerticalLayout implements Serializable {

    private final Table mainTable;
    private final VerticalLayout csfTableLayout = new VerticalLayout();
    private final HorizontalLayout searchFieldLayout;
    private String csfTableHeight = "160px";
    private ShowLabel show;
    private boolean stat;
    private final Label csfLabel;
    private final String labelStr;
    private   final HorizontalLayout underTableLayout;

    public CSFTable(Table mainTable, String labelStr, String counter, HorizontalLayout searchFieldLayout, HorizontalLayout lowerLeftLayout, HorizontalLayout exportLayout) {

        this.labelStr = labelStr;
        this.mainTable = mainTable;
        this.searchFieldLayout = searchFieldLayout;
        this.mainTable.setHeight(csfTableHeight);
        MarginInfo m = new MarginInfo(false, false, true, false);
        this.setMargin(m);
        this.setSpacing(false);
        this.setWidth("100%");

        final HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setHeight("20px");
        headerLayout.setSpacing(true);
        show = new ShowLabel(true);
        show.setHeight("20px");
        headerLayout.addComponent(show);
        headerLayout.setComponentAlignment(show, Alignment.BOTTOM_LEFT);
        stat = true;

        csfLabel = new Label(labelStr + " (" + counter + ")");
        csfLabel.setStyleName("filterShowLabel");
        csfLabel.setContentMode(ContentMode.HTML);
        csfLabel.setHeight("20px");
        headerLayout.addComponent(csfLabel);
        headerLayout.setComponentAlignment(csfLabel, Alignment.TOP_RIGHT);

        this.addComponent(headerLayout);

        //allow search in 
        if (this.searchFieldLayout != null) {
            headerLayout.addComponent(searchFieldLayout);
            headerLayout.setComponentAlignment(searchFieldLayout, Alignment.TOP_RIGHT);
        }

        this.addComponent(csfTableLayout);
        this.setComponentAlignment(csfTableLayout, Alignment.MIDDLE_CENTER);
        csfTableLayout.addComponent(mainTable);

       underTableLayout = new HorizontalLayout();
        underTableLayout.setWidth("100%");
        underTableLayout.setHeight("25px");
        this.addComponent(underTableLayout);
        this.setComponentAlignment(underTableLayout, Alignment.TOP_CENTER);

        if (lowerLeftLayout == null) {
            lowerLeftLayout = new HorizontalLayout();
            lowerLeftLayout.setWidth("185px");
            lowerLeftLayout.setHeight("15px");
            lowerLeftLayout.setStyleName(Reindeer.LAYOUT_WHITE);
        }
        lowerLeftLayout.setSpacing(true);
        underTableLayout.addComponent(lowerLeftLayout);
        lowerLeftLayout.setMargin(new MarginInfo(false, false, false, false));
        underTableLayout.setComponentAlignment(lowerLeftLayout, Alignment.MIDDLE_LEFT);

        HorizontalLayout lowerRightLayout = new HorizontalLayout();
        lowerRightLayout.setSpacing(true);
        lowerRightLayout.setWidth("550px");
        underTableLayout.addComponent(lowerRightLayout);
        underTableLayout.setComponentAlignment(lowerRightLayout, Alignment.BOTTOM_RIGHT);
        underTableLayout.setExpandRatio(lowerRightLayout, 0.7f);

        final TableResizeSet trs1 = new TableResizeSet(mainTable, csfTableHeight);//resize tables 
        lowerLeftLayout.addComponent(trs1);
        lowerLeftLayout.setComponentAlignment(trs1, Alignment.BOTTOM_LEFT);

        if (exportLayout == null) {
            exportLayout = new HorizontalLayout();
        }
        lowerRightLayout.addComponent(exportLayout);
        lowerRightLayout.setComponentAlignment(exportLayout, Alignment.BOTTOM_RIGHT);

        headerLayout.addLayoutClickListener(new com.vaadin.event.LayoutEvents.LayoutClickListener() {
            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {

                setShowTable(!stat);
            }
        });

    }

    public void setShowTable(boolean stat) {
        this.stat = stat;
        show.updateIcon(stat);
        csfTableLayout.setVisible(stat);
        underTableLayout.setVisible(stat);

    }

    public String getCsfTableHeight() {
        return csfTableHeight;
    }

    public void setCsfTableHeight(String csfTableHeight) {
        this.csfTableHeight = csfTableHeight;
    }

    public void updateCounter(String counter) {

        csfLabel.setValue(labelStr + " (" + counter + ")");

    }

}
