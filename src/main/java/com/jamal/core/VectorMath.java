package com.jamal.core;

import jdk.incubator.vector.DoubleVector;
import jdk.incubator.vector.VectorSpecies;
import jdk.incubator.vector.VectorOperators;

public class VectorMath {
    private static final VectorSpecies<Double> SPECIES = DoubleVector.SPECIES_PREFERRED;

    public static double dotProduct(double[] a, double[] b) {
        int i = 0;
        double res = 0;
        int upperBound = SPECIES.loopBound(a.length);
        for (; i < upperBound; i += SPECIES.length()) {
            var va = DoubleVector.fromArray(SPECIES, a, i);
            var vb = DoubleVector.fromArray(SPECIES, b, i);
            res += va.mul(vb).reduceLanes(VectorOperators.ADD);
        }
        for (; i < a.length; i++) res += a[i] * b[i];
        return res;
    }

    /**
     * Calculates the sum of squared differences: sum((a[i] - b[i])^2)
     * Optimized with SIMD.
     */
    public static double dotProductDifference(double[] a, double[] b) {
        int i = 0;
        double res = 0;
        int upperBound = SPECIES.loopBound(a.length);
        for (; i < upperBound; i += SPECIES.length()) {
            var va = DoubleVector.fromArray(SPECIES, a, i);
            var vb = DoubleVector.fromArray(SPECIES, b, i);
            var diff = va.sub(vb);
            res += diff.mul(diff).reduceLanes(VectorOperators.ADD);
        }
        for (; i < a.length; i++) {
            double d = a[i] - b[i];
            res += d * d;
        }
        return res;
    }
}
