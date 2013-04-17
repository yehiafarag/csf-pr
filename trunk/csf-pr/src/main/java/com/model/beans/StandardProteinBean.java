package com.model.beans;

import java.io.Serializable;

public class StandardProteinBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private double MW_kDa;
    private int lowerFraction;
    private int upperFraction;
    private String name;
    private String color;

    public int getLowerFraction() {
        return lowerFraction;
    }

    public void setLowerFraction(int lowerFraction) {
        this.lowerFraction = lowerFraction;
    }

    public double getMW_kDa() {
        return MW_kDa;
    }

    public void setMW_kDa(double mW_kDa) {
        MW_kDa = mW_kDa;
    }

    public int getUpperFraction() {
        return upperFraction;
    }

    public void setUpperFraction(int upperFraction) {
        this.upperFraction = upperFraction;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    @Override
    public String toString() {
        return this.name + " " + this.color;
    }
}
