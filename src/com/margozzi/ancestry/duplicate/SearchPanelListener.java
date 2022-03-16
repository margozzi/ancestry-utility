package com.margozzi.ancestry.duplicate;

import java.awt.event.ActionEvent;

public interface SearchPanelListener {
    // Search Button was clicked
    public void handleSearch();

    // Advanced button clicked
    public void handleAdvanced(ActionEvent e);

}
