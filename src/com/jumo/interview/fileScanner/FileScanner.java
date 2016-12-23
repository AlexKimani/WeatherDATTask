package com.jumo.interview.fileScanner;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Task: 
 * Write a program to find the row with the **maximum** spread in the weather.dat file,
 * where spread is defined as the difference between MxT and MnT.
 * client : Jumo.World
 * @author Joe Alex Kimani
 * @version 1.0
 * Author email : joealex.kimani@gmail.com
 */
public class FileScanner {

    /**
     * This is the project main method 
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        String filePath = System.getProperty("user.dir")+File.separator+"weather.dat";
        
        try {
            processDATFileData(filePath);
        } catch (Exception ex) {
            Logger.getLogger(FileScanner.class.getName()).log(Level.SEVERE, 
                    "ERROR in system, please view error thrown: "+ex.getMessage(), ex);
        }
        
    }
    
    /**
     * This function will read the data from the dat file, load it into an ordered list
     * and calculate the spread for each day. It will then determine the maximum spread 
     * and day of the month for the spread
     * @param filePath - the path to the location of the dat file
     * @throws Exception - It can run into: FileNotFoundException - not recoverable
     *                                      IOException - not recoverable
     *                                      ArrayOutOfBounsException - stops upon error, will not determine spread
     */
    private static void processDATFileData(String filePath) throws Exception {
        String fileData = readDATFile(filePath);
        //print out the original file data to easier evaluation
        System.out.println("DATA IN FILE\n\n\n\n" + fileData);

        //load the ordered list read from file into the List<String>
        List<String> dataList = readDATFileIntoList(filePath);
        
        //this map will store a named list of day and spread for each day
        Map<String,Float> dateDiffMap = new HashMap<>();

        //store the file header data in the string variable
        String dataHeader = dataList.get(0);
        //store the file header data in the string variable
        String totalsRow = dataList.get(dataList.size() - 1);//we need to get the value of the last index of the list which is size - 1
        dataList.remove(0);//remove the header value of the list
        dataList.removeAll(Arrays.asList(""));//remove all empty values in the list(remove all white spaces)
        dataList.remove(dataList.size() - 1);//remove the last index of the list

        /*
        * use a functional statement to create a foreach loop that gets each row value and splits it by the whiteSpace
        * and converts the resulting array into a list and removes all empty values in the list. The new List created
        * is then, for each value we get the day, max Temp and min Temp and calculate the spread for each. The day and
        * the spread is then stored in a map.
        */
        dataList.stream().map((dataRow) -> dataRow.trim().split("\\s"))
                .map((dataRowArray) -> new ArrayList<>(Arrays
                        .asList(dataRowArray))).map((dataRowList) -> {
            dataRowList.removeAll(Arrays.asList(""));
            return dataRowList;
        }).forEachOrdered((ArrayList<String> dataRowList) -> {
            String day = dataRowList.get(0);
            
            //use a regex to remove any special characters from the data excluding full stops
            float maxTemp = Float.parseFloat(dataRowList.get(1)
                    .replaceAll("[`~!@#$%^&*()_+[\\\\]\\\\\\\\;\\',/{}|:\\\"<>?]",""));
            float minTemp = Float.parseFloat(dataRowList.get(2)
                    .replaceAll("[`~!@#$%^&*()_+[\\\\]\\\\\\\\;\\',/{}|:\\\"<>?]",""));
            float difference = maxTemp - minTemp;
            
            dateDiffMap.put(day, difference);
        });
        
        System.out.println(System.lineSeparator()+"MAX SPREAD\n");
        //now we locate the maximum spread and the day of the month
        String day = Collections.max(dateDiffMap.keySet());//get the day with the maximum spread
        float maxSpread = Collections.max(dateDiffMap.values());//get the maximum spread
        
        System.out.println("DAY : "+day+" | SPREAD : "+maxSpread);
    }

    /**
     * This function will read all data stored in the dat file and load it into
     * a string line by line by storing each line buffer into StringBuilder
     * 
     * This is the List that is returned by the function
     * 
     * @param pathFile - file path to the location of the file
     * @return - String containing the data buffer
     * @throws FileNotFoundException - FileNotFound - the system cannot locate the dat file : Error not recoverable
     * @throws IOException - thrown if system cannot read the file contents eg: due to file privileges
     */
    private static String readDATFile(String pathFile) throws FileNotFoundException,IOException {
        //use try with resources in order to automatically reassign resources once execution of try is done: only in JDK 7 and above
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathFile))) {
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            //iterate the data buffer until all the data in buffer has been read into the data object
            while ((line = bufferedReader.readLine()) != null) {
                //append the line read from buffer to a string builder to create the dataset in file
                stringBuilder.append(line);
                //append a line separator to spread the data
                stringBuilder.append(System.lineSeparator());
            }
            return stringBuilder.toString();
        }
    }

    /**
     * This function will read all data stored in the dat file and load it into
     * a string line by line by storing each line buffer into List<String> fileContents
     * 
     * This is the List that is returned by the function
     * 
     * @param pathFile - file path to the location of the file
     * @return - List<String> containing ordered list of the data buffer
     * @throws FileNotFoundException - FileNotFound - the system cannot locate the dat file : Error not recoverable
     * @throws IOException - thrown if system cannot read the file contents eg: due to file privileges
     */
    private static List<String> readDATFileIntoList(String pathFile) throws FileNotFoundException,IOException {
        List<String> fileContents = new ArrayList<>();
        //use try with resources in order to automatically reassign resources once execution of try is done: only in JDK 7 and above
        try (BufferedReader bufferedReader = new BufferedReader(new FileReader(pathFile))) {
            String line;
            //iterate the data buffer until all the data in buffer has been read into the data object
            while ((line = bufferedReader.readLine()) != null) {
                //add every line buffer into the list, this will create an ordered list of the file data
                fileContents.add(line);
            }
        }
        return fileContents;
    }
}
