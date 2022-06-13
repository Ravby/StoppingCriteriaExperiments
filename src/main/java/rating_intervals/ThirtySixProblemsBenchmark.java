package rating_intervals;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.unconstrained.*;

import java.util.ArrayList;

public class ThirtySixProblemsBenchmark extends Benchmark {
    public ThirtySixProblemsBenchmark() {
        super();
        name="Benchmark containing 36 functions from the paper: A conceptual comparison of several metaheuristic algorithms on continuous optimisation problems";
        shortName = "36Functions";
        drawLimit = 1e-7;
        timeLimit = 2500;
        //maxEvaluations = 100000;
        //maxEvaluations = 300000;
        //maxEvaluations = 500000;
        maxEvaluations = 1000000;
        maxIterations = maxEvaluations / 50;
        stopCriterion = StopCriterion.EVALUATIONS;
        //stopCriterion = StopCriterion.ITERATIONS;
        //stopCriterion = StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS;
        //stopCriterion = StopCriterion.CPU_TIME;
    }

    @Override
    protected void addTask(Problem problem, StopCriterion stopCriterion, int maxEvaluations, long time, int maxIterations) {
        if (stopCriterion == StopCriterion.CPU_TIME) {
            long start;
            int numOfEvaluations = 1000000;
            int warmupIterations = 1000;
            double[] results = new double[numOfEvaluations];
            for (int x = 0; x < warmupIterations; x++) {
                problem.getRandomEvaluatedSolution();
            }

            for (int i = 0; i < numOfEvaluations; i++) {
                start = System.nanoTime();
                problem.getRandomEvaluatedSolution();
                results[i] = (System.nanoTime() - start) / 1e6;
            }
            Mean mean = new Mean();
            double avg = mean.evaluate(results);
            time = (long) (avg * maxEvaluations);
        }

        tasks.add(new Task(problem, stopCriterion, maxEvaluations, time, maxIterations));
    }

    @Override
    public void initAllProblems() {
        ArrayList<Problem> problems = new ArrayList<Problem>();

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
        problems.add(new Step2(30));
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

        for (Problem p : problems) {
            addTask(p, stopCriterion, maxEvaluations, timeLimit, maxIterations);
        }
    }

    @Override
    public String getName() {
        return name + "  D="+ dimension;
    }
}
