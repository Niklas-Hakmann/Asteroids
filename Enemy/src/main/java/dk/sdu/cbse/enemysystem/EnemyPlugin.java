package dk.sdu.cbse.enemysystem;
import javafx.scene.paint.Color;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IGamePluginService;

public class EnemyPlugin implements IGamePluginService{
    private Entity enemy;

    public EnemyPlugin() {
    }

    @Override
    public void start(GameData gameData, World world) {

        // Add entities to the world
        enemy = createEnemyShip(gameData);
        world.addEntity(enemy);
    }

    private Entity createEnemyShip(GameData gameData) {
        Entity enemyShip = new Enemy();
        enemyShip.setType("ENEMY");
        enemyShip.setHealthPoints(5);
        enemyShip.setPolygonCoordinates(-5,-5,10,0,-5,5);
        enemyShip.setX(gameData.getDisplayWidth() * 0.8);
        enemyShip.setY(gameData.getDisplayHeight() * 0.8);
        enemyShip.setRadius(8);
        enemyShip.setColor(Color.RED);
        return enemyShip;
    }


    @Override
    public void stop(GameData gameData, World world) {
        // Remove entities
        world.removeEntity(enemy);
    }

}

