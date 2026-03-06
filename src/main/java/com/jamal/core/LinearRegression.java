package com.jamal.core;

public class LinearRegression implements Classifier {
    private double slope;
    private double intercept;

    @Override
    public void fit(double[][] features, int[] labels) {
        // For simple linear regression, we assume features[i][0] is our X
        int n = features.length;
        double sumX = 0, sumY = 0, sumXY = 0, sumX2 = 0;

        for (int i = 0; i < n; i++) {
            double x = features[i][0];
            double y = labels[i];
            sumX += x;
            sumY += y;
            sumX2 += x * x;
            sumXY += x * y;
        }

        this.slope = (n * sumXY - sumX * sumY) / (n * sumX2 - sumX * sumX);
        this.intercept = (sumY - slope * sumX) / n;
        
        System.out.println("JAMAL: Model trained. Slope: " + slope + ", Intercept: " + intercept);
    }

    @Override
    public int predict(double[] features) {
        // y = mx + b
        return (int) Math.round(slope * features[0] + intercept);
    }
    
    public double predictExact(double feature) {
        return slope * feature + intercept;
    }
}
