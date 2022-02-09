package com.margozzi.ancestry;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

public class Utility {
    private static JFrame frame;

    private static void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        frame = new JFrame("Ancestry Utility");
        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(buildMenu());

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private static JMenuBar buildMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Utilities");
        menu.setMnemonic(KeyEvent.VK_U);
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Find Duplicates", KeyEvent.VK_F);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
                int result = fileChooser.showOpenDialog(frame);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
                    DuplicatePanel duplicatePanel = new DuplicatePanel(selectedFile.getAbsolutePath());
                    frame.getContentPane().add(duplicatePanel);
                }
            }
        });
        menu.add(menuItem);
        return menuBar;
    }

    public static void main(String[] args) {
        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
    }
}