package invaders.factory;

import invaders.engine.GameEngine;
import invaders.gameobject.GameObject;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;
import invaders.strategy.ProjectileStrategy;
import javafx.scene.image.Image;

import java.io.File;

public class PlayerProjectile extends Projectile {
    private ProjectileStrategy strategy;

    public PlayerProjectile(Vector2D position, ProjectileStrategy strategy) {
        super(position, new Image(new File("src/main/resources/player_shot.png").toURI().toString(), 10, 10, true, true));
        this.strategy = strategy;
    }
    @Override
    public void update(GameEngine model) {
        strategy.update(this);

        if(this.getPosition().getY() <= this.getImage().getHeight()){
            this.takeDamage(1);
        }
    }
    @Override
    public String getRenderableObjectName() {
        return "PlayerProjectile";
    }

    @Override
    public String getStrategy() {
        return null;
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
        PlayerProjectile clone = new PlayerProjectile(position, strategy);
        clone.lives = getLives();
        return clone;
    }
}
