package com.margozzi.ancestry.duplicate;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Properties;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;

import com.margozzi.ancestry.Utility;

public class AdvancedPanel extends JPanel {

    private Properties properties;
    private JSlider genderSlider;
    private JSlider firstNameSlider;
    private JSlider middleNameSlider;
    private JSlider lastNameSlider;
    private JSlider birthSlider;
    private JSlider deathSlider;
    private JSlider spouseSlider;
    private JSlider motherSlider;
    private JSlider fatherSlider;
    private JSlider siblingSlider;
    private JSlider childrenSlider;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        AdvancedPanel advancePanel = new AdvancedPanel(Utility.getDefaultProperties());
        JScrollPane scroller = new JScrollPane(advancePanel);
        frame.getContentPane().add(scroller);
        frame.pack();
        frame.setVisible(true);
    }

    public AdvancedPanel(Properties properties) {
        super(new GridBagLayout());
        this.properties = properties;

        JLabel titleLabel = new JLabel("Relative Importance");

        JLabel genderLabel = new JLabel("Gender");
        genderSlider = buildSlider(getWeight("genderWeight"));

        JLabel firstNameLabel = new JLabel("First Name");
        firstNameSlider = buildSlider(getWeight("firstNameWeight"));

        JLabel middleNameLabel = new JLabel("Middle Name");
        middleNameSlider = buildSlider(getWeight("middleNameWeight"));

        JLabel lastNameLabel = new JLabel("Last Name");
        lastNameSlider = buildSlider(getWeight("lastNameWeight"));

        JLabel birthLabel = new JLabel("Birth Year");
        birthSlider = buildSlider(getWeight("birthWeight"));

        JLabel deathLabel = new JLabel("Death Year");
        deathSlider = buildSlider(getWeight("deathWeight"));

        JLabel spouseLabel = new JLabel("Spouse");
        spouseSlider = buildSlider(getWeight("spouseWeight"));

        JLabel motherLabel = new JLabel("Mother");
        motherSlider = buildSlider(getWeight("motherWeight"));

        JLabel fatherLabel = new JLabel("Father");
        fatherSlider = buildSlider(getWeight("fatherWeight"));

        JLabel siblingLabel = new JLabel("Siblings");
        siblingSlider = buildSlider(getWeight("siblingWeight"));

        JLabel childrenLabel = new JLabel("Children");
        childrenSlider = buildSlider(getWeight("childrenWeight"));

        JButton saveButton = new JButton("Save as Defaults");
        saveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSaveDefaults();
            }
        });

        JButton factoryButton = new JButton("Restore Factory Defaults");
        factoryButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFactoryReset();
            }
        });

        Insets insets = new Insets(5, 5, 5, 5);

        GridBagConstraints c = buildLabelConstraints(1, 0, insets);
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(titleLabel, c);

        c = buildLabelConstraints(0, 1, insets);
        c.anchor = GridBagConstraints.PAGE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(firstNameLabel, c);
        this.add(firstNameSlider, buildConstraints(1, 1, insets));

        this.add(middleNameLabel, buildLabelConstraints(0, 2, insets));
        this.add(middleNameSlider, buildConstraints(1, 2, insets));

        this.add(lastNameLabel, buildLabelConstraints(0, 3, insets));
        this.add(lastNameSlider, buildConstraints(1, 3, insets));

        this.add(genderLabel, buildLabelConstraints(0, 4, insets));
        this.add(genderSlider, buildConstraints(1, 4, insets));

        this.add(birthLabel, buildLabelConstraints(0, 5, insets));
        this.add(birthSlider, buildConstraints(1, 5, insets));

        this.add(deathLabel, buildLabelConstraints(0, 6, insets));
        this.add(deathSlider, buildConstraints(1, 6, insets));

        this.add(spouseLabel, buildLabelConstraints(0, 7, insets));
        this.add(spouseSlider, buildConstraints(1, 7, insets));

        this.add(motherLabel, buildLabelConstraints(0, 8, insets));
        this.add(motherSlider, buildConstraints(1, 8, insets));

        this.add(fatherLabel, buildLabelConstraints(0, 9, insets));
        this.add(fatherSlider, buildConstraints(1, 9, insets));

        this.add(siblingLabel, buildLabelConstraints(0, 10, insets));
        this.add(siblingSlider, buildConstraints(1, 10, insets));

        this.add(childrenLabel, buildLabelConstraints(0, 11, insets));
        this.add(childrenSlider, buildConstraints(1, 11, insets));

        this.add(saveButton, buildConstraints(1, 12, insets));

        this.add(factoryButton, buildConstraints(1, 13, insets));
    }

    protected void handleFactoryReset() {
        Properties defaultProperties = Utility.getDefaultProperties();
        Set<String> propertyNames = defaultProperties.stringPropertyNames();
        for (String property : propertyNames) {
            properties.setProperty(property, (String) defaultProperties.get(property));
        }
        Utility.saveProperties();
        genderSlider.setValue(getWeight("genderWeight"));
        firstNameSlider.setValue(getWeight("firstNameWeight"));
        middleNameSlider.setValue(getWeight("middleNameWeight"));
        lastNameSlider.setValue(getWeight("lastNameWeight"));
        birthSlider.setValue(getWeight("birthWeight"));
        deathSlider.setValue(getWeight("deathWeight"));
        spouseSlider.setValue(getWeight("spouseWeight"));
        motherSlider.setValue(getWeight("motherWeight"));
        fatherSlider.setValue(getWeight("fatherWeight"));
        siblingSlider.setValue(getWeight("siblingWeight"));
        childrenSlider.setValue(getWeight("childrenWeight"));
    }

    protected void handleSaveDefaults() {
        properties.setProperty("genderWeight", Integer.toString(genderSlider.getValue()));
        properties.setProperty("firstNameWeight", Integer.toString(firstNameSlider.getValue()));
        properties.setProperty("middleNameWeight", Integer.toString(middleNameSlider.getValue()));
        properties.setProperty("lastNameWeight", Integer.toString(lastNameSlider.getValue()));
        properties.setProperty("birthWeight", Integer.toString(birthSlider.getValue()));
        properties.setProperty("deathWeight", Integer.toString(deathSlider.getValue()));
        properties.setProperty("spouseWeight", Integer.toString(spouseSlider.getValue()));
        properties.setProperty("motherWeight", Integer.toString(motherSlider.getValue()));
        properties.setProperty("fatherWeight", Integer.toString(fatherSlider.getValue()));
        properties.setProperty("siblingWeight", Integer.toString(siblingSlider.getValue()));
        properties.setProperty("childrenWeight", Integer.toString(childrenSlider.getValue()));
        Utility.saveProperties();
    }

    private int getWeight(String key) {
        String stringValue = this.properties.getProperty(key);
        return (Integer.parseInt(stringValue));
    }

    private JSlider buildSlider(int initialValue) {
        JSlider slider = new JSlider(JSlider.HORIZONTAL, 0, 100, initialValue);
        slider.setMajorTickSpacing(50);
        slider.setMinorTickSpacing(10);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMinimumSize(new Dimension(250, 55));
        return (slider);
    }

    private GridBagConstraints buildLabelConstraints(int x, int y, Insets insets) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.weightx = 0;
        c.fill = GridBagConstraints.NONE;
        c.insets = insets;
        return (c);
    }

    private GridBagConstraints buildConstraints(int x, int y, Insets insets) {
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = x;
        c.gridy = y;
        c.weightx = 1;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = insets;
        return (c);
    }

    public double getFirstName() {
        return firstNameSlider.getValue() / 100.0;
    }

    public double getMiddleName() {
        return middleNameSlider.getValue() / 100.0;
    }

    public double getlastName() {
        return lastNameSlider.getValue() / 100.0;
    }

    public double getGender() {
        return genderSlider.getValue() / 100.0;
    }

    public double getBirth() {
        return birthSlider.getValue() / 100.0;
    }

    public double getDeath() {
        return deathSlider.getValue() / 100.0;
    }

    public double getSpouse() {
        return spouseSlider.getValue() / 100.0;
    }

    public double getMother() {
        return motherSlider.getValue() / 100.0;
    }

    public double getFather() {
        return fatherSlider.getValue() / 100.0;
    }

    public double getSibling() {
        return siblingSlider.getValue() / 100.0;
    }

    public double getChildren() {
        return childrenSlider.getValue() / 100.0;
    }
}
