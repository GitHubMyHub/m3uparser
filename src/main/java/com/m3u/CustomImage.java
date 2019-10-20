/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import javafx.scene.image.Image;

/**
 *
 * @author V-Windows
 */
public class CustomImage {
    
    //private ImageView image;
    private Image image;

    CustomImage(String url) {
        setImage(url);
    }

    public void setImage(String value) {
        this.image = new Image(value);
    }

    public Image getImage() {
        return image;
    }
    
}
