package com.jamal.core;
import java.io.Serializable;

public interface ClusteringModel extends Serializable {
    /**
     * Clusters the data into groups.
     * @param features 2D array of input features
     */
    void fit(double[][] features);

    /**
     * Predicts which cluster a new point belongs to.
     */
    int predict(double[] features);
}
