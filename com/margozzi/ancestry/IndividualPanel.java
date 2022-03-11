package com.margozzi.ancestry;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;

import com.margozzi.ancestry.model.Individual;

public class IndividualPanel extends JPanel {

    public IndividualPanel(Individual individual) {
        JTextArea textArea = new JTextArea(individual.toString());
        textArea.setBackground(Color.LIGHT_GRAY);
        textArea.setFont(new Font(Font.DIALOG, Font.BOLD, 14));
        textArea.setMinimumSize(new Dimension(300, 200));
        this.add(textArea);
    }
}
