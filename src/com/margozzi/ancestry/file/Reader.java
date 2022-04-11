package com.margozzi.ancestry.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.margozzi.ancestry.model.Individual;

public class Reader {
    private File file;
    private HashMap<String, Individual> individuals = new HashMap<String, Individual>();
    private int countMale = 0;
    private int countFemale = 0;
    private int idIdx, genderIdx, firstNameIdx, middleNameIdx, lastNameIdx, birthDateIdx, deathDateIdx;
    private int spouseIdx, fatherIdx, motherIdx, siblingIdx, childrenIdx;

    public Reader(File file) {
        this.file = file;
    }

    public void read() {
        try {
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
                    spouseIdx = list.indexOf("Mates");
                    fatherIdx = list.indexOf("Fathers");
                    motherIdx = list.indexOf("Mothers");
                    siblingIdx = list.indexOf("Siblings");
                    childrenIdx = list.indexOf("Children");
                    break;
                }
            }
            while (scanner.hasNextLine()) {
                String data = scanner.nextLine();
                if (data.startsWith("[")) {
                    break; // Reached a new section.
                }
                String[] tokens = data.split("\t");
                int numTokens = tokens.length;

                String id = numTokens > idIdx ? tokens[idIdx] : null;
                if (id == null) {
                    System.out.println("No ID for " + data);
                    continue;
                }

                String gender = numTokens > genderIdx ? tokens[genderIdx] : null;
                if (gender != null) {
                    if (gender.equalsIgnoreCase("M")) {
                        countMale++;
                    } else {
                        countFemale++;
                    }
                } else {
                    System.out.println("No gender for: " + data);
                    continue;
                }

                String firstName = numTokens > firstNameIdx ? tokens[firstNameIdx] : null;
                String middleName = numTokens > middleNameIdx ? tokens[middleNameIdx] : null;
                String lastName = numTokens > lastNameIdx ? tokens[lastNameIdx] : null;
                if (firstName == null || firstName.length() == 0) {
                    if (middleName == null || middleName.length() == 0) {
                        if (lastName == null || lastName.length() == 0) {
                            System.out.println("No name for: " + tokens[idIdx]);
                            continue;
                        }
                    }
                }

                String birthDate = numTokens > birthDateIdx ? tokens[birthDateIdx] : null;
                String deathDate = numTokens > deathDateIdx ? tokens[deathDateIdx] : null;

                String spouse = numTokens > spouseIdx ? tokens[spouseIdx] : null;

                String father = numTokens > fatherIdx ? tokens[fatherIdx] : null;
                String mother = numTokens > motherIdx ? tokens[motherIdx] : null;

                String siblings = numTokens > siblingIdx ? tokens[siblingIdx] : null;
                String children = numTokens > childrenIdx ? tokens[childrenIdx] : null;

                Individual individual = Individual.createNewInstance(id, gender, firstName,
                        middleName, lastName, birthDate, deathDate, spouse, mother, father, siblings, children);
                individuals.put(individual.getId(), individual);
            }
            scanner.close();
        } catch (

        FileNotFoundException e) {
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