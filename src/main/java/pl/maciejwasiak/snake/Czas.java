package pl.maciejwasiak.snake;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

public class Czas extends Thread {
    static int min = 0;
    static int sec = 0;
    static int counter = 0;

    Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
        counter++;
        sec++;
        if (sec == 0) {
            min++;
            sec = 0;
        }
        Main.min = min;
        Main.sec = sec;
    }));

    @Override
    public void run() {
        while (!isInterrupted()) {
            timeline.play();
        }
    }
}
