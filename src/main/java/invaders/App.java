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
import invaders.util.styleModify;

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
        VBox layout = new VBox(10);
        // new Button Object
        Button easyButton = styleModify.buttonStyle(new Button("noob"));
        Button mediumButton = styleModify.buttonStyle(new Button("Medium"));
        Button hardButton = styleModify.buttonStyle(new Button("Hard"));
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
    static void startGame(Stage primaryStage, String configFilePath) {
        GameEngine model = new GameEngine(configFilePath);
        GameWindow window = new GameWindow(model);
        Scene gameScene = window.getScene();
        primaryStage.setScene(gameScene);
        window.run();
    }

}
