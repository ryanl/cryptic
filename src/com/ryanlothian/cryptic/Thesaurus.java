package com.ryanlothian.cryptic;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;

/** Represents a map from word -> synonyms of that word. */
public final class Thesaurus {
    
    private final ImmutableMap<String, ImmutableList<String>> words;

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

    public Thesaurus(List<? extends List<String>> synonymSets) {
        // TODO: remove spaces
        ImmutableMap.Builder<String, ImmutableList<String>> builder = ImmutableMap.builder();
                
        for (List<String> synonymSet : synonymSets) {
            // Note that this will include the word as a synonym of itself, which may be undesirable.
            ImmutableList<String> synonyms = ImmutableList.copyOf(synonymSet);
            for (String synonym : synonymSet) {
                builder.put(synonym, synonyms);
            }
        }
        words = builder.build();
    }

    /** Returns all synonyms of the given word, including the word itself. */
    public List<String> getSynonyms(String word) {
        @Nullable List<String> synonyms = words.get(word);
        if (synonyms != null) {
            return synonyms;
        } else {
            return ImmutableList.of(word);
        }
    }
}
