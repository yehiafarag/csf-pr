///*
// * To change this license header, choose License Headers in Project Properties.
// * To change this template file, choose Tools | Templates
// * and open the template in the editor.
// */
//package probe.com.view.core;
//
//import com.vaadin.ui.VerticalLayout;
//import java.util.HashSet;
//import java.util.Set;
//import org.vaadin.marcus.MouseEvents;
//import probe.com.model.beans.QuantDatasetObject;
//import probe.com.model.beans.FilterRoot;
//import probe.com.model.util.SparkLineGen;
//import probe.com.bin.DatasetsExplorerTreeLayout;
//
///**
// *
// * @author yehia Farag
// */
//public class FilterRow {
//    
//    private final Set<Integer> datasetsIds = new HashSet<Integer>();
//
//    public void layoutSelected(int datasetIndex) {
//        for (ListGridCell l : columns) {
//         
//            l.setStyleName("selectedpublicationoverviewlabel");
//            l.setSelected(true);
//             parent.updateStudyInformationLayout(filterRoot,true,datasetIndex);
//        }
//
//    }
//
//    public void layoutUnSelected() {
//        for (ListGridCell l : columns) {
//            l.setStyleName("publicationoverviewlabel");
//            l.setSelected(false);
//        }
//    }
//
//    private final ListGridCell[] columns;
//
//
//
//
//    public ListGridCell[] getColumns() {
//        return columns;
//    }
//
//    public String getKey() {
//        return key;
//    }
//
//    private final FilterRoot filterRoot;
//    private final DatasetsExplorerTreeLayout parent;
//    private final  VerticalLayout sparkline;
//    private final String key;
//
//    public FilterRow(final FilterRoot filterRoot, int colNumber, boolean[] masterColumn, final DatasetsExplorerTreeLayout parent, SparkLineGen sparkLineGen) {
//
//        this.parent=parent;
//        this.key = filterRoot.getKey()+"";
//        columns = new ListGridCell[colNumber];
//        this.filterRoot = filterRoot;
//        
//        sparkline = sparkLineGen.getSparkLine(filterRoot.getDatasetsList().size());
//        
//        mouseOverListener = new MouseEvents.MouseOverListener() {
//
//            @Override
//            public void mouseOver() {
//                parent.updateStudyInformationLayout(filterRoot, false,-1);
//            }
//        };
//
//        mouseOutListener = new MouseEvents.MouseOutListener() {
//
//            @Override
//            public void mouseOut() {
//                 parent.updateStudyInformationLayout(null,false,-1);
//            }
//        };
//        ListGridCell label = new ListGridCell("<b><span style='font-size: 13px;'>" + "&nbsp;&nbsp;" + "</span></b> ",filterRoot.getKey()+"", mouseOverListener, mouseOutListener, true);
//        columns[0] = (label);
//         ListGridCell sparkLine = new ListGridCell(sparkline, filterRoot.getKey()+"", mouseOverListener, mouseOutListener);
//        columns[1] = (sparkLine);//style='font-size: 13px;'
//        ListGridCell studiesNumLabel = new ListGridCell("<b><span >#Studies:" + filterRoot.getDatasetsList().size() + "&nbsp;&nbsp;</span></b>", filterRoot.getKey()+"", mouseOverListener, mouseOutListener, false);
//        columns[2] = (studiesNumLabel);
//        
//        int counter = 3;
//        for (int z = 0; z < masterColumn.length; z++) {           
//            boolean check = masterColumn[z];           
//            if (check) {
//                Object prop = filterRoot.getProperty(z);      
//                ListGridCell l = new ListGridCell("<i><span>&nbsp;&nbsp;" + prop + "&nbsp;&nbsp;</span></i> ", filterRoot.getKey()+"", mouseOverListener, mouseOutListener, false);
//                columns[counter] = (l);
//                counter++;
//
//            } 
//
//        }
//        
//       
//        
//        for(QuantDatasetObject ds: filterRoot.getDatasetsList()){
//            datasetsIds.add(ds.getUniqId());
//        
//        }
//        
//       
//
//    }
//    
//    public boolean includeDataset(int dsIndex){
//        return datasetsIds.contains(dsIndex);
//    
//    }
//
//
//   
//    private final MouseEvents.MouseOverListener mouseOverListener;
//
//    private final MouseEvents.MouseOutListener mouseOutListener;
//
//  
//    
//    
//}
//
