package invaders;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import invaders.engine.GameEngine;
import invaders.engine.GameWindow;

import java.util.Map;

public class App extends Application {
    private static String gameDiff;

    // difficultyConfigPath
    private static String easy = "src/main/resources/config_easy.json";
    private static String medium = "src/main/resources/config_medium.json";
    private static String hard = "src/main/resources/config_hard.json";

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        /**
         * @Description
         *  create a difficulty select menu.
         */
        primaryStage.setTitle("Space invaders");

        // difficulty Choose
//        Scene startScene  = createStartScene(primaryStage);
        VBox layout = new VBox(10);
        // new Button Object
        Button easyButton = buttonStyle(new Button("noob"));
        Button mediumButton = buttonStyle(new Button("Medium"));
        Button hardButton = buttonStyle(new Button("Hard"));
        // redirect to game scene
        easyButton.setOnAction(event -> startGame(primaryStage, easy));
        mediumButton.setOnAction(event -> startGame(primaryStage, medium));
        hardButton.setOnAction(event -> startGame(primaryStage, hard));
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(new Label(" Choose Difficulty You Wanna Play:"), easyButton, mediumButton, hardButton);
        Scene startScene = new Scene(layout,800,800);
        primaryStage.setScene(startScene);
        primaryStage.show();
    }

    /**
     * @Description
     * enter game
    */
    private void startGame(Stage primaryStage, String configFilePath) {
        GameEngine model = new GameEngine(configFilePath);
        GameWindow window = new GameWindow(model);
        Scene gameScene = window.getScene();

        primaryStage.setScene(gameScene);
        window.run();
    }

    /**
     * @Description
     * modify  button default style
    */
    public Button buttonStyle(Button button){
        button.setLayoutX(50);
        button.setLayoutY(50);
        button.setPrefWidth(100);
        button.setPrefHeight(50);
        button.setStyle(
                "-fx-background-color:#00BFFF;"+
                        "-fx-background-radius:20;"+
                        "-fx-text-fill:#FF0000;"+
                        "-fx-border-radius:20;"+
                        "-fx-border-width:5;"+
                        "-fx-border-insets:-5"
        );
        return  button ;
    }
}
