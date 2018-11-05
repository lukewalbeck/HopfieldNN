import java.io.BufferedReader;
import java.io.FileReader;

public class Project2 {

    public static int[] readDataFile(String fileName, int length, int width, int numPatterns) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(fileName));
            int[] initialArray = new int[numPatterns * length * width];
            String currLine;
            int index = 0;
            int lineCount = 0;
            while((currLine = bf.readLine()) != null) {
                lineCount++;
                if(lineCount == 10) {
                    lineCount = -2;
                }
                if(!currLine.equals("")) {
                    String[] splitLine = currLine.split("");
                    for(int i = 0; i < length; i++) {
                        if(i < splitLine.length) {
                            if (splitLine[i].equals("0")) {
                                initialArray[index++] = 1;
                            }
                            else {
                                initialArray[index++] = -1;
                            }
                        }
                        else {
                            initialArray[index++] = -1;
                        }
                    }
                }
                else if(lineCount <= 0) {

                }
                else {
                    for(int i = 0; i < 10; i++) {
                        initialArray[index++] = -1;
                    }
                }
            }
            return initialArray;
        }
        catch(Exception ex) {
            ex.getMessage();
        }
        return null;
    }

    public static void printMatrix(int[] array, int length, int width) {
        for(int i = 0; i < length; i++) {
            for(int j = 0; j < width; j++) {
                if(array[(j*length) + i] == -1) {
                    System.out.print(" ");
                }
                else {
                    System.out.print("0 ");
                }

            }
            System.out.println();
        }
        System.out.println();
    }



    public static void main(String[] args) {
        int numPatterns = 5;
        int numElements = 100;
        int length = 10;
        int width = 10;
        int[] training = readDataFile("training.txt", length, width, numPatterns);
        Hopfield hoppy = new Hopfield(numElements, numElements);
        for(int i = 0; i < numPatterns; i++) {
            hoppy.train(training, i);
        }
        int[] testing = readDataFile("testing.txt", length, width, numPatterns);
        for(int i = 0; i < numPatterns; i++) {
            int[] printMe = hoppy.test(testing, numElements, i);
            printMatrix(printMe, length, width);
        }
        System.out.println("Made it");
    }
}
