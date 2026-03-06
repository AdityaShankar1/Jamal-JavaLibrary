package com.jamal.core;

import java.util.*;
import java.util.concurrent.*;

public class RandomForest implements Classifier {
    private final int numTrees;
    private final int maxDepth;
    private final List<DecisionTree> trees;

    public RandomForest(int numTrees, int maxDepth) {
        this.numTrees = numTrees;
        this.maxDepth = maxDepth;
        this.trees = new ArrayList<>();
    }

    @Override
    public void fit(double[][] features, int[] labels) {
        int nSamples = features.length;
        Random rand = new Random();

        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<CompletableFuture<DecisionTree>> futures = new ArrayList<>();

            for (int i = 0; i < numTrees; i++) {
                futures.add(CompletableFuture.supplyAsync(() -> {
                    // Bootstrap: Sample with replacement
                    double[][] sampleX = new double[nSamples][];
                    int[] sampleY = new int[nSamples];
                    for (int j = 0; j < nSamples; j++) {
                        int idx = rand.nextInt(nSamples);
                        sampleX[j] = features[idx];
                        sampleY[j] = labels[idx];
                    }
                    DecisionTree tree = new DecisionTree(maxDepth);
                    tree.fit(sampleX, sampleY);
                    return tree;
                }, executor));
            }

            for (var future : futures) {
                trees.add(future.join());
            }
        }
        System.out.println("JAMAL: Random Forest ensemble of " + numTrees + " trees ready.");
    }

    @Override
    public int predict(double[] features) {
        int votesFor1 = 0;
        for (DecisionTree tree : trees) {
            if (tree.predict(features) == 1) votesFor1++;
        }
        return (votesFor1 > trees.size() / 2) ? 1 : 0;
    }
}
