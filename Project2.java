import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Project2 {

    public static int[] readTrainingPatterns(String fileName) {
        try{
            BufferedReader bf = new BufferedReader(new FileReader(fileName));
            String currLine;
            int numPatterns = 0;
            int numItems = 0;
            int numLength = 0;
            int lineCount = 0;
            boolean showNothing = true;
            //reading for total num and patterns
            while((currLine = bf.readLine()) != null) {

                String[] splitLine = currLine.split("\\s+");


                if(lineCount == 0) {
                    numItems = Integer.parseInt(splitLine[0]); //find numItems -- 1st line
                }
                else if(lineCount == 1) {
                    numPatterns = Integer.parseInt(splitLine[0]); //find numPatterns -- 2nd line
                }
                else if(lineCount == 2){
                    //nothing
                }
                else {
                    if(!currLine.equals("")) {
                        numLength++;
                        showNothing = false;
                    }
                    else if(currLine.equals("") && showNothing) {
                        numLength++;
                    }
                    else {
                        int numWidth = (numItems / numLength);
                        int[] vals = new int[3];
                        vals[0] = numItems;
                        vals[1] = numPatterns;
                        vals[2] = numLength;
                        return vals;
                    }
                }
                lineCount++;
            }
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Hopfield readWeights(String weightFile) {
        try {
            FileReader f = new FileReader(weightFile);
            BufferedReader bf = new BufferedReader(f);

            LineNumberReader count = new LineNumberReader(new FileReader(weightFile));
            while(count.skip(Long.MAX_VALUE) > 0){

            }
            int width = count.getLineNumber();
            int[][] weights = new int[width][width];

            String currLine;
            int j = 0;
            while((currLine = bf.readLine()) != null) {
                String[] splitLine = currLine.split("\\s+");
                for(int i = 0; i < splitLine.length; i++) {
                    weights[j][i] = Integer.parseInt(splitLine[i]);
                }
                j++;
            }
            Hopfield hoppy = new Hopfield(width, width);
            hoppy.loadWeights(weights);
            return hoppy;
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        catch(NumberFormatException n) {
            n.printStackTrace();
            System.out.println("Something went wrong with parsing");
        }
        return null;
    }


    public static int[] readDataFile(String fileName, int length, int width, int numPatterns) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(fileName));
            int[] initialArray = new int[numPatterns * length * width];
            String currLine;
            int index = 0;
            int lineCount = 0;
            int skipThreeLines = 3;
            for(int i = 0; i < 3; i++) {
                bf.readLine();
            }
            while((currLine = bf.readLine()) != null) {
                lineCount++;
                if(lineCount == width) {
                    lineCount = -2; //assumes there's two empty lines between each pattern
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
                    for(int i = 0; i < length; i++) {
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

    public static void printMatrix(int[] training, int[] testing, int[] outArr, int length, int width, String output) {
        try {
            FileWriter writer = new FileWriter(output, true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);

            for(int i = 0; i < length; i++) {
                if(training != null) {
                    for(int j = 0; j < width; j++) {
                        if(training[(j*length) + i] == -1) {
                            bufferedWriter.write(" ");
                        }
                        else {
                            bufferedWriter.write("0");
                        }
                    }
                    bufferedWriter.write("\t\t");
                }
                for(int j = 0; j < width; j++) {
                    if(testing[(j*length) + i] == -1) {
                        bufferedWriter.write(" ");
                    }
                    else {
                        bufferedWriter.write("0");
                    }
                }
                bufferedWriter.write("\t\t");
                for(int j = 0; j < width; j++) {
                    if(outArr[(j*length) + i] == -1) {
                        bufferedWriter.write(" ");
                    }
                    else {
                        bufferedWriter.write("0");
                    }
                }
                bufferedWriter.newLine();
            }
            bufferedWriter.newLine();
            bufferedWriter.newLine();
            bufferedWriter.close();
            writer.close();

        }
        catch (IOException e) {
            e.printStackTrace();
            System.out.println("Something went wrong with " + output);
        }
    }

    public static void displayMenu() {
        System.out.println("Welcome to Hopfield Neural Network Program!\n");
        System.out.println("1) Read in training data and testing data for Hopfield Neural Network");
        System.out.println("2) Read in weights data and testing data for Hopfield Neural Network");
        System.out.println("3) Exit Program");
    }

    public static void menu() {
        Scanner in = new Scanner(System.in);
        displayMenu();
        switch(in.nextLine()) {
            case "1":
                System.out.println("Enter valid name for training data file (with .txt)\n");
                String trainFile = in.nextLine();
                int[] vals = readTrainingPatterns(trainFile);
                if(vals != null) {
                    int numElements = vals[0];
                    int numPatterns = vals[1];
                    int length = vals[2];
                    int width = numElements / length;
                    System.out.println("Number of patterns in single pattern is " + numPatterns);
                    System.out.println("Number of elements in single pattern is " + numElements);
                    System.out.println("Dimensions are " + length + " by " + width);
                    int[] training = readDataFile(trainFile, length, width, numPatterns);
                    Hopfield hoppy = new Hopfield(numElements, numElements);
                    for(int i = 0; i < numPatterns; i++) {
                        hoppy.train(training, i);
                    }
                    testMenu(length, width, hoppy, training);
                }
                else {
                    System.out.println("An error has occurred");
                }
                break;
            case "2":
                System.out.println("Enter valid name for weights data file\n");
                String weightFileName = in.nextLine();
                Hopfield hoppy = readWeights(weightFileName);
                if(hoppy != null) {
                    System.out.println("Successfully loaded weight data");
                    testMenu(0, 0, hoppy, null);
                }
            case "3":
                System.out.println("Exited program");
                break;
            default:
                System.out.println("Unrecognized option");
                menu();
                break;

        }
    }

    public static void testMenu(int length, int width, Hopfield hoppy, int[] training) {
        System.out.println("Training Complete! Weights saved in weights.txt\nSelection for Testing\n");
        System.out.println("1) Test");
        System.out.println("2) Exit program");
        Scanner in = new Scanner(System.in);
        switch(in.nextLine()) {
            case "1":
                System.out.println("Please enter name for testing file");
                String testFile = in.nextLine();
                int[] vals = readTrainingPatterns(testFile);
                if(vals != null) {
                    int numElements = vals[0];
                    int numPatterns = vals[1];
                    int numLength = vals[2];

                    if(length == 0 && width == 0) {
                        length = numLength;
                        width = numElements / numLength;
                    }

                    if((length * width) != numElements) {
                        System.out.println("Something went wrong with the test file, does not match training file");
                    }
                    else {
                        int[] testing = readDataFile(testFile, length, width, numPatterns);
                        System.out.println("Please enter name for output text file");
                        String output = in.nextLine();
                        //empty previous contents of file
                        try {
                            FileWriter writer = new FileWriter(output, false);
                            writer.write("");
                            BufferedWriter bufferedWriter = new BufferedWriter(writer);

                            if(training != null) {
                                bufferedWriter.write("Training data\t");
                            }

                            bufferedWriter.write("Testing data\t");
                            bufferedWriter.write("Output data\n\n");
                            bufferedWriter.close();
                            writer.close();
                        }
                        catch(IOException e) {
                            e.printStackTrace();
                        }
                        for(int i = 0; i < numPatterns; i++) {
                            int from = numElements * i;
                            int[] trainArr = null;
                            if(training != null) {
                                trainArr = Arrays.copyOfRange(training, from, from+numElements);
                            }
                            int[] testArr = Arrays.copyOfRange(testing, from, from+numElements);
                            int[] printMe = hoppy.test(testing, numElements, i);
                            printMatrix(trainArr, testArr, printMe, length, width, output);
                        }
                        System.out.println("Finished training, check " + output + " for correctness and completeness");

                        endMenu();
                        break;
                    }
                }
                break;
            case "2":
                System.out.println("Exited program");
                break;
            default:
                System.out.println("Unrecognized option");
                testMenu(length, width, hoppy, training);
                break;
        }
    }

    public static void endMenu() {
        System.out.println("1) Try again");
        System.out.println("2) Exit program");
        Scanner in = new Scanner(System.in);
        switch(in.nextLine()) {
            case "1":
                menu();
                break;
            case "2":
                break;
            default:
                System.out.println("Unrecognized option");
                endMenu();
                break;
        }
        in.close();

    }



    public static void main(String[] args) {
        menu();
        //readTrainingPatterns("training.txt");
    }
}
