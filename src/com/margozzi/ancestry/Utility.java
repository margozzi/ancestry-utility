package com.margozzi.ancestry;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.margozzi.ancestry.duplicate.DuplicatePanel;

public class Utility {
    private static JFrame frame;

    private static void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);

        // Create and set up the window.
        frame = new JFrame("Ancestry Utility");
        frame.setPreferredSize(new Dimension(700, 600));
        frame.setMinimumSize(new Dimension(700, 550));
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
                frame.getContentPane().removeAll();
                DuplicatePanel duplicatePanel = new DuplicatePanel();
                frame.getContentPane().add(duplicatePanel, BorderLayout.NORTH);
                frame.revalidate();
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