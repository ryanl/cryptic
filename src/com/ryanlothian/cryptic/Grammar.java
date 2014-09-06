package com.ryanlothian.cryptic;

import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

/** A set of rules. */
@Immutable
public final class Grammar {
    
    public final ImmutableList<Rule> rules;
    
    public Grammar(List<Rule> rules) {
        this.rules = ImmutableList.copyOf(rules);
    }
    
    public StringDAG findAllSolutions() {
        
    }
    
}
