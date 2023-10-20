package invaders.gameobject;

import com.sun.javafx.event.EventUtil;
import invaders.engine.GameEngine;
import invaders.event.ScoreEvent;
import invaders.factory.EnemyProjectileFactory;
import invaders.factory.Projectile;
import invaders.factory.ProjectileFactory;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.strategy.ProjectileStrategy;
import javafx.event.Event;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.Random;

import static com.sun.javafx.event.EventUtil.fireEvent;




public class Enemy implements GameObject, Renderable {
    private Vector2D position;
    private int lives = 1;
    private Image image;
    private int xVel = -1;

    private ArrayList<Projectile> enemyProjectile;
    private ArrayList<Projectile> pendingToDeleteEnemyProjectile;
    private ProjectileStrategy projectileStrategy;
    private ProjectileFactory projectileFactory;
    private Image projectileImage;
    private Random random = new Random();
    /**
     * @Description :
     * score
    */
    public Enemy(Vector2D position) {
        this.position = position;
        this.projectileFactory = new EnemyProjectileFactory();
        this.enemyProjectile = new ArrayList<>();
        this.pendingToDeleteEnemyProjectile = new ArrayList<>();
    }

    @Override
    public void start() {}

    @Override
    public void update(GameEngine engine) {
        // control enemyProjectile
        if(enemyProjectile.size()<3){
            /**
             *  if bullet sub 3 and it is still  alive, it have chance to  shoot another time
             */
            if(this.isAlive() &&  random.nextInt(120)==20){
                Projectile p = projectileFactory.createProjectile(new Vector2D(position.getX() + this.image.getWidth() / 2, position.getY() + image.getHeight() + 2),projectileStrategy, projectileImage);
                enemyProjectile.add(p);
                engine.getPendingToAddGameObject().add(p);
                engine.getPendingToAddRenderable().add(p);
            }
        }else{
            /**
             * if  >= 3  delete
             */
            pendingToDeleteEnemyProjectile.clear();
            /**
             *  if projectile  hited , delete
             */
            for(Projectile p : enemyProjectile){
                if(!p.isAlive()){
                    engine.getPendingToRemoveGameObject().add(p);
                    engine.getPendingToRemoveRenderable().add(p);
                    pendingToDeleteEnemyProjectile.add(p);
                    /**
                     * @Description :
                     * set an event  will increase 1 point score
                     * and trigger it and add a listener in GameWindow
                    */

                }
            }
            // clean
            for(Projectile p: pendingToDeleteEnemyProjectile){
                enemyProjectile.remove(p);
            }
        }

        // if enemy move to border , go  down for 25px,and reverse the horizon move direction
        if(this.position.getX()<=this.image.getWidth() || this.position.getX()>=(engine.getGameWidth()-this.image.getWidth()-1)){
            this.position.setY(this.position.getY()+25);
            xVel*=-1;
        }

        // enemy movement
        this.position.setX(this.position.getX() + xVel);

        // player got colliding with enemy
        if((this.position.getY()+this.image.getHeight())>=engine.getPlayer().getPosition().getY()){
            engine.getPlayer().takeDamage(Integer.MAX_VALUE);
        }

        /*
        Logic TBD
         */

    }

    @Override
    public Image getImage() {
        return this.image;
    }

    @Override
    public double getWidth() {
        return this.image.getWidth();
    }

    @Override
    public double getHeight() {
       return this.image.getHeight();
    }

    @Override
    public Vector2D getPosition() {
        return this.position;
    }

    @Override
    public Layer getLayer() {
        return Layer.FOREGROUND;
    }

    public void setPosition(Vector2D position) {
        this.position = position;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void setImage(Image image) {
        this.image = image;
    }

    public void setProjectileImage(Image projectileImage) {
        this.projectileImage = projectileImage;
    }

    @Override
    public void takeDamage(double amount) {
        this.lives-=1;
    }

    @Override
    public double getHealth() {
        return this.lives;
    }

    @Override
    public String getRenderableObjectName() {
        return "Enemy";
    }

    @Override
    public boolean isAlive() {
        return this.lives>0;
    }

    public void setProjectileStrategy(ProjectileStrategy projectileStrategy) {
        this.projectileStrategy = projectileStrategy;
    }

    @Override
    public String getStrategy() {
        return null;
    }

    @Override
    public String getEnemyLevel(Renderable renderable) {
        if(renderable.getImage().getUrl().contains("slow")){
            return "weak";
        }else if(renderable.getImage().getUrl().contains("fast")){
            return "strong";
        }else
        return null;
    }
}
