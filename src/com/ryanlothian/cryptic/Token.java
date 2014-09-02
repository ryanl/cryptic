package com.ryanlothian.cryptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.ImmutableList;

@Immutable
public final class Token {
    public enum TokenType {
        LITERAL,
        PLACEHOLDER,
        FUNCTION
    }
    public enum FunctionType {
        ANAGRAM,
        CONCAT
    }
    
    public final Token.TokenType type;
    public final String literal;
    public final ImmutableList<Token> children;
    
    public static Token literal(String string) {
        return new Token(string, TokenType.LITERAL, Collections.<Token>emptyList());
    }
    
    public static Token placeholder(String name) {
        return new Token(name, TokenType.PLACEHOLDER, Collections.<Token>emptyList());
    }
    
    public static Token function(String functionName, List<Token> children) {
        return new Token(functionName, TokenType.FUNCTION, children);
    }
    
    public static Token function(String functionName, Token... children) {
        return new Token(functionName, TokenType.FUNCTION, Arrays.asList(children));
    }
    
    
    private Token(String literal, Token.TokenType type, List<Token> children) {
        checkNotNull(literal);
        checkNotNull(type);
        checkNotNull(children);
        
        if (literal.contains("(")) {
            throw new IllegalArgumentException("Literal should not contain (");
        }
        this.literal = literal;
        this.type = type;
        this.children = ImmutableList.copyOf(children);
    }
    
    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof Token)) return false;
        Token other = (Token) obj;
        return children.equals(other.children) &&
                literal.equals(other.literal) &&
                type.equals(other.type);
    }
    
    @Override
    public int hashCode() {
        return type.hashCode() * 31 * 31 + literal.hashCode() * 31 + children.hashCode();
    }
    
    @Override
    public String toString() {
        if (type == TokenType.LITERAL || type == TokenType.PLACEHOLDER) {
            return literal;
        } else {
            return literal + "(" + children + ")";
        }
    }
}