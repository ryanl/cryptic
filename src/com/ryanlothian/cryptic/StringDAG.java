package com.ryanlothian.cryptic;

import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableMap;

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
    }
    
    public boolean admits(String string) {
        // TODO: Taking substrings may be be inefficient here. We should use our own
        // string implementation instead with an inefficient substring method, or
        // pass a start index.
        if (string.isEmpty()) {
            return this.terminalNode;
        } else {
            for (Entry<String, StringDAG> entry : transitions.entrySet()) {
                if (string.startsWith(entry.getKey())) {
                    return entry.getValue().admits(string.substring(entry.getKey().length()));
                }
            }
        }
    }
    
    public boolean isTerminal() {
        return this.terminalNode;
    }
}
