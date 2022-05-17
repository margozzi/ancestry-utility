package com.margozzi.ancestry.duplicate.ignore;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.table.AbstractTableModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import com.margozzi.ancestry.duplicate.IgnoreProvider;

public class Dialog extends JDialog {
    // ImageIcon img = new ImageIcon("/images/people_32px.gif");
    Image image;

    public Dialog(IgnoreProvider ignore, Component parent) {

        setSize(new Dimension(500, 300));
        setTitle("Ignored Individuals");
        setModal(true);
        setLocationRelativeTo(parent);
        // try {
        // // image = ImageIO.read(getClass().getResource("/images/people_32px.gif"));
        // } catch (IOException e) {
        // e.printStackTrace();
        // }
        // // setIconImage(image);

        Container contentPane = getContentPane();
        contentPane.setLayout(new GridBagLayout());

        TableModel model = new TableModel(ignore);
        JTable table = new JTable(model);
        JScrollPane scrollpane = new JScrollPane(table);

        JButton remove = new JButton("Remove");
        remove.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                for (int index : table.getSelectedRows()) {
                    // ignore.removeFromIgnore((String) table.getModel().getValueAt(index, 0));
                    model.removeRow(index);
                }
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            }
        });
        JButton removeAll = new JButton("Remove All");
        removeAll.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // ignore.removeAllFromIgnore();
                model.removeAll();
                ((AbstractTableModel) table.getModel()).fireTableDataChanged();
            }
        });

        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setVisible(false);
            }
        });

        Insets insets = new Insets(5, 10, 5, 10);

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.weightx = 1;
        c.weighty = 1;
        c.insets = insets;
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.PAGE_START;

        add(scrollpane, c);

        c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.insets = insets;
        this.add(remove, c);

        c = new GridBagConstraints();
        c.gridx = 1;
        c.gridy = 1;
        c.insets = insets;
        this.add(removeAll, c);

        c = new GridBagConstraints();
        c.gridx = 2;
        c.gridy = 1;
        c.insets = insets;
        this.add(close, c);
    }

}
