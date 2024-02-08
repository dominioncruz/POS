/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

import java.awt.image.BufferedImage;
import java.sql.Statement;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javax.imageio.ImageIO;
import javax.sql.rowset.serial.SerialBlob;
import java.sql.Timestamp;
import java.sql.Time;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class DatabaseConnection {

    private final PasswordGenerator passwordGenerator = new PasswordGenerator();
    private static Connection connection;
    static String host = "localhost";
    static String port = "3306";
    static String user = "root";
    static String password = "serialKiller";
    static String database = "pos_application";

    private DatabaseConnection() {
        // Initialize the connection

        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?user=" + user + "&password=" + password);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Database connection unsuccessful");
        }
    }

    public static DatabaseConnection getInstance() {
        DatabaseConnection instance;
        instance = new DatabaseConnection();
        return instance;
    }

    public static ResultSet checkIfUserExists(String email, String query) throws SQLException {
        PreparedStatement ps = connection.prepareStatement(query);
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        return rs;
    }

    public static void backupMySQLDatabase() {
        String desktopPath = System.getProperty("user.home") + "\\Documents";

        String backup = desktopPath + "\\backup.sql";
        System.out.println(backup);
        try {
            // Build mysqldump command
            ProcessBuilder processBuilder = new ProcessBuilder(
                    "mysqldump",
                    "--host=" + host,
                    "--port=" + port,
                    "--user=" + user,
                    "--password=" + password,
                    "--databases", database,
                    "--result-file=" + backup
            );

            // Execute the command
            Process process = processBuilder.start();
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                System.out.println("Backup completed successfully.");
            } else {
                System.out.println("Backup failed. Exit code: " + exitCode);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public String insertUserIntoDatabase(String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth, String gender, String role, String email, Image profile_photo, String query) throws SQLException, Exception {
        BufferedImage bImage = SwingFXUtils.fromFXImage(profile_photo, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", s);
        byte[] imageBytes = s.toByteArray();
        SerialBlob imageBlob = new SerialBlob(imageBytes);
        String password = passwordGenerator.generatePassword();
        String salt = PasswordHashing.getSalt();
        String hashedPassword = PasswordHashing.hashPassword(password, salt);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(dateOfBirth.atStartOfDay()));
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, gender);
            preparedStatement.setBlob(7, imageBlob);
            preparedStatement.setString(8, role);
            preparedStatement.setString(9, hashedPassword);
            preparedStatement.setString(10, salt);
            int rs = preparedStatement.executeUpdate();
            if (rs > 0) {
                if ("Info Tech".equals(role)) {
                    makeDefaultTimeForSchedule(email);
                }
                return password;
            }
            return null;
        }
    }

    public boolean updateUserInDatabase(String firstName, String lastName, String phoneNumber, LocalDate dateOfBirth, String gender, String role, String email, Image profile_photo, String query) throws SQLException, Exception {
        BufferedImage bImage = SwingFXUtils.fromFXImage(profile_photo, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", s);
        byte[] imageBytes = s.toByteArray();
        SerialBlob imageBlob = new SerialBlob(imageBytes);
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, firstName);
            preparedStatement.setString(2, lastName);
            preparedStatement.setTimestamp(3, Timestamp.valueOf(dateOfBirth.atStartOfDay()));
            preparedStatement.setString(4, phoneNumber);
            preparedStatement.setString(5, email);
            preparedStatement.setString(6, gender);
            preparedStatement.setBlob(7, imageBlob);
            preparedStatement.setString(8, role);
            preparedStatement.setString(9, email);
            int rs = preparedStatement.executeUpdate();
            return rs > 0;
        }
    }

    public void updatePasswprd(String password, String matNumber) throws SQLException, Exception {
        String salt = PasswordHashing.getSalt();
        String hashedPassword = PasswordHashing.hashPassword(password, salt);
        String sql = "UPDATE personal_details SET password = ?, salt = ? WHERE email = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, hashedPassword);
            preparedStatement.setString(2, salt);
            preparedStatement.setString(3, matNumber);
            int rs = preparedStatement.executeUpdate();
        }
    }

    public void makeDefaultTimeForSchedule(String email) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO backup_times (email) VALUES (?)")) {
            preparedStatement.setString(1, email);
            int rs = preparedStatement.executeUpdate();
        }
    }

    public Time selectScheduleTime(String email) throws SQLException {

        PreparedStatement preparedStatement = connection.prepareStatement("SELECT backup_time FROM backup_times WHERE email = ?");
        preparedStatement.setString(1, email);
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            return rs.getTime("backup_time");
        }

        return null;
    }

    public boolean updateTimeForSchedule(Time backupTime, String email) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("UPDATE backup_times SET backup_time = ? WHERE email = ?")) {
            preparedStatement.setTime(1, backupTime);
            preparedStatement.setString(2, email);
            int rs = preparedStatement.executeUpdate();
            return rs > 0;
        }
    }

    public ResultSet getAllProducts() throws SQLException {
        PreparedStatement ps = connection.prepareStatement("SELECT * FROM products ORDER BY name ASC");
        ResultSet rs = ps.executeQuery();
        return rs;
    }

    public boolean createNewProductInDatabase(
            String product_code, String productNameInput, String manufacturer_name, LocalDate production_date, LocalDate expiry_date, String quantity, String price, Image productImage, String productDescription, String low_stock_count, String query) throws IOException, SQLException {
        BufferedImage bImage = SwingFXUtils.fromFXImage(productImage, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", s);
        byte[] imageBytes = s.toByteArray();
        SerialBlob imageBlob = new SerialBlob(imageBytes);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product_code);
            preparedStatement.setString(2, productNameInput);
            preparedStatement.setString(3, manufacturer_name);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(production_date.atStartOfDay()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(expiry_date.atStartOfDay()));
            preparedStatement.setInt(6, Integer.parseInt(quantity));
            preparedStatement.setInt(7, Integer.parseInt(price));
            preparedStatement.setBlob(8, imageBlob);
            preparedStatement.setString(9, productDescription);
            preparedStatement.setString(10, low_stock_count);
            int rs = preparedStatement.executeUpdate();
            return rs > 0;
        }
    }

    public boolean updateProductInDatabase(
            String product_code, String productNameInput, String manufacturer_name, LocalDate production_date, LocalDate expiry_date, String quantity, String price, Image productImage, String productDescription, String low_stock_count, String query) throws IOException, SQLException {
        BufferedImage bImage = SwingFXUtils.fromFXImage(productImage, null);
        ByteArrayOutputStream s = new ByteArrayOutputStream();
        ImageIO.write(bImage, "png", s);
        byte[] imageBytes = s.toByteArray();
        SerialBlob imageBlob = new SerialBlob(imageBytes);

        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, product_code);
            preparedStatement.setString(2, productNameInput);
            preparedStatement.setString(3, manufacturer_name);
            preparedStatement.setTimestamp(4, Timestamp.valueOf(production_date.atStartOfDay()));
            preparedStatement.setTimestamp(5, Timestamp.valueOf(expiry_date.atStartOfDay()));
            preparedStatement.setInt(6, Integer.parseInt(quantity));
            preparedStatement.setInt(7, Integer.parseInt(price));
            preparedStatement.setBlob(8, imageBlob);
            preparedStatement.setString(9, productDescription);
            preparedStatement.setString(10, low_stock_count);
            preparedStatement.setString(11, product_code);
            int rs = preparedStatement.executeUpdate();
            return rs > 0;
        }
    }

    public boolean addNewSaleForSeller(
            String buyerName,
            String product_code,
            String quantity,
            String amount,
            String paymentMethodUsed,
            String userEmail
    ) throws SQLException {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT into sales (customer_name, product_code, quantity, amount, payment_method, seller_email) values (?, ?, ?, ?, ?, ?)")) {
            if (buyerName.equals("")) {
                buyerName = "No name";
            }
            preparedStatement.setString(1, buyerName);
            preparedStatement.setString(2, product_code);
            preparedStatement.setInt(3, Integer.parseInt(quantity));
            preparedStatement.setInt(4, Integer.parseInt(amount));
            preparedStatement.setString(5, paymentMethodUsed);
            preparedStatement.setString(6, userEmail);
            int rs = preparedStatement.executeUpdate();
            if (rs > 0) {
                String updateInventorySQL = "UPDATE products SET quantity = quantity - ? WHERE product_code = ?";
                try (PreparedStatement updateInventoryStatement = connection.prepareStatement(updateInventorySQL)) {
                    updateInventoryStatement.setInt(1, Integer.parseInt(quantity));
                    updateInventoryStatement.setString(2, product_code);
                    rs = updateInventoryStatement.executeUpdate();
                    return rs > 0;
                }
            }
        }
        return false;

    }

    public ResultSet fetchRelevantManagerData(String query) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
        ResultSet rs = preparedStatement.executeQuery(query);
        return rs;
    }
    
    public ResultSet getBirthdays() throws SQLException{
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM personal_details WHERE MONTH(date_of_birth) = MONTH(CURRENT_DATE) AND DAY(date_of_birth) = DAY(CURRENT_DATE)");
        ResultSet rs = preparedStatement.executeQuery();
        return rs;

    }

}
