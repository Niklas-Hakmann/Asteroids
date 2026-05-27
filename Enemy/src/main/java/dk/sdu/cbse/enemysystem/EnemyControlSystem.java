package dk.sdu.cbse.enemysystem;

import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.util.ServiceLocator;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;
import java.util.stream.Collectors;

public class EnemyControlSystem implements IEntityProcessingService {

    private final Random random = new Random();

    @Override
    public void process(GameData gameData, World world) {

        for (Entity enemy : world.getEntities(Enemy.class)) {

            enemy.setRotation(enemy.getRotation() + random.nextInt(11) - 6);

            double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
            double changeY = Math.sin(Math.toRadians(enemy.getRotation()));

            enemy.setX(enemy.getX() + changeX);
            enemy.setY(enemy.getY() + changeY);

            if (random.nextDouble() < 0.02) {
                getBulletSPIs().stream().findFirst().ifPresent(
                        spi -> world.addEntity(spi.createBullet(enemy, gameData))
                );
            }

            if (enemy.getX() < 0) {
                enemy.setX(1);
            }

            if (enemy.getX() > gameData.getDisplayWidth()) {
                enemy.setX(gameData.getDisplayWidth() - 1);
            }

            if (enemy.getY() < 0) {
                enemy.setY(1);
            }

            if (enemy.getY() > gameData.getDisplayHeight()) {
                enemy.setY(gameData.getDisplayHeight() - 1);
            }
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(ServiceLocator.INSTANCE.getModuleLayer(), BulletSPI.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }
}