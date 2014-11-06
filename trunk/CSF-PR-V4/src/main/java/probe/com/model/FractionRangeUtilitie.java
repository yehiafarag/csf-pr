package probe.com.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.vaadin.ui.Table;
import probe.com.model.beans.ProteinBean;

public class FractionRangeUtilitie implements Serializable {

    /**
     *
     *
     */
    private static final long serialVersionUID = 1L;
    private DecimalFormat df = null;

//    public ArrayList<String> updateFractionRange(DatasetBean exp,Map<Integer, FractionBean> fractionList,Map<String, ProteinBean> proteinList) {
////        Map<Integer, FractionBean> fractionList = exp.getFractionsList();
//        ArrayList<String> rangeSet = new ArrayList<String>();
//        int x = 1;
//        Map<Integer, FractionBean> updatedFractionList = new HashMap<Integer, FractionBean>();
//        boolean tag = true;
//        List<Double> RangeList = null;
//        double maxRange = 0.0;
//        double minRange = 0.0;
//        double step = 0.0;
//        for (int key : fractionList.keySet()) {
//            FractionBean fb = fractionList.get(key);
//            if (tag) {
//                RangeList = getRange(fb.getProteinList(), proteinList);
//                tag = false;
//                minRange = RangeList.get(0);
//                maxRange = RangeList.get(1);
//                step = (maxRange - minRange) / (double) exp.getFractionsNumber();
//
//            }
////            fb.setMinRange(minRange);
////            fb.setMaxRange(minRange + step);
//            DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
//            otherSymbols.setGroupingSeparator('.');
//            df = new DecimalFormat("#.##", otherSymbols);
//
//            rangeSet.add(x++ + "\t" + df.format(fb.getMinRange()) + "-" + df.format(fb.getMaxRange()));
//            minRange = minRange + step;
//            updatedFractionList.put(key, fb);
//        }
////        exp.setFractionsList(updatedFractionList);
//        return rangeSet;
//    }

    private List<Double> getRange(Map<String, ProteinBean> proFracMap, Map<String, ProteinBean> proMap) {
        double minRange = 10000000000.0;
        double maxRange = 0.0;
        for (String key : proFracMap.keySet()) {
            if (proMap.containsKey(key)) {
                ProteinBean expPb = proMap.get(key);
                if (expPb.getMw_kDa() >= maxRange) {
                    maxRange = expPb.getMw_kDa();
                } else if (expPb.getMw_kDa() <= minRange) {
                    minRange = expPb.getMw_kDa();
                }


            }
        }

        List<Double> rangeList = new ArrayList<Double>();
        rangeList.add(minRange);
        rangeList.add(maxRange);

        return rangeList;

    }
    /*
     public ArrayList<String> getFractionRange( DatasetBean exp) {
		
     Map<Integer, FractionBean> fractionList = exp.getFractionsList();
     Map<Double,Double> rangeMap = new TreeMap<Double, Double>();
     for(FractionBean fb:fractionList.values()){
     rangeMap.put(fb.getMinRange(), fb.getMaxRange());
     }
     ArrayList<String> rangeSet = new ArrayList<String>();
     String ko= new String("");
     DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
     otherSymbols.setGroupingSeparator('.'); 
     df=  new DecimalFormat("#.##",otherSymbols);
     int x = rangeMap.size()-1;
     int y = 0;
     for(double min:rangeMap.keySet())
     {
     if(x == y)
     rangeSet.add(""+(y+1)+"\t"+df.format(min)+"- > "+df.format(min)+" kDa");
			
     else
     rangeSet.add(""+(y+1)+"\t"+df.format(min)+"-"+df.format(rangeMap.get(min))+" kDa");
     y++;
			
     }
				
     if(rangeSet.contains(ko))
     rangeSet.remove(ko);
		
     return rangeSet;
     }
     */

//    public ArrayList<String> getFractionRange(DatasetBean exp,Map<Integer, FractionBean> fractionList) {
//
////        Map<Integer, FractionBean> fractionList = exp.getFractionsList();
//
//
//
//        DecimalFormatSymbols otherSymbols = new DecimalFormatSymbols(Locale.ENGLISH);
//        otherSymbols.setGroupingSeparator('.');
//        df = new DecimalFormat("#.##", otherSymbols);
//        //	Map<Double,FractionBean> rangeMap = new TreeMap<Double, FractionBean>();
//        ArrayList<String> rangeSet = new ArrayList<String>();
//        //	double maxValue =-1;
//        //get the greatest max range
//        //	for(FractionBean fb:fractionList.values()){
//        //		if(maxValue< fb.getMaxRange())
//        //				maxValue = fb.getMaxRange();
//        //rangeMap.put(fb.getMaxRange(), fb);
//        //	}
//        String str = "";
//        for (FractionBean fb : fractionList.values()) {
//            //	if(fb.getMaxRange() == maxValue)
//            //		str = fb.getFractionIndex()+"\t"+df.format(fb.getMinRange())+"->"+df.format(fb.getMinRange());
//            //	else
////            str = fb.getFractionIndex() + "\t" + df.format(fb.getMinRange()) + "\t" + df.format(fb.getMaxRange());
//
//            rangeSet.add(str);
//        }
//        String ko = new String("");
//
//        if (rangeSet.contains(ko)) {
//            rangeSet.remove(ko);
//        }
//
//        return rangeSet;
//    }

    public Table getRangeTable(ArrayList<String> ranges, double mw2) {

        Table table = new Table();
//	table.setStyle(Reindeer.TABLE_STRONG+" "+Reindeer.TABLE_BORDERLESS);
        //table.setStyleName(".v-table-header{ text-transform: none !important }");
        table.setHeight("100%");
        table.setWidth("100%");
        table.setColumnReorderingAllowed(true);
        table.setColumnCollapsingAllowed(true);
        table.setImmediate(true); // react at once when something is selected

        table.addContainerProperty("Fraction Index", String.class, null, "Fraction Index", null, com.vaadin.ui.Table.ALIGN_CENTER);
        table.addContainerProperty("Start (kDa)", Integer.class, null, " Start (kDa)", null, com.vaadin.ui.Table.ALIGN_CENTER);
        table.addContainerProperty("Stop (kDa)", String.class, null, "Stop (kDa)", null, com.vaadin.ui.Table.ALIGN_CENTER);
        table.addContainerProperty("Theoretical MW (kDa)", Double.class, null, "Theoretical MW (kDa)", null, com.vaadin.ui.Table.ALIGN_CENTER);
        /* Add a few items in the table. */
        table.setSortDisabled(true);
        int minRange = 0;
        int maxRange = 0;
        int fractIndex = 0;
        for (String str : ranges) {

            fractIndex = Integer.valueOf((str.split("\t")[0]));
            minRange = Integer.valueOf((str.split("\t")[1]));

            try {
                maxRange = Integer.valueOf(str.split("\t")[2]);

            } catch (java.lang.NumberFormatException e) {
                if ((str.split("\t")[1].split("-")[1]).contains(">")) {
                    maxRange = Integer.valueOf(0);
                    if (mw2 >= minRange) {
                        table.addItem(new Object[]{(fractIndex) + "(*)", minRange, "Greater Than " + minRange, mw2}, fractIndex);
                        table.select(fractIndex);
                        table.setCurrentPageFirstItemIndex(fractIndex);
                        continue;
                    } else {
                        table.addItem(new Object[]{"" + (fractIndex), minRange, "Greater Than " + minRange, null}, fractIndex);
                        continue;

                    }
                }
            }
            if (mw2 >= minRange && mw2 < maxRange) {
                //index = new Integer(x+1);
                table.addItem(new Object[]{"[ " + (fractIndex) + " ]", minRange, String.valueOf(maxRange), mw2}, fractIndex);
                table.select(fractIndex);
                table.setCurrentPageFirstItemIndex(fractIndex);
                continue;

            }
            table.addItem(new Object[]{"" + (fractIndex), minRange, String.valueOf(maxRange), null}, new Integer(fractIndex));
        }
        for (Object propertyId : table.getSortableContainerPropertyIds()) {
            table.setColumnExpandRatio(propertyId.toString(), 2.0f);
        }

        return table;

    }

    /*
     public Table getRangeTable(ArrayList<String> ranges, double mw2)
     {
		
     Integer index = null;
     Table table= new Table();
     //	table.setStyle(Reindeer.TABLE_STRONG+" "+Reindeer.TABLE_BORDERLESS);
     //table.setStyleName(".v-table-header{ text-transform: none !important }");
     table.setHeight("100%");
     table.setWidth("100%");
     table.setColumnReorderingAllowed(true);
     table.setColumnCollapsingAllowed(true);
     table.setImmediate(true); // react at once when something is selected
	   
     table.addContainerProperty("Fraction Index",String.class, null,"Fraction Index",null,com.vaadin.ui.Table.ALIGN_CENTER);
     table.addContainerProperty("Start (kDa)",Integer.class, null," Start (kDa)",null,com.vaadin.ui.Table.ALIGN_CENTER);
     table.addContainerProperty("Stop (kDa)",String.class, null,"Stop (kDa)",null,com.vaadin.ui.Table.ALIGN_CENTER);
     table.addContainerProperty("Theoretical MW (kDa)",Double.class, null,"Theoretical MW (kDa)",null,com.vaadin.ui.Table.ALIGN_CENTER); 
     /* Add a few items in the table. *	
     table.setSortDisabled(true);
     int minRange = 0;
     int maxRange  = 0;
     int x = 0;
     for(String str :ranges){
     minRange = Integer.valueOf((str.split("\t")[1]).split("-")[0]);
					
     try{
     maxRange = Integer.valueOf(str.split("\t")[1].split("-")[1].split(" ")[0]);
					
     }catch(java.lang.NumberFormatException e){
     if((str.split("\t")[1].split("-")[1]).contains(">"))
     {
     maxRange = Integer.valueOf(0);
     if(mw2>=minRange){
     index = new Integer(x+1);
     table.addItem(new Object[] {(x+1)+"(*)",minRange,"Greater Than "+minRange,mw2}, index);	
     table.select(index);
     table.setCurrentPageFirstItemIndex(index);
     x++;
     continue;
     }
     else
     {
     index = new Integer(x+1);
     table.addItem(new Object[] {""+(x+1),minRange,"Greater Than "+minRange,null}, index);	
     x++;
     continue;
								 
     }
     }
     }
     if(mw2>=minRange && mw2<maxRange)
     {
     index = new Integer(x+1);
     table.addItem(new Object[] {"[ "+(x+1)+" ]",minRange,String.valueOf(maxRange),mw2},index );	
     table.select(index);
     table.setCurrentPageFirstItemIndex(index);
     x++;
     continue;
						 
     }
     table.addItem(new Object[] {""+(x+1),minRange,String.valueOf(maxRange),null}, new Integer(x+1));	
     x++;			
     }
     for(Object propertyId:table.getSortableContainerPropertyIds())
     table.setColumnExpandRatio(propertyId.toString(), 2.0f);
		 
     return table;
	
     }*/
    public Map<String, String> getRangeMap(ArrayList<String> rangList) {
        Map<String, String> rangesMap = new HashMap<String, String>();
        for (String str : rangList) {
            String[] strArr = str.split("\t");
            rangesMap.put(strArr[0], strArr[1] + "-" + strArr[2]);
        }
        return rangesMap;
    }
}
