package com.ryanlothian.cryptic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

public class GrammarTest {

    private static final Grammar GRAMMAR_A = grammarOf(
            "clear = manifest",
            "document = manifest",
            "club = c",
            "clear = transparent");
    
    private static final Grammar GRAMMAR_B = grammarOf(
            "X after Y = Y X");
    
    @Test
    public void parseSingleLiteral() {
        assertEquals(
                StringSet.of("clear", "manifest", "transparent"), 
                GRAMMAR_A.findAllSolutions(ImmutableList.of("clear")));
    }

    @Test
    public void parseUsingRuleWithTokens() {
        Set<String> solutions = GRAMMAR_B
                .findAllSolutions(ImmutableList.of("dog after cat after mouse"))
                .toSet();
        assertTrue(solutions.contains("mousecatdog"));
    }
    
    private static Grammar grammarOf(String... strings) {
        List<Rule> rules = Lists.newArrayList();
        for (String s : strings) {
            rules.add(Rule.fromString(s));
        }
        return new Grammar(rules, Thesaurus.of());
    }

}
