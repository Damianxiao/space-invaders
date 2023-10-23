package invaders.gameobject;

import invaders.engine.GameEngine;

import java.io.Serializable;

// contains basic methods that all GameObjects must implement
public interface GameObject extends Serializable {

    public void start();
    public void update(GameEngine model);

    public boolean isAlive();
    
    public Object clones();


}
