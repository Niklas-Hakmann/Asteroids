package dk.sdu.cbse.bulletsystem;

import dk.sdu.cbse.common.bullet.Bullet;
import dk.sdu.cbse.common.bullet.BulletSPI;
import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import javafx.scene.paint.Color;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {

    private static final double BULLET_SPEED = 3;
    private static final double BULLET_SPAWN_DISTANCE = 10;

    @Override
    public void process(GameData gameData, World world) {
        for (Entity bullet : world.getEntities(Bullet.class)) {
            moveBullet(bullet);
        }
    }

    private void moveBullet(Entity bullet) {
        double directionX = Math.cos(Math.toRadians(bullet.getRotation()));
        double directionY = Math.sin(Math.toRadians(bullet.getRotation()));
        bullet.setX(bullet.getX() + directionX * BULLET_SPEED);
        bullet.setY(bullet.getY() + directionY * BULLET_SPEED);
    }

    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = buildBullet(shooter);
        return bullet;
    }

    private Entity buildBullet(Entity shooter) {
        double directionX = Math.cos(Math.toRadians(shooter.getRotation()));
        double directionY = Math.sin(Math.toRadians(shooter.getRotation()));

        Entity bullet = new Bullet();
        bullet.setType("BULLET");
        bullet.setColor(Color.WHITE);
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);
        bullet.setX(shooter.getX() + directionX * BULLET_SPAWN_DISTANCE);
        bullet.setY(shooter.getY() + directionY * BULLET_SPAWN_DISTANCE);
        bullet.setRotation(shooter.getRotation());
        bullet.setRadius(1);
        return bullet;
    }
}