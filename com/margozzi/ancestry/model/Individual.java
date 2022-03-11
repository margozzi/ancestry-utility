package com.margozzi.ancestry.model;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAccessor;
import java.util.Locale;

public final class Individual {

    private final String id;
    private final String gender;
    private final String firstName;
    private final String middleName;
    private final String lastName;
    private final Integer birthDateDay;
    private final Integer birthDateMonth;
    private final Integer birthDateYear;
    private final Integer deathDateDay;
    private final Integer deathDateMonth;
    private final Integer deathDateYear;
    private final String motherId;
    private final String fatherId;

    private String motherFullName;
    private String fatherFullName;

    // Default private constructor will ensure no unplanned construction of class
    private Individual(String id, String gender, String firstName, String middleName,
            String lastName, Integer birthDateDay, Integer birthDateMonth, Integer birthDateYear, Integer deathDateDay,
            Integer deathDateMonth, Integer deathDateYear,
            String motherId, String fatherId) {
        this.id = id;
        this.gender = gender;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthDateDay = birthDateDay;
        this.birthDateMonth = birthDateMonth;
        this.birthDateYear = birthDateYear;
        this.deathDateDay = deathDateDay;
        this.deathDateMonth = deathDateMonth;
        this.deathDateYear = deathDateYear;
        this.motherId = motherId;
        this.fatherId = fatherId;
    }

    // Factory method to store object creation logic in single place
    public static Individual createNewInstance(String id, String gender, String firstName,
            String middleName, String lastName, String birthDate, String deathDate, String motherId,
            String fatherId) {

        Integer[] numericBirthDate = parseDateString(birthDate);
        Integer[] numericDeathDate = parseDateString(deathDate);
        return (new Individual(id, gender, firstName, middleName, lastName, numericBirthDate[0], numericBirthDate[1],
                numericBirthDate[2], numericDeathDate[0], numericDeathDate[1],
                numericDeathDate[2], motherId, fatherId));
    }

    private static Integer[] parseDateString(String birthDate) {
        Integer[] triplet = new Integer[3];
        if (birthDate != null) {
            try {
                String[] tokens = birthDate.trim().split("\\s+|~|-");
                if (tokens.length != 0) { // No Date
                    if (tokens.length == 1 && tokens[0].length() == 4) {
                        triplet[2] = Integer.parseInt(tokens[0]);
                    } else if (tokens.length == 2) {
                        if (tokens[0].length() == 4) {
                            triplet[2] = Integer.parseInt(tokens[0]);
                        } else if (tokens[1].length() == 4) {
                            triplet[2] = Integer.parseInt(tokens[1]);
                        }
                    } else {
                        triplet[0] = Integer.parseInt(tokens[0]); // Day
                        triplet[2] = Integer.parseInt(tokens[2]); // Year

                        DateTimeFormatter parser = DateTimeFormatter.ofPattern("MMM").withLocale(Locale.ENGLISH);
                        TemporalAccessor accessor = parser.parse(tokens[1]);
                        triplet[1] = accessor.get(ChronoField.MONTH_OF_YEAR); // Month
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Unparsable Birth Date: " + birthDate);
            }
        }
        return triplet;

    }

    public String getId() {
        return id;
    }

    public String getGender() {
        return gender;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        String name = "";
        if (firstName != null)
            name += firstName;
        if (middleName != null)
            name += " " + middleName;
        if (lastName != null)
            name += " " + lastName;
        return (name);
    }

    public Integer getBirthDateDay() {
        return birthDateDay;
    }

    public Integer getBirthDateMonth() {
        return birthDateMonth;
    }

    public Integer getBirthDateYear() {
        return birthDateYear;
    }

    public Integer getDeathDateDay() {
        return deathDateDay;
    }

    public Integer getDeathDateMonth() {
        return deathDateMonth;
    }

    public Integer getDeathDateYear() {
        return deathDateYear;
    }

    public String getMotherId() {
        return motherId;
    }

    public String getMotherFullName() {
        return motherFullName;
    }

    public void setMotherFullName(String name) {
        motherFullName = name;
    }

    public String getFatherId() {
        return fatherId;
    }

    public String getFatherFullName() {
        return fatherFullName;
    }

    public void setFatherFullName(String name) {
        fatherFullName = name;
    }

    @Override
    public String toString() {
        String individual = id;
        if (firstName != null)
            individual += "\n" + firstName;
        if (middleName != null)
            individual += " " + middleName;
        if (lastName != null)
            individual += " " + lastName;
        individual += "\n";
        individual += gender.equalsIgnoreCase("m") ? "Male" : "Female";
        individual += "\n";
        individual += "Born ";
        if (birthDateDay != null)
            individual += birthDateDay + "-";
        if (birthDateMonth != null)
            individual += birthDateMonth + "-";
        if (birthDateYear != null)
            individual += birthDateYear;
        if (birthDateDay == null && birthDateMonth == null && birthDateYear == null)
            individual += "Unknown";

        individual += "\nDied ";
        if (deathDateDay != null)
            individual += deathDateDay + "-";
        if (deathDateMonth != null)
            individual += deathDateMonth + "-";
        if (deathDateYear != null)
            individual += deathDateYear;
        if (deathDateDay == null && deathDateMonth == null && deathDateYear == null)
            individual += "Unknown";

        individual += "\nMother  " + motherId;
        individual += "\nFather  " + fatherId;

        return individual;
    }

    public static void main(String[] args) {
        Integer[] date = parseDateString("1 Jun 1908");
        System.out.println(date[0] + "-" + date[1] + "-" + date[2]);
    }
}