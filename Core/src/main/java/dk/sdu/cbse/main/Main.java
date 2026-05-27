package dk.sdu.cbse.main;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage window) throws Exception {


        var context = new AnnotationConfigApplicationContext(ModuleConfig.class);

        Game game = context.getBean(Game.class);
        game.start(window);
        game.render();
    }
}