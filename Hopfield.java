import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Random;

public class Hopfield {
    private int[][] weights;
    private int m_length;
    private int m_width;

    public Hopfield(int length, int width) {
        m_length = length;
        m_width = width;
        weights = new int[m_length][m_width];
    }

    //sampleDat is for a single pattern, this will be run n pattern times in main
    public void train(int[] sampleDat, int numPattern) {
        for(int i = 0; i < m_length; i++) {
            for(int j = 0; j < m_width; j++) {
                if(j == i) {
                    weights[i][j] += 0;
                }
                else {
                    weights[i][j] += sampleDat[i + (numPattern * m_length)] * sampleDat[j + (numPattern * m_width)];
                }
            }
        }
        printWeights();
    }


    public void loadWeights(int[][] sampleDat) {
        weights = sampleDat;
    }

    public void printWeights() {
        try {
            FileWriter writer = new FileWriter("weights.txt", false);
            writer.write("");
            PrintWriter printWriter = new PrintWriter(writer);

            for(int i = 0; i < m_length; i++) {
                for(int j = 0; j < m_width; j++) {
                    int num = weights[i][j];
                    String print = Integer.toString(num);
                    printWriter.write(print);
                    printWriter.write(" ");
                }
                printWriter.write("\n");
            }

            printWriter.close();
            writer.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }


    }

    //testDat is a single pattern, will be run n pattern times to test, and output n times in main
    public int[] test(int[] testDat, int numElements, int numPattern) {
        int from = numElements * numPattern;
        int count = 0;
        testDat = Arrays.copyOfRange(testDat, from, from+numElements);
        int[] y = createCopy(testDat);
        int[] ordering = createOrdering(y.length);
        boolean converged = false;
        while(!converged) {
            count = 0;
            for(int i = 0; i < ordering.length; i++) {
                int index = ordering[i];
                int newY = computationOfY(index, testDat, y);
                if(newY != 0) {
                    if(y[index] != newY) {
                        y[index] = newY;
                        count++;
                    }
                }
            }
            //compare y to testDat, if not equal, testDat equals y
            if(Arrays.equals(y, testDat)) {
                converged = true;
            }
            else {
                testDat = y;
            }
        }
        return y;
    }

    private int[] createOrdering(int size) {
        int[] array = new int[size];
        for(int i = 0; i < size; i++) {
            array[i] = i;
        }
        shuffleArray(array);
        return array;
    }

    private int[] createCopy(int[] array) {
        int[] newArr = new int[array.length];
        for(int i = 0; i < newArr.length; i++) {
            newArr[i] = array[i];
        }
        return  newArr;
    }

    private static void shuffleArray(int[] array) {
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

    private int computationOfY(int index, int[] x, int[] y) {
        int sum = y[index];
        for(int i = 0; i < y.length; i++) {
            sum += (y[i] * weights[index][i]); //accessing weights row-wise
        }

        if(sum > 0) {
            return 1;
        }
        else if(sum < 0) {
            return -1;
        }
        else {
            return 0; //no change
        }
    }

}
