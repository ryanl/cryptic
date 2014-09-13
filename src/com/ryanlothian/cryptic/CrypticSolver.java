package com.ryanlothian.cryptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.concurrent.Immutable;

/**
 * Solver for cryptic crossword clues.
 */
@Immutable
public final class CrypticSolver {
    
    private final Thesaurus thesaurus;
    private final Grammar grammar;
    
    public CrypticSolver(Thesaurus thesaurus, Grammar grammar) {
        this.thesaurus = checkNotNull(thesaurus);
        this.grammar = checkNotNull(grammar);
    }
	
	/** Solves a cryptic crossword clue. */
	public Set<String> solve(String problem) {
	    Set<String> result = new HashSet<>();
		List<String> words = splitIntoWordsIgnoringPunctuation(problem);

		// Try each position for splitting the clue into two parts: definition | cryptic.
        // TODO: This assumes definition : cryptic, but sometimes we get
        // cryptic : definition.
		for (int splitPoint = 0; splitPoint < words.size(); splitPoint++) {

		    // The definition consists of all words before that point.
			List<String> definition = words.subList(0, splitPoint);
			
			// The cryptic parts consists of all the words after that point. 
			List<String> crypticPart = words.subList(splitPoint, words.size());
			
			// Find all solutions to the non-cryptic part (the definition).
			Set<String> solutionsForDefinition = solveNonCryptic(definition);
			
			// Find all solutions to the cryptic part.
			StringDAG solutionsForCryptic = solveCrypticHalf(crypticPart);
			
			// Solutions that match both.
			result.addAll(intersect(solutionsForDefinition, solutionsForCryptic));
		}
		return result;
	}
	
    /** Returns a set of possible solutions to a traditional non-cryptic crossword clue. */
    private Set<String> solveNonCryptic(List<String> definition) {
        // For now we just use the thesaurus.
        Set<String> solutions = new HashSet<>(thesaurus.getSynonyms(Util.join("", definition)));
        solutions.remove(definition);
        return solutions;
    }
    
    private static Set<String> intersect(Set<String> a, StringDAG b) {
        HashSet<String> result = new HashSet<>();
        for (String s : a) {
            if (b.admits(s)) {
                result.add(s);
            }
        }
        return result;
    }
    
    private static Set<String> filterByStringLength(int length, Set<String> strings) {
        Set<String> wordsOfRequestedLength = new HashSet<>();
        for (String s : strings) {
            if (s.length() == length) {
                wordsOfRequestedLength.add(s);
            }
        }
        return wordsOfRequestedLength;
    }
    
	/** Solve the cryptic half of a cryptic crossword clue. */   
    private StringSet solveCrypticHalf(List<String> clue) {
        return grammar.findAllSolutions(words);
    }
    
    /** Returns lower case words from the given string. Punctuation and non-alphabet characters are ignored. */ 
	private static List<String> splitIntoWordsIgnoringPunctuation(String line) {
        List<String> words = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        for (char c : (line + " ").toCharArray()) {
            if (c >= 'A' && c <= 'Z') {
                sb.append(c + 'a' - 'A');
            } else if (c >= 'a' && c <= 'z') {
                sb.append(c);
            } else {
                if (sb.length() > 0) {
                    words.add(sb.toString());
                    sb.setLength(0);
                }
            }
        }
        return words;
    }
    
}
