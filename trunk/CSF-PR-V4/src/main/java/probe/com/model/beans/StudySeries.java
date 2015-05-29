/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package probe.com.model.beans;

import java.io.Serializable;

/**
 *
 * @author Yehia Farag
 */
public class StudySeries implements Serializable{
   Object[] quantSeriousMap ;
   Object[] idSeriousMap ;
   String id;

    public Object[] getQuantSeriousMap() {
        return quantSeriousMap;
    }

    public void setQuantSeriousMap(Object[] quantSeriousMap) {
        this.quantSeriousMap = quantSeriousMap;
    }

    public Object[] getIdSeriousMap() {
        return idSeriousMap;
    }

    public void setIdSeriousMap(Object[] idSeriousMap) {
        this.idSeriousMap = idSeriousMap;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
}
