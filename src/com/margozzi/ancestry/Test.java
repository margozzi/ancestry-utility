package com.margozzi.ancestry;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.intuit.fuzzymatcher.component.MatchService;
import com.intuit.fuzzymatcher.domain.Document;
import com.intuit.fuzzymatcher.domain.Element;
import com.intuit.fuzzymatcher.domain.Match;
import com.intuit.fuzzymatcher.domain.ElementType;

public class Test {

    static String[][] input = {
            { "1", "Steven Wilson", "45th Avenue 5th st." },
            { "2", "John Doe", "546 freeman ave" },
            { "3", "Stephen Wilkson", "45th Ave 5th Street" }
    };

    public static void main(String[] args) {
        List<Document> documentList = Arrays.asList(input).stream().map(contact -> {
            return new Document.Builder(contact[0])
                    .addElement(new Element.Builder<String>().setValue(contact[1]).setType(ElementType.NAME)
                            .createElement())
                    .addElement(new Element.Builder<String>().setValue(contact[2]).setType(ElementType.ADDRESS)
                            .createElement())
                    .createDocument();
        }).collect(Collectors.toList());

        MatchService matchService = new MatchService();
        Map<String, List<Match<Document>>> result = matchService.applyMatchByDocId(documentList);

        result.entrySet().forEach(entry -> {
            entry.getValue().forEach(match -> {
                System.out.println("Data: " + match.getData() + " Matched With: " + match.getMatchedWith() + " Score: "
                        + match.getScore().getResult());
            });
        });

    }
}