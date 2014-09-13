package com.ryanlothian.cryptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.annotation.concurrent.Immutable;

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

    public StringSet findAllSolutions(List<String> words) {
        StringSet[] solutionsStartingAt = new StringSet[words.size() + 1];
        solutionsStartingAt[words.size()] = StringSet.oneString("");

        // Work backwards through the list of words.
        // TODO: change this to work forward
        for (int i = words.size() - 1; i >= 0; i--) {
            List<StringSet> resultsForAllJ = Lists.newArrayList();
            
            for (int j = i + 1; j <= words.size(); j++) {
                List<String> sublist = words.subList(i, j);
                String joinedList = Util.join(" ", sublist);
                
                List<StringSet> results = Lists.newArrayList();

                // TODO: The thesaurus will also return the word itself. Should
                // we disallow that? Maybe there should be a boolean parameter to
                // this method that says whether or not to allow it. We probably want
                // to allow when we have recursed but not at top level.
                for (String synonym : this.thesaurus.getSynonyms(joinedList)) {
                    results.add(StringSet.oneString(synonym.replace(" ", "")));
                }

                for (Rule rule : this.rules) {
                    results.addAll(ruleAppliedToWords(rule, sublist, joinedList));
                }
                resultsForAllJ.add(StringSet.concat(
                        StringSet.union(results),
                        solutionsStartingAt[j]));
            }
            solutionsStartingAt[i] = StringSet.union(resultsForAllJ);
        }

        return solutionsStartingAt[0];
    }

    // Result is really the union of the retval but we optimize by not doing the union yet.
    private List<StringSet> ruleAppliedToWords(Rule rule, List<String> sublist, String joinedList) {
        // We find all ways of parsing the given rule.
        // e.g. if the clue is "dog after cat after mouse" and the rule is
        // "X after Y = X Y", then there are two parsings:
        // X = dog after cat, Y = mouse
        // X = dog, Y = cat after mouse
        
        List<StringSet> results = Lists.newArrayList();
        // For each parsing...
        for (Map<Token, String> parsing : Rule.parsingsFor(rule.leftTokens, joinedList)) {
            
            // Recursively solve the token values (because the solution to a clue can
            // require the nested rules/expressions).
            // For each token we end up with all possible meanings of that token.
            Map<Token, StringSet> solutionsForToken = new HashMap<>();
            for (Entry<Token, String> tokenValue : parsing.entrySet()) {
                solutionsForToken.put(
                        tokenValue.getKey(),
                        findAllSolutions(
                                Arrays.asList(tokenValue.getValue().split(" "))));
            }
            
            // We have meanings for tokens. Now we need to order them correctly,
            // Apply functions, and add any literals that are on the r.h.s. of the rules.
         
            StringSet strings = StringSet.epsilon();
            for (Token token : rule.rightTokens) {
                StringSet stringsForToken;
                if (token.type == TokenType.LITERAL) {
                    stringsForToken = StringSet.oneString(token.literal);
                } else if (token.type == TokenType.FUNCTION) {
                    // TODO: Support functions
                    stringsForToken = StringSet.emptySet();
                } else if (token.type == TokenType.PLACEHOLDER) {
                    stringsForToken = solutionsForToken.get(token);
                } else {
                    throw new IllegalArgumentException("Unknown token type");
                }
                strings = StringSet.concat(strings, stringsForToken);
            }
            results.add(strings);
        }
        return results;
    }
}
