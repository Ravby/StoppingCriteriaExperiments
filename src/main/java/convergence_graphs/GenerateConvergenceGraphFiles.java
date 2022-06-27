package convergence_graphs;

import org.apache.commons.math3.stat.descriptive.moment.Mean;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;


public class GenerateConvergenceGraphFiles {

    static final String PROBLEM_NAME = "@PROBLEM_NAME";
    static final String FOLDER_LOCATION = "@FOLDER_LOCATION";

    static final String[] thirtySixProblems = {"Beale", "Easom", "Matyas", "Bohachevsky1", "Booth", "Michalewicz2", "Schaffer1", "SixHumpCamelBack", "Bohachevsky2", "Bohachevsky3", "Shubert1", "Colville", "Michalewicz5", "Zakharov", "Michalewicz10",
            "Step1", "Sphere", "SumSquares", "Quartic", "Schwefel222", "Schwefel12", "RosenbrockDeJong2", "DixonPrice", "Rastrigin", "Griewank", "Ackley1", "DropWave", "Hartman3", "Hartman6", "Shekel5", "Shekel7", "Shekel10", "Branin1", "GoldsteinPrice", "Salomon5", "Salomon6"};

    static final String[] soilModelProblems = {"Soil-Model-TE1", "Soil-Model-TE2", "Soil-Model-TE3"};

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

        ArrayList<Problem> problemsSecnario1 = new ArrayList<Problem>();

        problemsSecnario1.add(new Beale());
        problemsSecnario1.add(new Easom());
        problemsSecnario1.add(new Matyas());
        problemsSecnario1.add(new Bohachevsky1());
        problemsSecnario1.add(new Booth());
        problemsSecnario1.add(new Michalewicz2());
        problemsSecnario1.add(new Schaffer1());
        problemsSecnario1.add(new SixHumpCamelBack());
        problemsSecnario1.add(new Bohachevsky2());
        problemsSecnario1.add(new Bohachevsky3());
        problemsSecnario1.add(new Shubert1(2));
        problemsSecnario1.add(new Colville());
        problemsSecnario1.add(new Michalewicz5());
        problemsSecnario1.add(new Zakharov(30));
        problemsSecnario1.add(new Michalewicz10());
        problemsSecnario1.add(new Step1(30));
        problemsSecnario1.add(new Sphere(30));
        problemsSecnario1.add(new SumSquares(30));
        problemsSecnario1.add(new Quartic(30));
        problemsSecnario1.add(new Schwefel222(30));
        problemsSecnario1.add(new Schwefel12());
        problemsSecnario1.add(new RosenbrockDeJong2(30));
        problemsSecnario1.add(new DixonPrice(30));
        problemsSecnario1.add(new Rastrigin(30));
        problemsSecnario1.add(new Griewank(30));
        problemsSecnario1.add(new Ackley1(30));
        problemsSecnario1.add(new DropWave());
        problemsSecnario1.add(new Hartman3());
        problemsSecnario1.add(new Hartman6());
        problemsSecnario1.add(new Shekel5());
        problemsSecnario1.add(new Shekel7());
        problemsSecnario1.add(new Shekel10());
        problemsSecnario1.add(new Branin1());
        problemsSecnario1.add(new GoldsteinPrice());
        problemsSecnario1.add(new Salomon(5));
        problemsSecnario1.add(new Salomon(6));

        // generate graph data files for scenario 1
        StringBuilder sb = new StringBuilder();

        // generate files based on evaluations
        for (Problem p : problemsSecnario1) {
            Task task = new Task(p, stopCriterion, maxEvaluations, timeLimit, maxIterations);

            for (Algorithm a : algorithms) {
                sb.setLength(0);
                sb.append("x y");
                sb.append(getFitnessValuesByEvaluation(a, task, numberOfRuns));
                Util.writeToFile(System.getProperty("user.dir") + "\\Convergence graphs\\evaluations\\eval_36p" + a.getId() + "_" + p.getFileNameString() + ".txt", sb.toString());
            }
        }
        // generate files based on generations
        for (Problem p : problemsSecnario1) {
            Task task = new Task(p, stopCriterion, maxEvaluations, timeLimit, maxIterations);

            for (Algorithm a : algorithms) {
                sb.setLength(0);
                sb.append("x y");
                sb.append(getFitnessValuesByGeneration(a, task, numberOfRuns));
                Util.writeToFile(System.getProperty("user.dir") + "\\Convergence graphs\\generations\\gen_36p" + a.getId() + "_" + p.getFileNameString() + ".txt", sb.toString());
            }
        }

        // generate graph data files for scenario 2
        ArrayList<Problem> problemsSecnario2 = new ArrayList<Problem>();

        problemsSecnario2.add(new SoilModelProblem(5, 3,"TE1"));
        problemsSecnario2.add(new SoilModelProblem(5, 3,"TE2"));
        problemsSecnario2.add(new SoilModelProblem(5, 3,"TE3"));
        // generate files based on evaluations
        for (Problem p : problemsSecnario2) {
            Task task = new Task(p, stopCriterion, maxEvaluations, timeLimit, maxIterations);

            for (Algorithm a : algorithms) {
                sb.setLength(0);
                sb.append("x y");
                sb.append(getFitnessValuesByEvaluation(a, task, numberOfRuns));
                Util.writeToFile(System.getProperty("user.dir") + "\\Convergence graphs\\evaluations\\eval_soil_model" + a.getId() + "_" + p.getFileNameString() + ".txt", sb.toString());
            }
        }
        // generate files based on generations
        for (Problem p : problemsSecnario2) {
            Task task = new Task(p, stopCriterion, maxEvaluations, timeLimit, maxIterations);

            for (Algorithm a : algorithms) {
                sb.setLength(0);
                sb.append("x y");
                sb.append(getFitnessValuesByGeneration(a, task, numberOfRuns));
                Util.writeToFile(System.getProperty("user.dir") + "\\Convergence graphs\\generations\\gen_soil_model" + a.getId() + "_" + p.getFileNameString() + ".txt", sb.toString());
            }
        }

        generateGraphTexFiles();

        //run the command in evaluations and generations folders to generate pdf files for each graph:
        //cd folder_location; for %i in (*.tex) do pdflatex -shell-escape -extra-mem-top=10000000 "%i";done
    }

    private static void generateGraphTexFiles() {
        final String destination = System.getProperty("user.dir") + "\\Convergence graphs";
        String templateFile = System.getProperty("user.dir") + File.separator + "LatexTemplate.txt";

        StringBuilder fileData = new StringBuilder();
        StringBuilder newFile;
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(templateFile))) {

            while ((line = br.readLine()) != null) {
                fileData.append(line);
                fileData.append("\n");
            }
            for (String problemName : thirtySixProblems) {
                //replace all tags in template file
                newFile = new StringBuilder(fileData.toString().replaceAll(PROBLEM_NAME, problemName));
                newFile = new StringBuilder(newFile.toString().replaceAll(FOLDER_LOCATION, "eval_36p"));

                //save template file
                Util.writeToFile(destination + File.separator + "evaluations" + File.separator + problemName + ".tex", newFile.toString());

                newFile = new StringBuilder(newFile.toString().replaceAll("eval_36p", "gen_36p"));
                Util.writeToFile(destination + File.separator + "generations" + File.separator + problemName + ".tex", newFile.toString());
            }
            for (String problemName : soilModelProblems) {
                newFile = new StringBuilder(fileData.toString().replaceAll(PROBLEM_NAME, problemName));
                newFile = new StringBuilder(newFile.toString().replaceAll(FOLDER_LOCATION, "eval_soil_model"));

                Util.writeToFile(destination + File.separator + "evaluations" + File.separator + problemName + ".tex", newFile.toString());

                newFile = new StringBuilder(newFile.toString().replaceAll("eval_soil_model", "gen_soil_model"));
                Util.writeToFile(destination + File.separator + "generations" + File.separator + problemName + ".tex", newFile.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFitnessValuesByEvaluation(Algorithm algorithm, Task task, int numberOfRuns) {

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

        return averages.toString();
    }

    public static String getFitnessValuesByGeneration(Algorithm algorithm, Task task, int numberOfRuns) {

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

        int currentGen = 20; // 1000 evaluations / 50 pop size = 20 generations

        for (int e = 0; e < evaluations[0].length; e++) {

            if (e+1 < evaluations[0].length && evaluations[0][e+1].iteration > currentGen) //every 20th iteration
            {
                currentGen += 20;

                if (evaluations[0][e].evalNum > task.getMaxEvaluations())
                    break;
                values = new double[numberOfRuns];

                averages.append("\n");

                for (int r = 0; r < numberOfRuns; r++) {
                    values[r] = evaluations[r][e].fitness;
                }
                avg = mean.evaluate(values);
                averages.append(evaluations[0][e].iteration).append(" ").append(avg);
            }
        }

        return averages.toString();
    }
}