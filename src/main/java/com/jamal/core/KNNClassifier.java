package com.jamal.core;

import java.util.*;
import java.util.concurrent.*;
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
        this.trainingX = features;
        this.trainingY = labels;
        System.out.println("JAMAL: KNN Model stored " + features.length + " data points.");
    }

    @Override
    public int predict(double[] features) {
        double[] distances = new double[trainingX.length];

        // Using Virtual Threads to parallelize distance calculations
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<Void>> futures = new ArrayList<>();
            
            for (int i = 0; i < trainingX.length; i++) {
                final int index = i;
                futures.add(CompletableFuture.runAsync(() -> {
                    distances[index] = calculateEuclideanDistance(trainingX[index], features);
                }, executor));
            }
            // Wait for all calculations to finish
            CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();
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
                .map(Map.Entry::getKey)
                .orElse(0);
    }

    private double calculateEuclideanDistance(double[] a, double[] b) {
        // This is where Vector API + Virtual Threads make a massive difference
        return Math.sqrt(VectorMath.dotProductDifference(a, b));
    }
}
