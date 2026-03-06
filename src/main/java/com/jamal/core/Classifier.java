package com.jamal.core;

public interface Classifier {
    /**
     * Train the model using features (X) and labels (y).
     */
    void fit(double[][] features, int[] labels);

    /**
     * Predict the label for a single set of features.
     */
    int predict(double[] features);
}
