package com.ryanlothian.cryptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.concurrent.Immutable;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.ryanlothian.cryptic.Token.TokenType;

/** A set of rules. */
@Immutable
public final class Grammar {

    public final ImmutableList<Rule> rules;
    public final Thesaurus thesaurus;

    public Grammar(List<Rule> rules, Thesaurus thesaurus) {
        this.rules = ImmutableList.copyOf(rules);
        this.thesaurus = checkNotNull(thesaurus);
    }

    public StringDAG findAllSolutions(List<String> words) {
        Set<String> solutions = new HashSet<>();

        StringDAG[] graphForStartingAt = new StringDAG[words.size() + 1];
        graphForStartingAt[words.size()] = StringDAG.acceptingOnlyEpsilon();

        // Work backwards through the list of words.
        for (int i = words.size() - 1; i >= 0; i--) {
            for (int j = i + 1; j <= words.size(); j++) {
                List<String> sublist = words.subList(i, j);
                StringDAG trailingDAG = graphForStartingAt[j];
                String joinedList = Util.join(" ", sublist);

                // This sublist must match a thesaurus entry or a rule exactly.

                Map<String, StringDAG> edges = new HashMap<String, StringDAG>();

                // TODO: The thesaurus will also return the word itself. Should
                // we
                // disallow that? Maybe there should be a boolean parameter to
                // this
                // method that says whether or not to allow it. We probably want
                // to
                // allow when we have recursed but not at top level.
                for (String synonym : this.thesaurus.getSynonyms(joinedList)) {
                    edges.put(synonym.replace(" ", ""), trailingDAG);
                }

                for (Rule rule : this.rules) {
                    StringDAG newDAG = getStringDAGForRuleAppliedToWords(rule,
                            sublist, trailingDAG, joinedList);
                }
            }
        }

        // TODO
        return null;
    }

    private StringDAG getStringDAGForRuleAppliedToWords(
            Rule rule,
            List<String> sublist,
            StringDAG trailingDAG,
            String joinedList) {
        
        // Find all ways of parsing the given rule.
        // e.g. if the clue is "dog after cat after mouse" and the rule is
        // "X after Y = X Y", then there are two parsings:
        // X = dog after cat, Y = mouse
        // X = dog, Y = cat after mouse
        
        Set<Map<Token, String>> parsings = 
                 Rule.parsingsFor(rule.leftTokens, joinedList);
        
        // For each parsing...
        for (Map<Token, String> parsing : parsings) {
            
            // Recursively solve the token values (because the solution to a clue can
            // require the nested rules/expressions).
            // For each token we end up with all possible meanings of that token.
            Map<Token, StringDAG> solutionsForToken = new HashMap<>();
            for (Entry<Token, String> tokenValue : parsing.entrySet()) {
                solutionsForToken.put(
                        tokenValue.getKey(),
                        findAllSolutions(
                                Arrays.asList(tokenValue.getValue().split(" "))));
            }
            
            // Build up the appropriate DAG.
            StringDAG currentDAG = trailingDAG;
            List<StringDAG> dagsToJoin = new ArrayList<>();
            for (Token token : Lists.reverse(rule.rightTokens)) {
                if (token.type == TokenType.LITERAL) {
                    dagsToJoin.add(StringDAG.oneString(token.literal));
                } else if (token.type == TokenType.FUNCTION) {
                    // TODO: Support functions
                    dagsToJoin.add(StringDAG.epsilon());
                } else if (token.type == TokenType.PLACEHOLDER) {
                    dagsToJoin.add(solutionsForToken.get(token));
                }
            }
        }
    }
}
