package com.margozzi.ancestry;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import com.margozzi.ancestry.duplicate.DuplicatePanel;

public class Utility {
    private JFrame frame;

    private static Properties defaultProps;
    private static Properties properties;

    private void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        int width = Integer.parseInt(properties.getProperty("frameWidth"));
        int height = Integer.parseInt(properties.getProperty("frameHeight"));

        // Create and set up the window.
        frame = new JFrame("Ancestry Utility");
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(700, 550));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(buildMenu());
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                saveProperties();
            }
        });

        // Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    private JMenuBar buildMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Utilities");
        menu.setMnemonic(KeyEvent.VK_U);
        menuBar.add(menu);

        JMenuItem menuItem = new JMenuItem("Find Duplicates", KeyEvent.VK_F);
        menuItem.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                frame.getContentPane().removeAll();
                DuplicatePanel duplicatePanel = new DuplicatePanel(properties);
                frame.getContentPane().add(duplicatePanel, BorderLayout.NORTH);
                frame.revalidate();
            }
        });
        menu.add(menuItem);
        return menuBar;
    }

    private void loadApplicationProperties() {
        FileInputStream dfis = null;
        FileInputStream ufis = null;
        defaultProps = getDefaultProperties();
        try {
            properties = new Properties(defaultProps);

            File homeDir = new File(System.getProperty("user.home"));
            File userPropertiesFile = new File(homeDir, "utility.properties");
            if (userPropertiesFile.exists()) {
                ufis = new FileInputStream(userPropertiesFile);
                properties.load(ufis);
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            try {
                if (dfis != null) {
                    dfis.close();
                }
                if (ufis != null) {
                    ufis.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Properties getDefaultProperties() {
        FileInputStream fis = null;
        Properties props = new Properties();
        try {
            fis = new FileInputStream("resource/application.properties");
            props.load(fis);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return props;
    }

    public static void saveProperties() {
        File homeDir = new File(System.getProperty("user.home"));
        File userPropertiesFile = new File(homeDir, "utility.properties");
        if (userPropertiesFile.exists()) {
            userPropertiesFile.delete();
        }
        try {
            FileOutputStream fos = new FileOutputStream(userPropertiesFile);
            properties.store(fos, "System generated, do not edit");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Utility utility = new Utility();
        utility.loadApplicationProperties();

        // Schedule a job for the event-dispatching thread:
        // creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                utility.createAndShowGUI();
            }
        });
    }
}