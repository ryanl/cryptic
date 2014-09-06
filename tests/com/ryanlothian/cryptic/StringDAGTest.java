package com.ryanlothian.cryptic;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import javax.annotation.concurrent.Immutable;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;

/**
 * Tests for {@link StringDAG}.
 */
public final class StringDAGTest {

    private static final StringDAG EMPTY = StringDAG.terminalNode();
    private static final StringDAG ABC = 
            new StringDAG(false, ImmutableMap.of("abc", EMPTY));
    
    @Immutable
    private static final class TestParams {
        public final StringDAG graph;
        public final String testString;
        public final boolean expectedResult;
        
        TestParams(StringDAG graph, String testString, boolean expectedResult) {
            this.graph = graph;
            this.testString = testString;
            this.expectedResult = expectedResult;
        }
    }
    
    private static final TestParams[] testCases = new TestParams[]{
        // The graph representing the string set {""} should admit "" but not "a".
        new TestParams(EMPTY, "", true),
        new TestParams(EMPTY, "a", false),

        // The graph representing the string set {"abc"} should admit "abc" not "a", "", "b" or "abcd".
        new TestParams(ABC, "abc", true),
        new TestParams(ABC, "ab", false),
        new TestParams(ABC, "b", false),
        new TestParams(ABC, "abcd", false)
        
        // TODO -- add some tests that use larger graphs
    };
    
    @Test
    public void isTerminalShouldBeTrueForTerminalNode() {
        assertTrue(EMPTY.isTerminal());
    }
    
    @Test
    public void isTerminalShouldBeFalseForNonTerminalNode() {
        assertFalse(ABC.isTerminal());
    }
        
    @Test
    public void testAdmits() {
        for (TestParams params : testCases) {
            assertEquals(params.expectedResult, params.graph.admits(params.testString));
        }
    }
}


