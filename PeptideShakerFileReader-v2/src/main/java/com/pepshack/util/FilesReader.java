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
        if (strArr.length == 27/*26*/ && strArr[0].trim().equalsIgnoreCase("Protein") && strArr[8].trim().equalsIgnoreCase("Sequence") && strArr[11].trim().equalsIgnoreCase("Enzymatic")) {
            test = true;
        }
        if (test) {
//            int num24 = 0;
//            int num25 = 0;
//            int num26=0;
//            int num27=0;
//            int less24=0;
//            int gret27 = 0;
            PeptideBean pb = null;
            try {
                while ((line = bufRdr.readLine()) != null && row < 1000) {
                    
                    pb = new PeptideBean();
                    strArr = line.split("\t");
                      if (strArr.length > 24 && strArr[24] != null && !strArr[24].equals("")) {
                            pb.setDeamidationAndGlycopattern(Boolean.valueOf(strArr[24]));
                       } else {
                            pb.setDeamidationAndGlycopattern(false);
                        }
                       if (strArr.length > 25 && strArr[25] != null && !strArr[25].equals("")) {
                            pb.setGlycopatternPositions((strArr[25]));
                        } else {
                            pb.setGlycopatternPositions("");
                    }
                    if (strArr.length > 26 && strArr[26] != null && !strArr[26].equals("")) {
                        pb.setLikelyNotGlycosite(Boolean.valueOf(strArr[26]));
                    } else {
                        pb.setLikelyNotGlycosite(false);
                    }

                    
//                    if (strArr.length == 24) {
//                        if(num24 == 0)
//                            System.out.println("24 line is "+line);
//                        num24++;
//                    }
//                    if (strArr.length == 25) {
//                        num25++;
//                    }
//                    if (strArr.length == 26) {
//                        if(num26 == 0)
//                            System.out.println("26 line is "+line);
//                       num26++;
//                    }
//                    if (strArr.length == 27) {
//                        if(num27 == 0)
//                            System.out.println("27 line is "+line);
//                        num27++;
//                    }
//                     if (strArr.length > 27) {
//                        gret27++;
//                    }
                    
                    
//                    else {
//                         System.out.println("STRING LINE IS 24  is "+strArr.length);
//                      
//                        if (strArr[25] != null && !strArr[25].equals("")) {
//                            pb.setGlycopatternPositions((strArr[25]));
//                          //   System.out.println("position  "+pb.getGlycopatternPositions());
//                        
//                            
//                        } else {
//                            pb.setGlycopatternPositions("");
//                            
//                            // System.out.println("position  empty .. "+pb.getGlycopatternPositions());
//                        }
//                    }
                    String key = "[" + strArr[0].trim() + "][" + strArr[9].trim() + "]";
                    peptideList.put(key, pb);
                }
                
                
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println(e.getLocalizedMessage());
            }
//        System.out.println("num24 = "+num24+"  num25 = "+num25+" num26 = "+num26+" num27 = "+num27+" less24 = "+less24+" gret27 ="+gret27);
        }
        else 
            System.out.println("error in reading file");
        
        return peptideList;
    }
}
