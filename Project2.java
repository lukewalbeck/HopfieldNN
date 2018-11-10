import java.io.*;
import java.util.Scanner;

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
                    for(int i = 0; i < width; i++) {
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
        try {
            FileWriter writer = new FileWriter("output.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(writer);
            for(int i = 0; i < length; i++) {
                for(int j = 0; j < width; j++) {
                    if(array[(j*length) + i] == -1) {
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
        }
    }

    public static void displayMenu() {
        System.out.println("Welcome to Hopfield Neural Network Program!\n");
        System.out.println("1) Read in training data and testing data for Hopfield Neural Network");
        System.out.println("2) Exit Program");
    }

    public static void menu() {
        Scanner in = new Scanner(System.in);
        displayMenu();
        switch(in.nextInt()) {
            case 1:
                System.out.println("Enter number of patterns within file\n");
                int numPatterns = in.nextInt();
                System.out.println("Enter dimension length of single pattern\n");
                int length = in.nextInt();
                System.out.println("Enter dimension width of single pattern\n");
                int width = in.nextInt();
                int numElements = length * width;
                System.out.println("Confirming number of elements in single pattern is " + numElements + "\n");


                System.out.println("Last question: Enter valid name for training data file (with .txt)\n");
                in.nextLine();
                String trainFile = in.nextLine();
                int[] training = readDataFile(trainFile, length, width, numPatterns);
                Hopfield hoppy = new Hopfield(numElements, numElements);
                for(int i = 0; i < numPatterns; i++) {
                    hoppy.train(training, i);
                }

                System.out.println("Training complete ...\nEnter testing data filename (with .txt)\n(Be sure it has same dimensions as training data please :)\n");
                String testFile = in.nextLine();
                System.out.println("Pretty please enter name of file to output data to (give it a .txt extension)\n");
                String output = in.nextLine();
                int[] testing = readDataFile(testFile, length, width, numPatterns);

                //empty previous contents of file
                try {
                    FileWriter writer = new FileWriter(output, false);
                    writer.write("");
                    writer.close();
                }
                catch(IOException e) {
                    e.printStackTrace();
                }


                for(int i = 0; i < numPatterns; i++) {
                    int[] printMe = hoppy.test(testing, numElements, i);
                    printMatrix(printMe, length, width);
                }


                System.out.println("Finished, check " + output + " for correctness and completeness");
                subMenu();
                break;
            case 2:
                System.out.println("Exited program");
                break;
            default:
                System.out.println("Unrecognized option");
                menu();
                break;

        }
    }

    public static void subMenu() {
        System.out.println("1) Try again");
        System.out.println("2) Exit program");
        Scanner in = new Scanner(System.in);
        switch(in.nextInt()) {
            case 1:
                menu();
                break;
            case 2:
                System.out.println("Exited program");
                break;
            default:
                System.out.println("Unrecognized option");
                break;
        }
    }



    public static void main(String[] args) {
        menu();
    }
}
