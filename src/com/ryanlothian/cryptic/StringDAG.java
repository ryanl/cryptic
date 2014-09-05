package com.ryanlothian.cryptic;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

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
    private final ImmutableMap<String, StringDAG> transitions;
    private final boolean terminalNode;
    
    public static StringDAG terminalNode() { 
        return new StringDAG(true, Collections.<String, StringDAG>emptyMap());
    }
    
    private StringDAG(boolean terminalNode, Map<String, StringDAG> transitions) {
        this.terminalNode = terminalNode;
        this.transitions = ImmutableMap.copyOf(transitions);
        // TODO: Make certain that no transition is a prefix of another one
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
