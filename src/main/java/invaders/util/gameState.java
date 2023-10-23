package invaders.util;

import invaders.entities.Player;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.rendering.Renderable;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static invaders.engine.GameWindow.scoreCount;
import static invaders.engine.GameWindow.timerTime;

/**
 * @Description :
 * gameState in every Frame
*/
public class gameState implements Serializable {
    /**
     * @Description :
     * record  all variable need
    */
    private int score;
    private int time;
    private List<Enemy> enemyList;

    private List<Bunker> bunkersList;

    private List<Projectile> projectileList;
    private List<Player> playerList;
    private List<Renderable> renderables;
    private List<GameObject> gameObjects;


    public gameState(int score, int time, List<Renderable> renderables, List<GameObject> gameObjects) {
        this.score = score;
        this.time = time;
        this.gameObjects = new ArrayList<>();
        this.renderables = new ArrayList<>();
        
       
        for (Renderable gameObject : renderables) {
            Object clones = gameObject.clones();
            if (clones instanceof  GameObject) {
                this.gameObjects.add((GameObject) clones);
            }
            if (clones instanceof  Renderable) {
                this.renderables.add((Renderable) clones);
            }
        }
    }

    public gameState(int score, int time, List<Enemy> enemyList, List<Bunker> bunkersList, List<Projectile> projectileList) {
        this.score = score;
        this.time = time;
        this.enemyList = enemyList;
        this.bunkersList = bunkersList;
        this.projectileList = projectileList;
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
