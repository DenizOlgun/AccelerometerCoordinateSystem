import java.util.function.Function;

import static java.lang.Math.*;

public class CoordinateSystemBase {

    private static double x = 0; //x+ right, x- left
    private static double y = 0; //y+ up, y- down

    public static void incrementX(double incrementVal) {

        x += incrementVal;
    }

    public static void incrementY(double incrementVal) {

        y += incrementVal;
    }

    private static double startTime; //seconds

    static final double TIME_BETWEEN_READINGS = 0.005/pow(2,2);

    static final double TIME_BETWEEN_READINGS_SQUARED = pow(TIME_BETWEEN_READINGS, 2);

    public static double elapsedTime() { //seconds

        return (System.nanoTime()/pow(10,9)) - startTime;
    }

    public static void delayUntil(double delayAmt) { //delays for `delayAmt` seconds after the program start

        while (elapsedTime() < delayAmt) ;
    }

    public static double[] calculateMove(double angle, Function<Double, Double> acceleration, double totalTime) {

        double sum = 0;
        double accelerationResult;
        double timeForNextReading = TIME_BETWEEN_READINGS; //initial timeForNextReading
        Function<Double, Double> getTimeForNextReading = (a) -> {

            double tempStoreValue = elapsedTime() / TIME_BETWEEN_READINGS;
            if(rint(tempStoreValue) == tempStoreValue) return TIME_BETWEEN_READINGS * (tempStoreValue + 1);
            else return TIME_BETWEEN_READINGS * ceil(tempStoreValue);
        };

        startTime = System.nanoTime()/pow(10,9);
        while (elapsedTime() < totalTime - TIME_BETWEEN_READINGS) {

            if(elapsedTime() > timeForNextReading) throw new RuntimeException("Benchmark was passed due to small margins; increase the value of TIME_BETWEEN_READINGS" + " currentTime: " + elapsedTime() + " timeForNextReading: " + timeForNextReading); //this line causes first number to be skipped
            delayUntil(timeForNextReading);
            accelerationResult = acceleration.apply(elapsedTime());
            timeForNextReading = getTimeForNextReading.apply(-1D);
            sum += accelerationResult; // * TIME_BETWEEN_READINGS; should be multiplied with TIME_BETWEEN_READINGS, but it's faster to multiply with the squared version, so it helps not miss the timeForNextReading
            incrementX(sum * TIME_BETWEEN_READINGS_SQUARED * Math.sin(angle * PI/180D));
            incrementY(sum * TIME_BETWEEN_READINGS_SQUARED * Math.cos(angle * PI/180D));
        }

        System.out.printf("%f\n", elapsedTime());
        System.out.println();
        System.out.println("Calculated x: " + x);
        System.out.println("Calculated y: " + y);
        System.out.println();
        System.out.println("Calculated magnitude: " + sqrt(x*x + y*y));
        System.out.println();
        System.out.println("Time between each reading: " + TIME_BETWEEN_READINGS);
        System.out.println("Fixed angle: " + angle + " degrees");
        System.out.println("Final Velocity: " + sum * TIME_BETWEEN_READINGS);

        double[] XYPosition = new double[2];
        XYPosition[0] = x;
        XYPosition[1] = y;
        return XYPosition;
    }

    public static void reset() { //see the reset() comment in Tests

        x = 0;
        y = 0;
    }

    public static void main(String[] args) {

    }
}