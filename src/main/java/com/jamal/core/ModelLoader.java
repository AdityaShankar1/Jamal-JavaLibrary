package com.jamal.core;

import java.io.*;

public class ModelLoader {
    public static void save(Classifier model, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(model);
            System.out.println("JAMAL: Model saved successfully to " + fileName);
        }
    }

    public static Classifier load(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (Classifier) ois.readObject();
        }
    }
}
