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
import invaders.factory.Projectile;
import invaders.gameobject.Bunker;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.entities.Player;
import invaders.rendering.Renderable;
import invaders.util.gameState;
import invaders.util.gameUndo;
import javafx.event.Event;
import org.json.simple.JSONObject;

import static invaders.engine.GameWindow.*;

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

	private Player player;

	private boolean left;
	private boolean right;
	private int gameWidth;
	private int gameHeight;
	private int timer = 45;
	/**
	 * @Description :
	 * gameUndo function
	 */
	private List<EntityView> entityViews =  new ArrayList<EntityView>();
	private double xViewportOffset = 0.0;
	private double yViewportOffset = 0.0;

	private  gameUndo gameUndo;

	Stack<gameState> saves = new Stack<>();

	private  gameState gameState;

	//init engine
	public GameEngine(String config){
		// Read the config here
		gameUndo = new gameUndo();
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
//							if(renderableA.getRenderableObjectName().equals("Player") || renderableB.getRenderableObjectName().equals("Player")){
								updateScoreCount(renderableA);
								updateScoreCount(renderableB);
//							}
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
	 * calculate point
	*/
	public void updateScoreCount(Renderable renderable){
		if(!renderable.isAlive()&&!renderable.getRenderableObjectName().equals("Bunker")){

			if(renderable.getRenderableObjectName().equals("Enemy") || renderable.getRenderableObjectName().equals("EnemyProjectile")){
//			for test
//			if(renderable.getRenderableObjectName().equals("Enemy")){
				switch (renderable.getRenderableObjectName()){
					case "EnemyProjectile":
						if(renderable.getStrategy().contains("Fast")){
							updateScore(2); break;
						} else if (renderable.getStrategy().contains("Slow")) {
							updateScore(1);break;
						}
						break;
					case "Enemy" :
						if(renderable.getEnemyLevel(renderable).equals("weak")){
							updateScore(3);break;
						} else if (renderable.getEnemyLevel(renderable).equals("strong")) {
							updateScore(4);break;
						}
						break;
				}
			}
		}
	}
	/**
	 * @Description :
	 * save game every seconds
	 */
	void saveCurrentGame() {

		int score = Integer.parseInt(scoreCount.getText());
		int time = Integer.parseInt(timerTime.getText());
		List<Renderable> renderables = getRenderables();
		List<GameObject> gameObjects = getGameObjects();
		gameState gs = new gameState(score, time, renderables, gameObjects);
		gameUndo.saveCurrentState(gs);
	}

	/**
	 * @Description :
	 * Undo game
	 */
	public void undoGame(){
		//get the gameState of last frame
		gameState gs = gameUndo.Undo();
		if(gs !=null){
			List<Renderable> renderableList = gs.getRenderables();
			List<GameObject> gameObjects = gs.getGameObjects();
			setRenderables(renderableList);
			setGameObjects(gameObjects);
			resetStatic(Integer.parseInt(String.valueOf(gs.getTime())));
			scoreCount.setText(String.valueOf(gs.getScore()));
			timerTime.setText(String.valueOf(gs.getTime()));
			// update EntityView location
			updateEntity(renderableList,gameObjects);
			System.out.println("SLSLSLSLSLSLSLSLS");
		}else{
			System.out.println("none save");
		}
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


}
