package dk.sdu.cbse.main;

import dk.sdu.cbse.common.data.Entity;
import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.GameKeys;
import dk.sdu.cbse.common.data.World;
import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import dk.sdu.cbse.common.services.IPointService;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

class Game {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();
    private Text scoreText;

    private final List<IGamePluginService> gamePlugins;
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postEntityProcessors;

    private final List<IPointService> pointServices;


    public Game(List<IGamePluginService> gamePlugins,
                List<IEntityProcessingService> entityProcessors,
                List<IPostEntityProcessingService> postEntityProcessors, List<IPointService> pointServices) {
        this.gamePlugins = gamePlugins;
        this.entityProcessors = entityProcessors;
        this.postEntityProcessors = postEntityProcessors;
        this.pointServices = pointServices;
    }

    public void start(Stage window) throws Exception {
        //midlertidig debugging
        System.out.println("Plugins fundet: " + gamePlugins.size());
        System.out.println("Entity processors fundet: " + entityProcessors.size());
        System.out.println("Post processors fundet: " + postEntityProcessors.size());

        // Ny runde starter altid på 0 (nulstiller også den eksterne score-microservice).
        for (IPointService pointService : pointServices) {
            pointService.reset();
        }

        scoreText = new Text(10, 20, "Score: 0");
        scoreText.setFill(javafx.scene.paint.Color.WHITE);
        gameWindow.getChildren().add(scoreText);
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.setStyle("-fx-background-color: black;");

        Random random = new Random();
        for (int i = 0; i < 200; i++) {
            double size = random.nextDouble() * 2 + 0.5;
            javafx.scene.shape.Circle star = new javafx.scene.shape.Circle(size);
            star.setFill(javafx.scene.paint.Color.YELLOW);
            star.setOpacity(random.nextDouble() * 0.7 + 0.3);
            star.setTranslateX(random.nextInt(gameData.getDisplayWidth()));
            star.setTranslateY(random.nextInt(gameData.getDisplayHeight()));
            gameWindow.getChildren().add(star);
        }
        //gameWindow.getChildren().add(scoreText);

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT))
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            if (event.getCode().equals(KeyCode.RIGHT))
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            if (event.getCode().equals(KeyCode.UP))
                gameData.getKeys().setKey(GameKeys.UP, true);
            if (event.getCode().equals(KeyCode.SPACE))
                gameData.getKeys().setKey(GameKeys.SPACE, true);
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT))
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            if (event.getCode().equals(KeyCode.RIGHT))
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            if (event.getCode().equals(KeyCode.UP))
                gameData.getKeys().setKey(GameKeys.UP, false);
            if (event.getCode().equals(KeyCode.SPACE))
                gameData.getKeys().setKey(GameKeys.SPACE, false);
        });

        // Brug injicerede plugins i stedet for ServiceLoader
        for (IGamePluginService plugin : gamePlugins) {
            plugin.start(gameData, world);
        }

        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygon.setStroke(entity.getColor());
            polygon.setFill(entity.getColor());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }
        }.start();
    }

    private void update() {
        for (IEntityProcessingService service : entityProcessors) {
            service.process(gameData, world);
        }
        for (IPostEntityProcessingService service : postEntityProcessors) {
            service.process(gameData, world);
        }
    }

    private void draw() {
        for (Entity polygonEntity : polygons.keySet()) {
            if (!world.getEntities().contains(polygonEntity)) {
                Polygon removedPolygon = polygons.get(polygonEntity);
                polygons.remove(polygonEntity);
                gameWindow.getChildren().remove(removedPolygon);
            }
        }

        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
            polygon.setStroke(entity.getColor());
            polygon.setFill(entity.getColor());
        }
        if (!pointServices.isEmpty()) {
            scoreText.setText("Score: " + pointServices.get(0).getScore());
        }
    }
}