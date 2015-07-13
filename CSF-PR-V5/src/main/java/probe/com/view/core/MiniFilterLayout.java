
package probe.com.view.core;

import com.vaadin.ui.GridLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.util.Map;
import java.util.Set;
import probe.com.selectionmanager.CSFFilter;
import probe.com.selectionmanager.DatasetExploringCentralSelectionManager;

/**
 *
 * @author Yehia Farag
 */
public class MiniFilterLayout extends VerticalLayout implements CSFFilter{

    private final DatasetExploringCentralSelectionManager Filter_Manager;
    private final String filter_Id = "miniFilter";
    private final GridLayout filtersLayout;
   public MiniFilterLayout(DatasetExploringCentralSelectionManager Filter_Manager){
   this.Filter_Manager = Filter_Manager;
   Filter_Manager.registerFilter(MiniFilterLayout.this);
   this.setWidth("100%");
   this.setHeight("200px");
   this.setStyleName(Reindeer.LAYOUT_WHITE);
   filtersLayout = new GridLayout(15,3);
   filtersLayout.setMargin(false);
   filtersLayout.setSpacing(true);
   this.setMargin(false);
   filtersLayout.setHideEmptyRowsAndColumns(false);
   
   this.addComponent(filtersLayout);
   
   
    }

    @Override
    public String getFilterId() {
        return filter_Id;
    }

    @Override
    public void selectionChanged(String type) {
//        if(type.equalsIgnoreCase("Disease_Groups_Level")){
//        Map<String, Set<String>> appliedFilterList = Filter_Manager.getAppliedFilterList();
//        filtersLayout.removeAllComponents();
//        if(appliedFilterList.isEmpty())
//            return;
//        filtersLayout.addComponent(new Label(" [ "));
////        sb.append("(");
//        int counter=0;
//        for (String key : appliedFilterList.keySet()) {
//           
//            for (String filter : appliedFilterList.get(key)) { 
////                MiniStudyFilter closfilter = new MiniStudyFilter(key, filter,Filter_Manager,Filter_Manager.getFilterTitle(key));
////                filtersLayout.addComponent(closfilter);
////                filtersLayout.setColumnExpandRatio(counter, closfilter.getExpandRatio());
//                counter++;
//            }
//
//        }
//        filtersLayout.addComponent(new Label(" ] "));
//        }
    }

    @Override
    public void removeFilterValue(String value) {
    }
    


    
}
