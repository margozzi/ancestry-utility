package com.margozzi.ancestry.duplicate;

import java.io.File;

public interface SearchPanelListener {

    public void onSearch();

    public void onFileChange(File newFile);
}
