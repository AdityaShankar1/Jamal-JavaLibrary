# JAMAL: Java Machine Learning Library 🚀

**JAMAL** (Java Machine Learning) is a high-performance, lightweight "Scikit-Learn style" library built for **Java 21+**. It leverages the **JDK Vector API (SIMD)** to provide lightning-fast matrix math and modern concurrency for machine learning workflows.

## ✨ Features
* **SIMD Accelerated:** Core math operations (Dot Product, Euclidean Distance) use hardware-level vectorization.
* **Familiar API:** Uses the `fit` / `predict` pattern inspired by Scikit-Learn.
* **Zero Dependencies:** Pure Java implementation with no heavy external jars.
* **Standardization:** Built-in `StandardScaler` and `Dataset` loaders.

## 📦 Installation (Local)
To use JAMAL in your own projects, clone this repo and install it to your local Maven repository:
```bash
git clone https://github.com/AdityaShankar1/Jamal-JavaLibrary.git
cd Jamal-JavaLibrary
mvn clean install
```

Then, add the dependency to your `pom.xml`:
```xml
<dependency>
    <groupId>com.jamal</groupId>
    <artifactId>jamal-ml</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

## 🚀 Quick Start
```java
// 1. Load Data
Dataset data = Dataset.fromCsv("data.csv", true);

// 2. Preprocess
StandardScaler scaler = new StandardScaler();
scaler.fit(data.getFeatures());
double[][] x = scaler.transform(data.getFeatures());

// 3. Train & Predict
Classifier model = new KNNClassifier(k: 3);
model.fit(x, data.getLabels());

int prediction = model.predict(new double[]{5.1, 3.5});
System.out.println("Result: " + prediction);
```

## 🛠 Included Models
| Model | Type | Math Engine |
| :--- | :--- | :--- |
| **Linear Regression** | Regression/Classification | Gradient Descent |
| **KNN Classifier** | Instance-based | Vectorized Euclidean |
| **Naive Bayes** | Probabilistic | Gaussian Likelihood |

## ⚠️ Requirements
* **Java 21+** (Uses Virtual Threads and Vector API)
* **JVM Flag:** Must run with `--add-modules jdk.incubator.vector`

---
*Created by Aditya Shankar as a high-performance Java ML exploration.*
