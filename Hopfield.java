import java.util.Arrays;
import java.util.Random;

public class Hopfield {
    int[][] weights;
    int m_length;
    int m_width;

    public Hopfield(int length, int width) { //size must be equal to sampleDat.length
        m_length = length;
        m_width = width;
        weights = new int[m_length][m_width];
    }

    //sampleDat is for a single pattern, this will be run n pattern times in main
    public void train(int[] sampleDat) {
        for(int i = 0; i < m_length; i++) {
            for(int j = 0; j < m_width; j++) {
                if(j == i) {
                    weights[i][j] += 0;
                }
                else {
                    weights[i][j] += sampleDat[i] * sampleDat[j];
                }
            }
        }
    }

    //testDat is a single pattern, will be run n pattern times to test, and output n times in main
    public void test(int[] testDat) {
        int[] y = testDat;
        int[] ordering = createOrdering(testDat.length);
        shuffleArray(ordering);
        boolean converged = false;
        while(!converged) {
            for(int i = 0; i < ordering.length; i++) {
                int index = ordering[i];
                int newY = computationOfY(index, testDat, y);
                if(newY != 0) {
                    y[index] = newY;
                }
            }
            //compare y to testDat, if not equal, testDat equals y
            if(Arrays.equals(y, testDat)) {
                converged = true;
                System.out.println("here");
            }
            else {
                testDat = y;
            }
        }
    }

    public int[] createOrdering(int size) {
        int[] array = new int[size];
        for(int i = 0; i < size; i++) {
            array[i] = i;
        }
        return array;
    }

    private static void shuffleArray(int[] array)
    {
        int index, temp;
        Random random = new Random();
        for (int i = array.length - 1; i > 0; i--)
        {
            index = random.nextInt(i + 1);
            temp = array[index];
            array[index] = array[i];
            array[i] = temp;
        }
    }

    public int computationOfY(int index, int[] x, int[] y) {
        int sum = x[index];
        for(int i = 0; i < y.length; i++) {
            sum += (y[i] * weights[index][i]); //accessing weights row-wise
        }

        if(sum > 1) {
            return 1;
        }
        else if(sum < 1) {
            return -1;
        }
        else {
            return 0; //no change
        }
    }

}
