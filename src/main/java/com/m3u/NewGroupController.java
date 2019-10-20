/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author V-Windows
 */
public class NewGroupController implements Initializable {
    
    @FXML private VBox newGroupPane;
    @FXML private TextField txtGroupName;
    @FXML private Button btnAdd;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }
    
    public void setGroupName(String name){
        System.out.println("setGroupName");
        this.txtGroupName.setText(name);
        
        btnAdd.setText("Save");
        
    }
    
    public String getNewItem(){
        return this.txtGroupName.getText();
    }
    
    
    public void closeWindow(){
        Stage newGroupStage = (Stage) newGroupPane.getScene().getWindow();
        newGroupStage.hide();
    }
    
}
