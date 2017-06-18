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

    static final double timeBetweenReadings = 0.005/pow(2,2);

    static final double timeBetweenReadingsSquared = pow(timeBetweenReadings, 2);

    public static double elapsedTime() { //seconds

        return (System.nanoTime()/pow(10,9)) - startTime;
    }

    public static void delayUntil(double delayAmt) { //delays for `delayAmt` seconds after the program start

        while (elapsedTime() < delayAmt) ;
    }

    public static double[] calculateMove(double angle, Function<Double, Double> acceleration, double totalTime) {

        double sum = 0;
        double accelerationResult;
        double benchmark = timeBetweenReadings; //initial benchmark
        Function<Double, Double> timeForNextReading = (a) -> {

            double foo = elapsedTime() / timeBetweenReadings;
            if(rint(foo) == foo) return timeBetweenReadings * (foo + 1);
            else return timeBetweenReadings * ceil(foo);
        };

        startTime = System.nanoTime()/pow(10,9);
        while (elapsedTime() < totalTime - timeBetweenReadings) {

            if(elapsedTime() > benchmark) throw new RuntimeException("Benchmark was passed due to small margins; increase the value of TIME_BETWEEN_READINGS" + " currentTime: " + elapsedTime() + " benchmark: " + benchmark); //this line causes first number to be skipped
            delayUntil(benchmark);
            accelerationResult = acceleration.apply(elapsedTime());
            benchmark = timeForNextReading.apply(-1D);
            sum += accelerationResult; // * TIME_BETWEEN_READINGS; should be multiplied with TIME_BETWEEN_READINGS, but it's faster to multiply with the squared version, so it helps not miss the benchmark
            incrementX(sum * timeBetweenReadingsSquared * Math.sin(angle * PI/180D));
            incrementY(sum * timeBetweenReadingsSquared * Math.cos(angle * PI/180D));

        }

        System.out.printf("%f\n", elapsedTime());
        System.out.println();
        System.out.println("Calculated x: " + x);
        System.out.println("Calculated y: " + y);
        System.out.println();
        System.out.println("Calculated magnitude: " + sqrt(x*x + y*y));
        System.out.println();
        System.out.println("Time between each reading: " + timeBetweenReadings);
        System.out.println("Fixed angle: " + angle + " degrees");
        System.out.println("Final Velocity: " + sum * timeBetweenReadings);

        double[] result = new double[2];
        //double correctionFactor = 100D/101;
        result[0] = x;// * 100D / 101D;
        result[1] = y;// * 100D / 101D;
        return result;
    }

    public static void reset() { //see the reset() comment in Tests

        x = 0;
        y = 0;
    }

    public static void main(String[] args) {

    }
}