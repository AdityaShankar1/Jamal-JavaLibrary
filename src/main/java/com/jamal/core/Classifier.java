package com.jamal.core;
import java.io.Serializable;

public interface Classifier extends Serializable {
    void fit(double[][] features, int[] labels);
    int predict(double[] features);
}
