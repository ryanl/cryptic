package com.ryanlothian.cryptic;

import static com.ryanlothian.cryptic.Token.TokenType.FUNCTION;
import static com.ryanlothian.cryptic.Token.TokenType.LITERAL;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

public final class Rule {
    public final List<Token> leftTokens;
    public final List<Token> rightTokens;
    
    Rule(
            List<Token> leftTokens,
            List<Token> rightTokens) {
        for (Token token : leftTokens) {
            if (token.type == FUNCTION) {
                throw new IllegalArgumentException("Function on lhs");
            }
        }
        this.leftTokens = ImmutableList.copyOf(leftTokens);
        this.rightTokens = ImmutableList.copyOf(rightTokens);
    }
    
    public static Rule fromString(String rule) {
        boolean left = true;
        List<Token> leftTokens = new ArrayList<>();
        List<Token> rightTokens = new ArrayList<>();
        List<Token> tokens = leftTokens;
        String currentToken = "";
        String tweakedRule = rule.replaceAll("=", " = ") + " ";
        for (int i = 0; i < tweakedRule.length(); i++) {
            char c = tweakedRule.charAt(i);
            if (c >= 'A' && c <= 'Z') {
                tokens.add(Token.placeholder(String.valueOf(c)));
            } else if (c == '=') {
                if (!left) {
                    throw new IllegalArgumentException("Too many equals");
                }
                tokens = rightTokens;
                left = false;
            } else if (c == ' ') {
                if (currentToken != "") {
                    tokens.add(Token.literal(currentToken));
                    currentToken = "";
                }
            } else if (c == '(') {
                i++;
                ImmutableList.Builder<Token> children = ImmutableList.builder();
                while (tweakedRule.charAt(i) != ')') {
                    char d = tweakedRule.charAt(i);
                    if (d != ',') {
                        children.add(Token.placeholder(String.valueOf(d)));
                    }
                    i++;
                }
                tokens.add(Token.function(currentToken, children.build()));
                currentToken = "";
            } else {
                currentToken += c;
            }
        }
        return new Rule(leftTokens, rightTokens);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Rule)) return false;
        Rule other = (Rule) obj;
        return leftTokens.equals(other.leftTokens) &&
                rightTokens.equals(other.rightTokens);
    }
    
    @Override
    public int hashCode() {
        return leftTokens.hashCode() * 31 + rightTokens.hashCode();
    }
    
    @Override
    public String toString() {
        return "Rule(" + this.leftTokens + ", " + this.rightTokens + ")";
    }
    
    /**
     * Returns a set of mappings from placeholder -> value consumed by that placeholder.
     */
    public static Set<Map<Token, String>> parsingsFor(List<Token> lhsTokens, String clue) {
        // TODO: This might not be the best way to do things
        clue = clue.trim();
        
        // Optimization: all literals must be present. If not, you can bail out early.
        for (Token t : lhsTokens) {
            if (t.type == LITERAL) {
                if (!clue.contains(t.literal)) {
                    return ImmutableSet.of();
                }
            }
        }
        
        if (lhsTokens.isEmpty() && clue.equals("")) {
            return ImmutableSet.<Map<Token, String>>of(ImmutableMap.<Token, String>of());
        } else if (lhsTokens.isEmpty()) {
            return ImmutableSet.of();
        }
        
        List<Token> remainingTokens = lhsTokens.subList(1, lhsTokens.size());
        Token firstToken = lhsTokens.get(0);
        if (firstToken.type == LITERAL) {
            if (clue.startsWith(firstToken.literal)) {
                String remainder = clue.substring(firstToken.literal.length());
                return parsingsFor(
                        remainingTokens,
                        remainder);
            } else {
                return ImmutableSet.of();
            }
        } else {
            // Token is a placeholder. Return each valid parsing.
            ImmutableSet.Builder<Map<Token, String>> parsings = ImmutableSet.builder();    
            for (int i = 0; i < clue.length(); i++) {
                if (i == clue.length() - 1 || clue.charAt(i + 1) == ' ') {
                    String placeHolderValue = clue.substring(0, i + 1);
                    String remainder = clue.substring(i + 1);
                    for (Map<Token, String> parsing : parsingsFor(remainingTokens, remainder)) {
                        parsings.add(ImmutableMap.<Token, String>builder()
                            .putAll(parsing)
                            .put(firstToken, placeHolderValue)
                            .build());
                    }
                }
            }
            return parsings.build();
        }
    }
}