/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pain.t.fx;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

/**
 *
 * @author Erik
 */
public class PainTFX extends Application {

    Image image;

    @Override
    public void start(Stage primaryStage) {
        
        GridPane root = new GridPane();
        Scene scene = new Scene(root, 300, 250);
        
        Menu menu1 = new Menu("File");
        Menu menu2 = new Menu("Would You Like To End The Pain Sir?");
        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().add(menu1);
        menuBar.getMenus().add(menu2);
        MenuItem menu12 = new MenuItem("Open");
        MenuItem menu13 = new MenuItem("Save");
        MenuItem menu22 = new MenuItem("Closing On Command");
        menu1.getItems().add(menu12);
        menu1.getItems().add(menu13);
        menu2.getItems().add(menu22);

        menu12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
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
                ImageView saveView = new ImageView();
                saveView.setImage(image);
                // Obtain PixelReader
                PixelReader pixelReader = image.getPixelReader();
                // Create WritableImage
                WritableImage wImage = new WritableImage( pixelReader,
                        (int) image.getWidth(),
                        (int) image.getHeight()); 

                // Write wImage to file system as a .png image

                saveBtn.setOnAction(new EventHandler<ActionEvent>() {

                    @Override
                    public void handle(ActionEvent event) {
                        
                        
                        try {
                    File outFile = new File(saveInput.getText());
                    ImageIO.write(SwingFXUtils.fromFXImage(wImage, null),
                            "png", outFile);
                } catch (Exception ex) {
                    //error message
                }
                        root.getChildren().add(menuBar);
                        primaryStage.setScene(scene);
                        primaryStage.show();
                        
                    }
                });
            }
        });

        menu22.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent e) {
                Platform.exit();
            }
        });

        root.getChildren().add(menuBar);

        primaryStage.setTitle("Pain(t)");
        primaryStage.setScene(scene);

        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
