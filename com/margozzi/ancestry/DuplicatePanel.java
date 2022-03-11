package com.margozzi.ancestry;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

public class DuplicatePanel extends JPanel {

    private final JPanel panel = this;
    private String filePath;

    public DuplicatePanel(String filePath) {
        this.setLayout(new BorderLayout());
        this.filePath = filePath;
        Thread t = new Thread() {
            public void run() {
                load();
            }
        };
        t.start();
    }

    private void load() {
        Reader reader = new Reader(filePath);
        reader.read();

        HashMap<String, Individual> individuals = reader.getIndividuals();
        System.out.println("File: " + filePath);
        System.out.println("Individuals: " + individuals.size());
        System.out.println("Males: " + reader.getCountMale());
        System.out.println("Females: " + reader.getCountFemale());

        List<Document> documentList = new ArrayList<Document>();
        individuals.forEach((id, individual) -> {
            String motherId = individual.getMotherId();
            if (motherId != null && motherId.length() > 0) {
                Individual mom = individuals.get(motherId);
                if (mom != null && mom.getFullName() != null) {
                    individual.setMotherFullName(mom.getFullName());
                } else {
                    individual.setMotherFullName("");
                    System.out.println("Mother not foud: " + motherId);
                }
            } else {
                individual.setMotherFullName("");
                System.out.println("No mother id for: " + id);
            }

            String fatherId = individual.getFatherId();
            if (fatherId != null && fatherId.length() > 0) {
                Individual dad = individuals.get(fatherId);
                if (dad != null && dad.getFullName() != null) {
                    individual.setFatherFullName(dad.getFullName());
                } else {
                    individual.setFatherFullName("");
                    System.out.println("Father not foud: " + fatherId);
                }
            } else {
                individual.setFatherFullName("");
                System.out.println("No father id for: " + id);
            }

            documentList.add(
                    new Document.Builder(id)
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getGender())
                                    .setVariance("Gender")
                                    .setType(ElementType.TEXT)
                                    .setWeight(0.3)
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getFirstName())
                                    .setVariance("FirstName")
                                    .setType(ElementType.NAME)
                                    .setWeight(0.5)
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getMiddleName())
                                    .setVariance("MiddleName")
                                    .setType(ElementType.NAME)
                                    .setWeight(0.3)
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getLastName())
                                    .setVariance("LastName")
                                    .setType(ElementType.NAME)
                                    .setWeight(0.5)
                                    .createElement())
                            .addElement(new Element.Builder<Integer>()
                                    .setValue(individual.getBirthDateYear())
                                    .setVariance("Birth")
                                    .setType(ElementType.NUMBER)
                                    .setWeight(0.3)
                                    .setNeighborhoodRange(0.999)
                                    .createElement())
                            .addElement(new Element.Builder<Integer>()
                                    .setValue(individual.getDeathDateYear())
                                    .setVariance("Death")
                                    .setType(ElementType.NUMBER)
                                    .setWeight(0.3)
                                    .setNeighborhoodRange(0.999)
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getMotherFullName())
                                    .setVariance("MotherName")
                                    .setType(ElementType.NAME)
                                    .setWeight(0.3)
                                    .createElement())
                            .addElement(new Element.Builder<String>()
                                    .setValue(individual.getFatherFullName())
                                    .setVariance("FatherName")
                                    .setType(ElementType.NAME)
                                    .setWeight(0.3)
                                    .createElement())
                            .setThreshold(0.9)
                            .createDocument());
        });

        MatchService matchService = new MatchService();
        Map<String, List<Match<Document>>> result = matchService.applyMatchByDocId(documentList);

        final AtomicInteger yHolder = new AtomicInteger();
        final AtomicInteger numDupsHolder = new AtomicInteger();
        ArrayList<IndividualPanel> panels = new ArrayList<IndividualPanel>();
        ArrayList<GridBagConstraints> constraints = new ArrayList<GridBagConstraints>();
        // Object[] noDuplicates = removeDuplicates(result);
        result.entrySet().forEach(entry -> {
            entry.getValue().forEach(match -> {
                GridBagConstraints c = new GridBagConstraints();
                c.gridx = 0;
                c.weightx = 0;
                int y = yHolder.getAndIncrement();
                c.gridy = y;
                panels.add(new IndividualPanel(individuals.get(match.getData().getKey())));
                constraints.add(c);
                c = new GridBagConstraints();
                c.gridx = 1;
                c.weightx = 0;
                c.gridy = y++;
                panels.add(new IndividualPanel(individuals.get(match.getMatchedWith().getKey())));
                constraints.add(c);
                numDupsHolder.incrementAndGet();

                System.out.println(match.getData().getKey() + ": " + match.getData() + " Matched: " + match
                        .getMatchedWith().getKey() + ": " + match.getMatchedWith() + " Score: "
                        + match.getScore().getResult());

            });
        });

        System.out.println("Duplicates: " + numDupsHolder.get());
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JScrollPane scrollPane = new JScrollPane();
                JPanel resultPanel = new JPanel(new GridBagLayout());
                resultPanel.setPreferredSize(new Dimension(300, 400));
                for (int i = 0; i < panels.size(); i++) {
                    resultPanel.add(panels.get(i), constraints.get(i));
                }
                panel.add(scrollPane, BorderLayout.CENTER);
            }
        });
    }

    private Object[] removeDuplicates(Map<String, List<Match<Document>>> result) {
        @SuppressWarnings("unchecked")
        Map<String, List<Match<Document>>>[] data = (Map<String, List<Match<Document>>>[]) (result.entrySet()
                .toArray());
        for (int i = 0; i < data.length; i++) {
            for (int j = i + 1; j < data.length; j++) {
                Map<String, List<Match<Document>>> left = data[i];
                Map<String, List<Match<Document>>> right = data[j];
            }
        }

        return data;
    }
}