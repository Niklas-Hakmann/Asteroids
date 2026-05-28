package dk.sdu.cbse.pointsystem;

import dk.sdu.cbse.common.services.IPointService;

public class PointSystem implements IPointService {
    private int score = 0;

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void addPoint() {
        score++;
    }

    @Override
    public void deductPoint() {
        if(score>0)score--;
    }

    @Override
    public void reset() {
        score = 0;
    }
}