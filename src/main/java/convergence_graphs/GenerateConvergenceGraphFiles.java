package convergence_graphs;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.um.feri.ears.algorithms.Algorithm;
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
import org.um.feri.ears.util.Util;

import java.io.File;
import java.util.ArrayList;

public class GenerateConvergenceGraphFiles {

    public static void main(String[] args) {

        long timeLimit = 2500;
        int maxEvaluations = 500000;
        int maxIterations = maxEvaluations / 50;
        StopCriterion stopCriterion = StopCriterion.EVALUATIONS;
        int numberOfRuns = 30;

        ArrayList<Algorithm> algorithms = new ArrayList<Algorithm>();

        algorithms.add(new SOMA(50));
        algorithms.add(new ABC(50));
        algorithms.add(new TLBOAlgorithm(50));
        algorithms.add(new PSO(50));
        algorithms.add(new JADE(50));
        algorithms.add(new LaF(50));
        algorithms.add(new HSA(50));
        algorithms.add(new RandomWalkAlgorithm());

        // Uncomment the problems for which you want to get the data

        ArrayList<Problem> problems = new ArrayList<Problem>();

        /*problems.add(new Beale());
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

        for (Problem p : problems) {
            Task task = new Task(p, stopCriterion, maxEvaluations, timeLimit, maxIterations);

            for (Algorithm a : algorithms) {
                sb.setLength(0);
                sb.append("x y");
                sb.append(readEvaluations(a, task, numberOfRuns));
                Util.writeToFile(System.getProperty("user.dir") + "\\Convergence graphs\\" + "eval_" + a.getId() + "_" + p.getFileNameString() + ".txt", sb.toString());
            }
        }
    }

    public static String readEvaluations(Algorithm algorithm, Task task, int numberOfRuns) {

        task.enableEvaluationHistory();
        task.setStoreEveryNthEvaluation(1000);

        EvaluationStorage.Evaluation[][] evaluations = new EvaluationStorage.Evaluation[numberOfRuns][];

        for (int i = 0; i < numberOfRuns; i++) {

            try {
                algorithm.execute(task.clone());
                ArrayList<EvaluationStorage.Evaluation> eh = task.getEvaluationHistory();
                evaluations[i] = task.getEvaluationHistory().toArray(new EvaluationStorage.Evaluation[0]);
            } catch (StopCriterionException e) {
                e.printStackTrace();
            }
        }


        StringBuilder averages = new StringBuilder();

        double avg;
        Mean mean = new Mean();

        double[] values;

        for (int e = 0; e < evaluations[0].length; e++) {

            if(evaluations[0][e].evalNum % 1000 != 0) //every 1000th evaluation
                continue;

            if (evaluations[0][e].evalNum > task.getMaxEvaluations())
                break;
            values = new double[numberOfRuns];

            averages.append("\n");


            for (int r = 0; r < numberOfRuns; r++) {
                values[r] = evaluations[r][e].fitness;
            }
            avg = mean.evaluate(values);
            averages.append(evaluations[0][e].evalNum).append(" ").append(avg);
        }

        //output = output.replace(".", ",");

        return averages.toString();
    }
}