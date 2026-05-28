import dk.sdu.cbse.common.services.IPointService;
import dk.sdu.cbse.scoreclient.ScoreServiceClient;

module ScoreClient {
    requires Common;
    requires spring.web;

    provides IPointService with ScoreServiceClient;
}
