/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

/**
 *
 * @author V-Windows
 */
public class M3UFileParser {
    
    public static native String[] parse(String source);
    private static ArrayList<M3UItem> items = new ArrayList<>();
    private GroupCategory map = new GroupCategory();
    
    public M3UFileParser(){
        //System.setProperty( "java.library.path", "C:/Users/V-Windows/Documents/NetBeansProjects/M3U-Editor-New/src/m3u/editor/pkgnew" );
        //System.loadLibrary("m3ulib");
        System.load("C:/Users/V-Windows/Documents/NetBeansProjects/M3U-Editor-New/src/main/java/com/m3u/m3ulib.dll");
        
        //C:\Users\V-Windows\Documents\NetBeansProjects\M3U-Editor-New\src\main\java\com\m3u
        
        //System.out.println(System.getProperty("java.library.path"));
    }
    
    public void start(String source) throws UnsupportedEncodingException{
        int itemCounter = 0;
        
        M3UItem item = new M3UItem();
        items.clear();
        
        
        
        System.out.println("m3u.editor.MainPage.main()");
        
        String[] content;
        
        if(source.compareTo("") != 0){
            content = M3UFileParser.parse(source);
        }else{
            content = M3UFileParser.parse("C:/Users/V-Windows/Documents/NetBeansProjects/M3U-Editor-New/src/com/m3u/dreck.txt");
        }
        
                                                                                            
        System.out.println("++++++++++++++++++++++++++++++++");
        //System.out.println(content.length);
        for(int i = 0; i < content.length; i++){
            
            //System.out.println(content.length);
            

            //String value = new String(content[i].getBytes("UTF-8"));
            System.out.println(content[i]);
            //System.out.println(value);
            //System.out.println("JO");

            switch(itemCounter){
                case 0:
                    item = new M3UItem();
                    item.setTime(content[i]);
                    break;
                case 1:
                    item.setId(content[i]);
                    break;
                case 2:
                    if(!content[i].equals("")){
                        item.setLogoUrl(content[i]);
                        item.setLogo(new CustomImage(content[i]));
                    }else{
                        item.setLogoUrl("https://marketplace.canva.com/MABzgDu4ChI/2/0/thumbnail_large/canva-travel-adventures-youtube-thumbnail-MABzgDu4ChI.jpg");
                        item.setLogo(new CustomImage("https://marketplace.canva.com/MABzgDu4ChI/2/0/thumbnail_large/canva-travel-adventures-youtube-thumbnail-MABzgDu4ChI.jpg"));
                    }
                    break;
                case 3:
                    item.setTitle(content[i]);
                    parseGroup(content[i]);
                    break;
                case 4:
                    //System.out.println(content[i]);
                    item.setName(content[i]);
                    break;
                case 5:
                    item.setUrl(content[i]);
                    //System.out.println(content[i]);
                    //System.out.println(item.toString());
                    items.add(item);
                    itemCounter = -1;
                    break;
                    
            }
            
          itemCounter++;
        }
    }
    
    public void parseGroup(String group){
        
        System.out.println("parseGroup");
        
        
        String[] groupItems;
        groupItems = group.split(",");
        
        if(groupItems.length > 1){
            System.out.println("PARSE ARRAY");
            System.out.println(groupItems.length);

            for(int i=0; i<groupItems.length; i++){
                System.out.println("EDDY:" + groupItems[i]);
                System.out.println("EDDY:" + map.getData().containsKey(groupItems[i]));
                if(!map.getData().containsKey(groupItems[i])){
                    System.out.println("ADDS GROUP:" + groupItems[i]);
                    map.setGroupCategory(groupItems[i]);
                }

            }
        }else{
            System.out.println("PARSE STRING");
            System.out.println(group);
            System.out.println(map.getData().containsKey(group));

            if(!map.getData().containsKey(group)){
                map.setGroupCategory(group);
            }  
        }
   
    }
    
    public ArrayList<M3UItem> getContent(){
        return items;
    }
    
    public GroupCategory getGroupContent(){
        return map;
    }
    
}
