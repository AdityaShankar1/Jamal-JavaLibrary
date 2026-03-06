package com.jamal.core;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.VectorOperators;

public class VectorMath {
    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    /**
     * High-performance Dot Product using SIMD (Vector API).
     */
    public static double dotProduct(double[] a, double[] b) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("Arrays must be same length");
        }

        int i = 0;
        double res = 0;
        int upperBound = SPECIES.loopBound(a.length);

        // Vectorized loop
        for (; i < upperBound; i += SPECIES.length()) {
            var va = DoubleVector.fromArray(SPECIES, a, i);
            var vb = DoubleVector.fromArray(SPECIES, b, i);
            // FIX: Use VectorOperators.ADD instead of VectorSpecies.Aggregation
            res += va.mul(vb).reduceLanes(VectorOperators.ADD);
        }

        // Cleanup loop for remaining elements
        for (; i < a.length; i++) {
            res += a[i] * b[i];
        }
        return res;
    }
}
