/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class imageUpload {
    public Image uploadFile(Rectangle profile_photo) throws ClassNotFoundException, SQLException, IOException{
        Stage currentStage = (Stage) profile_photo.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Upload new profile photo");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("JPEG Images", "*.jpg"),
            new FileChooser.ExtensionFilter("PNG Images", "*.png"),
            new FileChooser.ExtensionFilter("All images", "*.jpg", "*.png")
        );
        File selectedFile = fileChooser.showOpenDialog(currentStage);

        if (selectedFile != null) {
            String imagePath = selectedFile.getAbsolutePath();
            Image image = new Image("file:" + imagePath, 720, 720, true, true);
            profile_photo.setFill(new ImagePattern(image));
            return image;
        } else {
            System.out.println("No selected file");
        }
        return null;
    }
    
    public boolean isImageFill(Rectangle rectangle) {
        return rectangle.getFill() instanceof ImagePattern;
    }
}
