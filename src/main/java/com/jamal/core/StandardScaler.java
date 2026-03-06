package com.jamal.core;

import java.util.Arrays;

public class StandardScaler {
    private double[] means;
    private double[] stds;

    public void fit(double[][] features) {
        int nSamples = features.length;
        int nFeatures = features[0].length;
        means = new double[nFeatures];
        stds = new double[nFeatures];

        // Calculate Means
        for (double[] row : features) {
            for (int j = 0; j < nFeatures; j++) {
                means[j] += row[j];
            }
        }
        for (int j = 0; j < nFeatures; j++) means[j] /= nSamples;

        // Calculate Std Devs
        for (double[] row : features) {
            for (int j = 0; j < nFeatures; j++) {
                double diff = row[j] - means[j];
                stds[j] += diff * diff;
            }
        }
        for (int j = 0; j < nFeatures; j++) {
            stds[j] = Math.sqrt(stds[j] / nSamples);
            if (stds[j] == 0) stds[j] = 1.0; // Prevent division by zero
        }
    }

    public double[][] transform(double[][] features) {
        double[][] scaled = new double[features.length][features[0].length];
        for (int i = 0; i < features.length; i++) {
            scaled[i] = transform(features[i]);
        }
        return scaled;
    }

    public double[] transform(double[] feature) {
        double[] scaled = new double[feature.length];
        for (int j = 0; j < feature.length; j++) {
            scaled[j] = (feature[j] - means[j]) / stds[j];
        }
        return scaled;
    }
}
