package com.jamal.core;

import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.CompletableFuture;

public class KMeans implements ClusteringModel {
    private final int k;
    private final int maxIterations;
    private double[][] centroids;

    public KMeans(int k, int maxIterations) {
        this.k = k;
        this.maxIterations = maxIterations;
    }

    @Override
    public void fit(double[][] features) {
        int nFeatures = features[0].length;
        centroids = new double[k][nFeatures];
        Random rand = new Random(42);

        // 1. Initialize centroids randomly from existing points
        for (int i = 0; i < k; i++) {
            centroids[i] = features[rand.nextInt(features.length)].clone();
        }

        for (int iter = 0; iter < maxIterations; iter++) {
            int[] assignments = new int[features.length];

            // 2. Assign points to nearest centroid (Parallelized)
            try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
                for (int i = 0; i < features.length; i++) {
                    final int idx = i;
                    executor.submit(() -> {
                        assignments[idx] = predict(features[idx]);
                    });
                }
            }

            // 3. Move centroids to the mean of assigned points
            double[][] newCentroids = new double[k][nFeatures];
            int[] counts = new int[k];

            for (int i = 0; i < features.length; i++) {
                int cluster = assignments[i];
                for (int j = 0; j < nFeatures; j++) {
                    newCentroids[cluster][j] += features[i][j];
                }
                counts[cluster]++;
            }

            boolean changed = false;
            for (int i = 0; i < k; i++) {
                if (counts[i] > 0) {
                    for (int j = 0; j < nFeatures; j++) {
                        double updated = newCentroids[i][j] / counts[i];
                        if (Math.abs(centroids[i][j] - updated) > 1e-4) changed = true;
                        centroids[i][j] = updated;
                    }
                }
            }

            if (!changed) break; // Convergence
        }
    }

    @Override
    public int predict(double[] features) {
        int bestCluster = -1;
        double minDistance = Double.MAX_VALUE;

        for (int i = 0; i < k; i++) {
            double dist = VectorMath.dotProductDifference(features, centroids[i]);
            if (dist < minDistance) {
                minDistance = dist;
                bestCluster = i;
            }
        }
        return bestCluster;
    }
}
