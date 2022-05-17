package com.margozzi.ancestry.file;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class IgnoreFile {
    private File file;
    private HashMap<String, String> ids;

    public IgnoreFile(File file) {
        this.file = file;
    }

    public void read() {
        ids = new HashMap<String, String>();
        try {
            Scanner scanner = new Scanner(file);
            while (scanner.hasNextLine()) {
                String id = scanner.nextLine();
                ids.put(id, id);
            }
            scanner.close();
        } catch (

        FileNotFoundException e) {
            System.out.println("No ignore file found");
        }
    }

    public boolean containsId(String id) {
        return ids.get(id) != null;
    }

    public void addId(String id) {
        ids.put(id, id);
        write();
    }

    public void removeId(String id) {
        ids.remove(id);
        write();
    }

    public void write() {
        file.delete();
        try {
            file.createNewFile();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
        try (PrintWriter out = new PrintWriter(file)) {
            for (String id : ids.values()) {
                out.println(id);
            }
            out.flush();
        } catch (FileNotFoundException fnfe) {
            fnfe.printStackTrace();
        }
    }

    public ArrayList<String> getAll() {
        return new ArrayList<String>(ids.values());
    }

    public void removeAll() {
        ids.clear();
        write();
    }
}