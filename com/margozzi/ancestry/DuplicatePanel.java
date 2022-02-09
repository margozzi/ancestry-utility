package com.margozzi.ancestry;

import java.util.ArrayList;

import javax.swing.JPanel;

import com.margozzi.ancestry.file.Reader;
import com.margozzi.ancestry.model.Individual;

public class DuplicatePanel extends JPanel {

    private Reader reader;

    public DuplicatePanel(String filePath) {
        reader = new Reader(filePath);
        reader.read();
        ArrayList<Individual> individuals = reader.getIndividuals();
        System.out.println("Individuals: " + individuals.size());
        System.out.println("Males: " + reader.getCountMale());
        System.out.println("Females: " + reader.getCountFemale());
        for (int i = 0; i < 10; i++) {
            this.add(new IndividualPanel(individuals.get(i)));
        }
    }
}