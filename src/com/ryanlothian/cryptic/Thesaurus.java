package com.ryanlothian.cryptic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import javax.annotation.Nullable;
import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.ImmutableSet;

/** Represents a map from word -> synonyms of that word. */
@Immutable
public final class Thesaurus {
    
    private final ImmutableMultimap<String, ImmutableList<String>> words;

    /** 
     * Creates a thesaurus from an input stream.
     * 
     * <p>The input stream should contain lines of colon-separated synoynms.
     */
    public static Thesaurus loadFromInputStream(InputStream inputStream) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));	
        try {
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
        } finally {
            reader.close();
        }
    }

    public Thesaurus(List<? extends List<String>> synonymSets) {
        ImmutableMultimap.Builder<String, ImmutableList<String>> builder = 
                ImmutableMultimap.builder();
                
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
    public ImmutableSet<String> getSynonyms(String word) {
        ImmutableCollection<ImmutableList<String>> synonyms = words.get(word);
        ImmutableSet.Builder<String> union = ImmutableSet.builder();
        
        // Every word is a synonym of itself.
        union.add(word);
        
        for (ImmutableList<String> s : synonyms) {
            union.addAll(s);
        }
        return union.build();
    }
}
