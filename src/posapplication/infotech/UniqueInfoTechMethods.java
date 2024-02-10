/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.infotech;

import java.awt.Toolkit;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import posapplication.reusableFunctions.DatabaseConnection;
import posapplication.reusableFunctions.InputMethods;
import java.sql.ResultSet;
import java.util.stream.Collectors;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Rectangle;
import posapplication.reusableFunctions.imageUpload;
import posapplication.reusableFunctions.mailSender;
import java.sql.Time;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXMLLoader;
import posapplication.components.message.MessageController;
import posapplication.models.message;
import posapplication.reusableFunctions.timerClass;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class UniqueInfoTechMethods {

    private final mailSender mailSender = new mailSender();

    public void initializeButtons(
            ComboBox<String> genderComboBox,
            ComboBox<String> roleComboBox,
            ComboBox<String> gender2ComboBox,
            ComboBox<String> role2ComboBox,
            ComboBox<Integer> hoursComboBox,
            ComboBox<Integer> minutesComboBox
    ) {
        genderComboBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        roleComboBox.setItems(FXCollections.observableArrayList("Manager", "Cashier", "Info Tech", "Inventory"));
        gender2ComboBox.setItems(FXCollections.observableArrayList("Male", "Female"));
        role2ComboBox.setItems(FXCollections.observableArrayList("Manager", "Cashier", "Info Tech", "Inventory"));
        genderComboBox.setValue("Male");
        roleComboBox.setValue("Manager");
        gender2ComboBox.setValue("Male");
        role2ComboBox.setValue("Manager");
        minutesComboBox.setItems(FXCollections.observableArrayList(Arrays.asList(0, 15, 30, 45, 60)));
        int[] array = new int[24];
        for (int i = 0; i < 24; i++) {
            array[i] = i;
        }

        // Explicitly specify the type when creating observableArrayList
        hoursComboBox.setItems(FXCollections.observableArrayList(Arrays.stream(array).boxed().collect(Collectors.toList())));

    }

    public void initializeInputFields(
            Map<TextField, VBox> registrationMap,
            TextField firstNameValue,
            VBox firstNameBorder,
            TextField lastNameValue,
            VBox lastNameBorder,
            TextField emailValue,
            VBox emailBorder,
            TextField phoneValue,
            VBox phoneBorder
    ) {
        registrationMap.put(firstNameValue, firstNameBorder);
        registrationMap.put(lastNameValue, lastNameBorder);
        registrationMap.put(emailValue, emailBorder);
        registrationMap.put(phoneValue, phoneBorder);
    }

    public void registerNewUser(
            InputMethods currentInputMethods,
            Map<TextField, VBox> warningIndividualMap,
            TextField emailInput,
            TextField phoneNumberInput,
            Label errorMessage,
            DatePicker birthDatePicker,
            DatabaseConnection currentConnection,
            TextField firstNameInput,
            TextField lastNameInput,
            ComboBox<String> genderComboBox,
            ComboBox<String> roleComboBox,
            VBox successDialogBox,
            BorderPane fromContainer,
            VBox errorDialogBox,
            Label successMessage,
            imageUpload imageChooser,
            Image profile_photo,
            ProgressIndicator loadingBar,
            Rectangle userImage,
            InfotechController infotechcontroller,
            VBox messagesContainer
    ) throws SQLException, Exception {

        try {
            loadingBar.setVisible(true);
            boolean metRequirements = true;

            fromContainer.setDisable(true);

            for (TextField input : warningIndividualMap.keySet()) {
                if (input.getText().isEmpty()) {
                    metRequirements = false;
                }
            }
            String email = emailInput.getText().toLowerCase();

            if (metRequirements) {
                var phoneNumber = phoneNumberInput.getText();
                if (phoneNumber.length() != 11) {
                    errorMessage.setText("Invalid phone number");
                    metRequirements = false;
                } else if (birthDatePicker.getValue().isAfter(LocalDate.now())) {
                    metRequirements = false;
                    errorMessage.setText("You cannot set future dates");
                } else if (profile_photo == null) {
                    metRequirements = false;
                    errorMessage.setText("No picture uploaded");
                } else {
                    ResultSet rs = DatabaseConnection.checkIfUserExists(email, "SELECT * FROM personal_details WHERE email = ?");
                    if (rs.next()) {
                        metRequirements = false;
                        errorMessage.setText("User already exists");
                    } else {
                        String dataEntryState = currentConnection.insertUserIntoDatabase(firstNameInput.getText().toLowerCase(), lastNameInput.getText().toLowerCase(), phoneNumber, birthDatePicker.getValue(), genderComboBox.getValue(), roleComboBox.getValue(), email, profile_photo, "INSERT INTO personal_details values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                        if (dataEntryState == null) {
                            metRequirements = false;
                            errorMessage.setText("An occured somewhere");
                        } else {
                            successMessage.setText("User registered successfully");
                            successDialogBox.setVisible(true);
                            addNewInfoToMessagses("Registration", "New user registered", "You created a new user: " + firstNameInput.getText() + " " + lastNameInput.getText(), infotechcontroller, messagesContainer);
                            mailSender.sendMail(firstNameInput.getText(), email, dataEntryState, roleComboBox.getValue());
                            resetFields(firstNameInput, lastNameInput, emailInput, phoneNumberInput, birthDatePicker, genderComboBox, roleComboBox, profile_photo, userImage);
                        }
                    }
                }
            } else {
                errorMessage.setText("Please enter all fields");
            }

            if (!metRequirements) {
                errorDialogBox.setVisible(true);
                Toolkit.getDefaultToolkit().beep();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            loadingBar.setVisible(false);
        }

    }

    public void resetFields(
            TextField firstNameInput,
            TextField lastNameInput,
            TextField emailInput,
            TextField phoneNumberInput,
            DatePicker birthDatePicker,
            ComboBox<String> genderComboBox,
            ComboBox<String> roleComboBox,
            Image image,
            Rectangle userImage
    ) {
        firstNameInput.setText("");
        lastNameInput.setText("");
        emailInput.setText("");
        phoneNumberInput.setText("");
        birthDatePicker.setValue(LocalDate.now());
        genderComboBox.setValue("Male");
        roleComboBox.setValue("Manager");
        image = null;
        userImage.setFill(null);
    }

    public void initializeTabs(
            Map<HBox, BorderPane> buttonPaneMap,
            BorderPane registerBorderPane,
            BorderPane searchBorderPane,
            HBox registerSideBarHBox,
            HBox searchSideBarVBox,
            HBox scheduleSideHBox,
            BorderPane scheduleSideScreen
    ) {
        buttonPaneMap.put(registerSideBarHBox, registerBorderPane);
        buttonPaneMap.put(searchSideBarVBox, searchBorderPane);
        buttonPaneMap.put(scheduleSideHBox, scheduleSideScreen);
    }

    public void resetFields(
            TextField firstNameInput,
            TextField lastNameInput,
            TextField emailInput,
            TextField phoneNumberInput,
            DatePicker birthDatePicker,
            ComboBox<String> genderComboBox,
            ComboBox<String> roleComboBox,
            Image image,
            Rectangle userImage,
            TextField searchValue,
            Button resetButton,
            Button updateButton
    ) {
        firstNameInput.setText("");
        lastNameInput.setText("");
        emailInput.setText("");
        phoneNumberInput.setText("");
        birthDatePicker.setValue(LocalDate.now());
        searchValue.setText("");
        genderComboBox.setValue("Male");
        roleComboBox.setValue("Manager");
        image = null;
        userImage.setFill(null);
        resetButton.setDisable(true);
        updateButton.setDisable(true);
    }

    public void updateUser(
            InputMethods currentInputMethods,
            Map<TextField, VBox> warningIndividualMap,
            TextField emailInput,
            TextField phoneNumberInput,
            Label errorMessage,
            DatePicker birthDatePicker,
            DatabaseConnection currentConnection,
            TextField firstNameInput,
            TextField lastNameInput,
            ComboBox<String> genderComboBox,
            ComboBox<String> roleComboBox,
            VBox successDialogBox,
            BorderPane fromContainer,
            VBox errorDialogBox,
            Label successMessage,
            imageUpload imageChooser,
            Image profile_photo,
            ProgressIndicator loadingBar,
            Rectangle userImage,
            TextField queryEmail,
            Button resetButton,
            Button updateButton,
            InfotechController infotechcontroller,
            VBox messagesContainer
    ) throws SQLException, Exception {

        try {
            loadingBar.setVisible(true);
            boolean metRequirements = true;

            fromContainer.setDisable(true);

            for (TextField input : warningIndividualMap.keySet()) {
                if (input.getText().isEmpty()) {
                    metRequirements = false;
                }
            }
            String email = emailInput.getText().toLowerCase();

            if (metRequirements) {
                var phoneNumber = phoneNumberInput.getText();
                if (phoneNumber.length() != 11) {
                    errorMessage.setText("Invalid phone number");
                    metRequirements = false;
                } else if (birthDatePicker.getValue().isAfter(LocalDate.now())) {
                    metRequirements = false;
                    errorMessage.setText("You cannot set future dates");
                } else if (profile_photo == null) {
                    metRequirements = false;
                    errorMessage.setText("No picture uploaded");
                } else {
                    boolean dataEntryState = currentConnection.updateUserInDatabase(firstNameInput.getText().toLowerCase(), lastNameInput.getText().toLowerCase(), phoneNumber, birthDatePicker.getValue(), genderComboBox.getValue(), roleComboBox.getValue(), email, profile_photo, "UPDATE personal_details SET first_name = ?, last_name = ?, date_of_birth = ?, phone_number = ?, email = ?, gender = ?, profile_photo = ?, role = ? WHERE email = ?");
                    if (dataEntryState == false) {
                        metRequirements = false;
                        errorMessage.setText("An occured somewhere");
                    } else {
                        successMessage.setText("User data updated successfully");
                        successDialogBox.setVisible(true);
                        addNewInfoToMessagses("Update", "User Info updated", "You updated user data: " + firstNameInput.getText() + " " + lastNameInput.getText(), infotechcontroller, messagesContainer);
                        queryEmail.setText("");
                        resetButton.setDisable(true);
                        updateButton.setDisable(true);
                        resetFields(firstNameInput, lastNameInput, emailInput, phoneNumberInput, birthDatePicker, genderComboBox, roleComboBox, profile_photo, userImage);
                    }

                }
            } else {
                errorMessage.setText("Please enter all fields");
            }

            if (!metRequirements) {
                errorDialogBox.setVisible(true);
                Toolkit.getDefaultToolkit().beep();
            }
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            loadingBar.setVisible(false);
        }

    }

    public Time initializeScheduleTime(String email, ComboBox<Integer> hour, ComboBox<Integer> minute, DatabaseConnection databaseConnection) throws SQLException {
        Time backupTime = databaseConnection.selectScheduleTime(email);
        int hourValue = 0;
        int minuteValue = 0;
        if(backupTime == null){
            hourValue = 16;
            minuteValue = 30;
        }else{
            hourValue = backupTime.getHours();
            minuteValue = backupTime.getMinutes();
        }
        
        hour.setValue(hourValue);
        minute.setValue(minuteValue);
        return  backupTime;
    }
    
    public void updateTime(int hourValue, int minuteValue, VBox successBox, Label successMessage, Label errorMessage, VBox invalidDetailVBox, DatabaseConnection databaseConnection, String email, ProgressIndicator loadingBar, BorderPane entireScreen, Time scheduleTime, timerClass timerObject, VBox messagesVBox, InfotechController infotechController) throws SQLException{
        Time time = new Time(hourValue, minuteValue, 0);
        
        try{
            entireScreen.setDisable(true);
            loadingBar.setVisible(true);
            boolean bool = databaseConnection.updateTimeForSchedule(time, email);
            if(bool){
                successMessage.setText("Backup scheduled for " + time);
                scheduleTime = time;
                timerObject.updateTimerValue(time);
                successBox.setVisible(true);
                addNewInfoToMessagses("Backup", "Backup schedule", "You scheduled a data backup for time: " + time, infotechController, messagesVBox);
            }else{
                errorMessage.setText("An error occured somewhere");
                invalidDetailVBox.setVisible(true);
            }
        }catch(Exception e){
            errorMessage.setText("An error occured somewhere");
            invalidDetailVBox.setVisible(true);
        }finally{
            loadingBar.setVisible(false);
        }
        
    }
    
    public void addBirthdayToView(
            ResultSet rs,
            InfotechController infoTechController,
            VBox messagesContainer
    ) throws SQLException, IOException {
        
        
    
        LocalTime time = LocalTime.now();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Format the LocalTime object using the formatter
        String formattedTime = time.format(formatter);
        
        String title = "Birthday" ;
        String summary = rs.getString("first_name") + "'s birthday";
        String content = "Today is " + rs.getString("first_name") + "'s birthday, do wish them a happy birthday";
        
        message currentMessage = new message();

        currentMessage.setTime(formattedTime);
        currentMessage.setTitle(title);
        currentMessage.setSummary(summary);
        currentMessage.setContent(content);

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../components/message/message.fxml"));
            HBox currentMessageHBox = fxmlloader.load();
            MessageController currentMessageController = fxmlloader.getController();
            currentMessageController.setInfoTechControllerReference(infoTechController);
            currentMessageController.setData(currentMessage, rs.getString("email"));
            currentMessageHBox.getProperties().put("controller", currentMessageController);

            currentMessageHBox.setOnMouseClicked(event -> {
                    currentMessageController.openMessage(currentMessage);
            });
            messagesContainer.getChildren().add(currentMessageHBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void addNewInfoToMessagses(
            String title,
            String summary,
            String content,
            InfotechController infoTechController,
            VBox messagesContainer
    ) throws SQLException, IOException {
        
        
    
        LocalTime time = LocalTime.now();
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");

        // Format the LocalTime object using the formatter
        String formattedTime = time.format(formatter);
        
        message currentMessage = new message();

        currentMessage.setTime(formattedTime);
        currentMessage.setTitle(title);
        currentMessage.setSummary(summary);
        currentMessage.setContent(content);

        try {
            FXMLLoader fxmlloader = new FXMLLoader(getClass().getResource("../components/message/message.fxml"));
            HBox currentMessageHBox = fxmlloader.load();
            MessageController currentMessageController = fxmlloader.getController();
            currentMessageController.setInfoTechControllerReference(infoTechController);
            currentMessageController.setData(currentMessage, null);
            currentMessageHBox.getProperties().put("controller", currentMessageController);

            currentMessageHBox.setOnMouseClicked(event -> {
                    currentMessageController.openInfoTechNotification(currentMessage);
            });
            messagesContainer.getChildren().add(currentMessageHBox);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
