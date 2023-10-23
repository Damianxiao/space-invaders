package invaders.util;

import javafx.animation.FadeTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class styleModify {
    public static Button buttonStyle(Button button){
        button.setLayoutX(50);
        button.setLayoutY(50);
        button.setPrefWidth(100);
        button.setPrefHeight(50);
        button.setStyle("-fx-font-size: 16px;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-text-fill: #ff0000;\n" +
                "    -fx-background-color: #f0f0f0;\n" +
                "    -fx-padding: 10px;\n" +
                "    -fx-border-color: #000000;\n" +
                "    -fx-border-width: 2px;\n" +
                "    -fx-border-radius: 5px;\n" );
        return  button ;
    }

    /**
     * @Description :
     * set Label Style
     */
    public static Label setLabel(Label label){
        label.setFont(javafx.scene.text.Font.font(30));
        label.setStyle("-fx-font-size: 16px;\n" +
                "    -fx-font-weight: bold;\n" +
                "    -fx-text-fill: #ff0000;\n" +
                "    -fx-background-color: #f0f0f0;\n" +
                "    -fx-padding: 10px;\n" +
                "    -fx-border-color: #000000;\n" +
                "    -fx-border-width: 2px;\n" +
                "    -fx-border-radius: 5px;\n" );
        return label;
    }

    public static List<FadeTransition> initAnimation(Label cheatModeLabel){
        List<FadeTransition> list = new ArrayList<>();
        // cheatlabel fade in
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), cheatModeLabel);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        // cheatlabel fade out
        FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), cheatModeLabel);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        fadeOut.setDelay(Duration.seconds(2));
        list.add(fadeIn);
        list.add(fadeOut);
        return list;
    }
}
