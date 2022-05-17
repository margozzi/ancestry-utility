package com.margozzi.ancestry.duplicate;

import java.util.ArrayList;
import java.util.HashMap;

import com.margozzi.ancestry.model.Individual;

public interface IgnoreProvider {
    public HashMap<String, Individual> getInidividuals();

    public ArrayList<String> getIgnoreIds();

    public void addToIgnore(String id);

    public void removeFromIgnore(String id);

    public void removeAllFromIgnore();

}
