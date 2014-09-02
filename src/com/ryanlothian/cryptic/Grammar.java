package com.ryanlothian.cryptic;

import java.util.Arrays;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

/** A set of rules. */
@Immutable
public final class Grammar {
    public final ImmutableList<Rule> rules;
    
    public Grammar(String... ruleStrings) {
        this(Arrays.asList(ruleStrings));
    }
    
    public Grammar(List<String> ruleStrings) {
        ImmutableList.Builder<Rule> rules = ImmutableList.builder();
        for (String ruleStr: ruleStrings) {
            rules.add(Rule.fromString(ruleStr));
        }
        this.rules = rules.build();
    }

}
