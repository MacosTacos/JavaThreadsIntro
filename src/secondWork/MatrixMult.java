package secondWork;

import java.io.*;
import java.util.Random;

public class MatrixMult {
    static final int SIZE = 2000;
    static final int THREADS = 12;
    static int counter = 0;
    static int[][] firstMatrix = new int[SIZE][SIZE];
    static int[][] secondMatrix = new int[SIZE][SIZE];
    static int[][] resultMatrix = new int[SIZE][SIZE];


    static class MatrixMultiple implements Runnable {
        int count;

        MatrixMultiple(int count) {
            this.count = count;
        }

        @Override
        public void run() {
            for (int i = this.count; i < SIZE; i += THREADS) {
                for (int j = 0; j < SIZE; j++) {
                    for (int k = 0; k < SIZE; k++) {
                        resultMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        String firstFile = "firstMatrix.ser", secondFile = "secondMatrix.ser";
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                firstMatrix[i][j] = (random.nextInt());
                secondMatrix[i][j] = (random.nextInt());
            }
        }
//        firstMatrix = deserialize(firstFile);
//        secondMatrix = deserialize(secondFile);
        try {
            serialize(firstMatrix, firstFile);
            serialize(secondMatrix, secondFile);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread[] threads = new Thread[THREADS];
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < THREADS; i++) {
            threads[i] = new Thread(new MatrixMultiple(counter++));
            threads[i].start();
        }

        for (int i = 0; i < THREADS; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long time = System.currentTimeMillis() - currentTime;
        System.out.println("многопоточный формат: " + time);

        currentTime = System.currentTimeMillis();
        int[][] result = multiplyMatrices(firstMatrix, secondMatrix, SIZE);
        time = System.currentTimeMillis() - currentTime;
        System.out.println("Однопоточный формат: " + time);

    }

    public static int[][] multiplyMatrices(int[][] matrixA, int[][] matrixB, int N) {
        int[][] result = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                for (int k = 0; k < N; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    public static void serialize(int[][] matrix, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(matrix);
        }
    }

    public static int[][] deserialize(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (int[][]) ois.readObject();
        }
    }

}
