package invaders.entities;

import invaders.engine.GameEngine;
import invaders.physics.Vector2D;
import invaders.rendering.Renderable;

import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.io.Serializable;

public class SpaceBackground implements Renderable, Serializable {
	private Rectangle space;
	private Pane pane;
    private GameEngine model;

	@Override
	public String getStrategy() {
		return null;
	}

	public SpaceBackground(GameEngine engine, Pane pane){
		double width = pane.getWidth();
		double height = pane.getHeight();
		space = new Rectangle(0, 0, width, height);
		space.setFill(Paint.valueOf("BLACK"));
		space.setViewOrder(1000.0);

		pane.getChildren().add(space);
	}

	public Image getImage() {
		return null;
	}

	@Override
	public double getWidth() {
		return 0;
	}

	@Override
	public double getHeight() {
		return 0;
	}

	@Override
	public Vector2D getPosition() {
		return null;
	}

	@Override
	public Layer getLayer() {
		return Layer.BACKGROUND;
	}

	@Override
	public boolean isAlive() {
		return true;
	}

	@Override
	public void takeDamage(double amount) {}

	@Override
	public double getHealth() {
		return 0;
	}

	@Override
	public String getRenderableObjectName() {
		return "background";
	}

	@Override
	public String getEnemyLevel(Renderable renderable) {
		return null;
	}
}
