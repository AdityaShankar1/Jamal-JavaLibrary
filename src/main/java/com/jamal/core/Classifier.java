package com.jamal.core;

/**
 * The base interface for all JAMAL classification models.
 */
public interface Classifier {
    /**
     * Trains the model on the provided dataset.
     */
    void fit(double[][] features, int[] labels);

    /**
     * Predicts the class for a single input vector.
     */
    int predict(double[] features);
}
