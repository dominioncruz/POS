/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package posapplication.reusableFunctions;

import java.sql.Timestamp;
import java.sql.Time;
import java.time.LocalTime;
import java.util.concurrent.TimeUnit;

/**
 *
 * @author HP PROBOOK 430 G3
 */
public class timerClass {

    private Time specifiedTime;
    private TimeThread timer;
    private Thread t1;

    public timerClass(Time specifiedTime) {
        this.specifiedTime = specifiedTime;
        this.timer = new TimeThread();
        this.t1 = new Thread(timer);
        t1.start();
    }

    public class TimeThread implements Runnable {

        private volatile boolean stopTimer = false;

        @Override
        public void run() {

            try {

                while (!stopTimer) {
                    
                    LocalTime specifiedLocalTime = specifiedTime.toLocalTime();
                    LocalTime currentTime = LocalTime.now();
                    if (specifiedLocalTime.getHour() == currentTime.getHour() && specifiedLocalTime.getMinute() == currentTime.getMinute() && currentTime.getSecond() == 10) {
                        DatabaseConnection.backupMySQLDatabase();
                    }
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

    public void updateTimerValue(Time newSpecifiedTime){
        specifiedTime = newSpecifiedTime;
    }
    public void stopTimer() {
        timer.stopTimer();
    }
}
