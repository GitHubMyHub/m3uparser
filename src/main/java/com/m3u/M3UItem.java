/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import java.util.HashMap;
import java.util.stream.Collectors;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author V-Windows
 */
public class M3UItem {
    
    private final SimpleStringProperty time;
    private final SimpleStringProperty id;
    //private CustomImage logo;
    private ObjectProperty logo = new SimpleObjectProperty();
    private SimpleStringProperty logoUrl = new SimpleStringProperty();
    //private final SimpleStringProperty logo;
    //private final SimpleStringProperty title;
    private HashMap<String, String> title = new HashMap<String, String>();
    private final SimpleStringProperty name;
    private final SimpleStringProperty url;

    public M3UItem() {
        this.time = new SimpleStringProperty("");
        this.id = new SimpleStringProperty("");
        //this.logo = new CustomImage();
        //this.logo = new SimpleStringProperty("");
        //this.title = new SimpleStringProperty("");
        this.name = new SimpleStringProperty("");
        this.url = new SimpleStringProperty("");
    }
    
    public String getTime() {
        return time.get();
    }

    public void setTime(String time) {
        this.time.set(time);
    }

    public String getId() {
        return id.get();
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getLogoUrl() {   
        return logoUrl.get();
    }

    public void setLogoUrl(String logo) {
        this.logoUrl.set(logo);
    }
    
    //For Picture
    public void setLogo(CustomImage alb){
        logo.set(alb); 
    }
    
    public Object getLogo(){
        return logo.get();
    }
    
    public ObjectProperty logoProperty(){
        return logo;
    }

    public String getTitle() {
        //return title.get();
        return this.title.values().stream().map(Object::toString).collect(Collectors.joining(","));
    }
    
    public boolean getTitleItem(String title){
        System.out.println("FINDEN:" + title);
        System.out.println("GEFUNDEN:" + this.title.get(title));
        System.out.println("DRIN:" + getTitle());
        System.out.println("---------------------");
        for (String name: this.title.keySet()){

            String key =name.toString();
            String value = this.title.get(name).toString();  
            System.out.println(key + "=>" + value);  
        } 
        System.out.println("---------------------");
        if(!this.title.containsKey(title)){
            System.out.println("NO");
            return false;
        }else{
            System.out.println("YES");
            return true;
        }
    }
    
    public void updateTitle(String oldTitle, String newTitle){
        System.out.println("updateTitle");
        this.title.remove(oldTitle);
        setTitle(newTitle);
        //this.title.put(oldTitle, newTitle);
    }

    public void setTitle(String title) {
        //this.title.set(title);
        this.title.put(title, title);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getUrl() {
        return url.get();
    }

    public void setUrl(String url) {
        this.url.set(url);
    }
    
    @Override
    public String toString() {
        
        //return this.time.get() + "|" + this.id.get() + "|" + this.logo.get() + "|" + this.title.get() + "|" + this.name.get() + "|" + this.url.get();
        //return this.time.get() + "|" + this.id.get() + "|" + this.logo.get() + "|" + "|" + this.name.get() + "|" + this.url.get();
        return this.time.get() + "|" + this.id.get() + "|" + "|" + "|" + this.name.get() + "|" + this.url.get();
    }
    
}
