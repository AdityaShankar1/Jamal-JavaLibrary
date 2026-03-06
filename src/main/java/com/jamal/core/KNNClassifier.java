package com.jamal.core;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class KNNClassifier implements Classifier {
    private double[][] trainingX;
    private int[] trainingY;
    private final int k;

    public KNNClassifier(int k) {
        this.k = k;
    }

    @Override
    public void fit(double[][] features, int[] labels) {
        // KNN is a "lazy" learner; we just store the data
        this.trainingX = features;
        this.trainingY = labels;
        System.out.println("JAMAL: KNN Model stored " + features.length + " data points.");
    }

    @Override
    public int predict(double[] features) {
        // Calculate distances to all points using our VectorMath!
        double[] distances = new double[trainingX.length];
        
        for (int i = 0; i < trainingX.length; i++) {
            // We'll use Euclidean distance (sqrt of sum of squared differences)
            distances[i] = calculateEuclideanDistance(trainingX[i], features);
        }

        // Find the K nearest indices
        int[] nearestIndices = IntStream.range(0, distances.length)
                .boxed()
                .sorted(Comparator.comparingDouble(i -> distances[i]))
                .limit(k)
                .mapToInt(i -> i)
                .toArray();

        // Majority vote
        return Arrays.stream(nearestIndices)
                .map(i -> trainingY[i])
                .boxed()
                .collect(Collectors.groupingBy(label -> label, Collectors.counting()))
                .entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .get()
                .getKey();
    }

    private double calculateEuclideanDistance(double[] a, double[] b) {
        // Use your high-performance VectorMath dot product logic!
        // distance^2 = sum((a_i - b_i)^2)
        double sum = 0;
        // In a real version, we'd add a "squareDifference" method to VectorMath
        // For now, let's keep it simple:
        for (int i = 0; i < a.length; i++) {
            double diff = a[i] - b[i];
            sum += diff * diff;
        }
        return Math.sqrt(sum);
    }
}
