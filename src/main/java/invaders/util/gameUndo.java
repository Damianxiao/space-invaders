package invaders.util;

import java.util.Stack;

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
}
