package com.ryanlothian.cryptic;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableSet;

/**
 * Solver for cryptic crossword clues.
 */
public final class CrypticSolver {
    
    private final Thesaurus thesaurus;
    private final Grammar grammar;
    
    public CrypticSolver(Thesaurus thesaurus, Grammar grammar) {
        this.thesaurus = checkNotNull(thesaurus);
        this.grammar = checkNotNull(grammar);
    }
	
    /** Returns a set of possible solutions to a traditional non-cryptic crossword clue. */
	private Set<String> solveNonCryptic(List<String> definition) {
	    return ImmutableSet.copyOf(thesaurus.getSynonyms(join("", definition)));
	}
	
	public void solve(String problem) {
		List<String> words = splitIntoWordsIgnoringPunctuation(problem);
		for (int splitPoint = 0; splitPoint < words.size(); splitPoint++) {
		    // TODO: This assumes definition : cryptic, but sometimes we get
		    // cryptic : definition.
			List<String> definition = words.subList(0, splitPoint);
			List<String> clue = words.subList(splitPoint, words.size());
			Set<String> solutionsForDefinition = solveNonCryptic(definition);
			// TODO: Use a d.a.g instead of a set to reduce time complexity
			Set<String> solutionsForCryptic = solveCryptic(definition);
		}
	}
	
	/** Solve the cryptic part of the clue. */   
    private Set<String> solveCryptic(List<String> clue) {
        for (String word : clue) {
            this.thesaurus.getSynonyms(word);
        }
        // TODO: Implement this
        // for (Rule rule : )
        return ImmutableSet.of();
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

	/** Equivalent to separator.join(parts) in Python. */ 
    public static String join(String separator, List<String> parts) {
        StringBuilder sb = new StringBuilder();
        boolean first = true;
        for (String s : parts) {
            if (!first) {
                sb.append(separator);
            }
            first = false;
            sb.append(s);
        }
        return sb.toString();
    }
    
    private static final class ScoredString {
        public final float score;
        public final String string;
        
        public ScoredString(float score, String string) {
            this.score = score;
            this.string = string;
        }
    }

    
}
