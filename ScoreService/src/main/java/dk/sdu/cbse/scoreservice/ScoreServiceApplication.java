package dk.sdu.cbse.scoreservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Entry point of the scoring micro service. Started independently of the game
 * (its own JVM/process) and exposes the score over HTTP, see {@link ScoreController}.
 */
@SpringBootApplication
public class ScoreServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScoreServiceApplication.class, args);
    }
}
