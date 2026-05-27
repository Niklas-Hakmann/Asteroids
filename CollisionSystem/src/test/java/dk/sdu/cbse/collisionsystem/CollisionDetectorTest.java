package dk.sdu.cbse.collisionsystem;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IPointService;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class CollisionDetectorTest {

    //Stub for pointsystem
    private static class StubPointService implements IPointService {
        private int score = 0;

        @Override
        public int getScore() { return score; }

        @Override
        public void addPoint() { score++; }

        @Override
        public void deductPoint() { score--; }
    }
    //Stub for entity
    private static class StubEntity extends Entity {
        private final String type;

        public StubEntity(String type, double x, double y, float radius, int hp) {
            this.type = type;
            setX(x);
            setY(y);
            setRadius(radius);
            setHealthPoints(hp);
        }

        @Override
        public String getType() { return type; }
    }

    private StubPointService pointService;
    private CollisionDetector detector;
    private World world;
    private GameData gameData;

    @Before
    public void setUp() {
        pointService = new StubPointService();
        detector = new CollisionDetector(pointService);
        world = new World();
        gameData = new GameData();
    }

    @Test
    public void bulletKillsAsteroidShouldAddPoint() {
        Entity bullet = new StubEntity("BULLET", 0, 0, 5, 1);
        Entity asteroid = new StubEntity("ASTEROID", 0, 0, 5, 1);
        world.addEntity(bullet);
        world.addEntity(asteroid);

        detector.process(gameData, world);

        assertEquals(1, pointService.getScore());
    }

    @Test
    public void bulletHitsAsteroidButDoesNotKillShouldNotAddPoint() {
        Entity bullet = new StubEntity("BULLET", 0, 0, 5, 1);
        Entity asteroid = new StubEntity("ASTEROID", 0, 0, 5, 2); // 2 hp
        world.addEntity(bullet);
        world.addEntity(asteroid);

        detector.process(gameData, world);

        assertEquals(0, pointService.getScore());
    }

    @Test
    public void playerHitsAsteroidShouldNotAddPoint() {
        Entity player = new StubEntity("PLAYER", 0, 0, 5, 3);
        Entity asteroid = new StubEntity("ASTEROID", 0, 0, 5, 1);
        world.addEntity(player);
        world.addEntity(asteroid);

        detector.process(gameData, world);

        assertEquals(0, pointService.getScore());
    }

    @Test
    public void playerHitsAsteroidShouldReducePlayerHealth() {
        Entity player = new StubEntity("PLAYER", 0, 0, 5, 3);
        Entity asteroid = new StubEntity("ASTEROID", 0, 0, 5, 1);
        world.addEntity(player);
        world.addEntity(asteroid);

        detector.process(gameData, world);

        assertEquals(2, player.getHealthPoints());
    }

    @Test
    public void noCollisionShouldNotAddPoint() {
        Entity bullet = new StubEntity("BULLET", 0, 0, 5, 1);
        Entity asteroid = new StubEntity("ASTEROID", 1000, 1000, 5, 1); // langt væk
        world.addEntity(bullet);
        world.addEntity(asteroid);

        detector.process(gameData, world);

        assertEquals(0, pointService.getScore());
    }

    @Test
    public void bulletKillsAsteroidShouldRemoveAsteroidFromWorld() {
        Entity bullet = new StubEntity("BULLET", 0, 0, 5, 1);
        Entity asteroid = new StubEntity("ASTEROID", 0, 0, 5, 1);
        world.addEntity(bullet);
        world.addEntity(asteroid);

        detector.process(gameData, world);

        assertFalse(world.getEntities().contains(asteroid));
    }
}