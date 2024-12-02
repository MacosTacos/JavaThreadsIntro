package secondWork;

import java.io.*;

public class MatrixMult {
    static final int MAX_N = 3;
    static final int MAX_THREAD = 12;
    static int[][] firstMatrix = new int[MAX_N][MAX_N];
    static int[][] secondMatrix = new int[MAX_N][MAX_N];
    static int[][] resultMatrix = new int[MAX_N][MAX_N];
    static int offset_i = 0;


    static class Threader implements Runnable {
        int i;

        Threader(int i) {
            this.i = i;
        }

        @Override
        public void run() {
            for (int i = this.i; i < MAX_N; i += MAX_THREAD) {
                for (int j = 0; j < MAX_N; j++) {
                    for (int k = 0; k < MAX_N; k++) {
                        resultMatrix[i][j] += firstMatrix[i][k] * secondMatrix[k][j];
                    }
                }
            }
        }
    }


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {

        String fileNameA = "matrixA.ser", fileNameB = "matrixB.ser";
        int[][] matrixA = generateMatrix(MAX_N);
        int[][] matrixB = generateMatrix(MAX_N);
        try {
            serializeMatrix(matrixA, fileNameA);
            serializeMatrix(matrixB, fileNameB);
        } catch (IOException e) {
            e.printStackTrace();
        }

        firstMatrix = deserializeMatrix(fileNameA);
        secondMatrix = deserializeMatrix(fileNameB);


        Thread[] threads = new Thread[MAX_THREAD];
        long current = System.currentTimeMillis();
        for (int i = 0; i < MAX_THREAD; i++) {
            threads[i] = new Thread(new Threader(offset_i++));
            threads[i].start();
        }

        for (int i = 0; i < MAX_THREAD; i++) {
            try {
                threads[i].join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long time = System.currentTimeMillis() - current;
        System.out.println("многопоточный формат: " + time);

        long singleThreadTime = System.currentTimeMillis();
        int[][] result = multiplyMatrices(firstMatrix, secondMatrix, MAX_N);
        long timeSingleEnd = System.currentTimeMillis() - singleThreadTime;
        System.out.println("Однопоточный формат: " + timeSingleEnd);

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

    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int elem : row) {
                System.out.print(elem + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static void printMatrix(long[][] matrix) {
        for (long[] row : matrix) {
            for (long number : row) {
                System.out.print(number + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public static int[][] generateMatrix(int N) {
        int[][] matrix = new int[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = (int) (Math.random() * 10);
            }
        }
        return matrix;
    }

    public static void serializeMatrix(int[][] matrix, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(matrix);
        }
    }

    public static int[][] deserializeMatrix(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (int[][]) ois.readObject();
        }
    }

    public static long[][] generateMatrixLong(int N, long size) {
        long[][] matrix = new long[N][N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                matrix[i][j] = (long) (Math.random() * size);
            }
        }
        return matrix;
    }

    public static void serializeMatrixLong(long[][] matrix, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(matrix);
        }
    }

    public static long[][] deserializeMatrixLong(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (long[][]) ois.readObject();
        }
    }
}
