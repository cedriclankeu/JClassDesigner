/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jClassDesigner.data;

import java.util.ArrayList;

/**
 *
 * @author majiasheng
 */
public class SplittableConnector_DataWrapper {
    private String type="";
    private ArrayList<Double> anchorXes;
    private ArrayList<Double> anchorYs;
    
    public SplittableConnector_DataWrapper() {
        anchorXes = new ArrayList<>();
        anchorYs = new ArrayList<>();
    }
    
    public void setType(String type) {
        this.type = type;
    }
    public void addAnchor(double x, double y) {
        anchorXes.add(x);
        anchorYs.add(y);
    }
    
    public String getType() {
        return type;
    }
    public ArrayList<Double> getAnchorXes() {
        return anchorXes;
    }
    public ArrayList<Double> getAnchorYs() {
        return anchorYs;
    }
    
}
