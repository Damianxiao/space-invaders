package invaders.score;

import invaders.rendering.Renderable;

import static invaders.engine.GameWindow.updateScore;

public class scoreBroad {
    /**
     * @Description :
     * calculate point
     */
    public static  void updateScoreCount(Renderable renderable){
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
}
