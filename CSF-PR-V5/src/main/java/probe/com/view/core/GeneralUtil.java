/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.view.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import probe.com.model.beans.IdentificationDataset;

/**
 *
 * @author Yehia Farag
 */
public class GeneralUtil {

   
    

    public int getExpId(String expName, Map<Integer, IdentificationDataset> expList) {
        for (IdentificationDataset exp : expList.values()) {
            if (exp.getName().equalsIgnoreCase(expName)) {
                return exp.getDatasetId();
            }
        }
        return 0;
    }

    public List<String> getStrExpList(Map<Integer, IdentificationDataset> expList, String userEmail) {
        List<String> strExpList = new ArrayList<String>();
        for (IdentificationDataset exp : expList.values()) {
            if (userEmail.equalsIgnoreCase("csf-pr@googlegroups.com") || exp.getEmail().equalsIgnoreCase(userEmail)) {
                String str = exp.getDatasetId() + "	" + exp.getName() + "	( " + exp.getUploadedByName() + " )";
                strExpList.add(str);
            }
        }
        return strExpList;
    }


}
