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
        Label title = new Label();
        title.setStyle("-fx-font-size: 20px");
        title.setText(" Choose Difficulty You Wanna Play:");
        layout.setAlignment(Pos.CENTER);
        layout.getChildren().addAll(title, easyButton, mediumButton, hardButton);
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
}
