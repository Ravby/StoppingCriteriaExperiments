package rating_intervals;

import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.hsa.HSA;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.laf.LaF;
import org.um.feri.ears.algorithms.so.pso.PSO;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.soma.SOMA;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.util.Util;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class ThirtySixProblemsBenchmarkRun {
    public static void main(String[] args) {

        Util.rnd.setSeed(System.currentTimeMillis());

        int populationSize = 50;
        int numberOfRuns = 30;
        Benchmark.printInfo = false; //prints one on one results
        ArrayList<Algorithm> players = new ArrayList<Algorithm>();

        players.add(new SOMA(populationSize));
        players.add(new ABC(populationSize));
        players.add(new TLBOAlgorithm(populationSize));
        players.add(new PSO(populationSize));
        players.add(new JADE(populationSize));
        players.add(new LaF(populationSize));
        players.add(new HSA(populationSize));
        players.add(new RandomWalkAlgorithm());

        ThirtySixProblemsBenchmark benchmark = new ThirtySixProblemsBenchmark();
        benchmark.setDisplayRatingCharts(true);
        benchmark.addAlgorithms(players);

        long start = System.nanoTime();
        benchmark.run(numberOfRuns);
        System.out.println("Benchmark duration: "+ TimeUnit.NANOSECONDS.toMillis( System.nanoTime()-start));
    }
}
