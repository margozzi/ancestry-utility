package com.margozzi.ancestry.duplicate;

import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import com.margozzi.ancestry.model.Individual;

public class IndividualPanel extends JPanel {
    private static Color girl = Color.PINK;
    private static Color boy = Color.CYAN;
    private static Color ignore = Color.GRAY;
    private IgnoreListener listener;
    private String id;

    public IndividualPanel(Individual individual) {
        id = individual.getId();

        JTextArea textArea = new JTextArea(individual.toString());
        textArea.setBackground(individual.getGender().equalsIgnoreCase("M") ? boy : girl);
        textArea.setFont(new Font(Font.DIALOG, Font.BOLD, 12));
        textArea.setPreferredSize(new Dimension(250, 155));
        textArea.setMinimumSize(new Dimension(250, 155));
        textArea.setMargin(new Insets(5, 10, 5, 10));
        this.add(textArea);

        JPopupMenu menu = new JPopupMenu();
        JMenuItem ignore = new JMenuItem("Add to Ignore list");
        ignore.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (listener != null) {
                    listener.handleIgnore(id);
                    textArea.setBackground(IndividualPanel.ignore);
                }
            }
        });
        menu.add(ignore);

        textArea.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) && e.getClickCount() == 1) {
                    menu.show(IndividualPanel.this, e.getX(), e.getY());
                }
            }
        });
    }

    public void setIgnoreListener(IgnoreListener listener) {
        this.listener = listener;
    }
}
