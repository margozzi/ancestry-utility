package com.margozzi.ancestry.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import com.margozzi.ancestry.model.Individual;

public class Reader {
    private String filePath;
    private ArrayList<Individual> individuals = new ArrayList<Individual>();
    private int countMale = 0;
    private int countFemale = 0;

    public Reader(String filePath) {
        this.filePath = filePath;
    }

    public void read() {
        try {
            File file = new File(filePath);
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                if (scanner.nextLine().equals("[Individuals]")) {
                    scanner.nextLine(); // Eat the labels
                    break;
                }
            }
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                if (data.startsWith("[")) {
                    break; // Reached a new section.
                }
                String[] tokens = data.split("\t");
                if (tokens.length < 9) {
                    System.out.println("Bad line in file: " + data);
                } else {
                    if (tokens[1].equalsIgnoreCase("M")) {
                        countMale++;
                    } else {
                        countFemale++;
                    }
                    Individual individual = Individual.createNewInstance(tokens[0], tokens[1], tokens[2], tokens[3],
                            tokens[4], tokens[8]);
                    individuals.add(individual);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Not able to open the file");
            e.printStackTrace();
        }
    }

    public ArrayList<Individual> getIndividuals() {
        return individuals;
    }

    public int getCountMale() {
        return countMale;
    }

    public int getCountFemale() {
        return countFemale;
    }
}