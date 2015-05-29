/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.dal;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author y-mok_000
 */
public class QueryConstractorHandler {
//    private List<Integer> indexList = new ArrayList<Integer>();
    private final List<String> typeList = new ArrayList<String>();
    private final List<String> valueList = new ArrayList<String>();
    
    
    public void addQueryParam(String type, String value){
    typeList.add(type);
    valueList.add(value);
  
    
    }
    
    public PreparedStatement  initStatment(PreparedStatement selectStat ){
       try{
        
        for(int x =0;x<typeList.size();x++){
            String type = typeList.get(x);
            if(type.equalsIgnoreCase("String")){
            selectStat.setString(x+1,valueList.get(x) );
            
            }else if(type.equalsIgnoreCase("Integer")){
            selectStat.setInt(x, Integer.valueOf(valueList.get(x)));           
            
            }else if(type.equalsIgnoreCase("double")){
             selectStat.setDouble(x, Double.valueOf(valueList.get(x)));   
            
            }
        
        
        
        }
       }catch(SQLException sqlex){sqlex.printStackTrace();}
        
    return selectStat;
    }
    
}
