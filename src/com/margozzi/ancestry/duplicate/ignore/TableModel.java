package com.margozzi.ancestry.duplicate.ignore;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import com.margozzi.ancestry.duplicate.IgnoreProvider;
import com.margozzi.ancestry.model.Individual;

class TableModel extends AbstractTableModel {
    private ArrayList<Individual> ignoredList;
    private IgnoreProvider ignoreProvider;

    public TableModel(IgnoreProvider ignoreProvider) {
        this.ignoreProvider = ignoreProvider;
        ignoredList = new ArrayList<Individual>();
        for (String id : ignoreProvider.getIgnoreIds()) {
            ignoredList.add(ignoreProvider.getInidividuals().get(id));
        }
    }

    @Override
    public int getRowCount() {
        return ignoredList.size();
    }

    @Override
    public int getColumnCount() {
        return 3;
    }

    @Override
    public String getColumnName(int column) {
        switch (column) {
            case 0:
                return "Id";

            case 1:
                return "Full Name";

            case 2:
                return "Gender";
        }
        return null;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Individual ignored = ignoredList.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return ignored.getId();

            case 1:
                return ignored.getFullName();

            case 2:
                return ignored.getGender();

            default:
                return null;

        }
    }

    public void removeRow(int index) {
        ignoreProvider.removeFromIgnore(ignoredList.get(index).getId());
        ignoredList.remove(index);
    }

    public void removeAll() {
        ignoreProvider.removeAllFromIgnore();
        ignoredList.clear();
    }

}
