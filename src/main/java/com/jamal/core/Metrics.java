package com.jamal.core;

public class Metrics {

    /**
     * Calculates the percentage of correct predictions.
     */
    public static double accuracy(int[] actual, int[] predicted) {
        if (actual.length != predicted.length) throw new IllegalArgumentException("Arrays must be same length");
        
        int correct = 0;
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] == predicted[i]) correct++;
        }
        return (double) correct / actual.length;
    }

    /**
     * Simple score reporter
     */
    public static void report(int[] actual, int[] predicted) {
        double acc = accuracy(actual, predicted);
        System.out.println("JAMAL Metrics Report:");
        System.out.println("-> Accuracy: " + (acc * 100) + "%");
        
        int tp = 0, fp = 0, fn = 0;
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] == 1 && predicted[i] == 1) tp++;
            if (actual[i] == 0 && predicted[i] == 1) fp++;
            if (actual[i] == 1 && predicted[i] == 0) fn++;
        }
        
        double precision = (tp + fp == 0) ? 0 : (double) tp / (tp + fp);
        double recall = (tp + fn == 0) ? 0 : (double) tp / (tp + fn);
        
        System.out.println("-> Precision: " + String.format("%.2f", precision));
        System.out.println("-> Recall:    " + String.format("%.2f", recall));
    }
}
