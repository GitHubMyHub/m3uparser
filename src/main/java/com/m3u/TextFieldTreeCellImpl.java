/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import javafx.collections.ObservableList;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.TransferMode;

/**
 *
 * @author V-Windows
 */
    public class TextFieldTreeCellImpl extends TreeCell<String> {

        private TextField textField;
        private TableView tblContent;
        private ContextMenu addMenu = new ContextMenu();

        public TextFieldTreeCellImpl(TableView tblContent) {
            
            this.tblContent = tblContent;
            
            MenuItem addMenuItem = new MenuItem("Add Employee");
            
            addMenuItem.setOnAction(event -> {
                TreeItem<String> newEmployee = new TreeItem<>("New Employee");
                getTreeItem().getChildren().add(newEmployee);
            });
            addMenu.getItems().add(addMenuItem);
        setOnDragEntered(e -> {
            System.out.println(" Entered ");
            e.consume();
        });
        setOnDragDetected(e -> {
            System.out.println(" Detected ");
            startDragAndDrop(TransferMode.MOVE);
            e.consume();
        });
        setOnDragDone(e -> {
            System.out.println(" Done ");
            e.consume();
        });
        setOnDragDropped(e -> {
            System.out.println(" Dropped ");
            //System.out.println(e.getPickResult().getIntersectedNode());  
            //System.out.println(e.getPickResult().getIntersectedNode().getParent().toString());
            
            //Node node = e.getPickResult().getIntersectedNode().getParent().getId();
            String stringNode = e.getPickResult().getIntersectedNode().toString();
            
            int closeTag = stringNode.indexOf("\"", stringNode.indexOf("\"") + 1);
            String strOut = stringNode.substring(11, closeTag);
            //System.out.println(stringNode);
            System.out.println(strOut);
            


            Dragboard db = e.getDragboard();
            
            if (db.hasContent(MainController.SERIALIZED_MIME_TYPE)) {
                //System.out.println(db.getContent(MainController.SERIALIZED_MIME_TYPE));
                
                ObservableList<M3UItem> data = MainController.data;
                
                //TableView tblContent = MainController.tblContent;
                
                //System.out.println;
                
                /*for(Object item : tblContent.getSelectionModel().getSelectedIndices()){
                    System.out.println("getSelectedIndices");
                    System.out.println(item);*/
                    
                    ObservableList<M3UItem> list = this.tblContent.getSelectionModel().getSelectedItems();
                    
                    for(int i=0;i<list.size(); i++){
                        System.out.println("SELLNER" + list.get(i).getTitleItem(strOut));
                        if(!list.get(i).getTitleItem(strOut)){
                            list.get(i).setTitle(strOut);
                        }
                        
                        tblContent.refresh();
                        System.out.println(list.get(i).getTitle());
                    }
                    
                    
                    
                    
                    /*M3UItem m3UItem = (M3UItem) line.get(Integer.parseInt(item.toString()));
                    
                    
                    
                    m3UItem.setTitle(strOut);
                    
                    System.out.println("TITLE:" + m3UItem.getTitle());
                    
                    line.set(Integer.parseInt(item.toString()), m3UItem);*/
                    
                    
                //}
                
            }
            
            e.setDropCompleted(true);
            e.consume();
        });
        setOnDragExited(e -> {
            System.out.println(" Exited ");
            e.consume();
        });
        setOnDragOver(e -> {
            System.out.println(" Over ");
            e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });
        }

        private String getString() {
            return getItem() == null ? "" : getItem();
        }

        private void createTextField() {
            textField = new TextField(getString());
            
            textField.setOnKeyReleased(event -> {
                if (event.getCode() == KeyCode.ENTER)
                        commitEdit(textField.getText());
                    else if (event.getCode() == KeyCode.ESCAPE)
                        cancelEdit();
            });
            
        }

        @Override public void startEdit() {
            super.startEdit();
            if (textField == null) {
                /*
                instantiate the textField with the curr text
                and install an EventHandler to have it commit
                on ENTER and cancel on ESCAPE
                */
                createTextField();
            }
            setText(null); // get the text out of the way
            setGraphic(textField); // replace the text with the textField
            textField.selectAll(); // select the text inside the input box
        }

        @Override public void cancelEdit() {
            super.cancelEdit();
            /* set the Cell's Label to contain the content of its underlying
             * TreeItem<String> item; property */
            setText(getItem());
            /* Labelled Nodes have "graphic" properties that are located in
             * a configurable location wrt to their text */
            setGraphic(getTreeItem().getGraphic());
        }

        /** this gets called automagically by the framework
         * THIS IS WHERE THE ORIGINAL TEXT GETS SET!
         * if you don't override this baby right, everything goes haywire
         * the most important thing is calling super.
         */
        @Override protected void updateItem(String item, boolean empty) {
            super.updateItem(item, empty);
            if (!empty) {
                if (isEditing()) {
                    if (textField != null)
                        textField.setText(getString());
                    setText(null);
                    setGraphic(textField);
                }
                else {
                    setText(getString());
                    setGraphic(getTreeItem().getGraphic());
                    if (!getTreeItem().isLeaf() && getTreeItem().getParent() != null)
                        setContextMenu(addMenu);
                }
            }
            else {
                setText(null);
                setGraphic(null);
            }
        }

    }
