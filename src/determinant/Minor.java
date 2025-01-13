package determinant;

import java.io.*;
import java.util.Random;

public class Minor {
    static final int SIZE = 12;
    static long[][] matrix = new long[SIZE][SIZE];


    public static void main(String[] args) throws InterruptedException, IOException, ClassNotFoundException {
        String name = "matrixForDeterm.ser";
        Random random = new Random();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                matrix[i][j] = (random.nextLong());
            }
        }
        try {
            serializeMatrix(matrix, name);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        matrix = deserializeMatrixLong(fileNameA);
        long startTime = System.currentTimeMillis();
        long determinant = Determinant.calculate(matrix);
        long endTime = System.currentTimeMillis();
        System.out.printf("определитель: %d\n", determinant);
        System.out.printf("Многопоточный формат: %d ms\n", endTime - startTime);

//        matrix = deserializeMatrix(name);
        startTime = System.currentTimeMillis();
        System.out.printf("определитель: %d\n", calculateDeterminant(matrix));
        long timeEndSingle = System.currentTimeMillis();
        System.out.printf("Однопоточный формат: %d ms\n", timeEndSingle - startTime);
    }

    public static long calculateDeterminant(long[][] matrix) {
        int size = matrix.length;

        switch (size) {
            case 1:
                return matrix[0][0];
            case 2:
                return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
            default:
                long determinant = 0;
                for (int col = 0; col < size; col++) {
                    determinant += ((col % 2 == 0 ? 1 : -1) * matrix[0][col] * calculateDeterminant(createMinor(matrix, col)));
                }
                return determinant;
        }
    }

    private static long[][] createMinor(long[][] matrix, int excludeCol) {
        int size = matrix.length;
        long[][] minor = new long[size - 1][size - 1];

        for (int i = 1; i < size; i++) {
            int colIndex = 0;
            for (int j = 0; j < size; j++) {
                if (j != excludeCol) {
                    minor[i - 1][colIndex++] = matrix[i][j];
                }
            }
        }

        return minor;
    }

    public static void serializeMatrix(long[][] matrix, String fileName) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName))) {
            oos.writeObject(matrix);
        }
    }

    public static long[][] deserializeMatrix(String fileName) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName))) {
            return (long[][]) ois.readObject();
        }
    }
}
