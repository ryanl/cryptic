package com.ryanlothian.cryptic;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class GrammarTest {

    private final Grammar grammar = new Grammar(
            "clear = manifest",
            "document = manifest",
            "club = c",
            "clear = transparent");
    
    @Test
    public void parseSingleLiteral() {
        assertEquals("manifest", grammar.solve("clear"));
    }

}
