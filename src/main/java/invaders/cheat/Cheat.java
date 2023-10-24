package invaders.cheat;

import invaders.engine.GameEngine;
import invaders.entities.EntityView;
import invaders.factory.EnemyProjectile;
import invaders.factory.Projectile;
import invaders.gameobject.Enemy;
import invaders.gameobject.GameObject;
import invaders.rendering.Renderable;

import java.util.ArrayList;
import java.util.List;

import static invaders.engine.GameWindow.updateScore;

public class Cheat {

    public static  ArrayList<List<Enemy>> removeSlowEnemy(GameEngine model,List<EntityView> entityViews){
        ArrayList<List<Enemy>> list = new ArrayList<>();
        List<Enemy> slowEnemyToRemove = new ArrayList<>();
        List<Enemy> slowEnemyRenderablesToRemove = new ArrayList<>();
        for (GameObject gameObject : model.getGameObjects()) {
            if(gameObject instanceof Enemy){
                Enemy enemy = (Enemy) gameObject;
                if (enemy.getImage().getUrl().contains("slow")) {
                    slowEnemyToRemove.add(enemy);
                    updateScore(3);
                }
            }
        }
        for (Renderable renderable : model.getRenderables()) {
            if(renderable instanceof Enemy){
                Enemy enemy = (Enemy) renderable;
                if (enemy.getEnemyLevel(renderable).equals("weak")) {
                    slowEnemyRenderablesToRemove.add(enemy);
                    // entity dead,find match view and  delete
                    renderable.takeDamage(100);
                    if (!renderable.isAlive()){
                        for (EntityView entityView : entityViews){
                            if (entityView.matchesEntity(renderable)){
                                // MARKED  but not  delete instantly, will be deleted at  below
                                entityView.markForDelete();
                            }
                        }
                    }
                }
            }
        }
        list.add(slowEnemyToRemove);
        list.add(slowEnemyRenderablesToRemove);
        return list;
    }

    public static  ArrayList<List<Enemy>> removeFastEnemy(GameEngine model,List<EntityView> entityViews){
        ArrayList<List<Enemy>> list = new ArrayList<>();
        List<Enemy> fastEnemyToRemove = new ArrayList<>();
        List<Enemy> fastEnemyRenderablesToRemove = new ArrayList<>();
        for (GameObject gameObject : model.getGameObjects()) {
            if(gameObject instanceof Enemy){
                Enemy enemy = (Enemy) gameObject;
                if (enemy.getImage().getUrl().contains("fast")) {
                    fastEnemyToRemove.add(enemy);
                    updateScore(4);
                }
            }
        }
        for (Renderable renderable : model.getRenderables()) {
            if(renderable instanceof Enemy){
                Enemy enemy = (Enemy) renderable;
                if (enemy.getEnemyLevel(renderable).equals("strong")) {
                    fastEnemyRenderablesToRemove.add(enemy);
                    // entity dead,find match view and  delete
                    renderable.takeDamage(100);
                    if (!renderable.isAlive()){
                        for (EntityView entityView : entityViews){
                            if (entityView.matchesEntity(renderable)){
                                // MARKED  but not  delete instantly, will be deleted at  below
                                entityView.markForDelete();
                            }
                        }
                    }
                }
            }
        }
        list.add(fastEnemyToRemove);
        list.add(fastEnemyRenderablesToRemove);
        return list;
    }

    public static ArrayList<List<Projectile>> removeFastProjectile(GameEngine model,List<EntityView> entityViews){
        ArrayList<List<Projectile>> list = new ArrayList<>();
        List<Projectile> fastProjectilesToRemove = new ArrayList<>();
        List<Projectile> fastProjectilesRenderablesToRemove = new ArrayList<>();
        for (GameObject gameObject : model.getGameObjects()) {
            if(gameObject instanceof EnemyProjectile){
                EnemyProjectile enemyProjectile = (EnemyProjectile) gameObject;
                if (enemyProjectile.getStrategy().contains("Fast")) {
                    fastProjectilesToRemove.add(enemyProjectile);
                    updateScore(2);
                }
            }
        }
        for (Renderable renderable : model.getRenderables()) {
            if(renderable instanceof EnemyProjectile){
                EnemyProjectile enemyProjectile = (EnemyProjectile) renderable;
                if (enemyProjectile.getStrategy().contains("Fast")) {
                    fastProjectilesRenderablesToRemove.add(enemyProjectile);
                    // entity dead,find match view and  delete
                    renderable.takeDamage(100);
                    if (!renderable.isAlive()){
                        for (EntityView entityView : entityViews){
                            if (entityView.matchesEntity(renderable)){
                                // MARKED  but not  delete instantly, will be deleted at  below
                                entityView.markForDelete();
                            }
                        }
                    }
                }
            }
        }
        list.add(fastProjectilesToRemove);
        list.add(fastProjectilesRenderablesToRemove);
        return list;
    }

    public static  ArrayList<List<Projectile>> removeSlowProjectile(GameEngine model,List<EntityView> entityViews){
        ArrayList<List<Projectile>> list = new ArrayList<>();
        List<Projectile> slowProjectilesToRemove = new ArrayList<>();
        List<Projectile> slowProjectilesRenderablesToRemove = new ArrayList<>();
        for (GameObject gameObject : model.getGameObjects()) {
            if(gameObject instanceof EnemyProjectile){
                EnemyProjectile enemyProjectile = (EnemyProjectile) gameObject;
                if (enemyProjectile.getStrategy().contains("Slow")) {
                    slowProjectilesToRemove.add(enemyProjectile);
                    updateScore(1);
                }
            }
        }
        for (Renderable renderable : model.getRenderables()) {
            if(renderable instanceof EnemyProjectile){
                EnemyProjectile enemyProjectile = (EnemyProjectile) renderable;
                if (enemyProjectile.getStrategy().contains("Slow")) {
                    slowProjectilesRenderablesToRemove.add(enemyProjectile);
                    // entity dead,find match view and  delete
                    renderable.takeDamage(100);
                    if (!renderable.isAlive()){
                        for (EntityView entityView : entityViews){
                            if (entityView.matchesEntity(renderable)){
                                // MARKED  but not  delete instantly, will be deleted at  below
                                entityView.markForDelete();
                            }
                        }
                    }
                }
            }
        }
        list.add(slowProjectilesToRemove);
        list.add(slowProjectilesRenderablesToRemove);
        return list;
    }
}
