package com.margozzi.ancestry;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;

import com.margozzi.ancestry.model.Individual;

public class IndividualPanel extends JPanel {
    private static Color girl = Color.PINK;
    private static Color boy = Color.CYAN;

    public IndividualPanel(Individual individual) {
        JTextArea textArea = new JTextArea(individual.toString());
        textArea.setBackground(individual.getGender().equalsIgnoreCase("M") ? boy : girl);
        textArea.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        // textArea.setMinimumSize(new Dimension(125, 65));
        textArea.setPreferredSize(new Dimension(250, 105));
        textArea.setMargin(new Insets(5, 10, 5, 10));
        this.add(textArea);
    }
}
