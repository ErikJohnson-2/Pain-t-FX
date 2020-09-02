/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pain.t.fx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import javafx.scene.control.ScrollBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;

/**
 *
 * @author Erik
 */
public class PainTFX extends Application {

    Image image;
    Canvas canvas;
    ColorPicker colorPicker;
    String fileDest;
    GraphicsContext gc;
    Slider slider;
    Double imageWidth;
    Double imageHeight;
    BorderPane border;
    ScrollPane sp;
    ScrollPane windowSp;

    //@Override
    public void start(Stage primaryStage) {

        border = new BorderPane();
        sp = new ScrollPane();
        imageWidth = 400.0;
        imageHeight = 400.0;
        sp.setPrefSize(imageWidth, imageHeight);
        sp.setMaxSize(800, 800);
        windowSp = new ScrollPane();
        windowSp.setPrefSize(500, 500);

        //GridPane root = new GridPane();
        Scene scene = new Scene(windowSp);
        //canvas = new Canvas(300, 250);
        //gc = canvas.getGraphicsContext2D();

        canvasInit();

        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLUE);
        //root.getChildren().add(canvas);
        slider = new Slider(1, 10, 2);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.25f);
        slider.setBlockIncrement(0.1f);

        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                gc.setFill(colorPicker.getValue());
            }
        });
        /*
        #
        CANVAS INIT
         
        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                gc.setFill(colorPicker.getValue());
                gc.fillRect(e.getX(), e.getY(), slider.getValue(), slider.getValue());
            }
        });
        #
         */

        //root.getChildren().add(sp);
        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Would You Like To End The Pain Sir?");
        Menu menu3 = new Menu("Navigator");
        Menu menu4 = new Menu("Help");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);
        menuBar.getMenus().add(menu2);
        menuBar.getMenus().add(menu3);
        menuBar.getMenus().add(menu4);
        MenuItem menu12 = new MenuItem("Open");
        MenuItem menu13 = new MenuItem("Save As");
        MenuItem menu14 = new MenuItem("Save"); //no event handler added for home
        MenuItem menu22 = new MenuItem("Closing On Command");
        MenuItem menu32 = new MenuItem("Home"); //temporary event handler added for home
        MenuItem menu42 = new MenuItem("About"); //temporary event handler added for about
        MenuItem menu43 = new MenuItem("Online Docs and Support"); //no event handler added for home
        MenuItem menu44 = new MenuItem("Using Paint(t)"); //no event handler added for home

        menu1.getItems().add(menu12);
        menu1.getItems().add(menu13);
        menu1.getItems().add(menu14);
        menu2.getItems().add(menu22);
        menu3.getItems().add(menu32);
        menu4.getItems().add(menu42);
        menu4.getItems().add(menu43);
        menu4.getItems().add(menu44);

        menu12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                FileInputStream imageStream;
                try {

                    //imageStream = new FileInputStream(fileChooser.showOpenDialog(primaryStage));
                    File file = fileChooser.showOpenDialog(primaryStage);
                    fileDest = file.getPath();
                    imageStream = new FileInputStream(fileDest);
                    image = new Image(imageStream);
                    System.out.print(fileDest);

                    imageWidth = image.getWidth();
                    imageHeight = image.getHeight();

                    //canvas.resize(image.getWidth(), image.getHeight());
                    //canvas = new Canvas(imageWidth, imageHeight);
                    //gc = canvas.getGraphicsContext2D();
                    //sp.getChildrenUnmodifiable().remove(canvas);
                    canvasInit();
                    gc.drawImage(image, 0, 0);
                    sp.setContent(canvas);

                    //300, 250, image.getWidth(),image.getHeight());  
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PainTFX.class.getName()).log(Level.SEVERE, null, ex);
                    ButtonType exitButtonType = new ButtonType("Exit", ButtonData.OK_DONE);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.getDialogPane().getButtonTypes().add(exitButtonType);
                    boolean disabled = false; // computed based on content of text fields, for example
                    alert.getDialogPane().lookupButton(exitButtonType).setDisable(disabled);
                    alert.setTitle("Error");
                    alert.setHeaderText("File Error");
                    alert.setContentText("File not Found");

                }

            }
        });

        menu13.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                VBox saveLayout = new VBox(20);
                Scene saveScene = new Scene(saveLayout, 300, 250);
                saveLayout.getChildren().add(menuBar);

                Button saveBtn = new Button();
                saveBtn.setText("Click When Ready");

                TextField saveInput = new TextField();
                saveInput.setText("Please Input Where Your New File Will Be Saved: C:\\\\Users\\\\MyName\\\\filename.png");

                saveLayout.getChildren().add(saveBtn);
                saveLayout.getChildren().add(saveInput);

                primaryStage.setScene(saveScene);
                primaryStage.show();

                // Create Image and ImageView objects
                //ImageView saveView = new ImageView();
                //saveView.setImage(image);
                // Obtain PixelReader
                //PixelReader pixelReader = canvas.getPixelReader();
                // Create WritableImage
                WritableImage wImage = new WritableImage(
                        (int) canvas.getWidth(),
                        (int) canvas.getHeight());

                canvas.snapshot(null, wImage);

                // Write wImage to file system as a .png image
                saveBtn.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {

                        try {
                            fileDest = saveInput.getText();
                            File outFile = new File(fileDest);
                            ImageIO.write(SwingFXUtils.fromFXImage(wImage, null),
                                    "png", outFile);
                        } catch (Exception ex) {
                            //error message
                        }
                        border.setTop(menuBar);
                        primaryStage.setScene(scene);
                        primaryStage.show();

                    }
                });
            }
        });

        menu14.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                try {
                    WritableImage wImage = new WritableImage(
                            (int) canvas.getWidth(),
                            (int) canvas.getHeight());

                    canvas.snapshot(null, wImage);
                    System.out.print(fileDest);
                    File outFile = new File(fileDest);
                    ImageIO.write(SwingFXUtils.fromFXImage(wImage, null),
                            "png", outFile);
                } catch (Exception ex) {
                    //error message
                }
            }
        });

        menu22.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Platform.exit();
            }
        });
        menu32.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                border.setTop(menuBar);
                //root.getChildren().add(menuBar);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        menu42.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("About");
                alert.setHeaderText("Information Alert");
                String s = "This is an example of JavaFX 8 Dialogs... ";
                alert.setContentText(s);
                alert.show();
            }
        });
        menu43.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Online Docs and Support");
                alert.setHeaderText("Information Alert");
                String s = "This is an example online docs blah blah insert code... ";
                alert.setContentText(s);
                alert.show();
            }
        });
        menu44.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {

                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Using Paint(t)");
                alert.setHeaderText("Information Alert");
                String s = "how to use paint... ";
                alert.setContentText(s);
                alert.show();
            }
        });

        //root.getChildren().add(slider);
        //root.getChildren().addAll(colorPicker);
        //root.getChildren().add(menuBar);
        border.setTop(menuBar);
        border.setCenter(sp);
        border.setBottom(colorPicker);
        border.setRight(slider);

        windowSp.setContent(border);

        primaryStage.setTitle("Pain(t)");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void canvasInit() {

        sp.getChildrenUnmodifiable().remove(canvas);
        canvas = new Canvas(imageWidth, imageHeight);
        gc = canvas.getGraphicsContext2D();
        sp.setContent(canvas);
        //canvas.autosize();
        sp.setPrefSize(imageWidth, imageHeight);
        sp.autosize();
        windowSp.autosize();

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent e) {
                gc.setFill(colorPicker.getValue());
                gc.fillRect(e.getX(), e.getY(), slider.getValue(), slider.getValue());
            }
        });

    }

    public static void main(String[] args) {
        launch(args);
    }

}

//root.getChildren().add(canvas);
/*
                
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                FileInputStream imageStream;
                try {
                    imageStream = new FileInputStream(fileChooser.showOpenDialog(primaryStage));
                    image = new Image(imageStream);

                    // resizes the image to have width of 100 while preserving the ratio and using
                    // higher quality filtering method; this ImageView is also cached to
                    // improve performance
                    ImageView iv = new ImageView();
                    iv.setImage(image);
                    iv.setFitWidth(300);
                    iv.setPreserveRatio(true);
                    iv.setSmooth(true);
                    iv.setCache(true);

                    
                    root.add(iv, 3, 3);
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(PainTFX.class.getName()).log(Level.SEVERE, null, ex);
                    //print error statement?
                }

 */
