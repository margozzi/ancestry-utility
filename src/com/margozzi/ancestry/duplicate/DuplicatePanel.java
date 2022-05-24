package com.margozzi.ancestry.duplicate;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import com.intuit.fuzzymatcher.component.MatchService;
import com.intuit.fuzzymatcher.domain.Document;
import com.intuit.fuzzymatcher.domain.Element;
import com.intuit.fuzzymatcher.domain.ElementType;
import com.intuit.fuzzymatcher.domain.Match;
import com.margozzi.ancestry.file.IgnoreFile;
import com.margozzi.ancestry.file.Reader;
import com.margozzi.ancestry.model.Individual;

public class DuplicatePanel extends JSplitPane implements SearchPanelListener, IgnoreProvider {

    private JPanel rightPanel;
    private JScrollPane resultScrollPane;
    private JScrollPane leftScrollPane;
    private HashMap<String, Individual> individuals;
    private SearchPanel searchPanel;
    private AdvancedPanel advancedPanel;
    private JPanel resultPanel;
    private JLabel msgLabel = new JLabel("Searching...");
    private IgnoreFile ignoreFile;

    public DuplicatePanel(Properties properties) {

        advancedPanel = new AdvancedPanel(properties);
        leftScrollPane = new JScrollPane(advancedPanel);

        rightPanel = new JPanel(new GridBagLayout());
        rightPanel.setMinimumSize(new Dimension(550, 600));
        rightPanel.setPreferredSize(new Dimension(550, 600));

        searchPanel = new SearchPanel(this, properties);
        searchPanel.setIgnoreProvider(this);
        onFileChange(searchPanel.getSelectedFile());
        searchPanel.updateIgnoreButton();

        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 1;
        c.weighty = 0.01;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.PAGE_START;
        rightPanel.add(searchPanel, c);

        this.setLeftComponent(leftScrollPane);
        this.setRightComponent(rightPanel);
        this.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        this.setOneTouchExpandable(true);
    }

    private void load(File file) {

        List<Document> documentList = new ArrayList<Document>();
        // Augment Individuals with extra information.
        individuals.forEach((id, individual) -> {
            setSpouseFullName(individual);
            setMothersFullName(individual);
            setFathersFullName(individual);
            setSiblingsFirstNames(individual);
            setChildrenFirstNames(individual);

            documentList.add(
                    new Document.Builder(id)
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getGender())
                                    .setVariance("Gender")
                                    .setType(ElementType.TEXT)
                                    .setWeight(advancedPanel.getGender())
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getFirstName())
                                    .setVariance("FirstName")
                                    .setType(ElementType.NAME)
                                    .setWeight(advancedPanel.getFirstName())
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getMiddleName())
                                    .setVariance("MiddleName")
                                    .setType(ElementType.NAME)
                                    .setWeight(advancedPanel.getMiddleName())
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getLastName())
                                    .setVariance("LastName")
                                    .setType(ElementType.NAME)
                                    .setWeight(advancedPanel.getlastName())
                                    .createElement())
                            .addElement(new Element.Builder<Integer>()
                                    .setValue(individual.getBirthDateYear())
                                    .setVariance("Birth")
                                    .setType(ElementType.NUMBER)
                                    .setWeight(advancedPanel.getBirth())
                                    .setNeighborhoodRange(0.999)
                                    .createElement())
                            .addElement(new Element.Builder<Integer>()
                                    .setValue(individual.getDeathDateYear())
                                    .setVariance("Death")
                                    .setType(ElementType.NUMBER)
                                    .setWeight(advancedPanel.getDeath())
                                    .setNeighborhoodRange(0.999)
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getSpouseFullName())
                                    .setVariance("SpouseName")
                                    .setType(ElementType.NAME)
                                    .setWeight(advancedPanel.getSpouse())
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getMotherFullName())
                                    .setVariance("MotherName")
                                    .setType(ElementType.NAME)
                                    .setWeight(advancedPanel.getMother())
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getFatherFullName())
                                    .setVariance("FatherName")
                                    .setType(ElementType.NAME)
                                    .setWeight(advancedPanel.getFather())
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getSiblingsFirstNames())
                                    .setVariance("Siblings")
                                    .setType(ElementType.NAME)
                                    .setWeight(advancedPanel.getSibling())
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getChildrenFirstNames())
                                    .setVariance("Children")
                                    .setType(ElementType.NAME)
                                    .setWeight(advancedPanel.getChildren())
                                    .createElement())
                            .setThreshold(searchPanel.getThreshold())
                            .createDocument());
        });

        MatchService matchService = new MatchService();
        Map<String, List<Match<Document>>> result = matchService.applyMatchByDocId(documentList);

        final AtomicInteger yHolder = new AtomicInteger();
        final AtomicInteger numDupsHolder = new AtomicInteger();
        ArrayList<IndividualPanel> panels = new ArrayList<IndividualPanel>();
        ArrayList<GridBagConstraints> constraints = new ArrayList<GridBagConstraints>();
        List<Match<Document>> noDuplicates = removeDuplicates(result);
        noDuplicates = removeIgnores(noDuplicates);

        noDuplicates.forEach(match -> {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            int y = yHolder.getAndIncrement();
            c.gridy = y;
            IndividualPanel panel = new IndividualPanel(individuals.get(match.getData().getKey()));
            panel.setIgnoreProvider(this);
            panels.add(panel);
            constraints.add(c);
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = y++;
            panel = new IndividualPanel(individuals.get(match.getMatchedWith().getKey()));
            panel.setIgnoreProvider(this);
            panels.add(panel);
            constraints.add(c);
            numDupsHolder.incrementAndGet();

            // System.out.println(match.getData().getKey() + ": " + match.getData() +
            // "Matched: " + match
            // .getMatchedWith().getKey() + ": " + match.getMatchedWith() + " Score: "
            // + match.getScore().getResult());
        });

        System.out.println("Duplicates: " + numDupsHolder.get());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {

                if (panels.size() == 0) {
                    msgLabel.setText("No Matches Found");
                } else {
                    rightPanel.remove(msgLabel);

                    resultPanel = new JPanel(new GridBagLayout());
                    for (int i = 0; i < panels.size(); i++) {
                        resultPanel.add(panels.get(i), constraints.get(i));
                    }
                    resultScrollPane = new JScrollPane(resultPanel);

                    GridBagConstraints c = new GridBagConstraints();
                    c.gridx = 0;
                    c.gridy = 1;
                    c.weightx = 1;
                    c.weighty = 1;
                    c.fill = GridBagConstraints.BOTH;
                    rightPanel.add(resultScrollPane, c);
                }
                rightPanel.revalidate();
            }
        });
    }

    private List<Match<Document>> removeDuplicates(Map<String, List<Match<Document>>> result) {
        ArrayList<Match<Document>> data = new ArrayList<Match<Document>>();
        result.entrySet().forEach(entry -> {
            entry.getValue().forEach(match -> {
                data.add(match);
            });
        });
        for (int i = 0; i < data.size(); i++) {
            String leftKey = data.get(i).getData().getKey();
            String rightKey = data.get(i).getMatchedWith().getKey();
            for (int j = i + 1; j < data.size(); j++) {
                String leftKeyMatch = data.get(j).getData().getKey();
                String rightKeyMatch = data.get(j).getMatchedWith().getKey();
                if (leftKey.equals(rightKeyMatch) && rightKey.equals(leftKeyMatch)) {
                    data.remove(j);
                }
            }
        }
        return data;
    }

    private List<Match<Document>> removeIgnores(List<Match<Document>> data) {

        for (int i = data.size() - 1; i >= 0; i--) {
            String leftKey = data.get(i).getData().getKey();
            String rightKey = data.get(i).getMatchedWith().getKey();
            if (ignoreFile.containsId(leftKey) || ignoreFile.containsId(rightKey)) {
                data.remove(i);
            }
        }
        return data;
    }

    private void setSpouseFullName(Individual individual) {
        String spouseIds = individual.getSpouseId(); // Could have multiple
        if (spouseIds != null && spouseIds.length() > 0) {
            String[] ids = spouseIds.split(",");
            String spousesFullNames = null;
            for (String id : ids) {
                Individual spouse = this.individuals.get(id);
                if (spouse != null && spouse.getFullName() != null) {
                    spousesFullNames += spouse.getFullName() + " ";
                }
            }
            if (spousesFullNames != null) {
                individual.setSpouseFullName(spousesFullNames);
            } else {
                individual.setSpouseFullName("");
            }
        } else {
            individual.setSpouseFullName("");
        }
    }

    private void setMothersFullName(Individual individual) {
        String motherId = individual.getMotherId();
        if (motherId != null && motherId.length() > 0) {
            Individual mom = this.individuals.get(motherId);
            if (mom != null && mom.getFullName() != null) {
                individual.setMotherFullName(mom.getFullName());
            } else {
                individual.setMotherFullName("");
            }
        } else {
            individual.setMotherFullName("");
        }
    }

    private void setFathersFullName(Individual individual) {
        String fatherId = individual.getFatherId();
        if (fatherId != null && fatherId.length() > 0) {
            Individual dad = individuals.get(fatherId);
            if (dad != null && dad.getFullName() != null) {
                individual.setFatherFullName(dad.getFullName());
            } else {
                individual.setFatherFullName("");
            }
        } else {
            individual.setFatherFullName("");
        }
    }

    private void setSiblingsFirstNames(Individual individual) {
        String siblingFirstNames = "";
        String siblingIds = individual.getSiblingIds();
        if (siblingIds != null && siblingIds.length() > 0) {
            String[] tokens = siblingIds.split(", ");
            for (String siblingId : tokens) {
                Individual sibling = individuals.get(siblingId);
                if (sibling != null && sibling.getFirstName() != null) {
                    siblingFirstNames += sibling.getFirstName() + " ";
                }
            }
        }
        individual.setSiblingFirstNames(siblingFirstNames);
    }

    private void setChildrenFirstNames(Individual individual) {
        String childrenFirstNames = "";
        String childrenIds = individual.getChildrenIds();
        if (childrenIds != null && childrenIds.length() > 0) {
            String[] tokens = childrenIds.split(", ");
            for (String childId : tokens) {
                Individual child = individuals.get(childId);
                if (child != null && child.getFirstName() != null) {
                    childrenFirstNames += child.getFirstName() + " ";
                }
            }
        }
        individual.setChildrenFirstNames(childrenFirstNames);
    }

    void clearResults() {
        if (resultScrollPane != null) {
            rightPanel.remove(resultScrollPane);
            rightPanel.revalidate();
            rightPanel.repaint();
        }
    }

    @Override
    public void onSearch() {
        clearResults();
        rightPanel.remove(msgLabel);
        msgLabel.setText("Searching...");
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 1;
        c.weightx = 1;
        c.weighty = 1;
        c.fill = GridBagConstraints.BOTH;
        rightPanel.add(msgLabel, c);
        rightPanel.revalidate();
        rightPanel.repaint();

        Thread t = new Thread() {
            public void run() {
                load(searchPanel.getSelectedFile());
            }
        };
        t.start();

    }

    @Override
    public void onFileChange(File file) {

        if (file != null) {
            Reader reader = new Reader(file);
            reader.read();
            individuals = reader.getIndividuals();
            // System.out.println("Individuals: " + individuals.size());
            // System.out.println("Males: " + reader.getCountMale());
            // System.out.println("Females: " + reader.getCountFemale());

            String path = file.getParent() + File.separator;
            String baseFileName = file.getName().split("\\.")[0];
            path += baseFileName;
            path += ".ignore";
            ignoreFile = new IgnoreFile(new File(path));
            ignoreFile.read();

            clearResults();
        }
    }

    @Override
    public HashMap<String, Individual> getInidividuals() {
        return individuals;
    }

    @Override
    public ArrayList<String> getIgnoreIds() {
        if (ignoreFile != null) {
            return ignoreFile.getAll();
        } else {
            return null;
        }

    }

    @Override
    public void addToIgnore(String id) {
        ignoreFile.addId(id);
        updateIgnoreButton();
    }

    @Override
    public void removeFromIgnore(String id) {
        ignoreFile.removeId(id);
        updateIgnoreButton();
    }

    @Override
    public void removeAllFromIgnore() {
        ignoreFile.removeAll();
        updateIgnoreButton();
    }

    private void updateIgnoreButton() {
        searchPanel.updateIgnoreButton();
    }

}