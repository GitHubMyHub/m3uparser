/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import java.util.Map;
import java.util.TreeMap;
import javafx.scene.control.TreeItem;

/**
 *
 * @author V-Windows
 */
public class GroupCategory {
    
    private String groupName;
    private Map<String, String> data = new TreeMap<String, String>();
    
    public GroupCategory(){
        
    }
    
    public void setGroupCategory(String groupName){
        this.data.put(groupName, groupName);
    }
    
    public Map<String, String> getData(){
        return this.data;
    }
    
    public TreeItem<String> getTreeView(TreeItem<String> root){
        
        this.data.forEach((key, value) -> {
            System.out.println(value);
            
            TreeItem<String> item = new TreeItem<String>(value);
            
            //item.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
                
                //System.out.println("GOIIIIIIPPPPPPP");
               //event.
               //this.getTreeView(root).
                  
             
                       //this.getMyPrimaryStage().getScene()
            //});
            
            /*item.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                @Override
                public void handle(MouseEvent t) {
                    System.out.println("KOOOOOOOOOOOOOOOOOOO");
                    if(t.getButton() == MouseButton.SECONDARY){
                        System.out.println("KOOOOOOOOOOOOOOOOOOO");
                    }
                }
                

            });*/
            
            root.getChildren().add(item);
        });

        return root;
    }
    
}
