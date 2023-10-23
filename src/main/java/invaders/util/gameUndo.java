package invaders.util;

import invaders.engine.GameEngine;
import invaders.entities.Player;
import invaders.gameobject.GameObject;
import invaders.rendering.Renderable;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

//import static invaders.engine.GameEngine.previousGameState;
import static invaders.engine.GameWindow.*;
import static invaders.engine.GameWindow.updatePane;

/**
 * @Description :
 *  implement  undo function
*/
public class gameUndo {
    /**
     * @Description :
     * use stack , so we can undo game in timeline sequence
    */
    private Stack<gameState> saves;

    public gameUndo(){
        saves = new Stack<gameState>();
    }

    public gameState Undo(){
        if(!saves.isEmpty()){
            return saves.pop();
        }
        return null;
    }
    public void saveCurrentState(gameState state){
        saves.push(state);
    }

    public Stack<gameState> getSaves() {
        return saves;
    }

    public void setSaves(Stack<gameState> saves) {
        this.saves = saves;
    }

    /**
     * @Description :
     * save game
     */
    public static gameState saveGameState(GameEngine model) {
        int score = Integer.parseInt(scoreCount.getText());
        int time = Integer.parseInt(timerTime.getText());
        List<Renderable> renderablesCopy = new ArrayList<>(model.getRenderables());
        List<GameObject> gameObjectsCopy = new ArrayList<>(model.getGameObjects());
        return new gameState(score, time, renderablesCopy, gameObjectsCopy);
//		System.out.println(renderables.size()+"======="+gameObjects.size());
    }


    public static  void gameUndoSave(gameState previousGameState,GameEngine model){
        if(previousGameState!=null){
            final List<Renderable> renderables = previousGameState.getRenderables();
            for (Renderable renderable : renderables) {
                if (renderable instanceof Player) {
                    model.player = (Player) renderable;
                    break;
                }
            }
            model.setRenderables(new ArrayList<>(renderables));
            model.setGameObjects(new ArrayList<>(previousGameState.getGameObjects()));
            resetStatic(Integer.parseInt(String.valueOf(previousGameState.getTime())));
            scoreCount.setText(String.valueOf(previousGameState.getScore()));
            timerTime.setText(String.valueOf(previousGameState.getTime()));
            updatePane(renderables, previousGameState.getGameObjects());
//            previousGameState=null;

        }
    }

}
