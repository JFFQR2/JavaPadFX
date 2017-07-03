import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.swing.text.BadLocationException;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by quite on 13.04.17.
 */
public class GUI extends Application {
    private MenuBar menuBar = new MenuBar();

    @Override
    public void start(Stage primaryStage) throws Exception {
        Panel root = new Panel();
        root.setTop(menuBar);
        primaryStage.setScene(new Scene(root,1280,720));
        primaryStage.setTitle("JavaPadFx");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    private class Panel extends BorderPane {
        private Alert alert;
        private int pos = 0;
        private Stage chooserStage,frameReplace = null, frameFind = null,frameC=null,frameJ=null,
                frameChangeCharset = null, frameAbout = null,
                frameFont = null, frameTextColor = null, frameBackGroundColor = null, frameSelectionColor = null;
        private TextArea jTextArea = new TextArea();
        private FileChooser chooser;
        private ListView charsets;
        private String path = null,textColor="black",highlightColor="cyan";
        private String currentCharset = "";
        private final ObservableList<String> color = FXCollections.observableArrayList("White", "Black", "Cyan", "Blue", "Dark Gray", "Gray", "Light Gray", "Green", "Magenta", "Orange", "Pink", "Red", "Yellow","Lime","Gold","Silver","Light Green");

                private void replace(){
            if (frameReplace==null){
                frameReplace = new Stage();
                frameReplace.setTitle("Replace Text");
            }
            Pane root = new Pane();
            frameReplace.setScene(new Scene(root,278,80));
            TextField field = new TextField();
            TextField field2 = new TextField();
            field.setPrefSize(150,20);
            field2.setPrefSize(150,20);
            field.setLayoutX(120);
            field.setLayoutY(5);
            field2.setLayoutX(120);
            field2.setLayoutY(30);
            Label label = new Label("Find What:");
            Label label2 = new Label("Replace with");
            label.setLayoutX(5);
            label.setLayoutY(5);
            label2.setLayoutX(5);
            label2.setLayoutY(30);
            root.getChildren().addAll(label,field,label2,field2);
            Button button = new Button("Replace");
            button.setLayoutX(5);
            button.setLayoutY(50);
            root.getChildren().add(button);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    String s = jTextArea.getText().replaceAll(field.getText(),field2.getText());
                    jTextArea.setText(s);
                }
            });
            frameReplace.setResizable(false);
            frameReplace.show();
        }
        private void find(){
            if (frameFind==null){
                frameFind = new Stage();
                frameFind.setTitle("Find Text");
            }
            Pane root = new Pane();
            frameFind.setScene(new Scene(root,200,27));
            TextField field = new TextField();
            field.setPrefSize(200,20);
            field.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        findText(jTextArea,field.getText());
                    } catch (BadLocationException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            field.setLayoutX(0);
            field.setLayoutY(0);
            root.getChildren().add(field);
            frameFind.setResizable(false);
            frameFind.show();
        }
        private void changeCharsets(){
            if (frameChangeCharset==null){
                frameChangeCharset = new Stage();
                frameChangeCharset.setTitle("Change charset");
            }
            FlowPane root = new FlowPane();
            frameChangeCharset.setScene(new Scene(root,640,320));
            Map<String,Charset> charsetsAvailable = Charset.availableCharsets();
            ObservableList<Object> availableCharsets =FXCollections.observableArrayList(charsetsAvailable.keySet().toArray());
            charsets = new ListView(availableCharsets);
            charsets.setPrefSize(280,320);
            TextArea textArea = new TextArea();
            textArea.setEditable(false);
            textArea.setWrapText(true);
            textArea.setPrefSize(320,120);
            Button button = new Button("OK");
            button.setPrefSize(40,320);
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    jTextArea.setText(textArea.getText());
                    frameChangeCharset.close();
                }
            });
            charsets.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener() {
                @Override
                public void onChanged(Change c) {
                    try {
                        String s = new String(jTextArea.getText().getBytes(),charsets.getSelectionModel().getSelectedItem().toString());
                        currentCharset = new String("".getBytes(),charsets.getSelectionModel().getSelectedItem().toString());
                        textArea.setText(s);
                    } catch (UnsupportedEncodingException e1) {
                        e1.printStackTrace();
                    }
                }
            });
            root.getChildren().addAll(charsets,textArea,button);
            frameChangeCharset.setResizable(false);
            frameChangeCharset.show();
        }
        private void about(){
            if (frameAbout==null){
                frameAbout = new Stage();
                frameAbout.setTitle("About");
            }
            FlowPane root = new FlowPane();
            frameAbout.setScene(new Scene(root,250,60));
            Label label = new Label("          JavaPad 0.1.1");
            Label jLabel = new Label("Created by Quite River 13.04.2017");
            label.setFont(Font.font("Serif", FontWeight.BOLD,18));
            jLabel.setFont(Font.font("Serif", FontPosture.ITALIC,14));
            root.getChildren().addAll(label,jLabel);
            frameAbout.setResizable(false);
            frameAbout.show();
        }
        private void setFont(){
            if (frameFont==null){
                frameFont=new Stage();
                frameFont.setTitle("Font");
            }
            FlowPane root = new FlowPane();
            root.setPrefSize(745,260);
            ObservableList<String> styles = FXCollections.observableArrayList("Bold","Italic","Extra Bold","Light","Medium","Thin","Semi Bold","Regular");
            ListView<String> listStyle = new ListView<>(styles);
            ObservableList<Integer> size = FXCollections.observableArrayList(6,7,8,9,10,11,12,
                    13,14,15,16,17,18,20,22,24,26,28,32,36,40,48,56,64,72);
            ListView<Integer> listSize = new ListView<>(size);
            ObservableList fonts = FXCollections.observableArrayList(Font.getFamilies());
            ListView<String> list = new ListView<>(fonts);
            list.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
                @Override
                public void onChanged(Change<? extends Integer> c) {
                    int s;
                    if (listSize.getSelectionModel().getSelectedItem()==null)
                        s = 14;
                    else s=listSize.getSelectionModel().getSelectedItem();
                    switch (listStyle.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.BOLD, s));
                            break;
                        case 1:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontPosture.ITALIC, s));
                            break;
                        case 2:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.EXTRA_BOLD, s));
                            break;
                        case 3:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.LIGHT, s));
                            break;
                        case 4:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.MEDIUM, s));
                            break;
                        case 5:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(),FontWeight.THIN,s));
                            break;
                        case 6:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(),FontWeight.SEMI_BOLD,s));
                            break;
                        case 7:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontPosture.REGULAR, s));
                            break;
                        default:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(),s));
                            break;
                    }
                }
            });
            listStyle.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
                @Override
                public void onChanged(Change<? extends Integer> c) {
                    int s;
                    if (listSize.getSelectionModel().getSelectedItem()==null)
                        s = 14;
                    else s=listSize.getSelectionModel().getSelectedItem();
                    switch (listStyle.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.BOLD, s));
                            break;
                        case 1:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontPosture.ITALIC, s));
                            break;
                        case 2:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.EXTRA_BOLD, s));
                            break;
                        case 3:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.LIGHT, s));
                            break;
                        case 4:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.MEDIUM, s));
                            break;
                        case 5:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(),FontWeight.THIN,s));
                            break;
                        case 6:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(),FontWeight.SEMI_BOLD,s));
                            break;
                        case 7:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontPosture.REGULAR, s));
                            break;
                        default:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(),s));
                            break;
                    }
                }
            });
            listSize.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
                @Override
                public void onChanged(Change<? extends Integer> c) {
                    int s;
                    if (listSize.getSelectionModel().getSelectedItem()==null)
                        s = 14;
                    else s=listSize.getSelectionModel().getSelectedItem();
                    switch (listStyle.getSelectionModel().getSelectedIndex()) {
                        case 0:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.BOLD, s));
                            break;
                        case 1:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontPosture.ITALIC, s));
                            break;
                        case 2:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.EXTRA_BOLD, s));
                            break;
                        case 3:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.LIGHT, s));
                            break;
                        case 4:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontWeight.MEDIUM, s));
                            break;
                        case 5:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(),FontWeight.THIN,s));
                            break;
                        case 6:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(),FontWeight.SEMI_BOLD,s));
                            break;
                        case 7:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(), FontPosture.REGULAR, s));
                            break;
                        default:
                            jTextArea.setFont(Font.font(list.getSelectionModel().getSelectedItem(),s));
                            break;
                    }
                }
            });
            root.getChildren().addAll(list,listStyle,listSize);
            frameFont.setScene(new Scene(root));
            frameFont.setResizable(false);
            frameFont.show();
        }
        private void textColor(){
            if (frameTextColor==null){
                frameTextColor=new Stage();
                frameTextColor.setTitle("Text Color");
            }
            FlowPane root = new FlowPane();
            root.setPrefSize(120,150);
            ListView<String> colors = new ListView<>(color);
            colors.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
                @Override
                public void onChanged(Change<? extends Integer> c) {
                    int index = colors.getSelectionModel().getSelectedIndex();
                    switch (index){
                        case 0:  jTextArea.setStyle("-fx-text-fill: white; -fx-highlight-fill: " + highlightColor);
                            textColor="white";
                            break;
                        case 1:  jTextArea.setStyle("-fx-text-fill: black; -fx-highlight-fill: " + highlightColor);
                            textColor="black";
                            break;
                        case 2: jTextArea.setStyle("-fx-text-fill: cyan; -fx-highlight-fill: " + highlightColor);
                            textColor="cyan";
                            break;
                        case 3: jTextArea.setStyle("-fx-text-fill: blue; -fx-highlight-fill: " + highlightColor);
                            textColor="blue";
                            break;
                        case 4: jTextArea.setStyle("-fx-text-fill: a9a9a9; -fx-highlight-fill: " + highlightColor);
                            textColor="a9a9a9";
                            break;
                        case 5: jTextArea.setStyle("-fx-text-fill: gray; -fx-highlight-fill: " + highlightColor);
                            textColor="gray";
                            break;
                        case 6: jTextArea.setStyle("-fx-text-fill: d3d3d3; -fx-highlight-fill: " + highlightColor);
                            textColor="d3d3d3";
                            break;
                        case 7: jTextArea.setStyle("-fx-text-fill: green; -fx-highlight-fill: " + highlightColor);
                            textColor="green";
                            break;
                        case 8: jTextArea.setStyle("-fx-text-fill: magenta; -fx-highlight-fill: " + highlightColor);
                            textColor="magenta";
                            break;
                        case 9: jTextArea.setStyle("-fx-text-fill: orange; -fx-highlight-fill: " + highlightColor);
                            textColor="orange";
                            break;
                        case 10: jTextArea.setStyle("-fx-text-fill: pink; -fx-highlight-fill: " + highlightColor);
                            textColor="pink";
                            break;
                        case 11: jTextArea.setStyle("-fx-text-fill: red; -fx-highlight-fill: " + highlightColor);
                            textColor="red";
                            break;
                        case 12: jTextArea.setStyle("-fx-text-fill: yellow; -fx-highlight-fill: " + highlightColor);
                            textColor="yellow";
                            break;
                        case 13: jTextArea.setStyle("-fx-text-fill: BFFF00; -fx-highlight-fill: " + highlightColor);
                            textColor="bfff00";
                            break;
                        case 14: jTextArea.setStyle("-fx-text-fill: ffd700; -fx-highlight-fill: " + highlightColor);
                            textColor="ffd700";
                            break;
                        case 15: jTextArea.setStyle("-fx-text-fill: c0c0c0; -fx-highlight-fill: " + highlightColor);
                            textColor="c0c0c0";
                            break;
                        case 16: jTextArea.setStyle("-fx-text-fill: rgb(0,255,0); -fx-highlight-fill: " + highlightColor);
                            textColor="rgb(0,250,0)";
                            break;

                    }
                }
            });
            root.getChildren().add(colors);
            Scene scene = new Scene(root);
            frameTextColor.setScene(scene);
            frameTextColor.setResizable(false);
            frameTextColor.show();
        }
        private void backGroundColor(){
            if (frameBackGroundColor==null){
                frameBackGroundColor = new Stage();
                frameBackGroundColor.setTitle("BackGround Color");
            }
            FlowPane root = new FlowPane();
            root.setPrefSize(120,150);
            ListView<String> colors = new ListView<>(color);
            Region region = ( Region ) jTextArea.lookup( ".content" );
            colors.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
            @Override
            public void onChanged(Change<? extends Integer> c) {
                int index = colors.getSelectionModel().getSelectedIndex();
                switch (index){
                    case 0:  region.setStyle("-fx-background-color: white");
                        break;
                    case 1:  region.setStyle("-fx-background-color: black");
                        break;
                    case 2: region.setStyle("-fx-background-color: cyan");
                        break;
                    case 3: region.setStyle("-fx-background-color: blue");
                        break;
                    case 4: region.setStyle("-fx-background-color: a9a9a9");
                        break;
                    case 5: region.setStyle("-fx-background-color: gray");
                        break;
                    case 6: region.setStyle("-fx-background-color: d3d3d3");
                        break;
                    case 7: region.setStyle("-fx-background-color: green");
                        break;
                    case 8: region.setStyle("-fx-background-color: magenta");
                        break;
                    case 9: region.setStyle("-fx-background-color: orange");
                        break;
                    case 10: region.setStyle("-fx-background-color: pink");
                        break;
                    case 11: region.setStyle("-fx-background-color: red");
                        break;
                    case 12: region.setStyle("-fx-background-color: yellow");
                        break;
                    case 13: region.setStyle("-fx-background-color: BFFF00");
                        break;
                    case 14: region.setStyle("-fx-background-color: ffd700");
                        break;
                    case 15: region.setStyle("-fx-background-color: c0c0c0");
                        break;
                    case 16: region.setStyle("-fx-background-color: rgb(0,255,0)");
                        break;

                }
            }
        });
            root.getChildren().add(colors);
            Scene scene = new Scene(root);
            frameBackGroundColor.setScene(scene);
            frameBackGroundColor.setResizable(false);
            frameBackGroundColor.show();
        }
        private void selectionColor(){
            if (frameSelectionColor==null){
                frameSelectionColor = new Stage();
                frameSelectionColor.setTitle("Selection Color");
            }
            FlowPane root = new FlowPane();
            root.setPrefSize(120,150);
            ListView<String> colors = new ListView<>(color);
            colors.getSelectionModel().getSelectedIndices().addListener(new ListChangeListener<Integer>() {
                @Override
                public void onChanged(Change<? extends Integer> c) {
                    int index = colors.getSelectionModel().getSelectedIndex();
                    switch (index){
                        case 0:  jTextArea.setStyle("-fx-highlight-fill: white; -fx-text-fill: "+textColor);
                            highlightColor="white";
                            break;
                        case 1:  jTextArea.setStyle("-fx-highlight-fill: black; -fx-text-fill: "+textColor);
                            highlightColor="black";
                            break;
                        case 2: jTextArea.setStyle("-fx-highlight-fill: cyan; -fx-text-fill: "+textColor);
                            highlightColor="cyan";
                            break;
                        case 3: jTextArea.setStyle("-fx-highlight-fill: blue; -fx-text-fill: "+textColor);
                            highlightColor="blue";
                            break;
                        case 4: jTextArea.setStyle("-fx-highlight-fill: a9a9a9; -fx-text-fill: "+textColor);
                            highlightColor="a9a9a9";
                            break;
                        case 5: jTextArea.setStyle("-fx-highlight-fill: gray; -fx-text-fill: "+textColor);
                            highlightColor="gray";
                            break;
                        case 6: jTextArea.setStyle("-fx-highlight-fill: d3d3d3; -fx-text-fill: "+textColor);
                            highlightColor="d3d3d3";
                            break;
                        case 7: jTextArea.setStyle("-fx-highlight-fill: green; -fx-text-fill: "+textColor);
                            highlightColor="green";
                            break;
                        case 8: jTextArea.setStyle("-fx-highlight-fill: magenta; -fx-text-fill: "+textColor);
                            highlightColor="magenta";
                            break;
                        case 9: jTextArea.setStyle("-fx-highlight-fill: orange; -fx-text-fill: "+textColor);
                            highlightColor="orange";
                            break;
                        case 10: jTextArea.setStyle("-fx-highlight-fill: pink; -fx-text-fill: "+textColor);
                            highlightColor="pink";
                            break;
                        case 11: jTextArea.setStyle("-fx-highlight-fill: red; -fx-text-fill: "+textColor);
                            highlightColor="red";
                            break;
                        case 12: jTextArea.setStyle("-fx-highlight-fill: yellow; -fx-text-fill: "+textColor);
                            highlightColor="yellow";
                            break;
                        case 13: jTextArea.setStyle("-fx-highlight-fill: BFFF00; -fx-text-fill: "+textColor);
                            highlightColor="bfff00";
                            break;
                        case 14: jTextArea.setStyle("-fx-highlight-fill: ffd700; -fx-text-fill: "+textColor);
                            highlightColor="ffd700";
                            break;
                        case 15: jTextArea.setStyle("-fx-highlight-fill: c0c0c0; -fx-text-fill: "+textColor);
                            highlightColor="c0c0c0";
                            break;
                        case 16: jTextArea.setStyle("-fx-highlight-fill: rgb(0,255,0); -fx-text-fill: "+textColor);
                            highlightColor="rgb(0,255,0)";
                            break;

                    }
                }
            });
            root.getChildren().add(colors);
            Scene scene = new Scene(root);
            frameSelectionColor.setScene(scene);
            frameSelectionColor.setResizable(false);
            frameSelectionColor.show();
        }
        private void save(){
            if (path ==null) {
                chooser = new FileChooser();
                chooser.setTitle("Save");
                if (chooserStage==null){
                    chooserStage = new Stage();
                }
                    File selectedDirectory = chooser.showOpenDialog(chooserStage);
                    if (selectedDirectory != null) {
                        try (BufferedWriter br = new BufferedWriter(new FileWriter(selectedDirectory))) {
                            br.write(jTextArea.getText());
                        } catch (IOException exp) {
                            exp.printStackTrace();
                            alertError("save");
                        }
                    }
            } else {
                try (BufferedWriter br = new BufferedWriter(new FileWriter(new File(path)))) {
                    br.write(jTextArea.getText());
                } catch (IOException exp) {
                    exp.printStackTrace();
                    alertError("save");
                }
            }
        }
        private void create() {
            if (chooser == null) {
                chooser = new FileChooser();
            }
            if (chooserStage==null){
                chooserStage = new Stage();
            }
            chooser.setTitle("Create");

            File selectedDirectory = chooser.showOpenDialog(chooserStage);
            if (selectedDirectory != null&&!Files.exists(Paths.get(selectedDirectory.getAbsolutePath()))){
                try {
                    Files.createFile(Paths.get(selectedDirectory.getAbsolutePath()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        private void addTo(){
            if (chooser == null) {
                chooser = new FileChooser();
            }
            if (chooserStage==null){
                chooserStage = new Stage();
            }
            chooser.setTitle("Add to...");
            File selectedDirectory = chooser.showOpenDialog(chooserStage);
            if (selectedDirectory != null) {
                try (BufferedWriter br = new BufferedWriter(new FileWriter(selectedDirectory, true))) {
                    path = selectedDirectory.getAbsolutePath();
                    br.write("\n");
                    br.write(jTextArea.getText());
                } catch (IOException exp) {
                    exp.printStackTrace();
                    alertError("add to");
                }
            }
        }
        private void saveAs(){
            if (chooser==null) {
                chooser = new FileChooser();
            }
            if (chooserStage==null){
                chooserStage = new Stage();
            }
            chooser.setTitle("Save As");
            File selectedDirectory = chooser.showOpenDialog(chooserStage);
            if (selectedDirectory != null) {
                try (BufferedWriter br = new BufferedWriter(new FileWriter(selectedDirectory))) {
                    path = selectedDirectory.getAbsolutePath();
                    br.write(jTextArea.getText());
                } catch (IOException exp) {
                    exp.printStackTrace();
                    alertError("save");
                }
            }
        }
        private void load(){
            if (chooser == null) {
                chooser = new FileChooser();
            }
                if (chooserStage==null) {
                    chooserStage = new Stage();
                }
//               chooser.addChoosableFileFilter(new FileFilter() {
//                   @Override
//                   public boolean accept(File f) {
//                       return f.isDirectory() || f.getName().toLowerCase().endsWith(".txt");
//                   }
//
//                   @Override
//                   public String getDescription() {
//                       return "Text Files (*.txt)";
//                   }
//
//               });
            chooser.setTitle("Open");

            File selectedDirectory = chooser.showOpenDialog(chooserStage);
            if (selectedDirectory != null) {
                try (BufferedReader br = new BufferedReader(new FileReader(selectedDirectory))) {
                    path = selectedDirectory.getAbsolutePath();
                    Stage stage = new Stage();
                    progress(stage);
                    jTextArea.setText(null);
                    String text = null;
                    StringBuilder builder = new StringBuilder();
                    while ((text = br.readLine()) != null) {
                        builder.append(text);
                        builder.append("\n");
                    }
                    jTextArea.appendText(builder.toString());
                    stage.close();
                } catch (IOException exp) {
                    exp.printStackTrace();
                    alertError("load");
                }
            }
        }
        private void cCompiling(){
            if (frameC == null){
                frameC = new Stage();
                frameC.setTitle("C compiling");
            }
            Pane root = new Pane();
            TextField fieldIn = new TextField();
            TextField fieldOut = new TextField();
            fieldIn.setPrefSize(50,20);
            fieldOut.setPrefSize(50,20);
            Label labelIn = new Label("In File");
            Label labelOut = new Label("Out File");
            Button button = new Button("Choose a compiling file");
            Button buttonC = new Button("Compile");
            buttonC.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    try {
                        Process proc = Runtime.getRuntime().exec("gcc " + fieldIn.getText()+" -o " + fieldOut.getText());
                        proc.waitFor();
                        BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        BufferedReader bufError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                        String line = "";
                        while ((line=buf.readLine())!=null) {
                            System.out.println(line);
                        }
                        while ((line=bufError.readLine())!=null) {
                            System.out.print(line);
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    frameC.close();
                }
            });
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    chooser(chooser,fieldIn,"Choose a compiling file");

                }
            });
            if (path!=null){
                fieldIn.setText(path);
            }
            root.setPrefSize(340,100);
            labelIn.setLayoutX(5);
            labelIn.setLayoutY(5);
            labelIn.setPrefSize(50,20);
            fieldIn.setLayoutX(60);
            fieldIn.setLayoutY(5);
            fieldIn.setPrefSize(270,20);
            labelOut.setLayoutX(5);
            labelOut.setLayoutY(30);
            labelOut.setPrefSize(55,20);
            fieldOut.setLayoutX(65);
            fieldOut.setLayoutY(30);
            fieldOut.setPrefSize(265,20);
            button.setLayoutX(5);
            button.setLayoutY(60);
            button.setPrefSize(200,20);
            buttonC.setLayoutX(210);
            buttonC.setLayoutY(60);
            buttonC.setPrefSize(120,20);
            root.getChildren().addAll(labelIn,fieldIn,labelOut,fieldOut,button,buttonC);
            frameC.setScene(new Scene(root));
            frameC.setResizable(false);
            frameC.show();
        }
        private void javaCompiling(){
            if (frameJ == null){
                frameJ = new Stage();
                frameJ.setTitle("Java Compiling");
            }
            Pane root = new Pane();
            TextField fieldIn = new TextField();
            fieldIn.setPrefSize(50,20);
            Label labelIn = new Label("File");
            TextArea area = new TextArea();
            area.setPrefSize(325,100);
            area.setWrapText(true);
            area.setEditable(false);
            Button button = new Button("Choose a compiling file");
            Button buttonC = new Button("Compile");
            buttonC.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    area.setText("");
                    String[] data;
                    data=fieldIn.getText().split(File.separator);
                    StringBuilder text=new StringBuilder();
                    for (int i=0;i<data.length;i++){
                        if (i==data.length-1){
                            break;
                        }
                        text.append(data[i] + File.separator);
                    }
                    try {
                        Process proc = Runtime.getRuntime().exec("javac -sourcepath " + text + " " + fieldIn.getText());
                        proc.waitFor();
                        BufferedReader buf = new BufferedReader(new InputStreamReader(proc.getInputStream()));
                        BufferedReader bufError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
                        String line = "";
                        while ((line=buf.readLine())!=null) {
                            area.appendText(line);
                            area.appendText("\n");
                        }
                        while ((line=bufError.readLine())!=null) {
                            area.appendText(line);
                            area.appendText("\n");
                        }
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    } catch (InterruptedException e1) {
                        e1.printStackTrace();
                    }
                    if (area.getText().isEmpty())
                        frameJ.close();
                }
            });
            button.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    chooser(chooser,fieldIn,"Choose a compiling file");
                }
            });
            if (path!=null){
                fieldIn.setText(path);
            }
            root.setPrefSize(340,170);
            labelIn.setLayoutX(5);
            labelIn.setLayoutY(5);
            labelIn.setPrefSize(50,20);
            fieldIn.setLayoutX(60);
            fieldIn.setLayoutY(5);
            fieldIn.setPrefSize(270,20);
            button.setLayoutX(5);
            button.setLayoutY(140);
            button.setPrefSize(200,20);
            buttonC.setLayoutX(210);
            buttonC.setLayoutY(140);
            buttonC.setPrefSize(120,20);
            area.setLayoutX(5);
            area.setLayoutY(35);
            root.getChildren().addAll(labelIn,fieldIn,button,buttonC,area);
            frameJ.setScene(new Scene(root));
            frameJ.setResizable(false);
            frameJ.show();
        }
        public Panel() {
            jTextArea.setPrefSize(1280, 720);
            jTextArea.setWrapText(true);
            Menu menuFile = new Menu("File");
            Menu menuEditing = new Menu("Editing");
            Menu menuFind = new Menu("Find");
            Menu menuCode = new Menu("Code");
            Menu menuCharsets = new Menu("Charsets");
            Menu menuSettings = new Menu("Settings");
            Menu menuAbout = new Menu("About");
            MenuItem cCompileItem = new MenuItem("C Compile");
            MenuItem loadItem = new MenuItem("Load File");
            MenuItem fontItem = new MenuItem("Font");
            MenuItem fontColor = new MenuItem("Text Color");
            MenuItem backGroundColor = new MenuItem("BackGround Color");
            MenuItem selectionSetColor = new MenuItem("Selection Color");
            MenuItem findItem = new MenuItem("Find Text");
            MenuItem replaceItem = new MenuItem("Replace Text");
            loadItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    load();
                }
            });
            MenuItem saveAsItem = new MenuItem("Save as");
            saveAsItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    saveAs();
                }
            });
            MenuItem addToFile = new MenuItem("Add to File");
            addToFile.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    addTo();
                }
            });
            MenuItem createItem = new MenuItem("Create File");
            createItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    create();
                }
            });
            MenuItem saveItem = new MenuItem("Save File");
            saveItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    save();
                }
            });
            selectionSetColor.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameSelectionColor==null)
                        selectionColor();
                    else frameSelectionColor.show();
                }
            });
            backGroundColor.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameBackGroundColor==null)
                        backGroundColor();
                    else frameBackGroundColor.show();
                }
            });
            fontColor.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameTextColor==null)
                        textColor();
                    else frameTextColor.show();
                }
            });
            fontItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameFont==null)
                        setFont();
                    else frameFont.show();
                }
            });
            MenuItem clearItem = new MenuItem("Clear");
            clearItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    jTextArea.setText(currentCharset);
                }
            });
            MenuItem aboutItem = new MenuItem("about");
            aboutItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameAbout==null)
                        about();
                    else frameAbout.show();
                }
            });
            MenuItem charsetsItem = new MenuItem("Change charset");
            charsetsItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameChangeCharset==null)
                        changeCharsets();
                    else frameChangeCharset.show();
                }
            });
            cCompileItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameC==null){
                        cCompiling();
                    } else frameC.show();
                }
            });
            MenuItem jCompileItem = new MenuItem("Java Compiling");
            jCompileItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameJ==null){
                        javaCompiling();
                    } else frameJ.show();
                }
            });
            menuFile.getItems().addAll(createItem,loadItem,saveItem,saveAsItem,addToFile);
            menuEditing.getItems().add(clearItem);
            menuAbout.getItems().add(aboutItem);
            menuCharsets.getItems().add(charsetsItem);
            menuSettings.getItems().addAll(fontItem,fontColor,backGroundColor,selectionSetColor);
            menuCode.getItems().addAll(cCompileItem,jCompileItem);
            menuBar.getMenus().addAll(menuFile, menuEditing, menuFind,menuCode, menuCharsets, menuSettings, menuAbout);

            findItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameFind==null)
                        find();
                    else frameFind.show();
                }
            });
            replaceItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {
                    if (frameReplace==null)
                        replace();
                    else frameReplace.show();
                }
            });
            menuFind.getItems().addAll(findItem,replaceItem);
            setCenter(((jTextArea)));
    }
    private void findText(final TextArea component, final String testString) throws BadLocationException {
        if (testString.isEmpty()) {
            alert = new Alert(Alert.AlertType.WARNING);
            alert.setHeaderText("Field is empty!");
            alert.setTitle("Warning");
            alert.showAndWait();
            frameFind.close();
        }
        if ((pos = component.getText().toUpperCase().indexOf(testString.toUpperCase(),pos)) !=-1) {
            component.selectRange(pos, pos + testString.length());
            pos+=testString.length();
            }
        }
        private void alertError(String action){
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed " + action + " file");
            alert.showAndWait();
        }
        private void chooser(FileChooser chooser,TextField field,String title){
            if (chooser == null) {
                chooser = new FileChooser();
            }
            if (chooserStage == null) {
                chooserStage = new Stage();
            }
            chooser.setTitle(title);

            File selectedDirectory = chooser.showOpenDialog(chooserStage);
            if (selectedDirectory != null){
                field.setText(selectedDirectory.getAbsolutePath());
            }
        }
        private void progress(Stage stage){
            stage.setTitle("Waiting");
            stage.setResizable(false);
            FlowPane flowPane = new FlowPane();
            ImageView imageView = new ImageView(new Image("progress.gif"));
            flowPane.getChildren().add(imageView);
            stage.setScene(new Scene(flowPane,150,150));
            stage.show();
        }
    }
}