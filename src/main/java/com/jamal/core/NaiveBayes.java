package com.jamal.core;

import java.util.HashMap;
import java.util.Map;

public class NaiveBayes implements Classifier {
    private Map<Integer, Double> classPriors = new HashMap<>();
    private Map<Integer, double[]> classMeans = new HashMap<>();
    private Map<Integer, double[]> classVars = new HashMap<>();
    private int[] uniqueClasses;

    @Override
    public void fit(double[][] features, int[] labels) {
        int nSamples = features.length;
        int nFeatures = features[0].length;
        
        // Find unique classes (e.g., 0 and 1)
        uniqueClasses = java.util.Arrays.stream(labels).distinct().toArray();

        for (int c : uniqueClasses) {
            // Filter features belonging to class C
            double[][] cFeatures = java.util.stream.IntStream.range(0, nSamples)
                .filter(i -> labels[i] == c)
                .mapToObj(i -> features[i])
                .toArray(double[][]::new);

            classPriors.put(c, (double) cFeatures.length / nSamples);

            // Calculate Mean and Variance for each feature in this class
            double[] means = new double[nFeatures];
            double[] vars = new double[nFeatures];

            for (int j = 0; j < nFeatures; j++) {
                final int col = j;
                means[j] = java.util.Arrays.stream(cFeatures).mapToDouble(row -> row[col]).average().orElse(0);
                
                double sumSqDiff = 0;
                for (double[] row : cFeatures) {
                    sumSqDiff += Math.pow(row[j] - means[j], 2);
                }
                vars[j] = sumSqDiff / cFeatures.length + 1e-9; // Small epsilon to avoid div by zero
            }
            classMeans.put(c, means);
            classVars.put(c, vars);
        }
        System.out.println("JAMAL: Naive Bayes trained on " + uniqueClasses.length + " classes.");
    }

    @Override
    public int predict(double[] features) {
        double bestProb = Double.NEGATIVE_INFINITY;
        int bestClass = -1;

        for (int c : uniqueClasses) {
            double logProb = Math.log(classPriors.get(c));
            double[] means = classMeans.get(c);
            double[] vars = classVars.get(c);

            for (int j = 0; j < features.length; j++) {
                logProb += calculateLogLikelihood(features[j], means[j], vars[j]);
            }

            if (logProb > bestProb) {
                bestProb = logProb;
                bestClass = c;
            }
        }
        return bestClass;
    }

    private double calculateLogLikelihood(double x, double mean, double var) {
        // Gaussian Probability Density Function in Log space for stability
        double exponent = -Math.pow(x - mean, 2) / (2 * var);
        return exponent - Math.log(Math.sqrt(2 * Math.PI * var));
    }
}
