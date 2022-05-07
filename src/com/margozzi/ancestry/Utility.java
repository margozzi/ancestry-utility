package com.margozzi.ancestry;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
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
import javax.swing.JScrollPane;

import com.margozzi.ancestry.duplicate.DuplicatePanel;

public class Utility {
    private static JFrame frame;
    private JScrollPane scrollPane;

    private static Properties defaultProps;
    private static Properties properties;

    private void createAndShowGUI() {
        // Make sure we have nice window decorations.
        JFrame.setDefaultLookAndFeelDecorated(true);
        int width = Integer.parseInt(properties.getProperty("frameWidth"));
        int height = Integer.parseInt(properties.getProperty("frameHeight"));

        int xPosition = Integer.parseInt(properties.getProperty("xPosition"));
        int yPosition = Integer.parseInt(properties.getProperty("yPosition"));

        // Create and set up the window.
        frame = new JFrame("Ancestry Utility");
        frame.getContentPane().setLayout(new GridBagLayout());
        frame.setPreferredSize(new Dimension(width, height));
        frame.setLocation(xPosition, yPosition);
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
                scrollPane = new JScrollPane(duplicatePanel);
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 0;
                c.weightx = 1;
                c.weighty = 1;
                c.fill = GridBagConstraints.BOTH;
                c.anchor = GridBagConstraints.PAGE_START;
                frame.getContentPane().add(scrollPane, c);
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
        properties.setProperty("frameWidth", Integer.toString(frame.getWidth()));
        properties.setProperty("frameHeight", Integer.toString(frame.getHeight()));
        Point location = frame.getLocation();
        properties.setProperty("xPosition", Integer.toString((int) location.getX()));
        properties.setProperty("yPosition", Integer.toString((int) location.getY()));
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
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                utility.createAndShowGUI();
            }
        });
    }
}