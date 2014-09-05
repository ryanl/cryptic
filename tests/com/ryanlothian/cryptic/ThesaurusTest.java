package com.ryanlothian.cryptic;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.ImmutableList;

/** Tests for {@link Thesaurus}/ */
public final class ThesaurusTest {
    
    @Test
    public void getSynonymsShouldReturnSynoynms() {
        // GIVEN that 'child', 'kid' and 'infant' are synoynms; 
        ImmutableList<String> childSynonyms = ImmutableList.of("child", "kid", "infant");
        ImmutableList<ImmutableList<String>> synonymSets = ImmutableList.of(childSynonyms);
        
        // GIVEN a thesaurus created from those synoynms
        Thesaurus thesaurus = new Thesaurus(synonymSets);
        
        // WHEN getSynonyms is called for 'kid'
        // THEN the result are all the synonyms we gave for 'child'
        assertEquals(childSynonyms, thesaurus.getSynonyms("child"));
    }
    
    @Test
    public void getSynonymsShouldReturnJustTheInputForUnknownWords() {        
        // GIVEN an empty thesaurus
        Thesaurus thesaurus = new Thesaurus(ImmutableList.<List<String>>of());
        
        // WHEN getSynonyms is called for 'cat'
        // THEN the result should be just ['cat'] as we don't know any synoynms for it
        assertEquals(ImmutableList.<String>of("cat"), thesaurus.getSynonyms("cat"));
    }

}

