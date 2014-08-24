/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.subview.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import probe.com.model.beans.DatasetBean;
import probe.com.model.beans.PeptideBean;

/**
 *
 * @author Yehia Farag
 */
public class GeneralUtil {

    private TreeSet<String> alphabetSet;

    public GeneralUtil(String chart) {
        alphabetSet = new TreeSet<String>();
        char[] arr = new char[]{'A', 'B', 'C', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        for (int index = 0; index < arr.length; index++) {
            alphabetSet.add(("" + arr[index]).toLowerCase());
        }
    }

    public GeneralUtil() {
    }

    public int getValidatedPepNumber(Map<Integer, PeptideBean> pepProtList) {
        int count = 0;
        for (PeptideBean pb : pepProtList.values()) {
            if (pb.getValidated() == 1.0) {
                count++;
            }
        }
        return count;
    }

    public int getKey(Map<Integer, String> expListStr, String str,Map<Integer,Integer>datasetIndex) {
        for (int key1 : expListStr.keySet()) {
            if (str.equalsIgnoreCase(expListStr.get(key1))) {
                for(int k:datasetIndex.keySet())
                {
                    int value = datasetIndex.get(k);
                    if(value == key1){ 
                        System.out.println(" key before indexing  "+key1+"  ---  return value --- "+k);
                        return k;
                    }
                }
                return datasetIndex.get(key1);
            }
        }
        return 0;
    }

    public int getExpId(String expName, Map<Integer, DatasetBean> expList) {
        for (DatasetBean exp : expList.values()) {
            if (exp.getName().equalsIgnoreCase(expName)) {
                return exp.getExpId();
            }
        }
        return 0;
    }

    public List<String> getStrExpList(Map<Integer, DatasetBean> expList, String userEmail) {
        List<String> strExpList = new ArrayList<String>();
        for (DatasetBean exp : expList.values()) {
            if (userEmail.equalsIgnoreCase("admin@csf.no") || exp.getEmail().equalsIgnoreCase(userEmail)) {
                String str = exp.getExpId() + "	" + exp.getName() + "	( " + exp.getUploadedByName() + " )";
                strExpList.add(str);
            }
        }
        return strExpList;
    }

    public Set getAlphabet() {
        TreeSet<String> alphabet = new TreeSet<String>();
        char[] arr = new char[]{'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'};
        for (int index = 0; index < arr.length; index++) {
            alphabet.add(("" + arr[index]).toUpperCase());
        }

        return alphabet;


    }

    public TreeSet<String> getAlphabetSet() {
        return alphabetSet;
    }

    public void setAlphabetSet(TreeSet<String> alphabetSet) {
        this.alphabetSet = alphabetSet;
    }

    public TreeMap<Integer, Integer> getSearchIndexesSet(Map<String, Integer> searchMap, Map<String, Integer> searchMapIndex, String keySearch) {
        TreeMap<Integer, Integer> treeSet = new TreeMap<Integer, Integer>();
        for (String key : searchMap.keySet()) {
            if (key.contains(keySearch)) {
                treeSet.put(searchMapIndex.get(key), searchMap.get(key));
            }
        }

        return treeSet;
    }
}
