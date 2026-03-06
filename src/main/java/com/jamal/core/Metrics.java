package com.jamal.core;

/**
 * Utility class for evaluating Model Performance.
 */
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
     * Prints a detailed performance report including Accuracy, Precision, and Recall.
     */
    public static void report(int[] actual, int[] predicted) {
        double acc = accuracy(actual, predicted);
        
        int tp = 0, fp = 0, tn = 0, fn = 0;
        for (int i = 0; i < actual.length; i++) {
            if (actual[i] == 1 && predicted[i] == 1) tp++;
            else if (actual[i] == 0 && predicted[i] == 1) fp++;
            else if (actual[i] == 0 && predicted[i] == 0) tn++;
            else if (actual[i] == 1 && predicted[i] == 0) fn++;
        }
        
        double precision = (tp + fp == 0) ? 0 : (double) tp / (tp + fp);
        double recall = (tp + fn == 0) ? 0 : (double) tp / (tp + fn);
        double f1 = (precision + recall == 0) ? 0 : 2 * (precision * recall) / (precision + recall);

        System.out.println("\n====================================");
        System.out.println("       JAMAL PERFORMANCE REPORT     ");
        System.out.println("====================================");
        System.out.println(String.format("Accuracy:  %.2f%%", acc * 100));
        System.out.println(String.format("Precision: %.2f", precision));
        System.out.println(String.format("Recall:    %.2f", recall));
        System.out.println(String.format("F1-Score:  %.2f", f1));
        
        printConfusionMatrix(tn, fp, fn, tp);
    }

    private static void printConfusionMatrix(int tn, int fp, int fn, int tp) {
        System.out.println("\n--- Confusion Matrix ---");
        System.out.println("Actual \\ Pred |  NEG (0) |  POS (1) |");
        System.out.println("--------------|----------|----------|");
        System.out.println(String.format("  NEG (0)     |    %d     |    %d     |", tn, fp));
        System.out.println(String.format("  POS (1)     |    %d     |    %d     |", fn, tp));
        System.out.println("------------------------------------");
    }
}
