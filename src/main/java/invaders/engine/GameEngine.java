package invaders.engine;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import invaders.ConfigReader;
import invaders.builder.BunkerBuilder;
import invaders.builder.Director;
import invaders.builder.EnemyBuilder;
import invaders.entities.EntityView;
import invaders.entities.EntityViewImpl;
import invaders.event.ScoreEvent;
import invaders.factory.PlayerProjectileFactory;
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.entities.Player;
import invaders.rendering.Renderable;
import invaders.util.gameState;
import invaders.util.gameUndo;
import javafx.event.Event;
import org.apache.commons.lang3.SerializationUtils;
import org.json.simple.JSONObject;

import javax.swing.text.Position;

import static invaders.engine.GameWindow.*;
import static invaders.score.scoreBroad.updateScoreCount;
import static invaders.util.gameUndo.gameUndoSave;

import invaders.util.gameUndo;

/**
 * This class manages the main loop and logic of the game
 */
public class GameEngine {
	private List<GameObject> gameObjects = new ArrayList<>(); // A list of game objects that gets updated each frame
	private List<GameObject> pendingToAddGameObject = new ArrayList<>();
	private List<GameObject> pendingToRemoveGameObject = new ArrayList<>();

	private List<Renderable> pendingToAddRenderable = new ArrayList<>();
	private List<Renderable> pendingToRemoveRenderable = new ArrayList<>();

	private List<Renderable> renderables =  new ArrayList<>();

	public Player player;

	private boolean left;
	private boolean right;
	private int gameWidth;
	private int gameHeight;
	private int timer = 45;
	/**
	 * @Description :
	 * gameUndo function
	 */
	List<Player> playerList = new ArrayList<>();


	private static gameState currentGameState;
	private static gameState previousGameState;

	/* *
	 * @Description
	 * cheat
	*/
	private boolean cheatMode;
	//init engine
	public GameEngine(String config){
		// Read the config here
		 ConfigReader.parse(config);

		// Get game width and height
		gameWidth = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("x")).intValue();
		gameHeight = ((Long)((JSONObject) ConfigReader.getGameInfo().get("size")).get("y")).intValue();

		// init game  object : at this set value of enemy
		//Get player info
		// speed lives positions
		this.player = new Player(ConfigReader.getPlayerInfo());
		// add to render objects list
		renderables.add(player);


		Director director = new Director();
		BunkerBuilder bunkerBuilder = new BunkerBuilder();
		//Get Bunkers info
		for(Object eachBunkerInfo:ConfigReader.getBunkersInfo()){
			Bunker bunker = director.constructBunker(bunkerBuilder, (JSONObject) eachBunkerInfo);
			gameObjects.add(bunker);
			renderables.add(bunker);
		}


		EnemyBuilder enemyBuilder = new EnemyBuilder();
		//Get Enemy info
		for(Object eachEnemyInfo:ConfigReader.getEnemiesInfo()){
			Enemy enemy = director.constructEnemy(this,enemyBuilder,(JSONObject)eachEnemyInfo);
			gameObjects.add(enemy);
			renderables.add(enemy);
		}

	}

	/**
	 * Updates the game/simulation
	 * update every Frame
	 */
	public void update(){

		timer+=1;

		movePlayer();

		// every fram refresh  each object
		for(GameObject go: gameObjects){
			go.update(this);
		}
		// Check  colliding of renderables
		for (int i = 0; i < renderables.size(); i++) {
			Renderable renderableA = renderables.get(i);
			for (int j = i+1; j < renderables.size(); j++) {
				Renderable renderableB = renderables.get(j);


				if((renderableA.getRenderableObjectName().equals("Enemy") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))
						||(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("Enemy"))||
						(renderableA.getRenderableObjectName().equals("EnemyProjectile") && renderableB.getRenderableObjectName().equals("EnemyProjectile"))){
				}else{
					// totally,there are three object must be  check; playerProjectile -->enemy  ; playerProjectile ---> enemyProjectile
					if(renderableA.isColliding(renderableB) && (renderableA.getHealth()>0 && renderableB.getHealth()>0)) {
						renderableA.takeDamage(1);
						renderableB.takeDamage(1);
						/**
						 * @Description :
						 * calculate point
						 */
						if(!renderableA.getRenderableObjectName().equals("Bunker")&&!renderableB.getRenderableObjectName().equals("Bunker")){
							if(renderableA.getRenderableObjectName().equals("PlayerProjectile") || renderableB.getRenderableObjectName().equals("PlayerProjectile")){
								updateScoreCount(renderableA);
								updateScoreCount(renderableB);
							}
						}
					}
				}
			}
		}

		// ensure that renderable foreground objects don't go off-screen
		int offset = 1;
		for(Renderable ro: renderables){
			if(!ro.getLayer().equals(Renderable.Layer.FOREGROUND)){
				continue;
			}
			if(ro.getPosition().getX() + ro.getWidth() >= gameWidth) {
				ro.getPosition().setX((gameWidth - offset) -ro.getWidth());
			}

			if(ro.getPosition().getX() <= 0) {
				ro.getPosition().setX(offset);
			}

			if(ro.getPosition().getY() + ro.getHeight() >= gameHeight) {
				ro.getPosition().setY((gameHeight - offset) -ro.getHeight());
			}

			if(ro.getPosition().getY() <= 0) {
				ro.getPosition().setY(offset);
			}
		}

	}

	public List<Renderable> getRenderables(){
		return renderables;
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}
	public List<GameObject> getPendingToAddGameObject() {
		return pendingToAddGameObject;
	}

	public List<GameObject> getPendingToRemoveGameObject() {
		return pendingToRemoveGameObject;
	}

	public List<Renderable> getPendingToAddRenderable() {
		return pendingToAddRenderable;
	}

	public List<Renderable> getPendingToRemoveRenderable() {
		return pendingToRemoveRenderable;
	}


	public void leftReleased() {
		this.left = false;
	}

	public void rightReleased(){
		this.right = false;
	}

	public void leftPressed() {
		this.left = true;
	}
	public void rightPressed(){
		this.right = true;
	}

	public boolean shootPressed(){
		if(timer>45 && player.isAlive()){
			Projectile projectile = player.shoot();
			gameObjects.add(projectile);
			renderables.add(projectile);
			timer=0;
			return true;
		}
		return false;
	}

	private void movePlayer(){
		if(left){
			player.left();
		}

		if(right){
			player.right();
		}
	}

	public int getGameWidth() {
		return gameWidth;
	}

	public int getGameHeight() {
		return gameHeight;
	}

	public Player getPlayer() {
		return player;
	}


	/**
	 * @Description :
	 * save game
	 */
	void saveCurrentGame() {
		previousGameState = gameUndo.saveGameState(this);
	}

	/**
	 * @Description :
	 * Undo game
	 */
	public void undoGame(){
		gameUndoSave(previousGameState,this);
		previousGameState=null;
	}

	/**
	 * @Description :
	 *  undo set model
	*/
	public void setRenderables(List<Renderable> renderables){
		this.renderables=renderables;
	}
	public void setGameObjects(List<GameObject> gameObjects){
		this.gameObjects=gameObjects;
	}

	/* *
	 * @Description
	 * hackerOn
	*/
	public void hackerOn(){
		cheatMode = !cheatMode;
		showCheatLabel(cheatMode);
	}

	/**
	 * @Description :
	 * trigger cheat
	*/
	public void Hacker(String key){
		if(cheatMode) {
			cheatModeSelection(key);
		}
	}
}
