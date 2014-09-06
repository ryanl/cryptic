package com.ryanlothian.cryptic;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/** Tests for {@link Thesaurus}/ */
public final class ThesaurusTest {
    
    @Test
    public void getSynonymsShouldReturnSynoynms() {
        // GIVEN that 'child', 'kid' and 'infant' are synoynms;  
        ImmutableList<ImmutableList<String>> synonymSets = ImmutableList.of(
                ImmutableList.of("child", "kid", "infant"));
        
        // GIVEN a thesaurus created from those synoynms
        Thesaurus thesaurus = new Thesaurus(synonymSets);
        
        // WHEN getSynonyms is called for 'kid'
        // THEN the result are all the synonyms we gave for 'child'
        assertEquals(
                ImmutableSet.of("child", "kid", "infant"),
                thesaurus.getSynonyms("child"));
    }
    
    @Test
    public void getSynonymsShouldReturnJustTheInputForUnknownWords() {        
        // GIVEN an empty thesaurus
        Thesaurus thesaurus = new Thesaurus(ImmutableList.<List<String>>of());
        
        // WHEN getSynonyms is called for 'cat'
        // THEN the result should be just ['cat'] as we don't know any synoynms for it
        assertEquals(
                ImmutableSet.<String>of("cat"), 
                thesaurus.getSynonyms("cat"));
    }
    
    @Test
    public void getSynoynmsShouldWorkForNonTransitiveSynonyms() {
        // GIVEN that "tear" and "drop" are synonyms
        // GIVEN that "tear" and "render" are synonyms 
        ImmutableList<ImmutableList<String>> synonymSets = ImmutableList.of(
                ImmutableList.of("tear", "drop"),
                ImmutableList.of("tear", "rend"));
        Thesaurus thesaurus = new Thesaurus(synonymSets);
                
        // WHEN we get synonyms for "tear" 
        // THEN we get back "tear", "drop" and "rend"
        assertEquals(
                ImmutableSet.of("tear", "drop", "rend"),
                thesaurus.getSynonyms("tear"));
    }

}

