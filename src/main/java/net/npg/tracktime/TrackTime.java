package net.npg.tracktime;

import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TrackTime {
    public static void main(final String[] args) {
        Application.launch(TrackTimeUI.class, args);
    }
}
