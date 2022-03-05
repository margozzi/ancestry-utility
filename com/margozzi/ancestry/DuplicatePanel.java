package com.margozzi.ancestry;

import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.util.ArrayList;

import javax.swing.JPanel;

import com.margozzi.ancestry.file.Reader;
import com.margozzi.ancestry.model.Individual;

public class DuplicatePanel extends JPanel {

    private final JPanel panel = this;
    private String filePath;

    public DuplicatePanel(String filePath) {
        super(new GridBagLayout());
        this.filePath = filePath;
        Thread t = new Thread() {
            public void run() {
                load();
            }
        };
        t.start();
    }

    private void load() {
        Reader reader = new Reader(filePath);
        reader.read();
        IndividualComparable checker = new IndividualComparable();

        ArrayList<Individual> individuals = reader.getIndividuals();
        System.out.println("File: " + filePath);
        System.out.println("Individuals: " + individuals.size());
        System.out.println("Males: " + reader.getCountMale());
        System.out.println("Females: " + reader.getCountFemale());

        int y = 0;
        int numDups = 0;
        GridBagConstraints c;
        ArrayList<IndividualPanel> panels = new ArrayList<IndividualPanel>();
        ArrayList<GridBagConstraints> constraints = new ArrayList<GridBagConstraints>();
        for (int i = 0; i < individuals.size(); i++) {
            for (int j = i + 1; j < individuals.size(); j++) {
                if (checker.compare(individuals.get(i), individuals.get(j))) {
                    c = new GridBagConstraints();
                    c.gridx = 0;
                    c.weightx = 1;
                    c.gridy = y;
                    panels.add(new IndividualPanel(individuals.get(i)));
                    constraints.add(c);
                    c = new GridBagConstraints();
                    c.gridx = 1;
                    c.weightx = 1;
                    c.gridy = y++;
                    panels.add(new IndividualPanel(individuals.get(j)));
                    constraints.add(c);
                    numDups++;
                }
            }
        }
        System.out.println("Duplicates: " + numDups);
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                for (int i = 0; i < panels.size(); i++) {
                    panel.add(panels.get(i), constraints.get(i));
                }
            }
        });
    }
}