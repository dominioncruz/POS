/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class InputMethods {

    private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

    public void delayShow(VBox border) {
        border.getStyleClass().add("warning_border");
        border.getStyleClass().remove("normal_border");
        executorService.schedule(() -> {
            border.getStyleClass().remove("warning_border");
            border.getStyleClass().add("normal_border");
        }, 3, TimeUnit.SECONDS);
    }
}
