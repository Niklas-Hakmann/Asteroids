package dk.sdu.cbse.asteroid;

import javafx.scene.paint.Color;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AsteroidPlugin implements IGamePluginService, IEntityProcessingService {

    private final List<Entity> asteroids = new ArrayList<>();
    private final Random random = new Random();
    private static final int MAX_ASTEROIDS = 5;
    private static final int SPAWN_INTERVAL = 300;
    private static final float BIG_RADIUS = 30f;
    private static final float MEDIUM_RADIUS = 18f;
    private static final float SMALL_RADIUS = 10f;
    private int frameCount = 0;

    public AsteroidPlugin() {}

    @Override
    public void start(GameData gameData, World world) {
        for (int i = 0; i < 3; i++) {
            spawnAsteroid(gameData, world);
        }
    }

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> destroyed = new ArrayList<>();
        asteroids.removeIf(a -> {
            if (!world.getEntities().contains(a)) {
                destroyed.add(a);
                return true;
            }
            return false;
        });
        for (Entity old : destroyed) {
            splitAsteroid(old, world);
        }

        frameCount++;
        if (frameCount >= SPAWN_INTERVAL && asteroids.size() < MAX_ASTEROIDS) {
            spawnAsteroid(gameData, world);
            frameCount = 0;
        }
    }

    private void spawnAsteroid(GameData gameData, World world) {
        Entity asteroid = createAsteroid(gameData);
        asteroids.add(asteroid);
        world.addEntity(asteroid);
    }

    private void splitAsteroid(Entity old, World world) {
        float newRadius;
        if (old.getRadius() >= BIG_RADIUS) {
            newRadius = MEDIUM_RADIUS;
        } else if (old.getRadius() >= MEDIUM_RADIUS) {
            newRadius = SMALL_RADIUS;
        } else {
            return;
        }
        for (int i = 0; i < 2; i++) {
            Entity child = buildAsteroid(newRadius);
            child.setX(old.getX());
            child.setY(old.getY());
            child.setRotation(random.nextInt(360));
            asteroids.add(child);
            world.addEntity(child);
        }
    }

    private Entity createAsteroid(GameData gameData) {
        Entity asteroid = buildAsteroid(BIG_RADIUS);
        asteroid.setX(random.nextInt(gameData.getDisplayWidth()));
        asteroid.setY(random.nextInt(gameData.getDisplayHeight()));
        asteroid.setRotation(random.nextInt(360));
        return asteroid;
    }

    private Entity buildAsteroid(float radius) {
        Entity asteroid = new Asteroid();
        asteroid.setType("ASTEROID");
        asteroid.setHealthPoints(4);
        asteroid.setPolygonCoordinates(buildAsteroidShape(radius));
        asteroid.setRadius(radius);
        asteroid.setColor(Color.GREY);
        return asteroid;
    }

    private double[] buildAsteroidShape(double radius) {
        int points = 12;
        double[] coords = new double[points * 2];
        for (int i = 0; i < points; i++) {
            double angle = (2 * Math.PI * i) / points;
            double jitter = 0.65 + random.nextDouble() * 0.55;
            double r = radius * jitter;
            coords[i * 2] = Math.cos(angle) * r;
            coords[i * 2 + 1] = Math.sin(angle) * r;
        }
        return coords;
    }

    @Override
    public void stop(GameData gameData, World world) {
        asteroids.forEach(world::removeEntity);
        asteroids.clear();
    }
}