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
 * 
 */
public class QuantDSIndexes implements Serializable{

    private int [] indexes;
    private int value;

    public int[] getIndexes() {
        return indexes;
    }

    public void setIndexes(int[] indexes) {
        this.indexes = indexes;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
