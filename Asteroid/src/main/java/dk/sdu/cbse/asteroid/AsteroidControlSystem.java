package dk.sdu.cbse.asteroid;

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

public class AsteroidControlSystem implements IEntityProcessingService {

    private final Random random = new Random();

    @Override
    public void process(GameData gameData, World world) {
        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));

            asteroid.setX(asteroid.getX() + changeX);
            asteroid.setY(asteroid.getY() + changeY);

            if (asteroid.getX() < 0) asteroid.setX(gameData.getDisplayWidth());
            if (asteroid.getX() > gameData.getDisplayWidth()) asteroid.setX(0);
            if (asteroid.getY() < 0) asteroid.setY(gameData.getDisplayHeight());
            if (asteroid.getY() > gameData.getDisplayHeight()) asteroid.setY(0);
        }
    }

    private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(ServiceLocator.INSTANCE.getModuleLayer(), BulletSPI.class)
                .stream()
                .map(ServiceLoader.Provider::get)
                .collect(Collectors.toList());
    }
}