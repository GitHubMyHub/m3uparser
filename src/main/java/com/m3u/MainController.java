/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.m3u;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeSet;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.TablePosition;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.Dragboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 *
 * @author V-Windows
 */
public class MainController implements Initializable {
    
    public static final DataFormat SERIALIZED_MIME_TYPE = new DataFormat("application/x-java-serialized-object");
    private ArrayList<M3UItem> selections = new ArrayList<>();
    public static ObservableList<M3UItem> data;
    private TreeItem<String> root;
    private ClipboardContent clipboardContent;
    private ContextMenu cm;
    private ContextMenu cmg;
    
    @FXML private VBox mainPane;
    @FXML private Label pizzaOrderLabel;
    @FXML private Button btnOpenFile;
    @FXML private TableView tblContent;
    @FXML private Label txtItemsCounter;
    @FXML private Label txtGoupsCounter;
    
    @FXML private MainController mainController;
    
    @FXML private TreeView<String> treeView;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.    
        
        this.data = FXCollections.observableArrayList();
        setRootGroup();
        setViewsFormat();
        setRowHandlers();
    }
    
    public void openFileExplorer(){
        
        Stage stage = (Stage) mainPane.getScene().getWindow();
        
        FileChooserController controller = new FileChooserController(stage, false);
           
        setContent(controller);
        setRowHandlers();
        setGroupListeners();
        setCounters();
    }
    
    public void openFileSaver() throws IOException{
        Stage stage = (Stage) mainPane.getScene().getWindow();
        
        FileChooserController controller = new FileChooserController(stage, true);
        
        System.out.println(controller.getSavePath());
        
        File selectedFile = controller.getSavePath();
    
        FileWriter fw = null;
        try {
            fw = new FileWriter(selectedFile);
            SaveFileCollection transform = new SaveFileCollection(data);
            //fw.write(transform.toString());
            fw.write(transform.toM3UString());
            fw.close();
        }catch(Exception e){
            
        }
        
    }
    
    public void getContent(){
        System.out.println(this.data.toString());
    }
    
    private void handleMouseClicked(MouseEvent event) {
        Node node = event.getPickResult().getIntersectedNode();
        // Accept clicks only on node cells, and not on empty spaces of the TreeView
        System.out.println("GOIL");
        System.out.println(node.toString());
        
        if(event.isSecondaryButtonDown()){
            System.out.println("RIGHT");
        }else{
            System.out.println("LEFT");
        }
        
        /*if(node instanceof Text){
            
            System.out.println("KAKA");
            System.out.println(event.isSecondaryButtonDown());
            
            if(event.isSecondaryButtonDown()){
                System.out.println("ITEM");

                cmg = new ContextMenu();

                MenuItem mi1 = new MenuItem("Create new Group");
                mi1.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));
                cmg.getItems().add(mi1);

                cmg.show(treeView, event.getScreenX(), event.getScreenY());
            
            }else{
                cmg.hide();
            }
        }*/
                
        
    }
    
    public void setGroupListeners(){
        try {
            cmg.hide();
        }catch(Exception e){
            
        }
        cmg = new ContextMenu();
        MenuItem mi1 = new MenuItem("Create new Group");
        mi1.setAccelerator(new KeyCodeCombination(KeyCode.G, KeyCombination.CONTROL_DOWN));
        mi1.setOnAction(event -> {
            createNewGroup();
        });
        cmg.getItems().add(mi1);
        
        //System.out.println("/////////////////////////////////////");
        //System.out.println(treeView.getSelectionModel().getSelectedItems().size());
        //System.out.println("/////////////////////////////////////");
        
        if(this.treeView.getSelectionModel().getSelectedItems().size() > 0){
        
            MenuItem mi2 = new MenuItem("Edit Group name");
            mi2.setAccelerator(new KeyCodeCombination(KeyCode.E, KeyCombination.CONTROL_DOWN));
            cmg.getItems().add(mi2);

            MenuItem mi3 = new MenuItem("Delete Group");
            mi3.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
            cmg.getItems().add(mi3);
            
            mi2.setOnAction(event -> {
                System.out.println("EDIT");
                
                //TreeItem<String> string = treeView.getSelectionModel().getSelectedItem();
                
                //System.out.println(string.toString());
                
                editNewGroup();
                
            });

            mi3.setOnAction(event -> {
                System.out.println("Delete Group");
                
                TreeItem<String> string = treeView.getSelectionModel().getSelectedItem();
                
                boolean remove = string.getParent().getChildren().remove(string);
                System.out.println("Remove");
                
            });
            
        }
        
        treeView.setCellFactory(e -> new TextFieldTreeCellImpl(tblContent));
        
    }
    
    public void setViewsFormat(){
        
        setGroupListeners();
        
        // Context Menu
        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() == MouseButton.SECONDARY) {
                    //tblContent.getContextMenu().show(tblContent, me.getSceneX(), me.getSceneY());
                    System.out.println("Clicked");
                    setGroupListeners();
                    cmg.show(treeView, me.getScreenX(), me.getScreenY());
                }else{
                    cmg.hide();
                }
            }
        };
        treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
        
        
        
        
        /*EventHandler<MouseEvent> mouseEventHandle = (MouseEvent event) -> {
            Node node = event.getPickResult().getIntersectedNode();
            // Accept clicks only on node cells, and not on empty spaces of the TreeView
            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)) {
                //String name = (String) ((TreeItem)treeView.getSelectionModel().getSelectedItem()).getValue();
                //TreeItem<String> item = (TreeItem)treeView.getSelectionModel().getSelectedItem();
                
                //System.out.println("Node click: " + name);

                System.out.println("Node click: " + node);

                System.out.println("POOOP");
            }
        };

        treeView.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, mouseEventHandle);*/
        
        /*treeView.setCellFactory(new Callback<TreeView<String>, TreeCell<String>>() {
            @Override
            public TreeCell<String> call(TreeView<String> stringTreeView) {
                TreeCell<String> treeCell = new TreeCell<String>() {
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            setText(item);
                        }
                    }
                };

                treeCell.setOnDragDetected(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {

                    }
                });

                return treeCell;
            }
        });*/
        
        
        treeView.setOnDragOver(event -> {
            event.acceptTransferModes(TransferMode.MOVE);
        });
        
        
        
        //TreeItem<String> test = treeView.getRoot().getChildren();
        
       
        
        

        
        /*treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                if(event.isSecondaryButtonDown()){
                    System.out.println("RIGHT");
                }else{
                    System.out.println("LEFT");
                }
            }
            
        });*/
        
        /*this.treeView.setOnMouseClicked(event -> {
            if(event.isSecondaryButtonDown()){
                System.out.println("RIGHT");
            }else{
                System.out.println("LEFT");
            }
        });*/
        
        
        tblContent.getColumns().clear();
        tblContent.getItems().clear();
        
        tblContent.setEditable(true);
        tblContent.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        TableColumn timeCol = new TableColumn("Time");
        timeCol.setMinWidth(100);
        timeCol.setSortable(false);
        timeCol.setCellValueFactory(
                new PropertyValueFactory<>("time"));
        timeCol.setCellFactory(TextFieldTableCell.forTableColumn());
        timeCol.setOnEditCommit(
            new EventHandler<CellEditEvent<M3UItem, String>>() {
                @Override
                public void handle(CellEditEvent<M3UItem, String> t) {
                    ((M3UItem) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                }
            }
        );
        
        TableColumn idCol = new TableColumn("Id");
        idCol.setMinWidth(100);
        idCol.setSortable(false);
        idCol.setCellValueFactory(
                new PropertyValueFactory<>("id"));
        idCol.setCellFactory(TextFieldTableCell.forTableColumn());
        idCol.setOnEditCommit(
            new EventHandler<CellEditEvent<M3UItem, String>>() {
                @Override
                public void handle(CellEditEvent<M3UItem, String> t) {
                    ((M3UItem) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                }
            }
        );
        
        
        /*TableColumn<M3UItem, CustomImage> logoCol = new TableColumn<M3UItem, CustomImage>("Logo");
        logoCol.setCellValueFactory(new PropertyValueFactory<>("logo"));
        logoCol.setMinWidth(200);
        logoCol.setSortable(false);
        
        
        logoCol.setCellFactory(new Callback<TableColumn<M3UItem, CustomImage>,TableCell<M3UItem, CustomImage>>(){        
            @Override
            public TableCell<M3UItem, CustomImage> call(TableColumn<M3UItem, CustomImage> param) {
                    TableCell<M3UItem, CustomImage> cell = new TableCell<M3UItem, CustomImage>(){
                        @Override
                        protected void updateItem(CustomImage item, boolean empty) {
                            if(item!=null){
                                HBox box= new HBox();
                                box.setSpacing(10) ;
                                VBox vbox = new VBox();
    
                                //if(item.getImage().getProgress() == 1){
                                    ImageView imageview = new ImageView();
                                    imageview.setFitHeight(50);
                                    imageview.setFitWidth(100);
                                    //imageview.setImage(new Image("https://i.stack.imgur.com/kS9Kf.png"));
                                    imageview.setImage(item.getImage());
                                //}
                                
                                box.getChildren().addAll(imageview, vbox);
                                //SETTING ALL THE GRAPHICS COMPONENT FOR CELL
                                setGraphic(box);
                            }
                        }

                    };

                    System.out.println(cell.getIndex());
                    return cell;
            }

        });*/
        
        TableColumn logoCol = new TableColumn("Logo");
        logoCol.setMinWidth(200);
        logoCol.setSortable(false);
        logoCol.setEditable(true);
        logoCol.setCellValueFactory(
                new PropertyValueFactory<>("logoUrl"));
        logoCol.setCellFactory(TextFieldTableCell.forTableColumn());
        logoCol.setOnEditCommit(
            new EventHandler<CellEditEvent<M3UItem, String>>() {
                @Override
                public void handle(CellEditEvent<M3UItem, String> t) {
                    ((M3UItem) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                }
            }
        );
        
        TableColumn titleCol = new TableColumn("Title");
        titleCol.setMinWidth(200);
        titleCol.setSortable(false);
        titleCol.setEditable(false);
        titleCol.setCellValueFactory(
                new PropertyValueFactory<>("title"));
        titleCol.setCellFactory(TextFieldTableCell.forTableColumn());
        titleCol.setOnEditCommit(
            new EventHandler<CellEditEvent<M3UItem, String>>() {
                @Override
                public void handle(CellEditEvent<M3UItem, String> t) {
                    ((M3UItem) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                }
            }
        );
        
        TableColumn nameCol = new TableColumn("Name");
        nameCol.setMinWidth(200);
        nameCol.setSortable(false);
        nameCol.setCellValueFactory(
                new PropertyValueFactory<>("name"));
        nameCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nameCol.setOnEditCommit(
            new EventHandler<CellEditEvent<M3UItem, String>>() {
                @Override
                public void handle(CellEditEvent<M3UItem, String> t) {
                    ((M3UItem) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                }
            }
        );
        
        TableColumn urlCol = new TableColumn("Url");
        urlCol.setMinWidth(200);
        urlCol.setSortable(false);
        urlCol.setCellValueFactory(
                new PropertyValueFactory<>("url"));
        urlCol.setCellFactory(TextFieldTableCell.forTableColumn());
        urlCol.setOnEditCommit(
            new EventHandler<CellEditEvent<M3UItem, String>>() {
                @Override
                public void handle(CellEditEvent<M3UItem, String> t) {
                    ((M3UItem) t.getTableView().getItems().get(
                            t.getTablePosition().getRow())
                            ).setName(t.getNewValue());
                }
            }
        );
        
        //this.tblContent.setEditable(true);
        this.tblContent.setItems(data);
        this.tblContent.getColumns().addAll(timeCol, idCol, logoCol, titleCol, nameCol, urlCol);
        this.tblContent.refresh();
    }
   
    public void setContent(FileChooserController controller){
        
        System.out.println("setContent");
        System.out.println(controller.items.toString());
        
        this.data.clear();
        this.data.addAll(controller.items);
        
        setRootGroup();
        
        //this.treeView.getRoot().getChildren().add(controller.groups.getTreeView(root));
        
        this.treeView.setRoot(controller.groups.getTreeView(root));
        
    }
    
    public void setRootGroup(){
        System.out.println("setRootGroup");
        root = new TreeItem<String>("All");
        root.setExpanded(true);
        this.treeView.setRoot(root);
    }
    
    public void createNewEntry(){
        System.out.println("createNewEntry");
        
        
        Stage primaryStage = (Stage) mainPane.getScene().getWindow();
        Stage stage = new Stage();
        
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        
        Parent root;
        try {
            //root = FXMLLoader.load(getClass().getResource("new_item_template.fxml"));
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new_item_template.fxml"));
            root = (Parent)loader.load();
      
            stage.setOnHiding(event ->{
                System.out.println("setOnHiding");
                NewItemController controller = (NewItemController) loader.getController();
                
                if(controller.getAddStatus() == true){
                    M3UItem item = controller.getNewItem();
                    System.out.println(item.toString());

                    this.data.add(item);
                }
                
                controller.closeWindow();

            });
           
            stage.setTitle("Add new entry");
            stage.setScene(new Scene(root, 450, 250));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void createNewGroup(){
        System.out.println("createNewGroup");
        
        Stage primaryStage = (Stage) mainPane.getScene().getWindow();
        Stage stage = new Stage();
        
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        
        Parent root;
        try {
            //root = FXMLLoader.load(getClass().getResource("new_item_template.fxml"));
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new_group_template.fxml"));
            root = (Parent)loader.load();
      
            stage.setOnHiding(event ->{
                System.out.println("setOnHiding");
                NewGroupController controller = (NewGroupController) loader.getController();
                String item = controller.getNewItem();
                System.out.println(item.toString());
                
                TreeItem<String> treeItem = new TreeItem<String>(item.toString());
                
                
                System.out.println(this.treeView.getRoot().getChildren().toString());

                this.treeView.getRoot().getChildren().add(treeItem);
                
                setCounters();

                controller.closeWindow();

            });
           
            stage.setTitle("Add new Group");
            stage.setScene(new Scene(root, 450, 100));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void editNewGroup(){
        System.out.println("editNewGroup");
        
        Stage primaryStage = (Stage) mainPane.getScene().getWindow();
        Stage stage = new Stage();
        
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        
        Parent root;
        try {
            //root = FXMLLoader.load(getClass().getResource("new_item_template.fxml"));
            
            FXMLLoader loader = new FXMLLoader(getClass().getResource("new_group_template.fxml"));
            root = (Parent)loader.load();
            final NewGroupController controller = (NewGroupController) loader.getController();
            
            TreeItem<String> string = treeView.getSelectionModel().getSelectedItem();
            controller.setGroupName(string.getValue());
      
            stage.setOnHiding(event ->{
                System.out.println("setOnHiding");
                //controller = (NewGroupController) loader.getController();

                String item = controller.getNewItem();
                System.out.println(item.toString());
                
                this.data.forEach(action -> {
                    System.out.println("NAME:" + action.getName());
                    System.out.println("ALL-VALUE:" + action.getTitle().toString());
                    System.out.println("NEW_VALUE:" + item.toString());
                    System.out.println("OLD-VALUE:" + string.getValue());
                    if(action.getTitleItem(string.getValue())){
                        System.out.println("UPDATE:" + string.getValue() + " TO " + item.toString());
                        action.updateTitle(string.getValue(), item.toString());
                    }
                    
                });
                
                tblContent.refresh();

                string.setValue(item);
                
                setCounters();

                controller.closeWindow();

            });
           
            stage.setTitle("Edit Group");
            stage.setScene(new Scene(root, 450, 100));
            stage.show();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @SuppressWarnings("rawtypes")
    public void copySelectionToClipboard(){
        
        final Set<Integer> rows = new TreeSet<>();
        for (final TablePosition tablePosition : (ObservableList<TablePosition>) tblContent.getSelectionModel().getSelectedCells()) {
            rows.add(tablePosition.getRow());
        }
        final StringBuilder strb = new StringBuilder();
        boolean firstRow = true;
        for (final Integer row : rows) {
            if (!firstRow) {
                strb.append('\n');
            }
            firstRow = false;
            boolean firstCol = true;
            for (final TableColumn<?, ?> column : (ObservableList<TableColumn>) tblContent.getColumns()) {
                if (!firstCol) {
                    //System.out.println("KENFM:" + column.getText().toString());
                    strb.append('\t');
                }
                firstCol = false;
                Object cellData;
                if(!column.getText().toString().equals("Logo")){
                    cellData = column.getCellData(row);
                    System.out.println("KENFM:" + cellData);
                }else{
                    cellData = this.data.get(row).getLogoUrl();
                    System.out.println("KENFM:" + cellData);
                    //System.out.println("-->" + row);
                }
                
                strb.append(cellData == null ? "" : cellData.toString());
            }
        }
        final ClipboardContent clipboardContent = new ClipboardContent();
        clipboardContent.putString(strb.toString());
        Clipboard.getSystemClipboard().setContent(clipboardContent);
        
    }
    
    public void pasteSelectionToClipboard(int index) {
        
        System.out.println("pasteSelectionToClipboard");
        
        System.out.println(Clipboard.getSystemClipboard().getString());
        
        String string = Clipboard.getSystemClipboard().getString();
        
        String[] row = string.split("\n");
        
        for(String line: row){
            
            String[] item = line.split("\t");

            M3UItem m3UItem = new M3UItem();

            m3UItem.setTime(item[0].toString());
            m3UItem.setId(item[1].toString());
            //m3UItem.setLogo(new CustomImage(item[2].toString()));
            m3UItem.setLogo(new CustomImage(item[2].toString()));
            m3UItem.setLogoUrl(item[2].toString());
            m3UItem.setTitle(item[3].toString());
            m3UItem.setName(item[4].toString());
            m3UItem.setUrl(item[5].toString());

            data.add(index, m3UItem); 
        
        }

        //System.out.println(item[3].toString());

    }
    
    public void cutRow(){
        System.out.println("cutRow");
    }
    
    public void selectAll(){
        System.out.println("selectAll");
        tblContent.getSelectionModel().selectAll();
    }
    
    public void unselectAll(){
        System.out.println("unselectAll");
        tblContent.getSelectionModel().clearAndSelect(0);
    }
    
    public void deleteSelected(){
        System.out.println("deleteSelected");
        tblContent.getItems().removeAll(new ArrayList<Object>(tblContent.getSelectionModel().getSelectedItems()));
    }

    public void setRowHandlers(){
        
        System.out.println("setRowHandlers");
        
        cm = new ContextMenu();
        MenuItem mi1 = new MenuItem("Create new entry");
        mi1.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        cm.getItems().add(mi1);
        
        mi1.setOnAction(event -> {
            createNewEntry();
        });
        
        
        if(tblContent.getItems().size()> 0){
            
        
            MenuItem mi2 = new MenuItem("Copy");
            mi2.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
            cm.getItems().add(mi2);
            MenuItem mi3 = new MenuItem("Cut");
            mi3.setAccelerator(new KeyCodeCombination(KeyCode.X, KeyCombination.CONTROL_DOWN));
            cm.getItems().add(mi3);
            MenuItem mi4 = new MenuItem("Paste");
            mi4.setAccelerator(new KeyCodeCombination(KeyCode.V, KeyCombination.CONTROL_DOWN));
            cm.getItems().add(mi4);
            MenuItem mi5 = new MenuItem("Select All");
            mi5.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.CONTROL_DOWN));
            cm.getItems().add(mi5);
            MenuItem mi6 = new MenuItem("Unselect All");
            cm.getItems().add(mi6);
            MenuItem mi7 = new MenuItem("Delete");
            mi7.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.CONTROL_DOWN));
            cm.getItems().add(mi7);

            mi2.setOnAction(event -> {
                copySelectionToClipboard();
            });
            mi3.setOnAction(event -> {
                cutRow();
            });
            mi4.setOnAction(event -> {

                ObservableList selectedCells = tblContent.getSelectionModel().getSelectedCells();
                TablePosition tablePosition = (TablePosition) selectedCells.get(0);

                pasteSelectionToClipboard(tablePosition.getRow());
            });
            mi5.setOnAction(event -> {
                selectAll();
            });
            mi6.setOnAction(event -> {
                unselectAll();
            });
            mi7.setOnAction(event -> {
                deleteSelected();
            });
        
        }
        
        EventHandler eventHandler = new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent me) {
                if (me.getButton() == MouseButton.SECONDARY) {
                    //tblContent.getContextMenu().show(tblContent, me.getSceneX(), me.getSceneY());
                    System.out.println("Clicked");
                    cm.show(tblContent, me.getScreenX(), me.getScreenY());
                }else{
                    cm.hide();
                }
            }
        };
        tblContent.addEventHandler(MouseEvent.MOUSE_CLICKED, eventHandler);
                    
        tblContent.setRowFactory(tv -> {
            TableRow<M3UItem> row = new TableRow<>();

            row.setOnMousePressed(event ->{
                if(event.isSecondaryButtonDown() && !row.isEmpty()){
                    cm.show(tblContent, event.getScreenX(), event.getScreenY());
                    System.out.println(event.isSecondaryButtonDown());
                }else{
                    cm.hide();
                }

            });

            row.setOnDragDetected(event -> {
                if (! row.isEmpty()) {
                    Integer index = row.getIndex();

                    selections.clear();//important...

                    ObservableList<M3UItem> items = tblContent.getSelectionModel().getSelectedItems();

                    for(M3UItem iI:items) {
                        selections.add(iI);
                    }


                    Dragboard db = row.startDragAndDrop(TransferMode.MOVE);
                    db.setDragView(row.snapshot(null, null));
                    ClipboardContent cc = new ClipboardContent();
                    cc.put(SERIALIZED_MIME_TYPE, index);
                    db.setContent(cc);
                    event.consume();
                }
            });

            row.setOnDragOver(event -> {
                Dragboard db = event.getDragboard();
                if (db.hasContent(SERIALIZED_MIME_TYPE)) {
                    if (row.getIndex() != ((Integer)db.getContent(SERIALIZED_MIME_TYPE)).intValue()) {
                        event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
                        event.consume();
                    }
                }
            });

            row.setOnDragDropped(event -> {
                Dragboard db = event.getDragboard();

                if (db.hasContent(SERIALIZED_MIME_TYPE)) {

                    int dropIndex;M3UItem dI=null; 

                    if (row.isEmpty()) {
                        dropIndex = tblContent.getItems().size();
                    } else {
                        dropIndex = row.getIndex();
                        dI = (M3UItem) tblContent.getItems().get(dropIndex);
                    }
                    int delta=0;
                    if(dI!=null)
                    while(selections.contains(dI)) {
                        delta=1;
                        --dropIndex;
                        if(dropIndex<0) {
                            dI=null;dropIndex=0;
                            break;
                        }
                        dI = (M3UItem) tblContent.getItems().get(dropIndex);
                    }

                    for(M3UItem sI:selections) {
                        tblContent.getItems().remove(sI);
                    }

                    if(dI!=null)
                        dropIndex=tblContent.getItems().indexOf(dI)+delta;
                    else if(dropIndex!=0)
                        dropIndex=tblContent.getItems().size();



                    tblContent.getSelectionModel().clearSelection();

                    for(M3UItem sI:selections) {
                        //draggedIndex = selections.get(i);
                        tblContent.getItems().add(dropIndex, sI);
                        tblContent.getSelectionModel().select(dropIndex);
                        dropIndex++;

                    }

                    event.setDropCompleted(true);
                    selections.clear();
                    event.consume();
                }
            });

            return row;
        });
    }
    
    public void setCounters(){

        int size = tblContent.getItems().size();
        txtItemsCounter.setText(String.valueOf(size));
        
        size = treeView.getExpandedItemCount();
        txtGoupsCounter.setText(String.valueOf(size-1));

    }

}
