/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author V-Windows
 */
public class M3UEditor extends Application {
    
    public static native String[] parse(String source);
    
    @Override
    public void start(Stage primaryStage) throws Exception {
        //Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
        
        FXMLLoader root = new FXMLLoader(M3UEditor.class.getResource("primary.fxml"));
        
        //FXMLLoader root = new FXMLLoader(M3UEditor.class.getResource("m3u_main_template.fxml"));
        
        primaryStage.setTitle("M3U-Editor");
        primaryStage.setScene(new Scene(root.load()));
        primaryStage.show();
        
    }

    @Override
    public void stop() throws Exception {
        super.stop(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void init() throws Exception {
        super.init(); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
