package invaders.engine;

import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import invaders.entities.EntityViewImpl;
import invaders.entities.SpaceBackground;
import invaders.event.ScoreEvent;
import invaders.gameobject.GameObject;
import invaders.util.gameState;
import invaders.util.gameUndo;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GameWindow {
	private final int width;
    private final int height;
	private Scene scene;
    private static Pane pane;
    private static  GameEngine model;
    private static List<EntityView> entityViews =  new ArrayList<EntityView>();
    private Renderable background;

    private static double xViewportOffset = 0.0;
    private static double yViewportOffset = 0.0;
    // private static final double VIEWPORT_MARGIN = 280.0;
    /**
     * @Description :
     * set Timer Label
    */
    private  static  int gameTime=0;
    public static Label timerTime = new Label("0");
    public  Label timerTitle = new Label("Time:");

    /**
     * @Description :
     * set Score  Label
    */
    private static int score = 0;
    public static  Label scoreCount = new Label("0");
    public static  Label scoreTitle = new Label("Score:");

    /**
     * @Description :
     * gameUndo function
    */

    private static gameUndo gameUndo = new gameUndo();

    private static gameState gameState ;

	public GameWindow(GameEngine model){
        this.model = model;
		this.width =  model.getGameWidth();
        this.height = model.getGameHeight();

        //label style
        timerTitle = setLabel(timerTitle);
        timerTime = setLabel(timerTime);
        scoreCount = setLabel(scoreCount);
        scoreTitle = setLabel(scoreTitle);
        HBox box = new HBox();
        box.setSpacing(5);
        box.setPadding(new Insets(10,10,10,10));
        box.getChildren().addAll(timerTitle,timerTime,scoreTitle,scoreCount);

        /**
         * @Description :
         * scoreListener
        */

        pane = new Pane();
        pane.getChildren().addAll(box);
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

    }


    public void run() {

        /**
         * @Description :
         * undo game
        */
        Timeline undoTimeline = new Timeline(new KeyFrame(Duration.seconds(3), t -> model.saveCurrentGame()));
        undoTimeline.setCycleCount(Timeline.INDEFINITE);
        undoTimeline.play();
        //1ms for a frame, every frame do a draw()
         Timeline timeline = new Timeline(new KeyFrame(Duration.millis(17), t -> this.draw()));
         //setTimeline Cycle  infinity
         timeline.setCycleCount(Timeline.INDEFINITE);
         /**
          * @Description :
          * Timer timeline set
          * timeline could bind on any object ,btw not need to recreate a scene
         */
         Timeline  timerTimeline = new Timeline(new KeyFrame(Duration.seconds(1),e -> updateTimer()));
         timerTimeline.setCycleCount(Timeline.INDEFINITE);

         timerTimeline.play();

         timeline.play();
    }

    /**
     * @Description :
     * Undo game
    */

    private void draw(){
        // everyTime call Draw , do update once
        model.update(); // logic layer
        /* *
         * @Description
         *  view layer
        */
        List<Renderable> renderables = model.getRenderables();
        //if matches any entity update  each object  view
        for (Renderable entity : renderables) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    // update object position
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }
            // if have no object(entity) create it
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (Renderable entity : renderables){
            // entity dead,find match view and  delete
            if (!entity.isAlive()){
                for (EntityView entityView : entityViews){
                    if (entityView.matchesEntity(entity)){
                        // MARKED  but not  delete instantly, will be deleted at  below
                        entityView.markForDelete();
                    }
                }
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }


        model.getGameObjects().removeAll(model.getPendingToRemoveGameObject());
        model.getGameObjects().addAll(model.getPendingToAddGameObject());
        model.getRenderables().removeAll(model.getPendingToRemoveRenderable());
        model.getRenderables().addAll(model.getPendingToAddRenderable());

        model.getPendingToAddGameObject().clear();
        model.getPendingToRemoveGameObject().clear();
        model.getPendingToAddRenderable().clear();
        model.getPendingToRemoveRenderable().clear();

        entityViews.removeIf(EntityView::isMarkedForDelete);

    }

	public Scene getScene() {
        return scene;
    }
    /**
     * @Description :
     * set Label Style
    */
    public Label setLabel(Label label){
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

    /**
     * @Description :
     * update timer
    */
    public void updateTimer(){
        gameTime++;
        timerTime.setText(String.valueOf(gameTime));
    }
    /**
     * @Description :
     * update score
    */
    public static  void updateScore(int scoreChange) {
        model.saveCurrentGame();
        score += scoreChange;
        scoreCount.setText(String.valueOf(score));
    }

    /* *
     * @Description
     * set static variable to zero
    */
    public static void resetStatic(int time){
        gameTime=time;
//        score=0;
    }

    /**
     * @Description :
     *  update entityview
    */
    public static void updatePane(List<Renderable> renderableList){
        entityViews.clear();
        for (Renderable entity : renderableList) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    for (Renderable r : renderableList) {
                        EntityView e = new EntityViewImpl(r);
                        entityViews.add(e);
                        pane.getChildren().add(e.getNode());
                    }
                    // update object position
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }

            // if have no object(entity) create it
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

    }

    public static void updateEntity(List<Renderable> renderableList,List<GameObject> gameObjectList){
        /**
         * @Description :
         * clean original object
        */
            // entity dead,find match view and  delete
        for (Renderable entity : renderableList) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
//                    updatePane(view);
                    // update object position
                    view.update(xViewportOffset, yViewportOffset);
                    break;
                }
            }

            // if have no object(entity) create it
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

    }


}
