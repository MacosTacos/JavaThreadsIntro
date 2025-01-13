package determinant;

import java.util.concurrent.RecursiveTask;
import java.util.concurrent.ForkJoinPool;

public class Determinant {
    static class DeterminantCalculator extends RecursiveTask<Long> {
        private final long[][] matrix;

        public DeterminantCalculator(long[][] matrix) {
            this.matrix = matrix;
        }

        @Override
        protected Long compute() {
            int size = matrix.length;

            if (size == 1) {
                return matrix[0][0];
            }

            if (size == 2) {
                return matrix[0][0] * matrix[1][1] - matrix[0][1] * matrix[1][0];
            }

            long determinant = 0;
            DeterminantCalculator[] subtasks = new DeterminantCalculator[size];

            for (int col = 0; col < size; col++) {
                long[][] minor = createMinor(matrix, col);
                subtasks[col] = new DeterminantCalculator(minor);
                subtasks[col].fork();
            }

            for (int col = 0; col < size; col++) {
                long cofactor = ((col % 2 == 0) ? 1 : -1) * matrix[0][col];
                determinant += cofactor * subtasks[col].join();
            }

            return determinant;
        }

        private long[][] createMinor(long[][] matrix, int excludeCol) {
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
    }

    public static long calculate(long[][] matrix) {
        ForkJoinPool pool = new ForkJoinPool();
        DeterminantCalculator task = new DeterminantCalculator(matrix);
        return pool.invoke(task);
    }
}
