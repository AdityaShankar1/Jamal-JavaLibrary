package com.jamal.core;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Dataset {
    private double[][] features;
    private int[] labels;

    private Dataset(double[][] features, int[] labels) {
        this.features = features;
        this.labels = labels;
    }

    /**
     * Loads a CSV file where the LAST column is the label (y) 
     * and all other columns are features (X).
     */
    public static Dataset fromCsv(String path, boolean hasHeader) throws IOException {
        List<double[]> featureList = new ArrayList<>();
        List<Integer> labelList = new ArrayList<>();

        try (Stream<String> lines = Files.lines(Paths.get(path))) {
            Stream<String> stream = hasHeader ? lines.skip(1) : lines;
            
            stream.forEach(line -> {
                String[] parts = line.split(",");
                double[] feats = new double[parts.length - 1];
                
                // Parse features
                for (int i = 0; i < parts.length - 1; i++) {
                    feats[i] = Double.parseDouble(parts[i].trim());
                }
                
                featureList.add(feats);
                // Parse label (last column)
                labelList.add((int) Double.parseDouble(parts[parts.length - 1].trim()));
            });
        }

        double[][] x = featureList.toArray(new double[0][0]);
        int[] y = labelList.stream().mapToInt(i -> i).toArray();
        
        return new Dataset(x, y);
    }

    public double[][] getFeatures() { return features; }
    public int[] getLabels() { return labels; }
}
