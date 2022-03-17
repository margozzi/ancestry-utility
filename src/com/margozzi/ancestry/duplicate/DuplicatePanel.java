package com.margozzi.ancestry.duplicate;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.intuit.fuzzymatcher.component.MatchService;
import com.intuit.fuzzymatcher.domain.Document;
import com.intuit.fuzzymatcher.domain.Element;
import com.intuit.fuzzymatcher.domain.ElementType;
import com.intuit.fuzzymatcher.domain.Match;
import com.margozzi.ancestry.file.Reader;
import com.margozzi.ancestry.model.Individual;

public class DuplicatePanel extends JPanel implements SearchPanelListener {

    private final JPanel panel = this;
    private HashMap<String, Individual> individuals;
    private SearchPanel searchPanel;
    private AdvancedPanel advancedPanel;
    private boolean advancedVisible = false;
    private JScrollPane scrollPane;
    private Properties properties;

    public DuplicatePanel(Properties properties) {
        this.properties = properties;
        searchPanel = new SearchPanel(this, properties);
        advancedPanel = new AdvancedPanel(properties);
        this.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.anchor = GridBagConstraints.PAGE_START;
        this.add(searchPanel, c);
    }

    private void load(File file) {
        Reader reader = new Reader(file);
        reader.read();

        individuals = reader.getIndividuals();
        System.out.println("Individuals: " + individuals.size());
        System.out.println("Males: " + reader.getCountMale());
        System.out.println("Females: " + reader.getCountFemale());

        List<Document> documentList = new ArrayList<Document>();
        // Augment Individuals with extra information.
        individuals.forEach((id, individual) -> {
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
                                    .setWeight(getWeight("genderWeight"))
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getFirstName())
                                    .setVariance("FirstName")
                                    .setType(ElementType.NAME)
                                    .setWeight(getWeight("firstNameWeight"))
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getMiddleName())
                                    .setVariance("MiddleName")
                                    .setType(ElementType.NAME)
                                    .setWeight(getWeight("middleNameWeight"))
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getLastName())
                                    .setVariance("LastName")
                                    .setType(ElementType.NAME)
                                    .setWeight(getWeight("lastNameWeight"))
                                    .createElement())
                            .addElement(new Element.Builder<Integer>()
                                    .setValue(individual.getBirthDateYear())
                                    .setVariance("Birth")
                                    .setType(ElementType.NUMBER)
                                    .setWeight(getWeight("birthWeight"))
                                    .setNeighborhoodRange(0.999)
                                    .createElement())
                            .addElement(new Element.Builder<Integer>()
                                    .setValue(individual.getDeathDateYear())
                                    .setVariance("Death")
                                    .setType(ElementType.NUMBER)
                                    .setWeight(getWeight("deathWeight"))
                                    .setNeighborhoodRange(0.999)
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getMotherFullName())
                                    .setVariance("MotherName")
                                    .setType(ElementType.NAME)
                                    .setWeight(getWeight("motherWeight"))
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getFatherFullName())
                                    .setVariance("FatherName")
                                    .setType(ElementType.NAME)
                                    .setWeight(getWeight("fatherWeight"))
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getSiblingsFirstNames())
                                    .setVariance("Siblings")
                                    .setType(ElementType.NAME)
                                    .setWeight(getWeight("siblingWeight"))
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getChildrenFirstNames())
                                    .setVariance("Children")
                                    .setType(ElementType.NAME)
                                    .setWeight(getWeight("childrenWeight"))
                                    .createElement())
                            .setThreshold(getWeight("threshold"))
                            .createDocument());
        });

        MatchService matchService = new MatchService();
        Map<String, List<Match<Document>>> result = matchService.applyMatchByDocId(documentList);

        final AtomicInteger yHolder = new AtomicInteger();
        final AtomicInteger numDupsHolder = new AtomicInteger();
        ArrayList<IndividualPanel> panels = new ArrayList<IndividualPanel>();
        ArrayList<GridBagConstraints> constraints = new ArrayList<GridBagConstraints>();
        List<Match<Document>> noDuplicates = removeDuplicates(result);
        noDuplicates.forEach(match -> {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            int y = yHolder.getAndIncrement();
            c.gridy = y;
            panels.add(new IndividualPanel(individuals.get(match.getData().getKey())));
            constraints.add(c);
            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = y++;
            panels.add(new IndividualPanel(individuals.get(match.getMatchedWith().getKey())));
            constraints.add(c);
            numDupsHolder.incrementAndGet();

            System.out.println(match.getData().getKey() + ": " + match.getData() + "Matched: " + match
                    .getMatchedWith().getKey() + ": " + match.getMatchedWith() + " Score: "
                    + match.getScore().getResult());
        });

        System.out.println("Duplicates: " + numDupsHolder.get());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JPanel resultPanel = new JPanel(new GridBagLayout());
                for (int i = 0; i < panels.size(); i++) {
                    resultPanel.add(panels.get(i), constraints.get(i));
                }
                if (scrollPane != null) {
                    panel.remove(scrollPane);
                }
                scrollPane = new JScrollPane(resultPanel);
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.gridy = 2;
                panel.add(scrollPane, c);
                panel.revalidate();
            }
        });
    }

    private double getWeight(String key) {
        String stringValue = properties.getProperty(key);
        return (Integer.parseInt(stringValue) / 100.0);
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

    @Override
    public void handleSearch() {
        Thread t = new Thread() {
            public void run() {
                load(searchPanel.getSelectedFile());
            }
        };
        t.start();

    }

    @Override
    public void handleAdvanced(ActionEvent e) {
        if (advancedVisible) {
            this.remove(advancedPanel);
            advancedVisible = false;
        } else {
            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            this.add(advancedPanel, c);
            advancedVisible = true;
        }
        this.revalidate();
    }
}