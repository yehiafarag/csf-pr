/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pepshack.util;

import com.pepshack.util.beans.PeptideBean;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Yehia Mokhtar
 */
public class FilesReader {

    public Map<String, PeptideBean> readGlycoFile(File file) {
        Map<String, PeptideBean> peptideList = new HashMap<String, PeptideBean>();
        boolean test = false;
        String[] strArr = null;
        BufferedReader bufRdr = null;
        String line = null;
        int row = 0;
        try {
            FileReader fr = new FileReader(file);
            bufRdr = new BufferedReader(fr);
            line = bufRdr.readLine();
            strArr = line.split("\t");
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage());
        }//e.printStackTrace();}
        if (strArr.length == 26 && strArr[0].trim().equalsIgnoreCase("Protein") && strArr[8].trim().equalsIgnoreCase("Sequence") && strArr[11].trim().equalsIgnoreCase("Enzymatic")) {
            test = true;
        }
        if (test) {
            PeptideBean pb = null;
            try {
                while ((line = bufRdr.readLine()) != null && row < 1000)//loop to fill the protein beans and add it to fraction list
                {
                    strArr = line.split("\t");
                    pb = new PeptideBean();
                    if (strArr.length == 24) {
                    } else {
                        if (strArr[24] != null && !strArr[24].equals("")) {
                            pb.setDeamidationAndGlycopattern(Boolean.valueOf(strArr[24]));
                        } else {
                            pb.setDeamidationAndGlycopattern(false);
                        }
                        if (strArr[25] != null && !strArr[25].equals("")) {
                            pb.setGlycopatternPositions((strArr[25]));
                        } else {
                            pb.setGlycopatternPositions("");
                        }
                    }
                    String key = "[" + strArr[0].trim() + "][" + strArr[9].trim() + "]";
                    peptideList.put(key, pb);
                }
            } catch (Exception e) {
                System.out.println(e.getLocalizedMessage());
            }
        }
        return peptideList;
    }
}
