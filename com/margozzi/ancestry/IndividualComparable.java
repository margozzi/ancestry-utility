package com.margozzi.ancestry;

import com.margozzi.ancestry.model.Individual;

public class IndividualComparable {

    /** Should all fields match exactly */
    private boolean exactMatch = false;
    /** Match must include first name */
    private boolean firstName = true;
    /** Match must include middle name */
    private boolean middleName = false;
    /** Match must include last name */
    private boolean lastName = true;
    /** Match must include birth year */
    private boolean birthYear = true;
    /** How many years off can the birth date be */
    private int birthYearVariance = 5;
    /** Match must include death year */
    private boolean deathYear = true;
    /** How many years off can the birth date be */
    private int deathYearVariance = 5;
    /** Include nick names, known misspellings and so on */
    private boolean fuzzyNames = false;

    public IndividualComparable() {
    }

    public IndividualComparable(boolean exactMatch, boolean firstName, boolean middleName, boolean lastName,
            int birthYearVariance, int deathYearVariance, boolean fuzzyNames, String fuzzyNameFilePath) {

    }

    public boolean compare(Individual left, Individual right) {
        if (firstName && left.getFirstName().equalsIgnoreCase(right.getFirstName()) == false) {
            return (false);
        }

        if (middleName && left.getMiddleName().equalsIgnoreCase(right.getMiddleName()) == false) {
            return (false);
        }

        if (lastName && left.getLastName().equalsIgnoreCase(right.getLastName()) == false) {
            return (false);
        }

        if (birthYear && left.getBirthDateYear() != null && right.getBirthDateYear() != null) {
            int leftYear = left.getBirthDateYear().intValue();
            int rightYear = right.getBirthDateYear().intValue();
            if (Math.abs(leftYear - rightYear) > birthYearVariance) {
                return (false);
            }
        }

        if (deathYear && left.getBirthDateYear() != null && right.getBirthDateYear() != null) {
            int leftYear = left.getBirthDateYear().intValue();
            int rightYear = right.getBirthDateYear().intValue();
            if (Math.abs(leftYear - rightYear) > birthYearVariance) {
                return (false);
            }
        }
        return (true);
    }
}