<p align="center">
  <img src="assets/logo.png" width="200" />
</p>

# JAMAL: Java Machine Learning Library 🚀

**JAMAL** (Java Machine Learning) is a high-performance, enterprise-grade ML library built for **Java 21+**. It leverages **JDK Vector API (SIMD)** for hardware-accelerated math and **Virtual Threads** for massive parallelization of ensemble models.

[![Java 21+](https://img.shields.io/badge/Java-21%2B-blue.svg)](https://jdk.java.net/21/)
[![License](https://img.shields.io/badge/License-EPL%202.0-orange.svg)](https://opensource.org/licenses/EPL-2.0)

## ✨ Why JAMAL?
* **Hardware Accelerated:** Uses SIMD instructions to process data vectors at the CPU level.
* **Modern Concurrency:** Parallelizes KNN searches and Random Forest training using lightweight Virtual Threads.
* **Production Ready:** Includes model serialization (Save/Load) and comprehensive performance metrics.
* **Zero Dependencies:** Pure Java. No bulky native binaries or external wrappers.

## 🛠 Model Suite
| Category | Model | Engine |
| :--- | :--- | :--- |
| **Ensemble** | **Random Forest** | Parallelized Decision Trees |
| **Supervised** | **Decision Tree** | Gini Impurity Recursive Splitting |
| **Supervised** | **KNN Classifier** | Vector-Accelerated Euclidean Space |
| **Supervised** | **Naive Bayes** | Gaussian Probability Density |
| **Supervised** | **Linear Regression** | Gradient Descent |
| **Unsupervised** | **K-Means** | Centroid-based Clustering |

## 🚀 Quick Start
```java
// Load and Split Data
Dataset data = Dataset.fromCsv("students.csv", true);
Dataset[] split = data.split(0.2); // 80/20 Train-Test split

// Train a Random Forest using all CPU cores
Classifier forest = new RandomForest(numTrees: 10, maxDepth: 5);
forest.fit(split[0].getFeatures(), split[0].getLabels());

// Evaluate with built-in Metrics
int[] preds = Arrays.stream(split[1].getFeatures()).mapToInt(forest::predict).toArray();
Metrics.report(split[1].getLabels(), preds);

// Save your model for deployment
ModelLoader.save(forest, "model.jamal");
```

## 📦 Installation
```bash
git clone https://github.com/AdityaShankar1/Jamal-JavaLibrary.git
cd Jamal-JavaLibrary
mvn clean install
```

## ⚠️ Requirements
* **Java 21+**
* **JVM Flag:** Must run with \`--add-modules jdk.incubator.vector\`

---
**License:** Distributed under the **Eclipse Public License 2.0**.
*Created by Aditya Shankar - Exploring the limits of Modern Java for ML.*
