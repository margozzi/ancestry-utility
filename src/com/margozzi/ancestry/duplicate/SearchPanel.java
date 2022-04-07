package com.margozzi.ancestry.duplicate;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import com.margozzi.ancestry.Utility;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Properties;

public class SearchPanel extends JPanel {
    Properties properties;
    File selectedFile;
    private JTextField fileTextField;
    JSlider thresholdSlider;
    JButton browseButton;
    JButton searchButton;

    public SearchPanel(SearchPanelListener listener, Properties properties) {
        super(new GridBagLayout());
        this.properties = properties;
        JLabel fileLabel = new JLabel("File");
        fileLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fileTextField = new JTextField("Click the browse button -->", 25);
        fileTextField.setEditable(false);
        browseButton = new JButton("Browse...");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBrowse();
            }
        });

        JLabel thresholdLabel = new JLabel("Match %");
        thresholdLabel.setHorizontalAlignment(SwingConstants.LEFT);
        thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 80);
        thresholdSlider.setMajorTickSpacing(50);
        thresholdSlider.setMinorTickSpacing(10);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);

        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.handleSearch();
                }
            }
        });
        searchButton.setEnabled(false);

        Insets insets = new Insets(5, 10, 5, 10);
        Insets bottomInsets = new Insets(5, 10, 15, 10);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 4;
        c.insets = insets;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(searchButton, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.insets = insets;
        this.add(fileLabel, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 1;
        c.weightx = 1;
        c.insets = insets;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(fileTextField, c);

        c = new GridBagConstraints();
        c.gridx = 3;
        c.gridy = 1;
        c.insets = insets;
        this.add(browseButton, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 2;
        c.insets = bottomInsets;
        this.add(thresholdLabel, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 2;
        c.weightx = 1;
        c.insets = bottomInsets;
        c.fill = GridBagConstraints.HORIZONTAL;
        this.add(thresholdSlider, c);
    }

    private void handleBrowse() {
        browseButton.setEnabled((false));
        JFileChooser fileChooser = new JFileChooser();
        String dir = this.properties.getProperty("lastFileDirectory");
        if (dir.equals("user.home")) {
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        } else {
            fileChooser.setCurrentDirectory(new File(dir));
        }
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fileTextField.setText(selectedFile.getName());
            properties.setProperty("lastFileDirectory", selectedFile.getPath());
            searchButton.setEnabled(true);
        }
        browseButton.setEnabled((true));
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public double getThreshold() {
        return thresholdSlider.getValue() / 100.0;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new SearchPanel(null, Utility.getDefaultProperties()), BorderLayout.CENTER);
        frame.pack();
        frame.setVisible(true);
    }
}
