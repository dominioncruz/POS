package posapplication.login;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.awt.Toolkit;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import posapplication.reusableFunctions.DatabaseConnection;
import posapplication.reusableFunctions.InputMethods;
import posapplication.reusableFunctions.UserPreferences;
import java.sql.Connection;
import java.sql.SQLException;
import javafx.stage.Stage;
import posapplication.reusableFunctions.PasswordHashing;
import posapplication.reusableFunctions.centerScreen;

public class LoginPageController implements Initializable {

    @FXML
    private AnchorPane entirePage;
    @FXML
    private VBox loginPart;
    @FXML
    private VBox invalidDetailVBox;
    @FXML
    private VBox emailBorder;
    @FXML
    private TextField emailValue;
    @FXML
    private VBox passwordBorder;
    @FXML
    private PasswordField passwordValue;
    @FXML
    private FontAwesomeIconView passwordVisibility;
    @FXML
    private TextField passwordTextForm;

    private final UserPreferences currentPreferences;
    private final InputMethods currentInputMethods;
    private final centerScreen centerScreenInstance;
    static DatabaseConnection databaseConnection;

    @FXML
    private CheckBox saveState;
    @FXML
    private ProgressIndicator loadingBar;

    public LoginPageController() throws ClassNotFoundException {
        this.currentPreferences = new UserPreferences();
        this.currentInputMethods = new InputMethods();
        this.centerScreenInstance = new centerScreen();
    }

    public void setData(DatabaseConnection data) {
        databaseConnection = data;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (currentPreferences.getStatus() == false) {
            saveState.setSelected(false);
        } else {
            saveState.setSelected(true);
            emailValue.setText(currentPreferences.getSavedEmail());
        }
    }

    @FXML
    private void loginUser(ActionEvent event) throws SQLException, Exception {

        try {

            boolean canCheck = true;

            if (emailValue.getText().isEmpty()) {
                currentInputMethods.delayShow(emailBorder);
                canCheck = false;
            }
            if (passwordValue.getText().isEmpty()) {
                currentInputMethods.delayShow(passwordBorder);
                canCheck = false;
            }
            if (canCheck) {
                loadingBar.setVisible(true);
                entirePage.setDisable(true);
                ResultSet rs = databaseConnection.checkIfUserExists(emailValue.getText().toLowerCase(), "SELECT * FROM personal_details WHERE email = ?");
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    String storedSalt = rs.getString("salt");
                    boolean isPasswordCorrect = PasswordHashing.checkPassword(passwordValue.getText(), storedSalt, storedHash);

                    if (isPasswordCorrect) {
                        if (saveState.isSelected()) {
                            currentPreferences.setEmail(emailValue.getText());
                            currentPreferences.setStatus(true);
                        } else {
                            currentPreferences.setEmail("");
                            currentPreferences.setStatus(false);
                        }
                        Stage currentStage = (Stage) entirePage.getScene().getWindow();
                        currentStage.close();
                        switch (rs.getString("role").replaceAll("\\s+", "")) {
                            case "InfoTech" -> centerScreenInstance.centerInfoTech(currentStage, rs, "/posapplication/infotech/infotech.fxml", databaseConnection);
                            case "Inventory" -> centerScreenInstance.centerInventory(currentStage, rs, "/posapplication/inventory/inventory.fxml", databaseConnection);
                            case "Cashier" -> centerScreenInstance.centerSales(currentStage, rs, "/posapplication/cashier/cashier.fxml", databaseConnection);
                            case "Manager" -> centerScreenInstance.centerManager(currentStage, rs, "/posapplication/manager/manager.fxml", databaseConnection);
                        
                        }

                    } else {
                        invalidDetailVBox.setVisible(true);
                        Toolkit.getDefaultToolkit().beep();
                    }
                } else {
                    invalidDetailVBox.setVisible(true);
                    Toolkit.getDefaultToolkit().beep();
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        } finally {
            loadingBar.setVisible(false);
        }
    }

    @FXML
    private void closeWarningBox(ActionEvent event) {
        invalidDetailVBox.setVisible(false);
        entirePage.setDisable(false);
    }

    @FXML
    private void togglePassword(MouseEvent event
    ) {
        passwordTextForm.textProperty().bindBidirectional(passwordValue.textProperty());
        passwordValue.setVisible(!passwordValue.isVisible());
        passwordTextForm.setVisible(!passwordTextForm.isVisible());
        if (!passwordValue.isVisible()) {
            passwordVisibility.setGlyphName("EYE");
        } else {
            passwordVisibility.setGlyphName("EYE_SLASH");
        }
    }
}
