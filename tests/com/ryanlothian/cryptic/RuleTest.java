package com.ryanlothian.cryptic;

import static com.ryanlothian.cryptic.Token.function;
import static com.ryanlothian.cryptic.Token.literal;
import static com.ryanlothian.cryptic.Token.placeholder;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

/**
 * Tests for {@link Rule}.
 */
public final class RuleTest {

    private static final Token X = placeholder("X");
    private static final Token Y = placeholder("Y");
    
    @Test
    public void fromStringShouldPassSimplestRule() {
        // A simple string parses successfully.
        assertEquals(
                new Rule(
                        tokens(literal("clubs")),
                        tokens(literal("c"))),
                Rule.fromString("clubs = c"));
    }

    @Test
    public void fromStringShouldPassSimpleRule() {
        // A simple string parses successfully.
        assertEquals(
                new Rule(
                        tokens(literal("big"), literal("cat")),
                        tokens(literal("lion"))),
                Rule.fromString("big cat = lion")); 
    }
    
    @Test
    public void fromStringShouldParsePlaceholders() {
        // A rule with placeholders parses successfully.
        assertEquals(
                new Rule(
                        tokens(X, literal("after"), Y),
                        tokens(Y, X)),
                Rule.fromString("X after Y = Y X")); 
    }
    
    @Test
    public void fromStringShouldParseFunctions() {
        // A rule with functions parses successfully.
        assertEquals(
                new Rule(
                        tokens(literal("broken"), X),
                        tokens(function("anagram", X))),
                Rule.fromString("broken X = anagram(X)")); 
    }
    
    @Test
    public void parsingsForShouldHandlePlaceholders() {
        Rule rule = Rule.fromString("X after Y = Y X");
        assertEquals(
                ImmutableSet.of(ImmutableMap.of(
                        X, "the cat",
                        Y, "the dog")),
                Rule.parsingsFor(rule.leftTokens, "the cat after the dog"));
    }
    
    private static ImmutableList<Token> tokens(Token... tokenArray) {
        return ImmutableList.copyOf(tokenArray);
    }
}
