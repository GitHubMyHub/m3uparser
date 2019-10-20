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
public class NewItemController implements Initializable{
    
    @FXML private VBox newItemPane;
    @FXML private TextField txtTime;
    @FXML private TextField txtId;
    //@FXML private TextField txtTitle;
    @FXML private TextField txtName;
    @FXML private TextField txtLogo;
    @FXML private TextField txtUrl;
    @FXML private Button btnAdd;
    
    //private MainController mainController;
    private boolean boolAdd = false;
    

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    

    
    public M3UItem getNewItem(){
        
        M3UItem m3UItem = new M3UItem();
        
        m3UItem.setTime(txtTime.getText());
        m3UItem.setId(txtId.getText());
        //m3UItem.setTitle(txtTitle.getText());
        m3UItem.setName(txtName.getText());
        //m3UItem.setLogo(txtLogo.getText());
        
        if(!txtLogo.getText().equals("")){
            m3UItem.setLogoUrl(txtLogo.getText());
            m3UItem.setLogo(new CustomImage(txtLogo.getText()));
        }else{
            m3UItem.setLogoUrl("https://marketplace.canva.com/MABzgDu4ChI/2/0/thumbnail_large/canva-travel-adventures-youtube-thumbnail-MABzgDu4ChI.jpg");
            m3UItem.setLogo(new CustomImage("https://marketplace.canva.com/MABzgDu4ChI/2/0/thumbnail_large/canva-travel-adventures-youtube-thumbnail-MABzgDu4ChI.jpg"));
        }
        
        m3UItem.setUrl(txtUrl.getText());
        
        return m3UItem;
        
    }
    
    public boolean getAddStatus(){
        return boolAdd;
    }
    
    public void closeWindow(){
        
        boolAdd = true;
        
        Stage newItemStage = (Stage) newItemPane.getScene().getWindow();
        newItemStage.hide();
    }
    
}
