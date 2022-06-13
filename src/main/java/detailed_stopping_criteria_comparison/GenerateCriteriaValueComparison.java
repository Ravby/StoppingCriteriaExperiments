package detailed_stopping_criteria_comparison;

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
import org.um.feri.ears.problems.unconstrained.*;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public class GenerateCriteriaValueComparison {

    static ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();
    static ArrayList<Problem> problems = new ArrayList<Problem>();
    static ArrayList<Task> tasks = new ArrayList<>();

    static int maxEvaluations;
    static long timeLimit; //milliseconds
    static int maxIterations;
    static int numberOfRuns = 30;

    static StandardDeviation sd = new StandardDeviation();
    static Mean mean = new Mean();
    static DecimalFormat formatter = new DecimalFormat("#0.00");

    public static void main(String[] args) {

        int populationSize = 50;

        algorithms.add(new ABC(populationSize));
        algorithms.add(new JADE(populationSize));
        algorithms.add(new LaF(populationSize));
        algorithms.add(new TLBOAlgorithm(populationSize));
        algorithms.add(new SOMA(populationSize));
        algorithms.add(new PSO(populationSize));
        algorithms.add(new HSA(populationSize));
        algorithms.add(new RandomWalkAlgorithm());


        // Uncomment the problems for which you want to get the data

        /*problems.add(new SoilModelProblem(5, 3,"TE1"));
        problems.add(new SoilModelProblem(5, 3,"TE2"));
        problems.add(new SoilModelProblem(5, 3,"TE3"));*/

        problems.add(new Beale());
        problems.add(new Easom()); //wrong global minimum in paper
        problems.add(new Matyas());
        problems.add(new Bohachevsky1());
        problems.add(new Booth());
        problems.add(new Michalewicz2());
        problems.add(new Schaffer1());
        problems.add(new SixHumpCamelBack()); //x1[-3,3], x2[-2,2]
        problems.add(new Bohachevsky2());
        problems.add(new Bohachevsky3());
        problems.add(new Shubert1(2));
        problems.add(new Colville());
        problems.add(new Michalewicz5());
        problems.add(new Zakharov(30));
        problems.add(new Michalewicz10());
        problems.add(new Step1(30)); //x[-5.12,5.12]
        problems.add(new Sphere(30));
        problems.add(new SumSquares(30));
        problems.add(new Quartic(30)); //paper uses noise
        problems.add(new Schwefel222(30)); //x[-10,10]
        problems.add(new Schwefel12());
        problems.add(new RosenbrockDeJong2(30)); //x[-30,30]
        problems.add(new DixonPrice(30));
        problems.add(new Rastrigin(30));
        problems.add(new Griewank(30)); // x - 100 in paper's equation
        problems.add(new Ackley1(30)); // x - 100 in paper's equation
        problems.add(new DropWave());
        problems.add(new Hartman3());
        problems.add(new Hartman6()); //error in table Hartman10 instead of Hartman6
        problems.add(new Shekel5());
        problems.add(new Shekel7());
        problems.add(new Shekel10());
        problems.add(new Branin1());
        problems.add(new GoldsteinPrice());
        problems.add(new Salomon(5));
        problems.add(new Salomon(6));

        StopCriterion stopCriterion;

        maxEvaluations = 100000;
        stopCriterion = StopCriterion.EVALUATIONS;
        createTasks(stopCriterion);
        runAndDisplay();

        maxIterations = maxEvaluations / 50;
        stopCriterion = StopCriterion.ITERATIONS;
        createTasks(stopCriterion);
        runAndDisplay();

        stopCriterion = StopCriterion.CPU_TIME;
        createTasks(stopCriterion);
        runAndDisplay();

        maxEvaluations = 300000;
        stopCriterion = StopCriterion.EVALUATIONS;
        createTasks(stopCriterion);
        runAndDisplay();

        maxIterations = maxEvaluations / 50;
        stopCriterion = StopCriterion.ITERATIONS;
        createTasks(stopCriterion);
        runAndDisplay();

        stopCriterion = StopCriterion.CPU_TIME;
        createTasks(stopCriterion);
        runAndDisplay();

        maxEvaluations = 500000;
        stopCriterion = StopCriterion.EVALUATIONS;
        createTasks(stopCriterion);
        runAndDisplay();

        maxIterations = maxEvaluations / 50;
        stopCriterion = StopCriterion.ITERATIONS;
        createTasks(stopCriterion);
        runAndDisplay();

        stopCriterion = StopCriterion.CPU_TIME;
        createTasks(stopCriterion);
        runAndDisplay();

        maxEvaluations = 1000000;
        stopCriterion = StopCriterion.GLOBAL_OPTIMUM_OR_EVALUATIONS;
        createTasks(stopCriterion);
        runAndDisplay();
    }

    private static void runAndDisplay() {
        LinkedHashMap<String, ArrayList<Run>> algorithmResults = new LinkedHashMap<>();
        String stopString="";
        for (int i = 0; i < algorithms.size(); i++) {
            //System.out.println("Algorithm: " + algorithm.getAlgorithmName());
            ArrayList<Run> runResults = new ArrayList<>();
            for (int j = 0; j < numberOfRuns; j++) {
                //System.out.println("Current run: " + (j + 1));
                for (Task task : tasks) {
                    stopString = task.getStopCriterionString();
                    //System.out.println("Current problem: " + task.getProblemName());
                    Task taskCopy = task.clone();
                    DoubleSolution result = null;
                    try {
                        result = algorithms.get(i).execute(taskCopy);
                    } catch (StopCriterionException e) {
                        e.printStackTrace();
                    }
                    runResults.add(new Run(taskCopy, result));
                }
            }
            algorithmResults.put(algorithms.get(i).getId(), runResults);
        }
        System.out.println(stopString);
        String line1 = "~", line2 = "Evaluations", line3 = "Generations", line4 = "CPU time";
        for (Map.Entry<String, ArrayList<Run>> entry : algorithmResults.entrySet()) {
            line1 += " & " + entry.getKey();
            int numberOfExecutions = entry.getValue().size();
            double[] evaluations = new double[numberOfExecutions];
            double[] iterations = new double[numberOfExecutions];
            long [] cpuTime = new long[numberOfExecutions];

            for (int i = 0; i < numberOfExecutions; i++) {
                Task t = entry.getValue().get(i).task;
                evaluations[i] = t.getNumberOfEvaluations();
                iterations[i] = t.getNumberOfIterations();
                cpuTime[i] = t.getEvaluationTimeMs();
            }
            double meanCpuTime = Arrays.stream(cpuTime).sum() / (double)cpuTime.length;
            line2 += " & " + formatter.format(mean.evaluate(evaluations));
            line3 += " & " + formatter.format(mean.evaluate(iterations));
            line4 += " & " + formatter.format(meanCpuTime); //ms

            //System.out.println("mean eval: " + mean.evaluate(evaluations) + " mean iterations: " + mean.evaluate(iterations) + " mean CPU time: " + mean.evaluate(cpuTime) + " s");
        }
        System.out.println(line1 + " \\\\");
        System.out.println(line2 + " \\\\");
        System.out.println(line3 + " \\\\");
        System.out.println(line4 + " \\\\");
    }

    public static void createTasks(StopCriterion stopCriterion) {
        tasks.clear();

        for (Problem p : problems) {

            if (stopCriterion == StopCriterion.CPU_TIME) {
                long start;
                int numOfEvaluations = 1000000;
                int warmupIterations = 1000;
                double[] results = new double[numOfEvaluations];
                for (int x = 0; x < warmupIterations; x++) {
                    p.getRandomEvaluatedSolution();
                }

                for (int i = 0; i < numOfEvaluations; i++) {
                    start = System.nanoTime();
                    p.getRandomEvaluatedSolution();
                    results[i] = (System.nanoTime() - start) / 1e6;
                }
                Mean mean = new Mean();
                double avg = mean.evaluate(results);
                timeLimit = (long) (avg * maxEvaluations);
            }

            tasks.add(new Task(p, stopCriterion, maxEvaluations, timeLimit, maxIterations));
        }
    }

    static class Run {
        public Task task;
        public DoubleSolution solution;

        public Run(Task task, DoubleSolution solution) {
            this.task = task;
            this.solution = solution;
        }
    }
}
