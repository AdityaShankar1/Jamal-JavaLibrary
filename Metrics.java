public static void printConfusionMatrix(int[] actual, int[] predicted) {
    int tp = 0, fp = 0, tn = 0, fn = 0;
    for (int i = 0; i < actual.length; i++) {
        if (actual[i] == 1 && predicted[i] == 1) tp++;
        else if (actual[i] == 0 && predicted[i] == 1) fp++;
        else if (actual[i] == 0 && predicted[i] == 0) tn++;
        else if (actual[i] == 1 && predicted[i] == 0) fn++;
    }
    System.out.println("\n--- Confusion Matrix ---");
    System.out.println("Actual \\ Pred |  0  |  1  |");
    System.out.println("--------------|-----|-----|");
    System.out.println("      0       | " + tn + " | " + fp + " |");
    System.out.println("      1       | " + fn + " | " + tp + " |");
}