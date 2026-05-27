package dk.sdu.cbse.pointsystem;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;


import static org.junit.jupiter.api.Assertions.*;

public class PointSystemTest {
    private PointSystem pointSystem;

    @Before
    public void setUp(){
        pointSystem = new PointSystem();
    }

    @Test
    public void initialShouldBeZero(){
        assertEquals(0, pointSystem.getScore());
    }

    @Test
    public void addPointShouldIncreaseScoreByOne() {
        pointSystem.addPoint();
        assertEquals(1, pointSystem.getScore());
    }

    @Test
    public void addMultipleShouldIncrease(){
        pointSystem.addPoint();
        pointSystem.addPoint();
        pointSystem.addPoint();
        assertEquals(3, pointSystem.getScore());
    }

    @Test
    public void deductShouldDecreaseByOne(){
        pointSystem.addPoint();
        pointSystem.deductPoint();
        assertEquals(0, pointSystem.getScore());
    }

    @Test
    public void deductMultipleShouldDecrease(){
        pointSystem.addPoint();
        pointSystem.addPoint();
        pointSystem.addPoint();
        pointSystem.deductPoint();
        pointSystem.deductPoint();
        pointSystem.deductPoint();
        assertEquals(0, pointSystem.getScore());
    }

    @Test
    public void deductOnlyOverZero(){
        pointSystem.deductPoint();
        assertEquals(0, pointSystem.getScore());
    }

}