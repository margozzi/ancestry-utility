package com.margozzi.ancestry;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;

import com.margozzi.ancestry.model.Individual;

public class IndividualPanel extends JPanel {

    public IndividualPanel(Individual individual) {
        JTextArea textArea = new JTextArea(individual.toString());
        textArea.setBackground(Color.yellow);
        // textArea.setSize(400, 100);
        this.add(textArea);
    }
}
