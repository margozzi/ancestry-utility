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

    // Default private constructor will ensure no unplanned construction of class
    private Individual(String id, String gender, String firstName, String middleName,
            String lastName, Integer birthDateDay, Integer birthDateMonth, Integer birthDateYear) {
        this.id = id;
        this.gender = gender;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthDateDay = birthDateDay;
        this.birthDateMonth = birthDateMonth;
        this.birthDateYear = birthDateYear;
    }

    // Factory method to store object creation logic in single place
    public static Individual createNewInstance(String id, String gender, String firstName,
            String middleName, String lastName, String birthDate) {

        Integer[] numericDate = parseDateString(birthDate);
        return (new Individual(id, gender, firstName, middleName, lastName, numericDate[0], numericDate[1],
                numericDate[2]));
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

    public Integer getBirthDateDay() {
        return birthDateDay;
    }

    public Integer getBirthDateMonth() {
        return birthDateMonth;
    }

    public Integer getBirthDateYear() {
        return birthDateYear;
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
        individual += "\nBorn ";
        if (birthDateDay != null)
            individual += birthDateDay + "-";
        if (birthDateMonth != null)
            individual += birthDateMonth + "-";
        if (birthDateYear != null)
            individual += birthDateYear + "\n\n";
        if (birthDateDay == null && birthDateMonth == null && birthDateYear == null)
            individual += "Unknown" + "\n\n";
        return individual;
    }

    public static void main(String[] args) {
        Integer[] date = parseDateString("1 Jun 1908");
        System.out.println(date[0] + "-" + date[1] + "-" + date[2]);
    }
}