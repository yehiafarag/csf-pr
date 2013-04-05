package com.model;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import com.model.beans.ExperimentBean;
import com.model.beans.FractionBean;
import com.model.beans.ProteinBean;

public class FractionRangeUtilitie implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ArrayList<String> updateFractionRange(ExperimentBean exp) {
		Map<Integer, FractionBean> fractionList = exp.getFractionsList();
		ArrayList<String> rangeSet = new ArrayList<String>();
		int x = 1;
		Map<Integer, FractionBean> updatedFractionList = new HashMap<Integer, FractionBean>();
			boolean tag = true;
			List<Double>RangeList = null;
			double maxRange = 0.0;
			double minRange=0.0;
			double step =0.0;
		for(int key:fractionList.keySet())
		{
			FractionBean fb = fractionList.get(key);
			if(tag){
				RangeList = getRange(fb.getProteinList(),exp.getProteinList());
				tag = false;
				minRange = RangeList.get(0);
				maxRange = RangeList.get(1);
				step = (maxRange - minRange)/Double.valueOf(exp.getFractionsNumber());			
				
			}			
			fb.setMinRange(minRange);
			fb.setMaxRange(minRange+step);
	        DecimalFormat df = new DecimalFormat("#.##");	        
			rangeSet.add(x++ +"\t"+df.format(fb.getMinRange())+"-"+df.format(fb.getMaxRange()));
			minRange = 	minRange+step;
			updatedFractionList.put(key, fb);
		}
		exp.setFractionsList(updatedFractionList);
		return rangeSet;
	}

	/**
	 * @param map2 
	 * @param map 
	 * @param args
	 */
	private List<Double>getRange(Map<String, ProteinBean> proFracMap, Map<String, ProteinBean> proMap)
	{
		double minRange=10000000000.0;
		double maxRange = 0.0;
		for(String key :proFracMap.keySet())
		{
			if(proMap.containsKey(key))
			{
				ProteinBean expPb   = proMap.get(key);
					if(expPb.getMw_kDa() >= maxRange)
					{
						maxRange = expPb.getMw_kDa();
					}
					else if(expPb.getMw_kDa() <= minRange)
					{
						minRange = expPb.getMw_kDa();
					}
				
				
			}
		}

		List<Double> rangeList = new ArrayList<Double>();
		rangeList.add(minRange);
		rangeList.add(maxRange);
		
		return rangeList;
		
	}
	

}
