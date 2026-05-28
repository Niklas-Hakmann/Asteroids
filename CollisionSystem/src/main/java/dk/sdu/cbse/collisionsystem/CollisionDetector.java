package dk.sdu.cbse.collisionsystem;
import dk.sdu.cbse.common.services.IPointService;
import dk.sdu.cbse.common.util.ServiceLocator;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollisionDetector implements IPostEntityProcessingService {
    private IPointService pointService;
    //Not Testable without constructor
    public CollisionDetector(IPointService pointService) {
        this.pointService = pointService;
    }


    public CollisionDetector() {
        this.pointService = ServiceLocator.INSTANCE.locateAll(IPointService.class)
                .stream()
                .findFirst()
                .orElse(null);
    }
    private final Set<String> activeCollisions = new HashSet<>();

    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());
        Set<String> currentCollisions = new HashSet<>();
        Set<String> removed = new HashSet<>();

        for (int i = 0; i < entities.size(); i++) {
            for (int j = i + 1; j < entities.size(); j++) {
                Entity a = entities.get(i);
                Entity b = entities.get(j);

                if (collides(a, b)) {
                    String key = a.getID() + "_" + b.getID();
                    currentCollisions.add(key);

                    if (!activeCollisions.contains(key)) {
                        handleCollision(a, b, world, removed);
                    }
                }
            }
        }

        activeCollisions.clear();
        activeCollisions.addAll(currentCollisions);
    }

    private void handleCollision(Entity a, Entity b, World world, Set<String> removed) {

        boolean aPlayer = "PLAYER".equals(a.getType());
        boolean bPlayer = "PLAYER".equals(b.getType());
        boolean aAsteroid = "ASTEROID".equals(a.getType());
        boolean bAsteroid = "ASTEROID".equals(b.getType());
        boolean aBullet = "BULLET".equals(a.getType());
        boolean bBullet = "BULLET".equals(b.getType());

        if (aBullet || bBullet) {
            Entity bullet = aBullet ? a : b;
            Entity other = aBullet ? b : a;

            if (removed.contains(bullet.getID())) {
                return;
            }
            if (!removed.contains(other.getID())) {
                other.setHealthPoints(other.getHealthPoints() - 1);
                if (other.getHealthPoints() <= 0) {
                    destroy(other, world, removed);
                    if (pointService != null
                            && ("ASTEROID".equals(other.getType()) || "ENEMY".equals(other.getType()))) {
                        pointService.addPoint();
                    }
                }
            }
            destroy(bullet, world, removed);
            return;
        }

        if (removed.contains(a.getID()) || removed.contains(b.getID())) {
            return;
        }

        if ((aPlayer && bAsteroid) || (aAsteroid && bPlayer)) {
            Entity player = aPlayer ? a : b;
            Entity asteroid = aAsteroid ? a : b;

            player.setHealthPoints(player.getHealthPoints() - 1);
            if (player.getHealthPoints() <= 0) destroy(player, world, removed);
            destroy(asteroid, world, removed);

        } else {
            a.setHealthPoints(a.getHealthPoints() - 1);
            if (a.getHealthPoints() <= 0) destroy(a, world, removed);
            b.setHealthPoints(b.getHealthPoints() - 1);
            if (b.getHealthPoints() <= 0) destroy(b, world, removed);
        }
    }

    private void destroy(Entity entity, World world, Set<String> removed) {
        world.removeEntity(entity);
        removed.add(entity.getID());
    }

    private boolean collides(Entity a, Entity b) {
        double dx = a.getX() - b.getX();
        double dy = a.getY() - b.getY();
        double distance = Math.sqrt(dx * dx + dy * dy);
        return distance < (a.getRadius() + b.getRadius());
    }
}