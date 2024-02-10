/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Time;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.layout.VBox;
import posapplication.infotech.InfotechController;
import posapplication.infotech.UniqueInfoTechMethods;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class timerClass {

    private Time specifiedTime;
    private TimeThread timer;
    private Thread t1;
    UniqueInfoTechMethods infotechMethod;
    InfotechController infoTechController;
    VBox messagesContainer;

    public timerClass(Time specifiedTime, UniqueInfoTechMethods intotech, InfotechController infocontrol, VBox messageCont) throws ClassNotFoundException {
        this.specifiedTime = specifiedTime;
        this.timer = new TimeThread();
        this.t1 = new Thread(timer);
        this.infotechMethod = intotech;
        this.infoTechController = infocontrol;
        this.messagesContainer = messageCont;
        t1.start();
    }

    public class TimeThread implements Runnable {

        private volatile boolean stopTimer = false;

        @Override
        public void run() {

            try {

                while (!stopTimer) {
                    Platform.runLater(() -> {
                        LocalTime specifiedLocalTime = specifiedTime.toLocalTime();
                        LocalTime currentTime = LocalTime.now();
                        if (specifiedLocalTime.getHour() == currentTime.getHour() && specifiedLocalTime.getMinute() == currentTime.getMinute() && currentTime.getSecond() == 10) {
                            DatabaseConnection.backupMySQLDatabase();
                            try {
                                infotechMethod.addNewInfoToMessagses("Backup", "Data backup successful", "Data backed up to Documents folder successfully at " + currentTime.getHour() + ":" + currentTime.getMinute(), infoTechController, messagesContainer);
                            } catch (SQLException ex) {
                                Logger.getLogger(timerClass.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(timerClass.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    });

                    TimeUnit.SECONDS.sleep(1);

                }

            } catch (InterruptedException err) {
                System.out.println(err);
            }
        }

        ;
        
        public void stopTimer() {
            stopTimer = true;
        }
    }

    public void updateTimerValue(Time newSpecifiedTime) {
        specifiedTime = newSpecifiedTime;
    }

    public void stopTimer() {
        timer.stopTimer();
    }
}
