package dk.sdu.cbse.scoreservice;

import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/score")
public class ScoreController {

    private final AtomicInteger score = new AtomicInteger(0);

    @GetMapping
    public int getScore() {
        return score.get();
    }

    @PostMapping("/add")
    public int addPoint() {
        return score.incrementAndGet();
    }

    @PostMapping("/deduct")
    public int deductPoint() {
        return score.updateAndGet(current -> current > 0 ? current - 1 : 0);
    }

    @PostMapping("/reset")
    public int reset() {
        score.set(0);
        return score.get();
    }
}
