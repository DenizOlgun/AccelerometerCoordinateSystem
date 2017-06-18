import org.junit.*;

import java.util.function.Function;

import static org.junit.Assert.*;

import static java.lang.Math.*;

/**
 * Created by 4924_Users on 6/10/2017.
 */
public class Tests  {

    double startAngle;
    Function<Double, Double> acceleration;
    double totalTime;
    double[] expectedResult = new double[2];
    double[] actualResult;
    final double DELTA = 0.01;
    final double X_ANGLE = 90; //y = 0
    final double Y_ANGLE = 0; //x = 0
    final double XY_ANGLE = 90 - atan(2)*180/PI; //y = 2x

    @Before
    public void reset() { //currentTime is now reset in CoordinateSystemBase, initialization doesn't mess up the timing, but apparently the exception check takes more than 0.05 seconds to start

        CoordinateSystemBase.reset();
    }

    @Test
    public void noAccelerationXTest() {

        startAngle = X_ANGLE;
        acceleration = t -> 0D;
        totalTime = 1D;
        expectedResult[0] = 0;
        expectedResult[1] = 0;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void constantAccelerationXTest() { //consistently 1% too large

        startAngle = X_ANGLE;
        acceleration = t -> 2D;
        totalTime = 5D;
        expectedResult[0] = acceleration.apply(totalTime) * pow(totalTime, 2) / 2D;
        expectedResult[1] = 0;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void noAccelerationYTest() {

        startAngle = Y_ANGLE;
        acceleration = t -> 0D;
        totalTime = 1D;
        expectedResult[0] = 0;
        expectedResult[1] = 0;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void constantAccelerationYTest() { //consistently 1% too large

        startAngle = Y_ANGLE;
        acceleration = t -> 2D;
        totalTime = 5D;
        expectedResult[0] = 0;
        expectedResult[1] = acceleration.apply(totalTime) * pow(totalTime, 2) / 2D;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void constantAccelerationXYTest() {

        startAngle = XY_ANGLE;
        acceleration = t -> 2D;
        totalTime = 5D;
        expectedResult[0] = acceleration.apply(totalTime) * pow(totalTime, 2) / (2D * sqrt(5));
        expectedResult[1] = expectedResult[0] * 2;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void noAccelerationXYTest() {

        startAngle = XY_ANGLE;
        acceleration = t -> 0D;
        totalTime = 1D;
        expectedResult[0] = 0;
        expectedResult[1] = 0;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void linearAccelerationXTest() {

        startAngle = X_ANGLE;
        acceleration = t -> 2*t;
        totalTime = 5D;
        expectedResult[0] = acceleration.apply(1D) * pow(totalTime, 3) / 6D;
        expectedResult[1] = 0;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void linearAccelerationYTest() {

        startAngle = Y_ANGLE;
        acceleration = t -> 2*t;
        totalTime = 5D;
        expectedResult[0] = 0;
        expectedResult[1] = acceleration.apply(1D) * pow(totalTime, 3) / 6D;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void linearAccelerationXYTest() {

        startAngle = XY_ANGLE;
        acceleration = t -> 2*t;
        totalTime = 5D;
        expectedResult[0] = acceleration.apply(1D) * pow(totalTime, 3) / (6D * sqrt(5));
        expectedResult[1] = expectedResult[0] * 2;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void sinusoidalAccelerationXTest() {

        startAngle = X_ANGLE;
        acceleration = t -> 2*sin(t);
        totalTime = 5D;
        expectedResult[0] = (acceleration.apply(totalTime) * -1D) + (totalTime * acceleration.apply(PI/2)); //the addition of totalTime accounts for the arbitrary constant
        expectedResult[1] = 0;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void sinusoidalAccelerationYTest() {

        startAngle = Y_ANGLE;
        acceleration = t -> 2*sin(t);
        totalTime = 5D;
        expectedResult[0] = 0;
        expectedResult[1] = (acceleration.apply(totalTime) * -1D) + (totalTime * acceleration.apply(PI/2)); //the addition of totalTime accounts for the arbitrary constant
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }

    @Test
    public void sinusoidalAccelerationXYTest() {

        startAngle = XY_ANGLE;
        acceleration = t -> 2*sin(t);
        totalTime = 5D;
        expectedResult[0] = (acceleration.apply(totalTime) * -1D + totalTime * acceleration.apply(PI/2)) / sqrt(5);
        expectedResult[1] = expectedResult[0] * 2;
        actualResult = CoordinateSystemBase.calculateMove(startAngle, acceleration, totalTime);
        assertArrayEquals(expectedResult, actualResult, DELTA);
    }
}
