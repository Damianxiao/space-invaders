package invaders.engine;

import java.util.Collection;
import java.util.List;
import java.util.ArrayList;
import java.util.Stack;

import invaders.entities.EntityViewImpl;
import invaders.entities.Player;
import invaders.entities.SpaceBackground;
import invaders.event.ScoreEvent;
import invaders.factory.EnemyProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.util.gameState;
import invaders.util.gameUndo;
import javafx.animation.FadeTransition;
import javafx.animation.SequentialTransition;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import invaders.entities.EntityView;
import invaders.rendering.Renderable;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import invaders.util.styleModify;
import invaders.cheat.Cheat;

import static invaders.cheat.Cheat.*;

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


    /* *
     * @Description
     * cheatmode
    */
    private static Label cheatModeLabel = new Label();
	public GameWindow(GameEngine model){
        this.model = model;
		this.width =  model.getGameWidth();
        this.height = model.getGameHeight();

        //label style
        timerTitle = styleModify.setLabel(timerTitle);
        timerTime = styleModify.setLabel(timerTime);
        scoreCount = styleModify.setLabel(scoreCount);
        scoreTitle = styleModify.setLabel(scoreTitle);
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
        /* *
         * @Description
         * hacker mode
         */
        cheatModeLabel.setFont(Font.font(24));
        cheatModeLabel.setTextFill(Color.RED);
        cheatModeLabel.setOpacity(0.0);
        cheatModeLabel.setLayoutX(50);
        cheatModeLabel.setLayoutY(50);
        pane.getChildren().add(cheatModeLabel);
        scene = new Scene(pane, width, height);
        this.background = new SpaceBackground(model, pane);

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler(this.model);

        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);
        scene.setOnKeyReleased(keyboardInputHandler::handleReleased);

    }


    public void run() {
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
//        model.saveCurrentGame();
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
    public static void updatePane(List<Renderable> renderableList,List<GameObject> gameObjectList){
        // clear previous  renderables
        for (EntityView entityView : entityViews) {
            pane.getChildren().remove(entityView.getNode());
        }
        // clear previous gameState
        model.getGameObjects().clear();
        model.getRenderables().clear();
        entityViews.clear();

        model.getRenderables().addAll(renderableList);
        model.getGameObjects().addAll(gameObjectList);

        // draw new gamePane
        for (Renderable renderable : renderableList) {
            EntityView entityView = new EntityViewImpl(renderable);
            entityViews.add(entityView);
            pane.getChildren().add(entityView.getNode());
        }

    }

    /* *
     * @Description
     * cheatmode change
    */
    public static void showCheatLabel(boolean cheatMode) {
        if (cheatMode) {
            cheatModeLabel.setText("cheatMode On!");
        } else {
            cheatModeLabel.setText("cheatMode Off!");
        }
        List<FadeTransition> list =styleModify.initAnimation(cheatModeLabel);
        SequentialTransition sequence = new SequentialTransition(list.get(0), list.get(1));
        sequence.setOnFinished(event -> cheatModeLabel.setText(""));
        sequence.play();
    }
    /* *
     * @Description
     *  cheat selection
    */
    public static  void cheatModeSelection(String key){
        ArrayList<List<Projectile>> list1 = new ArrayList<List<Projectile>>();
        ArrayList<List<Projectile>> list2 = new ArrayList<List<Projectile>>();
        ArrayList<List<Enemy>> list3 = new ArrayList<List<Enemy>>();
        ArrayList<List<Enemy>> list4 = new ArrayList<List<Enemy>>();
        switch (key){
            case "J":
                list1=removeSlowProjectile(model,entityViews);
                model.getGameObjects().removeAll(list1.get(0));
                model.getRenderables().removeAll(list1.get(1));
                break;
            case "K":
                list2=removeFastProjectile(model,entityViews);
                model.getGameObjects().removeAll(list2.get(0));
                model.getRenderables().removeAll(list2.get(1));
                break;
            case "L":
                list3=removeSlowEnemy(model,entityViews);
                model.getGameObjects().removeAll(list3.get(0));
                model.getRenderables().removeAll(list3.get(1));
                break;
            case "P":
                list4=removeFastEnemy(model,entityViews);
                model.getGameObjects().removeAll(list4.get(0));
                model.getRenderables().removeAll(list4.get(1));
                break;
        }
    }

}
