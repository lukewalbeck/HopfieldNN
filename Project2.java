import com.sun.tools.internal.ws.wsdl.document.http.HTTPOperation;

import java.io.BufferedReader;
import java.io.FileReader;

public class Project2 {

    public static int[] readDataFile(String fileName, int length, int width) {
        try {
            BufferedReader bf = new BufferedReader(new FileReader(fileName));
            int[] initialArray = new int[length * width];
            String currLine;
            int index = 0;
            while((currLine = bf.readLine()) != null) { //TODO: change this so that it reads in one pattern each time
                if(!currLine.equals("")) {
                    String[] splitLine = currLine.split("");
                    for(int i = 0; i < length; i++) {
                        if(i < splitLine.length) {
                            if (splitLine[i].equals("0")) {
                                initialArray[index++] = 1;
                            }
                            else {
                                index++;
                            }
                        }
                        else {
                            index++;
                        }
                    }
                }
                else {
                    index +=10;
                }
            }
            return initialArray;
        }
        catch(Exception ex) {
            ex.getMessage();
        }
        return null;
    }

    public static void main(String[] args) {
        int[] training = readDataFile("training.txt", 10, 10);
        Hopfield hoppy = new Hopfield(100, 100);
        hoppy.train(training);
        int[] testing = readDataFile("testing.txt", 10, 10);
        hoppy.test(testing);

    }
}
