package com.margozzi.ancestry.duplicate;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

public class AdvancedPanel extends JPanel {

    private JSlider genderSlider;
    private JSlider firstNameSlider;
    private JSlider middleNameSlider;
    private JSlider lastNameSlider;
    private JSlider birthSlider;
    private JSlider deathSlider;
    private JSlider motherSlider;
    private JSlider fatherSlider;
    private JSlider siblingSlider;
    private JSlider childrenSlider;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new AdvancedPanel());
        frame.pack();
        frame.setVisible(true);
    }

    public AdvancedPanel() {
        super(new GridBagLayout());

        JLabel genderLabel = new JLabel("Gender");
        genderSlider = buildSlider(30);

        JLabel firstNameLabel = new JLabel("First Name");
        firstNameSlider = buildSlider(50);

        JLabel middleNameLabel = new JLabel("Middle Name");
        middleNameSlider = buildSlider(20);

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameSlider = buildSlider(50);

        JLabel birthLabel = new JLabel("Birth Year");
        birthSlider = buildSlider(30);

        JLabel deathLabel = new JLabel("Death Year");
        deathSlider = buildSlider(30);

        JLabel motherLabel = new JLabel("Mother");
        motherSlider = buildSlider(30);

        JLabel fatherLabel = new JLabel("Father");
        fatherSlider = buildSlider(30);

        JLabel siblingLabel = new JLabel("Siblings");
        siblingSlider = buildSlider(10);

        JLabel childrenLabel = new JLabel("Children");
        childrenSlider = buildSlider(10);

        JButton saveButton = new JButton("Save as Defaults");
        JButton factoryButton = new JButton("Restore Factory Settings");

        Insets insetsLeftColumn = new Insets(5, 20, 15, 10);
        Insets insetsRightColumn = new Insets(5, 10, 15, 10);

        GridBagConstraints c = buildConstraints(0, 0, insetsLeftColumn);
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(firstNameLabel, c);
        this.add(firstNameSlider, buildConstraints(1, 0, insetsRightColumn));

        this.add(lastNameLabel, buildConstraints(2, 0, insetsLeftColumn));
        this.add(lastNameSlider, buildConstraints(3, 0, insetsRightColumn));

        this.add(genderLabel, buildConstraints(0, 1, insetsLeftColumn));
        this.add(genderSlider, buildConstraints(1, 1, insetsRightColumn));

        this.add(middleNameLabel, buildConstraints(2, 1, insetsLeftColumn));
        this.add(middleNameSlider, buildConstraints(3, 1, insetsRightColumn));

        this.add(birthLabel, buildConstraints(0, 2, insetsLeftColumn));
        this.add(birthSlider, buildConstraints(1, 2, insetsRightColumn));

        this.add(deathLabel, buildConstraints(2, 2, insetsLeftColumn));
        this.add(deathSlider, buildConstraints(3, 2, insetsRightColumn));

        this.add(motherLabel, buildConstraints(0, 3, insetsLeftColumn));
        this.add(motherSlider, buildConstraints(1, 3, insetsRightColumn));

        this.add(fatherLabel, buildConstraints(2, 3, insetsLeftColumn));
        this.add(fatherSlider, buildConstraints(3, 3, insetsRightColumn));

        this.add(siblingLabel, buildConstraints(0, 4, insetsLeftColumn));
        this.add(siblingSlider, buildConstraints(1, 4, insetsRightColumn));

        this.add(childrenLabel, buildConstraints(2, 4, insetsLeftColumn));
        this.add(childrenSlider, buildConstraints(3, 4, insetsRightColumn));

        this.add(saveButton, buildConstraints(1, 5, insetsLeftColumn));
        this.add(factoryButton, buildConstraints(3, 5, insetsRightColumn));
    }

    private JSlider buildSlider(int initialValue) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, initialValue);
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        return (slider);
    }

    private GridBagConstraints buildConstraints(int x, int y, Insets insets) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.insets = insets;
        return (c);
    }

    public double getFirstName() {
        return firstNameSlider.getValue();
    }

    public double getMiddleName() {
        return middleNameSlider.getValue();
    }

    public double getlastName() {
        return lastNameSlider.getValue();
    }

    public double getGender() {
        return genderSlider.getValue();
    }

    public double getBirth() {
        return birthSlider.getValue();
    }

    public double getDeath() {
        return deathSlider.getValue();
    }

    public double getMother() {
        return motherSlider.getValue();
    }

    public double getFather() {
        return fatherSlider.getValue();
    }

    public double getSibling() {
        return siblingSlider.getValue();
    }

    public double getChildren() {
        return childrenSlider.getValue();
    }
}
