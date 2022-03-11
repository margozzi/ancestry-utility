package com.margozzi.ancestry.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.margozzi.ancestry.model.Individual;

public class Reader {
    private String filePath;
    private HashMap<String, Individual> individuals = new HashMap<String, Individual>();
    private int countMale = 0;
    private int countFemale = 0;
    private int idIdx, genderIdx, firstNameIdx, middleNameIdx, lastNameIdx, birthDateIdx, deathDateIdx;
    private int fatherIdx, motherIdx;

    public Reader(String filePath) {
        this.filePath = filePath;
    }

    public void read() {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().startsWith("[Individuals]")) {
                    String[] labels = scanner.nextLine().split("\t");
                    List<String> list = Arrays.asList(labels);
                    idIdx = list.indexOf("ID");
                    genderIdx = list.indexOf("Gender");
                    firstNameIdx = list.indexOf("Name.First");
                    middleNameIdx = list.indexOf("Name.Middle");
                    lastNameIdx = list.indexOf("Name.Last");
                    birthDateIdx = list.indexOf("Birth.Date");
                    deathDateIdx = list.indexOf("Death.Date");
                    fatherIdx = list.indexOf("Fathers");
                    motherIdx = list.indexOf("Mothers");
                    break;
                }
            }
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                if (data.startsWith("[")) {
                    break; // Reached a new section.
                }
                String[] tokens = data.split("\t");
                if (tokens.length < 13) {
                    System.out.println("Bad line in file: " + data);
                } else {
                    if (tokens[1].equalsIgnoreCase("M")) {
                        countMale++;
                    } else {
                        countFemale++;
                    }
                    if (tokens[firstNameIdx].length() == 0 && tokens[middleNameIdx].length() == 0
                            && tokens[lastNameIdx].length() == 0) {
                        continue;
                    }
                    Individual individual = Individual.createNewInstance(tokens[idIdx], tokens[genderIdx],
                            tokens[firstNameIdx], tokens[middleNameIdx],
                            tokens[lastNameIdx], tokens[birthDateIdx], tokens[deathDateIdx], tokens[fatherIdx],
                            tokens[motherIdx]);
                    individuals.put(individual.getId(), individual);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Not able to open the file");
            e.printStackTrace();
        }
    }

    public HashMap<String, Individual> getIndividuals() {
        return individuals;
    }

    public int getCountMale() {
        return countMale;
    }

    public int getCountFemale() {
        return countFemale;
    }
}