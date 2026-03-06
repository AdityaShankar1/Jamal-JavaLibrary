package com.jamal.core;

import java.util.*;

public class DecisionTree implements Classifier {
    private Node root;
    private final int maxDepth;

    public DecisionTree(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    private static class Node implements java.io.Serializable {
        int featureIndex = -1;
        double threshold;
        Node left, right;
        Integer prediction;

        boolean isLeaf() { return prediction != null; }
    }

    @Override
    public void fit(double[][] features, int[] labels) {
        this.root = buildTree(features, labels, 0);
        System.out.println("JAMAL: Decision Tree trained to depth " + maxDepth);
    }

    private Node buildTree(double[][] x, int[] y, int depth) {
        if (x.length == 0) return null;
        
        Node node = new Node();
        if (depth >= maxDepth || isPure(y)) {
            node.prediction = mostCommon(y);
            return node;
        }

        int bestFeat = -1;
        double bestThresh = 0;
        double bestGini = Double.MAX_VALUE;

        for (int f = 0; f < x[0].length; f++) {
            for (double[] row : x) {
                double thresh = row[f];
                double gini = calculateSplitGini(x, y, f, thresh);
                if (gini < bestGini) {
                    bestGini = gini;
                    bestFeat = f;
                    bestThresh = thresh;
                }
            }
        }

        List<Integer> leftIdx = new ArrayList<>();
        List<Integer> rightIdx = new ArrayList<>();
        for (int i = 0; i < x.length; i++) {
            if (x[i][bestFeat] <= bestThresh) leftIdx.add(i);
            else rightIdx.add(i);
        }

        if (leftIdx.isEmpty() || rightIdx.isEmpty()) {
            node.prediction = mostCommon(y);
            return node;
        }

        node.featureIndex = bestFeat;
        node.threshold = bestThresh;
        node.left = buildTree(getSubX(x, leftIdx), getSubY(y, leftIdx), depth + 1);
        node.right = buildTree(getSubX(x, rightIdx), getSubY(y, rightIdx), depth + 1);
        return node;
    }

    @Override
    public int predict(double[] features) {
        Node current = root;
        while (!current.isLeaf()) {
            if (features[current.featureIndex] <= current.threshold) current = current.left;
            else current = current.right;
        }
        return current.prediction;
    }

    private double calculateSplitGini(double[][] x, int[] y, int f, double t) {
        int lTrue = 0, lTotal = 0, rTrue = 0, rTotal = 0;
        for (int i = 0; i < x.length; i++) {
            if (x[i][f] <= t) { lTotal++; if (y[i] == 1) lTrue++; }
            else { rTotal++; if (y[i] == 1) rTrue++; }
        }
        if (lTotal == 0 || rTotal == 0) return 1.0;
        double lg = 1.0 - Math.pow((double)lTrue/lTotal, 2) - Math.pow((double)(lTotal-lTrue)/lTotal, 2);
        double rg = 1.0 - Math.pow((double)rTrue/rTotal, 2) - Math.pow((double)(rTotal-rTrue)/rTotal, 2);
        return (lTotal * lg + rTotal * rg) / (lTotal + rTotal);
    }

    private boolean isPure(int[] y) { return Arrays.stream(y).distinct().count() <= 1; }
    private int mostCommon(int[] y) {
        long count1 = Arrays.stream(y).filter(i -> i == 1).count();
        return (count1 >= (double)y.length / 2) ? 1 : 0;
    }
    private double[][] getSubX(double[][] x, List<Integer> idx) {
        double[][] res = new double[idx.size()][];
        for(int i=0; i<idx.size(); i++) res[i] = x[idx.get(i)];
        return res;
    }
    private int[] getSubY(int[] y, List<Integer> idx) {
        return idx.stream().mapToInt(i -> y[i]).toArray();
    }
}
