package com.jamal.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;

public class Dataset {
    private double[][] features;
    private int[] labels;

    public Dataset(double[][] features, int[] labels) {
        this.features = features;
        this.labels = labels;
    }

    public static Dataset fromCsv(String path, boolean hasHeader) throws IOException {
        List<double[]> featureList = new ArrayList<>();
        List<Integer> labelList = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            Stream<String> stream = hasHeader ? lines.skip(1) : lines;
            stream.forEach(line -> {
                String[] parts = line.split(",");
                double[] feats = new double[parts.length - 1];
                for (int i = 0; i < parts.length - 1; i++) {
                    feats[i] = Double.parseDouble(parts[i].trim());
                }
                featureList.add(feats);
                labelList.add((int) Double.parseDouble(parts[parts.length - 1].trim()));
            });
        }
        return new Dataset(featureList.toArray(new double[0][0]), 
                           labelList.stream().mapToInt(i -> i).toArray());
    }

    /**
     * Splits the dataset into two: [Training, Testing]
     * @param testSize fraction of data for testing (e.g., 0.2 for 20%)
     */
    public Dataset[] split(double testSize) {
        int n = features.length;
        int testCount = (int) (n * testSize);
        int trainCount = n - testCount;

        // Create index list and shuffle
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < n; i++) indices.add(i);
        Collections.shuffle(indices, new Random(42)); // Fixed seed for reproducibility

        double[][] trainX = new double[trainCount][];
        int[] trainY = new int[trainCount];
        double[][] testX = new double[testCount][];
        int[] testY = new int[testCount];

        for (int i = 0; i < trainCount; i++) {
            trainX[i] = features[indices.get(i)];
            trainY[i] = labels[indices.get(i)];
        }
        for (int i = 0; i < testCount; i++) {
            testX[i] = features[indices.get(trainCount + i)];
            testY[i] = labels[indices.get(trainCount + i)];
        }

        return new Dataset[]{new Dataset(trainX, trainY), new Dataset(testX, testY)};
    }

    public double[][] getFeatures() { return features; }
    public int[] getLabels() { return labels; }
}
