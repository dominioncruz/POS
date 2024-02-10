/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package posapplication.infotech;

import java.awt.Toolkit;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Screen;
import javafx.stage.Stage;
import posapplication.reusableFunctions.DatabaseConnection;
import posapplication.reusableFunctions.InputMethods;
import posapplication.reusableFunctions.centerScreen;
import posapplication.reusableFunctions.imageUpload;
import java.sql.ResultSet;
import javafx.scene.control.Button;
import posapplication.reusableFunctions.PasswordGenerator;
import posapplication.reusableFunctions.mailSender;
import java.sql.Time;
import java.time.LocalTime;
import posapplication.models.message;
import posapplication.reusableFunctions.timerClass;
/**
 * FXML Controller class
 *
 * @author HP PROBOOK 430 G3
 */
public class InfotechController implements Initializable {

    private final UniqueInfoTechMethods currentInfoTechMethods;
    private final centerScreen centerScreenAccess;
    private final InputMethods currentInputMethods;
    private final Map<TextField, VBox> registrationMap = new HashMap<>();
    private final Map<TextField, VBox> searchMap = new HashMap<>();
    private final Map<HBox, BorderPane> buttonPaneMap = new HashMap<>();
    private final imageUpload imageChooser;
    static DatabaseConnection databaseConnection;
    private final PasswordGenerator newPasswordGenerator;
    private final mailSender currentMailSender;
    Time scheduleTime;
    Image image = null;
    Image searchImage = null;
    timerClass timerClassToWorkWith;
    @FXML
    private Circle profile_picture;
    @FXML
    private Label userFullName;
    @FXML
    private Label userEmail;
    @FXML
    private HBox registerSideBarHBox;
    @FXML
    private HBox searchSideBarVBox;
    @FXML
    private HBox backUpSideBarHBox;
    @FXML
    private BorderPane registerBorderPane;
    @FXML
    private Label greeting;
    @FXML
    private Label mainBorderPaneDate;
    @FXML
    private VBox firstNameBorder;
    @FXML
    private TextField firstNameValue;
    @FXML
    private VBox lastNameBorder;
    @FXML
    private TextField lastNameValue;
    @FXML
    private DatePicker dateOfBirth;
    @FXML
    private VBox emailBorder;
    @FXML
    private TextField emailValue;
    @FXML
    private VBox phoneBorder;
    @FXML
    private TextField phoneValue;
    @FXML
    private ComboBox<String> genderComboBox;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private Rectangle imageComtainer;
    @FXML
    private Label sideBarDate;
    @FXML
    private StackPane userImage;
    @FXML
    private BorderPane entireScreen;
    @FXML
    private VBox invalidDetailVBox;
    @FXML
    private Label errorMessage;
    @FXML
    private ProgressIndicator loadingBar;
    @FXML
    private VBox successBox;
    @FXML
    private Label successMessage;
    @FXML
    private BorderPane searchBorderPane;
    @FXML
    private VBox firstNameBorder1;
    @FXML
    private TextField firstNameSearchvalue;
    @FXML
    private VBox lastNameBorder1;
    @FXML
    private TextField lastNameSearchValue;
    @FXML
    private DatePicker dateOfBirthSearchValue;
    @FXML
    private VBox emailBorder1;
    @FXML
    private TextField emailSearchValue;
    @FXML
    private VBox phoneBorder1;
    @FXML
    private TextField phoneSearchValue;
    @FXML
    private ComboBox<String> genderSearchValue;
    @FXML
    private ComboBox<String> roleSearchValue;
    @FXML
    private Rectangle imageComtainer2;
    @FXML
    private TextField emailSearchField;
    @FXML
    private Button resetPasswordButton;
    @FXML
    private Button updateUserPassword;
    @FXML
    private BorderPane schedulePane;
    @FXML
    private ComboBox<Integer> hourComboBox;
    @FXML
    private ComboBox<Integer> minuteComboBox;
    @FXML
    private VBox birthdayCard;
    @FXML
    private Label userNameBirthdayMessage;
    @FXML
    private VBox messagesVBox;
    @FXML
    private VBox messageBox;
    @FXML
    private Label messageTitle;
    @FXML
    private Label messageSummary;
    @FXML
    private Label messageContent;

    public InfotechController() throws ClassNotFoundException {
        this.currentInfoTechMethods = new UniqueInfoTechMethods();
        this.centerScreenAccess = new centerScreen();
        this.currentInputMethods = new InputMethods();
        this.imageChooser = new imageUpload();
        this.newPasswordGenerator = new PasswordGenerator();
        this.currentMailSender = new mailSender();
    }

    public void setData(HashMap<String, Object> data) throws SQLException, IOException, IOException, ClassNotFoundException {

        userFullName.setText((String) data.get("firstname") + " " + (String) data.get("lastname"));
        userEmail.setText((String) data.get("email"));
        greeting.setText("Hello, " + (String) data.get("firstname"));
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy");
        String formattedDate = LocalDate.now().format(dateFormatter);
        mainBorderPaneDate.setText(formattedDate.format(LocalDate.now().format(dateFormatter)));
        Image profileImage = (Image) data.get("profilePhoto");
        databaseConnection = (DatabaseConnection) data.get("databaseConnection");
        if (profileImage == null) {
            profileImage = new Image(getClass().getResource("../images/userIcon.png").toExternalForm(), 150, 150, true, true);
        }
        profile_picture.setFill(new ImagePattern(profileImage));
        //currentInfoTechMethods.initializeScheduleScreenFields();
        currentInfoTechMethods.initializeButtons(genderComboBox, roleComboBox, genderSearchValue, roleSearchValue, hourComboBox, minuteComboBox);
        currentInfoTechMethods.initializeTabs(buttonPaneMap, registerBorderPane, searchBorderPane, registerSideBarHBox, searchSideBarVBox, backUpSideBarHBox, schedulePane);
        currentInfoTechMethods.initializeInputFields(registrationMap, firstNameValue, firstNameBorder, lastNameValue, lastNameBorder, emailValue, emailBorder, phoneValue, phoneBorder);
        currentInfoTechMethods.initializeInputFields(searchMap, firstNameSearchvalue, firstNameBorder1, lastNameSearchValue, lastNameBorder1, emailSearchValue, emailBorder1, phoneSearchValue, phoneBorder1);
        scheduleTime = currentInfoTechMethods.initializeScheduleTime(userEmail.getText(), hourComboBox, minuteComboBox, databaseConnection);
        timerClassToWorkWith = new timerClass(scheduleTime, currentInfoTechMethods, this, messagesVBox);
        
        ResultSet rs = databaseConnection.getBirthdays();
        while(rs.next()){
            currentInfoTechMethods.addBirthdayToView(rs, this, messagesVBox);
        }
        
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        dateOfBirth.setValue(LocalDate.now());
        // TODO
    }

    @FXML
    private void signOut(MouseEvent event) throws IOException {
        timerClassToWorkWith.stopTimer();
        HBox logOutButton = (HBox) event.getSource();
        Stage currentStage = (Stage) logOutButton.getScene().getWindow();
        currentStage.close();
        centerScreenAccess.centerFrame(currentStage, "/posapplication/login/loginPage.fxml", databaseConnection);
    }

    @FXML
    private void registerANewUser(ActionEvent event) throws SQLException, Exception {
        currentInfoTechMethods.registerNewUser(currentInputMethods, registrationMap, emailValue, phoneValue, errorMessage, dateOfBirth, databaseConnection, firstNameValue, lastNameValue, genderComboBox, roleComboBox, successBox, entireScreen, invalidDetailVBox, successMessage, imageChooser, image, loadingBar, imageComtainer, this, messagesVBox);

    }

    @FXML
    private void togglePassword(MouseEvent event) {
    }

    @FXML
    private void openWebcam(ActionEvent event) throws IOException {
        entireScreen.setDisable(true);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/posapplication/infotech/Camera.fxml"));
        Parent root = loader.load();
        Stage newStage = new Stage();
        newStage.setResizable(false);
        Scene scene = new Scene(root);
        newStage.setScene(scene);
        newStage.setTitle("Image Capture");
        newStage.setFullScreen(false);
        newStage.setOnCloseRequest(actionEvent -> {
            newStage.close();
            entireScreen.setDisable(false);
        });
        newStage.show();
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        newStage.setX(100);
        newStage.setY((screenBounds.getHeight() - newStage.getHeight()) / 2);
    }

    @FXML
    private void selectProfilePhoto(ActionEvent event) throws ClassNotFoundException, SQLException {
        try {
            image = imageChooser.uploadFile(imageComtainer);
        } catch (IOException ex) {
            Logger.getLogger(InfotechController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void closeWarningBox(ActionEvent event) {
        entireScreen.setDisable(false);
        invalidDetailVBox.setVisible(false);
        successBox.setVisible(false);
    }

    @FXML
    private void searchForUser(ActionEvent event) throws SQLException {
        ResultSet rs = DatabaseConnection.checkIfUserExists(emailSearchField.getText(), "SELECT * FROM personal_details WHERE email = ?");
        if (rs.next()) {
            firstNameSearchvalue.setText(rs.getString("first_name"));
            lastNameSearchValue.setText(rs.getString("last_name"));
            phoneSearchValue.setText(rs.getString("phone_number"));
            LocalDate currentDate = new java.sql.Date(rs.getDate("date_of_birth").getTime()).toLocalDate();
            dateOfBirthSearchValue.setValue(currentDate);
            genderSearchValue.setValue(rs.getString("gender"));
            roleSearchValue.setValue(rs.getString("role"));
            emailSearchValue.setText(rs.getString("email"));
            resetPasswordButton.setDisable(false);
            updateUserPassword.setDisable(false);
            java.sql.Blob columnValue = rs.getBlob("profile_photo");

            if (columnValue != null) {
                byte[] imageData = columnValue.getBytes(1, (int) columnValue.length());
                searchImage = new Image(new ByteArrayInputStream(imageData));

                imageComtainer2.setFill(new ImagePattern(searchImage));
            } else {
                System.out.println("Profile photo is null.");
            }
        } else {
            errorMessage.setText("No user found");
            invalidDetailVBox.setVisible(true);
            Toolkit.getDefaultToolkit().beep();
            entireScreen.setDisable(true);
        }
    }

    @FXML
    private void resetUserPassword(ActionEvent event) throws Exception {
        String newPassword = newPasswordGenerator.generatePassword();
        databaseConnection.updatePasswprd(newPassword, emailSearchField.getText());
        currentMailSender.resetPasswordMail(firstNameSearchvalue.getText(), emailSearchField.getText(), newPassword);
        successMessage.setText("Password reset successful");
        successBox.setVisible(true);
        entireScreen.setDisable(false);
        currentInfoTechMethods.resetFields(firstNameSearchvalue, lastNameSearchValue, emailSearchValue, phoneSearchValue, dateOfBirthSearchValue, genderSearchValue, roleSearchValue, searchImage, imageComtainer2, emailSearchField, resetPasswordButton, updateUserPassword);
    }

    @FXML
    private void openWebcamForUpdate(ActionEvent event) {
    }

    @FXML
    private void selectProfilePhotoForUpdate(ActionEvent event) throws ClassNotFoundException, SQLException {
        try {
            searchImage = imageChooser.uploadFile(imageComtainer2);
        } catch (IOException ex) {
            Logger.getLogger(InfotechController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void togglePage(MouseEvent event) {

        for (HBox button : buttonPaneMap.keySet()) {
            button.getStyleClass().remove("selected_tab");
            buttonPaneMap.get(button).setVisible(false);
            if (event.getSource() == button) {
                button.getStyleClass().add("selected_tab");
                buttonPaneMap.get(button).setVisible(true);
            }

        }
    }

    @FXML
    private void updateUserInformation(ActionEvent event) throws Exception {
        currentInfoTechMethods.updateUser(currentInputMethods, searchMap, emailSearchValue, phoneSearchValue, errorMessage, dateOfBirthSearchValue, databaseConnection, firstNameSearchvalue, lastNameSearchValue, genderSearchValue, roleSearchValue, successBox, entireScreen, invalidDetailVBox, successMessage, imageChooser, searchImage, loadingBar, imageComtainer2, emailSearchField, resetPasswordButton, updateUserPassword, this, messagesVBox);

    }

    @FXML
    private void scheduleBackup(ActionEvent event) throws SQLException {
        int hourValue = hourComboBox.getValue();
        int minuteValue = minuteComboBox.getValue();
        currentInfoTechMethods.updateTime(hourValue, minuteValue, successBox, successMessage, errorMessage, invalidDetailVBox, databaseConnection, userEmail.getText(), loadingBar, entireScreen, scheduleTime, timerClassToWorkWith, messagesVBox, this);
        
    }
    
    public void showMessage(message currentMessage, String email){
        birthdayCard.setVisible(false);
        messageBox.setVisible(false);
        
        if(email.equals(userEmail.getText())){
            birthdayCard.setVisible(true);
        }else{
            messageTitle.setText(currentMessage.getTitle());
            messageSummary.setText(currentMessage.getSummary());
            messageContent.setText(currentMessage.getContent());
            messageBox.setVisible(true);
        }
        
    }
    
      public void showInfoOtherMessage(message currentMessage){
        birthdayCard.setVisible(false);
        messageBox.setVisible(false);
        
        {
            messageTitle.setText(currentMessage.getTitle());
            messageSummary.setText(currentMessage.getSummary());
            messageContent.setText(currentMessage.getContent());
            messageBox.setVisible(true);
        }
        
    }

    @FXML
    private void closeMessages(MouseEvent event) {
        birthdayCard.setVisible(false);
        messageBox.setVisible(false);
    }

}
