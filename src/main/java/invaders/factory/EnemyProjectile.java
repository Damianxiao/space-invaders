package invaders.factory;

import invaders.engine.GameEngine;
import invaders.gameobject.GameObject;
import invaders.physics.Collider;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

public class EnemyProjectile extends Projectile{
    private ProjectileStrategy strategy;

    public EnemyProjectile(Vector2D position, ProjectileStrategy strategy, Image image) {
        super(position,image);
        this.strategy = strategy;
    }

    @Override
    public void update(GameEngine model) {
        strategy.update(this);

        if(this.getPosition().getY()>= model.getGameHeight() - this.getImage().getHeight()){
            this.takeDamage(1);
        }

    }
    @Override
    public String getRenderableObjectName() {
        return "EnemyProjectile";
    }

    @Override
    public String getStrategy() {
        return strategy.toString();
    }

    @Override
    public String getEnemyLevel(Renderable renderable) {
        return null;
    }

    @Override
    public int getLives() {
        return lives;
    }

    public GameObject clones() {
        Vector2D position = new Vector2D(getPosition().getX(), getPosition().getY());
        EnemyProjectile clone = new EnemyProjectile(position, strategy, getImage());
        clone.lives = getLives();
        return clone;
    }
}
