package rating_intervals;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.um.feri.ears.benchmark.Benchmark;
import org.um.feri.ears.problems.Problem;
import org.um.feri.ears.problems.StopCriterion;
import org.um.feri.ears.problems.Task;
import org.um.feri.ears.problems.misc.SoilModelProblem;

import java.util.ArrayList;

public class SoilModelBenchmark extends Benchmark {

    public SoilModelBenchmark() {
        super();
        name = "Soil Model Benchmark";
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

        problems.add(new SoilModelProblem(5, 3,"TE1"));
        problems.add(new SoilModelProblem(5, 3,"TE2"));
        problems.add(new SoilModelProblem(5, 3,"TE3"));

        for (Problem p : problems) {
            addTask(p, stopCriterion, maxEvaluations, timeLimit, maxIterations);
        }
    }
}
