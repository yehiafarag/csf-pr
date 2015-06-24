/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.components;

import com.vaadin.event.LayoutEvents;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.Reindeer;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import probe.com.dal.Query;
import probe.com.view.core.ClosableFilterLabel;

/**
 *
 * @author Yehia Farag
 */
public class FiltersControl implements Serializable {

    private final Map<String, ClosableFilterLabel> filtersBtnsMap = new HashMap<String, ClosableFilterLabel>();
    private final Map<String, HorizontalLayout> filtersLabelLayoutMap = new HashMap<String, HorizontalLayout>();
    private String searchKeyWords;
    private final HorizontalLayout mainFilterLayout;
    VerticalLayout fullleft = new VerticalLayout();
    VerticalLayout fullright = new VerticalLayout();
    

    private final LayoutEvents.LayoutClickListener listener;
    private Query query;

    private final VerticalLayout minimumfilterLayout = new VerticalLayout();

    public Query getQuery() {
        return query;
    }
//    private final VerticalLayout filtersHeaders,filtersValues;

    public FiltersControl() {
        mainFilterLayout = new HorizontalLayout();
        minimumfilterLayout.setSpacing(true);
        
        mainFilterLayout.addComponent(fullleft);
        mainFilterLayout.addComponent(fullright);
        MarginInfo marginInfo = new MarginInfo(false, false, false, true);
        mainFilterLayout.setMargin(marginInfo);
        listener = new LayoutEvents.LayoutClickListener() {

            @Override
            public void layoutClick(LayoutEvents.LayoutClickEvent event) {
                String filterLabelId = event.getClickedComponent().toString().replace("&nbsp;", "").trim();
                Map<String, ClosableFilterLabel> ufiltersBtnsMap = new HashMap<String, ClosableFilterLabel>();
                ufiltersBtnsMap.putAll(filtersBtnsMap);
                for (ClosableFilterLabel btn : ufiltersBtnsMap.values()) {
                    if (btn.getFilterTitle().equalsIgnoreCase(filterLabelId)) {
                        btn.layoutClick(event);
                    }

                }
                ufiltersBtnsMap.clear();
            }
        };

    }

    public void addFilter(ClosableFilterLabel filterBtn) {

        if (!filtersBtnsMap.containsKey(filterBtn.getCaption())) {
            if (filtersLabelLayoutMap.containsKey(filterBtn.getFilterTitle())) {
                //get filter with specific header
                HorizontalLayout tempFilterLayout = filtersLabelLayoutMap.get(filterBtn.getFilterTitle());
                tempFilterLayout.addComponent(filterBtn);
            } else {
                //create filter header and filter layout
                HorizontalLayout tempFilterLayout = new HorizontalLayout();
                tempFilterLayout.setSpacing(true);
                VerticalLayout filterHeaderLayout = initFilterLabel(filterBtn.getFilterTitle());
                tempFilterLayout.addComponent(filterHeaderLayout);
                tempFilterLayout.addComponent(filterBtn);
                filtersLabelLayoutMap.put(filterBtn.getFilterTitle(), tempFilterLayout);
                if (filterBtn.getFilterId() == 1) {
                    fullleft.addComponent(tempFilterLayout, 0);
                } else {
                    arrangeFullFiltersLayout(tempFilterLayout);
                }
//                mainFilterLayout.addComponent(tempFilterLayout);

            }
            filtersBtnsMap.put(filterBtn.getCaption(), filterBtn);
        } else {

        }

    }

    public void removeFilter(String filterBtnCaption) {
        if (filtersBtnsMap.containsKey(filterBtnCaption)) {
            ClosableFilterLabel btn = filtersBtnsMap.get(filterBtnCaption);

            filtersLabelLayoutMap.get(btn.getFilterTitle()).removeComponent(btn);
            if (filtersLabelLayoutMap.get(btn.getFilterTitle()).getComponentCount() == 1) {
                fullleft.removeComponent(filtersLabelLayoutMap.get(btn.getFilterTitle()));
                fullright.removeComponent(filtersLabelLayoutMap.get(btn.getFilterTitle()));
                filtersLabelLayoutMap.remove(btn.getFilterTitle());
            }
            filtersBtnsMap.remove(filterBtnCaption);
        }

    }

    public void clearAllFilters(LayoutEvents.LayoutClickEvent event) {
        Map<String, ClosableFilterLabel> ufiltersBtnsMap = new HashMap<String, ClosableFilterLabel>();
        ufiltersBtnsMap.putAll(filtersBtnsMap);
        for (ClosableFilterLabel btn : ufiltersBtnsMap.values()) {
            btn.layoutClick(event);
        }
        ufiltersBtnsMap.clear();
    }

    public Set getLabels() {
        return filtersBtnsMap.keySet();
    }

    public void finalizeQuery() {
        query = new Query();
        query.setValidatedProteins(false);
        query.setSearchDataset("");
        query.setSearchKeyWords("temp key words until we see");
        searchKeyWords = "temp key words until we see";
        if (searchKeyWords != null && !searchKeyWords.equalsIgnoreCase("")) {
            query.setSearchKeyWords(searchKeyWords);
        }
        for (ClosableFilterLabel filter : filtersBtnsMap.values()) {
            switch (filter.getFilterId()) {
                case 1:
                    query.setSearchKeyWords(filter.getFilterValue());
                case 2:
                    query.setSearchDataType(filter.getFilterValue());
                    break;

                case 3:
                    query.setSearchDataset(filter.getFilterValue());
                    break;
                case 4:
                    query.setSearchBy(filter.getFilterValue());
                    break;
                case 5:
                    if (filter.getFilterValue().equalsIgnoreCase("Validated Proteins Only")) {
                        query.setValidatedProteins(true);
                    } else {
                        query.setValidatedProteins(false);
                    }

            }

        }

    }

    public HorizontalLayout getFullFilterLayout() {
//       return arrangeFullFiltersLayout();
        return mainFilterLayout;
    }

    public VerticalLayout getMinimumFilterLayout() {
        updateMinFilterL1bel();
        return minimumfilterLayout;

    }

    public void setSearchKeyWords(String searchKeyWords) {
        this.searchKeyWords = searchKeyWords;
    }

    public boolean isValidQuery() {
        finalizeQuery();
        if (query.getSearchDataType().equalsIgnoreCase("Identification Data") && (searchKeyWords == null || searchKeyWords.length() < 4)) {
            System.err.println(query.getSearchDataType().equalsIgnoreCase("Identification Data") + "&&" + (searchKeyWords == null || searchKeyWords.length() < 4));
            return false;
        }
//        filtersBtnsMap.clear();
//        searchKeyWords = null;
        return true;

    }

    private VerticalLayout initFilterLabel(String title) {

        Label filterLabel = new Label("&nbsp; &nbsp; &nbsp; &nbsp; " + title);
        filterLabel.setContentMode(ContentMode.HTML);
        filterLabel.setStyleName("showFilterLabelHeader");
        filterLabel.setDescription("Drop all " + title + " filter");

        VerticalLayout layout = new VerticalLayout();
//        layout.setId(title);
        layout.addComponentAsFirst(filterLabel);
        layout.addLayoutClickListener(listener);

        return layout;

    }

    public void resetQuntificationFilters() {
        Map<String, ClosableFilterLabel> ufiltersBtnsMap = new HashMap<String, ClosableFilterLabel>();
        ufiltersBtnsMap.putAll(filtersBtnsMap);
        for (ClosableFilterLabel btn : ufiltersBtnsMap.values()) {
            if (btn.getFilterId() > 5) {
                btn.layoutClick(LayoutEvents.LayoutClickEvent.createEvent(mainFilterLayout, null, btn));
            }
        }
        ufiltersBtnsMap.clear();

    }

    public void resetIdentificationFilters() {
        Map<String, ClosableFilterLabel> ufiltersBtnsMap = new HashMap<String, ClosableFilterLabel>();
        ufiltersBtnsMap.putAll(filtersBtnsMap);
        for (ClosableFilterLabel btn : ufiltersBtnsMap.values()) {
            if (btn.getFilterId() == 2 || btn.getFilterId() == 5) {
                btn.layoutClick(new LayoutEvents.LayoutClickEvent(mainFilterLayout, null, mainFilterLayout, mainFilterLayout));
            }
        }
        ufiltersBtnsMap.clear();

    }

    private void arrangeFullFiltersLayout(HorizontalLayout filter) {

        if (fullleft.getComponentCount() > fullright.getComponentCount()) {
            fullright.addComponent(filter);
        } else {
            fullleft.addComponent(filter);
        }

    }
    
    private void updateMinFilterL1bel(){
    minimumfilterLayout.removeAllComponents();
        minimumfilterLayout.setWidth("100%");
     HorizontalLayout hlo = new HorizontalLayout();
     minimumfilterLayout.addComponent(hlo);
    int index =0;
    for (ClosableFilterLabel btn:filtersBtnsMap.values() ) {
        
                Label l = new Label("[ "+btn.getFilterValue()+" ] ");
                l .setStyleName("filterNonClosableBtnLabel");
                if (index > 4) {
                    hlo = new HorizontalLayout();
                    minimumfilterLayout.addComponent(hlo);
                    index = 0;

                }

                index++;
                hlo.addComponent(l);
            }
           
    }
    
        
           
    
    
    

}
