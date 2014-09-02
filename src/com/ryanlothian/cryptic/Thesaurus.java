package com.ryanlothian.cryptic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class Thesaurus {
    private final Map<String, List<String>> words = new HashMap<>();

    public static Thesaurus loadFromFile(File file) throws IOException {
        try (InputStream inputStream = new FileInputStream(file);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {	
            
            List<List<String>> synonymSets = new ArrayList<>();
            for (;;) {
                String line = reader.readLine();
                if (line == null) {
                    break;
                }
                List<String> synonyms = Arrays.asList(line.split(","));
                synonymSets.add(synonyms);
            }
            return new Thesaurus(synonymSets);
        }
    }

    public Thesaurus(List<List<String>> synonymSets) {
        // TODO: remove spaces
        Map<String, List<String>> words = new HashMap<>();
        for (List<String> synonymSet : synonymSets) {
            for (String synonym : synonymSet) {
                if (!words.containsKey(synonym)) {
                    words.put(synonym, new ArrayList<String>());
                }
                for (String synonymB : synonymSet) {
                    words.get(synonym).add(synonymB);
                }
            }
        }
        // TODO: Use unmodifiable lists.
    }

    public List<String> getSynonyms(String word) {
        return words.get(word);
    }
}
