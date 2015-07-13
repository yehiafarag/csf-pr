package probe.com.view.body.quantdatasetsoverview.quantproteinscomparisons;

import com.vaadin.event.LayoutEvents;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.VerticalLayout;
import java.util.Map;
import java.util.Set;
import probe.com.model.beans.GroupsComparison;

/**
 * this class is the container for the comparison bar chart the class developed
 * using JfreeChart and DiVA concept this class support both comparisons
 * selection and table data selection
 *
 * @author Yehia Farag
 */
public class ComparisonChartContainer extends HorizontalLayout{

    private final  JFreeBarchartDivaWrapper chart ;
    private final VerticalLayout closeCompariosonBtn;
    private final GroupsComparison comparison; 
    
    
   
    
    public Map<Integer, Set<String>> getCompProtMap() {
        return chart.getCompProtMap();
    }

    public void addCloseListiner(LayoutEvents.LayoutClickListener closeListener) {

        closeCompariosonBtn.addLayoutClickListener(closeListener);

    }

    public void addChartListener(LayoutEvents.LayoutClickListener chartListener) {
        this.chart.addLayoutClickListener(chartListener);

    }

    public ComparisonChartContainer(GroupsComparison comparison, int width) {
        this.setStyleName("lightborder");
        this.setWidthUndefined();
        this.setHeight("250px");
        this.setMargin(false);       
        width = width - 26;
        this.comparison=comparison;        
        chart = new JFreeBarchartDivaWrapper(width, comparison);
        this.addComponent(chart);
        closeCompariosonBtn = new VerticalLayout();
        closeCompariosonBtn.setWidth("20px");
        closeCompariosonBtn.setHeight("20px");
        closeCompariosonBtn.setStyleName("closebtn");
        this.addComponent(closeCompariosonBtn);
    }


    public void updateExternalSelection(Set<String> accessions) {
        chart.updateExternalTableSelection(accessions);
    }
   public void updateChartsWithSelectedChartColumn(Set<String> accessions,boolean reset){
        chart.updateChartsWithSelectedChartColumn(accessions,reset);
    }

    public void resizeChart(int width) {
        width = width-26;
        chart.resize(width);
    }
    public void reset(){
        chart.reset();
    
    }
    public String getComparisonHeader() {
        return comparison.getComparisonHeader();

    }

    public void setHeghlighted(boolean hl) {
        if (hl) {
            this.setStyleName("lightselectedborder");
        } else {
            this.setStyleName("lightborder");
        }

    }
    public void heighLightBar(int barIndex) {
        chart.heighLightBar(barIndex);

    }

}
