module Collision {
    exports dk.sdu.cbse.collisionsystem;
    requires Common;
    uses dk.sdu.cbse.common.services.IPointService;
    provides dk.sdu.cbse.common.services.IPostEntityProcessingService
            with dk.sdu.cbse.collisionsystem.CollisionDetector;
}