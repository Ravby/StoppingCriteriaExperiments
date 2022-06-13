package rating_intervals;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.de.DEAlgorithm;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class DEvariationsRun {
    public static void main(String[] args) {

        Util.rnd.setSeed(System.currentTimeMillis());

        int populationSize = 50;
        int numberOfRuns = 30;
        Benchmark.printInfo = false; //prints one on one results
        ArrayList<Algorithm> players = new ArrayList<Algorithm>();

        players.add(new JADE(populationSize));
        players.add(new DEAlgorithm(DEAlgorithm.Strategy.DE_RAND_1_BIN, populationSize));
        players.add(new DEAlgorithm(DEAlgorithm.Strategy.DE_BEST_1_EXP, populationSize));
        players.add(new DEAlgorithm(DEAlgorithm.Strategy.DE_BEST_1_BIN, populationSize));
        players.add(new DEAlgorithm(DEAlgorithm.Strategy.DE_RAND_TO_BEST_1_BIN, populationSize));
        players.add(new DEAlgorithm(DEAlgorithm.Strategy.JDE_RAND_1_BIN, populationSize));

        // Uncomment the benchmark you want to run
        ThirtySixProblemsBenchmark benchmark = new ThirtySixProblemsBenchmark();
        //SoilModelBenchmark benchmark = new SoilModelBenchmark();

        benchmark.setDisplayRatingCharts(true);
        benchmark.addAlgorithms(players);

        long start = System.nanoTime();
        benchmark.run(numberOfRuns);
        System.out.println("Benchmark duration: "+ TimeUnit.NANOSECONDS.toMillis( System.nanoTime()-start));
    }
}
