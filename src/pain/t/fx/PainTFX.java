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
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
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

    //global variables
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
    MenuBar menuBar;
    Scene scene;
    Menu menu1, menu2, menu3, menu4;
    MenuItem menu12, menu13, menu14, menu22, menu32, menu42, menu43, menu44;

    //@Override
    public void start(Stage primaryStage) {

        //initialize panes and scene. this is not in an init() because it is expected to change
        border = new BorderPane();
        sp = new ScrollPane();
        imageWidth = 400.0;
        imageHeight = 400.0;
        sp.setPrefSize(imageWidth, imageHeight);
        sp.setMaxSize(800, 800);
        windowSp = new ScrollPane();
        windowSp.setPrefSize(500, 500);
        scene = new Scene(windowSp);
        //Remove previous canvas, set canvas to imageWidth/Height, 
        //set canvas to scrollpane, autosize scrollpane, autosize window scrollpane,
        //add eventhandler for mouse dragged to create line  
        canvasInit();
        //Set basic properties of slider like amount of ticks and tick increment
        lengthsliderInit();
        //Set default color and create listener for color picking
        colorPickerInit();
        //Create system of menus and menuItems
        menuInit();
        //menuItem eventHandlers start here

        //eH for file/open, sets fileDest, sets image, sets imageWidth/Height,
        //calls file chooser and calls canvas init
        menu12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                fileChooser.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.gif"));
                FileInputStream imageStream;
                try {
                    File file = fileChooser.showOpenDialog(primaryStage);
                    fileDest = file.getPath();
                    imageStream = new FileInputStream(fileDest);
                    image = new Image(imageStream);
                    imageWidth = image.getWidth();
                    imageHeight = image.getHeight();
                    canvasInit();
                    gc.drawImage(image, 0, 0);
                    sp.setContent(canvas);

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
        //eH for file/saveAs, creates new scene and adds menubar, sets fileDest,
        //takes snapshot of canvas and saves it to fileDest as a png,
        //closes by changing scene and moving menubar
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
        //eH for file/save, uses same method as file/saveAs with fileDest already set by open or saveAs
        //should have an error message that states if a file is unsaved
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
        //eH for close menuItem
        menu22.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Platform.exit();
            }
        });
        //eH for navigator, sets scene and moves menubar
        menu32.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                border.setTop(menuBar);
                primaryStage.setScene(scene);
                primaryStage.show();
            }
        });
        //eH for About dialog
        menu42.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("About");
                alert.setHeaderText("Information Alert");
                String s = "Product Version: Pain(t)FX 3.1.0\n"
                        + "Java JDK 8\n"
                        + "Compiled on Netbeans IDE 12.0\n"
                        + "\n";
                alert.setContentText(s);
                alert.show();
            }
        });
        //eH for OnlineDocs&Support dialog
        menu43.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Online Docs and Support");
                alert.setHeaderText("Information Alert");
                String s = "Youtube Demo: https://youtu.be/mHzZLGYWiYI \n "
                        + "Github Code: https://github.com/ErikJohnson-2/Pain-t-FX \n"
                        + "Oracle Documentation: https://docs.oracle.com/ \n"
                        + "Email: erik.johnson2@valpo.edu \n";
                alert.setContentText(s);
                alert.show();
            }
        });
        //eH for UsingPaint dialog
        menu44.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Using Paint(t)");
                alert.setHeaderText("Information Alert");
                String s = "Open and Save images with the Menu. Modify Images by clicking and dragging the cursor "
                        + "over the image. You can can change the line length with the slider and the line color with the Color Chooser."
                        + "Navigator can help you get back to the image editor from the file options";
                alert.setContentText(s);
                alert.show();
            }
        });
        //MenuItem listeners end here

        //add all panes and nodes into stage and display stage
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

    public void lengthsliderInit() {
        slider = new Slider(1, 10, 2);
        slider.setShowTickMarks(true);
        slider.setShowTickLabels(true);
        slider.setMajorTickUnit(0.25f);
        slider.setBlockIncrement(0.1f);
    }

    public void colorPickerInit() {
        colorPicker = new ColorPicker();
        colorPicker.setValue(Color.BLUE);
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                gc.setFill(colorPicker.getValue());
            }
        });
    }

    public void menuInit() {
        menu1 = new Menu("File");
        menu2 = new Menu("Would You Like To End The Pain Sir?");
        menu3 = new Menu("Navigator");
        menu4 = new Menu("Help");
        menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);
        menuBar.getMenus().add(menu2);
        menuBar.getMenus().add(menu3);
        menuBar.getMenus().add(menu4);
        menu12 = new MenuItem("Open");
        menu13 = new MenuItem("Save As");
        menu14 = new MenuItem("Save"); //no event handler added for home
        menu22 = new MenuItem("Closing On Command");
        menu32 = new MenuItem("Home"); //temporary event handler added for home
        menu42 = new MenuItem("About"); //temporary event handler added for about
        menu43 = new MenuItem("Online Docs and Support"); //no event handler added for home
        menu44 = new MenuItem("Using Paint(t)"); //no event handler added for home
        menu1.getItems().add(menu12);
        menu1.getItems().add(menu13);
        menu1.getItems().add(menu14);
        menu2.getItems().add(menu22);
        menu3.getItems().add(menu32);
        menu4.getItems().add(menu42);
        menu4.getItems().add(menu43);
        menu4.getItems().add(menu44);

    }

    public static void main(String[] args) {
        launch(args);
    }
}
