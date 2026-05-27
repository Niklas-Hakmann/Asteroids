
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.playersystem.PlayerPlugin;

module Player {
    requires Common;
    requires CommonBullet;
    requires javafx.graphics;

    uses dk.sdu.cbse.common.bullet.BulletSPI;
    provides IGamePluginService with PlayerPlugin;
    provides IEntityProcessingService with dk.sdu.cbse.playersystem.PlayerControlSystem;
}
