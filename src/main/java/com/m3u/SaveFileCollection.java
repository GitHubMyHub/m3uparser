/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import javafx.collections.ObservableList;

/**
 *
 * @author V-Windows
 */
public class SaveFileCollection {
    
    ObservableList<M3UItem> list;
    
    public SaveFileCollection(ObservableList<M3UItem> list){
        this.list = list;
    }
    
    @Override
    public String toString(){
        
        String line = "";
        
        for(int i=0; i<list.size(); i++){
            line = list.get(i).getTime() + "," + list.get(i).getName() + String.format("%n");
            line += list.get(i).getUrl();
        }
        
        return line;
    }
    
    public String toM3UString(){
        String line = "#EXTM3U" + String.format("%n");
        
        for(int i=0; i<list.size(); i++){
            line += "#EXTINF:" + list.get(i).getTime() + " tvg-logo=" + "\"" + list.get(i).getLogo() + "\"" + " group-title=" + "\"" + list.get(i).getTitle() + "\"" + "," + list.get(i).getName() + String.format("%n");
            //line = "#EXTINF:" + list.get(i).getTime() + " tv-logo=" + list.get(i).getName() + String.format("%n");
            line += list.get(i).getUrl() + String.format("%n");
        }
        
        return line; 
    }
    
}
