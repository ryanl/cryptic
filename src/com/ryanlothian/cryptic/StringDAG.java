package com.ryanlothian.cryptic;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;

/**
 * Represents a directed acyclic graph where edges are labelled with strings and each vertex is labelled with
 * a boolean indicating whether or not it is a terminal node. The DAG has a root element from which all other
 * vertices are reachable.
 * 
 * <p>For each vertex, no outgoing edge may have a label that is a prefix of another edge.
 * 
 * <p>This class is intended to serve as an efficient description for a set of strings. 
 */
@Immutable
public final class StringDAG {
    
    private static final ImmutableMultimap<String, StringDAG> EMPTY_MULTIMAP = 
            ImmutableMultimap.of();
    
    public final ImmutableMultimap<String, StringDAG> transitions;
    public final ImmutableMultimap<String, StringDAG> anagramTransitions; 
    public final boolean terminalNode;
    
    /** 
     * Returns a graph c such that c.admits(s) iff s.equals("").
     */
    public static StringDAG epsilon() { 
        return new StringDAG(true, EMPTY_MULTIMAP, EMPTY_MULTIMAP);
    }
    
    /** 
     * Returns a graph c such that c.admits(t) iff s.equals(t).
     */
    public static StringDAG oneString(String s) { 
        return new StringDAG(
                false,
                ImmutableMultimap.of(s, epsilon()),
                EMPTY_MULTIMAP);
    }
    
    /** 
     * Returns a graph for which {@link StringDAG#admits(String)} always returns false.
     */
    public static StringDAG emptySet() {
        return new StringDAG(false, EMPTY_MULTIMAP, EMPTY_MULTIMAP);
    }
    
    /**
     * Creates a StringDAG c such that c.admits(s) iff there exist strings s1, s2 such that
     * s = s1 + s2 and a.admits(s1) and b.admits(s2).
     */
    public static concat(StringDAG a, StringDAG b) {
        // TODO
    }
    
    public static prependAnagramEdge(String letters, StringDAG successor) {
        transitions = ImmutableMultimap.of();
        anagramTransitions = ImmutableMultimap.of(letters, successor);
    }
    
    public static union(StringDAG a, StringDAG b) {
        
        transitions = ImmutableMultimap.of();
        anagramTransitions = ImmutableMultimap.of(letters, successor);
    }
    
    public StringDAG(
            boolean terminalNode, 
            ImmutableMultimap<String, StringDAG> transitions,
            ImmutableMultimap<String, StringDAG> anagramTransitions) {
        this.terminalNode = terminalNode;
        this.transitions = ImmutableMultimap.copyOf((transitions);
        this.anagramTransitions = ImmutableMultimap.copyOf(anagramTransitions);
    }
    
    /** 
     * Returns true if there is a path from the root of the graph to some terminal vertex in the
     * graph such that the concatenation of the edges traversed another the way equals the given
     * string.
     */
    public boolean admits(String string) {
        return admits(string, 0);
    }
    
    private boolean admits(String string, int startPosInInput) {
        // TODO: Taking substrings may be be inefficient here. We should use our own
        // string implementation instead with an efficient substring method, or
        // pass a start index.
        if (string.length() <= startPosInInput) {
            return this.terminalNode;
        } else {
            for (Entry<String, StringDAG> entry : transitions.entrySet()) {
                String edge = entry.getKey();
                if (string.regionMatches(startPosInInput, edge, 0, edge.length())) {
                    return entry.getValue().admits(string, startPosInInput + edge.length());
                }
            }
            return false;
        }
    }
    
    public boolean isTerminal() {
        return this.terminalNode;
    }
}
