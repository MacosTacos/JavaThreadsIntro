package determinant;

import secondWork.MatrixMult;

import java.io.IOException;

import static secondWork.MatrixMult.generateMatrixLong;

public class MatrixMinor {
    static final int SIZE = 11;
    static final int MAX_THREAD = 12;
    static long[][] matrix = new long[SIZE][SIZE];
    static int step_i = 0;


    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        String fileNameA = "matrixmin.ser";
        long[][] matrixA = generateMatrixLong(SIZE, 100);
        try {
            MatrixMult.serializeMatrixLong(matrixA, fileNameA);
        } catch (Exception e) {
            e.printStackTrace();
        }
        matrix = MatrixMult.deserializeMatrixLong(fileNameA);
        MatrixMult.printMatrix(matrix);
        long timeStartSingle = System.currentTimeMillis();
        System.out.printf("Determinant: %d\n", calculateDeterminant(matrix));
        long timeEndSingle = System.currentTimeMillis();
        System.out.printf("Single thread time: %d ms\n", timeEndSingle - timeStartSingle);

        matrix = MatrixMult.deserializeMatrixLong(fileNameA);

        long startTime = System.currentTimeMillis();
        long determinant = MatrixDeterminant.calculateDeterminant(matrix);
        long endTime = System.currentTimeMillis();
        System.out.printf("Determinant: %d\n", determinant);
        System.out.printf("Execution Time: %d ms\n", endTime - startTime);
    }

    public static long calculateDeterminant(long[][] matrix) {
        int size = matrix.length;
        if (size == 1) {
            return matrix[0][0];
        } else if (size == 2) {
            return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
        } else {
            long determinant = 0;
            for (int col = 0; col < size; col++) {
                long[][] minor = getMinor(matrix, 0, col);
                long minorDeterminant = calculateDeterminant(minor);
                long cofactor = ((col % 2 == 0) ? 1 : -1) * matrix[0][col] * minorDeterminant;
                determinant += cofactor;
            }
            return determinant;
        }
    }

    private static long[][] getMinor(long[][] matrix, int excludeRow, int excludeCol) {
        int size = matrix.length;
        long[][] minor = new long[size - 1][size - 1];
        int rowOffset = 0;

        for (int i = 0; i < size; i++) {
            if (i == excludeRow) {
                rowOffset = -1;
                continue;
            }
            int colOffset = 0;
            for (int j = 0; j < size; j++) {
                if (j == excludeCol) {
                    colOffset = -1;
                    continue;
                }
                minor[i + rowOffset][j + colOffset] = matrix[i][j];
            }
        }
        return minor;
    }
}
