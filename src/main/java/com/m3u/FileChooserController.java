/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import java.awt.Desktop;
import java.io.File;
import java.util.ArrayList;

import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author V-Windows
 */
public class FileChooserController {
    private Desktop desktop = Desktop.getDesktop();
    public ArrayList<M3UItem> items = new ArrayList<>();
    public GroupCategory groups;
    private File savePath;
 

    public FileChooserController(final Stage stage, boolean saveDialog) {
        stage.setTitle("Select M3U File");

        
        final FileChooser fileChooser = new FileChooser();
        File file;
        configureFileChooser(fileChooser);
        
            if(saveDialog == false){
                file = fileChooser.showOpenDialog(stage);
            }else{
                file = fileChooser.showSaveDialog(stage);
            }
            
            
            if (file != null) {
                if(saveDialog == false){
                    openFile(file);
                }else{
                    this.savePath = file.getAbsoluteFile();
                }
                
            }
            
    }
 
    private static void configureFileChooser(
        final FileChooser fileChooser) {      
            fileChooser.setTitle("Select M3U File");
            fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
            );
            fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("TXT", "*.txt"),
                new FileChooser.ExtensionFilter("M3U", "*.m3u"),
                new FileChooser.ExtensionFilter("M3U8", "*.m3u8")
                
            );
    }
 
    private void openFile(File file) {
        try {
            //desktop.open(file);
            //System.out.println(file.getAbsoluteFile());
            M3UFileParser parsed = new M3UFileParser();
            //System.out.println(file.getAbsoluteFile().toString());
            parsed.start(file.getAbsoluteFile().toString());
            
            //this.items.clear();
            this.items = parsed.getContent();
            this.groups = parsed.getGroupContent();
            
        } catch (Exception ex) {
            Logger.getLogger(FileChooser.class.getName()).log(
                Level.SEVERE, null, ex
            );
        }
    }
    
    public File getSavePath(){
        return this.savePath;
    }
    
    
    
}
