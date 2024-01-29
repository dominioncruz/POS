/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

/**
 *
 * @author HP PROBOOK 430 G3
 */
import java.security.SecureRandom;

public class PasswordGenerator {

    private static final String LOWERCASE_CHARACTERS = "abcdefghijklmnopqrstuvwxyz";
    private static final String UPPERCASE_CHARACTERS = LOWERCASE_CHARACTERS.toUpperCase();
    private static final String NUMBERS = "0123456789";

    public String main(String[] args) {
        String password = generatePassword();
        return password;
    }

    public String generatePassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder password = new StringBuilder();

        // Add at least one uppercase letter
        password.append(getRandomChar(UPPERCASE_CHARACTERS, random));

        // Add at least one number
        password.append(getRandomChar(NUMBERS, random));

        // Fill the remaining characters with a mix of uppercase, lowercase, and numbers
        for (int i = 0; i < 8; i++) {
            String allCharacters = LOWERCASE_CHARACTERS + UPPERCASE_CHARACTERS + NUMBERS;
            password.append(getRandomChar(allCharacters, random));
        }

        // Shuffle the characters to make the password more random
        return shuffleString(password.toString(), random);
    }

    private static char getRandomChar(String source, SecureRandom random) {
        int randomIndex = random.nextInt(source.length());
        return source.charAt(randomIndex);
    }

    private static String shuffleString(String input, SecureRandom random) {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int randomIndex = random.nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[randomIndex];
            characters[randomIndex] = temp;
        }
        return new String(characters);
    }
    
    public String generateIDNumber() {
        SecureRandom random = new SecureRandom();
        StringBuilder idNumber = new StringBuilder();

        // Generate a 5-digit ID number
        for (int i = 0; i < 5; i++) {
            idNumber.append(getRandomDigit(random));
        }

        return idNumber.toString();
    }

    private static char getRandomDigit(SecureRandom random) {
        int randomIndex = random.nextInt(NUMBERS.length());
        return NUMBERS.charAt(randomIndex);
    }
}
