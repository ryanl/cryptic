package com.ryanlothian.cryptic;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import com.google.common.collect.Lists;

public class GrammarTest {

    private static final Grammar GRAMMAR = grammarOf(
            "clear = manifest",
            "document = manifest",
            "club = c",
            "clear = transparent");
    
    @Test
    public void parseSingleLiteral() {
        assertEquals(
                "manifest", 
                GRAMMAR.findAllSolutions("clear"));
    }
    
    private static Grammar grammarOf(String... strings) {
        List<Rule> rules = Lists.newArrayList();
        for (String s : strings) {
            rules.add(Rule.fromString(s));
        }
        return new Grammar(rules, Thesaurus.of());
    }

}
