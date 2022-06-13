package fitness_means_and_stds;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.um.feri.ears.algorithms.Algorithm;
import org.um.feri.ears.algorithms.DummyAlgorithm;
import org.um.feri.ears.algorithms.so.abc.ABC;
import org.um.feri.ears.algorithms.so.hsa.HSA;
import org.um.feri.ears.algorithms.so.jade.JADE;
import org.um.feri.ears.algorithms.so.laf.LaF;
import org.um.feri.ears.algorithms.so.pso.PSO;
import org.um.feri.ears.algorithms.so.random.RandomWalkAlgorithm;
import org.um.feri.ears.algorithms.so.soma.SOMA;
import org.um.feri.ears.algorithms.so.tlbo.TLBOAlgorithm;
import org.um.feri.ears.problems.*;
import org.um.feri.ears.problems.misc.SoilModelProblem;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;

public class FitnessStatisticsRun {

    static DecimalFormat formatter = new DecimalFormat("#.#####E0");

    public static void main(String[] args) {

        long timeLimit = 2500;
        int maxEvaluations;
        StopCriterion stopCriterion;
        //maxEvaluations = 100000;
        //maxEvaluations = 300000;
        //maxEvaluations = 500000;
        maxEvaluations = 1000000;
        int maxIterations = maxEvaluations / 50;
        //stopCriterion = StopCriterion.EVALUATIONS;
        //stopCriterion = StopCriterion.ITERATIONS;
        stopCriterion = StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS;
        //stopCriterion = StopCriterion.CPU_TIME;

        int numberOfRuns = 30;

        ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();

        algorithms.add(new ABC(50));
        algorithms.add(new JADE(50));
        algorithms.add(new LaF(50));
        algorithms.add(new TLBOAlgorithm(50));
        algorithms.add(new SOMA(50));
        algorithms.add(new PSO(50));
        algorithms.add(new HSA(50));
        algorithms.add(new RandomWalkAlgorithm());

        // Uncomment the problems for which you want to get the data

        ArrayList<Problem> problems = new ArrayList<Problem>();

/*
        problems.add(new Beale());
        problems.add(new Easom());
        problems.add(new Matyas());
        problems.add(new Bohachevsky1());
        problems.add(new Booth());
        problems.add(new Michalewicz2());
        problems.add(new Schaffer1());
        problems.add(new SixHumpCamelBack());
        problems.add(new Bohachevsky2());
        problems.add(new Bohachevsky3());
        problems.add(new Shubert1(2));
        problems.add(new Colville());
        problems.add(new Michalewicz5());
        problems.add(new Zakharov(30));
        problems.add(new Michalewicz10());
        problems.add(new Step1(30));
        problems.add(new Sphere(30));
        problems.add(new SumSquares(30));
        problems.add(new Quartic(30));
        problems.add(new Schwefel222(30));
        problems.add(new Schwefel12());
        problems.add(new RosenbrockDeJong2(30));
        problems.add(new DixonPrice(30));
        problems.add(new Rastrigin(30));
        problems.add(new Griewank(30));
        problems.add(new Ackley1(30));
        problems.add(new DropWave());
        problems.add(new Hartman3());
        problems.add(new Hartman6());
        problems.add(new Shekel5());
        problems.add(new Shekel7());
        problems.add(new Shekel10());
        problems.add(new Branin1());
        problems.add(new GoldsteinPrice());
        problems.add(new Salomon(5));
        problems.add(new Salomon(6));
*/

        problems.add(new SoilModelProblem(5, 3,"TE1"));
        problems.add(new SoilModelProblem(5, 3,"TE2"));
        problems.add(new SoilModelProblem(5, 3,"TE3"));


        StringBuilder sb = new StringBuilder();
        sb.append("\\hline").append("\n");
        sb.append("\\textbf{Function}");

        for (int i = 0; i < algorithms.size(); i++) {
            sb.append(" & \\textbf{").append(algorithms.get(i).getId()).append("}");
        }

        sb.append(" \\\\ ").append(" \\hline").append("\n");


        for (int i = 0; i < problems.size(); i++) {

            if (stopCriterion == StopCriterion.CPU_TIME) {
                long start;
                int numOfEvaluations = 1000000;
                int warmupIterations = 1000;
                double[] results = new double[numOfEvaluations];
                for (int x = 0; x < warmupIterations; x++) {
                    problems.get(i).getRandomEvaluatedSolution();
                }

                for (int j = 0; j < numOfEvaluations; j++) {
                    start = System.nanoTime();
                    problems.get(i).getRandomEvaluatedSolution();
                    results[j] = (System.nanoTime() - start) / 1e6;
                }
                Mean mean = new Mean();
                double avg = mean.evaluate(results);
                timeLimit = (long) (avg * maxEvaluations);
            }

            Task task = new Task(problems.get(i), stopCriterion, maxEvaluations, timeLimit, maxIterations);

            sb.append("\\multirow{2}{*}{$f_{").append(i + 1).append("}$} & ");

            ArrayList<Statistics> stats = new ArrayList<>();

            for (int j = 0; j < algorithms.size(); j++) {
                stats.add(getStats(algorithms.get(j), task, numberOfRuns));
            }

            for (int j = 0; j < stats.size(); j++) {
                sb.append(formatter.format(stats.get(j).average));

                if (j + 1 < stats.size())
                    sb.append(" & ");
            }
            sb.append(" \\\\").append("\n");

            for (int j = 0; j < stats.size(); j++) {
                sb.append(" & $\\pm$").append(formatter.format(stats.get(j).std));
            }
            sb.append(" \\\\").append("\n").append("\\hline");
        }
        System.out.println(sb.toString());
    }

    public static Statistics getStats(Algorithm algorithm, Task task, int numberOfRuns) {

        double avg = 0, std = 0;
        Mean mean = new Mean();
        StandardDeviation standardDeviation = new StandardDeviation();
        double[] values = new double[numberOfRuns];

        for (int r = 0; r < numberOfRuns; r++) {
            try {
                DoubleSolution best = algorithm.execute(task);
                values[r] = best.getEval();
            } catch (StopCriterionException e) {
                e.printStackTrace();
            }
        }

        avg = mean.evaluate(values);
        std = standardDeviation.evaluate(values, avg);

        return new Statistics(avg, std, Arrays.stream(values).min().getAsDouble(), Arrays.stream(values).max().getAsDouble());
    }

    public static class Statistics {
        public Statistics(double average, double std, double min, double max) {
            this.average = average;
            this.std = std;
            this.min = min;
            this.max = max;
        }

        public double average;
        public double std;
        public double min;
        public double max;
    }
}
