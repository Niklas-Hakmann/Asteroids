import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;

module Asteroid {
    requires Common;
    requires CommonBullet;
    requires javafx.graphics;

    uses dk.sdu.cbse.common.bullet.BulletSPI;

    provides IGamePluginService
            with dk.sdu.cbse.asteroid.AsteroidPlugin;

    provides IEntityProcessingService
            with dk.sdu.cbse.asteroid.AsteroidControlSystem,
                    dk.sdu.cbse.asteroid.AsteroidPlugin;
}