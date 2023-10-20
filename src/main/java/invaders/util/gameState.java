package invaders.util;

import invaders.gameobject.GameObject;
import invaders.rendering.Renderable;

import java.util.List;

/**
 * @Description :
 * gameState in every Frame
*/
public class gameState {
    /**
     * @Description :
     * record  all variable need
    */
    private int score;
    private int time;
    private List<Renderable> renderables;
    private List<GameObject> gameObjects;

    public gameState(int score, int time, List<Renderable> renderables, List<GameObject> gameObjects) {
        this.score = score;
        this.time = time;
        this.renderables = renderables;
        this.gameObjects = gameObjects;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public List<Renderable> getRenderables() {
        return renderables;
    }

    public void setRenderables(List<Renderable> renderables) {
        this.renderables = renderables;
    }

    public List<GameObject> getGameObjects() {
        return gameObjects;
    }

    public void setGameObjects(List<GameObject> gameObjects) {
        this.gameObjects = gameObjects;
    }
}
