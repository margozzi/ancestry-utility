package com.margozzi.ancestry.duplicate;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SearchPanel extends JPanel {
    File selectedFile;
    private JTextField fileTextField;
    JSlider thresholdSlider;
    JButton browseButton;
    JButton advancedButton;
    JButton searchButton;

    public SearchPanel(SearchPanelListener listener) {
        super(new GridBagLayout());
        JLabel fileLabel = new JLabel("File");
        fileLabel.setHorizontalAlignment(SwingConstants.LEFT);
        fileTextField = new JTextField("Click the browse button -->", 20);
        browseButton = new JButton("Browse...");
        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleBrowse();
            }
        });

        JLabel thresholdLabel = new JLabel("Match Percent");
        thresholdLabel.setHorizontalAlignment(SwingConstants.LEFT);
        thresholdSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, 80);
        thresholdSlider.setMajorTickSpacing(50);
        thresholdSlider.setMinorTickSpacing(10);
        thresholdSlider.setPaintTicks(true);
        thresholdSlider.setPaintLabels(true);
        advancedButton = new JButton("Advanced");
        advancedButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                advancedButton.setText(advancedButton.getText().equals("Advanced") ? "Basic" : "Advanced");
                if (listener != null) {
                    listener.handleAdvanced(e);
                }
            }
        });

        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.handleSearch();
                }
            }
        });

        Insets insets = new Insets(5, 10, 5, 10);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.insets = insets;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(fileLabel, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 0;
        c.insets = insets;
        this.add(fileTextField, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 0;
        c.insets = insets;
        this.add(browseButton, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.insets = insets;
        this.add(thresholdLabel, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.insets = insets;
        this.add(thresholdSlider, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 1;
        c.insets = insets;
        this.add(advancedButton, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridwidth = 3;
        c.gridy = 2;
        c.insets = insets;
        this.add(searchButton, c);
    }

    private void handleBrowse() {
        browseButton.setEnabled((false));
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
        browseButton.setEnabled((true));
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedFile = fileChooser.getSelectedFile();
            fileTextField.setText(selectedFile.getName());
        }
    }

    public File getSelectedFile() {
        return selectedFile;
    }

    public double getThreshold() {
        return thresholdSlider.getValue() / 100.0;
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.getContentPane().add(new SearchPanel(null), BorderLayout.NORTH);
        frame.pack();
        frame.setVisible(true);
    }
}
